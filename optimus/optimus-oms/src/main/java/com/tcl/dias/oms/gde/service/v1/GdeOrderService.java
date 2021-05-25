package com.tcl.dias.oms.gde.service.v1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.beans.OrderProductComponentsAttributeValueBean;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.entity.entities.LinkFeasibility;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.MstOrderLinkStatus;
import com.tcl.dias.oms.entity.entities.OdrScheduleDetails;
import com.tcl.dias.oms.entity.entities.OdrScheduleDetailsAudit;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderLinkFeasibility;
import com.tcl.dias.oms.entity.entities.OrderNplLink;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.repository.GdeScheduleDetailsRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.LinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstOrderLinkStatusRepository;
import com.tcl.dias.oms.entity.repository.NplLinkRepository;
import com.tcl.dias.oms.entity.repository.OdrScheduleDetailsAuditRepository;
import com.tcl.dias.oms.entity.repository.OdrScheduleDetailsRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderLinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderNplLinkRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.gde.beans.CancelBodScheduleRequest;
import com.tcl.dias.oms.gde.beans.GdeCancelScheduleInputs;
import com.tcl.dias.oms.gde.beans.GdeCancelScheduleRequest;
import com.tcl.dias.oms.gde.beans.GdeCreateScheduleInputs;
import com.tcl.dias.oms.gde.beans.GdeFeasiblityBean;
import com.tcl.dias.oms.gde.beans.GdeLinkBean;
import com.tcl.dias.oms.gde.beans.GdeOdrScheduleAuditBean;
import com.tcl.dias.oms.gde.beans.GdeOrdersBean;
import com.tcl.dias.oms.gde.beans.GdePricingFeasibilityBean;
import com.tcl.dias.oms.gde.beans.GdeQuoteDetail;
import com.tcl.dias.oms.gde.beans.GdeScheduleDetailBean;
import com.tcl.dias.oms.gde.beans.OrderGdeSiteBean;
import com.tcl.dias.oms.gde.beans.OrderProductSolutionBean;
import com.tcl.dias.oms.gde.beans.OrderScheduleStageBean;
import com.tcl.dias.oms.gde.beans.OrderToLeBean;
import com.tcl.dias.oms.gde.beans.OrderToLeProductFamilyBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Class to hold all gde order related methods
 * @author archchan
 *
 */
@Service
@Transactional
public class GdeOrderService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GdeOrderService.class);
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	OrderToLeRepository orderToLeRepository;
	
	@Autowired
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;
	
	@Autowired
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;
	
	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;
	
	@Autowired
	OrderNplLinkRepository orderNplLinkRepository;
	
	@Autowired
	OrderIllSitesRepository orderIllSitesRepository;
	
	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;
	
	@Autowired
	GdeScheduleDetailsRepository gdeScheduleDetailsRepository;
	
	@Autowired
	OdrScheduleDetailsRepository odrScheduleDetailsRepository;
	
	@Autowired
	OrderPriceRepository orderPriceRepository;
	
	@Autowired
	OdrScheduleDetailsAuditRepository odrScheduleDetailsAuditRepository;
	
	@Autowired
	OrderLinkFeasibilityRepository orderLinkFeasibilityRepository;
	
	@Autowired
	IllSiteRepository illSiteRepository;
	
	@Autowired
	NplLinkRepository nplLinkRepository;

	@Autowired
	LinkFeasibilityRepository linkFeasibilityRepository;
	
	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;
	
	@Autowired
	MstOrderLinkStatusRepository mstOrderLinkStatusRepository;
	
	@Value("${rabbitmq.odr.process.queue}")
	String odrProcessQueue;
	
	@Autowired
	MQUtils mqUtils;
	
	@Autowired
	GdePricingFeasibilityService gdePricingFeasibilityService;
	
	@Value("${gde.mdso.notify.update}")
	String notifyUpdateUrl;
	
	@Value("${app.host}")
	String appHost;
	
	@Value("${gde.mdso.create.bod.url}")
	String cancelBodScheduleUrl;
	
	@Autowired
	RestClientService restClientService;
	
	/**
	 * Method to get order details for given id
	 * @param orderId
	 * @return
	 * @throws TclCommonException 
	 */
	public GdeOrdersBean getOrderDetailsById(Integer orderId) throws TclCommonException {
		GdeOrdersBean gdeOrdersBean = null;
		try {
			if (Objects.isNull(orderId)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);

			}
			Order order = orderRepository.findByIdAndStatus(orderId, (byte) 1);

			if (order == null) {
				throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_ERROR);

			}

			gdeOrdersBean = constructOrder(order);
			gdeOrdersBean.setCustomerId(order.getCustomer().getErfCusCustomerId());

		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());

			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return gdeOrdersBean;
		
		
	}
	
	/**
	 * constructOrder - Method to construct order bean
	 * 
	 * @param orders
	 * @param version
	 * @return
	 * @throws TclCommonException
	 */
	public GdeOrdersBean constructOrder(Order orders) throws TclCommonException {
		GdeOrdersBean orderBean = new GdeOrdersBean();
		try {
			if (Objects.isNull(orders)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			orderBean.setId(orders.getId());
			orderBean.setCreatedBy(orders.getCreatedBy());
			orderBean.setOrderCode(orders.getOrderCode());
			orderBean.setCreatedTime(orders.getCreatedTime());
			orderBean.setStatus(orders.getStatus());
			orderBean.setTermInMonths(orders.getTermInMonths());
			orderBean.setStage(orders.getStage());
			orderBean.setOrderToLeBeans(constructOrderLeEntityDtos(orders));
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return orderBean;

	}

	/**
	 * constructOrderLeEntityDtos - Method to construct legal entity dtos
	 * 
	 * @param order
	 * @param version
	 * @return
	 * @throws TclCommonException
	 */
	public Set<OrderToLeBean> constructOrderLeEntityDtos(Order order) throws TclCommonException {
		Set<OrderToLeBean> orderToLeDtos = new HashSet<>();
		try {
			if (Objects.isNull(order)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			if (getOrderToLeBasenOnVersion(order) != null) {
				for (OrderToLe orTle : getOrderToLeBasenOnVersion(order)) {
					OrderToLeBean orderToLe = new OrderToLeBean(orTle);
					orderToLe.setTermInMonths(orTle.getTermInMonths());
					orderToLe.setCurrency(orTle.getCurrencyCode());
					orderToLe.setLegalAttributes(constructLegalAttributes(orTle));
					orderToLe.setOrderType(orTle.getOrderType());
					orderToLe.setOrderCategory(orTle.getOrderCategory());
					orderToLe.setStage(orTle.getStage());
					orderToLe.setOrderToLeProductFamilyBeans(
							constructOrderToLeFamilyDtos(getProductFamilyBasenOnVersion(orTle)));
					orderToLeDtos.add(orderToLe);

				}
			}
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return orderToLeDtos;

	}
	
	
	/**
	 * getOrderToLeBasenOnVersion - Method to get orderToLe based on version
	 * 
	 * @param orders
	 * @param version
	 * @return
	 * @throws TclCommonException
	 */
	public List<OrderToLe> getOrderToLeBasenOnVersion(Order orders) throws TclCommonException {
		List<OrderToLe> orToLes = null;
		try {
			if (Objects.isNull(orders)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			orToLes = orderToLeRepository.findByOrder(orders);
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return orToLes;
	}
	
	/**
	 * constructLegalAttributes - Method to construct legal attributes
	 * 
	 * @param orderToLe
	 * @return
	 * @throws TclCommonException
	 */
	public Set<LegalAttributeBean> constructLegalAttributes(OrderToLe orderToLe) throws TclCommonException {
		Set<LegalAttributeBean> leAttributeBeans = new HashSet<>();
		try {

			if (Objects.isNull(orderToLe)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}

			List<OrdersLeAttributeValue> attributeValues = ordersLeAttributeValueRepository.findByOrderToLe(orderToLe);
			if (attributeValues != null) {
				attributeValues.stream().forEach(attrVal -> {
					LegalAttributeBean attributeBean = new LegalAttributeBean();
					attributeBean.setAttributeValue(attrVal.getAttributeValue());
					attributeBean.setDisplayValue(attrVal.getDisplayValue());
					attributeBean.setMstOmsAttribute(constructMstAttributBean(attrVal.getMstOmsAttribute()));
					leAttributeBeans.add(attributeBean);

				});

			}
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return leAttributeBeans;
	}
	
	
	/**
	 * getProductFamilyBasenOnVersion - Method to get product family based on
	 * version and orderToLe
	 * 
	 * @param orderToLe
	 * @param version
	 * @return
	 * @throws TclCommonException
	 */
	public List<OrderToLeProductFamily> getProductFamilyBasenOnVersion(OrderToLe orderToLe) throws TclCommonException {
		List<OrderToLeProductFamily> prodFamilys = null;

		try {
			if (Objects.isNull(orderToLe))
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);

			prodFamilys = orderToLeProductFamilyRepository.findByOrderToLe(orderToLe);
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return prodFamilys;

	}
	
	/**
	 * constructOrderToLeFamilyDtos - Method to construct orderToLeProductFamilyBean
	 * 
	 * @param orderToLeProductFamilies
	 * @param version
	 * @return
	 * @throws TclCommonException
	 */
	public Set<OrderToLeProductFamilyBean> constructOrderToLeFamilyDtos(
			List<OrderToLeProductFamily> orderToLeProductFamilies) throws TclCommonException {
		Set<OrderToLeProductFamilyBean> orderToLeProductFamilyBeans = new HashSet<>();
		try {
			if (Objects.isNull(orderToLeProductFamilies) || orderToLeProductFamilies.isEmpty())
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);

			for (OrderToLeProductFamily orFamily : orderToLeProductFamilies) {
				OrderToLeProductFamilyBean orderToLeProductFamilyBean = new OrderToLeProductFamilyBean();
				if (orFamily.getMstProductFamily() != null) {
					orderToLeProductFamilyBean.setStatus(orFamily.getMstProductFamily().getStatus());
					orderToLeProductFamilyBean.setProductName(orFamily.getMstProductFamily().getName());
				}
				List<OrderProductSolutionBean> orderProductSolutionBeans = getSortedSolution(
						constructProductSolution(getProductSolutionBasenOnVersion(orFamily)));
				orderToLeProductFamilyBean.setOrderProductSolutions(orderProductSolutionBeans);
				orderToLeProductFamilyBeans.add(orderToLeProductFamilyBean);
			}
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return orderToLeProductFamilyBeans;
	}
	
	private List<OrderProductSolutionBean> getSortedSolution(List<OrderProductSolutionBean> solutionBeans) {
		if (solutionBeans != null) {

			solutionBeans.sort(Comparator.comparingInt(OrderProductSolutionBean::getId));
		}

		return solutionBeans;

	} 
	
	/**
	 * getProductSolutionBasenOnVersion - Method to get product solution based on
	 * version
	 * 
	 * @param family
	 * @param version
	 * @return
	 * @throws TclCommonException
	 */
	public List<OrderProductSolution> getProductSolutionBasenOnVersion(OrderToLeProductFamily family)
			throws TclCommonException {
		List<OrderProductSolution> productSolutions = null;
		try {
			if (Objects.isNull(family)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			productSolutions = orderProductSolutionRepository.findByOrderToLeProductFamily(family);
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return productSolutions;
	}
	
	/**
	 * constructMstAttributBean - Method to constuct MstOmsAttributeBean
	 * 
	 * @param mstOmsAttribute
	 * @return
	 */
	public MstOmsAttributeBean constructMstAttributBean(MstOmsAttribute mstOmsAttribute) {
		MstOmsAttributeBean mstOmsAttributeBean = null;
		if (mstOmsAttribute != null) {
			mstOmsAttributeBean = new MstOmsAttributeBean();
			mstOmsAttributeBean.setCategory(mstOmsAttribute.getCategory());
			mstOmsAttributeBean.setCreatedBy(mstOmsAttribute.getCreatedBy());
			mstOmsAttributeBean.setCreatedTime(mstOmsAttribute.getCreatedTime());
			mstOmsAttributeBean.setDescription(mstOmsAttribute.getDescription());
		}
		return mstOmsAttributeBean;
	}
	
	/**
	 * @link http://www.tatacommunications.com/
	 * @constructProductSolution - Method to construct OrderProductSolutionBean
	 * @param productSolutions
	 * @return Set<OrderProductSolutionBean>
	 * @throws TclCommonException
	 */
	public List<OrderProductSolutionBean> constructProductSolution(List<OrderProductSolution> productSolutions)
			throws TclCommonException {
		List<OrderProductSolutionBean> productSolutionBeans = new ArrayList<>();
		try {
			if (Objects.isNull(productSolutions) || productSolutions.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}

			for (OrderProductSolution solution : productSolutions) {
				OrderProductSolutionBean orderProductSolutionBean = new OrderProductSolutionBean();
				orderProductSolutionBean.setId(solution.getId());
				if (solution.getMstProductOffering() != null) {
					orderProductSolutionBean
							.setOfferingDescription(solution.getMstProductOffering().getProductDescription());
					orderProductSolutionBean.setOfferingName(solution.getMstProductOffering().getProductName());
					orderProductSolutionBean.setStatus(solution.getMstProductOffering().getStatus());
				}
					List<OrderIllSite> orderNplSiteList = null;
					List<OrderNplLink> orderNplLinkList = orderNplLinkRepository.findByProductSolutionId(solution.getId());
					List<GdeLinkBean> nplLinkBeanList = new ArrayList<>();
					if (orderNplLinkList != null && !orderNplLinkList.isEmpty()) {
						for (OrderNplLink orderNplLinkBean : orderNplLinkList) {
							orderNplSiteList = new ArrayList<>();
							OrderIllSite orderNplSiteA = getSitesBasedOnNplLink(orderNplLinkBean.getSiteAId());
							OrderIllSite orderNplSiteB = getSitesBasedOnNplLink(orderNplLinkBean.getSiteBId());
							orderNplSiteList.add(orderNplSiteA);
							orderNplSiteList.add(orderNplSiteB);
							List<OrderGdeSiteBean> OrdeNplSiteBeans = getSortedNplSiteDtos(
									constructNplSiteDtos(orderNplSiteList));
							nplLinkBeanList.add(constructNplLinkBean(orderNplLinkBean, OrdeNplSiteBeans,
									solution.getOrderToLeProductFamily().getMstProductFamily().getName()));

						}
					}
					orderProductSolutionBean.setNplLinkBean(nplLinkBeanList);
					productSolutionBeans.add(orderProductSolutionBean);
			}
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return productSolutionBeans;
	}
	
	
	/**
	 * getSitesBasedOnNplLink
	 * 
	 * @param siteId
	 * @return
	 */
	private OrderIllSite getSitesBasedOnNplLink(Integer siteId) {
		OrderIllSite illsites = null;

		if (siteId != null) {
			illsites = orderIllSitesRepository.findByIdAndStatus(siteId, (byte) 1);
		}

		return illsites;

	}
	
	/**
	 * 
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteNplSiteBean>
	 */
	private List<OrderGdeSiteBean> getSortedNplSiteDtos(List<OrderGdeSiteBean> nplSiteBeans) {
		if (nplSiteBeans != null) {
			nplSiteBeans.sort(Comparator.comparingInt(OrderGdeSiteBean::getId));

		}

		return nplSiteBeans;
	}
	
	/**
	 * constructNplSiteDtos
	 * 
	 * @param nplSites
	 * @param version
	 * @return
	 */
	private List<OrderGdeSiteBean> constructNplSiteDtos(List<OrderIllSite> nplSites) {
		List<OrderGdeSiteBean> sites = new ArrayList<>();
		if (nplSites != null) {
			for (OrderIllSite nplSite : nplSites) {
				if (nplSite.getStatus() == 1) {
					OrderGdeSiteBean nplSiteBean = new OrderGdeSiteBean(nplSite);
					if (nplSite.getMstOrderSiteStage() != null) {
						nplSiteBean.setCurrentStage(nplSite.getMstOrderSiteStage().getName());
					}
					if (nplSite.getMstOrderSiteStatus() != null) {
						nplSiteBean.setCurrentStatus(nplSite.getMstOrderSiteStatus().getName());
					}
					sites.add(nplSiteBean);
				}
			}
		}
		return sites;
	}
	
	/**
	 * constructNplLinkBean
	 * 
	 * @param orderNplLink      - Method to construct NplLinkBean
	 * @param orderNplSiteBeans
	 * @param version
	 * @return
	 * @throws TclCommonException
	 */
	public GdeLinkBean constructNplLinkBean(OrderNplLink orderNplLink, List<OrderGdeSiteBean> orderNplSiteBeans,
			String productFamilyName) throws TclCommonException {
		GdeLinkBean nplLinkBean = null;
		try {
			if (Objects.isNull(orderNplLink) || Objects.isNull(orderNplSiteBeans) || orderNplSiteBeans.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}

			nplLinkBean = new GdeLinkBean(orderNplLink);
			nplLinkBean.getLinkFeasibility().stream().forEach(linkF->{
				OdrScheduleDetails orderGdeSchedule = odrScheduleDetailsRepository.findByOrderLinkId(orderNplLink.getId());
				linkF.setOrderScheduleDetail(constructodrScheduleByOrderLinkId(orderGdeSchedule));
			});
			nplLinkBean.setOrderSites(orderNplSiteBeans);

			if (orderNplLink.getStatus() == 1) {
				List<OrderProductComponentBean> linkComp = getSortedComponents(
						constructOrderProductComponent(orderNplLink.getId(), CommonConstants.LINK, productFamilyName));
				nplLinkBean.setOrderProductComponentBeans(linkComp);

				List<OrderProductComponentBean> siteAComp = getSortedComponents(constructOrderProductComponent(
						orderNplLink.getSiteAId(), CommonConstants.SITEA, productFamilyName));
				nplLinkBean.getOrderProductComponentBeans().addAll(siteAComp);

				List<OrderProductComponentBean> siteBComp = getSortedComponents(constructOrderProductComponent(
						orderNplLink.getSiteBId(), CommonConstants.SITEB, productFamilyName));
				nplLinkBean.getOrderProductComponentBeans().addAll(siteBComp);
			}
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return nplLinkBean;
	}

	private GdeScheduleDetailBean constructodrScheduleByOrderLinkId(OdrScheduleDetails orderGdeSchedule) {
		GdeScheduleDetailBean GdeScheduleDetailBean = new GdeScheduleDetailBean();
		GdeScheduleDetailBean.setActivationStatus(orderGdeSchedule.getActivationStatus());
		GdeScheduleDetailBean.setCreatedTime(Utils.convertTimeStampToString(orderGdeSchedule.getCreatedTime()));
		GdeScheduleDetailBean.setFeasibilityStatus(orderGdeSchedule.getFeasibilityStatus());
		GdeScheduleDetailBean.setFeasibilityValidity(Utils.convertTimeStampToString(orderGdeSchedule.getFeasibilityValidity()));
		GdeScheduleDetailBean.setMdsoFeasibilityUuid(orderGdeSchedule.getMdsoFeasibilityUuid());
		GdeScheduleDetailBean.setMdsoResourceId(orderGdeSchedule.getMdsoResourceId());
//		GdeScheduleDetailBean.setScheduleEndDate(Utils.convertTimeStampToString(orderGdeSchedule.getScheduleEndDate()));
//		GdeScheduleDetailBean.setScheduleStartDate(Utils.convertTimeStampToString(orderGdeSchedule.getScheduleStartDate()));
		GdeScheduleDetailBean.setScheduleStartDate(orderGdeSchedule.getScheduleStartDate().toString());
		GdeScheduleDetailBean.setScheduleEndDate(orderGdeSchedule.getScheduleEndDate().toString());
		GdeScheduleDetailBean.setOperationId(orderGdeSchedule.getScheduleOperationId());
		GdeScheduleDetailBean.setSlots(orderGdeSchedule.getSlots());
		GdeScheduleDetailBean.setBaseCircuitBw(String.valueOf(orderGdeSchedule.getBaseCircuitBw()));
		GdeScheduleDetailBean.setBwOnDemand(String.valueOf(orderGdeSchedule.getBwOnDemand()));
		GdeScheduleDetailBean.setOrderLinkId(orderGdeSchedule.getOrderLinkId());
		GdeScheduleDetailBean.setTicketId(orderGdeSchedule.getTicketId());
		GdeScheduleDetailBean.setUpgradedBw(String.valueOf(orderGdeSchedule.getUpgradedBw()));
		GdeScheduleDetailBean.setUpdatedTime(Utils.convertTimeStampToString(orderGdeSchedule.getUpdatedTime()));
		GdeScheduleDetailBean.setScheduleId(orderGdeSchedule.getScheduleId());
		return GdeScheduleDetailBean;
	}
	
	
	/**
	 * constructOrderProductComponent - Method to construct
	 * OrderProductComponentBean list
	 * 
	 * @param id
	 * @param version
	 * @param type
	 * @return
	 * @throws TclCommonException
	 */
	public List<OrderProductComponentBean> constructOrderProductComponent(Integer id, String type,
			String productFamilyName) throws TclCommonException {
		List<OrderProductComponentBean> orderProductComponentDtos = new ArrayList<>();

		try {
			if (Objects.isNull(id) || Objects.isNull(type) || StringUtils.isEmpty(type)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}

			List<OrderProductComponent> productComponents = getComponentBasedOnIdVersionType(id, type,
					productFamilyName);

			if (productComponents != null) {

				for (OrderProductComponent quoteProductComponent : productComponents) {
					OrderProductComponentBean orderProductComponentBean = new OrderProductComponentBean();
					orderProductComponentBean.setId(id);
					orderProductComponentBean.setReferenceId(quoteProductComponent.getReferenceId());
					if (quoteProductComponent.getMstProductComponent() != null) {
						orderProductComponentBean
								.setDescription(quoteProductComponent.getMstProductComponent().getDescription());
						orderProductComponentBean.setName(quoteProductComponent.getMstProductComponent().getName());
					}
					orderProductComponentBean.setType(quoteProductComponent.getType());
					orderProductComponentBean.setPrice(constructComponentPriceDto(quoteProductComponent));
					List<OrderProductComponentsAttributeValueBean> attributeValueBeans = getSortedAttributeComponents(
							constructAttribute(quoteProductComponent.getOrderProductComponentsAttributeValues()));
					orderProductComponentBean.setOrderProductComponentsAttributeValues(attributeValueBeans);
					orderProductComponentDtos.add(orderProductComponentBean);
				}

			}
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return orderProductComponentDtos;

	}
	
	/**
	 * 
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteNplSiteBean>
	 */
	private List<OrderProductComponentBean> getSortedComponents(List<OrderProductComponentBean> orderComponentBeans) {
		if (orderComponentBeans != null) {
			orderComponentBeans.sort(Comparator.comparingInt(OrderProductComponentBean::getId));

		}

		return orderComponentBeans;
	}
	
	/**
	 * getComponentBasedOnIdVersionType - Method to get component based on site id ,
	 * version and type
	 * 
	 * @param siteId
	 * @param version
	 * @param type
	 * @return
	 * @throws TclCommonException
	 */
	public List<OrderProductComponent> getComponentBasedOnIdVersionType(Integer siteId, String type,
			String productFamilyName) throws TclCommonException {
		List<OrderProductComponent> components = null;
		try {
			if (Objects.isNull(siteId) || Objects.isNull(type) || StringUtils.isEmpty(type)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			components = orderProductComponentRepository.findByReferenceIdAndTypeAndMstProductFamily_Name(siteId, type,
					productFamilyName);
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return components;
	}
	
	
	/**
	 * constructComponentPriceDto
	 * 
	 * @param orderProductComponent
	 * @param version
	 * @return
	 */
	private QuotePriceBean constructComponentPriceDto(OrderProductComponent orderProductComponent) {
		QuotePriceBean priceDto = null;
		if (orderProductComponent != null && orderProductComponent.getMstProductComponent() != null) {
			OrderPrice price = orderPriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(orderProductComponent.getId()), QuoteConstants.COMPONENTS.toString());
			priceDto = new QuotePriceBean(price);
		}
		return priceDto;

	}
	
	/**
	 * constructAttribute
	 * 
	 * @param orderProductComponentsAttributeValues
	 * @param version
	 * @return
	 */
	private List<OrderProductComponentsAttributeValueBean> constructAttribute(
			Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues) {
		List<OrderProductComponentsAttributeValueBean> orderProductComponentsAttributeValueBean = new ArrayList<>();
		if (orderProductComponentsAttributeValues != null) {
			for (OrderProductComponentsAttributeValue attributeValue : orderProductComponentsAttributeValues) {
				OrderProductComponentsAttributeValueBean qtAttributeValue = new OrderProductComponentsAttributeValueBean(
						attributeValue);
				ProductAttributeMaster productAttributeMaster = attributeValue.getProductAttributeMaster();
				if (productAttributeMaster != null) {
					qtAttributeValue.setDescription(productAttributeMaster.getDescription());
					qtAttributeValue.setName(productAttributeMaster.getName());
				}

				qtAttributeValue.setPrice(constructAttributePriceDto(attributeValue));
				orderProductComponentsAttributeValueBean.add(qtAttributeValue);
			}
		}

		return orderProductComponentsAttributeValueBean;
	}
	
	/**
	 * constructAttributePriceDto
	 * 
	 * @param attributeValue
	 * @param version
	 * @return
	 */
	private QuotePriceBean constructAttributePriceDto(OrderProductComponentsAttributeValue attributeValue) {
		QuotePriceBean priceDto = null;
		if (attributeValue != null && attributeValue.getProductAttributeMaster() != null) {
			OrderPrice attrPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(attributeValue.getId()), QuoteConstants.COMPONENTS.toString());
			priceDto = new QuotePriceBean(attrPrice);
		}
		return priceDto;

	}
	
	/**
	 * 
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteNplSiteBean>
	 */
	private List<OrderProductComponentsAttributeValueBean> getSortedAttributeComponents(
			List<OrderProductComponentsAttributeValueBean> attributeBeans) {
		if (attributeBeans != null) {
			attributeBeans.sort(Comparator.comparingInt(OrderProductComponentsAttributeValueBean::getId));
		}
		return attributeBeans;
	}
	
	/**
	 * Method to get order activation stage
	 * @param orderId
	 * @return
	 * @throws TclCommonException 
	 */
	public List<GdeOdrScheduleAuditBean> getOrderActivationStage(Integer orderId) throws TclCommonException {
		List<GdeOdrScheduleAuditBean> gdeOrderStage = new ArrayList<>();
		try {
			Order order = orderRepository.findByIdAndStatus(orderId, (byte) 1);
			if(order != null) {
				List<OdrScheduleDetailsAudit> orderScheduleDetails = odrScheduleDetailsAuditRepository.findByOrderCodeOrderByIdAsc(order.getOrderCode());
				orderScheduleDetails.stream().forEach(odr->{
					GdeOdrScheduleAuditBean gdeOdrScheduleAuditBean = new GdeOdrScheduleAuditBean(odr);
					gdeOrderStage.add(gdeOdrScheduleAuditBean);
				});
			} else {
				throw new TclCommonException("Invalid OrderId {}",orderId) ;
			}
			
			
		} catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR) ;
		}
		return gdeOrderStage;		
	}
	
	/**
	 * getFeasiblityAndPricingDetails - retrieves feasibility and pricing details
	 * @author archchan 
	 * @param linkId
	 * @return
	 * @throws TclCommonException
	 */
	public GdePricingFeasibilityBean getFeasiblityAndPricingDetails(Integer linkId) throws TclCommonException {
		GdePricingFeasibilityBean link = new GdePricingFeasibilityBean();
		try {
			if (Objects.isNull(linkId)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			Optional<OrderNplLink> orderNplLink = orderNplLinkRepository.findById(linkId);
			if (orderNplLink.isPresent()) {
				List<OrderLinkFeasibility> feasiblityDetails = orderLinkFeasibilityRepository.findByOrderNplLink(orderNplLink.get());
				link = constructOrderLinkPricingFeasibilityDetails(orderNplLink.get(), feasiblityDetails);
			} else {
				throw new TclCommonException(ExceptionConstants.ORDER_LINK_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return link;
	}
	
	/**
	 * constructPricingFeasibilityDetails - constructs feasbility and pricing details as a dto object
	 * @author archchan 
	 * @param orderLink
	 * @param feasiblityDetails
	 * @param pricingDetails
	 * @return
	 * @throws TclCommonException
	 */
	public GdePricingFeasibilityBean constructOrderLinkPricingFeasibilityDetails(OrderNplLink orderLink, List<OrderLinkFeasibility> feasiblityDetails)
			throws TclCommonException {
		if (Objects.isNull(orderLink) || Objects.isNull(feasiblityDetails)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		GdePricingFeasibilityBean link = new GdePricingFeasibilityBean();
		Optional<OrderIllSite> quoteIllOrderSiteA = orderIllSitesRepository.findById(orderLink.getSiteAId());
		Optional<OrderIllSite> quoteIllOrderSiteB = orderIllSitesRepository.findById(orderLink.getSiteBId());
		link.setLinkId(orderLink.getId());
		link.setLinkCode(orderLink.getLinkCode());
		link.setIsFeasible(orderLink.getFeasibility());
		link.setChargeableDistance(orderLink.getChargeableDistance());
		if (quoteIllOrderSiteA.isPresent()) {
			link.setIsTaxExemptedSiteA(quoteIllOrderSiteA.get().getIsTaxExempted());
		}
		if (quoteIllOrderSiteB.isPresent()) {
			link.setIsTaxExemptedSiteB(quoteIllOrderSiteB.get().getIsTaxExempted());
		}
		link.setFeasiblityDetails(constructOrderFeasiblityResponse(feasiblityDetails));
		return link;
	}
	
	/**
	 * constructOrderFeasiblityResponse
	 * 
	 * @param feasiblityDetails
	 * @return
	 */
	private List<GdeFeasiblityBean> constructOrderFeasiblityResponse(List<OrderLinkFeasibility> feasiblityDetails) {
		List<GdeFeasiblityBean> feasiblityResponse = new ArrayList<>();
		feasiblityDetails.stream().forEach(feasiblity -> {
			GdeFeasiblityBean feasiblityBean = new GdeFeasiblityBean();
			feasiblityBean.setId(feasiblity.getId());
			feasiblityBean.setCreatedTime(feasiblity.getCreatedTime());
			feasiblityBean.setFeasibilityCheck(feasiblity.getFeasibilityCheck());
			feasiblityBean.setFeasibilityMode(feasiblity.getFeasibilityMode());
			feasiblityBean.setFeasibilityCode(feasiblity.getFeasibilityCode());
			feasiblityBean.setIsSelected(feasiblity.getIsSelected());
			feasiblityBean.setType(feasiblity.getType());
			feasiblityBean.setProvider(feasiblity.getProvider());
			feasiblityBean.setRank(feasiblity.getRank());
			feasiblityBean.setResponse(feasiblity.getResponseJson());
			feasiblityBean.setLinkId(feasiblity.getOrderNplLink().getId());
			feasiblityResponse.add(feasiblityBean);
		});
		return feasiblityResponse;
	}
	
	/**
	 * getFeasiblityAndPricingDetailsForQuoteNplBean - retrieves feasibility and
	 * pricing details for a link
	 * 
	 * @param linkId
	 * @return
	 * @throws TclCommonException
	 */
	public GdePricingFeasibilityBean getFeasiblityAndPricingDetailsForQuoteNplBean(Integer linkId)
			throws TclCommonException {
		GdePricingFeasibilityBean link = new GdePricingFeasibilityBean();
		try {
			if (Objects.isNull(linkId)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			Optional<QuoteNplLink> quoteNplLink = nplLinkRepository.findById(linkId);
			if (quoteNplLink.isPresent()) {
				List<LinkFeasibility> feasiblityDetails = linkFeasibilityRepository
						.findByQuoteNplLink(quoteNplLink.get());
				link = constructQuoteLinkPricingFeasibilityDetails(quoteNplLink.get(), feasiblityDetails);
			} else {
				throw new TclCommonException(ExceptionConstants.ORDER_LINK_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return link;
	}
	
	/**
	 * constructQuoteLinkPricingFeasibilityDetails
	 * 
	 * @param quoteNplLink
	 * @param feasiblityDetails
	 * @param pricingDetails
	 * @return
	 */
	private GdePricingFeasibilityBean constructQuoteLinkPricingFeasibilityDetails(QuoteNplLink quoteNplLink,
			List<LinkFeasibility> feasiblityDetails) {
		Optional<QuoteIllSite> quoteIllSiteA = illSiteRepository.findById(quoteNplLink.getSiteAId());
		Optional<QuoteIllSite> quoteIllSiteB = illSiteRepository.findById(quoteNplLink.getSiteBId());
		GdePricingFeasibilityBean link = new GdePricingFeasibilityBean();
		link.setLinkId(quoteNplLink.getId());
		link.setLinkCode(quoteNplLink.getLinkCode());
		link.setIsFeasible(quoteNplLink.getFeasibility());
		link.setChargeableDistance(quoteNplLink.getChargeableDistance());
		if (quoteIllSiteA.isPresent())
			link.setIsTaxExemptedSiteA(quoteIllSiteA.get().getIsTaxExempted());
		if (quoteIllSiteB.isPresent())
			link.setIsTaxExemptedSiteB(quoteIllSiteB.get().getIsTaxExempted());
		link.setFeasiblityDetails(constructQuoteFeasiblityResponse(feasiblityDetails));
		return link;
	}
	
	/**
	 * constructQuoteFeasiblityResponse
	 * 
	 * @param feasiblityDetails
	 * @return
	 */
	private List<GdeFeasiblityBean> constructQuoteFeasiblityResponse(List<LinkFeasibility> feasiblityDetails) {
		List<GdeFeasiblityBean> feasiblityResponse = new ArrayList<>();
		feasiblityDetails.stream().forEach(feasiblity -> {
			GdeFeasiblityBean feasiblityBean = new GdeFeasiblityBean();
			feasiblityBean.setId(feasiblity.getId());
			feasiblityBean.setCreatedTime(feasiblity.getCreatedTime());
			feasiblityBean.setFeasibilityCheck(feasiblity.getFeasibilityCheck());
			feasiblityBean.setFeasibilityMode(feasiblity.getFeasibilityMode());
			feasiblityBean.setFeasibilityCode(feasiblity.getFeasibilityCode());
			feasiblityBean.setIsSelected(feasiblity.getIsSelected());
			feasiblityBean.setType(feasiblity.getType());
			feasiblityBean.setProvider(feasiblity.getProvider());
			feasiblityBean.setRank(feasiblity.getRank());
			feasiblityBean.setResponse(feasiblity.getResponseJson());
			feasiblityBean.setLinkId(feasiblity.getQuoteNplLink().getId());
			feasiblityResponse.add(feasiblityBean);
		});
		return feasiblityResponse;
	}
	
	/**
	 * Method to cancel scheduled BOD in MDSO
	 * 	
	 * @param feasiblityDetails
	 * @return
	 * @throws TclCommonException 
	 */
	public String cancelBodSchedule(List<CancelBodScheduleRequest> cancelBodRequests) throws TclCommonException {
		String cancelSchedule = CommonConstants.FAILIURE;
		try {
			
			if (Objects.isNull(cancelBodRequests) || (cancelBodRequests.isEmpty())) {
				throw new TclCommonException(ExceptionConstants.GDE_ORDER_CANCELLATION_INVALID_INPUT,
						ResponseResource.R_CODE_ERROR);
			}
			cancelBodRequests.stream().forEach(cancelBod->{
				LOGGER.info("Cancel booked schedule for scheduleId {} ", cancelBod.getScheduleId());
				try {
					OdrScheduleDetails odrScheduleDetails = odrScheduleDetailsRepository.findByScheduleIdAndMdsoResourceId(cancelBod.getScheduleId(), cancelBod.getResourceId());
					GdeCancelScheduleRequest  gdeCancelScheduleRequest = new GdeCancelScheduleRequest();
					gdeCancelScheduleRequest.setInterfaces("delete_schedule");
					gdeCancelScheduleRequest.setDescription(Utils.generateFeasibilityTitle("deleteScheduleDesc", odrScheduleDetails.getOrderCode()));
					gdeCancelScheduleRequest.setTitle(Utils.generateFeasibilityTitle("deleteSchedule", odrScheduleDetails.getOrderCode()));
					GdeCancelScheduleInputs gdeCanceleScheduleInputs = new GdeCancelScheduleInputs();
					gdeCanceleScheduleInputs.setScheduleId(Arrays.asList(cancelBod.getScheduleId()));
					gdeCanceleScheduleInputs.setCallbackUrl(appHost+notifyUpdateUrl); 
					gdeCanceleScheduleInputs.setDeleteSpecificSchedule(true);
					gdeCancelScheduleRequest.setInputs(gdeCanceleScheduleInputs);
					LOGGER.info("Cancel BOD request payload {} ", Utils.convertObjectToJson(gdeCancelScheduleRequest));
					String token = gdePricingFeasibilityService.generateMdsoAuthToken();
					HttpHeaders headers = new HttpHeaders();
					headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
					headers.add("Authorization", token); 
					RestResponse cancelScheduleResponse = restClientService.post(cancelBodScheduleUrl.replace("<resourceId>",odrScheduleDetails.getMdsoResourceId()),
							Utils.convertObjectToJson(gdeCancelScheduleRequest), headers);
					if(cancelScheduleResponse != null & cancelScheduleResponse.getData() != null && cancelScheduleResponse.getStatus().equals(Status.SUCCESS)) {
//						JSONParser jsonParserf = new JSONParser(); 
//						JSONObject createScheduleObj = (JSONObject) jsonParserf.parse(cancelScheduleResponse.getData());
					} else {
						throw new TclCommonException(ExceptionConstants.MDSO_CANCEL_BOD_FAILED, ResponseResource.R_CODE_ERROR);
					}
					List<OrderToLe> orderLes = orderToLeRepository.findByOrder_OrderCode(odrScheduleDetails.getOrderCode());
					Optional<OrderToLe> orderToLe = orderLes.stream().findFirst();
					orderToLe.get().setStage("CANCELLATION_INITIATED");
					orderToLeRepository.save(orderToLe.get());
					odrScheduleDetails.setActivationStatus("CANCELLATION_INITIATED");				
					odrScheduleDetails = odrScheduleDetailsRepository.save(odrScheduleDetails);	
					gdePricingFeasibilityService.constructOdrScheduleAudit(odrScheduleDetails);
				} catch(Exception e) {
					LOGGER.error("Exception while initiating cancelling scheduled BOD for scheduleId {} ",cancelBod.getScheduleId());
				}
			});
			cancelSchedule = "CANCELLATION_INITIATED";
		} catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return cancelSchedule;
		
	}
	
	/**
	 * 
	 * @link http://www.tatacommunications.com/ updateOrderToLeStatus to update the
	 *       order to le status
	 * @param orderToLeId, status
	 * @return NplQuoteDetail
	 * @throws TclCommonException
	 */

	public GdeQuoteDetail updateOrderToLeStatus(Integer orderToLeId, String stage) throws TclCommonException {
		GdeQuoteDetail quoteDetail = new GdeQuoteDetail();
		try {
			if (Objects.isNull(orderToLeId) || (StringUtils.isEmpty(stage))) {
				throw new TclCommonException(ExceptionConstants.ORDER_TO_LE_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
			Optional<OrderToLe> orderToLe = orderToLeRepository.findById(orderToLeId);
			if (!orderToLe.isPresent())
				throw new TclCommonException(ExceptionConstants.ORDER_TO_LE_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			orderToLe.get().setStage(OrderStagingConstants.valueOf(stage.toUpperCase()).toString());
			orderToLeRepository.save(orderToLe.get());

			Order orders = orderToLe.get().getOrder();
			orders.setStage(OrderStagingConstants.valueOf(stage.toUpperCase()).toString());
			orderRepository.save(orders);

//			if (stage.equalsIgnoreCase(OrderStagingConstants.ORDER_COMPLETED.toString())) {
//				updateSiteStatusToOrderEnrichment(orders.getId());
//				LOGGER.info("MDC Filter token value in before Queue call processOrderEnrichment {} :",
//						MDC.get(CommonConstants.MDC_TOKEN_KEY));
//				//Commented as order freeze is to be confirmed.
//				if (orders.getIsOrderToCashEnabled() != null
//						&& orders.getIsOrderToCashEnabled().equals(CommonConstants.BACTIVE)) {
//					if (orderToLe.get().getOrderType() == null || orderToLe.get().getOrderType().equals("NEW")) {
//						Map<String, Object> requestparam = new HashMap<>();
//						requestparam.put("orderId", orders.getId());
//						requestparam.put("productName", "NPL");
//						requestparam.put("userName", Utils.getSource());
//						mqUtils.send(odrProcessQueue, Utils.convertObjectToJson(requestparam));
//					}
//				}
//				
//			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;

	}
	
	/**
	 * Method to update the order ill sites as order enrichment on launch delivery
	 * ui call.
	 * 
	 * @param orderId
	 */
	private void updateSiteStatusToOrderEnrichment(Integer orderId) {
		Optional<Order> optionalOrder = orderRepository.findById(orderId);
		if (optionalOrder.isPresent()) {
			optionalOrder.get().getOrderToLes().stream().forEach(orderToLe -> {
				orderToLe.getOrderToLeProductFamilies().stream().forEach(family -> {
					if (family.getMstProductFamily().getName()
							.equalsIgnoreCase(OrderDetailsExcelDownloadConstants.NPL)) {
						family.getOrderProductSolutions().stream().forEach(orderProdSol -> {

							List<OrderNplLink> orderNplLink = orderNplLinkRepository
									.findByProductSolutionId(orderProdSol.getId());

							orderNplLink.stream().forEach(link -> {
								MstOrderLinkStatus mstOrderSiteStatus = mstOrderLinkStatusRepository
										.findByName(OrderDetailsExcelDownloadConstants.ORDER_ENRICHMENT);
								// site.setMstOrderSiteStatus(mstOrderSiteStatus);
								link.setMstOrderLinkStatus(mstOrderSiteStatus);
								orderNplLinkRepository.save(link);
							});
						});
					}
				});
			});
		}
	}
	
	/**
	 * getLeAttributes - method to get attribute value for a legal attribute
	 * 
	 * @param orderToLe
	 * @param attr
	 * @return
	 * @throws TclCommonException
	 */
	public String getLeAttributes(OrderToLe orderToLe, String attr) throws TclCommonException {
		MstOmsAttribute mstOmsAttribute = null;
		String attrValue = null;
		if (Objects.isNull(attr) || Objects.isNull(orderToLe)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(attr,
				CommonConstants.BACTIVE);
		if (!mstOmsAttributes.isEmpty()) {
			mstOmsAttribute = mstOmsAttributes.get(0);
		}
		Set<OrdersLeAttributeValue> orderToLeAttribute = ordersLeAttributeValueRepository
				.findByMstOmsAttributeAndOrderToLe(mstOmsAttribute, orderToLe);
		for (OrdersLeAttributeValue quoteLeAttributeValue : orderToLeAttribute) {
			attrValue = quoteLeAttributeValue.getAttributeValue();
		}
		return attrValue;
	}
	
	/**
	 * Method to fetch order schedule status from MDSO
	 * @param orderId
	 * @param orderLeId
	 * @param linkId
	 * @return
	 * @throws TclCommonException
	 */
	public OrderScheduleStageBean orderScheduleCheck(Integer orderId, Integer orderLeId) throws TclCommonException {
		OrderScheduleStageBean orderScheduleStageBean = new OrderScheduleStageBean();
		try {
			LOGGER.info("Entering orderScheduleCheck for orderid {} orderLe {} ",orderId, orderLeId);
			orderScheduleStageBean.setOrderLeId(orderLeId);
			orderScheduleStageBean.setIsScheduleUpdated(FPConstants.FALSE.toString());
			Optional<OrderToLe> orderToLes = orderToLeRepository.findById(orderLeId);
			if(orderToLes.isPresent()) {
				List<OrderNplLink> orderLinks = orderNplLinkRepository.findByOrderId(orderId);
				orderLinks.stream().forEach(link->{
					OdrScheduleDetails odrSchdeuleDetails = odrScheduleDetailsRepository.findByOrderCodeAndOrderLinkId(orderToLes.get().getOrder().getOrderCode(), link.getId());
					if(odrSchdeuleDetails != null) {
						if(odrSchdeuleDetails.getScheduleId() != null || odrSchdeuleDetails.getActivationStatus().equalsIgnoreCase("BOOKING_FAILED")) {
							orderScheduleStageBean.setIsScheduleUpdated(FPConstants.TRUE.toString());
						}
					} else {
						LOGGER.error("Error occured while chekcing order Schedule status for orderLe {} and linkId {}",orderLeId, link.getId());
					}
				});
				
				
			}
		} catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return orderScheduleStageBean;
	}
	
	
}


