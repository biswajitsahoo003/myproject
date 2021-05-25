package com.tcl.dias.oms.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import java.util.Set;


import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.CustomerLeBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.VproxySolutionBean;
import com.tcl.dias.oms.entity.entities.IzosdwanSiteFeasibility;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;
import com.tcl.dias.oms.entity.entities.QuoteIzoSdwanMssPricing;
import com.tcl.dias.oms.pricing.bean.FeasibilityBean;
import com.tcl.dias.oms.pricing.bean.FeasibilitySiteEngineBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.springframework.stereotype.Service;

import com.mysql.fabric.xmlrpc.base.Array;
import com.tcl.dias.common.beans.CpeRequestBean;
import com.tcl.dias.common.beans.IzoSdwanCpeBomInterface;
import com.tcl.dias.common.beans.IzoSdwanCpeDetails;
import com.tcl.dias.common.beans.IzoSdwanSiteDetails;

import com.tcl.dias.common.beans.IzosdwanQuoteAttributesUpdateBean;
import com.tcl.dias.common.beans.IzosdwanQuoteAttributesUpdateRequest;
import com.tcl.dias.common.beans.LocationInputDetails;
import com.tcl.dias.common.beans.VProxyAddonsBean;
import com.tcl.dias.common.beans.VproxyProductOfferingBean;
import com.tcl.dias.common.beans.VproxyQuestionnaireDet;
import com.tcl.dias.common.beans.VproxySolutionsBean;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.ByonUploadResponse;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.LocationTemplateRequest;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteLeAttributeBean;

import com.tcl.dias.common.beans.IzosdwanQuoteAttributesBean;
import com.tcl.dias.common.beans.LocationInputDetails;
import com.tcl.dias.common.beans.ProductOfferingsBean;
import com.tcl.dias.common.beans.ProfileSelectionInputDetails;
import com.tcl.dias.common.beans.VendorProfileDetailsBean;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteIzoSdwanAttributeValue;
import com.tcl.dias.oms.beans.QuoteLeAttributeBean;
import com.tcl.dias.oms.beans.ServiceResponse;

import com.tcl.dias.oms.beans.SolutionDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.IzosdwanPricingService;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;

import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIzoSdwanAttributeValues;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanSite;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;

import com.tcl.dias.oms.entity.entities.QuoteIzoSdwanAttributeValues;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanByonUploadDetail;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanCgwDetail;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanSite;

import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.izosdwan.beans.BandWidthSummaryCpeBean;
import com.tcl.dias.oms.izosdwan.beans.BandwidthDet;
import com.tcl.dias.oms.izosdwan.beans.ChargeableLineItemSummaryBean;
import com.tcl.dias.oms.izosdwan.beans.ConfigurationCpeInfo;
import com.tcl.dias.oms.izosdwan.beans.ConfigurationSummaryBean;
import com.tcl.dias.oms.izosdwan.beans.CpeBandwidthSummaryDetails;
import com.tcl.dias.oms.izosdwan.beans.CpeLinks;
import com.tcl.dias.oms.izosdwan.beans.CpeTypes;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanPdfSiteBean;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanQuotePdfBean;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanSolutionLevelCharges;
import com.tcl.dias.oms.izosdwan.beans.NetworkSummaryDetails;


import com.tcl.dias.oms.izosdwan.beans.PricingInformationRequestBean;

import com.tcl.dias.oms.izosdwan.beans.QuotePricingDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.SiteTypeDetails;
import com.tcl.dias.oms.izosdwan.beans.SiteTypeSummary;
import com.tcl.dias.oms.izosdwan.beans.SiteTypes;

import com.tcl.dias.oms.izosdwan.beans.SolutionLevelPricingBreakupDetailsBean;

import com.tcl.dias.oms.izosdwan.beans.SolutionPricingDetailsBean;

import com.tcl.dias.oms.izosdwan.beans.ViewSitesSummaryBean;
import com.tcl.dias.oms.izosdwan.beans.VproxySolutionRequestBean;

import io.swagger.models.auth.In;
import sun.nio.ch.Util;

@Service
public class IzoSdwanObjectCreator extends ObjectCreator{
	
	public CpeRequestBean getCpeDetailsBean() {
		CpeRequestBean bean=new CpeRequestBean();
		bean.setProfileName("basic");
		bean.setVendorName("IZO_SDWAN_SELECT");
		bean.setAddons("select plus");
		return bean;
	}

	public List<IzoSdwanCpeDetails> getSdwanDetails(){
		List<IzoSdwanCpeDetails> cpe=new ArrayList<IzoSdwanCpeDetails>();
		IzoSdwanCpeDetails cpeDet=new IzoSdwanCpeDetails();
		cpeDet.setCpeName("uCPE_110");
		cpeDet.setBandwidth(83);
		cpeDet.setBandwidthRate("Mbps");
		cpeDet.setL2Ports(0);
		cpeDet.setL3Ports(5);
		cpeDet.setCpePriority(1);
		cpeDet.setMaxL3Cu(5);
		cpeDet.setMaxL3Fi(0);
		cpeDet.setVendor("IZO_SDWAN_SELECT");
		cpeDet.setAddon("SELECT_PLUS");
		cpeDet.setProfile("ENHANCED");
		cpe.add(cpeDet);
		return cpe;
	}
	
	public ConfigurationSummaryBean getConfigDet() {
		ConfigurationSummaryBean response=new ConfigurationSummaryBean();
		response.setNoOfSites(10);
		response.setSiteTypeName("Single Gvpn Single Ias");
		List<CpeTypes> cpeTypes=new ArrayList<CpeTypes>();
		CpeTypes cpe=new CpeTypes();
		List<CpeLinks> links=new ArrayList<CpeLinks>();
		CpeLinks link=new CpeLinks();
		link.setLinkName("GVPN LOCAL");
		link.setRange("200-230");
		links.add(link);
		cpe.setCpeTypeName("Primary");
		cpe.setCpeLinks(links);
		response.setCpeTypes(cpeTypes);
		List<ConfigurationCpeInfo> cpeInfo=new ArrayList<ConfigurationCpeInfo>();
		ConfigurationCpeInfo info=new ConfigurationCpeInfo();
		List<BandWidthSummaryCpeBean> bean=new ArrayList<BandWidthSummaryCpeBean>();
		BandWidthSummaryCpeBean bandCpe=new BandWidthSummaryCpeBean();
		List<BandwidthDet> det=new ArrayList<BandwidthDet>();
		BandwidthDet details=new BandwidthDet();
		details.setBandwidthRange("20-90");
		details.setBandwidthTypeName("GVPN");
		det.add(details);
	//	bandCpe.setCpeModelName("Ucpe_100");
		bandCpe.setNoOfCpes(120);
		bandCpe.setBandwidthSummary(det);
		bean.add(bandCpe);
		info.setCpetype("Primary");
		cpeInfo.add(info);
		return response;
	}
	
	public List<IzoSdwanSiteDetails> getSiteDetails(){
		List<IzoSdwanSiteDetails> siteDet=new ArrayList<>();
		IzoSdwanSiteDetails det=new IzoSdwanSiteDetails();
		det.setNoOfCpePorts(10);
		det.setNoOfL3Ports(1);
		det.setSiteTypeName("SINGLE GVPN SINGLE IAS");
		det.setNoOfL2Ports(8);
		siteDet.add(det);
		return siteDet;
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
		quote.setQuoteCode("IZOSDWAN2IOUCV");
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
		customer.setCustomerEmailId("vengi@gmail.com");
		customer.setCustomerName("Vengamma");
		// customer.toString();
		customer.setUsers(getUserEntity());
		quote.setCustomer(customer);

		quote.setCustomer(getCustomerWithOutQuote());
		return quote;
	}
	
	public List<String> getCpe(){
		List<String> cpeList=new ArrayList<>();
		cpeList.add("Ucpe_110");
		cpeList.add("Ucpe_520");
		return cpeList;
	}
	
	public List<ViewSitesSummaryBean> getViewSite(){
		List<ViewSitesSummaryBean>  bean=new ArrayList<>();
		ViewSitesSummaryBean summary=new ViewSitesSummaryBean();
		summary.setLocationId(7687981);
		
		//summary.setSiteId(765);
		bean.add(summary);
		return bean;
	}

	public List<SiteTypeDetails> getSiteTypeDetails(){
		List<SiteTypeDetails> siteDet=new ArrayList<>();
		SiteTypeDetails site=new SiteTypeDetails();
		site.setSiteTypename("Single IAS Single GVPN");
		site.setNoOfSites(20);
		List<Integer> siteIds=new ArrayList<>();
		siteIds.add(7676);
		siteIds.add(28787);
		site.setSiteIds(siteIds);
		siteDet.add(site);
		return siteDet;
		
	}
	
	public List<ViewSitesSummaryBean>  getViewSiteDetails(){
		List<ViewSitesSummaryBean>  summary=new ArrayList<>();
		ViewSitesSummaryBean bean=new ViewSitesSummaryBean();
		bean.setLocationId(7637);
		List<Integer> siteIds=new ArrayList<>();
		siteIds.add(78387);
		siteIds.add(77387);
		bean.setSiteIds(siteIds);
		summary.add(bean);
		return summary;
	}
	
	public LocationInputDetails getInputDetails() {
		LocationInputDetails request=new LocationInputDetails();
		request.setTextToSearch("mql");
		List<Integer> locIds=new ArrayList<>();
		locIds.add(9887);
		locIds.add(7777);
		request.setLocationIds(locIds);
		return request;
	}
	
	public List<Integer> getLocIds(){
		List<Integer> loc=new ArrayList<>();
		loc.add(8353);
		loc.add(1221);
		loc.add(54656);
		return loc;
	}
	
	public QuoteLeAttributeBean getQuoteLe() {
		QuoteLeAttributeBean bean=new QuoteLeAttributeBean();
		bean.setIsFeasibilityCheckDone("yes");
		bean.setIsPricingCheckDone("no");
		bean.setQuoteLegalEntityId(787878);
		return bean;
		
	}
	
	public QuoteIzosdwanSite getIzoSdwanSite() {
		QuoteIzosdwanSite siteDet=new QuoteIzosdwanSite();
		siteDet.setCreatedBy(7612);
		siteDet.setCreatedTime(new Date());
		siteDet.setEffectiveDate(new Date());
		siteDet.setErfLocSiteaLocationId(1333);
		siteDet.setIzosdwanSiteOffering("Single IAS Single CPE");
		siteDet.setPriSec("Primary");
		siteDet.setIzosdwanSiteProduct("IAS");
		siteDet.setIzosdwanSiteType("1");
		siteDet.setIzosdwanSiteProduct("sfdc");
		siteDet.setIzosdwanSiteType("Single GVP single CPE");
		siteDet.setServiceSiteCountry("India");
		return siteDet;
	}
	
	public List<QuoteIzosdwanSite> getIzoSdwanSites(){
		 List<QuoteIzosdwanSite> sites=new ArrayList<>();
		 QuoteIzosdwanSite site=getIzoSdwanSite();
		 sites.add(site);
		 return sites;
	}

	public List<String> getSiteTypes(){
		List<String> sites=new ArrayList<>();
		sites.add("Single IAS Single CPE");
		sites.add("Single GVPN Single CPE");
		sites.add("Dual IAS Single CPE");
		sites.add("Dual GVPN Single CPE");
		sites.add("Single GVPN Single IAS");
		sites.add("DualIAS");
		sites.add("Single GVPN");
		return sites;

	}
	
	public NetworkSummaryDetails getSummaryDet() {
		NetworkSummaryDetails summary=new NetworkSummaryDetails();
		summary.setQuoteId(66544);
		summary.setQuoteLeId(676767);
		SiteTypeSummary siteSumm=new SiteTypeSummary();
		List<SiteTypes> siteTypes=new ArrayList<>();
		SiteTypes types=new SiteTypes();
		types.setNoOfSites(122);
		types.setSiteTypeName("Single IAS Single GVPN");
		siteTypes.add(types);
		siteSumm.setSiteDetails(siteTypes);
		CpeBandwidthSummaryDetails det=new CpeBandwidthSummaryDetails();
		det.setTotalNoOfSites(12);
		summary.setCpeBandwidthSummaryDet(det);
		
		return summary;
		
	}
	
	public List<BigInteger> getIds(){
		List<BigInteger> ids=new ArrayList<BigInteger>();
		BigInteger i=new BigInteger("7372222");
		ids.add(i);
		BigInteger j=new BigInteger("737122");
		ids.add(j);
		return ids;
	}
	
	public List<String> getCpeNames(){
		List<String> cpe=new ArrayList<>();
		cpe.add("UCPE27287");
		cpe.add("U_CPE122");
		return cpe;
	}
	
	public QuoteProductComponent getComponent() {
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setId(1);
		quoteProductComponent.setReferenceId(1);
		quoteProductComponent.setMstProductComponent(getMstProductComponent());
		return quoteProductComponent;
	}
	
//updated

	public Optional<Quote> getquoteDetail(){
		Optional<Quote> quoteOptional=Optional.empty();
		QuoteDetail quoteDetail = new QuoteDetail();
		/* quoteDetail.toString(); */
		quoteDetail.setCustomerId(1);
		quoteDetail.setSite(getSite());
		quoteDetail.setProductName("IAS");
		quoteDetail.setSite(getSite());
		quoteDetail.setQuoteleId(1);
		quoteDetail.setQuoteId(1);
		quoteDetail.setQuoteId(2);
		quoteDetail.setIzosdwanFlavour("slecct");
		
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
		//quoteOptional
		Optional.of(quoteDetail);
		return quoteOptional;
	}
	public UpdateRequest getUpdateRequest() {
		UpdateRequest request=new UpdateRequest();
		request.setTermInMonths("12 Months");
		return request;
	}
	public QuotePrice getPrice() {
		 QuotePrice price=getPrice();
		 price.setCatalogArc(6456456789.9);
		 price.setCatalogMrc(867663.9);
		 price.setCatalogNrc(6565454.98);
		 price.setComputedMrc(54433.6);
		 return price;
	}
	public List<QuotePrice> getQuotePrices(){
		 List<QuotePrice> prices=new ArrayList<>();
		 QuotePrice price=getPrice();
		 prices.add(price);
		 return prices;
	}
	public QuotePricingDetailsBean getPricingDetailBean() {
		QuotePricingDetailsBean priceBean=new QuotePricingDetailsBean();
		priceBean.setCurrency("INR");
		return priceBean;
		
	}
	
	public List<QuoteIzoSdwanAttributeValues> getIzoSdwanVal(){
		List<QuoteIzoSdwanAttributeValues> values=new ArrayList<>();
//		values.add(getIzoSdwanAttribute());
		values.add(getIzoSdwanAttribute4());
		return values;
	}
	
	public QuoteIzoSdwanAttributeValues getIzoSdwanAttribute() {
		QuoteIzoSdwanAttributeValues val=new QuoteIzoSdwanAttributeValues();
		val.setAttributeValue("isSWGProxy");
		val.setDisplayValue("No");
		return val;
	}

    public List<VproxySolutionsBean> getVproxySolutionDet(){
        List<VproxySolutionsBean> solutions=new ArrayList<>();
        VproxySolutionsBean solutionBean=new VproxySolutionsBean();
        solutionBean.setSolutionName("Secure Web GateWay");
        List<VproxyProductOfferingBean> vproxyProductOfferingBeans =new ArrayList<>();
        VproxyProductOfferingBean productBean=new VproxyProductOfferingBean();
        List<String> description=new ArrayList<>();
        description.add("Data Centres");
        productBean.setProductOfferingDescription(description);
        List<VProxyAddonsBean> vProxyAddonsBeans=new ArrayList<>();
        VProxyAddonsBean addons=new VProxyAddonsBean();
        addons.setName("Adavanced Cloud Sandbox");
        //addons.se
        vProxyAddonsBeans.add(addons);
        productBean.setvProxyAddonsBeans(vProxyAddonsBeans);
        vproxyProductOfferingBeans.add(productBean);
        List<VproxyQuestionnaireDet> vproxyProductQuestionnaires=new ArrayList<>();
        VproxyQuestionnaireDet vproxyQues=new VproxyQuestionnaireDet();
        vproxyQues.setName("Total No Of Users");
        vproxyQues.setDescription("Total no of question");
        vproxyProductQuestionnaires.add(vproxyQues);
        solutionBean.setVproxyProductOfferingBeans(vproxyProductOfferingBeans);
        solutions.add(solutionBean);
        return solutions;
    }
    
    public VproxySolutionRequestBean getVproxySolutionDetails() {
    	VproxySolutionRequestBean solution=new VproxySolutionRequestBean();
    	solution.setProductName("Secure Private Access");
    	solution.setQuoteId(32768);
    	solution.setQuoteLeId(34567);
    //	solution.setVproxySolutionsBeans(getVproxySolutionDet());
    	return solution;
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
	
    public QuoteToLeProductFamily  getLeProductFamily() {
    	QuoteToLeProductFamily qFamily = new QuoteToLeProductFamily();
		qFamily.setId(1);
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(1);
		mstProductFamily.setName("ILL");
		mstProductFamily.setStatus((byte) 2);
		qFamily.setMstProductFamily(mstProductFamily);
		qFamily.setProductSolutions(getProductSolution());
		return qFamily;
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
	
    public MstProductOffering getMstOffering() {
		MstProductOffering mstProductOffering = new MstProductOffering();
		mstProductOffering.setId(1);
		mstProductOffering.setErfProductOfferingId(1);
		mstProductOffering.setProductDescription("VproxySolution");
		mstProductOffering.setProductName("Vproxys");
		mstProductOffering.setMstProductFamily(geProductFamily());
		return mstProductOffering;
	}
    
    public Set<QuoteIllSite> getIllsitesList() {
		Set<QuoteIllSite> illSites = new HashSet<>();
		illSites.add(getIllsites1());
		illSites.add(getIllsites2());
		return illSites;
	}
    
    public ProductSolution getSolution() {
    	ProductSolution productSolution = new ProductSolution();
		productSolution.setId(1);
		productSolution.setProductProfileData(
				"{\"offeringName\":\"Single Internet Access\",\"image\":\"\",\"bandwidth\":\"10\",\"bandwidthUnit\":\"mbps\",\"components\":[{\"componentId\":\"1\",\"componentMasterId\":\"1\",\"name\":\"CPE Management\",\"isActive\":\"true\",\"attributes\":[{\"attributeId\":\"1\",\"attributeMasterId\":\"1\",\"name\":\"CPE Manufacturer\",\"value\":\"CISCO\"}]}]}");
		productSolution.setMstProductOffering(getMstOffering());
		productSolution.setQuoteIllSites(getIllsitesList());
		return productSolution;
    }
    
    public VproxySolutionRequestBean getVproxySolutionDetailsNegatiive() {
    	VproxySolutionRequestBean solution=new VproxySolutionRequestBean();
    	solution.setProductName("Secure Private Access");
    	//solution.setVproxySolutionsBeans(getVproxySolutionDet());
    	return solution;
    }
    
    public UpdateRequest getUpdateRequestDetails() {
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
		return request;
	}

    public Customer createCustomer() {

		Customer customer = new Customer();
		customer.setId(1);
		customer.setCustomerEmailId("test@gmail.com");
		Set<Order> set = new HashSet<>();
		set.add(getOrders());
		customer.setOrders(set);
		return customer;
	}
    
	public User getUser() {
		User user = new User();
		user.setId(1);
		user.setFirstName("test");
		user.setId(1);
		user.setStatus(1);
		user.setCustomer(craeteCustomer());

		return user;

	}

	public QuoteLeAttributeValue getLeValues() {
		QuoteLeAttributeValue leVal=new QuoteLeAttributeValue();
		leVal.setAttributeValue("INR");
		leVal.setQuoteToLe(getQuoteToLe());
		leVal.setMstOmsAttribute(getMstOmsAttribute());
		leVal.setId(871);
		leVal.setDisplayValue("Payment Currency");
		return leVal;
	}
	
	public QuoteDetail getQuoteDetail() {
		QuoteDetail quoteDetail = new QuoteDetail();
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
	public IzosdwanQuoteAttributesUpdateRequest getUpdateRequestSdwan() {
		IzosdwanQuoteAttributesUpdateRequest updateRequest=new IzosdwanQuoteAttributesUpdateRequest();
		updateRequest.setName("IsSPA");
		updateRequest.setValue("Yes");
		return updateRequest;
	}
	public List<IzosdwanQuoteAttributesUpdateRequest> getUpdateRequestBeanForSdwan(){
		List<IzosdwanQuoteAttributesUpdateRequest> attributesUpdateRequests =new ArrayList<>();
		attributesUpdateRequests.add(getUpdateRequestSdwan());
		return attributesUpdateRequests;
	}
	public IzosdwanQuoteAttributesUpdateBean getSdwanUpdateBean() {
		IzosdwanQuoteAttributesUpdateBean bean=new IzosdwanQuoteAttributesUpdateBean();
		bean.setQuoteId(23456);
		bean.setIzosdwanQuoteAttributesUpdateRequests(getUpdateRequestBeanForSdwan());
		return bean;
	}
	
	public IzosdwanQuoteAttributesUpdateBean getSdwanUpdateBeanNegative() {
		IzosdwanQuoteAttributesUpdateBean bean=new IzosdwanQuoteAttributesUpdateBean();
		return bean;
	}
	
	public SolutionLevelPricingBreakupDetailsBean getSolutionPricing() {
		SolutionLevelPricingBreakupDetailsBean pricing=new SolutionLevelPricingBreakupDetailsBean();
		pricing.setActionType("Sale");
		pricing.setArc(80872.388);
		pricing.setBandwidth("101 Mbps");
		pricing.setNrc(12333222.33);
		pricing.setName("IZOSDWAN");
		return pricing;
	}
	public List<SolutionLevelPricingBreakupDetailsBean> getSolutionLevelPricing(){
		List<SolutionLevelPricingBreakupDetailsBean> solutions=new ArrayList<>();
		solutions.add(getSolutionPricing());
		return solutions;
	}

//updated end
    
    public PricingInformationRequestBean getPriceRequestBean() {
    	PricingInformationRequestBean pricingInformationRequestBean=new PricingInformationRequestBean();
    	List<Integer> siteIds=new ArrayList<>();
    	siteIds.add(1);
    	siteIds.add(2);
    	pricingInformationRequestBean.setProductName("Izosdwan");
    	pricingInformationRequestBean.setQuoteId(1234);
    	pricingInformationRequestBean.setSiteType("Single GVP single CPE");
    	pricingInformationRequestBean.setSiteIds(siteIds);
    	return pricingInformationRequestBean;
    }
    
    public  SolutionPricingDetailsBean getSolutionPricingDet() {
    	SolutionPricingDetailsBean solutionPricingDetailsBean=new SolutionPricingDetailsBean();
    	solutionPricingDetailsBean.setArc(new BigDecimal(12.0));
    	solutionPricingDetailsBean.setMrc(new BigDecimal(876.8));
    	return solutionPricingDetailsBean;
    }
//
    public List<Integer> getUniqueLocationIds(){
		List<Integer> locIds=new ArrayList<>();
		locIds.add(6136);
		locIds.add(2326);
		return locIds;
	}
	
	public IzosdwanQuotePdfBean getIzosdwanQuotePdfBean() {
		IzosdwanQuotePdfBean quotePdfBean=new IzosdwanQuotePdfBean();
		quotePdfBean.setArcTcv("INRA");
		quotePdfBean.setCreatedDate("01/8772/726");
		quotePdfBean.setNrcTcv("7127");
		quotePdfBean.setChargeableLineItemSummaryBeans(getChargable());
		quotePdfBean.setIzosdwanPdfSiteBeans(getpdfList());
		return quotePdfBean;
	}

    public ChargeableLineItemSummaryBean getChargble() {
		ChargeableLineItemSummaryBean charge=new ChargeableLineItemSummaryBean();
		charge.setArc("6677");
		charge.setName("abcde");
		charge.setNrc("167177");
		return charge;
	}
	public IzosdwanPdfSiteBean getpdfSiteBean() {
		IzosdwanPdfSiteBean pdf=new IzosdwanPdfSiteBean();
		pdf.setAddress("Nandambakam");
		pdf.setCity("Chennai");
		pdf.setCountry("India");
		pdf.setSiteType("Single IAS Single CPE");
		return pdf;
	}
	public List<IzosdwanPdfSiteBean> getpdfList(){
		List<IzosdwanPdfSiteBean> pdfs=new ArrayList<IzosdwanPdfSiteBean>();
		pdfs.add(getpdfSiteBean());
		return pdfs;
	}

    public List<ChargeableLineItemSummaryBean> getChargable(){
		List<ChargeableLineItemSummaryBean> charges=new ArrayList<>();
		charges.add(getChargble());
		return charges;
	}
	
	public ServiceResponse getServiceResponse() {
		ServiceResponse response=new ServiceResponse();
		response.setFileName("Hello");
		response.setAttachmentId(353265);
		response.setStatus(Status.SUCCESS);
		response.setUrlPath("//dgd tedy qd");
		return response;
	}
	
	public List<String> getCpes(){
		List<String> cpe=new ArrayList<>();
		cpe.add("UCPE_110");
		cpe.add("UCPE_520");
		return cpe;
	}

    public VendorProfileDetailsBean getVendorDetails() {
		VendorProfileDetailsBean bean=new VendorProfileDetailsBean();
		bean.setVendor("IZOSDWAN");
		bean.setProductOfferingsBeans(getOfferingList());
		return bean;
	}
	public ProductOfferingsBean getOfferingBean() {
		ProductOfferingsBean bean=new ProductOfferingsBean();
		bean.setAction("Enable");
		bean.setIsRecommended(true);
		bean.setMrc("INR");
		bean.setNrc("USD");
		bean.setProductOfferingsName("Enhanced");
		return bean;
	}
	
	
	public List<ProductOfferingsBean> getOfferingList(){
		List<ProductOfferingsBean> beans=new ArrayList<>();
		beans.add(getOfferingBean());
		return beans;
	}
  
//
	
	public List<QuoteIzoSdwanAttributeValues> getSdwanAttrValues(){
		List<QuoteIzoSdwanAttributeValues> attrValues=new  ArrayList<>();
		QuoteIzoSdwanAttributeValues attrVal=new QuoteIzoSdwanAttributeValues();
		attrVal.setDisplayValue("CGWHeteroBw");
		attrVal.setAttributeValue("70");
		attrVal.setDisplayValue("cgwMigUserModifiedBW");
		attrVal.setAttributeValue("300");
		attrValues.add(attrVal);
		return attrValues;
	}
	
	public List<QuoteIzoSdwanAttributeValues> getSdwanAttrValues1(){
		List<QuoteIzoSdwanAttributeValues> attrValues=new  ArrayList<>();
		QuoteIzoSdwanAttributeValues attrVal=new QuoteIzoSdwanAttributeValues();
		attrVal.setDisplayValue("isSWGVproxy");
		attrVal.setAttributeValue("Yes");
		attrVal.setDisplayValue("isSPAVproxy");
		attrVal.setAttributeValue("Yes");
		attrValues.add(attrVal);
		return attrValues;
	}
    
	public List<QuoteIzosdwanByonUploadDetail> getByonUploadDetails(){
		List<QuoteIzosdwanByonUploadDetail> uploadDetails=new ArrayList<>();
		QuoteIzosdwanByonUploadDetail uploadDet=new QuoteIzosdwanByonUploadDetail();
		uploadDet.setAddress("Street no.5,Nandhambakkam,Chennai,TamilNadu");
		uploadDet.setCity("Chennai");
		uploadDet.setCountry("India");
		uploadDet.setId(1);
		uploadDet.setInternetQuality("Enterprise Grade");
		uploadDet.setLocality("Nandhambakkam");
		//uploadDet.setErrorDetails("error");
		uploadDet.setStatus("OPEN");
		uploadDetails.add(uploadDet);
		return uploadDetails;
	}
	
	public ProfileSelectionInputDetails getProfileSelectionInputDetails() {
		ProfileSelectionInputDetails profileSelectionInputDetails=new ProfileSelectionInputDetails();
		profileSelectionInputDetails.setCustomerId(108);
		profileSelectionInputDetails.setProfileName("Enhanced");
		profileSelectionInputDetails.setQuoteId(34567);
		profileSelectionInputDetails.setVendorName("IZOSDWAN_SELECT");
		return profileSelectionInputDetails;
	}
	
	public List<VendorProfileDetailsBean> getvendorProfileDetailsList(){
		List<VendorProfileDetailsBean> vendorProfileDetailsBeans=new ArrayList<>();
		VendorProfileDetailsBean vendorProfileDetailsBean=getVendorDetails();
		vendorProfileDetailsBeans.add(vendorProfileDetailsBean);
		return vendorProfileDetailsBeans;
	}
	
	public List<IzoSdwanCpeBomInterface> getcpeBomInterfaces(){
		List<IzoSdwanCpeBomInterface> cpeBomInterfaces=new ArrayList<>();
		IzoSdwanCpeBomInterface izoSdwanCpeBomInterface=new IzoSdwanCpeBomInterface();
		izoSdwanCpeBomInterface.setBomNameCd("uCPE_110");
		izoSdwanCpeBomInterface.setDescription("abc");
		izoSdwanCpeBomInterface.setInterfaceType("1000_BASE_LX");
		izoSdwanCpeBomInterface.setPhysicalResourceCd("xyz");
		izoSdwanCpeBomInterface.setProductCategory("abcd");
		cpeBomInterfaces.add(izoSdwanCpeBomInterface);
		return cpeBomInterfaces;
	}
	
	public List<QuoteIzosdwanCgwDetail> getCgwDetail() {
		List<QuoteIzosdwanCgwDetail> quoteIzosdwanCgwDetails=new ArrayList<>();
		QuoteIzosdwanCgwDetail cgwDetail=new QuoteIzosdwanCgwDetail();
		cgwDetail.setId(1);
		cgwDetail.setHetroBw("56");
		cgwDetail.setMigrationSystemBw("20");
		quoteIzosdwanCgwDetails.add(cgwDetail);
		return quoteIzosdwanCgwDetails;
	}
	
	public QuoteProductComponent getProductComponent() {
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setId(1);
		quoteProductComponent.setReferenceId(1);
		quoteProductComponent.setMstProductComponent(getMstProductComponent());
		quoteProductComponent.setMstProductFamily(geProductFamily());
		quoteProductComponent.setQuoteProductComponentsAttributeValues(getQuoteComponentsAttributeValues());
		return quoteProductComponent;
	}
	
	public List<QuoteIzoSdwanAttributeValue> getInputIzosdwanSdwanAttributeValues(){
		 List<QuoteIzoSdwanAttributeValue> quoteIzoSdwanAttributeValues=new ArrayList<>();
		 QuoteIzoSdwanAttributeValue attrValue=new QuoteIzoSdwanAttributeValue();
		 attrValue.setDisplayValue("CGWHeteroBw");
		 attrValue.setAttributeValue("60");
		 quoteIzoSdwanAttributeValues.add(attrValue);
		 return quoteIzoSdwanAttributeValues;
	}
	
	public List<IzosdwanQuoteAttributesBean> getIzosdwanQuoteAttributesBean(){
		List<IzosdwanQuoteAttributesBean> quoteAttributesBeans=new ArrayList<>();
		IzosdwanQuoteAttributesBean izosdwanQuoteAttributesBean=new IzosdwanQuoteAttributesBean();
		izosdwanQuoteAttributesBean.setId(123);
		izosdwanQuoteAttributesBean.setName("isCGWHetero");
		izosdwanQuoteAttributesBean.setValue("true");
		quoteAttributesBeans.add(izosdwanQuoteAttributesBean);
		return quoteAttributesBeans;
	}
	
	public List<String> getStatusList() {
		List<String> statusList = new ArrayList<>();
		statusList.add(IzosdwanCommonConstants.OPEN);
		statusList.add(IzosdwanCommonConstants.INPROGRESS);
		return statusList;
	}
	
	public ByonUploadResponse getByonUploadResponseDet() {
		ByonUploadResponse byon=new ByonUploadResponse();
		byon.setByonProfileValid(true);
		byon.setByonUploadError(false);
		byon.setFileEmpty(false);
		return byon;
	}
	
	public LocationTemplateRequest getLocationDetails() {
		LocationTemplateRequest location = new LocationTemplateRequest();
		List<String> countries = new ArrayList<>();
		countries.add("India");
		countries.add("USA");
		location.setCountries(countries);
		return location;
	}
	
	public IzosdwanSolutionLevelCharges getSolutionCharge() {
		IzosdwanSolutionLevelCharges solution=new IzosdwanSolutionLevelCharges();
		solution.setArc(new BigDecimal(4567.765));
		solution.setName("IZOSDWAN");
		solution.setNrc(new BigDecimal(23456.987));
		return solution;
	}
	public List<IzosdwanSolutionLevelCharges> getSolutionLevelCharges(){
		List<IzosdwanSolutionLevelCharges> charges=new ArrayList<>();
		charges.add(getSolutionCharge());
		return charges;
	}
	
	public QuoteIzoSdwanAttributeValue getAttributeValueSdwan() {
		QuoteIzoSdwanAttributeValue val=new QuoteIzoSdwanAttributeValue();
		val.setAttributeValue("IsUpload");
		val.setDisplayValue("Yes");
		return val;
	}
	
	public List<QuoteIzoSdwanAttributeValue> getAttributeValuesSdwan(){
		List<QuoteIzoSdwanAttributeValue> values=new ArrayList<>();
		values.add(getAttributeValueSdwan());
		return values;
	}
	
	public QuoteDetail getQuoteDetailSdwan() {
		QuoteDetail quoteDetail = new QuoteDetail();
		/* quoteDetail.toString(); */
		quoteDetail.setCustomerId(1);
		quoteDetail.setProductName("IZOSDWAN");
		quoteDetail.setVendorName("IZO_SDWAN_SELECT");
		quoteDetail.setQuoteleId(39202);
		quoteDetail.setQuoteId(39216);
		List<ProductOfferingsBean> izosdwanSolutions=new ArrayList<>();
		ProductOfferingsBean productOfferingsBean=new ProductOfferingsBean();
		productOfferingsBean.setProductOfferingsName("Enhanced");
		productOfferingsBean.setProductOfferingsCode("ENHANCED");
		izosdwanSolutions.add(productOfferingsBean);
		quoteDetail.setIzosdwanSolutions(izosdwanSolutions);
	
		return quoteDetail;
	}
	public Optional<Quote> getQuoteDetailSdwans() {
		Optional<Quote> quoteOptional=Optional.empty();
		QuoteDetail quoteDetail = new QuoteDetail();
		/* quoteDetail.toString(); */
		quoteDetail.setCustomerId(1);
		quoteDetail.setProductName("IZOSDWAN");
		quoteDetail.setVendorName("IZO_SDWAN_SELECT");
		quoteDetail.setQuoteleId(39202);
		quoteDetail.setQuoteId(39216);
		quoteDetail.setIzosdwanFlavour("vice");
		List<ProductOfferingsBean> izosdwanSolutions=new ArrayList<>();
		ProductOfferingsBean productOfferingsBean=new ProductOfferingsBean();
		productOfferingsBean.setProductOfferingsName("Enhanced");
		productOfferingsBean.setProductOfferingsCode("ENHANCED");
		izosdwanSolutions.add(productOfferingsBean);
		quoteDetail.setIzosdwanSolutions(izosdwanSolutions);
		Optional.of(quoteDetail);
		return quoteOptional;
	}

	public Optional<QuoteIzosdwanSite> getOptionalsiteDetails(){
		QuoteIzosdwanSite siteDet = new QuoteIzosdwanSite();
		siteDet.setCreatedBy(7612);
		siteDet.setCreatedTime(new Date());
		siteDet.setEffectiveDate(new Date());
		siteDet.setErfLocSiteaLocationId(1333);
		siteDet.setIzosdwanSiteOffering("Single IAS Single CPE");
		siteDet.setPriSec("Primary");
		siteDet.setFeasibility((byte) 1);
		siteDet.setIsTaxExempted((byte) 1);
		Optional<QuoteIzosdwanSite> sites = Optional.of(siteDet);
		return sites;
	}

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

	public List<IzosdwanSiteFeasibility> feasibilitySitesList(){
		List<IzosdwanSiteFeasibility> list = new ArrayList<>();
		list.add(feasibilitySites());
		return list;
	}


	public IzosdwanSiteFeasibility feasibilitySites(){
		IzosdwanSiteFeasibility sFb = new IzosdwanSiteFeasibility();
		sFb.setId(1);
		sFb.setFeasibilityCode("Code");
		sFb.setFeasibilityCheck("check");
		sFb.setFeasibilityMode("mode");
		sFb.setIsSelected((byte) 1);
		sFb.setCreatedTime(new Timestamp(0));
		sFb.setType("Type");
		sFb.setQuoteIzosdwanSite(getIzoSdwanSite());
		sFb.setProvider("provider");
		sFb.setResponseJson(
				"{\"lm_nrc_ospcapex_onwl\":0.0,\"BW_mbps_2\":2.0,\"Service_ID\":0.0,\"total_cost\":132160.0,\"X0.5km_avg_dist\":9999999.0,\"POP_DIST_KM\":0.5100730350500826,\"X0.5km_prospect_avg_bw\":45.2727272727273,\"Latitude_final\":13.0184111,\"OnnetCity_tag\":1.0,\"lm_arc_bw_prov_ofrf\":0.0,\"Network_F_NF_CC\":\"NA\",\"lm_nrc_nerental_onwl\":0.0,\"lm_nrc_inbldg_onwl\":40000.0,\"Network_Feasibility_Check\":\"Feasible\",\"core_check_CC\":\"NA\",\"X5km_min_bw\":120.0,\"access_check_CC\":\"NA\",\"rank\":10,\"POP_DIST_KM_SERVICE_MOD\":15.0,\"X5km_max_bw\":200.0,\"X5km_prospect_perc_feasible\":0.926775147928994,\"lm_arc_bw_onrf\":0.0,\"POP_Category\":\"Metro Service Ready\",\"lm_nrc_bw_prov_ofrf\":0.0,\"FATG_Building_Type\":\"Commercial\",\"HH_0_5_access_rings_F\":\"NA\",\"hh_flag\":\"NA\",\"local_loop_interface\":\"FE\",\"X5km_prospect_avg_dist\":2738.92671182687,\"latitude_final\":13.0184111,\"FATG_Network_Location_Type\":\"Access\\/Customer POP\",\"topology\":\"primary_active\",\"BW_mbps_act\":2.0,\"city_tier\":\"Non_Tier1\",\"lm_nrc_bw_onwl\":0.0,\"X2km_prospect_min_dist\":2.21569108223123,\"X2km_prospect_perc_feasible\":0.94300518134715,\"X0.5km_prospect_num_feasible\":33.0,\"cpe_supply_type\":\"rental\",\"Orch_Connection\":\"Wireline\",\"site_id\":\"607_primary\",\"cpe_management_type\":\"Fully Managed\",\"Capex\":\"NA\",\"sales_org\":\"Enterprise\",\"opportunity_term\":12,\"BW_mbps\":2.0,\"resp_city\":\"Tiruvallur\",\"Burstable_BW\":0.0,\"connected_cust_tag\":0.0,\"net_pre_feasible_flag\":1.0,\"Network_F_NF_CC_Flag\":\"NA\",\"Orch_LM_Type\":\"Onnet\",\"X5km_prospect_avg_bw\":201.780650887574,\"FATG_Category\":\"Metro Service Ready\",\"lm_nrc_mast_ofrf\":0.0,\"Feasibility.Response..Created.Date\":17730.0,\"X2km_prospect_min_bw\":2.0,\"X5km_prospect_num_feasible\":1253.0,\"Selected\":false,\"lm_nrc_mast_onrf\":0.0,\"X5km_prospect_count\":1352.0,\"connection_type\":\"Premium\",\"X0.5km_avg_bw\":9999999.0,\"pool_size\":0,\"X2km_cust_count\":2.0,\"Customer_Segment\":\"Enterprise - Strategic\",\"customer_segment\":\"Enterprise - Strategic\",\"FATG_PROW\":\"No\",\"connected_building_tag\":0.0,\"FATG_DIST_KM\":41.33379039696604,\"X0.5km_prospect_max_bw\":500.0,\"X5km_prospect_min_dist\":2.21569108223123,\"Type\":\"OnnetWL\",\"FATG_Ring_type\":\"SDH\",\"longitude_final\":80.18774819999999,\"Orch_BW\":2.0,\"scenario_1\":0.0,\"scenario_2\":1.0,\"sno\":3,\"access_check_hh\":\"NA\",\"X2km_prospect_count\":386.0,\"min_hh_fatg\":41.33379039696604,\"POP_Construction_Status\":\"In Service\",\"X5km_avg_dist\":1616.32490273161,\"X2km_avg_dist\":1616.32490273161,\"X2km_prospect_avg_bw\":390.502590673575,\"X0.5km_prospect_min_dist\":2.21569108223123,\"cost_permeter\":0.0,\"Identifier\":\"NA\",\"prospect_name\":\"Kasturi & Sons Limited\",\"Predicted_Access_Feasibility\":\"Feasible\",\"X2km_max_bw\":200.0,\"X2km_prospect_max_bw\":10240.0,\"last_mile_contract_term\":\"1 Year\",\"X5km_prospect_min_bw\":0.064,\"X2km_min_bw\":120.0,\"burstable_bw\":0,\"seq_no\":141,\"bw_mbps\":2,\"X0.5km_cust_count\":0.0,\"X0.5km_min_bw\":9999999.0,\"product_name\":\"Internet Access Service\",\"Network_F_NF_HH\":\"NA\",\"HH_0_5_access_rings_NF\":\"NA\",\"X0.5km_min_dist\":9999999.0,\"POP_Building_Type\":\"Commercial\",\"core_check_hh\":\"NA\",\"HH_DIST_KM\":48.632402798319305,\"X0.5km_prospect_min_bw\":2.0,\"POP_DIST_KM_SERVICE\":14.152705282191185,\"lm_arc_bw_onwl\":33350.0,\"num_connected_building\":0.0,\"account_id_with_18_digit\":\"0012000000E1z9KAAR\",\"Orch_Category\":\"Capex less than 175m\",\"X0.5km_prospect_avg_dist\":243.087859734992,\"POP_Network_Location_Type\":\"Mega POP\",\"lm_nrc_mux_onwl\":58810.0,\"num_connected_cust\":0.0,\"HH_0_5km\":\"NA\",\"X5km_min_dist\":1424.59360546586,\"feasibility_response_created_date\":\"2018-07-18\",\"X2km_prospect_num_feasible\":364.0,\"additional_ip\":0,\"X2km_avg_bw\":160.0,\"X0.5km_max_bw\":9999999.0,\"X5km_avg_bw\":160.0,\"Status.v2\":\"NA\",\"X5km_prospect_max_bw\":10240.0,\"X2km_prospect_avg_dist\":1361.96557355991,\"cpe_variant\":\"None\",\"Longitude_final\":80.18774819999999,\"X5km_cust_count\":2.0,\"sum_no_of_sites_uni_len\":1,\"Product.Name\":\"Internet Access Service\",\"X0.5km_prospect_perc_feasible\":1.0,\"POP_TCL_Access\":\"Yes\",\"X0.5km_prospect_count\":33.0,\"Probabililty_Access_Feasibility\":0.8533333333333334,\"lm_nrc_bw_onrf\":0.0,\"quotetype_quote\":\"New Order\",\"X2km_min_dist\":1424.59360546586,\"FATG_TCL_Access\":\"No\",\"Network_F_NF_HH_Flag\":\"NA\"},{\"lm_nrc_ospcapex_onwl\":0.0,\"total_cost\":62375.757575757576,\"otc_tikona\":10000.0,\"Latitude_final\":13.0184111,\"lm_arc_bw_prov_ofrf\":35000.0,\"lm_nrc_nerental_onwl\":0.0,\"lm_nrc_inbldg_onwl\":0.0,\"offnet_5km_Min_accuracy_num\":50.0,\"interim_BW\":2.0,\"offnet_5km_Min_DistanceKilometers\":1.2178610509953627,\"rank\":5,\"offnet_0_5km_Max_BW_Mbps\":0.0,\"lm_arc_bw_onrf\":0.0,\"prospect_0_5km_Min_BW_Mbps\":0.0,\"lm_nrc_bw_prov_ofrf\":10000.0,\"local_loop_interface\":\"FE\",\"bw_flag_32\":0.0,\"arc_tikona\":35000.0,\"latitude_final\":13.0184111,\"topology\":\"primary_active\",\"min_mast_ht\":0.0,\"lm_nrc_bw_onwl\":0.0,\"cpe_supply_type\":\"rental\",\"offnet_2km_Min_DistanceKilometers\":1.2178610509953627,\"prospect_2km_feasibility_pct\":0.7636363636363637,\"Orch_Connection\":\"Wireless\",\"bw_flag_3\":0.0,\"offnet_2km_cust_Count\":2.0,\"site_id\":\"607_primary\",\"cpe_management_type\":\"Fully Managed\",\"sales_org\":\"Enterprise\",\"opportunity_term\":12,\"BW_mbps\":2.0,\"resp_city\":\"Tiruvallur\",\"prospect_2km_Avg_DistanceKilometers\":1.4529000280961837,\"offnet_5km_Max_BW_Mbps\":16.0,\"prospect_2km_Sum_Feasibility_flag\":42.0,\"arc_sify\":45000.0,\"prospect_2km_Avg_BW_Mbps\":2.343709090909091,\"Orch_LM_Type\":\"Offnet\",\"max_mast_ht\":27.0,\"lm_nrc_mast_ofrf\":17375.757575757576,\"Feasibility.Response..Created.Date\":17730.0,\"Selected\":true,\"provider_tot_towers\":0.0,\"lm_nrc_mast_onrf\":0.0,\"connection_type\":\"Premium\",\"offnet_2km_Min_BW_Mbps\":0.512,\"pool_size\":0,\"offnet_0_5km_Min_BW_Mbps\":0.0,\"customer_segment\":\"Enterprise - Strategic\",\"offnet_5km_Avg_BW_Mbps\":1.7612,\"time_taken\":3.1749999999883585,\"prospect_0_5km_Sum_Feasibility_flag\":0.0,\"Local.Loop.Interface\":\"FE\",\"prospect_2km_cust_Count\":55.0,\"Type\":\"OffnetRF\",\"longitude_final\":80.18774819999999,\"Orch_BW\":2.0,\"prospect_2km_Max_BW_Mbps\":20.0,\"otc_sify\":15000.0,\"Customer.Segment\":\"Enterprise - Strategic\",\"provider_min_dist\":0.7138602916391107,\"offnet_5km_Min_BW_Mbps\":0.128,\"prospect_name\":\"Kasturi & Sons Limited\",\"Predicted_Access_Feasibility\":\"Feasible\",\"avg_mast_ht\":9.696969696969697,\"last_mile_contract_term\":\"1 Year\",\"prospect_0_5km_feasibility_pct\":0.0,\"burstable_bw\":0,\"bw_mbps\":2,\"prospect_0_5km_Max_BW_Mbps\":0.0,\"offnet_0_5km_Min_accuracy_num\":0.0,\"product_name\":\"Internet Access Service\",\"closest_provider\":\"tikona_min_dist_km\",\"lm_arc_bw_onwl\":0.0,\"nearest_mast_ht_cost\":56400.0,\"account_id_with_18_digit\":\"0012000000E1z9KAAR\",\"Orch_Category\":\"Tikona\",\"lm_nrc_mux_onwl\":0.0,\"offnet_2km_Avg_BW_Mbps\":1.256,\"offnet_2km_Min_accuracy_num\":50.0,\"cust_count\":132.0,\"offnet_0_5km_Avg_BW_Mbps\":0.0,\"feasibility_response_created_date\":\"2018-07-18\",\"prospect_0_5km_Min_DistanceKilometers\":99999.0,\"additional_ip\":0,\"nearest_mast_ht\":18.0,\"cpe_variant\":\"None\",\"prospect_2km_Min_BW_Mbps\":0.064,\"Longitude_final\":80.18774819999999,\"Last.Mile.Contract.Term\":\"1 Year\",\"sum_no_of_sites_uni_len\":1,\"Product.Name\":\"Internet Access Service\",\"offnet_0_5km_Min_DistanceKilometers\":99999.0,\"offnet_0_5km_cust_Count\":0.0,\"prospect_0_5km_Avg_BW_Mbps\":0.0,\"Probabililty_Access_Feasibility\":0.9666666666666667,\"lm_nrc_bw_onrf\":0.0,\"quotetype_quote\":\"New Order\"}");
		return sFb;
	}

	public List<ConfigurationCpeInfo> getCpeInfo(){
		List<ConfigurationCpeInfo> list = new ArrayList<>();
		ConfigurationCpeInfo cpeInfo = new ConfigurationCpeInfo();
		cpeInfo.setCpetype("cpe");
		list.add(cpeInfo);
		return list;
	}

	public String getAddressDetailJSON() {
		return "{\"addressLineOne\":\"omkar 1988\",\"city\":\"Mumbai\",\"country\":\"INDIA\",\"locality\":\"Mumbai\",\"pincode\":\"400045\",\"source\":\"manual\",\"state\":\"MAHARASHTRA\",\"latLong\":\"19.0759837,72.8776559\"}";
	}

	public AddressDetail getaddressdetailNull(){
		AddressDetail dets = new AddressDetail();
		return dets;
	}

	public FeasibilityBean getFeasibilityBean() {
		FeasibilityBean feasibilityBean = new FeasibilityBean();
		feasibilityBean.setLegalEntityId(1);
		feasibilityBean.setLegalEntityName("enitity");
		feasibilityBean.setSites(getFeasibilitySiteEngineBeanList());
		return feasibilityBean;
	}

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

	public List<QuoteIzoSdwanAttributeValues> getIzoSdwanVal2(){
		List<QuoteIzoSdwanAttributeValues> values=new ArrayList<>();
		values.add(getIzoSdwanAttribute2());
		return values;
	}

	public QuoteIzoSdwanAttributeValues getIzoSdwanAttribute2() {
		QuoteIzoSdwanAttributeValues val=new QuoteIzoSdwanAttributeValues();
		val.setAttributeValue("true");
		val.setDisplayValue("No");
		return val;
	}

	public List<QuoteIzoSdwanAttributeValues> getIzoSdwanVal3(){
		List<QuoteIzoSdwanAttributeValues> values=new ArrayList<>();
		values.add(getIzoSdwanAttribute3());
		return values;
	}

	public QuoteIzoSdwanAttributeValues getIzoSdwanAttribute3() {
		QuoteIzoSdwanAttributeValues val=new QuoteIzoSdwanAttributeValues();
		val.setAttributeValue("isSWGProxy");
		val.setDisplayValue("cgwMigUserModifiedBW");
		return val;
	}


	public QuoteIzoSdwanAttributeValue getAttributeValueSdwan4() {
		QuoteIzoSdwanAttributeValue val=new QuoteIzoSdwanAttributeValue();
		val.setAttributeValue("IsUpload");
		val.setDisplayValue("cgwMigUserModifiedBW");
		return val;
	}

	public List<QuoteIzoSdwanAttributeValue> getAttributeValuesSdwancgw(){
		List<QuoteIzoSdwanAttributeValue> values=new ArrayList<>();
		values.add(getAttributeValueSdwan4());
		return values;
	}

//	public List<QuoteIzosdwanCgwDetail> getCgwDetail2() {
//		List<QuoteIzosdwanCgwDetail> quoteIzosdwanCgwDetails=new ArrayList<>();
//		QuoteIzosdwanCgwDetail cgwDetail=new QuoteIzosdwanCgwDetail();
//		cgwDetail.setHetroBw("56");
//		quoteIzosdwanCgwDetails.add(cgwDetail);
//		return quoteIzosdwanCgwDetails;
//	}

	public List<String> locs(){
		List<String> list = new ArrayList<>();
		list.add("loc1");
		list.add("loc2");
		return list;
	}
   
	public List<IzosdwanPricingService> getPricingListsss(){
		List<IzosdwanPricingService> list=new ArrayList<>();
		IzosdwanPricingService izosdwanPricingService=new IzosdwanPricingService();
		izosdwanPricingService.setId(1);
		izosdwanPricingService.setRefId("IZOSDWAN2004202R");
		izosdwanPricingService.setStatus("COMPLETED");
		izosdwanPricingService.setPriority(1);
		izosdwanPricingService.setServiceType("PRICING");
		list.add(izosdwanPricingService);
		return list;
		
	}

	public String CustomerLegalEntityDetailsBeanDets() throws TclCommonException {
		CustomerLegalEntityDetailsBean dets = new CustomerLegalEntityDetailsBean();
		dets.setCustomerLeDetails(customerLeBeanDetails());
		String json = Utils.convertObjectToJson(dets);
		return json;
	}

	public List<CustomerLeBean> customerLeBeanDetails(){
		List<CustomerLeBean> list = new ArrayList<>();
		CustomerLeBean dets = new CustomerLeBean();
		dets.setSfdcId("1");
		list.add(dets);
		return list;
	}

	public List<QuoteIzosdwanByonUploadDetail> getByonUploadDetailsStatus() {
		List<QuoteIzosdwanByonUploadDetail> uploadDetails = new ArrayList<>();
		QuoteIzosdwanByonUploadDetail uploadDet = new QuoteIzosdwanByonUploadDetail();
		uploadDet.setStatus("MIGRATED");
		uploadDetails.add(uploadDet);
		return uploadDetails;
	}
	public List<QuoteIzosdwanByonUploadDetail> getByonUploadDetailsStatus2() {
		List<com.tcl.dias.oms.entity.entities.QuoteIzosdwanByonUploadDetail> uploadDetails = new ArrayList<>();
		com.tcl.dias.oms.entity.entities.QuoteIzosdwanByonUploadDetail uploadDet = new com.tcl.dias.oms.entity.entities.QuoteIzosdwanByonUploadDetail();
		uploadDet.setStatus("COMPLETED");
		uploadDetails.add(uploadDet);
		return uploadDetails;
	}

	public IzosdwanQuoteAttributesUpdateBean getSdwanUpdateBean2() {
		IzosdwanQuoteAttributesUpdateBean bean=new IzosdwanQuoteAttributesUpdateBean();
		bean.setQuoteId(1);
		bean.setIzosdwanQuoteAttributesUpdateRequests(getUpdateRequestBeanForSdwan());
		return bean;
	}

	public VproxySolutionRequestBean getVproxySolutionDetails2() {
		VproxySolutionRequestBean solution=new VproxySolutionRequestBean();
		solution.setProductName("Cpe");
		solution.setQuoteId(1);
		solution.setQuoteLeId(1);
		solution.setVproxySolutionsBeans(VproxySolutionBeanDets());
		return solution;
	}

	public List<VproxySolutionBean> VproxySolutionBeanDets(){
		List<VproxySolutionBean> list = new ArrayList<>();
		VproxySolutionBean dets = new VproxySolutionBean();
		dets.setVproxyProductOfferingBeans(VproxyProductOfferingBeanDets());
		list.add(dets);
		return list;
	}

	public VproxyProductOfferingBean VproxyProductOfferingBeanDets(){
		VproxyProductOfferingBean dets = new VproxyProductOfferingBean();
		dets.setProductOfferingName("IZOSDWAN");
		return dets;
	}

    public QuoteIzoSdwanAttributeValues getIzoSdwanAttribute4() {
        QuoteIzoSdwanAttributeValues val=new QuoteIzoSdwanAttributeValues();
		val.setDisplayValue("isSWGVproxy");
        val.setAttributeValue("Yes");
        return val;
    }

    public List<QuoteIzoSdwanMssPricing> getQuoteIzoSdwanMssPricingDetailslist(){
	    List<QuoteIzoSdwanMssPricing> list = new ArrayList<>();
        QuoteIzoSdwanMssPricing dets = new QuoteIzoSdwanMssPricing();
        dets.setId("1");
        list.add(dets);
        return list;
    }
	public List<QuoteIzoSdwanAttributeValues> getIzoSdwanVal4(){
		List<QuoteIzoSdwanAttributeValues> values=new ArrayList<>();
		values.add(getIzoSdwanAttribute4());
		return values;
	}
    public QuoteIzoSdwanMssPricing getQuoteIzoSdwanMssPricingDetails(){
        QuoteIzoSdwanMssPricing dets = new QuoteIzoSdwanMssPricing();
        dets.setId("1");
        return dets;
    }


}
