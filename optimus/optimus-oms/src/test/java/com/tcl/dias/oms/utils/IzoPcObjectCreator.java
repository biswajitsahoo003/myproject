package com.tcl.dias.oms.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.Site;
import com.tcl.dias.oms.beans.SiteDetail;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.izopc.beans.ProductSolutionBean;
import com.tcl.dias.oms.izopc.beans.QuoteBean;
import com.tcl.dias.oms.izopc.beans.QuoteDetail;
import com.tcl.dias.oms.izopc.beans.QuoteToLeBean;
import com.tcl.dias.oms.izopc.beans.QuoteToLeProductFamilyBean;
import com.tcl.dias.oms.izopc.beans.SolutionDetail;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;



/**
 *  This class consist of the  mock values for the test cases
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class IzoPcObjectCreator extends ObjectCreator {
	
	
	public QuoteBean getQuoteBean_IZO() throws TclCommonException {
		QuoteBean quoteDto = new QuoteBean(getQuote());
		for (QuoteToLeBean quoteLeBean : quoteDto.getLegalEntities()) {
			quoteLeBean.setLegalAttributes(getLegalAttributeBeanList());
			quoteLeBean.setProductFamilies(getQuoteToLeProductFamilyBean_IZO());
			quoteLeBean.setFinalArc(Double.parseDouble("450356"));
			quoteLeBean.setFinalMrc(Double.parseDouble("450356"));
			quoteLeBean.setFinalNrc(Double.parseDouble("450356"));
			quoteLeBean.setSupplierLegalEntityId(1);
		}

		return quoteDto;

	}
	
	public Set<QuoteToLeProductFamilyBean> getQuoteToLeProductFamilyBean_IZO() throws TclCommonException {
		Set<QuoteToLeProductFamilyBean> quoteToLeProductFamilySet = new HashSet<>();
		QuoteToLeProductFamilyBean qFamily = new QuoteToLeProductFamilyBean();
		qFamily.setId(1);
		qFamily.setProductName("IZO Private Connnect");
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(1);
		mstProductFamily.setName("IZO Private Connnect");
		mstProductFamily.setStatus((byte) 2);
		qFamily.setSolutions(getProductSolutionBean_IZO());
		quoteToLeProductFamilySet.add(qFamily);
		return quoteToLeProductFamilySet;
	}
	
	public List<ProductSolutionBean> getProductSolutionBean_IZO() throws TclCommonException {

		List<ProductSolutionBean> productSolutions = new ArrayList<>();
		ProductSolutionBean productSolution = new ProductSolutionBean();

		productSolution.setProductSolutionId(1);
		productSolution.setOfferingName("Dual Unmanaged GVPN");

		productSolution.setProductProfileData(
				"{\"offeringName\":\"Dual Unmanaged GVPN\",\"image\":\"./assets/images/gvpn-profile-images/P-L-L.png\",\"components\":[{\"componentMasterId\":1,\"name\":\"VPN Port\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":1,\"name\":\"Interface\",\"value\":\"Fast Ethernet\"},{\"attributeMasterId\":29,\"name\":\"Port Bandwidth\",\"value\":\"12\"},{\"attributeMasterId\":48,\"name\":\"WAN IP Address\",\"value\":\"\"},{\"attributeMasterId\":3,\"name\":\"Service type\",\"value\":\"Fixed\"},{\"attributeMasterId\":4,\"name\":\"Burstable Bandwidth\",\"value\":\"\"},{\"attributeMasterId\":5,\"name\":\"Usage Model\",\"value\":\"\"},{\"attributeMasterId\":18,\"name\":\"Routing Protocol\",\"value\":\"BGP\"},{\"attributeMasterId\":16,\"name\":\"Extended LAN Required?\",\"value\":\"No\"},{\"attributeMasterId\":17,\"name\":\"BFD Required\",\"value\":\"No\"},{\"attributeMasterId\":21,\"name\":\"BGP Peering on\",\"value\":\"Loopback\"},{\"attributeMasterId\":37,\"name\":\"AS Number\",\"value\":\"TCL private AS Number\"},{\"attributeMasterId\":36,\"name\":\"WAN IP Provided By\",\"value\":\"TCL\"},{\"attributeMasterId\":51,\"name\":\"BGP AS Number\",\"value\":\"\"},{\"attributeMasterId\":52,\"name\":\"Customer prefixes\",\"value\":\"\"}]},{\"componentMasterId\":1,\"name\":\"VPN Port\",\"type\":\"secondary\",\"attributes\":[{\"attributeMasterId\":1,\"name\":\"Interface\",\"value\":\"Fast Ethernet\"},{\"attributeMasterId\":29,\"name\":\"Port Bandwidth\",\"value\":\"12\"},{\"attributeMasterId\":48,\"name\":\"WAN IP Address\",\"value\":\"\"},{\"attributeMasterId\":3,\"name\":\"Service type\",\"value\":\"Fixed\"},{\"attributeMasterId\":4,\"name\":\"Burstable Bandwidth\",\"value\":\"\"},{\"attributeMasterId\":5,\"name\":\"Usage Model\",\"value\":\"\"},{\"attributeMasterId\":18,\"name\":\"Routing Protocol\",\"value\":\"BGP\"},{\"attributeMasterId\":16,\"name\":\"Extended LAN Required?\",\"value\":\"No\"},{\"attributeMasterId\":17,\"name\":\"BFD Required\",\"value\":\"No\"},{\"attributeMasterId\":21,\"name\":\"BGP Peering on\",\"value\":\"Loopback\"},{\"attributeMasterId\":37,\"name\":\"AS Number\",\"value\":\"TCL private AS Number\"},{\"attributeMasterId\":36,\"name\":\"WAN IP Provided By\",\"value\":\"TCL\"},{\"attributeMasterId\":51,\"name\":\"BGP AS Number\",\"value\":\"\"},{\"attributeMasterId\":52,\"name\":\"Customer prefixes\",\"value\":\"\"}]},{\"componentMasterId\":2,\"name\":\"Last mile\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":22,\"name\":\"Shared Last Mile\",\"value\":\"No\"},{\"attributeMasterId\":23,\"name\":\"Shared Last Mile Service ID\",\"value\":\"\"},{\"attributeMasterId\":31,\"name\":\"Local Loop Bandwidth\",\"value\":\"12\"}]},{\"componentMasterId\":2,\"name\":\"Last mile\",\"type\":\"secondary\",\"attributes\":[{\"attributeMasterId\":22,\"name\":\"Shared Last Mile\",\"value\":\"No\"},{\"attributeMasterId\":23,\"name\":\"Shared Last Mile Service ID\",\"value\":\"\"},{\"attributeMasterId\":31,\"name\":\"Local Loop Bandwidth\",\"value\":\"12\"}]},{\"componentMasterId\":4,\"name\":\"CPE Management\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":27,\"name\":\"CPE Management Type\",\"value\":\"Unmanaged\"},{\"attributeMasterId\":28,\"name\":\"CPE Management\",\"value\":\"false\"}]},{\"componentMasterId\":4,\"name\":\"CPE Management\",\"type\":\"secondary\",\"attributes\":[{\"attributeMasterId\":27,\"name\":\"CPE Management Type\",\"value\":\"Unmanaged\"},{\"attributeMasterId\":28,\"name\":\"CPE Management\",\"value\":\"false\"}]},{\"componentMasterId\":5,\"name\":\"GVPN Common\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":2,\"name\":\"Service Variant\",\"value\":\"Enhanced\"},{\"attributeMasterId\":19,\"name\":\"isAuthenticationRequired for protocol\",\"value\":\"No\"},{\"attributeMasterId\":20,\"name\":\"Routes Exchanged\",\"value\":\"Default routes\"},{\"attributeMasterId\":35,\"name\":\"Port Mode\",\"value\":\"Active\"},{\"attributeMasterId\":38,\"name\":\"cos 1\",\"value\":\"0%\"},{\"attributeMasterId\":39,\"name\":\"cos 2\",\"value\":\"0%\"},{\"attributeMasterId\":40,\"name\":\"cos 3\",\"value\":\"100%\"},{\"attributeMasterId\":41,\"name\":\"cos 4\",\"value\":\"0%\"},{\"attributeMasterId\":42,\"name\":\"cos 5\",\"value\":\"0%\"},{\"attributeMasterId\":43,\"name\":\"cos 6\",\"value\":\"0%\"},{\"attributeMasterId\":44,\"name\":\"VPN Topology\",\"value\":\"Full Mesh\"},{\"attributeMasterId\":45,\"name\":\"Site Type\",\"value\":\"Mesh\"},{\"attributeMasterId\":7,\"name\":\"Resiliency\",\"value\":\"Yes\"},{\"attributeMasterId\":26,\"name\":\"Connector Type\",\"value\":\"LC\"},{\"attributeMasterId\":30,\"name\":\"Access Required\",\"value\":\"Yes\"},{\"attributeMasterId\":32,\"name\":\"CPE\",\"value\":\"Customer provided\"},{\"attributeMasterId\":50,\"name\":\"Access Topology\",\"value\":\"Resilient/Redundant\"}]},{\"componentMasterId\":5,\"name\":\"GVPN Common\",\"type\":\"secondary\",\"attributes\":[{\"attributeMasterId\":2,\"name\":\"Service Variant\",\"value\":\"Enhanced\"},{\"attributeMasterId\":19,\"name\":\"isAuthenticationRequired for protocol\",\"value\":\"No\"},{\"attributeMasterId\":20,\"name\":\"Routes Exchanged\",\"value\":\"Default routes\"},{\"attributeMasterId\":35,\"name\":\"Port Mode\",\"value\":\"Active\"},{\"attributeMasterId\":38,\"name\":\"cos 1\",\"value\":\"0%\"},{\"attributeMasterId\":39,\"name\":\"cos 2\",\"value\":\"0%\"},{\"attributeMasterId\":40,\"name\":\"cos 3\",\"value\":\"100%\"},{\"attributeMasterId\":41,\"name\":\"cos 4\",\"value\":\"0%\"},{\"attributeMasterId\":42,\"name\":\"cos 5\",\"value\":\"0%\"},{\"attributeMasterId\":43,\"name\":\"cos 6\",\"value\":\"0%\"},{\"attributeMasterId\":44,\"name\":\"VPN Topology\",\"value\":\"Full Mesh\"},{\"attributeMasterId\":45,\"name\":\"Site Type\",\"value\":\"Mesh\"},{\"attributeMasterId\":7,\"name\":\"Resiliency\",\"value\":\"No\"},{\"attributeMasterId\":26,\"name\":\"Connector Type\",\"value\":\"LC\"},{\"attributeMasterId\":30,\"name\":\"Access Required\",\"value\":\"Yes\"},{\"attributeMasterId\":32,\"name\":\"CPE\",\"value\":\"Customer provided\"},{\"attributeMasterId\":50,\"name\":\"Access Topology\",\"value\":\"Resilient/Redundant\"}]}]}");
		productSolution.setSites(getQuoteIllSiteBean());
		productSolution.setSolution(getSolutionDetail_IZO());
		productSolutions.add(productSolution);
		return productSolutions;
	}
	
	/**
	 * getSolutionDetail
	 * 
	 * @return
	 * @throws TclCommonException
	 */
	public SolutionDetail getSolutionDetail_IZO() throws TclCommonException {

		String solution = "{\"offeringName\":\"Dual Unmanaged GVPN\",\"image\":\"./assets/images/gvpn-profile-images/P-L-L.png\",\"components\":[{\"componentMasterId\":1,\"name\":\"VPN Port\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":1,\"name\":\"Interface\",\"value\":\"Fast Ethernet\"},{\"attributeMasterId\":29,\"name\":\"Port Bandwidth\",\"value\":\"12\"},{\"attributeMasterId\":48,\"name\":\"WAN IP Address\",\"value\":\"\"},{\"attributeMasterId\":3,\"name\":\"Service type\",\"value\":\"Fixed\"},{\"attributeMasterId\":4,\"name\":\"Burstable Bandwidth\",\"value\":\"\"},{\"attributeMasterId\":5,\"name\":\"Usage Model\",\"value\":\"\"},{\"attributeMasterId\":18,\"name\":\"Routing Protocol\",\"value\":\"BGP\"},{\"attributeMasterId\":16,\"name\":\"Extended LAN Required?\",\"value\":\"No\"},{\"attributeMasterId\":17,\"name\":\"BFD Required\",\"value\":\"No\"},{\"attributeMasterId\":21,\"name\":\"BGP Peering on\",\"value\":\"Loopback\"},{\"attributeMasterId\":37,\"name\":\"AS Number\",\"value\":\"TCL private AS Number\"},{\"attributeMasterId\":36,\"name\":\"WAN IP Provided By\",\"value\":\"TCL\"},{\"attributeMasterId\":51,\"name\":\"BGP AS Number\",\"value\":\"\"},{\"attributeMasterId\":52,\"name\":\"Customer prefixes\",\"value\":\"\"}]},{\"componentMasterId\":1,\"name\":\"VPN Port\",\"type\":\"secondary\",\"attributes\":[{\"attributeMasterId\":1,\"name\":\"Interface\",\"value\":\"Fast Ethernet\"},{\"attributeMasterId\":29,\"name\":\"Port Bandwidth\",\"value\":\"12\"},{\"attributeMasterId\":48,\"name\":\"WAN IP Address\",\"value\":\"\"},{\"attributeMasterId\":3,\"name\":\"Service type\",\"value\":\"Fixed\"},{\"attributeMasterId\":4,\"name\":\"Burstable Bandwidth\",\"value\":\"\"},{\"attributeMasterId\":5,\"name\":\"Usage Model\",\"value\":\"\"},{\"attributeMasterId\":18,\"name\":\"Routing Protocol\",\"value\":\"BGP\"},{\"attributeMasterId\":16,\"name\":\"Extended LAN Required?\",\"value\":\"No\"},{\"attributeMasterId\":17,\"name\":\"BFD Required\",\"value\":\"No\"},{\"attributeMasterId\":21,\"name\":\"BGP Peering on\",\"value\":\"Loopback\"},{\"attributeMasterId\":37,\"name\":\"AS Number\",\"value\":\"TCL private AS Number\"},{\"attributeMasterId\":36,\"name\":\"WAN IP Provided By\",\"value\":\"TCL\"},{\"attributeMasterId\":51,\"name\":\"BGP AS Number\",\"value\":\"\"},{\"attributeMasterId\":52,\"name\":\"Customer prefixes\",\"value\":\"\"}]},{\"componentMasterId\":2,\"name\":\"Last mile\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":22,\"name\":\"Shared Last Mile\",\"value\":\"No\"},{\"attributeMasterId\":23,\"name\":\"Shared Last Mile Service ID\",\"value\":\"\"},{\"attributeMasterId\":31,\"name\":\"Local Loop Bandwidth\",\"value\":\"12\"}]},{\"componentMasterId\":2,\"name\":\"Last mile\",\"type\":\"secondary\",\"attributes\":[{\"attributeMasterId\":22,\"name\":\"Shared Last Mile\",\"value\":\"No\"},{\"attributeMasterId\":23,\"name\":\"Shared Last Mile Service ID\",\"value\":\"\"},{\"attributeMasterId\":31,\"name\":\"Local Loop Bandwidth\",\"value\":\"12\"}]},{\"componentMasterId\":4,\"name\":\"CPE Management\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":27,\"name\":\"CPE Management Type\",\"value\":\"Unmanaged\"},{\"attributeMasterId\":28,\"name\":\"CPE Management\",\"value\":\"false\"}]},{\"componentMasterId\":4,\"name\":\"CPE Management\",\"type\":\"secondary\",\"attributes\":[{\"attributeMasterId\":27,\"name\":\"CPE Management Type\",\"value\":\"Unmanaged\"},{\"attributeMasterId\":28,\"name\":\"CPE Management\",\"value\":\"false\"}]},{\"componentMasterId\":5,\"name\":\"GVPN Common\",\"type\":\"primary\",\"attributes\":[{\"attributeMasterId\":2,\"name\":\"Service Variant\",\"value\":\"Enhanced\"},{\"attributeMasterId\":19,\"name\":\"isAuthenticationRequired for protocol\",\"value\":\"No\"},{\"attributeMasterId\":20,\"name\":\"Routes Exchanged\",\"value\":\"Default routes\"},{\"attributeMasterId\":35,\"name\":\"Port Mode\",\"value\":\"Active\"},{\"attributeMasterId\":38,\"name\":\"cos 1\",\"value\":\"0%\"},{\"attributeMasterId\":39,\"name\":\"cos 2\",\"value\":\"0%\"},{\"attributeMasterId\":40,\"name\":\"cos 3\",\"value\":\"100%\"},{\"attributeMasterId\":41,\"name\":\"cos 4\",\"value\":\"0%\"},{\"attributeMasterId\":42,\"name\":\"cos 5\",\"value\":\"0%\"},{\"attributeMasterId\":43,\"name\":\"cos 6\",\"value\":\"0%\"},{\"attributeMasterId\":44,\"name\":\"VPN Topology\",\"value\":\"Full Mesh\"},{\"attributeMasterId\":45,\"name\":\"Site Type\",\"value\":\"Mesh\"},{\"attributeMasterId\":7,\"name\":\"Resiliency\",\"value\":\"Yes\"},{\"attributeMasterId\":26,\"name\":\"Connector Type\",\"value\":\"LC\"},{\"attributeMasterId\":30,\"name\":\"Access Required\",\"value\":\"Yes\"},{\"attributeMasterId\":32,\"name\":\"CPE\",\"value\":\"Customer provided\"},{\"attributeMasterId\":50,\"name\":\"Access Topology\",\"value\":\"Resilient/Redundant\"}]},{\"componentMasterId\":5,\"name\":\"GVPN Common\",\"type\":\"secondary\",\"attributes\":[{\"attributeMasterId\":2,\"name\":\"Service Variant\",\"value\":\"Enhanced\"},{\"attributeMasterId\":19,\"name\":\"isAuthenticationRequired for protocol\",\"value\":\"No\"},{\"attributeMasterId\":20,\"name\":\"Routes Exchanged\",\"value\":\"Default routes\"},{\"attributeMasterId\":35,\"name\":\"Port Mode\",\"value\":\"Active\"},{\"attributeMasterId\":38,\"name\":\"cos 1\",\"value\":\"0%\"},{\"attributeMasterId\":39,\"name\":\"cos 2\",\"value\":\"0%\"},{\"attributeMasterId\":40,\"name\":\"cos 3\",\"value\":\"100%\"},{\"attributeMasterId\":41,\"name\":\"cos 4\",\"value\":\"0%\"},{\"attributeMasterId\":42,\"name\":\"cos 5\",\"value\":\"0%\"},{\"attributeMasterId\":43,\"name\":\"cos 6\",\"value\":\"0%\"},{\"attributeMasterId\":44,\"name\":\"VPN Topology\",\"value\":\"Full Mesh\"},{\"attributeMasterId\":45,\"name\":\"Site Type\",\"value\":\"Mesh\"},{\"attributeMasterId\":7,\"name\":\"Resiliency\",\"value\":\"No\"},{\"attributeMasterId\":26,\"name\":\"Connector Type\",\"value\":\"LC\"},{\"attributeMasterId\":30,\"name\":\"Access Required\",\"value\":\"Yes\"},{\"attributeMasterId\":32,\"name\":\"CPE\",\"value\":\"Customer provided\"},{\"attributeMasterId\":50,\"name\":\"Access Topology\",\"value\":\"Resilient/Redundant\"}]}]}";
		SolutionDetail res = (SolutionDetail) Utils.convertJsonToObject(solution, SolutionDetail.class);
		return res;
	}
	
	public QuoteToLeProductFamily getQuoteToLeProductFamily_IZO() {
		QuoteToLeProductFamily qFamily = new QuoteToLeProductFamily();
		qFamily.setId(1);
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(1);
		mstProductFamily.setName("IZO Private Connect");
		mstProductFamily.setStatus((byte) 2);
		qFamily.setMstProductFamily(mstProductFamily);
		qFamily.setProductSolutions(getProductSolution());
		return qFamily;
	}
	
	public QuoteProductComponent createQuoteProductComponent() {
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setId(1);
		quoteProductComponent.setReferenceId(1);
		quoteProductComponent.setMstProductComponent(getMstProductComponent());
		quoteProductComponent.setMstProductFamily(geProductFamily());
		/* quoteProductComponent.toString(); */
		quoteProductComponent.setQuoteProductComponentsAttributeValues(getQuoteComponentsAttributeValues());
		return quoteProductComponent;
	}

	
	public List<ProductAttributeMaster> getProductAtrributeList() {
		List<ProductAttributeMaster> list = new ArrayList<>();
		list.add(getProductAtrribute());

		return list;
	}
	
	
	public QuoteToLe getQuoteToLe_IZO() {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setId(1);
		quoteToLe.setCurrencyId(1);
		quoteToLe.setFinalMrc(45D);
		quoteToLe.setFinalNrc(67D);
		quoteToLe.setProposedMrc(78D);
		quoteToLe.setProposedNrc(98D);
		quoteToLe.setQuote(getQuote());
		quoteToLe.setErfCusCustomerLegalEntityId(1);
		quoteToLe.setErfCusSpLegalEntityId(1);
		quoteToLe.setQuoteLeAttributeValues(getQuoteLeAttributeValueSet());
		quoteToLe.setQuoteToLeProductFamilies(getQuoteFamilesWithoutProductSolutions());
		quoteToLe.setStage(QuoteStageConstants.SELECT_CONFIGURATION.getConstantCode());
		quoteToLe.setQuoteToLeProductFamilies(getQuoteToLeProductFamilies_IZO());
		return quoteToLe;
	}
	
	public Set<QuoteToLeProductFamily> getQuoteToLeProductFamilies_IZO() {
		Set<QuoteToLeProductFamily> quoteToLeProductFamilySet = new HashSet<>();
		QuoteToLeProductFamily qFamily = new QuoteToLeProductFamily();
		qFamily.setId(1);
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(1);
		mstProductFamily.setName("IZO Private Connect");
		mstProductFamily.setStatus((byte) 2);
		qFamily.setMstProductFamily(mstProductFamily);
		qFamily.setProductSolutions(getProductSolution());
		quoteToLeProductFamilySet.add(qFamily);
		return quoteToLeProductFamilySet;
	}
	/**
	 * 
	 * getQuoteDetail-mock values
	 * 
	 * @return QuoteDetail
	 */
	public QuoteDetail getQuoteDetail_IZO() {
		QuoteDetail quoteDetail = new QuoteDetail();
		/* quoteDetail.toString(); */
		quoteDetail.setCustomerId(1);
		quoteDetail.setProductName("IZOPC");
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
	
	public QuoteDetail getQuoteDetail2_IZO() {
		QuoteDetail quoteDetail = new QuoteDetail();
		quoteDetail.setProductName("IZOPC");
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
	 * getSite-mock values
	 * 
	 * @return List<Site>
	 */
	public List<Site> getSiteDetailsWithoutSiteId() {
		List<Site> siList = new ArrayList<>();
		SiteDetail siteDetail = new SiteDetail();
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
		Site site = new Site();
		site.setBandwidth("f");
		site.setOfferingName("IZO Private Connect");
		site.setBandwidthUnit("daf");
		site.setImage("adgdag");
		/* site.toString(); */
		site.setSite(getSitDetails());
		siList.add(site);

		return siList;
	}
	
	public Site getSiteWithoutSiteId() {
		SiteDetail siteDetail = new SiteDetail();
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
		Site site = new Site();
		site.setBandwidth("f");
		site.setOfferingName("IAS");
		site.setBandwidthUnit("daf");
		site.setImage("adgdag");
		/* site.toString(); */
		site.setSite(getSitDetails());

		return site;
	}
	
	public List<SiteDetail> getSitDetails() {
		List<SiteDetail> siteDetails = new ArrayList<>();
		SiteDetail detail = new SiteDetail();
		detail.setLocationId(1);
		detail.setLocationCode("123");
		detail.setComponents(getcompDetails());
		siteDetails.add(detail);

		return siteDetails;
	}

	
	public QuoteDetail getQuoteDetailWithoutSite() {
		QuoteDetail quoteDetail = new QuoteDetail();
		/* quoteDetail.toString(); */
		quoteDetail.setCustomerId(1);
		quoteDetail.setProductName("IZO Private Connect");
		quoteDetail.setQuoteleId(1);
		quoteDetail.setQuoteId(1);
		quoteDetail.setQuoteId(2);
		List<SolutionDetail> solutionDetails = new ArrayList<>();
		SolutionDetail solution = new SolutionDetail();
		solution.setBandwidth("20");
		solution.setBandwidthUnit("mbps");
		solution.setSiteDetail(getSitDetails());
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

}
