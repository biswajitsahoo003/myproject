package com.tcl.dias.oms.factory.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.Approver;
import com.tcl.dias.common.beans.MDMServiceDetailBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailsBean;
import com.tcl.dias.common.sfdc.bean.FeasibilityRequestBean;
import com.tcl.dias.common.sfdc.bean.OpportunityBean;
import com.tcl.dias.common.sfdc.bean.ProductServiceBean;
import com.tcl.dias.common.sfdc.bean.SiteSolutionOpportunityBean;
import com.tcl.dias.common.sfdc.bean.UpdateOpportunityStage;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.MDMServiceInventoryBean;
import com.tcl.dias.oms.beans.SolutionDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.cancellation.service.v1.CancellationService;
import com.tcl.dias.oms.constants.AttributeConstants;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.OmsExcelConstants;
import com.tcl.dias.oms.entity.entities.MacdDetail;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteSiteDifferentialCommercial;
import com.tcl.dias.oms.entity.entities.QuoteSiteServiceTerminationDetails;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MacdDetailRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteSiteDifferentialCommercialRepository;
import com.tcl.dias.oms.entity.repository.QuoteSiteServiceTerminationDetailsRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.iwan.service.v1.IwanQuoteService;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.pricing.bean.NotFeasible;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.core.IOmsSfdcInputHandler;
import com.tcl.dias.oms.sfdc.core.OmsAbstractSfdcHandler;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * This file contains the OmsSfdcIWANMapper.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class OmsSfdcIWANMapper extends OmsAbstractSfdcHandler implements IOmsSfdcInputHandler {

	@Autowired
	IllSiteRepository illSitesRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	OmsSfdcService omsSfdcService;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	SiteFeasibilityRepository siteFeasibilityRepository;

	@Autowired
	MACDUtils macdUtils;

	@Autowired
	MacdDetailRepository macdDetailRepository;
	
	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(OmsSfdcIWANMapper.class);


	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;
	
	@Value("${rabbitmq.si.order.details.queue}")
	String siOrderDetailsQueue;
	
	@Autowired
	protected MQUtils mqUtils;
	
	@Autowired
	CancellationService cancellationService;
	
	@Autowired
	QuoteSiteDifferentialCommercialRepository quoteSiteDifferentialCommercialRepository;
	
	@Autowired
	IwanQuoteService iwanQuoteService;
	
	@Autowired
	OrderIllSitesRepository orderIllSiteRepository;

	@Autowired
	QuoteSiteServiceTerminationDetailsRepository quoteSiteServiceTerminationDetailsRepository;

	@Value("${rabbitmq.customer.code.sfdc.queue}")
	protected String customerCodeSfdcQueue;
	
	@Value("${rabbitmq.customer.account.manager.email}")
	String customerAccountManagerName;



	/**
	 * getOpportunityBean
	 * 
	 * @param quoteToLe
	 * @param opportunityBean
	 * @return
	 */

	@Override
	public OpportunityBean getOpportunityBean(QuoteToLe quoteToLe, OpportunityBean opportunityBean)
			throws TclCommonException {
		opportunityBean.setSelectProductType(SFDCConstants.IWAN.toString());

		if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			LOGGER.info("quote type,quote category iwan"+quoteToLe.getQuoteType()+""+quoteToLe.getQuoteCategory());
			SIServiceDetailDataBean siServiceDetailBean = null;
			
			/*
			 * Optional<SIServiceDetailDataBean> siServiceDetailDataBean =
			 * orderData.getServiceDetails().stream() .filter(b ->
			 * b.getId().equals(quoteToLe.getErfServiceInventoryServiceDetailId())).
			 * findFirst();
			 */
			opportunityBean.setiLLAutoCreation(SFDCConstants.IWAN);
			List<SIServiceDetailsBean> serviceDetailsList = macdUtils.getServiceDetailsBeanList(quoteToLe, opportunityBean);
			if (MACDConstants.REQUEST_TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				opportunityBean.setType(SFDCConstants.TERMINATION);
				opportunityBean.setSubType(SFDCConstants.ONCUSTOMERDEMAND);
				// opportunityBean.setParentOpportunityName("716474");
				opportunityBean.setParentOpportunityName(quoteToLe.getTpsSfdcParentOptyId().toString());
				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String formatDateTime = now.format(formatter);
				opportunityBean.setCustomerMailReceivedDate(formatDateTime);
				// query macd_detail to get cancellation date and reason for cancellation
				MacdDetail macdDetail = macdDetailRepository.findByQuoteToLeId(quoteToLe.getId());
				if (macdDetail != null && Objects.nonNull(macdDetail.getCancellationDate())) {
					DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
					String formattedDate = dateFormatter.format(macdDetail.getCancellationDate());
					opportunityBean.setEffectiveDateOfChange(formattedDate);
					opportunityBean.setReasonForTermination(macdDetail.getCancellationReason());
				}
				/*
				 * if (siServiceDetailDataBean.isPresent()) {
				 * opportunityBean.setPreviousMRC(siServiceDetailDataBean.get().getMrc() == null
				 * ? StringUtils.EMPTY : siServiceDetailDataBean.get().getMrc().toString());
				 * opportunityBean.setPreviousNRC(siServiceDetailDataBean.get().getNrc() == null
				 * ? StringUtils.EMPTY : siServiceDetailDataBean.get().getNrc().toString()); }
				 */
				if (serviceDetailsList != null && !serviceDetailsList.isEmpty()) {
					setPreviousMrcNrc(opportunityBean, serviceDetailsList);
				} else {
					opportunityBean.setPreviousMRC(StringUtils.EMPTY);
					opportunityBean.setPreviousNRC(StringUtils.EMPTY);
				}
			} else if (MACDConstants.ADD_IP_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {

				opportunityBean.setType(SFDCConstants.HOTUPGRADE);
				opportunityBean.setSubType(SFDCConstants.HOTUPGRADE);
				// opportunityBean.setParentOpportunityName("716474");
				opportunityBean.setParentOpportunityName(quoteToLe.getTpsSfdcParentOptyId().toString());
				if (serviceDetailsList != null && !serviceDetailsList.isEmpty()) {
					setPreviousMrcNrc(opportunityBean, serviceDetailsList);
				} else {
					opportunityBean.setPreviousMRC(StringUtils.EMPTY);
					opportunityBean.setPreviousNRC(StringUtils.EMPTY);
				}
			} else if (MACDConstants.CHANGE_BANDWIDTH_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				opportunityBean.setType(SFDCConstants.PARALLELUPGRADE);
				opportunityBean.setSubType(SFDCConstants.PARALLELUPGRADE);
					
				// opportunityBean.setParentOpportunityName("716474");
				if(quoteToLe!=null && quoteToLe.getTpsSfdcParentOptyId()!=null) {
					opportunityBean.setParentOpportunityName(quoteToLe.getTpsSfdcParentOptyId().toString());
				}
				if (serviceDetailsList != null && !serviceDetailsList.isEmpty()) {
					setPreviousMrcNrc(opportunityBean, serviceDetailsList);
				}  else {
					opportunityBean.setPreviousMRC(StringUtils.EMPTY);
					opportunityBean.setPreviousNRC(StringUtils.EMPTY);
				}
			} else if (MACDConstants.SHIFT_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				opportunityBean.setType(SFDCConstants.PARALLELUPGRADE);
				opportunityBean.setSubType(SFDCConstants.PARALLELUPGRADE);

				// opportunityBean.setParentOpportunityName("716474");
				opportunityBean.setParentOpportunityName(quoteToLe.getTpsSfdcParentOptyId().toString());
				if (serviceDetailsList != null && !serviceDetailsList.isEmpty()) {
					setPreviousMrcNrc(opportunityBean, serviceDetailsList);
				} else {
					opportunityBean.setPreviousMRC(StringUtils.EMPTY);
					opportunityBean.setPreviousNRC(StringUtils.EMPTY);
				}
			}else if (MACDConstants.OTHERS.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				LOGGER.info("Others scenario");
				opportunityBean.setType(SFDCConstants.OTHERS_SFDC);
				opportunityBean.setSubType("");
		}
		} else if(MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			LOGGER.info("Inside Cancellation if condition");
			if(quoteToLe!=null && quoteToLe.getTpsSfdcParentOptyId()!=null) {
				opportunityBean.setParentOpportunityName(quoteToLe.getTpsSfdcParentOptyId().toString());
			}
			opportunityBean.setType(SFDCConstants.CANCELLATION_ORDER);
			opportunityBean.setSubType(SFDCConstants.CANCELLATION_ORDER);
			opportunityBean.setPreviousMRC(StringUtils.EMPTY);
			opportunityBean.setPreviousNRC(StringUtils.EMPTY);
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = dateFormatter.format(new Date());
			opportunityBean.setEffectiveDateOfChange(formattedDate);
			
			List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.getId());
			String serviceIds = quoteIllSiteToServiceList.stream().findFirst().get().getErfServiceInventoryTpsServiceId();
			LOGGER.info("service ids {}", serviceIds);
			if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
				if(quoteIllSiteToServiceList.size() > 1) {
					opportunityBean.setCurrentCircuitServiceId(serviceIds + "- Multiple");
				} else {
					opportunityBean.setCurrentCircuitServiceId(serviceIds);
				}
				
			}
		} else if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			LOGGER.info("Inside Termination condition for create opportunity iwan");
			if(quoteToLe!=null && quoteToLe.getTpsSfdcParentOptyId()!=null) {
				opportunityBean.setParentOpportunityName(quoteToLe.getTpsSfdcParentOptyId().toString());
			}
			String custId = quoteToLe.getQuote().getCustomer().getErfCusCustomerId().toString();
			String name = (String) mqUtils.sendAndReceive(customerAccountManagerName, custId);
			LOGGER.info("Owner name {}", name);
			opportunityBean.setOwnerName(name);
			opportunityBean.setType(SFDCConstants.TERMINATION);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 7);
			opportunityBean.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
			
			List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.getId());
			String serviceIds = quoteIllSiteToServiceList.stream().findFirst().get().getErfServiceInventoryTpsServiceId();
			LOGGER.info("service ids {}", serviceIds);
			if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
				if(quoteIllSiteToServiceList.size() > 1) {
					if(opportunityBean.getDummyParentTerminationOpportunity() != null && 
							"true".equalsIgnoreCase(opportunityBean.getDummyParentTerminationOpportunity())) {
					opportunityBean.setCurrentCircuitServiceId(serviceIds + "- Multiple");
					}
				} else {
					opportunityBean.setCurrentCircuitServiceId(serviceIds);
				}
			}
			QuoteSiteServiceTerminationDetails quoteSiteServiceTerminationDetails = null;
				if(quoteToLe.getIsMultiCircuit() != null && CommonConstants.BACTIVE.equals(quoteToLe.getIsMultiCircuit()) && !"true".equalsIgnoreCase(opportunityBean.getDummyParentTerminationOpportunity())) {
					Optional<QuoteIllSiteToService> quoteIllSiteToServiceOpt = quoteIllSiteToServiceList.stream().filter(service -> service.getErfServiceInventoryTpsServiceId().equalsIgnoreCase(opportunityBean.getCurrentCircuitServiceId())).findFirst();
					if(quoteIllSiteToServiceOpt.isPresent()) {
					LOGGER.info("quoteIllSiteToServiceOpt service id {}, opty id {}", quoteIllSiteToServiceOpt.get().getErfServiceInventoryTpsServiceId(), quoteIllSiteToServiceOpt.get().getTpsSfdcOptyId());
					quoteSiteServiceTerminationDetails = quoteSiteServiceTerminationDetailsRepository.findByQuoteIllSiteToService(quoteIllSiteToServiceOpt.get());
					}
				}
				else {
					 quoteSiteServiceTerminationDetails = quoteSiteServiceTerminationDetailsRepository.findByQuoteIllSiteToService(quoteIllSiteToServiceList.stream().findFirst().get());
				}
				
				LOGGER.info("quoteSiteServiceTerminationDetailsList iwan--- {}", quoteSiteServiceTerminationDetails.toString());
				if(Objects.nonNull(quoteSiteServiceTerminationDetails)) {
					opportunityBean.setSubType(quoteSiteServiceTerminationDetails.getTerminationSubtype());
					DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
					if(quoteSiteServiceTerminationDetails.getEffectiveDateOfChange() != null) {
					String formattedECDate = dateFormatter.format(quoteSiteServiceTerminationDetails.getEffectiveDateOfChange());
					opportunityBean.setEffectiveDateOfChange(formattedECDate);
					}
					opportunityBean.setReasonForTermination(quoteSiteServiceTerminationDetails.getReasonForTermination());
					if(quoteSiteServiceTerminationDetails.getCustomerMailReceivedDate() != null) {
					String formattedCMRDate = dateFormatter.format(quoteSiteServiceTerminationDetails.getCustomerMailReceivedDate());
					opportunityBean.setCustomerMailReceivedDate(formattedCMRDate);
					}
					if(quoteSiteServiceTerminationDetails.getTermInMonths() != null)
						opportunityBean.setTermOfMonths(quoteSiteServiceTerminationDetails.getTermInMonths().toString());
					opportunityBean.setTerminationSubReason(quoteSiteServiceTerminationDetails.getSubReason());
					if(quoteSiteServiceTerminationDetails.getTerminationSendToTdDate() != null) {
					String formattedTDDate = dateFormatter.format(quoteSiteServiceTerminationDetails.getTerminationSendToTdDate());
					opportunityBean.setTerminationSendToTDDate(formattedTDDate);
					}
					opportunityBean.setInternalOrCustomer(quoteSiteServiceTerminationDetails.getInternalCustomer());

					String csmSfdcCode = getSfdcCodeForCustomerEmailId("CSM" , quoteSiteServiceTerminationDetails);
					LOGGER.info("csmSfdcCode iwan{}" , csmSfdcCode);
					opportunityBean.setCsmNonCsm(csmSfdcCode);
					String commRecipientCode = getSfdcCodeForCustomerEmailId("CR" , quoteSiteServiceTerminationDetails);
					LOGGER.info("commRecipientCode {}" , commRecipientCode);
					opportunityBean.setCommunicationRecipient(commRecipientCode);
					opportunityBean.setHandoverTo(quoteSiteServiceTerminationDetails.getHandoverTo());
					
					// get account manager
					String OrderDetailsQueue = (String) mqUtils.sendAndReceive(siOrderDetailsQueue, quoteSiteServiceTerminationDetails.getQuoteIllSiteToService().getErfServiceInventoryTpsServiceId());
					SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
							.convertJsonToObject(OrderDetailsQueue, SIServiceDetailsBean[].class);
					List<SIServiceDetailsBean> serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
					if (serviceDetailsList != null && !serviceDetailsList.isEmpty()) {
						setPreviousMrcNrc(opportunityBean, serviceDetailsList);
					//	opportunityBean.setOwnerName(serviceDetailsList.get(0).getAccountManager());
					} else {
						opportunityBean.setPreviousMRC(StringUtils.EMPTY);
						opportunityBean.setPreviousNRC(StringUtils.EMPTY);
					}
						
					

				}

			
		/*
		 * List<SIServiceDetailsBean> serviceDetailsList =
		 * macdUtils.getServiceDetailsBeanList(quoteToLe, opportunityBean); if
		 * (serviceDetailsList != null && !serviceDetailsList.isEmpty()) {
		 * setPreviousMrcNrc(opportunityBean, serviceDetailsList); } else {
		 * opportunityBean.setPreviousMRC(StringUtils.EMPTY);
		 * opportunityBean.setPreviousNRC(StringUtils.EMPTY); }
		 */

		}

		/* 
		 * if(Objects.nonNull(quoteToLe.getIsAmended())){ int result =
		 * Byte.compare(quoteToLe.getIsAmended(), BACTIVE); if (result == 0){
		 * opportunityBean.setType("Cancellation"); } }
		 */
		//else opportunityBean.setType(SFDCConstants.NEW_ORDER);

		LOGGER.info("opportunitybean type and subtype"+opportunityBean.getType()+" "+opportunityBean.getSubType());
		return opportunityBean;
	}

	private void setPreviousMrcNrc(OpportunityBean opportunityBean, List<SIServiceDetailsBean> serviceDetailsList) {
		Double mrc = serviceDetailsList.stream().mapToDouble(serviceDetail -> new Double(serviceDetail.getMrc())).sum();
		Double nrc = serviceDetailsList.stream().mapToDouble(serviceDetail -> new Double(serviceDetail.getNrc())).sum();
			opportunityBean.setPreviousMRC(mrc == null ? StringUtils.EMPTY
					: String.valueOf(mrc));
			opportunityBean.setPreviousNRC(nrc == null ? StringUtils.EMPTY
					: String.valueOf(nrc));
	}

	/**
	 * getProductServiceInput
	 * 
	 * @param quoteToLe
	 * @param productServiceBean
	 * @param solution
	 * @return
	 * @throws TclCommonException
	 */
	@Override
	public ProductServiceBean getProductServiceInput(QuoteToLe quoteToLe, ProductServiceBean productServiceBean,
			ProductSolution productSolution) throws TclCommonException {
		String variant = CommonConstants.EMPTY;
		String flavour = CommonConstants.EMPTY;
		productServiceBean.setProductType(SFDCConstants.IZO_INTERNET_WAN.toString());
		productServiceBean.setProductLineOfBusiness(SFDCConstants.IPVPN.toString());
		if(productSolution != null && productSolution.getProductProfileData() != null) {
		SolutionDetail solution = (SolutionDetail) Utils.convertJsonToObject(productSolution.getProductProfileData(),
				SolutionDetail.class);
		if(solution != null) {
		for (ComponentDetail component : solution.getComponents()) {
			for (AttributeDetail attr : component.getAttributes()) {
				if (attr.getName().equals(FPConstants.SERVICE_VARIANT.toString())) {
					variant = attr.getValue();
					//break;
					LOGGER.info("variant1 is ---> {} ", variant);
				}
				if (attr.getName().equals(FPConstants.SERVICE_FLAVOUR.toString())) {
					flavour = attr.getValue();
					LOGGER.info("Flavour1 is ---> {} ", flavour);
					//break;
				}
			}
		}
		
		LOGGER.info("variant1: {}, flavour1: {}",variant,flavour);
		if (variant.contains(FPConstants.STANDARD.toString())) {
			variant = SFDCConstants.STANDARD_INTERNET_ACCESS.toString();// need to change shoul be "Global VPN"
		} else {
			if(flavour.equalsIgnoreCase("Enhanced")) {
				variant = "IZO Internet WAN Enhanced";
			}else {
				variant = "IZO Internet WAN Essential";
			}
		}
		LOGGER.info("Set variant 1: {}",variant);
		productServiceBean.setType(variant);
		}
		}
		productServiceBean.setRecordTypeName(SFDCConstants.IZO_INTERNET_WAN.toString());

		if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			if (MACDConstants.REQUEST_TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				productServiceBean.setOrderType(SFDCConstants.TERMINATION);
				productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
			} else if (MACDConstants.ADD_IP_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				productServiceBean.setOrderType(SFDCConstants.HOTUPGRADE);
				productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
			} else if (MACDConstants.SHIFT_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				productServiceBean.setOrderType(SFDCConstants.PARALLELUPGRADE);
				productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
			} else if (MACDConstants.CHANGE_BANDWIDTH_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				productServiceBean.setOrderType(SFDCConstants.PARALLELUPGRADE);
				productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
			}else if (MACDConstants.OTHERS.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
                productServiceBean.setOrderType(MACDConstants.CHANGE_ORDER+" \u2013 "+CommonConstants.UPGRADE);
                LOGGER.info("Order Type for Product Services is ----> {} for quote code ----> {} " , productServiceBean.getOrderType(), quoteToLe.getQuote().getQuoteCode());
                productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
            } 
		} else if(MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
        	productServiceBean.setOrderType(SFDCConstants.NEW_ORDER);
			productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
			productServiceBean.setIsCancel(true);
        } else if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			productServiceBean.setOrderType(SFDCConstants.TERMINATION);
			productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
		}
		LOGGER.info("ordertype"+productServiceBean.getOrderType());
		return productServiceBean;

	}

	/**
	 * updateProductServiceInput
	 * 
	 * @param quoteToLe
	 * @param productServiceBean
	 * @param solution
	 * @return
	 * @throws TclCommonException
	 */
	@Override
	public ProductServiceBean updateProductServiceInput(QuoteToLe quoteToLe, ProductServiceBean productServiceBean,
			ProductSolution productSolution) throws TclCommonException {
		String variant = CommonConstants.EMPTY;
		String flavour = CommonConstants.EMPTY;
		productServiceBean.setProductLineOfBusiness(SFDCConstants.IPVPN.toString());
		if(productSolution != null && productSolution.getProductProfileData() != null) {
		SolutionDetail solution = (SolutionDetail) Utils.convertJsonToObject(productSolution.getProductProfileData(),
				SolutionDetail.class);
		if(solution != null) {
		for (ComponentDetail component : solution.getComponents()) {
			for (AttributeDetail attr : component.getAttributes()) {
				if (attr.getName().equals(FPConstants.SERVICE_VARIANT.toString())) {
					variant = attr.getValue();
					LOGGER.info("Variant2 is ---> {} ", variant);
					//break;
				}
				if (attr.getName().equals(FPConstants.SERVICE_FLAVOUR.toString())) {
					flavour = attr.getValue();
					LOGGER.info("Flavour2 is ---> {} ", flavour);
					//break;
				}
			}

		}
		LOGGER.info("variant2: {}, flavour2: {}",variant,flavour);
		
		if (variant.contains(FPConstants.STANDARD.toString())) {
			variant = SFDCConstants.STANDARD_INTERNET_ACCESS.toString();
		} else {
			if(flavour.equalsIgnoreCase("Enhanced")) {
				variant = "IZO Internet WAN Enhanced";
			}else {
				variant = "IZO Internet WAN Essential";
			}
		}
		LOGGER.info("Set variant2: {}",variant);
		productServiceBean.setType(variant);
		}
		}
		productServiceBean.setRecordTypeName(SFDCConstants.IZO_INTERNET_WAN.toString());
		// if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()))
		// {
		// if
		// (MACDConstants.ADD_IP_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory()))
		// {
		// productServiceBean.setProductMRC(quoteToLe.getFinalMrc());
		// productServiceBean.setProductARC(quoteToLe.getFinalArc());
		// } else if
		// (MACDConstants.SHIFT_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory()))
		// {
		// productServiceBean.setProductMRC(quoteToLe.getFinalMrc());
		// productServiceBean.setProductARC(quoteToLe.getFinalArc());
		// } else if
		// (MACDConstants.CHANGE_BANDWIDTH_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory()))
		// {
		// productServiceBean.setProductMRC(quoteToLe.getFinalMrc());
		// productServiceBean.setProductARC(quoteToLe.getFinalArc());
		// }
		// }
		if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()))
		{
			UpdateRequest updateRequest = new UpdateRequest();
			OpportunityBean opportunityBean = new OpportunityBean();
			Quote quote = quoteToLe.getQuote();
			//Quote quote = quoteToLe.getQuote();
			if (Objects.nonNull(quote)) {
				Optional<QuoteIllSite> illSiteOpt = quoteToLe.getQuote().getQuoteToLes().stream().findFirst().map(QuoteToLe::getQuoteToLeProductFamilies)
						.get().stream().map(QuoteToLeProductFamily::getProductSolutions)
						.findFirst().get().stream()
						.map(ProductSolution::getQuoteIllSites).findFirst().get()
						.stream().findFirst();
				LOGGER.info("Site before update opportunity is ::::: {}", illSiteOpt.get().getId());
				updateRequest.setSiteId(Objects.nonNull(illSiteOpt.get().getId())? illSiteOpt.get().getId():-1);
				updateRequest.setQuoteToLe(quoteToLe.getId());
				opportunityBean = omsSfdcService.updateOrderTypeAndSubtype(updateRequest);
				String orderType=opportunityBean.getType();
				if(orderType!=null) {
					orderType=orderType.replace("-", "\u2013");
				}
				productServiceBean.setOrderType(orderType);
			}

		} else if (MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			LOGGER.info("Inside Cancellation if condition updateProductServiceInput");
			OpportunityBean opportunityBean = new OpportunityBean();
			productServiceBean.setProductType(SFDCConstants.IZO_INTERNET_WAN.toString());
			Quote quote = quoteToLe.getQuote();
			
			if (Objects.nonNull(quote)) {
				Optional<QuoteIllSite> illSiteOpt = quote.getQuoteToLes().stream().findFirst()
						.map(QuoteToLe::getQuoteToLeProductFamilies).get().stream()
						.map(QuoteToLeProductFamily::getProductSolutions).findFirst().get().stream()
						.map(ProductSolution::getQuoteIllSites).findFirst().get().stream().findFirst();
				List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository
						.findByQuoteToLe_Id(quote.getQuoteToLes().stream().findFirst().get().getId());
				if (quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
					
					Double[] diffMrc = {0D};
					Double[] diffNrc = {0D};
					for(QuoteIllSiteToService quoteIllSiteToService : quoteIllSiteToServiceList) {
						if(quoteIllSiteToService.getQuoteIllSite().getProductSolution().getId().equals(productSolution.getId())) {
					MDMServiceInventoryBean serviceDetail = cancellationService.getServiceDetailsForOrderCancellation(1, 10, null, null, null, null,
							quoteIllSiteToService.getErfServiceInventoryTpsServiceId(), null, null);
					if(serviceDetail != null && serviceDetail.getServiceDetailBeans() != null && !serviceDetail.getServiceDetailBeans().isEmpty()) {
						LOGGER.info("service id {}, mrc {}, nrc {}, site Code {}, order code {}", serviceDetail.getServiceDetailBeans().get(0).getServiceId(), serviceDetail.getServiceDetailBeans().get(0).getMrc(), 
								serviceDetail.getServiceDetailBeans().get(0).getNrc(), serviceDetail.getServiceDetailBeans().get(0).getSiteCode(), serviceDetail.getServiceDetailBeans().get(0).getOrderCode());
						// Differential mrc/nrc 
						List<QuoteSiteDifferentialCommercial> quoteSiteDifferentialCommercialList = null;
						MDMServiceDetailBean optimusMdmDetailBean = null;
						if(serviceDetail.getServiceDetailBeans() != null && serviceDetail.getServiceDetailBeans().size() > 1) {
							LOGGER.info("multiple records from cancellation view for this service id ");
							Optional<MDMServiceDetailBean> optimusMdmDetailBeanOpt = serviceDetail.getServiceDetailBeans().stream().filter(si->si.getSourceSystem().equalsIgnoreCase("OPTIMUS_O2C")).findFirst();
							if(optimusMdmDetailBeanOpt.isPresent()) {
								optimusMdmDetailBean = optimusMdmDetailBeanOpt.get();	
							}
						} else {
							optimusMdmDetailBean = serviceDetail.getServiceDetailBeans().get(0);
						}
						if(optimusMdmDetailBean != null) {
						if(optimusMdmDetailBean.getSiteCode() != null && optimusMdmDetailBean.getOrderCode() != null && optimusMdmDetailBean.getSourceSystem().equalsIgnoreCase("OPTIMUS_O2C")) {
							LOGGER.info("site code and order code not null");
							quoteSiteDifferentialCommercialList =quoteSiteDifferentialCommercialRepository.findByQuoteSiteCodeAndQuoteCode(optimusMdmDetailBean.getSiteCode(), optimusMdmDetailBean.getOrderCode());
							if(quoteSiteDifferentialCommercialList != null && !quoteSiteDifferentialCommercialList.isEmpty()) {
								LOGGER.info("Entry present in quote site commercial");
								quoteSiteDifferentialCommercialList.stream().forEach(differentialCommercial -> {
									diffMrc[0] += differentialCommercial.getDifferentialMrc();
									diffNrc[0] += differentialCommercial.getDifferentialNrc();
								});
								
							} else {
								LOGGER.info("Entry is not present in quote site commercial, saving in quote site diff commercial");
								List<QuoteIllSite> quoteIllSitesList = illSitesRepository.findBySiteCodeAndStatus(optimusMdmDetailBean.getSiteCode(), CommonConstants.BACTIVE);
								List<OrderIllSite> orderIllSitesList = orderIllSiteRepository.findBySiteCodeAndStatus(optimusMdmDetailBean.getSiteCode(), CommonConstants.BACTIVE);
								if(quoteIllSitesList != null && orderIllSitesList != null && !orderIllSitesList.isEmpty() && !quoteIllSitesList.isEmpty()) {
								quoteSiteDifferentialCommercialList = iwanQuoteService.persistQuoteSiteCommercialsAtServiceIdLevel(quoteIllSitesList.get(0), quoteIllSitesList.get(0).getProductSolution().getQuoteToLeProductFamily().getQuoteToLe(), orderIllSitesList.get(0));
								quoteSiteDifferentialCommercialList.stream().forEach(differentialCommercial -> {
									diffMrc[0] += differentialCommercial.getDifferentialMrc();
									diffNrc[0] += differentialCommercial.getDifferentialNrc();
								});
								}
							}
							
							
							} else {
								SIServiceDetailDataBean posServiceDetails;
								try {
									//if macd differ mrc = cancellationViewPosmrc - inventorymrc
									LOGGER.info("POS ILL Fetching  service details for {} ",optimusMdmDetailBean.getServiceId());
									//create new queue call to take data from siservice details instead of view 
									posServiceDetails = macdUtils.getUnderProvisioningServiceDetail(quoteIllSiteToService.getErfServiceInventoryTpsServiceId());
								} catch (TclCommonException e) {
									LOGGER.info("Error in POS condition persistQuoteSiteCommercialsAtServiceIdLevel {}", e);
									throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
									
								}
								cancellationService.getPosDifferentialCommercial(diffMrc, diffNrc, productServiceBean,
										posServiceDetails, optimusMdmDetailBean);
							}
					}
						
					}
				}
					}
					LOGGER.info("Differential MRC {}, diff NRC {}", diffMrc[0], diffNrc[0]);
					productServiceBean.setProductMRC(0-diffMrc[0]);
					productServiceBean.setProductNRC(0-diffNrc[0]);
				}
			}
		}

		return productServiceBean;

	}
	/**
	 * getSiteBeanInput
	 * 
	 * @param quoteToLe
	 * @param solutionOpportunityBean
	 * @return
	 */
	@Override
	public SiteSolutionOpportunityBean getSiteBeanInput(QuoteToLe quoteToLe,
			SiteSolutionOpportunityBean solutionOpportunityBean) {
		// TODO Auto-generated method stub
		return solutionOpportunityBean;
	}

	/**
	 * getOpportunityUpdate
	 * 
	 * @param quoteToLe
	 * @param updateOpportunityStage
	 * @return
	 */
	@Override
	public UpdateOpportunityStage getOpportunityUpdate(QuoteToLe quoteToLe,
			UpdateOpportunityStage updateOpportunityStage) throws TclCommonException {
		LOGGER.info("Inside getOpportunityUpdate");
		if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()))
		{

			UpdateRequest updateRequest = new UpdateRequest();
			OpportunityBean opportunityBean = new OpportunityBean();
			Quote quote = quoteToLe.getQuote();
			if (Objects.nonNull(quote)) {
				Optional<QuoteIllSite> illSiteOpt = quoteToLe.getQuote().getQuoteToLes().stream().findFirst().map(QuoteToLe::getQuoteToLeProductFamilies)
						.get().stream().map(QuoteToLeProductFamily::getProductSolutions)
						.findFirst().get().stream()
						.map(ProductSolution::getQuoteIllSites).findFirst().get()
						.stream().findFirst();
				updateRequest.setSiteId(illSiteOpt.get().getId());
				updateRequest.setQuoteToLe(quoteToLe.getId());
				opportunityBean = omsSfdcService.updateOrderTypeAndSubtype(updateRequest);
				LOGGER.info("OpportunityBean is {}",opportunityBean);
				updateOpportunityStage.setType(opportunityBean.getType());
				updateOpportunityStage.setSub_Type__c(opportunityBean.getSubType());
				updateOpportunityStage.setCopfIdC(opportunityBean.getCopfIdC());
			}

		} else if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			updateOpportunityStage.setType(SFDCConstants.TERMINATION);
			updateOpportunityStage.setCloseDate(DateUtil.convertDateToString(new Date()));
			if(updateOpportunityStage.getDummyParentTerminationOpportunity() == null || (updateOpportunityStage.getDummyParentTerminationOpportunity() != null
				&& "false".equalsIgnoreCase(updateOpportunityStage.getDummyParentTerminationOpportunity()))){
			List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.getId());
			QuoteSiteServiceTerminationDetails quoteSiteServiceTerminationDetails = quoteSiteServiceTerminationDetailsRepository.findByQuoteIllSiteToService(quoteIllSiteToServiceList.stream().findFirst().get());
			LOGGER.info("quoteSiteServiceTerminationDetailsList--- {}", quoteSiteServiceTerminationDetails.toString());
			if(Objects.nonNull(quoteSiteServiceTerminationDetails)) {
				updateOpportunityStage.setSub_Type__c(quoteSiteServiceTerminationDetails.getTerminationSubtype());
				updateOpportunityStage.setReasonForTermination(quoteSiteServiceTerminationDetails.getReasonForTermination());
				updateOpportunityStage.setTerminationSubReason(quoteSiteServiceTerminationDetails.getSubReason());
				DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
				if(quoteSiteServiceTerminationDetails.getEffectiveDateOfChange() != null) {
				String formattedECDate = dateFormatter.format(quoteSiteServiceTerminationDetails.getEffectiveDateOfChange());
				updateOpportunityStage.setEffectiveDateOfChange(formattedECDate);
				}
				if(quoteSiteServiceTerminationDetails.getCustomerMailReceivedDate() != null) {
				String formattedCMRDate = dateFormatter.format(quoteSiteServiceTerminationDetails.getCustomerMailReceivedDate());
				updateOpportunityStage.setCustomerMailReceivedDate(formattedCMRDate);
				}
				if(quoteSiteServiceTerminationDetails.getTermInMonths() != null)
					updateOpportunityStage.setTermOfMonths(quoteSiteServiceTerminationDetails.getTermInMonths().toString());
				if(quoteSiteServiceTerminationDetails.getTerminationSendToTdDate() != null) {
				String formattedTDDate = dateFormatter.format(quoteSiteServiceTerminationDetails.getTerminationSendToTdDate());
				updateOpportunityStage.setTerminationSendToTDDate(formattedTDDate);
				}
				updateOpportunityStage.setInternalOrCustomer(quoteSiteServiceTerminationDetails.getInternalCustomer());

				String csmSfdcCode = getSfdcCodeForCustomerEmailId("CSM" , quoteSiteServiceTerminationDetails);
				LOGGER.info("csmSfdcCode {}" , csmSfdcCode);
				updateOpportunityStage.setCsmNonCsm(csmSfdcCode);
				String commRecipientCode = getSfdcCodeForCustomerEmailId("CR" , quoteSiteServiceTerminationDetails);
				LOGGER.info("commRecipientCode {}" , commRecipientCode);
				updateOpportunityStage.setCommunicationRecipient(commRecipientCode);
				updateOpportunityStage.setRetentionReason(quoteSiteServiceTerminationDetails.getRetentionReason());
				updateOpportunityStage.setHandoverTo(MACDConstants.SEND_TO_TD);
			}
		
			// setting copf like it is done in macd for now --- getting value from inventory
			SIOrderDataBean sIOrderDataBean = null;
			Integer siParentOrderId = quoteIllSiteToServiceList.get(0).getErfServiceInventoryParentOrderId();
			sIOrderDataBean = macdUtils.getSiOrderData(String.valueOf(siParentOrderId));
			if (Objects.nonNull(sIOrderDataBean)) {
				LOGGER.info("Si Order Data Bean is not null and COPF id is :::: {}", sIOrderDataBean.getTpsCrmCofId());
				updateOpportunityStage.setCopfIdC(sIOrderDataBean.getTpsCrmCofId());
			}
		}
		}
		return updateOpportunityStage;
	}


	@Override
	public void processSiteDetails(QuoteToLe quoteToLe, SiteSolutionOpportunityBean siteSolutionOpportunityBean,
			ProductSolution productSolution) throws TclCommonException {

	}


	  /**
			 * getFeasibilityBean
			 * 
			 * @param quoteIllSiteId
			 * @param feasibilityBean
			 * @return
			 * @throws TclCommonException
			 */
		@Override
		public FeasibilityRequestBean getFeasibilityBean(Integer quoteIllSiteId,
				FeasibilityRequestBean feasibilityBean) throws TclCommonException {
			LOGGER.info("Inside getFeasibilityBean: {} ",feasibilityBean.getSiteId());
			List<QuoteProductComponent> quoteProductComponents= quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndType
					(quoteIllSiteId,ComponentConstants.PORT_CMP.getComponentsValue(),"primary");
			List<QuoteProductComponentsAttributeValue> quotatt = quoteProductComponentsAttributeValueRepository.
				findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponents.get(0).getId(), 
						ComponentConstants.PORT_BANDWIDTH.getComponentsValue());
			List<QuoteProductComponentsAttributeValue> quotatt1 = quoteProductComponentsAttributeValueRepository.
					findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponents.get(0).getId(), 
							AttributeConstants.INTERFACE.toString());
			feasibilityBean.setPortCircuitCapacityC(quotatt.get(0).getAttributeValues() + " Mbps");
			feasibilityBean.setInterfaceC(quotatt1.get(0).getAttributeValues());
			feasibilityBean.setLocalLoopCapacityAEndc(quotatt.get(0).getAttributeValues() + " Mbps");
			List<QuoteProductComponent> quoteProductComponents1 = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndType(
							quoteIllSiteId, AttributeConstants.LAST_MILE.toString(),
							"primary");
			List<QuoteProductComponentsAttributeValue> quoteattribute = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(
							quoteProductComponents1.get(0).getId(),
							ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue());
			String localLoopCapacity = "";
			if(!quoteattribute.isEmpty()) {
				localLoopCapacity = quoteattribute.get(0).getAttributeValues();
			}
//			String[] spliter = localLoopCapacity.split("\\s+");
//			String locLoopCap = spliter[0];
//			String unit = "Mbps";
//			if (spliter.length > 1) {
//				unit = spliter[1];
//			}
//			feasibilityBean.setIllLocalLoopCapacityC(locLoopCap);
//			feasibilityBean.setIllLocalLoopCapacityUnitC(unit);
//			}


			String feasiblityResponseJson = siteFeasibilityRepository.findByQuoteIllSite_IdAndTypeAndFeasibilityMode(quoteIllSiteId,"primary", OmsExcelConstants.ONNET_WIRELINE)
					.stream().findFirst()
					//.min(Comparator.comparing(SiteFeasibility::getRank))
					.map(SiteFeasibility::getResponseJson)
					.orElse(StringUtils.EMPTY);

			if(!StringUtils.isEmpty(feasiblityResponseJson)) {
				NotFeasible nonFeasible = (NotFeasible)Utils.convertJsonToObject(feasiblityResponseJson, NotFeasible.class);
				feasibilityBean.setOtherPOPAEndC(nonFeasible.getPopName() != null ? nonFeasible.getPopName() : SFDCConstants.DEFAULT_POP_NAME);
			} else {
				feasiblityResponseJson = siteFeasibilityRepository.findByQuoteIllSite_IdAndType(quoteIllSiteId,"primary")
						.stream().findFirst()
						//.min(Comparator.comparing(SiteFeasibility::getRank))
						.map(SiteFeasibility::getResponseJson)
						.orElse(StringUtils.EMPTY);


				NotFeasible nonFeasible = (NotFeasible)Utils.convertJsonToObject(feasiblityResponseJson, NotFeasible.class);
				feasibilityBean.setOtherPOPAEndC(nonFeasible.getPopName() != null ? nonFeasible.getPopName() : SFDCConstants.DEFAULT_POP_NAME);

			}

			Optional<QuoteIllSite> quoteIllSite = illSitesRepository.findById(quoteIllSiteId);
			String type = quoteIllSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuoteType();
			String portCapacity = quotatt.get(0).getAttributeValues();
			omsSfdcService.setLocalLoopAndPortCircuitCapacity(type, portCapacity, localLoopCapacity, feasibilityBean);
			feasibilityBean.setSalesRemarks("");
			return feasibilityBean;
		}



	private String getSfdcCodeForCustomerEmailId(String role, QuoteSiteServiceTerminationDetails quoteSiteServiceTerminationDetails) throws TclCommonException {
		Approver customerBean = new Approver();
		String responseSfdcCode = "";
		try {
			if ("CSM".equalsIgnoreCase(role)){
				LOGGER.info("csm loooop");
				customerBean.setEmail(quoteSiteServiceTerminationDetails.getCsmNonCsmEmail());
				customerBean.setName("CSM");
				LOGGER.info("MDC Filter token value in before Queue call getSfdcCodeForCustomerEmailId {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				 responseSfdcCode = (String) mqUtils.sendAndReceive(customerCodeSfdcQueue,
						Utils.convertObjectToJson(customerBean));


			} else if ("CR".equalsIgnoreCase(role)){
				LOGGER.info("communication recipient loooop");
				customerBean.setEmail(quoteSiteServiceTerminationDetails.getCommunicationReceipient());
				customerBean.setName("CR");
				LOGGER.info("MDC Filter token value in before Queue call getSfdcCodeForCustomerEmailId {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				 responseSfdcCode = (String) mqUtils.sendAndReceive(customerCodeSfdcQueue,
						Utils.convertObjectToJson(customerBean));


			}
		} catch (Exception e) {
			LOGGER.error("error in processing sfdc customer code queue{}", e);
		}
		return responseSfdcCode;
	}

}
