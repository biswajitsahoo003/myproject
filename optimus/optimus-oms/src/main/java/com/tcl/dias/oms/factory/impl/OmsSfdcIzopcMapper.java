package com.tcl.dias.oms.factory.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.MDMServiceDetailBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
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
import com.tcl.dias.oms.beans.MDMServiceInventoryBean;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.cancellation.service.v1.CancellationService;
import com.tcl.dias.oms.constants.AttributeConstants;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.entity.entities.MacdDetail;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteSiteDifferentialCommercial;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MacdDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteSiteDifferentialCommercialRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.izopc.pricing.bean.NotFeasible;
import com.tcl.dias.oms.izopc.service.v1.IzoPcQuoteService;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.core.IOmsSfdcInputHandler;
import com.tcl.dias.oms.sfdc.core.OmsAbstractSfdcHandler;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * SFDC mapper class for IZOPC
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class OmsSfdcIzopcMapper extends OmsAbstractSfdcHandler implements IOmsSfdcInputHandler{
	
	@Autowired
	QuoteProductComponentRepository componentRepository;
	
	@Autowired
	IllSiteRepository illSitesRepository;
	
	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;
	
	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;
	
	@Autowired
	SiteFeasibilityRepository siteFeasibilityRepository;
	
	@Autowired
	MQUtils mqUtils;
	
	@Autowired
	OmsSfdcService omsSfdcService;
	
	@Value("${rabbitmq.product.provider.name}")
	String productProviderNameQueue;
	
	@Autowired
	CancellationService cancellationService;
	
	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;
	
	@Autowired
	QuoteSiteDifferentialCommercialRepository quoteSiteDifferentialCommercialRepository;
	
	@Autowired
	IzoPcQuoteService izoPcQuoteService;
	
	@Autowired
	OrderIllSitesRepository orderIllSiteRepository;
	
	@Autowired
	MACDUtils macdUtils;
	
	@Autowired
	MacdDetailRepository macdDetailRepository;
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OmsSfdcIzopcMapper.class);

	
	/**
	 * @return the productProviderNameQueue
	 */
	public String getProductProviderNameQueue() {
		return productProviderNameQueue;
	}

	/**
	 * @param productProviderNameQueue the productProviderNameQueue to set
	 */
	public void setProductProviderNameQueue(String productProviderNameQueue) {
		this.productProviderNameQueue = productProviderNameQueue;
	}

	/**
	 * getProductServiceInput
	 * 
	 * Method to construct product service attributes for SFDC product service input
	 * 
	 * @param quoteToLe
	 * @param productServiceBean
	 * @return
	 * @throws IllegalArgumentException 
	 * @throws TclCommonException 
	 */
	@Override
	public ProductServiceBean getProductServiceInput(QuoteToLe quoteToLe, ProductServiceBean productServiceBean,
			ProductSolution solution) throws TclCommonException, IllegalArgumentException {
		String[] cloudProvider =new String[1];
		Integer refId = solution.getQuoteIllSites().stream().findFirst().get().getId();
		QuoteProductComponent component = componentRepository.findByReferenceIdAndMstProductComponent_NameAndMstProductFamily_NameAndType(refId,CommonConstants.IZOPC_PORT,CommonConstants.IZOPC,"primary");
		if (component != null) {
			component.getQuoteProductComponentsAttributeValues().forEach(attr -> {
				if (attr.getProductAttributeMaster().getName().equalsIgnoreCase("Cloud Provider"))
					cloudProvider[0] = attr.getAttributeValues();
			});
		}
		productServiceBean.setRecordTypeName("Global VPN");
		productServiceBean.setType("Layer 3 Managed MPLS VPN");
		productServiceBean.setProductType(SFDCConstants.GVPN);
		productServiceBean.setProductLineOfBusiness(SFDCConstants.IPVPN);
		productServiceBean.setOfPortsC(String.valueOf(solution.getQuoteIllSites().size()));// no of DCs
		productServiceBean.setCloudEnablementC("Yes");
		productServiceBean.setCloudProvider(getCloudProviderAlias(cloudProvider[0]));
		if (quoteToLe.getTermInMonths() != null) {
			Integer months = 12;
			Double months1 = 0.0;
			String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
			if (termsInMonth.contains("year")) {
				months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;
			}
			else if (termsInMonth.contains(".") && termsInMonth.contains("months")){
				months1 = Double.valueOf(termsInMonth.replace("months", "").trim());
			}
			else if (termsInMonth.contains("months")) {
				months = Integer.valueOf(termsInMonth.replace("months", "").trim());
			} else if (termsInMonth.contains("month")) {
				months = Integer.valueOf(termsInMonth.replace("month", "").trim());
			}

			int compare = Double.compare(months1, 0.0);
			if(compare==0){
				LOGGER.info("Inside 'Integer' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months);
				productServiceBean.setTermInMonthsC(String.valueOf(months));
			}
			else {
				LOGGER.info("Inside 'Double' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months1);
				productServiceBean.setTermInMonthsC(String.valueOf(months1));
			}
			
			if(MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
	        	productServiceBean.setOrderType(SFDCConstants.NEW_ORDER);
				productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
				productServiceBean.setIsCancel(true);
	        }
			
			if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
				if (MACDConstants.CHANGE_BANDWIDTH_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
					productServiceBean.setType(SFDCConstants.LAYER3MANAGEDMPLSVPN);
					productServiceBean.setOrderType(SFDCConstants.HOTUPGRADE);
					productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
				} else if (MACDConstants.OTHERS.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
					productServiceBean.setType(SFDCConstants.LAYER3MANAGEDMPLSVPN);
					productServiceBean.setOrderType(SFDCConstants.OTHERS_SFDC);
					productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
				}
			}
			
			LOGGER.info("Final set value for term in months in Update Opty block is ----> {} " , productServiceBean.getTermInMonthsC());

		}
		LOGGER.info("Input  for product Service IZOPC {}", productServiceBean);


		return productServiceBean;
	}

	private String getCloudProviderAlias(String cloudProviderName) throws TclCommonException, IllegalArgumentException {
		
		String providerAlias = (String) mqUtils.sendAndReceive(productProviderNameQueue,
					Utils.convertObjectToJson(cloudProviderName));
		return providerAlias;
			
	}

	@Override
	public OpportunityBean getOpportunityBean(QuoteToLe quoteToLe, OpportunityBean opportunityBean)
			throws TclCommonException {
		opportunityBean.setSelectProductType(SFDCConstants.GVPN);
		if (quoteToLe.getTermInMonths()!=null) {
			Integer months = 12;
			Double months1 = 0.0;
			String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
			if (termsInMonth.contains("year")) {
				months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;
			}else if (termsInMonth.contains(".") && termsInMonth.contains("months")){
				months1 = Double.valueOf(termsInMonth.replace("months", "").trim());
			} else if (termsInMonth.contains("months")) {
				months = Integer.valueOf(termsInMonth.replace("months", "").trim());
			} else if (termsInMonth.contains("month")) {
				months = Integer.valueOf(termsInMonth.replace("month", "").trim());
			}

			int compare = Double.compare(months1, 0.0);
			if(compare==0){
				LOGGER.info("Inside 'Integer' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months);
				opportunityBean.setTermOfMonths(String.valueOf(months));
			}
			else {
				LOGGER.info("Inside 'Double' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months1);
				opportunityBean.setTermOfMonths(String.valueOf(months1));
			}

			LOGGER.info("Final set value for term in months in Update Opty block is ----> {} " , opportunityBean.getTermOfMonths());

		}
		
		if(MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
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
		}
	if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			
			opportunityBean.setiLLAutoCreation(SFDCConstants.GLOBALVPN);
			List<SIServiceDetailsBean> serviceDetailsList = macdUtils.getServiceDetailsBeanList(quoteToLe, opportunityBean);
			LOGGER.info("Service Details List : {}", serviceDetailsList);
		if (MACDConstants.CHANGE_BANDWIDTH_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				opportunityBean.setType(SFDCConstants.PARALLELUPGRADE);
				opportunityBean.setSubType(SFDCConstants.PARALLELUPGRADE);
					
				opportunityBean.setParentOpportunityName(quoteToLe.getTpsSfdcParentOptyId().toString());

				if (serviceDetailsList != null && !serviceDetailsList.isEmpty()) {
					setPreviousMrcNrc(opportunityBean, serviceDetailsList);
				} else {
					opportunityBean.setPreviousMRC(StringUtils.EMPTY);
					opportunityBean.setPreviousNRC(StringUtils.EMPTY);
				}

			}else if (MACDConstants.OTHERS.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				opportunityBean.setType(SFDCConstants.OTHERS_SFDC);
				opportunityBean.setSubType("");
			}


		} 
		return opportunityBean;
	}
	
	private void setPreviousMrcNrc(OpportunityBean opportunityBean, List<SIServiceDetailsBean> serviceDetailsList) {
		Double mrc = serviceDetailsList.stream().mapToDouble(serviceDetail -> new Double(serviceDetail.getMrc())).sum();
		Double nrc = serviceDetailsList.stream().mapToDouble(serviceDetail -> new Double(serviceDetail.getNrc())).sum();
		LOGGER.info("Mrc calculated : {} , Nrc calculated : {}", mrc,nrc);
			opportunityBean.setPreviousMRC(mrc == null ? StringUtils.EMPTY
					: String.valueOf(mrc));
			opportunityBean.setPreviousNRC(nrc == null ? StringUtils.EMPTY
					: String.valueOf(nrc));
	}



	@Override
	public ProductServiceBean updateProductServiceInput(QuoteToLe quoteToLe, ProductServiceBean productServiceBean,
			ProductSolution solution) throws TclCommonException {

		productServiceBean.setRecordTypeName("Global VPN");
		productServiceBean.setProductLineOfBusiness("IPVPN");
		productServiceBean.setCloudEnablementC("No");

		if (quoteToLe.getTermInMonths() != null) {
			Integer months = 12;
			Double months1 = 0.0;
			String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
			if (termsInMonth.contains("year")) {
				months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;
			}
			else if (termsInMonth.contains(".") && termsInMonth.contains("months")){
				months1 = Double.valueOf(termsInMonth.replace("months", "").trim());
			}
			else if (termsInMonth.contains("months")) {
				months = Integer.valueOf(termsInMonth.replace("months", "").trim());
			} else if (termsInMonth.contains("month")) {
				months = Integer.valueOf(termsInMonth.replace("month", "").trim());
			}

			int compare = Double.compare(months1, 0.0);
			if(compare==0){
				LOGGER.info("Inside 'Integer' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months);
				productServiceBean.setTermInMonthsC(String.valueOf(months));
			}
			else {
				LOGGER.info("Inside 'Double' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months1);
				productServiceBean.setTermInMonthsC(String.valueOf(months1));
			}

			LOGGER.info("Final set value for term in months in Update Opty block is ----> {} " , productServiceBean.getTermInMonthsC());

		}
		LOGGER.info("Input  for product Service Izopc {}", productServiceBean);
		
		
		productServiceBean.setProductType(SFDCConstants.GVPN.toString());
		productServiceBean.setOfPortsC(String.valueOf(solution.getQuoteIllSites().size()));
		productServiceBean.setCloudEnablementC("Yes");
		
		

		if (quoteToLe.getFinalArc() != null) {
			productServiceBean.setProductARC(quoteToLe.getFinalArc());
			productServiceBean.setBigMachinesArc(quoteToLe.getFinalArc());
			productServiceBean.setProductMRC(quoteToLe.getFinalArc() / 12);
			productServiceBean.setBigMachinesMrc(quoteToLe.getFinalArc() / 12);
		}
		/*
		 * if (quoteToLe.getFinalMrc()!=null) {
		 * productServiceBean.setProductMRC(quoteToLe.getFinalMrc());
		 * productServiceBean.setBigMachinesMrc(quoteToLe.getFinalMrc()); }
		 */
		if (quoteToLe.getFinalNrc() != null) {
			productServiceBean.setProductNRC(quoteToLe.getFinalNrc());
			productServiceBean.setBigMachinesNrc(quoteToLe.getFinalNrc());
		}
		if (quoteToLe.getTotalTcv() != null)
			productServiceBean.setBigMachinesTcv(quoteToLe.getTotalTcv());
	
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
				updateRequest.setSiteId(illSiteOpt.get().getId());
				updateRequest.setQuoteToLe(quoteToLe.getId());
				opportunityBean = omsSfdcService.updateOrderTypeAndSubtype(updateRequest);
				productServiceBean.setOrderType(opportunityBean.getType());

			}

		} 
		if (MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			LOGGER.info("Inside Cancellation if condition updateProductServiceInput");
			OpportunityBean opportunityBean = new OpportunityBean();
			productServiceBean.setProductType(SFDCConstants.GVPN.toString());
			Quote quote = quoteToLe.getQuote();
			if (Objects.nonNull(quote)) {
				Optional<QuoteIllSite> illSiteOpt = quote.getQuoteToLes().stream().findFirst()
						.map(QuoteToLe::getQuoteToLeProductFamilies).get().stream()
						.map(QuoteToLeProductFamily::getProductSolutions).findFirst().get().stream()
						.map(ProductSolution::getQuoteIllSites).findFirst().get().stream().findFirst();
				List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository
						.findByQuoteToLe_Id(quoteToLe.getId());
				if (quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
					// Differential mrc/nrc 
					Double[] diffMrc = {0D};
					Double[] diffNrc = {0D};
					for(QuoteIllSiteToService quoteIllSiteToService : quoteIllSiteToServiceList) {
						if(quoteIllSiteToService.getQuoteIllSite().getProductSolution().getId().equals(solution.getId())) {
					MDMServiceInventoryBean serviceDetail = cancellationService.getServiceDetailsForOrderCancellation(1, 10, null, null, null, null,
							quoteIllSiteToService.getErfServiceInventoryTpsServiceId(), null, null);
					if(serviceDetail != null && serviceDetail.getServiceDetailBeans() != null && !serviceDetail.getServiceDetailBeans().isEmpty()) {
						LOGGER.info("service id {}, mrc {}, nrc {}, site code {}, order code {}", serviceDetail.getServiceDetailBeans().get(0).getServiceId(), 
								serviceDetail.getServiceDetailBeans().get(0).getMrc(), serviceDetail.getServiceDetailBeans().get(0).getNrc(),
								serviceDetail.getServiceDetailBeans().get(0).getSiteCode(),serviceDetail.getServiceDetailBeans().get(0).getOrderCode());
						
						List<QuoteSiteDifferentialCommercial> quoteSiteDifferentialCommercialList = null;
						MDMServiceDetailBean optimusMdmDetailBean = null;
						if(serviceDetail.getServiceDetailBeans() != null && serviceDetail.getServiceDetailBeans().size() > 1) {
							LOGGER.info("multiple records from cancellation view for this service id and source system {} ", quoteToLe.getSourceSystem());
							Optional<MDMServiceDetailBean> optimusMdmDetailBeanOpt = null;
							if(quoteToLe.getSourceSystem().equalsIgnoreCase(SFDCConstants.OPTIMUS)) {
								optimusMdmDetailBeanOpt = serviceDetail.getServiceDetailBeans().stream().filter(si->si.getSourceSystem().equalsIgnoreCase("OPTIMUS_O2C")).findFirst();
							}
							if(quoteToLe.getSourceSystem().equalsIgnoreCase(MACDConstants.LEGACY_SOURCE_SYSTEM)) {
								optimusMdmDetailBeanOpt = serviceDetail.getServiceDetailBeans().stream().filter(si->si.getSourceSystem().equalsIgnoreCase("POS_GENEVA")).findFirst();
							}
							if(optimusMdmDetailBeanOpt.isPresent()) {
								optimusMdmDetailBean = optimusMdmDetailBeanOpt.get();
							}
						} else {
							optimusMdmDetailBean = serviceDetail.getServiceDetailBeans().get(0);
						}
						if(optimusMdmDetailBean != null) {
						if(optimusMdmDetailBean.getSiteCode() != null && optimusMdmDetailBean.getOrderCode() != null 
								&& optimusMdmDetailBean.getSourceSystem().equalsIgnoreCase("OPTIMUS_O2C")) {
							LOGGER.info("site code order code is not null");
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
								quoteSiteDifferentialCommercialList = izoPcQuoteService.persistQuoteSiteCommercialsAtServiceIdLevel(quoteIllSitesList.get(0));
								quoteSiteDifferentialCommercialList.stream().forEach(differentialCommercial -> {
									diffMrc[0] += differentialCommercial.getDifferentialMrc();
									diffNrc[0] += differentialCommercial.getDifferentialNrc();
								});
								}								
							}
							} else {
								LOGGER.info("IZOPC POS differntial calculation  site code or order code  null");
//								productServiceBean.setProductMRC((optimusMdmDetailBean.getMrc() != null) ? Double.valueOf(0-optimusMdmDetailBean.getMrc()) : 0);
//								productServiceBean.setProductNRC((optimusMdmDetailBean.getNrc() != null) ? Double.valueOf(0-optimusMdmDetailBean.getNrc()) : 0);
								SIServiceDetailDataBean posServiceDetails;
								try {
									//if macd differ mrc = cancellationViewPosmrc - inventorymrc
									LOGGER.info("POS IZOPC Fetching  service details for {} ",optimusMdmDetailBean.getServiceId());
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


	@Override
	public SiteSolutionOpportunityBean getSiteBeanInput(QuoteToLe quoteToLe,
			SiteSolutionOpportunityBean solutionOpportunityBean) throws TclCommonException {
		// TODO Auto-generated method stub
		return solutionOpportunityBean;
	}


	@Override
	public UpdateOpportunityStage getOpportunityUpdate(QuoteToLe quoteToLe,
			UpdateOpportunityStage updateOpportunityStage) throws TclCommonException {
		Calendar cal = Calendar.getInstance();
		updateOpportunityStage.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
		updateOpportunityStage.setCurrencyIsoCode(null != quoteToLe.getCurrencyCode() ? quoteToLe.getCurrencyCode() : SFDCConstants.INR);
		return updateOpportunityStage;
	}


	@Override
	public void processSiteDetails(QuoteToLe quoteToLe, SiteSolutionOpportunityBean siteSolutionOpportunityBean,
			ProductSolution productSolution) throws TclCommonException {
		// TODO Auto-generated method stub
		
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

			List<QuoteProductComponent> quoteProductComponents= quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndType
					(quoteIllSiteId,CommonConstants.IZOPC_PORT,"primary");
			List<QuoteProductComponentsAttributeValue> quotatt = quoteProductComponentsAttributeValueRepository.
				findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponents.get(0).getId(), 
						"Bandwidth");
			feasibilityBean.setPortCircuitCapacityC(quotatt.get(0).getAttributeValues() + " Mbps");
			List<QuoteProductComponentsAttributeValue> quotatt1 = quoteProductComponentsAttributeValueRepository.
					findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponents.get(0).getId(), 
							AttributeConstants.INTERFACE.toString());
			if(quotatt1 != null && !quotatt1.isEmpty())
				feasibilityBean.setInterfaceC(StringUtils.isEmpty(quotatt1.get(0).getAttributeValues())?"Fast Ethernet":quotatt1.get(0).getAttributeValues());
			else
				feasibilityBean.setInterfaceC("Fast Ethernet");
			List<QuoteProductComponent> quoteProductComponents1 = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndType(
							quoteIllSiteId, CommonConstants.IZOPC_PORT,
							"primary");
			List<QuoteProductComponentsAttributeValue> quoteattribute = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(
							quoteProductComponents1.get(0).getId(),
							ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue());
			if(!quoteattribute.isEmpty()) {
			String localLoopCapacity = quoteattribute.get(0)
					.getAttributeValues();

			String[] spliter = localLoopCapacity.split("\\s+");
			String locLoopCap = spliter[0];
			String unit = "Mbps";
			if (spliter.length > 1) {
			unit  = spliter[1];
			}
			feasibilityBean.setIllLocalLoopCapacityC(locLoopCap);
			feasibilityBean.setIllLocalLoopCapacityUnitC(unit);
			}
			
			String feasiblityResponseJson = siteFeasibilityRepository.findByQuoteIllSite_IdAndType(quoteIllSiteId,"primary")
					.stream().findFirst()
					//.min(Comparator.comparing(SiteFeasibility::getRank))
					.map(SiteFeasibility::getResponseJson)
					.orElse(StringUtils.EMPTY);
			NotFeasible nonFeasible = (NotFeasible)Utils.convertJsonToObject(feasiblityResponseJson, NotFeasible.class);
//			System.out.println("RESULT!!!");
//			System.out.println(nonFeasible.getPopName());
			if (nonFeasible.getPopName()!= null) {
			feasibilityBean.setOtherPOPAEndC(nonFeasible.getPopName());
			} else
				feasibilityBean.setOtherPOPAEndC(SFDCConstants.DEFAULT_POP_NAME);

			
			return feasibilityBean;
		}

}
