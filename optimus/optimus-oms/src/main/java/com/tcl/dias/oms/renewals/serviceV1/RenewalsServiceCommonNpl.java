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
import com.tcl.dias.oms.constants.MACDConstants;
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
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
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
import com.tcl.dias.oms.entity.repository.NplLinkRepository;
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
import com.tcl.dias.oms.npl.beans.NplUpdateRequest;
import com.tcl.dias.oms.npl.constants.SiteTypeConstants;
import com.tcl.dias.oms.npl.service.v1.NplQuoteService;
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
public class RenewalsServiceCommonNpl {

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
	protected NplLinkRepository nplLinkRepository;
		
	@Autowired
	NplQuoteService nplQuoteService;

	@Autowired
	UpdateGstRequestRepository updateGstRequestRepository;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(RenewalsServiceCommonNpl.class);

	public RenewalsQuoteDetail processExcel(Integer leId, Integer custId, String product, Integer term,
			Character commercial, String date,String oppId, XSSFWorkbook workbook) throws TclCommonException, ParseException {

		User user = getUserId(Utils.getSource());
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(product,
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

		long startTime = System.currentTimeMillis();
		long temp;
		LOGGER.info("String create Quote Excel");

		if (commercial.equals('Y')) {
			LOGGER.info("construct price excel");
			serviceIdPriceMapping = renewalsExcelPriceMapping.constructPriceNpl(workbook);
			LOGGER.info("insert data");
			rQuoteDetail = compareAndInsertObject(serviceIdPriceMapping, rQuoteDetail);
		}else {
			 LOGGER.info("construct price excel");
				serviceIdPriceMapping = renewalsExcelPriceMapping.constructPriceNpl(workbook);
		}

		LOGGER.info("create quote");
		RenewalsProductsolutionLinkMap quoteResponse = createQuote(rQuoteDetail, rQuoteDetail.getCustomerId(), false,
				term, commercial, serviceIdPriceMapping, oppId, product);
		QuoteToLe quoteTole = quoteResponse.getQuoteTole();
		Integer quoteId = quoteTole.getQuote().getId();
		Integer quoteToleId = quoteTole.getId();

		LOGGER.info("construct SiteObject");
		List<QuoteIllSite> quteIllSiteList = constructSiteObject(rQuoteDetail, user);
		Double previousMrc = calculatePreviousMrc(quteIllSiteList,  rQuoteDetail);
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
		List<QuoteIllSite> quteIllSiteListWithId = persistingIllSites(quteIllSiteList, 
				quoteResponse, rQuoteDetail, commercial, term);
		LOGGER.info("persist ill sites end");
		
		LOGGER.info("persist ill link sites strats");
		List<ProductSolutionSiLink> productSolutionSiLinkList = productSolutionSiLinkRepository
				.findByQuoteToLeId(quoteToleId);
		Map<Integer, String> productSolutionSiLinkMap = productSolutionSiLinkList.stream().collect(
				Collectors.toMap(ProductSolutionSiLink::getProductSolutionId, ProductSolutionSiLink::getServiceId));
		List<QuoteNplLink> nplLinkList = constructIllLinkSite(quteIllSiteListWithId, rQuoteDetail, user, quoteId);
		List<QuoteNplLink> nplLinkListWithId = persistingNplLink(nplLinkList, quoteResponse,  rQuoteDetail,  commercial,
				 term,  quteIllSiteListWithId, productSolutionSiLinkMap);
		LOGGER.info("persist ill sites end");
		
		LOGGER.info("construct component attributes");
		Map<String, List<QuoteProductComponent>> serviceIdComponentMapping = constructComponentAttributes(rQuoteDetail,
				mstProductFamily, user, quteIllSiteListWithId, nplLinkListWithId);
		
		LOGGER.info("persist ill component starts");
		persistComponent(nplLinkListWithId,serviceIdComponentMapping, productSolutionSiLinkMap);
		LOGGER.info("persist ill component end");

		if (commercial.equals('Y')) {
			LOGGER.info("construct price");
			RenevalsIllSiteBean quotePrice = constructQutePrice( nplLinkListWithId,productSolutionSiLinkMap,
					 serviceIdPriceMapping,  mstProductFamily,  quoteId);
			LOGGER.info("construct ends");
			LOGGER.info("persist price starts");
			persistingPrice(quotePrice, nplLinkListWithId, quoteTole, term, commercial);
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
		LOGGER.info("time taken = " + (endTime - startTime) / (1000));
		orderFormStage(quoteTole, leId);
//		if(product.equalsIgnoreCase("NDE")) {
//		createOppturnityProduct(quoteTole, quoteResponse);
//		}
		List<UpdateGstRequest> requestList=  quoteResponse.getUpdateList();
		String type;
		for( int i=0; i<requestList.size() ;i++) {
		//	NplUpdateRequest npl = new NplUpdateRequest();
			List<UpdateGstRequest> list = new ArrayList<UpdateGstRequest>();
			requestList.get(i).setSiteId(quteIllSiteListWithId.get(i).getId());
			list.add(requestList.get(i));
		//	npl.setUpdateRequest(list);
//			if(i%2==0) {
//				type = CommonConstants.SITEB;
//			}else {
//				type = CommonConstants.SITEA;
//			}
//		nplQuoteService.updateSiteProperties(npl, type);
		}
		updateGstRequestRepository.saveAll(requestList);

		return rQuoteDetail;
	}
	
//	public void createOppturnityProduct(QuoteToLe quoteTole, RenewalsProductsolutionLinkMap renewalsProductsolutionLinkMap) throws TclCommonException {
//		quoteTole = quoteToLeRepository.findById(quoteTole.getId()).get();
//		List<QuoteToLeProductFamily> quoteLeProductFamily=quoteToLeProductFamilyRepository.findByQuoteToLe(quoteTole.getId());
//		Set<QuoteToLeProductFamily> hSet = new HashSet<QuoteToLeProductFamily>(); 
//        for (QuoteToLeProductFamily x : quoteLeProductFamily) 
//            hSet.add(x);
//		quoteTole.setQuoteToLeProductFamilies(hSet);
//		for (Map.Entry<String, ProductSolution> entry : renewalsProductsolutionLinkMap.getProductsolutionLinkMap().entrySet()) {
//	      omsSfdcService.processProductServiceForSolution(quoteTole, entry.getValue(),
//					quoteTole.getTpsSfdcOptyId());
//	    }	
//	}
	
	public void orderFormStage(QuoteToLe quoteTole, Integer leId) {
		quoteTole.setErfCusCustomerLegalEntityId(leId);
		quoteTole.setStage(QuoteStageConstants.ORDER_FORM.getConstantCode());
		quoteToLeRepository.save(quoteTole);
	}

	public List<QuoteNplLink> persistingNplLink(List<QuoteNplLink> nplLinkList,
				RenewalsProductsolutionLinkMap quoteResponse, RenewalsQuoteDetail rQuoteDetail, Character commercial,
			Integer term, List<QuoteIllSite> quteIllSiteListWithId, Map<Integer, String> productSolutionSiLinkMap) {

		Double totalmrc = 0D;
		Double totalnrc = 0D;
		Double totalarc = 0D;
	//	Map<String, ProductSolution> productsolutionLinkMap = quoteResponse.getProductsolutionLinkMap();
		Map<String, RenewalsPriceBean> renewalsPriceBean = rQuoteDetail.getRenewalsPriceBean();
		int i = 0;
		for (QuoteNplLink illsite : nplLinkList) {
			String serviceId = productSolutionSiLinkMap.get(illsite.getProductSolutionId());
			if (commercial.equals('N')) {
				RenewalsPriceBean price = renewalsPriceBean.get(serviceId);
				Double mrc = price.getMrc();
				Double nrc = 0D;
				Double arc = price.getArc();
				totalmrc = roundOff(totalmrc + (mrc != null ? mrc : 0D));
				totalnrc = 0D;
				totalarc = roundOff(totalarc + (arc != null ? arc : 0D));
				illsite.setMrc(mrc);
				illsite.setNrc(0D);
				illsite.setArc(arc);
				illsite.setTcv(roundOff(calculateTcv(arc,0D, term)));

			}
			nplLinkList.set(i, illsite);
			i++;
		}
		nplLinkList = nplLinkRepository.saveAll(nplLinkList);

		if (commercial.equals('N')) {
			QuoteToLe quoteTole = quoteResponse.getQuoteTole();
			quoteTole.setFinalMrc(totalmrc);
			quoteTole.setFinalNrc(0D);
			quoteTole.setFinalArc(totalarc);
			quoteTole.setTotalTcv(roundOff(calculateTcv(totalarc,0D, term)));
			quoteToLeRepository.save(quoteTole);
		}
		return nplLinkList;
	}
	public double roundOff(double value) {
		if(value!=0D) {
		return (value*100)/100;
		}
		return 0D;
	}
	public void persistingPrice(RenevalsIllSiteBean quotePrice, List<QuoteNplLink> quteIllSiteListWithId,
			QuoteToLe quoteTole, Integer term, Character commercial) {

		if (!quotePrice.getQuotePriceList().isEmpty()) {
			quotePriceRepository.saveAll(quotePrice.getQuotePriceList());
		}

		Map<Integer, RenewalsPriceBean> illSitePrice = quotePrice.getIllSitePrice();
		List<QuoteNplLink> illSitePriceUpdate = new ArrayList<QuoteNplLink>();
		for (QuoteNplLink illSite : quteIllSiteListWithId) {
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
			nplLinkRepository.saveAll(illSitePriceUpdate);
		}
		if (commercial.equals('Y')) {
		quoteTole.setFinalMrc(roundOff(quotePrice.getTotalMrc()));
		quoteTole.setFinalNrc(roundOff(quotePrice.getTotalNrc()));
		quoteTole.setFinalArc(roundOff(quotePrice.getTotalArc()));
		quoteTole.setTotalTcv(calculateTcv(roundOff(quotePrice.getTotalArc()), roundOff(quotePrice.getTotalNrc()),term));
		quoteToLeRepository.save(quoteTole);
		}

	}

	public List<QuoteIllSite> persistingIllSites(List<QuoteIllSite> quteIllSiteList,
					RenewalsProductsolutionLinkMap quoteResponse, RenewalsQuoteDetail rQuoteDetail, Character commercial,
			Integer term) {
		Map<String, ProductSolution> productsolutionLinkMap = quoteResponse.getProductsolutionLinkMap();

		int i = 0;
		for (QuoteIllSite illsite : quteIllSiteList) {
			ProductSolution productSolution = productsolutionLinkMap.get(illsite.getErfServiceInventoryTpsServiceId());
			illsite.setProductSolution(productSolution);
			quteIllSiteList.set(i, illsite);
			i++;
		}
		quteIllSiteList = illSiteRepository.saveAll(quteIllSiteList);

		return quteIllSiteList;
	}

	public Double calculateTcv(Double arc,Double nrc, Integer term) {
		if (arc != null && arc != 0D) {
			return (((((arc / 12) * term)+nrc))*100)/100;
		} else {
			return 0D;
		}
	}

	public void persistComponent(List<QuoteNplLink> nplLinkListWithId,
			Map<String, List<QuoteProductComponent>> serviceIdComponentMapping, Map<Integer, String> productSolutionSiLinkMap ) {
		List<QuoteProductComponent> quoteProductComponentListToSave = new ArrayList<QuoteProductComponent>();
		Integer id =null;
		for (QuoteNplLink illsite : nplLinkListWithId) {
			List<QuoteProductComponent> quoteProductComponentList = serviceIdComponentMapping
					.get(productSolutionSiLinkMap.get(illsite.getProductSolutionId()));
			if (quoteProductComponentList != null && !quoteProductComponentList.isEmpty()) {
				for (QuoteProductComponent component : quoteProductComponentList) {
					
					String refType = QuoteConstants.NPL_SITES.toString();
					if (component.getType() != null && component.getType().equalsIgnoreCase(CommonConstants.LINK)) {
						id = illsite.getId();
						refType = QuoteConstants.NPL_LINK.toString();
					}
					if (component.getType() != null && component.getType().equalsIgnoreCase(CommonConstants.SITEA))
						id = illsite.getSiteAId();
					if (component.getType() != null && component.getType().equalsIgnoreCase(CommonConstants.SITEB))
						id = illsite.getSiteBId();
					
					component.setReferenceId(id);
					component.setReferenceName(refType);			
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

		completeQuote = markPrimaryOrSecondary(completeQuote);
		return completeQuote;
	}

	public RenewalsQuoteDetail markPrimaryOrSecondary(RenewalsQuoteDetail quoteDetail) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < quoteDetail.getSolutions().size(); i++) {
			if (quoteDetail.getProductName().equalsIgnoreCase("NDE")) {
				quoteDetail.getSolutions().get(i).setOfferingName("National Dedicated Ethernet");
				
			} else if (quoteDetail.getProductName().equalsIgnoreCase("NPL")) {
				quoteDetail.getSolutions().get(i).setOfferingName("Private Line - NPL");
				
			}
			map = new HashMap<String, Integer>();
			for (int j = 0; j < quoteDetail.getSolutions().get(i).getComponents().size(); j++) {
				if (map.containsKey(quoteDetail.getSolutions().get(i).getComponents().get(j).getName())) {
					map.put(quoteDetail.getSolutions().get(i).getComponents().get(j).getName(),
							(map.get(quoteDetail.getSolutions().get(i).getComponents().get(j).getName()) + 2));
				} else {
					map.put(quoteDetail.getSolutions().get(i).getComponents().get(j).getName(), 1);
				}
			}
			for (int k = 0; k < quoteDetail.getSolutions().get(i).getComponents().size(); k++) {
				if (map.get(quoteDetail.getSolutions().get(i).getComponents().get(k).getName()) == 1) {
					quoteDetail.getSolutions().get(i).getComponents().get(k).setType(CommonConstants.LINK);
				} else if (map.get(quoteDetail.getSolutions().get(i).getComponents().get(k).getName()) == 3) {
					quoteDetail.getSolutions().get(i).getComponents().get(k).setType(CommonConstants.SITEA);
					map.put(quoteDetail.getSolutions().get(i).getComponents().get(k).getName(),
							(map.get(quoteDetail.getSolutions().get(i).getComponents().get(k).getName()) + -1));
				} else if (map.get(quoteDetail.getSolutions().get(i).getComponents().get(k).getName()) == 2) {
					quoteDetail.getSolutions().get(i).getComponents().get(k).setType(CommonConstants.SITEB);
					map.put(quoteDetail.getSolutions().get(i).getComponents().get(k).getName(),
							(map.get(quoteDetail.getSolutions().get(i).getComponents().get(k).getName()) + -1));

				}
			}
		}

		return quoteDetail;
	}


	public RenewalsProductsolutionLinkMap createQuote(RenewalsQuoteDetail quoteDetail, Integer erfCustomerId,
			Boolean ns, Integer term, Character isCommercial,  Map<String, Map<String, Object>> serviceIdPriceMapping, String oppId, String product) throws TclCommonException {
		QuoteResponse response = new QuoteResponse();
		RenewalsProductsolutionLinkMap combinedresponse = new RenewalsProductsolutionLinkMap();
		try {
			if (ns == null) {
				ns = false;
			}

			User user = getUserId(Utils.getSource());
			combinedresponse = processQuote(quoteDetail, erfCustomerId, user, ns, term, isCommercial, serviceIdPriceMapping, product);
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
			User user, Boolean ns, Integer term, Character isCommercial,  Map<String, Map<String, Object>> serviceIdPriceMapping,
			String product) throws TclCommonException {
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
							(String)serviceIdInputExcelMapper.get(RenewalsConstant.EFFECTIVE_DATE),solution.getCopfId(),
							Character.toString((Character)solution.getTaxException())));
			
			UpdateGstRequest updateA  = new UpdateGstRequest();
			updateA.setFamilyName(product);
			updateA.setQuoteToLeId(quoteToLe.getId());
			updateA.setQuoteId(quote.getId());
			updateA.setIsGstUpdated("N");
			updateA.setSiteAgst((String)serviceIdInputExcelMapper.get(RenewalsConstant.GST_NUMBER+"A"));
	        updateList.add(updateA);
			
			UpdateGstRequest updateB  = new UpdateGstRequest();
			updateB.setFamilyName(product);
			updateB.setQuoteToLeId(quoteToLe.getId());
			updateB.setQuoteId(quote.getId());
			updateB.setIsGstUpdated("N");
			updateB.setSiteBgst((String)serviceIdInputExcelMapper.get(RenewalsConstant.GST_NUMBER+"B"));
	        updateList.add(updateB);
	        
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

	protected QuoteNplLink constructNplLinks(User user, Integer quoteId, Integer siteAId, Integer siteBId,
			final String siteAType, final String siteBType, Integer productSolutionId) {
		QuoteNplLink link = null;
		link = new QuoteNplLink();
		link.setSiteAId(siteAId);
		link.setSiteBId(siteBId);
		link.setProductSolutionId(productSolutionId);
		link.setCreatedDate(new Date());
		link.setStatus((byte) 1);
		link.setQuoteId(quoteId);
		link.setCreatedBy(user.getId());
		link.setWorkflowStatus(null);
		link.setLinkCode(Utils.generateUid());
		link.setSiteAType(siteAType);
		link.setSiteBType(siteBType);
		link.setIsTaskTriggered(0);
		link.setFeasibility((byte)1);
		link.setFpStatus("FP");
		// Implemented as in ILL
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date()); // Now use today date.
		cal.add(Calendar.DATE, 60); // Adding 60 days
		link.setEffectiveDate(cal.getTime());
		return link;
	}

	public Map<String, List<QuoteProductComponent>> constructComponentAttributes(RenewalsQuoteDetail rQuoteDetail,
			MstProductFamily mstProductFamily, User user, List<QuoteIllSite> quteIllSiteListWithId ,List<QuoteNplLink> nplLinkListWithId) throws TclCommonException {
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
		quoteProductComponent.setReferenceName(QuoteConstants.ILLSITES.toString());
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
			//	illSite.setProductSolution(productSolution);
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date()); // Now use today date.
				cal.add(Calendar.DATE, 60); // Adding 60 days
				illSite.setEffectiveDate(cal.getTime());
				illSite.setCreatedBy(user.getId());
				illSite.setCreatedTime(new Date());
				illSite.setStatus((byte) 1);
				illSite.setSiteCode(Utils.generateUid());
				illSite.setFeasibility((byte) 0);
				illSite.setErfServiceInventoryTpsServiceId(site.getServiceId());
				int shiftSite =  0;
				illSite.setNplShiftSiteFlag(shiftSite);
	//			illSite = illSiteRepository.save(illSite);
				// siteDetail.setSiteId(illSite.getId());
				quoteIllSiteList.add(illSite);
				LOGGER.info("Npl shift site: " + illSite.getNplShiftSiteFlag());

//				}

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
		if (size > 0)
			for (int i = 0; i < size; i++) {
				List<String> listOfComponentsPrimary = new ArrayList<String>();
				List<String> listOfComponentsExcelPrimary = new ArrayList<String>();
				Map<String, Object> mappingexcel = serviceIdPriceMapping
						.get(quoteDetail.getSolutions().get(i).getServiceId());
				if (quoteDetail.getSolutions().get(i).getComponents() != null) {
					for (int j = 0; j < quoteDetail.getSolutions().get(i).getComponents().size(); j++) {
						if(quoteDetail.getSolutions().get(i).getComponents().get(j).getType().equalsIgnoreCase(RenewalsConstant.LINK))
						listOfComponentsPrimary.add(quoteDetail.getSolutions().get(i).getComponents().get(j).getName());
					}
				}
				Set<String> keys = mappingexcel.keySet();
				for (String key : keys) {
					if (key.contains(RenewalsConstant.PRIMARY_COMP_MRC)
							|| key.contains(RenewalsConstant.PRIMARY_COMP_NRC)
							|| key.contains(RenewalsConstant.PRIMARY_COMP_ARC)
							|| key.contains(RenewalsConstant.PRIMARY_COMP_EUC)) {
						key = key.replace(RenewalsConstant.PRIMARY_COMP_MRC, "")
								.replace(RenewalsConstant.PRIMARY_COMP_NRC, "")
								.replace(RenewalsConstant.PRIMARY_COMP_ARC, "")
								.replace(RenewalsConstant.PRIMARY_COMP_EUC, "").trim();
						if (!listOfComponentsExcelPrimary.contains(key)) {
							listOfComponentsExcelPrimary.add(key);
						}
					}
				}

				listOfComponentsExcelPrimary.removeAll(listOfComponentsPrimary);
				if (!listOfComponentsExcelPrimary.isEmpty()) {
					LOGGER.info("Adding Components From Excel");
					for (String mapElement1 : listOfComponentsExcelPrimary) {
						LOGGER.info("Adding Components Primary From Excel, Component Name is -->{}",
								mapElement1.trim());
						if (mapElement1.equalsIgnoreCase(RenewalsConstant.NATIONAL_CONNECTIVITY)) {
							RenewalsComponentDetail component = new RenewalsComponentDetail();
							component.setName(mapElement1.trim());
							component.setType(RenewalsConstant.LINK);
							component.setAttributes(new ArrayList<RenewalsAttributeDetail>());
							quoteDetail.getSolutions().get(i).getComponents().add(component);
						}

						if (mapElement1.equalsIgnoreCase(RenewalsConstant.LINK_MANAGEMENT_CHARGES)) {
							RenewalsComponentDetail component = new RenewalsComponentDetail();
							component.setName(mapElement1.trim());
							component.setType(RenewalsConstant.LINK);
							component.setAttributes(new ArrayList<RenewalsAttributeDetail>());
							quoteDetail.getSolutions().get(i).getComponents().add(component);
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
		obj.setName(RenewalsConstant.MAST_COST);
		obj.setValue("");
		return obj;
	}

	public RenevalsIllSiteBean constructQutePrice(List<QuoteNplLink> nplLinkListWithId, Map<Integer, String> productSolutionSiLinkMap,
			Map<String, Map<String, Object>> serviceIdPriceMapping, MstProductFamily mstProductFamily, Integer quoteId)
			throws TclCommonException {
		RenevalsIllSiteBean renevalsIllSiteBean = new RenevalsIllSiteBean();
		List<QuotePrice> quotePriceList = new ArrayList<QuotePrice>();
		Map<Integer, RenewalsPriceBean> illSitePrice = new HashMap<Integer, RenewalsPriceBean>();
		Double quoteTotalMrc = 0D;
		Double quoteTotalNrc = 0D;
		Double quoteTotalArc = 0D;

		for (QuoteNplLink quoteNpl : nplLinkListWithId) {
			RenewalsPriceBean renewalsPriceBean = new RenewalsPriceBean();
			Map<String, Object> mappingexcel = serviceIdPriceMapping
					.get(productSolutionSiLinkMap.get(quoteNpl.getProductSolutionId()));
			List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
					.findByReferenceId(quoteNpl.getId());
//			List<QuoteProductComponent> quoteProductComponentLista = quoteProductComponentRepository
//					.findByReferenceId(quoteNpl.getSiteAId());
//			List<QuoteProductComponent> quoteProductComponentListb = quoteProductComponentRepository
//					.findByReferenceId(quoteNpl.getSiteBId());
//			quoteProductComponentList.addAll(quoteProductComponentLista);
//			quoteProductComponentList.addAll(quoteProductComponentListb);
			for (QuoteProductComponent quoteProductComponent : quoteProductComponentList) {

				MstProductComponent mstprd = quoteProductComponent.getMstProductComponent();
				String productName = mstprd.getName();
				if (productName.equalsIgnoreCase(RenewalsConstant.NATIONAL_CONNECTIVITY)
						|| productName.equalsIgnoreCase(RenewalsConstant.LINK_MANAGEMENT_CHARGES)) {
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
				} else {
					/*
					 * QuotePrice quotePrice = new QuotePrice(); quotePrice.setQuoteId(quoteId);
					 * quotePrice.setMstProductFamily(mstProductFamily);
					 * quotePrice.setReferenceName(RenewalsConstant.COMPONENTS);
					 * quotePrice.setReferenceId(quoteProductComponent.getId().toString());
					 * renewalsPriceBean.setMrc(0D); renewalsPriceBean.setNrc(0D);
					 * renewalsPriceBean.setArc(0D);
					 */}
			}
			illSitePrice.put(quoteNpl.getId(), renewalsPriceBean);

			quoteTotalMrc = roundOff(quoteTotalMrc + renewalsPriceBean.getMrc());
			quoteTotalNrc = roundOff(quoteTotalNrc + renewalsPriceBean.getNrc());
			quoteTotalArc = roundOff(quoteTotalArc + renewalsPriceBean.getArc());
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
		// LOGGER.info("Processing Component Price-->{}", productName);
		Double mrc;
		Double nrc;
		Double arc;
	
			mrc = (Double)mappingexcel.get(productName + RenewalsConstant.PRIMARY_COMP_MRC_SPACE);
			nrc = (Double)mappingexcel.get(productName + RenewalsConstant.PRIMARY_COMP_NRC_SPACE);
			arc = (Double)mappingexcel.get(productName + RenewalsConstant.PRIMARY_COMP_ARC_SPACE);
			// LOGGER.info("Primary Component Name-->{ Mrc --> {}, NRC --> {}, ARC -->{} }",
			// productName, mrc, nrc, arc);
		

		quotePrice.setEffectiveMrc(mrc != null ? mrc : 0D);
		quotePrice.setEffectiveNrc(nrc != null ? nrc : 0D);
		quotePrice.setEffectiveArc(arc != null ? arc : 0D);

		return quotePrice;
	}

	public QuotePrice constructAtributePrice(Map<String, Double> mappingexcel, String attributeNametName,
			QuotePrice quotePrice, String type) {
		// LOGGER.info("Processing Attribute Price-->{}", attributeNametName);
		Double eucA;
		if (type.equalsIgnoreCase(RenewalsConstant.PRIMARY)) {
			eucA = mappingexcel.get(attributeNametName + RenewalsConstant.PRIMARY_ATTR_EUC_SPACE);
			// LOGGER.info("Primary Attribute Name-->{ Mrc --> {}, NRC --> {}, ARC -->{},
			// EUC-->{} }", attributeNametName,
			// eucA);
		} else {
			eucA = mappingexcel.get(attributeNametName + RenewalsConstant.SECONDARY_ATTR_EUC_SPACE);
			// LOGGER.info("Primary Attribute Name-->{ Mrc --> {}, NRC --> {}, ARC -->{},
			// EUC-->{} }", attributeNametName,
			// eucA);
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

	public List<QuoteNplLink> constructIllLinkSite(List<QuoteIllSite> quteIllSiteListWithId,
			RenewalsQuoteDetail rQuoteDetail, User user, Integer quoteId) {
		List<RenewalsSite> renewalsSiteList = rQuoteDetail.getSite();
		List<QuoteNplLink> quoteNplLink = new ArrayList<QuoteNplLink>();

		for (int i = 0; i < quteIllSiteListWithId.size(); i = i + 2) {
			if (i + 1 < quteIllSiteListWithId.size()) {
				Integer siteAId = quteIllSiteListWithId.get(i).getId();
				Integer siteBId = quteIllSiteListWithId.get(i + 1).getId();
				String siteAType = SiteTypeConstants.SITE.toString();
				String siteBType = SiteTypeConstants.SITE.toString();
				quoteNplLink.add(constructNplLinks(user, quoteId, siteAId, siteBId, siteAType,
						siteBType, quteIllSiteListWithId.get(i).getProductSolution().getId()));
				
			}
		}
		return quoteNplLink;
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
	  //    break;
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