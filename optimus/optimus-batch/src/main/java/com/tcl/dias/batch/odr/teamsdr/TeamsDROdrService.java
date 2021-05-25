package com.tcl.dias.batch.odr.teamsdr;

import com.tcl.dias.batch.odr.base.service.OrderService;
import com.tcl.dias.batch.odr.constants.OdrConstants;
import com.tcl.dias.batch.odr.gsip.WebexGscOdrService;
import com.tcl.dias.batch.odr.mapper.OdrTeamsDRMapper;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.GscCommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.constants.TeamsDROdrConstants;
import com.tcl.dias.common.fulfillment.beans.OdrOrderBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.OdrAdditionalServiceParam;
import com.tcl.dias.oms.entity.entities.OdrAsset;
import com.tcl.dias.oms.entity.entities.OdrComponent;
import com.tcl.dias.oms.entity.entities.OdrComponentAttribute;
import com.tcl.dias.oms.entity.entities.OdrContractInfo;
import com.tcl.dias.oms.entity.entities.OdrOrder;
import com.tcl.dias.oms.entity.entities.OdrOrderAttribute;
import com.tcl.dias.oms.entity.entities.OdrServiceAttribute;
import com.tcl.dias.oms.entity.entities.OdrServiceCommercial;
import com.tcl.dias.oms.entity.entities.OdrServiceDetail;
import com.tcl.dias.oms.entity.entities.OdrSolutionComponent;
import com.tcl.dias.oms.entity.entities.OdrTeamsDRServiceCommercial;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderDirectRouting;
import com.tcl.dias.oms.entity.entities.OrderDirectRoutingCity;
import com.tcl.dias.oms.entity.entities.OrderDirectRoutingMediaGateways;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderTeamsDR;
import com.tcl.dias.oms.entity.entities.OrderTeamsLicense;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.repository.*;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
import java.util.stream.Collectors;

import static com.tcl.dias.batch.odr.constants.OdrConstants.MEDIA_GATEWAY;
import static com.tcl.dias.batch.odr.constants.OdrConstants.MICROSOFT_CLOUD_SOLUTIONS;
import static com.tcl.dias.batch.odr.constants.OdrConstants.MICROSOFT_LICENSE;
import static com.tcl.dias.common.constants.CommonConstants.NEW;
import static com.tcl.dias.common.constants.CommonConstants.MACD;
import static com.tcl.dias.common.constants.TeamsDROdrConstants.*;

/**
 * This Class is used to freeze the order by transforming the order to a new
 * table odr
 *
 * @author Syed Ali.
 */

@Service
public class TeamsDROdrService extends OrderService {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OdrOrderRepository odrOrderRepository;

	@Autowired
	OrderToLeRepository orderToLeRepository;

	@Autowired
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@Autowired
	WebexGscOdrService gscOdrService;

	@Autowired
	OrderTeamsDRRepository orderTeamsDRRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	OrderDirectRoutingRepository orderDirectRoutingRepository;

	@Autowired
	OrderDirectRoutingCityRepository orderDirectRoutingCityRepository;

	@Autowired
	OrderDirectRoutingMgRepository orderDirectRoutingMgRepository;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@Autowired
	OdrComponentAttributeRepository odrComponentAttributeRepository;

	@Autowired
	OrderPriceRepository orderPriceRepository;

	@Autowired
	OdrServiceDetailRepository odrServiceDetailRepository;

	@Autowired
	OdrSolutionComponentRepository odrSolutionComponentRepository;

	@Autowired
	OrderTeamsLicenseRepository orderTeamsLicenseRepository;

	@Autowired
	OdrOrderAttributeRepository odrOrderAttributeRepository;

	@Autowired
	OdrContractInfoRepository odrContractInfoRepository;

	@Autowired
	OdrAssetRepository odrAssetRepository;

	@Autowired
	OdrTeamsDRServiceCommercialRepository odrTeamsDRServiceCommercialRepository;

	@Autowired
	OdrTeamsDRMapper odrMapper;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.o2c.fulfillmentdate}")
	String o2cFulfillmentQueue;

	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;

	@Autowired
	WebexGscOdrService webexGscOdrService;

	@Autowired
	OrderTeamsDRDetailsRepository orderTeamsDRDetailsRepository;

	@Autowired
	OdrServiceCommercialRepository odrServiceCommercialRepository;

	@Autowired
	OdrAdditionalServiceParamRepository odrAdditionalServiceParamRepository;

	@Autowired
	CofDetailsRepository cofDetailsRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(TeamsDROdrService.class);

	@Override
	protected String getReferenceName() {
		return null;
	}

	/**
	 * processOrderFrost - This method is used to freeze the order by transforming
	 * the order to a new table odr
	 *
	 * @param orderId
	 * @param userName
	 * @return
	 */
	public synchronized Boolean processOrderFrost(Integer orderId, String userName) {
		LOGGER.info("Process starting to freeze the order with id :: {}", orderId);
		Boolean status = true;
		Map<String, String> flowMapper = new HashMap<>();
		Optional<Order> optionalOrder = orderRepository.findById(orderId);
		OdrServiceDetail parentPlanServiceDetail = null;
		OdrServiceDetail voiceParentServiceDetail = null;
		if (optionalOrder.isPresent()) {
			Order order = optionalOrder.get();
			try {
				// Filtering out only plan and license order to le ...
				List<OrderToLe> orderToLes = orderToLeRepository.findByOrder(order);
				OrderToLe parentOrderToLe = findParentQuoteToLe(orderToLes);

				// Filtering out license and voice..
				orderToLes = orderToLes.stream()
						.filter(orderToLe -> orderToLe.getOrderToLeProductFamilies().stream()
								.anyMatch(orderToLeProductFamily -> MICROSOFT_CLOUD_SOLUTIONS
										.equals(orderToLeProductFamily.getMstProductFamily().getName())))
						.filter(orderToLe -> orderTeamsDRRepository.findByOrderToLeId(orderToLe.getId()).stream()
								.noneMatch(orderTeamsDR -> MICROSOFT_LICENSE.equals(orderTeamsDR.getServiceName())))
						.collect(Collectors.toList());

				LOGGER.info("ParentOrderToLe :: {}", parentOrderToLe.getId());
				for (OrderToLe orderToLe : orderToLes) {
					LOGGER.info("Processing order to le {}", orderToLe.getId());
					OdrOrder odrOrder = new OdrOrder();
					processOrder(order, odrOrder, userName);
					String stage = order.getStage();
					LOGGER.info("Processed process order...");
					boolean flag = true;
					List<OdrOrderAttribute> odrAttributes = new ArrayList<>();
					List<OdrContractInfo> odrContractInfos = new ArrayList<>();
					List<OdrServiceDetail> odrServiceDetails = new ArrayList<>();
					List<OdrServiceCommercial> odrServiceCommercials = new ArrayList<>();
					List<OdrTeamsDRServiceCommercial> odrTeamsDRServiceCommercials = new ArrayList<>();
					List<OdrAsset> odrAssets = new ArrayList<>();

					OdrContractInfo odrContractInfo = new OdrContractInfo();
					odrContractInfo.setOdrOrder(odrOrder);

					// Processing order to le
					LOGGER.info("Processing ordertole for teamsdr...");
					processOrderToLe(orderToLe, odrOrder, flag, odrContractInfo, userName);
					flag = false;
					List<Map<String, Object>> orderToLeAttributes = ordersLeAttributeValueRepository
							.findByLeId(orderToLe.getId());
					LOGGER.info("Processing ordertoleattributes for teamsdr...");
					processOrderToLeAttr(orderToLeAttributes, odrOrder, odrAttributes, odrContractInfo, userName,
							flowMapper);

					// Teamsdr specific odr le attributes
					addTeamsDRSpecificAttributes(orderToLe,odrOrder,odrContractInfo,odrAttributes);

					odrContractInfos.add(odrContractInfo);
					LOGGER.info("Processing productfamily for teamsdr...");
					processProductFamily(orderToLe, flowMapper, odrServiceDetails, odrContractInfo, odrOrder, userName,
							odrServiceCommercials, odrAttributes, odrTeamsDRServiceCommercials, odrAssets);

					LOGGER.info("Saving all the Order Repository");
					odrOrderRepository.save(odrOrder);
					odrOrderAttributeRepository.saveAll(odrAttributes);
					odrContractInfoRepository.saveAll(odrContractInfos);
					odrServiceDetailRepository.saveAll(odrServiceDetails);
					odrAssetRepository.saveAll(odrAssets);
					odrTeamsDRServiceCommercialRepository.saveAll(odrTeamsDRServiceCommercials);

					if (Objects.isNull(parentPlanServiceDetail)) {
						parentPlanServiceDetail = createParentOdrServiceDetail(odrOrder, order, userName,
								odrContractInfo, flowMapper, odrServiceDetails);
						// TODO : to be uncommented for voice later
						// if (orderToLes.stream().flatMap(orderToLe1 -> orderToLe1
						// .getOrderToLeProductFamilies().stream())
						// .anyMatch(orderToLeProductFamily -> GSIP
						// .equals(orderToLeProductFamily.getMstProductFamily()
						// .getName()))) {
						// voiceParentServiceDetail = processGscParentService(orderToLe,
						// odrOrder, userName,
						// odrContractInfo, odrServiceDetails,
						// parentPlanServiceDetail);
						// }
					} else {
						for (OdrServiceDetail odrServiceDetail : odrServiceDetails) {
							if (odrServiceDetail.getErfPrdCatalogProductName()
									.equalsIgnoreCase(MICROSOFT_CLOUD_SOLUTIONS)) {
								odrServiceDetail.setParentId(parentPlanServiceDetail.getId());
								odrServiceDetailRepository.save(odrServiceDetail);
								constructOdrSolutionComponents(parentPlanServiceDetail, odrServiceDetail,
										parentPlanServiceDetail, null, odrOrder, COMMON);
							}

							// TODO : to be uncommented for voice later
							// else {
							// if (Objects.nonNull(voiceParentServiceDetail)) {
							// odrServiceDetail.setParentId(voiceParentServiceDetail
							// .getId());
							// odrServiceDetailRepository.save(odrServiceDetail);
							// }
							// }
						}
					}

					for (OdrServiceCommercial odrServiceCommercial : odrServiceCommercials) {
						LOGGER.info("Type {}", odrServiceCommercial.getServiceType());
						LOGGER.info("Saving {}", odrServiceCommercial.getOdrServiceDetail().getId());
						odrServiceCommercialRepository.save(odrServiceCommercial);
					}

					List<OdrSolutionComponent> odrSolutionComponents = odrSolutionComponentRepository
							.findByOdrOrder(odrOrder);
					OdrOrderBean odrOrderBean = odrMapper.mapOrderEntityToBean(odrOrder, odrContractInfos,
							odrAttributes, odrServiceDetails, odrServiceCommercials, odrSolutionComponents,
							odrTeamsDRServiceCommercials);
					odrOrderBean.setStage(stage);
					LOGGER.info("Order Froze completed for id :: {}", orderToLe.getId());
					String resPayload = Utils.convertObjectToJson(odrOrderBean);
					LOGGER.info("Response to be transformed to sc is {}", resPayload);
					LOGGER.info("MDC Filter token value in before Queue call fulfillment {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					mqUtils.send(o2cFulfillmentQueue, Utils.convertObjectToJson(odrOrderBean));
				}
			} catch (Exception e) {
				LOGGER.error("Error in Order Frost {}", ExceptionUtils.getStackTrace(e));
				status = false;
			}
		} else {
			status = false;
			LOGGER.info("Order is Not found for id {}", orderId);
		}
		return status;
	}

	/**
	 * Method to add splecountry in odrorderattribute.
	 * Required in billing .
	 * @param orderToLe
	 * @param odrOrder
	 * @param odrContractInfo
	 * @param odrAttributes
	 */
	private void addTeamsDRSpecificAttributes(OrderToLe orderToLe,OdrOrder odrOrder, OdrContractInfo odrContractInfo, List<OdrOrderAttribute> odrAttributes){
		// Sple Country
		if(Objects.nonNull(odrContractInfo) && Objects.nonNull(odrContractInfo.getBillingCountry())){
			odrAttributes.add(constructOdrAttribute(SP_LE_COUNTRY,odrContractInfo.getBillingCountry(),odrOrder));
		}

		// CofReferenceId and cofCreatedTime.
		if(Objects.nonNull(orderToLe)){
			CofDetails cofDetails = cofDetailsRepository.
					findByReferenceIdAndReferenceType(orderToLe.getOrderLeCode(),CommonConstants.ORDER_LE_CODE);
			if(Objects.nonNull(cofDetails)){
				// CofReferenceId
				odrAttributes.add(constructOdrAttribute(COF_REF_ID,
						orderToLe.getOrder().getOrderCode() + CommonConstants.HYPHEN + orderToLe.getOrderLeCode(),
						odrOrder));
				// CofCreatedTime
				odrAttributes.add(constructOdrAttribute(COF_CREATED_TIME,String.valueOf(cofDetails.getCreatedTime()),odrOrder));
			}
		}
	}

	/**
	 * Method to construct odrAttribute
	 * @param attributeName
	 * @param attributeValue
	 * @param odrOrder
	 * @return
	 */
	private OdrOrderAttribute constructOdrAttribute(String attributeName,String attributeValue,OdrOrder odrOrder){
		OdrOrderAttribute orderAttributes = new OdrOrderAttribute();
		orderAttributes.setAttributeAltValueLabel(attributeValue);
		orderAttributes.setAttributeName(attributeName);
		orderAttributes.setAttributeValue(attributeValue);
		orderAttributes.setCreatedBy(Utils.getSource());
		orderAttributes.setCreatedDate(new Date());
		orderAttributes.setUpdatedDate(new Date());
		orderAttributes.setOdrOrder(odrOrder);
		return orderAttributes;
	}

	/**
	 * Check for null - if not null return the price, else return 0
	 *
	 * @param price
	 * @return
	 */
	public static double checkForNull(Double price) {
		if (Objects.nonNull(price))
			return price;
		else
			return 0.0;
	}

	/**
	 * Method to find parent quote to le..
	 *
	 * @param orderToLes
	 * @return
	 */
	public OrderToLe findParentQuoteToLe(List<OrderToLe> orderToLes) {
		for (OrderToLe orderToLe : orderToLes) {
			List<OrderTeamsDR> orderTeamsDRS = orderTeamsDRRepository.findByOrderToLeId(orderToLe.getId());
			if (!orderTeamsDRS.isEmpty()) {
				for (OrderTeamsDR orderTeamsDR : orderTeamsDRS) {
					if (Objects.nonNull(orderTeamsDR.getProfileName())) {
						if (orderTeamsDR.getProfileName().contains(PLAN)) {
							return orderToLe;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * processProductFamily
	 *
	 * @param orderToLe
	 * @param flowMapper
	 * @param odrAttributes
	 * @throws ParseException
	 */
	public void processProductFamily(OrderToLe orderToLe, Map<String, String> flowMapper,
			List<OdrServiceDetail> odrServiceDetails, OdrContractInfo odrContractInfo, OdrOrder odrOrder,
			String username, List<OdrServiceCommercial> odrServiceCommercials, List<OdrOrderAttribute> odrAttributes,
			List<OdrTeamsDRServiceCommercial> odrWebexServiceCommercials, List<OdrAsset> odrAssets) {
		for (OrderToLeProductFamily orderToLeProdFamily : orderToLe.getOrderToLeProductFamilies()) {
			flowMapper.put(PRODUCT_NAME, orderToLeProdFamily.getMstProductFamily().getName());
			flowMapper.put(PRODUCT_CODE, orderToLeProdFamily.getMstProductFamily().getId() + CommonConstants.EMPTY);
			processProductSolution(orderToLe, flowMapper, orderToLeProdFamily, odrServiceDetails, odrContractInfo,
					odrOrder, username, odrServiceCommercials, odrAttributes, odrWebexServiceCommercials, odrAssets);
		}
	}

	/**
	 * Method to process product solution
	 *
	 * @param orderToLe
	 * @param flowMapper
	 * @param orderToLeProdFamily
	 * @param odrServiceDetails
	 * @param odrContractInfo
	 * @param odrOrder
	 * @param userName
	 * @param odrServiceCommercials
	 * @param odrAttributes
	 * @param odrTeamsDRServiceCommercials
	 * @param odrAssets
	 */
	public void processProductSolution(OrderToLe orderToLe, Map<String, String> flowMapper,
			OrderToLeProductFamily orderToLeProdFamily, List<OdrServiceDetail> odrServiceDetails,
			OdrContractInfo odrContractInfo, OdrOrder odrOrder, String userName,
			List<OdrServiceCommercial> odrServiceCommercials, List<OdrOrderAttribute> odrAttributes,
			List<OdrTeamsDRServiceCommercial> odrTeamsDRServiceCommercials, List<OdrAsset> odrAssets) {
		LOGGER.info("Processing productsolution for teamsdr...");
		for (OrderProductSolution orderProductSolution : orderToLeProdFamily.getOrderProductSolutions()) {
			if (MACD.equals(orderToLe.getOrderType())) {
				if (odrOrder.getOpOrderCode().contains(MICROSOFT_CLOUD_SOLUTIONS)) {
					// Need to be implemented later for MACD...
				}
			}
			if (MICROSOFT_CLOUD_SOLUTIONS.equals(orderToLeProdFamily.getMstProductFamily().getName())) {
				flowMapper.put("offeringName", orderProductSolution.getMstProductOffering().getProductName());
				LOGGER.info("Entering Teamsdr with offeringName {} :: ",
						orderProductSolution.getMstProductOffering().getProductName());
				LOGGER.info("Processing teamsdrsolution...");
				processTeamsDR(flowMapper, orderProductSolution, odrServiceDetails, odrContractInfo, odrOrder, userName,
						odrAttributes, odrTeamsDRServiceCommercials);
			} else if (OdrConstants.GSIP.equals(orderToLeProdFamily.getMstProductFamily().getName())) {
				// TODO : to be uncommented for voice later
				// flowMapper.put("offeringName", orderProductSolution.getMstProductOffering()
				// .getProductName());
				// LOGGER.info("Processing gsc solution...");
				// webexGscOdrService
				// .processGsc(flowMapper, orderProductSolution, odrServiceDetails,
				// odrContractInfo, odrOrder,
				// userName, odrServiceCommercials, odrAttributes, odrAssets);
			}
		}
	}

	/**
	 * Method to process plan details.
	 *
	 * @param orderTeamsDR
	 * @param mstProductFamily
	 * @param odrOrder
	 * @param odrServiceDetails
	 * @param odrContractInfo
	 * @param userName
	 * @param flowMapper
	 */
	private void processPlan(OrderTeamsDR orderTeamsDR, MstProductFamily mstProductFamily, OdrOrder odrOrder,
			List<OdrServiceDetail> odrServiceDetails, OdrContractInfo odrContractInfo, String userName,
			Map<String, String> flowMapper, List<OdrTeamsDRServiceCommercial> odrTeamsDRServiceCommercials) {
		OdrServiceDetail odrServiceDetail = new OdrServiceDetail();
		odrServiceDetails.add(odrServiceDetail);
		processCommonServiceDetailAttr(odrServiceDetail, odrOrder, odrContractInfo, userName, flowMapper);
		odrServiceDetail.setServiceCommissionedDate(orderTeamsDR.getCreatedTime());
		odrServiceDetail.setMrc(orderTeamsDR.getMrc());
		odrServiceDetail.setNrc(orderTeamsDR.getNrc());
		odrServiceDetail.setArc(orderTeamsDR.getArc());
		odrServiceDetail.setErfPrdCatalogOfferingName(orderTeamsDR.getProfileName());
		odrServiceDetail.setErfPrdCatalogFlavourName(orderTeamsDR.getProfileName());
		OdrComponent lmOdrComponent = persistOdrComponent(odrServiceDetail, "LM");
		odrServiceDetail.getOdrComponents().add(lmOdrComponent);

		// To process commercial for plan..
		processCommercialsForPlan(orderTeamsDR, mstProductFamily, odrServiceDetail, odrTeamsDRServiceCommercials,
				userName);

		lmOdrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(odrServiceDetail, lmOdrComponent,
				TOTAL_COMMITTED_USERS, String.valueOf(orderTeamsDR.getNoOfUsers())));

		orderTeamsDRDetailsRepository.findByOrderTeamsDR(orderTeamsDR).forEach(orderTeamsDRDetails -> {
			OdrComponent odrComponent = persistOdrComponent(odrServiceDetail, orderTeamsDRDetails.getCountry());
			odrServiceDetail.getOdrComponents().add(odrComponent);

			odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(odrServiceDetail, odrComponent,
					NO_OF_NAMED_USERS, String.valueOf(orderTeamsDRDetails.getNoOfNamedUsers())));
			odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(odrServiceDetail, odrComponent,
					NO_OF_COMMON_AREA_DEVICES, String.valueOf(orderTeamsDRDetails.getNoOfCommonAreaDevices())));
			odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(odrServiceDetail, odrComponent,
					TOTAL_USERS, String.valueOf(orderTeamsDRDetails.getTotalUsers())));

			// To process service attributes...
			processAttributesPlan(orderTeamsDRDetails.getId(), TEAMSDR_SERVICE_ATTRIBUTES, mstProductFamily,
					odrServiceDetail, odrComponent, null, odrTeamsDRServiceCommercials, userName, orderTeamsDR);
		});

		// To process config attributes...
		processAttributesPlan(orderTeamsDR.getId(), TEAMSDR_CONFIG_ATTRIBUTES, mstProductFamily, odrServiceDetail,
				lmOdrComponent, orderTeamsDR.getProfileName(), odrTeamsDRServiceCommercials, userName, orderTeamsDR);

		// To process enrichment attributes...
		processAttributesPlan(orderTeamsDR.getId(), PLAN_REF_NAME, mstProductFamily, odrServiceDetail, lmOdrComponent,
				orderTeamsDR.getProfileName(), odrTeamsDRServiceCommercials, userName, orderTeamsDR);
	}

	/**
	 * Method to process commercials for plan.
	 *
	 * @param orderTeamsDR
	 * @param odrServiceDetail
	 * @param odrTeamsDRServiceCommercials
	 * @param userName
	 */
	private void processCommercialsForPlan(OrderTeamsDR orderTeamsDR, MstProductFamily mstProductFamily,
			OdrServiceDetail odrServiceDetail, List<OdrTeamsDRServiceCommercial> odrTeamsDRServiceCommercials,
			String userName) {
		// For plan.. component type - plan
		processTeamsDrServiceCommercialForPlan(orderTeamsDR, odrServiceDetail, odrTeamsDRServiceCommercials,
				orderTeamsDR.getProfileName(), PLAN, PLAN_COMMERCIAL, userName);

		List<String> servicesToFilter = Arrays.asList(MEDIA_GATEWAY, MICROSOFT_LICENSE);
		List<OrderTeamsDR> orderTeamsDRS = orderTeamsDRRepository.findByOrderToLeId(orderTeamsDR.getOrderToLe().getId())
				.stream().filter(orderTeamsDR1 -> (Objects.nonNull(orderTeamsDR1.getServiceName())
						&& !servicesToFilter.contains(orderTeamsDR1.getServiceName())))
				.collect(Collectors.toList());

		orderTeamsDRS.forEach(orderTeamsDR1 -> {
			//other items under plan , component type - plan
			LOGGER.info("Underlying component :: {}",orderTeamsDR1.getServiceName());
			processTeamsDrServiceCommercialForPlan(orderTeamsDR1, odrServiceDetail, odrTeamsDRServiceCommercials,
					orderTeamsDR1.getServiceName(), PLAN, orderTeamsDR1.getServiceName(), userName);
			if (CUSTOM_PLAN.equals(orderTeamsDR1.getProfileName()) && Objects.nonNull(orderTeamsDR1.getServiceName())) {
				LOGGER.info("For underlying components of custom plan :: {}",orderTeamsDR1.getServiceName());
				List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
						.findByReferenceIdAndAndMstProductFamilyAndReferenceName(orderTeamsDR1.getId(),
								mstProductFamily, TEAMSDR_CONFIG_ATTRIBUTES);
				orderProductComponents.forEach(orderProductComponent -> {
					List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
							.findByOrderProductComponent(orderProductComponent);
					orderProductComponentsAttributeValues.forEach(attributeValue -> {
						String attrName = attributeValue.getProductAttributeMaster().getName();
						LOGGER.info("AttrName for underlying components :: {},{}", attrName, attrName.length());
						processEnrichmentAttribute(odrServiceDetail, attrName, attributeValue.getAttributeValues());
						if (ALL_CHARGES.contains(attrName)) {
							LOGGER.info("Contains charge line item... :: {}", attrName);
							String hsnCode = fetchHsnCode(attributeValue);
							//other commercial line items under plan - component type - planChargeItem
							if (RECURRING.equalsIgnoreCase(attrName))
								processTeamsDrServiceCommercialFromProductComponent(orderTeamsDR1.getServiceName(),
										orderTeamsDR1.getServiceName(), attrName, attributeValue.getAttributeValues(),
										hsnCode, String.valueOf(attributeValue.getId()), odrServiceDetail,
										odrTeamsDRServiceCommercials, userName, PLAN_CHARGE_ITEM,
										orderTeamsDR.getNoOfUsers());
							else {
								processTeamsDrServiceCommercialFromProductComponent(orderTeamsDR1.getServiceName(),
										orderTeamsDR1.getServiceName(), attrName, attributeValue.getAttributeValues(),
										hsnCode, String.valueOf(attributeValue.getId()), odrServiceDetail,
										odrTeamsDRServiceCommercials, userName, PLAN_CHARGE_ITEM, 1);
							}
						}
					});
				});
			}
		});

	}

	/**
	 * Method to process teamsdr service commercial for Plan.
	 *
	 * @param orderTeamsDR
	 * @param odrServiceDetail
	 * @param odrTeamsDRServiceCommercials
	 * @param username
	 */
	private void processTeamsDrServiceCommercialForPlan(OrderTeamsDR orderTeamsDR, OdrServiceDetail odrServiceDetail,
			List<OdrTeamsDRServiceCommercial> odrTeamsDRServiceCommercials, String componentName, String componentType,
			String componentDesc, String username) {
		OdrTeamsDRServiceCommercial odrTeamsDRServiceCommercial = new OdrTeamsDRServiceCommercial();
		odrTeamsDRServiceCommercial.setOdrServiceDetail(odrServiceDetail);
		odrTeamsDRServiceCommercial.setComponentName(componentName);
		odrTeamsDRServiceCommercial.setComponentDesc(componentDesc);
		odrTeamsDRServiceCommercial.setComponentType(componentType);
		// odrTeamsDRServiceCommercial.setHsnCode(orderDirectRoutingCity.getHsnCode());
		odrTeamsDRServiceCommercial.setQuantity(orderTeamsDR.getNoOfUsers());
		// odrTeamsDRServiceCommercial.setUnitMrc(orderDirectRoutingCity.getm());
		// odrTeamsDRServiceCommercial.setUnitNrc(orderDirectRoutingCity.getUnitNrc());
		odrTeamsDRServiceCommercial.setMrc(orderTeamsDR.getMrc());
		odrTeamsDRServiceCommercial.setNrc(orderTeamsDR.getNrc());
		odrTeamsDRServiceCommercial.setArc(orderTeamsDR.getArc());
		odrTeamsDRServiceCommercial.setTcv(orderTeamsDR.getTcv());
		// odrTeamsDRServiceCommercial.setContractType(orderDirectRoutingCity.getContractType());
		odrTeamsDRServiceCommercial.setCreatedBy(username);
		odrTeamsDRServiceCommercial.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		odrTeamsDRServiceCommercials.add(odrTeamsDRServiceCommercial);
	}

	/**
	 * Method to process attributes
	 *  @param referenceId
	 * @param component
	 * @param mstProductFamily
	 * @param odrServiceDetail
	 * @param odrComponent
	 * @param orderTeamsDR
	 */
	public void processAttributesPlan(Integer referenceId, String component, MstProductFamily mstProductFamily,
			OdrServiceDetail odrServiceDetail, OdrComponent odrComponent, String compName,
			List<OdrTeamsDRServiceCommercial> teamsDRServiceCommercials, String userName, OrderTeamsDR orderTeamsDR) {

		List<OrderProductComponent> orderProductComponentsForLicense = orderProductComponentRepository
				.findByReferenceIdAndAndMstProductFamilyAndReferenceName(referenceId, mstProductFamily, component);

		orderProductComponentsForLicense.forEach(orderProductComponent -> orderProductComponentsAttributeValueRepository
				.findByOrderProductComponent(orderProductComponent).forEach(attrValue -> {
					String attrName = attrValue.getProductAttributeMaster().getName();
					LOGGER.info("AttrName for plan :: {},{}", attrName, attrName.length());
					processEnrichmentAttribute(odrServiceDetail, attrName, attrValue.getAttributeValues());
					switch (attrName) {
					case DO_YOU_NEED_E1_E3_LICENSE:
						attrName = DO_YOU_NEED_LICENSE;
						break;
					case DID_ATTRIBUTE:
						attrName = DO_CUSTOMER_NEEDS_DID;
						break;
					default:
						break;
					}
					if (ALL_CHARGES.contains(attrName)) {
						LOGGER.info("Contains charge line item... :: {}", attrName);
						String hsnCode = fetchHsnCode(attrValue);
						String componentName = orderProductComponent.getMstProductComponent().getName();
						//plan charge line items - planChargeItem
						if(RECURRING.equalsIgnoreCase(attrName))
							processTeamsDrServiceCommercialFromProductComponent(componentName, componentName, attrName,
									attrValue.getAttributeValues(), hsnCode, String.valueOf(attrValue.getId()),
									odrServiceDetail, teamsDRServiceCommercials, userName, PLAN_CHARGE_ITEM,
									orderTeamsDR.getNoOfUsers());
						else {
							processTeamsDrServiceCommercialFromProductComponent(componentName, componentName, attrName,
									attrValue.getAttributeValues(), hsnCode, String.valueOf(attrValue.getId()),
									odrServiceDetail, teamsDRServiceCommercials, userName, PLAN_CHARGE_ITEM, 1);
						}
					} else if (SERVICE_LEVEL_CHARGES.contains(attrName)) {
						LOGGER.info("Contains charge line item... :: {}", attrName);
						String hsnCode = fetchHsnCode(attrValue);
						String componentName = orderProductComponent.getMstProductComponent().getName();
						//simple, professional service charge items, component type - serviceChargeItem
						processTeamsDrServiceCommercialFromProductComponent(componentName, componentName, attrName,
								attrValue.getAttributeValues(), hsnCode, String.valueOf(attrValue.getId()),
								odrServiceDetail, teamsDRServiceCommercials, userName, SERVICE_CHARGE_ITEM, null);
					} else {
						odrComponent.getOdrComponentAttributes()
								.add(persistOdrComponentAttributes(odrServiceDetail, odrComponent, attrName,
										attrValue.getAttributeValues()));
					}

				}));
	}

	/**
	 * Method to map details.
	 *
	 * @param odrServiceDetail
	 * @param attrName
	 * @param attrValue
	 */
	private void processEnrichmentAttribute(OdrServiceDetail odrServiceDetail, String attrName, String attrValue) {
		switch (attrName) {
		case DEMARCATION_FLOOR:
			odrServiceDetail.setDemarcationFloor(attrValue);
			break;
		case DEMARCATION_ROOM:
			odrServiceDetail.setDemarcationRoom(attrValue);
			break;
		case DEMARCATION_BUILDING_NAME:
			odrServiceDetail.setDemarcationApartment(attrValue);
			break;
		case LOCAL_IT_CONTACT_NAME:
			odrServiceDetail.setLocalItContactName(attrValue);
			break;
		case LOCAL_IT_CONTACT_NUMBER:
			odrServiceDetail.setLocalItContactMobile(attrValue);
			break;
		case LOCAL_IT_EMAIL_ID:
			odrServiceDetail.setLocalItContactEmail(attrValue);
			break;
		case RACK:
			odrServiceDetail.setDemarcationRack(attrValue);
			break;
		case ROOM:
			odrServiceDetail.setDemarcationRoom(attrValue);
			break;
		default:
			break;
		}
	}

	/**
	 * Method to create parent odr service detail.
	 *
	 * @param odrOrder
	 * @param order
	 * @param username
	 * @param odrContractInfo
	 * @param flowMapper
	 * @param odrServiceDetails
	 */
	private OdrServiceDetail createParentOdrServiceDetail(OdrOrder odrOrder, Order order, String username,
			OdrContractInfo odrContractInfo, Map<String, String> flowMapper, List<OdrServiceDetail> odrServiceDetails) {
		LOGGER.info("CreateParentOdrServiceDetail invoked ...");
		OdrServiceDetail solutionOdrServiceDetail = new OdrServiceDetail();
		solutionOdrServiceDetail.setServiceRefId(Utils.generateUid());
		// solutionOdrServiceDetail.setErfPrdCatalogOfferingName(
		// orderProductSolution.getMstProductOffering().getProductName());
		solutionOdrServiceDetail.setCreatedBy(username);
		solutionOdrServiceDetail.setCreatedDate(new Date());
		solutionOdrServiceDetail.setUpdatedDate(new Date());
		solutionOdrServiceDetail.setUpdatedBy(username);
		solutionOdrServiceDetail.setIsActive(CommonConstants.Y);
		solutionOdrServiceDetail.setIsIzo(CommonConstants.N);
		// TODO: To ask what is erf catalogue name for teamsdr
		solutionOdrServiceDetail.setErfPrdCatalogProductName(TEAMSDR_SOLUTION);
		solutionOdrServiceDetail.setOdrOrder(odrOrder);
		solutionOdrServiceDetail.setOdrOrderUuid(odrOrder.getUuid());
		solutionOdrServiceDetail.setOrderType(CommonConstants.NEW);
		OdrComponent solutionOdrComponent = persistOdrComponent(solutionOdrServiceDetail);
		solutionOdrComponent.setSiteType("A");
		solutionOdrComponent.setOdrServiceDetail(solutionOdrServiceDetail);
		solutionOdrServiceDetail.getOdrComponents().add(solutionOdrComponent);
		solutionOdrServiceDetail = odrServiceDetailRepository.saveAndFlush(solutionOdrServiceDetail);

		for (OdrServiceDetail odrServiceDetail : odrServiceDetails) {
			if (odrServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase(MICROSOFT_CLOUD_SOLUTIONS)) {
				odrServiceDetail.setParentId(solutionOdrServiceDetail.getId());
				odrServiceDetailRepository.save(odrServiceDetail);
				constructOdrSolutionComponents(solutionOdrServiceDetail, odrServiceDetail, solutionOdrServiceDetail,
						null, odrOrder, COMMON);
			}
		}

		odrServiceDetails.add(solutionOdrServiceDetail);
		return solutionOdrServiceDetail;
	}

	/**
	 * Method to process gsc parent service...
	 *
	 * @param odrOrder
	 * @param username
	 * @param odrContractInfo
	 * @param odrServiceDetails
	 * @return
	 */
	private OdrServiceDetail processGscParentService(OrderToLe orderToLe, OdrOrder odrOrder, String username,
			OdrContractInfo odrContractInfo, List<OdrServiceDetail> odrServiceDetails,
			OdrServiceDetail parentPlanOdrServiceDetail) {
		LOGGER.info("process gsc parent service method invoked");
		OdrServiceDetail parentOdrServiceDetail = new OdrServiceDetail();
		// parentOdrServiceDetail.setServiceRefId(Utils.generateUid());
		// parentOdrServiceDetail.setErfPrdCatalogOfferingName(
		// orderProductSolution.getMstProductOffering().getProductName());
		parentOdrServiceDetail.setCreatedBy(username);
		parentOdrServiceDetail.setCreatedDate(new Date());
		parentOdrServiceDetail.setUpdatedDate(new Date());
		parentOdrServiceDetail.setUpdatedBy(username);
		parentOdrServiceDetail.setIsActive(CommonConstants.Y);
		parentOdrServiceDetail.setIsIzo(CommonConstants.N);
		// parentOdrServiceDetail.setErfPrdCatalogProductName(GscCommonConstants.GSC_SOLUTION);
		parentOdrServiceDetail.setErfPrdCatalogOfferingName(GscCommonConstants.GSC_SOLUTION);
		parentOdrServiceDetail.setErfPrdCatalogProductName(CommonConstants.GSIP);
		/*
		 * parentOdrServiceDetail
		 * .setErfPrdCatalogProductId(orderProductSolution.getMstProductOffering()
		 * .getMstProductFamily().getProductCatalogFamilyId());
		 */
		parentOdrServiceDetail.setOdrOrder(odrOrder);
		parentOdrServiceDetail.setOdrOrderUuid(odrOrder.getUuid());
		parentOdrServiceDetail.setServiceRefId(odrOrder.getOpOrderCode());
		parentOdrServiceDetail.setOdrContractInfo(odrContractInfo);
		// parentOdrServiceDetail.setOrderType(CommonConstants.NEW);
		OdrComponent solutionOdrComponent = persistOdrComponent(parentOdrServiceDetail);
		solutionOdrComponent.setSiteType("A");
		solutionOdrComponent.setOdrServiceDetail(parentOdrServiceDetail);
		parentOdrServiceDetail.getOdrComponents().add(solutionOdrComponent);
		parentOdrServiceDetail = odrServiceDetailRepository.saveAndFlush(parentOdrServiceDetail);

		for (OdrServiceDetail odrServiceDetail : odrServiceDetails) {
			if (orderToLe.getOrderToLeProductFamilies().stream()
					.anyMatch(orderToLeProductFamily -> MICROSOFT_CLOUD_SOLUTIONS
							.equals(orderToLeProductFamily.getMstProductFamily().getName()))) {
				odrServiceDetail.setParentId(parentPlanOdrServiceDetail.getId());
			} else {
				odrServiceDetail.setParentId(parentOdrServiceDetail.getId());
			}
		}
		odrServiceDetailRepository.saveAll(odrServiceDetails);
		odrServiceDetails.add(parentOdrServiceDetail);
		return parentOdrServiceDetail;
	}

	/**
	 * Method to construct odr solution components.
	 *
	 * @param solutionOdrServiceDetail
	 * @param odrServiceDetail
	 * @param parenOdrServiceDetail
	 * @param cpeComponent
	 * @param odrOrder
	 * @param componentGroup
	 * @return
	 */
	private OdrSolutionComponent constructOdrSolutionComponents(OdrServiceDetail solutionOdrServiceDetail,
			OdrServiceDetail odrServiceDetail, OdrServiceDetail parenOdrServiceDetail, OdrComponent cpeComponent,
			OdrOrder odrOrder, String componentGroup) {
		OdrSolutionComponent odrSolutionComponent = new OdrSolutionComponent();
		odrSolutionComponent.setComponentGroup(componentGroup);
		if (cpeComponent != null && cpeComponent.getId() != null) {
			odrSolutionComponent.setCpeComponentId(cpeComponent.getId());
		}
		odrSolutionComponent.setIsActive(CommonConstants.Y);
		odrSolutionComponent.setOdrOrder(odrOrder);
		if (!Objects.isNull(odrServiceDetail)) {
			odrSolutionComponent.setOdrServiceDetail1(odrServiceDetail);
		}
		if (!Objects.isNull(parenOdrServiceDetail)) {
			odrSolutionComponent.setOdrServiceDetail2(parenOdrServiceDetail);
			odrSolutionComponent.setParentServiceCode(parenOdrServiceDetail.getServiceRefId());
		}
		if (!Objects.isNull(solutionOdrServiceDetail)) {
			odrSolutionComponent.setOdrServiceDetail3(solutionOdrServiceDetail);
			odrSolutionComponent.setSolutionCode(solutionOdrServiceDetail.getServiceRefId());
		}
		odrSolutionComponent.setOdrOrder(odrOrder);
		odrSolutionComponent.setOrderCode(odrOrder.getUuid());
		odrSolutionComponent = odrSolutionComponentRepository.save(odrSolutionComponent);
		return odrSolutionComponent;
	}

	/**
	 * Method to process teamsdr
	 *
	 * @param flowMapper
	 * @param orderProductSolution
	 * @param odrServiceDetails
	 * @param odrContractInfo
	 * @param odrOrder
	 * @param userName
	 * @param odrAttributes
	 * @param odrTeamsDRServiceCommercials
	 */
	public void processTeamsDR(Map<String, String> flowMapper, OrderProductSolution orderProductSolution,
			List<OdrServiceDetail> odrServiceDetails, OdrContractInfo odrContractInfo, OdrOrder odrOrder,
			String userName, List<OdrOrderAttribute> odrAttributes,
			List<OdrTeamsDRServiceCommercial> odrTeamsDRServiceCommercials) {
		LOGGER.info("processTeamsDR method invoked wih solution id :: {}", orderProductSolution.getId());
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(MICROSOFT_CLOUD_SOLUTIONS,
				CommonConstants.BACTIVE);
		List<OrderTeamsDR> orderTeamsDRS = orderTeamsDRRepository
				.findByOrderProductSolutionAndStatus(orderProductSolution, CommonConstants.BACTIVE);
		LOGGER.info("OrderTeamsDR size  :: {}", orderTeamsDRS.size());
		List<String> servicesToFilter = Arrays.asList(MICROSOFT_LICENSE, MEDIA_GATEWAY);
		orderTeamsDRS.stream().filter(orderTeamsDR -> servicesToFilter.contains(orderTeamsDR.getServiceName())
				|| Objects.isNull(orderTeamsDR.getServiceName())).forEach(orderTeamsDR -> {
					LOGGER.info("For service name :: {}", orderTeamsDR.getServiceName());
					if (Objects.isNull(orderTeamsDR.getServiceName())) {
						LOGGER.info("Processing plan orderteamsdr for teamsdr...");
						processPlan(orderTeamsDR, mstProductFamily, odrOrder, odrServiceDetails, odrContractInfo,
								userName, flowMapper, odrTeamsDRServiceCommercials);
					} else if (MEDIA_GATEWAY.equals(orderTeamsDR.getServiceName())) {
						LOGGER.info("Processing mg orderteamsdr for teamsdr...");
						processMediaGateway(orderTeamsDR, odrOrder, odrServiceDetails, odrContractInfo, userName,
								flowMapper, odrTeamsDRServiceCommercials, orderProductSolution);
					} else if (MICROSOFT_LICENSE.equals(orderTeamsDR.getServiceName())) {
						LOGGER.info("Processing license orderteamsdr for teamsdr...");
						// TODO: To be uncommented later for license
						// processLicense(orderTeamsDR, mstProductFamily, odrOrder, odrServiceDetails,
						// odrContractInfo, userName,
						// flowMapper, odrTeamsDRServiceCommercials);
					}
				});

	}

	/**
	 * Method to process mediagateway data.
	 *
	 * @param orderTeamsDR
	 * @param odrOrder
	 * @param odrServiceDetails
	 * @param odrContractInfo
	 * @param userName
	 * @param flowMapper
	 * @param odrTeamsDRServiceCommercials
	 * @param orderProductSolution
	 */
	private void processMediaGateway(OrderTeamsDR orderTeamsDR, OdrOrder odrOrder,
			List<OdrServiceDetail> odrServiceDetails, OdrContractInfo odrContractInfo, String userName,
			Map<String, String> flowMapper, List<OdrTeamsDRServiceCommercial> odrTeamsDRServiceCommercials,
			OrderProductSolution orderProductSolution) {
		LOGGER.info("Processing mg flow for teamsdr...");
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(MICROSOFT_CLOUD_SOLUTIONS,
				CommonConstants.BACTIVE);
		List<OrderDirectRouting> orderDirectRoutings = orderDirectRoutingRepository.findByOrderTeamsDR(orderTeamsDR);
		orderDirectRoutings.forEach(orderDirectRouting -> {
			List<OrderDirectRoutingCity> sites = orderDirectRoutingCityRepository
					.findByOrderDirectRoutingId(orderDirectRouting.getId());
			sites.forEach(site -> {
				// Creating service detail for each site...
				OdrServiceDetail odrServiceDetail = new OdrServiceDetail();
				odrServiceDetails.add(odrServiceDetail);

				// Processing common attributes..
				LOGGER.info("Processing common attributes for teamsdr...");
				processCommonServiceDetailAttr(odrServiceDetail, odrOrder, odrContractInfo, userName, flowMapper);

				odrServiceDetail.setServiceCommissionedDate(orderTeamsDR.getCreatedTime());
				odrServiceDetail.setMrc(site.getMrc().doubleValue());
				odrServiceDetail.setNrc(site.getNrc().doubleValue());
				odrServiceDetail.setArc(site.getArc().doubleValue());
				odrServiceDetail.setErfPrdCatalogOfferingName(MEDIA_GATEWAY);
				odrServiceDetail.setErfPrdCatalogFlavourName(MEDIA_GATEWAY);
				odrServiceDetail.setDestinationCountry(orderDirectRouting.getCountry());
				processSiteDetail(odrServiceDetail, site, mstProductFamily);

				// Storing the attributes..
				LOGGER.info("Storing attributes for teamsdr...");
				processTeamsDRServiceAttribute(odrServiceDetail, "site_name", site.getName(), SITE_CONFIGURATION,
						userName, false);
				processTeamsDRServiceAttribute(odrServiceDetail, "media_gateway_type", site.getMediaGatewayType(),
						SITE_CONFIGURATION, userName, false);
				List<OrderDirectRoutingMediaGateways> mediaGateways = orderDirectRoutingMgRepository
						.findByOrderDirectRoutingCityId(site.getId());
				mediaGateways.forEach(
						mediaGateway -> processTeamsDRServiceAttribute(odrServiceDetail, "mediagateway_quantity",
								String.valueOf(mediaGateway.getQuantity()), mediaGateway.getName(), userName, false));

				// Processing service commercial for mg..
				LOGGER.info("Processing service commercial for teamsdr...");
				processTeamsDrServiceCommercialForSite(site, odrServiceDetail, odrTeamsDRServiceCommercials, userName);

				// Creating odr component for mg.
				LOGGER.info("Processing odr component for teamsdr...");
				OdrComponent lmOdrComponent = persistOdrComponent(odrServiceDetail, "LM");
				odrServiceDetail.getOdrComponents().add(lmOdrComponent);
				processOdrComponentForMg(flowMapper, odrServiceDetail, lmOdrComponent, site,
						odrTeamsDRServiceCommercials, userName);
			});
		});
	}

	/**
	 * Method to process site address to service detail.
	 *
	 * @param odrServiceDetail
	 * @param orderDirectRoutingCity
	 * @param mstProductFamily
	 */
	private void processSiteDetail(OdrServiceDetail odrServiceDetail, OrderDirectRoutingCity orderDirectRoutingCity,
			MstProductFamily mstProductFamily) {

		odrServiceDetail.setDestinationCity(orderDirectRoutingCity.getName());
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndAndMstProductFamilyAndReferenceName(orderDirectRoutingCity.getId(),
						mstProductFamily, TEAMSDR_SITE_ATTRIBUTES);
		orderProductComponents.forEach(orderProductComponent -> {
			List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
					.findByOrderProductComponent(orderProductComponent);
			orderProductComponentsAttributeValues.forEach(attrValue -> {
				String attrName = attrValue.getProductAttributeMaster().getName();
				switch (attrName) {
				case SITE_ADDRESS_1:
					odrServiceDetail.setDestinationAddressLineOne(attrValue.getAttributeValues());
					break;
				case SITE_ADDRESS_2:
					odrServiceDetail.setDestinationAddressLineTwo(attrValue.getAttributeValues());
					break;
				case SITE_ADDRESS_3:
					odrServiceDetail.setDestinationLocality(attrValue.getAttributeValues());
					break;
				case STATE:
					odrServiceDetail.setDestinationState(attrValue.getAttributeValues());
					break;
				case PINCODE:
					odrServiceDetail.setDestinationPincode(attrValue.getAttributeValues());
					break;
				default:
					break;
				}
			});
		});
	}

	/**
	 * Process odr component for mg.
	 *
	 * @param flowMapper
	 * @param odrServiceDetail
	 * @param lmOdrComponent
	 * @param orderDirectRoutingCity
	 * @param odrTeamsDRServiceCommercials
	 * @param userName
	 */
	private void processOdrComponentForMg(Map<String, String> flowMapper, OdrServiceDetail odrServiceDetail,
			OdrComponent lmOdrComponent, OrderDirectRoutingCity orderDirectRoutingCity,
			List<OdrTeamsDRServiceCommercial> odrTeamsDRServiceCommercials, String userName) {
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(MICROSOFT_CLOUD_SOLUTIONS,
				CommonConstants.BACTIVE);
		Set<OdrComponentAttribute> odrComponentAttributes = new HashSet<>();

		// Processing site attributes in odr component..
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndAndMstProductFamilyAndReferenceName(orderDirectRoutingCity.getId(),
						mstProductFamily, TEAMSDR_SITE_ATTRIBUTES);
		Map<String, String> fullAddress = new HashMap<>();
		orderProductComponents.forEach(orderProductComponent -> {
			List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
					.findByOrderProductComponent(orderProductComponent);
			orderProductComponentsAttributeValues.forEach(attributeValue -> {
				OdrComponentAttribute odrComponentAttribute = null;
				String attrName = attributeValue.getProductAttributeMaster().getName();
				LOGGER.info("AttrName for Mg :: {},{}", attrName, attrName.length());
				processEnrichmentAttribute(odrServiceDetail, attrName, attributeValue.getAttributeValues());
				if (ALL_CHARGES.contains(attrName)) {
					LOGGER.info("Contains charge line item... :: {}", attrName);
					String hsnCode = fetchHsnCode(attributeValue);
					processTeamsDrServiceCommercialFromProductComponent(SITE, SITE, attrName,
							attributeValue.getAttributeValues(), hsnCode, String.valueOf(attributeValue.getId()),
							odrServiceDetail, odrTeamsDRServiceCommercials, userName, ENDPOINT, null);
				}
				//mapping L2O attributes to below keys for maintaining common standard across o2c and UI accessibility
				//the below attributes("Site Address 1", "Site Address 2" etc.)  are saved in table from UI
				if (attrName.equalsIgnoreCase(SITE_ADDRESS_1)) {
					fullAddress.put(SITE_ADDRESS_1, attributeValue.getAttributeValues());
					odrComponentAttribute = persistOdrComponentAttributes(odrServiceDetail, lmOdrComponent,
							"destinationAddressLineOne", attributeValue.getAttributeValues());
				} else if (attrName.equalsIgnoreCase(SITE_ADDRESS_2)) {
					fullAddress.put(SITE_ADDRESS_2, attributeValue.getAttributeValues());
					odrComponentAttribute = persistOdrComponentAttributes(odrServiceDetail, lmOdrComponent,
							"destinationAddressLineTwo", attributeValue.getAttributeValues());
				} else if (attrName.equalsIgnoreCase(SITE_ADDRESS_3)) {
					fullAddress.put(SITE_ADDRESS_3, attributeValue.getAttributeValues());
					odrComponentAttribute = persistOdrComponentAttributes(odrServiceDetail, lmOdrComponent,
							"destinationLocality", attributeValue.getAttributeValues());
				} else if (attrName.equalsIgnoreCase(STATE)) {
					fullAddress.put(STATE, attributeValue.getAttributeValues());
					odrComponentAttribute = persistOdrComponentAttributes(odrServiceDetail, lmOdrComponent,
							"destinationState", attributeValue.getAttributeValues());
				} else if (attrName.equalsIgnoreCase(PINCODE)) {
					fullAddress.put(PINCODE, attributeValue.getAttributeValues());
					odrComponentAttribute = persistOdrComponentAttributes(odrServiceDetail, lmOdrComponent,
							"destinationPincode", attributeValue.getAttributeValues());
				} else if (attrName.equalsIgnoreCase(VENDOR_NAME)) {
					fullAddress.put(PINCODE, attributeValue.getAttributeValues());
					odrComponentAttribute = persistOdrComponentAttributes(odrServiceDetail, lmOdrComponent,
							"vendorName", attributeValue.getAttributeValues());
				} else
					odrComponentAttribute = persistOdrComponentAttributes(odrServiceDetail, lmOdrComponent, attrName,
							attributeValue.getAttributeValues());
				odrComponentAttributes.add(odrComponentAttribute);
				processAdditionalSericeParam(attributeValue, userName, odrComponentAttribute);
			});
		});
		odrComponentAttributes.add(persistOdrComponentAttributes(odrServiceDetail, lmOdrComponent, "destinationCity",
				orderDirectRoutingCity.getName()));
		odrComponentAttributes.add(persistOdrComponentAttributes(odrServiceDetail, lmOdrComponent, "destinationCountry",
				orderDirectRoutingCity.getOrderDirectRouting().getCountry()));
		String addr = StringUtils.trimToEmpty(fullAddress.get(SITE_ADDRESS_1)) + CommonConstants.SPACE + StringUtils
				.trimToEmpty(fullAddress.get(SITE_ADDRESS_2)) + CommonConstants.SPACE + StringUtils
				.trimToEmpty(fullAddress.get(SITE_ADDRESS_3)) + CommonConstants.SPACE + StringUtils
				.trimToEmpty(orderDirectRoutingCity.getName()) + CommonConstants.SPACE + StringUtils
				.trimToEmpty(fullAddress.get(STATE)) + CommonConstants.SPACE + StringUtils.trimToEmpty(
				orderDirectRoutingCity.getOrderDirectRouting().getCountry()) + CommonConstants.SPACE + StringUtils
				.trimToEmpty(fullAddress.get(PINCODE));

		odrComponentAttributes
				.add(persistOdrComponentAttributes(odrServiceDetail, lmOdrComponent, "siteAddress", addr));

		// Processing enrichment Attributes in odr Component.
		List<OrderProductComponent> orderEnrichmentComponents = orderProductComponentRepository
				.findByReferenceIdAndAndMstProductFamilyAndReferenceName(orderDirectRoutingCity.getId(),
						mstProductFamily, EQUIPMENT_REF_NAME);
		orderEnrichmentComponents.forEach(orderProductComponent -> {
			List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
					.findByOrderProductComponent(orderProductComponent);
			orderProductComponentsAttributeValues.forEach(attributeValue -> {
				String attrName = attributeValue.getProductAttributeMaster().getName();
				processEnrichmentAttribute(odrServiceDetail, attrName, attributeValue.getAttributeValues());
				LOGGER.info("AttrName for Mg :: {},{}", attrName, attrName.length());
				LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				if (attrName.equalsIgnoreCase(DEMARCATION_BUILDING_NAME)) {
					odrComponentAttributes.add(persistOdrComponentAttributes(odrServiceDetail, lmOdrComponent,
							"demarcationBuildingName", attributeValue.getAttributeValues()));
				} else if (attrName.equalsIgnoreCase(DEMARCATION_FLOOR)) {
					odrComponentAttributes
							.add(persistOdrComponentAttributes(odrServiceDetail, lmOdrComponent, "demarcationFloor",
									attributeValue.getAttributeValues()));
				} else if (attrName.equalsIgnoreCase(TeamsDROdrConstants.DEMARCATION_WING)) {
					odrComponentAttributes
							.add(persistOdrComponentAttributes(odrServiceDetail, lmOdrComponent, "demarcationWing",
									attributeValue.getAttributeValues()));
				} else if (attrName.equalsIgnoreCase(DEMARCATION_ROOM)) {
					odrComponentAttributes
							.add(persistOdrComponentAttributes(odrServiceDetail, lmOdrComponent, "demarcationRoom",
									attributeValue.getAttributeValues()));
				} else {
					odrComponentAttributes.add(persistOdrComponentAttributes(odrServiceDetail, lmOdrComponent, attrName,
							attributeValue.getAttributeValues()));
				}
			});
			try {
				processCommonComponentAttr(String.valueOf(orderProductComponent.getReferenceId()), odrServiceDetail,
						userName, flowMapper, lmOdrComponent);
			} catch (TclCommonException e) {
				LOGGER.info("Error while persisting common component attributes {}", e.getMessage());
			}
		});

		// Processing mg attributes in odr component..
		orderDirectRoutingMgRepository.findByOrderDirectRoutingCityId(orderDirectRoutingCity.getId())
				.forEach(mediaGateway -> {
					OdrComponent odrComponent = persistOdrComponent(odrServiceDetail, mediaGateway.getName());
					odrServiceDetail.getOdrComponents().add(odrComponent);
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(odrServiceDetail,
							odrComponent, MG_QUANTITY, String.valueOf(mediaGateway.getQuantity())));
					List<OrderProductComponent> orderProductComponentsForMg = orderProductComponentRepository
							.findByReferenceIdAndAndMstProductFamilyAndReferenceName(mediaGateway.getId(),
									mstProductFamily, TEAMSDR_MEDIAGATEWAY_ATTRIBUTES);
					orderProductComponentsForMg.forEach(orderProductComponent -> {
						List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
								.findByOrderProductComponent(orderProductComponent);

						orderProductComponentsAttributeValues.forEach(attributeValue -> {
							String attrName = attributeValue.getProductAttributeMaster().getName();
							LOGGER.info("AttrName for Mg :: {},{}", attrName, attrName.length());
							processEnrichmentAttribute(odrServiceDetail, attrName, attributeValue.getAttributeValues());
							if (ALL_CHARGES.contains(attrName)) {
								LOGGER.info("Contains charge line item... :: {}", attrName);
								String hsnCode = fetchHsnCode(attributeValue);
								// If rental/outright charge adding AMC charge with it...
								if (CPE_RENTAL_CHARGES.equals(attrName) || CPE_OUTRIGHT_CHARGES.equals(attrName)) {
									orderProductComponentsAttributeValues.stream()
											.filter(attrValue -> CPE_AMC_CHARGES
													.equals(attrValue.getProductAttributeMaster().getName()))
											.findAny().ifPresent(attrValue -> {
												OrderPrice amcOrderPrice = orderPriceRepository
														.findByReferenceIdAndReferenceName(
																String.valueOf(attrValue.getId()), ATTRIBUTES);
												OrderPrice orderPrice = orderPriceRepository
														.findByReferenceIdAndReferenceName(
																String.valueOf(attributeValue.getId()), ATTRIBUTES);
												if (Objects.nonNull(orderPrice) && Objects.nonNull(amcOrderPrice)) {
													orderPrice
															.setEffectiveNrc(checkForNull(orderPrice.getEffectiveNrc())
																	+ checkForNull(amcOrderPrice.getEffectiveNrc()));
													orderPrice
															.setEffectiveArc(checkForNull(orderPrice.getEffectiveArc())
																	+ checkForNull(amcOrderPrice.getEffectiveArc()));
													orderPrice
															.setEffectiveMrc(checkForNull(orderPrice.getEffectiveMrc())
																	+ checkForNull(amcOrderPrice.getEffectiveMrc()));
													orderPrice.setEffectiveUsagePrice(checkForNull(
															orderPrice.getEffectiveUsagePrice())
															+ checkForNull(amcOrderPrice.getEffectiveUsagePrice()));
												}
											});

								}
								processTeamsDrServiceCommercialFromProductComponent(mediaGateway.getName(),
										mediaGateway.getName(), attrName, attributeValue.getAttributeValues(), hsnCode,
										String.valueOf(attributeValue.getId()), odrServiceDetail,
										odrTeamsDRServiceCommercials, userName, ENDPOINT, null);
							} else {
								OdrComponentAttribute odrComponentAttribute = persistOdrComponentAttributes(
										odrServiceDetail, odrComponent, attrName, attributeValue.getAttributeValues());
								processAdditionalSericeParam(attributeValue, userName, odrComponentAttribute);
								odrComponent.getOdrComponentAttributes().add(odrComponentAttribute);
							}
						});
					});
				});
		lmOdrComponent.getOdrComponentAttributes().addAll(odrComponentAttributes);
	}

	/**
	 * Method to process additional service param
	 *
	 * @param attributeValue
	 * @param userName
	 * @param odrComponentAttribute
	 */
	private void processAdditionalSericeParam(OrderProductComponentsAttributeValue attributeValue, String userName,
			OdrComponentAttribute odrComponentAttribute) {
		// To save additional Params...
		if (Objects.nonNull(attributeValue.getIsAdditionalParam())
				&& CommonConstants.Y.equals(attributeValue.getIsAdditionalParam())) {
			additionalServiceParamRepository.findById(Integer.parseInt(attributeValue.getAttributeValues()))
					.ifPresent(additionalServiceParams -> {
						OdrAdditionalServiceParam odrAdditionalServiceParam = new OdrAdditionalServiceParam();
						odrAdditionalServiceParam.setAttribute(additionalServiceParams.getAttribute());
						odrAdditionalServiceParam.setCategory(additionalServiceParams.getCategory());
						odrAdditionalServiceParam.setCreatedBy(userName);
						odrAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
						odrAdditionalServiceParam.setIsActive(CommonConstants.Y);
						odrAdditionalServiceParam.setValue(additionalServiceParams.getValue());
						odrAdditionalServiceParam = odrAdditionalServiceParamRepository.save(odrAdditionalServiceParam);
						odrComponentAttribute.setAttributeValue(String.valueOf(odrAdditionalServiceParam.getId()));
						odrComponentAttribute.setIsAdditionalParam(CommonConstants.Y);
					});
		}
	}

	/**
	 * Method to persist odr component attributes
	 *
	 * @param odrServiceDetail
	 * @param odrComponent
	 * @param attrName
	 * @param attrValue
	 * @return
	 */
	private OdrComponentAttribute persistOdrComponentAttributes(OdrServiceDetail odrServiceDetail,
			OdrComponent odrComponent, String attrName, String attrValue) {
		OdrComponentAttribute odrComponentAttribute = null;
		if (odrComponent.getId() != null) {
			odrComponentAttribute = odrComponentAttributeRepository.findByOdrComponentAndAttributeValue(odrComponent,
					attrName);
			if (odrComponentAttribute == null) {
				odrComponentAttribute = new OdrComponentAttribute();
				odrComponentAttribute.setCreatedBy(Utils.getSource());
				odrComponentAttribute.setCreatedDate(new Date());
			} else {
				odrComponentAttribute.setUpdatedBy(Utils.getSource());
				odrComponentAttribute.setUpdatedDate(new Date());
			}
		} else {
			odrComponentAttribute = new OdrComponentAttribute();
			odrComponentAttribute.setCreatedBy(Utils.getSource());
			odrComponentAttribute.setCreatedDate(new Date());
		}
		odrComponentAttribute.setOdrComponent(odrComponent);
		odrComponentAttribute.setAttributeName(attrName);
		odrComponentAttribute.setAttributeValue(attrValue);
		odrComponentAttribute.setIsActive(CommonConstants.Y);
		odrComponentAttribute.setOdrServiceDetail(odrServiceDetail);
		odrComponentAttribute.setUuid(Utils.generateUid());
		odrComponentAttribute.setAttributeAltValueLabel(attrValue);
		return odrComponentAttribute;
	}

	/**
	 * Method to persist odr component.
	 *
	 * @param odrServiceDetail
	 * @param componentName
	 * @return
	 */
	private OdrComponent persistOdrComponent(OdrServiceDetail odrServiceDetail, String componentName) {
		OdrComponent odrComponent = new OdrComponent();
		odrComponent.setComponentName(componentName);
		odrComponent.setOdrServiceDetail(odrServiceDetail);
		odrComponent.setCreatedBy(Utils.getSource());
		odrComponent.setCreatedDate(new Date());
		odrComponent.setSiteType("A");
		odrComponent.setIsActive(CommonConstants.Y);
		odrComponent.setUuid(Utils.generateUid());
		odrComponent.setOdrComponentAttributes(new HashSet<>());
		return odrComponent;
	}

	/**
	 *
	 * @param primaryserviceDetail
	 * @return
	 */
	private OdrComponent persistOdrComponent(OdrServiceDetail primaryserviceDetail) {
		OdrComponent odrComponent = new OdrComponent();
		odrComponent.setComponentName("LM");
		odrComponent.setOdrServiceDetail(primaryserviceDetail);
		odrComponent.setCreatedBy(Utils.getSource());
		odrComponent.setCreatedDate(new Date());
		// odrComponent.setSiteType(siteType);
		odrComponent.setIsActive(CommonConstants.Y);
		odrComponent.setUuid(Utils.generateUid());
		odrComponent.setOdrComponentAttributes(new HashSet<>());
		return odrComponent;
	}

	/**
	 * Method to process license
	 *
	 * @param orderTeamsDR
	 * @param mstProductFamily
	 * @param odrOrder
	 * @param odrServiceDetails
	 * @param odrContractInfo
	 * @param userName
	 * @param flowMapper
	 * @param odrTeamsDRServiceCommercials
	 */
	private void processLicense(OrderTeamsDR orderTeamsDR, MstProductFamily mstProductFamily, OdrOrder odrOrder,
			List<OdrServiceDetail> odrServiceDetails, OdrContractInfo odrContractInfo, String userName,
			Map<String, String> flowMapper, List<OdrTeamsDRServiceCommercial> odrTeamsDRServiceCommercials) {
		LOGGER.info("Processing license for teamsdr...");
		OdrServiceDetail odrServiceDetail = new OdrServiceDetail();
		odrServiceDetails.add(odrServiceDetail);
		processCommonServiceDetailAttr(odrServiceDetail, odrOrder, odrContractInfo, userName, flowMapper);
		odrServiceDetail.setServiceCommissionedDate(orderTeamsDR.getCreatedTime());
		odrServiceDetail.setMrc(orderTeamsDR.getMrc());
		odrServiceDetail.setNrc(orderTeamsDR.getNrc());
		odrServiceDetail.setArc(orderTeamsDR.getArc());
		odrServiceDetail.setErfPrdCatalogOfferingName(MICROSOFT_LICENSE);
		odrServiceDetail.setErfPrdCatalogFlavourName(MICROSOFT_LICENSE);

		OdrComponent lmOdrComponent = persistOdrComponent(odrServiceDetail, "LM");
		odrServiceDetail.getOdrComponents().add(lmOdrComponent);

		OdrComponent odrComponent = persistOdrComponent(odrServiceDetail, MICROSOFT_LICENSE);
		odrServiceDetail.getOdrComponents().add(odrComponent);

		Set<OdrComponentAttribute> odrComponentAttributes = new HashSet<>();

		// Storing the service attributes for each license..
		LOGGER.info("Storing license attributes for teamsdr...");
		List<OrderTeamsLicense> orderTeamsLicenses = orderTeamsLicenseRepository.findByOrderTeamsDR(orderTeamsDR);
		setCommonLicenseAttributes(orderTeamsDR, odrServiceDetail, odrComponent, orderTeamsLicenses);
		orderTeamsLicenses.forEach(orderTeamsLicense -> {

			// orderTeamsLicense.getLicenseName() + CommonConstants.SPACE +
			odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(odrServiceDetail, odrComponent,
					PROVIDER, orderTeamsLicense.getProvider()));
			odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(odrServiceDetail, odrComponent,
					NO_OF_LICENSES, String.valueOf(orderTeamsLicense.getNoOfLicenses())));
			odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(odrServiceDetail, odrComponent,
					SFDC_PRODUCT_NAME, orderTeamsLicense.getSfdcProductName()));

			LOGGER.info("Processing service commercial for license...");
			processTeamsDrServiceCommercialForLicense(orderTeamsLicense, odrServiceDetail, odrTeamsDRServiceCommercials,
					userName);

			LOGGER.info("Processing odr component attr for license...");
			processOdrCompAttrFromProductCompForLicense(orderTeamsLicense, mstProductFamily, odrServiceDetail,
					odrComponent, odrTeamsDRServiceCommercials, userName);

		});

		// To process management charges for license.
		processManagementChargesForLicense(orderTeamsDR, mstProductFamily, odrServiceDetail, lmOdrComponent,
				odrTeamsDRServiceCommercials, userName, odrComponentAttributes);
		lmOdrComponent.getOdrComponentAttributes().addAll(odrComponentAttributes);
	}

	/**
	 * Method to set common license attributes
	 *
	 * @param orderTeamsDR
	 * @param odrServiceDetail
	 * @param odrComponent
	 * @param orderTeamsLicenses
	 */
	private void setCommonLicenseAttributes(OrderTeamsDR orderTeamsDR, OdrServiceDetail odrServiceDetail,
			OdrComponent odrComponent, List<OrderTeamsLicense> orderTeamsLicenses) {

		OrderTeamsLicense orderTeamsLicense = orderTeamsLicenses.stream().findFirst().get();
		odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(odrServiceDetail, odrComponent,
				TOTAL_NO_OF_LICENSE, String.valueOf(orderTeamsDR.getNoOfUsers())));
		odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(odrServiceDetail, odrComponent,
				CONTRACT_PERIOD, String.valueOf(orderTeamsLicense.getContractPeriod())));
		odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(odrServiceDetail, odrComponent,
				AGREEMENT_TYPE, orderTeamsLicense.getAgreementType()));
	}

	/**
	 * Process management charge for license
	 *
	 * @param orderTeamsDR
	 * @param mstProductFamily
	 * @param odrServiceDetail
	 * @param odrComponent
	 * @param teamsDRServiceCommercials
	 * @param userName
	 * @param odrComponentAttributes
	 */
	public void processManagementChargesForLicense(OrderTeamsDR orderTeamsDR, MstProductFamily mstProductFamily,
			OdrServiceDetail odrServiceDetail, OdrComponent odrComponent,
			List<OdrTeamsDRServiceCommercial> teamsDRServiceCommercials, String userName,
			Set<OdrComponentAttribute> odrComponentAttributes) {

		List<OrderProductComponent> orderProductComponentsForLicense = orderProductComponentRepository
				.findByReferenceIdAndAndMstProductFamilyAndReferenceName(orderTeamsDR.getId(), mstProductFamily,
						TEAMSDR_LICENSE_ATTRIBUTES);

		orderProductComponentsForLicense.forEach(orderProductComponent -> orderProductComponentsAttributeValueRepository
				.findByOrderProductComponent(orderProductComponent).forEach(attrValue -> {
					String attrName = attrValue.getProductAttributeMaster().getName();
					LOGGER.info("AttrName for license :: {}", attrName);
					if (ALL_CHARGES.contains(attrName)) {
						String hsnCode = fetchHsnCode(attrValue);
						processTeamsDrServiceCommercialFromProductComponent(LICENSE_MANAGEMENT, LICENSE_MANAGEMENT,
								attrName, attrValue.getAttributeValues(), hsnCode, String.valueOf(attrValue.getId()),
								odrServiceDetail, teamsDRServiceCommercials, userName,MICROSOFT_LICENSE, orderTeamsDR.getNoOfUsers());
					}
					odrComponentAttributes.add(persistOdrComponentAttributes(odrServiceDetail, odrComponent, attrName,
							attrValue.getAttributeValues()));
				}));
	}

	/**
	 * Method to fetch hsnCode from additional service param.
	 *
	 * @param attributeValue
	 * @return
	 */
	private String fetchHsnCode(OrderProductComponentsAttributeValue attributeValue) {
		String hsnCode = null;
		if (Objects.nonNull(attributeValue) && Objects.nonNull(attributeValue.getIsAdditionalParam())) {
			Optional<AdditionalServiceParams> additionalServiceParams = additionalServiceParamRepository
					.findById(Integer.valueOf(attributeValue.getAttributeValues()));
			if (additionalServiceParams.isPresent()) {
				hsnCode = additionalServiceParams.get().getValue();
			}
		}
		LOGGER.info("HSN code :: {}", hsnCode);
		return hsnCode;
	}

	/**
	 * Method to process commercial and attributes from product component
	 *
	 * @param orderTeamsLicense
	 * @param mstProductFamily
	 * @param odrServiceDetail
	 * @param odrComponent
	 * @param teamsDRServiceCommercials
	 * @param userName
	 */
	public void processOdrCompAttrFromProductCompForLicense(OrderTeamsLicense orderTeamsLicense,
			MstProductFamily mstProductFamily, OdrServiceDetail odrServiceDetail, OdrComponent odrComponent,
			List<OdrTeamsDRServiceCommercial> teamsDRServiceCommercials, String userName) {

		List<OrderProductComponent> orderProductComponentsForLicense = orderProductComponentRepository
				.findByReferenceIdAndAndMstProductFamilyAndReferenceName(orderTeamsLicense.getId(), mstProductFamily,
						TEAMSDR_LICENSE_CHARGES);

		orderProductComponentsForLicense.forEach(orderProductComponent -> orderProductComponentsAttributeValueRepository
				.findByOrderProductComponent(orderProductComponent).forEach(attrValue -> {
					String attrName = attrValue.getProductAttributeMaster().getName();
					LOGGER.info("Attribute name for license :: {}", attrName);
					if (ALL_CHARGES.contains(attrName)) {
						String hsnCode = fetchHsnCode(attrValue);
						processTeamsDrServiceCommercialFromProductComponent(orderTeamsLicense.getLicenseName(),
								LICENSE_COMMERCIAL, attrName, attrValue.getAttributeValues(), hsnCode,
								String.valueOf(attrValue.getId()), odrServiceDetail, teamsDRServiceCommercials,
								userName,MICROSOFT_LICENSE, orderTeamsLicense.getNoOfLicenses());
					}
				}));
	}

	/**
	 * Method to process common service detail attributes.
	 *
	 * @param odrServiceDetail
	 * @param odrOrder
	 * @param odrContractInfo
	 * @param userName
	 * @param flowMapper
	 */
	private void processCommonServiceDetailAttr(OdrServiceDetail odrServiceDetail, OdrOrder odrOrder,
			OdrContractInfo odrContractInfo, String userName, Map<String, String> flowMapper) {
		odrServiceDetail.setOdrOrderUuid(odrOrder.getOpOrderCode());
		odrServiceDetail.setServiceRefId(Utils.generateUid());
		odrServiceDetail.setOdrContractInfo(odrContractInfo);
		odrServiceDetail.setOdrOrder(odrOrder);

		odrServiceDetail.setCreatedBy(userName);
		odrServiceDetail.setCreatedDate(new Date());
		odrServiceDetail.setUpdatedDate(new Date());
		odrServiceDetail.setUpdatedBy(userName);
		odrServiceDetail.setIsActive(CommonConstants.Y);
		odrServiceDetail.setIsIzo(CommonConstants.N);
		odrServiceDetail.setLocalItContactName(flowMapper.get(LeAttributesConstants.NAME));
		odrServiceDetail.setLocalItContactEmail(flowMapper.get(LeAttributesConstants.EMAIL_ID));
		odrServiceDetail.setLocalItContactMobile(flowMapper.get(LeAttributesConstants.NUMBER));
		odrServiceDetail.setErfPrdCatalogProductName(flowMapper.get(PRODUCT_NAME));
		odrServiceDetail.setTaxExemptionFlag("N");
		odrServiceDetail.setAccessType(PUBLIC_IP);
		odrServiceDetail.setOrderType(NEW);
		odrServiceDetail.setOrderCategory(NEW_ORDER);
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(flowMapper.get(PRODUCT_NAME),
				CommonConstants.BACTIVE);
		if (Objects.nonNull(mstProductFamily)) {
			odrServiceDetail.setErfPrdCatalogProductId(mstProductFamily.getId());
		}
	}

	/**
	 * Method to process teamsdrservice attributes.
	 *
	 * @param serviceDetail
	 * @param attrName
	 * @param attrValue
	 * @param category
	 * @param userName
	 * @param isAddParam
	 * @throws IllegalArgumentException
	 */
	private void processTeamsDRServiceAttribute(OdrServiceDetail serviceDetail, String attrName, String attrValue,
			String category, String userName, boolean isAddParam) throws IllegalArgumentException {
		OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
		odrServiceAttribute.setAttributeAltValueLabel(attrValue);
		odrServiceAttribute.setAttributeName(attrName);
		odrServiceAttribute.setAttributeValue(attrValue);
		odrServiceAttribute.setCategory(category);
		odrServiceAttribute.setCreatedBy(userName);
		odrServiceAttribute.setCreatedDate(new Date());
		odrServiceAttribute.setIsActive(CommonConstants.Y);
		odrServiceAttribute.setOdrServiceDetail(serviceDetail);
		odrServiceAttribute.setUpdatedBy(userName);
		odrServiceAttribute.setUpdatedDate(new Date());
		odrServiceAttribute.setIsAdditionalParam(isAddParam ? CommonConstants.Y : CommonConstants.N);
		serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
	}

	/**
	 * Method to process teamsdr service commercial for Mediagateway.
	 *
	 * @param orderDirectRoutingCity
	 * @param odrServiceDetail
	 * @param odrTeamsDRServiceCommercials
	 * @param username
	 */
	private void processTeamsDrServiceCommercialForSite(OrderDirectRoutingCity orderDirectRoutingCity,
			OdrServiceDetail odrServiceDetail, List<OdrTeamsDRServiceCommercial> odrTeamsDRServiceCommercials,
			String username) {
		OdrTeamsDRServiceCommercial odrTeamsDRServiceCommercial = new OdrTeamsDRServiceCommercial();
		odrTeamsDRServiceCommercial.setOdrServiceDetail(odrServiceDetail);
		odrTeamsDRServiceCommercial.setComponentName(orderDirectRoutingCity.getName());
		odrTeamsDRServiceCommercial.setComponentDesc(SITE_COMMERCIAL);
		odrTeamsDRServiceCommercial.setComponentType(SITE);
		// odrTeamsDRServiceCommercial.setHsnCode(orderDirectRoutingCity.getHsnCode());
		odrTeamsDRServiceCommercial.setQuantity(getMgQuantityForSite(orderDirectRoutingCity));
		// odrTeamsDRServiceCommercial.setUnitMrc(orderDirectRoutingCity.getm());
		// odrTeamsDRServiceCommercial.setUnitNrc(orderDirectRoutingCity.getUnitNrc());
		if (Objects.nonNull(orderDirectRoutingCity.getMrc())) {
			odrTeamsDRServiceCommercial.setMrc(orderDirectRoutingCity.getMrc().doubleValue());
		}
		if (Objects.nonNull(orderDirectRoutingCity.getNrc())) {
			odrTeamsDRServiceCommercial.setNrc(orderDirectRoutingCity.getNrc().doubleValue());
		}

		if (Objects.nonNull(orderDirectRoutingCity.getArc())) {
			odrTeamsDRServiceCommercial.setArc(orderDirectRoutingCity.getArc().doubleValue());
		}

		if (Objects.nonNull(orderDirectRoutingCity.getTcv())) {
			odrTeamsDRServiceCommercial.setTcv(orderDirectRoutingCity.getTcv().doubleValue());
		}
		// odrTeamsDRServiceCommercial.setContractType(orderDirectRoutingCity.getContractType());
		odrTeamsDRServiceCommercial.setCreatedBy(username);
		odrTeamsDRServiceCommercial.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		odrTeamsDRServiceCommercials.add(odrTeamsDRServiceCommercial);
	}

	/**
	 * Method to process teamsdr service commercial for License.
	 *
	 * @param orderTeamsLicense
	 * @param odrServiceDetail
	 * @param odrTeamsDRServiceCommercials
	 * @param username
	 */
	private void processTeamsDrServiceCommercialForLicense(OrderTeamsLicense orderTeamsLicense,
			OdrServiceDetail odrServiceDetail, List<OdrTeamsDRServiceCommercial> odrTeamsDRServiceCommercials,
			String username) {
		OdrTeamsDRServiceCommercial odrTeamsDRServiceCommercial = new OdrTeamsDRServiceCommercial();
		odrTeamsDRServiceCommercial.setOdrServiceDetail(odrServiceDetail);
		odrTeamsDRServiceCommercial.setComponentName(orderTeamsLicense.getLicenseName());
		odrTeamsDRServiceCommercial.setComponentDesc(LICENSE_COMMERCIAL);
		odrTeamsDRServiceCommercial.setComponentType(MICROSOFT_LICENSE);
		// odrTeamsDRServiceCommercial.setHsnCode(orderDirectRoutingCity.getHsnCode());
		odrTeamsDRServiceCommercial.setQuantity(orderTeamsLicense.getNoOfLicenses());
		// odrTeamsDRServiceCommercial.setUnitMrc(orderDirectRoutingCity.getm());
		// odrTeamsDRServiceCommercial.setUnitNrc(orderDirectRoutingCity.getUnitNrc());
		odrTeamsDRServiceCommercial.setMrc(orderTeamsLicense.getMrc());
		odrTeamsDRServiceCommercial.setNrc(orderTeamsLicense.getNrc());
		odrTeamsDRServiceCommercial.setArc(orderTeamsLicense.getArc());
		odrTeamsDRServiceCommercial.setTcv(orderTeamsLicense.getTcv());
		// odrTeamsDRServiceCommercial.setContractType(orderDirectRoutingCity.getContractType());
		odrTeamsDRServiceCommercial.setCreatedBy(username);
		odrTeamsDRServiceCommercial.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		odrTeamsDRServiceCommercials.add(odrTeamsDRServiceCommercial);
	}

	/**
	 * Method to process teamsdr service commercial for charge line items.
	 *
	 * @param componentName
	 * @param attrName
	 * @param attrValue
	 * @param referenceId
	 * @param odrServiceDetail
	 * @param odrTeamsDRServiceCommercials
	 * @param username
	 */
	private void processTeamsDrServiceCommercialFromProductComponent(String componentName, String componentDesc,
			String attrName, String attrValue, String hsnCode, String referenceId, OdrServiceDetail odrServiceDetail,
			List<OdrTeamsDRServiceCommercial> odrTeamsDRServiceCommercials, String username, String componentType, Integer quantity) {
		OdrTeamsDRServiceCommercial odrTeamsDRServiceCommercial = new OdrTeamsDRServiceCommercial();
		odrTeamsDRServiceCommercial.setOdrServiceDetail(odrServiceDetail);
		odrTeamsDRServiceCommercial.setComponentName(componentName);
		odrTeamsDRServiceCommercial.setComponentDesc(componentDesc);
		odrTeamsDRServiceCommercial.setComponentType(componentType);
		odrTeamsDRServiceCommercial.setChargeItem(attrName);
		odrTeamsDRServiceCommercial.setHsnCode(hsnCode);
		odrTeamsDRServiceCommercial.setQuantity(quantity);
		if (Objects.nonNull(referenceId)) {
			OrderPrice orderPrice = orderPriceRepository.findByReferenceIdAndReferenceName(referenceId, ATTRIBUTES);
			if (Objects.nonNull(orderPrice)) {
				odrTeamsDRServiceCommercial.setMrc(orderPrice.getEffectiveMrc());
				odrTeamsDRServiceCommercial.setNrc(orderPrice.getEffectiveNrc());
				odrTeamsDRServiceCommercial.setArc(orderPrice.getEffectiveArc());
				if (OVERAGE.equals(attrName)) {
					odrTeamsDRServiceCommercial.setOverage(orderPrice.getEffectiveUsagePrice());
				} else {
					odrTeamsDRServiceCommercial.setUsage(orderPrice.getEffectiveUsagePrice());
				}
				odrTeamsDRServiceCommercial.setUnitMrc(orderPrice.getMinimumMrc());
				odrTeamsDRServiceCommercial.setUnitNrc(orderPrice.getCatalogNrc());
			}
		}
		odrTeamsDRServiceCommercial.setHsnCode(hsnCode);
		odrTeamsDRServiceCommercial.setCreatedBy(username);
		odrTeamsDRServiceCommercial.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		odrTeamsDRServiceCommercials.add(odrTeamsDRServiceCommercial);
	}

	/**
	 * Method to get media gateway quantity for site.
	 *
	 * @param orderDirectRoutingCity
	 * @return
	 */
	private Integer getMgQuantityForSite(OrderDirectRoutingCity orderDirectRoutingCity) {
		return orderDirectRoutingMgRepository.findByOrderDirectRoutingCityId(orderDirectRoutingCity.getId()).stream()
				.mapToInt(OrderDirectRoutingMediaGateways::getQuantity).sum();
	}
}
