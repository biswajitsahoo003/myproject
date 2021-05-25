package com.tcl.dias.oms.renewals.serviceV1;

import static com.tcl.dias.common.constants.CommonConstants.BDEACTIVATE;
import static com.tcl.dias.common.constants.CommonConstants.PARTNER;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.SolutionDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.CommercialQuoteAudit;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.DocusignAudit;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.Opportunity;
import com.tcl.dias.oms.entity.entities.OrderConfirmationAudit;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.ProductSolutionSiLink;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteAccessPermission;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.UpdateGstRequest;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.CommercialQuoteAuditRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.OpportunityRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionSiLinkRepository;
import com.tcl.dias.oms.entity.repository.QuoteAccessPermissionRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityAuditRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.UpdateGstRequestRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.renewals.bean.RenevalsIllSiteBean;
import com.tcl.dias.oms.renewals.bean.RenevalsQuoteTotalBean;
import com.tcl.dias.oms.renewals.bean.RenewalsAttributeDetail;
import com.tcl.dias.oms.renewals.bean.RenewalsComponentDetail;
import com.tcl.dias.oms.renewals.bean.RenewalsConstant;
import com.tcl.dias.oms.renewals.bean.RenewalsExcelBean;
import com.tcl.dias.oms.renewals.bean.RenewalsPriceBean;
import com.tcl.dias.oms.renewals.bean.RenewalsProductsolutionLinkMap;
import com.tcl.dias.oms.renewals.bean.RenewalsQuoteDetail;
import com.tcl.dias.oms.renewals.bean.RenewalsSfdcObjectBean;
import com.tcl.dias.oms.renewals.bean.RenewalsSite;
import com.tcl.dias.oms.renewals.bean.RenewalsSiteDetail;
import com.tcl.dias.oms.renewals.bean.RenewalsSolutionDetail;
import com.tcl.dias.oms.renewals.service.RenewalsExcelPriceMapping;
import com.tcl.dias.oms.renewals.service.RenewalsService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

@Service
public class RenewalsServiceCommonGvpn {

	@Value("${rabbitmq.renewals.si.validate.details.queue}")
	String siValidateQueue;

	@Value("${rabbitmq.renewals.si.details.queue}")
	String siOrderDetailsQueue;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	protected OmsSfdcService omsSfdcService;
	@Autowired
	protected QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;
	@Autowired
	QuoteAccessPermissionRepository quoteAccessPermissionRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	protected QuoteRepository quoteRepository;

	@Autowired
	protected QuoteToLeRepository quoteToLeRepository;
	@Autowired
	protected QuoteProductComponentRepository quoteProductComponentRepository;
	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;
	@Autowired
	protected MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	PartnerService partnerService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	protected QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	protected IllSiteRepository illSiteRepository;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	OpportunityRepository opportunityRepository;

	@Autowired
	protected ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;

	@Autowired
	QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;

	@Autowired
	SiteFeasibilityRepository siteFeasibilityRepository;

	@Autowired
	SiteFeasibilityAuditRepository siteFeasibilityAuditRepository;

	@Autowired
	MstProductOfferingRepository mstProductOfferingRepository;

	@Autowired
	private CommercialQuoteAuditRepository commercialQuoteAuditRepository;

	@Autowired
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@Autowired
	protected CofDetailsRepository cofDetailsRepository;

	@Autowired
	protected DocusignAuditRepository docusignAuditRepository;

	@Autowired
	QuotePriceRepository quotePriceRepository;

	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;

	@Autowired
	ProductSolutionSiLinkRepository productSolutionSiLinkRepository;

	@Autowired
	RenewalsExcelPriceMappingV1 renewalsExcelPriceMapping;
	
	@Autowired
	GvpnQuoteService gvpnQuoteService;
	
	@Autowired
	UpdateGstRequestRepository updateGstRequestRepository;

	public static final Logger LOGGER = LoggerFactory.getLogger(RenewalsServiceCommonGvpn.class);

	public RenewalsQuoteDetail processExcel(Integer leId, Integer custId, String product, Integer term,
			Character commercial, String date,String oppId, XSSFWorkbook workbook) throws TclCommonException, ParseException {

		User user = getUserId(Utils.getSource());
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(RenewalsConstant.GVPN,
				(byte) 1);
		LOGGER.info("Processing Excel leId-->{} Customer Id -->{}  Product --> {} Contract Term -->{}", leId, custId,
				product, term);
		List<String> serviceIdList = new ArrayList<String>();
		Map<String, Map<String, Object>> serviceIdPriceMapping = null;
		XSSFSheet worksheet = workbook.getSheetAt(0);

		for (int i = 3; i < worksheet.getPhysicalNumberOfRows(); i++) {
			XSSFRow row = worksheet.getRow(i);
			DataFormatter formatter = new DataFormatter();
			String val = formatter.formatCellValue(row.getCell(0));
			if (val != null & val != "") {
				serviceIdList.add(val);
			}
		}

		RenewalsQuoteDetail rQuoteDetail = getServiceDetails(serviceIdList, commercial);

		long  startTime = System.currentTimeMillis();
		long temp;
		 LOGGER.info("String create Quote Excel");
		 
		
		if (commercial.equals('Y')) {
			 LOGGER.info("construct price excel");
			serviceIdPriceMapping = renewalsExcelPriceMapping.constructPriceGvpn(workbook);
			LOGGER.info("insert data");
			rQuoteDetail = compareAndInsertObject(serviceIdPriceMapping, rQuoteDetail);
		}else {
			 LOGGER.info("construct price excel");
				serviceIdPriceMapping = renewalsExcelPriceMapping.constructPriceGvpn(workbook);
		}

		 LOGGER.info("create quote");
		RenewalsProductsolutionLinkMap quoteResponse = createQuote(rQuoteDetail, rQuoteDetail.getCustomerId(), false,
				term, commercial, serviceIdPriceMapping, oppId);
		QuoteToLe quoteTole = quoteResponse.getQuoteTole();
		Integer quoteId = quoteTole.getQuote().getId();
		Integer quoteToleId = quoteTole.getId();
		 LOGGER.info("construct SiteObject");
		List<QuoteIllSite> quteIllSiteList = constructSiteObject(rQuoteDetail, user);
		Double previousMrc = calculatePreviousMrc(quteIllSiteList,  rQuoteDetail);
		LOGGER.info("construct component attributes");
		Map<String, List<QuoteProductComponent>> serviceIdComponentMapping = constructComponentAttributes(rQuoteDetail,
				mstProductFamily, user);
		LOGGER.info("construct additional attributes");
		List<RenewalsAttributeDetail> listAttributes = addAditionalAttributes(rQuoteDetail.getQuoteAttributeList(),
				commercial, date);
		LOGGER.info("construct le attributes");
		List<QuoteLeAttributeValue> listOfAttributes = constructListOfQuoteLeAttributes(listAttributes, user,
				quoteToleId);
		LOGGER.info("persist Legal Attributes strats");
		persistLegalAttributes(listOfAttributes);
		LOGGER.info("persist Legal Attributes end");
		
		LOGGER.info("persist ill sites strats");
		List<QuoteIllSite> quteIllSiteListWithId = persistingIllSites(quteIllSiteList, serviceIdComponentMapping,
				quoteResponse, rQuoteDetail, commercial, term, leId);
		LOGGER.info("persist ill sites end");
		
		LOGGER.info("persist ill component starts");
		persistComponent(quteIllSiteListWithId, serviceIdComponentMapping);
		LOGGER.info("persist ill component end");
		
		if (commercial.equals('Y')) {
			LOGGER.info("construct price");
			RenevalsIllSiteBean quotePrice = constructQutePrice(quteIllSiteListWithId, serviceIdPriceMapping,
					mstProductFamily, quoteId);
			LOGGER.info("construct ends");
			LOGGER.info("persist price starts");
			persistingPrice(quotePrice, quteIllSiteListWithId, quoteTole, term, leId, commercial);
			LOGGER.info("persist price end");
		}
		rQuoteDetail.setQuoteId(quoteTole.getQuote().getId());
		rQuoteDetail.setQuoteleId(quoteTole.getId());
		long endTime = System.currentTimeMillis();
		
		RenewalsSfdcObjectBean renewalsSfdcObjectBean = new RenewalsSfdcObjectBean();
		renewalsSfdcObjectBean.setServiceIdList(serviceIdList);
		renewalsSfdcObjectBean.setEffectiveDate(date);
		Optional<RenewalsSolutionDetail> copfId = rQuoteDetail.getSolutions().stream().findFirst().filter(x->x.getCopfId()!=null);
		if(copfId.isPresent()) {
		renewalsSfdcObjectBean.setCopfId(copfId.get().getCopfId());
		}
		renewalsSfdcObjectBean.setPreviousMrc(previousMrc);
		omsSfdcService.processCreateRenewalsOpty(quoteTole, product, commercial, oppId, renewalsSfdcObjectBean);
	//	omsSfdcService.processCreateRenewalsOpty(quoteTole, product, commercial, oppId, renewalsSfdcObjectBean);
		
		 LOGGER.info("time taken = "+ (endTime - startTime) / (1000));
			orderFormStage(quoteTole, leId);
//			List<UpdateRequest> requestList=  quoteResponse.getUpdateList();
//			for( int i=0; i<requestList.size() ;i++) {
//				requestList.get(i).setSiteId(quteIllSiteListWithId.get(i).getId());
//				gvpnQuoteService.updateSiteProperties(requestList.get(i));
//			}
			List<UpdateGstRequest> updateGstRequests = new ArrayList<UpdateGstRequest>();

			List<UpdateGstRequest> requestList = quoteResponse.getUpdateList();
			for (int i = 0; i < requestList.size(); i++) {
				requestList.get(i).setSiteId(quteIllSiteListWithId.get(i).getId());
				requestList.get(i).setUserName(Utils.getSource());
				// illQuoteService.updateSiteProperties(requestList.get(i));
			}
			updateGstRequestRepository.saveAll(requestList);
		return rQuoteDetail;
	}

	public void orderFormStage(QuoteToLe quoteTole, Integer leId) {
		quoteTole.setErfCusCustomerLegalEntityId(leId);
		quoteTole.setStage(QuoteStageConstants.ORDER_FORM.getConstantCode());
		quoteToLeRepository.save(quoteTole);
	}
	
	public void persistingPrice(RenevalsIllSiteBean quotePrice, List<QuoteIllSite> quteIllSiteListWithId,
			QuoteToLe quoteTole, Integer term, Integer leId, Character commercial) {

		if (!quotePrice.getQuotePriceList().isEmpty()) {
			quotePriceRepository.saveAll(quotePrice.getQuotePriceList());
		}

		Map<Integer, RenewalsPriceBean> illSitePrice = quotePrice.getIllSitePrice();
		List<QuoteIllSite> illSitePriceUpdate = new ArrayList<QuoteIllSite>();
		for (QuoteIllSite illSite : quteIllSiteListWithId) {
			RenewalsPriceBean priceBean = illSitePrice.get(illSite.getId());
			if (priceBean != null) {
				if (commercial.equals('Y')) {
				illSite.setMrc(priceBean.getMrc());
				illSite.setNrc(priceBean.getNrc());
				illSite.setArc(priceBean.getArc());
				illSite.setTcv(calculateTcv(roundOff(priceBean.getArc()),roundOff(priceBean.getNrc()), term));
				illSitePriceUpdate.add(illSite);
				}
			}
		}
		if (!illSitePriceUpdate.isEmpty()) {
			illSiteRepository.saveAll(illSitePriceUpdate);
		}
		if (commercial.equals('Y')) {
		quoteTole.setFinalMrc(roundOff(quotePrice.getTotalMrc()));
		quoteTole.setFinalNrc(roundOff(quotePrice.getTotalNrc()));
		quoteTole.setFinalArc(roundOff(quotePrice.getTotalArc()));
		quoteTole.setTotalTcv(calculateTcv(roundOff(quotePrice.getTotalArc()),roundOff(quotePrice.getTotalNrc()), term));
		quoteTole.setErfCusCustomerLegalEntityId(leId);
		quoteToLeRepository.save(quoteTole);
		}

	}

	public List<QuoteIllSite> persistingIllSites(List<QuoteIllSite> quteIllSiteList,
			Map<String, List<QuoteProductComponent>> serviceIdComponentMapping,
			RenewalsProductsolutionLinkMap quoteResponse, RenewalsQuoteDetail rQuoteDetail, Character commercial,
			Integer term, Integer leId) {

		Double totalmrc = 0D;
		Double totalnrc = 0D;
		Double totalarc = 0D;
		Map<String, ProductSolution> productsolutionLinkMap = quoteResponse.getProductsolutionLinkMap();
		Map<String, RenewalsPriceBean> renewalsPriceBean = rQuoteDetail.getRenewalsPriceBean();
		int i = 0;
		for (QuoteIllSite illsite : quteIllSiteList) {
			if (commercial.equals('N')) {
				RenewalsPriceBean price = renewalsPriceBean.get(illsite.getErfServiceInventoryTpsServiceId());
				Double mrc = price.getMrc();
				//Double nrc = price.getNrc();
				Double arc = price.getArc();
				totalmrc = roundOff(totalmrc + (mrc != null ? mrc : 0D));
				totalnrc = 0D;
				totalarc = roundOff(totalarc + (arc != null ? arc : 0D));
				illsite.setMrc(mrc);
				illsite.setNrc(0D);
				illsite.setArc(arc);
				illsite.setTcv(roundOff(calculateTcv(arc,0D, term)));

			}
			ProductSolution productSolution = productsolutionLinkMap.get(illsite.getErfServiceInventoryTpsServiceId());
			illsite.setProductSolution(productSolution);
			quteIllSiteList.set(i, illsite);
			i++;
		}
		quteIllSiteList = illSiteRepository.saveAll(quteIllSiteList);

		if (commercial.equals('N')) {
			QuoteToLe quoteTole = quoteResponse.getQuoteTole();
			quoteTole.setFinalMrc(totalmrc);
			quoteTole.setFinalNrc(0D);
			quoteTole.setFinalArc(totalarc);
			quoteTole.setTotalTcv(roundOff(calculateTcv(totalarc,0D, term)));
			quoteTole.setErfCusCustomerLegalEntityId(leId);
			quoteToLeRepository.save(quoteTole);
		}
		return quteIllSiteList;
	}

	public Double calculateTcv(Double arc,Double nrc, Integer term) {
		if (arc != null && arc != 0D) {
			return (((((arc / 12) * term)+nrc))*100)/100;
		} else {
			return 0D;
		}
	}
	public double roundOff(double value) {
		if(value!=0D) {
		return (value*100)/100;
		}
		return 0D;
	}
	public void persistComponent(List<QuoteIllSite> quteIllSiteList,
			Map<String, List<QuoteProductComponent>> serviceIdComponentMapping) {

		List<QuoteProductComponent> quoteProductComponentListToSave = new ArrayList<QuoteProductComponent>();

		for (QuoteIllSite illsite : quteIllSiteList) {
			List<QuoteProductComponent> quoteProductComponentList = serviceIdComponentMapping
					.get(illsite.getErfServiceInventoryTpsServiceId());
			if (quoteProductComponentList != null && !quoteProductComponentList.isEmpty()) {
				for (QuoteProductComponent component : quoteProductComponentList) {
					component.setReferenceId(illsite.getId());
					quoteProductComponentListToSave.add(component);
				}
			}
		}
		quoteProductComponentListToSave = quoteProductComponentRepository.saveAll(quoteProductComponentListToSave);
		Set<QuoteProductComponentsAttributeValue>  attributesToSave = new HashSet<QuoteProductComponentsAttributeValue>();
		for(QuoteProductComponent component: quoteProductComponentListToSave) {	
			Set<QuoteProductComponentsAttributeValue>  attributes = component.getQuoteProductComponentsAttributeValues();
			if(attributes!=null && !attributes.isEmpty()) {
				attributesToSave.addAll(attributes);
			}		
			
		}
		quoteProductComponentsAttributeValueRepository.saveAll(attributesToSave);
	}

	public void persistLegalAttributes(List<QuoteLeAttributeValue> listOfAttributes) {
		quoteLeAttributeValueRepository.saveAll(listOfAttributes);
	}

	public RenewalsQuoteDetail getServiceDetails(List<String> tpsId, Character commdercial) throws TclCommonException {

		LOGGER.info("Inside getServiceDetail to fetch SIServiceDetailDataBean for serviceid {} ", tpsId);
		String completeQuoteString = (String) mqUtils.sendAndReceive(siOrderDetailsQueue, tpsId.toString(), 300000); // todo
																														// need
																														// to
																														// fix
		if (completeQuoteString.isEmpty()) {
			throw new TclCommonRuntimeException(RenewalsConstant.SI_NO_DATA_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		if (Objects.nonNull(completeQuoteString)) {
			LOGGER.info("Empty Data Received From Service Invontory While Processing Excel");
			LOGGER.info("SIServiceDetailDataBean string queue response for s id ---> {} is ---> {}",
					completeQuoteString, tpsId);
		}
		RenewalsQuoteDetail completeQuote = (RenewalsQuoteDetail) Utils.convertJsonToObject(completeQuoteString,
				RenewalsQuoteDetail.class);

		if (completeQuote.getSolutions().isEmpty() && completeQuote.getOrderLeIds().isEmpty()
				&& completeQuote.getSite().isEmpty()) {
			LOGGER.info("Empty Array Received From Service Invontory While Processing Excel");
			throw new TclCommonRuntimeException(RenewalsConstant.SI_NO_DATA_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}

		completeQuote = markPrimaryOrSecondary(completeQuote, commdercial);
		return completeQuote;
	}

	public RenewalsQuoteDetail markPrimaryOrSecondary(RenewalsQuoteDetail quoteDetail, Character commercial)
			throws TclCommonException {
		LOGGER.info("Inside the Method desidePrimaryOrSecondary");
		for (int i = 0; i < quoteDetail.getSolutions().size(); i++) {
			quoteDetail.getSolutions().get(i)
					.setOfferingName(getOfferingName(quoteDetail.getSolutions().get(i).getComponents()));
//			HashMap<String, Integer> map = new HashMap<String, Integer>();
//			for (int j = 0; j < quoteDetail.getSolutions().get(i).getComponents().size(); j++) {
//
//				if (map.containsKey(quoteDetail.getSolutions().get(i).getComponents().get(j).getName())) {
//					quoteDetail.getSolutions().get(i).getComponents().get(j).setType(RenewalsConstant.SECONDARY);
//				} else {
//					quoteDetail.getSolutions().get(i).getComponents().get(j).setType(RenewalsConstant.PRIMARY);
//					map.put(quoteDetail.getSolutions().get(i).getComponents().get(j).getName(), 1);
//				}
//
//			}

		}
		LOGGER.info("Completed the Method desidePrimaryOrSecondary, quoteDetails -->{}", quoteDetail);
		return quoteDetail;
	}

	public String getOfferingName(List<RenewalsComponentDetail> listOfComponents) {
		boolean resiliencyPresent = false;
		boolean cpeManged = false;
		for (RenewalsComponentDetail component : listOfComponents) {

			for (RenewalsAttributeDetail attribute : component.getAttributes()) {
				if (attribute.getName().equalsIgnoreCase("Resiliency")
						&& attribute.getValue().equalsIgnoreCase("YES")) {
					resiliencyPresent = true;
				}
				if (attribute.getName().equalsIgnoreCase("CPE")
						&& !attribute.getValue().equalsIgnoreCase("customer provided")) {
					cpeManged = true;
				}
				if (attribute.getName().equalsIgnoreCase("CPE Management Type")
						&& attribute.getName().equalsIgnoreCase("proactive monitoring")) {
					cpeManged = true;
				}
				if (attribute.getName().equalsIgnoreCase("CPE Management Type")
						&& attribute.getName().equalsIgnoreCase("configuration management")) {
					cpeManged = true;
				}
			}

		}
		if (resiliencyPresent && cpeManged) {
			return "Dual managed GVPN";
		} else if (!resiliencyPresent && cpeManged) {
			return "Single managed GVPN";
		} else if (resiliencyPresent && !cpeManged) {
			return "Dual Unmanaged GVPN";
		} else if (!resiliencyPresent && !cpeManged) {
			return "Single Unmanaged GVPN";
		} else {
			return "Custom Configuration";
		}

	}

	public RenewalsProductsolutionLinkMap createQuote(RenewalsQuoteDetail quoteDetail, Integer erfCustomerId,
			Boolean ns, Integer term, Character isCommercial, Map<String, Map<String, Object>> serviceIdPriceMapping, String oppId) throws TclCommonException {
		QuoteResponse response = new QuoteResponse();
		RenewalsProductsolutionLinkMap combinedresponse = new RenewalsProductsolutionLinkMap();
		try {
			if (ns == null) {
				ns = false;
			}

			User user = getUserId(Utils.getSource());
			combinedresponse = processQuote(quoteDetail, erfCustomerId, user, ns, term, isCommercial, serviceIdPriceMapping);
			QuoteToLe quoteTole = combinedresponse.getQuoteTole();
			persistQuoteLeAttributes(user, quoteTole);
			if (quoteTole != null) {
				response.setQuoteleId(quoteTole.getId());
				response.setQuoteId(quoteTole.getQuote().getId());
				if (quoteDetail.getQuoteId() == null && Objects.isNull(quoteDetail.getEngagementOptyId())) {

//					omsSfdcService.processCreateRenewalsOpty(quoteTole, quoteDetail.getProductName(), isCommercial, oppId);
				}
			}
			processQuoteAccessPermissions(user, quoteTole);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return combinedresponse;
	}

	protected User getUserId(String username) {
		return userRepository.findByUsernameAndStatus(username, 1);
	}

	protected MstProductOffering getProductOffering(MstProductFamily mstProductFamily, String productOfferingName,
			User user) throws TclCommonException {
		MstProductOffering productOffering = null;

		productOffering = mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatus(mstProductFamily,
				productOfferingName, (byte) 1);
		if (productOffering == null) {
			productOffering = new MstProductOffering();
			productOffering.setCreatedBy(user.getUsername());
			productOffering.setCreatedTime(new Date());
			productOffering.setMstProductFamily(mstProductFamily);
			productOffering.setProductName(productOfferingName);
			productOffering.setStatus((byte) 1);
			productOffering.setProductDescription(productOfferingName);
			mstProductOfferingRepository.save(productOffering);

		}
		return productOffering;
	}

	private Customer getCustomerId(Integer erfCustomerId) throws TclCommonException {
		Customer customer = customerRepository.findByErfCusCustomerIdAndStatus(erfCustomerId, (byte) 1);
		if (customer == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
		}
		return customer;

	}

	private QuoteToLe constructQuoteToLe(Quote quote, RenewalsQuoteDetail quoteDetail, Integer term,
			Character isCommercial) {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setQuote(quote);
		quoteToLe.setStage(QuoteStageConstants.SELECT_CONFIGURATION.getConstantCode());
		quoteToLe.setTermInMonths(term + RenewalsConstant.MONTH);
		quoteToLe.setCurrencyCode(RenewalsConstant.INR);
		quoteToLe.setQuoteType(RenewalsConstant.RENEWALS);
		quoteToLe.setIsAmended(BDEACTIVATE);
		quoteToLe.setClassification(quoteDetail.getClassification());
		quoteToLe.setIsMultiCircuit(BDEACTIVATE);
		// quoteToLe.setIsCommercialChanges(isCommercial);
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			Opportunity opportunity = opportunityRepository.findByUuid(quote.getQuoteCode());
			quoteToLe.setTpsSfdcOptyId(opportunity.getTpsOptyId());
			quoteToLe.setErfCusCustomerLegalEntityId(partnerService
					.getCustomerLeIdFromEngagementOpportunityId(Integer.valueOf(quoteDetail.getEngagementOptyId())));
		}
		return quoteToLe;
	}

	private Quote constructQuote(Customer customer, Integer userId, String productName, String engagementOptyId,
			String quoteCode, Boolean ns, Integer term) {
		Quote quoteExisting = quoteRepository.findByQuoteCode(quoteCode);
		Quote quote = new Quote();
		if (quoteExisting != null) {
			quote = quoteExisting;
		}
		quote.setCustomer(customer);
		quote.setCreatedBy(userId);
		quote.setCreatedTime(new Date());
		quote.setStatus((byte) 1);
		quote.setNsQuote(ns ? CommonConstants.Y : CommonConstants.N);
		quote.setQuoteCode(null != engagementOptyId ? quoteCode : Utils.generateRefId(productName.toUpperCase()));
		quote.setEngagementOptyId(engagementOptyId);
		quote.setTermInMonths(term);
		return quote;
	}

	protected MstProductFamily getProductFamily(String productName) throws TclCommonException {
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(productName, (byte) 1);
		if (mstProductFamily == null) {
			throw new TclCommonException(ExceptionConstants.PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return mstProductFamily;

	}

	private void processQuoteAccessPermissions(User user, QuoteToLe quoteTole) {
		Integer prodFamilyId = null;
		List<QuoteToLeProductFamily> quoteToLeProductFamilys = quoteToLeProductFamilyRepository
				.findByQuoteToLe(quoteTole.getId());
		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteToLeProductFamilys) {
			prodFamilyId = quoteToLeProductFamily.getMstProductFamily().getId();
		}
		QuoteAccessPermission quoteAccessPermission = null;
		List<QuoteAccessPermission> quoteAccessPermissions = quoteAccessPermissionRepository
				.findByProductFamilyIdAndTypeAndRefId(prodFamilyId, "QUOTE", quoteTole.getQuote().getQuoteCode());
		if (!quoteAccessPermissions.isEmpty()) {
			quoteAccessPermission = quoteAccessPermissions.get(0);
		} else {
			quoteAccessPermission = new QuoteAccessPermission();
		}
		Quote quote=quoteTole.getQuote();
		quoteAccessPermission.setCreatedBy(Utils.getSource());
		quoteAccessPermission.setCreatedTime(new Date());
		quoteAccessPermission.setIsCustomerView(CommonConstants.BACTIVE);
		quoteAccessPermission.setIsSalesView(CommonConstants.BACTIVE);
		quote.setIsCustomerView(CommonConstants.BACTIVE);
		quote.setIsSalesView(CommonConstants.BACTIVE);
		quoteAccessPermission.setProductFamilyId(prodFamilyId);
		quoteAccessPermission.setRefId(quoteTole.getQuote().getQuoteCode());
		quoteAccessPermission.setType(RenewalsConstant.QUOTE);
		quoteAccessPermission.setUpdatedBy(Utils.getSource());
		quoteAccessPermission.setUpdatedTime(new Date());
		if (user.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
			quoteAccessPermission.setIsCustomerView(CommonConstants.BDEACTIVATE);
			quoteAccessPermission.setIsSalesView(CommonConstants.BACTIVE);
			quote.setIsCustomerView(CommonConstants.BDEACTIVATE);
			quote.setIsSalesView(CommonConstants.BACTIVE);
		} else if (user.getUserType().equalsIgnoreCase(UserType.CUSTOMER.toString())) {
			quoteAccessPermission.setIsCustomerView(CommonConstants.BACTIVE);
			quoteAccessPermission.setIsSalesView(CommonConstants.BDEACTIVATE);
			quote.setIsCustomerView(CommonConstants.BACTIVE);
			quote.setIsSalesView(CommonConstants.BDEACTIVATE);
		}
		quoteRepository.save(quote);
		quoteAccessPermissionRepository.save(quoteAccessPermission);
	}

	private QuoteToLeProductFamily constructQuoteToLeProductFamily(MstProductFamily mstProductFamily,
			QuoteToLe quoteToLe) {
		QuoteToLeProductFamily quoteToLeProductFamily = new QuoteToLeProductFamily();
		quoteToLeProductFamily.setMstProductFamily(mstProductFamily);
		quoteToLeProductFamily.setQuoteToLe(quoteToLe);
		return quoteToLeProductFamily;

	}

	private ProductSolution constructProductSolution(MstProductOffering mstProductOffering,
			QuoteToLeProductFamily quoteToLeProductFamily, String productProfileData) {
		ProductSolution productSolution = new ProductSolution();
		productSolution.setMstProductOffering(mstProductOffering);
		productSolution.setQuoteToLeProductFamily(quoteToLeProductFamily);
		productSolution.setProductProfileData(productProfileData);
		return productSolution;
	}

	protected RenewalsProductsolutionLinkMap processQuote(RenewalsQuoteDetail quoteDetail, Integer erfCustomerId,
			User user, Boolean ns, Integer term, Character isCommercial, Map<String, Map<String, Object>> serviceIdPriceMapping) throws TclCommonException {
		Customer customer = null;
		if (erfCustomerId != null) {
			customer = getCustomerId(erfCustomerId);// get the customer Id
		} else {
			customer = user.getCustomer();
		}
		Quote quote = null;

		if (quoteDetail.getQuoteleId() == null && quoteDetail.getQuoteId() == null) {
			quote = constructQuote(customer, user.getId(), quoteDetail.getProductName(),
					quoteDetail.getEngagementOptyId(), quoteDetail.getQuoteCode(), ns, term);
			quoteRepository.save(quote);
		}
		QuoteToLe quoteToLe = null;
		if (quoteDetail.getQuoteId() == null) {
			quoteToLe = constructQuoteToLe(quote, quoteDetail, term, isCommercial);
			quoteToLeRepository.save(quoteToLe);
		} else {
			Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteDetail.getQuoteleId());
			quoteToLe = quoteToLeEntity.isPresent() ? quoteToLeEntity.get() : null;
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				quoteToLe.setClassification(quoteDetail.getClassification());
				quoteToLe = quoteToLeRepository.save(quoteToLe);
			}
		}

		MstProductFamily productFamily = getProductFamily(quoteDetail.getProductName());
		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
				.findByQuoteToLeAndMstProductFamily(quoteToLe, productFamily);
		if (quoteToLeProductFamily == null) {
			quoteToLeProductFamily = constructQuoteToLeProductFamily(productFamily, quoteToLe);
			quoteToLeProductFamilyRepository.save(quoteToLeProductFamily);
		} /*
			 * else { removeUnselectedSolution(quoteDetail, quoteToLeProductFamily,
			 * quoteToLe); }
			 */

		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			quoteToLe.setQuoteToLeProductFamilies(
					Arrays.asList(quoteToLeProductFamily).stream().collect(Collectors.toSet()));
		}
		Map<String, ProductSolution> productsolutionLinkMap = new HashMap<String, ProductSolution>();
		List<ProductSolution> productSolutionList = new ArrayList<ProductSolution>();
		List<ProductSolutionSiLink> productSolutionSiLinkList = new ArrayList<ProductSolutionSiLink>();
		List<String> serviceIds = new ArrayList<String>();
		List<UpdateGstRequest> updateList = new ArrayList<UpdateGstRequest>();
		for (RenewalsSolutionDetail solution : quoteDetail.getSolutions()) {
			String productOffering = solution.getOfferingName();
			MstProductOffering productOfferng = getProductOffering(productFamily, productOffering, user);
			ProductSolution productSolution;
			productSolution = constructProductSolution(productOfferng, quoteToLeProductFamily,
					Utils.convertObjectToJson(solution));
			productSolution.setSolutionCode(Utils.generateUid());
			productSolutionList.add(productSolution);
			serviceIds.add(solution.getServiceId());
			// productSolutionRepository.save(productSolution);
			if (StringUtils.isNotBlank(quoteToLe.getTpsSfdcOptyId()))
				omsSfdcService.processProductServiceForSolution(quoteToLe, productSolution,
						quoteToLe.getTpsSfdcOptyId());// adding productService
			Map<String, Object> serviceIdInputExcelMapper =  serviceIdPriceMapping.get(solution.getServiceId());
			productSolutionSiLinkList.add(
					new ProductSolutionSiLink(solution.getServiceId(), solution.getAccessType(), quoteToLe.getId(),
							(String)serviceIdInputExcelMapper.get(RenewalsConstant.PO_NUMBER), 
							(String)serviceIdInputExcelMapper.get(RenewalsConstant.PO_DATE),
							(String)serviceIdInputExcelMapper.get(RenewalsConstant.EFFECTIVE_DATE),
							Character.toString((Character)solution.getTaxException())));
			UpdateGstRequest update  = new UpdateGstRequest();
			update.setFamilyName("GVPN");
			update.setQuoteToLeId(quoteToLe.getId());
			update.setQuoteId(quote.getId());
			update.setIsGstUpdated("N");
			update.setSiteAgst((String)serviceIdInputExcelMapper.get(RenewalsConstant.GST_NUMBER));
	        updateList.add(update);

		}
		productSolutionList = productSolutionRepository.saveAll(productSolutionList);
		for (int i = 0; i < productSolutionList.size(); i++) {
			productsolutionLinkMap.put(productSolutionSiLinkList.get(i).getServiceId(), productSolutionList.get(i));
			productSolutionSiLinkList.get(i).setProductSolutionId(productSolutionList.get(i).getId());
		}
		productSolutionSiLinkRepository.saveAll(productSolutionSiLinkList);
		RenewalsProductsolutionLinkMap response = new RenewalsProductsolutionLinkMap();
		response.setProductsolutionLinkMap(productsolutionLinkMap);
		response.setQuoteTole(quoteToLe);
		response.setUpdateList(updateList);
		return response;

	}

	public List<QuoteIllSite> constructSiteObject(RenewalsQuoteDetail rQuoteDetail, User user) {
		List<QuoteIllSite> quoteIllSiteList = new ArrayList<QuoteIllSite>();
		List<RenewalsSite> renewalsSeriveIdSites = rQuoteDetail.getSite();

		for (RenewalsSite renewals : renewalsSeriveIdSites) {
			quoteIllSiteList.addAll(constructIllSites(user, renewals));
		}
		return quoteIllSiteList;
	}

	public Map<String, List<QuoteProductComponent>> constructComponentAttributes(RenewalsQuoteDetail rQuoteDetail,
			MstProductFamily mstProductFamily, User user) throws TclCommonException {
		Map<String, List<QuoteProductComponent>> serviceIdComponentMapping = new HashMap<String, List<QuoteProductComponent>>();
		List<RenewalsSolutionDetail> solutionList = rQuoteDetail.getSolutions();
		for (RenewalsSolutionDetail solution : solutionList) {
			List<QuoteProductComponent> componentAttributeList = processProductComponent(mstProductFamily,
					solution.getComponents(), user);
			serviceIdComponentMapping.put(solution.getServiceId(), componentAttributeList);
		}
		return serviceIdComponentMapping;
	}

	private List<QuoteProductComponent> processProductComponent(MstProductFamily productFamily,
			List<RenewalsComponentDetail> components, User user) throws TclCommonException {
		try {
			List<QuoteProductComponent> listOfComponents = new ArrayList<QuoteProductComponent>();
			for (RenewalsComponentDetail component : components) {
				MstProductComponent productComponent = getProductComponent(component, user);
				QuoteProductComponent quoteComponent = constructProductComponent(productComponent, productFamily);
				quoteComponent.setType(component.getType());
				// quoteProductComponentRepository.save(quoteComponent);
				Set<QuoteProductComponentsAttributeValue> attribteList = new HashSet<QuoteProductComponentsAttributeValue>();
				for (RenewalsAttributeDetail attribute : component.getAttributes()) {
					attribteList.add(processProductAttribute(quoteComponent, attribute, user));
				}
				quoteComponent.setQuoteProductComponentsAttributeValues(attribteList);
				listOfComponents.add(quoteComponent);
			}
			return listOfComponents;
		} catch (TclCommonException e) {
			throw new TclCommonException(e);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.INVALID_COMPONENT_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private QuoteProductComponentsAttributeValue processProductAttribute(QuoteProductComponent quoteComponent,
			RenewalsAttributeDetail attribute, User user) {

		ProductAttributeMaster productAttribute = getProductAttributes(attribute, user);
		QuoteProductComponentsAttributeValue quoteProductAttribute = constructProductAttribute(quoteComponent,
				productAttribute, attribute);
		quoteProductAttribute.setQuoteProductComponent(quoteComponent);
		return quoteProductAttribute;
		// quoteProductComponentsAttributeValueRepository.save(quoteProductAttribute);

	}

	private QuoteProductComponentsAttributeValue constructProductAttribute(QuoteProductComponent quoteProductComponent,
			ProductAttributeMaster productAttributeMaster, RenewalsAttributeDetail attributeDetail) {
		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
		quoteProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
		quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
		return quoteProductComponentsAttributeValue;

	}

	private ProductAttributeMaster getProductAttributes(RenewalsAttributeDetail attributeDetail, User user) {
		ProductAttributeMaster productAttributeMaster = null;

		List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus(attributeDetail.getName(), (byte) 1);
		if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
			productAttributeMaster = productAttributeMasters.get(0);
		}
		if (productAttributeMaster == null) {
			productAttributeMaster = new ProductAttributeMaster();
			productAttributeMaster.setName(attributeDetail.getName());
			productAttributeMaster.setDescription(attributeDetail.getName());
			productAttributeMaster.setStatus((byte) 1);
			productAttributeMaster.setCreatedBy(user.getUsername());
			productAttributeMasterRepository.save(productAttributeMaster);
		}

		return productAttributeMaster;
	}

	private QuoteProductComponent constructProductComponent(MstProductComponent productComponent,
			MstProductFamily mstProductFamily) {
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setMstProductComponent(productComponent);
		quoteProductComponent.setMstProductFamily(mstProductFamily);
		// quoteProductComponent.setReferenceId(illSiteId);
		quoteProductComponent.setReferenceName(QuoteConstants.GVPN_SITES.toString());
		return quoteProductComponent;

	}

	private MstProductComponent getProductComponent(RenewalsComponentDetail component, User user)
			throws TclCommonException {
		MstProductComponent mstProductComponent = null;
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(component.getName(), (byte) 1);
		if (mstProductComponents != null && !mstProductComponents.isEmpty()) {
			mstProductComponent = mstProductComponents.get(0);

		}
		if (mstProductComponent == null) {
			mstProductComponent = new MstProductComponent();
			mstProductComponent.setName(component.getName());
			mstProductComponent.setCreatedBy(user.getUsername());
			mstProductComponent.setCreatedTime(new Date());
			mstProductComponent.setStatus((byte) 1);
			mstProductComponentRepository.save(mstProductComponent);
		}

		return mstProductComponent;
	}

	private List<QuoteIllSite> constructIllSites(User user, RenewalsSite site) {
		List<RenewalsSiteDetail> siteInp = site.getSite();
		List<QuoteIllSite> quoteIllSiteList = new ArrayList<QuoteIllSite>();
		for (RenewalsSiteDetail siteDetail : siteInp) {
			if (siteDetail.getSiteId() == null) {
				QuoteIllSite illSite = new QuoteIllSite();
				illSite.setErfLocSiteaLocationId(siteDetail.getSecondLocationId());
				illSite.setErfLocSiteaSiteCode(siteDetail.getSecondLocationCode());
				illSite.setErfLocSitebLocationId(siteDetail.getLocationId());
				illSite.setErfLocSitebSiteCode(siteDetail.getLocationCode());
				// illSite.setProductSolution(productSolution);
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date()); // Now use today date.
				cal.add(Calendar.DATE, 60); // Adding 60 days
				illSite.setEffectiveDate(cal.getTime());
				illSite.setCreatedBy(user.getId());
				illSite.setCreatedTime(new Date());
				illSite.setStatus((byte) 1);
				// illSite.setImageUrl(soDetail.getImage());
				illSite.setSiteCode(Utils.generateUid());
				illSite.setFeasibility((byte) 1);
				illSite.setIsTaskTriggered(0);
				illSite.setMfTaskTriggered(0);
				illSite.setCommercialRejectionStatus("0");
				illSite.setErfServiceInventoryTpsServiceId(site.getServiceId());
				// illSite.setFpStatus("FB");
				illSite.setIsColo((byte) 0);

				quoteIllSiteList.add(illSite);
			}
		}
		return quoteIllSiteList;
	}

	public List<RenewalsAttributeDetail> addAditionalAttributes(List<RenewalsAttributeDetail> renewalsList,
			Character commercial, String date) {
		RenewalsAttributeDetail attributeDetail = constructAttribute(RenewalsConstant.IS_COOMERCIAL,
				commercial.toString());
		renewalsList.add(attributeDetail);
		RenewalsAttributeDetail attributeDetailEffDate = constructAttribute(LeAttributesConstants.EFFECTIVE_DATE, date);
		renewalsList.add(attributeDetailEffDate);
		return renewalsList;
	}

	public RenewalsAttributeDetail constructAttribute(String name, String value) {
		RenewalsAttributeDetail attribute = new RenewalsAttributeDetail();
		attribute.setName(name);
		attribute.setValue(value);
		return attribute;
	}

	public List<QuoteLeAttributeValue> constructListOfQuoteLeAttributes(List<RenewalsAttributeDetail> request,
			User user, Integer quotToleId) throws TclCommonException {
		try {
			List<QuoteLeAttributeValue> quoteLeAttributeSave = new ArrayList<QuoteLeAttributeValue>();
			Optional<QuoteToLe> optionalQuoteToLe = quoteToLeRepository.findById(quotToleId);

			List<RenewalsAttributeDetail> attributeDetails = request;
			for (RenewalsAttributeDetail renewlsAttribute : attributeDetails) {
				AttributeDetail attribute = new AttributeDetail();
				BeanUtils.copyProperties(renewlsAttribute, attribute);
				if (attribute.getName() != null) {
					MstOmsAttribute mstOmsAttribute = null;
					List<MstOmsAttribute> mstOmsAttributeList = mstOmsAttributeRepository
							.findByNameAndIsActive(attribute.getName(), (byte) 1);
					if (!mstOmsAttributeList.isEmpty()) {
						mstOmsAttribute = mstOmsAttributeList.get(0);
					}
					if (mstOmsAttribute == null) {
						mstOmsAttribute = new MstOmsAttribute();
						mstOmsAttribute.setCreatedBy(user.getUsername());
						mstOmsAttribute.setCreatedTime(new Date());
						mstOmsAttribute.setIsActive((byte) 1);
						mstOmsAttribute.setName(attribute.getName());
						mstOmsAttribute.setDescription("");
						mstOmsAttributeRepository.save(mstOmsAttribute);

					}

					quoteLeAttributeSave
							.addAll(saveLegalEntityAttributes(optionalQuoteToLe.get(), attribute, mstOmsAttribute));
				}

			}
			return quoteLeAttributeSave;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private List<QuoteLeAttributeValue> saveLegalEntityAttributes(QuoteToLe quoteToLe, AttributeDetail attribute,
			MstOmsAttribute mstOmsAttribute) {
		List<QuoteLeAttributeValue> quoteLeAttributeSave = new ArrayList<QuoteLeAttributeValue>();
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, attribute.getName());
		if (quoteLeAttributeValues != null && !quoteLeAttributeValues.isEmpty()) {
			quoteLeAttributeValues.forEach(attrVal -> {
				attrVal.setMstOmsAttribute(mstOmsAttribute);
				attrVal.setAttributeValue(attribute.getValue());
				attrVal.setDisplayValue(attribute.getName());
				attrVal.setQuoteToLe(quoteToLe);
				quoteLeAttributeSave.add(attrVal);
			});
		} else {
			QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
			quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttribute);
			quoteLeAttributeValue.setAttributeValue(attribute.getValue());
			quoteLeAttributeValue.setDisplayValue(attribute.getName());
			quoteLeAttributeValue.setQuoteToLe(quoteToLe);
			quoteLeAttributeSave.add(quoteLeAttributeValue);
		}
		return quoteLeAttributeSave;
	}

	public RenewalsQuoteDetail compareAndInsertObject(Map<String, Map<String, Object>> serviceIdPriceMapping,
			RenewalsQuoteDetail quoteDetail) throws TclCommonException {

		int size = quoteDetail.getSolutions().size();
		for (int i = 0; i < size; i++) {
			List<String> listOfComponentsPrimary = new ArrayList<String>();
			List<String> listOfComponentsSecondary = new ArrayList<String>();
			List<String> listOfComponentsExcelPrimary = new ArrayList<String>();
			List<String> listOfComponentsExcelSecondary = new ArrayList<String>();
			Map<String, Object> mappingexcel = serviceIdPriceMapping
					.get(quoteDetail.getSolutions().get(i).getServiceId());
           if(mappingexcel!=null) {
			Set<String> keys = mappingexcel.keySet();
			for (String key : keys) {
				if (key.contains(RenewalsConstant.PRIMARY_COMP_MRC) || key.contains(RenewalsConstant.PRIMARY_COMP_NRC)
						|| key.contains(RenewalsConstant.PRIMARY_COMP_ARC)
						|| key.contains(RenewalsConstant.PRIMARY_COMP_EUC)) {
					key = key.replace(RenewalsConstant.PRIMARY_COMP_MRC, "")
							.replace(RenewalsConstant.PRIMARY_COMP_NRC, "")
							.replace(RenewalsConstant.PRIMARY_COMP_ARC, "")
							.replace(RenewalsConstant.PRIMARY_COMP_EUC, "").trim();
					if (!listOfComponentsExcelPrimary.contains(key)) {
						listOfComponentsExcelPrimary.add(key);
					}
				} else if (key.contains(RenewalsConstant.SECONDARY_COMP_MRC)
						|| key.contains(RenewalsConstant.SECONDARY_COMP_NRC)
						|| key.contains(RenewalsConstant.SECONDARY_COMP_ARC)
						|| key.contains(RenewalsConstant.SECONDARY_COMP_EUC)) {
					key = key.replace(RenewalsConstant.SECONDARY_COMP_MRC, "")
							.replace(RenewalsConstant.SECONDARY_COMP_NRC, "")
							.replace(RenewalsConstant.SECONDARY_COMP_ARC, "").trim();
					if (!listOfComponentsExcelSecondary.contains(key)) {
						listOfComponentsExcelSecondary.add(key);
					}
				}
			}

			for (int j = 0; j < quoteDetail.getSolutions().get(i).getComponents().size(); j++) {
				if (quoteDetail.getSolutions().get(i).getComponents().get(j).getType()
						.equalsIgnoreCase(RenewalsConstant.PRIMARY)) {
					listOfComponentsPrimary.add(quoteDetail.getSolutions().get(i).getComponents().get(j).getName());
					if (quoteDetail.getSolutions().get(i).getComponents().get(j).getName()
							.equalsIgnoreCase(RenewalsConstant.LAST_MILE)) {
						RenewalsAttributeDetail attr = checkAttribute(
								quoteDetail.getSolutions().get(i).getComponents().get(j).getAttributes(),
								RenewalsConstant.MAST_COST);
						if (attr != null) {
							quoteDetail.getSolutions().get(i).getComponents().get(j).getAttributes().add(attr);
						}
					}
					if (quoteDetail.getSolutions().get(i).getComponents().get(j).getName()
							.equalsIgnoreCase(RenewalsConstant.INTERNET_PORT)) {
						RenewalsAttributeDetail attr = checkAttribute(
								quoteDetail.getSolutions().get(i).getComponents().get(j).getAttributes(),
								RenewalsConstant.BURSTABLE_BANDWIDTH);
						if (attr != null) {
							quoteDetail.getSolutions().get(i).getComponents().get(j).getAttributes().add(attr);
						}
					}
				} else {
					listOfComponentsSecondary.add(quoteDetail.getSolutions().get(i).getComponents().get(j).getName());

					if (quoteDetail.getSolutions().get(i).getComponents().get(j).getName()
							.equalsIgnoreCase(RenewalsConstant.LAST_MILE)) {
						RenewalsAttributeDetail attr = checkAttribute(
								quoteDetail.getSolutions().get(i).getComponents().get(j).getAttributes(),
								RenewalsConstant.MAST_COST);
						if (attr != null) {
							quoteDetail.getSolutions().get(i).getComponents().get(j).getAttributes().add(attr);
						}
					}
					if (quoteDetail.getSolutions().get(i).getComponents().get(j).getName()
							.equalsIgnoreCase(RenewalsConstant.INTERNET_PORT)) {
						RenewalsAttributeDetail attr = checkAttribute(
								quoteDetail.getSolutions().get(i).getComponents().get(j).getAttributes(),
								RenewalsConstant.BURSTABLE_BANDWIDTH);
						if (attr != null) {
							quoteDetail.getSolutions().get(i).getComponents().get(j).getAttributes().add(attr);
						}
					}

				}
			}
			
			if (!listOfComponentsExcelSecondary.isEmpty() && quoteDetail.getSolutions().get(i).getComponents().get(0).getType().equalsIgnoreCase("primary")) {
				throw new TclCommonException(RenewalsConstant.SERVICEID_IS_NOT_SECONDAYY, ResponseResource.R_CODE_ERROR);
			}
			if (!listOfComponentsExcelPrimary.isEmpty() && quoteDetail.getSolutions().get(i).getComponents().get(0).getType().equalsIgnoreCase("secondary")) {
				throw new TclCommonException(RenewalsConstant.SERVICEID_IS_NOT_PRIMARY, ResponseResource.R_CODE_ERROR);
			}
			listOfComponentsExcelSecondary.removeAll(listOfComponentsSecondary);
			listOfComponentsExcelPrimary.removeAll(listOfComponentsPrimary);
			if (!listOfComponentsExcelPrimary.isEmpty()) {
				LOGGER.info("Adding Components From Excel");
				for (String mapElement1 : listOfComponentsExcelPrimary) {
					LOGGER.info("Adding Components Primary From Excel, Component Name is -->{}", mapElement1.trim());
					quoteDetail.getSolutions().get(i).getComponents()
							.add(constructComponent(mapElement1.trim(), RenewalsConstant.PRIMARY));
				}
			}
//			if (!listOfComponentsExcelSecondary.isEmpty() && !quoteDetail.getSolutions().get(i).isDual()) {
////				throw new TclCommonException(RenewalsConstant.SERVICEID_IS_NOT_DUAL, ResponseResource.R_CODE_ERROR);
//			}
			if (!listOfComponentsExcelSecondary.isEmpty()) {
				for (String mapElement1 : listOfComponentsExcelSecondary) {
					LOGGER.info("Adding Components Secondary From Excel, Component Name is -->{}", mapElement1.trim());
					quoteDetail.getSolutions().get(i).getComponents()
							.add(constructComponent(mapElement1.trim(), RenewalsConstant.SECONDARY));
				}
			}
           }
		}
		return quoteDetail;
	}

	public RenewalsAttributeDetail checkAttribute(List<RenewalsAttributeDetail> attrrList, String name) {
		boolean available = false;
		for (RenewalsAttributeDetail attributeDetails : attrrList) {
			if (attributeDetails.getName().equalsIgnoreCase(name)) {
				available = true;
			}
		}
		if (!available) {
			RenewalsAttributeDetail attr = constructrAttribute(name, "");
			return attr;
		}
		return null;
	}

	public RenewalsComponentDetail constructComponent(String name, String type) {
		RenewalsComponentDetail component = new RenewalsComponentDetail();
		component.setName(name.trim());
		component.setType(type);
		component.setAttributes(new ArrayList<RenewalsAttributeDetail>());
		if (name.equalsIgnoreCase(RenewalsConstant.LAST_MILE)) {
			RenewalsAttributeDetail obj = constructrAttribute(RenewalsConstant.MAST_COST, "");
			component.getAttributes().add(obj);
		}
		if (name.equalsIgnoreCase(RenewalsConstant.INTERNET_PORT)) {
			RenewalsAttributeDetail obj = constructrAttribute(RenewalsConstant.BURSTABLE_BANDWIDTH, "");
			component.getAttributes().add(obj);
		}
		return component;
	}

	public RenewalsAttributeDetail constructrAttribute(String name, String value) {
		RenewalsAttributeDetail obj = new RenewalsAttributeDetail();
		obj.setName(name);
		obj.setValue("");
		return obj;
	}

	public RenevalsIllSiteBean constructQutePrice(List<QuoteIllSite> quoteIllSiteList,
			Map<String, Map<String, Object>> serviceIdPriceMapping, MstProductFamily mstProductFamily, Integer quoteId)
			throws TclCommonException {
		RenevalsIllSiteBean renevalsIllSiteBean = new RenevalsIllSiteBean();
		List<QuotePrice> quotePriceList = new ArrayList<QuotePrice>();
		Map<Integer, RenewalsPriceBean> illSitePrice = new HashMap<Integer, RenewalsPriceBean>();
		Double quoteTotalMrc = 0D;
		Double quoteTotalNrc = 0D;
		Double quoteTotalArc = 0D;

		for (QuoteIllSite quoteIllSite : quoteIllSiteList) {
			RenewalsPriceBean renewalsPriceBean = new RenewalsPriceBean();
			Map<String, Object> mappingexcel = serviceIdPriceMapping
					.get(quoteIllSite.getErfServiceInventoryTpsServiceId());
			if(mappingexcel!=null) {
			List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
					.findByReferenceId(quoteIllSite.getId());
			for (QuoteProductComponent quoteProductComponent : quoteProductComponentList) {
				/*
				 * Optional<MstProductComponent> mstProductComponent =
				 * mstProductComponentRepository
				 * .findById(quoteProductComponent.getMstProductComponent().getId());
				 */
				MstProductComponent mstprd =   quoteProductComponent.getMstProductComponent(); 
				String productName = mstprd.getName();
				if (productName.equalsIgnoreCase(RenewalsConstant.VPN_PORT)
						|| productName.equalsIgnoreCase(RenewalsConstant.CPE)
						|| productName.equalsIgnoreCase(RenewalsConstant.LAST_MILE)
						|| productName.equalsIgnoreCase(RenewalsConstant.CPE_RECOVERY_CHARGES)) {
					QuotePrice quotePrice = new QuotePrice();
					quotePrice.setQuoteId(quoteId);
					quotePrice.setMstProductFamily(mstProductFamily);
					quotePrice.setReferenceName(RenewalsConstant.COMPONENTS);
					quotePrice.setReferenceId(quoteProductComponent.getId().toString());
					quotePrice = constructComponentPrice(mappingexcel, productName, quotePrice,
							quoteProductComponent.getType());
					quotePriceList.add(quotePrice);

					renewalsPriceBean.setMrc(roundOff(quotePrice.getEffectiveMrc() + renewalsPriceBean.getMrc()));
					renewalsPriceBean.setNrc(roundOff(quotePrice.getEffectiveNrc() + renewalsPriceBean.getNrc()));
					renewalsPriceBean.setArc(roundOff(quotePrice.getEffectiveArc() + renewalsPriceBean.getArc()));
					}
					/*
					 * if (productName.equalsIgnoreCase(RenewalsConstant.INTERNET_PORT) ||
					 * productName.equalsIgnoreCase(RenewalsConstant.LAST_MILE)) {
					 */
					Set<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValueList = quoteProductComponent.getQuoteProductComponentsAttributeValues();

					for (QuoteProductComponentsAttributeValue quotePrdAttributeValue : quoteProductComponentsAttributeValueList) {

						ProductAttributeMaster prdAtr = quotePrdAttributeValue.getProductAttributeMaster();
						String attributeNametName = prdAtr.getName();
						if((productName.equalsIgnoreCase(RenewalsConstant.INTERNET_PORT) ||
								 productName.equalsIgnoreCase(RenewalsConstant.LAST_MILE)) && (attributeNametName.equalsIgnoreCase(RenewalsConstant.MAST_COST)
								|| attributeNametName.equalsIgnoreCase(RenewalsConstant.BURSTABLE_BANDWIDTH))) {
							QuotePrice quotePriceA = new QuotePrice();
							quotePriceA.setQuoteId(quoteId);
							quotePriceA.setMstProductFamily(mstProductFamily);
							quotePriceA.setReferenceName(RenewalsConstant.ATTRIBUTES);
							quotePriceA.setReferenceId(quotePrdAttributeValue.getId().toString());
							quotePriceA = constructAtributePrice(mappingexcel, attributeNametName, quotePriceA,
									quoteProductComponent.getType());
							quotePriceList.add(quotePriceA);
							renewalsPriceBean.setMrc(roundOff(quotePriceA.getEffectiveMrc() + renewalsPriceBean.getMrc()));
							renewalsPriceBean.setNrc(roundOff(quotePriceA.getEffectiveNrc() + renewalsPriceBean.getNrc()));
							renewalsPriceBean.setArc(roundOff(quotePriceA.getEffectiveArc() + renewalsPriceBean.getArc()));
						} else {
							/*
							 * QuotePrice quotePriceA = new QuotePrice(); quotePriceA.setQuoteId(quoteId);
							 * quotePriceA.setMstProductFamily(mstProductFamily);
							 * quotePriceA.setReferenceName(RenewalsConstant.ATTRIBUTES);
							 * quotePriceA.setReferenceId(quotePrdAttributeValue.getId().toString());
							 * quotePriceList.add(quotePriceA); renewalsPriceBean.setMrc(0D);
							 * renewalsPriceBean.setNrc(0D); renewalsPriceBean.setArc(0D);
							 */}

					}
			//	}
			}
			illSitePrice.put(quoteIllSite.getId(), renewalsPriceBean);

			quoteTotalMrc = roundOff(quoteTotalMrc + renewalsPriceBean.getMrc());
			quoteTotalNrc = roundOff(quoteTotalNrc + renewalsPriceBean.getNrc());
			quoteTotalArc = roundOff(quoteTotalArc + renewalsPriceBean.getArc());
		}
		}
		renevalsIllSiteBean.setIllSitePrice(illSitePrice);
		renevalsIllSiteBean.setQuotePriceList(quotePriceList);
		renevalsIllSiteBean.setTotalMrc(quoteTotalMrc);
		renevalsIllSiteBean.setTotalNrc(quoteTotalNrc);
		renevalsIllSiteBean.setTotalArc(quoteTotalArc);
		return renevalsIllSiteBean;
	}

	public QuotePrice constructComponentPrice(Map<String, Object> mappingexcel, String productName,
			QuotePrice quotePrice, String type) {
	//	LOGGER.info("Processing Component Price-->{}", productName);
		Double mrc;
		Double nrc;
		Double arc;
		if (type.equalsIgnoreCase(RenewalsConstant.PRIMARY)) {
			mrc = (Double)mappingexcel.get(productName + RenewalsConstant.PRIMARY_COMP_MRC_SPACE);
			nrc = (Double)mappingexcel.get(productName + RenewalsConstant.PRIMARY_COMP_NRC_SPACE);
			arc = (Double)mappingexcel.get(productName + RenewalsConstant.PRIMARY_COMP_ARC_SPACE);
	//		LOGGER.info("Primary Component Name-->{ Mrc --> {}, NRC --> {}, ARC -->{} }", productName, mrc, nrc, arc);
		} else {
			mrc = (Double)mappingexcel.get(productName + RenewalsConstant.SECONDARY_COMP_MRC_SPACE);
			nrc = (Double)mappingexcel.get(productName + RenewalsConstant.SECONDARY_COMP_NRC_SPACE);
			arc = (Double)mappingexcel.get(productName + RenewalsConstant.SECONDARY_COMP_ARC_SPACE);
//			LOGGER.info("Secondary Component Name-->{ Mrc --> {}, NRC --> {}, ARC -->{} }", productName, mrc, nrc, arc);
		}

		quotePrice.setEffectiveMrc(mrc != null ? mrc : 0D);
		quotePrice.setEffectiveNrc(nrc != null ? nrc : 0D);
		quotePrice.setEffectiveArc(arc != null ? arc : 0D);

		return quotePrice;
	}

	public QuotePrice constructAtributePrice(Map<String, Object> mappingexcel, String attributeNametName,
			QuotePrice quotePrice, String type) {
	//	LOGGER.info("Processing Attribute Price-->{}", attributeNametName);
		Double eucA;
		if (type.equalsIgnoreCase(RenewalsConstant.PRIMARY)) {
			eucA = (Double)mappingexcel.get(attributeNametName + RenewalsConstant.PRIMARY_ATTR_EUC_SPACE);
	//		LOGGER.info("Primary Attribute Name-->{ Mrc --> {}, NRC --> {}, ARC -->{}, EUC-->{} }", attributeNametName,
	//				eucA);
		} else {
			eucA = (Double)mappingexcel.get(attributeNametName + RenewalsConstant.SECONDARY_ATTR_EUC_SPACE);
	//		LOGGER.info("Primary Attribute Name-->{ Mrc --> {}, NRC --> {}, ARC -->{}, EUC-->{} }", attributeNametName,
	//				eucA);
		}
		quotePrice.setEffectiveMrc(0D);
		quotePrice.setEffectiveNrc(0D);
		quotePrice.setEffectiveArc(0D);
		if (attributeNametName.equalsIgnoreCase(RenewalsConstant.MAST_COST)) {
			quotePrice.setEffectiveUsagePrice(eucA != null ? eucA : 0D);
			quotePrice.setEffectiveNrc(eucA != null ? eucA : 0D);
		}
		if (attributeNametName.equalsIgnoreCase(RenewalsConstant.BURSTABLE_BANDWIDTH)) {
			quotePrice.setEffectiveUsagePrice(eucA != null ? eucA : 0D);
		}

		return quotePrice;
	}
	protected void persistQuoteLeAttributes(User user, QuoteToLe quoteTole) {
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.CONTACT_NAME.toString(),
				user.getFirstName());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.CONTACT_EMAIL.toString(),
				user.getEmailId());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.CONTACT_ID.toString(),
				user.getUsername());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.CONTACT_NO.toString(),
				user.getContactNo());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.DESIGNATION.toString(),
				user.getDesignation());
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.RECURRING_CHARGE_TYPE.toString(), "ARC");
		updateLeAttribute(quoteTole, user.getUsername(), LeAttributesConstants.IS_ORDER_ENRICHMENT_ATTRIBUTES_PROVIDED,
				"No");
	}
	
	public void updateLeAttribute(QuoteToLe quoteTole, String userName, String name, String value) {
		MstOmsAttribute mstOmsAttribute = null;
		if (name != null) {
			List<MstOmsAttribute> mstOmsAttributesList = mstOmsAttributeRepository.findByNameAndIsActive(name,
					(byte) 1);

			if (!mstOmsAttributesList.isEmpty()) {
				mstOmsAttribute = mstOmsAttributesList.get(0);
			}
			if (mstOmsAttribute == null) {
				mstOmsAttribute = new MstOmsAttribute();
				mstOmsAttribute.setCreatedBy(userName);
				mstOmsAttribute.setCreatedTime(new Date());
				mstOmsAttribute.setIsActive((byte) 1);
				mstOmsAttribute.setName(name);
				mstOmsAttribute.setDescription(value);
				mstOmsAttributeRepository.save(mstOmsAttribute);
			}

			List<QuoteLeAttributeValue> attributeValueList=	constructLegaAttribute(mstOmsAttribute, quoteTole, name, value);
			if(!attributeValueList.isEmpty()) {
				quoteLeAttributeValueRepository.saveAll(attributeValueList);
			}
		}
	}
	private List<QuoteLeAttributeValue> constructLegaAttribute(MstOmsAttribute mstOmsAttribute, QuoteToLe quoteTole, String name,
			String value) {
		List<QuoteLeAttributeValue> quoteLeList = new ArrayList<QuoteLeAttributeValue>();
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteTole, name);
		if (quoteLeAttributeValues == null || quoteLeAttributeValues.isEmpty()) {
			QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();
			attributeValue.setAttributeValue(value);
			attributeValue.setMstOmsAttribute(mstOmsAttribute);
			attributeValue.setQuoteToLe(quoteTole);
			attributeValue.setDisplayValue(name);
			quoteLeList.add(attributeValue);
			// quoteLeAttributeValueRepository.save(attributeValue);
		} 
       return quoteLeList;
	}
	public void createOppturnityProduct(QuoteToLe quoteTole, RenewalsProductsolutionLinkMap renewalsProductsolutionLinkMap) throws TclCommonException {
		quoteTole = quoteToLeRepository.findById(quoteTole.getId()).get();
		List<QuoteToLeProductFamily> quoteLeProductFamily=quoteToLeProductFamilyRepository.findByQuoteToLe(quoteTole.getId());
		Set<QuoteToLeProductFamily> hSet = new HashSet<QuoteToLeProductFamily>(); 
        for (QuoteToLeProductFamily x : quoteLeProductFamily) 
            hSet.add(x);
		quoteTole.setQuoteToLeProductFamilies(hSet);
		for (Map.Entry<String, ProductSolution> entry : renewalsProductsolutionLinkMap.getProductsolutionLinkMap().entrySet()) {
	      omsSfdcService.processProductServiceForSolution(quoteTole, entry.getValue(),
					quoteTole.getTpsSfdcOptyId());
	    }	
	}
	public Double calculatePreviousMrc(List<QuoteIllSite> quteIllSiteList, RenewalsQuoteDetail rQuoteDetail) {
		Double totalmrc = 0D;
		Map<String, RenewalsPriceBean> renewalsPriceBean = rQuoteDetail.getRenewalsPriceBean();
		for (QuoteIllSite illsite : quteIllSiteList) {
			RenewalsPriceBean price = renewalsPriceBean.get(illsite.getErfServiceInventoryTpsServiceId());
			Double mrc = price.getMrc();
			totalmrc = roundOff(totalmrc + (mrc != null ? mrc : 0D));
		}
		return totalmrc;
	}

}