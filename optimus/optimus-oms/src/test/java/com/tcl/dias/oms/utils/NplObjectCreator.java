package com.tcl.dias.oms.utils;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.Attributes;
import com.tcl.dias.common.beans.CustomerAttributeBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.CustomerLeBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.OmsAttachBean;
import com.tcl.dias.common.beans.OmsListenerBean;
import com.tcl.dias.common.beans.ProductSlaBean;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.SLaDetailsBean;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.sfdc.bean.ProductServiceBean;
import com.tcl.dias.common.sfdc.bean.SiteSolutionOpportunityBean;
import com.tcl.dias.common.sfdc.response.bean.OpportunityResponse;
import com.tcl.dias.common.sfdc.response.bean.OpportunityResponseBean;
import com.tcl.dias.common.sfdc.response.bean.ProductServicesResponseBean;
import com.tcl.dias.common.sfdc.response.bean.ProductsserviceResponse;
import com.tcl.dias.common.sfdc.response.bean.SiteLocationResponse;
import com.tcl.dias.common.sfdc.response.bean.SiteResponseBean;
import com.tcl.dias.common.sfdc.response.bean.StagingResponseBean;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.BillingAttributesBean;
import com.tcl.dias.oms.beans.BillingRequest;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.ExcelBean;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.OrderIllSiteBean;
import com.tcl.dias.oms.beans.OrderLinkRequest;
import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.beans.OrderSiteRequest;
import com.tcl.dias.oms.beans.OrderSlaBean;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.beans.QuoteSlaBean;
import com.tcl.dias.oms.beans.SiteFeasibilityBean;
import com.tcl.dias.oms.beans.SlaMasterBean;
import com.tcl.dias.oms.beans.SlaUpdateRequest;
import com.tcl.dias.oms.beans.SolutionDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.OrderConstants;
import com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants;
import com.tcl.dias.oms.dto.CreateDocumentDto;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.CurrencyConversion;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.DocusignAudit;
import com.tcl.dias.oms.entity.entities.Engagement;
import com.tcl.dias.oms.entity.entities.LeProductSla;
import com.tcl.dias.oms.entity.entities.LinkFeasibility;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.MstOrderLinkStage;
import com.tcl.dias.oms.entity.entities.MstOrderLinkStatus;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStage;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStatus;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderConfirmationAudit;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIllSiteSla;
import com.tcl.dias.oms.entity.entities.OrderLinkFeasibility;
import com.tcl.dias.oms.entity.entities.OrderLinkStageAudit;
import com.tcl.dias.oms.entity.entities.OrderLinkStatusAudit;
import com.tcl.dias.oms.entity.entities.OrderNplLink;
import com.tcl.dias.oms.entity.entities.OrderNplLinkSla;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderSiteFeasibility;
import com.tcl.dias.oms.entity.entities.OrderSiteStageAudit;
import com.tcl.dias.oms.entity.entities.OrderSiteStatusAudit;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteDelegation;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteSla;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuoteNplLinkSla;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.SfdcJob;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.entities.SlaMaster;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.npl.beans.LinkFeasibilityBean;
import com.tcl.dias.oms.npl.beans.NplLinkBean;
import com.tcl.dias.oms.npl.beans.NplLinkSitesBean;
import com.tcl.dias.oms.npl.beans.NplLinksUpdateBean;
import com.tcl.dias.oms.npl.beans.NplQuoteBean;
import com.tcl.dias.oms.npl.beans.NplQuoteDetail;
import com.tcl.dias.oms.npl.beans.NplSite;
import com.tcl.dias.oms.npl.beans.NplSiteDetail;
import com.tcl.dias.oms.npl.beans.NplUpdateRequest;
import com.tcl.dias.oms.npl.beans.OrderNplSiteBean;
import com.tcl.dias.oms.npl.beans.OrderProductSolutionBean;
import com.tcl.dias.oms.npl.beans.ProductSolutionBean;
import com.tcl.dias.oms.npl.beans.QuoteNplSiteBean;
import com.tcl.dias.oms.npl.beans.QuoteToLeBean;
import com.tcl.dias.oms.npl.beans.QuoteToLeProductFamilyBean;
import com.tcl.dias.oms.npl.constants.SiteTypeConstants;
import com.tcl.dias.oms.npl.pricing.bean.FeasibilityBean;
import com.tcl.dias.oms.npl.pricing.bean.FeasibilitySiteEngineBean;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.pricing.bean.NotFeasible;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;


/**
 * 
 * This file contains the NplObjectCreator.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class NplObjectCreator {

	QuoteToLe quoteToLe = new QuoteToLe();

	/**
	 * 
	 * getQuoteDetail-mock values
	 * 
	 * @return QuoteDetail
	 */
	public NplQuoteDetail getNplQuoteDetailNew() {
		NplQuoteDetail quoteDetail = new NplQuoteDetail();
		/* quoteDetail.toString(); */
		quoteDetail.setCustomerId(1);
		quoteDetail.setSite(getSite());
		quoteDetail.setProductName("NPL");
		quoteDetail.setQuoteleId(1);
		quoteDetail.setQuoteId(2);
		List<SolutionDetail> solutionDetails = new ArrayList<>();
		SolutionDetail solution = new SolutionDetail();
		solution.setBandwidth("20");
		solution.setBandwidthUnit("mbps");
		/* solution.toString(); */
		List<ComponentDetail> components = new ArrayList<>();
		ComponentDetail component = new ComponentDetail();
		/* component.toString(); */
		components.add(component);
		List<AttributeDetail> attributeDetails = new ArrayList<>();
		AttributeDetail attributeDetail = new AttributeDetail();
		attributeDetail.setName("Model No");
		attributeDetail.setValue("CISCO");
		/* attributeDetail.toString(); */
		attributeDetails.add(attributeDetail);
		component.setAttributes(attributeDetails);
		component.setIsActive("Y");
		component.setName("CPE");
		solution.setComponents(components);
		solution.setImage("");
		// solution.toString();
		solution.setOfferingName("new");
		solutionDetails.add(solution);
		quoteDetail.setSolutions(solutionDetails);
		return quoteDetail;
	}

	public Map<String, Object> getUSerInfp() {
		UserInformation value = new UserInformation();
		value.setUserId("1");
		value.setCustomers(getCustomerList());
		Map<String, Object> map = new HashMap<>();
		map.put("USER_INFORMATION", value);

		return map;
	}

	/**
	 * 
	 * getQuoteDetail-mock values
	 * 
	 * @return QuoteDetail
	 */
	public NplQuoteDetail getNplQuoteDetail() {
		NplQuoteDetail quoteDetail = new NplQuoteDetail();
		/* quoteDetail.toString(); */
		quoteDetail.setCustomerId(1);
		quoteDetail.setSite(getSite());
		quoteDetail.setProductName("IAS");
		quoteDetail.setSite(getSite());
		quoteDetail.setQuoteleId(1);
		quoteDetail.setQuoteId(1);
		quoteDetail.setQuoteId(2);
		List<SolutionDetail> solutionDetails = new ArrayList<>();
		SolutionDetail solution = new SolutionDetail();
		solution.setBandwidth("20");
		solution.setBandwidthUnit("mbps");
		/* solution.toString(); */
		List<ComponentDetail> components = new ArrayList<>();
		ComponentDetail component = new ComponentDetail();
		/*
		 * component.toString();
		 */ components.add(component);
		List<AttributeDetail> attributeDetails = new ArrayList<>();
		AttributeDetail attributeDetail = new AttributeDetail();
		attributeDetail.setName("Model No");
		attributeDetail.setValue("CISCO");
		/*
		 * attributeDetail.toString();
		 */ attributeDetails.add(attributeDetail);
		component.setAttributes(attributeDetails);
		component.setIsActive("Y");
		component.setName("CPE");
		solution.setComponents(components);
		solution.setImage("");
		/* solution.toString(); */
		solution.setOfferingName("Vive");
		solutionDetails.add(solution);
		quoteDetail.setSolutions(solutionDetails);

		return quoteDetail;
	}

	public NplQuoteDetail getNplQuoteDetail2() {
		NplQuoteDetail quoteDetail = new NplQuoteDetail();
		quoteDetail.setProductName("IAS");
		List<SolutionDetail> solutionDetails = new ArrayList<>();
		SolutionDetail solution = new SolutionDetail();
		solution.setBandwidth("20");
		solution.setBandwidthUnit("mbps");
		/* solution.toString(); */
		List<ComponentDetail> components = new ArrayList<>();
		ComponentDetail component = new ComponentDetail();
		/* component.toString(); */
		components.add(component);
		List<AttributeDetail> attributeDetails = new ArrayList<>();
		AttributeDetail attributeDetail = new AttributeDetail();
		attributeDetail.setName("Model No");
		attributeDetail.setValue("CISCO");
		/* attributeDetail.toString(); */
		attributeDetails.add(attributeDetail);
		component.setAttributes(attributeDetails);
		component.setIsActive("Y");
		component.setName("CPE");
		solution.setComponents(components);
		solution.setImage("");
		/* solution.toString(); */
		solution.setOfferingName("Vive");
		solutionDetails.add(solution);
		quoteDetail.setSolutions(solutionDetails);
		return quoteDetail;
	}

	/**
	 * 
	 * getQuoteDetail-mock values
	 * 
	 * @return QuoteDetail
	 */
	public NplQuoteDetail getNplQuoteDetailWithoutSiteID() {
		NplQuoteDetail quoteDetail = new NplQuoteDetail();
		/* quoteDetail.toString(); */
		quoteDetail.setCustomerId(1);
		quoteDetail.setSite(getSiteWithoutId());
		quoteDetail.setProductName("IAS");
		quoteDetail.setSite(getSiteWithoutId());
		quoteDetail.setQuoteleId(1);
		List<SolutionDetail> solutionDetails = new ArrayList<>();
		SolutionDetail solution = new SolutionDetail();
		solution.setBandwidth("20");
		solution.setBandwidthUnit("mbps");
		/* solution.toString(); */
		List<ComponentDetail> components = new ArrayList<>();
		ComponentDetail component = new ComponentDetail();
		/* component.toString(); */
		components.add(component);
		List<AttributeDetail> attributeDetails = new ArrayList<>();
		AttributeDetail attributeDetail = new AttributeDetail();
		attributeDetail.setName("Model No");
		attributeDetail.setValue("CISCO");
		/* attributeDetail.toString(); */
		attributeDetails.add(attributeDetail);
		component.setAttributes(attributeDetails);
		component.setIsActive("Y");
		component.setName("CPE");
		solution.setComponents(components);
		solution.setImage("");
		/* solution.toString(); */
		solution.setOfferingName("Single Internet Access");
		solutionDetails.add(solution);
		quoteDetail.setSolutions(solutionDetails);
		return quoteDetail;
	}

	/**
	 * getSite-mock values
	 * 
	 * @return List<Site>
	 */
	private List<NplSite> getSiteWithoutId() {
		List<NplSite> siList = new ArrayList<>();
		NplSiteDetail siteDetail = new NplSiteDetail();
		siteDetail.setLocationId(1);
		List<ComponentDetail> components = new ArrayList<>();
		ComponentDetail component = new ComponentDetail();
		/* component.toString(); */
		components.add(component);
		List<AttributeDetail> attributeDetails = new ArrayList<>();
		AttributeDetail attributeDetail = new AttributeDetail();
		attributeDetail.setName("Model No");
		attributeDetail.setValue("CISCO");
		/* attributeDetail.toString(); */
		attributeDetails.add(attributeDetail);
		component.setAttributes(attributeDetails);
		component.setIsActive("Y");
		component.setName("CPE");
		siteDetail.setComponents(components);
		NplSite site = new NplSite();
		site.setBandwidth("f");
		site.setOfferingName("IAS");
		site.setBandwidthUnit("daf");
		site.setImage("adgdag");
		site.setType(SiteTypeConstants.SITE);
		/* site.toString(); */
		site.setSite(getSitDetailsWithSiteIdEmpty());
		siList.add(site);

		return siList;
	}

	/**
	 * getSite-mock values
	 *
	 * @return List<Site>
	 */
	public List<NplSite> getSite() {
		List<NplSite> siList = new ArrayList<>();
		NplSiteDetail siteDetail = new NplSiteDetail();
		siteDetail.setSiteId(1);
		siteDetail.setLocationId(1);
		List<ComponentDetail> components = new ArrayList<>();
		ComponentDetail component = new ComponentDetail();
		/* component.toString(); */
		components.add(component);
		List<AttributeDetail> attributeDetails = new ArrayList<>();
		AttributeDetail attributeDetail = new AttributeDetail();
		attributeDetail.setName("Model No");
		attributeDetail.setValue("CISCO");
		/* attributeDetail.toString(); */
		attributeDetails.add(attributeDetail);
		component.setAttributes(attributeDetails);
		component.setIsActive("Y");
		component.setName("CPE");
		siteDetail.setComponents(components);
		NplSite site = new NplSite();
		site.setBandwidth("f");
		site.setOfferingName("IAS");
		site.setBandwidthUnit("daf");
		site.setImage("adgdag");
		site.setType(SiteTypeConstants.SITE);
		/* site.toString(); */
		site.setSite(getSitDetails());
		siList.add(site);

		return siList;
	}
	/**
	 * getSite-mock values
	 * 
	 * @return List<Site>
	 */
	private List<NplSite> getNplSite() {
		List<NplSite> siList = new ArrayList<>();
		/*
		 * SiteDetail siteDetail = new SiteDetail(); siteDetail.setSiteId(1);
		 * siteDetail.setLocationId(1); List<ComponentDetail> components = new
		 * ArrayList<>(); ComponentDetail component = new ComponentDetail();
		 * component.toString(); components.add(component);
		 * List<AttributeDetail> attributeDetails = new ArrayList<>();
		 * AttributeDetail attributeDetail = new AttributeDetail();
		 * attributeDetail.setName("Model No");
		 * attributeDetail.setValue("CISCO"); attributeDetail.toString();
		 * attributeDetails.add(attributeDetail);
		 * component.setAttributes(attributeDetails);
		 * component.setIsActive("Y"); component.setName("CPE");
		 * siteDetail.setComponents(components);
		 */
		NplSite site = new NplSite();
		site.setBandwidth("f");
		site.setOfferingName("Buy India Point to Point Connectivity-2");
		site.setBandwidthUnit("daf");
		site.setImage("adgdag");
		site.setType(SiteTypeConstants.SITE);
		/*
		 * site.toString();
		 */ site.setSite(getSitDetails());
		NplSite site1 = new NplSite();
		site1.setBandwidth("f");
		site1.setOfferingName("Buy India Point to Point Connectivity-2");
		site1.setBandwidthUnit("daf");
		site1.setImage("adgdag");
		site1.setType(SiteTypeConstants.SITE);
		/* site1.toString(); */
		site1.setSite(getSitDetails());
		siList.add(site);
		siList.add(site1);

		return siList;
	}

	/**
	 * getSitDetails
	 * 
	 * @return
	 */
	private List<NplSiteDetail> getSitDetails() {
		List<NplSiteDetail> siteDetails = new ArrayList<>();
		NplSiteDetail detail = new NplSiteDetail();
		detail.setSiteId(1);
		detail.setLocationId(1);
		detail.setLocationCode("123");
		detail.setComponents(getcompDetails());
		siteDetails.add(detail);
		return siteDetails;
	}

	private List<NplSiteDetail> getSitDetailsWithSiteIdEmpty() {
		List<NplSiteDetail> siteDetails = new ArrayList<>();
		NplSiteDetail detail = new NplSiteDetail();
		detail.setLocationId(1);
		detail.setLocationCode("123");
		detail.setComponents(getcompDetails());
		siteDetails.add(detail);
		return siteDetails;
	}

	/**
	 * getcompDetails
	 * 
	 * @return
	 */
	private List<ComponentDetail> getcompDetails() {
		List<ComponentDetail> componentDetails = new ArrayList<>();
		ComponentDetail componentDetail = new ComponentDetail();
		componentDetail.setComponentId(1);
		componentDetail.setAttributes(getAttributes());
		componentDetail.setIsActive("Y");
		componentDetail.setName("CPE");
		componentDetails.add(componentDetail);
		return componentDetails;
	}

	/**
	 * getAttribute
	 * 
	 * @return
	 */
	private List<AttributeDetail> getAttributes() {
		List<AttributeDetail> arAttributeDetails = new ArrayList<>();
		AttributeDetail attributeDetail = new AttributeDetail();
		attributeDetail.setAttributeId(1);
		attributeDetail.setName("Up time");
		attributeDetail.setValue("Valse");
		arAttributeDetails.add(attributeDetail);
		return arAttributeDetails;
	}

	/**
	 * 
	 * getIllSitesId-mock values
	 * 
	 * @return Integer
	 */
	public List<Integer> getIllSitesId() {
		List<Integer> illsitesIds = new ArrayList<>();
		illsitesIds.add(1);
		illsitesIds.add(2);
		return illsitesIds;

	}

	/**
	 * 
	 * getDocumentDto-mock values
	 * 
	 * @return {@link CreateDocumentDto}
	 */
	public CreateDocumentDto getDocumentDto1() {
		CreateDocumentDto documentDto = new CreateDocumentDto();
		documentDto.setCustomerId(1);
		documentDto.setCustomerLegalEntityId(1);
		documentDto.setIllSitesIds(getIllSitesId());
		documentDto.setCustomerId(1);
		documentDto.setSupplierLegalEntityId(1);
		documentDto.setQuoteId(1);
		documentDto.setQuoteLeId(1);
		/* documentDto.toString(); */
		documentDto.setIllsiteId(1);
		documentDto.setQuoteLeId(1);
		documentDto.setFamilyName("NPL");
		return documentDto;
	}

	public CreateDocumentDto getDocumentDto() {
		CreateDocumentDto documentDto = new CreateDocumentDto();
		documentDto.setCustomerId(1);
		documentDto.setCustomerLegalEntityId(1);
		documentDto.setIllSitesIds(getIllSitesId());
		documentDto.setCustomerId(1);
		documentDto.setSupplierLegalEntityId(1);
		documentDto.setQuoteId(1);
		documentDto.setQuoteLeId(1);
		documentDto.toString();
		documentDto.setIllsiteId(1);
		documentDto.setQuoteLeId(1);
		documentDto.setFamilyName("ILL");

		return documentDto;
	}

	/**
	 * 
	 * getQuote-mock values
	 * 
	 * @return {@link Quote}
	 */
	public Quote getQuote() {
		Quote quote = new Quote();

		quote.setId(1);
		quote.setQuoteCode("TCXVE23CV");
		quote.setCreatedBy(0);
		quote.setEffectiveDate(new Date());
		for (QuoteToLe quoteToLe : getQuotesToLead()) {
			quoteToLe.setQuote(quote);
		}
		quote.setQuoteToLes(getQuotesToLead());
		quote.setStatus((byte) 1);
		// quote.toString();
		quote.setTermInMonths(12);

		Customer customer = new Customer();
		customer.setCustomerCode("123");
		customer.setStatus((byte) 0);
		customer.setId(1);
		customer.setCustomerEmailId("vick@gmail.com");
		customer.setCustomerName("Vivek");
		// customer.toString();
		customer.setUsers(getUserEntity());
		quote.setCustomer(customer);

		quote.setCustomer(getCustomerWithOutQuote());
		return quote;
	}

	/**
	 * 
	 * getQuote-mock values
	 * 
	 * @return {@link Quote}
	 */
	public List<Quote> getQuoteList() {
		List<Quote> listQuote = new ArrayList<>();
		listQuote.add(getQuote());
		return listQuote;
	}

	/**
	 * 
	 * getQuote-mock values
	 * 
	 * @return {@link Quote}
	 */
	public Quote getQuote1() {
		Quote quote = new Quote();
		quote.setId(1);
		quote.setCreatedBy(0);
		quote.setEffectiveDate(new Date());
		quote.setStatus((byte) 1);
		quote.toString();
		quote.setTermInMonths(12);
		quote.setQuoteCode("1");
		return quote;
	}

	public Quote getQuote2() {
		Quote quote = new Quote();
		quote.setId(1);
		quote.setCreatedBy(0);
		quote.setEffectiveDate(new Date());
		quote.setQuoteToLes(getQuotesToLead2());
		quote.setStatus((byte) 1);
		quote.toString();
		quote.setTermInMonths(12);
		quote.setCustomer(getCustomer());
		quote.setQuoteToLes(getQuotesToLead());
		return quote;
	}

	/**
	 * getCustomer-mock values
	 * 
	 * @return {@link Customer}
	 */
	public Customer getCustomer() {
		Customer customer = new Customer();
		customer.setCustomerCode("123");
		customer.setStatus((byte) 0);
		customer.setId(1);
		customer.setCustomerEmailId("vick@gmail.com");
		customer.setErfCusCustomerId(1);
		customer.setCustomerName("Vivek");
		// customer.toString();
		customer.setUsers(getUserEntity());
		Set<Quote> set = new HashSet<>();
		set.add(getQuote());
		customer.setQuotes(set);
		return customer;
	}

	/**
	 * getCustomer-mock values
	 * 
	 * @return {@link Customer}
	 */
	public Customer getCustomerWithOutQuote() {
		Customer customer = new Customer();
		customer.setCustomerCode("123");
		customer.setStatus((byte) 0);
		customer.setId(1);
		customer.setCustomerEmailId("vick@gmail.com");
		customer.setCustomerName("Vivek");
		customer.toString();
		customer.setUsers(getUserEntity());
		customer.setErfCusCustomerId(1);
		return customer;
	}

	/**
	 * 
	 * getQuoteToLe-mock values
	 * 
	 * @return {@link QuoteToLe}
	 */
	public QuoteToLe getQuoteToLe() {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setId(1);
		quoteToLe.setCurrencyId(1);
		quoteToLe.setFinalMrc(45D);
		quoteToLe.setFinalNrc(67D);
		quoteToLe.setProposedMrc(78D);
		/* quoteToLe.toString(); */
		quoteToLe.setProposedNrc(98D);
		quoteToLe.setQuote(getQuote());
		quoteToLe.setErfCusCustomerLegalEntityId(1);
		quoteToLe.setErfCusSpLegalEntityId(1);
		quoteToLe.setQuoteToLeProductFamilies(getQuoteFamilesWithoutProductSolutions());
		// quoteToLe.setStage("Get Quote");
		quoteToLe.setStage("Add Locations");
		quoteToLe.setQuoteToLeProductFamilies(getQuoteToLeProductFamily());
		return quoteToLe;
	}

	public QuoteToLe getQuoteToLeWithOutSolutions() {

		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setId(1);
		quoteToLe.setCurrencyId(1);
		quoteToLe.setFinalMrc(45D);
		quoteToLe.setFinalNrc(67D);
		quoteToLe.setProposedMrc(78D);
		/* quoteToLe.toString(); */
		quoteToLe.setProposedNrc(98D);
		quoteToLe.setQuote(getQuote());

		quoteToLe.setErfCusCustomerLegalEntityId(1);
		quoteToLe.setErfCusSpLegalEntityId(1);
		quoteToLe.setQuoteToLeProductFamilies(getQuoteFamilesWithoutProductSolutions());
		// quoteToLe.setStage("Get Quote");
		quoteToLe.setStage("Add Locations");

		return quoteToLe;
	}

	public QuoteToLe getNplQuoteToLe() {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setId(1);
		quoteToLe.setCurrencyId(1);
		quoteToLe.setFinalMrc(45D);
		quoteToLe.setFinalNrc(67D);
		quoteToLe.setProposedMrc(78D);
		/* quoteToLe.toString(); */
		quoteToLe.setProposedNrc(98D);
		quoteToLe.setQuote(getQuote());

		quoteToLe.setQuoteToLeProductFamilies(getQuoteToLeProductFamily1());

		quoteToLe.setErfCusCustomerLegalEntityId(1);
		quoteToLe.setErfCusSpLegalEntityId(1);

		return quoteToLe;
	}

	public Optional<QuoteToLe> getOptionalQuoteToLe() {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setId(1);
		quoteToLe.setCurrencyId(1);
		quoteToLe.setFinalMrc(45D);
		quoteToLe.setFinalNrc(67D);
		quoteToLe.setProposedMrc(78D);

		quoteToLe.setQuote(getQuote());
		quoteToLe.setProposedNrc(98D);
		quoteToLe.setQuote(getQuote());
		quoteToLe.setTpsSfdcOptyId("SFDCID");
		Optional<QuoteToLe> quoteLe = Optional.of(quoteToLe);

		return quoteLe;
	}

	/**
	 * 
	 * getQuoteToLe-mock values
	 * 
	 * @return {@link QuoteToLe}
	 */
	public QuoteToLe getQuoteToLe1() {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setId(1);
		quoteToLe.setCurrencyId(1);
		quoteToLe.setFinalMrc(45D);
		quoteToLe.setFinalNrc(67D);
		quoteToLe.setProposedMrc(78D);
		quoteToLe.toString();
		quoteToLe.setProposedNrc(98D);
		quoteToLe.setQuoteToLeProductFamilies(getQuoteFamilesWithoutProductSolutions());
		quoteToLe.setQuote(getQuote1());
		quoteToLe.setQuoteLeAttributeValues(getQuoteLeAttributeValueSet());
		return quoteToLe;
	}

	/**
	 * 
	 * getQuotesToLead-mock values
	 * 
	 * @return {@link QuoteToLe}
	 */
	public Set<QuoteToLe> getQuotesToLead() {
		Set<QuoteToLe> mockQuotesSet = new HashSet<>();
		quoteToLe.setCurrencyId(1);
		quoteToLe.setFinalMrc(3.5D);
		quoteToLe.setFinalNrc(34.5D);
		quoteToLe.setProposedMrc(23.6D);
		quoteToLe.setId(2);
		quoteToLe.setFinalNrc(67.4D);
		quoteToLe.setProposedMrc(78.6D);
		quoteToLe.setProposedNrc(98.7D);

		quoteToLe.setQuoteToLeProductFamilies(getQuoteFamiles());
		quoteToLe.setStage("Order Form");

		quoteToLe.setStage("Order Form");
		
		
		mockQuotesSet.add(quoteToLe);

		return mockQuotesSet;
	}

	/**
	 * 
	 * getQuotesToLead-mock values
	 * 
	 * @return {@link QuoteToLe}
	 */
	public Set<QuoteToLe> getQuotesToLead2() {
		Set<QuoteToLe> mockQuotesSet = new HashSet<>();
		quoteToLe.setCurrencyId(1);
		quoteToLe.setFinalMrc(3.5D);
		quoteToLe.setFinalNrc(34.5D);
		quoteToLe.setProposedMrc(23.6D);
		quoteToLe.setId(2);
		quoteToLe.setFinalNrc(67.4D);
		quoteToLe.setProposedMrc(78.6D);
		quoteToLe.setProposedNrc(98.7D);
		quoteToLe.setQuoteToLeProductFamilies(getQuoteFamiles());
		quoteToLe.setStage("Order Form");
		quoteToLe.setTpsSfdcOptyId("-1");
		quoteToLe.setQuote(getQuote1());
		return mockQuotesSet;
	}

	/**
	 * getQuoteFamiles-mock values
	 * 
	 * @return {@link QuoteToLeProductFamily}
	 */
	public Set<QuoteToLeProductFamily> getQuoteFamiles() {
		Set<QuoteToLeProductFamily> mockQoteFamily = new HashSet<>();
		QuoteToLeProductFamily quoTefamily = new QuoteToLeProductFamily();
		quoTefamily.setId(1);
		/* quoTefamily.toString(); */
		quoTefamily.setProductSolutions(getProductSolution());
		quoTefamily.setMstProductFamily(getMstProductFamily());
		mockQoteFamily.add(quoTefamily);
		return mockQoteFamily;
	}

	/**
	 * 
	 * getQuoteToLeFamily-mock values
	 * 
	 * @return {@link QuoteToLeProductFamily}
	 */
	public QuoteToLeProductFamily getQuoteToLeFamily() {
		QuoteToLeProductFamily quoTefamily = new QuoteToLeProductFamily();
		quoTefamily.setId(1);
		quoTefamily.setId(1);
		/* quoTefamily.toString(); */
		quoTefamily.setProductSolutions(getProductSolution());
		quoTefamily.setMstProductFamily(getMstProductFamily());
		quoTefamily.setQuoteToLe(getQuoteToLe());
		return quoTefamily;

	}

	/**
	 * 
	 * getQuoteToLeFamily-mock values
	 * 
	 * @return {@link QuoteToLeProductFamily}
	 */
	public QuoteToLeProductFamily getQuoteToLeFamily1() {
		QuoteToLeProductFamily quoTefamily = new QuoteToLeProductFamily();
		quoTefamily.setId(1);
		quoTefamily.setId(1);
		quoTefamily.toString();
		quoTefamily.setQuoteToLe(getQuoteToLe1());
		return quoTefamily;

	}

	/**
	 * getProductSolution-mock values
	 * 
	 * @return {@link ProductSolution}
	 */
	public Set<ProductSolution> getProductSolution() {

		Set<ProductSolution> productSolutions = new HashSet<>();
		ProductSolution productSolution = new ProductSolution();
		productSolution.setId(1);
		productSolution.setProductProfileData(
				"{\"offeringName\":\"Single Internet Access\",\"image\":\"\",\"bandwidth\":\"10\",\"bandwidthUnit\":\"mbps\",\"components\":[{\"componentId\":\"1\",\"componentMasterId\":\"1\",\"name\":\"CPE Management\",\"isActive\":\"true\",\"attributes\":[{\"attributeId\":\"1\",\"attributeMasterId\":\"1\",\"name\":\"CPE Manufacturer\",\"value\":\"CISCO\"}]}]}");
		productSolution.setMstProductOffering(getMstOffering());
		productSolution.setQuoteIllSites(getIllsitesList());
		ProductSolution productSolution1 = new ProductSolution();
		productSolution1.setId(1);
		productSolution1.setProductProfileData(
				"{\"offeringName\":\"Single Internet Access\",\"image\":\"\",\"bandwidth\":\"10\",\"bandwidthUnit\":\"mbps\",\"components\":[{\"componentId\":\"1\",\"componentMasterId\":\"1\",\"name\":\"CPE Management\",\"isActive\":\"true\",\"attributes\":[{\"attributeId\":\"1\",\"attributeMasterId\":\"1\",\"name\":\"CPE Manufacturer\",\"value\":\"CISCO\"}]}]}");
		productSolution1.setMstProductOffering(getMstOffering());
		productSolution1.setQuoteIllSites(getIllsitesList());
		productSolutions.add(productSolution);
		productSolutions.add(productSolution1);
		/* productSolution.toString(); */
		return productSolutions;
	}
	private  ProductSolution getProductSolution2() {
	ProductSolution productSolution = new ProductSolution();
	productSolution.setId(1);
	productSolution.setProductProfileData(
			"{\"offeringName\":\"Single Internet Access\",\"image\":\"\",\"bandwidth\":\"10\",\"bandwidthUnit\":\"mbps\",\"components\":[{\"componentId\":\"1\",\"componentMasterId\":\"1\",\"name\":\"CPE Management\",\"isActive\":\"true\",\"attributes\":[{\"attributeId\":\"1\",\"attributeMasterId\":\"1\",\"name\":\"CPE Manufacturer\",\"value\":\"CISCO\"}]}]}");
	productSolution.setMstProductOffering(getMstOffering());
	productSolution.setQuoteIllSites(getIllsitesList());
	
	return productSolution;
	}

	/**
	 * 
	 * getSolution-mock values
	 * 
	 * @return {@link ProductSolution}
	 */
	public ProductSolution getSolution() {
		ProductSolution productSolution = new ProductSolution();
		productSolution.setId(1);
		/* productSolution.toString(); */
		productSolution.setProductProfileData(
				"{\"offeringName\":\"Single Internet Access\",\"image\":\"\",\"bandwidth\":\"10\",\"bandwidthUnit\":\"mbps\",\"components\":[{\"componentId\":\"1\",\"componentMasterId\":\"1\",\"name\":\"CPE Management\",\"isActive\":\"true\",\"attributes\":[{\"attributeId\":\"1\",\"attributeMasterId\":\"1\",\"name\":\"CPE Manufacturer\",\"value\":\"CISCO\"}]}]}");
		productSolution.setMstProductOffering(getMstOffering());
		productSolution.setQuoteIllSites(getIllsitesList());
		productSolution.setQuoteToLeProductFamily(getQuoteToLeFamily());
		productSolution.setTpsSfdcProductId("1");
		productSolution.setTpsSfdcProductName("sfdc");

		return productSolution;
	}

	/**
	 * 
	 * getSolution-mock values
	 * 
	 * @return {@link ProductSolution}
	 */
	public ProductSolution getSolution1() {
		ProductSolution productSolution = new ProductSolution();
		productSolution.setId(1);
		productSolution.setProductProfileData(
				"{\"offeringName\":\"Single Internet Access\",\"image\":\"\",\"bandwidth\":\"10\",\"bandwidthUnit\":\"mbps\",\"components\":[{\"componentId\":\"1\",\"componentMasterId\":\"1\",\"name\":\"CPE Management\",\"isActive\":\"true\",\"attributes\":[{\"attributeId\":\"1\",\"attributeMasterId\":\"1\",\"name\":\"CPE Manufacturer\",\"value\":\"CISCO\"}]}]}");

		productSolution.setQuoteToLeProductFamily(getQuoteToLeFamily1());
		return productSolution;
	}

	public ProductSolution getSolution2() {
		ProductSolution productSolution = new ProductSolution();
		productSolution.setId(1);
		productSolution.setProductProfileData(
				"{\"offeringName\":\"Single Internet Access\",\"image\":\"\",\"bandwidth\":\"10\",\"bandwidthUnit\":\"mbps\",\"components\":[{\"componentId\":\"1\",\"componentMasterId\":\"1\",\"name\":\"CPE Management\",\"isActive\":\"true\",\"attributes\":[{\"attributeId\":\"1\",\"attributeMasterId\":\"1\",\"name\":\"Service Variant\",\"value\":\"CISCO\"}]}]}");

		productSolution.setQuoteToLeProductFamily(getQuoteToLeFamily1());
		return productSolution;
	}

	/**
	 * 
	 * getIllsites-mock values
	 * 
	 * @return {@link IllSite}
	 */
	public QuoteIllSite getIllsites() {
		QuoteIllSite site = new QuoteIllSite();
		site.setId(1);
		site.setIsTaxExempted((byte) 0);
		site.setFeasibility((byte) 0);
		site.setMrc(9.8);
		site.setNrc(19.8);
		site.setStatus((byte) 1);
		site.setErfLrSolutionId("YU");
		site.setFeasibility((byte) 1);
		site.setIsTaxExempted((byte) 1);
		site.setErfLocSiteaLocationId(1);
		site.setErfLocSitebLocationId(1);

		site.setProductSolution(getSolution1());

		site.setImageUrl("https://www.google.co.in/search?q=google+image&tbm=isch&source=iu");
		site.setStatus((byte) 1);
		site.setQuoteIllSiteSlas(createQuoteIllSiteSlaSet());
		return site;
	}
	
	

	/**
	 * 
	 * getIllsites-mock values
	 * 
	 * @return {@link IllSite}
	 */
	public QuoteIllSite getIllsites1() {
		QuoteIllSite site = new QuoteIllSite();
		site.setId(1);
		site.setIsTaxExempted((byte) 0);
		site.setFeasibility((byte) 0);
		site.setMrc(9.8);
		site.setNrc(19.8);
		site.setStatus((byte) 1);
		site.setErfLrSolutionId("YU");
		site.setFeasibility((byte) 1);
		site.setIsTaxExempted((byte) 1);
		site.setErfLocSiteaLocationId(1);
		site.setErfLocSitebLocationId(1);
		site.setFpStatus("N");
		site.setImageUrl("https://www.google.co.in/search?q=google+image&tbm=isch&source=iu");
		site.setStatus((byte) 1);

		site.setErfLocSiteaLocationId(2);
		site.setErfLocSiteaSiteCode("1");
		site.setErfLocSitebLocationId(2);
		site.setQuoteIllSiteSlas(getQuoteIllSiteSlaSet());
		return site;
	}
	
	public QuoteIllSite getIllsitesNon() {
		QuoteIllSite site = new QuoteIllSite();
		site.setId(1);
		site.setIsTaxExempted((byte) 0);
		site.setFeasibility((byte) 0);
		site.setMrc(9.8);
		site.setNrc(19.8);
		site.setStatus((byte) 1);
		site.setErfLrSolutionId(String.valueOf(1));
		site.setFeasibility((byte) 0);
		site.setIsTaxExempted((byte) 1);
		site.setErfLocSiteaLocationId(1);
		site.setErfLocSitebLocationId(1);

		site.setImageUrl("https://www.google.co.in/search?q=google+image&tbm=isch&source=iu");
		site.setStatus((byte) 1);

		site.setErfLocSiteaLocationId(2);
		site.setErfLocSiteaSiteCode("1");
		site.setErfLocSitebLocationId(2);
		site.setQuoteIllSiteSlas(getQuoteIllSiteSlaSet());
		return site;
	}

	/**
	 * 
	 * getIllsites-mock values
	 * 
	 * @return {@link IllSite}
	 */
	public QuoteIllSite getIllsites2() {
		QuoteIllSite site = new QuoteIllSite();
		site.setId(1);
		site.setIsTaxExempted((byte) 0);
		site.setFeasibility((byte) 0);
		site.setMrc(9.8);
		site.setNrc(19.8);
		site.setStatus((byte) 1);
		site.setErfLrSolutionId("YU");
		site.setFeasibility((byte) 1);
		site.setIsTaxExempted((byte) 1);
		site.setImageUrl("https://www.google.co.in/search?q=google+image&tbm=isch&source=iu");
		site.setFpStatus("N");
		site.setErfLocSiteaLocationId(2);
		site.setErfLocSitebLocationId(2);
		return site;
	}

	/**
	 * 
	 * getIllsitesList-mock values
	 * 
	 * @return {@link IllSite}
	 */
	public Set<QuoteIllSite> getIllsitesList() {
		Set<QuoteIllSite> illSites = new HashSet<>();
		illSites.add(getIllsites1());
		illSites.add(getIllsites2());
		return illSites;
	}
	
	public List<QuoteIllSite> getIllsitesLists() {
		List<QuoteIllSite> illSites = new ArrayList<>();
		illSites.add(getIllsites1());
		illSites.add(getIllsites2());
		return illSites;
	}
	
	public List<QuoteIllSite> getIllsitesListsNon() {
		List<QuoteIllSite> illSites = new ArrayList<>();
		illSites.add(getIllsitesNon());
		return illSites;
	}

	public Set<QuoteIllSite> getIllsitesList1() {
		Set<QuoteIllSite> illSites = new HashSet<>();
		illSites.add(getIllsites1());
		return illSites;
	}

	/**
	 * 
	 * getIllsitesMock-mock values
	 * 
	 * @return {@link IllSite}
	 */
	public List<QuoteIllSite> getIllsitesMock() {
		List<QuoteIllSite> illSites = new ArrayList<>();
		illSites.add(getIllsites());
		return illSites;
	}

	/**
	 * 
	 * getQuoteProductComponent-mock values
	 * 
	 * @return {@link QuoteProductComponent}
	 */
	public List<QuoteProductComponent> getQuoteProductComponent() {
		List<QuoteProductComponent> quoteProductComponents = new ArrayList<>();
		
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setId(1);
		quoteProductComponent.setReferenceId(1);
		quoteProductComponent.setMstProductComponent(getMstProductComponent());
		quoteProductComponent.setMstProductFamily(geProductFamily());
		quoteProductComponent.setType("Link");
		/* quoteProductComponent.toString(); */
		quoteProductComponent.setQuoteProductComponentsAttributeValues(getQuoteComponentsAttributeValues());
		quoteProductComponent.setType("Link");
		quoteProductComponents.add(quoteProductComponent);
		
		QuoteProductComponent quoteProductComponent1 = new QuoteProductComponent();
		quoteProductComponent1.setId(1);
		quoteProductComponent1.setReferenceId(1);
		quoteProductComponent1.setMstProductComponent(getMstProductComponent());
		quoteProductComponent1.setMstProductFamily(geProductFamily());
		/* quoteProductComponent.toString(); */
		quoteProductComponent1.setQuoteProductComponentsAttributeValues(getQuoteComponentsAttributeValues());
		quoteProductComponent1.setType("Site-A");
		quoteProductComponents.add(quoteProductComponent1);
		
		QuoteProductComponent quoteProductComponent2 = new QuoteProductComponent();
		quoteProductComponent2.setId(1);
		quoteProductComponent2.setReferenceId(1);
		quoteProductComponent2.setMstProductComponent(getMstProductComponent());
		quoteProductComponent2.setMstProductFamily(geProductFamily());
		/* quoteProductComponent.toString(); */
		quoteProductComponent2.setQuoteProductComponentsAttributeValues(getQuoteComponentsAttributeValues());
		quoteProductComponent2.setType("Site-B");
		quoteProductComponents.add(quoteProductComponent2);
		
		return quoteProductComponents;
	}
	
	public List<QuoteProductComponent> getQuoteProductComponent1() {
		List<QuoteProductComponent> quoteProductComponents = new ArrayList<>();
		
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setId(1);
		quoteProductComponent.setReferenceId(1);
		quoteProductComponent.setMstProductComponent(getMstProductComponent1());
		quoteProductComponent.setMstProductFamily(geProductFamily());
		quoteProductComponent.setType("Link");
		/* quoteProductComponent.toString(); */
		quoteProductComponent.setQuoteProductComponentsAttributeValues(getQuoteComponentsAttributeValues());
		quoteProductComponent.setType("Link");
		quoteProductComponents.add(quoteProductComponent);
		
		QuoteProductComponent quoteProductComponent1 = new QuoteProductComponent();
		quoteProductComponent1.setId(1);
		quoteProductComponent1.setReferenceId(1);
		quoteProductComponent1.setMstProductComponent(getMstProductComponent4());
		quoteProductComponent1.setMstProductFamily(geProductFamily());
		/* quoteProductComponent.toString(); */
		quoteProductComponent1.setQuoteProductComponentsAttributeValues(getQuoteComponentsAttributeValues());
		quoteProductComponent1.setType("Site-A");
		quoteProductComponents.add(quoteProductComponent1);
		
		QuoteProductComponent quoteProductComponent2 = new QuoteProductComponent();
		quoteProductComponent2.setId(1);
		quoteProductComponent2.setReferenceId(1);
		quoteProductComponent2.setMstProductComponent(getMstProductComponent5());
		quoteProductComponent2.setMstProductFamily(geProductFamily());
		/* quoteProductComponent.toString(); */
		quoteProductComponent2.setQuoteProductComponentsAttributeValues(getQuoteComponentsAttributeValues());
		quoteProductComponent2.setType("Site-B");
		quoteProductComponents.add(quoteProductComponent2);
		
		return quoteProductComponents;
	}

	/**
	 * getQuoteComponentsAttributeValues-mock values
	 * 
	 * @return {@link QuoteProductComponentsAttributeValue}
	 */
	public Set<QuoteProductComponentsAttributeValue> getQuoteComponentsAttributeValues() {
		Set<QuoteProductComponentsAttributeValue> mockProductComponentAttr = new HashSet<>();

		mockProductComponentAttr.add(getAttribute());
		return mockProductComponentAttr;
	}

	public QuoteProductComponentsAttributeValue getAttribute() {
		QuoteProductComponentsAttributeValue attributeValue = new QuoteProductComponentsAttributeValue();
		attributeValue.setId(1);
		attributeValue.setDisplayValue("CPE");
		attributeValue.setAttributeValues("1");
		attributeValue.setProductAttributeMaster(getProductAtrributeMaster().get(0));
		/* attributeValue.toString(); */

		return attributeValue;
	}

	/**
	 * getProductAtrributeMaster-mock values
	 * 
	 * @return {@link ProductAttributeMaster}
	 */
	public List<ProductAttributeMaster> getProductAtrributeMaster() {
		List<ProductAttributeMaster> attributeMasters = new ArrayList<>();
		ProductAttributeMaster productAttributeMaster = new ProductAttributeMaster();
		productAttributeMaster.setName("CPE");
		productAttributeMaster.setDescription("Cpe related");
		productAttributeMaster.setStatus((byte) 1);
		productAttributeMaster.setId(1);
		attributeMasters.add(productAttributeMaster);

		return attributeMasters;
	}
	
	public List<ProductAttributeMaster> getProductAtrributeMaster(String attrName) {
		List<ProductAttributeMaster> attributeMasters = new ArrayList<>();
		ProductAttributeMaster productAttributeMaster = new ProductAttributeMaster();
		productAttributeMaster.setName(attrName);
		productAttributeMaster.setDescription(attrName);
		productAttributeMaster.setStatus((byte) 1);
		productAttributeMaster.setId(1);
		attributeMasters.add(productAttributeMaster);

		return attributeMasters;
	}

	public List<ProductAttributeMaster> getProductAtrributeMasterForNull() {
		List<ProductAttributeMaster> attributeMasters = new ArrayList<>();
		attributeMasters.add(null);
		return attributeMasters;
	}

	public ProductAttributeMaster getProductAtrributeMas() {
		ProductAttributeMaster productAttributeMaster = new ProductAttributeMaster();
		productAttributeMaster.setName("Burstable Bandwidth");
		productAttributeMaster.setDescription("Cpe related");
		productAttributeMaster.setStatus((byte) 1);
		productAttributeMaster.setId(1);

		return productAttributeMaster;
	}
	
	public ProductAttributeMaster getProductAtrributeMas1() {
		ProductAttributeMaster productAttributeMaster = new ProductAttributeMaster();
		productAttributeMaster.setName("Port Bandwidth");
		productAttributeMaster.setDescription("");
		productAttributeMaster.setStatus((byte) 1);
		productAttributeMaster.setId(1);

		return productAttributeMaster;
	}
	
	public ProductAttributeMaster getProductAtrributeMas2() {
		ProductAttributeMaster productAttributeMaster = new ProductAttributeMaster();
		productAttributeMaster.setName("Interface Type - A end");
		productAttributeMaster.setDescription("");
		productAttributeMaster.setStatus((byte) 1);
		productAttributeMaster.setId(1);

		return productAttributeMaster;
	}
	
	public ProductAttributeMaster getProductAtrributeMas3() {
		ProductAttributeMaster productAttributeMaster = new ProductAttributeMaster();
		productAttributeMaster.setName("Service Availability");
		productAttributeMaster.setDescription("");
		productAttributeMaster.setStatus((byte) 1);
		productAttributeMaster.setId(1);

		return productAttributeMaster;
	}
	
	public ProductAttributeMaster getProductAtrributeMas4() {
		ProductAttributeMaster productAttributeMaster = new ProductAttributeMaster();
		productAttributeMaster.setName("Local Loop Bandwidth");
		productAttributeMaster.setDescription("");
		productAttributeMaster.setStatus((byte) 1);
		productAttributeMaster.setId(1);

		return productAttributeMaster;
	}

	/**
	 * return mst product component lsit
	 * 
	 * @return
	 */
	public List<MstProductComponent> getMstProductComponentList() {
		List<MstProductComponent> list = new ArrayList<>();
		list.add(getMstProductComponent());
		list.add(getMstProductComponent());
		return list;
	}

	public List<MstProductComponent> getMstProductComponentListForNull() {
		List<MstProductComponent> list = new ArrayList<>();
		list.add(null);
		return list;
	}

	/**
	 * getMstProductComponent-mock values
	 * 
	 * @return {@link MstProductComponent}
	 */
	public MstProductComponent getMstProductComponent() {
		MstProductComponent component = new MstProductComponent();
		component.setDescription("ILL descriptive Value");
		component.setId(1);
		component.setName("CPE");
		// component.toString();
		return component;
	}
	
	public MstProductComponent getNationalConnectivity() {
		MstProductComponent component = new MstProductComponent();
		component.setDescription("National Connectivity");
		component.setId(1);
		component.setName("National Connectivity");
		// component.toString();
		return component;
	}
	
	public MstProductComponent getLinkManagement() {
		MstProductComponent component = new MstProductComponent();
		component.setDescription("Link Management Charges");
		component.setId(1);
		component.setName("Link Management Charges");
		// component.toString();
		return component;
	}
	
	public MstProductComponent getPrivateLines() {
		MstProductComponent component = new MstProductComponent();
		component.setDescription("Private Lines");
		component.setId(1);
		component.setName("Private Lines");
		// component.toString();
		return component;
	}
	
	public MstProductComponent getLastMile() {
		MstProductComponent component = new MstProductComponent();
		component.setDescription("Last mile");
		component.setId(1);
		component.setName("Last mile");
		// component.toString();
		return component;
	}
	
	public MstProductComponent getSiteProperties() {
		MstProductComponent component = new MstProductComponent();
		component.setDescription("SITE_PROPERTIES");
		component.setId(1);
		component.setName("SITE_PROPERTIES");
		// component.toString();
		return component;
	}

	/**
	 * getMstProductComponent-mock values
	 * 
	 * @return {@link MstProductComponent}
	 */
	public MstProductComponent getMstProductComponent1() {
		MstProductComponent component = new MstProductComponent();
		component.setDescription("Last mile");
		component.setId(1);
		component.setName("Last mile");
		component.toString();
		return component;
	}

	/**
	 * getMstProductComponent-mock values
	 * 
	 * @return {@link MstProductComponent}
	 */
	public MstProductComponent getMstProductComponent2() {
		MstProductComponent component = new MstProductComponent();
		component.setDescription("Internet Port");
		component.setId(1);
		component.setName("Internet Port");
		component.toString();
		return component;
	}
	
	/**
	 * getMstProductComponent-mock values
	 * 
	 * @return {@link MstProductComponent}
	 */
	public MstProductComponent getMstProductComponentGVPN(String name) {
		MstProductComponent component = new MstProductComponent();
		component.setDescription(name);
		component.setId(1);
		component.setName(name);
		component.toString();
		return component;
	}

	/**
	 * getMstProductComponent-mock values
	 * 
	 * @return {@link MstProductComponent}
	 */
	public MstProductComponent getMstProductComponent3() {
		MstProductComponent component = new MstProductComponent();
		component.setDescription("Addon");
		component.setId(1);
		component.setName("Addon");
		component.toString();
		return component;
	}
	
	public MstProductComponent getMstProductComponent4() {
		MstProductComponent component = new MstProductComponent();
		component.setDescription("National Connectivity");
		component.setId(1);
		component.setName("National Connectivity");
		component.toString();
		return component;
	}
	
	public MstProductComponent getMstProductComponent5() {
		MstProductComponent component = new MstProductComponent();
		component.setDescription("Link Management Charges");
		component.setId(1);
		component.setName("Link Management Charges");
		component.toString();
		return component;
	}
	
	public MstProductComponent getMstProductComponent6() {
		MstProductComponent component = new MstProductComponent();
		component.setDescription("CPE");
		component.setId(1);
		component.setName("CPE");
		component.toString();
		return component;
	}
	
	public MstProductComponent getMstProductComponent7() {
		MstProductComponent component = new MstProductComponent();
		component.setDescription("VPN port");
		component.setId(1);
		component.setName("VPN Port");
		component.toString();
		return component;
	}
	
	public MstProductComponent getMstProductComponent8() {
		MstProductComponent component = new MstProductComponent();
		component.setDescription("Additional Ip");
		component.setId(1);
		component.setName("Additional IPs");
		component.toString();
		return component;
	}
	

	/**
	 * geQuotePrice-mock values
	 * 
	 * @return {@link QuotePrice}
	 */
	public QuotePrice geQuotePrice() {

		QuotePrice quotePrice = new QuotePrice();
		quotePrice.setId(1);
		quotePrice.setCatalogMrc(90.3D);
		quotePrice.setCatalogNrc(89.6D);
		quotePrice.setReferenceName("CPE");
		quotePrice.setReferenceId(String.valueOf(1));
		quotePrice.setComputedMrc(676D);
		quotePrice.setComputedNrc(34D);
		quotePrice.setDiscountInPercent(314.7D);
		quotePrice.setQuoteId(1);
		quotePrice.setMinimumMrc(767.7D);
		quotePrice.setMinimumNrc(787.7D);
		quotePrice.setEffectiveArc(Double.parseDouble("34324"));
		quotePrice.setEffectiveMrc(Double.parseDouble("3234"));
		quotePrice.setEffectiveNrc(Double.parseDouble("234234"));
		/* quotePrice.toString(); */
		return quotePrice;
	}

	public Set<Engagement> geEngagementSet() {
		Set<Engagement> setAttri = new HashSet<>();
		setAttri.add(getEngagement());
		return setAttri;
	}

	public Engagement getEngagement() {
		Engagement engagement = new Engagement();
		engagement.setId(1);
		engagement.setEngagementName("test");
		// engagement.setMstProductFamily(getMstProductFamily());
		engagement.setStatus((byte) 1);
		return engagement;
	}

	public Set<MstProductOffering> geMstProductOfferingSet() {
		Set<MstProductOffering> setAttri = new HashSet<>();
		setAttri.add(getMstProductOffering());
		return setAttri;
	}

	public MstProductOffering getMstProductOffering() {
		MstProductOffering engagement = new MstProductOffering();
		engagement.setErfProductOfferingId(1);
		engagement.setId(1);
		engagement.setMstProductFamily(geProductFamily());
		engagement.setProductDescription("description");
		engagement.setProductName("ias");
		engagement.setProductSolutions(getProductSolution());
		engagement.setStatus((byte) 1);
		return engagement;
	}

	/**
	 * geProductFamily -mock values
	 * 
	 * @return {@link MstProductFamily}
	 */
	public MstProductFamily geProductFamily() {
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(1);
		mstProductFamily.setName("Cpe");
		mstProductFamily.setStatus((byte) 0);
		mstProductFamily.setEngagements(geEngagementSet());
		mstProductFamily.setQuoteToLeProductFamilies(getQuoteFamilesWithoutProductSolutions());
		// mstProductFamily.setMstProductOfferings(geMstProductOfferingSet());
		// mstProductFamily.setOrderPrices(getOrderPriceSet());
		// mstProductFamily.setOrderProductComponents(getOrderProductComponentSet());
		// mstProductFamily.setOrderToLeProductFamilies(createOrderToLeProductFamilySet());
		return mstProductFamily;
	}

	/**
	 * getUser -mock values
	 * 
	 * @return- getUser
	 */
	public User getUser() {
		User user = new User();
		user.setUsername("username");
		user.setCustomer(getCustomer());
		user.setUserType("sales");;
		/* user.toString(); */
		user.setId(1);
		user.setEmailId("vivek@mail.com");
		return user;
	}

	/**
	 * getMstOffering-mock values
	 * 
	 * @return {@link MstProductOffering}
	 */
	public MstProductOffering getMstOffering() {
		MstProductOffering mstProductOffering = new MstProductOffering();
		mstProductOffering.setId(1);
		mstProductOffering.setErfProductOfferingId(1);
		mstProductOffering.setProductDescription("viek");
		mstProductOffering.setProductName("Vive");
		mstProductOffering.setMstProductFamily(geProductFamily());
		return mstProductOffering;
	}

	public Customer getUserList() {
		Set<User> users = new HashSet<>();
		User user = new User();
		Customer customer = new Customer();
		user.setContactNo("1234567890");
		user.setEmailId("demo@demouser.com");
		user.setFirstName("Demo");
		user.setId(1);
		user.setLastName("User");
		user.setStatus(1);
		user.setUsername("Demo User");
		user.setUserType("OTHERS");
		user.setPartnerId(1);
		users.add(user);
		QuoteToLe quoteToLes = new QuoteToLe();
		quoteToLes.setQuote(getQuote());
		quoteToLes.setCurrencyId(1);
		quoteToLes.setErfCusCustomerLegalEntityId(1);
		quoteToLes.setFinalMrc(124.3D);
		quoteToLes.setFinalNrc(123.4D);
		quoteToLes.setId(1);
		quoteToLes.setErfCusSpLegalEntityId(1);
		quoteToLes.setQuoteToLeProductFamilies(getQuoteToLeProductFamily());
		Set<Quote> quoteSet = new HashSet<>();
		quoteSet.add(getQuote());

		customer.setCustomerCode("FEDEX");
		customer.setCustomerEmailId("enquiry@fedex.com");
		customer.setCustomerName("FedEx");
		customer.setId(1);
		customer.setStatus((byte) 2);
		customer.setUsers(users);
		customer.setQuotes(quoteSet);

		return customer;
	}

	public QuoteDelegation getQuoteDelegation() {
		QuoteDelegation quoteDelegation = new QuoteDelegation();
		quoteDelegation.setAssignTo(3);
		Timestamp timeStamp = new Timestamp(0);
		quoteDelegation.setId(1);
		quoteDelegation.setCreatedTime(timeStamp);
		quoteDelegation.setInitiatedBy(1);
		quoteDelegation.setIpAddress("127.0.0.1");
		quoteDelegation.setIsActive((byte) 1);
		quoteDelegation.setParentId(0);
		quoteDelegation.setQuoteToLe(getQuoteToLe());
		quoteDelegation.setRemarks("Remarks");
		quoteDelegation.setStatus("Rdf");
		quoteDelegation.setTargetDate(timeStamp);
		quoteDelegation.setType("TY");
		/* quoteDelegation.toString(); */
		return quoteDelegation;
	}

	public Set<QuoteToLeProductFamily> getQuoteToLeProductFamily() {
		Set<QuoteToLeProductFamily> quoteToLeProductFamilySet = new HashSet<>();
		QuoteToLeProductFamily qFamily = new QuoteToLeProductFamily();
		qFamily.setId(1);
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(1);
		mstProductFamily.setName("ILL");
		mstProductFamily.setStatus((byte) 2);
		qFamily.setMstProductFamily(mstProductFamily);
		qFamily.setProductSolutions(getProductSolution());
		quoteToLeProductFamilySet.add(qFamily);
		return quoteToLeProductFamilySet;
	}

	public Set<QuoteToLeProductFamily> getQuoteToLeProductFamily1() {
		Set<QuoteToLeProductFamily> quoteToLeProductFamilySet = new HashSet<>();
		QuoteToLeProductFamily qFamily = new QuoteToLeProductFamily();
		qFamily.setId(1);
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(1);
		mstProductFamily.setName("NPL");
		mstProductFamily.setStatus((byte) 2);
		qFamily.setMstProductFamily(mstProductFamily);
		qFamily.setProductSolutions(getProductSolution());
		quoteToLeProductFamilySet.add(qFamily);
		return quoteToLeProductFamilySet;
	}

	/**
	 * getUserEntity- user entity objects
	 * 
	 * @return List<User>
	 */
	public Set<User> getUserEntity() {
		Set<User> users = new HashSet<>();
		User user1 = new User();
		user1.setContactNo("000010101");
		Customer customer = new Customer();
		customer.setId(1);
		user1.setCustomer(customer);
		user1.setEmailId("test@e.com");
		user1.setFirstName("test");
		user1.setLastName("test");
		user1.setPartnerId(1);
		user1.setStatus(1);
		user1.setId(1);
		user1.setUsername("test");
		user1.setUserType("customer");
		users.add(user1);
		return users;
	}

	/**
	 * getUpdateRequest
	 * 
	 * @return
	 */
	public UpdateRequest getUpdateRequest() {
		UpdateRequest request = new UpdateRequest();
		request.setQuoteId(1);
		request.setQuoteToLe(1);
		request.setOrderToLeId(1);
		request.setAttributeName("testAttribute");
		request.setSiteId(1);
		request.setLocalITContactId(1);
		request.setQuoteId(1);
		request.setQuoteToLe(1);
		request.setFamilyName("test");
		request.setFamilyName("Test");
		request.getComponentDetails().addAll(getcompDetails());
		request.setAttributeDetails(getAttributes());
		request.setRequestorDate(new Timestamp(new Date().getTime()));
		request.setServiceId("service id");
		request.setCheckList("test");
		return request;
		
	}

	/**
	 * getUpdateRequestList
	 * 
	 * @return
	 */
	public NplUpdateRequest getNplUpdateRequest() {
		NplUpdateRequest nplRequest = new NplUpdateRequest();
		List<UpdateRequest> requests = new ArrayList<UpdateRequest>();
		UpdateRequest request = new UpdateRequest();
		request.setQuoteId(1);
		request.setQuoteToLe(1);
		request.setOrderToLeId(1);
		request.setAttributeName("testAttribute");
		request.setFamilyName("NPL");
		request.setSiteId(1);
		request.setLocalITContactId(1);
		request.setQuoteId(1);
		request.setQuoteToLe(1);
		request.getComponentDetails().addAll(getcompDetails());
		requests.add(request);
		nplRequest.setUpdateRequest(requests);
		return nplRequest;
	}

	/**
	 * This Returns Oms AttachmenRequest
	 * 
	 * @return
	 */
	public OmsListenerBean createOmsAttachment() {
		OmsListenerBean bean = new OmsListenerBean();
		List<OmsAttachBean> beanList = new ArrayList<>();
		OmsAttachBean request = new OmsAttachBean();

		request.setAttachmentId(1);
		request.setAttachmentType("Tax");
		request.setOrderLeId(1);
		request.setQouteLeId(1);
		request.setReferenceId(1);
		request.setReferenceName("Site");
		beanList.add(request);
		bean.setOmsAttachBean(beanList);
		return bean;
	}

	public OmsAttachment createOmsAttachMent() {

		OmsAttachment oa = new OmsAttachment();
		oa.setAttachmentType("Tax");
		oa.setErfCusAttachmentId(1);
		oa.setId(1);
		oa.setQuoteToLe(getQuoteToLe());
		oa.setOrderToLe(getOrderToLes());
		oa.setQuoteToLe(getQuoteToLe());
		oa.setReferenceId(1);

		return oa;

	}
	
	public List<OmsAttachment> createOmsAttachMentList() {

		OmsAttachment oa = new OmsAttachment();
		oa.setAttachmentType("Tax");
		oa.setErfCusAttachmentId(1);
		oa.setId(1);
		oa.setQuoteToLe(getQuoteToLe());
		oa.setOrderToLe(getOrderToLes());
		oa.setQuoteToLe(getQuoteToLe());
		oa.setReferenceId(1);
		
		List<OmsAttachment> attachments = new ArrayList<>();
		attachments.add(oa);

		return attachments;

	}


	public OmsAttachment createOmsAttachMentWithoutQuoteToLe() {

		OmsAttachment oa = new OmsAttachment();
		oa.setAttachmentType("Tax");
		oa.setErfCusAttachmentId(1);
		oa.setId(1);
		oa.setQuoteToLe(null);
		oa.setOrderToLe(null);
		oa.setQuoteToLe(null);
		oa.setReferenceId(1);

		return oa;

	}

	public OrderProductSolution getOrderProductSolution() {

		OrderProductSolution orderProductSolution = new OrderProductSolution();
		orderProductSolution.setId(1);
		orderProductSolution.setMstProductOffering(getMstOffering());
		Set<OrderIllSite> set = new HashSet<>();
		set.add(getOrderIllSite());
		orderProductSolution.setOrderIllSites(set);
		Order order = new Order();
		order.setCreatedBy(1);
		OrderToLe ole = new OrderToLe();
		ole.setOrder(order);
		OrderToLeProductFamily fam = new OrderToLeProductFamily();
		fam.setId(1);
		fam.setOrderToLe(ole);
		orderProductSolution.setOrderToLeProductFamily(fam);
		return orderProductSolution;
	}
	
	public OrderProductSolutionBean getOrderProductSolutionBean() {

		OrderProductSolutionBean orderProductSolution = new OrderProductSolutionBean();
		orderProductSolution.setId(1);
		
		return orderProductSolution;
	}
	
	public List<OrderProductSolutionBean> getOrderProductSolutionBeanList(){
		
		List<OrderProductSolutionBean> list = new ArrayList<>();
		list.add(getOrderProductSolutionBean());
		return list;
		
	}

	public MstProductFamily getMstProductFamily() {
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(5);
		mstProductFamily.setName("NPL");
		return mstProductFamily;
	}
	
	public MstProductFamily getMstProductFamilyGVPN() {
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(5);
		mstProductFamily.setName("GVPN");
		return mstProductFamily;
	}

	public OrderToLeProductFamily getorderToLeProductFamilies() {
		OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
		orderToLeProductFamily.setId(1);
		orderToLeProductFamily.setMstProductFamily(getMstProductFamily());
		Set<OrderProductSolution> set = new HashSet<>();
		set.add(getOrderProductSolution());
		orderToLeProductFamily.setOrderProductSolutions(set);
		orderToLeProductFamily.setOrderToLe(getOrderToLes1());
		return orderToLeProductFamily;
	}
	
	public OrderToLeProductFamily getorderToLeProductFamiliesGvpn() {
		OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
		orderToLeProductFamily.setId(1);
		orderToLeProductFamily.setMstProductFamily(getMstProductFamilyGVPN());
		Set<OrderProductSolution> set = new HashSet<>();
		set.add(getOrderProductSolution());
		orderToLeProductFamily.setOrderProductSolutions(set);
		orderToLeProductFamily.setOrderToLe(getOrderToLes1());
		return orderToLeProductFamily;
	}

	public OrderToLeProductFamily getorderToLeProductFamilies1() {
		OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
		orderToLeProductFamily.setId(1);
		orderToLeProductFamily.setMstProductFamily(getMstProductFamily());
		Set<OrderProductSolution> set = new HashSet<>();
		set.add(getOrderProductSolution());
		orderToLeProductFamily.setOrderProductSolutions(set);

		return orderToLeProductFamily;
	}

	public Set<OrderToLeProductFamily> createOrderToLeProductFamilySet() {
		Set<OrderToLeProductFamily> setAttri = new HashSet<>();
		setAttri.add(getorderToLeProductFamilies());
		return setAttri;
	}
	
	public NotFeasible getNotFeasible() {
		NotFeasible obj = new NotFeasible();
		obj.setCustomerSegmentA("a");
		return obj;
	}

	public OrderToLe getOrderToLes() {
		OrderToLe orderToLe = new OrderToLe();
		Set set = new HashSet<>();
		set.add(getorderToLeProductFamilies());
		orderToLe.setId(1);
		orderToLe.setCurrencyId(1);
		orderToLe.setErfCusCustomerLegalEntityId(1);
		orderToLe.setErfCusSpLegalEntityId(1);
		orderToLe.setStage("ORDER_COMPLETED");
		orderToLe.setFinalMrc(1.0);
		orderToLe.setFinalNrc(2.0);

		orderToLe.setOrder(getOrder());
		Set<OrdersLeAttributeValue> setAttri = new HashSet<>();
		setAttri.add(getOrdersLeAttributeValue());
		orderToLe.setOrdersLeAttributeValues(setAttri);
		orderToLe.setOrderToLeProductFamilies(set);
		return orderToLe;
	}
	
	public OrderToLe getOrderToLesGVPN() {
		OrderToLe orderToLe = new OrderToLe();
		Set set = new HashSet<>();
		set.add(getorderToLeProductFamiliesGvpn());
		orderToLe.setId(1);
		orderToLe.setCurrencyId(1);
		orderToLe.setErfCusCustomerLegalEntityId(1);
		orderToLe.setErfCusSpLegalEntityId(1);
		orderToLe.setStage("ORDER_COMPLETED");
		orderToLe.setFinalMrc(1.0);
		orderToLe.setFinalNrc(2.0);

		orderToLe.setOrder(getOrder());
		Set<OrdersLeAttributeValue> setAttri = new HashSet<>();
		setAttri.add(getOrdersLeAttributeValue());
		orderToLe.setOrdersLeAttributeValues(setAttri);
		orderToLe.setOrderToLeProductFamilies(set);
		return orderToLe;
	}
	
	

	public OrderToLe getOrderToLes2() {
		OrderToLe orderToLe = new OrderToLe();
		Set<OrderToLeProductFamily> set = new HashSet<>();
		set.add(getorderToLeProductFamilies1());

		orderToLe.setId(1);
		orderToLe.setCurrencyId(1);
		orderToLe.setErfCusCustomerLegalEntityId(1);
		orderToLe.setErfCusSpLegalEntityId(1);
		orderToLe.setOrderToLeProductFamilies(set);
		orderToLe.setStage("stage");
		orderToLe.setFinalMrc(1.0);
		orderToLe.setFinalNrc(2.0);
		orderToLe.setOrdersLeAttributeValues(returnOrdersLeAttributeValueList().stream().collect(Collectors.toSet()));
		orderToLe.setOrderToLeProductFamilies(set);
		Order order = new Order();
		order.setOrderCode("order code");
		
		orderToLe.setOrder(order);
		
		return orderToLe;
	}

	public List<OrdersLeAttributeValue> returnOrdersLeAttributeValueList() {
		List<OrdersLeAttributeValue> ordersLeAttributeList = new ArrayList<>();
		ordersLeAttributeList.add(getOrdersLeAttributeValue());
		return ordersLeAttributeList;
	}

	public OrderToLe getOrderToLesForGetOrder(Order orders) {
		OrderToLe orderToLe = new OrderToLe();
		Set set = new HashSet<>();
		set.add(getorderToLeProductFamilies());
		orderToLe.setId(1);
		orderToLe.setCurrencyId(1);
		orderToLe.setErfCusCustomerLegalEntityId(1);
		orderToLe.setErfCusSpLegalEntityId(1);
		orderToLe.setStage("stage");
		orderToLe.setFinalMrc(1.0);
		orderToLe.setFinalNrc(2.0);
		orderToLe.setOrder(orders);
		orderToLe.setOrderToLeProductFamilies(set);
		orderToLe.setOrder(getOrder());
		Set ordersLeAttributeValue = new HashSet<>();
		ordersLeAttributeValue.add(getOrdersLeAttributeValue());
		orderToLe.setOrdersLeAttributeValues(ordersLeAttributeValue);
		return orderToLe;
	}

	public Order getOrders() {
		Order orders = new Order();
		Set set = new HashSet<>();
		set.add(getOrderToLesForGetOrder(orders));
		orders.setId(1);
		orders.setCustomer(getCustomer());
		orders.setOrderToLes(set);
		orders.setStatus((byte) 1);
		Set set1 = new HashSet<>();
		set1.add(getOrderToLesForGetOrder(orders));
		orders.setOrderToLes(set1);
		return orders;
	}

	public List<Order> getOrderList() {
		List<Order> orderList = new ArrayList<>();
		orderList.add(getOrder());
		return orderList;
	}

	public OrderToLe getOrderToLes1() {
		OrderToLe orderToLe = new OrderToLe();
		orderToLe.setId(1);
		orderToLe.setCurrencyId(1);
		orderToLe.setErfCusCustomerLegalEntityId(1);
		orderToLe.setErfCusSpLegalEntityId(1);
		orderToLe.setStage("stage");
		orderToLe.setFinalMrc(1.0);
		orderToLe.setFinalNrc(2.0);
		orderToLe.setOrder(getOrder());
		return orderToLe;
	}

	public OrderProductComponentsAttributeValue createOrderProducts() {

		OrderProductComponentsAttributeValue orderProductComponentsAttributeValue = new OrderProductComponentsAttributeValue();
		orderProductComponentsAttributeValue.setId(1);
		orderProductComponentsAttributeValue.setProductAttributeMaster(getProductAtrributeMaster().get(0));
		return orderProductComponentsAttributeValue;
	}
	
	public OrderProductComponentsAttributeValue createInterfaceType(String attrName) {

		OrderProductComponentsAttributeValue orderProductComponentsAttributeValue = new OrderProductComponentsAttributeValue();
		orderProductComponentsAttributeValue.setId(1);
		orderProductComponentsAttributeValue.setAttributeValues("tests");
		orderProductComponentsAttributeValue.setProductAttributeMaster(getProductAtrributeMaster(attrName).get(0));
		return orderProductComponentsAttributeValue;
	}

	public List<OrderProductComponentsAttributeValue> createOrderProductsList() {
		List<OrderProductComponentsAttributeValue> list = new ArrayList<>();
		OrderProductComponentsAttributeValue orderProductComponentsAttributeValue = new OrderProductComponentsAttributeValue();
		orderProductComponentsAttributeValue.setId(1);
		orderProductComponentsAttributeValue.setProductAttributeMaster(getProductAtrributeMaster().get(0));
		orderProductComponentsAttributeValue.setAttributeValues("1");
		list.add(orderProductComponentsAttributeValue);
		return list;
	}

	public OrderIllSite getOrderIllSite() {
		OrderIllSite orderIllSite = new OrderIllSite();
		orderIllSite.setErfLocSiteaLocationId(1);
		orderIllSite.setRequestorDate(new Date());
		orderIllSite.setId(1);
		orderIllSite.setCreatedBy(1);
		orderIllSite.setCreatedTime(new Date());
		orderIllSite.setEffectiveDate(new Date());
		orderIllSite.setErfLocSiteaLocationId(1);
		orderIllSite.setErfLocSiteaSiteCode("code");
		orderIllSite.setFeasibility((byte) 1);
		orderIllSite.setStatus((byte) 1);
		orderIllSite.setStage("PROVISION_SITES");
		orderIllSite.setSiteCode("TestCode");
		orderIllSite.setMstOrderSiteStatus(getMstOrderSiteStatus());
		orderIllSite.setMstOrderSiteStage(getMstOrderSiteStage());
		orderIllSite.setOrderSiteFeasibility(getSiteFeasiblitySet());
		OrderProductSolution sol = new OrderProductSolution();
		sol.setId(1);
		orderIllSite.setOrderProductSolution(sol);

		return orderIllSite;
	}

	public List<OrderToLe> getOrderToLesList() {
		List<OrderToLe> list = new ArrayList<>();
		list.add(getOrderToLes());
		list.add(getOrderToLes());
		list.add(getOrderToLes());
		return list;
	}
	
	public List<OrderToLe> getOrderToLesGVpnList() {
		List<OrderToLe> list = new ArrayList<>();
		list.add(getOrderToLesGVPN());
		return list;
	}

	public List<OrderProductSolution> getOrderProductSolutionList() {
		List<OrderProductSolution> list = new ArrayList<>();
		list.add(getOrderProductSolution());
		return list;
	}

	public List<OrderIllSite> getOrderIllSiteList() {
		List<OrderIllSite> list = new ArrayList<>();
		list.add(getOrderIllSite());
		list.add(getOrderIllSite());
		return list;
	}

	public OrderProductComponent getOrderProductComponent() {
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setId(1);
		orderProductComponent.setMstProductComponent(getMstProductComponent());
		Set<OrderProductComponentsAttributeValue> set = new HashSet<>();
		set.add(createOrderProducts());
		orderProductComponent.setOrderProductComponentsAttributeValues(set);
		return orderProductComponent;

	}
	
	public OrderProductComponent getOrderProductComponentNationalConnectivity() {
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setId(1);
		orderProductComponent.setType("Link");
		orderProductComponent.setMstProductComponent(getNationalConnectivity());
		Set<OrderProductComponentsAttributeValue> set = new HashSet<>();
		set.add(createInterfaceType("Interface Type - A end"));
		set.add(createInterfaceType("Interface Type - B end"));

		orderProductComponent.setOrderProductComponentsAttributeValues(set);
		return orderProductComponent;

	}
	
	public List<OrderProductComponent> getOrderProductComponentGVPN() {
		List<OrderProductComponent> list = new ArrayList<>();
		List<String> componentNames = new ArrayList<>();
		componentNames.add(PDFConstants.GVPN_COMMON);
		componentNames.add(OrderDetailsExcelDownloadConstants.VPN_PORT);
		componentNames.add(OrderDetailsExcelDownloadConstants.LAST_MILE);

		componentNames.add(OrderDetailsExcelDownloadConstants.CPE);

		componentNames.add(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT);
		List<String> type = new ArrayList<>();
		type.add("primary");
		type.add("secondary");

		componentNames.forEach(cmp -> {
			type.forEach(t -> {
				OrderProductComponent orderProductComponent = new OrderProductComponent();
				orderProductComponent.setId(1);
				orderProductComponent.setType(t);
				orderProductComponent.setMstProductComponent(getMstProductComponentGVPN(cmp));
				Set<OrderProductComponentsAttributeValue> set = new HashSet<>();
				set.add(createInterfaceType(OrderDetailsExcelDownloadConstants.LOCAL_LOOP_BANDWITHD));
				set.add(createInterfaceType(ComponentConstants.CPE_BASIC_CHASSIS.getComponentsValue()));

				orderProductComponent.setOrderProductComponentsAttributeValues(set);
				list.add(orderProductComponent);
			});

		});

		return list;

	}
	
	public OrderProductComponent getOrderProductComponentLinkManagement() {
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setId(1);
		orderProductComponent.setType("Link");
		orderProductComponent.setMstProductComponent(getLinkManagement());
		Set<OrderProductComponentsAttributeValue> set = new HashSet<>();
		set.add(createOrderProducts());
		orderProductComponent.setOrderProductComponentsAttributeValues(set);
		return orderProductComponent;

	}
	
	public OrderProductComponent getOrderProductComponentNationalConnectivitySecondary() {
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setId(1);
		orderProductComponent.setType("Link");
		orderProductComponent.setType("secondary");
		orderProductComponent.setMstProductComponent(getNationalConnectivity());
		Set<OrderProductComponentsAttributeValue> set = new HashSet<>();
		set.add(createInterfaceType("Interface Type - A end"));
		set.add(createInterfaceType("Interface Type - B end"));

		orderProductComponent.setOrderProductComponentsAttributeValues(set);
		return orderProductComponent;

	}
	
	public OrderProductComponent getOrderProductComponentLinkManagementSecondary() {
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setId(1);
		orderProductComponent.setType("Link");
		orderProductComponent.setType("secondary");
		orderProductComponent.setMstProductComponent(getLinkManagement());
		Set<OrderProductComponentsAttributeValue> set = new HashSet<>();
		set.add(createOrderProducts());
		orderProductComponent.setOrderProductComponentsAttributeValues(set);
		return orderProductComponent;

	}
	
	public OrderProductComponent getOrderProductComponentPrivateLines() {
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setId(1);
		orderProductComponent.setType("Link");
		orderProductComponent.setMstProductComponent(getPrivateLines());
		Set<OrderProductComponentsAttributeValue> set = new HashSet<>();
		set.add(createOrderProducts());
		orderProductComponent.setOrderProductComponentsAttributeValues(set);
		return orderProductComponent;

	}
	
	public OrderProductComponent getOrderProductComponentLastMileSiteA() {
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setId(1);
		orderProductComponent.setType("Site-A");
		orderProductComponent.setMstProductComponent(getLastMile());
		Set<OrderProductComponentsAttributeValue> set = new HashSet<>();
		set.add(createOrderProducts());
		orderProductComponent.setOrderProductComponentsAttributeValues(set);
		return orderProductComponent;

	}
	
	public OrderProductComponent getOrderProductComponentLastMileSiteB() {
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setId(1);
		orderProductComponent.setType("Site-B");
		orderProductComponent.setMstProductComponent(getLastMile());
		Set<OrderProductComponentsAttributeValue> set = new HashSet<>();
		set.add(createOrderProducts());
		orderProductComponent.setOrderProductComponentsAttributeValues(set);
		return orderProductComponent;

	}
	
	public OrderProductComponent getOrderProductComponentSiteProperties() {
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setId(1);
		orderProductComponent.setMstProductComponent(getLastMile());
		Set<OrderProductComponentsAttributeValue> set = new HashSet<>();
		set.add(createOrderProducts());
		orderProductComponent.setOrderProductComponentsAttributeValues(set);
		return orderProductComponent;

	}
	
	public OrderProductComponentBean getOrderProductComponentBean() {
		OrderProductComponentBean orderProductComponent = new OrderProductComponentBean();
		orderProductComponent.setId(1);
		return orderProductComponent;

	}
	
	public List<OrderProductComponentBean> getOrderProductComponentBeanList() {
		List<OrderProductComponentBean> list = new ArrayList<OrderProductComponentBean>();
		list.add(getOrderProductComponentBean());
		return list;

	}

	public Set<OrderProductComponent> getOrderProductComponentSet() {
		Set<OrderProductComponent> set = new HashSet<>();
		set.add(getOrderProductComponent());
		return set;
	}

	public List<OrderProductComponent> getOrderProductComponentList() {
		List<OrderProductComponent> list = new ArrayList<>();
		list.addAll(getOrderProductComponentListForLink());
		list.addAll(getOrderProductComponentListForSite());
		list.add(getOrderProductComponentNationalConnectivitySecondary());
		list.add(getOrderProductComponentLinkManagementSecondary());
		

		return list;
	}
	
	
	public List<OrderProductComponent> getGVPNOrderProductComponentList() {
		List<OrderProductComponent> list = new ArrayList<>();
		list.addAll(getOrderProductComponentListForLink());
		list.addAll(getOrderProductComponentListForSite());
		list.add(getOrderProductComponentNationalConnectivitySecondary());
		list.add(getOrderProductComponentLinkManagementSecondary());
		

		return list;
	}
	
	public List<OrderProductComponent> getOrderProductComponentListForLink() {
		List<OrderProductComponent> list = new ArrayList<>();
		list.add(getOrderProductComponentNationalConnectivity());
		list.add(getOrderProductComponentPrivateLines());
		list.add(getOrderProductComponentLinkManagement());

		return list;
	}
	
	public List<OrderProductComponent> getOrderProductComponentListForSite() {
		List<OrderProductComponent> list = new ArrayList<>();
		list.add(getOrderProductComponentLastMileSiteA());
		list.add(getOrderProductComponentLastMileSiteB());

		return list;
	}

	public List<OrderToLeProductFamily> getorderToLeProductFamiliesList() {
		List<OrderToLeProductFamily> list = new ArrayList<>();
		list.add(getorderToLeProductFamilies());
		return list;
	}

	public OrderPrice getOrderPrice() {
		OrderPrice orderPrice = new OrderPrice();
		orderPrice.setId(1);
		orderPrice.setQuoteId(1);
		return orderPrice;
	}

	public List<OrderPrice> getOrderPriceList() {
		List<OrderPrice> list = new ArrayList<>();
		list.add(getOrderPrice());
		return list;
	}

	public List<OmsAttachment> getAttachmentsList() {
		List<OmsAttachment> list = new ArrayList<>();
		list.add(createOmsAttachMent());
		return list;
	}

	public List<OmsAttachment> getAttachmentsListNegative() {
		List<OmsAttachment> list = new ArrayList<>();
		list.add(createOmsAttachMentWithoutQuoteToLe());
		return list;
	}

	/**
	 * getRootUser -mock values
	 * 
	 * @return- getUser
	 */
	public User getRootUser() {
		User user = new User();
		user.setId(1);
		user.setUsername("root");
		/* user.toString(); */
		user.setEmailId("root@gmail.com");
		return user;
	}

	/**
	 * getRootUser with quote -mock values
	 * 
	 * @return- getUser
	 */
	public User getRootUserWithQuote() {
		User user = new User();
		user.setId(1);
		user.setUsername("root");
		user.setCustomer(getCustomerWithQuote());
		/* user.toString(); */
		user.setEmailId("root@gmail.com");
		return user;
	}

	/**
	 * getCustomer with quote -mock values
	 * 
	 * @return {@link Customer}
	 */
	public Customer getCustomerWithQuote() {
		Customer customer = new Customer();
		customer.setCustomerCode("123");
		customer.setStatus((byte) 0);
		customer.setId(1);
		customer.setCustomerEmailId("vick@gmail.com");
		customer.setCustomerName("Vivek");
		/* customer.toString(); */
		customer.setUsers(getUserEntity());
		Quote quote = new Quote();
		Set<Quote> quoteSet = new HashSet<Quote>();
		quoteSet.add(getQuote());
		customer.setQuotes(quoteSet);
		return customer;
	}

	/**
	 * getOrder with object
	 * 
	 * @return orders
	 */
	public Order getOrder() {
		Order orders = new Order();

		Timestamp timeStamp = new Timestamp(0);

		orders.setCreatedBy(1);
		orders.setCreatedTime(timeStamp);
		orders.setCustomer(getCustomer());
		orders.setEffectiveDate(timeStamp);
		orders.setEndDate(timeStamp);
		orders.setId(1);
		orders.setOrderCode("CBV567GH");
		Set<OrderToLe> orderToLes = new HashSet<>();
		orderToLes.add(getOrderToLes2());
		orders.setOrderToLes(orderToLes);
		orders.setQuote(getQuote());
		orders.setStage("xyz");
		orders.setStartDate(timeStamp);
		orders.setStatus((byte) 1);

		return orders;
	}

	/**
	 * getUpdateRequest with Quote id null
	 * 
	 * @return
	 */
	public UpdateRequest getUpdateRequestQuoteIdNull() {
		UpdateRequest request = new UpdateRequest();
		request.setQuoteId(null);
		request.getComponentDetails().addAll(getcompDetails());
		return request;
	}
	
	/**
	 * getUpdateRequest with components as null
	 * 
	 * @return
	 */
	public UpdateRequest getUpdateRequestWithNullComp() {
		UpdateRequest request = new UpdateRequest();
		request.setQuoteId(null);
		request.setComponentDetails(null);
		return request;
	}

	/**
	 * 
	 * getQuoteToLeList-mock values
	 * 
	 * @return quoteToLeList
	 */
	public List<QuoteToLe> getQuoteToLeList() {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setId(1);
		quoteToLe.setCurrencyId(1);

		quoteToLe.setFinalMrc(5d);
		quoteToLe.setFinalNrc(67d);
		quoteToLe.setProposedMrc(78d);
		/* quoteToLe.toString(); */
		quoteToLe.setProposedNrc(98d);

		quoteToLe.setFinalMrc(5d);
		quoteToLe.setFinalNrc(67d);
		quoteToLe.setProposedMrc(78d);

		quoteToLe.setQuote(getQuote());
		List<QuoteToLe> quoteToLeList = new ArrayList<>();
		quoteToLeList.add(quoteToLe);

		return quoteToLeList;
	}

	public User craeteUser() {
		User user = new User();
		user.setId(1);
		user.setFirstName("test");
		user.setId(1);
		user.setStatus(1);
		user.setCustomer(craeteCustomer());

		return user;

	}

	public User craeteUserForInactiveOrders() {
		User user = new User();
		user.setId(1);
		user.setFirstName("test");
		user.setStatus(1);
		user.setCustomer(craeteCustomerForInActiveOrders());

		return user;

	}

	public Customer craeteCustomer() {

		Customer customer = new Customer();
		customer.setId(1);
		customer.setCustomerEmailId("test@gmail.com");
		Set<Order> set = new HashSet<>();
		set.add(getOrders());
		customer.setOrders(set);
		return customer;
	}

	public QuoteProductComponent craeteQuoteProductComponent() {
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();

		quoteProductComponent.setId(1);
		quoteProductComponent.setReferenceId(1);
		quoteProductComponent.setMstProductComponent(getMstProductComponent());
		quoteProductComponent.setMstProductFamily(geProductFamily());
		/* quoteProductComponent.toString(); */
		quoteProductComponent.setQuoteProductComponentsAttributeValues(getQuoteComponentsAttributeValues());
		return quoteProductComponent;
	}

	public List<QuoteProductComponentsAttributeValue> createQuoteProductAttributeValue() {
		List<QuoteProductComponentsAttributeValue> list = new ArrayList<>();
		list.add(createQuoteProductComponentsAttributeValue());
		list.add(createQuoteProductComponentsAttributeValue());
		return list;
	}

	public QuoteProductComponentsAttributeValue createQuoteProductComponentsAttributeValue() {
		QuoteProductComponentsAttributeValue qPCAV = new QuoteProductComponentsAttributeValue();
		qPCAV.setAttributeValues("1");
		qPCAV.setDisplayValue("TEST");
		qPCAV.setProductAttributeMaster(getProductAtrributeMas());
		qPCAV.setId(1);
		return qPCAV;
	}

	public QuotePrice getQuotePrice()

	{

		QuotePrice qp = new QuotePrice();
		qp.setId(1);
		return qp;

	}

	public QuoteIllSite getQuoteIllSite()

	{
		QuoteIllSite qIs = new QuoteIllSite();
		qIs.setId(1);
		qIs.setStatus((byte) 1);
		qIs.setErfLocSitebLocationId(2);
		qIs.setProductSolution(getSolution());
		qIs.setFeasibility((byte) 1);
		qIs.setFpStatus("N");
		return qIs;

	}

	public List<QuoteProductComponentsAttributeValue> getquoteProductComponentAttributeValues() {
		List<QuoteProductComponentsAttributeValue> list = new ArrayList<>();
		list.add(craeteproductComponentAttribute());
		list.add(craeteproductComponentAttribute1());
		list.add(craeteproductComponentAttribute2());
		
		return list;
	}
	
	public List<QuoteProductComponentsAttributeValue> getquoteProductComponentAttributeValuesGvpn() {
		List<QuoteProductComponentsAttributeValue> list = new ArrayList<>();
		list.add(craeteproductComponentAttribute());
		list.add(craeteproductComponentAttributeGvpn());
		list.add(craeteproductComponentAttribute2());
		
		return list;
	}

	public QuoteProductComponentsAttributeValue craeteproductComponentAttribute() {
		QuoteProductComponentsAttributeValue qpa = new QuoteProductComponentsAttributeValue();
		qpa.setId(1);
		qpa.setDisplayValue("bandwidth");
		qpa.setAttributeValues("256");
		qpa.setProductAttributeMaster(getProductAtrributeMas());
		return qpa;

	}
	
	public QuoteProductComponentsAttributeValue craeteproductComponentAttribute1() {
		QuoteProductComponentsAttributeValue qpa = new QuoteProductComponentsAttributeValue();
		qpa.setId(1);
		qpa.setDisplayValue("port bandwidth");
		qpa.setAttributeValues("2056 mbps");
		qpa.setProductAttributeMaster(getProductAtrributeMas1());
		return qpa;

	}
	
	public QuoteProductComponentsAttributeValue craeteproductComponentAttributeGvpn() {
		QuoteProductComponentsAttributeValue qpa = new QuoteProductComponentsAttributeValue();
		qpa.setId(1);
		qpa.setDisplayValue("port bandwidth");
		qpa.setAttributeValues("2056");
		qpa.setProductAttributeMaster(getProductAtrributeMas1());
		return qpa;

	}
	
	public QuoteProductComponentsAttributeValue craeteproductComponentAttribute2() {
		QuoteProductComponentsAttributeValue qpa = new QuoteProductComponentsAttributeValue();
		qpa.setId(1);
		qpa.setDisplayValue("service availability");
		qpa.setAttributeValues("99.5");
		qpa.setProductAttributeMaster(getProductAtrributeMas());
		return qpa;

	}

	public List<ProductSolution> createProductSolutions() {
		List<ProductSolution> list = new ArrayList<>();
		list.add(craeteproductSolutions());

		return list;

	}

	public ProductSolution craeteproductSolutions() {
		ProductSolution p = new ProductSolution();
		p.setId(1);
		p.setMstProductOffering(getMstOffering());
		p.setQuoteIllSites(getIllsitesList());

		return p;
	}

	public Order getOrdersForInActiveStatus() {
		Order orders = new Order();
		Set set = new HashSet<>();
		set.add(getOrderToLes());
		orders.setId(1);
		orders.setCustomer(getCustomer());
		orders.setOrderToLes(set);
		orders.setStatus((byte) 0);
		Set set1 = new HashSet<>();
		set1.add(getOrderToLes());
		orders.setOrderToLes(set1);
		return orders;
	}

	public Customer craeteCustomerForInActiveOrders() {

		Customer customer = new Customer();
		customer.setId(1);
		customer.setCustomerEmailId("test@gmail.com");
		Set<Order> set = new HashSet<>();
		set.add(getOrdersForInActiveStatus());
		customer.setOrders(set);
		return customer;

	}

	public List<QuoteToLeProductFamily> getQuoteToLeFamilyList() {
		List<QuoteToLeProductFamily> list = new ArrayList<>();
		list.add(getQuoteToLeFamily());
		return list;

	}

	public List<ProductSolution> getSolutionList() {
		List<ProductSolution> list = new ArrayList<>();
		list.add(getSolution());
		return list;

	}

	public List<QuoteIllSite> getListOfQouteIllSites() {

		List<QuoteIllSite> list = new ArrayList<>();
		list.add(getQuoteIllSite());
		list.add(getQuoteIllSite());
		return list;

	}

	public List<QuoteIllSite> getListOfQouteIllSites2() {

		List<QuoteIllSite> list = new ArrayList<>();
		QuoteIllSite site = new QuoteIllSite();
		site.setStatus((byte) 1);
		site.setFeasibility((byte) 1);
		site.setQuoteIllSiteSlas(createQuoteIllSiteSlaSet());
		site.setId(1);
		list.add(site);
		return list;

	}

	public Optional<OrderToLe> returnOrderToLeForUpdateStatus() {
		OrderToLe le = new OrderToLe();
		le.setId(1);
		le.setCurrencyId(1);
		le.setEndDate(new Date());
		le.setErfCusCustomerLegalEntityId(1);
		le.setErfCusSpLegalEntityId(1);
		le.setOrder(getOrdersForInActiveStatus());
		le.setStage("Sample");
		return Optional.of(le);
	}

	public Optional<QuoteToLe> returnQuoteToLeForUpdateStatus() {
		QuoteToLe le = new QuoteToLe();
		le.setId(1);
		le.setCurrencyId(1);
		le.setErfCusCustomerLegalEntityId(1);
		le.setErfCusSpLegalEntityId(1);
		le.setStage("Sample");
		le.setQuoteToLeProductFamilies(getQuoteToLeProductFamily());
		le.setTpsSfdcOptyId("tpsSfdcOptyId");
		le.setQuote(getQuote1());
		return Optional.of(le);
	}

	public Optional<OrderIllSite> returnQuoteDetailForUpdateStatus() {
		OrderIllSite le = new OrderIllSite();
		le.setId(1);
		le.setStage("Sample");
		le.setCreatedBy(1);
		le.setErfLocSiteaLocationId(1);
		le.setErfLocSiteaLocationId(1);
		le.setErfLocSitebLocationId(1);
		return Optional.of(le);
	}

	public Optional<OrderIllSite> getOrderIllSiteOptional() {
		OrderIllSite le = new OrderIllSite();
		le.setId(1);
		le.setStage("Sample");
		le.setCreatedBy(1);
		le.setErfLocSiteaLocationId(1);
		le.setErfLocSiteaLocationId(1);
		le.setErfLocSitebLocationId(1);
		le.setMstOrderSiteStage(getMstOrderSiteStage());
		le.setMstOrderSiteStatus(getMstOrderSiteStatus());
		return Optional.of(le);
	}

	public MstOmsAttribute getMstAttribute() {
		MstOmsAttribute moa = new MstOmsAttribute();
		moa.setId(1);
		moa.setDescription("test");
		moa.setName("Payment Currency");
		return moa;

	}

	public OrdersLeAttributeValue getOrdersLeAttributeValue() {

		OrdersLeAttributeValue slav = new OrdersLeAttributeValue();
		slav.setAttributeValue("Test");
		slav.setDisplayValue("Test");
		slav.setId(1);

		slav.setAttributeValue("rakesh");
		slav.setDisplayValue("Rakesh");
		slav.setMstOmsAttribute(getMstOmsAttribute());
		// slav.setOrderToLe(getOrderToLes());
		slav.setMstOmsAttribute(getMstOmsAttribute());
		return slav;
	}

	public MstOmsAttribute getMstOmsAttribute() {
		MstOmsAttribute mstOmsAttribute = new MstOmsAttribute();
		mstOmsAttribute.setId(1);
		mstOmsAttribute.setIsActive((byte) 1);
		mstOmsAttribute.setCreatedBy("Anandhi");
		mstOmsAttribute.setCreatedTime(new Date());
		mstOmsAttribute.setDescription("Description");
		return mstOmsAttribute;

	}
	
	public List<MstOmsAttribute> getMstOmsAttributeList() {
		List<MstOmsAttribute> mstOmsAttributeList = new ArrayList<>();
		mstOmsAttributeList.add(getMstOmsAttribute());
		return mstOmsAttributeList;

	}

	public QuoteLeAttributeValue getQuoteLeAttributeValue() {
		QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
		quoteLeAttributeValue.setId(1);
		;
		quoteLeAttributeValue.setAttributeValue("IAS");
		quoteLeAttributeValue.setMstOmsAttribute(getMstOmsAttribute());
		quoteLeAttributeValue.setDisplayValue("display Value");
		return quoteLeAttributeValue;

	}

	public QuoteLeAttributeValue getQuoteLeAttributeValue1() {
		QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
		quoteLeAttributeValue.setId(1);
		quoteLeAttributeValue.setAttributeValue("INR");
		quoteLeAttributeValue.setMstOmsAttribute(getMstOmsAttribute());
		quoteLeAttributeValue.setDisplayValue("Payment Currency");
		return quoteLeAttributeValue;

	}

	public Set<QuoteLeAttributeValue> getQuoteLeAttributeValueSet() {
		Set<QuoteLeAttributeValue> quoteLeAttributeValueSet = new HashSet<>();
		QuoteLeAttributeValue quoteLeAttributeValue = getQuoteLeAttributeValue();
		quoteLeAttributeValue.getMstOmsAttribute().setName("TermInMonths");
		QuoteLeAttributeValue quoteLeAttributeValue1 = getQuoteLeAttributeValue();
		quoteLeAttributeValue1.getMstOmsAttribute().setName("TermInMon");
		quoteLeAttributeValueSet.add(quoteLeAttributeValue);
		quoteLeAttributeValueSet.add(quoteLeAttributeValue1);
		return quoteLeAttributeValueSet;

	}

	public List<CustomerDetail> getCustomerList() {
		List<CustomerDetail> list = new ArrayList<>();
		list.add(createCustomerDetails());
		return list;

	}

	public CustomerDetail createCustomerDetails() {
		CustomerDetail cd = new CustomerDetail();
		cd.setCustomerAcId("test");
		cd.setCustomerEmailId("test@gmail.com");

		cd.setCustomerId(1);
		cd.setCustomerLeId(1);
		cd.setErfCustomerId(1);
		cd.setStatus((byte) 1);
		return cd;

	}

	public UserInformation getUserInformation() {
		UserInformation userInformation = new UserInformation();
		userInformation.setUserId("admin");
		userInformation.setCustomers(getCustomerList());
		userInformation.setRole(Arrays.asList("OPTIMUS_MU"));
		return userInformation;

	}

	/**
	 * getOrder with object
	 * 
	 * @return orders
	 */
	public Order getOrderWithOutOrderTole() {
		Order orders = new Order();

		Timestamp timeStamp = new Timestamp(0);

		orders.setCreatedBy(1);
		orders.setCreatedTime(timeStamp);
		orders.setCustomer(getCustomer());
		orders.setEffectiveDate(timeStamp);
		orders.setEndDate(timeStamp);
		orders.setId(1);
		orders.setQuote(getQuote());
		orders.setStage("aa");
		orders.setStartDate(timeStamp);
		orders.setStatus((byte) 1);

		return orders;
	}

	public List<OrderToLe> getOrderToLesListForDashBoard() {
		List<OrderToLe> list = new ArrayList<>();
		OrderToLe orderToLe = new OrderToLe();
		Set set = new HashSet<>();
		set.add(getorderToLeProductFamilies());
		orderToLe.setId(1);
		orderToLe.setCurrencyId(1);
		orderToLe.setErfCusCustomerLegalEntityId(1);
		orderToLe.setErfCusSpLegalEntityId(1);
		orderToLe.setStage("stage");
		orderToLe.setFinalMrc(1.0);
		orderToLe.setFinalNrc(2.0);
		orderToLe.setOrder(getOrder());
		orderToLe.setOrderToLeProductFamilies(set);
		orderToLe.setOrder(getOrderWithOutOrderTole());
		list.add(orderToLe);
		return list;
	}

	public List<OrdersLeAttributeValue> getOrdersLeAttributeValueList() {
		List<OrdersLeAttributeValue> ordersLeAttributeValueList = new ArrayList<>();
		ordersLeAttributeValueList.add(getOrdersLeAttributeValue());
		return ordersLeAttributeValueList;
	}

	public List<QuoteLeAttributeValue> getQuoteLeAttributeValueList() {
		List<QuoteLeAttributeValue> leAttributeValues = new ArrayList<>();
		leAttributeValues.add(getQuoteLeAttributeValue1());
		return leAttributeValues;
	}

	public UpdateRequest returnUpdateRequestForInvalidAttributeId() {
		UpdateRequest updateRequest = new UpdateRequest();
		ComponentDetail componentDetails = new ComponentDetail();
		AttributeDetail attributeDetail = new AttributeDetail();
		List<AttributeDetail> attributeDetailList = new ArrayList<>();
		List<ComponentDetail> componentDetailsList = new ArrayList<>();
		attributeDetail.setAttributeId(1);
		attributeDetail.setAttributeMasterId(1);
		attributeDetail.setName("tata");
		attributeDetail.setValue("tata");
		attributeDetailList.add(attributeDetail);
		componentDetails.setAttributes(attributeDetailList);
		componentDetails.setComponentId(1);
		componentDetails.setComponentMasterId(1);
		componentDetailsList.add(componentDetails);
		updateRequest.setSiteId(1);
		updateRequest.setQuoteId(1);
		updateRequest.setComponentDetails(componentDetailsList);
		updateRequest.setRequestorDate(new Timestamp(new Date().getTime()));
		return updateRequest;
	}

	public UpdateRequest returnUpdateRequestWithoutComponentDetails() {
		UpdateRequest updateRequest = new UpdateRequest();
		AttributeDetail attributeDetail = new AttributeDetail();
		List<AttributeDetail> attributeDetailList = new ArrayList<>();
		attributeDetail.setAttributeId(1);
		attributeDetail.setAttributeMasterId(1);
		attributeDetail.setName("tata");
		attributeDetail.setValue("tata");
		attributeDetailList.add(attributeDetail);
		updateRequest.setSiteId(1);
		updateRequest.setQuoteId(1);
		updateRequest.setRequestorDate(new Timestamp(new Date().getTime()));
		return updateRequest;

	}

	public OrderProductSolution getOrderProductSolutionWithOutIll() {
		OrderProductSolution orderProductSolution = new OrderProductSolution();
		orderProductSolution.setId(1);
		orderProductSolution.setMstProductOffering(getMstOffering());
		return orderProductSolution;
	}

	public OrderIllSite getOrderIllSiteForSiteDetails() {
		OrderIllSite orderIllSite = new OrderIllSite();
		orderIllSite.setErfLocSiteaLocationId(1);
		orderIllSite.setRequestorDate(new Date());
		orderIllSite.setId(1);
		orderIllSite.setCreatedBy(1);
		orderIllSite.setCreatedTime(new Date());
		orderIllSite.setEffectiveDate(new Date());
		orderIllSite.setErfLocSiteaLocationId(1);
		orderIllSite.setErfLocSiteaSiteCode("code");
		orderIllSite.setFeasibility((byte) 1);
		orderIllSite.setStatus((byte) 1);
		orderIllSite.setStage("PROVISION_SITES");
		orderIllSite.setOrderProductSolution(getOrderProductSolutionWithOutIll());
		return orderIllSite;
	}

	public OrderToLeProductFamily getorderToLeProductFamiliesWithOutProductSolutions() {
		OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
		orderToLeProductFamily.setId(1);
		orderToLeProductFamily.setMstProductFamily(getMstProductFamily());
		return orderToLeProductFamily;
	}

	public OrderProductSolution getOrderProductSolutionWithOrderToLeProductFamiles() {

		OrderProductSolution orderProductSolution = new OrderProductSolution();
		orderProductSolution.setId(1);
		orderProductSolution.setMstProductOffering(getMstOffering());
		Set set = new HashSet<>();
		set.add(getOrderIllSite());
		orderProductSolution.setOrderIllSites(set);
		orderProductSolution.setOrderToLeProductFamily(getorderToLeProductFamiliesWithOutProductSolutions());
		return orderProductSolution;
	}

	public NplQuoteDetail getNplQuoteDetailForCreateNew() {
		NplQuoteDetail quoteDetail = new NplQuoteDetail();
		quoteDetail.setCustomerId(25);
		quoteDetail.setProductName("NPL");
		quoteDetail.setSite(getNplSite());
		quoteDetail.setQuoteleId(298);
		quoteDetail.setQuoteId(298);
		List<SolutionDetail> solutionDetails = new ArrayList<>();
		SolutionDetail solution = new SolutionDetail();
		solution.setOfferingName("Buy India Point to Point Connectivity");
		solution.setBandwidth("20");
		solution.setBandwidthUnit("mbps");
		solution.toString();
		SolutionDetail solution1 = new SolutionDetail();
		solution1.setOfferingName("Buy India Point to Point Connectivity2");
		solution1.setBandwidth("20");
		solution1.setBandwidthUnit("mbps");
		solution1.toString();
		List<ComponentDetail> components = new ArrayList<>();

		ComponentDetail component = new ComponentDetail();
		/* component.toString(); */
		components.add(component);
		ComponentDetail component1 = new ComponentDetail();
		component1.setComponentMasterId(1);
		component1.setName("Private Lines");

		ComponentDetail component2 = new ComponentDetail();
		component2.setComponentMasterId(2);
		component2.setName("Connectivity");

		List<AttributeDetail> attributeDetails = new ArrayList<>();
		AttributeDetail attributeDetail = new AttributeDetail();
		attributeDetail.setName("Model No");
		attributeDetail.setValue("CISCO");
		/* attributeDetail.toString(); */
		attributeDetail.setName("Sub Product");
		attributeDetail.setAttributeMasterId(1);
		attributeDetail.setValue("Standard NPL");
		attributeDetails.add(attributeDetail);

		AttributeDetail attributeDetail1 = new AttributeDetail();
		attributeDetail1.setName("Sub Product");
		attributeDetail1.setAttributeMasterId(25);
		attributeDetail1.setValue("Standard NPL");
		attributeDetails.add(attributeDetail1);

		component1.setAttributes(attributeDetails);
		component2.setAttributes(attributeDetails);
		component1.setIsActive("Y");
		component2.setIsActive("Y");

		components.add(component2);
		components.add(component1);

		solution.setComponents(components);
		solution.setImage("");
		/* solution.toString(); */
		solution.setOfferingName("Vive");
		solution.toString();

		solution1.setComponents(components);
		solution1.setImage("");
		solution1.toString();
		solutionDetails.add(solution);
		solutionDetails.add(solution1);
		quoteDetail.setSolutions(solutionDetails);

		return quoteDetail;
	}

	
	public List<MstOmsAttribute> getMstAttributeList() {
		List<MstOmsAttribute> attributesList = new ArrayList<>();
		attributesList.add(getMstAttribute());
		return attributesList;
	}

	public List<MstOmsAttribute> getMstAttributeListWithNullAttr() {
		List<MstOmsAttribute> attributesList = new ArrayList<>();
		attributesList.add(null);
		return attributesList;
	}

	public Set<OrdersLeAttributeValue> setOrdersLeAttributeValue() {
		Set<OrdersLeAttributeValue> setAttri = new HashSet<>();
		setAttri.add(getOrdersLeAttributeValue());
		return setAttri;
	}

	public List<SiteFeasibility> getSiteFeasibilities() {

		List<SiteFeasibility> fblist = new ArrayList<>();
		fblist.add(siteFeasibilities());
		return fblist;
	}

	public SiteFeasibility siteFeasibilities() {

		SiteFeasibility sFb = new SiteFeasibility();
		sFb.setId(1);
		sFb.setFeasibilityCode("Code");
		sFb.setFeasibilityCheck("check");
		sFb.setFeasibilityMode("mode");
		sFb.setIsSelected((byte) 1);
		sFb.setCreatedTime(new Timestamp(0));
		sFb.setType("Type");
		sFb.setQuoteIllSite(getIllsites());
		sFb.setProvider("provider");
		sFb.setResponseJson(
				"{\"lm_nrc_ospcapex_onwl\":0.0,\"BW_mbps_2\":2.0,\"Service_ID\":0.0,\"total_cost\":132160.0,\"X0.5km_avg_dist\":9999999.0,\"POP_DIST_KM\":0.5100730350500826,\"X0.5km_prospect_avg_bw\":45.2727272727273,\"Latitude_final\":13.0184111,\"OnnetCity_tag\":1.0,\"lm_arc_bw_prov_ofrf\":0.0,\"Network_F_NF_CC\":\"NA\",\"lm_nrc_nerental_onwl\":0.0,\"lm_nrc_inbldg_onwl\":40000.0,\"Network_Feasibility_Check\":\"Feasible\",\"core_check_CC\":\"NA\",\"X5km_min_bw\":120.0,\"access_check_CC\":\"NA\",\"rank\":10,\"POP_DIST_KM_SERVICE_MOD\":15.0,\"X5km_max_bw\":200.0,\"X5km_prospect_perc_feasible\":0.926775147928994,\"lm_arc_bw_onrf\":0.0,\"POP_Category\":\"Metro Service Ready\",\"lm_nrc_bw_prov_ofrf\":0.0,\"FATG_Building_Type\":\"Commercial\",\"HH_0_5_access_rings_F\":\"NA\",\"hh_flag\":\"NA\",\"local_loop_interface\":\"FE\",\"X5km_prospect_avg_dist\":2738.92671182687,\"latitude_final\":13.0184111,\"FATG_Network_Location_Type\":\"Access\\/Customer POP\",\"topology\":\"primary_active\",\"BW_mbps_act\":2.0,\"city_tier\":\"Non_Tier1\",\"lm_nrc_bw_onwl\":0.0,\"X2km_prospect_min_dist\":2.21569108223123,\"X2km_prospect_perc_feasible\":0.94300518134715,\"X0.5km_prospect_num_feasible\":33.0,\"cpe_supply_type\":\"rental\",\"Orch_Connection\":\"Wireline\",\"site_id\":\"607_primary\",\"cpe_management_type\":\"Fully Managed\",\"Capex\":\"NA\",\"sales_org\":\"Enterprise\",\"opportunity_term\":12,\"BW_mbps\":2.0,\"resp_city\":\"Tiruvallur\",\"Burstable_BW\":0.0,\"connected_cust_tag\":0.0,\"net_pre_feasible_flag\":1.0,\"Network_F_NF_CC_Flag\":\"NA\",\"Orch_LM_Type\":\"Onnet\",\"X5km_prospect_avg_bw\":201.780650887574,\"FATG_Category\":\"Metro Service Ready\",\"lm_nrc_mast_ofrf\":0.0,\"Feasibility.Response..Created.Date\":17730.0,\"X2km_prospect_min_bw\":2.0,\"X5km_prospect_num_feasible\":1253.0,\"Selected\":false,\"lm_nrc_mast_onrf\":0.0,\"X5km_prospect_count\":1352.0,\"connection_type\":\"Premium\",\"X0.5km_avg_bw\":9999999.0,\"pool_size\":0,\"X2km_cust_count\":2.0,\"Customer_Segment\":\"Enterprise - Strategic\",\"customer_segment\":\"Enterprise - Strategic\",\"FATG_PROW\":\"No\",\"connected_building_tag\":0.0,\"FATG_DIST_KM\":41.33379039696604,\"X0.5km_prospect_max_bw\":500.0,\"X5km_prospect_min_dist\":2.21569108223123,\"Type\":\"OnnetWL\",\"FATG_Ring_type\":\"SDH\",\"longitude_final\":80.18774819999999,\"Orch_BW\":2.0,\"scenario_1\":0.0,\"scenario_2\":1.0,\"sno\":3,\"access_check_hh\":\"NA\",\"X2km_prospect_count\":386.0,\"min_hh_fatg\":41.33379039696604,\"POP_Construction_Status\":\"In Service\",\"X5km_avg_dist\":1616.32490273161,\"X2km_avg_dist\":1616.32490273161,\"X2km_prospect_avg_bw\":390.502590673575,\"X0.5km_prospect_min_dist\":2.21569108223123,\"cost_permeter\":0.0,\"Identifier\":\"NA\",\"prospect_name\":\"Kasturi & Sons Limited\",\"Predicted_Access_Feasibility\":\"Feasible\",\"X2km_max_bw\":200.0,\"X2km_prospect_max_bw\":10240.0,\"last_mile_contract_term\":\"1 Year\",\"X5km_prospect_min_bw\":0.064,\"X2km_min_bw\":120.0,\"burstable_bw\":0,\"seq_no\":141,\"bw_mbps\":2,\"X0.5km_cust_count\":0.0,\"X0.5km_min_bw\":9999999.0,\"product_name\":\"Internet Access Service\",\"Network_F_NF_HH\":\"NA\",\"HH_0_5_access_rings_NF\":\"NA\",\"X0.5km_min_dist\":9999999.0,\"POP_Building_Type\":\"Commercial\",\"core_check_hh\":\"NA\",\"HH_DIST_KM\":48.632402798319305,\"X0.5km_prospect_min_bw\":2.0,\"POP_DIST_KM_SERVICE\":14.152705282191185,\"lm_arc_bw_onwl\":33350.0,\"num_connected_building\":0.0,\"account_id_with_18_digit\":\"0012000000E1z9KAAR\",\"Orch_Category\":\"Capex less than 175m\",\"X0.5km_prospect_avg_dist\":243.087859734992,\"POP_Network_Location_Type\":\"Mega POP\",\"lm_nrc_mux_onwl\":58810.0,\"num_connected_cust\":0.0,\"HH_0_5km\":\"NA\",\"X5km_min_dist\":1424.59360546586,\"feasibility_response_created_date\":\"2018-07-18\",\"X2km_prospect_num_feasible\":364.0,\"additional_ip\":0,\"X2km_avg_bw\":160.0,\"X0.5km_max_bw\":9999999.0,\"X5km_avg_bw\":160.0,\"Status.v2\":\"NA\",\"X5km_prospect_max_bw\":10240.0,\"X2km_prospect_avg_dist\":1361.96557355991,\"cpe_variant\":\"None\",\"Longitude_final\":80.18774819999999,\"X5km_cust_count\":2.0,\"sum_no_of_sites_uni_len\":1,\"Product.Name\":\"Internet Access Service\",\"X0.5km_prospect_perc_feasible\":1.0,\"POP_TCL_Access\":\"Yes\",\"X0.5km_prospect_count\":33.0,\"Probabililty_Access_Feasibility\":0.8533333333333334,\"lm_nrc_bw_onrf\":0.0,\"quotetype_quote\":\"New Order\",\"X2km_min_dist\":1424.59360546586,\"FATG_TCL_Access\":\"No\",\"Network_F_NF_HH_Flag\":\"NA\"},{\"lm_nrc_ospcapex_onwl\":0.0,\"total_cost\":62375.757575757576,\"otc_tikona\":10000.0,\"Latitude_final\":13.0184111,\"lm_arc_bw_prov_ofrf\":35000.0,\"lm_nrc_nerental_onwl\":0.0,\"lm_nrc_inbldg_onwl\":0.0,\"offnet_5km_Min_accuracy_num\":50.0,\"interim_BW\":2.0,\"offnet_5km_Min_DistanceKilometers\":1.2178610509953627,\"rank\":5,\"offnet_0_5km_Max_BW_Mbps\":0.0,\"lm_arc_bw_onrf\":0.0,\"prospect_0_5km_Min_BW_Mbps\":0.0,\"lm_nrc_bw_prov_ofrf\":10000.0,\"local_loop_interface\":\"FE\",\"bw_flag_32\":0.0,\"arc_tikona\":35000.0,\"latitude_final\":13.0184111,\"topology\":\"primary_active\",\"min_mast_ht\":0.0,\"lm_nrc_bw_onwl\":0.0,\"cpe_supply_type\":\"rental\",\"offnet_2km_Min_DistanceKilometers\":1.2178610509953627,\"prospect_2km_feasibility_pct\":0.7636363636363637,\"Orch_Connection\":\"Wireless\",\"bw_flag_3\":0.0,\"offnet_2km_cust_Count\":2.0,\"site_id\":\"607_primary\",\"cpe_management_type\":\"Fully Managed\",\"sales_org\":\"Enterprise\",\"opportunity_term\":12,\"BW_mbps\":2.0,\"resp_city\":\"Tiruvallur\",\"prospect_2km_Avg_DistanceKilometers\":1.4529000280961837,\"offnet_5km_Max_BW_Mbps\":16.0,\"prospect_2km_Sum_Feasibility_flag\":42.0,\"arc_sify\":45000.0,\"prospect_2km_Avg_BW_Mbps\":2.343709090909091,\"Orch_LM_Type\":\"Offnet\",\"max_mast_ht\":27.0,\"lm_nrc_mast_ofrf\":17375.757575757576,\"Feasibility.Response..Created.Date\":17730.0,\"Selected\":true,\"provider_tot_towers\":0.0,\"lm_nrc_mast_onrf\":0.0,\"connection_type\":\"Premium\",\"offnet_2km_Min_BW_Mbps\":0.512,\"pool_size\":0,\"offnet_0_5km_Min_BW_Mbps\":0.0,\"customer_segment\":\"Enterprise - Strategic\",\"offnet_5km_Avg_BW_Mbps\":1.7612,\"time_taken\":3.1749999999883585,\"prospect_0_5km_Sum_Feasibility_flag\":0.0,\"Local.Loop.Interface\":\"FE\",\"prospect_2km_cust_Count\":55.0,\"Type\":\"OffnetRF\",\"longitude_final\":80.18774819999999,\"Orch_BW\":2.0,\"prospect_2km_Max_BW_Mbps\":20.0,\"otc_sify\":15000.0,\"Customer.Segment\":\"Enterprise - Strategic\",\"provider_min_dist\":0.7138602916391107,\"offnet_5km_Min_BW_Mbps\":0.128,\"prospect_name\":\"Kasturi & Sons Limited\",\"Predicted_Access_Feasibility\":\"Feasible\",\"avg_mast_ht\":9.696969696969697,\"last_mile_contract_term\":\"1 Year\",\"prospect_0_5km_feasibility_pct\":0.0,\"burstable_bw\":0,\"bw_mbps\":2,\"prospect_0_5km_Max_BW_Mbps\":0.0,\"offnet_0_5km_Min_accuracy_num\":0.0,\"product_name\":\"Internet Access Service\",\"closest_provider\":\"tikona_min_dist_km\",\"lm_arc_bw_onwl\":0.0,\"nearest_mast_ht_cost\":56400.0,\"account_id_with_18_digit\":\"0012000000E1z9KAAR\",\"Orch_Category\":\"Tikona\",\"lm_nrc_mux_onwl\":0.0,\"offnet_2km_Avg_BW_Mbps\":1.256,\"offnet_2km_Min_accuracy_num\":50.0,\"cust_count\":132.0,\"offnet_0_5km_Avg_BW_Mbps\":0.0,\"feasibility_response_created_date\":\"2018-07-18\",\"prospect_0_5km_Min_DistanceKilometers\":99999.0,\"additional_ip\":0,\"nearest_mast_ht\":18.0,\"cpe_variant\":\"None\",\"prospect_2km_Min_BW_Mbps\":0.064,\"Longitude_final\":80.18774819999999,\"Last.Mile.Contract.Term\":\"1 Year\",\"sum_no_of_sites_uni_len\":1,\"Product.Name\":\"Internet Access Service\",\"offnet_0_5km_Min_DistanceKilometers\":99999.0,\"offnet_0_5km_cust_Count\":0.0,\"prospect_0_5km_Avg_BW_Mbps\":0.0,\"Probabililty_Access_Feasibility\":0.9666666666666667,\"lm_nrc_bw_onrf\":0.0,\"quotetype_quote\":\"New Order\"}");
		return sFb;
	}

	public QuoteNplLink getQuoteNplLink() {
		QuoteNplLink nplLink = new QuoteNplLink();
		nplLink.setChargeableDistance("1");
		nplLink.setCreatedBy(1);
		nplLink.setCreatedDate(new Date());
		nplLink.setId(1);
		nplLink.setLinkCode("sdfsdf");
		nplLink.setProductSolutionId(1);
		nplLink.setQuoteId(1);
		nplLink.setSiteAId(1);
		nplLink.setSiteBId(1);
		nplLink.setSiteAType("SITE");
		nplLink.setSiteBType("SITE");
		nplLink.setStatus((byte) 1);
		nplLink.setFeasibility((byte) 1);
		nplLink.setFpStatus("N");
		nplLink.setWorkflowStatus("sdfsdf");
		return nplLink;
	}
	
	public QuoteNplLink getQuoteNplLinkForMFMP() {
		QuoteNplLink nplLink = new QuoteNplLink();
		nplLink.setChargeableDistance("1");
		nplLink.setCreatedBy(1);
		nplLink.setCreatedDate(new Date());
		nplLink.setId(1);
		nplLink.setLinkCode("sdfsdf");
		nplLink.setProductSolutionId(1);
		nplLink.setQuoteId(1);
		nplLink.setSiteAId(1);
		nplLink.setSiteBId(1);
		nplLink.setSiteAType("SITE");
		nplLink.setSiteBType("SITE");
		nplLink.setStatus((byte) 1);
		nplLink.setFeasibility((byte) 1);
		nplLink.setFpStatus("MFMP");
		nplLink.setWorkflowStatus("sdfsdf");
		return nplLink;
	}

	public List<QuoteNplLink> getQuoteNplLinkList() {
		List<QuoteNplLink> quoteNplLink = new ArrayList<QuoteNplLink>();
		quoteNplLink.add(getQuoteNplLink());
		return quoteNplLink;
	}
	
	public List<QuoteNplLink> getQuoteNplLinkListMFMP() {
		List<QuoteNplLink> quoteNplLink = new ArrayList<QuoteNplLink>();
		quoteNplLink.add(getQuoteNplLinkForMFMP());
		return quoteNplLink;
	}
	
	public List<QuoteNplLink> getQuoteNplLinkNonFeasibleList() {
		List<QuoteNplLink> quoteNplLink = new ArrayList<QuoteNplLink>();
		QuoteNplLink nplLink =getQuoteNplLink();
		nplLink.setFeasibility((byte) 0);
		quoteNplLink.add(nplLink);
		return quoteNplLink;
	}
	public List<QuoteIllSiteSla> getQuoteIllSiteSlaList() {
		List<QuoteIllSiteSla> list = new ArrayList<>();
		QuoteIllSiteSla sla = new QuoteIllSiteSla();
		sla.setId(1);
		sla.setQuoteIllSite(getQuoteIllSite());
		sla.setSlaEndDate(new Date());
		sla.setSlaMaster(new SlaMaster());
		sla.setSlaStartDate(new Date());
		sla.setSlaValue("sla value");
		list.add(sla);
		return list;
	}
	
	public Set<QuoteIllSiteSla> getQuoteIllSiteSlaSet() {
		Set<QuoteIllSiteSla> list = new HashSet<>();
		QuoteIllSiteSla sla = new QuoteIllSiteSla();
		sla.setId(1);
		sla.setSlaEndDate(new Date());
		sla.setSlaMaster(new SlaMaster());
		sla.setSlaStartDate(new Date());
		sla.setSlaValue("sla value");
		list.add(sla);
		return list;
	}


	public List<SiteFeasibility> getSiteFeasibilityList() {
		List<SiteFeasibility> feasibilities = new ArrayList<>();
		SiteFeasibility feasibility = new SiteFeasibility();
		feasibility.setId(1);
		feasibility.setFeasibilityCheck("Check");
		feasibility.setFeasibilityMode("Manual");
		feasibility.setResponseJson("{\"lm_nrc_ospcapex_onwl\":0,\"lm_arc_bw_prov_ofrf\":0,\"lm_nrc_nerental_onwl\":0,\"lm_nrc_inbldg_onwl\":0,\"Network_Feasibility_Check\":\"Not Feasible\",\"rank\":4,\"lm_arc_bw_onrf\":59000,\"lm_nrc_bw_prov_ofrf\":0,\"local_loop_interface\":\"FE\",\"latitude_final\":13.036317,\"topology\":\"primary_active\",\"lm_nrc_bw_onwl\":0,\"cpe_supply_type\":\"rental\",\"additional_ip_flag\":\"No\",\"Orch_Connection\":\"Wireless\",\"site_id\":\"2_primary\",\"cpe_management_type\":\"unmanaged\",\"sales_org\":\"Enterprise\",\"opportunity_term\":12,\"error_flag\":0,\"resp_city\":\"Chennai\",\"Orch_LM_Type\":\"Onnet\",\"ipv4_address_pool_size\":\"29\",\"lm_nrc_mast_ofrf\":0.0,\"Selected\":false,\"lm_nrc_mast_onrf\":42300,\"error_msg\":\"No error\",\"connection_type\":\"Standard\",\"ip_address_arrangement\":\"None\",\"customer_segment\":\"Enterprise – Growth Accounts\",\"time_taken\":3.731,\"Type\":\"OnnetRF\",\"longitude_final\":80.26758,\"Orch_BW\":2,\"ipv6_address_pool_size\":\"0\",\"prospect_name\":\"Shriram Value Services Private Limited\",\"Predicted_Access_Feasibility\":\"Feasible\",\"last_mile_contract_term\":\"1 Year\",\"burstable_bw\":2,\"bw_mbps\":2,\"product_name\":\"Internet Access Service\",\"error_code\":\"NA\",\"lm_arc_bw_onwl\":0,\"account_id_with_18_digit\":\"0012000000FtM59AAF\",\"Orch_Category\":\"UBR P2P - Onnet\",\"lm_nrc_mux_onwl\":0,\"feasibility_response_created_date\":\"2018-08-15\",\"cpe_variant\":\"None\",\"sum_no_of_sites_uni_len\":1,\"Probabililty_Access_Feasibility\":0.98,\"lm_nrc_bw_onrf\":25000,\"quotetype_quote\":\"New Order\",\"interim_BW\":2,\"prospect_0_5km_Min_BW_Mbps\":0,\"prospect_2km_feasibility_pct\":0.82653064,\"prospect_2km_Avg_DistanceKilometers\":1.2506608,\"prospect_2km_Sum_Feasibility_flag\":81,\"prospect_2km_Avg_BW_Mbps\":1.3648163,\"prospect_0_5km_Sum_Feasibility_flag\":6,\"prospect_2km_Max_BW_Mbps\":4,\"prospect_0_5km_feasibility_pct\":0,\"prospect_0_5km_Max_BW_Mbps\":2,\"prospect_0_5km_Min_DistanceKilometers\":0,\"prospect_2km_Min_BW_Mbps\":0.064,\"prospect_0_5km_Avg_BW_Mbps\":0}");
		feasibility.setType("OnnetWL");
		feasibilities.add(feasibility);
		return feasibilities;
	}

	public LeProductSla createLeProductSla() {
		LeProductSla leProductSla = new LeProductSla();
		leProductSla.setErfCustomerId(1);
		leProductSla.setErfCustomerLeId(1);
		leProductSla.setId(1);
		leProductSla.setMstProductFamily(getMstProductFamily());
		leProductSla.setSlaTier("tier");
		leProductSla.setSlaValue("sla");
		leProductSla.setSlaMaster(createSlaMaster());
		return leProductSla;
	}

	public List<LeProductSla> createLeProductSlaList() {
		List<LeProductSla> leProductSlaList = new ArrayList<>();
		leProductSlaList.add(createLeProductSla());
		leProductSlaList.add(createLeProductSla());
		return leProductSlaList;
	}

	public Set<QuoteIllSiteSla> createQuoteIllSiteSlaSet() {
		Set<QuoteIllSiteSla> setAttri = new HashSet<>();
		setAttri.add(createQuoteIllSiteSlaWithoutSlaMaster());
		return setAttri;
	}

	public SlaMaster createSlaMaster() {
		SlaMaster slaMaster = new SlaMaster();
		slaMaster.setId(1);
		slaMaster.setQuoteIllSiteSlas(createQuoteIllSiteSlaSet());
		slaMaster.setSlaName("Service Availability %");
		slaMaster.setSlaDurationInDays(1);
		return slaMaster;
	}
	
	public SlaMasterBean createSlaMasterBean() {
		SlaMasterBean slaMaster = new SlaMasterBean();
		slaMaster.setId(1);
		slaMaster.setSlaName("sla");
		slaMaster.setSlaDurationInDays(1);
		return slaMaster;
	}

	public QuoteIllSiteSla createQuoteIllSiteSla() {
		QuoteIllSiteSla leProductSla = new QuoteIllSiteSla();
		leProductSla.setId(1);
		leProductSla.setQuoteIllSite(getQuoteIllSite());
		leProductSla.setSlaMaster(createSlaMaster());
		leProductSla.setSlaValue("sla");
		return leProductSla;
	}

	public QuoteIllSiteSla createQuoteIllSiteSlaWithoutSlaMaster() {
		QuoteIllSiteSla leProductSla = new QuoteIllSiteSla();
		leProductSla.setId(1);
		leProductSla.setQuoteIllSite(getQuoteIllSite());
		return leProductSla;
	}

	public List<QuoteIllSiteSla> createQuoteIllSiteSlaList() {
		List<QuoteIllSiteSla> leProductSlaList = new ArrayList<>();
		leProductSlaList.add(createQuoteIllSiteSla());
		leProductSlaList.add(createQuoteIllSiteSla());
		return leProductSlaList;
	}

	public SlaUpdateRequest slaUpdateRequest() {
		SlaUpdateRequest slaUpdateRequest = new SlaUpdateRequest();
		slaUpdateRequest.setProductFamily("ill");
		slaUpdateRequest.setQuoteLeId(1);
		slaUpdateRequest.setSiteId(1);
		return slaUpdateRequest;
	}

	public String createSlaDetailsJson() {
		String jsonString = "";
		return jsonString;
	}

	public SLaDetailsBean createSLaDetailsBean() {
		SLaDetailsBean slaDetailsBean = new SLaDetailsBean();
		slaDetailsBean.setName("sla");
		slaDetailsBean.setSlaTier("tier1");
		slaDetailsBean.setSlaValue("sla");
		return slaDetailsBean;
	}

	public String createSLaDetailsBeanList() throws TclCommonException {
		ProductSlaBean productSlaBean = new ProductSlaBean();
		List<SLaDetailsBean> SLaDetailsBeanList = new ArrayList<>();
		SLaDetailsBeanList.add(createSLaDetailsBean());
		productSlaBean.setsLaDetails(SLaDetailsBeanList);
		String json = Utils.convertObjectToJson(productSlaBean).toString();
		return json;
	}

	/**
	 * getQuoteFamiles-mock values
	 * 
	 * @return {@link QuoteToLeProductFamily}
	 */
	public Set<QuoteToLeProductFamily> getQuoteFamilesWithoutProductSolutions() {
		Set<QuoteToLeProductFamily> mockQoteFamily = new HashSet<>();
		QuoteToLeProductFamily quoTefamily = new QuoteToLeProductFamily();
		quoTefamily.setId(1);
		quoTefamily.toString();
		quoTefamily.setMstProductFamily(getMstProductFamily());
		quoTefamily.setProductSolutions(getProductSolutionwithoutMstOffering());
		mockQoteFamily.add(quoTefamily);

		return mockQoteFamily;
	}

	/**
	 * getProductSolution-mock values
	 * 
	 * @return {@link ProductSolution}
	 */
	public Set<ProductSolution> getProductSolutionwithoutMstOffering() {

		Set<ProductSolution> productSolutions = new HashSet<>();
		ProductSolution productSolution = new ProductSolution();
		productSolution.setId(1);
		productSolution.setProductProfileData(
				"{\"offeringName\":\"Single Internet Access\",\"image\":\"\",\"bandwidth\":\"10\",\"bandwidthUnit\":\"mbps\",\"components\":[{\"componentId\":\"1\",\"componentMasterId\":\"1\",\"name\":\"CPE Management\",\"isActive\":\"true\",\"attributes\":[{\"attributeId\":\"1\",\"attributeMasterId\":\"1\",\"name\":\"CPE Manufacturer\",\"value\":\"CISCO\"}]}]}");
		productSolution.setQuoteIllSites(getIllsitesList());
		productSolutions.add(productSolution);
		return productSolutions;
	}

	/**
	 * getProductSolution-mock values
	 * 
	 * @return {@link ProductSolution}
	 * @throws TclCommonException
	 */
	public String getAddressDetail() throws TclCommonException {
		AddressDetail addressDetail = new AddressDetail();
		addressDetail.setAddressId(1);
		addressDetail.setAddressLineOne("line1");
		addressDetail.setCity("chennai");
		addressDetail.setCountry("INDIA");
		addressDetail.setLatLong("10,20");
		String json = Utils.convertObjectToJson(addressDetail);
		return json;
	}

	/**
	 * getProductSolution-mock values
	 * 
	 * @return {@link ProductSolution}
	 * @throws TclCommonException
	 */
	public OpportunityResponseBean createOpportunityResponseBean() {
		OpportunityResponseBean opportunityResponseBean = new OpportunityResponseBean();
		opportunityResponseBean.setCustomOptyId("1");
		opportunityResponseBean.setMessage("optimus");
		opportunityResponseBean.setOpportunity(getOpportunityResponse());
		return opportunityResponseBean;
	}

	/**
	 * getProductSolution-mock values
	 * 
	 * @return {@link ProductSolution}
	 * @throws TclCommonException
	 */
	public OpportunityResponse getOpportunityResponse() {
		OpportunityResponse opportunityResponse = new OpportunityResponse();
		opportunityResponse.setAccountId("1");
		opportunityResponse.setBillingFrequency("years");
		opportunityResponse.setBillingMethod("method");
		opportunityResponse.setCofType("cof");
		opportunityResponse.setPortalTransactionId("1");
		return opportunityResponse;
	}

	/**
	 * getProductSolution-mock values
	 * 
	 * @return {@link ProductSolution}
	 * @throws TclCommonException
	 */
	public ProductsserviceResponse getProductsserviceResponse() {
		ProductsserviceResponse productsserviceResponse = new ProductsserviceResponse();
		productsserviceResponse.setId("1");
		return productsserviceResponse;
	}

	/**
	 * getProductSolution-mock values
	 * 
	 * @return {@link ProductSolution}
	 * @throws TclCommonException
	 */
	public ProductServicesResponseBean getProductServicesResponseBean() {
		ProductServicesResponseBean ProductServicesResponseBean = new ProductServicesResponseBean();
		ProductServicesResponseBean.setMessage("optimus");
		ProductServicesResponseBean.setProductId("1");
		List<ProductsserviceResponse> list = new ArrayList<>();
		list.add(getProductsserviceResponse());
		ProductServicesResponseBean.setProductsservices(list);
		ProductServicesResponseBean.setSfdcid("1");
		ProductServicesResponseBean.setStatus("1");
		return ProductServicesResponseBean;
	}

	/**
	 * getProductSolution-mock values
	 * 
	 * @return {@link ProductSolution}
	 * @throws TclCommonException
	 */
	public StagingResponseBean getStagingResponseBean() {
		StagingResponseBean stagingResponseBean = new StagingResponseBean();
		stagingResponseBean.setCustomOptyId("1");
		stagingResponseBean.setMessage("optimus");
		stagingResponseBean.setOpportunity(getOpportunityResponse());
		stagingResponseBean.setStatus("1");
		return stagingResponseBean;
	}

	/**
	 * getProductSolution-mock values
	 * 
	 * @return {@link ProductSolution}
	 * @throws TclCommonException
	 */
	public List<SiteLocationResponse> getSiteLocationResponseListn() {
		SiteLocationResponse siteLocationResponse = new SiteLocationResponse();
		siteLocationResponse.setLocation("chennai");
		siteLocationResponse.setSfdcSiteName("chennai");
		siteLocationResponse.setSiteId("1");
		siteLocationResponse.setSiteName("chennai");
		List<SiteLocationResponse> list = new ArrayList<>();
		list.add(siteLocationResponse);
		return list;
	}

	/**
	 * getProductSolution-mock values
	 * 
	 * @return {@link ProductSolution}
	 * @throws TclCommonException
	 */
	public SiteResponseBean getSiteResponseBean() {
		SiteResponseBean siteResponseBean = new SiteResponseBean();
		siteResponseBean.setMessage("optimus");
		siteResponseBean.setOpportunityId("1");
		siteResponseBean.setProductService("service");
		siteResponseBean.setStatus("1");
		siteResponseBean.setSiteLocations(getSiteLocationResponseListn());
		return siteResponseBean;
	}

	public OrderSiteRequest getOrderSiteRequest() {
		OrderSiteRequest orderSiteRequest = new OrderSiteRequest();
		orderSiteRequest.setMstOrderSiteStageName("test");
		orderSiteRequest.setMstOrderSiteStatusName("test");
		return orderSiteRequest;
	}

	public MstOrderSiteStatus getMstOrderSiteStatus() {
		MstOrderSiteStatus mstOrderSiteStatus = new MstOrderSiteStatus();
		mstOrderSiteStatus.setId(1);
		mstOrderSiteStatus.setCode("1");
		mstOrderSiteStatus.setIsActive((byte) 1);
		mstOrderSiteStatus.setName("master");
		return mstOrderSiteStatus;
	}
	
	public MstOrderLinkStatus getMstOrderLinkStatus() {
		MstOrderLinkStatus mstOrderLinkStatus = new MstOrderLinkStatus();
		mstOrderLinkStatus.setId(1);
		mstOrderLinkStatus.setCode("1");
		mstOrderLinkStatus.setIsActive((byte) 1);
		mstOrderLinkStatus.setName("master");
		return mstOrderLinkStatus;
	}

	public MstOrderSiteStage getMstOrderSiteStage() {
		MstOrderSiteStage mstOrderSiteStage = new MstOrderSiteStage();
		mstOrderSiteStage.setId(1);
		mstOrderSiteStage.setName("site");
		return mstOrderSiteStage;

	}
	
	public MstOrderLinkStage getMstOrderLinkStage() {
		MstOrderLinkStage mstOrderLinkStage = new MstOrderLinkStage();
		mstOrderLinkStage.setId(1);
		mstOrderLinkStage.setName("site");
		return mstOrderLinkStage;

	}

	public List<OrderSiteStatusAudit> getOrderSiteStatusAuditList() {
		List<OrderSiteStatusAudit> orderSiteStatusAuditList = new ArrayList<>();
		OrderSiteStatusAudit orderSiteStatusAudit = new OrderSiteStatusAudit();
		orderSiteStatusAudit.setCreatedBy("Vishesh");
		orderSiteStatusAudit.setCreatedTime(new Timestamp(0));
		orderSiteStatusAudit.setEndTime(null);
		orderSiteStatusAudit.setMstOrderSiteStatus(getMstOrderSiteStatus());
		orderSiteStatusAudit.setOrderSiteStageAudits(orderSiteStageAudits());
		orderSiteStatusAuditList.add(orderSiteStatusAudit);
		return orderSiteStatusAuditList;
	}
	
	public List<OrderLinkStatusAudit> getOrderLinkStatusAuditList() {
		List<OrderLinkStatusAudit> orderLinkStatusAuditList = new ArrayList<>();
		OrderLinkStatusAudit orderLinkStatusAudit = new OrderLinkStatusAudit();
		orderLinkStatusAudit.setCreatedBy("Vishesh");
		orderLinkStatusAudit.setCreatedTime(new Timestamp(0));
		orderLinkStatusAudit.setEndTime(null);
		orderLinkStatusAudit.setMstOrderLinkStatus(getMstOrderLinkStatus());
		orderLinkStatusAudit.setOrderLinkStageAudits(orderLinkStageAudits());
		orderLinkStatusAuditList.add(orderLinkStatusAudit);
		return orderLinkStatusAuditList;
	}

	public Set<OrderSiteStageAudit> orderSiteStageAudits() {
		Set<OrderSiteStageAudit> set = new HashSet<>();
		OrderSiteStageAudit audit = new OrderSiteStageAudit();
		audit.setCreatedBy("oms");
		audit.setCreatedTime(new Timestamp(0));
		audit.setEndTime(new Timestamp(0));
		audit.setId(1);
		audit.setIsActive((byte) 1);
		audit.setMstOrderSiteStage(getMstOrderSiteStage());
		return set;
	}

	public Set<OrderLinkStageAudit> orderLinkStageAudits() {
		Set<OrderLinkStageAudit> set = new HashSet<>();
		OrderLinkStageAudit audit = new OrderLinkStageAudit();
		audit.setCreatedBy("oms");
		audit.setCreatedTime(new Timestamp(0));
		audit.setEndTime(new Timestamp(0));
		audit.setId(1);
		audit.setIsActive((byte) 1);
		audit.setMstOrderLinkStage(getMstOrderLinkStage());
		return set;
	}

	public List<OrderSiteStageAudit> getOrderSiteStageAuditList() {
		List<OrderSiteStageAudit> list = new ArrayList<>();
		OrderSiteStageAudit audit = new OrderSiteStageAudit();
		audit.setEndTime(null);
		list.add(audit);
		return list;
	}
	
	public List<OrderLinkStageAudit> getOrderLinkStageAuditList() {
		List<OrderLinkStageAudit> list = new ArrayList<>();
		OrderLinkStageAudit audit = new OrderLinkStageAudit();
		audit.setCreatedBy("oms");
		audit.setCreatedTime(new Timestamp(0));
		audit.setEndTime(new Timestamp(0));
		audit.setId(1);
		audit.setIsActive((byte) 1);
		audit.setMstOrderLinkStage(getMstOrderLinkStage());
		list.add(audit);
		return list;
	}

	public RestResponse cretateResponse() {
		RestResponse response = new RestResponse();
		response.setData("{\r\n" + "    \"results\": [\r\n" + "        {\r\n"
				+ "            \"Account.RTM_Cust\": \"Direct\",\r\n"
				+ "            \"Account_id_with_18_Digit\": \"0012000000BSpusAAD\",\r\n"
				+ "            \"Adjusted_CPE_Discount\": 0.25,\r\n" + "            \"Adjustment_Factor\": 0.07,\r\n"
				+ "            \"BW_mbps_upd\": \"10\",\r\n"
				+ "            \"Bucket_Adjustment_Type\": \"No Change\",\r\n" + "            \"Burstable_BW\": 10,\r\n"
				+ "            \"CPE_HW_MP\": \"NA\",\r\n" + "            \"CPE_Hardware_LP_USD\": \"NA\",\r\n"
				+ "            \"CPE_Installation_INR\": \"NA\",\r\n"
				+ "            \"CPE_Installation_MP\": \"NA\",\r\n" + "            \"CPE_Management_INR\": \"NA\",\r\n"
				+ "            \"CPE_Support_INR\": \"NA\",\r\n" + "            \"CPE_Support_MP\": \"NA\",\r\n"
				+ "            \"CPE_Variant\": \"None\",\r\n"
				+ "            \"CPE_management_type\": \"full_managed\",\r\n" + "            \"CPE_pred\": 0.28,\r\n"
				+ "            \"CPE_supply_type\": \"rental\",\r\n" + "            \"Discounted_CPE_ARC\": 0,\r\n"
				+ "            \"Discounted_CPE_MRC\": 0,\r\n" + "            \"Discounted_CPE_NRC\": 0,\r\n"
				+ "            \"GVPN_ARC_per_BW\": 36048.52,\r\n" + "            \"ILL_ARC\": 2063725.71,\r\n"
				+ "            \"ILL_ARC_per_BW\": 13267.29,\r\n" + "            \"ILL_CPE_ARC\": 0,\r\n"
				+ "            \"ILL_CPE_MRC\": 0,\r\n" + "            \"ILL_CPE_NRC\": 0,\r\n"
				+ "            \"ILL_NRC\": 125000,\r\n" + "            \"ILL_Port_ARC_Adjusted\": 224261.78,\r\n"
				+ "            \"ILL_Port_Adjusted_net_Price\": 231761.78,\r\n"
				+ "            \"ILL_Port_MRC_Adjusted\": 18688.48,\r\n"
				+ "            \"ILL_Port_NRC_Adjusted\": 7500,\r\n" + "            \"Identifier\": \"VALIDATION\",\r\n"
				+ "            \"Industry_Cust\": \"IT Enabled Services (ITeS)\",\r\n"
				+ "            \"Inv_GVPN_bw\": 3658,\r\n" + "            \"Inv_ILL_bw\": 1097,\r\n"
				+ "            \"Inv_NPL_bw\": 2,\r\n" + "            \"Inv_Other_bw\": 270,\r\n"
				+ "            \"Inv_Tot_BW\": 5027,\r\n" + "            \"Last_Mile_Cost_ARC\": 10871.19,\r\n"
				+ "            \"Last_Mile_Cost_MRC\": 905.93,\r\n" + "            \"Last_Mile_Cost_NRC\": 0,\r\n"
				+ "            \"Last_Modified_Date\": \"2018-07-24\",\r\n"
				+ "            \"NPL_ARC_per_BW\": 20107.54,\r\n" + "            \"OEM_Discount\": \"NA\",\r\n"
				+ "            \"OpportunityID_Prod_Identifier\": \"0012000000BSpusAAD|1\",\r\n"
				+ "            \"Orch_Connection\": \"Wireline\",\r\n" + "            \"Orch_LM_Type\": \"Onnet\",\r\n"
				+ "            \"Other_ARC_per_BW\": 10242.84,\r\n" + "            \"Recovery_INR\": \"NA\",\r\n"
				+ "            \"Segment_Cust\": \"Enterprise\",\r\n"
				+ "            \"Sum_CAT_1_2_MACD_FLAG\": 218,\r\n"
				+ "            \"Sum_CAT_1_2_New_Opportunity_FLAG\": 41,\r\n"
				+ "            \"Sum_CAT_3_MACD_FLAG\": 6,\r\n"
				+ "            \"Sum_CAT_3_New_Opportunity_FLAG\": 1,\r\n"
				+ "            \"Sum_CAT_4_MACD_FLAG\": 0,\r\n"
				+ "            \"Sum_CAT_4_New_Opportunity_FLAG\": 0,\r\n" + "            \"Sum_Cat_1_2_opp\": 259,\r\n"
				+ "            \"Sum_GVPN_Flag\": 79,\r\n" + "            \"Sum_IAS_FLAG\": 27,\r\n"
				+ "            \"Sum_MACD_Opportunity\": 224,\r\n" + "            \"Sum_NPL_Flag\": 1,\r\n"
				+ "            \"Sum_New_ARC_Converted\": 149225488,\r\n"
				+ "            \"Sum_New_ARC_Converted_GVPN\": 131865488,\r\n"
				+ "            \"Sum_New_ARC_Converted_ILL\": 14554213,\r\n"
				+ "            \"Sum_New_ARC_Converted_NPL\": 40215.08,\r\n"
				+ "            \"Sum_New_ARC_Converted_Other\": 2765566.25,\r\n"
				+ "            \"Sum_New_Opportunity\": 42,\r\n" + "            \"Sum_Offnet_Flag\": 0,\r\n"
				+ "            \"Sum_Onnet_Flag\": 0,\r\n" + "            \"Sum_Other_Flag\": 15,\r\n"
				+ "            \"Sum_tot_oppy_historic_opp\": 266,\r\n"
				+ "            \"Sum_tot_oppy_historic_prod\": 15,\r\n" + "            \"TOT_ARC_per_BW\": 29684.8,\r\n"
				+ "            \"Total_CPE_Cost\": 0,\r\n" + "            \"Total_CPE_Price\": 0,\r\n"
				+ "            \"account_id_with_18_digit\": \"0012000000BSpusAAD\",\r\n"
				+ "            \"additional_IP_ARC\": 0,\r\n" + "            \"additional_IP_MRC\": 0,\r\n"
				+ "            \"additional_ip_flag\": \"No\",\r\n"
				+ "            \"burst_per_MB_price\": 27811.41,\r\n" + "            \"burstable_bw\": \"10\",\r\n"
				+ "            \"bw_mbps\": \"10\",\r\n" + "            \"calc_arc_list_inr\": 2063725.71,\r\n"
				+ "            \"connection_type\": \"Standard\",\r\n"
				+ "            \"cpe_management_type\": \"full_managed\",\r\n"
				+ "            \"cpe_supply_type\": \"rental\",\r\n" + "            \"cpe_variant\": \"None\",\r\n"
				+ "            \"createdDate_quote\": 0,\r\n"
				+ "            \"customer_segment\": \"Enterprise - Gold\",\r\n" + "            \"datediff\": 0,\r\n"
				+ "            \"error_code\": \"NA\",\r\n" + "            \"error_flag\": 0,\r\n"
				+ "            \"error_msg\": \"No error\",\r\n" + "            \"f_lm_arc_bw_onrf\": \"0\",\r\n"
				+ "            \"f_lm_arc_bw_onwl\": \"100040\",\r\n"
				+ "            \"f_lm_arc_bw_prov_ofrf\": \"0\",\r\n" + "            \"f_lm_nrc_bw_onrf\": \"0\",\r\n"
				+ "            \"f_lm_nrc_bw_onwl\": \"0\",\r\n" + "            \"f_lm_nrc_bw_prov_ofrf\": \"0\",\r\n"
				+ "            \"f_lm_nrc_inbldg_onwl\": \"40000\",\r\n"
				+ "            \"f_lm_nrc_mast_ofrf\": \"0.0\",\r\n" + "            \"f_lm_nrc_mast_onrf\": \"0\",\r\n"
				+ "            \"f_lm_nrc_mux_onwl\": \"58810\",\r\n"
				+ "            \"f_lm_nrc_nerental_onwl\": \"0\",\r\n"
				+ "            \"f_lm_nrc_ospcapex_onwl\": \"0\",\r\n"
				+ "            \"feasibility_response_created_date\": \"2018-07-24\",\r\n"
				+ "            \"hist_flag\": 0,\r\n" + "            \"ip_address_arrangement\": \"None\",\r\n"
				+ "            \"ipv4_address_pool_size\": \"8\",\r\n"
				+ "            \"ipv6_address_pool_size\": \"0\",\r\n"
				+ "            \"last_mile_contract_term\": \"1 Year\",\r\n"
				+ "            \"latitude_final\": \"13.018411\",\r\n" + "            \"list_price_mb\": 206372.57,\r\n"
				+ "            \"list_price_mb_dummy\": 0,\r\n" + "            \"local_loop_interface\": \"FE\",\r\n"
				+ "            \"log_Inv_Tot_BW\": 8.52,\r\n" + "            \"log_Inv_Tot_BW_dummy\": 0,\r\n"
				+ "            \"longitude_final\": \"80.18775\",\r\n"
				+ "            \"num_products_opp_new.x\": 0,\r\n" + "            \"opportunityTerm\": 12,\r\n"
				+ "            \"opportunity_day\": 1,\r\n" + "            \"opportunity_month\": 7,\r\n"
				+ "            \"opportunity_term\": \"12\",\r\n" + "            \"overall_BW_mbps_upd\": 10,\r\n"
				+ "            \"overall_CPE_node\": \"Node_5\",\r\n"
				+ "            \"overall_PortType\": \"Fixed\",\r\n" + "            \"overall_node\": \"node_9\",\r\n"
				+ "            \"p_lm_arc_bw_onrf\": \"0\",\r\n" + "            \"p_lm_arc_bw_onwl\": \"100040\",\r\n"
				+ "            \"p_lm_arc_bw_prov_ofrf\": \"0\",\r\n" + "            \"p_lm_nrc_bw_onrf\": \"0\",\r\n"
				+ "            \"p_lm_nrc_bw_onwl\": \"0\",\r\n" + "            \"p_lm_nrc_bw_prov_ofrf\": \"0\",\r\n"
				+ "            \"p_lm_nrc_inbldg_onwl\": \"0\",\r\n"
				+ "            \"p_lm_nrc_mast_ofrf\": \"0.0\",\r\n" + "            \"p_lm_nrc_mast_onrf\": \"0\",\r\n"
				+ "            \"p_lm_nrc_mux_onwl\": \"0\",\r\n" + "            \"p_lm_nrc_nerental_onwl\": \"0\",\r\n"
				+ "            \"p_lm_nrc_ospcapex_onwl\": \"0\",\r\n" + "            \"port_lm_arc\": 0,\r\n"
				+ "            \"port_pred_discount\": 0.89,\r\n"
				+ "            \"predicted_ILL_Port_ARC\": 224261.78,\r\n"
				+ "            \"predicted_ILL_Port_NRC\": 7500,\r\n"
				+ "            \"predicted_net_price\": 231761.78,\r\n"
				+ "            \"product_name\": \"Internet Access Service\",\r\n"
				+ "            \"prospect_name\": \"Intelenet Global Services Private Limited\",\r\n"
				+ "            \"quoteType_quote\": \"New Order\",\r\n"
				+ "            \"quotetype_quote\": \"New Order\",\r\n"
				+ "            \"resp_city\": \"Tiruvallur\",\r\n" + "            \"sales_org\": \"Enterprise\",\r\n"
				+ "            \"site_id\": \"1008_primary\",\r\n" + "            \"sum_cat1_2_Opportunity\": 259,\r\n"
				+ "            \"sum_cat_3_Opportunity\": 7,\r\n" + "            \"sum_cat_4_Opportunity\": 0,\r\n"
				+ "            \"sum_no_of_sites_uni_len\": \"1\",\r\n" + "            \"sum_offnet_flag\": \"0\",\r\n"
				+ "            \"sum_onnet_flag\": \"1\",\r\n" + "            \"sum_product_flavours.x\": 1,\r\n"
				+ "            \"time_taken\": 1.64,\r\n" + "            \"topology\": \"primary_active\",\r\n"
				+ "            \"tot_oppy_current_prod.x\": 1\r\n" + "        }\r\n" + "    ]\r\n" + "}\r\n" + "");
		response.setStatus(Status.SUCCESS);
		return response;
	}

	/**
	 * getProductSolution-mock values
	 * 
	 * @return {@link ProductSolution}
	 * @throws TclCommonException
	 */
	public List<FeasibilitySiteEngineBean> getFeasibilitySiteEngineBeanList() {
		List<FeasibilitySiteEngineBean> list = new ArrayList<>();
		FeasibilitySiteEngineBean feasibilitySiteEngineBean = new FeasibilitySiteEngineBean();
		feasibilitySiteEngineBean.setCity("chennai");
		feasibilitySiteEngineBean.setLatitude("0123");
		feasibilitySiteEngineBean.setLongitude("0123");
		feasibilitySiteEngineBean.setSiteId(1);
		feasibilitySiteEngineBean.setState("chennai");
		list.add(feasibilitySiteEngineBean);
		return list;
	}

	/**
	 * getProductSolution-mock values
	 * 
	 * @return {@link ProductSolution}
	 * @throws TclCommonException
	 */
	public FeasibilityBean getFeasibilityBean() {
		FeasibilityBean feasibilityBean = new FeasibilityBean();
		feasibilityBean.setLegalEntityId(1);
		feasibilityBean.setLegalEntityName("enitity");
		feasibilityBean.setSites(getFeasibilitySiteEngineBeanList());
		return feasibilityBean;
	}

	/**
	 * getProductSolution-mock values
	 * 
	 * @return {@link ProductSolution}
	 * @throws TclCommonException
	 */
	public List<CustomerAttributeBean> getCustomerAttributeBean() throws TclCommonException {
		List<CustomerAttributeBean> customerDetailsBeanList = new ArrayList<>();
		CustomerAttributeBean customerDetailsBean = new CustomerAttributeBean();
		customerDetailsBean.setName("ACCOUNT_ID_18");
		customerDetailsBean.setValue("tcl");
		CustomerAttributeBean customerDetailsBean1 = new CustomerAttributeBean();
		customerDetailsBean1.setName("CUSTOMER TYPE");
		customerDetailsBean1.setValue("tcl");
		CustomerAttributeBean customerDetailsBean2 = new CustomerAttributeBean();
		customerDetailsBean2.setName("SALES ORG");
		customerDetailsBean2.setValue("tcl");
		customerDetailsBeanList.add(customerDetailsBean);
		customerDetailsBeanList.add(customerDetailsBean1);
		customerDetailsBeanList.add(customerDetailsBean2);
		return customerDetailsBeanList;
	}

	/**
	 * getProductSolution-mock values
	 * 
	 * @return {@link ProductSolution}
	 * @throws TclCommonException
	 */
	public String getCustomerDetailsBean() throws TclCommonException {
		CustomerDetailsBean customerDetailsBean = new CustomerDetailsBean();
		customerDetailsBean.setCustomerAttributes(getCustomerAttributeBean());
		String json = Utils.convertObjectToJson(customerDetailsBean);
		return json;
	}
	
	public String getCustomerLegalEntityDetailsBean() throws TclCommonException {
		CustomerLegalEntityDetailsBean customerDetailsBean = new CustomerLegalEntityDetailsBean();
		customerDetailsBean.setCustomerLeDetails(getCustomerLeBeans());
		String json = Utils.convertObjectToJson(customerDetailsBean);
		return json;
	}
	public List<CustomerLeBean> getCustomerLeBeans(){
		List<CustomerLeBean> customerLeList = new ArrayList<>();
		CustomerLeBean leBean = new CustomerLeBean();
		leBean.setSfdcId("255");
		customerLeList.add(leBean);
		
		return customerLeList;
		
	}
	
	public CustomerDetailsBean getCustomerDetailBean() throws TclCommonException {
		CustomerDetailsBean customerDetailsBean = new CustomerDetailsBean();
		customerDetailsBean.setCustomerAttributes(getCustomerAttributeBean());
		return customerDetailsBean;
	}
	
	public List<CustomerDetail> getCustomerDetails() throws TclCommonException {
		List <CustomerDetail> custList = new ArrayList<>();

		CustomerDetail c = new CustomerDetail();
		c.setCustomerId(4);
		c.setCustomerAcId("SFDC - 000430");
		c.setErfCustomerId(2);
		c.setCustomerLeId(2);
		c.setCustomerName("cus 1");
		c.setStatus((byte) 1);
		
		CustomerDetail c1 = new CustomerDetail();
		c1.setCustomerId(4);
		c1.setCustomerAcId("SFDC - 000430");
		c1.setErfCustomerId(2);
		c1.setCustomerLeId(2);
		c1.setStatus((byte) 1);
		c1.setCustomerName("cus 2");

		
		custList.add(c1);
		
		return custList;
	}
	
	public OrderNplSiteBean getOrderNplSiteBean() {
		return new OrderNplSiteBean();
	}
	
	public Map<Integer, List<CustomerDetail>> getCustomerMap() throws TclCommonException{
		Map<Integer, List<CustomerDetail>> map = new HashMap <Integer, List<CustomerDetail>>();
		map.put(4, getCustomerDetails());
		return map;
	}

	public OrderNplLink getOrderNplLink() {
		OrderNplLink orderNplLink = new OrderNplLink();
		orderNplLink.setId(1);
		orderNplLink.setLinkCode("AS3GMIGZZFXRLMGP");
		orderNplLink.setProductSolutionId(419);
		orderNplLink.setSiteAId(905);
		orderNplLink.setSiteAType("DC");
		orderNplLink.setSiteBId(906);
		orderNplLink.setSiteBType("DC");
		orderNplLink.setOrderId(298);
		orderNplLink.setStatus((byte) 1);
		orderNplLink.setMstOrderLinkStatus(getMstOrderLinkStatus());
		orderNplLink.setMstOrderLinkStage(getMstOrderLinkStage());
		orderNplLink.setOrderNplLinkSlas(getOrderNplLinkSlaList());
		orderNplLink.setStage("PROVISION_LINK");
		orderNplLink.setFeasibility((byte) 1);
		return orderNplLink;

	}
	
	public Set <OrderNplLinkSla> getOrderNplLinkSlaList() {
		
		Set<OrderNplLinkSla> list = new HashSet<>();
		
		list.add(getOrderNplLinkSla());
		return list;
	}
	
public OrderNplLinkSla getOrderNplLinkSla() {
	OrderNplLinkSla sla = new OrderNplLinkSla();
	sla.setId(1);
	sla.setSlaEndDate(new Date());
	sla.setSlaStartDate(new Date());
	sla.setSlaValue("99.5");
	sla.setSlaMaster(createSlaMaster());
	return sla;
	}
	
public List<QuoteSlaBean> getQuoteSlaList() {
		
	List<QuoteSlaBean> list = new ArrayList<QuoteSlaBean>();
	QuoteSlaBean sla = new QuoteSlaBean();
		sla.setId(1);
		sla.setSlaEndDate(new Date());
		sla.setSlaStartDate(new Date());
		sla.setSlaMaster(createSlaMasterBean());
		sla.setSlaValue("99.5");
		list.add(sla);
		return list;
	}
	
	
	public List<OrderNplLink> getOrderNplLinkList() {
		List<OrderNplLink> orderNplLinks = new ArrayList<>();
		orderNplLinks.add(getOrderNplLink());
		return orderNplLinks;

	}

	public OrderIllSiteSla getOrderIllSiteSla() {
		OrderIllSiteSla orderIllSiteSla = new OrderIllSiteSla();
		orderIllSiteSla.setOrderIllSite(getOrderIllSite());
		orderIllSiteSla.setSlaEndDate(new Date());
		orderIllSiteSla.setSlaStartDate(new Date());
		orderIllSiteSla.setSlaValue("Varient");
		orderIllSiteSla.setSlaMaster(createSlaMaster());
		return orderIllSiteSla;

	}

	/*
	 * Get Order site fesiblity details
	 * 
	 * @author Anandhi Vijayaraghavan
	 * 
	 * @return OrderSiteFeasibility
	 * 
	 * @throws TclCommonException
	 */
	public List<OrderSiteFeasibility> getOrderIllSiteFeasiblity() {
		List<OrderSiteFeasibility> feasiblityList = new ArrayList<>();
		OrderSiteFeasibility feasiblity = new OrderSiteFeasibility();
		feasiblity.setId(1);
		feasiblity.setOrderIllSite(getOrderIllSite());
		feasiblity.setCreatedTime(new Timestamp(0));
		feasiblity.setFeasibilityCheck("Test");
		feasiblity.setFeasibilityMode("Test");
		feasiblity.setIsSelected((byte) 1);
		feasiblity.setProvider("Test");
		feasiblity.setRank(1);
		feasiblity.setResponseJson("{\"test\":\"sample\"}");
		feasiblity.setType("Type");
		feasiblityList.add(feasiblity);
		return feasiblityList;
	}

	/*
	 * Get Order site fesiblity details
	 * 
	 * @author Anandhi Vijayaraghavan
	 * 
	 * @return List<PricingDetail>
	 * 
	 * @throws TclCommonException
	 */
	public List<PricingEngineResponse> getPricingDetails() {
		List<PricingEngineResponse> pricingList = new ArrayList<>();
		PricingEngineResponse pricing = new PricingEngineResponse();
		pricing.setDateTime(new Timestamp(0));
		pricing.setId(1);
		pricing.setPriceMode("Test");
		pricing.setPricingType("Test");
		pricing.setRequestData("{\"test\":\"sample\"}");
		pricing.setResponseData("{\"test\":\"sample\"}");
		pricing.setSiteCode("TestCode");
		pricingList.add(pricing);
		return pricingList;
	}

	public OrderConfirmationAudit getOrderConfirmationAudit() {
		OrderConfirmationAudit orderConfirmationAudit = new OrderConfirmationAudit();
		orderConfirmationAudit.setName("Test");
		orderConfirmationAudit.setPublicIp("10.86.12.1");
		orderConfirmationAudit.setOrderRefUuid("45");
		orderConfirmationAudit.setCreatedTime(new Date());
		return orderConfirmationAudit;
	}

	public SfdcJob getSfdcJob() {
		SfdcJob sfdcjob = new SfdcJob();
		sfdcjob.setCode(quoteToLe.getQuote().getQuoteCode());
		sfdcjob.setCreatedTime(new Timestamp(new Date().getTime()));
		sfdcjob.setOperation(SFDCConstants.UPDATE_OPTY.toString());
		sfdcjob.setQueueName("sfdcUpdateOpty");
		sfdcjob.setRequestPayload("testRequest");
		sfdcjob.setStatus(SFDCConstants.CREATED.toString());
		sfdcjob.setType(SFDCConstants.OPTY.toString());
		return sfdcjob;
	}



	public ProductAttributeMaster getProductAtrribute() {
		ProductAttributeMaster productAttributeMaster = new ProductAttributeMaster();
		productAttributeMaster.setName("Service Variant");
		productAttributeMaster.setDescription("Cpe related");
		productAttributeMaster.setStatus((byte) 1);
		productAttributeMaster.setId(1);

		return productAttributeMaster;
	}

	public com.tcl.dias.common.beans.AddressDetail getAddressDetailcommon() {
		com.tcl.dias.common.beans.AddressDetail locationDetail = new com.tcl.dias.common.beans.AddressDetail();
		locationDetail.setAddressId(1);
		locationDetail.setCity("chennai");
		locationDetail.setCountry("INDIA");
		locationDetail.setLocationId(1);
		return locationDetail;
	}

	public LocationDetail getLocationDetail() {
		LocationDetail locationDetail = new LocationDetail();
		locationDetail.setPopId("1");
		locationDetail.setCustomerId(1);
		locationDetail.setApiAddress(getAddressDetailcommon());
		locationDetail.setAddress(getAddressDetailcommon());
		locationDetail.setErfCusCustomerLeId(1);
		locationDetail.setTier("1");
		locationDetail.setUserAddress(getAddressDetailcommon());
		locationDetail.setLatLong("1");
		locationDetail.setLocationId(1);
		return locationDetail;
	}
	
	public List<LocationDetail> getLocationDetails() {
		LocationDetail locationDetail = new LocationDetail();
		locationDetail.setPopId("1");
		locationDetail.setCustomerId(1);
		locationDetail.setApiAddress(getAddressDetailcommon());
		locationDetail.setAddress(getAddressDetailcommon());
		locationDetail.setErfCusCustomerLeId(1);
		locationDetail.setTier("1");
		locationDetail.setUserAddress(getAddressDetailcommon());
		locationDetail.setLatLong("1");
		locationDetail.setLocationId(1);
		List<LocationDetail> locationList = new ArrayList<>();
		locationList.add(locationDetail);
		return locationList;
	}

	public String getLocationDetailjson() throws TclCommonException {
		String json = Utils.convertObjectToJson(getLocationDetail());
		return json;
	}
	
	public String getLocationDetailsjson() throws TclCommonException {
		String json = Utils.convertObjectToJson(getLocationDetails());
		return json;
	}

	public Optional<QuoteToLe> returnQuoteToLeForUpdateStatusForContactAttributeInfo() {
		QuoteToLe le = new QuoteToLe();
		le.setId(1);
		le.setCurrencyId(1);
		le.setErfCusCustomerLegalEntityId(1);
		le.setErfCusSpLegalEntityId(1);
		le.setStage("Sample");
		le.setQuoteToLeProductFamilies(getQuoteToLeProductFamily());
		le.setTpsSfdcOptyId("tpsSfdcOptyId");
		le.setQuote(getQuote1());
		le.setQuoteLeAttributeValues(getQuoteLeAttributeValueSet1());
		return Optional.of(le);
	}

	public Set<QuoteLeAttributeValue> getQuoteLeAttributeValueSet1() {
		Set<QuoteLeAttributeValue> quoteLeAttributeValueSet = new HashSet<>();
		QuoteLeAttributeValue quoteLeAttributeValue = getQuoteLeAttributeValue();
		quoteLeAttributeValue.getMstOmsAttribute().setName("CONTACTID");
		quoteLeAttributeValue.setAttributeValue("1");
		quoteLeAttributeValueSet.add(quoteLeAttributeValue);
		return quoteLeAttributeValueSet;
	}

	public BillingRequest getBillingRequest() {
		BillingRequest billingRequest = new BillingRequest();
		billingRequest.setAccountId("1");
		billingRequest.setAccounCuId("1");
		billingRequest.setBillingContactId(1);
		billingRequest.setLegalEntityName("billing");
		billingRequest.setQuoteLeId(1);
		billingRequest.setCustomerLeId(1);
		billingRequest.setAttributes(billingAttributesBeanList());
		return billingRequest;
	}

	public List<BillingAttributesBean> billingAttributesBeanList() {
		List<BillingAttributesBean> list = new ArrayList<>();
		BillingAttributesBean billingAttributesBean = new BillingAttributesBean();
		billingAttributesBean.setAttributeName("Attribute name");
		billingAttributesBean.setAttributeValue("attribute value");
		billingAttributesBean.setType("attribute type");
		list.add(billingAttributesBean);
		return list;
	}

	public List<SiteFeasibility> getSiteFeasibilities1() {

		List<SiteFeasibility> fblist = new ArrayList<>();
		fblist.add(siteFeasibilities1());

		return fblist;
	}

	private SiteFeasibility siteFeasibilities1() {

		SiteFeasibility sFb = new SiteFeasibility();
		sFb.setId(1);
		sFb.setType("Onnet");

		return sFb;
	}

	public List<Map<String, Object>> getDashBoardConfiguration() {
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("productName", "optimus");
		map.put("quoteCode", "optimus");
		map.put("createdTime", new Date());
		map.put("quoteId", 1);
		map.put("stage", "optimus");
		map.put("siteCount", BigInteger.valueOf(1));
		list.add(map);
		return list;
	}

	public Map<String, String> craeteMap() {
		Map<String, String> map = new HashMap<>();
		map.put("", "");
		return map;

	}

	public OrderSiteFeasibility getSiteFeasiblity() {
		OrderSiteFeasibility feasiblity = new OrderSiteFeasibility();
		feasiblity.setId(1);
		feasiblity.setCreatedTime(new Timestamp(0));
		feasiblity.setFeasibilityCheck("Test");
		feasiblity.setFeasibilityMode("Test");
		feasiblity.setIsSelected((byte) 1);
		feasiblity.setProvider("Test");
		feasiblity.setRank(1);
		feasiblity.setResponseJson("{\"test\":\"sample\"}");
		feasiblity.setType("Type");

		return feasiblity;
	}

	public Set<OrderSiteFeasibility> getSiteFeasiblitySet() {
		Set<OrderSiteFeasibility> orderSiteFeasibilitySet = new HashSet<>();
		orderSiteFeasibilitySet.add(getSiteFeasiblity());
		return orderSiteFeasibilitySet;
	}

	public List<QuotePrice> getQuotePriceList()

	{
		List<QuotePrice> priceList = new ArrayList<QuotePrice>();

		QuotePrice qp = new QuotePrice();
		qp.setId(1);
		qp.setCatalogArc(Double.parseDouble("10000"));
		qp.setCatalogMrc(Double.parseDouble("5000"));
		qp.setCatalogNrc(Double.parseDouble("8000"));
		qp.setComputedArc(Double.parseDouble("342234"));
		qp.setComputedNrc(Double.parseDouble("345345"));
		qp.setComputedMrc(Double.parseDouble("23222"));
		qp.setEffectiveArc(Double.parseDouble("34324"));
		qp.setEffectiveMrc(Double.parseDouble("3234"));
		qp.setEffectiveNrc(Double.parseDouble("234234"));
		qp.setEffectiveUsagePrice(Double.parseDouble("234234"));
		qp.setMinimumMrc(Double.parseDouble("23434"));
		qp.setMinimumArc(Double.parseDouble("234234"));
		qp.setMinimumNrc(Double.parseDouble("234234"));
		priceList.add(qp);
		return priceList;

	}

	public CurrencyConversion getCurrencyConversionRate() {
		CurrencyConversion currency = new CurrencyConversion();
		currency.setId(1);
		currency.setInputCurrency("USD");
		currency.setOutputCurrency("INR");
		currency.setStatus("YES");
		currency.setConversionRate("68.15");
		return currency;
	}

	public List<SfdcJob> getSfdcJobList() throws TclCommonException {
		List<SfdcJob> sfdcJobsList = new ArrayList<>();
		SfdcJob sfdcJob = new SfdcJob();
		sfdcJob.setRequestPayload(getProductServiceBean());
		sfdcJobsList.add(sfdcJob);
		return sfdcJobsList;
	}

	public String getProductServiceBean() throws TclCommonException {
		return Utils.convertObjectToJson(new ProductServiceBean());
	}

	public List<SfdcJob> getSfdcJobList2() throws TclCommonException {
		List<SfdcJob> sfdcJobsList = new ArrayList<>();
		SfdcJob sfdcJob = new SfdcJob();
		sfdcJob.setRequestPayload(getSiteSolutionOpportunityBean());
		sfdcJobsList.add(sfdcJob);
		return sfdcJobsList;
	}

	public String getSiteSolutionOpportunityBean() throws TclCommonException {
		return Utils.convertObjectToJson(new SiteSolutionOpportunityBean());
	}
	
		public NplQuoteDetail getNplQuoteDetailForCreateNew1() {
		NplQuoteDetail quoteDetail = new NplQuoteDetail();
		quoteDetail.setCustomerId(25);
		quoteDetail.setProductName("NPL");
		quoteDetail.setSite(getNplSite3());
		quoteDetail.setQuoteleId(298);
		quoteDetail.setQuoteId(298);
		List<SolutionDetail> solutionDetails = new ArrayList<>();
		SolutionDetail solution = new SolutionDetail();
		solution.setOfferingName("Standard point-to-point conectivity");
		solution.setBandwidth("20");
		solution.setBandwidthUnit("mbps");
		solution.toString();
		SolutionDetail solution1 = new SolutionDetail();
		solution1.setOfferingName("Standard point-to-point conectivity");
		solution1.setBandwidth("20");
		solution1.setBandwidthUnit("mbps");
		solution1.toString();
		List<ComponentDetail> components = new ArrayList<>();

		ComponentDetail component = new ComponentDetail();
		/* component.toString(); */
		components.add(component);
		ComponentDetail component1 = new ComponentDetail();
		component1.setComponentMasterId(1);
		component1.setName("Private Lines");

		ComponentDetail component2 = new ComponentDetail();
		component2.setComponentMasterId(2);
		component2.setName("Connectivity");

		List<AttributeDetail> attributeDetails = new ArrayList<>();
		AttributeDetail attributeDetail = new AttributeDetail();
		attributeDetail.setName("Model No");
		attributeDetail.setValue("CISCO");
		/* attributeDetail.toString(); */
		attributeDetail.setName("Sub Product");
		attributeDetail.setAttributeMasterId(1);
		attributeDetail.setValue("Standard NPL");
		attributeDetails.add(attributeDetail);

		AttributeDetail attributeDetail1 = new AttributeDetail();
		attributeDetail1.setName("Sub Product");
		attributeDetail1.setAttributeMasterId(25);
		attributeDetail1.setValue("Standard NPL");
		attributeDetails.add(attributeDetail1);

		component1.setAttributes(attributeDetails);
		component2.setAttributes(attributeDetails);
		component1.setIsActive("Y");
		component2.setIsActive("Y");

		components.add(component2);
		components.add(component1);

		solution.setComponents(components);
		solution.setImage("");
		/* solution.toString(); */
		solution.setOfferingName("Vive");
		solution.toString();

		solution1.setComponents(components);
		solution1.setImage("");
		solution1.toString();
		solutionDetails.add(solution);
		solutionDetails.add(solution1);
		quoteDetail.setSolutions(solutionDetails);

		return quoteDetail;
	}
	


	public NplQuoteDetail getNplQuoteDetailForCreateNew3() {
		NplQuoteDetail quoteDetail = new NplQuoteDetail();
		quoteDetail.setCustomerId(25);
		quoteDetail.setProductName("NPL");
		quoteDetail.setSite(getNplSite3());
		quoteDetail.setQuoteleId(298);
		quoteDetail.setQuoteId(298);
		List<SolutionDetail> solutionDetails = new ArrayList<>();
		SolutionDetail solution = new SolutionDetail();
		solution.setOfferingName("Standard Point-to-Point Connectivity");
		solution.setBandwidth("20");
		solution.setBandwidthUnit("mbps");
		solution.toString();
		SolutionDetail solution1 = new SolutionDetail();
		solution1.setOfferingName("Standard Point-to-Point Connectivity");
		solution1.setBandwidth("20");
		solution1.setBandwidthUnit("mbps");
		solution1.toString();
		List<ComponentDetail> components = new ArrayList<>();

		ComponentDetail component = new ComponentDetail();
		/* component.toString(); */
		components.add(component);
		ComponentDetail component1 = new ComponentDetail();
		component1.setComponentMasterId(1);
		component1.setName("Private Lines");

		ComponentDetail component2 = new ComponentDetail();
		component2.setComponentMasterId(2);
		component2.setName("Connectivity");

		List<AttributeDetail> attributeDetails = new ArrayList<>();
		AttributeDetail attributeDetail = new AttributeDetail();
		attributeDetail.setName("Model No");
		attributeDetail.setValue("CISCO");
		/* attributeDetail.toString(); */
		attributeDetail.setName("Sub Product");
		attributeDetail.setAttributeMasterId(1);
		attributeDetail.setValue("Standard NPL");
		attributeDetails.add(attributeDetail);

		AttributeDetail attributeDetail1 = new AttributeDetail();
		attributeDetail1.setName("Sub Product");
		attributeDetail1.setAttributeMasterId(25);
		attributeDetail1.setValue("Standard NPL");
		attributeDetails.add(attributeDetail1);

		component1.setAttributes(attributeDetails);
		component2.setAttributes(attributeDetails);
		component1.setIsActive("Y");
		component2.setIsActive("Y");

		components.add(component2);
		components.add(component1);

		solution.setComponents(components);
		solution.setImage("");
		/* solution.toString(); */
		solution.setOfferingName("Vive");
		solution.toString();

		solution1.setComponents(components);
		solution1.setImage("");
		solution1.toString();
		solutionDetails.add(solution);
		solutionDetails.add(solution1);
		quoteDetail.setSolutions(solutionDetails);

		return quoteDetail;
	}

	public NplQuoteDetail getNplQuoteDetailForCreateNew2() {
		NplQuoteDetail quoteDetail = new NplQuoteDetail();
		quoteDetail.setCustomerId(25);
		quoteDetail.setProductName("NPL");
		quoteDetail.setSite(getNplSite2());
		quoteDetail.setQuoteleId(298);
		quoteDetail.setQuoteId(298);
		List<SolutionDetail> solutionDetails = new ArrayList<>();
		SolutionDetail solution = new SolutionDetail();
		solution.setOfferingName("Buy India Point to Point Connectivity");
		solution.setBandwidth("20");
		solution.setBandwidthUnit("mbps");
		solution.toString();
		SolutionDetail solution1 = new SolutionDetail();
		solution1.setOfferingName("Buy India Point to Point Connectivity2");
		solution1.setBandwidth("20");
		solution1.setBandwidthUnit("mbps");
		solution1.toString();
		List<ComponentDetail> components = new ArrayList<>();

		ComponentDetail component = new ComponentDetail();
		/* component.toString(); */
		components.add(component);
		ComponentDetail component1 = new ComponentDetail();
		component1.setComponentMasterId(1);
		component1.setName("Private Lines");

		ComponentDetail component2 = new ComponentDetail();
		component2.setComponentMasterId(2);
		component2.setName("Connectivity");

		List<AttributeDetail> attributeDetails = new ArrayList<>();
		AttributeDetail attributeDetail = new AttributeDetail();
		attributeDetail.setName("Model No");
		attributeDetail.setValue("CISCO");
		/* attributeDetail.toString(); */
		attributeDetail.setName("Sub Product");
		attributeDetail.setAttributeMasterId(1);
		attributeDetail.setValue("Standard NPL");
		attributeDetails.add(attributeDetail);

		AttributeDetail attributeDetail1 = new AttributeDetail();
		attributeDetail1.setName("Sub Product");
		attributeDetail1.setAttributeMasterId(25);
		attributeDetail1.setValue("Standard NPL");
		attributeDetails.add(attributeDetail1);

		component1.setAttributes(attributeDetails);
		component2.setAttributes(attributeDetails);
		component1.setIsActive("Y");
		component2.setIsActive("Y");

		components.add(component2);
		components.add(component1);

		solution.setComponents(components);
		solution.setImage("");
		/* solution.toString(); */
		solution.setOfferingName("Vive");
		solution.toString();

		solution1.setComponents(components);
		solution1.setImage("");
		solution1.toString();
		solutionDetails.add(solution);
		solutionDetails.add(solution1);
		quoteDetail.setSolutions(solutionDetails);

		return quoteDetail;
	}


	/**
	 * getSite-mock values
	 * 
	 * @return List<Site>
	 */
	private List<NplSite> getNplSite1() {
		List<NplSite> siList = new ArrayList<>();
		/*
		 * SiteDetail siteDetail = new SiteDetail(); siteDetail.setSiteId(1);
		 * siteDetail.setLocationId(1); List<ComponentDetail> components = new
		 * ArrayList<>(); ComponentDetail component = new ComponentDetail();
		 * component.toString(); components.add(component); List<AttributeDetail>
		 * attributeDetails = new ArrayList<>(); AttributeDetail attributeDetail = new
		 * AttributeDetail(); attributeDetail.setName("Model No");
		 * attributeDetail.setValue("CISCO"); attributeDetail.toString();
		 * attributeDetails.add(attributeDetail);
		 * component.setAttributes(attributeDetails); component.setIsActive("Y");
		 * component.setName("CPE"); siteDetail.setComponents(components);
		 */
		NplSite site = new NplSite();
		site.setBandwidth("f");
		site.setOfferingName("Standard point-to-point conectivity");
		site.setBandwidthUnit("daf");
		site.setImage("adgdag");
		/*
		 * site.toString();
		 */ site.setSite(getSitDetails());
		
		siList.add(site);
	

		return siList;
	}
	
	/**
	 * getSite-mock values
	 * 
	 * @return List<Site>
	 */
	private List<NplSite> getNplSite2() {
		List<NplSite> siList = new ArrayList<>();
		/*
		 * SiteDetail siteDetail = new SiteDetail(); siteDetail.setSiteId(1);
		 * siteDetail.setLocationId(1); List<ComponentDetail> components = new
		 * ArrayList<>(); ComponentDetail component = new ComponentDetail();
		 * component.toString(); components.add(component); List<AttributeDetail>
		 * attributeDetails = new ArrayList<>(); AttributeDetail attributeDetail = new
		 * AttributeDetail(); attributeDetail.setName("Model No");
		 * attributeDetail.setValue("CISCO"); attributeDetail.toString();
		 * attributeDetails.add(attributeDetail);
		 * component.setAttributes(attributeDetails); component.setIsActive("Y");
		 * component.setName("CPE"); siteDetail.setComponents(components);
		 */
		NplSite site = new NplSite();
		site.setBandwidth("f");
		site.setOfferingName("Buy India Point to Point Connectivity-2");
		site.setBandwidthUnit("daf");
		site.setImage("adgdag");
		/*
		 * site.toString();
		 */ site.setSite(getSitDetails());
		
		siList.add(site);
		siList.add(new NplSite());
	

		return siList;
	}

	private List<NplSite> getNplSite3() {
		List<NplSite> siList = new ArrayList<>();
		/*
		 * SiteDetail siteDetail = new SiteDetail(); siteDetail.setSiteId(1);
		 * siteDetail.setLocationId(1); List<ComponentDetail> components = new
		 * ArrayList<>(); ComponentDetail component = new ComponentDetail();
		 * component.toString(); components.add(component); List<AttributeDetail>
		 * attributeDetails = new ArrayList<>(); AttributeDetail attributeDetail = new
		 * AttributeDetail(); attributeDetail.setName("Model No");
		 * attributeDetail.setValue("CISCO"); attributeDetail.toString();
		 * attributeDetails.add(attributeDetail);
		 * component.setAttributes(attributeDetails); component.setIsActive("Y");
		 * component.setName("CPE"); siteDetail.setComponents(components);
		 */
		NplSite site = new NplSite();
		site.setBandwidth("f");
		site.setOfferingName("Standard point-to-point conectivity");
		site.setBandwidthUnit("daf");
		site.setImage("adgdag");
		site.setType(SiteTypeConstants.SITE);
		/*
		 * site.toString();
		 */ site.setSite(getSitDetails());
		site.getSite().get(0).setSiteId(null);
		siList.add(site);
		
		NplSite site1 = new NplSite();
		site1.setBandwidth("f");
		site1.setOfferingName("Standard point-to-point conectivity");
		site1.setBandwidthUnit("daf");
		site1.setImage("adgdag");
		site1.setType(SiteTypeConstants.SITE);
		/*
		 * site.toString();
		 */ site1.setSite(getSitDetails());
			site1.getSite().get(0).setSiteId(null);
		
		siList.add(site1);
	

		return siList;
	}
	
	public NplQuoteDetail getNplQuoteDetail3() {
		NplQuoteDetail quoteDetail = new NplQuoteDetail();
		/* quoteDetail.toString(); */
		quoteDetail.setCustomerId(1);
		quoteDetail.setProductName("IAS");
		quoteDetail.setSite(getSite2());
		quoteDetail.setQuoteleId(1);
		quoteDetail.setQuoteId(1);
		quoteDetail.setQuoteId(2);
		List<SolutionDetail> solutionDetails = new ArrayList<>();
		SolutionDetail solution = new SolutionDetail();
		solution.setBandwidth("20");
		solution.setBandwidthUnit("mbps");
		/* solution.toString(); */
		List<ComponentDetail> components = new ArrayList<>();
		ComponentDetail component = new ComponentDetail();
		/*
		 * component.toString();
		 */ components.add(component);
		List<AttributeDetail> attributeDetails = new ArrayList<>();
		AttributeDetail attributeDetail = new AttributeDetail();
		attributeDetail.setName("Model No");
		attributeDetail.setValue("CISCO");
		/*
		 * attributeDetail.toString();
		 */ attributeDetails.add(attributeDetail);
		component.setAttributes(attributeDetails);
		component.setIsActive("Y");
		component.setName("CPE");
		solution.setComponents(components);
		solution.setImage("");
		/* solution.toString(); */
		solution.setOfferingName("Vive");
		solutionDetails.add(solution);
		quoteDetail.setSolutions(solutionDetails);

		return quoteDetail;
	}

	public List<NplSite> getSite2() {
		List<NplSite> siList = new ArrayList<>();
		NplSite site = new NplSite();
		site.setBandwidth("f");
		site.setOfferingName("NPL1");
		site.setBandwidthUnit("daf");
		site.setImage("adgdag");
		site.setType(SiteTypeConstants.SITE);
		/* site.toString(); */
		site.setSite(getSitDetails());
		siList.add(site);
		NplSite site2 = new NplSite();
		site2.setBandwidth("f");
		site2.setOfferingName("NPL2");
		site2.setBandwidthUnit("daf");
		site2.setImage("adgdag");
		site2.setType(SiteTypeConstants.SITE);
		/* site.toString(); */
		site2.setSite(getSitDetails());
		siList.add(site2);

		return siList;
	}

	public QuoteNplLink getQuoteLinkNpl() {
		QuoteNplLink quoteDetail = new QuoteNplLink();
		Set<QuoteNplLinkSla> quoteNplLinkSlas =new HashSet<>();
		quoteDetail.setId(1);
		quoteDetail.setLinkCode("AS3GMIGZZFXRLMGP");
		quoteDetail.setProductSolutionId(419);
		quoteDetail.setSiteAId(905);
		quoteDetail.setSiteBId(906);
		quoteDetail.setQuoteId(298);
		quoteDetail.setStatus((byte) 1);
		quoteNplLinkSlas.add(getQuoteNplLinkSla());
		quoteDetail.setQuoteNplLinkSlas(quoteNplLinkSlas);
		return quoteDetail;

	}
	
	/**
	 *
	 * getIllsites-mock values
	 *
	 * @return {@link IllSite}
	 */
	public QuoteIllSite getIllsites4() {
		QuoteIllSite site = new QuoteIllSite();
		site.setId(1);
		site.setIsTaxExempted((byte) 0);
		site.setFeasibility((byte) 0);
		site.setMrc(9.8);
		site.setNrc(19.8);
		site.setStatus((byte) 1);
		site.setErfLrSolutionId("YU");
		site.setFeasibility((byte) 1);
		site.setIsTaxExempted((byte) 1);
		site.setErfLocSiteaLocationId(1);
		site.setErfLocSitebLocationId(1);
		
		site.setProductSolution(getSolution1());

		site.setImageUrl("https://www.google.co.in/search?q=google+image&tbm=isch&source=iu");
		site.setStatus((byte) 1);
		site.setQuoteIllSiteSlas(createQuoteIllSiteSlaSetWithSlaMaster());
		return site;
	}
	
	/**
	 * getUpdateRequest
	 * 
	 * @return
	 */
	public UpdateRequest getUpdateRequest1() {
		UpdateRequest request = new UpdateRequest();
		request.setQuoteId(1);
		request.setQuoteToLe(1);
		request.setOrderToLeId(1);
		request.setAttributeName("testAttribute");
		request.setSiteId(1);
		request.setLocalITContactId(1);
		request.setQuoteId(1);
		request.setQuoteToLe(1);
		request.setFamilyName("test");
		request.setFamilyName("Test");
		request.getComponentDetails().addAll(getcompDetails1());
		request.setAttributeDetails(getAttributes());
		return request;
	}
	
	public Set<QuoteIllSiteSla> createQuoteIllSiteSlaSetWithSlaMaster() {
		Set<QuoteIllSiteSla> setAttri = new HashSet<>();
		setAttri.add(createQuoteIllSiteSla());
		return setAttri;
	}
	
	private List<ComponentDetail> getcompDetails1() {
		List<ComponentDetail> componentDetails = new ArrayList<>();
		ComponentDetail componentDetail = new ComponentDetail();
		componentDetail.setComponentId(1);
		componentDetail.setAttributes(getAttributes1());
		componentDetail.setIsActive("Y");
		componentDetail.setName("CPE");
		componentDetails.add(componentDetail);
		return componentDetails;
	}
	
	private List<AttributeDetail> getAttributes1() {
		List<AttributeDetail> arAttributeDetails = new ArrayList<>();
		AttributeDetail attributeDetail = new AttributeDetail();
		attributeDetail.setAttributeId(1);
		attributeDetail.setName("Up time");
		attributeDetail.setValue("Premium");
		arAttributeDetails.add(attributeDetail);
		return arAttributeDetails;
	}

	public List<QuoteNplLink> getNplLinks()
	{
		List<QuoteNplLink> nplLinks=new ArrayList<>();
		QuoteNplLink link=new QuoteNplLink();
		link.setArc(100D);
		link.setChargeableDistance("50");
		link.setId(10);
		link.setLinkCode("SDFGFG");
		link.setLinkType("link");
		link.setMrc(240D);
		link.setNrc(300D);
		link.setProductSolutionId(100);
		link.setQuoteId(152);
		link.setSiteAId(142);
		link.setSiteBId(115);
		link.setSiteAType("site");
		link.setSiteBType("site");
		link.setStatus((byte)1);
		link.setWorkflowStatus("status");
		nplLinks.add(link);
		return nplLinks;
	}
	
	public Optional<OrderToLe> getOptionalOrderToLe() {
		OrderToLe orderToLe = new OrderToLe();
		orderToLe.setId(1);
		orderToLe.setCurrencyId(1);
		orderToLe.setFinalMrc(45D);
		orderToLe.setFinalNrc(67D);
		orderToLe.setProposedMrc(78D);

		orderToLe.setProposedNrc(98D);
		
		Optional<OrderToLe> orderLe = Optional.of(orderToLe);

		return orderLe;
	}
	public List<QuoteNplLinkSla> getQuoteNplLinkSlaList() {
		List<QuoteNplLinkSla> quoteNplLinkSlaList=new ArrayList<>();
		quoteNplLinkSlaList.add(getQuoteNplLinkSla());
		return quoteNplLinkSlaList;
	}
	public QuoteNplLinkSla getQuoteNplLinkSla() {
		QuoteNplLinkSla quoteNplLinkSla =new QuoteNplLinkSla();
		quoteNplLinkSla.setQuoteNplLink(getQuoteNplLink());
		quoteNplLinkSla.setSlaMaster(createSlaMaster());
		quoteNplLinkSla.setSlaValue("99.5");
		return quoteNplLinkSla;
	}
	public OrderLinkRequest getOrderLinkRequest() {
		OrderLinkRequest orderLinkRequest = new OrderLinkRequest();
		orderLinkRequest.setMstOrderLinkStageName("LM Delivery Initiated");
		orderLinkRequest.setMstOrderLinkStatusName(OrderConstants.START_OF_SERVICE.toString());
		return orderLinkRequest;
	}
	
	public OrderLinkRequest getOrderLinkRequestWithEmptyStatus() {
		OrderLinkRequest orderLinkRequest = new OrderLinkRequest();
		orderLinkRequest.setMstOrderLinkStageName("LM Delivery Initiated");
		orderLinkRequest.setMstOrderLinkStatusName("");
		return orderLinkRequest;
	}
	
	public NplLinkBean createNplLinkBean() {
		NplLinkBean link = new NplLinkBean();
		link.setId(1);
		link.setSiteA(100);
		link.setSiteB(101);
		link.setSiteAType("DC");
		link.setSiteBType("DC");
		link.setLinkCode("SDFSVEEW");
		link.setFeasibility((byte)1);
		link.setComponents(getQuoteProductComponentBeanList());
		link.setSites(getQuoteNplSiteBeanList());
		link.setChargeableDistance("501");
		link.setArc(Double.parseDouble("4324"));
		link.setMrc(Double.parseDouble("3324"));
		link.setNrc(Double.parseDouble("3424"));
		link.setLinkType("Link");
		link.setLinkFeasibility(getLinkFeasibilityBeanList());
		
		return link; 
	}
	
	
	public OrderProductSolution createOrderProductSolution() {
		return createOrderProductSolutionList().get(0);
		
	}
	
	public List<OrderProductSolution> createOrderProductSolutionList() {
		List<OrderProductSolution> solns = new ArrayList<>();
		for (OrderToLe ole : getOrders().getOrderToLes()) {
			for ( OrderToLeProductFamily opf : ole.getOrderToLeProductFamilies()) {
				for (OrderProductSolution soln : opf.getOrderProductSolutions()) {
					solns.add(soln);
				}
			}
		}
			
		return solns;
	}
	public NplQuoteBean getNplQuoteBean() {
	NplQuoteBean quoteDto = new NplQuoteBean(getQuote());
	for(QuoteToLeBean quoteLeBean:quoteDto.getLegalEntities()) {
		quoteLeBean.setLegalAttributes(getLegalAttributeBeanList());
		quoteLeBean.setProductFamilies(getQuoteToLeProductFamilyBean());
		quoteLeBean.setFinalArc(Double.parseDouble("450356"));
		quoteLeBean.setFinalMrc(Double.parseDouble("450356"));
		quoteLeBean.setFinalNrc(Double.parseDouble("450356"));
	}
	
	return quoteDto;

	}
	
	public OrderProductSolution createSolutionWithOrderFamilyAndOrder() {
		Order order = new Order();
		order.setCreatedBy(1);
		OrderToLe ole = new OrderToLe();
		OrderToLeProductFamily fam = new OrderToLeProductFamily();
		OrderProductSolution sol = new OrderProductSolution();
	
		ole.setOrder(order);
		fam.setOrderToLe(ole);
		sol.setOrderToLeProductFamily(fam);
		return sol;
	}
	public LinkFeasibility getLinkFeasibility() {
	LinkFeasibility linkFeasibility =new LinkFeasibility();
	linkFeasibility.setFeasibilityCheck("Manual");
	linkFeasibility.setFeasibilityCode("CNBP45RF");
	linkFeasibility.setFeasibilityMode("OnnetWL NPL");
	linkFeasibility.setId(1);
	linkFeasibility.setIsSelected((byte) 1);
	linkFeasibility.setProvider("TCL");
	linkFeasibility.setQuoteNplLink(getQuoteNplLink());
	linkFeasibility.setRank(1);
	linkFeasibility.setResponseJson("{\"Feasible\":[{\"a_X2km_prospect_min_bw\":\"0.064\",\"b_POP_DIST_KM_SERVICE_MOD\":\"5\",\"intra_inter_flag\":\"Intracity\",\"a_X2km_min_bw\":\"0.001\",\"b_lm_arc_bw_prov_ofrf\":\"0\",\"b_num_connected_cust\":\"0\",\"b_X2km_avg_dist\":\"1441.0390018\",\"b_lm_nrc_bw_onwl\":\"0\",\"b_X2km_max_bw\":\"  10240\",\"a_X2km_max_bw\":\"10240\",\"a_X2km_prospect_max_bw\":\"10240\",\"b_X2km_min_bw\":\"0.001\",\"a_longitude_final\":\"77.2150254\",\"b_access_check_CC\":\"No CC Found\",\"a_num_connected_building\":\"0\",\"b_Product.Name\":\"NPL\",\"a_city_tier\":\"Non_Tier1\",\"b_X5km_prospect_count\":\"3412\",\"a_sales_org\":\"Enterprise\",\"b_scenario_1\":\"0\",\"a_FATG_Ring_type\":\"SDH\",\"a_pop_lat\":\"28.63095826\",\"a_resp_city\":\"Central Delhi\",\"b_X2km_prospect_min_bw\":\"0.064\",\"b_FATG_Ring_type\":\"SDH\",\"a_X5km_prospect_min_bw\":\"0.064\",\"a_POP_Construction_Status\":\"In Service\",\"b_city_tier\":\"Non_Tier1\",\"b_time_taken\":\"11.589\",\"b_lm_nrc_mux_onwl\":\"58810\",\"b_sales_org\":\"Enterprise\",\"b_FATG_Category\":\"Metro Service Ready\",\"b_scenario_2\":\"0\",\"b_Network_F_NF_HH\":\"Network Feasible on HH\",\"Selected\":false,\"a_hh_name\":\"SC-DEL-BCP-0031-240340403\",\"b_lm_nrc_bw_prov_ofrf\":\"0\",\"b_latitude_final\":\"28.616262\",\"b_connected_cust_tag\":\"0\",\"a_POP_Building_Type\":\"Government\",\"a_X0.5km_prospect_avg_bw\":\"68.79545455\",\"a_lm_arc_bw_onwl\":\"108910\",\"b_X2km_prospect_max_bw\":\"10240\",\"b_X5km_prospect_min_bw\":\"0.064\",\"b_X0.5km_prospect_count\":\"  38\",\"b_OnnetCity_tag\":\"1\",\"a_pop_network_loc_id\":\"TINDDLNDELCNPL0030\",\"a_X0.5km_avg_bw\":\"      0.001\",\"a_X2km_prospect_avg_dist\":\"1447.093673\",\"a_POP_Category\":\"Metro Service Ready\",\"b_POP_DIST_KM_SERVICE\":\"2.16362202992\",\"a_X5km_prospect_avg_dist\":\"2570.671488\",\"b_pop_long\":\"77.20774270\",\"a_Predicted_Access_Feasibility\":\"Feasible with Capex\",\"a_POP_Network_Location_Type\":\"Mega POP\",\"a_scenario_1\":\"0\",\"a_scenario_2\":\"0\",\"b_POP_Building_Type\":\"Government\",\"b_X2km_prospect_count\":\"1641\",\"a_FATG_DIST_KM\":\" 367.2465429\",\"b_net_pre_feasible_flag\":\"0\",\"a_X5km_min_bw\":\"0.001\",\"a_X0.5km_prospect_max_bw\":\"500\",\"a_X2km_prospect_perc_feasible\":\"0.9946396665\",\"b_X5km_prospect_max_bw\":\"10240\",\"b_pop_name\":\"TCL  VSB, DELHI-1-11641110\",\"b_X0.5km_avg_bw\":\"  26.5000000\",\"b_X5km_max_bw\":\"  10240\",\"a_Network_F_NF_CC_Flag\":\"0\",\"b_Orch_BW\":\"34\",\"a_X0.5km_prospect_perc_feasible\":\"1\",\"a_site_id\":\"33\",\"b_Predicted_Access_Feasibility\":\"Feasible with Capex\",\"burstable_bw\":\"34\",\"a_Product.Name\":\"NPL\",\"b_X5km_prospect_num_feasible\":\"3400\",\"bw_mbps\":\"34\",\"b_X5km_prospect_avg_bw\":\" 76.02218757\",\"a_X2km_prospect_count\":\"1679\",\"b_X2km_prospect_min_dist\":\"352.601910124\",\"b_FATG_TCL_Access\":\"No\",\"b_last_mile_contract_term\":\"2 Year\",\"b_customer_segment\":\"Enterprise-Direct\",\"a_num_connected_cust\":\"0\",\"b_longitude_final\":\"77.213528\",\"a_POP_DIST_KM\":\"0.3672465429\",\"b_pop_address\":\"TCL,_Videsh_Sanchar_Bhavan,_Banglasaheb_Road,_New_Delhi_-_110_001\",\"b_hh_name\":\"HH-DEL-BCP-0032-240340453\",\"a_lm_nrc_nerental_onwl\":\"0\",\"b_lm_nrc_inbldg_onwl\":\"40000\",\"account_id_with_18_digit\":\"0012000001OiMX6\",\"a_lm_nrc_bw_onwl\":\"0\",\"a_X2km_cust_count\":\"300\",\"b_Network_Feasibility_Check\":\"Feasible\",\"b_FATG_DIST_KM\":\"333.96474119\",\"a_X5km_avg_bw\":\"164.6554943\",\"b_lm_nrc_ospcapex_onwl\":\"0\",\"b_core_check_hh\":\"Network Feasible on Core Ring of HH\",\"a_Network_F_NF_HH\":\"NA\",\"a_error_code\":\"NA\",\"b_X2km_min_dist\":\"481.84967993\",\"b_a_or_b_end\":\"B\",\"a_Network_Feasibility_Check\":\"Not Feasible\",\"b_X2km_prospect_num_feasible\":\"1632\",\"bw_mbps_upd\":\"34\",\"a_access_check_hh\":\"NA\",\"b_cost_permeter\":\"  0\",\"b_X0.5km_min_bw\":\"2.000\",\"a_X0.5km_prospect_count\":\"44\",\"a_X0.5km_avg_dist\":\"    471.9180868\",\"a_FATG_PROW\":\"No\",\"b_X2km_prospect_perc_feasible\":\"0.9945155393\",\"a_X2km_min_dist\":\" 471.9180868\",\"a_lm_nrc_mast_ofrf\":\"0\",\"a_X2km_prospect_num_feasible\":\"1670\",\"a_SERVICE_ID\":\"NA\",\"b_connected_building_tag\":\"0\",\"b_pop_lat\":\"28.63095826\",\"b_X5km_avg_dist\":\"2122.7313066\",\"a_lm_nrc_mast_onrf\":\"0\",\"a_HH_DIST_KM\":\"242.0701220\",\"a_pop_selected\":\"no\",\"a_min_hh_fatg\":\" 303\",\"a_X5km_avg_dist\":\"2116.888805\",\"b_X5km_prospect_min_dist\":\"352.601910124\",\"b_X2km_prospect_avg_bw\":\"105.8379427\",\"a_latitude_final\":\"28.6163909\",\"b_Network_F_NF_CC\":\"No CC Found\",\"b_POP_Category\":\"Metro Service Ready\",\"a_cost_permeter\":\"  0\",\"b_X0.5km_min_dist\":\"481.84967993\",\"b_lm_arc_bw_onwl\":\"108910\",\"b_Probabililty_Access_Feasibility\":\"0.93\",\"b_X0.5km_avg_dist\":\"490.60797661\",\"a_lm_arc_bw_onrf\":\"0\",\"link_id\":\"530\",\"a_lm_nrc_bw_prov_ofrf\":\"0\",\"a_POP_TCL_Access\":\"Yes\",\"b_POP_Network_Location_Type\":\"Mega POP\",\"a_FATG_TCL_Access\":\"Yes\",\"b_error_flag\":\"0\",\"a_lm_nrc_mux_onwl\":\"58810\",\"sla_varient\":\"Standard\",\"a_lm_nrc_bw_onrf\":\"0\",\"a_pop_ui_id\":\"none\",\"b_total_cost\":\"207720\",\"b_X0.5km_prospect_avg_dist\":\"457.07767145\",\"a_core_check_hh\":\"NA\",\"b_Network_F_NF_HH_Flag\":\"1\",\"a_local_loop_interface\":\"GE\",\"a_Probabililty_Access_Feasibility\":\"0.92\",\"manual_flag\":\"1\",\"a_lm_arc_bw_prov_ofrf\":\"0\",\"b_X0.5km_prospect_min_bw\":\"2.000\",\"b_X5km_cust_count\":\" 438\",\"b_X0.5km_prospect_num_feasible\":\"  38\",\"b_FATG_PROW\":\"No\",\"opportunity_term\":\"24\",\"b_num_connected_building\":\"   0\",\"a_X0.5km_min_bw\":\"      0.001\",\"a_X5km_prospect_count\":\"3420\",\"a_lm_nrc_inbldg_onwl\":\"40000\",\"a_X2km_avg_bw\":\"196.7626033\",\"b_lm_arc_bw_onrf\":\"0\",\"b_X2km_avg_bw\":\" 199.3269628\",\"b_hh_flag\":\"1\",\"b_pop_ui_id\":\"none\",\"a_Network_F_NF_HH_Flag\":\"0\",\"a_error_flag\":\"0\",\"a_X0.5km_min_dist\":\"    471.9180868\",\"a_Orch_Connection\":\"Wireline\",\"b_X5km_min_dist\":\"481.84967993\",\"b_X2km_prospect_avg_dist\":\"1471.5637826\",\"b_error_msg\":\"No error\",\"a_last_mile_contract_term\":\"2 Year\",\"a_OnnetCity_tag\":\"1\",\"a_POP_DIST_KM_SERVICE\":\" 2.213629916\",\"dist_betw_pops\":\"   1\",\"a_connected_cust_tag\":\"0\",\"b_Orch_LM_Type\":\"Onnet\",\"a_X5km_prospect_num_feasible\":\"3409\",\"b_pop_network_loc_id\":\"TINDDLNDELCNPL0030\",\"Type\":\"OnnetWL_NPL\",\"b_pop_selected\":\"no\",\"a_access_check_CC\":\"No CC Found\",\"a_X0.5km_prospect_min_dist\":\"326.21594202\",\"a_FATG_Category\":\"Wimax Site\",\"a_X0.5km_cust_count\":\"1\",\"b_X0.5km_prospect_min_dist\":\"352.601910124\",\"b_HH_0_5km\":\"Red cross-13045391\",\"a_X5km_cust_count\":\"437\",\"a_X5km_prospect_avg_bw\":\" 76.1186269\",\"a_hh_flag\":\"1\",\"a_X2km_prospect_min_dist\":\"326.21594202\",\"b_X5km_min_bw\":\"0.001\",\"b_X0.5km_prospect_avg_bw\":\" 17.15789474\",\"b_HH_DIST_KM\":\"203.29668004\",\"b_X5km_prospect_perc_feasible\":\"0.9964830012\",\"b_POP_Construction_Status\":\"In Service\",\"a_net_pre_feasible_flag\":\"0\",\"prospect_name\":\"Dev Test Services & Sons\",\"Predicted_Access_Feasibility\":\"Not Feasible\",\"a_time_taken\":\"11.589\",\"a_total_cost\":\" 207720\",\"b_error_code\":\"NA\",\"a_X5km_min_dist\":\" 471.9180868\",\"b_X5km_prospect_avg_dist\":\"2587.024005\",\"a_pop_address\":\"TCL,_Videsh_Sanchar_Bhavan,_Banglasaheb_Road,_New_Delhi_-_110_001\",\"a_X0.5km_prospect_avg_dist\":\"401.5080933\",\"b_POP_TCL_Access\":\"Yes\",\"b_core_check_CC\":\"No CC Found\",\"a_X5km_prospect_max_bw\":\"10240\",\"b_POP_DIST_KM\":\"0.50830909040\",\"product_name\":\"NPL\",\"a_Network_F_NF_CC\":\"No CC Found\",\"a_X5km_max_bw\":\"10240\",\"b_lm_nrc_bw_onrf\":\"0\",\"a_lm_nrc_ospcapex_onwl\":\"     0\",\"a_X2km_avg_dist\":\"1454.066273\",\"b_Network_F_NF_CC_Flag\":\"0\",\"b_lm_nrc_mast_ofrf\":\"0\",\"b_access_check_hh\":\"Network Feasible on Access Ring of HH\",\"b_FATG_Network_Location_Type\":\"Access/Customer POP\",\"b_X2km_cust_count\":\" 296\",\"b_lm_nrc_mast_onrf\":\"0\",\"a_X2km_prospect_avg_bw\":\"104.06197975\",\"a_X5km_prospect_perc_feasible\":\"0.9967836257\",\"b_X5km_avg_bw\":\" 164.2864178\",\"a_connected_building_tag\":\"0\",\"a_pop_name\":\"TCL  VSB, DELHI-1-11641110\",\"a_core_check_CC\":\"No CC Found\",\"feasibility_response_created_date\":\"2018-09-27\",\"b_resp_city\":\"Central Delhi\",\"a_FATG_Network_Location_Type\":\"Mega POP\",\"a_pop_long\":\"77.20774270\",\"a_X0.5km_prospect_min_bw\":\"2\",\"a_POP_DIST_KM_SERVICE_MOD\":\" 5\",\"a_X5km_prospect_min_dist\":\"326.21594202\",\"a_Orch_BW\":\"34\",\"b_Orch_Category\":\"Capex greater than 175m\",\"b_X0.5km_max_bw\":\"     56\",\"b_site_id\":\"34\",\"a_HH_0_5km\":\"SC-DEL-BCP-0031-240340403\",\"a_X0.5km_max_bw\":\"      0.001\",\"a_X0.5km_prospect_num_feasible\":\"44\",\"a_a_or_b_end\":\"A\",\"b_X0.5km_cust_count\":\"   4\",\"a_Orch_LM_Type\":\"Onnet\",\"b_lm_nrc_nerental_onwl\":\"0\",\"b_local_loop_interface\":\"GE\",\"a_FATG_Building_Type\":\"Commercial\",\"a_Orch_Category\":\"Capex greater than 175m\",\"chargeable_distance\":\"  5\",\"b_X0.5km_prospect_perc_feasible\":\"1.0000000000\",\"b_FATG_Building_Type\":\"Commercial\",\"b_Orch_Connection\":\"Wireline\",\"b_min_hh_fatg\":\"254\",\"b_X0.5km_prospect_max_bw\":\"  100\",\"quotetype_quote\":\"New Order\",\"a_error_msg\":\"No error\",\"b_SERVICE_ID\":\"NA\",\"a_customer_segment\":\"Enterprise-Direct\"}],\"NotFeasible\":[]}");
	linkFeasibility.setType("Intracity");
	return linkFeasibility;
	}
	public List<LinkFeasibility> getLinkFeasibilityList() {
		List<LinkFeasibility> linkFeasibilityList =new ArrayList<>();
		linkFeasibilityList.add(getLinkFeasibility());
		return linkFeasibilityList;
		
	}
	
	public LinkFeasibility getLinkFeasibility1() {
		LinkFeasibility linkFeasibility =new LinkFeasibility();
		linkFeasibility.setFeasibilityCheck("Manual");
		linkFeasibility.setFeasibilityCode("CNBP45RF");
		linkFeasibility.setFeasibilityMode("OnnetWL NPL");
		linkFeasibility.setId(1);
		linkFeasibility.setIsSelected((byte) 1);
		linkFeasibility.setProvider("TCL");
		linkFeasibility.setQuoteNplLink(getQuoteNplLink());
		linkFeasibility.setResponseJson("{\"NotFeasible\":[{\"a_X2km_prospect_min_bw\":\"0.064\",\"b_POP_DIST_KM_SERVICE_MOD\":\"5\",\"intra_inter_flag\":\"Intracity\",\"a_X2km_min_bw\":\"0.001\",\"b_lm_arc_bw_prov_ofrf\":\"0\",\"b_num_connected_cust\":\"0\",\"b_X2km_avg_dist\":\"1441.0390018\",\"b_lm_nrc_bw_onwl\":\"0\",\"b_X2km_max_bw\":\"  10240\",\"a_X2km_max_bw\":\"10240\",\"a_X2km_prospect_max_bw\":\"10240\",\"b_X2km_min_bw\":\"0.001\",\"a_longitude_final\":\"77.2150254\",\"b_access_check_CC\":\"No CC Found\",\"a_num_connected_building\":\"0\",\"b_Product.Name\":\"NPL\",\"a_city_tier\":\"Non_Tier1\",\"b_X5km_prospect_count\":\"3412\",\"a_sales_org\":\"Enterprise\",\"b_scenario_1\":\"0\",\"a_FATG_Ring_type\":\"SDH\",\"a_pop_lat\":\"28.63095826\",\"a_resp_city\":\"Central Delhi\",\"b_X2km_prospect_min_bw\":\"0.064\",\"b_FATG_Ring_type\":\"SDH\",\"a_X5km_prospect_min_bw\":\"0.064\",\"a_POP_Construction_Status\":\"In Service\",\"b_city_tier\":\"Non_Tier1\",\"b_time_taken\":\"11.589\",\"b_lm_nrc_mux_onwl\":\"58810\",\"b_sales_org\":\"Enterprise\",\"b_FATG_Category\":\"Metro Service Ready\",\"b_scenario_2\":\"0\",\"b_Network_F_NF_HH\":\"Network Feasible on HH\",\"Selected\":false,\"a_hh_name\":\"SC-DEL-BCP-0031-240340403\",\"b_lm_nrc_bw_prov_ofrf\":\"0\",\"b_latitude_final\":\"28.616262\",\"b_connected_cust_tag\":\"0\",\"a_POP_Building_Type\":\"Government\",\"a_X0.5km_prospect_avg_bw\":\"68.79545455\",\"a_lm_arc_bw_onwl\":\"108910\",\"b_X2km_prospect_max_bw\":\"10240\",\"b_X5km_prospect_min_bw\":\"0.064\",\"b_X0.5km_prospect_count\":\"  38\",\"b_OnnetCity_tag\":\"1\",\"a_pop_network_loc_id\":\"TINDDLNDELCNPL0030\",\"a_X0.5km_avg_bw\":\"      0.001\",\"a_X2km_prospect_avg_dist\":\"1447.093673\",\"a_POP_Category\":\"Metro Service Ready\",\"b_POP_DIST_KM_SERVICE\":\"2.16362202992\",\"a_X5km_prospect_avg_dist\":\"2570.671488\",\"b_pop_long\":\"77.20774270\",\"a_Predicted_Access_Feasibility\":\"Feasible with Capex\",\"a_POP_Network_Location_Type\":\"Mega POP\",\"a_scenario_1\":\"0\",\"a_scenario_2\":\"0\",\"b_POP_Building_Type\":\"Government\",\"b_X2km_prospect_count\":\"1641\",\"a_FATG_DIST_KM\":\" 367.2465429\",\"b_net_pre_feasible_flag\":\"0\",\"a_X5km_min_bw\":\"0.001\",\"a_X0.5km_prospect_max_bw\":\"500\",\"a_X2km_prospect_perc_feasible\":\"0.9946396665\",\"b_X5km_prospect_max_bw\":\"10240\",\"b_pop_name\":\"TCL  VSB, DELHI-1-11641110\",\"b_X0.5km_avg_bw\":\"  26.5000000\",\"b_X5km_max_bw\":\"  10240\",\"a_Network_F_NF_CC_Flag\":\"0\",\"b_Orch_BW\":\"34\",\"a_X0.5km_prospect_perc_feasible\":\"1\",\"a_site_id\":\"33\",\"b_Predicted_Access_Feasibility\":\"Feasible with Capex\",\"burstable_bw\":\"34\",\"a_Product.Name\":\"NPL\",\"b_X5km_prospect_num_feasible\":\"3400\",\"bw_mbps\":\"34\",\"b_X5km_prospect_avg_bw\":\" 76.02218757\",\"a_X2km_prospect_count\":\"1679\",\"b_X2km_prospect_min_dist\":\"352.601910124\",\"b_FATG_TCL_Access\":\"No\",\"b_last_mile_contract_term\":\"2 Year\",\"b_customer_segment\":\"Enterprise-Direct\",\"a_num_connected_cust\":\"0\",\"b_longitude_final\":\"77.213528\",\"a_POP_DIST_KM\":\"0.3672465429\",\"b_pop_address\":\"TCL,_Videsh_Sanchar_Bhavan,_Banglasaheb_Road,_New_Delhi_-_110_001\",\"b_hh_name\":\"HH-DEL-BCP-0032-240340453\",\"a_lm_nrc_nerental_onwl\":\"0\",\"b_lm_nrc_inbldg_onwl\":\"40000\",\"account_id_with_18_digit\":\"0012000001OiMX6\",\"a_lm_nrc_bw_onwl\":\"0\",\"a_X2km_cust_count\":\"300\",\"b_Network_Feasibility_Check\":\"Feasible\",\"b_FATG_DIST_KM\":\"333.96474119\",\"a_X5km_avg_bw\":\"164.6554943\",\"b_lm_nrc_ospcapex_onwl\":\"0\",\"b_core_check_hh\":\"Network Feasible on Core Ring of HH\",\"a_Network_F_NF_HH\":\"NA\",\"a_error_code\":\"NA\",\"b_X2km_min_dist\":\"481.84967993\",\"b_a_or_b_end\":\"B\",\"a_Network_Feasibility_Check\":\"Not Feasible\",\"b_X2km_prospect_num_feasible\":\"1632\",\"bw_mbps_upd\":\"34\",\"a_access_check_hh\":\"NA\",\"b_cost_permeter\":\"  0\",\"b_X0.5km_min_bw\":\"2.000\",\"a_X0.5km_prospect_count\":\"44\",\"a_X0.5km_avg_dist\":\"    471.9180868\",\"a_FATG_PROW\":\"No\",\"b_X2km_prospect_perc_feasible\":\"0.9945155393\",\"a_X2km_min_dist\":\" 471.9180868\",\"a_lm_nrc_mast_ofrf\":\"0\",\"a_X2km_prospect_num_feasible\":\"1670\",\"a_SERVICE_ID\":\"NA\",\"b_connected_building_tag\":\"0\",\"b_pop_lat\":\"28.63095826\",\"b_X5km_avg_dist\":\"2122.7313066\",\"a_lm_nrc_mast_onrf\":\"0\",\"a_HH_DIST_KM\":\"242.0701220\",\"a_pop_selected\":\"no\",\"a_min_hh_fatg\":\" 303\",\"a_X5km_avg_dist\":\"2116.888805\",\"b_X5km_prospect_min_dist\":\"352.601910124\",\"b_X2km_prospect_avg_bw\":\"105.8379427\",\"a_latitude_final\":\"28.6163909\",\"b_Network_F_NF_CC\":\"No CC Found\",\"b_POP_Category\":\"Metro Service Ready\",\"a_cost_permeter\":\"  0\",\"b_X0.5km_min_dist\":\"481.84967993\",\"b_lm_arc_bw_onwl\":\"108910\",\"b_Probabililty_Access_Feasibility\":\"0.93\",\"b_X0.5km_avg_dist\":\"490.60797661\",\"a_lm_arc_bw_onrf\":\"0\",\"link_id\":\"530\",\"a_lm_nrc_bw_prov_ofrf\":\"0\",\"a_POP_TCL_Access\":\"Yes\",\"b_POP_Network_Location_Type\":\"Mega POP\",\"a_FATG_TCL_Access\":\"Yes\",\"b_error_flag\":\"0\",\"a_lm_nrc_mux_onwl\":\"58810\",\"sla_varient\":\"Standard\",\"a_lm_nrc_bw_onrf\":\"0\",\"a_pop_ui_id\":\"none\",\"b_total_cost\":\"207720\",\"b_X0.5km_prospect_avg_dist\":\"457.07767145\",\"a_core_check_hh\":\"NA\",\"b_Network_F_NF_HH_Flag\":\"1\",\"a_local_loop_interface\":\"GE\",\"a_Probabililty_Access_Feasibility\":\"0.92\",\"manual_flag\":\"1\",\"a_lm_arc_bw_prov_ofrf\":\"0\",\"b_X0.5km_prospect_min_bw\":\"2.000\",\"b_X5km_cust_count\":\" 438\",\"b_X0.5km_prospect_num_feasible\":\"  38\",\"b_FATG_PROW\":\"No\",\"opportunity_term\":\"24\",\"b_num_connected_building\":\"   0\",\"a_X0.5km_min_bw\":\"      0.001\",\"a_X5km_prospect_count\":\"3420\",\"a_lm_nrc_inbldg_onwl\":\"40000\",\"a_X2km_avg_bw\":\"196.7626033\",\"b_lm_arc_bw_onrf\":\"0\",\"b_X2km_avg_bw\":\" 199.3269628\",\"b_hh_flag\":\"1\",\"b_pop_ui_id\":\"none\",\"a_Network_F_NF_HH_Flag\":\"0\",\"a_error_flag\":\"0\",\"a_X0.5km_min_dist\":\"    471.9180868\",\"a_Orch_Connection\":\"Wireline\",\"b_X5km_min_dist\":\"481.84967993\",\"b_X2km_prospect_avg_dist\":\"1471.5637826\",\"b_error_msg\":\"No error\",\"a_last_mile_contract_term\":\"2 Year\",\"a_OnnetCity_tag\":\"1\",\"a_POP_DIST_KM_SERVICE\":\" 2.213629916\",\"dist_betw_pops\":\"   1\",\"a_connected_cust_tag\":\"0\",\"b_Orch_LM_Type\":\"Onnet\",\"a_X5km_prospect_num_feasible\":\"3409\",\"b_pop_network_loc_id\":\"TINDDLNDELCNPL0030\",\"Type\":\"OnnetWL_NPL\",\"b_pop_selected\":\"no\",\"a_access_check_CC\":\"No CC Found\",\"a_X0.5km_prospect_min_dist\":\"326.21594202\",\"a_FATG_Category\":\"Wimax Site\",\"a_X0.5km_cust_count\":\"1\",\"b_X0.5km_prospect_min_dist\":\"352.601910124\",\"b_HH_0_5km\":\"Red cross-13045391\",\"a_X5km_cust_count\":\"437\",\"a_X5km_prospect_avg_bw\":\" 76.1186269\",\"a_hh_flag\":\"1\",\"a_X2km_prospect_min_dist\":\"326.21594202\",\"b_X5km_min_bw\":\"0.001\",\"b_X0.5km_prospect_avg_bw\":\" 17.15789474\",\"b_HH_DIST_KM\":\"203.29668004\",\"b_X5km_prospect_perc_feasible\":\"0.9964830012\",\"b_POP_Construction_Status\":\"In Service\",\"a_net_pre_feasible_flag\":\"0\",\"prospect_name\":\"Dev Test Services & Sons\",\"Predicted_Access_Feasibility\":\"Not Feasible\",\"a_time_taken\":\"11.589\",\"a_total_cost\":\" 207720\",\"b_error_code\":\"NA\",\"a_X5km_min_dist\":\" 471.9180868\",\"b_X5km_prospect_avg_dist\":\"2587.024005\",\"a_pop_address\":\"TCL,_Videsh_Sanchar_Bhavan,_Banglasaheb_Road,_New_Delhi_-_110_001\",\"a_X0.5km_prospect_avg_dist\":\"401.5080933\",\"b_POP_TCL_Access\":\"Yes\",\"b_core_check_CC\":\"No CC Found\",\"a_X5km_prospect_max_bw\":\"10240\",\"b_POP_DIST_KM\":\"0.50830909040\",\"product_name\":\"NPL\",\"a_Network_F_NF_CC\":\"No CC Found\",\"a_X5km_max_bw\":\"10240\",\"b_lm_nrc_bw_onrf\":\"0\",\"a_lm_nrc_ospcapex_onwl\":\"     0\",\"a_X2km_avg_dist\":\"1454.066273\",\"b_Network_F_NF_CC_Flag\":\"0\",\"b_lm_nrc_mast_ofrf\":\"0\",\"b_access_check_hh\":\"Network Feasible on Access Ring of HH\",\"b_FATG_Network_Location_Type\":\"Access/Customer POP\",\"b_X2km_cust_count\":\" 296\",\"b_lm_nrc_mast_onrf\":\"0\",\"a_X2km_prospect_avg_bw\":\"104.06197975\",\"a_X5km_prospect_perc_feasible\":\"0.9967836257\",\"b_X5km_avg_bw\":\" 164.2864178\",\"a_connected_building_tag\":\"0\",\"a_pop_name\":\"TCL  VSB, DELHI-1-11641110\",\"a_core_check_CC\":\"No CC Found\",\"feasibility_response_created_date\":\"2018-09-27\",\"b_resp_city\":\"Central Delhi\",\"a_FATG_Network_Location_Type\":\"Mega POP\",\"a_pop_long\":\"77.20774270\",\"a_X0.5km_prospect_min_bw\":\"2\",\"a_POP_DIST_KM_SERVICE_MOD\":\" 5\",\"a_X5km_prospect_min_dist\":\"326.21594202\",\"a_Orch_BW\":\"34\",\"b_Orch_Category\":\"Capex greater than 175m\",\"b_X0.5km_max_bw\":\"     56\",\"b_site_id\":\"34\",\"a_HH_0_5km\":\"SC-DEL-BCP-0031-240340403\",\"a_X0.5km_max_bw\":\"      0.001\",\"a_X0.5km_prospect_num_feasible\":\"44\",\"a_a_or_b_end\":\"A\",\"b_X0.5km_cust_count\":\"   4\",\"a_Orch_LM_Type\":\"Onnet\",\"b_lm_nrc_nerental_onwl\":\"0\",\"b_local_loop_interface\":\"GE\",\"a_FATG_Building_Type\":\"Commercial\",\"a_Orch_Category\":\"Capex greater than 175m\",\"chargeable_distance\":\"  5\",\"b_X0.5km_prospect_perc_feasible\":\"1.0000000000\",\"b_FATG_Building_Type\":\"Commercial\",\"b_Orch_Connection\":\"Wireline\",\"b_min_hh_fatg\":\"254\",\"b_X0.5km_prospect_max_bw\":\"  100\",\"quotetype_quote\":\"New Order\",\"a_error_msg\":\"No error\",\"b_SERVICE_ID\":\"NA\",\"a_customer_segment\":\"Enterprise-Direct\"}],\"Feasible\":[]}");
		linkFeasibility.setType("Intracity");
		return linkFeasibility;
		}
		public List<LinkFeasibility> getLinkFeasibilityList1() {
			List<LinkFeasibility> linkFeasibilityList =new ArrayList<>();
			linkFeasibilityList.add(getLinkFeasibility1());
			return linkFeasibilityList;
			
		}
	public List<QuoteDelegation> getQuoteDelegationList() {
		List<QuoteDelegation> quoteDelegationList =new ArrayList<>();
		quoteDelegationList.add(getQuoteDelegation());
		return quoteDelegationList;
	}
	
	public OrderProductComponentsAttributeValue getOrderProductComponentAttribtueValue() {
		OrderProductComponentsAttributeValue val = new OrderProductComponentsAttributeValue();
		val.setId(1);
		val.setAttributeValues("value");
		val.setDisplayValue("disp val");
		val.setOrderProductComponent(getOrderProductComponent());
		val.setProductAttributeMaster(getProductAtrributeMas());
		return val;
		
	}
	
	public List<OrderProductComponentsAttributeValue> getOrderProductComponentAttribtueValueList() {
		List<OrderProductComponentsAttributeValue> list = new ArrayList<>();
		list.add(getOrderProductComponentAttribtueValue());
		return list;
		
	}
	
	public Map<String, Integer> getCountMap(){
		Map<String, Integer> countMap = new HashMap<String,Integer>();
		countMap.put("totalOrderCount", 1);
		countMap.put("activeOrderCount", 1);
		return countMap;

	}
	

	public CofDetails getCofDetails() {
		CofDetails cofDetails = new CofDetails();
		cofDetails.setOrderUuid("VBHF65B");
		cofDetails.setUriPath("/testpath");
		cofDetails.setSource("automated_cof");
		cofDetails.setCreatedBy("optimus");
		return cofDetails;
	}
	
	public List<ComponentDetail> getComponetDetails() {
		List<ComponentDetail> componentDetails = new ArrayList<>();
		ComponentDetail componentDetail = new ComponentDetail();
		componentDetail.setComponentId(1);
		componentDetail.setAttributes(getAttributeDetail());
		componentDetail.setIsActive("Y");
		componentDetail.setName("CPE");
		componentDetails.add(componentDetail);
		return componentDetails;
	}
	public List<AttributeDetail> getAttributeDetail() {
		AttributeDetail attributeDetail = new AttributeDetail();
		List<AttributeDetail> arAttributeDetails = new ArrayList<>();
		attributeDetail.setAttributeId(1);
		attributeDetail.setName("Service Availability");
		attributeDetail.setValue("False");
		arAttributeDetails.add(attributeDetail);
		return arAttributeDetails;
	}
	 
	public List<OrderLinkFeasibility> getOrderLinkFeasibilityList() {
		List<OrderLinkFeasibility> list = new ArrayList<>();
		list.add(getOrderLinkFeasibility());
		return list;
	}
	
	public List<OrderLinkFeasibility> getOrderLinkFeasibilityListFailure() {
		List<OrderLinkFeasibility> list = new ArrayList<>();
		list.add(getOrderLinkFeasibilityFailure());
		return list;
	}
	
	public OrderLinkFeasibility getOrderLinkFeasibility() {
		OrderLinkFeasibility feas = new OrderLinkFeasibility();
		feas.setCreatedTime(new Timestamp(new Date().getTime()));
		feas.setFeasibilityCheck("done");
		feas.setFeasibilityCode("");
		feas.setFeasibilityMode("system");
		feas.setId(1);
		feas.setIsSelected((byte) 1);
		feas.setOrderNplLink(getOrderNplLink());
		feas.setProvider("provider");
		feas.setRank(1);
		feas.setResponseJson(
				"{\"lm_nrc_ospcapex_onwl\":0.0,\"BW_mbps_2\":2.0,\"Service_ID\":0.0,\"total_cost\":132160.0,\"X0.5km_avg_dist\":9999999.0,\"POP_DIST_KM\":0.5100730350500826,\"X0.5km_prospect_avg_bw\":45.2727272727273,\"Latitude_final\":13.0184111,\"OnnetCity_tag\":1.0,\"lm_arc_bw_prov_ofrf\":0.0,\"Network_F_NF_CC\":\"NA\",\"lm_nrc_nerental_onwl\":0.0,\"lm_nrc_inbldg_onwl\":40000.0,\"Network_Feasibility_Check\":\"Feasible\",\"core_check_CC\":\"NA\",\"X5km_min_bw\":120.0,\"access_check_CC\":\"NA\",\"rank\":10,\"POP_DIST_KM_SERVICE_MOD\":15.0,\"X5km_max_bw\":200.0,\"X5km_prospect_perc_feasible\":0.926775147928994,\"lm_arc_bw_onrf\":0.0,\"POP_Category\":\"Metro Service Ready\",\"lm_nrc_bw_prov_ofrf\":0.0,\"FATG_Building_Type\":\"Commercial\",\"HH_0_5_access_rings_F\":\"NA\",\"hh_flag\":\"NA\",\"local_loop_interface\":\"FE\",\"X5km_prospect_avg_dist\":2738.92671182687,\"latitude_final\":13.0184111,\"FATG_Network_Location_Type\":\"Access\\/Customer POP\",\"topology\":\"primary_active\",\"BW_mbps_act\":2.0,\"city_tier\":\"Non_Tier1\",\"lm_nrc_bw_onwl\":0.0,\"X2km_prospect_min_dist\":2.21569108223123,\"X2km_prospect_perc_feasible\":0.94300518134715,\"X0.5km_prospect_num_feasible\":33.0,\"cpe_supply_type\":\"rental\",\"Orch_Connection\":\"Wireline\",\"site_id\":\"607_primary\",\"cpe_management_type\":\"Fully Managed\",\"Capex\":\"NA\",\"sales_org\":\"Enterprise\",\"opportunity_term\":12,\"BW_mbps\":2.0,\"resp_city\":\"Tiruvallur\",\"Burstable_BW\":0.0,\"connected_cust_tag\":0.0,\"net_pre_feasible_flag\":1.0,\"Network_F_NF_CC_Flag\":\"NA\",\"Orch_LM_Type\":\"Onnet\",\"X5km_prospect_avg_bw\":201.780650887574,\"FATG_Category\":\"Metro Service Ready\",\"lm_nrc_mast_ofrf\":0.0,\"Feasibility.Response..Created.Date\":17730.0,\"X2km_prospect_min_bw\":2.0,\"X5km_prospect_num_feasible\":1253.0,\"Selected\":false,\"lm_nrc_mast_onrf\":0.0,\"X5km_prospect_count\":1352.0,\"connection_type\":\"Premium\",\"X0.5km_avg_bw\":9999999.0,\"pool_size\":0,\"X2km_cust_count\":2.0,\"Customer_Segment\":\"Enterprise - Strategic\",\"customer_segment\":\"Enterprise - Strategic\",\"FATG_PROW\":\"No\",\"connected_building_tag\":0.0,\"FATG_DIST_KM\":41.33379039696604,\"X0.5km_prospect_max_bw\":500.0,\"X5km_prospect_min_dist\":2.21569108223123,\"Type\":\"OnnetWL\",\"FATG_Ring_type\":\"SDH\",\"longitude_final\":80.18774819999999,\"Orch_BW\":2.0,\"scenario_1\":0.0,\"scenario_2\":1.0,\"sno\":3,\"access_check_hh\":\"NA\",\"X2km_prospect_count\":386.0,\"min_hh_fatg\":41.33379039696604,\"POP_Construction_Status\":\"In Service\",\"X5km_avg_dist\":1616.32490273161,\"X2km_avg_dist\":1616.32490273161,\"X2km_prospect_avg_bw\":390.502590673575,\"X0.5km_prospect_min_dist\":2.21569108223123,\"cost_permeter\":0.0,\"Identifier\":\"NA\",\"prospect_name\":\"Kasturi & Sons Limited\",\"Predicted_Access_Feasibility\":\"Feasible\",\"X2km_max_bw\":200.0,\"X2km_prospect_max_bw\":10240.0,\"last_mile_contract_term\":\"1 Year\",\"X5km_prospect_min_bw\":0.064,\"X2km_min_bw\":120.0,\"burstable_bw\":0,\"seq_no\":141,\"bw_mbps\":2,\"X0.5km_cust_count\":0.0,\"X0.5km_min_bw\":9999999.0,\"product_name\":\"Internet Access Service\",\"Network_F_NF_HH\":\"NA\",\"HH_0_5_access_rings_NF\":\"NA\",\"X0.5km_min_dist\":9999999.0,\"POP_Building_Type\":\"Commercial\",\"core_check_hh\":\"NA\",\"HH_DIST_KM\":48.632402798319305,\"X0.5km_prospect_min_bw\":2.0,\"POP_DIST_KM_SERVICE\":14.152705282191185,\"lm_arc_bw_onwl\":33350.0,\"num_connected_building\":0.0,\"account_id_with_18_digit\":\"0012000000E1z9KAAR\",\"Orch_Category\":\"Capex less than 175m\",\"X0.5km_prospect_avg_dist\":243.087859734992,\"POP_Network_Location_Type\":\"Mega POP\",\"lm_nrc_mux_onwl\":58810.0,\"num_connected_cust\":0.0,\"HH_0_5km\":\"NA\",\"X5km_min_dist\":1424.59360546586,\"feasibility_response_created_date\":\"2018-07-18\",\"X2km_prospect_num_feasible\":364.0,\"additional_ip\":0,\"X2km_avg_bw\":160.0,\"X0.5km_max_bw\":9999999.0,\"X5km_avg_bw\":160.0,\"Status.v2\":\"NA\",\"X5km_prospect_max_bw\":10240.0,\"X2km_prospect_avg_dist\":1361.96557355991,\"cpe_variant\":\"None\",\"Longitude_final\":80.18774819999999,\"X5km_cust_count\":2.0,\"sum_no_of_sites_uni_len\":1,\"Product.Name\":\"Internet Access Service\",\"X0.5km_prospect_perc_feasible\":1.0,\"POP_TCL_Access\":\"Yes\",\"X0.5km_prospect_count\":33.0,\"Probabililty_Access_Feasibility\":0.8533333333333334,\"lm_nrc_bw_onrf\":0.0,\"quotetype_quote\":\"New Order\",\"X2km_min_dist\":1424.59360546586,\"FATG_TCL_Access\":\"No\",\"Network_F_NF_HH_Flag\":\"NA\"},{\"lm_nrc_ospcapex_onwl\":0.0,\"total_cost\":62375.757575757576,\"otc_tikona\":10000.0,\"Latitude_final\":13.0184111,\"lm_arc_bw_prov_ofrf\":35000.0,\"lm_nrc_nerental_onwl\":0.0,\"lm_nrc_inbldg_onwl\":0.0,\"offnet_5km_Min_accuracy_num\":50.0,\"interim_BW\":2.0,\"offnet_5km_Min_DistanceKilometers\":1.2178610509953627,\"rank\":5,\"offnet_0_5km_Max_BW_Mbps\":0.0,\"lm_arc_bw_onrf\":0.0,\"prospect_0_5km_Min_BW_Mbps\":0.0,\"lm_nrc_bw_prov_ofrf\":10000.0,\"local_loop_interface\":\"FE\",\"bw_flag_32\":0.0,\"arc_tikona\":35000.0,\"latitude_final\":13.0184111,\"topology\":\"primary_active\",\"min_mast_ht\":0.0,\"lm_nrc_bw_onwl\":0.0,\"cpe_supply_type\":\"rental\",\"offnet_2km_Min_DistanceKilometers\":1.2178610509953627,\"prospect_2km_feasibility_pct\":0.7636363636363637,\"Orch_Connection\":\"Wireless\",\"bw_flag_3\":0.0,\"offnet_2km_cust_Count\":2.0,\"site_id\":\"607_primary\",\"cpe_management_type\":\"Fully Managed\",\"sales_org\":\"Enterprise\",\"opportunity_term\":12,\"BW_mbps\":2.0,\"resp_city\":\"Tiruvallur\",\"prospect_2km_Avg_DistanceKilometers\":1.4529000280961837,\"offnet_5km_Max_BW_Mbps\":16.0,\"prospect_2km_Sum_Feasibility_flag\":42.0,\"arc_sify\":45000.0,\"prospect_2km_Avg_BW_Mbps\":2.343709090909091,\"Orch_LM_Type\":\"Offnet\",\"max_mast_ht\":27.0,\"lm_nrc_mast_ofrf\":17375.757575757576,\"Feasibility.Response..Created.Date\":17730.0,\"Selected\":true,\"provider_tot_towers\":0.0,\"lm_nrc_mast_onrf\":0.0,\"connection_type\":\"Premium\",\"offnet_2km_Min_BW_Mbps\":0.512,\"pool_size\":0,\"offnet_0_5km_Min_BW_Mbps\":0.0,\"customer_segment\":\"Enterprise - Strategic\",\"offnet_5km_Avg_BW_Mbps\":1.7612,\"time_taken\":3.1749999999883585,\"prospect_0_5km_Sum_Feasibility_flag\":0.0,\"Local.Loop.Interface\":\"FE\",\"prospect_2km_cust_Count\":55.0,\"Type\":\"OffnetRF\",\"longitude_final\":80.18774819999999,\"Orch_BW\":2.0,\"prospect_2km_Max_BW_Mbps\":20.0,\"otc_sify\":15000.0,\"Customer.Segment\":\"Enterprise - Strategic\",\"provider_min_dist\":0.7138602916391107,\"offnet_5km_Min_BW_Mbps\":0.128,\"prospect_name\":\"Kasturi & Sons Limited\",\"Predicted_Access_Feasibility\":\"Feasible\",\"avg_mast_ht\":9.696969696969697,\"last_mile_contract_term\":\"1 Year\",\"prospect_0_5km_feasibility_pct\":0.0,\"burstable_bw\":0,\"bw_mbps\":2,\"prospect_0_5km_Max_BW_Mbps\":0.0,\"offnet_0_5km_Min_accuracy_num\":0.0,\"product_name\":\"Internet Access Service\",\"closest_provider\":\"tikona_min_dist_km\",\"lm_arc_bw_onwl\":0.0,\"nearest_mast_ht_cost\":56400.0,\"account_id_with_18_digit\":\"0012000000E1z9KAAR\",\"Orch_Category\":\"Tikona\",\"lm_nrc_mux_onwl\":0.0,\"offnet_2km_Avg_BW_Mbps\":1.256,\"offnet_2km_Min_accuracy_num\":50.0,\"cust_count\":132.0,\"offnet_0_5km_Avg_BW_Mbps\":0.0,\"feasibility_response_created_date\":\"2018-07-18\",\"prospect_0_5km_Min_DistanceKilometers\":99999.0,\"additional_ip\":0,\"nearest_mast_ht\":18.0,\"cpe_variant\":\"None\",\"prospect_2km_Min_BW_Mbps\":0.064,\"Longitude_final\":80.18774819999999,\"Last.Mile.Contract.Term\":\"1 Year\",\"sum_no_of_sites_uni_len\":1,\"Product.Name\":\"Internet Access Service\",\"offnet_0_5km_Min_DistanceKilometers\":99999.0,\"offnet_0_5km_cust_Count\":0.0,\"prospect_0_5km_Avg_BW_Mbps\":0.0,\"Probabililty_Access_Feasibility\":0.9666666666666667,\"lm_nrc_bw_onrf\":0.0,\"quotetype_quote\":\"New Order\"}");
		feas.setType("type");
		feas.setIsSelected((byte) 1);
		return feas;
		}
	
	public OrderLinkFeasibility getOrderLinkFeasibilityFailure() {
		OrderLinkFeasibility feas = new OrderLinkFeasibility();
		feas.setCreatedTime(new Timestamp(new Date().getTime()));
		feas.setFeasibilityCheck("Failed");
		feas.setFeasibilityCode("");
		feas.setFeasibilityMode("manual");
		feas.setId(1);
		feas.setIsSelected((byte) 1);
		feas.setOrderNplLink(getOrderNplLink());
		feas.setProvider("provider");
		feas.setRank(1);
		feas.setType("type");
		feas.setIsSelected((byte) 1);
		feas.setResponseJson(
				"{\"lm_nrc_ospcapex_onwl\":0.0,\"BW_mbps_2\":2.0,\"Service_ID\":0.0,\"total_cost\":132160.0,\"X0.5km_avg_dist\":9999999.0,\"POP_DIST_KM\":0.5100730350500826,\"X0.5km_prospect_avg_bw\":45.2727272727273,\"Latitude_final\":13.0184111,\"OnnetCity_tag\":1.0,\"lm_arc_bw_prov_ofrf\":0.0,\"Network_F_NF_CC\":\"NA\",\"lm_nrc_nerental_onwl\":0.0,\"lm_nrc_inbldg_onwl\":40000.0,\"Network_Feasibility_Check\":\"Feasible\",\"core_check_CC\":\"NA\",\"X5km_min_bw\":120.0,\"access_check_CC\":\"NA\",\"rank\":10,\"POP_DIST_KM_SERVICE_MOD\":15.0,\"X5km_max_bw\":200.0,\"X5km_prospect_perc_feasible\":0.926775147928994,\"lm_arc_bw_onrf\":0.0,\"POP_Category\":\"Metro Service Ready\",\"lm_nrc_bw_prov_ofrf\":0.0,\"FATG_Building_Type\":\"Commercial\",\"HH_0_5_access_rings_F\":\"NA\",\"hh_flag\":\"NA\",\"local_loop_interface\":\"FE\",\"X5km_prospect_avg_dist\":2738.92671182687,\"latitude_final\":13.0184111,\"FATG_Network_Location_Type\":\"Access\\/Customer POP\",\"topology\":\"primary_active\",\"BW_mbps_act\":2.0,\"city_tier\":\"Non_Tier1\",\"lm_nrc_bw_onwl\":0.0,\"X2km_prospect_min_dist\":2.21569108223123,\"X2km_prospect_perc_feasible\":0.94300518134715,\"X0.5km_prospect_num_feasible\":33.0,\"cpe_supply_type\":\"rental\",\"Orch_Connection\":\"Wireline\",\"site_id\":\"607_primary\",\"cpe_management_type\":\"Fully Managed\",\"Capex\":\"NA\",\"sales_org\":\"Enterprise\",\"opportunity_term\":12,\"BW_mbps\":2.0,\"resp_city\":\"Tiruvallur\",\"Burstable_BW\":0.0,\"connected_cust_tag\":0.0,\"net_pre_feasible_flag\":1.0,\"Network_F_NF_CC_Flag\":\"NA\",\"Orch_LM_Type\":\"Onnet\",\"X5km_prospect_avg_bw\":201.780650887574,\"FATG_Category\":\"Metro Service Ready\",\"lm_nrc_mast_ofrf\":0.0,\"Feasibility.Response..Created.Date\":17730.0,\"X2km_prospect_min_bw\":2.0,\"X5km_prospect_num_feasible\":1253.0,\"Selected\":false,\"lm_nrc_mast_onrf\":0.0,\"X5km_prospect_count\":1352.0,\"connection_type\":\"Premium\",\"X0.5km_avg_bw\":9999999.0,\"pool_size\":0,\"X2km_cust_count\":2.0,\"Customer_Segment\":\"Enterprise - Strategic\",\"customer_segment\":\"Enterprise - Strategic\",\"FATG_PROW\":\"No\",\"connected_building_tag\":0.0,\"FATG_DIST_KM\":41.33379039696604,\"X0.5km_prospect_max_bw\":500.0,\"X5km_prospect_min_dist\":2.21569108223123,\"Type\":\"OnnetWL\",\"FATG_Ring_type\":\"SDH\",\"longitude_final\":80.18774819999999,\"Orch_BW\":2.0,\"scenario_1\":0.0,\"scenario_2\":1.0,\"sno\":3,\"access_check_hh\":\"NA\",\"X2km_prospect_count\":386.0,\"min_hh_fatg\":41.33379039696604,\"POP_Construction_Status\":\"In Service\",\"X5km_avg_dist\":1616.32490273161,\"X2km_avg_dist\":1616.32490273161,\"X2km_prospect_avg_bw\":390.502590673575,\"X0.5km_prospect_min_dist\":2.21569108223123,\"cost_permeter\":0.0,\"Identifier\":\"NA\",\"prospect_name\":\"Kasturi & Sons Limited\",\"Predicted_Access_Feasibility\":\"Feasible\",\"X2km_max_bw\":200.0,\"X2km_prospect_max_bw\":10240.0,\"last_mile_contract_term\":\"1 Year\",\"X5km_prospect_min_bw\":0.064,\"X2km_min_bw\":120.0,\"burstable_bw\":0,\"seq_no\":141,\"bw_mbps\":2,\"X0.5km_cust_count\":0.0,\"X0.5km_min_bw\":9999999.0,\"product_name\":\"Internet Access Service\",\"Network_F_NF_HH\":\"NA\",\"HH_0_5_access_rings_NF\":\"NA\",\"X0.5km_min_dist\":9999999.0,\"POP_Building_Type\":\"Commercial\",\"core_check_hh\":\"NA\",\"HH_DIST_KM\":48.632402798319305,\"X0.5km_prospect_min_bw\":2.0,\"POP_DIST_KM_SERVICE\":14.152705282191185,\"lm_arc_bw_onwl\":33350.0,\"num_connected_building\":0.0,\"account_id_with_18_digit\":\"0012000000E1z9KAAR\",\"Orch_Category\":\"Capex less than 175m\",\"X0.5km_prospect_avg_dist\":243.087859734992,\"POP_Network_Location_Type\":\"Mega POP\",\"lm_nrc_mux_onwl\":58810.0,\"num_connected_cust\":0.0,\"HH_0_5km\":\"NA\",\"X5km_min_dist\":1424.59360546586,\"feasibility_response_created_date\":\"2018-07-18\",\"X2km_prospect_num_feasible\":364.0,\"additional_ip\":0,\"X2km_avg_bw\":160.0,\"X0.5km_max_bw\":9999999.0,\"X5km_avg_bw\":160.0,\"Status.v2\":\"NA\",\"X5km_prospect_max_bw\":10240.0,\"X2km_prospect_avg_dist\":1361.96557355991,\"cpe_variant\":\"None\",\"Longitude_final\":80.18774819999999,\"X5km_cust_count\":2.0,\"sum_no_of_sites_uni_len\":1,\"Product.Name\":\"Internet Access Service\",\"X0.5km_prospect_perc_feasible\":1.0,\"POP_TCL_Access\":\"Yes\",\"X0.5km_prospect_count\":33.0,\"Probabililty_Access_Feasibility\":0.8533333333333334,\"lm_nrc_bw_onrf\":0.0,\"quotetype_quote\":\"New Order\",\"X2km_min_dist\":1424.59360546586,\"FATG_TCL_Access\":\"No\",\"Network_F_NF_HH_Flag\":\"NA\"},{\"lm_nrc_ospcapex_onwl\":0.0,\"total_cost\":62375.757575757576,\"otc_tikona\":10000.0,\"Latitude_final\":13.0184111,\"lm_arc_bw_prov_ofrf\":35000.0,\"lm_nrc_nerental_onwl\":0.0,\"lm_nrc_inbldg_onwl\":0.0,\"offnet_5km_Min_accuracy_num\":50.0,\"interim_BW\":2.0,\"offnet_5km_Min_DistanceKilometers\":1.2178610509953627,\"rank\":5,\"offnet_0_5km_Max_BW_Mbps\":0.0,\"lm_arc_bw_onrf\":0.0,\"prospect_0_5km_Min_BW_Mbps\":0.0,\"lm_nrc_bw_prov_ofrf\":10000.0,\"local_loop_interface\":\"FE\",\"bw_flag_32\":0.0,\"arc_tikona\":35000.0,\"latitude_final\":13.0184111,\"topology\":\"primary_active\",\"min_mast_ht\":0.0,\"lm_nrc_bw_onwl\":0.0,\"cpe_supply_type\":\"rental\",\"offnet_2km_Min_DistanceKilometers\":1.2178610509953627,\"prospect_2km_feasibility_pct\":0.7636363636363637,\"Orch_Connection\":\"Wireless\",\"bw_flag_3\":0.0,\"offnet_2km_cust_Count\":2.0,\"site_id\":\"607_primary\",\"cpe_management_type\":\"Fully Managed\",\"sales_org\":\"Enterprise\",\"opportunity_term\":12,\"BW_mbps\":2.0,\"resp_city\":\"Tiruvallur\",\"prospect_2km_Avg_DistanceKilometers\":1.4529000280961837,\"offnet_5km_Max_BW_Mbps\":16.0,\"prospect_2km_Sum_Feasibility_flag\":42.0,\"arc_sify\":45000.0,\"prospect_2km_Avg_BW_Mbps\":2.343709090909091,\"Orch_LM_Type\":\"Offnet\",\"max_mast_ht\":27.0,\"lm_nrc_mast_ofrf\":17375.757575757576,\"Feasibility.Response..Created.Date\":17730.0,\"Selected\":true,\"provider_tot_towers\":0.0,\"lm_nrc_mast_onrf\":0.0,\"connection_type\":\"Premium\",\"offnet_2km_Min_BW_Mbps\":0.512,\"pool_size\":0,\"offnet_0_5km_Min_BW_Mbps\":0.0,\"customer_segment\":\"Enterprise - Strategic\",\"offnet_5km_Avg_BW_Mbps\":1.7612,\"time_taken\":3.1749999999883585,\"prospect_0_5km_Sum_Feasibility_flag\":0.0,\"Local.Loop.Interface\":\"FE\",\"prospect_2km_cust_Count\":55.0,\"Type\":\"OffnetRF\",\"longitude_final\":80.18774819999999,\"Orch_BW\":2.0,\"prospect_2km_Max_BW_Mbps\":20.0,\"otc_sify\":15000.0,\"Customer.Segment\":\"Enterprise - Strategic\",\"provider_min_dist\":0.7138602916391107,\"offnet_5km_Min_BW_Mbps\":0.128,\"prospect_name\":\"Kasturi & Sons Limited\",\"Predicted_Access_Feasibility\":\"Feasible\",\"avg_mast_ht\":9.696969696969697,\"last_mile_contract_term\":\"1 Year\",\"prospect_0_5km_feasibility_pct\":0.0,\"burstable_bw\":0,\"bw_mbps\":2,\"prospect_0_5km_Max_BW_Mbps\":0.0,\"offnet_0_5km_Min_accuracy_num\":0.0,\"product_name\":\"Internet Access Service\",\"closest_provider\":\"tikona_min_dist_km\",\"lm_arc_bw_onwl\":0.0,\"nearest_mast_ht_cost\":56400.0,\"account_id_with_18_digit\":\"0012000000E1z9KAAR\",\"Orch_Category\":\"Tikona\",\"lm_nrc_mux_onwl\":0.0,\"offnet_2km_Avg_BW_Mbps\":1.256,\"offnet_2km_Min_accuracy_num\":50.0,\"cust_count\":132.0,\"offnet_0_5km_Avg_BW_Mbps\":0.0,\"feasibility_response_created_date\":\"2018-07-18\",\"prospect_0_5km_Min_DistanceKilometers\":99999.0,\"additional_ip\":0,\"nearest_mast_ht\":18.0,\"cpe_variant\":\"None\",\"prospect_2km_Min_BW_Mbps\":0.064,\"Longitude_final\":80.18774819999999,\"Last.Mile.Contract.Term\":\"1 Year\",\"sum_no_of_sites_uni_len\":1,\"Product.Name\":\"Internet Access Service\",\"offnet_0_5km_Min_DistanceKilometers\":99999.0,\"offnet_0_5km_cust_Count\":0.0,\"prospect_0_5km_Avg_BW_Mbps\":0.0,\"Probabililty_Access_Feasibility\":0.9666666666666667,\"lm_nrc_bw_onrf\":0.0,\"quotetype_quote\":\"New Order\"}");		
		return feas;
		}
	
	public ExcelBean getExcelBeanForZeroLinkId() {
		ExcelBean bean = new ExcelBean();
		bean.setLinkId(0);
		bean.setLrSection("");
		return bean;
	}
	
	public ExcelBean getExcelBeanForZeroSiteId() {
		ExcelBean bean = new ExcelBean();
		bean.setSiteAId(0);
		bean.setSiteBId(0);
		bean.setLrSection("");
		return bean;
	}
	
	public ExcelBean getExcelBeanFoZeroSiteId() {
		ExcelBean bean = new ExcelBean();
		bean.setSiteAId(0);
		bean.setSiteBId(0);
		bean.setLrSection("");
		return bean;
	}
	
	public ExcelBean getExcelBeanForSiteAId() {
		ExcelBean bean = new ExcelBean();
		bean.setSiteAId(1);
		bean.setLrSection("");
		return bean;
	}
	
	public ExcelBean getExcelBeanForSiteBId() {
		ExcelBean bean = new ExcelBean();
		bean.setSiteBId(1);
		bean.setLrSection("");
		return bean;
	}
	
	public ExcelBean getExcelBeanForAllIdNull() {
		ExcelBean bean = new ExcelBean();
		return bean;
	}
	
	public Row createRow() {
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row = sheet.createRow(0);
		return row;
	}

	public LegalAttributeBean getLegalAttributeBean() {
		LegalAttributeBean legalAttributeBean=new LegalAttributeBean();
		legalAttributeBean.setId(1);
		legalAttributeBean.setAttributeValue("Value");
		legalAttributeBean.setDisplayValue("displayValue");
		return legalAttributeBean;
	}
	public MstOmsAttributeBean getMstOmsAttributeBean (String attrName) {
		MstOmsAttributeBean mstOmsAttributeBean=new MstOmsAttributeBean();
		mstOmsAttributeBean.setName(attrName);
		return mstOmsAttributeBean;
		
	}
	public Set<LegalAttributeBean> getLegalAttributeBeanList() {
		Set<LegalAttributeBean> legalAttributeBean=new HashSet<>();
		LegalAttributeBean legalAttributeBean1=getLegalAttributeBean();
		legalAttributeBean1.setMstOmsAttribute(getMstOmsAttributeBean("LEGAL_ENTITY_NAME"));
		LegalAttributeBean legalAttributeBean2=getLegalAttributeBean();
		legalAttributeBean2.setMstOmsAttribute(getMstOmsAttributeBean("GST_Number"));
		LegalAttributeBean legalAttributeBean3=getLegalAttributeBean();
		legalAttributeBean3.setMstOmsAttribute(getMstOmsAttributeBean("CONTACTNAME"));
		LegalAttributeBean legalAttributeBean4=getLegalAttributeBean();
		legalAttributeBean4.setMstOmsAttribute(getMstOmsAttributeBean("CONTACTNO"));
		LegalAttributeBean legalAttributeBean5=getLegalAttributeBean();
		legalAttributeBean5.setMstOmsAttribute(getMstOmsAttributeBean("CONTACTEMAIL"));
		LegalAttributeBean legalAttributeBean6=getLegalAttributeBean();
		legalAttributeBean6.setMstOmsAttribute(getMstOmsAttributeBean("Supplier Contracting Entity"));
		LegalAttributeBean legalAttributeBean7=getLegalAttributeBean();
		legalAttributeBean7.setMstOmsAttribute(getMstOmsAttributeBean("SUPPLIER_LE_OWNER"));
		LegalAttributeBean legalAttributeBean8=getLegalAttributeBean();
		legalAttributeBean8.setMstOmsAttribute(getMstOmsAttributeBean("SUPPLIER_LE_EMAIL"));
		LegalAttributeBean legalAttributeBean9=getLegalAttributeBean();
		legalAttributeBean9.setMstOmsAttribute(getMstOmsAttributeBean("Le Contact"));
		LegalAttributeBean legalAttributeBean10=getLegalAttributeBean();
		legalAttributeBean10.setMstOmsAttribute(getMstOmsAttributeBean("Billing Method"));
		LegalAttributeBean legalAttributeBean11=getLegalAttributeBean();
		legalAttributeBean11.setMstOmsAttribute(getMstOmsAttributeBean("Billing Frequency"));
		LegalAttributeBean legalAttributeBean12=getLegalAttributeBean();
		legalAttributeBean12.setMstOmsAttribute(getMstOmsAttributeBean("Billing Currency"));
		LegalAttributeBean legalAttributeBean13=getLegalAttributeBean();
		legalAttributeBean13.setMstOmsAttribute(getMstOmsAttributeBean("Payment Currency"));
		LegalAttributeBean legalAttributeBean14=getLegalAttributeBean();
		legalAttributeBean14.setMstOmsAttribute(getMstOmsAttributeBean("Payment Term (from date of invoice)"));
		LegalAttributeBean legalAttributeBean15=getLegalAttributeBean();
		legalAttributeBean15.setMstOmsAttribute(getMstOmsAttributeBean("Invoice Method"));
		LegalAttributeBean legalAttributeBean16=getLegalAttributeBean();
		legalAttributeBean16.setMstOmsAttribute(getMstOmsAttributeBean("TermInMonths"));
		/*LegalAttributeBean legalAttributeBean17=getLegalAttributeBean();
		legalAttributeBean17.setMstOmsAttribute(getMstOmsAttributeBean("BILLING_CONTACT_ID"));
		LegalAttributeBean legalAttributeBean18=getLegalAttributeBean();
		legalAttributeBean18.setMstOmsAttribute(getMstOmsAttributeBean("Customer Contracting Entity"));*/
		LegalAttributeBean legalAttributeBean19=getLegalAttributeBean();
		legalAttributeBean19.setMstOmsAttribute(getMstOmsAttributeBean("MSA"));
		LegalAttributeBean legalAttributeBean20=getLegalAttributeBean();
		legalAttributeBean20.setMstOmsAttribute(getMstOmsAttributeBean("Service Schedule"));
		legalAttributeBean.add(legalAttributeBean1);
		legalAttributeBean.add(legalAttributeBean2);
		legalAttributeBean.add(legalAttributeBean3);
		legalAttributeBean.add(legalAttributeBean4);
		legalAttributeBean.add(legalAttributeBean5);
		legalAttributeBean.add(legalAttributeBean6);
		legalAttributeBean.add(legalAttributeBean7);
		legalAttributeBean.add(legalAttributeBean8);
		legalAttributeBean.add(legalAttributeBean9);
		legalAttributeBean.add(legalAttributeBean10);
		legalAttributeBean.add(legalAttributeBean11);
		legalAttributeBean.add(legalAttributeBean12);
		legalAttributeBean.add(legalAttributeBean13);
		legalAttributeBean.add(legalAttributeBean14);
		legalAttributeBean.add(legalAttributeBean15);
		legalAttributeBean.add(legalAttributeBean16);
		/*legalAttributeBean.add(legalAttributeBean17);
		legalAttributeBean.add(legalAttributeBean18);*/
		legalAttributeBean.add(legalAttributeBean19);
		legalAttributeBean.add(legalAttributeBean20);
		return legalAttributeBean;
	}
	public Set<QuoteToLeProductFamilyBean> getQuoteToLeProductFamilyBean() {
		Set<QuoteToLeProductFamilyBean> quoteToLeProductFamilySet = new HashSet<>();
		QuoteToLeProductFamilyBean qFamily = new QuoteToLeProductFamilyBean();
		qFamily.setId(1);
		qFamily.setProductName("NPL");
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(1);
		mstProductFamily.setName("NPL");
		mstProductFamily.setStatus((byte) 2);
		qFamily.setSolutions(getProductSolutionBean());
		quoteToLeProductFamilySet.add(qFamily);
		return quoteToLeProductFamilySet;
	}
	public List<ProductSolutionBean> getProductSolutionBean() {

		List<ProductSolutionBean> productSolutions = new ArrayList<>();
		ProductSolutionBean productSolution = new ProductSolutionBean();
		
		productSolution.setProductSolutionId(1);
		productSolution.setOfferingName("point-to-point connectivity");
		productSolution.setLinks(getNplLinkBeanList());
		productSolution.setProductProfileData(
				"{\"offeringName\":\"Single Internet Access\",\"image\":\"\",\"bandwidth\":\"10\",\"bandwidthUnit\":\"mbps\",\"components\":[{\"componentId\":\"1\",\"componentMasterId\":\"1\",\"name\":\"CPE Management\",\"isActive\":\"true\",\"attributes\":[{\"attributeId\":\"1\",\"attributeMasterId\":\"1\",\"name\":\"CPE Manufacturer\",\"value\":\"CISCO\"}]}]}");
	
		productSolutions.add(productSolution);
		return productSolutions;
	}
	public List<NplLinkBean> getNplLinkBeanList() {
		List<NplLinkBean> nplLinkBeanList=new ArrayList<>();
		nplLinkBeanList.add(createNplLinkBean());
		return nplLinkBeanList; 
	}
	public List<QuoteProductComponentBean> getQuoteProductComponentBeanList() {
		List<QuoteProductComponentBean> quoteProductComponentBeanList=new ArrayList<>();
		QuoteProductComponentBean quoteProductComponentBean1=getQuoteProductComponentBean();
		quoteProductComponentBean1.setName("National Connectivity");
		quoteProductComponentBean1.setType("Link");
		QuoteProductComponentBean quoteProductComponentBean2=getQuoteProductComponentBean();
		quoteProductComponentBean2.setName("Private Lines");
		quoteProductComponentBean2.setType("Link");
		QuoteProductComponentBean quoteProductComponentBean3=getQuoteProductComponentBean();
		quoteProductComponentBean3.setName("Last mile");
		quoteProductComponentBean3.setType("Site-A");
		QuoteProductComponentBean quoteProductComponentBean4=getQuoteProductComponentBean();
		quoteProductComponentBean4.setName("Last mile");
		quoteProductComponentBean4.setType("Site-B");
		QuoteProductComponentBean quoteProductComponentBean5=getQuoteProductComponentBean();
		quoteProductComponentBean5.setName("Link Management Charges");
		quoteProductComponentBean5.setType("Link");
		
		quoteProductComponentBeanList.add(quoteProductComponentBean1);
		quoteProductComponentBeanList.add(quoteProductComponentBean2);
		quoteProductComponentBeanList.add(quoteProductComponentBean3);
		quoteProductComponentBeanList.add(quoteProductComponentBean4);
		quoteProductComponentBeanList.add(quoteProductComponentBean5);
		return quoteProductComponentBeanList;
	}
	public QuoteProductComponentBean getQuoteProductComponentBean() {
		QuoteProductComponentBean quoteProductComponentBean=new QuoteProductComponentBean(craeteQuoteProductComponent());
		quoteProductComponentBean.setComponentId(1);
		quoteProductComponentBean.setComponentMasterId(1);
		quoteProductComponentBean.setName("Private Lines");
		quoteProductComponentBean.setType("Link");
		quoteProductComponentBean.setPrice(getQuotePriceBean());
		quoteProductComponentBean.setAttributes(getQuoteProductComponentsAttributeValueBeanList());
		return quoteProductComponentBean;
		
	}
	public List<QuoteProductComponentsAttributeValueBean> getQuoteProductComponentsAttributeValueBeanList(){
		List<QuoteProductComponentsAttributeValueBean> quoteProductComponentsAttributeValueBean=new ArrayList<>();
		QuoteProductComponentsAttributeValueBean quotePrdctCmptsAttrValueBean1=getQuoteProductComponentsAttributeValueBean();
		quotePrdctCmptsAttrValueBean1.setName("Port Bandwidth");
		QuoteProductComponentsAttributeValueBean quotePrdctCmptsAttrValueBean2=getQuoteProductComponentsAttributeValueBean();
		quotePrdctCmptsAttrValueBean2.setName("Network Protection");
		QuoteProductComponentsAttributeValueBean quotePrdctCmptsAttrValueBean3=getQuoteProductComponentsAttributeValueBean();
		quotePrdctCmptsAttrValueBean3.setName("Service Availability");
		QuoteProductComponentsAttributeValueBean quotePrdctCmptsAttrValueBean4=getQuoteProductComponentsAttributeValueBean();
		quotePrdctCmptsAttrValueBean4.setName("Interface Type - A end");
		QuoteProductComponentsAttributeValueBean quotePrdctCmptsAttrValueBean5=getQuoteProductComponentsAttributeValueBean();
		quotePrdctCmptsAttrValueBean5.setName("Interface Type - B end");
		QuoteProductComponentsAttributeValueBean quotePrdctCmptsAttrValueBean6=getQuoteProductComponentsAttributeValueBean();
		quotePrdctCmptsAttrValueBean6.setName("Charge Type");
		QuoteProductComponentsAttributeValueBean quotePrdctCmptsAttrValueBean7=getQuoteProductComponentsAttributeValueBean();
		quotePrdctCmptsAttrValueBean6.setName("Local Loop Bandwidth");
		
		quoteProductComponentsAttributeValueBean.add(quotePrdctCmptsAttrValueBean1);
		quoteProductComponentsAttributeValueBean.add(quotePrdctCmptsAttrValueBean2);
		quoteProductComponentsAttributeValueBean.add(quotePrdctCmptsAttrValueBean3);
		quoteProductComponentsAttributeValueBean.add(quotePrdctCmptsAttrValueBean4);
		quoteProductComponentsAttributeValueBean.add(quotePrdctCmptsAttrValueBean5);
		quoteProductComponentsAttributeValueBean.add(quotePrdctCmptsAttrValueBean6);
		quoteProductComponentsAttributeValueBean.add(quotePrdctCmptsAttrValueBean7);
		
		
		return quoteProductComponentsAttributeValueBean;
	}
	public QuoteProductComponentsAttributeValueBean getQuoteProductComponentsAttributeValueBean() {
		QuoteProductComponentsAttributeValueBean attributeValue = new QuoteProductComponentsAttributeValueBean(getAttribute());
		return attributeValue;
	}
	public QuoteNplSiteBean getQuoteNplSiteBean () {
		QuoteNplSiteBean quoteNplSiteBean=new QuoteNplSiteBean(getIllsites());
		quoteNplSiteBean.setType("DC");
		return quoteNplSiteBean;
		
	}
	public List<QuoteNplSiteBean> getQuoteNplSiteBeanList () {
		List<QuoteNplSiteBean> quoteNplSiteBeanList=new ArrayList<>();
		quoteNplSiteBeanList.add(getQuoteNplSiteBean());
		quoteNplSiteBeanList.add(getQuoteNplSiteBean());
		return quoteNplSiteBeanList;
		
	}

	public String getAddressDetailJSON() {
		return "{\"addressLineOne\":\"omkar 1988\",\"city\":\"Mumbai\",\"country\":\"INDIA\",\"locality\":\"Mumbai\",\"pincode\":\"400045\",\"source\":\"manual\",\"state\":\"MAHARASHTRA\",\"latLong\":\"19.0759837,72.8776559\"}";
	}
	
	public QuotePriceBean getQuotePriceBean() {
		QuotePriceBean quotePriceBean=new QuotePriceBean(geQuotePrice());
		return quotePriceBean;
		
	}

	public String getBillingContactInfoJSON() {
		return "{\"billingInfoid\":194,\"addressSeq\":4,\"billAccNo\":\"ILL037394\",\"billAddr\":\"SREI, Level 5 Maruti Udyog, Sector -18 Plot 14-A, Anath Road Gurgaon Haryana 122008\",\"billContactSeq\":\"2\",\"contactType\":\"1000001\",\"country\":\"34\",\"customerId\":2,\"emailId\":\"rattish.shekhar@regus.com\",\"fname\":\"Rattish\",\"isactive\":null,\"lname\":\"Shekhar\",\"mobileNumber\":null,\"phoneNumber\":\"91-9819725888\",\"title\":\"Mr.\",\"customerLegalEntityId\":1}";
	}  
	public String getCustomerLocationDetailsJSON(){
		return "{\"addressLineOne\":\"Level 2, Raheja Centre Point, 294 CST Road, Kalina, Off. Bandra? Kurla Complex, Santacruz (E) \",\"city\":\"Mumbai\",\"country\":\"India\",\"locality\":\"\",\"pincode\":\"400098\",\"source\":\"system\",\"state\":\"Maharashtra\",\"latLong\":\"19.07105,72.86063\"}";
	}
	public String getConstructSupplierInformationsJSON(){
	return "{\"entityName\":\"1456\",\"gstnDetails\":\"27AAACV2808C1ZP\",\"address\":\"Videsh Sanchar Bhavan, MG Road, Opp Cross Maidan, Fort, Mumbai - 400001\"}";
	}
	public List<LinkFeasibilityBean> getLinkFeasibilityBeanList() {
		List<LinkFeasibilityBean> linkFeasibilityBeanList=new ArrayList<>();
		linkFeasibilityBeanList.add(getLinkFeasibilityBean());
		return linkFeasibilityBeanList;		
	}
	public LinkFeasibilityBean getLinkFeasibilityBean() {
		LinkFeasibilityBean linkFeasibilityBean=new LinkFeasibilityBean();
		linkFeasibilityBean.setFeasibilityMode("OnnetWL_NPL");
		linkFeasibilityBean.setProvider("TCL");
		linkFeasibilityBean.setCreatedTime(new Date());		
		return linkFeasibilityBean;		
	}
	public QuoteToLeBean getQuoteToLeBean() {
		QuoteToLeBean quoteLeBean =new QuoteToLeBean(getQuoteToLe());
		quoteLeBean.setSupplierLegalEntityId(1);
		quoteLeBean.setLegalAttributes(getLegalAttributeBeanList());
		quoteLeBean.setProductFamilies(getQuoteToLeProductFamilyBean());
		quoteLeBean.setFinalArc(Double.parseDouble("450356"));
		quoteLeBean.setFinalMrc(Double.parseDouble("450356"));
		quoteLeBean.setFinalNrc(Double.parseDouble("450356"));
		return quoteLeBean;
	}
	
	public List<ExcelBean> getExcelBeanList(){
		 List<ExcelBean> list = new ArrayList<>();
		ExcelBean bean = new ExcelBean();
		bean.setAttributeName("attrname");
		bean.setAttributeValue("attrval");
		bean.setLinkId(1);
		bean.setLrSection("lrsec");
		bean.setOrder(1);
		bean.setSiteAId(1);
		bean.setSiteBId(1);
		bean.setSiteId(1);
		list.add(bean);
		return list;
	}
	
	public Map<String,String> getAttrValMapForExcel(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("key", "val");
		return map;

	}

	public String getProductSlaDetailsJSON(){
		return "[{\"factor\":\"Service Availability %\",\"value\":\">= 99.9%\"}]";
	}

	
	
	 public OrderIllSite getOrderGvpnSiteSingleUnmanaged() {
         OrderIllSite orderIllSite = new OrderIllSite();
         orderIllSite.setErfLocSiteaLocationId(1);
         orderIllSite.setRequestorDate(new Date());
         orderIllSite.setId(1);
         orderIllSite.setCreatedBy(1);
         orderIllSite.setCreatedTime(new Date());
         orderIllSite.setEffectiveDate(new Date());
         orderIllSite.setErfLocSiteaLocationId(1);
         orderIllSite.setErfLocSiteaSiteCode("code");
         orderIllSite.setFeasibility((byte) 1);
         orderIllSite.setStatus((byte) 1);
         orderIllSite.setStage("PROVISION_SITES");
         orderIllSite.setSiteCode("TestCode");
         orderIllSite.setMstOrderSiteStatus(getMstOrderSiteStatus());
         orderIllSite.setMstOrderSiteStage(getMstOrderSiteStage());
         orderIllSite.setOrderSiteFeasibility(getSiteFeasiblitySet());
         OrderProductSolution sol = new OrderProductSolution();
         sol.setId(1);
         MstProductOffering mstProdOffering = new MstProductOffering();
      mstProdOffering.setProductName(OrderDetailsExcelDownloadConstants.BUY_SINGLE_UNMANAGED_GVPN);
         sol.setMstProductOffering(mstProdOffering);
         orderIllSite.setOrderProductSolution(sol);

         return orderIllSite;
  }
  
  
  public OrderIllSite getOrderGvpnSiteSingleManaged() {
         OrderIllSite orderIllSite = new OrderIllSite();
         orderIllSite.setErfLocSiteaLocationId(1);
         orderIllSite.setRequestorDate(new Date());
         orderIllSite.setId(1);
         orderIllSite.setCreatedBy(1);
         orderIllSite.setCreatedTime(new Date());
         orderIllSite.setEffectiveDate(new Date());
         orderIllSite.setErfLocSiteaLocationId(1);
         orderIllSite.setErfLocSiteaSiteCode("code");
         orderIllSite.setFeasibility((byte) 1);
         orderIllSite.setStatus((byte) 1);
         orderIllSite.setStage("PROVISION_SITES");
         orderIllSite.setSiteCode("TestCode");
         orderIllSite.setMstOrderSiteStatus(getMstOrderSiteStatus());
         orderIllSite.setMstOrderSiteStage(getMstOrderSiteStage());
         orderIllSite.setOrderSiteFeasibility(getSiteFeasiblitySet());
         orderIllSite.setOrderIllSiteSlas(createOrderIllSiteSlaSet1());
         OrderProductSolution sol = new OrderProductSolution();
         sol.setId(1);
         MstProductOffering mstProdOffering = new MstProductOffering();
        mstProdOffering.setProductName(OrderDetailsExcelDownloadConstants.BUY_SINGLE_MANAGED_GVPN);
         sol.setMstProductOffering(mstProdOffering);
         orderIllSite.setOrderProductSolution(sol);

         return orderIllSite;
  }
  
  
  
  public OrderIllSite getOrderGvpnSiteDualUnmanaged() {
         OrderIllSite orderIllSite = new OrderIllSite();
         orderIllSite.setErfLocSiteaLocationId(1);
         orderIllSite.setRequestorDate(new Date());
         orderIllSite.setId(1);
         orderIllSite.setCreatedBy(1);
         orderIllSite.setCreatedTime(new Date());
         orderIllSite.setEffectiveDate(new Date());
         orderIllSite.setErfLocSiteaLocationId(1);
         orderIllSite.setErfLocSiteaSiteCode("code");
         orderIllSite.setFeasibility((byte) 1);
         orderIllSite.setStatus((byte) 1);
         orderIllSite.setStage("PROVISION_SITES");
         orderIllSite.setSiteCode("TestCode");
         orderIllSite.setMstOrderSiteStatus(getMstOrderSiteStatus());
         orderIllSite.setMstOrderSiteStage(getMstOrderSiteStage());
         orderIllSite.setOrderSiteFeasibility(getSiteFeasiblitySet());
         OrderProductSolution sol = new OrderProductSolution();
         sol.setId(1);
         MstProductOffering mstProdOffering = new MstProductOffering();
        mstProdOffering.setProductName(OrderDetailsExcelDownloadConstants.BUY_DUAL_UNMANAGED_GVPN);
         sol.setMstProductOffering(mstProdOffering);
         orderIllSite.setOrderProductSolution(sol);

         return orderIllSite;
  }
  
  
  
  public OrderIllSite getOrderGvpnSiteDualManaged() {
         OrderIllSite orderIllSite = new OrderIllSite();
         orderIllSite.setErfLocSiteaLocationId(1);
         orderIllSite.setRequestorDate(new Date());
         orderIllSite.setId(1);
         orderIllSite.setCreatedBy(1);
         orderIllSite.setCreatedTime(new Date());
         orderIllSite.setEffectiveDate(new Date());
         orderIllSite.setErfLocSiteaLocationId(1);
         orderIllSite.setErfLocSiteaSiteCode("code");
         orderIllSite.setFeasibility((byte) 1);
         orderIllSite.setStatus((byte) 1);
         orderIllSite.setStage("PROVISION_SITES");
         orderIllSite.setSiteCode("TestCode");
         orderIllSite.setMstOrderSiteStatus(getMstOrderSiteStatus());
         orderIllSite.setMstOrderSiteStage(getMstOrderSiteStage());
         orderIllSite.setOrderSiteFeasibility(getSiteFeasiblitySet());
         OrderProductSolution sol = new OrderProductSolution();
         sol.setId(1);
         MstProductOffering mstProdOffering = new MstProductOffering();
         mstProdOffering.setProductName(OrderDetailsExcelDownloadConstants.BUY_DUAL_MANAGED_GVPN);
         sol.setMstProductOffering(mstProdOffering);
         orderIllSite.setOrderProductSolution(sol);

         return orderIllSite;
  }
  
  
  
  /**
  * getOrder with object
  * 
   * @return orders
  */
  public Order getGvpnOrder() {
         Order orders = new Order();

         Timestamp timeStamp = new Timestamp(0);

         orders.setCreatedBy(1);
         orders.setCreatedTime(timeStamp);
         orders.setCustomer(getCustomer());
         orders.setEffectiveDate(timeStamp);
         orders.setEndDate(timeStamp);
         orders.setId(1);
         Set<OrderToLe> orderToLes = new HashSet<>();
         orderToLes.add(getGvpnOrderToLes());
         orders.setOrderToLes(orderToLes);
         orders.setQuote(getQuote());
         orders.setStage("xyz");
         orders.setStartDate(timeStamp);
         orders.setStatus((byte) 1);

         return orders;
  }
  
  public OrderToLe getGvpnOrderToLes() {
         OrderToLe orderToLe = new OrderToLe();
         Set set = new HashSet<>();
         set.add(getorderToLeProductFamilies1());

         orderToLe.setId(1);
         orderToLe.setCurrencyId(1);
         orderToLe.setErfCusCustomerLegalEntityId(1);
         orderToLe.setErfCusSpLegalEntityId(1);
         orderToLe.setOrderToLeProductFamilies(set);
         orderToLe.setStage("stage");
         orderToLe.setFinalMrc(1.0);
         orderToLe.setFinalNrc(2.0);
  orderToLe.setOrdersLeAttributeValues(returnOrdersLeAttributeValueList().stream().collect(Collectors.toSet()));
         
         Order order = new Order();
         order.setOrderCode("order code");
         
         orderToLe.setOrder(order);
         
         return orderToLe;
  }
  
  
  /**
  * geProductFamily -mock values
  * 
   * @return {@link MstProductFamily}
  */
  public MstProductFamily getGvpnProductFamily() {
         MstProductFamily mstProductFamily = new MstProductFamily();
         mstProductFamily.setId(1);
         mstProductFamily.setName("Cpe");
         mstProductFamily.setStatus((byte) 0);
         mstProductFamily.setEngagements(geEngagementSet());
         mstProductFamily.setQuoteToLeProductFamilies(getQuoteFamilesWithoutProductSolutions());
         // mstProductFamily.setMstProductOfferings(geMstProductOfferingSet());
         // mstProductFamily.setOrderPrices(getOrderPriceSet());
         // mstProductFamily.setOrderProductComponents(getOrderProductComponentSet());
         // mstProductFamily.setOrderToLeProductFamilies(createOrderToLeProductFamilySet());
         return mstProductFamily;
  }
  
  
  
  public OrderToLeProductFamily getGvpnOrderToLeProductFamilies() {
         OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
         orderToLeProductFamily.setId(1);
         orderToLeProductFamily.setMstProductFamily(getMstProductFamily());
         Set<OrderProductSolution> set = new HashSet<>();
         set.add(getOrderProductSolution());
         orderToLeProductFamily.setOrderProductSolutions(set);
         orderToLeProductFamily.setOrderToLe(getOrderToLes1());
         return orderToLeProductFamily;
  }
  
  
  public List<OrderProductSolution> getGvpnOrderProductSolutionList() {
         List<OrderProductSolution> list = new ArrayList<>();
         list.add(getGvpnOrderProductSolution());
         return list;
  }
  
  
  public OrderProductSolution getGvpnOrderProductSolution() {

         OrderProductSolution orderProductSolution = new OrderProductSolution();
         orderProductSolution.setId(1);
         orderProductSolution.setMstProductOffering(getMstOffering());
         Set set = new HashSet<>();
         set.add(getOrderGvpnSiteList());
         orderProductSolution.setOrderIllSites(set);
         Order order = new Order();
         order.setCreatedBy(1);
         OrderToLe ole = new OrderToLe();
         ole.setOrder(order);
         OrderToLeProductFamily fam = new OrderToLeProductFamily();
         fam.setId(1);
         fam.setOrderToLe(ole);
         orderProductSolution.setOrderToLeProductFamily(fam);
         return orderProductSolution;
  }
  
  

  public List<OrderIllSite> getOrderGvpnSiteList() {
         List<OrderIllSite> list = new ArrayList<>();
         list.add(getOrderGvpnSiteSingleManaged());
         list.add(getOrderGvpnSiteSingleUnmanaged());
         list.add(getOrderGvpnSiteDualManaged());
         list.add(getOrderGvpnSiteDualUnmanaged());
         return list;
  }

  public List<OrderProductComponent> getGvpnOrderProductComponentList() {
         List<OrderProductComponent> list = new ArrayList<>();
         list.addAll(getGvpnOrderProductComponentListEach());
  //     list.addAll(getGvpnOrderProductComponentListForSite());

         return list;
  }

  public List<OrderProductComponent> getGvpnOrderProductComponentListEach() {
         List<OrderProductComponent> list = new ArrayList<>();
         list.add(getGvpnOrderProductComponentNationalConnectivity());
         list.add(getGvpnOrderProductComponentNationalConnectivity());
         //list.add(getOrderProductComponentLinkManagement());
         return list;
  }

  
/*     public List<OrderProductComponent> getGvpnOrderProductComponentListForSite() {
         List<OrderProductComponent> list = new ArrayList<>();
         list.add(getOrderProductComponentLastMileSiteA());
         list.add(getOrderProductComponentLastMileSiteB());

         return list;
  }
*/
  
  public OrderProductComponent getGvpnOrderProductComponentNationalConnectivity() {
         OrderProductComponent orderProductComponent = new OrderProductComponent();
         orderProductComponent.setId(1);
         orderProductComponent.setType("Primary");
         orderProductComponent.setMstProductComponent(getNationalConnectivity());
         Set<OrderProductComponentsAttributeValue> set = new HashSet<>();
         set.add(createInterfaceType("Interface Type - A end"));
         set.add(createInterfaceType("Interface Type - B end"));

         orderProductComponent.setOrderProductComponentsAttributeValues(set);
         return orderProductComponent;

  }
  
  public OrderProductComponent getGvpnOrderProductComponentNationalConnectivitySecondary() {
         OrderProductComponent orderProductComponent = new OrderProductComponent();
         orderProductComponent.setId(1);
         orderProductComponent.setType("Secondary");
         orderProductComponent.setMstProductComponent(getNationalConnectivity());
         Set<OrderProductComponentsAttributeValue> set = new HashSet<>();
         set.add(createInterfaceType("Interface Type - A end"));
         set.add(createInterfaceType("Interface Type - B end"));

         orderProductComponent.setOrderProductComponentsAttributeValues(set);
         return orderProductComponent;

  }
  
  
  public List<OrderSiteFeasibility> getOrderGvpnSiteFeasiblity() {
         List<OrderSiteFeasibility> feasiblityList = new ArrayList<>();
         OrderSiteFeasibility feasiblity = new OrderSiteFeasibility();
         feasiblity.setId(1);
         feasiblity.setOrderIllSite(getOrderIllSite());
         feasiblity.setCreatedTime(new Timestamp(0));
         feasiblity.setFeasibilityCheck("Test");
         feasiblity.setFeasibilityMode("Test");
         feasiblity.setIsSelected((byte) 1);
         feasiblity.setProvider("Test");
         feasiblity.setRank(1);
         feasiblity.setResponseJson("{\"test\":\"sample\"}");
         feasiblity.setType("Type");
         feasiblityList.add(feasiblity);
         return feasiblityList;
  }
  
  
  public List<OrderIllSite> getGvpnOrderSiteSla() {

         List<OrderIllSite> list = new ArrayList<>();
         OrderIllSite site = new OrderIllSite();
         site.setStatus((byte) 1);
         site.setFeasibility((byte) 1);
         site.setOrderIllSiteSlas(createOrderIllSiteSlaSet1());
         site.setId(1);
         list.add(site);
         return list;

  }
  
  

  public List<OrderIllSiteSla> getGvpnOrderSiteSla1() {
         List<OrderIllSiteSla> list = new ArrayList<>();
         OrderIllSiteSla site = new OrderIllSiteSla();
         SlaMaster slaMaster = new SlaMaster();
         slaMaster.setSlaName("\"Service Availability %\"");
         site.setSlaMaster(slaMaster);
         site.setOrderIllSite(getOrderIllSite1());
         site.setId(1);
         list.add(site);
         return list;

  }
  
  
  public List<OrderIllSiteSla> createOrderIllSiteSlaSetList1() {
         List<OrderIllSiteSla> setAttri = new ArrayList<>();
         setAttri.add(createQuoteIllSiteSlaWithoutSlaMasterSA());
         setAttri.add(createQuoteIllSiteSlaWithoutSlaMasterMTR());
         setAttri.add(createQuoteIllSiteSlaWithoutSlaMasterTTR());
         setAttri.add(createQuoteIllSiteSlaWithoutSlaMasterJSLT());
         return setAttri;
  }
  
  
  
  public Set<OrderIllSiteSla> createOrderIllSiteSlaSet1() {
         Set<OrderIllSiteSla> setAttri = new HashSet<>();
         setAttri.add(createQuoteIllSiteSlaWithoutSlaMasterSA());
         setAttri.add(createQuoteIllSiteSlaWithoutSlaMasterMTR());
         setAttri.add(createQuoteIllSiteSlaWithoutSlaMasterTTR());
         setAttri.add(createQuoteIllSiteSlaWithoutSlaMasterJSLT());
         return setAttri;
  }
  
  
  public List<OrderIllSiteSla> createOrderIllSiteSlaSetList() {
         List<OrderIllSiteSla> setAttri = new ArrayList<>();
         setAttri.add(createQuoteIllSiteSlaWithoutSlaMasterSA());
         setAttri.add(createQuoteIllSiteSlaWithoutSlaMasterMTR());
         setAttri.add(createQuoteIllSiteSlaWithoutSlaMasterTTR());
         setAttri.add(createQuoteIllSiteSlaWithoutSlaMasterJSLT());
         return setAttri;
  }
  
  
  public OrderIllSiteSla createQuoteIllSiteSlaWithoutSlaMasterSA() {
         OrderIllSiteSla leProductSla = new OrderIllSiteSla();
         leProductSla.setId(1);
         SlaMaster slaMaster = new SlaMaster();
         slaMaster.setSlaName("Service Availability %");
         leProductSla.setSlaValue("99.5");
         leProductSla.setSlaMaster(slaMaster);
         leProductSla.setOrderIllSite(getOrderIllSite1());
         return leProductSla;
  }
  
  public OrderIllSiteSla createQuoteIllSiteSlaWithoutSlaMasterMTR() {
         OrderIllSiteSla leProductSla = new OrderIllSiteSla();
         leProductSla.setId(1);
         SlaMaster slaMaster = new SlaMaster();
         slaMaster.setSlaName("Mean Time To Restore (MTTR) in Hrs");
         leProductSla.setSlaValue("99.5");
         leProductSla.setSlaMaster(slaMaster);
         leProductSla.setOrderIllSite(getOrderIllSite1());
         return leProductSla;
  }
  
  
  public OrderIllSiteSla createQuoteIllSiteSlaWithoutSlaMasterTTR() {
         OrderIllSiteSla leProductSla = new OrderIllSiteSla();
         leProductSla.setId(1);
         SlaMaster slaMaster = new SlaMaster();
         slaMaster.setSlaName("Time To Restore (TTR) in Hrs");
         leProductSla.setSlaValue("99.5");
         leProductSla.setSlaMaster(slaMaster);
         leProductSla.setOrderIllSite(getOrderIllSite1());
         return leProductSla;
  }
  
  public OrderIllSiteSla createQuoteIllSiteSlaWithoutSlaMasterJSLT() {
         OrderIllSiteSla leProductSla = new OrderIllSiteSla();
         leProductSla.setId(1);
         SlaMaster slaMaster = new SlaMaster();
         slaMaster.setSlaName("Jitter Servicer Level Target (ms)");
         leProductSla.setSlaValue("99.5");
         leProductSla.setSlaMaster(slaMaster);
         leProductSla.setOrderIllSite(getOrderIllSite1());
         return leProductSla;
  }
  
  
  
  public OrderIllSite getOrderIllSite1()

  {
         OrderIllSite qIs = new OrderIllSite();
         qIs.setId(1);
         qIs.setStatus((byte) 1);
         qIs.setErfLocSitebLocationId(2);
         qIs.setFeasibility((byte) 1);
         return qIs;

  }
  
  
  public String getLocationQueueResponse() {
         String response ="{\"locationId\":\"1\",\r\n" + 
                      "\"customerId\":\"1\",\r\n" + 
                      "\"popId\":\"1\",\r\n" + 
                      "\"tier\":\"1\",\r\n" + 
                      "\"userAddress\":{\"addressId\":\"1\",\r\n" + 
                      "\"addressLineOne\":\"test\",\r\n" + 
                      "\"addressLineTwo\":\"test\",\r\n" + 
                      "\"city\":\"chennai\",\r\n" + 
                      "\"country\":\"india\",\r\n" + 
                      "\"locality\":\"guindy\",\r\n" + 
                      "\"pincode\":\"600000\",\r\n" + 
                      "\"plotBuilding\":\"74\",\r\n" + 
                      "\"source\":\"api\",\r\n" + 
                      "\"state\":\"tamilnadu\",\r\n" + 
                      "\"latLong\":\"1829, 19272\"\r\n" + 
                      "},\r\n" + 
                      "\"apiAddress\":{\"addressId\":\"1\",\r\n" + 
                      "\"addressLineOne\":\"test\",\r\n" + 
                      "\"addressLineTwo\":\"test\",\r\n" + 
                      "\"city\":\"chennai\",\r\n" + 
                      "\"country\":\"india\",\r\n" + 
                      "\"locality\":\"guindy\",\r\n" + 
                      "\"pincode\":\"600000\",\r\n" + 
                      "\"plotBuilding\":\"74\",\r\n" + 
                      "\"source\":\"api\",\r\n" + 
                      "\"state\":\"tamilnadu\",\r\n" + 
                      "\"latLong\":\"1829, 19272\"\r\n" + 
                      "},\r\n" + 
                      "\"erfCusCustomerLeId\":\"1\",\r\n" + 
                      "\"address\":{\"addressId\":\"1\",\r\n" + 
                      "\"addressLineOne\":\"test\",\r\n" + 
                      "\"addressLineTwo\":\"test\",\r\n" + 
                      "\"city\":\"chennai\",\r\n" + 
                      "\"country\":\"india\",\r\n" + 
                      "\"locality\":\"guindy\",\r\n" + 
                      "\"pincode\":\"600000\",\r\n" + 
                      "\"plotBuilding\":\"74\",\r\n" + 
                      "\"source\":\"api\",\r\n" + 
                      "\"state\":\"tamilnadu\",\r\n" + 
                      "\"latLong\":\"1829, 19272\"\r\n" + 
                      "},\r\n" + 
                      "\"latLong\":\"1829, 19272\"\r\n" + 
                      "}";
         return response;
  }
  
  public OrderIllSite getOrderGvpnSite() {
      OrderIllSite orderIllSite = new OrderIllSite();
      orderIllSite.setErfLocSiteaLocationId(1);
      orderIllSite.setRequestorDate(new Date());
      orderIllSite.setId(2);
      orderIllSite.setCreatedBy(1);
      orderIllSite.setCreatedTime(new Date());
      orderIllSite.setEffectiveDate(new Date());
      orderIllSite.setErfLocSiteaLocationId(1);
      orderIllSite.setErfLocSiteaSiteCode("code");
      orderIllSite.setFeasibility((byte) 1);
      orderIllSite.setStatus((byte) 1);
      orderIllSite.setStage("PROVISION_SITES");
      orderIllSite.setSiteCode("TestCode");
      orderIllSite.setMstOrderSiteStatus(getMstOrderSiteStatus());
      orderIllSite.setMstOrderSiteStage(getMstOrderSiteStage());
      orderIllSite.setOrderSiteFeasibility(getSiteFeasiblitySet());

      return orderIllSite;
}
	
  /**
   * getOrder with object
   * 
    * @return orders
   */
   public Order getGvpnOrderExcel() {
          Order orders = new Order();

          Timestamp timeStamp = new Timestamp(0);

          orders.setCreatedBy(1);
          orders.setCreatedTime(timeStamp);
          orders.setCustomer(getCustomer());
          orders.setEffectiveDate(timeStamp);
          orders.setEndDate(timeStamp);
          orders.setId(1);
          orders.setOrderCode("oc");
          Set<OrderToLe> orderToLes = new HashSet<>();
          OrderToLe orderToLe = new OrderToLe();
          orderToLes.add(getOrderToLes2());
          orders.setOrderToLes(orderToLes);
          orders.setQuote(getQuote());
          orders.setStage("xyz");
          orders.setStartDate(timeStamp);
          orders.setStatus((byte) 1);
          return orders;
   }
   
   
   public List<OrderToLe> getOrderToLesListExcel() {
          List<OrderToLe> list = new ArrayList<>();
          list.add(getOrderToLesExcel());
          list.add(getOrderToLesExcel());
          list.add(getOrderToLesExcel());
          return list;
   }
   
   
   @SuppressWarnings("unchecked")
   public OrderToLe getOrderToLesExcel() {
          OrderToLe orderToLe = new OrderToLe();
          Set set = new HashSet<>();
          set.add(getorderToLeProductFamiliesExcel());
          orderToLe.setId(1);
          orderToLe.setCurrencyId(1);
          orderToLe.setErfCusCustomerLegalEntityId(1);
          orderToLe.setErfCusSpLegalEntityId(1);
          orderToLe.setStage("ORDER_COMPLETED");
          orderToLe.setFinalMrc(1.0);
          orderToLe.setFinalNrc(2.0);

          orderToLe.setOrder(getOrder());
          Set<OrdersLeAttributeValue> setAttri = new HashSet<>();
          setAttri.add(getOrdersLeAttributeValue());
          orderToLe.setOrdersLeAttributeValues(setAttri);
          orderToLe.setOrderToLeProductFamilies(set);
          return orderToLe;
   }

   
   public OrderToLeProductFamily getorderToLeProductFamiliesExcel() {
          OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
          orderToLeProductFamily.setId(1);
          orderToLeProductFamily.setMstProductFamily(getMstProductFamily());
          Set<OrderProductSolution> set = new HashSet<>();
          set.add(getOrderProductSolution());
          orderToLeProductFamily.setOrderProductSolutions(set);
          orderToLeProductFamily.setOrderToLe(getOrderToLes1());
          return orderToLeProductFamily;
   }
   
   
   /**
   * getOrder with object
   * 
    * @return orders
   */
   public Order getOrderExcel() {
          Order orders = new Order();

          Timestamp timeStamp = new Timestamp(0);

          orders.setCreatedBy(1);
          orders.setCreatedTime(timeStamp);
          orders.setCustomer(getCustomer());
          orders.setEffectiveDate(timeStamp);
          orders.setEndDate(timeStamp);
          orders.setId(1);
          Set<OrderToLe> orderToLes = new HashSet<>();
          orderToLes.add(getOrderToLesExcel());
          orders.setOrderToLes(orderToLes);
          orders.setQuote(getQuote());
          orders.setStage("xyz");
          orders.setStartDate(timeStamp);
          orders.setStatus((byte) 1);

          return orders;
   }
   
   /**
	 * getComponentsBean
	 * 
	 * @return
	 * @throws TclCommonException 
	 */
	private List<OrderProductComponentBean> getComponentsBean1() throws TclCommonException {
		List<OrderProductComponentBean> list = new ArrayList<>();
		String json = "[{\"componentId\":33816,\"componentMasterId\":12,\"referenceId\":5390,\"name\":\"VPN Port\",\"type\":\"primary\",\"attributes\":[{\"attributeId\":240715,\"attributeMasterId\":1,\"attributeValues\":\"Fast Ethernet\",\"description\":\"Interface\",\"name\":\"Interface\"},{\"attributeId\":240716,\"attributeMasterId\":2,\"attributeValues\":\"BGP\",\"description\":\"Routing Protocol\",\"name\":\"Routing Protocol\"},{\"attributeId\":240717,\"attributeMasterId\":3,\"attributeValues\":\"10\",\"description\":\"Port Bandwidth\",\"name\":\"Port Bandwidth\"},{\"attributeId\":240718,\"attributeMasterId\":78,\"attributeValues\":\"TCL\",\"description\":\"WAN IP Provided By\",\"name\":\"WAN IP Provided By\"},{\"attributeId\":240719,\"attributeMasterId\":79,\"attributeValues\":\"\",\"description\":\"WAN IP Address\",\"name\":\"WAN IP Address\"},{\"attributeId\":240720,\"attributeMasterId\":4,\"attributeValues\":\"Fixed\",\"description\":\"Service type\",\"name\":\"Service type\"},{\"attributeId\":240721,\"attributeMasterId\":5,\"attributeValues\":\"\",\"description\":\"Burstable Bandwidth\",\"name\":\"Burstable Bandwidth\"},{\"attributeId\":240722,\"attributeMasterId\":6,\"attributeValues\":\"\",\"description\":\"Usage Model\",\"name\":\"Usage Model\"},{\"attributeId\":240723,\"attributeMasterId\":10,\"attributeValues\":\"No\",\"description\":\"Extended LAN Required?\",\"name\":\"Extended LAN Required?\"},{\"attributeId\":240724,\"attributeMasterId\":11,\"attributeValues\":\"No\",\"description\":\"BFD Required\",\"name\":\"BFD Required\"},{\"attributeId\":240725,\"attributeMasterId\":12,\"attributeValues\":\"Loopback\",\"description\":\"BGP Peering on\",\"name\":\"BGP Peering on\"},{\"attributeId\":240726,\"attributeMasterId\":75,\"attributeValues\":\"\",\"description\":\"BGP AS Number\",\"name\":\"BGP AS Number\"},{\"attributeId\":240727,\"attributeMasterId\":76,\"attributeValues\":\"\",\"description\":\"Customer prefixes\",\"name\":\"Customer prefixes\"}],\"price\":{\"id\":6295,\"effectiveMrc\":19527.880859375,\"effectiveNrc\":10000.0,\"effectiveArc\":234334.625,\"quoteId\":2018,\"referenceId\":\"33816\",\"referenceName\":\"COMPONENTS\"}},\r\n"
				+ "{\r\n" + "  \"componentId\": 33816,\r\n" + "  \"componentMasterId\": 12,\r\n"
				+ "  \"referenceId\": 5390,\r\n" + "  \"name\": \"CPE\",\r\n" + "  \"type\": \"primary\",\r\n"
				+ "  \"attributes\": [\r\n" + "   \r\n" + "    {\r\n" + "      \"attributeId\": 240727,\r\n"
				+ "      \"attributeMasterId\": 76,\r\n" + "      \"attributeValues\": \"CPE Basic Chassis\",\r\n"
				+ "      \"description\": \"CPE Basic Chassis\",\r\n" + "      \"name\": \"CPE Basic Chassis\"\r\n"
				+ "    }\r\n" + "  ],\r\n" + "  \"price\": {\r\n" + "    \"id\": 6295,\r\n"
				+ "    \"effectiveMrc\": 19527.880859375,\r\n" + "    \"effectiveNrc\": 10000,\r\n"
				+ "    \"effectiveArc\": 234334.625,\r\n" + "    \"quoteId\": 2018,\r\n"
				+ "    \"referenceId\": \"33816\",\r\n" + "    \"referenceName\": \"COMPONENTS\"\r\n" + "  }\r\n"
				+ "},{\"componentId\":33817,\"componentMasterId\":2,\"referenceId\":5390,\"name\":\"Last mile\",\"type\":\"primary\",\"attributes\":[{\"attributeId\":240728,\"attributeMasterId\":13,\"attributeValues\":\"No\",\"description\":\"Shared Last Mile\",\"name\":\"Shared Last Mile\"},{\"attributeId\":240729,\"attributeMasterId\":14,\"attributeValues\":\"\",\"description\":\"Shared Last Mile Service ID\",\"name\":\"Shared Last Mile Service ID\"},{\"attributeId\":240730,\"attributeMasterId\":15,\"attributeValues\":\"10\",\"description\":\"Local Loop Bandwidth\",\"name\":\"Local Loop Bandwidth\"}],\"price\":{\"id\":6296,\"effectiveMrc\":14981.669921875,\"effectiveNrc\":0.0,\"effectiveArc\":179780.0,\"quoteId\":2018,\"referenceId\":\"33817\",\"referenceName\":\"COMPONENTS\"}},{\"componentId\":33818,\"componentMasterId\":3,\"referenceId\":5390,\"name\":\"CPE Management\",\"type\":\"primary\",\"attributes\":[{\"attributeId\":240731,\"attributeMasterId\":16,\"attributeValues\":\"Unmanaged\",\"description\":\"CPE Management Type\",\"name\":\"CPE Management Type\"},{\"attributeId\":240732,\"attributeMasterId\":17,\"attributeValues\":\"false\",\"description\":\"CPE Management\",\"name\":\"CPE Management\"}]},{\"componentId\":33819,\"componentMasterId\":10,\"referenceId\":5390,\"name\":\"GVPN Common\",\"type\":\"primary\",\"attributes\":[{\"attributeId\":240733,\"attributeMasterId\":18,\"attributeValues\":\"Enhanced\",\"description\":\"Service Variant\",\"name\":\"Service Variant\"},{\"attributeId\":240734,\"attributeMasterId\":25,\"attributeValues\":\"No\",\"description\":\"isAuthenticationRequired for protocol\",\"name\":\"isAuthenticationRequired for protocol\"},{\"attributeId\":240735,\"attributeMasterId\":26,\"attributeValues\":\"Default routes\",\"description\":\"Routes Exchanged\",\"name\":\"Routes Exchanged\"},{\"attributeId\":240736,\"attributeMasterId\":53,\"attributeValues\":\"Active\",\"description\":\"Port Mode\",\"name\":\"Port Mode\"},{\"attributeId\":240737,\"attributeMasterId\":27,\"attributeValues\":\"TCL private AS Number\",\"description\":\"AS Number\",\"name\":\"AS Number\"},{\"attributeId\":240738,\"attributeMasterId\":47,\"attributeValues\":\"0%\",\"description\":\"cos 1\",\"name\":\"cos 1\"},{\"attributeId\":240739,\"attributeMasterId\":48,\"attributeValues\":\"0%\",\"description\":\"cos 2\",\"name\":\"cos 2\"},{\"attributeId\":240740,\"attributeMasterId\":49,\"attributeValues\":\"100%\",\"description\":\"cos 3\",\"name\":\"cos 3\"},{\"attributeId\":240741,\"attributeMasterId\":50,\"attributeValues\":\"0%\",\"description\":\"cos 4\",\"name\":\"cos 4\"},{\"attributeId\":240742,\"attributeMasterId\":51,\"attributeValues\":\"0%\",\"description\":\"cos 5\",\"name\":\"cos 5\"},{\"attributeId\":240743,\"attributeMasterId\":52,\"attributeValues\":\"0%\",\"description\":\"cos 6\",\"name\":\"cos 6\"},{\"attributeId\":240744,\"attributeMasterId\":19,\"attributeValues\":\"No\",\"description\":\"Resiliency\",\"name\":\"Resiliency\"},{\"attributeId\":240745,\"attributeMasterId\":20,\"attributeValues\":\"LC\",\"description\":\"Connector Type\",\"name\":\"Connector Type\"},{\"attributeId\":240746,\"attributeMasterId\":21,\"attributeValues\":\"Yes\",\"description\":\"Access Required\",\"name\":\"Access Required\"},{\"attributeId\":240747,\"attributeMasterId\":22,\"attributeValues\":\"Customer provided\",\"description\":\"CPE\",\"name\":\"CPE\"},{\"attributeId\":240748,\"attributeMasterId\":61,\"attributeValues\":\"Full Mesh\",\"description\":\"VPN Topology\",\"name\":\"VPN Topology\"},{\"attributeId\":240749,\"attributeMasterId\":62,\"attributeValues\":\"Mesh\",\"description\":\"Site Type\",\"name\":\"Site Type\"},{\"attributeId\":240750,\"attributeMasterId\":64,\"attributeValues\":\"Unmanaged\",\"description\":\"Access Topology\",\"name\":\"Access Topology\"}]}]";
		OrderProductComponentBean[] comp = (OrderProductComponentBean[]) Utils.convertJsonToObject(json,
				OrderProductComponentBean[].class);
		for (int i = 0; i <= comp.length - 1; i++) {
			list.add(comp[i]);

		}
		list.addAll(getComponentsBeanSecon());
		return list;
	}

	private List<OrderProductComponentBean> getComponentsBeanSecon() throws TclCommonException {
		List<OrderProductComponentBean> list = new ArrayList<>();
		String json = "[\r\n" + 
				"  {\r\n" + 
				"    \"componentId\": 33816,\r\n" + 
				"    \"componentMasterId\": 12,\r\n" + 
				"    \"referenceId\": 5390,\r\n" + 
				"    \"name\": \"VPN Port\",\r\n" + 
				"    \"type\": \"secondary\",\r\n" + 
				"    \"attributes\": [\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240715,\r\n" + 
				"        \"attributeMasterId\": 1,\r\n" + 
				"        \"attributeValues\": \"Fast Ethernet\",\r\n" + 
				"        \"description\": \"Interface\",\r\n" + 
				"        \"name\": \"Interface\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240716,\r\n" + 
				"        \"attributeMasterId\": 2,\r\n" + 
				"        \"attributeValues\": \"BGP\",\r\n" + 
				"        \"description\": \"Routing Protocol\",\r\n" + 
				"        \"name\": \"Routing Protocol\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240717,\r\n" + 
				"        \"attributeMasterId\": 3,\r\n" + 
				"        \"attributeValues\": \"10\",\r\n" + 
				"        \"description\": \"Port Bandwidth\",\r\n" + 
				"        \"name\": \"Port Bandwidth\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240718,\r\n" + 
				"        \"attributeMasterId\": 78,\r\n" + 
				"        \"attributeValues\": \"TCL\",\r\n" + 
				"        \"description\": \"WAN IP Provided By\",\r\n" + 
				"        \"name\": \"WAN IP Provided By\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240719,\r\n" + 
				"        \"attributeMasterId\": 79,\r\n" + 
				"        \"attributeValues\": \"\",\r\n" + 
				"        \"description\": \"WAN IP Address\",\r\n" + 
				"        \"name\": \"WAN IP Address\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240720,\r\n" + 
				"        \"attributeMasterId\": 4,\r\n" + 
				"        \"attributeValues\": \"Fixed\",\r\n" + 
				"        \"description\": \"Service type\",\r\n" + 
				"        \"name\": \"Service type\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240721,\r\n" + 
				"        \"attributeMasterId\": 5,\r\n" + 
				"        \"attributeValues\": \"\",\r\n" + 
				"        \"description\": \"Burstable Bandwidth\",\r\n" + 
				"        \"name\": \"Burstable Bandwidth\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240722,\r\n" + 
				"        \"attributeMasterId\": 6,\r\n" + 
				"        \"attributeValues\": \"\",\r\n" + 
				"        \"description\": \"Usage Model\",\r\n" + 
				"        \"name\": \"Usage Model\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240723,\r\n" + 
				"        \"attributeMasterId\": 10,\r\n" + 
				"        \"attributeValues\": \"No\",\r\n" + 
				"        \"description\": \"Extended LAN Required?\",\r\n" + 
				"        \"name\": \"Extended LAN Required?\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240724,\r\n" + 
				"        \"attributeMasterId\": 11,\r\n" + 
				"        \"attributeValues\": \"No\",\r\n" + 
				"        \"description\": \"BFD Required\",\r\n" + 
				"        \"name\": \"BFD Required\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240725,\r\n" + 
				"        \"attributeMasterId\": 12,\r\n" + 
				"        \"attributeValues\": \"Loopback\",\r\n" + 
				"        \"description\": \"BGP Peering on\",\r\n" + 
				"        \"name\": \"BGP Peering on\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240726,\r\n" + 
				"        \"attributeMasterId\": 75,\r\n" + 
				"        \"attributeValues\": \"\",\r\n" + 
				"        \"description\": \"BGP AS Number\",\r\n" + 
				"        \"name\": \"BGP AS Number\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240727,\r\n" + 
				"        \"attributeMasterId\": 76,\r\n" + 
				"        \"attributeValues\": \"\",\r\n" + 
				"        \"description\": \"Customer prefixes\",\r\n" + 
				"        \"name\": \"Customer prefixes\"\r\n" + 
				"      }\r\n" + 
				"    ],\r\n" + 
				"    \"price\": {\r\n" + 
				"      \"id\": 6295,\r\n" + 
				"      \"effectiveMrc\": 19527.880859375,\r\n" + 
				"      \"effectiveNrc\": 10000,\r\n" + 
				"      \"effectiveArc\": 234334.625,\r\n" + 
				"      \"quoteId\": 2018,\r\n" + 
				"      \"referenceId\": \"33816\",\r\n" + 
				"      \"referenceName\": \"COMPONENTS\"\r\n" + 
				"    }\r\n" + 
				"  },\r\n" + 
				"  {\r\n" + 
				"    \"componentId\": 33816,\r\n" + 
				"    \"componentMasterId\": 12,\r\n" + 
				"    \"referenceId\": 5390,\r\n" + 
				"    \"name\": \"CPE\",\r\n" + 
				"    \"type\": \"secondary\",\r\n" + 
				"    \"attributes\": [\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240727,\r\n" + 
				"        \"attributeMasterId\": 76,\r\n" + 
				"        \"attributeValues\": \"CPE Basic Chassis\",\r\n" + 
				"        \"description\": \"CPE Basic Chassis\",\r\n" + 
				"        \"name\": \"CPE Basic Chassis\"\r\n" + 
				"      }\r\n" + 
				"    ],\r\n" + 
				"    \"price\": {\r\n" + 
				"      \"id\": 6295,\r\n" + 
				"      \"effectiveMrc\": 19527.880859375,\r\n" + 
				"      \"effectiveNrc\": 10000,\r\n" + 
				"      \"effectiveArc\": 234334.625,\r\n" + 
				"      \"quoteId\": 2018,\r\n" + 
				"      \"referenceId\": \"33816\",\r\n" + 
				"      \"referenceName\": \"COMPONENTS\"\r\n" + 
				"    }\r\n" + 
				"  },\r\n" + 
				"  {\r\n" + 
				"    \"componentId\": 33816,\r\n" + 
				"    \"componentMasterId\": 12,\r\n" + 
				"    \"referenceId\": 5390,\r\n" + 
				"    \"name\": \"Addon\",\r\n" + 
				"    \"type\": \"secondary\",\r\n" + 
				"    \"attributes\": [\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240727,\r\n" + 
				"        \"attributeMasterId\": 76,\r\n" + 
				"        \"attributeValues\": \"CPE Basic Chassis\",\r\n" + 
				"        \"description\": \"CPE Basic Chassis\",\r\n" + 
				"        \"name\": \"DNS\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240727,\r\n" + 
				"        \"attributeMasterId\": 76,\r\n" + 
				"        \"attributeValues\": \"CPE Basic Chassis\",\r\n" + 
				"        \"description\": \"CPE Basic Chassis\",\r\n" + 
				"        \"name\": \"Additional IPs\"\r\n" + 
				"      }\r\n" + 
				"    ],\r\n" + 
				"    \"price\": {\r\n" + 
				"      \"id\": 6295,\r\n" + 
				"      \"effectiveMrc\": 19527.880859375,\r\n" + 
				"      \"effectiveNrc\": 10000,\r\n" + 
				"      \"effectiveArc\": 234334.625,\r\n" + 
				"      \"quoteId\": 2018,\r\n" + 
				"      \"referenceId\": \"33816\",\r\n" + 
				"      \"referenceName\": \"COMPONENTS\"\r\n" + 
				"    }\r\n" + 
				"  },\r\n" + 
				"  {\r\n" + 
				"    \"componentId\": 33817,\r\n" + 
				"    \"componentMasterId\": 2,\r\n" + 
				"    \"referenceId\": 5390,\r\n" + 
				"    \"name\": \"Last mile\",\r\n" + 
				"    \"type\": \"secondary\",\r\n" + 
				"    \"attributes\": [\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240728,\r\n" + 
				"        \"attributeMasterId\": 13,\r\n" + 
				"        \"attributeValues\": \"No\",\r\n" + 
				"        \"description\": \"Shared Last Mile\",\r\n" + 
				"        \"name\": \"Shared Last Mile\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240729,\r\n" + 
				"        \"attributeMasterId\": 14,\r\n" + 
				"        \"attributeValues\": \"\",\r\n" + 
				"        \"description\": \"Shared Last Mile Service ID\",\r\n" + 
				"        \"name\": \"Shared Last Mile Service ID\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240730,\r\n" + 
				"        \"attributeMasterId\": 15,\r\n" + 
				"        \"attributeValues\": \"10\",\r\n" + 
				"        \"description\": \"Local Loop Bandwidth\",\r\n" + 
				"        \"name\": \"Local Loop Bandwidth\"\r\n" + 
				"      }\r\n" + 
				"    ],\r\n" + 
				"    \"price\": {\r\n" + 
				"      \"id\": 6296,\r\n" + 
				"      \"effectiveMrc\": 14981.669921875,\r\n" + 
				"      \"effectiveNrc\": 0,\r\n" + 
				"      \"effectiveArc\": 179780,\r\n" + 
				"      \"quoteId\": 2018,\r\n" + 
				"      \"referenceId\": \"33817\",\r\n" + 
				"      \"referenceName\": \"COMPONENTS\"\r\n" + 
				"    }\r\n" + 
				"  },\r\n" + 
				"  {\r\n" + 
				"    \"componentId\": 33818,\r\n" + 
				"    \"componentMasterId\": 3,\r\n" + 
				"    \"referenceId\": 5390,\r\n" + 
				"    \"name\": \"CPE Management\",\r\n" + 
				"    \"type\": \"secondary\",\r\n" + 
				"    \"attributes\": [\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240731,\r\n" + 
				"        \"attributeMasterId\": 16,\r\n" + 
				"        \"attributeValues\": \"Unmanaged\",\r\n" + 
				"        \"description\": \"CPE Management Type\",\r\n" + 
				"        \"name\": \"CPE Management Type\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240732,\r\n" + 
				"        \"attributeMasterId\": 17,\r\n" + 
				"        \"attributeValues\": \"false\",\r\n" + 
				"        \"description\": \"CPE Management\",\r\n" + 
				"        \"name\": \"CPE Management\"\r\n" + 
				"      }\r\n" + 
				"    ]\r\n" + 
				"  },\r\n" + 
				"  {\r\n" + 
				"    \"componentId\": 33819,\r\n" + 
				"    \"componentMasterId\": 10,\r\n" + 
				"    \"referenceId\": 5390,\r\n" + 
				"    \"name\": \"GVPN Common\",\r\n" + 
				"    \"type\": \"secondary\",\r\n" + 
				"    \"attributes\": [\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240733,\r\n" + 
				"        \"attributeMasterId\": 18,\r\n" + 
				"        \"attributeValues\": \"Enhanced\",\r\n" + 
				"        \"description\": \"Service Variant\",\r\n" + 
				"        \"name\": \"Service Variant\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240734,\r\n" + 
				"        \"attributeMasterId\": 25,\r\n" + 
				"        \"attributeValues\": \"No\",\r\n" + 
				"        \"description\": \"isAuthenticationRequired for protocol\",\r\n" + 
				"        \"name\": \"isAuthenticationRequired for protocol\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240735,\r\n" + 
				"        \"attributeMasterId\": 26,\r\n" + 
				"        \"attributeValues\": \"Default routes\",\r\n" + 
				"        \"description\": \"Routes Exchanged\",\r\n" + 
				"        \"name\": \"Routes Exchanged\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240736,\r\n" + 
				"        \"attributeMasterId\": 53,\r\n" + 
				"        \"attributeValues\": \"Active\",\r\n" + 
				"        \"description\": \"Port Mode\",\r\n" + 
				"        \"name\": \"Port Mode\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240737,\r\n" + 
				"        \"attributeMasterId\": 27,\r\n" + 
				"        \"attributeValues\": \"TCL private AS Number\",\r\n" + 
				"        \"description\": \"AS Number\",\r\n" + 
				"        \"name\": \"AS Number\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240738,\r\n" + 
				"        \"attributeMasterId\": 47,\r\n" + 
				"        \"attributeValues\": \"0%\",\r\n" + 
				"        \"description\": \"cos 1\",\r\n" + 
				"        \"name\": \"cos 1\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240739,\r\n" + 
				"        \"attributeMasterId\": 48,\r\n" + 
				"        \"attributeValues\": \"0%\",\r\n" + 
				"        \"description\": \"cos 2\",\r\n" + 
				"        \"name\": \"cos 2\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240740,\r\n" + 
				"        \"attributeMasterId\": 49,\r\n" + 
				"        \"attributeValues\": \"100%\",\r\n" + 
				"        \"description\": \"cos 3\",\r\n" + 
				"        \"name\": \"cos 3\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240741,\r\n" + 
				"        \"attributeMasterId\": 50,\r\n" + 
				"        \"attributeValues\": \"0%\",\r\n" + 
				"        \"description\": \"cos 4\",\r\n" + 
				"        \"name\": \"cos 4\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240742,\r\n" + 
				"        \"attributeMasterId\": 51,\r\n" + 
				"        \"attributeValues\": \"0%\",\r\n" + 
				"        \"description\": \"cos 5\",\r\n" + 
				"        \"name\": \"cos 5\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240743,\r\n" + 
				"        \"attributeMasterId\": 52,\r\n" + 
				"        \"attributeValues\": \"0%\",\r\n" + 
				"        \"description\": \"cos 6\",\r\n" + 
				"        \"name\": \"cos 6\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240744,\r\n" + 
				"        \"attributeMasterId\": 19,\r\n" + 
				"        \"attributeValues\": \"No\",\r\n" + 
				"        \"description\": \"Resiliency\",\r\n" + 
				"        \"name\": \"Resiliency\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240745,\r\n" + 
				"        \"attributeMasterId\": 20,\r\n" + 
				"        \"attributeValues\": \"LC\",\r\n" + 
				"        \"description\": \"Connector Type\",\r\n" + 
				"        \"name\": \"Connector Type\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240746,\r\n" + 
				"        \"attributeMasterId\": 21,\r\n" + 
				"        \"attributeValues\": \"Yes\",\r\n" + 
				"        \"description\": \"Access Required\",\r\n" + 
				"        \"name\": \"Access Required\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240747,\r\n" + 
				"        \"attributeMasterId\": 22,\r\n" + 
				"        \"attributeValues\": \"Customer provided\",\r\n" + 
				"        \"description\": \"CPE\",\r\n" + 
				"        \"name\": \"CPE\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240748,\r\n" + 
				"        \"attributeMasterId\": 61,\r\n" + 
				"        \"attributeValues\": \"Full Mesh\",\r\n" + 
				"        \"description\": \"VPN Topology\",\r\n" + 
				"        \"name\": \"VPN Topology\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240749,\r\n" + 
				"        \"attributeMasterId\": 62,\r\n" + 
				"        \"attributeValues\": \"Mesh\",\r\n" + 
				"        \"description\": \"Site Type\",\r\n" + 
				"        \"name\": \"Site Type\"\r\n" + 
				"      },\r\n" + 
				"      {\r\n" + 
				"        \"attributeId\": 240750,\r\n" + 
				"        \"attributeMasterId\": 64,\r\n" + 
				"        \"attributeValues\": \"Unmanaged\",\r\n" + 
				"        \"description\": \"Access Topology\",\r\n" + 
				"        \"name\": \"Access Topology\"\r\n" + 
				"      }\r\n" + 
				"    ]\r\n" + 
				"  }\r\n" + 
				"]";
		OrderProductComponentBean[] comp = (OrderProductComponentBean[]) Utils.convertJsonToObject(json,
				OrderProductComponentBean[].class);
		for (int i = 0; i <= comp.length - 1; i++) {
			list.add(comp[i]);

		}

		return list;
	}

	/**
	 * getQuoteIllSiteBean
	 * 
	 * @return
	 * @throws TclCommonException 
	 */
	public List<OrderIllSiteBean> getOrderIllSiteBean() throws TclCommonException {
		List<OrderIllSiteBean> list = new ArrayList<>();
		OrderIllSiteBean quoteIllSiteBean = new OrderIllSiteBean();
		quoteIllSiteBean.setArc(78.0D);
		quoteIllSiteBean.setIsFeasible((byte) 1);
		quoteIllSiteBean.setMrc(78.0D);
		quoteIllSiteBean.setNrc(78.0D);
		quoteIllSiteBean.setErfLocSiteBLocationId(1);
		quoteIllSiteBean.setOrderProductComponentBeans(getComponentsBean1());
		quoteIllSiteBean.setSiteFeasibility(getFeasibilityDetails());
		quoteIllSiteBean.setOrderSla(getQuoteSlaDetails());
		list.add(quoteIllSiteBean);
		return list;
	}
	
	/**
	 * getQuoteSlaDetails
	 * 
	 * @return
	 * @throws TclCommonException 
	 */
	private List<OrderSlaBean> getQuoteSlaDetails() throws TclCommonException {
		String json = "[{\"id\":11741,\"slaEndDate\":null,\"slaStartDate\":null,\"slaMaster\":{\"id\":11741,\"slaDurationInDays\":null,\"slaName\":\"Service Availability %\"},\"slaValue\":\">= 99%\"},{\"id\":11742,\"slaEndDate\":null,\"slaStartDate\":null,\"slaMaster\":{\"id\":11742,\"slaDurationInDays\":null,\"slaName\":\"Mean Time To Restore (MTTR) in Hrs\"},\"slaValue\":\"2\"},{\"id\":11743,\"slaEndDate\":null,\"slaStartDate\":null,\"slaMaster\":{\"id\":11743,\"slaDurationInDays\":null,\"slaName\":\"Time To Restore (TTR) in Hrs\"},\"slaValue\":\"4\"},{\"id\":11744,\"slaEndDate\":null,\"slaStartDate\":null,\"slaMaster\":{\"id\":11744,\"slaDurationInDays\":null,\"slaName\":\"Jitter Servicer Level Target (ms)\"},\"slaValue\":\"[]\"},{\"id\":11745,\"slaEndDate\":null,\"slaStartDate\":null,\"slaMaster\":{\"id\":11745,\"slaDurationInDays\":null,\"slaName\":\"Packet Delivery Ratio Service Level Target %\"},\"slaValue\":\"[{\\\"cosKey\\\":\\\"cos 3\\\",\\\"cosValue\\\":\\\">= 99.99%\\\"}]\"}]";
		List<OrderSlaBean> list = new ArrayList<>();

		OrderSlaBean[] comp = (OrderSlaBean[]) Utils.convertJsonToObject(json, OrderSlaBean[].class);
		for (int i = 0; i <= comp.length - 1; i++) {
			list.add(comp[i]);

		}
		return list;
	}

	
	/**
	 * getFeasibilityDetails
	 * 
	 * @return
	 * @throws TclCommonException 
	 */
	private List<SiteFeasibilityBean> getFeasibilityDetails() throws TclCommonException {
		List<SiteFeasibilityBean> list = new ArrayList<>();
		
		String json="[{\"feasibilityCheck\":\"system\",\"feasibilityMode\":\"OnnetWL\",\"feasibilityCode\":\"1GKVIMTXLHBGNYQF\",\"rank\":2,\"responseJson\":null,\"provider\":\"TATA COMMUNICATIONS\",\"isSelected\":1,\"type\":\"primary\",\"createdTime\":1537949577000}]";
		SiteFeasibilityBean[] comp=(SiteFeasibilityBean[])Utils.convertJsonToObject(json, SiteFeasibilityBean[].class);
		for(int i=0; i<=comp.length-1;i++) {
			list.add(comp[i]);

		}
		return list;
	}
	 
	public CustomerLeDetailsBean getCustomerLeDetailsBean() {
		CustomerLeDetailsBean customerLeDetailsBean=new CustomerLeDetailsBean();
		List<Attributes>  attributeList= new ArrayList<>();
		customerLeDetailsBean.setAccounCuId("1");
		customerLeDetailsBean.setAccountId("2");
		customerLeDetailsBean.setBillingContactId(1);
		customerLeDetailsBean.setLegalEntityName("regus");	
		attributeList.add(getAttributesBean());
		customerLeDetailsBean.setAttributes(attributeList);
		return customerLeDetailsBean;
	}

	public Attributes getAttributesBean() {
		Attributes attributes = new Attributes();
		attributes.setAttributeName("entityName");
		attributes.setAttributeValue("Regus");
		return attributes;
	}

	public List<OrderNplLinkSla> getOrderNplLinkSlas() {
		List<OrderNplLinkSla> list = new ArrayList<>();
		list.add(getOrderNplLinkSla());
		return list;
	}
	public DocusignAudit getDocusignAudit() {
		DocusignAudit docusignAudit=new DocusignAudit();
		docusignAudit.setCustomerEnvelopeId("1");
		docusignAudit.setId(1);
		docusignAudit.setOrderRefUuid("UUID");
		docusignAudit.setStage("stage");
		docusignAudit.setStatus("status");
		docusignAudit.setSupplierEnvelopeId("envolopeId");
		return docusignAudit;
	}

	public NplLinksUpdateBean getNplLinkUpdateBean()
	{
		NplLinksUpdateBean nplLinkBean=new NplLinksUpdateBean();
		nplLinkBean.setQuoteId(21040);
		nplLinkBean.setCustomerId(2);
		nplLinkBean.setQuoteleId(20996);
		nplLinkBean.setProductName("NPL");
		NplLinkSitesBean nplLinkSitesBean=new NplLinkSitesBean();
		nplLinkSitesBean.setLink(getNplSite());
		List<NplLinkSitesBean> linkSitesBeans=new ArrayList<>();
		linkSitesBeans.add(nplLinkSitesBean);
		nplLinkBean.setLinks(linkSitesBeans);
		return nplLinkBean;
	}
}
