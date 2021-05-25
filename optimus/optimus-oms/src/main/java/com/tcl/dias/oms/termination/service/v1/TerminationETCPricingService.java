package com.tcl.dias.oms.termination.service.v1;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import com.tcl.dias.oms.entity.entities.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.CustomerAttributeBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.CustomerAttributeConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.serviceinventory.beans.SIPriceRevisionDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.enums.PriceRevisionType;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.NplLinkRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.pricing.bean.ETCPricingInputDatum;
import com.tcl.dias.oms.pricing.bean.ETCPricingRequest;
import com.tcl.dias.oms.pricing.bean.ETCPricingResponse;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

@Service
public class TerminationETCPricingService {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(TerminationETCPricingService.class);
	
	@Value("${pricing.termination.request.url}")
	String pricingTerminationUrl;	
	
	@Autowired
	RestClientService restClientService;	
	
	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Value("${rabbitmq.customer.queue}")
	String customerDetailsQueue;
	
	@Value("${rabbitmq.location.detail}")
	String locationQueue;	
	
	@Autowired
	MQUtils mqUtils;	
	
	@Autowired
	MACDUtils macdUtils;
	
	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;
	
	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;
	
	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;
	
	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;
	
	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;
	
	@Autowired
	IllSiteRepository illSiteRepository;
	
	@Autowired
	PricingDetailsRepository pricingDetailsRepository;
	
	@Autowired
	NplLinkRepository nplLinkRepository;
	
	/**
	 * retrieveETCChargesFromPricing Engine
	 * 
	 * @throws TclCommonException
	 */
	public Double retrieveETCChargesFromPricing(Integer quoteToLeId, String serviceId, 
			String terminationDate) throws TclCommonException {
		Double etcPrice=0.0;
		
		  ETCPricingRequest etcPricingRequest = constructETCPricingRequest(quoteToLeId,
		  serviceId, terminationDate); if(Objects.nonNull(etcPricingRequest)) {
		  ETCPricingResponse etcResponse = processETCPricingRequest(etcPricingRequest,
		  quoteToLeId, serviceId); if(Objects.nonNull(etcResponse) &&
		  !etcResponse.getResults().isEmpty()) etcPrice =
		  Double.valueOf(etcResponse.getResults().get(0).getEtc());
		 
		} 
		return etcPrice;
		
	}
	
	/**
	 * constructETCPricingRequest
	 * 
	 * @throws TclCommonException
	 */
	public ETCPricingRequest constructETCPricingRequest(Integer quoteToLeId, String serviceId, 
			String terminationDate) throws TclCommonException {
		
		LOGGER.info("Inside constructETCPricingRequest for service : {}", serviceId);

		ETCPricingRequest etcPricingRequest = new ETCPricingRequest();
		List<ETCPricingInputDatum> inputDataList =  new ArrayList<> ();
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);

		if (quoteToLe.isPresent()) {	
			ETCPricingInputDatum inputData = new ETCPricingInputDatum();
			String siteId = "";
			String salesOrg = "";
			String customerSegment = "";
			String prospectName = "";
			String serviceCommissionedDate = "";
			String contractExpiryDate = "";
			String oldContractTerm = "";
			String country = "";
			String city = "";
			String siteFlag = "";
			Double bustableBw = 0D;
			Double bw = 0D;
			String cpeManagementType = "full_managed";
			String suppyType = FPConstants.OUTRIGHT_SALE.toString();
			String cpeVariant = "NA";
			String quoteType = MACDConstants.TERMINATION_SERVICE;
			String prodName = quoteToLe.get().getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName();
			String productName = retrieveProductLongNameByProduct(prodName);
			
			List<QuoteLeAttributeValue> quoteLeAttributesList = quoteLeAttributeValueRepository.findByQuoteToLe(quoteToLe.get());
			Optional<QuoteLeAttributeValue> custLeName = quoteLeAttributesList.stream()
					.filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute().getName()
							.equalsIgnoreCase(LeAttributesConstants.LEGAL_ENTITY_NAME))
					.findFirst();
			if(custLeName.isPresent())
				prospectName = custLeName.get().getAttributeValue();
			
			CustomerDetailsBean customerDetails = processCustomerData(quoteToLe.get().getQuote().getCustomer().getErfCusCustomerId());
			for (CustomerAttributeBean attribute : customerDetails.getCustomerAttributes()) {
				if (attribute.getName().equals(CustomerAttributeConstants.CUSTOMER_TYPE.getAttributeValue())) {
					customerSegment = attribute.getValue();
	
				} else if (attribute.getName().equals(CustomerAttributeConstants.SALES_ORG.getAttributeValue())) {
					salesOrg = attribute.getValue();
	
				}
			}

			SIServiceDetailDataBean serviceDetail=null;
			Integer siteIde=null;
			if (prodName.startsWith("IAS")|| prodName.startsWith("GVPN")) {
				List<QuoteIllSiteToService> quoteIllSiteToServiceList = 
						quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quoteToLe.get());
				if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
					List<QuoteProductComponent> components = null;
					if(prodName.startsWith("IAS")) {
					components = quoteProductComponentRepository.findByReferenceIdAndReferenceName(
									quoteIllSiteToServiceList.get(0).getQuoteIllSite().getId(), "ILL_SITES");
					} else {
						components = quoteProductComponentRepository.findByReferenceIdAndReferenceName(
								quoteIllSiteToServiceList.get(0).getQuoteIllSite().getId(), "GVPN_SITES");
					}
					for (QuoteProductComponent quoteProductComponent : components ) {
						List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
								.findByQuoteProductComponent_Id(quoteProductComponent.getId());
		
						for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : attributes) {
							Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository
									.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());
							if (prodAttrMaster.isPresent()) {
								if (prodAttrMaster.get().getName().equals(FPConstants.BURSTABLE_BANDWIDTH.toString())) {
									if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
										bustableBw = new Double(quoteProductComponentsAttributeValue.getAttributeValues().trim());
								} else if (prodAttrMaster.get().getName().equals(FPConstants.PORT_BANDWIDTH.toString())) {
									if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
										bw = new Double(quoteProductComponentsAttributeValue.getAttributeValues().trim());
								} else if (prodAttrMaster.get().getName().equals(FPConstants.CPE_MANAGEMENT_TYPE.toString())) {
									if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
										cpeManagementType = quoteProductComponentsAttributeValue.getAttributeValues();
										if (cpeManagementType.equals("Fully Managed")) {
											cpeManagementType = "full_managed";
										} else if (cpeManagementType.equals("Physically Managed")) {
											cpeManagementType = "physical_managed";
										} else if (cpeManagementType.equals("Proactive Services")
												|| cpeManagementType.equalsIgnoreCase("Proactive Monitoring")) {
											cpeManagementType = "proactive_services";
										} else if (cpeManagementType.equals("Configuration Management")) {
											cpeManagementType = "configuration_management";
										} else if (cpeManagementType.equals("Unmanaged")) {
											cpeManagementType = "unmanaged";
										}
									}
								} else if (prodAttrMaster.get().getName().equals(FPConstants.CPE.toString())) {
									if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
										suppyType = quoteProductComponentsAttributeValue.getAttributeValues();
										if (suppyType.contains("Outright sale")) {
											suppyType = FPConstants.OUTRIGHT_SALE.toString();
										}else if(StringUtils.isEmpty(suppyType) || suppyType.equalsIgnoreCase("Customer provided")) {
											suppyType = "customer_owned";
										} else {
											suppyType = FPConstants.RENTAL.toString();
										}
									}
								} else if (prodAttrMaster.get().getName().equals(FPConstants.CPE_BASIC_CHASSIS.toString())) {
									if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
										cpeVariant = quoteProductComponentsAttributeValue.getAttributeValues();
								}
							}
						}
					}

					QuoteIllSite illSite = illSiteRepository.findByIdAndStatus(
							quoteIllSiteToServiceList.get(0).getQuoteIllSite().getId(), CommonConstants.BACTIVE);
					if( Objects.nonNull(illSite)) {

					}
					serviceDetail = macdUtils.getServiceDetailIASTermination(serviceId);
					siteIde = quoteIllSiteToServiceList.get(0).getQuoteIllSite().getId();
				}
			} else if (prodName.startsWith("NPL")|| prodName.startsWith("NDE")) {
					List<SIServiceDetailDataBean> sIServiceDetailDataBean = null;
					sIServiceDetailDataBean = macdUtils.getServiceDetailNPLTermination(serviceId);
					if (sIServiceDetailDataBean!=null) { 
						serviceDetail=sIServiceDetailDataBean.get(0);
						List<QuoteIllSiteToService> quoteIllSiteToServiceList = 
								quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quoteToLe.get());
						if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
							Set<ProductSolution> productSolutions = quoteToLe.get().getQuoteToLeProductFamilies().stream().findFirst().get().getProductSolutions();
							if(Objects.nonNull(productSolutions.stream().findFirst().get().getMstProductOffering().getProductName()) &&
									CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(productSolutions.stream().findFirst().get().getMstProductOffering().getProductName()))
							{
								siteId = quoteIllSiteToServiceList.get(0).getQuoteIllSite().getId() + "_" + PDFConstants.PRIMARY;
							}
							else {
								Optional<QuoteNplLink> quoteNplLink = nplLinkRepository.findById(quoteIllSiteToServiceList.get(0).getQuoteNplLink().getId());
								if (quoteNplLink.isPresent()) {
									siteId = quoteNplLink.get().getId() + "_" + PDFConstants.PRIMARY;
								}
							}
						}
					}
			}
			
			//Common Logic for all products
			if (Objects.nonNull(serviceDetail)) {
				LOGGER.info("Terminations : Service Details for ETC : {}", serviceDetail.toString());
				serviceCommissionedDate = serviceDetail.getsCommisionDate();
				//Override Service Commission date for PR MACD cases
				if (Boolean.TRUE.equals(PriceRevisionType.isPriceRevisedCirucit(serviceDetail.getCurrentOpportunityType()))) {
					LOGGER.info("Terminations : Inside Price Revision Logic : {}", serviceDetail.getCurrentOpportunityType());
					List<SIPriceRevisionDetailDataBean> priceRevisionDetailsList = macdUtils.getPriceRevisionDetailForTermination(serviceId);
					if(Objects.nonNull(priceRevisionDetailsList) && !priceRevisionDetailsList.isEmpty()) {
						if(serviceDetail.getCurrentOpportunityType().equalsIgnoreCase(MACDConstants.RENEWAL_WO_PRC_RVS)) {
							serviceCommissionedDate = (Objects.nonNull(priceRevisionDetailsList.get(0)) 
									? priceRevisionDetailsList.get(0).getEffDateOfPriceRevision() : serviceCommissionedDate);
						} else {
							serviceCommissionedDate = (Objects.nonNull(priceRevisionDetailsList.get(0)) 
									? priceRevisionDetailsList.get(0).getMaxPriceRevDate() : serviceCommissionedDate);
						}
						LOGGER.info("Terminations :  Commisioned Date : {} - Price Revised Date : {}", serviceDetail.getsCommisionDate(), serviceCommissionedDate);
					}
				} 
				
				Date contractExpiryDt = serviceDetail.getCircuitExpiryDate();
				if (Objects.nonNull(contractExpiryDt)) {
					contractExpiryDate = DateUtil.convertDateToString(contractExpiryDt);
				}
				oldContractTerm = serviceDetail.getContractTerm().toString();
				serviceId = serviceDetail.getTpsServiceId();			
				//Fetch Address Details
				AddressDetail addressDetail = new AddressDetail();
				try {
					String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
							String.valueOf(serviceDetail.getErfLocSiteAddressId()));
					if(locationResponse != null && !locationResponse.isEmpty()) {
						addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
								AddressDetail.class);
						if(addressDetail != null ) {
							country = addressDetail.getCountry();
							city = addressDetail.getCity();
							if (country.equals("INDIA")) {
								siteFlag = "ID";
							} else
								siteFlag = "ID";
						}
						LOGGER.info("Region for the locationId {} : {} ", serviceDetail.getErfLocSiteAddressId(), addressDetail.getRegion());
					} else {
						LOGGER.warn("Location data not found for the locationId {} ", serviceDetail.getErfLocSiteAddressId());
					}
				} catch(Exception e) {
					LOGGER.warn("constructETCPricingRequest: Error in invoking locationQueue {}", ExceptionUtils.getStackTrace(e));
				}
				if(prodName.startsWith("IAS")|| prodName.startsWith("GVPN")) {
				if(serviceDetail.getLinkType() != null && (PDFConstants.PRIMARY.equalsIgnoreCase(serviceDetail.getLinkType()) 
						|| MACDConstants.SINGLE.equalsIgnoreCase(serviceDetail.getLinkType()) ))
					siteId = siteIde + "_" + PDFConstants.PRIMARY;
				else
					siteId = siteIde + "_" + PDFConstants.SECONDARY;	
				} 

			}
			inputData.setBwMbps(String.valueOf(bw));
			inputData.setBurstableBw(String.valueOf(bustableBw));
			inputData.setCpeManagementType(cpeManagementType);
			inputData.setCpeSupplyType(suppyType);
			inputData.setCpeVariant(cpeVariant);
			inputData.setProductName(productName);
			inputData.setProspectName(prospectName);
			inputData.setCustomerSegment(customerSegment);
			inputData.setSalesOrg(salesOrg);
			inputData.setCountry(country);
			inputData.setRespCity(city);
			inputData.setSiteFlag(siteFlag);
			inputData.setServiceCommissionedDate(serviceCommissionedDate);
			inputData.setContractExpiryDate(contractExpiryDate);
			inputData.setOldContractTerm(oldContractTerm);
			inputData.setServiceId(serviceId);
			inputData.setTerminationDate(terminationDate);
			inputData.setQuotetypeQuote(quoteType);
			inputData.setSiteId(siteId);
			
			inputDataList.add(inputData);
			etcPricingRequest.setInputData(inputDataList);
		}
		return etcPricingRequest;
	}
	
	

	/**
	 * processETCPricingRequest
	 * 
	 * @throws TclCommonException
	 */
	public ETCPricingResponse processETCPricingRequest(ETCPricingRequest pricingRequest, Integer quoteToLeId, String serviceId) throws TclCommonException {
		ETCPricingResponse etcResponse = null;
		try {
			LOGGER.info("Process ETC pricing request for terminations...");
			String pricingRequestURL = pricingTerminationUrl;
			if (!pricingRequest.getInputData().isEmpty()) {
				String request = Utils.convertObjectToJson(pricingRequest);
				LOGGER.info("Pricing input :: {}", request);
				LOGGER.info("Pricing request Called for {} in {}",quoteToLeId, new Date());
				RestResponse pricingResponse = restClientService.post(pricingRequestURL, request);
				if (pricingResponse.getStatus() == Status.SUCCESS) {
					String response = pricingResponse.getData();
					LOGGER.info("Pricing output :: {}", response);
					response = response.replaceAll("NaN", "0");
					etcResponse = (ETCPricingResponse) Utils.convertJsonToObject(response, ETCPricingResponse.class);
					persistETCPricingDetails(quoteToLeId, pricingRequest, etcResponse, MACDConstants.TERMINATION_SERVICE, serviceId);
				}
			}
		} catch (TclCommonException e) {
			LOGGER.error("Error in Termination ETC pricing engine request - {} - {}",e.getMessage(), e);
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return etcResponse;
	}	
	
	public CustomerDetailsBean processCustomerData(Integer customerId) throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call processCustomerData {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String customerResponse = (String) mqUtils.sendAndReceive(customerDetailsQueue, String.valueOf(customerId));
		return (CustomerDetailsBean) Utils.convertJsonToObject(customerResponse, CustomerDetailsBean.class);

	}	
	
	private String retrieveProductLongNameByProduct(String prodName) {
		String productName = null;
		switch(prodName) {
			case "IAS":
				productName=FPConstants.IAS_ILL.toString();
				break;
			case "GVPN":
				productName=FPConstants.GLOBAL_VPN.toString();
				break;
			case "NPL":
				productName=FPConstants.NATIONAL_PRIVATE_LINES.toString();
				break;
			case "NDE":
				productName=FPConstants.NATIONAL_DEDICATED_ETHERNET.toString();
				break;
			default:
				productName=FPConstants.IAS_ILL.toString();
		}
		return productName;
	}
	
	/**
	 * persist ETC PricingDetails
	 * 
	 * @param presult
	 * @param type
	 * @param illSite
	 * @throws TclCommonException
	 */
	private void persistETCPricingDetails(Integer quoteToLeId, ETCPricingRequest prequest, ETCPricingResponse presult, String type, String serviceId) 
			throws TclCommonException {

		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		if (quoteToLe.isPresent()) {	
			LOGGER.info("Persist ETC details : "+quoteToLe);
			String prodName = quoteToLe.get().getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName();
			if (prodName.startsWith("IAS")|| prodName.startsWith("GVPN")) {
				LOGGER.info("Input : {} - {} - {} ", prodName, type, serviceId);
				List<QuoteIllSiteToService> quoteIllSiteToServiceList = 
						quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quoteToLe.get());
				LOGGER.info("Ill Site to Service : {}  ", quoteIllSiteToServiceList);
				if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
					quoteIllSiteToServiceList.stream().filter(Objects::nonNull).forEach(service -> {
						LOGGER.info("Service : {}  ", service);
						try {
						List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
						.findBySiteCodeAndPricingType(service.getQuoteIllSite().getSiteCode(), type);
						LOGGER.info("Pricing Details : {} ", pricingDetails);
						if (pricingDetails.isEmpty()) {
							LOGGER.info("Pricing Does not exist");
							PricingEngineResponse pricingDetail = new PricingEngineResponse();
							pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
							pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
							pricingDetail.setPricingType(type);
							pricingDetail.setRequestData(Utils.convertObjectToJson(prequest.getInputData().get(0)));
							pricingDetail.setResponseData(Utils.convertObjectToJson(presult.getResults().get(0)));
							pricingDetail.setSiteCode(service.getQuoteIllSite().getSiteCode());
							pricingDetailsRepository.save(pricingDetail);
						} else {
							LOGGER.info("Pricing Exists : {} ", pricingDetails);
							for (PricingEngineResponse pricingDetail : pricingDetails) {
								pricingDetail.setResponseData(Utils.convertObjectToJson(presult.getResults().get(0)));
								pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
								pricingDetailsRepository.save(pricingDetail);
							}
						}
						} catch (TclCommonException e) {
							LOGGER.error("Termination : Error in Persisting ETC response to Pricing Engine Response Table - {} - {}",e.getMessage(), e);
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
						}
					});
				}
			} else if (prodName.startsWith("NPL")|| prodName.startsWith("NDE")) {

				LOGGER.info("Input : {} - {} - {} ", prodName, type, serviceId);
				List<QuoteIllSiteToService> quoteIllSiteToServiceList = 
						quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quoteToLe.get());
				LOGGER.info("Ill Site to Service : {}  ", quoteIllSiteToServiceList);
				if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
					quoteIllSiteToServiceList.stream().filter(Objects::nonNull).forEach(service -> {
						LOGGER.info("Service : {}  ", service);
						try {
							String sitecode="";
							Set<ProductSolution> productSolutions = quoteToLe.get().getQuoteToLeProductFamilies().stream().findFirst().get().getProductSolutions();

							if(Objects.nonNull(productSolutions.stream().findFirst().get().getMstProductOffering().getProductName()) &&
									CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(productSolutions.stream().findFirst().get().getMstProductOffering().getProductName()))
							{
								sitecode = service.getQuoteIllSite().getSiteCode();

							}else {
								sitecode = service.getQuoteNplLink().getLinkCode();
							}
							LOGGER.info("SiteCode  : {}",sitecode);

							List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
									.findBySiteCodeAndPricingType(sitecode, type);

						LOGGER.info("Pricing Details : {} ", pricingDetails);
						if (pricingDetails.isEmpty()) {
							LOGGER.info("Pricing Does not exist");
							PricingEngineResponse pricingDetail = new PricingEngineResponse();
							pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
							pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
							pricingDetail.setPricingType(type);
							pricingDetail.setRequestData(Utils.convertObjectToJson(prequest.getInputData().get(0)));
							pricingDetail.setResponseData(Utils.convertObjectToJson(presult.getResults().get(0)));
							pricingDetail.setSiteCode(sitecode);
							pricingDetailsRepository.save(pricingDetail);
						} else {
							LOGGER.info("Pricing Exists : {} ", pricingDetails);
							for (PricingEngineResponse pricingDetail : pricingDetails) {
								pricingDetail.setResponseData(Utils.convertObjectToJson(presult.getResults().get(0)));
								pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
								pricingDetailsRepository.save(pricingDetail);
							}
						}
						} catch (TclCommonException e) {
							LOGGER.error("Termination : Error in Persisting ETC response to Pricing Engine Response Table - {} - {}",e.getMessage(), e);
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
						}
					});
				}
			
			}
			
			// throw error if error flag is set in the pricing engine response.
			if(presult != null && presult.getResults() != null ) {
				if(presult.getResults().get(0).getErrorFlag() != null && !"NA".equalsIgnoreCase(presult.getResults().get(0).getErrorFlag()) && !"0".equalsIgnoreCase(presult.getResults().get(0).getErrorFlag())) {
					throw new TclCommonRuntimeException(ExceptionConstants.ETC_PRICING_ERROR, ResponseResource.R_CODE_ERROR);
					
				}
			}
		}
	}

}
