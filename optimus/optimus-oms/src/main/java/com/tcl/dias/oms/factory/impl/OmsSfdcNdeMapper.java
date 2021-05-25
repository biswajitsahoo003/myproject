package com.tcl.dias.oms.factory.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tcl.dias.common.beans.Approver;
import com.tcl.dias.common.beans.CustomerContactDetails;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.oms.entity.entities.QuoteSiteServiceTerminationDetails;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.QuoteSiteServiceTerminationDetailsRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.core.IOmsSfdcInputHandler;
import com.tcl.dias.oms.sfdc.core.OmsAbstractSfdcHandler;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
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
import com.tcl.dias.common.sfdc.bean.SiteOpportunityLocation;
import com.tcl.dias.common.sfdc.bean.SiteSolutionOpportunityBean;
import com.tcl.dias.common.sfdc.bean.UpdateOpportunityStage;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.MDMServiceInventoryBean;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.cancellation.service.v1.CancellationService;
import com.tcl.dias.oms.constants.AttributeConstants;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.crossconnect.pricing.bean.CrossconnectConstants;
import com.tcl.dias.oms.entity.entities.LinkFeasibility;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteSiteDifferentialCommercial;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.LinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.NplLinkRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteSiteDifferentialCommercialRepository;
import com.tcl.dias.oms.macd.utils.MACDUtils;
//import com.tcl.dias.oms.mapper.IOmsSfdcInputHandler;
//import com.tcl.dias.oms.mapper.OmsAbstractSfdcHandler;
import com.tcl.dias.oms.npl.pricing.bean.NotFeasible;
import com.tcl.dias.oms.npl.service.v1.NplQuoteService;
//import com.tcl.dias.oms.service.v1.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
/**
 * This file contains the OmsSfdcNdeMapper.java class.
 * 
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class OmsSfdcNdeMapper extends OmsAbstractSfdcHandler implements IOmsSfdcInputHandler {


	private static final Logger LOGGER = LoggerFactory.getLogger(OmsSfdcNdeMapper.class);

	@Autowired
	NplLinkRepository nplLinkRepository;

	@Autowired
	MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	IllSiteRepository illSiteRepository;

	@Autowired
	LinkFeasibilityRepository linkFeasibilityRepository;
	
	@Autowired
	MACDUtils macdUtils;

	@Value("${rabbitmq.location.detail}")
	String locationQueue;
	
	@Autowired
	MQUtils mqUtils;
	
	@Autowired
	OmsSfdcService omsSfdcService;
	
	@Autowired
	NplLinkRepository quoteNplLinkRepository;
	
	@Autowired
	CancellationService cancellationService;
	
	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;

	@Autowired
	QuoteSiteDifferentialCommercialRepository quoteSiteDifferentialCommercialRepository;
	
	@Autowired
	NplQuoteService nplQuoteService;
	
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	QuoteSiteServiceTerminationDetailsRepository quoteSiteServiceTerminationDetailsRepository;

	@Value("${rabbitmq.customer.code.sfdc.queue}")
	protected String customerCodeSfdcQueue;
	
	@Value("${rabbitmq.si.order.details.queue}")
	String siOrderDetailsQueue;
	
	@Value("${rabbitmq.customer.account.manager.email}")
	String customerAccountManagerName;

	@Autowired
	UserRepository userRepository;

	@Value("${rabbitmq.owner.email.business.segment.queue}")
	String ownerNameBasedOnBusinessSegment;

	
	/**
	 * getOpportunityBean
	 *
	 * @param quoteToLe
	 * @param opportunityBean
	 * @return
	 * @throws TclCommonException
	 */
	@Override
	public OpportunityBean getOpportunityBean(QuoteToLe quoteToLe, OpportunityBean opportunityBean)
			throws TclCommonException {
		opportunityBean.setSelectProductType(SFDCConstants.NDE.toString());
		
		if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			List<SIServiceDetailsBean> serviceDetailsList = macdUtils.getServiceDetailsBeanList(quoteToLe, opportunityBean);
			 if (MACDConstants.CHANGE_BANDWIDTH_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
					opportunityBean.setType(SFDCConstants.PARALLELUPGRADE);
					opportunityBean.setSubType(SFDCConstants.PARALLELUPGRADE);
					opportunityBean.setParentOpportunityName(quoteToLe.getTpsSfdcParentOptyId().toString());
					if (serviceDetailsList != null && !serviceDetailsList.isEmpty()) {
						setPreviousMrcNrc(opportunityBean, serviceDetailsList);
					}  else {
						opportunityBean.setPreviousMRC(StringUtils.EMPTY);
						opportunityBean.setPreviousNRC(StringUtils.EMPTY);
					}
				} else if(MACDConstants.SHIFT_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
					
					opportunityBean.setType(SFDCConstants.PARALLELUPGRADE);
					opportunityBean.setSubType(SFDCConstants.PARALLELUPGRADE);
					opportunityBean.setParentOpportunityName(quoteToLe.getTpsSfdcParentOptyId().toString());
					if (serviceDetailsList != null && !serviceDetailsList.isEmpty()) {
						setPreviousMrcNrc(opportunityBean, serviceDetailsList);
					} else {
						opportunityBean.setPreviousMRC(StringUtils.EMPTY);
						opportunityBean.setPreviousNRC(StringUtils.EMPTY);
					
				}
			
			if (quoteToLe.getTermInMonths()!=null) {
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
			LOGGER.info("Inside Termination condition for create opportunity");
			if(quoteToLe!=null && quoteToLe.getTpsSfdcParentOptyId()!=null) {
				opportunityBean.setParentOpportunityName(quoteToLe.getTpsSfdcParentOptyId().toString());
			}
			opportunityBean.setType(SFDCConstants.TERMINATION);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 7);
			opportunityBean.setCloseDate(DateUtil.convertDateToString(cal.getTime()));
			
			List<String> customerIds = new ArrayList<>();
			customerIds.add(quoteToLe.getQuote().getCustomer().getErfCusCustomerId().toString());
			customerIds.add(quoteToLe.getErfCusCustomerLegalEntityId().toString());
			String request = customerIds.stream().collect(Collectors.joining(","));
			String customerLeContacts = (String) mqUtils.sendAndReceive(ownerNameBasedOnBusinessSegment, request);
			if(customerLeContacts != null) {
				LOGGER.info("customerLeContacts {}", Utils.convertObjectToJson(customerLeContacts));
			CustomerContactDetails ownerNameDetails = (CustomerContactDetails) Utils
					.convertJsonToObject(customerLeContacts, CustomerContactDetails.class);
			LOGGER.info("Owner name {}", ownerNameDetails.getEmailId());
			opportunityBean.setOwnerName(ownerNameDetails.getEmailId());
			}
			
			List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.getId());
			String serviceIds = quoteIllSiteToServiceList.stream().findFirst().get().getErfServiceInventoryTpsServiceId();
			LOGGER.info("service ids {}", serviceIds);
			if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
				if(quoteIllSiteToServiceList.size() > 1) {
					if(opportunityBean.getDummyParentTerminationOpportunity() != null && "true".equalsIgnoreCase(opportunityBean.getDummyParentTerminationOpportunity())) {
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
				LOGGER.info("quoteSiteServiceTerminationDetailsList--- {}", quoteSiteServiceTerminationDetails.toString());
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
					if(quoteSiteServiceTerminationDetails.getTerminationSendToTdDate() != null) {
					opportunityBean.setTerminationSubReason(quoteSiteServiceTerminationDetails.getSubReason());
					String formattedTDDate = dateFormatter.format(quoteSiteServiceTerminationDetails.getTerminationSendToTdDate());
					opportunityBean.setTerminationSendToTDDate(formattedTDDate);
					}
					opportunityBean.setInternalOrCustomer(quoteSiteServiceTerminationDetails.getInternalCustomer());

					String csmSfdcCode = getSfdcCodeForCustomerEmailId("CSM" , quoteSiteServiceTerminationDetails);
					LOGGER.info("csmSfdcCode {}" , csmSfdcCode);
					opportunityBean.setCsmNonCsm(csmSfdcCode);
					String commRecipientCode = getSfdcCodeForCustomerEmailId("CR" , quoteSiteServiceTerminationDetails);
					LOGGER.info("commRecipientCode {}" , commRecipientCode);
					opportunityBean.setCommunicationRecipient(commRecipientCode);
					opportunityBean.setHandoverTo(quoteSiteServiceTerminationDetails.getHandoverTo());
					opportunityBean.setHandoverOn(DateUtil.convertDateToString(new Date()));
					opportunityBean.setSalesAdministratorRegion("Global Termination Desk");
				/*
				 * User user = getUserId(Utils.getSource());
				 * opportunityBean.setSalesAdministrator(user.getFirstName().concat(" ").concat(
				 * user.getLastName())); // TODO
				 */					
					
					Optional<User> user = userRepository.findById(quoteToLe.getQuote().getCreatedBy());
					if(user.isPresent())
						opportunityBean.setSalesAdministrator(user.get().getFirstName().concat(" ").concat(user.get().getLastName())); 
					
					// get account manager
					String OrderDetailsQueue = (String) mqUtils.sendAndReceive(siOrderDetailsQueue, quoteSiteServiceTerminationDetails.getQuoteIllSiteToService().getErfServiceInventoryTpsServiceId());
					SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
							.convertJsonToObject(OrderDetailsQueue, SIServiceDetailsBean[].class);
					List<SIServiceDetailsBean> serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
					if (serviceDetailsList != null && !serviceDetailsList.isEmpty() && !opportunityBean.getName().contains("Parent")) {
						setPreviousMrcNrc(opportunityBean, serviceDetailsList);
						//opportunityBean.setOwnerName(serviceDetailsList.get(0).getAccountManager());
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
			else {
		
		
		opportunityBean.setType(SFDCConstants.NEW_ORDER.toString());
		if (quoteToLe.getTermInMonths()!=null) {
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
				opportunityBean.setTermOfMonths(String.valueOf(months));
			}
			else {
				LOGGER.info("Inside 'Double' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months1);
				opportunityBean.setTermOfMonths(String.valueOf(months1));
			}

			LOGGER.info("Final set value for term in months in Update Opty block is ----> {} " , opportunityBean.getTermOfMonths());

		}
		}
		return opportunityBean;
	}

	/**
	 * getFeasibilityBean
	 *
	 * @param productSolutionId
	 * @param feasibilityBean
	 * @return
	 * @throws TclCommonException
	 */
	@Override
	public FeasibilityRequestBean getFeasibilityBean(Integer linkId , FeasibilityRequestBean feasibilityBean)
			throws TclCommonException {

		Optional<QuoteNplLink> nplLinkOpt = nplLinkRepository.findById(linkId);
		QuoteNplLink quoteNplLink = nplLinkOpt.get();
			try {
			LOGGER.info("MDC Filter token value in before Queue call getFeasibilityBean {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			Optional<QuoteIllSite> quoteIllSiteA = illSiteRepository.findById(quoteNplLink.getSiteAId());
			String locationResponseA = (String) mqUtils.sendAndReceive(locationQueue,
					String.valueOf(quoteIllSiteA.get().getErfLocSitebLocationId()));
			if (StringUtils.isNotBlank(locationResponseA)) {
				AddressDetail addressDetailA = (AddressDetail) Utils.convertJsonToObject(locationResponseA,
						AddressDetail.class);
				feasibilityBean.setCityAEndC(addressDetailA.getCity());
				feasibilityBean.setCountryAEndC(addressDetailA.getCountry());
				feasibilityBean.setStateAEndC(addressDetailA.getState());
				feasibilityBean.setAddressAEndC(addressDetailA.getAddressLineOne());
				feasibilityBean.setAddressLine1AEndC(addressDetailA.getAddressLineOne());
				feasibilityBean.setAddressLine2AEndC(addressDetailA.getAddressLineTwo());
				if (StringUtils.isNoneBlank(addressDetailA.getAddressLineTwo())) {
					// added for too long address
					LOGGER.info("address line 2 length" + addressDetailA.getAddressLineTwo().length());
					LOGGER.info("address 2 sfdc" + feasibilityBean.getAddressLine2AEndC());
					LOGGER.info("address 2 sfdc length" + feasibilityBean.getAddressLine2AEndC().length());
					if (feasibilityBean.getAddressLine2AEndC().length() <= 80) {
						LOGGER.info("address line 2 length below 80" + feasibilityBean.getAddressLine2AEndC());
						feasibilityBean.setAddressLine2AEndC(addressDetailA.getAddressLineTwo());
					} else {
						LOGGER.info("address line 2 length above 80" + feasibilityBean.getAddressLine2AEndC());
						String valueAddress = addressDetailA.getAddressLineTwo();
						String addressTwo = valueAddress.substring(0, 79);
						LOGGER.info("after reduce length" + addressTwo.length() + "adress" + addressTwo);
						feasibilityBean.setAddressLine2AEndC(addressTwo);

					}
				}
				feasibilityBean.setPinZipAEndC(addressDetailA.getPincode());
				feasibilityBean.setCityAEnd(addressDetailA.getCity());
			}
			LOGGER.info("MDC Filter token value in before Queue call getFeasibilityBean {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			Optional<QuoteIllSite> quoteIllSiteB = illSiteRepository.findById(quoteNplLink.getSiteBId());
			String locationResponseB = (String) mqUtils.sendAndReceive(locationQueue,
					String.valueOf(quoteIllSiteB.get().getErfLocSitebLocationId()));
			if (StringUtils.isNotBlank(locationResponseB)) {
				AddressDetail addressDetailB = (AddressDetail) Utils.convertJsonToObject(locationResponseB,
						AddressDetail.class);
				feasibilityBean.setCityBEndC(addressDetailB.getCity());
				feasibilityBean.setCountryBEndC(addressDetailB.getCountry());
				feasibilityBean.setStateBEndC(addressDetailB.getState());
				feasibilityBean.setAddressBEndC(addressDetailB.getAddressLineOne());
				feasibilityBean.setAddressLine1BEndC(addressDetailB.getAddressLineOne());
				feasibilityBean.setAddressLine2BEndC(addressDetailB.getAddressLineTwo());
				if (StringUtils.isNoneBlank(addressDetailB.getAddressLineTwo())) {
					// addedd for two long address
					LOGGER.info("address line 2 b length" + addressDetailB.getAddressLineTwo().length());
					LOGGER.info("address 2  b sfdc address" + feasibilityBean.getAddressLine2BEndC());
					LOGGER.info("address 2 sfdc b length" + feasibilityBean.getAddressLine2BEndC().length());
					if (feasibilityBean.getAddressLine2BEndC().length() <= 80) {
						LOGGER.info("address line 2 b length below 80" + feasibilityBean.getAddressLine2BEndC());
						feasibilityBean.setAddressLine2BEndC(addressDetailB.getAddressLineTwo());
					} else {
						LOGGER.info("address line 2 b length above 80" + feasibilityBean.getAddressLine2BEndC());
						String valueAddress = addressDetailB.getAddressLineTwo();
						String addressTwo = valueAddress.substring(0, 79);
						LOGGER.info("after reduce b  length" + addressTwo.length() + "adress" + addressTwo);
						feasibilityBean.setAddressLine2BEndC(addressTwo);

					}
				}
				feasibilityBean.setPinZipBEndC(addressDetailB.getPincode());
				feasibilityBean.setCityBEnd(addressDetailB.getCity());
			}
			List<QuoteProductComponent> quoteProductComponents= quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndType
					(quoteNplLink.getId(),ComponentConstants.NATIONAL_CONNECTIVITY.getComponentsValue(), "Link");
			List<QuoteProductComponentsAttributeValue> quotatt = quoteProductComponentsAttributeValueRepository.
				findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponents.get(0).getId(),
						ComponentConstants.PORT_BANDWIDTH.getComponentsValue());
			feasibilityBean.setPortCircuitCapacityC(quotatt.get(0).getAttributeValues() + " Mbps");
			List<QuoteProductComponentsAttributeValue> quotatt1 = quoteProductComponentsAttributeValueRepository.
					findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponents.get(0).getId(),
							AttributeConstants.INTERFACE.toString());
			
			List<QuoteProductComponentsAttributeValue> intefaceA = quoteProductComponentsAttributeValueRepository.
					findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponents.get(0).getId(),
							"Interface Type - A end");
			List<QuoteProductComponentsAttributeValue> interfaceB = quoteProductComponentsAttributeValueRepository.
					findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponents.get(0).getId(),
							"Interface Type - B end");
			
			if(quotatt1 != null && !quotatt1.isEmpty())
				feasibilityBean.setInterfaceC(StringUtils.isEmpty(quotatt1.get(0).getAttributeValues())?"Fast Ethernet":quotatt1.get(0).getAttributeValues());
			else
				feasibilityBean.setInterfaceC("Fast Ethernet");
			
			if(null != intefaceA && !intefaceA.isEmpty())
				feasibilityBean.setInterfaceAEndC(StringUtils.isEmpty(intefaceA.get(0).getAttributeValues())?" ":intefaceA.get(0).getAttributeValues());
			if(null != interfaceB && !interfaceB.isEmpty())
				feasibilityBean.setInterfaceBEndC(StringUtils.isEmpty(interfaceB.get(0).getAttributeValues())?" ":interfaceB.get(0).getAttributeValues());
			
			Optional<String> providersName = linkFeasibilityRepository.findByQuoteNplLink_Id(quoteNplLink.getId())
					.stream().findFirst()
					.map(LinkFeasibility::getProvider);

			if(providersName.isPresent())
					feasibilityBean.setAvailableTelecomPRIProviderAEndC(providersName.get());

			List<QuoteProductComponent> components = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndReferenceName(linkId,
							IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(),QuoteConstants.NPL_LINK.toString());
			List<QuoteProductComponentsAttributeValue> lconNameValue = null;
			List<QuoteProductComponentsAttributeValue> lconNumberValue = null;

			Optional<QuoteProductComponent> componentOptional = components.stream().findFirst();
				if (componentOptional.isPresent()) {
					lconNameValue = quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(componentOptional.get().getId(),
									AttributeConstants.LCON_NAME.toString());
					lconNumberValue = quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(componentOptional.get().getId(),
									AttributeConstants.LCON_NUMBER.toString());
				}

			if (lconNameValue != null && !lconNameValue.isEmpty())
				feasibilityBean.setSiteContactNameAEndC(lconNameValue.get(0).getAttributeValues());
			if (lconNumberValue != null && !lconNumberValue.isEmpty())
				feasibilityBean.setSiteLocalContactNumberAEndC(lconNumberValue.get(0).getAttributeValues());

			List<String> type = new ArrayList<>();
			type.add("Intercity");
			type.add("Intracity");
			Optional<String> feasiblityResponseJson = linkFeasibilityRepository.findByQuoteNplLink_IdAndTypeIn(quoteNplLink.getId(),type)
					.stream().findFirst()
					.map(LinkFeasibility::getResponseJson);

			if(feasiblityResponseJson.isPresent()) {
			NotFeasible nonFeasible = (NotFeasible)Utils.convertJsonToObject(feasiblityResponseJson.get(), NotFeasible.class);
			feasibilityBean.setOtherPOPAEndC(nonFeasible.getAPopName());
			feasibilityBean.setOtherPOPBEndC(nonFeasible.getBPopName());
			}

			feasibilityBean.setTypeOfTaskC("Single");


			List<String> types = new ArrayList<>();
			types.add("Site-A");
			types.add("Site-B");
			List<Integer> siteIds = new ArrayList<>();
			siteIds.add(quoteNplLink.getSiteAId());
			siteIds.add(quoteNplLink.getSiteBId());
			Optional <QuoteProductComponent> quoteProductComponents1= quoteProductComponentRepository.findByReferenceIdInAndMstProductComponent_NameAndTypeIn
					(siteIds,AttributeConstants.LAST_MILE.toString(),types).stream().findFirst();
			if (quoteProductComponents1.isPresent()) {
			List<QuoteProductComponentsAttributeValue> quoteattribute = quoteProductComponentsAttributeValueRepository.
				findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponents1.get().getId(),
						ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue());


			if(!quoteattribute.isEmpty()) {
			String localLoopCapacity = quoteattribute.get(0).getAttributeValues();

			String[] spliter = localLoopCapacity.split("\\s+");
			String locLoopCap = spliter[0];
			String unit = "Mbps";
			if (spliter.length > 1) {
			unit  = spliter[1];
			}
			feasibilityBean.setIllLocalLoopCapacityC(locLoopCap);
			feasibilityBean.setIllLocalLoopCapacityUnitC(unit);
			}
			}
			feasibilityBean.setContinentAEndC("Asia");
			feasibilityBean.setContinentBEndC("Asia");

			feasibilityBean.setStatus(SFDCConstants.STATUS_ASSIGNED);
			feasibilityBean.setRequestTypeC(SFDCConstants.LM_FIRM_QUOTE);
			feasibilityBean.setRecordTypeName(SFDCConstants.DEFAULT_FEASIBILITY_REQUEST);
			feasibilityBean.setSpecialRequirementsC(SFDCConstants.NOT_APPLICABLE);

			}catch(TclCommonException e) {
				throw new TclCommonRuntimeException(e);
			}



		return feasibilityBean;
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
			ProductSolution solution) throws TclCommonException {
		LOGGER.info("inside nde mapper getProductServiceInput");
		
		List<QuoteNplLink> nplLinks = nplLinkRepository.findByQuoteIdAndStatus(quoteToLe.getQuote().getId(), (byte) 1);
		String hubEhsId=null;
		int linkId=0;
		if (nplLinks != null) {
			QuoteNplLink link = nplLinks.get(0);
			if (link != null) {
				linkId = link.getId();
				hubEhsId = getAttibuteValue(SFDCConstants.NATIONAL_CONECTIVITY, linkId, SFDCConstants.HUB_PARENT_ID,
						"Link");
				LOGGER.info("hub ehs id ********" + hubEhsId);
			}
		}
		
			if (StringUtils.isNotBlank(hubEhsId)) {
				LOGGER.info(" Entered into hub nde :hub ehs id ********" + hubEhsId);
				productServiceBean.setHubId(hubEhsId);
				productServiceBean.setProductType(SFDCConstants.H_NDE.toString());
				productServiceBean.setRecordTypeName(SFDCConstants.H_NDE.toString());
				productServiceBean.setProductSubType(SFDCConstants.HUB_NDE);
			}
		
		else {
			   LOGGER.info(" Entered into without hub nde :hub ehs id ********" + hubEhsId);
			   productServiceBean.setHubId(hubEhsId);
			   productServiceBean.setProductType(SFDCConstants.NATIONAL_DEDICATED_ETHERNET.toString());
			   productServiceBean.setRecordTypeName(SFDCConstants.NATIONAL_DEDICATED_ETHERNET.toString());
			   productServiceBean.setProductSubType(SFDCConstants.NDE);
		}
		
		productServiceBean.setProductLineOfBusiness(SFDCConstants.IPVPN.toString());
		productServiceBean.setaEndCountryC("India");
		productServiceBean.setQuantityC("0");
		if (Objects.nonNull(solution.getMstProductOffering()) && Objects.nonNull(solution.getMstProductOffering().getProductName())
				&& CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(solution.getMstProductOffering().getProductName())){
			productServiceBean.setType(SFDCConstants.BSO_MMR_CROSS_CONNECT);
			List<QuoteIllSite> quoteIllSites = illSiteRepository.findByProductSolutionAndStatus(solution, (byte) 1);
			Integer siteId=quoteIllSites.stream().findFirst().get().getId();
			String subType = getAttibuteValue(
					SFDCConstants.CROSS_CONNECT,siteId,"Cross Connect Type","primary");
			if(subType.equalsIgnoreCase("Passive")) {
				productServiceBean.setInterfaceC("NA");
				productServiceBean.setBandwidthCircuitSpeed("NA");
				productServiceBean.setOfFiberC(getAttibuteValue(
					SFDCConstants.FIBER_ENTRY,siteId,CrossconnectConstants.FIBER_PAIRS_COUNT,"primary"));
				productServiceBean.setTypeOfFiberEntry(getAttibuteValue(
						SFDCConstants.FIBER_ENTRY,siteId,CrossconnectConstants.TYPE_OF_FIBER_ENTRY,"primary"));
			}else
				productServiceBean.setInterfaceC(getAttibuteValue(
					SFDCConstants.CROSS_CONNECT,siteId,"Interface","primary"));
			productServiceBean.setSubType(subType);
			productServiceBean.setMediaType(getAttibuteValue(
					SFDCConstants.CROSS_CONNECT,siteId,"Media Type","primary"));

		}else {
			productServiceBean.setType("Inter City");
			productServiceBean.setInterfaceC("G.957");
			productServiceBean.setaEndCityC("siteACity");
			productServiceBean.setbEndCityC("siteBCity");
		}
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
		
		if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			 if (MACDConstants.SHIFT_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				productServiceBean.setOrderType(SFDCConstants.PARALLELUPGRADE);
				productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
			} else if (MACDConstants.CHANGE_BANDWIDTH_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				productServiceBean.setOrderType(SFDCConstants.PARALLELUPGRADE);
				productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
			}
		} else if(MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
        	productServiceBean.setOrderType(SFDCConstants.NEW_ORDER);
			productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
			productServiceBean.setIsCancel(true);
        } else if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			productServiceBean.setOrderType(SFDCConstants.TERMINATION);
			productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
			productServiceBean.setProductMRC(0D);
			productServiceBean.setProductNRC(0D);
		}

		LOGGER.info("Input  for product Service Npl {}", productServiceBean);

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
			ProductSolution solution) throws TclCommonException {
		Integer linkId = 0;
		//added for nde without hub needs to pass nde
		List<QuoteNplLink> nplLinkDetails = nplLinkRepository.findByQuoteIdAndStatus(quoteToLe.getQuote().getId(), (byte) 1);
		String hubEhsId=null;
		if (nplLinkDetails != null) {
			QuoteNplLink link = nplLinkDetails.get(0);
			if (link != null) {
				linkId = link.getId();
				hubEhsId = getAttibuteValue(SFDCConstants.NATIONAL_CONECTIVITY, linkId, SFDCConstants.HUB_PARENTED,
						"Link");
				LOGGER.info("hub ehs id ********" + hubEhsId);
			}
		}
		if(StringUtils.isNotBlank(hubEhsId) && hubEhsId.equalsIgnoreCase(CommonConstants.YES)) {
		 productServiceBean.setRecordTypeName(SFDCConstants.HUB_NDE_SFDC_RECORD.toString());
		}
		else {
			productServiceBean.setRecordTypeName(SFDCConstants.NATIONAL_DEDICATED_ETHERNET.toString());
		}
		LOGGER.info("Record type name"+productServiceBean.getRecordTypeName());
		productServiceBean.setProductLineOfBusiness(SFDCConstants.GTS.toString());
		if (Objects.nonNull(solution.getMstProductOffering()) && Objects.nonNull(solution.getMstProductOffering().getProductName())
				&& CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(solution.getMstProductOffering().getProductName())) {
			productServiceBean.setType(SFDCConstants.BSO_MMR_CROSS_CONNECT);
			List<QuoteIllSite> quoteIllSites = illSiteRepository.findByProductSolutionAndStatus(solution, (byte) 1);
			Integer siteId=quoteIllSites.stream().findFirst().get().getId();
			AddressDetail addressDetail = getAddressDetail(quoteIllSites.stream().findFirst().get().getErfLocSitebLocationId());
			productServiceBean.setbEndCountryC(addressDetail.getCountry());
			productServiceBean.setbEndCityC(addressDetail.getCity());
			String subType = getAttibuteValue(
					SFDCConstants.CROSS_CONNECT,siteId,"Cross Connect Type","primary");
			if(subType.equalsIgnoreCase("Passive")) {
				productServiceBean.setInterfaceC("NA");
				productServiceBean.setBandwidthCircuitSpeed("NA");
				productServiceBean.setOfFiberC(getAttibuteValue(
						SFDCConstants.FIBER_ENTRY,siteId,CrossconnectConstants.FIBER_PAIRS_COUNT,"primary"));
				productServiceBean.setTypeOfFiberEntry(getAttibuteValue(
						SFDCConstants.FIBER_ENTRY,siteId,CrossconnectConstants.TYPE_OF_FIBER_ENTRY,"primary"));
			}else
				productServiceBean.setInterfaceC(getAttibuteValue(
					SFDCConstants.CROSS_CONNECT,siteId,"Interface","primary"));
			productServiceBean.setSubType(subType);
			productServiceBean.setMediaType(getAttibuteValue(
					SFDCConstants.CROSS_CONNECT,siteId,"Media Type","primary"));

			
		} else {
			List<QuoteNplLink> nplLinks = nplLinkRepository.findByQuoteIdAndStatus(quoteToLe.getQuote().getId(), (byte) 1);
			if (nplLinks != null) {
				QuoteNplLink link = nplLinks.get(0);
				if (link != null) {
					String linkType = link.getLinkType();
					if (linkType != null && linkType.equals("Intercity")) {
						linkType = "Inter City";
					} else if (linkType != null && linkType.equals("Intracity")) {
						linkType = "Intra City";
					}
					productServiceBean.setType(linkType);
					linkId = link.getId();
					QuoteIllSite siteA = null, siteB = null;
					if (link.getSiteAId() != null)
						siteA = illSiteRepository.findByIdAndStatus(link.getSiteAId(), (byte) 1);

					if (link.getSiteBId() != null)
						siteB = illSiteRepository.findByIdAndStatus(link.getSiteBId(), (byte) 1);
					if (siteA != null && siteA.getErfLocSitebLocationId() != null) {
						/*LOGGER.info("MDC Filter token value in before Queue call updateProductServiceInput {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
						String locationResponseSiteA = (String) mqUtils.sendAndReceive(locationQueue,
								String.valueOf(siteA.getErfLocSitebLocationId()));
						AddressDetail addressDetail1 = (AddressDetail) Utils.convertJsonToObject(locationResponseSiteA,
								AddressDetail.class);*/
						AddressDetail addressDetail1 = getAddressDetail(siteA.getErfLocSitebLocationId());
						productServiceBean.setaEndCountryC(addressDetail1.getCountry());
						productServiceBean.setaEndCityC(addressDetail1.getCity());
					}
					if (siteB != null && siteB.getErfLocSitebLocationId() != null) {
						/*LOGGER.info("MDC Filter token value in before Queue call updateProductServiceInput {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
						String locationResponseSiteB = (String) mqUtils.sendAndReceive(locationQueue,
								String.valueOf(siteB.getErfLocSitebLocationId()));
						AddressDetail addressDetail2 = (AddressDetail) Utils.convertJsonToObject(locationResponseSiteB,
								AddressDetail.class);*/
						AddressDetail addressDetail2 = getAddressDetail(siteB.getErfLocSitebLocationId());
						productServiceBean.setbEndCountryC(addressDetail2.getCountry());
						productServiceBean.setbEndCityC(addressDetail2.getCity());
					}


				}

				/*String interfaceName = null;
				MstProductComponent productComponent = mstProductComponentRepository.findByName("National Connectivity");
				MstProductFamily productFamily = mstProductFamilyRepository.findByNameAndStatus(SFDCConstants.NPL.toString(),
						(byte) 1);
				List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
						.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(linkId, productComponent,
								productFamily, "Link");

				for (QuoteProductComponent proComponent : quoteProductComponents) {
					List<QuoteProductComponentsAttributeValue> quoteProductComponentAttributes = quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponent_Id(proComponent.getId());

					for (QuoteProductComponentsAttributeValue quoteProAttribute : quoteProductComponentAttributes) {
						if (interfaceName == null) {
							Optional<ProductAttributeMaster> attribute = productAttributeMasterRepository
									.findById(quoteProAttribute.getProductAttributeMaster().getId());
							if (attribute.isPresent()) {
								if (attribute.get().getName().toLowerCase().contains("interface")) {
									interfaceName = attribute.get().getName();
								}
							}
						} else
							break;
					}
				}*/
				;

				productServiceBean.setInterfaceC(getAttibuteValue(
						"National Connectivity",linkId,"Interface","Link"));
			}
		}
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

		productServiceBean.setQuantityC("0");
		productServiceBean.setProductType(SFDCConstants.NDE.toString());
		productServiceBean.setCloudEnablementC("No");


		Map<String, String> attributeMapper = getQuoteLeAttributes(quoteToLe);
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
		
		if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()))
		{
			//added for nde mc
			if (quoteToLe.getIsMultiCircuit() == 1) {
				LOGGER.info("entered into mc update product macd ");
				UpdateRequest updateRequest = new UpdateRequest();
				OpportunityBean opportunityBean = new OpportunityBean();
				Quote quote = quoteToLe.getQuote();
				QuoteIllSite[] illSiteOpt = { null };
				// Quote quote = quoteToLe.getQuote();
				if (Objects.nonNull(quote)) {
					List<QuoteNplLink> quoteNplLinkList = quoteNplLinkRepository.findByQuoteIdAndStatus(quote.getId(),
							CommonConstants.BACTIVE);
					if (quoteNplLinkList != null && !quoteNplLinkList.isEmpty()) {
						LOGGER.info("Link Id :: {}", quoteNplLinkList.get(0).getId());
						updateRequest.setLinkId(quoteNplLinkList.get(0).getId());
					}
					LOGGER.info("updateRequest Link Id :: {}", updateRequest.getLinkId());
					Optional<QuoteNplLink> nplLink = quoteNplLinkRepository.findById(updateRequest.getLinkId());
					LOGGER.info("nplLink Link Id :: {}", nplLink.get().getId());
					if (updateRequest.getLinkId() != null) {
						if (MACDConstants.SHIFT_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
							LOGGER.info("Entered into shift site mc");
							if (nplLink.isPresent()) {
								if (nplLink.get().getSiteAId() != null) {
									Optional<QuoteIllSite> illSiteOpta = illSiteRepository
											.findById(nplLink.get().getSiteAId());
									if (illSiteOpta.get().getNplShiftSiteFlag() != null) {
										if (illSiteOpta.get().getNplShiftSiteFlag() == 1) {
											illSiteOpt[0] = illSiteOpta.get();
										}
									}
								}
								if (nplLink.get().getSiteBId() != null) {
									Optional<QuoteIllSite> illSiteOptb = illSiteRepository
											.findById(nplLink.get().getSiteBId());
									if (illSiteOptb.get().getNplShiftSiteFlag() != null) {
										if (illSiteOptb.get().getNplShiftSiteFlag() == 1) {
											illSiteOpt[0] = illSiteOptb.get();
										}
									}
								}
								LOGGER.info("Entered into shift site check id"+illSiteOpt[0].getId());
							}

						} else {
							LOGGER.info("Entered into chnage bandwidth mc"+nplLink.get().getSiteAId());
							if (nplLink.isPresent()) {
								if (nplLink.get().getSiteAId() != null) {
									Optional<QuoteIllSite> illSiteOpta = illSiteRepository
											.findById(nplLink.get().getSiteAId());
											illSiteOpt[0] = illSiteOpta.get();
											LOGGER.info("Entered into change bandwidth check site id"+illSiteOpt[0].getId());
								}
								
							}
						}
					}
					LOGGER.info("Site before update product is ::::: {}", illSiteOpt[0].getId());
					updateRequest.setSiteId(Objects.nonNull(illSiteOpt[0]) ? illSiteOpt[0].getId() : -1);
					updateRequest.setQuoteToLe(quoteToLe.getId());
					LOGGER.info("link id and site id for method"+"linkid:"+updateRequest.getLinkId()+"siteid:"+updateRequest.getSiteId());
					opportunityBean = omsSfdcService.updateOrderTypeAndSubtype(updateRequest);
					productServiceBean.setOrderType(opportunityBean.getType());
				}
			}
			else {
				LOGGER.info("entered into normal macd update prod");
				UpdateRequest updateRequest = new UpdateRequest();
				OpportunityBean opportunityBean = new OpportunityBean();
				Quote quote = quoteToLe.getQuote();
				QuoteIllSite[] illSiteOpt = {null};
				//Quote quote = quoteToLe.getQuote();
				if (Objects.nonNull(quote)) {
					if(MACDConstants.SHIFT_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
						quote.getQuoteToLes().stream().forEach(quoteLe -> {
							quoteLe.getQuoteToLeProductFamilies().stream().forEach(quoteLeProductFamily -> {
								quoteLeProductFamily.getProductSolutions().forEach(quoteProdSolutions -> {
									illSiteOpt[0] = quoteProdSolutions.getQuoteIllSites().stream().filter(illsite ->
									CommonConstants.ACTIVE.equals(illsite.getNplShiftSiteFlag())).findFirst().get();
								});
							});
						});
								
					} else {
						illSiteOpt[0] = quoteToLe.getQuote().getQuoteToLes().stream().findFirst().map(QuoteToLe::getQuoteToLeProductFamilies)
								.get().stream().map(QuoteToLeProductFamily::getProductSolutions)
								.findFirst().get().stream()
								.map(ProductSolution::getQuoteIllSites).findFirst().get()
								.stream().findFirst().get();
					}
					LOGGER.info("Site before update product is ::::: {}", illSiteOpt[0].getId());
					updateRequest.setSiteId(Objects.nonNull(illSiteOpt[0])? illSiteOpt[0].getId():-1);
					updateRequest.setQuoteToLe(quoteToLe.getId());
					List<QuoteNplLink> quoteNplLinkList = quoteNplLinkRepository.findByQuoteIdAndStatus(quote.getId(), CommonConstants.BACTIVE);
					if(quoteNplLinkList != null && !quoteNplLinkList.isEmpty()) {
						LOGGER.info("Link Id :: {}", quoteNplLinkList.get(0).getId() );
						updateRequest.setLinkId(quoteNplLinkList.get(0).getId());
					}
					opportunityBean = omsSfdcService.updateOrderTypeAndSubtype(updateRequest);
					productServiceBean.setOrderType(opportunityBean.getType());
				}
				
			}
		} else if (MACDConstants.CANCELLATION.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			LOGGER.info("Inside Cancellation if condition updateProductServiceInput");
			OpportunityBean opportunityBean = new OpportunityBean();
			if(StringUtils.isNotBlank(hubEhsId)) {
				 productServiceBean.setRecordTypeName(SFDCConstants.H_NDE.toString());
				 productServiceBean.setType(SFDCConstants.H_NDE.toString());
				}
				else {
					productServiceBean.setRecordTypeName(SFDCConstants.NATIONAL_DEDICATED_ETHERNET.toString());
					productServiceBean.setType(SFDCConstants.NATIONAL_DEDICATED_ETHERNET.toString());
				}
			if (Objects.nonNull(solution.getMstProductOffering()) && Objects.nonNull(solution.getMstProductOffering().getProductName())
					&& CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(solution.getMstProductOffering().getProductName())) {
				productServiceBean.setRecordTypeName(SFDCConstants.BSO_MMR_CROSS_CONNECT);
				productServiceBean.setType(SFDCConstants.BSO_MMR_CROSS_CONNECT);
			}
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
						if(quoteIllSiteToService.getQuoteNplLink().getProductSolutionId().equals(solution.getId())) {
					MDMServiceInventoryBean serviceDetail = cancellationService.getServiceDetailsForOrderCancellation(1, 10, null, null, null, null,
							quoteIllSiteToService.getErfServiceInventoryTpsServiceId(), null, null);
					if(serviceDetail != null && serviceDetail.getServiceDetailBeans() != null && !serviceDetail.getServiceDetailBeans().isEmpty()) {						
						LOGGER.info("service id {}, mrc {}, nrc {}, link code {}, order code {}", serviceDetail.getServiceDetailBeans().get(0).getServiceId(), 
								serviceDetail.getServiceDetailBeans().get(0).getMrc(), serviceDetail.getServiceDetailBeans().get(0).getNrc(), 
								serviceDetail.getServiceDetailBeans().get(0).getLinkCode(), serviceDetail.getServiceDetailBeans().get(0).getOrderCode()) ;
						
						List<QuoteSiteDifferentialCommercial> quoteSiteDifferentialCommercialList = null;
							LOGGER.info("NDE service id");
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
							if(optimusMdmDetailBean.getLinkCode() != null && optimusMdmDetailBean.getOrderCode() != null 
									&& optimusMdmDetailBean.getSourceSystem().equalsIgnoreCase("OPTIMUS_O2C")) {
								LOGGER.info("link code  order code not null");
								quoteSiteDifferentialCommercialList =quoteSiteDifferentialCommercialRepository.findByQuoteLinkCodeAndQuoteCode(optimusMdmDetailBean.getLinkCode(), optimusMdmDetailBean.getOrderCode());
								if(quoteSiteDifferentialCommercialList != null && !quoteSiteDifferentialCommercialList.isEmpty()) {
									LOGGER.info("Entry present in quote site commercial");
									quoteSiteDifferentialCommercialList.stream().forEach(differentialCommercial -> {
										diffMrc[0] += differentialCommercial.getDifferentialMrc();
										diffNrc[0] += differentialCommercial.getDifferentialNrc();
									});
									
								} else {
									LOGGER.info("Entry is not present in quote site commercial, saving in quote site diff commercial");
									List<QuoteNplLink> quoteNplLinkList = quoteNplLinkRepository.findByLinkCodeAndStatus(optimusMdmDetailBean.getLinkCode(), CommonConstants.BACTIVE);
									Order order  = orderRepository.findByOrderCode(optimusMdmDetailBean.getOrderCode());
									if(quoteNplLinkList != null && !quoteNplLinkList.isEmpty()) {
									quoteSiteDifferentialCommercialList = nplQuoteService.persistQuoteSiteCommercialsAtServiceIdLevel(quoteNplLinkList.get(0).getId(), order);
									quoteSiteDifferentialCommercialList.stream().forEach(differentialCommercial -> {
										diffMrc[0] += differentialCommercial.getDifferentialMrc();
										diffNrc[0] += differentialCommercial.getDifferentialNrc();
									});
									}
								}
								}else {
									LOGGER.info("order code or link code is null POS NDE {} ",optimusMdmDetailBean.getServiceId());
//									productServiceBean.setProductMRC((optimusMdmDetailBean.getMrc() != null) ? Double.valueOf(0-optimusMdmDetailBean.getMrc()) : 0);
//									productServiceBean.setProductNRC((optimusMdmDetailBean.getNrc() != null) ? Double.valueOf(0-optimusMdmDetailBean.getNrc()) : 0);
									
									LOGGER.info("NDE order code or link code is null");
									SIServiceDetailDataBean posServiceDetails;
									try {
										//if macd differ mrc = cancellationViewPosmrc - inventorymrc
										LOGGER.info("POS NDE Fetching  service details for {} ",optimusMdmDetailBean.getServiceId());
										posServiceDetails = macdUtils.getUnderProvisioningServiceDetail(quoteIllSiteToService.getErfServiceInventoryTpsServiceId());
//										posServiceDetails = serviceDetailsBySrvId.stream().findFirst().get();
									} catch (TclCommonException e) {
										LOGGER.info("Cancellation NDE Error in POS condition persistQuoteSiteCommercialsAtServiceIdLevel {}", e);
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

		LOGGER.info("Input  for product Service Npl {}", productServiceBean);

		return productServiceBean;
	}

	private AddressDetail getAddressDetail(Integer erfLocSitebLocationId) throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call updateProductServiceInput {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String locationResponseSiteA = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(erfLocSitebLocationId));
		AddressDetail addressDetail1 = (AddressDetail) Utils.convertJsonToObject(locationResponseSiteA,
				AddressDetail.class);
		return addressDetail1;
	}

	private String getAttibuteValue(String mstComponentName,Integer referenceId,String attributeName,String type){
		String attributeValue = null;
		MstProductComponent productComponent = mstProductComponentRepository.findByName(mstComponentName);
		MstProductFamily productFamily = mstProductFamilyRepository.findByNameAndStatus(SFDCConstants.NDE.toString(),
				(byte) 1);
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(referenceId, productComponent,
						productFamily, type);

		for (QuoteProductComponent proComponent : quoteProductComponents) {
			List<QuoteProductComponentsAttributeValue> quoteProductComponentAttributes = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_Id(proComponent.getId());

			for (QuoteProductComponentsAttributeValue quoteProAttribute : quoteProductComponentAttributes) {
				if(attributeName.equalsIgnoreCase(quoteProAttribute.getProductAttributeMaster().getName())){
					attributeValue=quoteProAttribute.getAttributeValues();
					break;
				}
			}
		}
		return attributeValue;
	}
	@Override
	public void processSiteDetails(QuoteToLe quoteToLe, SiteSolutionOpportunityBean siteSolutionOpportunityBean,ProductSolution productSolution) throws TclCommonException
	{
		List<SiteOpportunityLocation> siteOpportunityLocations = new ArrayList<>();

		siteSolutionOpportunityBean.setSiteOpportunityLocations(null);// Empty the generalised data
		List<QuoteNplLink> links = nplLinkRepository.findByProductSolutionIdAndStatus(productSolution.getId(),CommonConstants.BACTIVE);

		if (!links.isEmpty()) {
			for (QuoteNplLink link : links) {

				QuoteIllSite siteA = illSiteRepository.findByIdAndStatus(link.getSiteAId(), CommonConstants.BACTIVE);
				QuoteIllSite siteB = illSiteRepository.findByIdAndStatus(link.getSiteBId(), CommonConstants.BACTIVE);
				if (siteA!=null)
					siteOpportunityLocations.add(constructSiteLocation(link,siteA));
				if (siteB!=null)
					siteOpportunityLocations.add(constructSiteLocation(link,siteB));
			}
		}

		siteSolutionOpportunityBean.setSiteOpportunityLocations(siteOpportunityLocations);

	}

	public SiteOpportunityLocation constructSiteLocation(QuoteNplLink link, QuoteIllSite site ) throws TclCommonException{

		Double siteMrc=0D,siteArc=0D,siteNrc=0D;
		SiteOpportunityLocation siteOpportunityLocation = new SiteOpportunityLocation();
		LOGGER.info("MDC Filter token value in before Queue call constructSiteLocation {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(site.getErfLocSitebLocationId()));
		if (StringUtils.isNotBlank(locationResponse)) {
			AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
					AddressDetail.class);

			siteOpportunityLocation.setCity(addressDetail.getCity());
			siteOpportunityLocation.setCountry(addressDetail.getCountry());
			siteOpportunityLocation.setLocation(addressDetail.getCity());
			siteOpportunityLocation.setState(addressDetail.getState());
		}
		siteOpportunityLocation
				.setSiteLocationID(SFDCConstants.OPTIMUS.toString() + site.getSiteCode());

		if (link.getMrc()!=null && link.getMrc()!=0)
			siteMrc = link.getMrc()/2;

		if (link.getArc()!=null && link.getArc()!=0)
			siteArc = link.getArc()/2;

		if (link.getNrc()!=null && link.getNrc()!=0)
			siteNrc = link.getNrc()/2;

		siteOpportunityLocation.setSiteMRC(siteMrc);
		siteOpportunityLocation.setSiteARC(siteArc);
		siteOpportunityLocation.setSiteNRC(siteNrc);
		siteOpportunityLocation.setLinkCode(link.getLinkCode());
		
		if(MACDConstants.CANCELLATION.equalsIgnoreCase(site.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuoteType())) {
			List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteNplLink_Id(link.getId());
			if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
				
			siteOpportunityLocation.setCurrentCircuitServiceId(quoteIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId());
			LOGGER.info("Setting current circuit service id for NDE Cancellation service {}", quoteIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId());
			}
		}
		return siteOpportunityLocation;

	}



	/**
	 * getSiteBeanInput
	 *
	 * @param quoteToLe
	 * @param solutionOpportunityBean
	 * @return
	 * @throws TclCommonException
	 */
	@Override
	public SiteSolutionOpportunityBean getSiteBeanInput(QuoteToLe quoteToLe,
			SiteSolutionOpportunityBean solutionOpportunityBean) throws TclCommonException {
		// TODO Auto-generated method stub
		return solutionOpportunityBean;
	}

	/**
	 * getOpportunityUpdate
	 *
	 * @param quoteToLe
	 * @param updateOpportunityStage
	 * @return
	 * @throws TclCommonException
	 */
	@Override
	public UpdateOpportunityStage getOpportunityUpdate(QuoteToLe quoteToLe,
			UpdateOpportunityStage updateOpportunityStage) throws TclCommonException {
		if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()))
		{
			
			if (quoteToLe.getIsMultiCircuit() == 1) {
				LOGGER.info("entered into mc macd update opty");
				UpdateRequest updateRequest = new UpdateRequest();
				OpportunityBean opportunityBean = new OpportunityBean();
				Quote quote = quoteToLe.getQuote();
				QuoteIllSite[] illSiteOpt = { null };
				if (Objects.nonNull(quote)) {

					List<QuoteNplLink> quoteNplLinkList = quoteNplLinkRepository.findByQuoteIdAndStatus(quote.getId(),
							CommonConstants.BACTIVE);
					if (quoteNplLinkList != null && !quoteNplLinkList.isEmpty()) {
						LOGGER.info("Link Id :: {}", quoteNplLinkList.get(0).getId());
						updateRequest.setLinkId(quoteNplLinkList.get(0).getId());
					}
					LOGGER.info("updateRequest Link update Id :: {}", updateRequest.getLinkId());
					Optional<QuoteNplLink> nplLink = quoteNplLinkRepository.findById(updateRequest.getLinkId());
					if (updateRequest.getLinkId() != null) {
						if (MACDConstants.SHIFT_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
							if (nplLink.isPresent()) {
								if (nplLink.get().getSiteAId() != null) {
									Optional<QuoteIllSite> illSiteOpta = illSiteRepository
											.findById(nplLink.get().getSiteAId());
									if (illSiteOpta.get().getNplShiftSiteFlag() != null) {
										if (illSiteOpta.get().getNplShiftSiteFlag() == 1) {
											illSiteOpt[0] = illSiteOpta.get();
										}
									}
								}
								if (nplLink.get().getSiteBId() != null) {
									Optional<QuoteIllSite> illSiteOptb = illSiteRepository
											.findById(nplLink.get().getSiteBId());
									if (illSiteOptb.get().getNplShiftSiteFlag() != null) {
										if (illSiteOptb.get().getNplShiftSiteFlag() == 1) {
											illSiteOpt[0] = illSiteOptb.get();
										}
									}
								}
							}

						} else {
							if (nplLink.isPresent()) {
								if (nplLink.get().getSiteAId() != null) {
									Optional<QuoteIllSite> illSiteOpta = illSiteRepository
											.findById(nplLink.get().getSiteAId());
											illSiteOpt[0] = illSiteOpta.get();
								}
								
							}
						}
					}
					LOGGER.info("Site before update opportunity is ::::: {}", illSiteOpt[0].getId());
					updateRequest.setSiteId(Objects.nonNull(illSiteOpt[0]) ? illSiteOpt[0].getId() : -1);
					updateRequest.setQuoteToLe(quoteToLe.getId());
					LOGGER.info("link id and site id for method"+"linkid:"+updateRequest.getLinkId()+"siteid:"+updateRequest.getSiteId());
					opportunityBean = omsSfdcService.updateOrderTypeAndSubtype(updateRequest);
					
					updateOpportunityStage.setType(opportunityBean.getType());
					updateOpportunityStage.setSub_Type__c(opportunityBean.getSubType());
					updateOpportunityStage.setCopfIdC(opportunityBean.getCopfIdC());
				}

			}
			else {
				LOGGER.info("entered into normal macd upadte opty");
				UpdateRequest updateRequest = new UpdateRequest();
				OpportunityBean opportunityBean = new OpportunityBean();
				Quote quote = quoteToLe.getQuote();
				QuoteIllSite[] illSiteOpt = { null };
				if (Objects.nonNull(quote)) {
					if (MACDConstants.SHIFT_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
						quote.getQuoteToLes().stream().forEach(quoteLe -> {
							quoteLe.getQuoteToLeProductFamilies().stream().forEach(quoteLeProductFamily -> {
								quoteLeProductFamily.getProductSolutions().forEach(quoteProdSolutions -> {
									illSiteOpt[0] = quoteProdSolutions.getQuoteIllSites().stream().filter(
											illsite -> CommonConstants.ACTIVE.equals(illsite.getNplShiftSiteFlag()))
											.findFirst().get();
								});
							});
						});

					} else {
						illSiteOpt[0] = quoteToLe.getQuote().getQuoteToLes().stream().findFirst()
								.map(QuoteToLe::getQuoteToLeProductFamilies).get().stream()
								.map(QuoteToLeProductFamily::getProductSolutions).findFirst().get().stream()
								.map(ProductSolution::getQuoteIllSites).findFirst().get().stream().findFirst().get();
					}
					List<QuoteNplLink> quoteNplLinkList = quoteNplLinkRepository.findByQuoteIdAndStatus(quote.getId(),
							CommonConstants.BACTIVE);
					updateRequest.setSiteId(illSiteOpt[0].getId());
					updateRequest.setQuoteToLe(quoteToLe.getId());
					if (quoteNplLinkList != null && !quoteNplLinkList.isEmpty()) {
						LOGGER.info("Link Id :: {}", quoteNplLinkList.get(0).getId());
						updateRequest.setLinkId(quoteNplLinkList.get(0).getId());
					}

					opportunityBean = omsSfdcService.updateOrderTypeAndSubtype(updateRequest);

					updateOpportunityStage.setType(opportunityBean.getType());
					updateOpportunityStage.setSub_Type__c(opportunityBean.getSubType());
					updateOpportunityStage.setCopfIdC(opportunityBean.getCopfIdC());
				}

			
				
			}
		} else if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			updateOpportunityStage.setType(SFDCConstants.TERMINATION);
			updateOpportunityStage.setCloseDate(DateUtil.convertDateToString(new Date()));
			LOGGER.info("Value of dummyParentTerminationOpportunity  :: {}",updateOpportunityStage.getDummyParentTerminationOpportunity());
			if(updateOpportunityStage.getDummyParentTerminationOpportunity() == null || (updateOpportunityStage.getDummyParentTerminationOpportunity() != null
					&& "false".equalsIgnoreCase(updateOpportunityStage.getDummyParentTerminationOpportunity()))){
			List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.getId());

			if(updateOpportunityStage.getStageName()!= null && !SFDCConstants.CLOSED_DROPPED.equalsIgnoreCase(updateOpportunityStage.getStageName())){
				quoteIllSiteToServiceList.removeIf(quoteIllSiteToService -> CommonConstants.INACTIVE.equals(quoteIllSiteToService.getIsDeleted()));
			}

			Optional<QuoteIllSiteToService> qSiteToService  = null;
			if(quoteIllSiteToServiceList != null && quoteIllSiteToServiceList.size() > 1) {
			 qSiteToService = quoteIllSiteToServiceList.stream()
					.filter(quoteSiteToService -> updateOpportunityStage.getCurrentCircuitServiceId().equalsIgnoreCase(quoteSiteToService.getErfServiceInventoryTpsServiceId())).findFirst();
			} else {
				qSiteToService = quoteIllSiteToServiceList.stream().findFirst();
			}
			QuoteSiteServiceTerminationDetails quoteSiteServiceTerminationDetails  = null;
			if(qSiteToService.isPresent()) {
			quoteSiteServiceTerminationDetails = quoteSiteServiceTerminationDetailsRepository.findByQuoteIllSiteToService(qSiteToService.get());
			LOGGER.info("quoteSiteServiceTerminationDetailsList--- {}", quoteSiteServiceTerminationDetails.toString());
			}
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
				if(updateOpportunityStage.getStageName() != null && SFDCConstants.IDENTIFIED_OPTY_STAGE.equals(updateOpportunityStage.getStageName())) {
					updateOpportunityStage.setHandoverTo(quoteSiteServiceTerminationDetails.getHandoverTo());
				}
				else {
				updateOpportunityStage.setHandoverTo(MACDConstants.SEND_TO_TD);
				}
				updateOpportunityStage.setTerminationRemarks(quoteSiteServiceTerminationDetails.getTerminationRemarks());
				updateOpportunityStage.setRegrettedNonRegrettedTermination(quoteSiteServiceTerminationDetails.getRegrettedNonRegrettedTermination());
				updateOpportunityStage.setLeadTimeToRFSC("30");
				updateOpportunityStage.setEarlyTerminationChargesApplicable(CommonConstants.BACTIVE.equals(quoteSiteServiceTerminationDetails.getEtcApplicable()) ? SFDCConstants.TRUE : SFDCConstants.FALSE);
				updateOpportunityStage.setEarlyTerminationCharges(quoteSiteServiceTerminationDetails.getActualEtc() != null ? quoteSiteServiceTerminationDetails.getActualEtc().toString() : "");
				if(quoteSiteServiceTerminationDetails.getFinalEtc() == null) {
				updateOpportunityStage.setActualEtcToBeCharged(quoteSiteServiceTerminationDetails.getActualEtc() != null ? quoteSiteServiceTerminationDetails.getActualEtc().toString() : "");
				} else {
					updateOpportunityStage.setActualEtcToBeCharged(quoteSiteServiceTerminationDetails.getFinalEtc() != null ? quoteSiteServiceTerminationDetails.getFinalEtc().toString() : "");
				}
				if(quoteSiteServiceTerminationDetails.getWaiverType() != null)
					updateOpportunityStage.setEtcWaiverType(quoteSiteServiceTerminationDetails.getWaiverType());
				else
					updateOpportunityStage.setEtcWaiverType(StringUtils.EMPTY);
				if(quoteSiteServiceTerminationDetails.getFinalEtc() == null) {
					updateOpportunityStage.setEtcWaived(SFDCConstants.FALSE);
				}
				else {
					updateOpportunityStage.setEtcWaived(SFDCConstants.TRUE);
				}
				if(quoteSiteServiceTerminationDetails.getWaiverApprovalRemarks() != null)
					updateOpportunityStage.setEtcRemarks(quoteSiteServiceTerminationDetails.getWaiverApprovalRemarks());
				else
					updateOpportunityStage.setEtcRemarks(StringUtils.EMPTY);
			}
			if(updateOpportunityStage.getStageName() != null && !SFDCConstants.VERBAL_AGREEMENT_STAGE.equalsIgnoreCase(updateOpportunityStage.getStageName()) 
					&& !SFDCConstants.CLOSED_WON_COF_RECI.equalsIgnoreCase(updateOpportunityStage.getStageName())) {
			// setting copf like it is done in macd for now --- getting value from inventory
			List<SIServiceDetailDataBean> servicesList = null;
			servicesList = macdUtils.getServiceDetailNPLTermination(quoteIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId());
			if (servicesList != null && !servicesList.isEmpty()) {
				LOGGER.info("NPL COPF id is :::: {}", servicesList.get(0).getTpsCrmCofId());
				updateOpportunityStage.setCopfIdC(String.valueOf(servicesList.get(0).getTpsCrmCofId()));
			}
			}

		}
		}
		
		return updateOpportunityStage;
	}
	
	
	private void setPreviousMrcNrc(OpportunityBean opportunityBean, List<SIServiceDetailsBean> serviceDetailsListInput) {
		LOGGER.info("List of service details bean is ---> {} and its size is----> {} ", serviceDetailsListInput, serviceDetailsListInput.size());
		List<SIServiceDetailsBean> serviceDetailsList = new ArrayList<>();
		serviceDetailsListInput.forEach(service->{
			LOGGER.info("Entered SRV Site Type is -----> {} ", Optional.ofNullable(service.getSrvSiteType()));
			if(Objects.nonNull(service.getSrvSiteType())){
				LOGGER.info("Srv Site Type for service id ----> {} is ----> {} ", service.getTpsServiceId(), service.getSrvSiteType());
				if("SiteA".equalsIgnoreCase(service.getSrvSiteType())){
					serviceDetailsList.add(service);
				}
			}
		});
		LOGGER.info("Filtered Service Details List Bean is ----> {} and size is----> {} ", serviceDetailsList, serviceDetailsList.size() );
		Double mrc = serviceDetailsList.stream().mapToDouble(serviceDetail -> new Double(serviceDetail.getMrc())).sum();
		Double nrc = serviceDetailsList.stream().mapToDouble(serviceDetail -> new Double(serviceDetail.getNrc())).sum();
			opportunityBean.setPreviousMRC(mrc == null ? StringUtils.EMPTY
					: String.valueOf(mrc));
			opportunityBean.setPreviousNRC(nrc == null ? StringUtils.EMPTY
					: String.valueOf(nrc));
			LOGGER.info("MRC and NRC are ----> {} ---> {} ", opportunityBean.getPreviousMRC(), opportunityBean.getPreviousNRC());
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

	protected User getUserId(String username) {
		return userRepository.findByUsernameAndStatus(username, 1);
	}


}
