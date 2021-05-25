package com.tcl.dias.preparefulfillment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.primitives.Doubles;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.servicefulfillment.beans.ScContractInfoBean;
import com.tcl.dias.common.servicefulfillment.beans.ScOrderBean;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.preparefulfillment.beans.ActivityPlanBean;
import com.tcl.dias.preparefulfillment.beans.OrderDashboardResponse;
import com.tcl.dias.preparefulfillment.beans.OrderSolutionViewBean;
import com.tcl.dias.preparefulfillment.beans.PagedResult;
import com.tcl.dias.preparefulfillment.beans.ProcessPlanBean;
import com.tcl.dias.preparefulfillment.beans.ServiceDashBoardBean;
import com.tcl.dias.preparefulfillment.beans.ServiceRequest;
import com.tcl.dias.preparefulfillment.beans.ServiceSolutionViewBean;
import com.tcl.dias.preparefulfillment.beans.SolutionViewDetailsBean;
import com.tcl.dias.preparefulfillment.beans.StagePlanBean;
import com.tcl.dias.preparefulfillment.beans.TaskPlanBean;
import com.tcl.dias.preparefulfillment.specification.ServiceDetailSpecification;
import com.tcl.dias.servicefulfillment.entity.entities.*;
import com.tcl.dias.servicefulfillment.entity.repository.*;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.AttributesBean;
import com.tcl.dias.servicefulfillmentutils.beans.CGWDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.ComponentAttributes;
import com.tcl.dias.servicefulfillmentutils.beans.ComponentAttributesBean;
import com.tcl.dias.servicefulfillmentutils.beans.DashBoardCount;
import com.tcl.dias.servicefulfillmentutils.beans.DashboardAttributesBean;
import com.tcl.dias.servicefulfillmentutils.beans.OverlayBean;
import com.tcl.dias.servicefulfillmentutils.beans.ProcessTaskLogBean;
import com.tcl.dias.servicefulfillmentutils.beans.ScChargeLineItemBean;
import com.tcl.dias.servicefulfillmentutils.beans.ScOrderAttributesBean;
import com.tcl.dias.servicefulfillmentutils.beans.ScServiceDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.ServiceAttributesBean;
import com.tcl.dias.servicefulfillmentutils.beans.SolutionBean;
import com.tcl.dias.servicefulfillmentutils.beans.StageCountBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskAdminBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskBean;
import com.tcl.dias.servicefulfillmentutils.beans.UnderlayBean;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.mapper.ServiceFulfillmentMapper;
import com.tcl.dias.servicefulfillmentutils.mapper.ServiceInventoryMapper;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CREATED_DATE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_TYPE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.PRODUCT_NAME;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SC_ORDER_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * This file contains the ServiceDashboardService.java class.
 *
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ServiceDashboardService extends ServiceFulfillmentBaseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDashboardService.class);

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScOrderRepository scOrderRepository;

	@Autowired
	StagePlanRepository stagePlanRepository;

	@Autowired
	ProcessPlanRepository processPlanRepository;

	@Autowired
	ActivityPlanRepository activityPlanRepository;

	@Autowired
	ProcessTaskLogRepository processTaskLogRepository;

	@Autowired
	MstStageDefRepository mstStageDefRepository;

	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;

	@Autowired
	ScContractInfoRepository scContractInfoRepository;

	@Autowired
	TaskPlanRepository taskPlanRepository;

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;

	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;

	@Autowired
	ScAttachmentRepository scAttachmentRepository;

	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;

	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;

	@Autowired
	StageRepository stageRepository;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	ScServiceSlaRepository scServiceSlaRepository;

	@Autowired
	MstStatusRepository mstStatusRepository;

	@Autowired
	ScSolutionComponentRepository scSolutionComponentRepository;

	@Autowired
	ScComponentRepository scComponentRepository;

	@Autowired
	CancellationRequestRepository cancellationRequestRepository;

	@Autowired
	CpeDeviceNameDetailRepository cpeDeviceNameDetailRepository;

	@Autowired
	ScChargeLineitemRepository scChargeLineitemRepository;

	public OrderDashboardResponse getOrderDashboardDetails(String orderCode) {
		OrderDashboardResponse orderDashboardResponse = new OrderDashboardResponse();
		LOGGER.info("Input scOrderId {}", orderCode);
		ScOrder scOrderEntity = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
		if (scOrderEntity != null) {
			orderDashboardResponse.setOrderCode(scOrderEntity.getOpOrderCode());
			orderDashboardResponse.setOrderDate(scOrderEntity.getCreatedDate());
			orderDashboardResponse.setErfOrderId(scOrderEntity.getErfOrderId());
			orderDashboardResponse.setErfOrderLeId(scOrderEntity.getErfOrderLeId());
			orderDashboardResponse.setOrderType(scOrderEntity.getOrderType());
			orderDashboardResponse.setOrderCategory(scOrderEntity.getOrderCategory());
			List<ScServiceDetail> scServiceEntitys = scServiceDetailRepository.findByScOrderId(scOrderEntity.getId());
			if (!scServiceEntitys.isEmpty()) {

				List<ServiceDashBoardBean> serviceDetailBeans = new ArrayList<>();
				orderDashboardResponse.setServiceDetails(serviceDetailBeans);
				orderDashboardResponse.setScOrderId(scOrderEntity.getId());
				scServiceEntitys.stream().forEach(scServiceDetail -> {
					Map<String, String> componentAMap = commonFulfillmentUtils.getComponentAttributesDetails(
							Arrays.asList("siteAddress"), scServiceDetail.getId(), "LM", "A");
					Map<String, String> componentBMap = commonFulfillmentUtils.getComponentAttributesDetails(
							Arrays.asList("siteAddress"), scServiceDetail.getId(), "LM", "B");
					ServiceDashBoardBean serviceDashBoardBean = new ServiceDashBoardBean();
					serviceDashBoardBean.setTargetedDeliveryDate(scServiceDetail.getTargetedDeliveryDate());
					serviceDashBoardBean.setCommittedDeliveryDate(scServiceDetail.getCommittedDeliveryDate());
					serviceDashBoardBean.setRrfsDate(scServiceDetail.getRrfsDate());
					serviceDashBoardBean.setCrfsDate(scServiceDetail.getServiceCommissionedDate());
					serviceDashBoardBean.setEstimatedDeliveryDate(scServiceDetail.getEstimatedDeliveryDate());
					serviceDashBoardBean.setActualDeliveryDate(scServiceDetail.getActualDeliveryDate());
					serviceDashBoardBean.setPrimarySecondary(scServiceDetail.getPrimarySecondary());
					serviceDashBoardBean.setProductName(scServiceDetail.getErfPrdCatalogProductName());
					serviceDashBoardBean.setAssignedPM(scServiceDetail.getAssignedPM());
					if (scServiceDetail.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.INPROGRESS)
							&& scServiceDetail.getTargetedDeliveryDate() != null
							&& scServiceDetail.getCommittedDeliveryDate() != null
							&& (scServiceDetail.getTargetedDeliveryDate().toLocalDateTime()
									.isAfter(scServiceDetail.getCommittedDeliveryDate().toLocalDateTime()))) {
						serviceDashBoardBean.setCustomerServiceDelayed(true);
					}
					serviceDashBoardBean.setLocation(componentAMap.get("siteAddress"));
					serviceDashBoardBean.setLocationB(componentBMap.get("siteAddress"));
					orderDashboardResponse.setOrderType(scServiceDetail.getOrderType());
					orderDashboardResponse.setOrderCategory(scServiceDetail.getOrderCategory());

					orderDashboardResponse.setCrfsDate(scServiceDetail.getServiceCommissionedDate());
					orderDashboardResponse.setOrderAge(ChronoUnit.DAYS
							.between(scOrderEntity.getCreatedDate().toLocalDateTime(), LocalDateTime.now()));

					serviceDashBoardBean.setLocationId(StringUtils.isNotBlank(scServiceDetail.getErfLocSiteAddressId())
							? Integer.valueOf(scServiceDetail.getErfLocSiteAddressId())
							: null);
					serviceDashBoardBean.setServiceCode(scServiceDetail.getUuid());
					serviceDashBoardBean.setServiceId(scServiceDetail.getId());
					List<StagePlan> stagePlans = stagePlanRepository.findByServiceId(scServiceDetail.getId());
					List<StagePlanBean> stagePlanBeans = new ArrayList<>();
					serviceDashBoardBean.setStages(stagePlanBeans);
					String isDeemedAcceptance[] = { "" };
					if ("IPC".equals(serviceDashBoardBean.getProductName())
							|| "IZO Private Cloud".equals(serviceDashBoardBean.getProductName())) {
						scServiceDetail.getScServiceAttributes().stream()
								.filter(sca -> "isDeemedAcceptance".equals(sca.getAttributeName())).forEach(sca -> {
									isDeemedAcceptance[0] = sca.getAttributeValue();
								});
					}
					stagePlans.forEach(stagePlan -> {
						processStagePlans(scOrderEntity.getId(), serviceDashBoardBean, stagePlanBeans, stagePlan,
								isDeemedAcceptance[0]);

					});
					serviceDetailBeans.add(serviceDashBoardBean);

				});

			}

		} else {
			LOGGER.warn("ServiceDetils for order code  {} is not present", orderCode);
		}

		return orderDashboardResponse;
	}

	public OrderDashboardResponse getOrderDashboardDetailsIpc(String orderCode) {
		OrderDashboardResponse orderDashboardResponse = new OrderDashboardResponse();
		LOGGER.info("Input scOrderId {}", orderCode);
		ScOrder scOrderEntity = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
		if (scOrderEntity != null) {
			orderDashboardResponse.setOrderCode(scOrderEntity.getOpOrderCode());
			orderDashboardResponse.setOrderDate(scOrderEntity.getCreatedDate());
			orderDashboardResponse.setErfOrderId(scOrderEntity.getErfOrderId());
			orderDashboardResponse.setErfOrderLeId(scOrderEntity.getErfOrderLeId());

			List<ScServiceDetail> scServiceEntitys = scServiceDetailRepository.findByScOrderId(scOrderEntity.getId());
			if (!scServiceEntitys.isEmpty()) {

				List<ServiceDashBoardBean> serviceDetailBeans = new ArrayList<>();
				orderDashboardResponse.setServiceDetails(serviceDetailBeans);
				orderDashboardResponse.setScOrderId(scOrderEntity.getId());
				scServiceEntitys.stream().forEach(scServiceDetail -> {
					ServiceDashBoardBean serviceDashBoardBean = new ServiceDashBoardBean();
					serviceDashBoardBean.setTargetedDeliveryDate(scServiceDetail.getTargetedDeliveryDate());
					serviceDashBoardBean.setCommittedDeliveryDate(scServiceDetail.getCommittedDeliveryDate());
					serviceDashBoardBean.setEstimatedDeliveryDate(scServiceDetail.getEstimatedDeliveryDate());
					serviceDashBoardBean.setActualDeliveryDate(scServiceDetail.getActualDeliveryDate());
					serviceDashBoardBean.setPrimarySecondary(scServiceDetail.getPrimarySecondary());
					serviceDashBoardBean.setProductName(scServiceDetail.getErfPrdCatalogProductName());
					serviceDashBoardBean.setAssignedPM(scServiceDetail.getAssignedPM());
					serviceDashBoardBean.setLocation(scServiceDetail.getSiteAddress());
					orderDashboardResponse.setCrfsDate(scServiceDetail.getServiceCommissionedDate());
					orderDashboardResponse.setOrderAge(ChronoUnit.DAYS
							.between(scOrderEntity.getCreatedDate().toLocalDateTime(), LocalDateTime.now()));

					serviceDashBoardBean.setLocationId(StringUtils.isNotBlank(scServiceDetail.getErfLocSiteAddressId())
							? Integer.valueOf(scServiceDetail.getErfLocSiteAddressId())
							: null);
					serviceDashBoardBean.setServiceCode(scServiceDetail.getUuid());
					serviceDashBoardBean.setServiceId(scServiceDetail.getId());
					List<StagePlan> stagePlans = stagePlanRepository.findByServiceId(scServiceDetail.getId());
					List<StagePlanBean> stagePlanBeans = new ArrayList<>();
					serviceDashBoardBean.setStages(stagePlanBeans);
					String isDeemedAcceptance[] = { "" };
					if ("IPC".equals(serviceDashBoardBean.getProductName())
							|| "IZO Private Cloud".equals(serviceDashBoardBean.getProductName())) {
						scServiceDetail.getScServiceAttributes().stream()
								.filter(sca -> "isDeemedAcceptance".equals(sca.getAttributeName())).forEach(sca -> {
									isDeemedAcceptance[0] = sca.getAttributeValue();
								});
					}
					stagePlans.forEach(stagePlan -> {
						processStagePlans(scOrderEntity.getId(), serviceDashBoardBean, stagePlanBeans, stagePlan,
								isDeemedAcceptance[0]);

					});
					serviceDetailBeans.add(serviceDashBoardBean);

				});

			}

		} else {
			LOGGER.warn("ServiceDetils for order code  {} is not present", orderCode);
		}

		return orderDashboardResponse;
	}

	/**
	 * getServiceDashboardDetails
	 *
	 * @param serviceId
	 * @param scOrderId
	 * @return
	 */
	@Transactional
	public ServiceDashBoardBean getServiceDashboardDetails(Integer serviceId, String scOrderCode) {
		ServiceDashBoardBean serviceDashBoardBean = new ServiceDashBoardBean();
		LOGGER.info("Input serviceId {}", serviceId);
		ScServiceDetail scServiceEntity = scServiceDetailRepository.findByScOrderUuidAndId(scOrderCode, serviceId);
		try {
			if (scServiceEntity != null) {

				ScOrder scOrderEntity = scServiceEntity.getScOrder();
				if (scOrderEntity != null) {
					ScServiceDetail scServiceDetail = scServiceEntity;

					Map<String, String> componentAMap = commonFulfillmentUtils.getComponentAttributesDetails(
							Arrays.asList("siteAddress", "terminationDate", "customerMailReceiveDate"),
							scServiceDetail.getId(), "LM", "A");
					Map<String, String> componentBMap = commonFulfillmentUtils.getComponentAttributesDetails(
							Arrays.asList("siteAddress", "terminationDate"), scServiceDetail.getId(), "LM", "B");

					serviceDashBoardBean.setOrderCode(scOrderEntity.getOpOrderCode());
					serviceDashBoardBean.setOrderDate(scOrderEntity.getCreatedDate());
					serviceDashBoardBean.setLocation(scServiceDetail.getSiteAddress());
					serviceDashBoardBean.setLocationId(StringUtils.isNotBlank(scServiceDetail.getErfLocSiteAddressId())
							? Integer.valueOf(scServiceDetail.getErfLocSiteAddressId())
							: null);
					serviceDashBoardBean.setLocation(componentAMap.get("siteAddress"));
					serviceDashBoardBean.setLocationB(componentBMap.get("siteAddress"));
					serviceDashBoardBean.setSupplierBillStopDate(componentAMap.get("terminationDate"));
					serviceDashBoardBean.setCustomerMailReceiveDate(componentAMap.get("customerMailReceiveDate"));
					serviceDashBoardBean.setSupplierBillStopDateB(componentBMap.get("terminationDate"));
					serviceDashBoardBean.setServiceCode(scServiceDetail.getUuid());
					serviceDashBoardBean
							.setOrderType(scOrderEntity.getOrderType() != null ? scOrderEntity.getOrderType()
									: scServiceEntity.getOrderType());
					serviceDashBoardBean.setProductName(scServiceDetail.getErfPrdCatalogProductName());
					serviceDashBoardBean.setAssignedPM(scServiceDetail.getAssignedPM());
					serviceDashBoardBean.setTargetedDeliveryDate(scServiceDetail.getTargetedDeliveryDate());
					serviceDashBoardBean.setCommittedDeliveryDate(scServiceDetail.getCommittedDeliveryDate());
					serviceDashBoardBean.setRrfsDate(scServiceDetail.getRrfsDate());
					serviceDashBoardBean.setEstimatedDeliveryDate(scServiceDetail.getEstimatedDeliveryDate());
					serviceDashBoardBean.setActualDeliveryDate(scServiceDetail.getActualDeliveryDate());
					serviceDashBoardBean.setServiceId(scServiceDetail.getId());
					if (Objects.nonNull(scServiceDetail.getServiceCommissionedDate()))
						serviceDashBoardBean.setCrfsDate(scServiceDetail.getServiceCommissionedDate());
					// setting termination details
					serviceDashBoardBean.setTerminationFlowTriggered(scServiceDetail.getTerminationFlowTriggered());
					serviceDashBoardBean.setTerminationTrigerredDate(scServiceDetail.getTerminationInitiationDate());
					serviceDashBoardBean.setCustomerRequestorDate(scServiceDetail.getCustomerRequestorDate());
					serviceDashBoardBean.setTerminationEffectiveDate(scServiceDetail.getTerminationEffectiveDate());
					// setting cancellation details
					// serviceDashBoardBean.setCancellationFlowTriggered(scServiceDetail.getCancellationFlowTriggered());
					// serviceDashBoardBean.setCancellationInitiationDate(scServiceDetail.getCancellationInitiationDate());

					if (scServiceDetail.getIsAmended() != null
							&& CommonConstants.Y.equalsIgnoreCase(scServiceDetail.getIsAmended())) {
						serviceDashBoardBean.setIsOrderAmendent(scServiceDetail.getIsAmended());
					}

					List<StagePlan> stagePlans = stagePlanRepository.findByServiceId(scServiceDetail.getId());
					List<StagePlanBean> stagePlanBeans = new ArrayList<>();
					serviceDashBoardBean.setStages(stagePlanBeans);
					String isDeemedAcceptance[] = { "" };
					if ("IPC".equals(serviceDashBoardBean.getProductName())
							|| "IZO Private Cloud".equals(serviceDashBoardBean.getProductName())) {
						scServiceDetail.getScServiceAttributes().stream()
								.filter(sca -> "isDeemedAcceptance".equals(sca.getAttributeName())).forEach(sca -> {
									isDeemedAcceptance[0] = sca.getAttributeValue();
								});
					}
					for (StagePlan stagePlan : stagePlans) {
						processStagePlans(scOrderEntity.getId(), serviceDashBoardBean, stagePlanBeans, stagePlan,
								isDeemedAcceptance[0]);
					}
				}

			} else {
				LOGGER.warn("ServiceDetils for service Id  {} is not present", serviceId);
			}
		} catch (Exception ee) {
			LOGGER.error("Exception", ee);

		}

		return serviceDashBoardBean;
	}

	@Transactional
	public ServiceDashBoardBean getServiceDashboardDetailsIpc(Integer serviceId, String scOrderCode) {
		ServiceDashBoardBean serviceDashBoardBean = new ServiceDashBoardBean();
		LOGGER.info("Input serviceId {}", serviceId);
		Optional<ScServiceDetail> scServiceEntity = scServiceDetailRepository.findById(serviceId);
		if (scServiceEntity.isPresent()) {

			ScOrder scOrderEntity = scServiceEntity.get().getScOrder();
			if (scOrderEntity != null) {
				ScServiceDetail scServiceDetail = scServiceEntity.get();

				serviceDashBoardBean.setOrderCode(scOrderEntity.getOpOrderCode());
				serviceDashBoardBean.setOrderDate(scOrderEntity.getCreatedDate());
				serviceDashBoardBean.setLocation(scServiceDetail.getSiteAddress());
				serviceDashBoardBean.setLocationId(StringUtils.isNotBlank(scServiceDetail.getErfLocSiteAddressId())
						? Integer.valueOf(scServiceDetail.getErfLocSiteAddressId())
						: null);
				serviceDashBoardBean.setLocation(scServiceDetail.getSiteAddress());
				serviceDashBoardBean.setServiceCode(scServiceDetail.getUuid());
				serviceDashBoardBean.setProductName(scServiceDetail.getErfPrdCatalogProductName());
				serviceDashBoardBean.setAssignedPM(scServiceDetail.getAssignedPM());
				serviceDashBoardBean.setTargetedDeliveryDate(scServiceDetail.getTargetedDeliveryDate());
				serviceDashBoardBean.setCommittedDeliveryDate(scServiceDetail.getCommittedDeliveryDate());
				serviceDashBoardBean.setEstimatedDeliveryDate(scServiceDetail.getEstimatedDeliveryDate());
				serviceDashBoardBean.setActualDeliveryDate(scServiceDetail.getActualDeliveryDate());
				serviceDashBoardBean.setServiceId(scServiceDetail.getId());
				serviceDashBoardBean.setIsJeopardyTask(scServiceDetail.getIsJeopardyTask());
				serviceDashBoardBean.setServiceCommissionedDate(scServiceDetail.getServiceCommissionedDate());
				List<StagePlan> stagePlans = stagePlanRepository.findByServiceId(scServiceDetail.getId());
				List<StagePlanBean> stagePlanBeans = new ArrayList<>();
				serviceDashBoardBean.setStages(stagePlanBeans);
				String isDeemedAcceptance[] = { "" };
				if ("IPC".equals(serviceDashBoardBean.getProductName())
						|| "IZO Private Cloud".equals(serviceDashBoardBean.getProductName())) {
					scServiceDetail.getScServiceAttributes().stream()
							.filter(sca -> "isDeemedAcceptance".equals(sca.getAttributeName())).forEach(sca -> {
								isDeemedAcceptance[0] = sca.getAttributeValue();
							});
				}
				for (StagePlan stagePlan : stagePlans) {
					processStagePlans(scOrderEntity.getId(), serviceDashBoardBean, stagePlanBeans, stagePlan,
							isDeemedAcceptance[0]);
				}
			}

		} else {
			LOGGER.warn("ServiceDetils for service Id  {} is not present", serviceId);
		}

		return serviceDashBoardBean;
	}

	/**
	 * getStageCount
	 *
	 * @param scOrderId
	 * @return
	 */
	public List<Map<String, String>> getStageCount(String scOrderCode) {
		LOGGER.info("Input scOrderCode {}", scOrderCode);
		List<Map<String, String>> stageCount = stagePlanRepository.findStageCountByOrderCode(scOrderCode);
		LOGGER.info("List of Stage Count {}", stageCount);
		return stageCount;
	}

	/**
	 * getStageCount
	 *
	 * @param scOrderId
	 * @return
	 */
	public StageCountBean getInprogressStageCount() {
		StageCountBean stageCountBean = new StageCountBean();
		List<Map<String, String>> stageCount = stagePlanRepository.findInProgressStageCount();
		LOGGER.info("List of Stage Count {}", stageCount);
		Map<String, String> orderCount = stagePlanRepository.findActiveOrderCount();
		LOGGER.info("Active Order Count {}", orderCount);
		stageCountBean.setStageCount(stageCount);
		stageCountBean.setOrderCount(orderCount);
		return stageCountBean;
	}

	@Transactional(readOnly = true)
	public DashBoardCount getAllCounts() {

		DashBoardCount dashBoardCount = new DashBoardCount();
		// List<Map<String, String>> stageCount =
		// stagePlanRepository.findInProgressStageCount();
		List<Map<String, String>> processCount = processPlanRepository.findInProgressProcessCount();

		LOGGER.info("List of processCount {}", processCount);

		Map<String, String> orderCount = stagePlanRepository.findActiveOrderCount();
		LOGGER.info("Active Order Count {}", orderCount);

		List<Map<String, String>> productWiseCount = scServiceDetailRepository.groupByProductAndType();

		LOGGER.info("Active productWiseCount Count {}", productWiseCount);

		List<Map<String, String>> lastMileCount = scServiceDetailRepository.groupbytLMScenarioTypeCount();

		List<Map<String, String>> activeServiceRecordCount = scServiceDetailRepository.groupbyActiveServiceRecords();

		Map<String, String> activeRecordsCount = scServiceDetailRepository.getAllActiveRecords();

		Integer billedCircuits = scServiceDetailRepository.findTotalBilledCircuits();
		dashBoardCount.setTotalDeliveredCircuit(scServiceDetailRepository.findTotalDeliveredCircuits());

		dashBoardCount.setActiveRecordsCount(activeRecordsCount);
		dashBoardCount.setTotalBilledCircuits(billedCircuits);
		dashBoardCount.setStageCount(processCount);
		dashBoardCount.setOrderCount(orderCount);
		dashBoardCount.setProductWiseCount(productWiseCount);
		dashBoardCount.setLastMileCount(lastMileCount);
		dashBoardCount.setActiveServiceRecordCount(activeServiceRecordCount);
		return dashBoardCount;
	}

	/**
	 * getActiveServiceRecordsByStageDefKey
	 *
	 * @param stageKey
	 * @return
	 */
	public List<Map<String, String>> getServiceRecordsByStageDefKey(String stageKey) {
		List<Map<String, String>> stagePlanList = processPlanRepository.findInProgressByStageKey(stageKey);
		LOGGER.info("List of Service Records {}", stagePlanList);
		return stagePlanList;
	}

	public List<Map<String, String>> getServiceRecordsByProcessKey(String processKey) {
		List<Map<String, String>> stagePlanList = processPlanRepository.findInProgressByProcessKey(processKey);
		LOGGER.info("List of Service Records {}", stagePlanList);
		return stagePlanList;
	}

	/**
	 * getStageCount
	 *
	 * @param scOrderId
	 * @return
	 */
	public List<Map<String, String>> getServiceIdGroupByProduct() {
		List<Map<String, String>> stageCount = scServiceDetailRepository.groupByProductAndType();
		LOGGER.info("List of Stage Count {}", stageCount);
		return stageCount;
	}

	/**
	 * GetCountBasedOnOrderTypeAndProductName
	 *
	 * @param productName,orderType
	 * @return
	 */
	public List<Map<String, String>> getServiceIdGroupByOrderType(String productName, String orderType) {
		List<Map<String, String>> serviceRecord = scServiceDetailRepository.groupByProductNameAndOrderType(productName,
				orderType);
		LOGGER.info("List of Service Records based on productName and orderType {}", serviceRecord);
		return serviceRecord;
	}

	/**
	 * getTaskLogsByScorderId
	 *
	 * @param scOrderId
	 * @param orderCode
	 * @return
	 */
	public List<ProcessTaskLogBean> getTaskLogsByScorderId(Integer scOrderId, String orderCode) {
		List<ProcessTaskLog> processtaskLogEntities = processTaskLogRepository.findByScOrderIdAndOrderCode(scOrderId,
				orderCode);
		List<ProcessTaskLogBean> processTaskLogs = processtaskLogEntities.stream().map(pt -> {
			ProcessTaskLogBean processTaskLogBean = new ProcessTaskLogBean();
			processTaskLogBean.setAction(pt.getAction());
			processTaskLogBean.setActionFrom(pt.getActionFrom());
			processTaskLogBean.setActionTo(pt.getActionTo());
			processTaskLogBean.setCreatedTime(pt.getCreatedTime());
			processTaskLogBean.setDescription(pt.getDescrption());
			processTaskLogBean.setOrderCode(pt.getOrderCode());
			processTaskLogBean.setScOrderId(pt.getScOrderId());
			processTaskLogBean.setServiceCode(pt.getServiceCode());
			processTaskLogBean.setServiceId(pt.getServiceId());
			processTaskLogBean.setTask(constructTaskBean(pt.getTask()));
			processTaskLogBean.setCategory(pt.getCategory());
			processTaskLogBean.setSubCategory(pt.getSubCategory());
			return processTaskLogBean;
		}).collect(Collectors.toList());
		LOGGER.info("List of processTaskLog Count {}", processTaskLogs.size());
		return processTaskLogs;
	}

	/**
	 * @param task
	 * @return
	 */
	private TaskBean constructTaskBean(Task task) {
		TaskBean taskBean = new TaskBean();
		if (task != null) {
			setTaskDetails(taskBean, task);
		}
		return taskBean;
	}

	private void setTaskDetails(TaskBean taskBean, Task task) {
		if (task != null && taskBean != null) {
			taskBean.setTaskId(taskBean.getTaskId());
			taskBean.setTaskId(task.getId());
			taskBean.setCatagory(task.getCatagory());
			taskBean.setClaimTime(task.getClaimTime());
			taskBean.setCompletedTime(task.getCompletedTime());
			taskBean.setCreatedTime(task.getCreatedTime());
			taskBean.setDuedate(task.getDuedate());
			taskBean.setOrderCode(task.getOrderCode());
			taskBean.setServiceId(task.getServiceId());
			taskBean.setServiceCode(task.getServiceCode());
			taskBean.setStatus(task.getMstStatus().getCode());
			taskBean.setUpdatedTime(task.getUpdatedTime());
			taskBean.setWfProcessInstId(task.getWfProcessInstId());
			taskBean.setWfTaskId(task.getWfTaskId());
			taskBean.setCity(task.getCity());
			taskBean.setOrderType(task.getOrderType());
			taskBean.setServiceType(task.getServiceType());
			taskBean.setIsJeopardyTask(task.getIsJeopardyTask());
			if (task.getMstTaskDef() != null) {
				taskBean.setButtonLabel(task.getMstTaskDef().getButtonLabel());
				taskBean.setDescription(task.getMstTaskDef().getDescription());
				taskBean.setTitle(task.getMstTaskDef().getTitle());
				taskBean.setAssignedGroup(task.getMstTaskDef().getAssignedGroup());
				taskBean.setTaskDefKey(task.getMstTaskDef().getKey());
				taskBean.setName(task.getMstTaskDef().getName());

			}
			if (task.getScServiceDetail().getIsAmended() != null
					&& CommonConstants.Y.equalsIgnoreCase(task.getScServiceDetail().getIsAmended())) {
				taskBean.setIsOrderAmendent(task.getScServiceDetail().getIsAmended());
			}
		}
	}

	/**
	 * processStagePlans
	 *
	 * @param scOrderId
	 * @param serviceDashBoardBean
	 * @param stagePlanBeans
	 * @param stagePlan
	 * @param isDeemedAcceptance
	 */
	private void processStagePlans(Integer scOrderId, ServiceDashBoardBean serviceDashBoardBean,
			List<StagePlanBean> stagePlanBeans, StagePlan stagePlan, String isDeemedAcceptance) {
		StagePlanBean stagePlanBean = new StagePlanBean();
		stagePlanBean.setActualEndTime(stagePlan.getActualEndTime());
		stagePlanBean.setActualStartTime(stagePlan.getActualStartTime());
		if (stagePlan.getMstStageDef().getCustomerView() != null)
			stagePlanBean.setCustomerView(stagePlan.getMstStageDef().getCustomerView().equals("Y") ? true : false);
		stagePlanBean.setEstimatedEndTime(stagePlan.getEstimatedEndTime());
		stagePlanBean.setEstimatedStartTime(stagePlan.getEstimatedStartTime());
		stagePlanBean.setPlannedEndTime(stagePlan.getPlannedEndTime());
		stagePlanBean.setPlannedStartTime(stagePlan.getPlannedStartTime());
		if ("IPC".equals(serviceDashBoardBean.getProductName())
				|| "IZO Private Cloud".equals(serviceDashBoardBean.getProductName())) {
			if ("Service Acceptance".equals(stagePlan.getMstStageDef().getName())
					&& (null != isDeemedAcceptance && isDeemedAcceptance.equals("Y"))) {
				stagePlanBean.setStageName("Service Acceptance-Deemed");
			} else if (("Service Acceptance".equals(stagePlan.getMstStageDef().getName())
					&& (null != isDeemedAcceptance && isDeemedAcceptance.equals("N")))) {
				stagePlanBean.setStageName(stagePlan.getMstStageDef().getName());
			} else {
				stagePlanBean.setStageName(stagePlan.getMstStageDef().getName());
			}
		} else {
			stagePlanBean.setStageName(stagePlan.getMstStageDef().getName());
		}
		if (stagePlan.getMstStageDef().getAdminView() != null
				&& stagePlan.getMstStageDef().getAdminView().equalsIgnoreCase("Y")) {
			stagePlanBean.setAdminView(true);
		}
		stagePlanBean.setStatus(stagePlan.getMstStatus().getCode());
		stagePlanBean.setIsDelayed(false);
		if (stagePlan.getEstimatedEndTime() != null
				&& !stagePlan.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)) {
			Integer difference = (new Date()).compareTo(stagePlan.getPlannedEndTime());
			if (difference > 0) {
				stagePlanBean.setIsDelayed(true);
			} else {
				stagePlanBean.setIsDelayed(false);
			}
		}
		stagePlanBean.setStagePlanId(stagePlan.getId());
		stagePlanBean.setTargetedEndTime(stagePlan.getTargettedEndTime());
		stagePlanBean.setTargetedStartTime(stagePlan.getTargettedStartTime());
		stagePlanBeans.add(stagePlanBean);
		List<ProcessPlanBean> processPlanBeans = new ArrayList<>();
		stagePlanBean.setProcessPlans(processPlanBeans);
		List<ProcessPlan> processPlans = processPlanRepository.findByStagePlanId(stagePlan.getId());
		for (ProcessPlan processPlan : processPlans) {
			processProcessPlans(scOrderId, processPlanBeans, processPlan);
		}
	}

	/**
	 * processProcessPlans
	 *
	 * @param scOrderId
	 * @param processPlanBeans
	 * @param processPlan
	 */
	private void processProcessPlans(Integer scOrderId, List<ProcessPlanBean> processPlanBeans,
			ProcessPlan processPlan) {
		ProcessPlanBean processPlanBean = new ProcessPlanBean();
		processPlanBean.setActualEndTime(processPlan.getActualEndTime());
		processPlanBean.setActualStartTime(processPlan.getActualStartTime());
		if (processPlan.getMstProcessDef().getCustomerView() != null)
			processPlanBean
					.setCustomerView(processPlan.getMstProcessDef().getCustomerView().equals("Y") ? true : false);
		processPlanBean.setEstimatedEndTime(processPlan.getEstimatedEndTime());
		processPlanBean.setEstimatedStartTime(processPlan.getEstimatedStartTime());
		processPlanBean.setPlannedEndTime(processPlan.getPlannedEndTime());
		processPlanBean.setPlannedStartTime(processPlan.getPlannedStartTime());
		processPlanBean.setStatus(processPlan.getMstStatus().getCode());
		if (processPlan.getEstimatedEndTime() != null) {
			Integer difference = (new Date()).compareTo(processPlan.getPlannedEndTime());
			if (difference > 0) {
				processPlanBean.setIsDelayed(true);
			} else {
				processPlanBean.setIsDelayed(false);
			}
		}
		processPlanBean.setProcessPlanId(processPlan.getId());
		processPlanBean.setScOrderId(scOrderId);
		processPlanBean.setName(processPlan.getMstProcessDef().getName());
		if (processPlan.getMstProcessDef().getAdminView() != null
				&& processPlan.getMstProcessDef().getAdminView().equalsIgnoreCase("Y")) {
			processPlanBean.setAdminView(true);
		}
		processPlanBean.setSequence(processPlan.getSequence());
		processPlanBean.setTargetedEndTime(processPlan.getTargettedEndTime());
		processPlanBean.setTargetedStartTime(processPlan.getTargettedStartTime());
		processPlanBean.setSiteType(processPlan.getSiteType());
		processPlanBeans.add(processPlanBean);
		List<ActivityPlanBean> activityPlanBeans = new ArrayList<>();
		List<ActivityPlan> activities = activityPlanRepository.findByProcessPlanId(processPlan.getId());
		for (ActivityPlan activityPlan : activities) {
			processActivityPlans(activityPlanBeans, activityPlan);
		}

		processPlanBean.setActivityPlans(activityPlanBeans);
	}

	/**
	 * processActivityPlans
	 *
	 * @param activityPlanBeans
	 * @param activityPlan
	 */
	private void processActivityPlans(List<ActivityPlanBean> activityPlanBeans, ActivityPlan activityPlan) {
		ActivityPlanBean activityPlanBean = new ActivityPlanBean();
		activityPlanBean.setActivityPlanId(activityPlan.getId());
		activityPlanBean.setActualEndTime(activityPlan.getActualEndTime());
		activityPlanBean.setActualStartTime(activityPlan.getActualStartTime());
		if (activityPlan.getMstActivityDef().getCustomerView() != null)
			activityPlanBean
					.setCustomerView(activityPlan.getMstActivityDef().getCustomerView().equals("Y") ? true : false);
		activityPlanBean.setEstimatedEndTime(activityPlan.getEstimatedEndTime());
		activityPlanBean.setEstimatedStartTime(activityPlan.getEstimatedStartTime());
		activityPlanBean.setPlannedEndTime(activityPlan.getPlannedEndTime());
		activityPlanBean.setPlannedStartTime(activityPlan.getPlannedStartTime());
		activityPlanBean.setStatus(activityPlan.getMstStatus().getCode());
		activityPlanBean.setName(activityPlan.getMstActivityDef().getName());
		if (activityPlan.getMstActivityDef().getAdminView() != null
				&& activityPlan.getMstActivityDef().getAdminView().equalsIgnoreCase("Y")) {
			activityPlanBean.setAdminView(true);
		}
		if (activityPlan.getEstimatedEndTime() != null) {
			Integer difference = (new Date()).compareTo(activityPlan.getPlannedEndTime());
			if (difference > 0) {
				activityPlanBean.setIsDelayed(true);
			} else {
				activityPlanBean.setIsDelayed(false);
			}
		}
		activityPlanBean.setSequence(activityPlan.getSequence());
		activityPlanBean.setTargetedEndTime(activityPlan.getTargettedEndTime());
		activityPlanBean.setTargetedStartTime(activityPlan.getTargettedStartTime());
		activityPlanBean.setSiteType(activityPlan.getSiteType());
		List<TaskPlan> taskPlans = taskPlanRepository.findByActivityPlanIdSort(activityPlan.getId());
		if (taskPlans != null) {
			for (TaskPlan taskPlan : taskPlans) {

				activityPlanBean.getTaskPlans().add(setTaskBean(taskPlan));

			}
		}
		activityPlanBeans.add(activityPlanBean);

	}

	private TaskPlanBean setTaskBean(TaskPlan taskPlan) {

		TaskPlanBean taskPlanBean = new TaskPlanBean();
		taskPlanBean.setTaskPlanId(taskPlan.getId());
		taskPlanBean.setActualEndTime(taskPlan.getActualEndTime());
		taskPlanBean.setActualEndTime(taskPlan.getActualEndTime());
		taskPlanBean.setActualStartTime(taskPlan.getActualStartTime());
		if (taskPlan.getMstTaskDef().getIsCustomerTask() != null) {
			taskPlanBean.setCustomerTask(taskPlan.getMstTaskDef().getIsCustomerTask().equals("Y") ? true : false);
		}
		taskPlanBean.setCustomerView(true);
		taskPlanBean.setEstimatedEndTime(taskPlan.getEstimatedEndTime());
		taskPlanBean.setEstimatedStartTime(taskPlan.getEstimatedStartTime());
		taskPlanBean.setPlannedEndTime(taskPlan.getPlannedEndTime());
		taskPlanBean.setPlannedStartTime(taskPlan.getPlannedStartTime());
		taskPlanBean.setStatus(taskPlan.getMstStatus().getCode());
		taskPlanBean.setName(taskPlan.getMstTaskDef().getName());
		if (taskPlan.getMstTaskDef().getAdminView() != null
				&& taskPlan.getMstTaskDef().getAdminView().equalsIgnoreCase("Y")) {
			taskPlanBean.setAdminView(true);
		} else {
			taskPlanBean.setAdminView(false);

		}
		taskPlanBean.setIsDelayed(false);
		if (taskPlan.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.INPROGRESS)
				&& taskPlan.getPlannedEndTime() != null) {
			Integer difference = (new Date()).compareTo(taskPlan.getPlannedEndTime());
			if (difference > 0) {
				taskPlanBean.setIsDelayed(true);
			} else {
				taskPlanBean.setIsDelayed(false);
			}
		}
		taskPlanBean.setSiteType(taskPlan.getSiteType());
		taskPlanBean.setTargetedEndTime(taskPlan.getTargettedEndTime());
		taskPlanBean.setTargetedStartTime(taskPlan.getTargettedStartTime());

		return taskPlanBean;

	}

	public List<OrderDashboardResponse> customerSearch(String type, String input) {
		List<Integer> orderIds = new ArrayList<>();
		List<String> serviceCodes = new ArrayList<>();
		switch (type) {
		case "CUSTOMER_ID":
			orderIds.addAll(scOrderRepository.findAllByTpsSfdcCuidAndIsActive(input, "Y").stream().map(ScOrder::getId)
					.collect(Collectors.toList()));
			break;
		case "LOCAL_CONTACT_NAME":
			orderIds.addAll(scServiceDetailRepository.findByOrdersWithLocalContactName(input).stream()
					.map(scServiceDetail -> scServiceDetail.getScOrder().getId()).collect(Collectors.toList()));
			break;
		case "CUSTOMER_NAME":
			orderIds.addAll(scOrderRepository.findAllByErfCustLeNameAndIsActive(String.valueOf(input), "Y").stream()
					.map(ScOrder::getId).collect(Collectors.toList()));
			break;
		case "SERVICE_ID":
			serviceCodes.add(input);
			List<ScServiceDetail> scServiceDetails = scServiceDetailRepository
					.findByMstStatus_codeInAndIsMigratedOrderAndUuidIn(
							Arrays.asList(TaskStatusConstants.RESOURCE_RELEASED, TaskStatusConstants.JEOPARDY_INITIATED,
									TaskStatusConstants.CANCELLATION_INITIATED, TaskStatusConstants.INPROGRESS,
									TaskStatusConstants.ACTIVE, TaskStatusConstants.TERMINATE, TaskStatusConstants.HOLD,
									TaskStatusConstants.CANCELLED, TaskStatusConstants.DEFERRED_DELIVERY,
									TaskStatusConstants.RESOURCE_RELEASED_INITIATED,
									TaskStatusConstants.CANCELLATION_INITIATED, TaskStatusConstants.JEOPARDY_INITIATED,
									TaskStatusConstants.AMENDED, TaskStatusConstants.MOVETOM6,
									TaskStatusConstants.TERMINATION_INITIATED,
									TaskStatusConstants.TERMINATION_INPROGRESS, TaskStatusConstants.CANCEL_TERMINATION,TaskStatusConstants.INACTIVE),

							"N", serviceCodes);
			if (!CollectionUtils.isEmpty(scServiceDetails)) {
				scServiceDetails.forEach(scServiceDetail -> {
					orderIds.add(scServiceDetail.getScOrder().getId());
				});
			}
			break;
		case "INTERNATIONAL_SERVICE_ID":
			serviceCodes.add(input);
			LOGGER.info("INTERNATIONAL_SERVICE_ID::{}", input);
			List<ScServiceDetail> internationalScServiceDetails = scServiceDetailRepository
					.findByMstStatus_codeInAndIsMigratedOrderAndTpsServiceIdIn(
							Arrays.asList(TaskStatusConstants.RESOURCE_RELEASED, TaskStatusConstants.JEOPARDY_INITIATED,
									TaskStatusConstants.CANCELLATION_INITIATED, TaskStatusConstants.INPROGRESS,
									TaskStatusConstants.ACTIVE, TaskStatusConstants.TERMINATE,
									TaskStatusConstants.INACTIVE, TaskStatusConstants.HOLD,
									TaskStatusConstants.CANCELLED, TaskStatusConstants.DEFERRED_DELIVERY,
									TaskStatusConstants.RESOURCE_RELEASED_INITIATED,
									TaskStatusConstants.CANCELLATION_INITIATED, TaskStatusConstants.JEOPARDY_INITIATED,
									TaskStatusConstants.AMENDED, TaskStatusConstants.MOVETOM6),

							"N", serviceCodes);
			if (!CollectionUtils.isEmpty(internationalScServiceDetails)) {
				internationalScServiceDetails.forEach(scServiceDetail -> {
					orderIds.add(scServiceDetail.getScOrder().getId());
				});
			}
			break;
		case "PARENT_SERVICE_ID":
			serviceCodes.add(input);
			LOGGER.info("PARENT_SERVICE_ID::{}", input);
			List<ScServiceDetail> parentScServiceDetails = scServiceDetailRepository
					.findByMstStatus_codeInAndIsMigratedOrderAndParentUuidIn(
							Arrays.asList(TaskStatusConstants.RESOURCE_RELEASED, TaskStatusConstants.JEOPARDY_INITIATED,
									TaskStatusConstants.CANCELLATION_INITIATED, TaskStatusConstants.INPROGRESS,
									TaskStatusConstants.ACTIVE, TaskStatusConstants.TERMINATE, TaskStatusConstants.HOLD,
									TaskStatusConstants.CANCELLED, TaskStatusConstants.DEFERRED_DELIVERY,
									TaskStatusConstants.RESOURCE_RELEASED_INITIATED,
									TaskStatusConstants.CANCELLATION_INITIATED, TaskStatusConstants.JEOPARDY_INITIATED,
									TaskStatusConstants.AMENDED, TaskStatusConstants.MOVETOM6,
									TaskStatusConstants.TERMINATION_INITIATED,
									TaskStatusConstants.TERMINATION_INPROGRESS, TaskStatusConstants.CANCEL_TERMINATION,TaskStatusConstants.INACTIVE),

							"N", serviceCodes);
			if (!CollectionUtils.isEmpty(parentScServiceDetails)) {
				parentScServiceDetails.forEach(scServiceDetail -> {
					orderIds.add(scServiceDetail.getScOrder().getId());
				});
			}
			break;
		default:
			List<ScOrder> scOrder = scOrderRepository.findAllByOpOrderCodeAndIsActive(String.valueOf(input), "Y");
			if (scOrder.isEmpty() || scOrder == null) {
				CancellationRequest cancellationRequest = cancellationRequestRepository
						.findByOrderCode(String.valueOf(input));
				if (cancellationRequest != null) {
					scOrder = scOrderRepository
							.findAllByOpOrderCodeAndIsActive(cancellationRequest.getCancellationOrderCode(), "Y");
				}
			}
			orderIds.addAll(scOrder.stream().map(ScOrder::getId).collect(Collectors.toList()));
		}
		return orderIds.stream().map(orders -> getOrderDashboardDetailsForCustomer(orders, serviceCodes, type))
				.collect(Collectors.toList());
	}

	public List<OrderDashboardResponse> customerSearchByServiceId(String type, String input) {
		LOGGER.info("Input scOrderId in customerSearchByServiceId input={} type={}", input, type);
		List<String> serviceCodes = new ArrayList<>();
		List<ScServiceDetail> scServiceDetails = new ArrayList<>();
		switch (type) {

		case "SERVICE_ID":
			serviceCodes.add(input);
			scServiceDetails = scServiceDetailRepository.findByMstStatus_codeInAndIsMigratedOrderAndUuidIn(
					Arrays.asList(TaskStatusConstants.RESOURCE_RELEASED, TaskStatusConstants.JEOPARDY_INITIATED,
							TaskStatusConstants.CANCELLATION_INITIATED, TaskStatusConstants.INPROGRESS,
							TaskStatusConstants.ACTIVE, TaskStatusConstants.TERMINATE, TaskStatusConstants.HOLD,
							TaskStatusConstants.CANCELLED, TaskStatusConstants.DEFERRED_DELIVERY,
							TaskStatusConstants.RESOURCE_RELEASED_INITIATED, TaskStatusConstants.CANCELLATION_INITIATED,
							TaskStatusConstants.JEOPARDY_INITIATED, TaskStatusConstants.AMENDED,
							TaskStatusConstants.MOVETOM6, TaskStatusConstants.TERMINATION_INITIATED,
							TaskStatusConstants.TERMINATION_INPROGRESS, TaskStatusConstants.CANCEL_TERMINATION,TaskStatusConstants.INACTIVE),
					"N", serviceCodes);

			break;
		case "INTERNATIONAL_SERVICE_ID":
			serviceCodes.add(input);
			LOGGER.info("INTERNATIONAL_SERVICE_ID::{}", input);
			scServiceDetails = scServiceDetailRepository.findByMstStatus_codeInAndIsMigratedOrderAndTpsServiceIdIn(
					Arrays.asList(TaskStatusConstants.RESOURCE_RELEASED, TaskStatusConstants.JEOPARDY_INITIATED,
							TaskStatusConstants.CANCELLATION_INITIATED, TaskStatusConstants.INPROGRESS,
							TaskStatusConstants.ACTIVE, TaskStatusConstants.TERMINATE, TaskStatusConstants.INACTIVE,
							TaskStatusConstants.HOLD, TaskStatusConstants.CANCELLED,
							TaskStatusConstants.DEFERRED_DELIVERY, TaskStatusConstants.RESOURCE_RELEASED_INITIATED,
							TaskStatusConstants.CANCELLATION_INITIATED, TaskStatusConstants.JEOPARDY_INITIATED,
							TaskStatusConstants.AMENDED, TaskStatusConstants.MOVETOM6),
					"N", serviceCodes);

			break;
		case "PARENT_SERVICE_ID":
			serviceCodes.add(input);
			LOGGER.info("PARENT_SERVICE_ID::{}", input);
			scServiceDetails = scServiceDetailRepository.findByMstStatus_codeInAndIsMigratedOrderAndParentUuidIn(
					Arrays.asList(TaskStatusConstants.RESOURCE_RELEASED, TaskStatusConstants.JEOPARDY_INITIATED,
							TaskStatusConstants.CANCELLATION_INITIATED, TaskStatusConstants.INPROGRESS,
							TaskStatusConstants.ACTIVE, TaskStatusConstants.TERMINATE, TaskStatusConstants.HOLD,
							TaskStatusConstants.CANCELLED, TaskStatusConstants.DEFERRED_DELIVERY,
							TaskStatusConstants.RESOURCE_RELEASED_INITIATED, TaskStatusConstants.CANCELLATION_INITIATED,
							TaskStatusConstants.JEOPARDY_INITIATED, TaskStatusConstants.AMENDED,
							TaskStatusConstants.MOVETOM6, TaskStatusConstants.TERMINATION_INITIATED,
							TaskStatusConstants.TERMINATION_INPROGRESS, TaskStatusConstants.CANCEL_TERMINATION,TaskStatusConstants.INACTIVE),
					"N", serviceCodes);
			break;

		}
		return getOrderDashboardDetailsForCustomer(scServiceDetails, type, input);
	}

	@Transactional(readOnly = true)
	public PagedResult<OrderDashboardResponse> getActiveOrdersBasedOnFilter(ServiceRequest request) {

		List<OrderDashboardResponse> responses = new ArrayList<>();

		List<String> status = request.getStatus();

		if (status == null || status.isEmpty()) {
			status = Arrays.asList("INPROGRESS", "TERMINATE", "HOLD", "CANCELLED", "ACTIVE",
					TaskStatusConstants.DEFERRED_DELIVERY, TaskStatusConstants.RESOURCE_RELEASED_INITIATED,
					TaskStatusConstants.CANCELLATION_INITIATED, TaskStatusConstants.JEOPARDY_INITIATED,
					TaskStatusConstants.TERMINATION_INITIATED);
		}

		List<ScServiceDetail> totalServiceDetails = scServiceDetailRepository.findAll(
				ServiceDetailSpecification.getServiceDetailsFilter(request.getOrdeType(), request.getOrderCategory(),
						request.getOrderCategory(), status, request.getCustomerName(), request.getLastMileScenario(),
						request.getProductName(), request.getGroupName(), request.getAssignedPM(),
						request.getOrderCode(), request.getServiceCode(), request.getInternationalServiceCode(),
						request.getIsJeopardyTask(), request.getServiceConfigurationStatus(),
						request.getActivationConfigStatus(), request.getBillingStatus(), request.getBillStartDate(),
						request.getBillEndDate(), request.getCommissionedStartDate(), request.getCommissionedEndDate(),
						request.getBillingCompletionStartDate(), request.getBillingCompletionEndDate(),
						request.getServiceConfigStartDate(), request.getServiceConfigEndDate()));

		Page<ScServiceDetail> scServiceDetails = scServiceDetailRepository.findAll(
				ServiceDetailSpecification.getServiceDetailsFilter(request.getOrdeType(), request.getOrderCategory(),
						request.getOrderSubCategory(), status, request.getCustomerName(), request.getLastMileScenario(),
						request.getProductName(), request.getGroupName(), request.getAssignedPM(),
						request.getOrderCode(), request.getServiceCode(), request.getInternationalServiceCode(),
						request.getIsJeopardyTask(), request.getServiceConfigurationStatus(),
						request.getActivationConfigStatus(), request.getBillingStatus(), request.getBillStartDate(),
						request.getBillEndDate(), request.getCommissionedStartDate(), request.getCommissionedEndDate(),
						request.getBillingCompletionStartDate(), request.getBillingCompletionEndDate(),
						request.getServiceConfigStartDate(), request.getServiceConfigEndDate()),
				PageRequest.of(request.getPage() - 1, request.getSize()));

		scServiceDetails.forEach(scServiceDetail -> {
			ScOrder scOrderEntity = scServiceDetail.getScOrder();
			OrderDashboardResponse orderDashboardResponse = new OrderDashboardResponse();
			orderDashboardResponse.setOrderCode(scServiceDetail.getScOrder().getUuid());
			orderDashboardResponse.setServiceId(scServiceDetail.getId());
			orderDashboardResponse.setStatus(scServiceDetail.getMstStatus().getCode());
			orderDashboardResponse.setOrderDate(scOrderEntity.getCreatedDate());
			orderDashboardResponse.setErfOrderId(scOrderEntity.getErfOrderId());
			orderDashboardResponse.setCustomerId(scOrderEntity.getTpsSfdcCuid());
			orderDashboardResponse.setOrderType(scServiceDetail.getOrderType());
			orderDashboardResponse.setTargetedDeliveryDate(scServiceDetail.getTargetedDeliveryDate());
			orderDashboardResponse.setCommittedDeliveryDate(scServiceDetail.getCommittedDeliveryDate());
			orderDashboardResponse.setEstimatedDeliveryDate(scServiceDetail.getEstimatedDeliveryDate());
			orderDashboardResponse.setActualDeliveryDate(scServiceDetail.getActualDeliveryDate());
			orderDashboardResponse.setServiceCode(scServiceDetail.getUuid());
			orderDashboardResponse.setProductName(scServiceDetail.getErfPrdCatalogProductName());
			orderDashboardResponse.setOrderCategory(scServiceDetail.getOrderCategory());
			orderDashboardResponse.setOrderSubCategory(scServiceDetail.getOrderSubCategory());
			orderDashboardResponse.setLocation(scServiceDetail.getSiteAddress());
			orderDashboardResponse.setLmType(scServiceDetail.getLastmileScenario());
			orderDashboardResponse.setCustomerName(scOrderEntity.getErfCustCustomerName());
			if (scServiceDetail.getArc() != null)
				orderDashboardResponse.setMrc(String.format("%.2f", ((scServiceDetail.getArc() / 12) / 70)));
			if (scServiceDetail.getNrc() != null)
				orderDashboardResponse.setNrc(String.format("%.2f", (scServiceDetail.getNrc() / 70)));
			if (scServiceDetail.getDifferentialMrc() != null)
				orderDashboardResponse
						.setDifferentialMrc(String.format("%.2f", (scServiceDetail.getDifferentialMrc() / 70)));
			if (scServiceDetail.getDifferentialNrc() != null)
				orderDashboardResponse
						.setDifferentialNrc(String.format("%.2f", (scServiceDetail.getDifferentialNrc() / 70)));
			orderDashboardResponse.setAssignedPM(scServiceDetail.getAssignedPM());

			Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils.getComponentAttributesDetails(
					Arrays.asList("localLoopBandwidth", "localLoopBandwidthUnit", "bwUnit"), scServiceDetail.getId(),
					"LM", "A");

			String bwUnit = StringUtils.trimToEmpty(scComponentAttributesAMap.get("localLoopBandwidthUnit"));
			if (StringUtils.isBlank(bwUnit))
				bwUnit = scComponentAttributesAMap.get("bwUnit");
			orderDashboardResponse.setPortBandwidth(scComponentAttributesAMap.get("localLoopBandwidth"));
			orderDashboardResponse.setBwUnit(bwUnit);
			orderDashboardResponse.setCrfsDate(scServiceDetail.getServiceCommissionedDate());
			orderDashboardResponse.setIsJeopardyTask(scServiceDetail.getIsJeopardyTask());
			orderDashboardResponse.setOrderAge(
					ChronoUnit.DAYS.between(scOrderEntity.getCreatedDate().toLocalDateTime(), LocalDateTime.now()));
			responses.add(orderDashboardResponse);

		});

		Double filteredMrc = 0.0, filteredNrc = 0.0, billedMrc = 0.0, billedNrc = 0.0;

		if (!CollectionUtils.isEmpty(totalServiceDetails)) {
			filteredMrc = (totalServiceDetails.stream().filter(s -> s.getArc() != null)
					.mapToDouble(s -> new Double((s.getArc() / 12))).sum()) / 70;
			filteredNrc = (totalServiceDetails.stream().filter(s -> s.getNrc() != null)
					.mapToDouble(s -> new Double(s.getNrc())).sum()) / 70;
		}

		return new PagedResult<OrderDashboardResponse>(responses, scServiceDetails.getTotalElements(),
				scServiceDetails.getTotalPages(), null, null, null, String.format("%.2f", filteredMrc),
				String.format("%.2f", filteredNrc));
	}

	@Transactional(readOnly = true)
	public PagedResult<OrderDashboardResponse> getActiveOrders(int page, int size) {
		List<OrderDashboardResponse> responses = new ArrayList<>();

		Page<ScServiceDetail> scServiceDetails = scServiceDetailRepository
				.findByMstStatus_codeOrderByIdDesc(TaskStatusConstants.INPROGRESS, PageRequest.of(page - 1, size));

		scServiceDetails.forEach(scServiceDetail -> {
			ScOrder scOrderEntity = scServiceDetail.getScOrder();
			OrderDashboardResponse orderDashboardResponse = new OrderDashboardResponse();
			orderDashboardResponse.setOrderCode(scServiceDetail.getScOrder().getUuid());
			orderDashboardResponse.setServiceId(scServiceDetail.getId());
			orderDashboardResponse.setOrderDate(scOrderEntity.getCreatedDate());
			orderDashboardResponse.setErfOrderId(scOrderEntity.getErfOrderId());
			orderDashboardResponse.setCustomerId(scOrderEntity.getTpsSfdcCuid());
			String orderType = OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder());
			String orderCategory = OrderCategoryMapping.getOrderCategory(scServiceDetail, scServiceDetail.getScOrder());
			orderDashboardResponse.setOrderType(orderType);
			orderDashboardResponse.setTargetedDeliveryDate(scServiceDetail.getTargetedDeliveryDate());
			orderDashboardResponse.setCommittedDeliveryDate(scServiceDetail.getCommittedDeliveryDate());
			orderDashboardResponse.setEstimatedDeliveryDate(scServiceDetail.getEstimatedDeliveryDate());
			orderDashboardResponse.setActualDeliveryDate(scServiceDetail.getActualDeliveryDate());
			orderDashboardResponse.setServiceCode(scServiceDetail.getUuid());
			orderDashboardResponse.setProductName(scServiceDetail.getErfPrdCatalogProductName());
			orderDashboardResponse.setLocation(scServiceDetail.getSiteAddress());
			orderDashboardResponse.setLmType(scServiceDetail.getLastmileScenario());
			orderDashboardResponse.setOrderCategory(orderCategory);
			orderDashboardResponse.setCrfsDate(scServiceDetail.getServiceCommissionedDate());
			orderDashboardResponse.setOrderAge(
					ChronoUnit.DAYS.between(scOrderEntity.getCreatedDate().toLocalDateTime(), LocalDateTime.now()));

			orderDashboardResponse.setCustomerName(scOrderEntity.getErfCustCustomerName());

			Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils.getComponentAttributesDetails(
					Arrays.asList("localLoopBandwidth", "localLoopBandwidthUnit"), scServiceDetail.getId(), "LM", "A");

			orderDashboardResponse.setPortBandwidth(scComponentAttributesAMap.get("localLoopBandwidth"));
			orderDashboardResponse.setBwUnit(scComponentAttributesAMap.get("localLoopBandwidthUnit"));
			responses.add(orderDashboardResponse);

		});
		return new PagedResult<OrderDashboardResponse>(responses, scServiceDetails.getTotalElements(),
				scServiceDetails.getTotalPages());

	}

	public OrderDashboardResponse getOrderDashboardDetailsForCustomer(Integer orderId, List<String> serviceCodes,
			String type) {
		OrderDashboardResponse orderDashboardResponse = new OrderDashboardResponse();
		LOGGER.info("Input scOrderId in getOrderDashboardDetailsForCustomer, {}", orderId);
		Optional<ScOrder> scOrderEntityOptional = scOrderRepository.findById(orderId);
		if (scOrderEntityOptional.isPresent() && scOrderEntityOptional != null) {
			ScOrder scOrderEntity = scOrderEntityOptional.get();
			LOGGER.info("SCORDER ID EXISTS, {}", scOrderEntity.getId());
			orderDashboardResponse.setOrderCode(scOrderEntity.getOpOrderCode());
			orderDashboardResponse.setOrderDate(scOrderEntity.getCreatedDate());
			orderDashboardResponse.setErfOrderId(scOrderEntity.getErfOrderId());
			orderDashboardResponse.setErfOrderLeId(scOrderEntity.getErfOrderLeId());
			orderDashboardResponse.setCustomerId(scOrderEntity.getTpsSfdcCuid());
			/*
			 * orderDashboardResponse.setOrderType(scOrderEntity.getOrderType());
			 * orderDashboardResponse.setOrderCategory(scOrderEntity.getOrderCategory());
			 */

			Optional<ScContractInfo> scContractInfo = scContractInfoRepository.findByScOrder_id(scOrderEntity.getId())
					.stream().findFirst();
			if (scContractInfo.isPresent()) {
				orderDashboardResponse.setCustomerLeName(scContractInfo.get().getErfCustLeName());
				orderDashboardResponse.setCustomerContact(scContractInfo.get().getCustomerContact());
				orderDashboardResponse.setCustomerContactEmail(scContractInfo.get().getCustomerContactEmail());
				orderDashboardResponse.setCustomerBillingAddress(scContractInfo.get().getBillingAddress());
			}
			List<ScServiceDetail> scServiceEntitys = scServiceDetailRepository.findByScOrderId(scOrderEntity.getId());

			if (!CollectionUtils.isEmpty(serviceCodes)) {
				String service = serviceCodes.get(0);
				if (type.equalsIgnoreCase("INTERNATIONAL_SERVICE_ID")) {
					scServiceEntitys = scServiceEntitys.stream()
							.filter(scServiceDetail -> service.equalsIgnoreCase(scServiceDetail.getTpsServiceId()))
							.collect(Collectors.toList());
				} else if (type.equalsIgnoreCase("PARENT_SERVICE_ID")) {
					scServiceEntitys = scServiceEntitys.stream()
							.filter(scServiceDetail -> service.equalsIgnoreCase(scServiceDetail.getParentUuid()))
							.collect(Collectors.toList());
				} else {
					scServiceEntitys = scServiceEntitys.stream()
							.filter(scServiceDetail -> service.equalsIgnoreCase(scServiceDetail.getUuid()))
							.collect(Collectors.toList());
				}
			}

			if (!scServiceEntitys.isEmpty()) {
				List<ServiceDashBoardBean> serviceDetailBeans = new ArrayList<>();
				orderDashboardResponse.setServiceDetails(serviceDetailBeans);
				orderDashboardResponse.setScOrderId(scOrderEntity.getId());
				scServiceEntitys.stream().forEach(scServiceDetail -> {

					if ("N".equalsIgnoreCase(scServiceDetail.getIsMigratedOrder())) {
						ServiceDashBoardBean serviceDashBoardBean = new ServiceDashBoardBean();
						serviceDashBoardBean.setTargetedDeliveryDate(scServiceDetail.getTargetedDeliveryDate());
						serviceDashBoardBean.setCommittedDeliveryDate(scServiceDetail.getCommittedDeliveryDate());
						serviceDashBoardBean.setEstimatedDeliveryDate(scServiceDetail.getEstimatedDeliveryDate());
						serviceDashBoardBean.setActualDeliveryDate(scServiceDetail.getActualDeliveryDate());
						serviceDashBoardBean.setRrfsDate(scServiceDetail.getRrfsDate());
						serviceDashBoardBean.setOrderCode(scServiceDetail.getScOrderUuid());
						serviceDashBoardBean.setOrderSubCategory(scServiceDetail.getOrderSubCategory());
						serviceDashBoardBean.setStatus(scServiceDetail.getMstStatus().getCode());
						serviceDashBoardBean.setParentServiceCode(scServiceDetail.getParentUuid());
						if(("IZO Internet WAN".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogProductName()) && scServiceDetail.getDestinationCountry()!= null && !scServiceDetail.getDestinationCountry().equalsIgnoreCase("India"))
								|| "DIA".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogProductName())
								|| ("GVPN".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogProductName()) 
										&& scServiceDetail.getDestinationCountry()!=null && !scServiceDetail.getDestinationCountry().equalsIgnoreCase("India"))) {
								LOGGER.info("International Service Id::{}",scServiceDetail.getId());
								serviceDashBoardBean.setInternationalServiceCode(scServiceDetail.getTpsServiceId());
						}
						if ("TERMINATION-INPROGRESS".equalsIgnoreCase(scServiceDetail.getMstStatus().getCode())) {
							Date terminationEffectiveDate = DateUtil
									.convertStringToDateYYMMDD(scServiceDetail.getTerminationEffectiveDate());
							Date currentDate = DateUtil
									.convertStringToDateYYMMDD(DateUtil.convertDateToString(new Date()));
							LOGGER.info(
									"Service Id::{} with Termination Inprogress with terminationEffectiveDate::{} and  currentDate::{}",
									scServiceDetail.getId(), terminationEffectiveDate, currentDate);
							if (terminationEffectiveDate.compareTo(currentDate) > 0) {
								serviceDashBoardBean.setIsTerminationStatusChangeOptionRequired(true);
							}
						}
						String orderType = OrderCategoryMapping.getOrderType(scServiceDetail,
								scServiceDetail.getScOrder());
						String orderCategory = OrderCategoryMapping.getOrderCategory(scServiceDetail,
								scServiceDetail.getScOrder());
						serviceDashBoardBean.setOrderType(orderType);
						serviceDashBoardBean.setOrderCategory(orderCategory);
						orderDashboardResponse.setCrfsDate(scServiceDetail.getServiceCommissionedDate());
						orderDashboardResponse.setOrderAge(ChronoUnit.DAYS
								.between(scOrderEntity.getCreatedDate().toLocalDateTime(), LocalDateTime.now()));
						serviceDashBoardBean.setOrderType(scServiceDetail.getOrderType());
						serviceDashBoardBean.setOrderCategory(scServiceDetail.getOrderCategory());

						serviceDashBoardBean.setProductName(scServiceDetail.getErfPrdCatalogProductName());
						serviceDashBoardBean.setOfferingName(scServiceDetail.getErfPrdCatalogOfferingName());
						serviceDashBoardBean.setAssignedPM(scServiceDetail.getAssignedPM());
						serviceDashBoardBean.setLocation(scServiceDetail.getSiteAddress());
						serviceDashBoardBean
								.setLocationId(StringUtils.isNotBlank(scServiceDetail.getErfLocSiteAddressId())
										? Integer.valueOf(scServiceDetail.getErfLocSiteAddressId())
										: null);

						/*Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
								.getComponentAttributesDetails(
										Arrays.asList("localLoopBandwidth", "localLoopBandwidthUnit", "billStartDate",
												"localItContactEmailId", "localItContactMobile", "localItContactName"),
										scServiceDetail.getId(), "LM", "A");

						serviceDashBoardBean.setLocalLoopBandwidth(scComponentAttributesAMap.get("localLoopBandwidth"));
						serviceDashBoardBean
								.setLocalLoopBandwidthUnit(scComponentAttributesAMap.get("localLoopBandwidthUnit"));
						serviceDashBoardBean.setBillStartDate(scComponentAttributesAMap.get("billStartDate"));*/

						serviceDashBoardBean.setLocalLoopBandwidth(scServiceDetail.getLastmileBw());
						serviceDashBoardBean
								.setLocalLoopBandwidthUnit(scServiceDetail.getLastmileBwUnit());
						serviceDashBoardBean.setBillStartDate(String.valueOf(scServiceDetail.getBillStartDate()));
						

						if(StringUtils.isBlank(scServiceDetail.getLastmileBw()) || scServiceDetail.getBillStartDate()==null) {
							Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
							.getComponentAttributesDetails(
									Arrays.asList("localLoopBandwidth", "billStartDate"),scServiceDetail.getId(), "LM", "A");
		
							serviceDashBoardBean.setLocalLoopBandwidth(scComponentAttributesAMap.get("localLoopBandwidth"));
							serviceDashBoardBean.setLocalLoopBandwidthUnit("Mbps");
							serviceDashBoardBean.setBillStartDate(scComponentAttributesAMap.get("billStartDate"));
						}
						
						serviceDashBoardBean.setServiceCode(scServiceDetail.getUuid());
						serviceDashBoardBean.setServiceId(scServiceDetail.getId());
						List<StagePlanBean> stagePlanBeans = new ArrayList<>();
						Optional<StagePlan> stagePlan = stagePlanRepository.findByServiceId(scServiceDetail.getId())
								.stream().filter(stage -> "INPROGRESS".equalsIgnoreCase(stage.getMstStatus().getCode()))
								.findFirst();
						if (stagePlan.isPresent()) {
							stagePlanBeans.add(mapToStagePlanBean(stagePlan.get()));
						}
						serviceDashBoardBean.setStages(stagePlanBeans);
						serviceDashBoardBean.setTerminationFlowTriggered(scServiceDetail.getTerminationFlowTriggered());
						serviceDashBoardBean
								.setTerminationTrigerredDate(scServiceDetail.getTerminationInitiationDate());
						serviceDashBoardBean.setCustomerRequestorDate(scServiceDetail.getCustomerRequestorDate());
						serviceDashBoardBean.setTerminationEffectiveDate(scServiceDetail.getTerminationEffectiveDate());
						serviceDashBoardBean
								.setCancellationFlowTriggered(scServiceDetail.getCancellationFlowTriggered());
						serviceDashBoardBean
								.setCancellationInitiationDate(scServiceDetail.getCancellationInitiationDate());
						if (scServiceDetail.getIsAmended() != null
								&& CommonConstants.Y.equalsIgnoreCase(scServiceDetail.getIsAmended())) {
							serviceDashBoardBean.setIsOrderAmendent(scServiceDetail.getIsAmended());
						}

						if (scServiceDetail.getRequestForAmendment() != null
								&& CommonConstants.Y.equalsIgnoreCase(scServiceDetail.getRequestForAmendment())) {
							serviceDashBoardBean.setRequestForAmendment(scServiceDetail.getRequestForAmendment());
						}

						serviceDetailBeans.add(serviceDashBoardBean);
					}
				});
			}
		}
		return orderDashboardResponse;
	}

	private List<OrderDashboardResponse> getOrderDashboardDetailsForCustomer(List<ScServiceDetail> scServiceDetails,
			String type, String input) {
		List<OrderDashboardResponse> orderDashboardResponseList = new ArrayList<>();
		LOGGER.info("Input scOrderId in getOrderDashboardDetailsForCustomer input={} type={}", input, type);
		ScOrder scOrderEntity = null;
		if (!scServiceDetails.isEmpty()) {
		
			for (ScServiceDetail scServiceDetail : scServiceDetails) {
				OrderDashboardResponse orderDashboardResponse = new OrderDashboardResponse();
				List<ServiceDashBoardBean> serviceDetailBeans = new ArrayList<>();
				if ("N".equalsIgnoreCase(scServiceDetail.getIsMigratedOrder())) {

					scOrderEntity = scServiceDetail.getScOrder();
					LOGGER.info("SCORDER ID EXISTS, {}", scOrderEntity.getId());
					orderDashboardResponse.setOrderCode(scOrderEntity.getOpOrderCode());
					orderDashboardResponse.setOrderDate(scOrderEntity.getCreatedDate());
					orderDashboardResponse.setErfOrderId(scOrderEntity.getErfOrderId());
					orderDashboardResponse.setErfOrderLeId(scOrderEntity.getErfOrderLeId());
					orderDashboardResponse.setCustomerId(scOrderEntity.getTpsSfdcCuid());
					orderDashboardResponse.setScOrderId(scOrderEntity.getId());

					Optional<ScContractInfo> scContractInfo = scContractInfoRepository
							.findByScOrder_id(scOrderEntity.getId()).stream().findFirst();
					if (scContractInfo.isPresent()) {
						orderDashboardResponse.setCustomerLeName(scContractInfo.get().getErfCustLeName());
						orderDashboardResponse.setCustomerContact(scContractInfo.get().getCustomerContact());
						orderDashboardResponse.setCustomerContactEmail(scContractInfo.get().getCustomerContactEmail());
						orderDashboardResponse.setCustomerBillingAddress(scContractInfo.get().getBillingAddress());
					}
					ServiceDashBoardBean serviceDashBoardBean = new ServiceDashBoardBean();
					serviceDashBoardBean.setTargetedDeliveryDate(scServiceDetail.getTargetedDeliveryDate());
					serviceDashBoardBean.setCommittedDeliveryDate(scServiceDetail.getCommittedDeliveryDate());
					serviceDashBoardBean.setEstimatedDeliveryDate(scServiceDetail.getEstimatedDeliveryDate());
					serviceDashBoardBean.setActualDeliveryDate(scServiceDetail.getActualDeliveryDate());
					serviceDashBoardBean.setRrfsDate(scServiceDetail.getRrfsDate());
					serviceDashBoardBean.setOrderCode(scServiceDetail.getScOrderUuid());
					serviceDashBoardBean.setOrderSubCategory(scServiceDetail.getOrderSubCategory());
					serviceDashBoardBean.setStatus(scServiceDetail.getMstStatus().getCode());
					serviceDashBoardBean.setParentServiceCode(scServiceDetail.getParentUuid());
					if(("IZO Internet WAN".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogProductName()) && scServiceDetail.getDestinationCountry()!= null && !scServiceDetail.getDestinationCountry().equalsIgnoreCase("India"))
						|| "DIA".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogProductName())
						|| ("GVPN".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogProductName()) 
								&& scServiceDetail.getDestinationCountry()!=null && !scServiceDetail.getDestinationCountry().equalsIgnoreCase("India"))) {
						LOGGER.info("International Service Id::{}",scServiceDetail.getId());
						serviceDashBoardBean.setInternationalServiceCode(scServiceDetail.getTpsServiceId());
					}
					if ("TERMINATION-INPROGRESS".equalsIgnoreCase(scServiceDetail.getMstStatus().getCode())) {
						Date terminationEffectiveDate = DateUtil
								.convertStringToDateYYMMDD(scServiceDetail.getTerminationEffectiveDate());
						Date currentDate = DateUtil.convertStringToDateYYMMDD(DateUtil.convertDateToString(new Date()));
						LOGGER.info(
								"Service Id::{} with Termination Inprogress with terminationEffectiveDate::{} and  currentDate::{}",
								scServiceDetail.getId(), terminationEffectiveDate, currentDate);
						if (terminationEffectiveDate.compareTo(currentDate) > 0) {
							serviceDashBoardBean.setIsTerminationStatusChangeOptionRequired(true);
						}
					}

					String orderType = OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder());
					String orderCategory = OrderCategoryMapping.getOrderCategory(scServiceDetail,
							scServiceDetail.getScOrder());
					serviceDashBoardBean.setOrderType(orderType);
					serviceDashBoardBean.setOrderCategory(orderCategory);
					orderDashboardResponse.setCrfsDate(scServiceDetail.getServiceCommissionedDate());
					orderDashboardResponse.setOrderAge(ChronoUnit.DAYS
							.between(scOrderEntity.getCreatedDate().toLocalDateTime(), LocalDateTime.now()));
					serviceDashBoardBean.setOrderType(scServiceDetail.getOrderType());
					serviceDashBoardBean.setOrderCategory(scServiceDetail.getOrderCategory());

					serviceDashBoardBean.setProductName(scServiceDetail.getErfPrdCatalogProductName());
					serviceDashBoardBean.setOfferingName(scServiceDetail.getErfPrdCatalogOfferingName());
					serviceDashBoardBean.setAssignedPM(scServiceDetail.getAssignedPM());
					serviceDashBoardBean.setLocation(scServiceDetail.getSiteAddress());
					serviceDashBoardBean.setLocationId(StringUtils.isNotBlank(scServiceDetail.getErfLocSiteAddressId())
							? Integer.valueOf(scServiceDetail.getErfLocSiteAddressId())
							: null);
					
					
					serviceDashBoardBean.setLocalLoopBandwidth(scServiceDetail.getLastmileBw());
					serviceDashBoardBean.setLocalLoopBandwidthUnit(scServiceDetail.getLastmileBwUnit());
					serviceDashBoardBean.setBillStartDate(String.valueOf(scServiceDetail.getBillStartDate()));
					
					if(StringUtils.isBlank(scServiceDetail.getLastmileBw()) || scServiceDetail.getBillStartDate()==null) {
						Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
						.getComponentAttributesDetails(
								Arrays.asList("localLoopBandwidth", "billStartDate"),scServiceDetail.getId(), "LM", "A");
	
						serviceDashBoardBean.setLocalLoopBandwidth(scComponentAttributesAMap.get("localLoopBandwidth"));
						serviceDashBoardBean.setLocalLoopBandwidthUnit("Mbps");
						serviceDashBoardBean.setBillStartDate(scComponentAttributesAMap.get("billStartDate"));
					}

					serviceDashBoardBean.setServiceCode(scServiceDetail.getUuid());
					serviceDashBoardBean.setServiceId(scServiceDetail.getId());
					List<StagePlanBean> stagePlanBeans = new ArrayList<>();
					Optional<StagePlan> stagePlan = stagePlanRepository.findByServiceId(scServiceDetail.getId())
							.stream().filter(stage -> "INPROGRESS".equalsIgnoreCase(stage.getMstStatus().getCode()))
							.findFirst();
					if (stagePlan.isPresent()) {
						stagePlanBeans.add(mapToStagePlanBean(stagePlan.get()));
					}
					serviceDashBoardBean.setStages(stagePlanBeans);
					serviceDashBoardBean.setTerminationFlowTriggered(scServiceDetail.getTerminationFlowTriggered());
					serviceDashBoardBean.setTerminationTrigerredDate(scServiceDetail.getTerminationInitiationDate());
					serviceDashBoardBean.setCustomerRequestorDate(scServiceDetail.getCustomerRequestorDate());
					serviceDashBoardBean.setTerminationEffectiveDate(scServiceDetail.getTerminationEffectiveDate());
					serviceDashBoardBean.setCancellationFlowTriggered(scServiceDetail.getCancellationFlowTriggered());
					serviceDashBoardBean.setCancellationInitiationDate(scServiceDetail.getCancellationInitiationDate());
					if (scServiceDetail.getIsAmended() != null
							&& CommonConstants.Y.equalsIgnoreCase(scServiceDetail.getIsAmended())) {
						serviceDashBoardBean.setIsOrderAmendent(scServiceDetail.getIsAmended());
					}

					if (scServiceDetail.getRequestForAmendment() != null
							&& CommonConstants.Y.equalsIgnoreCase(scServiceDetail.getRequestForAmendment())) {
						serviceDashBoardBean.setRequestForAmendment(scServiceDetail.getRequestForAmendment());
					}

					serviceDetailBeans.add(serviceDashBoardBean);
					orderDashboardResponse.setServiceDetails(serviceDetailBeans);
					orderDashboardResponseList.add(orderDashboardResponse);
				}
			}

			
		}
		return orderDashboardResponseList;
	}

	private StagePlanBean mapToStagePlanBean(StagePlan stagePlan) {
		StagePlanBean stagePlanBean = new StagePlanBean();
		stagePlanBean.setActualEndTime(stagePlan.getActualEndTime());
		stagePlanBean.setActualStartTime(stagePlan.getActualStartTime());
		if (stagePlan.getMstStageDef().getCustomerView() != null)
			stagePlanBean.setCustomerView(stagePlan.getMstStageDef().getCustomerView().equals("Y") ? true : false);
		stagePlanBean.setEstimatedEndTime(stagePlan.getEstimatedEndTime());
		stagePlanBean.setEstimatedStartTime(stagePlan.getEstimatedStartTime());
		stagePlanBean.setPlannedEndTime(stagePlan.getPlannedEndTime());
		stagePlanBean.setPlannedStartTime(stagePlan.getPlannedStartTime());
		stagePlanBean.setStageName(stagePlan.getMstStageDef().getName());
		stagePlanBean.setStatus(stagePlan.getMstStatus().getCode());
		stagePlanBean.setIsDelayed(false);
		if (stagePlan.getEstimatedEndTime() != null
				&& !stagePlan.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)) {
			Integer difference = (new Date()).compareTo(stagePlan.getPlannedEndTime());
			if (difference > 0) {
				stagePlanBean.setIsDelayed(true);
			} else {
				stagePlanBean.setIsDelayed(false);
			}
		}
		stagePlanBean.setStagePlanId(stagePlan.getId());
		stagePlanBean.setTargetedEndTime(stagePlan.getTargettedEndTime());
		stagePlanBean.setTargetedStartTime(stagePlan.getTargettedStartTime());
		return stagePlanBean;
	}

	public List<String> getCustomerList(String customerName) {
		return scOrderRepository.findAllDistinctCustomerLeName(customerName);
	}

	/**
	 * Gets orderCode for given service Code.
	 *
	 * @param serviceCode
	 * @return
	 */
	public List<String> getOrderCode(String serviceCode) {

//		return Optional.ofNullable(scServiceDetailRepository.findFirstByUuidOrderByIdDesc(serviceCode))
//				.map(ScServiceDetail::getScOrderUuid)
//				.orElse("");

//		Changed return type to return valid response for MACD services 
		return scServiceDetailRepository.findByUuidAndOrderByIdDesc(serviceCode).stream()
				.flatMap(map -> map.entrySet().stream()).map(Map.Entry::getValue).collect(Collectors.toList());
	}

	public Map<String, Object> getCompleteServiceDetails(Integer serviceId, String orderCode, String serviceCode) {
		LOGGER.info("getCompleteServiceDetails method invoked for Service Id::{}", serviceId);
		Map<String, Object> completeServiceDetails = new HashMap<>();
		ScServiceDetail scServiceDetail = getServiceDetailsByOrderCodeAndServiceDeatils(orderCode, serviceCode,
				serviceId);
		if (scServiceDetail.getErfPrdCatalogProductName().equals("IZOSDWAN")
				|| scServiceDetail.getErfPrdCatalogProductName().equals("IZO SDWAN")) {
			LOGGER.info("IZOSDWAN Service Id::{}", serviceId);
			return getCompleteServiceDetailsForIzosdwan(serviceId, orderCode, serviceCode);
		}
		if (scServiceDetail != null) {
			LOGGER.info("getCompleteServiceDetails Service Id exists::{}", serviceId);
			ScOrder scOrder = scServiceDetail.getScOrder();
			String orderCategory = OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);
			String orderType = OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);
			completeServiceDetails.putAll(constructSeviceDetailMap(scServiceDetail));
			completeServiceDetails.put("status", scServiceDetail.getMstStatus().getCode());
			completeServiceDetails.put("isBundle", scServiceDetail.getIsBundle() == null ? "N" : "Y");
			completeServiceDetails.put("demoFlag", scOrder.getDemoFlag());
			completeServiceDetails
					.putAll(commonFulfillmentUtils.getServiceAttributesAttributes(scServiceAttributeRepository
							.findByScServiceDetail_idAndIsActiveAndAttributeValueIsNotNull(serviceId, "Y")));
			Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
					.getComponentAttributes(scServiceDetail.getId(), "LM", "A");
			scComponentAttributesAMap.put("klmDetailsSiteA", getKlmAndIorDetails(scServiceDetail, "klmDetails"));
			completeServiceDetails.putAll(scComponentAttributesAMap);
			try {
				if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("NPL")) {
					Map<String, String> scComponentAttributesBMap = commonFulfillmentUtils
							.getComponentAttributes(scServiceDetail.getId(), "LM", "B");
					scComponentAttributesBMap.put("klmDetailsSiteB",
							getKlmAndIorDetails(scServiceDetail, "klmDetails_b"));
					completeServiceDetails.put("siteBData", scComponentAttributesBMap);
				}
			} catch (Exception e) {
				LOGGER.error("error while getting siteBData:", e);
			}
			try {
				if (completeServiceDetails.containsKey("access_rings")) {
					Optional<ScAdditionalServiceParam> scAddParam = scAdditionalServiceParamRepository
							.findById(Integer.valueOf((String) completeServiceDetails.get("access_rings")));
					if (scAddParam.isPresent()) {

						completeServiceDetails.put("accessRings", scAddParam.get().getValue());
					}
				}
			} catch (Exception e) {
				LOGGER.error("error while getting access ring:{}", e);
			}

			// Add all attachments linked to service Id.
			List<AttachmentBean> attachmentBeans = new ArrayList<>();
			if (scServiceDetail.getErfPrdCatalogProductName().equals("IZOSDWAN_SOLUTION")) {
				List<String> categoryList = new ArrayList<>();
				categoryList.add("internal");
				categoryList.add("kickoff");
				categoryList.add("Migration Document");
				categoryList.add("Supporting Document");
				attachmentBeans.addAll(getScAttachmentsByCategoryList(serviceId, categoryList));
				attachmentBeans.addAll(getScAttachmentsByCategory(serviceId, "LLD Document"));
			} else {
				if (scServiceDetail.getOrderType() != null
						&& scServiceDetail.getOrderType().equalsIgnoreCase("Termination")) {
					attachmentBeans.addAll(getScAttachmentsWithoutOneCataogory(serviceId, "TRF"));
					attachmentBeans.addAll(getScAttachmentsByCategoryList(serviceId, Arrays.asList("TRF")));
				} else {
					attachmentBeans.addAll(getScAttachments(serviceId));
				}
			}

			completeServiceDetails.put("customerName", scOrder.getErfCustCustomerName());
			completeServiceDetails.put("customerLeName", scOrder.getErfCustLeName());
			completeServiceDetails.put("isMigratedOrder", scOrder.getIsMigratedOrder());
			completeServiceDetails.put("sfdcOpportunityId", scOrder.getTpsSfdcOptyId());
			completeServiceDetails.put("custCuId", scOrder.getTpsSfdcCuid());
			completeServiceDetails.put("customerCategory",
					null != scOrder.getCustomerSegment() ? scOrder.getCustomerSegment() : "Enterprise");
			completeServiceDetails.put("customerType", "Others");
			String orderSubCategory = scServiceDetail.getOrderSubCategory();
			if (orderType == null)
				orderType = "NEW";
			if (orderCategory == null)
				orderCategory = "NEW";
			if ("NEW".equalsIgnoreCase(orderType) && "NEW".equalsIgnoreCase(orderCategory)) {
				orderSubCategory = "NEW";
			}

			ScContractInfo scContractInfo = scContractInfoRepository.findFirstByScOrder_id(scOrder.getId());
			if (scContractInfo != null) {
				completeServiceDetails.put("billingMethod", scContractInfo.getBillingMethod());
				completeServiceDetails.put("cpeContractTerm", scContractInfo.getOrderTermInMonths());
				completeServiceDetails.put("billingPaymentTerm", scContractInfo.getPaymentTerm());
				completeServiceDetails.put("billingAccountManager", scContractInfo.getAccountManager());
				completeServiceDetails.put("billingFrequency", scContractInfo.getBillingFrequency());
				completeServiceDetails.put("billingAddress", scContractInfo.getBillingAddress());
			}
			completeServiceDetails.put("orderCategory", orderCategory);
			completeServiceDetails.put("orderSubCategory", orderSubCategory);
			completeServiceDetails.put("oldServiceId",
					Objects.nonNull(scServiceDetail.getParentUuid()) ? scServiceDetail.getParentUuid() : "");
			completeServiceDetails.put("orderType", orderType);
			completeServiceDetails.put("erfCustCustomerId", scOrder.getErfCustCustomerId());
			completeServiceDetails.put("erfCustLeId", scOrder.getErfCustLeId());
			completeServiceDetails.put("erfOrderId", scOrder.getErfOrderId());
			completeServiceDetails.put("erfOrderLeId", scOrder.getErfOrderLeId());
			ScOrderAttribute attribute = scOrderAttributeRepository
					.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_NUMBER, scOrder);
			if (attribute != null) {
				completeServiceDetails.put("customerPoNumber", attribute.getAttributeValue());
			}

			ScOrderAttribute billingTypeAttribute = scOrderAttributeRepository
					.findFirstByAttributeNameAndScOrder(LeAttributesConstants.BILLING_TYPE, scOrder);
			if (billingTypeAttribute != null) {
				completeServiceDetails.put("demoBillingType", billingTypeAttribute.getAttributeValue());
			}

			ScOrderAttribute sddAttchementIdAttribute = scOrderAttributeRepository
					.findFirstByAttributeNameAndScOrder(LeAttributesConstants.SDD, scOrder);
			if (sddAttchementIdAttribute != null) {
				completeServiceDetails.put("SDD", sddAttchementIdAttribute.getAttributeValue());
			}

			ScOrderAttribute sddAttchementAttribute = scOrderAttributeRepository
					.findFirstByAttributeNameAndScOrder(LeAttributesConstants.SDD_ATTACHMENT, scOrder);
			if (sddAttchementAttribute != null) {
				completeServiceDetails.put("sddAttachment", sddAttchementAttribute.getAttributeValue());
			}

			try {
				if (completeServiceDetails.containsKey("CPE Basic Chassis")) {
					if (!(StringUtils.isBlank((String) completeServiceDetails.get("CPE Basic Chassis")))) {
						Optional<ScAdditionalServiceParam> scAddParam = scAdditionalServiceParamRepository
								.findById(Integer.valueOf((String) completeServiceDetails.get("CPE Basic Chassis")));
						if (scAddParam.isPresent())
							completeServiceDetails.put("cpeBasicChassis", scAddParam.get().getValue());
					}
				}
			} catch (Exception e) {
				LOGGER.error("Error while getting CPE Basic Chassis:{}", e);
			}

			try {
				if (completeServiceDetails.containsKey("klmDetails")
						&& completeServiceDetails.get("klmDetails") != null) {
					Optional<ScAdditionalServiceParam> scAddParam = scAdditionalServiceParamRepository
							.findById(Integer.valueOf((String) completeServiceDetails.get("klmDetails")));
					if (scAddParam.isPresent()) {

						completeServiceDetails.put("klmDetails", scAddParam.get().getValue());
					}
				}
				if (completeServiceDetails.containsKey("klmDetails_b")
						&& completeServiceDetails.get("klmDetails_b") != null) {
					Optional<ScAdditionalServiceParam> scAddParam = scAdditionalServiceParamRepository
							.findById(Integer.valueOf((String) completeServiceDetails.get("klmDetails_b")));
					if (scAddParam.isPresent()) {

						completeServiceDetails.put("klmDetails_b", scAddParam.get().getValue());
					}
				}
			} catch (Exception e) {
				LOGGER.error("Error while getting klmDetails:{}", e);
			}

			if (completeServiceDetails.containsKey("downtime_duration")
					&& completeServiceDetails.containsKey("downtime_needed_ind")
					&& "yes".equalsIgnoreCase((String) completeServiceDetails.get("downtime_needed_ind")))
				completeServiceDetails.put("parallelDays", completeServiceDetails.get("downtime_duration"));

			List<ScServiceSla> slas = scServiceSlaRepository.findAllByScServiceDetail_Id(serviceId);
			if (!CollectionUtils.isEmpty(slas))
				slas.forEach(scServiceSla -> {
					if (Objects.nonNull(scServiceSla.getSlaComponent())) {
						if ("Round Trip Delay (RTD)".equalsIgnoreCase(scServiceSla.getSlaComponent()))
							completeServiceDetails.put("roundTripDelay", scServiceSla.getSlaValue());
						if ("Packet Drop".equalsIgnoreCase(scServiceSla.getSlaComponent()))
							completeServiceDetails.put("packetDrop", scServiceSla.getSlaValue());
						if (scServiceSla.getSlaComponent().equalsIgnoreCase("Network Uptime")
								|| scServiceSla.getSlaComponent().toLowerCase().contains("service availability"))
							completeServiceDetails.put("networkUptime", scServiceSla.getSlaValue());
						if (scServiceSla.getSlaComponent().toLowerCase().contains("jitter"))
							completeServiceDetails.put("jitter", scServiceSla.getSlaValue());
						if (scServiceSla.getSlaComponent().toLowerCase().contains("mean time"))
							completeServiceDetails.put("meanTimeToRestore", scServiceSla.getSlaValue());
						if (scServiceSla.getSlaComponent().toLowerCase().contains("packet delivery ratio"))
							completeServiceDetails.put("packetDeliveryRatio", scServiceSla.getSlaValue());
						if (scServiceSla.getSlaComponent().toLowerCase().contains("time to restore"))
							completeServiceDetails.put("timeToRestore", scServiceSla.getSlaValue());
					}
				});
			if (scServiceDetail.getIsBundle() != null && "Y".equalsIgnoreCase(scServiceDetail.getIsBundle())) {
				LOGGER.info("getCompleteServiceDetails isBundle exists for Service Id::{}", serviceId);
				getIzoSdwanDetails(serviceId, serviceCode, orderCode, completeServiceDetails);
			}
			ScSolutionComponent scSolutionComponent = scSolutionComponentRepository
					.findByScServiceDetail1(scServiceDetail);
			if (scSolutionComponent != null) {
				LOGGER.info("getCompleteServiceDetails scSolutionComponent exists for Service Id::{}", serviceId);
				if (scSolutionComponent.getScServiceDetail3() != null) {
					List<String> categoryList = new ArrayList<>();
					categoryList.add("Migration Document");
					categoryList.add("Supporting Document");
					attachmentBeans.addAll(getScAttachmentsByCategoryList(
							scSolutionComponent.getScServiceDetail3().getId(), categoryList));
					attachmentBeans.addAll(getScAttachmentsByCategory(scSolutionComponent.getScServiceDetail3().getId(),
							"LLD Document"));
				}
				completeServiceDetails.put("solutionParentServiceCode", scSolutionComponent.getParentServiceCode());
				if (scSolutionComponent.getScServiceDetail2() != null) {
					LOGGER.info("Parent service id {}", scSolutionComponent.getScServiceDetail2().getId());
					ScServiceAttribute scServiceAttribute = scServiceAttributeRepository
							.findByScServiceDetail_idAndAttributeName(scSolutionComponent.getScServiceDetail2().getId(),
									"serviceClassification");
					if (scServiceAttribute != null) {
						LOGGER.info("Service classification attribute present of name {} and value {}",
								scServiceAttribute.getAttributeName(), scServiceAttribute.getAttributeValue());
						completeServiceDetails.put("serviceClassification", scServiceAttribute.getAttributeValue());
					}
				}
				if (completeServiceDetails.containsKey("Shared CPE") && completeServiceDetails.get("Shared CPE") != null
						&& !completeServiceDetails.get("Shared CPE").equals("N")) {
					List<Integer> serviceIds = new ArrayList<>();
					List<String> cpeSharedServiceIds = new ArrayList<>();
					serviceIds.add(scServiceDetail.getId());
					List<ScSolutionComponent> scSolutionComponents = scSolutionComponentRepository
							.findByScServiceDetail1_IdNotInAndCpeComponentId(serviceIds,
									scSolutionComponent.getCpeComponentId());
					if (scSolutionComponents != null && !scSolutionComponents.isEmpty()) {
						cpeSharedServiceIds = scSolutionComponents.stream().map(sc -> sc.getServiceCode()).distinct()
								.collect(Collectors.toList());
					}
					completeServiceDetails.put("cpeSharedServiceId", cpeSharedServiceIds);
				}
			}
			completeServiceDetails.put("attachmentDetails", attachmentBeans);
			List<ScSolutionComponent> scSolutionComponents = scSolutionComponentRepository
					.findByParentServiceId(serviceId);
			Map<String, List<UnderlayBean>> productChildServiceIdMap = new HashMap<>();
			if (scSolutionComponents != null && !scSolutionComponents.isEmpty()) {
				List<String> products = scSolutionComponents.stream()
						.map(sc -> sc.getScServiceDetail1().getErfPrdCatalogProductName()).distinct()
						.collect(Collectors.toList());
				if (products != null && !products.isEmpty()) {
					products.stream().forEach(prod -> {
						List<ScSolutionComponent> scSolComp = scSolutionComponents.stream()
								.filter(sc -> sc.getScServiceDetail1().getErfPrdCatalogProductName().equals(prod))
								.collect(Collectors.toList());
						if (scSolComp != null && !scSolComp.isEmpty()) {
							List<UnderlayBean> underlayBeans = new ArrayList<>();
							scSolComp.stream().forEach(scSol -> {
								UnderlayBean underlayBean = new UnderlayBean();
								underlayBean.setServiceId(scSol.getScServiceDetail1().getId());
								underlayBean.setServiceCode(scSol.getServiceCode());
								underlayBeans.add(underlayBean);
							});
							productChildServiceIdMap.put(prod, underlayBeans);
						}
					});
				}
			}
			completeServiceDetails.put("childServiceIdDetails", productChildServiceIdMap);
			if ((scServiceDetail.getErfPrdCatalogProductName().equals("IZO Internet WAN")
					&& !scServiceDetail.getDestinationCountry().equalsIgnoreCase("India"))
					|| scServiceDetail.getErfPrdCatalogProductName().equals("DIA")
					|| (scServiceDetail.getErfPrdCatalogProductName().equals("GVPN")
							&& scServiceDetail.getDestinationCountry() != null
							&& !scServiceDetail.getDestinationCountry().equalsIgnoreCase("India"))) {
				LOGGER.info("IWAN or DIA or GVPN International with parent service id exists::{}",
						scSolutionComponent.getScServiceDetail2().getId());
				ScOrderAttribute vatNumberAttribute = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.Vat_Number, scOrder);
				if (vatNumberAttribute != null) {
					completeServiceDetails.put("vatNumber", vatNumberAttribute.getAttributeValue());
				}
				ScOrderAttribute crnIdAttribute = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.CRN_ID, scOrder);
				if (crnIdAttribute != null) {
					completeServiceDetails.put("customerCrnId", crnIdAttribute.getAttributeValue());
				}
				ScOrderAttribute billCurrencyAttribute = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.BILLING_CURRENCY, scOrder);
				if (billCurrencyAttribute != null) {
					completeServiceDetails.put("billCurrency", billCurrencyAttribute.getAttributeValue());
				}
				ScOrderAttribute customerPoDateAttribute = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_DATE, scOrder);
				if (customerPoDateAttribute != null) {
					completeServiceDetails.put("customerPoDate", customerPoDateAttribute.getAttributeValue());
				}
				ScOrderAttribute paymentCurrencyAttribute = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PAYMENT_CURRENCY, scOrder);
				if (paymentCurrencyAttribute != null) {
					completeServiceDetails.put("paymentCurrency", paymentCurrencyAttribute.getAttributeValue());
				}
				ScOrderAttribute paymentMethodAttribute = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.INVOICE_METHOD, scOrder);
				if (paymentMethodAttribute != null) {
					completeServiceDetails.put("billingInvoiceMethod", paymentMethodAttribute.getAttributeValue());
				}
				ScOrderAttribute billingContactNameAttribute = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.BILLING_CONTACT_NAME, scOrder);
				if (billingContactNameAttribute != null) {
					completeServiceDetails.put("billingContactName", billingContactNameAttribute.getAttributeValue());
				}
				ScOrderAttribute billingContactEmailAttribute = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.BILLING_CONTACT_EMAIL, scOrder);
				if (billingContactEmailAttribute != null) {
					completeServiceDetails.put("billingContactEmail", billingContactEmailAttribute.getAttributeValue());
				}
				ScOrderAttribute creditLimitAttribute = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder("Credit Limit", scOrder);
				if (creditLimitAttribute != null) {
					completeServiceDetails.put("creditLimit", creditLimitAttribute.getAttributeValue());
				}
				ScOrderAttribute leStateGstAttribute = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.LE_STATE_GST_NUMBER, scOrder);
				if (leStateGstAttribute != null) {
					completeServiceDetails.put("leStateGstNumber", leStateGstAttribute.getAttributeValue());
				}
				ScOrderAttribute customerSegmentAttribute = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder("CUSTOMER SEGMENT", scOrder);
				if (customerSegmentAttribute != null) {
					completeServiceDetails.put("customerSegment", customerSegmentAttribute.getAttributeValue());
				}
				completeServiceDetails.put("rrfsDate", scServiceDetail.getRrfsDate());
				completeServiceDetails.put("secsId", scOrder.getTpsSecsId());
				completeServiceDetails.put("spLeName", scOrder.getErfCustSpLeName());
				completeServiceDetails.put("opportunityClassification", scOrder.getOpportunityClassification());
				Optional<ScComponent> scComponentOptional = scComponentRepository
						.findById(scSolutionComponent.getCpeComponentId());
				if (scComponentOptional.isPresent()) {
					LOGGER.info("Component Exists with id::{}", scComponentOptional.get().getId());
					completeServiceDetails.put("overlayServiceId", scSolutionComponent.getScServiceDetail2().getUuid());
					CpeDeviceNameDetail cpeDeviceNameDetail = cpeDeviceNameDetailRepository
							.findByScServiceDetailAndScComponent(scSolutionComponent.getScServiceDetail2(),
									scComponentOptional.get());
					if (cpeDeviceNameDetail != null) {
						LOGGER.info("Cpe Device Name exists for underlay service id::{}",
								scSolutionComponent.getScServiceDetail1().getId());
						completeServiceDetails.put("cpeDeviceName", cpeDeviceNameDetail.getCpeDeviceName());
					}
				}
			}
		}

		return completeServiceDetails;
	}

	private void getIzoSdwanDetails(Integer serviceId, String serviceCode, String orderCode,
			Map<String, Object> completeServiceDetails) {
		LOGGER.info("getIzoSdwanDetails method invoked");
		List<Map<Object, Object>> scSolutionMapList = null;
		if (serviceCode.toLowerCase().contains("sdsol")) {
			LOGGER.info("Solution service Id as input");
			Map<Object, Object> solutionMap = new HashMap<>();
			solutionMap.put("solutionId", serviceId);
			solutionMap.put("solutionCode", serviceCode);
			solutionMap.put("orderCode", orderCode);
			scSolutionMapList = new ArrayList<>();
			scSolutionMapList.add(solutionMap);
		} else {
			LOGGER.info("Other than Solution service Id as input");
			scSolutionMapList = scSolutionComponentRepository.getSolutionDetails(serviceId, serviceCode, "Y");
		}
		if (scSolutionMapList != null && !scSolutionMapList.isEmpty()) {
			List<String> componentGroups = new ArrayList<>();
			componentGroups.add("OVERLAY");
			componentGroups.add("UNDERLAY");
			List<SolutionBean> solutionBeanList = new ArrayList<>();
			for (Map<Object, Object> solutionMap : scSolutionMapList) {
				LOGGER.info("Solution Id: {}", solutionMap.get("solutionId"));
				Integer solutionId = Integer.valueOf(String.valueOf(solutionMap.get("solutionId")));
				List<ScSolutionComponent> scSolutionComponentList = scSolutionComponentRepository
						.findAllByScServiceDetail3_idAndComponentGroupAndIsActive(solutionId, "OVERLAY", "Y");
				String solutionOrderCode = String.valueOf(solutionMap.get("orderCode"));
				LOGGER.info("solutionOrderCode: {}", solutionOrderCode);
				ScOrder scSolutionOrder = scOrderRepository.findByOpOrderCodeAndIsActive(solutionOrderCode, "Y");
				Set<ScServiceDetail> scServiceDetails = scSolutionOrder.getScServiceDetails();
				Map<Integer, ScServiceDetail> scServiceDetailMap = new HashMap<>();
				for (ScServiceDetail scServiceSolutionDetail : scServiceDetails) {
					scServiceDetailMap.put(scServiceSolutionDetail.getId(), scServiceSolutionDetail);
				}
				List<OverlayBean> overlayBeanList = new ArrayList<>();
				for (ScSolutionComponent scSolutionComponent : scSolutionComponentList) {
					LOGGER.info("Overlay Id: {}", scSolutionComponent.getScServiceDetail1().getId());
					List<Map<String, Integer>> layDetails = scSolutionComponentRepository
							.findScServiceDetailByComponentType(solutionOrderCode, componentGroups, "Y",
									scSolutionComponent.getScServiceDetail1().getId());
					if (layDetails != null && !layDetails.isEmpty()) {
						for (Map<String, Integer> overUnderlayMap : layDetails) {
							LOGGER.info("Underlay Ids: {}", overUnderlayMap.get("underlayIds"));
							LOGGER.info("Overlay Ids: {}", overUnderlayMap.get("overlayIds"));
							String underlays = String.valueOf(overUnderlayMap.get("underlayIds"));
							LOGGER.info("Underlay Ids{}", underlays);
							String[] underlayList = underlays.split(",");
							List<UnderlayBean> underlayBeanList = new ArrayList<>();
							for (String underlay : underlayList) {
								Integer underlayId = Integer.valueOf(underlay);
								if (scServiceDetailMap.containsKey(underlayId)) {
									ScServiceDetail underlayServiceDetail = scServiceDetailMap.get(underlayId);
									UnderlayBean underlayBean = new UnderlayBean();
									underlayBean.setOrderId(scSolutionOrder.getId());
									underlayBean.setOrderCode(scSolutionOrder.getOpOrderCode());
									underlayBean.setOrderType(underlayServiceDetail.getOrderType());
									underlayBean.setOrderCategory(underlayServiceDetail.getOrderCategory());
									underlayBean.setOrderSubCategory(underlayServiceDetail.getOrderSubCategory());
									underlayBean.setProductName(underlayServiceDetail.getErfPrdCatalogProductName());
									underlayBean.setServiceCode(underlayServiceDetail.getUuid());
									underlayBean.setServiceId(underlayServiceDetail.getId());
									underlayBean.setOfferingName(underlayServiceDetail.getErfPrdCatalogOfferingName());
									underlayBeanList.add(underlayBean);
								}
							}
							Integer overlayId = overUnderlayMap.get("overlayIds");
							LOGGER.info("OverlayId Ids: {}", overUnderlayMap.get("overlayIds"));
							if (scServiceDetailMap.containsKey(overlayId)) {
								ScServiceDetail overlayServiceDetail = scServiceDetailMap.get(overlayId);
								OverlayBean overlayBean = new OverlayBean();
								overlayBean.setOrderId(scSolutionOrder.getId());
								overlayBean.setOrderCode(scSolutionOrder.getOpOrderCode());
								overlayBean.setOrderType(overlayServiceDetail.getOrderType());
								overlayBean.setOrderCategory(overlayServiceDetail.getOrderCategory());
								overlayBean.setOrderSubCategory(overlayServiceDetail.getOrderSubCategory());
								overlayBean.setProductName(overlayServiceDetail.getErfPrdCatalogProductName());
								overlayBean.setServiceCode(overlayServiceDetail.getUuid());
								overlayBean.setServiceId(overlayServiceDetail.getId());
								overlayBean.setOfferingName(overlayServiceDetail.getErfPrdCatalogOfferingName());
								overlayBean.setUnderlayBeanList(underlayBeanList);
								overlayBean.setUnderlaySize(underlayBeanList.size());
								overlayBeanList.add(overlayBean);
							}

						}
					}
				}
				List<CGWDetailsBean> cgwDetailsBeans = new ArrayList<>();
				List<ScSolutionComponent> scSolutionComponentListCgw = scSolutionComponentRepository
						.findAllByScServiceDetail3_idAndComponentGroupAndIsActive(solutionId, "CGW", "Y");
				if (scSolutionComponentListCgw != null && !scSolutionComponentListCgw.isEmpty()) {
					scSolutionComponentListCgw.stream().forEach(scSolComp -> {
						CGWDetailsBean cgwDetailsBean = new CGWDetailsBean();
						cgwDetailsBean.setOrderCode(scSolComp.getOrderCode());
						cgwDetailsBean.setOrderId(scSolComp.getScOrder().getId());
						cgwDetailsBean.setPriSec(scSolComp.getScServiceDetail1().getPrimarySecondary());
						cgwDetailsBean.setServiceCode(scSolComp.getServiceCode());
						cgwDetailsBean.setServiceId(scSolComp.getScServiceDetail1().getId());
						cgwDetailsBean.setPriSecLinkedServiceId(scSolComp.getScServiceDetail1().getPriSecServiceLink());
						cgwDetailsBeans.add(cgwDetailsBean);
					});
				}
				ScServiceDetail scSolutionServiceDetail = scServiceDetailMap.get(solutionId);
				SolutionBean solutionBean = new SolutionBean();
				solutionBean.setOrderId(scSolutionOrder.getId());
				solutionBean.setOrderCode(scSolutionOrder.getOpOrderCode());
				solutionBean.setOrderType(scSolutionServiceDetail.getOrderType());
				solutionBean.setOrderCategory(scSolutionServiceDetail.getOrderCategory());
				solutionBean.setOrderSubCategory(scSolutionServiceDetail.getOrderSubCategory());
				solutionBean.setProductName(scSolutionServiceDetail.getErfPrdCatalogProductName());
				solutionBean.setServiceCode(scSolutionServiceDetail.getUuid());
				solutionBean.setServiceId(scSolutionServiceDetail.getId());
				solutionBean.setOfferingName(scSolutionServiceDetail.getErfPrdCatalogOfferingName());
				solutionBean.setOverlayBeanList(overlayBeanList);
				solutionBean.setOverlaySize(overlayBeanList.size());
				solutionBean.setCgwDetailList(cgwDetailsBeans);
				solutionBean.setSolutionServiceCode(String.valueOf(solutionMap.get("solutionCode")));
				solutionBean.setSolutionServiceId(solutionId);
				solutionBeanList.add(solutionBean);
			}
			completeServiceDetails.put("serviceBundle", solutionBeanList);
		}
	}

	private List<AttachmentBean> getScAttachments(Integer serviceId) {
		List<AttachmentBean> attachmentBeans = new ArrayList<>();

		List<ScAttachment> scAttachments = scAttachmentRepository.findAllByScServiceDetail_Id(serviceId);
		scAttachments = scAttachments.stream().filter(sc -> sc.getAttachment().getCategory() != null)
				.collect(Collectors.toList());
		Map<String, List<Attachment>> attachmentMap = scAttachments.stream().map(ScAttachment::getAttachment)
				.collect(Collectors.groupingBy(Attachment::getCategory));

		attachmentMap.keySet().forEach(category -> {
			attachmentBeans
					.add(attachmentMap.get(category).stream().sorted(Comparator.comparing(Attachment::getId).reversed())
							.findFirst().map(AttachmentBean::mapToBean).get());
		});
		return attachmentBeans;
	}

	private List<AttachmentBean> getScAttachmentsWithoutOneCataogory(Integer serviceId, String catagory) {
		List<AttachmentBean> attachmentBeans = new ArrayList<>();

		List<ScAttachment> scAttachments = scAttachmentRepository
				.findAllByScServiceDetail_IdAndAttachment_categoryNotIn(serviceId, Arrays.asList(catagory));
		scAttachments = scAttachments.stream().filter(sc -> sc.getAttachment().getCategory() != null)
				.collect(Collectors.toList());
		Map<String, List<Attachment>> attachmentMap = scAttachments.stream().map(ScAttachment::getAttachment)
				.collect(Collectors.groupingBy(Attachment::getCategory));

		attachmentMap.keySet().forEach(category -> {
			attachmentBeans
					.add(attachmentMap.get(category).stream().sorted(Comparator.comparing(Attachment::getId).reversed())
							.findFirst().map(AttachmentBean::mapToBean).get());
		});
		return attachmentBeans;
	}

	private List<AttachmentBean> getAllScAttachments(Integer serviceId) {
		List<AttachmentBean> attachmentBeans = new ArrayList<>();

		List<ScAttachment> scAttachments = scAttachmentRepository.findAllByScServiceDetail_Id(serviceId);
		scAttachments = scAttachments.stream().filter(sc -> sc.getAttachment().getCategory() != null)
				.collect(Collectors.toList());
		List<Attachment> attachmentList = scAttachments.stream().map(ScAttachment::getAttachment)
				.collect(Collectors.toList());
		LOGGER.info("getAllScAttachments size::{}, for  serviceId: {}", attachmentList.size(), serviceId);
		attachmentBeans.addAll(attachmentList.stream().map(AttachmentBean::mapToBean).collect(Collectors.toList()));
		return attachmentBeans;
	}

	private List<AttachmentBean> getScAttachmentsByCategoryList(Integer serviceId, List<String> categoryList) {
		List<AttachmentBean> attachmentBeans = new ArrayList<>();
		List<ScAttachment> scAttachments = scAttachmentRepository
				.findAllByScServiceDetail_IdAndAttachment_categoryIn(serviceId, categoryList);
		scAttachments = scAttachments.stream().filter(sc -> sc.getAttachment().getCategory() != null)
				.collect(Collectors.toList());
		List<Attachment> attachmentList = scAttachments.stream().map(ScAttachment::getAttachment)
				.collect(Collectors.toList());
		LOGGER.info("getScAttachmentsByCategoryList size::{}, for  serviceId: {}", attachmentList.size(), serviceId);
		attachmentBeans.addAll(attachmentList.stream().map(AttachmentBean::mapToBean).collect(Collectors.toList()));
		return attachmentBeans;
	}

	private List<AttachmentBean> getScAttachmentsByCategory(Integer serviceId, String category) {
		LOGGER.info("getScAttachmentsByCategory for  serviceId: {},category:{}", serviceId, category);
		List<AttachmentBean> attachmentBeans = new ArrayList<>();
		ScAttachment scAttachment = scAttachmentRepository
				.findFirstByScServiceDetail_IdAndAttachment_categoryOrderByIdDesc(serviceId, category);
		if (scAttachment != null) {
			Attachment attachment = scAttachment.getAttachment();
			if (attachment != null) {
				attachmentBeans.add(AttachmentBean.mapToBean(attachment));
			}
		}
		return attachmentBeans;
	}

	private Map<String, Object> constructSeviceDetailMap(ScServiceDetail scServiceDetail) {
		ScServiceDetailBean scServiceDetailBean = new ScServiceDetailBean();
		BeanUtils.copyProperties(scServiceDetail, scServiceDetailBean);
		scServiceDetailBean.setStatus(scServiceDetail.getMstStatus().getCode());
		scServiceDetailBean.setIsJeopardyTask(scServiceDetail.getIsJeopardyTask());
		scServiceDetailBean.setIsOrderAmendent(scServiceDetail.getIsAmended());
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>();
		try {
			map = objectMapper.readValue(Utils.convertObjectToJson(scServiceDetailBean), Map.class);
		} catch (IOException | TclCommonException e) {
			LOGGER.info("Exception Occurred while constructing service detail map : {}", e);
		}
		return map;
	}

	public StagePlanBean getActiveStageDetails(String serviceCode) {

		Stage stagePlan = stageRepository.findByServiceCodeAndMstStatus_code(serviceCode,
				TaskStatusConstants.INPROGRESS);

		StagePlanBean stagePlanBean = new StagePlanBean();

		stagePlanBean.setStatus(stagePlan.getMstStatus().getCode());
		stagePlanBean.setStageName(stagePlan.getMstStageDef().getName());
		stagePlanBean.setStageKey(stagePlan.getMstStageDef().getKey());

		if (stagePlan.getProcesses() != null && !stagePlan.getProcesses().isEmpty()) {
			List<com.tcl.dias.servicefulfillment.entity.entities.Process> processess = stagePlan.getProcesses().stream()
					.filter(proc -> proc.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.INPROGRESS))
					.collect(Collectors.toList());
			for (com.tcl.dias.servicefulfillment.entity.entities.Process process : processess) {
				ProcessPlanBean processBean = new ProcessPlanBean();
				processBean.setName(process.getMstProcessDef().getName());
				processBean.setProcessKey(process.getMstProcessDef().getKey());

				if (process.getActivities() != null && !process.getActivities().isEmpty()) {
					List<com.tcl.dias.servicefulfillment.entity.entities.Activity> activities = process.getActivities()
							.stream().filter(acti -> acti.getMstStatus().getCode()
									.equalsIgnoreCase(TaskStatusConstants.INPROGRESS))
							.collect(Collectors.toList());
					for (Activity activity : activities) {
						ActivityPlanBean activityPlanBean = new ActivityPlanBean();
						activityPlanBean.setName(activity.getMstActivityDef().getName());
						activityPlanBean.setActivityKey(activity.getMstActivityDef().getKey());

						processBean.getActivityPlans().add(activityPlanBean);

						if (activity.getTasks() != null && !activity.getTasks().isEmpty()) {
							List<Task> tasks = activity.getTasks().stream()
									.filter(tas -> tas.getMstStatus().getCode()
											.equalsIgnoreCase(TaskStatusConstants.INPROGRESS))
									.collect(Collectors.toList());
							for (Task task : tasks) {
								TaskPlanBean taskPlanBean = new TaskPlanBean();
								taskPlanBean.setName(task.getMstTaskDef().getName());
								taskPlanBean.setTaskKey(task.getMstTaskDef().getKey());

								activityPlanBean.getTaskPlans().add(taskPlanBean);

							}
						}
					}

				}
				stagePlanBean.getProcessPlans().add(processBean);
			}
		}

		return stagePlanBean;
	}

	@Transactional
	public List<ScServiceDetailBean> getInprogressServiceDetails(String orderCode, String[] serviceDetailAvailable) {

		List<ScServiceDetailBean> serviceDetailBeans = new ArrayList<>();
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
		if (scOrder != null) {
			LOGGER.info("L2O order code for fetching service details is ----> {} ", orderCode);
			List<ScServiceDetail> scServiceDetails = scOrder.getScServiceDetails().stream()
					.filter(ser -> ser.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.INPROGRESS)
							|| ser.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.HOLD)
							|| ser.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.TERMINATE)
							|| ser.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.CANCELLED))
					.collect(Collectors.toList());
			if(scServiceDetails == null || scServiceDetails.isEmpty()) {
				if(orderCode.startsWith("IAS") || orderCode.startsWith("GVPN")) {
					serviceDetailAvailable[0] = CommonConstants.NONE;
				}
			}
			if (scServiceDetails != null && !scServiceDetails.isEmpty()) {
				for (ScServiceDetail scServiceDetail : scServiceDetails) {
					LOGGER.info("L2O order code for fetching service details is -----> {} ", orderCode);
					com.tcl.dias.servicefulfillmentutils.beans.ScServiceDetailBean scServiceDetailBean = new com.tcl.dias.servicefulfillmentutils.beans.ScServiceDetailBean();
					scServiceDetailBean.setAccessType(scServiceDetail.getAccessType());
					scServiceDetailBean.setArc(scServiceDetail.getArc());
					scServiceDetailBean.setBillingAccountId(scServiceDetail.getBillingAccountId());
					scServiceDetailBean.setBillingGstNumber(scServiceDetail.getBillingGstNumber());
					scServiceDetailBean.setBillingRatioPercent(scServiceDetail.getBillingRatioPercent());
					scServiceDetailBean.setBillingType(scServiceDetail.getBillingType());
					scServiceDetailBean.setBwPortspeedAltName(scServiceDetail.getBwPortspeedAltName());
					scServiceDetailBean.setCallType(scServiceDetail.getCallType());
					scServiceDetailBean.setCreatedBy(scServiceDetail.getCreatedBy());
					scServiceDetailBean.setCreatedDate(scServiceDetail.getCreatedDate());
					scServiceDetailBean.setCustOrgNo(scServiceDetail.getCustOrgNo());
					scServiceDetailBean.setDestinationCountryCode(scServiceDetail.getDestinationCountryCode());
					scServiceDetailBean.setDestinationCountryCodeRepc(scServiceDetail.getDestinationCountryCodeRepc());
					scServiceDetailBean.setDiscountArc(scServiceDetail.getDiscountArc());
					scServiceDetailBean.setDiscountMrc(scServiceDetail.getDiscountMrc());
					scServiceDetailBean.setDiscountNrc(scServiceDetail.getDiscountNrc());
					scServiceDetailBean.setErfLocDestinationCityId(scServiceDetail.getErfLocDestinationCityId());
					scServiceDetailBean.setErfLocDestinationCountryId(scServiceDetail.getErfLocDestinationCountryId());
					scServiceDetailBean.setErfLocPopSiteAddressId(scServiceDetail.getErfLocPopSiteAddressId());
					scServiceDetailBean.setErfLocSiteAddressId(scServiceDetail.getErfLocSiteAddressId());
					scServiceDetailBean.setErfLocSourceCityId(scServiceDetail.getErfLocSourceCityId());
					scServiceDetailBean.setErfLocSrcCountryId(scServiceDetail.getErfLocSrcCountryId());
					scServiceDetailBean.setErfPrdCatalogOfferingId(scServiceDetail.getErfPrdCatalogOfferingId());
					scServiceDetailBean.setErfPrdCatalogOfferingName(scServiceDetail.getErfPrdCatalogOfferingName());
					scServiceDetailBean
							.setErfPrdCatalogParentProductName(scServiceDetail.getErfPrdCatalogParentProductName());
					scServiceDetailBean.setErfPrdCatalogParentProductOfferingName(
							scServiceDetail.getErfPrdCatalogParentProductOfferingName());
					scServiceDetailBean.setErfPrdCatalogProductId(scServiceDetail.getErfPrdCatalogProductId());
					scServiceDetailBean.setErfPrdCatalogProductName(scServiceDetail.getErfPrdCatalogProductName());
					scServiceDetailBean.setGscOrderSequenceId(scServiceDetail.getGscOrderSequenceId());
					scServiceDetailBean.setIsActive(scServiceDetail.getIsActive());
					scServiceDetailBean.setIsIzo(scServiceDetail.getIsIzo());
					scServiceDetailBean.setLocalItContactEmail(scServiceDetail.getLocalItContactEmail());
					scServiceDetailBean.setLocalItContactName(scServiceDetail.getLocalItContactName());
					scServiceDetailBean.setMrc(scServiceDetail.getMrc());
					scServiceDetailBean.setNrc(scServiceDetail.getNrc());
					scServiceDetailBean.setDifferentialMrc(scServiceDetail.getDifferentialMrc());
					scServiceDetailBean.setDifferentialNrc(scServiceDetail.getDifferentialNrc());
					scServiceDetailBean.setParentBundleServiceId(scServiceDetail.getParentBundleServiceId());
					scServiceDetailBean.setParentId(scServiceDetail.getParentId());
					scServiceDetailBean.setPrimarySecondary(scServiceDetail.getPrimarySecondary());
					scServiceDetailBean.setProductReferenceId(scServiceDetail.getProductReferenceId());
					scServiceDetailBean.setScOrderUuid(scServiceDetail.getScOrderUuid());
					scServiceDetailBean.setServiceClass(scServiceDetail.getServiceClass());
					scServiceDetailBean.setServiceClassification(scServiceDetail.getServiceClassification());
					scServiceDetailBean.setServiceCommissionedDate(scServiceDetail.getServiceCommissionedDate());
					scServiceDetailBean.setServiceGroupId(scServiceDetail.getServiceGroupId());
					scServiceDetailBean.setServiceGroupType(scServiceDetail.getServiceGroupType());
					scServiceDetailBean.setServiceStatus(scServiceDetail.getServiceStatus());
					scServiceDetailBean.setServiceTerminationDate(scServiceDetail.getServiceTerminationDate());
					scServiceDetailBean.setServiceTopology(scServiceDetail.getServiceTopology());
					scServiceDetailBean.setSiteAddress(scServiceDetail.getSiteAddress());
					scServiceDetailBean.setSiteAlias(scServiceDetail.getSiteAlias());
					scServiceDetailBean.setSiteEndInterface(scServiceDetail.getSiteEndInterface());
					scServiceDetailBean.setSiteLinkLabel(scServiceDetail.getSiteLinkLabel());
					scServiceDetailBean.setSiteTopology(scServiceDetail.getSiteTopology());
					scServiceDetailBean.setSlaTemplate(scServiceDetail.getSlaTemplate());
					scServiceDetailBean.setSmEmail(scServiceDetail.getSmEmail());
					scServiceDetailBean.setSmName(scServiceDetail.getSmName());
					scServiceDetailBean.setSourceCountryCodeRepc(scServiceDetail.getSourceCountryCodeRepc());
					scServiceDetailBean.setSupplOrgNo(scServiceDetail.getSupplOrgNo());
					scServiceDetailBean.setTpsCopfId(scServiceDetail.getTpsCopfId());
					scServiceDetailBean.setTpsServiceId(scServiceDetail.getTpsServiceId());
					scServiceDetailBean.setTpsSourceServiceId(scServiceDetail.getTpsSourceServiceId());
					scServiceDetailBean.setUpdatedBy(scServiceDetail.getUpdatedBy());
					scServiceDetailBean.setUpdatedDate(scServiceDetail.getUpdatedDate());
					scServiceDetailBean.setUuid(scServiceDetail.getUuid());
					scServiceDetailBean.setVpnName(scServiceDetail.getVpnName());
					scServiceDetailBean.setAssignedPM(scServiceDetail.getAssignedPM());
					scServiceDetailBean.setIsMultiVrf(scServiceDetail.getIsMultiVrf());
					scServiceDetailBean.setMasterVrfServiceId(scServiceDetail.getMasterVrfServiceCode());
					scServiceDetailBean.setMultiVrfSolution(scServiceDetail.getMultiVrfSolution());
					String orderType = OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);

					scServiceDetailBean.setStatus("IN-PROGRESS");
					scServiceDetailBean.setOrderType(orderType);
					scServiceDetailBean.setLastmileProvider(scServiceDetail.getLastmileScenario());
					scServiceDetailBean.setLastmileType(scServiceDetail.getLastmileConnectionType());
					ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
							.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
									scServiceDetail.getId(), "siteCode", "LM", "A");
					if (scComponentAttribute != null)
						scServiceDetailBean.setSiteCode(scComponentAttribute.getAttributeValue());
					getClrForAmendment(scServiceDetailBean);

					serviceDetailBeans.add(scServiceDetailBean);

				}
			}
		}
		LOGGER.info("Service detail beans size returned for order code ----> {} is -----> {} ", orderCode,
				serviceDetailBeans.size());
		return serviceDetailBeans;
	}

	private void getClrForAmendment(ScServiceDetailBean scServiceDetailBean) {
		scServiceDetailBean.setClrStageForAmendment("Yes");
		List<Task> task = taskRepository.findByServiceIdAndMstTaskDef_key(scServiceDetailBean.getId(),
				"enrich-service-design");
		task.stream().findAny().ifPresent(task1 -> {
			scServiceDetailBean.setClrStageForAmendment("No");
		});
	}

	public List<Map<String, String>> getLMScenarioTypeCount() {
		List<Map<String, String>> resp = scServiceDetailRepository.groupbytLMScenarioTypeCount();
		LOGGER.info("Groupby LM Scenario {}", resp);
		return resp;

	}

	@Transactional(readOnly = false)
	public AttributesBean getComponentAndServiceAttributes(String orderCode, String serviceId, Integer serviceDetailId,
			Integer taskId) {
		AttributesBean attributesBean = new AttributesBean();
		List<ComponentAttributesBean> componentAttributesA = new ArrayList<>();
		List<ComponentAttributesBean> componentAttributesB = new ArrayList<>();
		ScServiceDetailBean scServiceDetailBean = null;
		List<ServiceAttributesBean> serviceAttributesBeans = new ArrayList<>();
		ScServiceDetail scServiceDetail = null;
		ScOrder scOrder = null;
		TaskAdminBean taskAdminBean = null;
		List<ScOrderAttributesBean> scOrderAttributesBeans = new ArrayList<>();
		List<ScChargeLineItemBean> scChargeLineItemBeans=new ArrayList<>();

		if (Objects.isNull(taskId)) {
			if (Objects.nonNull(serviceDetailId)) {
				Optional<ScServiceDetail> scServiceDetailOptional = scServiceDetailRepository.findById(serviceDetailId);
				if (scServiceDetailOptional.isPresent()) {
					scServiceDetail = scServiceDetailOptional.get();
				}
			} else if (Objects.isNull(orderCode)) {
				// scServiceDetail =
				// scServiceDetailRepository.findByUuidAndMstStatus_code(serviceId,"INPROGRESS");
				scServiceDetail = scServiceDetailRepository
						.findFirstByUuidAndIsMigratedOrderAndIsActiveOrderByIdDesc(serviceId, "N", "Y");
				if (Objects.isNull(scServiceDetail))
					scServiceDetail = scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(serviceId,
							"ACTIVE");
			} else if (Objects.isNull(serviceId)) {
				scServiceDetail = scServiceDetailRepository.findFirstByScOrderUuidOrderByIdDesc(orderCode);
			}

			if (Objects.nonNull(scServiceDetail)) {
				LOGGER.info("ScServiceDetail exists for orderCode {} and serviceCode {} : {}", orderCode, serviceId,
						scServiceDetail.getId());

				// Populating Component Attributes for Site A
				List<ScComponentAttribute> scComponentAttributesA = scComponentAttributesRepository
						.findByScServiceDetailIdAndScComponent_siteType(scServiceDetail.getId(), "A");
				if (!CollectionUtils.isEmpty(scComponentAttributesA)) {
					LOGGER.info("Total Component Attributes for Site A : {}", scComponentAttributesA.size());
					scComponentAttributesA.forEach(attrA -> {
						componentAttributesA
								.add(new ComponentAttributesBean(attrA.getAttributeName(), attrA.getAttributeValue()));
					});
				}

				// Populating Component Attributes for Site B
				List<ScComponentAttribute> scComponentAttributesB = scComponentAttributesRepository
						.findByScServiceDetailIdAndScComponent_siteType(scServiceDetail.getId(), "B");
				if (!CollectionUtils.isEmpty(scComponentAttributesB)) {
					LOGGER.info("Total Component Attributes for Site B : {}", scComponentAttributesB.size());
					scComponentAttributesB.forEach(attrB -> {
						componentAttributesB
								.add(new ComponentAttributesBean(attrB.getAttributeName(), attrB.getAttributeValue()));
					});
				}

				// Populating Service Attributes
				List<ScServiceAttribute> scServiceAttributes = scServiceAttributeRepository
						.findByScServiceDetail_id(scServiceDetail.getId());
				if (!CollectionUtils.isEmpty(scServiceAttributes)) {
					LOGGER.info("Total Service Attributes for serviceId {} : {}", serviceId,
							scServiceAttributes.size());
					scServiceAttributes.forEach(attr -> {
						ServiceAttributesBean serviceAttributesBean = new ServiceAttributesBean();
						serviceAttributesBean.setName(attr.getAttributeName());
						serviceAttributesBean.setValue(attr.getAttributeValue());
						serviceAttributesBean.setCategory(attr.getCategory());
						serviceAttributesBeans.add(serviceAttributesBean);
					});
				}

				scServiceDetailBean = mapScServiceDetailEntityToBean(scServiceDetail);
			}
			if (Objects.nonNull(scServiceDetail)) {

				scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(scServiceDetail.getScOrderUuid(), "Y");
				if (Objects.nonNull(scOrder)) {
					attributesBean.setScOrderBean(ServiceFulfillmentMapper.mapEntityToOrderBean(scOrder));
					if(scOrder.getScContractInfos1()!=null && !scOrder.getScContractInfos1().isEmpty()) {
						attributesBean.setScContractInfoBean(ServiceFulfillmentMapper.mapScContractInfoBean(scOrder));
					}
					attributesBean.setScOrderAttributesBeans(
							ServiceFulfillmentMapper.mapScOrderAttributesBeans(scOrder, scOrderAttributesBeans));
				} else
					LOGGER.info("Scorder is not found" + orderCode);

				List<ScChargeLineitem> scChargeLineitems=scChargeLineitemRepository.findByServiceId(scServiceDetail.getId().toString());
				scChargeLineitems.stream().forEach(scChargeLineitem -> {
					scChargeLineItemBeans.add(mapScChargeLineItemEntityToBean(scChargeLineitem));

				});

			}
		} else {
			Optional<Task> task = taskRepository.findById(taskId);
			if (task.isPresent()) {
				taskAdminBean = ServiceFulfillmentMapper.mapTaskBean(task.get());
			}
		}
		attributesBean.setComponentAttributesA(componentAttributesA);
		attributesBean.setComponentAttributesB(componentAttributesB);
		attributesBean.setServiceAttributesBeans(serviceAttributesBeans);
		attributesBean.setScServiceDetailBean(scServiceDetailBean);
		attributesBean.setTaskAdminBean(taskAdminBean);
		attributesBean.setScChargeLineItemBeans(scChargeLineItemBeans);
		return attributesBean;
	}


	private ScChargeLineItemBean mapScChargeLineItemEntityToBean(ScChargeLineitem scChargeLineitem)
	{
		ScChargeLineItemBean scChargeLineItemBean=new ScChargeLineItemBean();
		scChargeLineItemBean.setId(scChargeLineitem.getId());
		scChargeLineItemBean.setAccountNumber(scChargeLineitem.getAccountNumber());
		scChargeLineItemBean.setChargeLineitem(scChargeLineitem.getChargeLineitem());
		scChargeLineItemBean.setCommissioningFlag(scChargeLineitem.getCommissioningFlag());
		scChargeLineItemBean.setServiceCode(scChargeLineitem.getServiceCode());
		scChargeLineItemBean.setServiceId(scChargeLineitem.getServiceId());
		return scChargeLineItemBean;

	}


	private ScServiceDetailBean mapScServiceDetailEntityToBean(ScServiceDetail scServiceDetail) {
		ScServiceDetailBean scServiceDetailBean = new ScServiceDetailBean();
		scServiceDetailBean.setAccessType(scServiceDetail.getAccessType());
		scServiceDetailBean.setArc(scServiceDetail.getArc());
		scServiceDetailBean.setBillingAccountId(scServiceDetail.getBillingAccountId());
		scServiceDetailBean.setBillingGstNumber(scServiceDetail.getBillingGstNumber());
		scServiceDetailBean.setBillingRatioPercent(scServiceDetail.getBillingRatioPercent());
		scServiceDetailBean.setBillingType(scServiceDetail.getBillingType());
		scServiceDetailBean.setBwPortspeedAltName(scServiceDetail.getBwPortspeedAltName());
		scServiceDetailBean.setBwUnit(scServiceDetail.getBwUnit());
		scServiceDetailBean.setDemarcationRoom(scServiceDetail.getDemarcationRoom());
		scServiceDetailBean.setDemarcationRack(scServiceDetail.getDemarcationRack());
		scServiceDetailBean.setDemarcationBuildingName(scServiceDetail.getDemarcationBuildingName());
		scServiceDetailBean.setDemarcationFloor(scServiceDetail.getDemarcationFloor());
		scServiceDetailBean.setCallType(scServiceDetail.getCallType());
		scServiceDetailBean.setCreatedBy(scServiceDetail.getCreatedBy());
		scServiceDetailBean.setCustOrgNo(scServiceDetail.getCustOrgNo());
		scServiceDetailBean.setDestinationCountryCode(scServiceDetail.getDestinationCountryCode());
		scServiceDetailBean.setDestinationCountryCodeRepc(scServiceDetail.getDestinationCountryCodeRepc());
		scServiceDetailBean.setErfLocDestinationCityId(scServiceDetail.getErfLocDestinationCityId());
		scServiceDetailBean.setErfLocDestinationCountryId(scServiceDetail.getErfLocDestinationCountryId());
		scServiceDetailBean.setDiscountArc(scServiceDetail.getDiscountArc());
		scServiceDetailBean.setDiscountMrc(scServiceDetail.getDiscountMrc());
		scServiceDetailBean.setDiscountNrc(scServiceDetail.getDiscountNrc());
		scServiceDetailBean.setErfLocSourceCityId(scServiceDetail.getErfLocSourceCityId());
		scServiceDetailBean.setErfLocSrcCountryId(scServiceDetail.getErfLocSrcCountryId());
		scServiceDetailBean.setErfPrdCatalogOfferingId(scServiceDetail.getErfPrdCatalogOfferingId());
		scServiceDetailBean.setErfPrdCatalogOfferingName(scServiceDetail.getErfPrdCatalogOfferingName());
		scServiceDetailBean.setErfPrdCatalogParentProductName(scServiceDetail.getErfPrdCatalogParentProductName());
		scServiceDetailBean
				.setErfPrdCatalogParentProductOfferingName(scServiceDetail.getErfPrdCatalogParentProductOfferingName());
		scServiceDetailBean.setErfPrdCatalogProductId(scServiceDetail.getErfPrdCatalogProductId());
		scServiceDetailBean.setErfPrdCatalogProductName(scServiceDetail.getErfPrdCatalogProductName());
		scServiceDetailBean.setGscOrderSequenceId(scServiceDetail.getGscOrderSequenceId());
		scServiceDetailBean.setId(scServiceDetail.getId());
		scServiceDetailBean.setIsActive(scServiceDetail.getIsActive());
		scServiceDetailBean.setIsIzo(scServiceDetail.getIsIzo());
		scServiceDetailBean.setMrc(scServiceDetail.getMrc());
		scServiceDetailBean.setNrc(scServiceDetail.getNrc());
		scServiceDetailBean.setDifferentialMrc(scServiceDetail.getDifferentialMrc());
		scServiceDetailBean.setDifferentialNrc(scServiceDetail.getDifferentialNrc());
		scServiceDetailBean.setParentBundleServiceId(scServiceDetail.getParentBundleServiceId());
		scServiceDetailBean.setParentId(scServiceDetail.getParentId());
		scServiceDetailBean.setProductReferenceId(scServiceDetail.getProductReferenceId());
		scServiceDetailBean.setScOrderUuid(scServiceDetail.getScOrderUuid());
		scServiceDetailBean.setServiceClass(scServiceDetail.getServiceClass());
		scServiceDetailBean.setServiceClassification(scServiceDetail.getServiceClassification());
		scServiceDetailBean.setServiceGroupId(scServiceDetail.getServiceGroupId());
		scServiceDetailBean.setServiceGroupType(scServiceDetail.getServiceGroupType());
		scServiceDetailBean.setServiceStatus(scServiceDetail.getServiceStatus());
		scServiceDetailBean.setServiceTopology(scServiceDetail.getServiceTopology());
		scServiceDetailBean.setSiteAddress(scServiceDetail.getSiteAddress());
		scServiceDetailBean.setSiteAlias(scServiceDetail.getSiteAlias());
		scServiceDetailBean.setSiteEndInterface(scServiceDetail.getSiteEndInterface());
		scServiceDetailBean.setSiteLinkLabel(scServiceDetail.getSiteLinkLabel());
		scServiceDetailBean.setSiteTopology(scServiceDetail.getSiteTopology());
		scServiceDetailBean.setSiteType(scServiceDetail.getSiteType());
		scServiceDetailBean.setSlaTemplate(scServiceDetail.getSlaTemplate());
		scServiceDetailBean.setSmEmail(scServiceDetail.getSmEmail());
		scServiceDetailBean.setSmName(scServiceDetail.getSmName());
		scServiceDetailBean.setSourceCity(scServiceDetail.getSourceCity());
		scServiceDetailBean.setSourceCountry(scServiceDetail.getSourceCountry());
		scServiceDetailBean.setSourceState(scServiceDetail.getSourceState());
		scServiceDetailBean.setDestinationState(scServiceDetail.getDestinationState());
		scServiceDetailBean.setSourceAddressLineOne(scServiceDetail.getSourceAddressLineOne());
		scServiceDetailBean.setSourceAddressLineTwo(scServiceDetail.getSourceAddressLineTwo());
		scServiceDetailBean.setSourceLocality(scServiceDetail.getSourceLocality());
		scServiceDetailBean.setSourcePincode(scServiceDetail.getSourcePincode());
		scServiceDetailBean.setDestinationAddressLineOne(scServiceDetail.getDestinationAddressLineOne());
		scServiceDetailBean.setDestinationAddressLineTwo(scServiceDetail.getDestinationAddressLineTwo());
		scServiceDetailBean.setDestinationLocality(scServiceDetail.getDestinationLocality());
		scServiceDetailBean.setDestinationPincode(scServiceDetail.getDestinationPincode());
		scServiceDetailBean.setSourceCountryCode(scServiceDetail.getSourceCountryCode());
		scServiceDetailBean.setSourceCountryCodeRepc(scServiceDetail.getSourceCountryCodeRepc());
		scServiceDetailBean.setOrderType(scServiceDetail.getOrderType());
		scServiceDetailBean.setSupplOrgNo(scServiceDetail.getSupplOrgNo());
		scServiceDetailBean.setTaxExemptionFlag(scServiceDetail.getTaxExemptionFlag());
		scServiceDetailBean.setTpsCopfId(scServiceDetail.getTpsCopfId());
		scServiceDetailBean.setTpsServiceId(scServiceDetail.getTpsServiceId());
		scServiceDetailBean.setTpsSourceServiceId(scServiceDetail.getTpsSourceServiceId());
		scServiceDetailBean.setUpdatedBy(scServiceDetail.getUpdatedBy());
		scServiceDetailBean.setUuid(scServiceDetail.getUuid());
		scServiceDetailBean.setPriority(scServiceDetail.getPriority());
		scServiceDetailBean.setVpnSolutionId(scServiceDetail.getVpnSolutionId());
		scServiceDetailBean.setBurstableBw(scServiceDetail.getBurstableBw());
		scServiceDetailBean.setBurstableBwUnit(scServiceDetail.getBurstableBwUnit());
		scServiceDetailBean.setLastmileScenario(scServiceDetail.getLastmileScenario());
		scServiceDetailBean.setLastmileConnectionType(scServiceDetail.getLastmileConnectionType());
		scServiceDetailBean.setLineRate(scServiceDetail.getLineRate());
		scServiceDetailBean.setSiteCode(scServiceDetail.getPopSiteCode());
		scServiceDetailBean.setAssignedPM(scServiceDetail.getAssignedPM());
		scServiceDetailBean.setVpnName(scServiceDetail.getVpnName());
		scServiceDetailBean.setOrderSubCategory(scServiceDetail.getOrderSubCategory());
		scServiceDetailBean.setStatus(scServiceDetail.getMstStatus().getCode());
		scServiceDetailBean.setIsMultiVrf(scServiceDetail.getIsMultiVrf());
		scServiceDetailBean.setMasterVrfServiceId(scServiceDetail.getMasterVrfServiceCode());
		scServiceDetailBean.setMultiVrfSolution(scServiceDetail.getMultiVrfSolution());
		scServiceDetailBean.setBwPortspeed(scServiceDetail.getBwPortspeed());
		scServiceDetailBean.setDestinationCity(scServiceDetail.getDestinationCity());
		scServiceDetailBean.setDestinationCountry(scServiceDetail.getDestinationCountry());
		scServiceDetailBean.setErfOdrServiceId(scServiceDetail.getErfOdrServiceId());
		scServiceDetailBean.setFeasibilityId(scServiceDetail.getFeasibilityId());
		scServiceDetailBean.setErfLocPopSiteAddressId(scServiceDetail.getErfLocPopSiteAddressId());
		scServiceDetailBean.setErfLocSiteAddressId(scServiceDetail.getErfLocSiteAddressId());
		scServiceDetailBean.setLastmileBw(scServiceDetail.getLastmileBw());
		scServiceDetailBean.setLastmileBwAltName(scServiceDetail.getLastmileBwAltName());
		scServiceDetailBean.setLastmileBwUnit(scServiceDetail.getLastmileBwUnit());
		scServiceDetailBean.setLastmileProvider(scServiceDetail.getLastmileProvider());
		scServiceDetailBean.setLastmileType(scServiceDetail.getLastmileType());
		scServiceDetailBean.setLatLong(scServiceDetail.getLatLong());
		scServiceDetailBean.setLocalItContactEmail(scServiceDetail.getLocalItContactEmail());
		scServiceDetailBean.setLocalItContactMobile(scServiceDetail.getLocalItContactMobile());
		scServiceDetailBean.setLocalItContactName(scServiceDetail.getLocalItContactName());
		scServiceDetailBean.setPopSiteAddress(scServiceDetail.getPopSiteAddress());
		scServiceDetailBean.setPopSiteCode(scServiceDetail.getPopSiteCode());
		scServiceDetailBean.setPriSecServiceLink(scServiceDetail.getPriSecServiceLink());
		scServiceDetailBean.setErdPriSecServiceLinkId(scServiceDetail.getErdPriSecServiceLinkId());
		scServiceDetailBean.setPrimarySecondary(scServiceDetail.getPrimarySecondary());
		scServiceDetailBean.setServiceOption(scServiceDetail.getServiceOption());
		scServiceDetailBean.setIsOrderAmendent(scServiceDetail.getIsAmended());
		scServiceDetailBean.setIsJeopardyTask(scServiceDetail.getIsJeopardyTask());
		scServiceDetailBean.setOrderCategory(scServiceDetail.getOrderCategory());
		scServiceDetailBean.setServiceLinkId(scServiceDetail.getServiceLinkId());
		scServiceDetailBean.setTigerOrderId(scServiceDetail.getTigerOrderId());
		scServiceDetailBean.setAdditionalIpPoolType(scServiceDetail.getAdditionalIpPoolType());
		scServiceDetailBean.setNoOfAdditionalIps(scServiceDetail.getNoOfAdditionalIps());
		scServiceDetailBean.setTerminationFlowTriggered(scServiceDetail.getTerminationFlowTriggered());
		scServiceDetailBean.setCancellationFlowTriggered(scServiceDetail.getCancellationFlowTriggered());
		scServiceDetailBean.setIsAmended(scServiceDetail.getIsAmended());
		scServiceDetailBean.setRequestForAmendment(scServiceDetail.getRequestForAmendment());
		scServiceDetailBean.setWorkFlowName(scServiceDetail.getWorkFlowName());
		scServiceDetailBean.setParentUuid(scServiceDetail.getParentUuid());
		return scServiceDetailBean;
	}

	@Transactional(readOnly = false)
	public void saveComponentAttributes(ComponentAttributes componentAttributes) {
		List<ComponentAttributesBean> componentAttributesBeansA = componentAttributes.getComponentAttributesA();
		List<ComponentAttributesBean> componentAttributesBeansB = componentAttributes.getComponentAttributesB();
		ScServiceDetail scServiceDetail = null;

		String serviceId = null, orderCode = null;
		Integer serviceDetailId = null;
		if (!CollectionUtils.isEmpty(componentAttributesBeansA)) {
			for (ComponentAttributesBean bean : componentAttributesBeansA) {
				if ("serviceId".equalsIgnoreCase(bean.getName())) {
					serviceId = bean.getValue();
					break;
				} else if ("orderCode".equalsIgnoreCase(bean.getName())) {
					orderCode = bean.getValue();
					break;
				} else if ("serviceDetailId".equalsIgnoreCase(bean.getName())) {
					serviceDetailId = Integer.parseInt(bean.getValue());
					break;
				}
			}
		}

		if (Objects.nonNull(serviceId)) {
			scServiceDetail = scServiceDetailRepository.findFirstByUuidAndScOrderUuidIsNotNullOrderByIdDesc(serviceId);
		} else if (Objects.nonNull(serviceDetailId)) {
			Optional<ScServiceDetail> scServiceDetailOptional = scServiceDetailRepository.findById(serviceDetailId);
			if (scServiceDetailOptional.isPresent()) {
				scServiceDetail = scServiceDetailOptional.get();
			}
		} else if (Objects.nonNull(orderCode)) {
			scServiceDetail = scServiceDetailRepository.findFirstByScOrderUuidOrderByIdDesc(orderCode);
		}

		if (Objects.nonNull(scServiceDetail)) {
			if (!CollectionUtils.isEmpty(componentAttributesBeansA)) {
				componentAttributesBeansA.removeIf(bean -> (bean.getName().equalsIgnoreCase("serviceId")
						|| bean.getName().equalsIgnoreCase("orderCode")));
				Map<String, String> attrMapA = new HashMap<>();
				componentAttributesBeansA.forEach(beanA -> {
					attrMapA.put(beanA.getName(), beanA.getValue());
				});
				componentAndAttributeService.updateAttributes(scServiceDetail.getId(), attrMapA, "LM", "A");
			}
			if (!CollectionUtils.isEmpty(componentAttributesBeansB)) {
				componentAttributesBeansB.removeIf(bean -> (bean.getName().equalsIgnoreCase("serviceId")
						|| bean.getName().equalsIgnoreCase("orderCode")));
				Map<String, String> attrMapB = new HashMap<>();
				componentAttributesBeansB.forEach(beanB -> {
					attrMapB.put(beanB.getName(), beanB.getValue());
				});
				componentAndAttributeService.updateAttributes(scServiceDetail.getId(), attrMapB, "LM", "B");
			}
		}
	}

	@Transactional(readOnly = false)
	public void saveServiceAttributes(List<ServiceAttributesBean> serviceAttributesBeans) {
		ScServiceDetail scServiceDetail = null;
		String serviceId = null, orderCode = null;
		Integer serviceDetailId = null;
		if (!CollectionUtils.isEmpty(serviceAttributesBeans)) {
			for (ServiceAttributesBean bean : serviceAttributesBeans) {
				if ("serviceId".equalsIgnoreCase(bean.getName())) {
					serviceId = bean.getValue();
					break;
				} else if ("orderCode".equalsIgnoreCase(bean.getName())) {
					orderCode = bean.getValue();
					break;
				} else if ("serviceDetailId".equalsIgnoreCase(bean.getName())) {
					serviceDetailId = Integer.parseInt(bean.getValue());
					break;
				}
			}
		}
		serviceAttributesBeans.removeIf(
				bean -> (bean.getName().equalsIgnoreCase("serviceId") || bean.getName().equalsIgnoreCase("orderCode")));
		Map<String, String> attrMap = new HashMap<>();
		Map<String, String> feasibilityAttrMap = new HashMap<>();
		Map<String, String> categoryMap = new HashMap<>();
		serviceAttributesBeans.forEach(beanA -> {
			attrMap.put(beanA.getName(), beanA.getValue());
			categoryMap.put(beanA.getName(), beanA.getCategory());
			if (beanA.getName().equals("closest_provider_bso_name") || beanA.getName().equals("solution_type"))
				feasibilityAttrMap.put(beanA.getName(), beanA.getValue());
		});
		if (Objects.nonNull(serviceId)) {
			scServiceDetail = scServiceDetailRepository.findFirstByUuidAndScOrderUuidIsNotNullOrderByIdDesc(serviceId);
		} else if (Objects.nonNull(serviceDetailId)) {
			Optional<ScServiceDetail> scServiceDetailOptional = scServiceDetailRepository.findById(serviceDetailId);
			if (scServiceDetailOptional.isPresent()) {
				scServiceDetail = scServiceDetailOptional.get();
			}
		} else if (Objects.nonNull(orderCode)) {
			scServiceDetail = scServiceDetailRepository.findFirstByScOrderUuidOrderByIdDesc(orderCode);
		}

		if (!CollectionUtils.isEmpty(serviceAttributesBeans) && Objects.nonNull(scServiceDetail)) {
			componentAndAttributeService.updateScServiceAttributes(scServiceDetail.getId(), attrMap, categoryMap, null);
			componentAndAttributeService.updateScServiceAttributes(scServiceDetail.getId(), feasibilityAttrMap, null,
					"FEASIBILITY");
		}
	}

	@Transactional(readOnly = false)
	public void saveScServiceDetails(ScServiceDetailBean scServiceDetailBean) {
		Integer scServiceDetailId = scServiceDetailBean.getId();
		Optional<ScServiceDetail> oScServiceDetail = scServiceDetailRepository.findById(scServiceDetailId);
		if (oScServiceDetail.isPresent()) {
			ScServiceDetail scServiceDetail = oScServiceDetail.get();
			scServiceDetail.setErfOdrServiceId(scServiceDetailBean.getErfOdrServiceId());
			scServiceDetail.setAccessType(scServiceDetailBean.getAccessType());
			scServiceDetail.setArc(scServiceDetailBean.getArc());
			scServiceDetail.setBillingAccountId(scServiceDetailBean.getBillingAccountId());
			scServiceDetail.setBillingGstNumber(scServiceDetailBean.getBillingGstNumber());
			scServiceDetail.setBillingRatioPercent(scServiceDetailBean.getBillingRatioPercent());
			scServiceDetail.setBillingType(scServiceDetailBean.getBillingType());
			scServiceDetail.setBwPortspeed(scServiceDetailBean.getBwPortspeed());
			scServiceDetail.setBwPortspeedAltName(scServiceDetailBean.getBwPortspeedAltName());
			scServiceDetail.setBwUnit(scServiceDetailBean.getBwUnit());
			scServiceDetail.setCallType(scServiceDetailBean.getCallType());
			scServiceDetail.setCreatedBy(scServiceDetailBean.getCreatedBy());
			scServiceDetail.setCustOrgNo(scServiceDetailBean.getCustOrgNo());
			scServiceDetail.setDemarcationFloor(scServiceDetailBean.getDemarcationFloor());
			scServiceDetail.setDemarcationRack(scServiceDetailBean.getDemarcationRack());
			scServiceDetail.setDemarcationRoom(scServiceDetailBean.getDemarcationRoom());
			scServiceDetail.setDestinationCity(scServiceDetailBean.getDestinationCity());
			scServiceDetail.setDestinationCountry(scServiceDetailBean.getDestinationCountry());
			scServiceDetail.setDestinationCountryCode(scServiceDetailBean.getDestinationCountryCode());
			scServiceDetail.setDestinationCountryCodeRepc(scServiceDetailBean.getDestinationCountryCodeRepc());
			scServiceDetail.setDiscountArc(scServiceDetailBean.getDiscountArc());
			scServiceDetail.setDiscountMrc(scServiceDetailBean.getDiscountMrc());
			scServiceDetail.setDiscountNrc(scServiceDetailBean.getDiscountNrc());
			scServiceDetail.setErfLocDestinationCityId(scServiceDetailBean.getErfLocDestinationCityId());
			scServiceDetail.setErfLocDestinationCountryId(scServiceDetailBean.getErfLocDestinationCountryId());
			scServiceDetail.setErfLocPopSiteAddressId(scServiceDetailBean.getErfLocPopSiteAddressId());
			scServiceDetail.setErfLocSiteAddressId(scServiceDetailBean.getErfLocSiteAddressId());
			scServiceDetail.setErfLocSourceCityId(scServiceDetailBean.getErfLocSourceCityId());
			scServiceDetail.setErfLocSrcCountryId(scServiceDetailBean.getErfLocSrcCountryId());
			scServiceDetail.setErfPrdCatalogOfferingId(scServiceDetailBean.getErfPrdCatalogOfferingId());
			scServiceDetail.setErfPrdCatalogOfferingName(scServiceDetailBean.getErfPrdCatalogOfferingName());
			scServiceDetail.setErfPrdCatalogParentProductName(scServiceDetailBean.getErfPrdCatalogParentProductName());
			scServiceDetail.setErfPrdCatalogParentProductOfferingName(
					scServiceDetailBean.getErfPrdCatalogParentProductOfferingName());
			scServiceDetail.setErfPrdCatalogProductId(scServiceDetailBean.getErfPrdCatalogProductId());
			scServiceDetail.setErfPrdCatalogProductName(scServiceDetailBean.getErfPrdCatalogProductName());
			scServiceDetail.setFeasibilityId(scServiceDetailBean.getFeasibilityId());
			scServiceDetail.setGscOrderSequenceId(scServiceDetailBean.getGscOrderSequenceId());
			scServiceDetail.setIsActive(scServiceDetailBean.getIsActive());
			scServiceDetail.setIsIzo(scServiceDetailBean.getIsIzo());
			scServiceDetail.setLastmileBw(scServiceDetailBean.getLastmileBw());
			scServiceDetail.setLastmileBwAltName(scServiceDetailBean.getLastmileBwAltName());
			scServiceDetail.setLastmileBwUnit(scServiceDetailBean.getLastmileBwUnit());
			scServiceDetail.setLastmileProvider(scServiceDetailBean.getLastmileProvider());
			scServiceDetail.setLastmileType(scServiceDetailBean.getLastmileType());
			scServiceDetail.setLatLong(scServiceDetailBean.getLatLong());
			scServiceDetail.setLocalItContactEmail(scServiceDetailBean.getLocalItContactEmail());
			scServiceDetail.setLocalItContactMobile(scServiceDetailBean.getLocalItContactMobile());
			scServiceDetail.setLocalItContactName(scServiceDetailBean.getLocalItContactName());
			scServiceDetail.setMrc(scServiceDetailBean.getMrc());
			scServiceDetail.setNrc(scServiceDetailBean.getNrc());
			scServiceDetail.setDifferentialMrc(scServiceDetailBean.getDifferentialMrc());
			scServiceDetail.setDifferentialNrc(scServiceDetailBean.getDifferentialNrc());
			scServiceDetail.setParentBundleServiceId(scServiceDetailBean.getParentBundleServiceId());
			scServiceDetail.setParentId(scServiceDetailBean.getParentId());
			scServiceDetail.setPopSiteAddress(scServiceDetailBean.getPopSiteAddress());
			scServiceDetail.setPopSiteCode(scServiceDetailBean.getPopSiteCode());
			scServiceDetail.setPriSecServiceLink(scServiceDetailBean.getPriSecServiceLink());
			scServiceDetail.setErdPriSecServiceLinkId(scServiceDetailBean.getErdPriSecServiceLinkId());
			scServiceDetail.setPrimarySecondary(scServiceDetailBean.getPrimarySecondary());
			scServiceDetail.setProductReferenceId(scServiceDetailBean.getProductReferenceId());
			scServiceDetail.setScOrderUuid(scServiceDetailBean.getScOrderUuid());
			scServiceDetail.setServiceClass(scServiceDetailBean.getServiceClass());
			scServiceDetail.setServiceClassification(scServiceDetailBean.getServiceClassification());
			scServiceDetail.setServiceGroupId(scServiceDetailBean.getServiceGroupId());
			scServiceDetail.setServiceGroupType(scServiceDetailBean.getServiceGroupType());
			scServiceDetail.setServiceOption(scServiceDetailBean.getServiceOption());
			scServiceDetail.setServiceStatus(scServiceDetailBean.getServiceStatus());
			scServiceDetail.setServiceTopology(scServiceDetailBean.getServiceTopology());
			scServiceDetail.setSiteAddress(scServiceDetailBean.getSiteAddress());
			scServiceDetail.setSiteAlias(scServiceDetailBean.getSiteAlias());
			scServiceDetail.setSiteEndInterface(scServiceDetailBean.getSiteEndInterface());
			scServiceDetail.setSiteLinkLabel(scServiceDetailBean.getSiteLinkLabel());
			scServiceDetail.setSiteTopology(scServiceDetailBean.getSiteTopology());
			scServiceDetail.setSiteType(scServiceDetailBean.getSiteType());
			scServiceDetail.setSlaTemplate(scServiceDetailBean.getSlaTemplate());
			scServiceDetail.setSmEmail(scServiceDetailBean.getSmEmail());
			scServiceDetail.setSmName(scServiceDetailBean.getSmName());
			scServiceDetail.setSourceCity(scServiceDetailBean.getSourceCity());
			scServiceDetail.setSourceCountry(scServiceDetailBean.getSourceCountry());
			scServiceDetail.setSourceCountryCode(scServiceDetailBean.getSourceCountryCode());
			scServiceDetail.setSourceCountryCodeRepc(scServiceDetailBean.getSourceCountryCodeRepc());
			scServiceDetail.setSupplOrgNo(scServiceDetailBean.getSupplOrgNo());
			scServiceDetail.setTaxExemptionFlag(scServiceDetailBean.getTaxExemptionFlag());
			scServiceDetail.setTpsCopfId(scServiceDetailBean.getTpsCopfId());
			scServiceDetail.setTpsServiceId(scServiceDetailBean.getTpsServiceId());
			scServiceDetail.setTpsSourceServiceId(scServiceDetailBean.getTpsSourceServiceId());
			scServiceDetail.setUpdatedBy(scServiceDetailBean.getUpdatedBy());
			scServiceDetail.setUuid(scServiceDetailBean.getUuid());
			scServiceDetail.setVpnName(scServiceDetailBean.getVpnName());
			scServiceDetail.setPriority(scServiceDetailBean.getPriority());
			scServiceDetail.setSourceAddressLineOne(scServiceDetailBean.getSourceAddressLineOne());
			scServiceDetail.setSourceAddressLineTwo(scServiceDetailBean.getSourceAddressLineTwo());
			scServiceDetail.setSourceLocality(scServiceDetailBean.getSourceLocality());
			scServiceDetail.setSourcePincode(scServiceDetailBean.getSourcePincode());
			scServiceDetail.setDestinationAddressLineOne(scServiceDetailBean.getDestinationAddressLineOne());
			scServiceDetail.setDestinationAddressLineTwo(scServiceDetailBean.getDestinationAddressLineTwo());
			scServiceDetail.setDestinationLocality(scServiceDetailBean.getDestinationLocality());
			scServiceDetail.setDestinationPincode(scServiceDetailBean.getDestinationPincode());
			scServiceDetail.setSourceState(scServiceDetailBean.getSourceState());
			scServiceDetail.setDestinationState(scServiceDetailBean.getDestinationState());
			scServiceDetail.setServiceVariant(scServiceDetailBean.getServiceVariant());
			scServiceDetail.setVpnSolutionId(scServiceDetailBean.getVpnSolutionId());
			scServiceDetail.setLineRate(scServiceDetailBean.getLineRate());
			scServiceDetail.setDemarcationBuildingName(scServiceDetailBean.getDemarcationBuildingName());
			scServiceDetail.setBurstableBw(scServiceDetailBean.getBurstableBw());
			scServiceDetail.setBurstableBwUnit(scServiceDetailBean.getBurstableBwUnit());
			scServiceDetail.setLastmileScenario(scServiceDetailBean.getLastmileScenario());
			scServiceDetail.setLastmileConnectionType(scServiceDetailBean.getLastmileConnectionType());
			scServiceDetail.setAssignedPM(scServiceDetailBean.getAssignedPM());
			scServiceDetail.setOrderSubCategory(scServiceDetailBean.getOrderSubCategory());
			scServiceDetail.setServiceLinkId(scServiceDetailBean.getServiceLinkId());
			scServiceDetail.setTigerOrderId(scServiceDetailBean.getTigerOrderId());
			scServiceDetail.setAdditionalIpPoolType(scServiceDetailBean.getAdditionalIpPoolType());
			scServiceDetail.setNoOfAdditionalIps(scServiceDetailBean.getNoOfAdditionalIps());
			scServiceDetail.setTerminationFlowTriggered(scServiceDetailBean.getTerminationFlowTriggered());
			scServiceDetail.setCancellationFlowTriggered(scServiceDetailBean.getCancellationFlowTriggered());
			scServiceDetail.setIsAmended(scServiceDetailBean.getIsAmended());
			scServiceDetail.setRequestForAmendment(scServiceDetailBean.getRequestForAmendment());
			scServiceDetail.setWorkFlowName(scServiceDetailBean.getWorkFlowName());
			scServiceDetail.setIsAmended(scServiceDetailBean.getIsAmended());
			scServiceDetail.setIsJeopardyTask(scServiceDetailBean.getIsJeopardyTask());
			// scServiceDetail.getMstStatus().setCode(scServiceDetailBean.getStatus());
			scServiceDetail.setMstStatus(taskCacheService.getMstStatus(scServiceDetailBean.getStatus()));
			scServiceDetail.setIsMultiVrf(scServiceDetailBean.getIsMultiVrf());
			scServiceDetail.setMasterVrfServiceCode(scServiceDetailBean.getMasterVrfServiceId());
			scServiceDetail.setMultiVrfSolution(scServiceDetailBean.getMultiVrfSolution());
			scServiceDetail.setPopSiteCode(scServiceDetailBean.getPopSiteCode());
			scServiceDetail.setOrderType(scServiceDetailBean.getOrderType());
			scServiceDetail.setOrderCategory(scServiceDetailBean.getOrderCategory());
			scServiceDetail.setParentUuid(scServiceDetailBean.getParentUuid());
			LOGGER.info("Servicedetailbeans Uuid:{} OrderType:{} OrderCategory:{}", scServiceDetailBean.getUuid(),
					scServiceDetailBean.getOrderType(), scServiceDetailBean.getOrderCategory());
			scServiceDetailRepository.save(scServiceDetail);

		}
	}

	@Transactional(readOnly = false)
	public DashboardAttributesBean getDashboardAttributes() {
		DashboardAttributesBean dashboardAttributesBean = new DashboardAttributesBean();
		List<ScServiceDetail> scServiceDetails = scServiceDetailRepository.findByMstStatus_codeInAndIsMigratedOrder(
				Arrays.asList(TaskStatusConstants.ACTIVE, TaskStatusConstants.INPROGRESS, TaskStatusConstants.INACTIVE),
				"N");

		List<ScServiceDetail> activeScServiceDetails = scServiceDetailRepository.getServiceConfiguredList();

		List<Integer> billedCircuitIds = new ArrayList<>();
		scServiceDetails.forEach(s -> billedCircuitIds.add(s.getId()));

		List<Integer> billedCircuits = taskRepository.findBilledCircuits(billedCircuitIds);
		List<ScServiceDetail> totalBilledScServiceDetails = new ArrayList<>();

		scServiceDetails.forEach(scServiceDetail -> {
			if (billedCircuits.contains(scServiceDetail.getId())) {
				totalBilledScServiceDetails.add(scServiceDetail);
			}
		});

		if (!CollectionUtils.isEmpty(scServiceDetails)) {

			Double totalMrc = (scServiceDetails.stream().filter(s -> s.getArc() != null)
					.mapToDouble(s -> new Double((s.getArc() / 12))).sum()) / 70;
			Double totalNrc = (scServiceDetails.stream().filter(s -> s.getNrc() != null)
					.mapToDouble(s -> new Double(s.getNrc())).sum()) / 70;

			Double totalActiveMrc = (activeScServiceDetails.stream().filter(s -> s.getArc() != null)
					.mapToDouble(s -> new Double((s.getArc() / 12))).sum()) / 70;
			Double totalActiveNrc = (activeScServiceDetails.stream().filter(s -> s.getNrc() != null)
					.mapToDouble(s -> new Double(s.getNrc())).sum()) / 70;

			dashboardAttributesBean.setTotalMrc(String.format("%.2f", totalMrc));
			dashboardAttributesBean.setTotalNrc(String.format("%.2f", totalNrc));

			Double totalDifferentialMrc = (scServiceDetails.stream()
					.filter(scServiceDetail -> scServiceDetail.getDifferentialMrc() != null
							&& scServiceDetail.getDifferentialMrc() > 0)
					.mapToDouble(scServiceDetail -> new Double(scServiceDetail.getDifferentialMrc())).sum()) / 70;
			Double totalDifferentialNrc = (scServiceDetails.stream()
					.filter(scServiceDetail -> scServiceDetail.getDifferentialNrc() != null
							&& scServiceDetail.getDifferentialNrc() > 0)
					.mapToDouble(scServiceDetail -> new Double(scServiceDetail.getDifferentialNrc())).sum()) / 70;
			dashboardAttributesBean.setTotalDifferentialMrc(String.format("%.2f", totalDifferentialMrc));
			dashboardAttributesBean.setTotalDifferentialNrc(String.format("%.2f", totalDifferentialNrc));

			if (!totalBilledScServiceDetails.isEmpty()) {
				Double totalBilledMrc = (totalBilledScServiceDetails.stream().filter(s -> s.getArc() != null)
						.mapToDouble(s -> new Double((s.getArc() / 12))).sum()) / 70;
				Double totalBilledNrc = (totalBilledScServiceDetails.stream().filter(s -> s.getNrc() != null)
						.mapToDouble(s -> new Double(s.getNrc())).sum()) / 70;
				dashboardAttributesBean.setTotalBilledMrc(String.format("%.2f", totalBilledMrc));
				dashboardAttributesBean.setTotalBilledNrc(String.format("%.2f", totalBilledNrc));

				Double totalBilledDifferentialMrc = (totalBilledScServiceDetails.stream()
						.filter(s -> s.getDifferentialMrc() != null && s.getDifferentialMrc() > 0)
						.mapToDouble(s -> new Double((s.getDifferentialMrc()))).sum()) / 70;
				Double totalBilledDifferentialNrc = (totalBilledScServiceDetails.stream()
						.filter(s -> s.getDifferentialNrc() != null && s.getDifferentialNrc() > 0)
						.mapToDouble(s -> new Double(s.getDifferentialNrc())).sum()) / 70;
				dashboardAttributesBean
						.setTotalBilledDifferentialMrc(String.format("%.2f", totalBilledDifferentialMrc));
				dashboardAttributesBean
						.setTotalBilledDifferentialNrc(String.format("%.2f", totalBilledDifferentialNrc));

			}

			dashboardAttributesBean.setTotalActiveMrc(String.format("%.2f", totalActiveMrc));
			dashboardAttributesBean.setTotalActiveNrc(String.format("%.2f", totalActiveNrc));

		}

		return dashboardAttributesBean;
	}

	public String getKlmAndIorDetails(ScServiceDetail scServiceDetail, String klmAttributeName) {
		String klmDetail = null;
		try {
			klmDetail = componentAndAttributeService.getAdditionalAttributes(scServiceDetail, klmAttributeName);
			LOGGER.info("KLM details " + klmAttributeName + " {} :", klmDetail);
			/*
			 * if (Objects.nonNull(klmDetails) && !klmDetails.isEmpty()) {
			 * klmAndIORDetailBean=Utils.convertJsonToObject(klmDetails,
			 * KlmAndIORDetailBean.class); }
			 */
		} catch (Exception ex) {
			LOGGER.error("error while getting klm details :", ex);
		}
		return klmDetail;
	}

	public Map<String, Object> getCompleteServiceDetailsForIzosdwan(Integer serviceId, String orderCode,
			String serviceCode) {
		Map<String, Object> completeServiceDetails = new HashMap<>();
		ScServiceDetail scServiceDetail = getServiceDetailsByOrderCodeAndServiceDeatils(orderCode, serviceCode,
				serviceId);
		if (scServiceDetail != null) {
			ScOrder scOrder = scServiceDetail.getScOrder();
			ScSolutionComponent scSolutionComponent = scSolutionComponentRepository
					.findByScServiceDetail1(scServiceDetail);
			List<AttachmentBean> attachmentBeans = new ArrayList<>();
			attachmentBeans.addAll(getScAttachments(serviceId));
			if (scSolutionComponent != null) {
				LOGGER.info("getCompleteServiceDetailsForIzosdwan scSolutionComponent exists for Service Id::{}",
						serviceId);
				if (scSolutionComponent.getScServiceDetail3() != null) {
					List<String> categoryList = new ArrayList<>();
					categoryList.add("Migration Document");
					categoryList.add("Supporting Document");
					attachmentBeans.addAll(getScAttachmentsByCategoryList(
							scSolutionComponent.getScServiceDetail3().getId(), categoryList));
					attachmentBeans.addAll(getScAttachmentsByCategory(scSolutionComponent.getScServiceDetail3().getId(),
							"LLD Document"));
				}
				completeServiceDetails.put("solutionParentServiceCode", scSolutionComponent.getParentServiceCode());
			}
			completeServiceDetails.put("attachmentDetails", attachmentBeans);
			completeServiceDetails.putAll(constructSeviceDetailMap(scServiceDetail));
			completeServiceDetails.put("status", scServiceDetail.getMstStatus().getCode());
			completeServiceDetails.put("isBundle", scServiceDetail.getIsBundle() == null ? "N" : "Y");
			completeServiceDetails.putAll(commonFulfillmentUtils
					.getServiceAttributesAttributesWithAdditionalParam(scServiceAttributeRepository
							.findByScServiceDetail_idAndIsActiveAndAttributeValueIsNotNull(serviceId, "Y")));
			completeServiceDetails.putAll(commonFulfillmentUtils.getComponentAttributesDetails(
					Arrays.asList("customerAcceptanceDate", "deemedAcceptance", "deemedAcceptanceApplicable",
							"billStartDate", "billFreePeriod", "commissioningDate"),
					scServiceDetail.getId(), "LM", "A"));
			List<ScComponent> scComponentList = scComponentRepository.findByScServiceDetailId(serviceId);
			List<Map<String, Object>> cpeList = new ArrayList<>();
			if (scComponentList != null && !scComponentList.isEmpty()) {

				scComponentList.stream().forEach(scComponent -> {
					Map<String, Object> scComponentAttributeMap = commonFulfillmentUtils
							.getComponentAttributesAttributesWithAdditionalParam(
									scComponentAttributesRepository.findByScComponent(scComponent));
					try {
						scComponentAttributeMap.put("klmDetailsSite".concat(scComponent.getSiteType()),
								getKlmAndIorDetails(scServiceDetail,
										scComponent.getSiteType().equals("A") ? "klmDetails"
												: "klmDetails_".concat(scComponent.getSiteType().toLowerCase())));
					} catch (Exception e) {
						LOGGER.error("Error on getting klm details ", e);
					}
					try {
						List<Map<String, Object>> underlayList = new ArrayList<>();
						List<ScSolutionComponent> scSolutionComponentList = scSolutionComponentRepository
								.findByCpeComponentId(scComponent.getId());
						if (scSolutionComponentList != null && !scSolutionComponentList.isEmpty()) {
							scSolutionComponentList.stream().forEach(solComp -> {
								Map<String, Object> map = new HashMap<>();
								map.put("underlayServiceId", solComp.getScServiceDetail1().getId());
								map.put("underlayServiceCode", solComp.getServiceCode());
								underlayList.add(map);
							});
						}
						scComponentAttributeMap.put("underlayList", underlayList);
					} catch (Exception e) {
						LOGGER.error("Error on mapping underlay details ", e);
					}
					scComponentAttributeMap.put("cpeComponentId", scComponent.getId());
					cpeList.add(scComponentAttributeMap);
				});

			}
			completeServiceDetails.put("cpeDetails", cpeList);
			// Add all attachments linked to service Id.

			completeServiceDetails.put("customerName", scOrder.getErfCustCustomerName());
			completeServiceDetails.put("customerLeName", scOrder.getErfCustLeName());
			completeServiceDetails.put("isMigratedOrder", scOrder.getIsMigratedOrder());
			completeServiceDetails.put("sfdcOpportunityId", scOrder.getTpsSfdcOptyId());
			completeServiceDetails.put("custCuId", scOrder.getTpsSfdcCuid());
			completeServiceDetails.put("customerCategory",
					null != scOrder.getCustomerSegment() ? scOrder.getCustomerSegment() : "Enterprise");
			completeServiceDetails.put("customerType", "Others");
			String orderCategory = scServiceDetail.getOrderCategory();
			String orderType = scServiceDetail.getOrderType();
			String orderSubCategory = scServiceDetail.getOrderSubCategory();
			if (orderType == null)
				orderType = "NEW";
			if (orderCategory == null)
				orderCategory = "NEW";
			if ("NEW".equalsIgnoreCase(orderType) && "NEW".equalsIgnoreCase(orderCategory)) {
				orderSubCategory = "NEW";
			}

			ScContractInfo scContractInfo = scContractInfoRepository.findFirstByScOrder_id(scOrder.getId());
			if (scContractInfo != null) {
				completeServiceDetails.put("billingMethod", scContractInfo.getBillingMethod());

			}
			completeServiceDetails.put("orderCategory", orderCategory);
			completeServiceDetails.put("orderSubCategory", orderSubCategory);
			completeServiceDetails.put("oldServiceId",
					Objects.nonNull(scServiceDetail.getParentUuid()) ? scServiceDetail.getParentUuid() : "");
			completeServiceDetails.put("orderType", orderType);
			completeServiceDetails.put("erfCustCustomerId", scOrder.getErfCustCustomerId());
			completeServiceDetails.put("erfCustLeId", scOrder.getErfCustLeId());
			completeServiceDetails.put("erfOrderId", scOrder.getErfOrderId());
			completeServiceDetails.put("erfOrderLeId", scOrder.getErfOrderLeId());
			ScOrderAttribute attribute = scOrderAttributeRepository
					.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_NUMBER, scOrder);
			if (attribute != null) {
				completeServiceDetails.put("customerPoNumber", attribute.getAttributeValue());
			}

			ScOrderAttribute sddAttchementIdAttribute = scOrderAttributeRepository
					.findFirstByAttributeNameAndScOrder(LeAttributesConstants.SDD, scOrder);
			if (sddAttchementIdAttribute != null) {
				completeServiceDetails.put("SDD", sddAttchementIdAttribute.getAttributeValue());
			}

			ScOrderAttribute sddAttchementAttribute = scOrderAttributeRepository
					.findFirstByAttributeNameAndScOrder(LeAttributesConstants.SDD_ATTACHMENT, scOrder);
			if (sddAttchementAttribute != null) {
				completeServiceDetails.put("sddAttachment", sddAttchementAttribute.getAttributeValue());
			}

			if (completeServiceDetails.containsKey("downtime_duration")
					&& completeServiceDetails.containsKey("downtime_needed_ind")
					&& "yes".equalsIgnoreCase((String) completeServiceDetails.get("downtime_needed_ind")))
				completeServiceDetails.put("parallelDays", completeServiceDetails.get("downtime_duration"));

			List<ScServiceSla> slas = scServiceSlaRepository.findAllByScServiceDetail_Id(serviceId);
			if (!CollectionUtils.isEmpty(slas))
				slas.forEach(scServiceSla -> {
					if (Objects.nonNull(scServiceSla.getSlaComponent())) {
						if ("Round Trip Delay (RTD)".equalsIgnoreCase(scServiceSla.getSlaComponent()))
							completeServiceDetails.put("roundTripDelay", scServiceSla.getSlaValue());
						if ("Packet Drop".equalsIgnoreCase(scServiceSla.getSlaComponent()))
							completeServiceDetails.put("packetDrop", scServiceSla.getSlaValue());
						if (scServiceSla.getSlaComponent().equalsIgnoreCase("Network Uptime")
								|| scServiceSla.getSlaComponent().toLowerCase().contains("service availability"))
							completeServiceDetails.put("networkUptime", scServiceSla.getSlaValue());
						if (scServiceSla.getSlaComponent().toLowerCase().contains("jitter"))
							completeServiceDetails.put("jitter", scServiceSla.getSlaValue());
						if (scServiceSla.getSlaComponent().toLowerCase().contains("mean time"))
							completeServiceDetails.put("meanTimeToRestore", scServiceSla.getSlaValue());
						if (scServiceSla.getSlaComponent().toLowerCase().contains("packet delivery ratio"))
							completeServiceDetails.put("packetDeliveryRatio", scServiceSla.getSlaValue());
						if (scServiceSla.getSlaComponent().toLowerCase().contains("time to restore"))
							completeServiceDetails.put("timeToRestore", scServiceSla.getSlaValue());
					}
				});
		}
		if (scServiceDetail.getIsBundle() != null && "Y".equalsIgnoreCase(scServiceDetail.getIsBundle())) {
			getIzoSdwanDetails(serviceId, serviceCode, orderCode, completeServiceDetails);
		}
		List<ScSolutionComponent> scSolutionComponents = scSolutionComponentRepository.findByParentServiceId(serviceId);
		Map<String, List<UnderlayBean>> productChildServiceIdMap = new HashMap<>();
		if (scSolutionComponents != null && !scSolutionComponents.isEmpty()) {
			List<String> products = scSolutionComponents.stream()
					.map(sc -> sc.getScServiceDetail1().getErfPrdCatalogProductName()).distinct()
					.collect(Collectors.toList());
			if (products != null && !products.isEmpty()) {
				products.stream().forEach(prod -> {
					List<ScSolutionComponent> scSolComp = scSolutionComponents.stream()
							.filter(sc -> sc.getScServiceDetail1().getErfPrdCatalogProductName().equals(prod))
							.collect(Collectors.toList());
					if (scSolComp != null && !scSolComp.isEmpty()) {
						List<UnderlayBean> underlayBeans = new ArrayList<>();
						scSolComp.stream().forEach(scSol -> {
							UnderlayBean underlayBean = new UnderlayBean();
							underlayBean.setServiceId(scSol.getScServiceDetail1().getId());
							underlayBean.setServiceCode(scSol.getServiceCode());
							underlayBeans.add(underlayBean);
						});
						productChildServiceIdMap.put(prod, underlayBeans);
					}
				});
			}
		}

		completeServiceDetails.put("childServiceIdDetails", productChildServiceIdMap);
		return completeServiceDetails;
	}

	@Transactional(readOnly = false)
	public List<SolutionViewDetailsBean> getSolutionDetailsServices(String solutionCode) {
		LOGGER.info("getSolutionDetailsServices method invoked:{}", solutionCode);
		SolutionViewDetailsBean solutionViewDetailsBean = new SolutionViewDetailsBean();
		List<SolutionViewDetailsBean> solutionViewDetailsList = new ArrayList<>();
		List<String> scOrderList = scSolutionComponentRepository
				.findDistinctOrderBySolutionCodeAndIsActive(solutionCode, "Y");
		if (scOrderList == null || scOrderList.isEmpty()) {
			return solutionViewDetailsList;
		}
		String orderCode = scOrderList.stream().findFirst().orElse(null);
		List<ScSolutionComponent> scSolutionComponentList = null;
		if (orderCode != null) {
			LOGGER.info("Order Code exists:{}", orderCode);
			if (orderCode.toLowerCase().contains("izosdwan")) {
				LOGGER.info("Izosdwan.Solution Code::{}, with Order Code:{}", solutionCode, orderCode);
				scSolutionComponentList = scSolutionComponentRepository
						.findAllBySolutionCodeAndIsActiveOrderByRRFSDate(solutionCode, "Y");
			} else {
				LOGGER.info("Solution Code::{}, with Order Code:{}", solutionCode, orderCode);
				scSolutionComponentList = scSolutionComponentRepository.findAllBySolutionCodeAndIsActive(solutionCode,
						"Y");
			}
		}
		List<ScOrder> scOrders = scOrderRepository.findByOpOrderCodeInAndIsActive(scOrderList, "Y");
		List<ScServiceDetail> scServiceDetailList = scServiceDetailRepository.findAllByScOrderUuidIn(scOrderList);
		List<Integer> serviceIds = new ArrayList<>();
		Map<String, ScOrder> scOrderMap = new HashMap<>();
		if (scOrders != null && !scOrders.isEmpty()) {
			LOGGER.info("List of order exists with size:{}", scOrders.size());
			for (ScOrder scOrder : scOrders) {
				scOrderMap.put(scOrder.getOpOrderCode(), scOrder);
			}
		}

		Map<String, ScServiceDetail> scServiceDetailMap = new HashMap<>();
		if (scServiceDetailList != null && !scServiceDetailList.isEmpty()) {
			LOGGER.info("List of Service Detail exists with size:{}", scServiceDetailList.size());
			for (ScServiceDetail scServiceDetail : scServiceDetailList) {
				scServiceDetailMap.put(scServiceDetail.getUuid(), scServiceDetail);
				serviceIds.add(scServiceDetail.getId());
			}
			LOGGER.info("List of serviceIds with size:{}", serviceIds.size());
		}

		Map<String, String> scSolutionComponentMap = new HashMap<>();
		if (scSolutionComponentList != null && !scSolutionComponentList.isEmpty()) {
			LOGGER.info("List of Solution Component exists with size:{}", scSolutionComponentList.size());
			for (ScSolutionComponent scSolutionComponent : scSolutionComponentList) {
				scSolutionComponentMap.put(scSolutionComponent.getScServiceDetail1().getUuid(),
						scSolutionComponent.getPriority());
			}
		}

		List<String> stages = new ArrayList<>();
		stages.add("order_enrichment_stage");
		stages.add("experience_survey_stage");
		List<StagePlan> stagePlanList = stagePlanRepository.findByServiceIdAndMstStageDefKey(serviceIds, stages);
		Map<String, StagePlan> stagePlanMap = new HashMap<>();
		if (stagePlanList != null && !stagePlanList.isEmpty()) {
			LOGGER.info("List of StagePlan exists with size:{}", stagePlanList.size());
			for (StagePlan stagePlan : stagePlanList) {
				stagePlanMap.put(stagePlan.getServiceId() + "_" + stagePlan.getMstStageDef().getKey(), stagePlan);
			}
		}

		List<OrderSolutionViewBean> orderSolutionViewList = new ArrayList<>();

		LOGGER.info("Solution Orders List orderList:{} and ServiceDetailList:{}", scOrderList, scServiceDetailList);
		for (String scOrderCode : scOrderList) {
			OrderSolutionViewBean orderSolutionViewBean = new OrderSolutionViewBean();
			orderSolutionViewBean = getOrderDetails(scSolutionComponentList, orderSolutionViewBean, scOrderMap,
					solutionCode, scOrderCode, scServiceDetailMap, stagePlanMap, scSolutionComponentMap);
			orderSolutionViewList.add(orderSolutionViewBean);
		}
		solutionViewDetailsBean.setOrders(orderSolutionViewList);
		if (orderCode != null && orderCode.toLowerCase().contains("izosdwan")) {
			LOGGER.info("Construct solutionDetails::{} for izosdwan order::{}", solutionCode, orderCode);
			ServiceSolutionViewBean solutionViewBean = new ServiceSolutionViewBean();
			solutionViewBean = getServiceSolutionDetails(solutionViewBean, stagePlanMap, scSolutionComponentMap,
					scServiceDetailMap.get(solutionCode));
			StagePlan stageEndDate = stagePlanMap
					.get(scServiceDetailMap.get(solutionCode).getId() + "_" + "order_enrichment_stage");
			if (stageEndDate != null) {
				LOGGER.info("Solution Code::{}, Stage Plan End Date::{} for izosdwan order::{}", solutionCode,
						stageEndDate.getPlannedEndTime(), orderCode);
				solutionViewBean.setEndDate(stageEndDate.getPlannedEndTime());
			}
			solutionViewDetailsBean.setSolutionDetail(solutionViewBean);
		}
		solutionViewDetailsList.add(solutionViewDetailsBean);

		return solutionViewDetailsList;
	}

	private OrderSolutionViewBean getOrderDetails(List<ScSolutionComponent> scSolutionComponentList,
			OrderSolutionViewBean orderSolutionViewBean, Map<String, ScOrder> scOrderMap, String solutionCode,
			String scOrderCode, Map<String, ScServiceDetail> scServiceDetailMap, Map<String, StagePlan> stagePlanMap,
			Map<String, String> scSolutionComponentMap) {
		LOGGER.info("getOrderDetails method invoked");
		ScOrder scOrder = scOrderMap.get(scOrderCode);
		orderSolutionViewBean.setOrderCode(scOrder.getUuid());
		orderSolutionViewBean.setOrderId(scOrder.getErfOrderId());
		orderSolutionViewBean.setOrderLeId(scOrder.getErfOrderLeId());
		LOGGER.info("Solution Component List component size:{} and ServiceDetailMap:{}", scSolutionComponentList.size(),
				scServiceDetailMap);
		List<ServiceSolutionViewBean> serviceSolutionViewList = new ArrayList<>();
		scSolutionComponentList.stream().filter(el -> el.getParentServiceCode().equalsIgnoreCase(solutionCode))
				.forEach(service -> {
					LOGGER.info("Parent Service Code:{} with Service Code::{}", solutionCode, service.getServiceCode());
					ServiceSolutionViewBean serviceSolutionViewBean = new ServiceSolutionViewBean();
					serviceSolutionViewBean = getServiceDetails(scSolutionComponentList, serviceSolutionViewBean,
							scServiceDetailMap, stagePlanMap, service.getServiceCode(), scSolutionComponentMap);
					serviceSolutionViewList.add(serviceSolutionViewBean);
				});
		orderSolutionViewBean.setServiceDetails(serviceSolutionViewList);
		return orderSolutionViewBean;
	}

	private ServiceSolutionViewBean getServiceDetails(List<ScSolutionComponent> scSolutionComponentList,
			ServiceSolutionViewBean serviceSolutionViewBean, Map<String, ScServiceDetail> scServiceDetailMap,
			Map<String, StagePlan> stagePlanMap, String serviceCode, Map<String, String> scSolutionComponentMap) {
		LOGGER.info("getServiceDetails method invoked with service detail size::{}", scServiceDetailMap.size());
		ScServiceDetail ssd = scServiceDetailMap.get(serviceCode);
		if (ssd != null) {
			LOGGER.info("Service Detail exists with Id::{},serviceCode::{}", ssd.getId(), ssd.getUuid());
			getServiceSolutionDetails(serviceSolutionViewBean, stagePlanMap, scSolutionComponentMap, ssd);
			List<ServiceSolutionViewBean> childServiceSolutionViewList = new ArrayList<>();
			/*
			 * for(ScSolutionComponent childService:scSolutionComponentList){
			 * LOGGER.info("Child.Parent Service Code:{} with Service Code::{}",
			 * childService.getParentServiceCode(),ssd.getUuid());
			 * if(childService.getParentServiceCode().equalsIgnoreCase(ssd.getUuid())){
			 * LOGGER.
			 * info("Child.Parent Service Code and Service Code matches for Service Code::{}"
			 * ,childService.getServiceCode()); ServiceSolutionViewBean
			 * childServiceSolutionViewBean = new ServiceSolutionViewBean(); ScServiceDetail
			 * childSsd = scServiceDetailMap.get(childService.getServiceCode()); if(childSsd
			 * !=null){ LOGGER.info("childSsd exists::{}",childSsd.getId());
			 * childServiceSolutionViewBean=getServiceSolutionDetails(
			 * childServiceSolutionViewBean,stagePlanMap,scSolutionComponentMap,childSsd);
			 * childServiceSolutionViewList.add(childServiceSolutionViewBean); } } }
			 */
			scSolutionComponentList.stream().filter(el -> el.getParentServiceCode().equalsIgnoreCase(ssd.getUuid()))
					.forEach(childService -> {
						LOGGER.info(
								"Child.Current Parent Service Code::{} and Parent Service Code::{} matches for Service Code::{}",
								childService.getParentServiceCode(), ssd.getUuid(), childService.getServiceCode());
						ServiceSolutionViewBean childServiceSolutionViewBean = new ServiceSolutionViewBean();
						ScServiceDetail childSsd = scServiceDetailMap.get(childService.getServiceCode());
						if (childSsd != null) {
							LOGGER.info("childSsd exists::{}", childSsd.getId());
							childServiceSolutionViewBean = getServiceSolutionDetails(childServiceSolutionViewBean,
									stagePlanMap, scSolutionComponentMap, childSsd);
							childServiceSolutionViewList.add(childServiceSolutionViewBean);
						}
					});
			serviceSolutionViewBean.setServiceDetails(childServiceSolutionViewList);
		}

		return serviceSolutionViewBean;

	}

	private ServiceSolutionViewBean getServiceSolutionDetails(ServiceSolutionViewBean serviceSolutionViewBean,
			Map<String, StagePlan> stagePlanMap, Map<String, String> scSolutionComponentMap,
			ScServiceDetail scServiceDetail) {
		LOGGER.info("getServiceSolutionDetails method invoked for Service Id::{}", scServiceDetail.getId());
		StagePlan stageStartDate = stagePlanMap.get(scServiceDetail.getId() + "_" + "order_enrichment_stage");
		StagePlan stageEndDate = stagePlanMap.get(scServiceDetail.getId() + "_" + "experience_survey_stage");
		serviceSolutionViewBean.setServiceId(scServiceDetail.getId());
		serviceSolutionViewBean.setServiceCode(scServiceDetail.getUuid());
		serviceSolutionViewBean.setProductName(scServiceDetail.getErfPrdCatalogProductName());
		serviceSolutionViewBean.setOfferingName(scServiceDetail.getErfPrdCatalogOfferingName());
		serviceSolutionViewBean.setRrfsDate(scServiceDetail.getRrfsDate());
		if (stageStartDate != null) {
			serviceSolutionViewBean.setStartDate(stageStartDate.getPlannedStartTime());
		}
		if (stageEndDate != null) {
			serviceSolutionViewBean.setEndDate(stageEndDate.getPlannedEndTime());
		}
		if (scSolutionComponentMap.containsKey(scServiceDetail.getUuid())
				&& scSolutionComponentMap.get(scServiceDetail.getUuid()) != null) {
			serviceSolutionViewBean.setPriority(scSolutionComponentMap.get(scServiceDetail.getUuid()));
		}
		return serviceSolutionViewBean;
	}

	@Transactional(readOnly = false)
	public void saveScOrders(ScOrderBean scOrderBean) {
		Integer scOrderId = scOrderBean.getId();
		Optional<ScOrder> scOrderObject = scOrderRepository.findById(scOrderId);
		if (scOrderObject.isPresent()) {
			ScOrder scOrder = scOrderObject.get();
			scOrder.setCreatedBy(scOrderBean.getCreatedBy());
			if (Objects.nonNull(scOrderBean.getCreatedDate()))
				scOrder.setCreatedDate(new Timestamp(scOrderBean.getCreatedDate().getTime()));
			scOrder.setCustomerGroupName(scOrderBean.getCustomerGroupName());
			scOrder.setCustomerSegment(scOrderBean.getCustomerSegment());
			scOrder.setDemoFlag(scOrderBean.getDemoFlag());
			scOrder.setErfCustCustomerId(scOrderBean.getErfCustCustomerId());
			scOrder.setErfCustCustomerName(scOrderBean.getErfCustCustomerName());
			scOrder.setErfCustLeId(scOrderBean.getErfCustLeId());
			scOrder.setErfCustLeName(scOrderBean.getErfCustLeName());
			scOrder.setErfCustPartnerId(scOrderBean.getErfCustPartnerId());
			scOrder.setErfCustPartnerName(scOrderBean.getErfCustPartnerName());
			scOrder.setErfCustPartnerLeId(scOrderBean.getErfCustPartnerLeId());
			scOrder.setPartnerCuid(scOrderBean.getPartnerCuid());
			scOrder.setErfCustSpLeId(scOrderBean.getErfCustSpLeId());
			scOrder.setErfCustSpLeName(scOrderBean.getErfCustSpLeName());
			scOrder.setErfUserCustomerUserId(scOrderBean.getErfUserCustomerUserId());
			scOrder.setErfUserInitiatorId(scOrderBean.getErfUserInitiatorId());
			scOrder.setUuid(scOrderBean.getUuid());
			if (Objects.nonNull(scOrderBean.getUpdatedDate()))
				scOrder.setUpdatedDate(new Timestamp(scOrderBean.getUpdatedDate().getTime()));
			scOrder.setUpdatedBy(scOrderBean.getUpdatedBy());
			scOrder.setTpsSfdcCuid(scOrderBean.getTpsSfdcCuid());
			scOrder.setTpsSecsId(scOrderBean.getTpsSecsId());
			scOrder.setTpsSapCrnId(scOrderBean.getTpsSapCrnId());
			scOrder.setTpsCrmSystem(scOrderBean.getTpsCrmSystem());
			scOrder.setTpsCrmOptyId(scOrderBean.getTpsCrmOptyId());
			scOrder.setTpsCrmCofId(scOrderBean.getTpsCrmCofId());
			scOrder.setTpsCrmOptyId(scOrderBean.getTpsCrmOptyId());
			scOrder.setErfOrderId(scOrderBean.getErfOrderId());
			scOrder.setSfdcAccountId(scOrderBean.getSfdcAccountId());
			scOrder.setParentOpOrderCode(scOrderBean.getParentOpOrderCode());
			scOrder.setTpsSfdcOptyId(scOrderBean.getSfdcOptyId());
			scOrder.setParentId(scOrderBean.getParentId());
			scOrder.setOrderType(scOrderBean.getOrderType());
			scOrder.setOrderStatus(scOrderBean.getOrderStatus());
			if (Objects.nonNull(scOrderBean.getOrderStartDate()))
				scOrder.setOrderStartDate(new Timestamp(scOrderBean.getOrderStartDate().getTime()));
			scOrder.setOrderSource(scOrderBean.getOrderSource());
			if (Objects.nonNull(scOrderBean.getOrderEndDate()))
				scOrder.setOrderEndDate(new Timestamp(scOrderBean.getOrderEndDate().getTime()));
			scOrder.setOrderCategory(scOrderBean.getOrderCategory());
			scOrder.setErfOrderLeId(scOrderBean.getErfOrderLeId());
			scOrder.setOpOrderCode(scOrderBean.getOpOrderCode());
			scOrder.setOpportunityClassification(scOrderBean.getOpportunityClassification());
			scOrder.setIsActive(scOrderBean.getIsActive());
			scOrder.setIsBundleOrder(scOrderBean.getIsBundleOrder());
			scOrder.setIsMultipleLe(scOrderBean.getIsMultipleLe());
			if (Objects.nonNull(scOrderBean.getLastMacdDate()))
				scOrder.setLastMacdDate(new Timestamp(scOrderBean.getLastMacdDate().getTime()));
			if (Objects.nonNull(scOrderBean.getMacdCreatedDate()))
				scOrder.setMacdCreatedDate(new Timestamp(scOrderBean.getMacdCreatedDate().getTime()));
			scOrderRepository.save(scOrder);
		}
	}

	@Transactional(readOnly = false)
	public void saveScContract(ScContractInfoBean scContractInfoBean) {
		Integer scContractInfoId = scContractInfoBean.getId();
		Optional<ScContractInfo> optionalScContractInfo = scContractInfoRepository.findById(scContractInfoId);
		if (optionalScContractInfo.isPresent()) {
			ScContractInfo scContractInfo = optionalScContractInfo.get();
			scContractInfo.setAccountManager(scContractInfoBean.getAccountManager());
			scContractInfo.setAccountManagerEmail(scContractInfoBean.getAccountManagerEmail());
			scContractInfo.setArc(scContractInfoBean.getArc());
			scContractInfo.setBillingAddress(scContractInfoBean.getBillingAddress());
			scContractInfo.setBillingFrequency(scContractInfoBean.getBillingFrequency());
			scContractInfo.setBillingMethod(scContractInfoBean.getBillingMethod());
			if (Objects.nonNull(scContractInfoBean.getContractEndDate()))
				scContractInfo.setContractEndDate(new Timestamp(scContractInfoBean.getContractEndDate().getTime()));
			if (Objects.nonNull(scContractInfoBean.getContractStartDate()))
				scContractInfo.setContractStartDate(new Timestamp(scContractInfoBean.getContractStartDate().getTime()));
			scContractInfo.setCreatedBy(scContractInfoBean.getCreatedBy());
			if (Objects.nonNull(scContractInfoBean.getCreatedDate()))
				scContractInfo.setCreatedDate(new Timestamp(scContractInfoBean.getCreatedDate().getTime()));
			scContractInfo.setCustomerContact(scContractInfoBean.getCustomerContact());
			scContractInfo.setCustomerContactEmail(scContractInfoBean.getCustomerContactEmail());
			scContractInfo.setDiscountArc(scContractInfoBean.getDiscountArc());
			scContractInfo.setDiscountMrc(scContractInfoBean.getDiscountMrc());
			scContractInfo.setDiscountNrc(scContractInfoBean.getDiscountNrc());
			scContractInfo.setErfCustCurrencyId(scContractInfoBean.getErfCustCurrencyId());
			scContractInfo.setErfCustLeId(scContractInfoBean.getErfCustLeId());
			scContractInfo.setErfCustLeName(scContractInfoBean.getErfCustLeName());
			scContractInfo.setErfCustSpLeId(scContractInfoBean.getErfCustSpLeId());
			scContractInfo.setErfCustSpLeName(scContractInfoBean.getErfCustSpLeName());
			scContractInfo.setErfLocBillingLocationId(scContractInfoBean.getErfLocBillingLocationId());
			scContractInfo.setIsActive(scContractInfoBean.getIsActive());
			scContractInfo.setLastMacdDate(scContractInfoBean.getLastMacdDate());
			scContractInfo.setMrc(scContractInfoBean.getMrc());
			scContractInfo.setNrc(scContractInfoBean.getNrc());
			scContractInfo.setBillingAddressLine1(scContractInfoBean.getBillingAddressLine1());
			scContractInfo.setBillingAddressLine2(scContractInfoBean.getBillingAddressLine2());
			scContractInfo.setBillingAddressLine3(scContractInfoBean.getBillingAddressLine3());
			scContractInfo.setBillingCity(scContractInfoBean.getBillingCity());
			scContractInfo.setBillingCountry(scContractInfoBean.getBillingCountry());
			scContractInfo.setBillingCity(scContractInfoBean.getBillingCity());
			scContractInfo.setBillingState(scContractInfoBean.getBillingState());
			scContractInfo.setBillingPincode(scContractInfoBean.getBillingPincode());
			scContractInfo.setOrderTermInMonths(scContractInfoBean.getOrderTermInMonths());
			scContractInfo.setPaymentTerm(scContractInfoBean.getPaymentTerm());
			scContractInfo.setTpsSfdcCuid(scContractInfoBean.getTpsSfdcCuid());
			scContractInfo.setUpdatedBy(scContractInfoBean.getUpdatedBy());
			if (Objects.nonNull(scContractInfoBean.getUpdatedDate()))
				scContractInfo.setUpdatedDate(new Timestamp(scContractInfoBean.getUpdatedDate().getTime()));
			scContractInfo.setBillingContactId(scContractInfoBean.getBillingContactId());
			scContractInfoRepository.save(scContractInfo);
		}
	}

	@Transactional(readOnly = false)
	public void saveServiceOrderAttributes(List<ScOrderAttributesBean> scOrderAttributesBeans) {
		ScServiceDetail scServiceDetail = null;
		String serviceId = null, orderCode = null;
		Integer serviceDetailId = null;
		if (!CollectionUtils.isEmpty(scOrderAttributesBeans)) {
			for (ScOrderAttributesBean bean : scOrderAttributesBeans) {
				if ("serviceId".equalsIgnoreCase(bean.getName())) {
					serviceId = bean.getValue();
					break;
				} else if ("orderCode".equalsIgnoreCase(bean.getName())) {
					orderCode = bean.getValue();
					break;
				} else if ("serviceDetailId".equalsIgnoreCase(bean.getName())) {
					serviceDetailId = Integer.parseInt(bean.getValue());
					break;
				}
			}
		}
		scOrderAttributesBeans.removeIf(
				bean -> (bean.getName().equalsIgnoreCase("serviceId") || bean.getName().equalsIgnoreCase("orderCode")));
		Map<String, String> attrMap = new HashMap<>();
		scOrderAttributesBeans.forEach(beanA -> {
			attrMap.put(beanA.getName(), beanA.getValue());
		});
		if (Objects.nonNull(serviceId)) {
			scServiceDetail = scServiceDetailRepository.findFirstByUuidAndScOrderUuidIsNotNullOrderByIdDesc(serviceId);
		} else if (Objects.nonNull(serviceDetailId)) {
			Optional<ScServiceDetail> scServiceDetailOptional = scServiceDetailRepository.findById(serviceDetailId);
			if (scServiceDetailOptional.isPresent()) {
				scServiceDetail = scServiceDetailOptional.get();
			}
		} else if (Objects.nonNull(orderCode)) {
			scServiceDetail = scServiceDetailRepository.findFirstByScOrderUuidOrderByIdDesc(orderCode);
		}
		if (!CollectionUtils.isEmpty(scOrderAttributesBeans) && Objects.nonNull(scServiceDetail)) {
			componentAndAttributeService.updateServiceOrderAttributes(scServiceDetail.getId(), attrMap);

		}
	}

	@Transactional(readOnly = false)
	public void saveTaskData(TaskAdminBean taskAdminBean) {
		Integer taskId = taskAdminBean.getTaskId();
		Optional<Task> taskDetail = taskRepository.findById(taskId);
		if (taskDetail.isPresent()) {
			Task task = taskDetail.get();
			// task.getMstStatus().setCode(taskAdminBean.getStatus());
			task.setMstStatus(taskCacheService.getMstStatus(taskAdminBean.getStatus()));
			task.setAssignee(taskAdminBean.getAssignee());
			task.setCatagory(taskAdminBean.getCategory());
			task.setCity(taskAdminBean.getCity());
			task.setCountry(taskAdminBean.getCountry());
			task.setCustomerName(taskAdminBean.getCustomerName());
			task.setDevicePlatform(taskAdminBean.getDevicePlatform());
			task.setDeviceType(taskAdminBean.getDeviceType());
			task.setDistributionCenterName(taskAdminBean.getDistributionCenterName());
			task.setDowntime(taskAdminBean.getDowntime());
			task.setGscFlowGroupId(taskAdminBean.getGscFlowGroupId());
			task.setIsIpDownTimeRequired(taskAdminBean.getIsIpDownTimeRequired());
			task.setIsJeopardyTask(taskAdminBean.getIsJeopardyTask());
			task.setIsTxDowntimeReqd(taskAdminBean.getIsTxDowntimeReqd());
			task.setLastMileScenario(taskAdminBean.getLastMileScenario());
			task.setLatitude(taskAdminBean.getLatitude());
			task.setLmProvider(taskAdminBean.getLmProvider());
			task.setLmType(taskAdminBean.getLmType());
			task.setLongitude(taskAdminBean.getLongitude());
			task.setOrderCategory(taskAdminBean.getOrderCategory());
			task.setOrderCode(taskAdminBean.getOrderCode());
			task.setOrderSubCategory(taskAdminBean.getOrderSubCategory());
			task.setOrderType(taskAdminBean.getOrderType());
			task.setPriority(taskAdminBean.getPriority());
			task.setProcessId(taskAdminBean.getProcessId());
			task.setQuoteCode(taskAdminBean.getQuoteCode());
			task.setQuoteId(taskAdminBean.getQuoteId());
			task.setScOrderId(taskAdminBean.getScOrderId());
			task.setServiceCode(taskAdminBean.getServiceCode());
			task.setServiceId(taskAdminBean.getServiceId());
			task.setServiceType(taskAdminBean.getServiceType());
			task.setSiteType(taskAdminBean.getSiteType());
			task.setState(taskAdminBean.getState());
			task.setTaskClosureCategory(taskAdminBean.getTaskClosureCategory());
			task.setVendorCode(taskAdminBean.getVendorCode());
			task.setVendorName(taskAdminBean.getVendorName());
			task.setWfCaseInstId(taskAdminBean.getWfCaseInstId());
			task.setWfExecutorId(taskAdminBean.getWfExecutorId());
			task.setWfPlanItemInstId(taskAdminBean.getWfPlanItemInstId());
			task.setWfProcessInstId(taskAdminBean.getWfProcessInstId());
			task.setWfTaskId(taskAdminBean.getWfTaskId());
			taskRepository.save(task);

		}
	}


	@Transactional(readOnly = false)
	public ScChargeLineitem saveScChargeLineItemBean(ScChargeLineItemBean scChargeLineItemBean){
		ScChargeLineitem scChargeLineitem=null;
		Integer scChargeLineItemBeanId = scChargeLineItemBean.getId();
		Optional<ScChargeLineitem> optionalScChargeLineitem = scChargeLineitemRepository.findById(scChargeLineItemBeanId);
		if(optionalScChargeLineitem.isPresent()){
			scChargeLineitem = optionalScChargeLineitem.get();
			scChargeLineitem.setAccountNumber(scChargeLineItemBean.getAccountNumber());
			scChargeLineitem.setChargeLineitem(scChargeLineItemBean.getChargeLineitem());
			scChargeLineitem.setCommissioningFlag(scChargeLineItemBean.getCommissioningFlag());
			scChargeLineitem.setServiceCode(scChargeLineItemBean.getServiceCode());
			scChargeLineitem.setServiceId(scChargeLineItemBean.getServiceId());
		}
		else
		{
			LOGGER.info("ScChargeLineItem is not found for id"+scChargeLineItemBeanId);
		}
		return scChargeLineitem;
	}

	@Transactional(readOnly = false)
	public void saveScChargeLineItems(List<ScChargeLineItemBean> scChargeLineItemBeans){
		List<ScChargeLineitem> scChargeLineItemList=new ArrayList<>();
		scChargeLineItemBeans.stream().forEach(scChargeLineItemBean -> {
			try {
				ScChargeLineitem scChargeLineitem=saveScChargeLineItemBean(scChargeLineItemBean);
				if(Objects.nonNull(scChargeLineitem))
					scChargeLineItemList.add(scChargeLineitem);
			}
			catch(Exception e)
			{
				LOGGER.info("Problem in updating scChargeLineItem");
			}

		});
		if(!scChargeLineItemList.isEmpty())
			scChargeLineitemRepository.saveAll(scChargeLineItemList);
	}

}
