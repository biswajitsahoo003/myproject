package com.tcl.dias.batch.odr.gvpn;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tcl.dias.batch.odr.base.service.OrderService;
import com.tcl.dias.batch.odr.constants.OdrConstants;
import com.tcl.dias.batch.odr.mapper.OdrMapper;
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.GstAddressBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.Attachment;
import com.tcl.dias.oms.entity.entities.OdrAdditionalServiceParam;
import com.tcl.dias.oms.entity.entities.OdrAttachment;
import com.tcl.dias.oms.entity.entities.OdrComponent;
import com.tcl.dias.oms.entity.entities.OdrComponentAttribute;
import com.tcl.dias.oms.entity.entities.OdrContractInfo;
import com.tcl.dias.oms.entity.entities.OdrGstAddress;
import com.tcl.dias.oms.entity.entities.OdrOrder;
import com.tcl.dias.oms.entity.entities.OdrServiceAttribute;
import com.tcl.dias.oms.entity.entities.OdrServiceCommercial;
import com.tcl.dias.oms.entity.entities.OdrServiceDetail;
import com.tcl.dias.oms.entity.entities.OdrServiceSla;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIllSiteToService;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderSiteAddress;
import com.tcl.dias.oms.entity.entities.OrderVrfSites;
import com.tcl.dias.oms.entity.entities.QuoteSiteDifferentialCommercial;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.AttachmentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OdrAdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.OdrAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OdrComponentAttributeRepository;
import com.tcl.dias.oms.entity.repository.OdrComponentRepository;
import com.tcl.dias.oms.entity.repository.OdrContractInfoRepository;
import com.tcl.dias.oms.entity.repository.OdrOrderAttributeRepository;
import com.tcl.dias.oms.entity.repository.OdrOrderRepository;
import com.tcl.dias.oms.entity.repository.OdrServiceAttributeRepository;
import com.tcl.dias.oms.entity.repository.OdrServiceCommercialRepository;
import com.tcl.dias.oms.entity.repository.OdrServiceDetailRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderCloudRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteAddressRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteSiteDifferentialCommercialRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This class is used to define the Gvpn related Order flat table
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GvpnOdrService extends OrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GvpnOdrService.class);

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderToLeRepository orderToLeRepository;

	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;

	@Autowired
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@Autowired
	OdrOrderRepository odrOrderRepository;

	@Autowired
	OdrContractInfoRepository odrContractInfoRepository;

	@Autowired
	OdrOrderAttributeRepository odrOrderAttributeRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	OdrServiceDetailRepository odrServiceDetailRepository;

	@Autowired
	OdrServiceCommercialRepository odrServiceCommercialRepository;

	@Autowired
	OdrServiceAttributeRepository odrServiceAttributeRepository;

	@Autowired
	OrderSiteFeasibilityRepository orderSiteFeasibilityRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository OrderProductComponentsAttributeValueRepository;

	@Autowired
	OrderIllSiteSlaRepository orderIllSiteSlaRepository;

	@Autowired
	OrderCloudRepository orderCloudRepository;

	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;

	@Value("${rabbitmq.location.detail}")
	String locationQueue;

	@Value("${rabbitmq.pricing.ipc.location}")
	String ipcPricingLocationQueue;

	@Value("${rabbitmq.poplocation.detail}")
	String poplocationQueue;

	@Value("${rabbitmq.location.localitcontact}")
	String localItQueue;

	@Value("${odr.attachment.details}")
	String attachmentQueue;

	@Value("${rabbitmq.location.demarcation}")
	String demarcationQueue;

	@Value("${rabbitmq.o2c.fulfillmentdate}")
	String o2cFulfillmentQueue;

	@Value("${rabbitmq.o2c.oe.fulfillmentdate}")
	String orderEnrichmentFulfillQueue;

	@Value("${rabbitmq.cpe.bom.resource.queue}")
	String productBomQueue;

	@Value("${rabbitmq.billing.contact.queue}")
	String billingContactQueue;

	@Value("${rabbitmq.si.order.get.queue}")
	String getSIOrderQueue;

	@Value("${rabbitmq.site.gst.queue}")
	String siteGstQueue;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	OdrMapper odrMapper;

	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Autowired
	OdrAttachmentRepository odrAttachmentRepository;

	@Autowired
	AttachmentRepository attachmentRepository;

	@Autowired
	OdrAdditionalServiceParamRepository odrAdditionalServiceParamRepository;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	OrderPriceRepository orderPriceRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	OrderIllSitesRepository orderIllSiteRepository;

	@Autowired
	OdrComponentAttributeRepository odrComponentAttributeRepository;

	@Autowired
	OdrComponentRepository odrComponentRepository;

	@Autowired
	OrderIllSiteToServiceRepository orderIllSiteToServiceRepository;

	@Autowired
	OrderSiteAddressRepository orderSiteAddressRepository;

	@Value("${rabbitmq.cpe.bom.ntw.products.queue}")
	String cpeBomNtwProductsQueue;

	@Autowired
	QuoteSiteDifferentialCommercialRepository quoteSiteDifferentialCommercialRepository;

	protected String getReferenceName() {
		return OdrConstants.GVPN_SITES;
	}

	public void processVRFSite(Map<String, String> flowMapper, OrderProductSolution orderProductSolution,
			List<OdrServiceDetail> odrServiceDetails, OdrContractInfo odrContractInfo, OdrOrder odrOrder,
			String username, List<OdrServiceCommercial> odrServiceCommercials, List<OrderVrfSites> orderVrfSites) {
		LOGGER.info("Inside processVRFSite");
		for (OrderVrfSites orderVrfSite : orderVrfSites) {
			Boolean isMaster = (orderVrfSite.getVrfType() != null
					&& orderVrfSite.getVrfType().equalsIgnoreCase("master")) ? true : false;
			processVrfSiteDetails(flowMapper, orderProductSolution, odrServiceDetails, odrContractInfo, odrOrder, username,
					odrServiceCommercials, orderVrfSite.getOrderIllSite(), orderVrfSite, isMaster,orderVrfSites);
		}
	}

	private OdrServiceDetail processVrfSiteDetails(Map<String, String> flowMapper, OrderProductSolution orderProductSolution,
			List<OdrServiceDetail> odrServiceDetails, OdrContractInfo odrContractInfo, OdrOrder odrOrder,
			String username, List<OdrServiceCommercial> odrServiceCommercials, OrderIllSite orderIllSite,
			OrderVrfSites orderVrfSites, Boolean isMaster, List<OrderVrfSites> orderVrfSitesList) {
		LOGGER.info("Inside processVrfSiteDetails");
		OdrServiceDetail primaryserviceDetail = new OdrServiceDetail();
		try {
			String type = null;
			if ("MACD".equals(odrOrder.getOrderType()) || "CANCELLATION".equals(odrOrder.getOrderType())) {
				if (flowMapper.get(orderIllSite.getSiteCode()) != null
						&& flowMapper.get(orderIllSite.getSiteCode() + "-SEC") != null) {
					LOGGER.info("Into dual");
					type = "dual";
				} else if (flowMapper.get(orderIllSite.getSiteCode() + "-SEC") != null) {
					LOGGER.info("Into secondary");
					type = "secondary";
				} else if (flowMapper.get(orderIllSite.getSiteCode()) != null) {
					LOGGER.info("Into primary");
					type = "primary";
				}
			}
			
			
			primaryserviceDetail.setMasterServiceId(orderVrfSites.getId());
			
			OdrComponent primaryOdrComponent = persistOdrComponent(primaryserviceDetail);
			Set<OdrComponentAttribute> odrComponentAttributes = primaryOdrComponent.getOdrComponentAttributes();
			primaryserviceDetail.setFlowStatus("NEW");
			primaryserviceDetail.setOdrOrderUuid(odrOrder.getOpOrderCode());
			if(isMaster) {
				primaryserviceDetail.setIsMultiVrf(CommonConstants.Y);
			}else {
				primaryserviceDetail.setIsMultiVrf(CommonConstants.N);
			}
			primaryserviceDetail.setMultiVrfSolution(CommonConstants.Y);
			odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
					"siteCode", orderIllSite.getSiteCode()));
			primaryserviceDetail.setServiceRefId(orderIllSite.getSiteCode());
			primaryserviceDetail.setOdrContractInfo(odrContractInfo);
			primaryserviceDetail.setOdrOrder(odrOrder);
			primaryserviceDetail.setServiceCommissionedDate(
					orderIllSite.getEffectiveDate() != null ? orderIllSite.getEffectiveDate()
							: orderIllSite.getRequestorDate());
			primaryserviceDetail.setRrfsDate(orderIllSite.getRequestorDate());
			primaryserviceDetail.setArc(orderIllSite.getArc());
			primaryserviceDetail.setMrc(orderIllSite.getMrc());
			primaryserviceDetail.setNrc(orderIllSite.getNrc());
			primaryserviceDetail.setErfPrdCatalogOfferingName(flowMapper.get("offeringName"));
			primaryserviceDetail.setCreatedBy(username);
			primaryserviceDetail.setCreatedDate(new Date());
			primaryserviceDetail.setUpdatedDate(new Date());
			primaryserviceDetail.setUpdatedBy(username);
			primaryserviceDetail.setIsActive(CommonConstants.Y);
			primaryserviceDetail.setIsIzo(CommonConstants.N);
			primaryserviceDetail.setIsAmended(flowMapper.get("isAmended"));
			String absorbedOrPassedOn = null;
			List<OrderIllSiteToService> orderIllSiteToServiceList = orderIllSiteToServiceRepository
					.findByOrderIllSite(orderIllSite);
			if (orderIllSiteToServiceList != null && !orderIllSiteToServiceList.isEmpty()) {
				absorbedOrPassedOn = orderIllSiteToServiceList.get(0).getAbsorbedOrPassedOn();
				LOGGER.info("Absorbed or passed on {} order site Id {}", absorbedOrPassedOn, orderIllSite.getId());
				if (absorbedOrPassedOn != null) {
					if ("Absorbed".equalsIgnoreCase(absorbedOrPassedOn)) {
						primaryserviceDetail.setAbsorbedOrPassedOn(absorbedOrPassedOn);
						primaryserviceDetail.setCancellationCharges(0D);
					} else if ("Passed On".equalsIgnoreCase(absorbedOrPassedOn)) {
						primaryserviceDetail.setAbsorbedOrPassedOn(absorbedOrPassedOn);
						primaryserviceDetail.setCancellationCharges(orderIllSite.getNrc());
					}
					LOGGER.info("cancellation charges for order site id {} is {}", orderIllSite.getId(),
							primaryserviceDetail.getCancellationCharges());
				}

				primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail,
						"cancellationReason", orderIllSiteToServiceList.get(0).getCancellationReason()));
				primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail,
						"leadToRFSDate", orderIllSiteToServiceList.get(0).getLeadToRFSDate()));
				primaryserviceDetail.getOdrServiceAttributes()
						.add(persistOdrServiceAttributes(primaryserviceDetail, "effectiveDateOfChange",
								String.valueOf(orderIllSiteToServiceList.get(0).getEffectiveDateOfChange())));
			}
			if (flowMapper.get("amended_o2c_serviceId-" + orderIllSite.getSiteCode()) != null) {
				primaryserviceDetail
						.setAmendedServiceId(flowMapper.get("amended_o2c_serviceId-" + orderIllSite.getSiteCode()));
			}

			if ("MACD".equals(odrOrder.getOrderType())
					&& flowMapper.containsKey(String.valueOf(orderIllSite.getId()))) {
				LOGGER.info("Order Sub Category");
				primaryserviceDetail.setOrderSubCategory(flowMapper.get(String.valueOf(orderIllSite.getId())));
			}
			if (orderIllSite.getIsTaxExempted() != null
					&& orderIllSite.getIsTaxExempted().equals(CommonConstants.BACTIVE)) {
				primaryserviceDetail.setTaxExemptionFlag(CommonConstants.Y);
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"taxExemption", CommonConstants.Y));
			} else {
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"taxExemption", CommonConstants.N));
			}
			if(isMaster) {
				primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail,
						"MASTER_VRF_FLAG",CommonConstants.Y));
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"MASTER_VRF_FLAG", CommonConstants.Y));
			}else {
				primaryserviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(primaryserviceDetail,
						"MASTER_VRF_FLAG",CommonConstants.N));
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"MASTER_VRF_FLAG", CommonConstants.N));
			}
			primaryserviceDetail.setErfPrdCatalogProductName(flowMapper.get("productName"));
			primaryserviceDetail.setErfPrdCatalogProductId(Objects.nonNull(flowMapper.get("productCode"))
					? Integer.valueOf(flowMapper.get("productCode").trim())
					: null);
			if (("MACD".equals(odrOrder.getOrderType()) && "ADD_SITE".equals(odrOrder.getOrderCategory()))
					|| ("MACD".equals(odrOrder.getOrderType())
							&& Objects.nonNull(primaryserviceDetail.getOrderSubCategory())
							&& primaryserviceDetail.getOrderSubCategory().toLowerCase().contains("parallel"))) {
				LOGGER.info("Parent UUID {}", flowMapper.get(orderIllSite.getSiteCode()));
				LOGGER.info("Order Sub Category {}", primaryserviceDetail.getOrderSubCategory());
				primaryserviceDetail.setParentUuid(flowMapper.get(orderIllSite.getSiteCode()));
			} else if ("MACD".equals(odrOrder.getOrderType()) && !"ADD_SITE".equals(odrOrder.getOrderCategory())
					&& (Objects.isNull(primaryserviceDetail.getOrderSubCategory()) || (Objects
							.nonNull(primaryserviceDetail.getOrderSubCategory())
							&& !primaryserviceDetail.getOrderSubCategory().toLowerCase().contains("parallel")))) {
				LOGGER.info("UUID {}", orderIllSite.getSiteCode());
				primaryserviceDetail.setUuid(flowMapper.get(orderIllSite.getSiteCode()));
			} else if ("CANCELLATION".equalsIgnoreCase(odrOrder.getOrderType())) {
				LOGGER.info("CANCELLATION UUID {}", orderIllSite.getSiteCode());
				primaryserviceDetail.setUuid(flowMapper.get(orderIllSite.getSiteCode()));
			}
			if(!isMaster) {
				primaryserviceDetail.setUuid(orderVrfSites.getTpsServiceId());
			}
			LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			LOGGER.info("Order Site address for Reference Id {}", orderIllSite.getId());
			List<QuoteSiteDifferentialCommercial> quoteSiteDifferentialCommercialList = quoteSiteDifferentialCommercialRepository
					.findByQuoteSiteCodeAndServiceType(orderIllSite.getSiteCode(), "primary");
			if (quoteSiteDifferentialCommercialList != null && !quoteSiteDifferentialCommercialList.isEmpty()) {
				LOGGER.info("quote site differential commercial order site code {} diff mrc {}, diff nrc {}",
						orderIllSite.getSiteCode(), quoteSiteDifferentialCommercialList.get(0).getDifferentialMrc(),
						quoteSiteDifferentialCommercialList.get(0).getDifferentialNrc());
				primaryserviceDetail
						.setDifferentialMrc(quoteSiteDifferentialCommercialList.get(0).getDifferentialMrc());
				primaryserviceDetail
						.setDifferentialNrc(quoteSiteDifferentialCommercialList.get(0).getDifferentialNrc());
			}

			OrderSiteAddress orderSiteAddress = orderSiteAddressRepository
					.findByReferenceIdAndReferenceNameAndSiteType(orderIllSite.getId().toString(), "ILL_SITES", "b");
			if (orderSiteAddress != null) {
				LOGGER.info("Order Site Loaded with id {} ", orderSiteAddress.getId());
				String addr = StringUtils.trimToEmpty(orderSiteAddress.getAddressLineOne()) + CommonConstants.SPACE
						+ StringUtils.trimToEmpty(orderSiteAddress.getAddressLineTwo()) + CommonConstants.SPACE
						+ StringUtils.trimToEmpty(orderSiteAddress.getLocality()) + CommonConstants.SPACE
						+ StringUtils.trimToEmpty(orderSiteAddress.getCity()) + CommonConstants.SPACE
						+ StringUtils.trimToEmpty(orderSiteAddress.getState()) + CommonConstants.SPACE
						+ StringUtils.trimToEmpty(orderSiteAddress.getCountry()) + CommonConstants.SPACE
						+ StringUtils.trimToEmpty(orderSiteAddress.getPincode());
				primaryserviceDetail.setSiteAddress(addr);
				primaryserviceDetail.setSiteLatLang(orderSiteAddress.getLatLong());
				primaryserviceDetail.setLatLong(orderSiteAddress.getLatLong());
				primaryserviceDetail.setDestinationCountry(orderSiteAddress.getCountry());
				primaryserviceDetail.setDestinationCity(orderSiteAddress.getCity());
				primaryserviceDetail.setDestinationAddressLineOne(orderSiteAddress.getAddressLineOne());
				primaryserviceDetail.setDestinationAddressLineTwo(orderSiteAddress.getAddressLineTwo());
				primaryserviceDetail.setDestinationLocality(orderSiteAddress.getLocality());
				primaryserviceDetail.setDestinationState(orderSiteAddress.getState());
				primaryserviceDetail.setDestinationPincode(orderSiteAddress.getPincode());
				primaryserviceDetail.setErfLocSiteAddressId(String.valueOf(orderSiteAddress.getErfLocationLocId()));
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"siteAddressId", orderSiteAddress.getErfLocationLocId() + ""));
				odrComponentAttributes.add(
						persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "siteAddress", addr));
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"latLong", orderSiteAddress.getLatLong()));
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"destinationCountry", orderSiteAddress.getCountry()));
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"destinationCity", orderSiteAddress.getCity()));
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"destinationAddressLineOne", orderSiteAddress.getAddressLineOne()));
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"destinationAddressLineTwo", orderSiteAddress.getAddressLineTwo()));
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"destinationLocality", orderSiteAddress.getLocality()));
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"destinationState", orderSiteAddress.getState()));
				odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
						"destinationPincode", orderSiteAddress.getPincode()));
			} else {
				String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
						String.valueOf(orderIllSite.getErfLocSitebLocationId()));
				if (StringUtils.isNotBlank(locationResponse)) {
					AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
							AddressDetail.class);
					if (addressDetail != null) {
						String addr = StringUtils.trimToEmpty(addressDetail.getAddressLineOne()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getLocality()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getCity()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getState()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getCountry()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getPincode());
						primaryserviceDetail.setSiteAddress(addr);
						primaryserviceDetail.setSiteLatLang(addressDetail.getLatLong());
						primaryserviceDetail.setLatLong(addressDetail.getLatLong());
						primaryserviceDetail.setDestinationCountry(addressDetail.getCountry());
						primaryserviceDetail.setDestinationCity(addressDetail.getCity());
						primaryserviceDetail.setDestinationAddressLineOne(addressDetail.getAddressLineOne());
						primaryserviceDetail.setDestinationAddressLineTwo(addressDetail.getAddressLineTwo());
						primaryserviceDetail.setDestinationLocality(addressDetail.getLocality());
						primaryserviceDetail.setDestinationState(addressDetail.getState());
						primaryserviceDetail.setDestinationPincode(addressDetail.getPincode());
						primaryserviceDetail
								.setErfLocSiteAddressId(String.valueOf(orderIllSite.getErfLocSitebLocationId()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "siteAddressId", orderIllSite.getErfLocSitebLocationId() + ""));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "siteAddress", addr));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "latLong", addressDetail.getLatLong()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationCountry", addressDetail.getCountry()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationCity", addressDetail.getCity()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationAddressLineOne", addressDetail.getAddressLineOne()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationAddressLineTwo", addressDetail.getAddressLineTwo()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationLocality", addressDetail.getLocality()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationState", addressDetail.getState()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "destinationPincode", addressDetail.getPincode()));
					}
				}
			}
			/*
			 * String cityCode = CommonConstants.EMPTY; if
			 * (primaryserviceDetail.getDestinationCity() != null &&
			 * primaryserviceDetail.getDestinationCity().length() > 4) { cityCode =
			 * primaryserviceDetail.getDestinationCity().substring(0, 4); } else { cityCode
			 * = primaryserviceDetail.getDestinationCity(); } String serviceCode =
			 * getServiceCode(flowMapper, cityCode);
			 * primaryserviceDetail.setUuid(serviceCode);
			 */

			LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String demarReponse = (String) mqUtils.sendAndReceive(demarcationQueue,
					String.valueOf(orderIllSite.getErfLocSitebLocationId()));
			if (demarReponse != null) {
				@SuppressWarnings("unchecked")
				Map<String, Object> demarDetails = (Map<String, Object>) Utils.convertJsonToObject(demarReponse,
						Map.class);
				if (demarDetails != null) {
					primaryserviceDetail.setDemarcationApartment(
							demarDetails.get("BUILDING_NAME") != null ? (String) demarDetails.get("BUILDING_NAME")
									: null);
					primaryserviceDetail.setDemarcationFloor(
							demarDetails.get("FLOOR") != null ? (String) demarDetails.get("FLOOR") : null);
					primaryserviceDetail.setDemarcationRack(
							demarDetails.get("WING") != null ? (String) demarDetails.get("WING") : null);
					primaryserviceDetail.setDemarcationRoom(
							demarDetails.get("ROOM") != null ? (String) demarDetails.get("ROOM") : null);
					odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent,
							"demarcationBuildingName",
							demarDetails.get("BUILDING_NAME") != null ? (String) demarDetails.get("BUILDING_NAME")
									: null));
					odrComponentAttributes.add(
							persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "demarcationFloor",
									demarDetails.get("FLOOR") != null ? (String) demarDetails.get("FLOOR") : null));
					odrComponentAttributes.add(
							persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "demarcationWing",
									demarDetails.get("WING") != null ? (String) demarDetails.get("WING") : null));
					odrComponentAttributes.add(
							persistOdrComponentAttributes(primaryserviceDetail, primaryOdrComponent, "demarcationRoom",
									demarDetails.get("ROOM") != null ? (String) demarDetails.get("ROOM") : null));
				}
			}

			if (orderIllSite.getErfLocSiteaLocationId() != null) {
				LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String popResponse = (String) mqUtils.sendAndReceive(locationQueue,
						String.valueOf(orderIllSite.getErfLocSiteaLocationId()));
				if (popResponse != null) {
					AddressDetail popAddressDetail = (AddressDetail) Utils.convertJsonToObject(popResponse,
							AddressDetail.class);
					if (popAddressDetail != null) {
						String popAddr = StringUtils.trimToEmpty(popAddressDetail.getAddressLineOne())
								+ CommonConstants.SPACE + StringUtils.trimToEmpty(popAddressDetail.getAddressLineTwo())
								+ CommonConstants.SPACE + StringUtils.trimToEmpty(popAddressDetail.getLocality())
								+ CommonConstants.SPACE + StringUtils.trimToEmpty(popAddressDetail.getCity())
								+ CommonConstants.SPACE + StringUtils.trimToEmpty(popAddressDetail.getState())
								+ CommonConstants.SPACE + StringUtils.trimToEmpty(popAddressDetail.getCountry())
								+ CommonConstants.SPACE + StringUtils.trimToEmpty(popAddressDetail.getPincode());
						primaryserviceDetail.setPopSiteAddress(popAddr);
						primaryserviceDetail.setPopSiteCode(orderIllSite.getErfLocSiteaSiteCode());
						primaryserviceDetail.setSourceCountry(popAddressDetail.getCountry());
						primaryserviceDetail.setSourceCity(popAddressDetail.getCity());
						primaryserviceDetail.setSourceAddressLineOne(popAddressDetail.getAddressLineOne());
						primaryserviceDetail.setSourceAddressLineTwo(popAddressDetail.getAddressLineTwo());
						primaryserviceDetail.setSourceLocality(popAddressDetail.getLocality());
						primaryserviceDetail.setSourcePincode(popAddressDetail.getPincode());
						primaryserviceDetail.setSourceState(popAddressDetail.getState());

						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "popSiteAddressId", orderIllSite.getErfLocSiteaLocationId() + ""));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "popSiteAddress", popAddr));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "popSiteCode", orderIllSite.getErfLocSiteaSiteCode()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "sourceCountry", popAddressDetail.getCountry()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "sourceCity", popAddressDetail.getCity()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "sourceAddressLineOne", popAddressDetail.getAddressLineOne()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "sourceAddressLineTwo", popAddressDetail.getAddressLineTwo()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "sourceLocality", popAddressDetail.getLocality()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "sourcePincode", popAddressDetail.getPincode()));
						odrComponentAttributes.add(persistOdrComponentAttributes(primaryserviceDetail,
								primaryOdrComponent, "sourceState", popAddressDetail.getState()));
					}
				}
			}
			if (("MACD".equals(odrOrder.getOrderType()) || "CANCELLATION".equals(odrOrder.getOrderType()))
					&& !odrOrder.getOpOrderCode().contains("IPC")) {
				if (type != null && (type.equals("primary") || type.equals("dual"))) {
					odrServiceDetails.add(primaryserviceDetail);
				}
			} else {
				odrServiceDetails.add(primaryserviceDetail);
			}

			LOGGER.info("Saving secondary Attributes");
			OdrServiceDetail secondaryserviceDetail = new OdrServiceDetail();
			OdrComponent secondaryOdrComponent = new OdrComponent();
			BeanUtils.copyProperties(primaryserviceDetail, secondaryserviceDetail);
			BeanUtils.copyProperties(primaryOdrComponent, secondaryOdrComponent);
			primaryOdrComponent.setOdrServiceDetail(primaryserviceDetail);
			primaryOdrComponent.setOdrComponentAttributes(new HashSet<>(odrComponentAttributes));
			primaryOdrComponent.setSiteType("A");
			primaryserviceDetail.setOdrComponents(null);
			primaryserviceDetail.getOdrComponents().add(primaryOdrComponent);
			secondaryserviceDetail.setOdrServiceSlas(new HashSet<>());
			// persistAttachments(flowMapper, username, orderIllSite, primaryserviceDetail);
			processCommonComponentAttrMultiVrf(String.valueOf(orderIllSite.getId()), primaryserviceDetail, username,
					flowMapper, primaryOdrComponent,orderVrfSites);
			processProductComponentAttrMultiVrf(String.valueOf(orderIllSite.getId()), primaryserviceDetail, "primary",
					username, false, odrServiceCommercials, primaryOdrComponent,orderVrfSites,isMaster);
			processVrfComponentAttributes(orderVrfSites, primaryserviceDetail, primaryOdrComponent, username);
			LOGGER.info("Total Number of primary Service attributes {}",
					primaryserviceDetail.getOdrServiceAttributes().size());
			processSiteSla(orderIllSite, primaryserviceDetail, username);
			secondaryOdrComponent.setOdrComponentAttributes(new HashSet<>(odrComponentAttributes));
			if (("MACD".equals(odrOrder.getOrderType()) || "CANCELLATION".equals(odrOrder.getOrderType()))
					&& flowMapper.get(orderIllSite.getSiteCode() + "-SEC") == null) {
				secondaryserviceDetail = null;
			} else {
				LOGGER.info("Getting into the Secondary Flow");
				secondaryserviceDetail = processProductComponentAttrMultiVrf(String.valueOf(orderIllSite.getId()),
						secondaryserviceDetail, "secondary", username, false, odrServiceCommercials,
						secondaryOdrComponent,orderVrfSites,isMaster);
				if ("MACD".equals(odrOrder.getOrderType())
						&& flowMapper.containsKey(String.valueOf(orderIllSite.getId()))) {
					LOGGER.info("Order Sub Category");
					secondaryserviceDetail.setOrderSubCategory(flowMapper.get(String.valueOf(orderIllSite.getId())));
				}
			}
			if (secondaryserviceDetail != null) {
				if (("MACD".equals(odrOrder.getOrderType()) && "ADD_SITE".equals(odrOrder.getOrderCategory()))
						|| ("MACD".equals(odrOrder.getOrderType())
								&& Objects.nonNull(secondaryserviceDetail.getOrderSubCategory())
								&& secondaryserviceDetail.getOrderSubCategory().toLowerCase().contains("parallel"))) {
					LOGGER.info("Parent UUID {}", flowMapper.get(orderIllSite.getSiteCode() + "-SEC"));
					LOGGER.info("Order Sub Category {}", secondaryserviceDetail.getOrderSubCategory());
					secondaryserviceDetail.setParentUuid(flowMapper.get(orderIllSite.getSiteCode() + "-SEC"));
				} else if ("MACD".equals(odrOrder.getOrderType()) && !"ADD_SITE".equals(odrOrder.getOrderCategory())
						&& (Objects.isNull(secondaryserviceDetail.getOrderSubCategory()) || (Objects
								.nonNull(secondaryserviceDetail.getOrderSubCategory())
								&& !secondaryserviceDetail.getOrderSubCategory().toLowerCase().contains("parallel")))) {
					LOGGER.info("UUID {}", orderIllSite.getSiteCode());
					secondaryserviceDetail.setUuid(flowMapper.get(orderIllSite.getSiteCode() + "-SEC"));
				} else if ("CANCELLATION".equalsIgnoreCase(odrOrder.getOrderType())) {
					LOGGER.info("CANCELLATION UUID {}", orderIllSite.getSiteCode());
					secondaryserviceDetail.setUuid(flowMapper.get(orderIllSite.getSiteCode() + "-SEC"));

				}
				if (flowMapper.get("amended_o2c_serviceId-sec-" + orderIllSite.getSiteCode()) != null) {
					secondaryserviceDetail.setAmendedServiceId(
							flowMapper.get("amended_o2c_serviceId-sec-" + orderIllSite.getSiteCode()));
				}
				List<QuoteSiteDifferentialCommercial> quoteSiteDifferentialCommercialListSecondary = quoteSiteDifferentialCommercialRepository
						.findByQuoteSiteCodeAndServiceType(orderIllSite.getSiteCode(), "secondary");
				if (quoteSiteDifferentialCommercialListSecondary != null
						&& !quoteSiteDifferentialCommercialListSecondary.isEmpty()) {
					LOGGER.info("quote site differential commercial order site code {} diff mrc {}, diff nrc {}",
							orderIllSite.getSiteCode(),
							quoteSiteDifferentialCommercialListSecondary.get(0).getDifferentialMrc(),
							quoteSiteDifferentialCommercialListSecondary.get(0).getDifferentialNrc());
					secondaryserviceDetail.setDifferentialMrc(
							quoteSiteDifferentialCommercialListSecondary.get(0).getDifferentialMrc());
					secondaryserviceDetail.setDifferentialNrc(
							quoteSiteDifferentialCommercialListSecondary.get(0).getDifferentialNrc());
				}
				secondaryOdrComponent.setOdrServiceDetail(secondaryserviceDetail);
				secondaryOdrComponent.setSiteType("A");
				secondaryserviceDetail.setOdrComponents(null);
				secondaryserviceDetail.getOdrComponents().add(secondaryOdrComponent);
				primaryserviceDetail.setServiceRefId(orderIllSite.getSiteCode() + "-PRIM");
				secondaryserviceDetail.setServiceRefId(orderIllSite.getSiteCode() + "-SECO");
				// persistAttachments(flowMapper, username, orderIllSite,
				// secondaryserviceDetail);
				processCommonComponentAttrMultiVrf(String.valueOf(orderIllSite.getId()), secondaryserviceDetail, username,
						flowMapper, secondaryOdrComponent,orderVrfSites);
				processVrfComponentAttributes(orderVrfSites, secondaryserviceDetail, secondaryOdrComponent, username);
				processSiteSla(orderIllSite, secondaryserviceDetail, username);
				/*
				 * String secServiceCode = getServiceCode(flowMapper, cityCode);
				 * secondaryserviceDetail.setUuid(secServiceCode);
				 */
				if (("MACD".equals(odrOrder.getOrderType()) || "CANCELLATION".equals(odrOrder.getOrderType()))
						&& !odrOrder.getOpOrderCode().contains("IPC")) {
					if (type != null && (type.equals("secondary") || type.equals("dual"))) {
						LOGGER.info("Into secondary");
						odrServiceDetails.add(secondaryserviceDetail);
					}
				} else {
					odrServiceDetails.add(secondaryserviceDetail);
				}

				// primaryserviceDetail.setPriSecServiceLink(secondaryserviceDetail.getUuid());

				primaryserviceDetail.setErfPriSecServiceLinkId(secondaryserviceDetail.getId());
				LOGGER.info("Total Number of seondary Service attributes {}",
						secondaryserviceDetail.getOdrServiceAttributes().size());
				// secondaryserviceDetail.setUuid(primaryserviceDetail.getUuid());
				LOGGER.info("Secondary attributes is processed for id {}", secondaryserviceDetail.getUuid());
				for (OdrComponent odrComp : secondaryserviceDetail.getOdrComponents()) {
					odrComp.setOdrServiceDetail(secondaryserviceDetail);
					for (OdrComponentAttribute odrComponentAttribute : odrComp.getOdrComponentAttributes()) {
						LOGGER.info("setting secondary reference");
						odrComponentAttribute.setOdrServiceDetail(secondaryserviceDetail);
					}
				}
				// for (OdrServiceAttribute odrComp :
				// secondaryserviceDetail.getOdrServiceAttributes()) {
				// odrComp.setOdrServiceDetail(secondaryserviceDetail);
				// }
				LOGGER.info("Attributes {}", secondaryOdrComponent.getOdrComponentAttributes());
			}
			for (OdrComponent odrComp : primaryserviceDetail.getOdrComponents()) {
				odrComp.setOdrServiceDetail(primaryserviceDetail);
				for (OdrComponentAttribute odrComponentAttribute : odrComp.getOdrComponentAttributes()) {
					LOGGER.info("setting primary reference");
					odrComponentAttribute.setOdrServiceDetail(primaryserviceDetail);
				}
			}
			// for (OdrServiceAttribute odrComp :
			// primaryserviceDetail.getOdrServiceAttributes()) {
			// odrComp.setOdrServiceDetail(primaryserviceDetail);
			// }

		} catch (Exception e) {
			LOGGER.warn("Error in Order Le Site : {}", ExceptionUtils.getStackTrace(e));
		}

		return primaryserviceDetail;
	}

	/**
	 * persistOdrComponent
	 * 
	 * @param primaryserviceDetail
	 * @param odrComponentAttributes
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
	 * 
	 * persistOdrComponentAttributes- persists the component Attr
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
	 * processSiteSla
	 * 
	 * @param orderIllSite
	 * @param primaryserviceDetail
	 */
	private void processSiteSla(OrderIllSite orderIllSite, OdrServiceDetail primaryserviceDetail, String username) {
		List<Map<String, String>> siteSlas = orderIllSiteSlaRepository.findByOrderIllSiteId(orderIllSite.getId());
		Set<OdrServiceSla> odrServiceSlas = new HashSet<>();
		for (Map<String, String> siteSla : siteSlas) {
			String attrN = siteSla.get("sla_name");
			String attrVa = siteSla.get("sla_value");
			OdrServiceSla odrServiceSla = new OdrServiceSla();
			odrServiceSla.setCreatedBy(username);
			odrServiceSla.setCreatedTime(new Date());
			odrServiceSla.setIsActive(CommonConstants.Y);
			odrServiceSla.setOdrServiceDetail(primaryserviceDetail);
			odrServiceSla.setSlaComponent(attrN);
			odrServiceSla.setSlaValue(attrVa);
			odrServiceSla.setUpdatedBy(username);
			odrServiceSla.setUpdatedTime(new Date());
			odrServiceSlas.add(odrServiceSla);
		}
		primaryserviceDetail.setOdrServiceSlas(odrServiceSlas);
	}

	public void processCommonComponentAttrMultiVrf(String refId, OdrServiceDetail serviceDetail, String username,
			Map<String, String> flowMapper, OdrComponent odrComponent,OrderVrfSites orderVrfSites)
			throws TclCommonException, IllegalArgumentException {
		LOGGER.info("Querying for LOCALIT Contact for reference Name {} and reference Id {}", refId,
				getReferenceName());
		List<Map<String, String>> oderProdCompAttrs = OrderProductComponentsAttributeValueRepository
				.findByRefidAndRefName(refId, getReferenceName());
		for (Map<String, String> oderProdCompAttr : oderProdCompAttrs) {
			String category = oderProdCompAttr.get("category");
			String attrName = oderProdCompAttr.get("attrName");
			String attrValue = oderProdCompAttr.get("attrValue");

			if (attrName.equals("GSTNO") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setBillingGstNumber(attrValue);
				odrComponent.getOdrComponentAttributes().add(
						persistOdrComponentAttributes(serviceDetail, odrComponent, "siteGstNumber", attrValue.trim()));
			} else if (attrName.equals("GST_ADDRESS") && StringUtils.isNotBlank(attrValue)) {
				Optional<AdditionalServiceParams> addServiceParam = additionalServiceParamRepository
						.findById(Integer.valueOf(attrValue));
				if (addServiceParam.isPresent()) {
					String address = addServiceParam.get().getValue();
					if (StringUtils.isNotBlank(address)) {
						GstAddressBean gstAddress = Utils.convertJsonToObject(address, GstAddressBean.class);

						OdrGstAddress odrGstAddress = serviceDetail.getOdrGstAddress();
						if (serviceDetail.getOdrGstAddress() == null) {
							odrGstAddress = new OdrGstAddress();
						}
						odrGstAddress.setBuildingName(gstAddress.getBuildingName());
						odrGstAddress.setBuildingNumber(gstAddress.getBuildingNumber());
						odrGstAddress.setCreatedBy(serviceDetail.getCreatedBy());
						odrGstAddress.setCreatedTime(new Timestamp(new Date().getTime()));
						odrGstAddress.setDistrict(gstAddress.getDistrict());
						odrGstAddress.setFlatNumber(gstAddress.getFlatNumber());
						odrGstAddress.setLatitude(gstAddress.getLatitude());
						odrGstAddress.setLocality(gstAddress.getLocality());
						odrGstAddress.setLongitude(gstAddress.getLongitude());
						odrGstAddress.setPincode(gstAddress.getPinCode());
						odrGstAddress.setState(gstAddress.getState());
						odrGstAddress.setStreet(gstAddress.getStreet());
						serviceDetail.setOdrGstAddress(odrGstAddress);
						// odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail,
						// odrComponent, "siteGstAddress",address));
					} else {
						LOGGER.warn("Address is empty for id {}", attrValue);
					}

					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail,
							odrComponent, "siteGstAddressId", attrValue.trim()));
				}
			} else if (attrName.equals("TAX_EXCEMPTED_FILENAME") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setTaxExemptionFlag(CommonConstants.Y);
				odrComponent.getOdrComponentAttributes().add(
						persistOdrComponentAttributes(serviceDetail, odrComponent, "taxExemption", CommonConstants.Y));
			} else if (attrName.equals("CPE Basic Chassis") && StringUtils.isNotBlank(attrValue)) {
				OdrServiceAttribute odrEqupModelAttribute = new OdrServiceAttribute();
				odrEqupModelAttribute.setAttributeAltValueLabel(attrValue);
				odrEqupModelAttribute.setAttributeName("EQUIPMENT_MODEL");
				odrEqupModelAttribute.setAttributeValue(attrValue);
				odrEqupModelAttribute.setCategory(category);
				odrEqupModelAttribute.setCreatedBy(username);
				odrEqupModelAttribute.setCreatedDate(new Date());
				odrEqupModelAttribute.setIsActive(CommonConstants.Y);
				odrEqupModelAttribute.setOdrServiceDetail(serviceDetail);
				odrEqupModelAttribute.setUpdatedBy(username);
				odrEqupModelAttribute.setUpdatedDate(new Date());
				serviceDetail.getOdrServiceAttributes().add(odrEqupModelAttribute);
				List<String> list = new ArrayList<String>();
				list.add(attrValue);
				LOGGER.info("MDC Filter token value in before Queue call product BOM Details {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				LOGGER.info("calling the product with request {}", list);
				// String cpeString = list.stream().collect(Collectors.joining(","));
				String productBomDetailsStr = (String) mqUtils.sendAndReceive(productBomQueue,
						Utils.convertObjectToJson(list));
				/*
				 * String productBomDetailsStr = (String)
				 * mqUtils.sendAndReceive(cpeBomNtwProductsQueue, cpeString); String
				 * modifiedProductBomDetailsStr=
				 * productBomDetailsStr.replaceFirst("cpeBomDetails", "resources");
				 * LOGGER.info("productBomDetailsStr {}", modifiedProductBomDetailsStr);
				 */
				if (StringUtils.isNotBlank(productBomDetailsStr)) {
					LOGGER.info("response received product with response {}", productBomDetailsStr);
					OdrAdditionalServiceParam odrAdditionalServiceParam = new OdrAdditionalServiceParam();
					odrAdditionalServiceParam.setAttribute("cpe-bom-resource");
					odrAdditionalServiceParam.setCategory("CPE-BOM");
					odrAdditionalServiceParam.setCreatedBy(username);
					odrAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
					odrAdditionalServiceParam.setIsActive(CommonConstants.Y);
					odrAdditionalServiceParam.setValue(productBomDetailsStr);
					odrAdditionalServiceParamRepository.save(odrAdditionalServiceParam);

					/*
					 * OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
					 * .findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName,
					 * serviceDetail.getId(), category); if (odrServiceAttribute == null) {
					 */
					LOGGER.info("attrName : {}  --> attrValue : {} ---> type  {}", attrName, attrValue,
							serviceDetail.getPrimarySecondary());
					OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
					odrServiceAttribute.setAttributeAltValueLabel(attrValue);
					odrServiceAttribute.setAttributeName(attrName);
					odrServiceAttribute.setAttributeValue(odrAdditionalServiceParam.getId() + CommonConstants.EMPTY);
					odrServiceAttribute.setCategory(category);
					odrServiceAttribute.setCreatedBy(username);
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setIsAdditionalParam(CommonConstants.Y);
					odrServiceAttribute.setOdrServiceDetail(serviceDetail);
					odrServiceAttribute.setUpdatedBy(username);
					odrServiceAttribute.setUpdatedDate(new Date());
					serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
					/*
					 * }else { LOGGER.info("Already exists {} {}",attrName,serviceDetail.getId()); }
					 */

				}
			} else if (attrName.equals("TAX_EXCEMPTED_ATTACHMENTID") && StringUtils.isNotBlank(attrValue)) {
				LOGGER.info("Entering the tax exception with attachmentId {}", attrValue);
				Integer erfAttachmentId = Integer.valueOf(attrValue);
				LOGGER.info("MDC Filter token value in before Queue call getAttachment {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String attachmentResponse = (String) mqUtils.sendAndReceive(attachmentQueue,
						String.valueOf(erfAttachmentId));
				LOGGER.info("Response received from attachment queue {}", attachmentResponse);
				if (attachmentResponse != null) {
					@SuppressWarnings("unchecked")
					Map<String, Object> attachmentMapper = Utils.convertJsonToObject(attachmentResponse, Map.class);
					if (attachmentMapper != null) {
						Attachment attachment = new Attachment();
						attachment.setCreatedBy(username);
						attachment.setCreatedDate(new Timestamp(new Date().getTime()));
						attachment.setIsActive(CommonConstants.Y);
						attachment.setName((String) attachmentMapper.get("NAME"));
						attachment.setType("Tax");
						attachment.setStoragePathUrl((String) attachmentMapper.get("URL_PATH"));
						attachmentRepository.save(attachment);
						OdrAttachment odrAttachment = new OdrAttachment();
						odrAttachment.setAttachmentId(attachment.getId());
						odrAttachment.setAttachmentType("Tax");
						odrAttachment.setIsActive(CommonConstants.Y);
						odrAttachment.setOfferingName(flowMapper.get("offeringName"));
						odrAttachment.setProductName(flowMapper.get("productName"));
						// odrAttachment.setServiceCode(serviceDetail.getUuid());

						odrAttachment.setOdrServiceDetail(serviceDetail);
						serviceDetail.getOdrAttachments().add(odrAttachment);
						// odrAttachmentRepository.save(odrAttachment);
					}

				}
			} else if (attrName.equals("LOCAL_IT_CONTACT") && StringUtils.isNotBlank(attrValue)) {
				LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String localItResponse = (String) mqUtils.sendAndReceive(localItQueue, attrValue);
				if (localItResponse != null) {
					@SuppressWarnings("unchecked")
					Map<String, Object> localItDetail = (Map<String, Object>) Utils.convertJsonToObject(localItResponse,
							Map.class);
					serviceDetail.setLocalItContactEmail(
							localItDetail.get("EMAIL") != null ? (String) localItDetail.get("EMAIL") : null);
					serviceDetail.setLocalItContactMobile(
							localItDetail.get("CONTACT_NO") != null ? (String) localItDetail.get("CONTACT_NO") : null);
					serviceDetail.setLocalItContactName(
							localItDetail.get("NAME") != null ? (String) localItDetail.get("NAME") : null);
					odrComponent.getOdrComponentAttributes()
							.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localItContactEmailId",
									localItDetail.get("EMAIL") != null ? (String) localItDetail.get("EMAIL") : null));
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail,
							odrComponent, "localItContactMobile",
							localItDetail.get("CONTACT_NO") != null ? (String) localItDetail.get("CONTACT_NO") : null));
					odrComponent.getOdrComponentAttributes()
							.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localItContactName",
									localItDetail.get("NAME") != null ? (String) localItDetail.get("NAME") : null));
				}
			} else {
				// OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
				// .findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName,
				// serviceDetail.getId(),
				// category);
				// if (odrServiceAttribute == null) {
				LOGGER.info("attrName : {}  --> attrValue : {} ---> type  {}", attrName, attrValue,
						serviceDetail.getPrimarySecondary());
				OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
				odrServiceAttribute.setAttributeAltValueLabel(attrValue);
				odrServiceAttribute.setAttributeName(attrName);
				odrServiceAttribute.setAttributeValue(attrValue);
				odrServiceAttribute.setCategory(category);
				odrServiceAttribute.setCreatedBy(username);
				odrServiceAttribute.setCreatedDate(new Date());
				odrServiceAttribute.setIsActive(CommonConstants.Y);
				odrServiceAttribute.setOdrServiceDetail(serviceDetail);
				odrServiceAttribute.setUpdatedBy(username);
				odrServiceAttribute.setUpdatedDate(new Date());
				serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
				// }else{
				// LOGGER.info("Already exists {} {}",attrName,serviceDetail.getId());
				// }

			}

		}
	}

	@SuppressWarnings("unchecked")
	public OdrServiceDetail processProductComponentAttrMultiVrf(String refId, OdrServiceDetail serviceDetail,
			String type, String userName, boolean isUpdate, List<OdrServiceCommercial> odrServiceCommercials,
			OdrComponent odrComponent,OrderVrfSites orderVrfSites,Boolean isMaster) throws TclCommonException, IllegalArgumentException, ParseException {
		List<Map<String, String>> oderProdCompAttrs = OrderProductComponentsAttributeValueRepository
				.findByRefidAndRefName(refId, type, getReferenceName());
		String vrfBillingType = CommonConstants.EMPTY;
		if (oderProdCompAttrs.isEmpty()) {
			LOGGER.info("Secondary not available for {} ,{} ,{}", refId, type, getReferenceName());
			return null;
		}
		serviceDetail.setPrimarySecondary(type);
		for (Map<String, String> oderProdCompAttr : oderProdCompAttrs) {
			String category = oderProdCompAttr.get("category");
			String attrName = oderProdCompAttr.get("attrName");
			String attrValue = oderProdCompAttr.get("attrValue");

			if (attrName.equals("Burstable Bandwidth") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setBurstableBwPortspeed(attrValue.trim());
				serviceDetail.setBurstableBwPortspeedAltName(attrValue.trim());
				serviceDetail.setBurstableBwUnit("Mbps");
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent,
						"burstableBandwidth", attrValue.trim()));
				odrComponent.getOdrComponentAttributes()
						.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "burstableBwUnit", "Mbps"));
			} else if (attrName.equals("Port Bandwidth") && StringUtils.isNotBlank(attrValue)) {
				// serviceDetail.setBwPortspeed(attrValue.trim());
				// serviceDetail.setBwPortspeedAltName(attrValue.trim());
				serviceDetail.setBwUnit("Mbps");
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent,
						"TOTAL_VRF_BANDWIDTH_MBPS", attrValue.trim()));
				serviceDetail.getOdrServiceAttributes()
						.add(persistOdrServiceAttributes(serviceDetail, "TOTAL_VRF_BANDWIDTH_MBPS", attrValue));
				odrComponent.getOdrComponentAttributes()
						.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "bwUnit", "Mbps"));
			} else if (attrName.equals("Local Loop Bandwidth") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setLastmileBw(attrValue.trim());
				serviceDetail.setLastmileBwAltName(attrValue.trim());
				serviceDetail.setLastmileBwUnit("Mbps");
				odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail, odrComponent,
						"localLoopBandwidth", attrValue.trim()));
				odrComponent.getOdrComponentAttributes().add(
						persistOdrComponentAttributes(serviceDetail, odrComponent, "localLoopBandwidthUnit", "Mbps"));
			} else if (attrName.equals("GSTNO") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setBillingGstNumber(attrValue.trim());

			} else if (attrName.equals("CPE Basic Chassis") && StringUtils.isNotBlank(attrValue)) {
				LOGGER.info("MDC Filter token value in before Queue call product BOM Details {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				LOGGER.info("calling the product with request {}", attrValue);
				/*
				 * List<String> list = new ArrayList<>(); list.add(attrValue);
				 */
				String productBomDetailsStr = (String) mqUtils.sendAndReceive(productBomQueue, attrValue);
				/*
				 * String cpeString = list.stream().collect(Collectors.joining(","));
				 * 
				 * String productBomDetailsStr = (String)
				 * mqUtils.sendAndReceive(cpeBomNtwProductsQueue, cpeString); String
				 * modifiedProductBomDetailsStr=
				 * productBomDetailsStr.replaceFirst("cpeBomDetails", "resources");
				 * LOGGER.info("productBomDetailsStr {}", modifiedProductBomDetailsStr);
				 */
				if (StringUtils.isNotBlank(productBomDetailsStr)) {
					LOGGER.info("response received product with response {}", productBomDetailsStr);
					OdrAdditionalServiceParam odrAdditionalServiceParam = new OdrAdditionalServiceParam();
					odrAdditionalServiceParam.setAttribute("cpe-bom-resource");
					odrAdditionalServiceParam.setCategory("CPE-BOM");
					odrAdditionalServiceParam.setCreatedBy(userName);
					odrAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
					odrAdditionalServiceParam.setIsActive(CommonConstants.Y);
					odrAdditionalServiceParam.setValue(productBomDetailsStr);
					odrAdditionalServiceParamRepository.save(odrAdditionalServiceParam);

					// OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
					// .findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName,
					// serviceDetail.getId(),
					// category);
					// if (odrServiceAttribute == null) {
					LOGGER.info("attrName : {}  --> attrValue : {} ---> type  {}", attrName, attrValue,
							serviceDetail.getPrimarySecondary());
					OdrServiceAttribute odrServiceAttribute = new OdrServiceAttribute();
					odrServiceAttribute.setAttributeAltValueLabel(attrValue);
					odrServiceAttribute.setAttributeName(attrName);
					odrServiceAttribute.setAttributeValue(odrAdditionalServiceParam.getId() + CommonConstants.EMPTY);
					odrServiceAttribute.setCategory(category);
					odrServiceAttribute.setCreatedBy(userName);
					odrServiceAttribute.setCreatedDate(new Date());
					odrServiceAttribute.setIsActive(CommonConstants.Y);
					odrServiceAttribute.setIsAdditionalParam(CommonConstants.Y);
					odrServiceAttribute.setOdrServiceDetail(serviceDetail);
					odrServiceAttribute.setUpdatedBy(userName);
					odrServiceAttribute.setUpdatedDate(new Date());
					serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
					// }else{
					// LOGGER.info("Already exists {} {}",attrName,serviceDetail.getId());
					// }

				}
			} else if (attrName.equals("TAX_EXCEMPTED_FILENAME") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setTaxExemptionFlag(CommonConstants.Y);
				odrComponent.getOdrComponentAttributes().add(
						persistOdrComponentAttributes(serviceDetail, odrComponent, "taxExemption", CommonConstants.Y));
			} else if (attrName.equals("Interface") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setSiteEndInterface(attrValue);
				odrComponent.getOdrComponentAttributes()
						.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "interface", attrValue));
			} else if (attrName.equals("CPE Management Type") && StringUtils.isNotBlank(attrValue)) {
				serviceDetail.setServiceOption(attrValue);
				odrComponent.getOdrComponentAttributes().add(
						persistOdrComponentAttributes(serviceDetail, odrComponent, "cpeManagementType", attrValue));
			} else if (attrName.equals("LOCAL_IT_CONTACT") && StringUtils.isNotBlank(attrValue)) {
				LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String localItResponse = (String) mqUtils.sendAndReceive(localItQueue, attrValue);
				Map<String, Object> localItDetail = (Map<String, Object>) Utils.convertJsonToObject(localItResponse,
						Map.class);
				if (localItDetail != null) {
					serviceDetail.setLocalItContactEmail(
							localItDetail.get("EMAIL") != null ? (String) localItDetail.get("EMAIL") : null);
					serviceDetail.setLocalItContactMobile(
							localItDetail.get("CONTACT_NO") != null ? (String) localItDetail.get("CONTACT_NO") : null);
					serviceDetail.setLocalItContactName(
							localItDetail.get("NAME") != null ? (String) localItDetail.get("NAME") : null);
					LOGGER.info("Local IT Contact Received {}", localItDetail);
					odrComponent.getOdrComponentAttributes()
							.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localItContactEmailId",
									localItDetail.get("EMAIL") != null ? (String) localItDetail.get("EMAIL") : null));
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail,
							odrComponent, "localItContactMobile",
							localItDetail.get("CONTACT_NO") != null ? (String) localItDetail.get("CONTACT_NO") : null));
					odrComponent.getOdrComponentAttributes()
							.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "localItContactName",
									localItDetail.get("NAME") != null ? (String) localItDetail.get("NAME") : null));
				}
			} else if (attrName.equals("vrfBillingType") && StringUtils.isNotBlank(attrValue)) {
				vrfBillingType = attrValue;
				odrComponent.getOdrComponentAttributes().add(
						persistOdrComponentAttributes(serviceDetail, odrComponent, "VRF based billing", attrValue));
				serviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(serviceDetail, "VRF based billing", attrValue));
				odrComponent.getOdrComponentAttributes().add(
						persistOdrComponentAttributes(serviceDetail, odrComponent, attrName, attrValue));
				serviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(serviceDetail, attrName, attrValue));
			} else if (attrName.equals("No of VRFs") && StringUtils.isNotBlank(attrValue)) {
				odrComponent.getOdrComponentAttributes().add(
						persistOdrComponentAttributes(serviceDetail, odrComponent, "NUMBER_OF_VRFS", attrValue));
				serviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(serviceDetail, "NUMBER_OF_VRFS", attrValue));
				odrComponent.getOdrComponentAttributes().add(
						persistOdrComponentAttributes(serviceDetail, odrComponent, attrName, attrValue));
				serviceDetail.getOdrServiceAttributes().add(persistOdrServiceAttributes(serviceDetail, attrName, attrValue));
			} else {
				// OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
				// .findByAttributeNameAndOdrServiceDetail_IdAndCategory(attrName,
				// serviceDetail.getId(),
				// category);
				// if (odrServiceAttribute == null) {
				LOGGER.info("attrName : {}  --> attrValue : {} ---> type  {}", attrName, attrValue,
						serviceDetail.getPrimarySecondary());
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
				serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
				// }else{
				// LOGGER.info("Already exists {} {}",attrName,serviceDetail.getId());
				// }
			}

		}
		processCommercialsMultiVrf(refId, type, userName, odrServiceCommercials, serviceDetail, orderVrfSites, isMaster,vrfBillingType);
		if (!isUpdate)
			processSiteFeasibility(refId, serviceDetail, type, userName, odrComponent);
		return serviceDetail;
	}
	
	private void processCommercialsMultiVrf(String refId, String type, String userName,
			List<OdrServiceCommercial> odrServiceCommercials, OdrServiceDetail serviceDetail,
			OrderVrfSites orderVrfSites, Boolean isMaster, String vrfBillingType) {
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndType(Integer.valueOf(refId), type);
		if (vrfBillingType.toLowerCase().contains("consolidated")) {
			if (isMaster) {
				for (OrderProductComponent orderProductComponent : orderProductComponents) {
					OrderPrice orderPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
							orderProductComponent.getId() + CommonConstants.EMPTY, "COMPONENTS");
					if (orderPrice != null) {
						String categoryName = orderProductComponent.getMstProductComponent().getName();
						String arcAttrName = categoryName + "_Arc_Price";
						String nrcAttrName = categoryName + "_Nrc_Price";
						addServiceAttr(serviceDetail, userName, orderPrice.getEffectiveArc(), categoryName,
								arcAttrName);
						addServiceAttr(serviceDetail, userName, orderPrice.getEffectiveNrc(), categoryName,
								nrcAttrName);
						OdrServiceCommercial odrServiceCommercial = new OdrServiceCommercial();
						odrServiceCommercial.setArc(orderPrice.getEffectiveArc());
						odrServiceCommercial.setMrc(orderPrice.getEffectiveMrc());
						odrServiceCommercial.setNrc(orderPrice.getEffectiveNrc());
						odrServiceCommercial.setReferenceName(orderProductComponent.getMstProductComponent().getName());
						odrServiceCommercial.setReferenceType("COMPONENTS");
						odrServiceCommercial.setOdrServiceDetail(serviceDetail);
						odrServiceCommercial.setServiceType(type);
						odrServiceCommercials.add(odrServiceCommercial);
					}

					for (OrderProductComponentsAttributeValue orderProductComponentsAttributeValue : orderProductComponent
							.getOrderProductComponentsAttributeValues()) {
						OrderPrice attrOrderPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
								orderProductComponentsAttributeValue.getId() + CommonConstants.EMPTY, "ATTRIBUTES");
						if (attrOrderPrice != null
								&& (attrOrderPrice.getEffectiveArc() != null || attrOrderPrice.getEffectiveMrc() != null
										|| attrOrderPrice.getEffectiveNrc() != null)) {
							OdrServiceCommercial odrServiceCommercial = new OdrServiceCommercial();
							odrServiceCommercial.setArc(attrOrderPrice.getEffectiveArc());
							odrServiceCommercial.setMrc(attrOrderPrice.getEffectiveMrc());
							odrServiceCommercial.setNrc(attrOrderPrice.getEffectiveNrc());
							odrServiceCommercial.setComponentReferenceName(
									orderProductComponent.getMstProductComponent().getName());
							odrServiceCommercial.setReferenceName(
									orderProductComponentsAttributeValue.getProductAttributeMaster().getName());
							odrServiceCommercial.setReferenceType("ATTRIBUTES");
							odrServiceCommercial.setOdrServiceDetail(serviceDetail);
							odrServiceCommercial.setServiceType(type);
							odrServiceCommercials.add(odrServiceCommercial);
						}
						if (attrOrderPrice != null && attrOrderPrice.getEffectiveUsagePrice() != null
								&& attrOrderPrice.getEffectiveUsagePrice() > 0) {
							addServiceAttr(serviceDetail, userName, attrOrderPrice.getEffectiveUsagePrice(),
									orderProductComponent.getMstProductComponent().getName(), "usage_price_per_mb");
						}

					}

				}
			}
		} else {
			if (isMaster) {
				for (OrderProductComponent orderProductComponent : orderProductComponents) {
					if (!orderProductComponent.getMstProductComponent().getName()
							.equals(IzosdwanCommonConstants.VPN_PORT)) {
						OrderPrice orderPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
								orderProductComponent.getId() + CommonConstants.EMPTY, "COMPONENTS");
						if (orderPrice != null) {
							String categoryName = orderProductComponent.getMstProductComponent().getName();
							String arcAttrName = categoryName + "_Arc_Price";
							String nrcAttrName = categoryName + "_Nrc_Price";
							addServiceAttr(serviceDetail, userName, orderPrice.getEffectiveArc(), categoryName,
									arcAttrName);
							addServiceAttr(serviceDetail, userName, orderPrice.getEffectiveNrc(), categoryName,
									nrcAttrName);
							OdrServiceCommercial odrServiceCommercial = new OdrServiceCommercial();
							odrServiceCommercial.setArc(orderPrice.getEffectiveArc());
							odrServiceCommercial.setMrc(orderPrice.getEffectiveMrc());
							odrServiceCommercial.setNrc(orderPrice.getEffectiveNrc());
							odrServiceCommercial
									.setReferenceName(orderProductComponent.getMstProductComponent().getName());
							odrServiceCommercial.setReferenceType("COMPONENTS");
							odrServiceCommercial.setOdrServiceDetail(serviceDetail);
							odrServiceCommercial.setServiceType(type);
							odrServiceCommercials.add(odrServiceCommercial);
						}

						for (OrderProductComponentsAttributeValue orderProductComponentsAttributeValue : orderProductComponent
								.getOrderProductComponentsAttributeValues()) {
							OrderPrice attrOrderPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
									orderProductComponentsAttributeValue.getId() + CommonConstants.EMPTY, "ATTRIBUTES");
							if (attrOrderPrice != null && (attrOrderPrice.getEffectiveArc() != null
									|| attrOrderPrice.getEffectiveMrc() != null
									|| attrOrderPrice.getEffectiveNrc() != null)) {
								OdrServiceCommercial odrServiceCommercial = new OdrServiceCommercial();
								odrServiceCommercial.setArc(attrOrderPrice.getEffectiveArc());
								odrServiceCommercial.setMrc(attrOrderPrice.getEffectiveMrc());
								odrServiceCommercial.setNrc(attrOrderPrice.getEffectiveNrc());
								odrServiceCommercial.setComponentReferenceName(
										orderProductComponent.getMstProductComponent().getName());
								odrServiceCommercial.setReferenceName(
										orderProductComponentsAttributeValue.getProductAttributeMaster().getName());
								odrServiceCommercial.setReferenceType("ATTRIBUTES");
								odrServiceCommercial.setOdrServiceDetail(serviceDetail);
								odrServiceCommercial.setServiceType(type);
								odrServiceCommercials.add(odrServiceCommercial);
							}
							if (attrOrderPrice != null && attrOrderPrice.getEffectiveUsagePrice() != null
									&& attrOrderPrice.getEffectiveUsagePrice() > 0) {
								addServiceAttr(serviceDetail, userName, attrOrderPrice.getEffectiveUsagePrice(),
										orderProductComponent.getMstProductComponent().getName(), "usage_price_per_mb");
							}

						}
					} else {

						String categoryName = IzosdwanCommonConstants.VPN_PORT;
						String arcAttrName = categoryName + "_Arc_Price";
						String nrcAttrName = categoryName + "_Nrc_Price";
						addServiceAttr(serviceDetail, userName, orderVrfSites.getArc(), categoryName, arcAttrName);
						addServiceAttr(serviceDetail, userName, orderVrfSites.getNrc(), categoryName, nrcAttrName);
						OdrServiceCommercial odrServiceCommercial = new OdrServiceCommercial();
						odrServiceCommercial.setArc(orderVrfSites.getArc());
						odrServiceCommercial.setMrc(orderVrfSites.getMrc());
						odrServiceCommercial.setNrc(orderVrfSites.getNrc());
						odrServiceCommercial.setReferenceName(IzosdwanCommonConstants.VPN_PORT);
						odrServiceCommercial.setReferenceType("COMPONENTS");
						odrServiceCommercial.setOdrServiceDetail(serviceDetail);
						odrServiceCommercial.setServiceType(type);
						odrServiceCommercials.add(odrServiceCommercial);

					}
				}
			} else {

				String categoryName = IzosdwanCommonConstants.VPN_PORT;
				String arcAttrName = categoryName + "_Arc_Price";
				String nrcAttrName = categoryName + "_Nrc_Price";
				addServiceAttr(serviceDetail, userName, orderVrfSites.getArc(), categoryName, arcAttrName);
				addServiceAttr(serviceDetail, userName, orderVrfSites.getNrc(), categoryName, nrcAttrName);
				OdrServiceCommercial odrServiceCommercial = new OdrServiceCommercial();
				odrServiceCommercial.setArc(orderVrfSites.getArc());
				odrServiceCommercial.setMrc(orderVrfSites.getMrc());
				odrServiceCommercial.setNrc(orderVrfSites.getNrc());
				odrServiceCommercial.setReferenceName(IzosdwanCommonConstants.VPN_PORT);
				odrServiceCommercial.setReferenceType("COMPONENTS");
				odrServiceCommercial.setOdrServiceDetail(serviceDetail);
				odrServiceCommercial.setServiceType(type);
				odrServiceCommercials.add(odrServiceCommercial);

			}
		}

	}

	/**
	 * addServiceAttr
	 * 
	 * @param serviceDetail
	 * @param userName
	 * @param orderPrice
	 * @param categoryName
	 * @param arcAttrName
	 */
	private void addServiceAttr(OdrServiceDetail serviceDetail, String userName, Double orderPrice, String categoryName,
			String arcAttrName) {
		OdrServiceAttribute odrServiceAttribute = odrServiceAttributeRepository
				.findByAttributeNameAndOdrServiceDetail_IdAndCategory(arcAttrName, serviceDetail.getId(), categoryName);
		if (odrServiceAttribute == null) {
			odrServiceAttribute = new OdrServiceAttribute();
			odrServiceAttribute.setAttributeAltValueLabel(arcAttrName);
			odrServiceAttribute.setAttributeName(arcAttrName);
			odrServiceAttribute.setAttributeValue(orderPrice + CommonConstants.EMPTY);
			odrServiceAttribute.setCategory(categoryName);
			odrServiceAttribute.setCreatedBy(userName);
			odrServiceAttribute.setCreatedDate(new Date());
			odrServiceAttribute.setIsActive(CommonConstants.Y);
			odrServiceAttribute.setOdrServiceDetail(serviceDetail);
			odrServiceAttribute.setUpdatedBy(userName);
			odrServiceAttribute.setUpdatedDate(new Date());
			serviceDetail.getOdrServiceAttributes().add(odrServiceAttribute);
		}

	}
	
	private OdrServiceAttribute addServiceAttribute(String attrValue,String attrName,String category,String userName,OdrServiceDetail serviceDetail) {
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
		return odrServiceAttribute;
	}
	
	private void processVrfComponentAttributes(OrderVrfSites orderVrfSites, OdrServiceDetail serviceDetail,
			OdrComponent odrComponent, String userName) {
		LOGGER.info("Inside processVrfComponentAttributes");
		List<Map<String, String>> oderProdCompAttrs = OrderProductComponentsAttributeValueRepository
				.findByRefidAndRefName(orderVrfSites.getId().toString(), "VRF_SITES");
		if (oderProdCompAttrs != null && !oderProdCompAttrs.isEmpty()) {
			for (Map<String, String> oderProdCompAttr : oderProdCompAttrs) {
				String category = oderProdCompAttr.get("category");
				String attrName = oderProdCompAttr.get("attrName");
				String attrValue = oderProdCompAttr.get("attrValue");
				if (attrName.equals("vrf Port Bandwidth") && StringUtils.isNotBlank(attrValue)) {
					serviceDetail.setBwPortspeed(attrValue.trim());
					serviceDetail.setBwPortspeedAltName(attrValue.trim());
					serviceDetail.setBwUnit("Mbps");
					serviceDetail.getOdrServiceAttributes()
							.add(addServiceAttribute(attrValue, "portBandwidth", category, userName, serviceDetail));
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail,
							odrComponent, "portBandwidth", attrValue.trim()));
					odrComponent.getOdrComponentAttributes()
							.add(persistOdrComponentAttributes(serviceDetail, odrComponent, "bwUnit", "Mbps"));
				} else if (attrName.equals("Project Name") && StringUtils.isNotBlank(attrValue)) {
					serviceDetail.getOdrServiceAttributes().add(
							addServiceAttribute(attrValue, "CUSTOMER_PROJECT_NAME", category, userName, serviceDetail));
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail,
							odrComponent, "CUSTOMER_PROJECT_NAME", attrValue.trim()));
					serviceDetail.getOdrServiceAttributes()
							.add(addServiceAttribute(attrValue, attrName, category, userName, serviceDetail));
					odrComponent.getOdrComponentAttributes().add(
							persistOdrComponentAttributes(serviceDetail, odrComponent, attrName, attrValue.trim()));
				}else if (attrName.equals("flexiQos") && StringUtils.isNotBlank(attrValue)) {
					serviceDetail.getOdrServiceAttributes().add(
							addServiceAttribute(attrValue, "FLEXICOS", category, userName, serviceDetail));
					odrComponent.getOdrComponentAttributes().add(persistOdrComponentAttributes(serviceDetail,
							odrComponent, "FLEXICOS", attrValue.trim()));
					serviceDetail.getOdrServiceAttributes()
							.add(addServiceAttribute(attrValue, attrName, category, userName, serviceDetail));
					odrComponent.getOdrComponentAttributes().add(
							persistOdrComponentAttributes(serviceDetail, odrComponent, attrName, attrValue.trim()));
				} else {
					serviceDetail.getOdrServiceAttributes()
							.add(addServiceAttribute(attrValue, attrName, category, userName, serviceDetail));
				}
			}
		}
	}
}
