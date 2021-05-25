package com.tcl.dias.oms.factory.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.tcl.dias.common.beans.MDMServiceDetailBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IpcCommonConstants;
import com.tcl.dias.common.sfdc.bean.FeasibilityRequestBean;
import com.tcl.dias.common.sfdc.bean.OpportunityBean;
import com.tcl.dias.common.sfdc.bean.ProductServiceBean;
import com.tcl.dias.common.sfdc.bean.SiteSolutionOpportunityBean;
import com.tcl.dias.common.sfdc.bean.UpdateOpportunityStage;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.MDMServiceInventoryBean;
import com.tcl.dias.oms.cancellation.service.v1.CancellationService;
import com.tcl.dias.oms.constants.AttributeConstants;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteCloud;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteCloudRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.ipc.service.v1.IPCOrderService;
import com.tcl.dias.oms.izopc.pricing.bean.NotFeasible;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.core.IOmsSfdcInputHandler;
import com.tcl.dias.oms.sfdc.core.OmsAbstractSfdcHandler;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component
public class OmsSfdcIPCMapper extends OmsAbstractSfdcHandler implements IOmsSfdcInputHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(OmsSfdcIPCMapper.class);

	@Value("${rabbitmq.product.provider.name}")
	private String productProviderNameQueue;
	
	@Value("${application.env}")
	private String appEnv;

	@Autowired
	private MQUtils mqUtils;

	@Autowired
	private ProductSolutionRepository productSolutionRepository;
	
	@Autowired
	private QuoteCloudRepository quoteCloudRepository;

	@Autowired
	private QuoteProductComponentRepository componentRepository;

	@Autowired
	private QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	private QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	private SiteFeasibilityRepository siteFeasibilityRepository;


	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;
	
	@Autowired
	CancellationService cancellationService;
	
	@Autowired
	IPCOrderService ipcOrderService;



	/**
	 * @return the productProviderNameQueue
	 */
	public String getProductProviderNameQueue() {
		return productProviderNameQueue;
	}

	/**
	 * @param productProviderNameQueue
	 *            the productProviderNameQueue to set
	 */
	public void setProductProviderNameQueue(String productProviderNameQueue) {
		this.productProviderNameQueue = productProviderNameQueue;
	}
	
	
	@Override
	public ProductServiceBean getProductServiceInput(QuoteToLe quoteToLe, ProductServiceBean productServiceBean,
			ProductSolution solution) throws TclCommonException {

		/*List<QuoteCloud>  quoteClouds = quoteCloudRepository.findByProductSolution(solution);
		String[] cloudProvider = new String[1];
		Integer refId = quoteClouds.stream().findFirst().get().getId();
		QuoteProductComponent component = componentRepository.findByReferenceIdAndMstProductComponent_NameAndMstProductFamily_Name(refId, "Flavor", CommonConstants.IPC);
		if (component != null) {
			component.getQuoteProductComponentsAttributeValues().forEach(attr -> {
				if (attr.getProductAttributeMaster().getName().equalsIgnoreCase("Cloud Provider"))
					cloudProvider[0] = attr.getAttributeValues();
			});
		}*/
		
		productServiceBean.setRecordTypeName(SFDCConstants.IZOPRIVATECLOUD);
		productServiceBean.setProductType(SFDCConstants.IZOPRIVATECLOUD);
		productServiceBean.setProductLineOfBusiness(SFDCConstants.IZOPRIVATECLOUD);
		if(solution.getQuoteIllSites()!=null) {
			productServiceBean.setOfPortsC(String.valueOf(solution.getQuoteIllSites().size()));
		}else{
			productServiceBean.setOfPortsC("0");// no
		}
																							// of
																							// DCs
		productServiceBean.setCloudEnablementC("Yes");
		productServiceBean.setCloudProvider("");
		//productServiceBean.setCloudProvider(getCloudProviderAlias(cloudProvider[0]));
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
		
		if(MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
        	productServiceBean.setOrderType(SFDCConstants.NEW_ORDER);
			productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
			productServiceBean.setIsCancel(true);
        }
		LOGGER.info("Input  for product Service IPC {}", productServiceBean);
		return productServiceBean;
	}

	private String getCloudProviderAlias(String cloudProviderName) throws TclCommonException {
		return (String) mqUtils.sendAndReceive(productProviderNameQueue,Utils.convertObjectToJson(cloudProviderName));
	}

	@Override
	public OpportunityBean getOpportunityBean(QuoteToLe quoteToLe, OpportunityBean opportunityBean)
			throws TclCommonException {
		opportunityBean.setSelectProductType(SFDCConstants.IZOPRIVATECLOUD);

		if (quoteToLe.getTermInMonths() != null) {
			Integer months = 12;
			String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
			if (termsInMonth.contains("year")) {
				months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;
			} else if (termsInMonth.contains("months")) {
				months = Integer.valueOf(termsInMonth.replace("months", "").trim());
			} else if (termsInMonth.contains("month")) {
				months = Integer.valueOf(termsInMonth.replace("month", "").trim());
			}
			opportunityBean.setTermOfMonths(String.valueOf(months));
		}
		
		if(CommonConstants.MIGRATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			opportunityBean.setExcludeFromObReporting(Boolean.TRUE);;
			opportunityBean.setReasonForExcludeFromNetAcv(SFDCConstants.MIGRATION_TO_OPTIMUS);
			
			// Hard-coded for IPC Migration Orders only. To be removed once Migration is completed in Prod.
			if (SFDCConstants.PROD.equals(appEnv)) {
				opportunityBean.setOwnerName("maneet.bhatia@tatacommunications.com"); 
			}
		}
	
		if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			if (MACDConstants.ADD_CLOUDVM_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				opportunityBean.setType(SFDCConstants.ADD_OR_UPGRADE_COMPONENT);
				opportunityBean.setSubType(SFDCConstants.UPGRADE_LICENSE);
			} else if (MACDConstants.UPGRADE_VM_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				opportunityBean.setType(SFDCConstants.ADD_OR_UPGRADE_COMPONENT);
				opportunityBean.setSubType(SFDCConstants.UPGRADE_LICENSE);
			} else if (MACDConstants.CONNECTIVITY_UPGRADE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory()) || 
						MACDConstants.ADDITIONAL_SERVICE_UPGRADE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				opportunityBean.setType(SFDCConstants.ADD_OR_UPGRADE_COMPONENT);
				opportunityBean.setSubType(SFDCConstants.UPGRADE_LICENSE);
			} else if (MACDConstants.DELETE_VM.equalsIgnoreCase(quoteToLe.getQuoteCategory()) || 
					MACDConstants.REQUEST_FOR_TERMINATION.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				opportunityBean.setType(SFDCConstants.TERMINATION);
				opportunityBean.setSubType(SFDCConstants.ONCUSTOMERDEMAND);
			}
		}
		
		if(MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			LOGGER.info("Inside Cancellation if condition");
			opportunityBean.setType(SFDCConstants.CANCELLATION_ORDER);
			opportunityBean.setSubType(SFDCConstants.CANCELLATION_ORDER);
			opportunityBean.setPreviousMRC(StringUtils.EMPTY);
			opportunityBean.setPreviousNRC(StringUtils.EMPTY);
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = dateFormatter.format(new Date());
			opportunityBean.setEffectiveDateOfChange(formattedDate);
		}
		
		return opportunityBean;
	}

	@Override
	public ProductServiceBean updateProductServiceInput(QuoteToLe quoteToLe, ProductServiceBean productServiceBean,
			ProductSolution solution) throws TclCommonException {

		productServiceBean.setRecordTypeName(SFDCConstants.IZOPRIVATECLOUD);
		productServiceBean.setProductLineOfBusiness(SFDCConstants.IZOPRIVATECLOUD);
		productServiceBean.setCloudEnablementC("No");

		if (quoteToLe.getTermInMonths() != null) {
			Integer months = 12;
			Double months1 = 0.0;
			String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
			if (termsInMonth.contains("year")) {
				months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;
			} else if (termsInMonth.contains(".") && termsInMonth.contains("months")){
				months1 = Double.valueOf(termsInMonth.replace("months", "").trim());
			} else if (termsInMonth.contains("months")) {
				months = Integer.valueOf(termsInMonth.replace("months", "").trim());
			} else if (termsInMonth.contains("month")) {
				months = Integer.valueOf(termsInMonth.replace("month", "").trim());
			}

			int compare = Double.compare(months1, 0.0);
			if(compare == 0) {
				productServiceBean.setTermInMonthsC(String.valueOf(months));
			} else {
				productServiceBean.setTermInMonthsC(String.valueOf(months1));
			}
		}

		productServiceBean.setProductType(SFDCConstants.IZOPRIVATECLOUD);
		productServiceBean.setOfPortsC("0");
		productServiceBean.setCloudEnablementC("Yes");

		if (quoteToLe.getFinalArc() != null) {
			productServiceBean.setProductARC(quoteToLe.getFinalArc());
			productServiceBean.setBigMachinesArc(quoteToLe.getFinalArc());
		}

		if (quoteToLe.getFinalMrc() != null) {
			productServiceBean.setProductMRC(quoteToLe.getFinalMrc());
			productServiceBean.setBigMachinesMrc(quoteToLe.getFinalMrc());
		}

		if (quoteToLe.getFinalNrc() != null) {
			productServiceBean.setProductNRC(quoteToLe.getFinalNrc());
			productServiceBean.setBigMachinesNrc(quoteToLe.getFinalNrc());
		}

		if (quoteToLe.getTotalTcv() != null) {
			productServiceBean.setBigMachinesTcv(quoteToLe.getTotalTcv());
		}
		
		if (MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			LOGGER.info("Inside Cancellation if condition updateProductServiceInput");
			OpportunityBean opportunityBean = new OpportunityBean();
			Quote quote = quoteToLe.getQuote();
			if (Objects.nonNull(quote)) {
				Optional<QuoteIllSite> illSiteOpt = quote.getQuoteToLes().stream().findFirst()
						.map(QuoteToLe::getQuoteToLeProductFamilies).get().stream()
						.map(QuoteToLeProductFamily::getProductSolutions).findFirst().get().stream()
						.map(ProductSolution::getQuoteIllSites).findFirst().get().stream().findFirst();
				List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository
						.findByQuoteIllSite(illSiteOpt.get());
				if (quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
					MDMServiceInventoryBean serviceDetail = cancellationService.getServiceDetailsForOrderCancellation(1, 10, null, null, null, null,
							quoteIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId(), null, null);
					if(serviceDetail != null && !serviceDetail.getServiceDetailBeans().isEmpty()) {
					MDMServiceDetailBean optimusMdmDetailBean = null;
					Optional<MDMServiceDetailBean> optimusMdmDetailBeanOpt = serviceDetail.getServiceDetailBeans().stream().filter(si->si.getSourceSystem().equalsIgnoreCase("OPTIMUS_O2C")).findFirst();
					if(optimusMdmDetailBeanOpt.isPresent())
						optimusMdmDetailBean = optimusMdmDetailBeanOpt.get();
					else
						optimusMdmDetailBean = serviceDetail.getServiceDetailBeans().get(0);
					if(optimusMdmDetailBean != null) {
						LOGGER.info("service id {}, mrc {}, nrc {}", optimusMdmDetailBean.getServiceId(), optimusMdmDetailBean.getMrc(), optimusMdmDetailBean.getNrc());
						
						productServiceBean.setProductMRC((optimusMdmDetailBean.getMrc() != null) ? Double.valueOf(0-optimusMdmDetailBean.getMrc()) : 0);
						productServiceBean.setProductNRC((optimusMdmDetailBean.getNrc() != null) ? Double.valueOf(0-optimusMdmDetailBean.getNrc()) : 0);
						
						
						
					}
				}
				}
			}
		}

		return productServiceBean;
	}

	@Override
	public SiteSolutionOpportunityBean getSiteBeanInput(QuoteToLe quoteToLe,
			SiteSolutionOpportunityBean solutionOpportunityBean) throws TclCommonException {
		// TODO Auto-generated method stub //NOSONAR
		return solutionOpportunityBean;
	}

	@Override
	public UpdateOpportunityStage getOpportunityUpdate(QuoteToLe quoteToLe,
			UpdateOpportunityStage updateOpportunityStage) throws TclCommonException {
		Calendar cal = Calendar.getInstance();
		updateOpportunityStage.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
		updateOpportunityStage.setCurrencyIsoCode(
				null != quoteToLe.getCurrencyCode() ? quoteToLe.getCurrencyCode() : SFDCConstants.INR);
		if (SFDCConstants.VERBAL_AGREEMENT_STAGE.equalsIgnoreCase(updateOpportunityStage.getStageName())) {
			updateOpportunityStage.setRfsInDaysMhsMssC(calculateRfsInDays(quoteToLe));
			LOGGER.info("RFS Days :: {}", updateOpportunityStage.getRfsInDaysMhsMssC());
		}
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
	public FeasibilityRequestBean getFeasibilityBean(Integer quoteIllSiteId, FeasibilityRequestBean feasibilityBean)
			throws TclCommonException {

		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndType(quoteIllSiteId, CommonConstants.IZOPC_PORT,
						"primary");
		List<QuoteProductComponentsAttributeValue> quotatt = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponents.get(0).getId(),
						"Bandwidth");
		feasibilityBean.setPortCircuitCapacityC(quotatt.get(0).getAttributeValues() + " Mbps");
		List<QuoteProductComponentsAttributeValue> quotatt1 = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponents.get(0).getId(),
						AttributeConstants.INTERFACE.toString());
		if (quotatt1 != null && !quotatt1.isEmpty())
			feasibilityBean.setInterfaceC(StringUtils.isEmpty(quotatt1.get(0).getAttributeValues()) ? "Fast Ethernet"
					: quotatt1.get(0).getAttributeValues());
		else
			feasibilityBean.setInterfaceC("Fast Ethernet");
		List<QuoteProductComponent> quoteProductComponents1 = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndType(quoteIllSiteId, CommonConstants.IZOPC_PORT,
						"primary");
		List<QuoteProductComponentsAttributeValue> quoteattribute = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponents1.get(0).getId(),
						ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue());
		if (!quoteattribute.isEmpty()) {
			String localLoopCapacity = quoteattribute.get(0).getAttributeValues();

			String[] spliter = localLoopCapacity.split("\\s+");
			String locLoopCap = spliter[0];
			String unit = "Mbps";
			if (spliter.length > 1) {
				unit = spliter[1];
			}
			feasibilityBean.setIllLocalLoopCapacityC(locLoopCap);
			feasibilityBean.setIllLocalLoopCapacityUnitC(unit);
		}

		String feasiblityResponseJson = siteFeasibilityRepository
				.findByQuoteIllSite_IdAndType(quoteIllSiteId, "primary").stream().findFirst()
				// .min(Comparator.comparing(SiteFeasibility::getRank))
				.map(SiteFeasibility::getResponseJson).orElse(StringUtils.EMPTY);
		NotFeasible nonFeasible = (NotFeasible) Utils.convertJsonToObject(feasiblityResponseJson, NotFeasible.class);
		// System.out.println("RESULT!!!");
		// System.out.println(nonFeasible.getPopName());
		if (nonFeasible.getPopName() != null) {
			feasibilityBean.setOtherPOPAEndC(nonFeasible.getPopName());
		} else
			feasibilityBean.setOtherPOPAEndC(SFDCConstants.DEFAULT_POP_NAME);

		return feasibilityBean;
	}

	private Integer calculateRfsInDays(QuoteToLe quoteToLe) {
		LOGGER.info("Calculating Rfs in Days..");
		Set<String> components = new HashSet<>();
		List<QuoteCloud> quoteClouds = quoteCloudRepository.findByQuoteToLeIdAndStatus(quoteToLe.getId(),
				CommonConstants.BACTIVE);
		if (null != quoteClouds && !CollectionUtils.isEmpty(quoteClouds)) {
			for (QuoteCloud quoteCloud : quoteClouds) {
				List<QuoteProductComponent> productComponents = quoteProductComponentRepository.findByReferenceId(quoteCloud.getId());
				if (null != productComponents && !CollectionUtils.isEmpty(productComponents)) {
					for (QuoteProductComponent productComponent : productComponents) {
						if (IpcCommonConstants.VPN_CONNECTIONS.equalsIgnoreCase(productComponent.getMstProductComponent().getName())) {
							Set<QuoteProductComponentsAttributeValue> attributeValues = productComponent.getQuoteProductComponentsAttributeValues();
							if (null != attributeValues && !CollectionUtils.isEmpty(attributeValues)) {
								for (QuoteProductComponentsAttributeValue attributeValue : attributeValues) {
									if (IpcCommonConstants.SITE_TO_SITE.equalsIgnoreCase(attributeValue.getProductAttributeMaster().getName())) {
										components.add(attributeValue.getProductAttributeMaster().getName());
									}
								}
							}
						} else if (productComponent.getMstProductComponent().getName().startsWith(IpcCommonConstants.MYSQL)) {
							components.add(IpcCommonConstants.MYSQL);
						} else if (productComponent.getMstProductComponent().getName().startsWith(IpcCommonConstants.MSSQL_SERVER)) {
							components.add(IpcCommonConstants.MSSQL_SERVER);
						} else if (productComponent.getMstProductComponent().getName().startsWith(IpcCommonConstants.POSTGRESQL)) {
							components.add(IpcCommonConstants.POSTGRESQL);
						} else if (productComponent.getMstProductComponent().getName().startsWith(IpcCommonConstants.ZERTO)) {
							components.add(IpcCommonConstants.ZERTO);
						} else if (productComponent.getMstProductComponent().getName().startsWith(IpcCommonConstants.DOUBLE_TAKE)) {
							components.add(IpcCommonConstants.DOUBLE_TAKE);
						} else {
							components.add(productComponent.getMstProductComponent().getName());
						}
					}
				}
			}
		}
		return ipcOrderService.calculateDays(components);
	}
}
