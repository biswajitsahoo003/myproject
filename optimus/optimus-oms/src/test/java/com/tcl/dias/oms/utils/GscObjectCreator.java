package com.tcl.dias.oms.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.tcl.dias.common.beans.Attributes;
import com.tcl.dias.common.beans.CustomerAttributeBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.SlaUpdateRequest;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStage;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStatus;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderGsc;
import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.entity.entities.OrderGscSla;
import com.tcl.dias.oms.entity.entities.OrderGscTfn;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteGscDetail;
import com.tcl.dias.oms.entity.entities.QuoteGscSla;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.QuoteUcaas;
import com.tcl.dias.oms.entity.entities.SlaMaster;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.gsc.beans.GscApiRequest;
import com.tcl.dias.oms.gsc.beans.GscDocumentBean;
import com.tcl.dias.oms.gsc.beans.GscProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteAttributesBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeSimpleValueBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.webex.beans.DeleteConfigurationBean;
import com.tcl.dias.oms.webex.util.WebexConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the GscObjectCreator.java class.
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GscObjectCreator {

	public final QuoteToLe quoteToLe = new QuoteToLe();
	public final ProductSolution productSolution = new ProductSolution();
	public final QuoteGsc quoteGsc = new QuoteGsc();
	public final QuoteGscDetail quotegscdetail = new QuoteGscDetail();
	public final QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
	public final QuoteToLeProductFamily quoteProductFamily = new QuoteToLeProductFamily();
	public final QuoteProductComponentsAttributeValue attributeValue = new QuoteProductComponentsAttributeValue();
	public final OrderGscDetail ordergscdetail = new OrderGscDetail();

	public UserInformation getUserInformation() {
		UserInformation userInformation = new UserInformation();
		userInformation.setUserId("admin");
		userInformation.setCustomers(getCustomerList());
		userInformation.setRole(Arrays.asList("OPTIMUS_MU"));
		return userInformation;

	}

	public List<CustomerDetail> getCustomerList() {
		UserInformation userInformation = new UserInformation();
		userInformation.setUserId("optimus_regus");
		List<CustomerDetail> list = new ArrayList<>();
		list.add(createCustomerDetails());
		list.add(createCustomerDetails());
		userInformation.setCustomers(list);
		return list;

	}

	public CustomerDetail createCustomerDetails() {
		CustomerDetail cd = new CustomerDetail();
		cd.setCustomerAcId("test");
		cd.setCustomerEmailId("test@gmail.com");
        cd.setCustomerName("testName");
		cd.setCustomerId(1);
		cd.setCustomerLeId(1);
		cd.setErfCustomerId(1);
		cd.setStatus((byte) 1);
		return cd;
	}

	public QuoteGscDetail getQuoteSummery() {
		QuoteGscDetail quoteGscDetail = new QuoteGscDetail();
		quoteGscDetail.setId(1);
		// quoteGscDetail.setListPriceMrcArc(1200f);
		// quoteGscDetail.setListPriceNrc(1200.f);
		return quoteGscDetail;
	}

	public List<QuoteGscDetail> getQuoteSummeryList() {
		List<QuoteGscDetail> list = new ArrayList<>();
		list.add(getQuoteSummery());
		return list;

	}

	/**
	 * getQuoteToLe-mock values
	 *
	 * @return {@link QuoteToLe}
	 */
	public QuoteToLe getGSCQuoteToLe() {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setId(1002);
		quoteToLe.setQuoteToLeProductFamilies(getGSCQuoteToLeProductFamily());
		return quoteToLe;
	}

	/**
	 * getProductSolution-mock values
	 *
	 * @return {@link ProductSolution}
	 */
	public Set<ProductSolution> getGSCProductSolution() {

		Set<ProductSolution> productSolutions = new HashSet<>();
		ProductSolution productSolution = new ProductSolution();
		productSolution.setId(1004);
		//		productSolution.setQuoteGsc(getGscList());
		//        productSolutions.add(productSolution);
		return productSolutions;
	}
	
	public ProductSolution getSingleProductSolution() {

		ProductSolution productSolution = new ProductSolution();
		productSolution.setId(1004);
		//		productSolution.setQuoteGsc(getGscList());
		//        productSolutions.add(productSolution);
		return productSolution;
	}

	public Set<QuoteGsc> getGscList() {
		Set<QuoteGsc> quoteGsc = new HashSet<>();
		quoteGsc.add(getQuoteGsc());
		return quoteGsc;
	}

	public List<QuoteToLeProductFamily> getGSCQuoteToLeProductFamilyList() {
		List<QuoteToLeProductFamily> quoteToLeProductFamilySet = new ArrayList<>();
		QuoteToLeProductFamily qFamily = new QuoteToLeProductFamily();
		qFamily.setId(1);
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(1);
		mstProductFamily.setName("GSIP");
		mstProductFamily.setStatus((byte) 2);
		qFamily.setMstProductFamily(mstProductFamily);
		qFamily.setProductSolutions(getGSCProductSolution());
		quoteToLeProductFamilySet.add(qFamily);
		return quoteToLeProductFamilySet;
	}

	public Set<QuoteToLeProductFamily> getGSCQuoteToLeProductFamily() {
		Set<QuoteToLeProductFamily> quoteToLeProductFamilySet = new HashSet<>();
		QuoteToLeProductFamily qFamily = new QuoteToLeProductFamily();
		qFamily.setId(1003);
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(2);
		mstProductFamily.setName("GSIP");
		qFamily.setMstProductFamily(mstProductFamily);
		qFamily.setProductSolutions(getGSCProductSolution());
		quoteToLeProductFamilySet.add(qFamily);
		return quoteToLeProductFamilySet;
	}
	
	public QuoteToLeProductFamily getQuoteToLeProductFamily() {
		QuoteToLeProductFamily qFamily = new QuoteToLeProductFamily();
		qFamily.setId(1003);
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(2);
		mstProductFamily.setName("GSIP");
		qFamily.setMstProductFamily(mstProductFamily);
		qFamily.setProductSolutions(getGSCProductSolution());
		return qFamily;
	}

	public QuoteGsc getQuoteGsc() {
		quoteGsc.setId(1005);
		quoteGsc.setStatus((byte) 1);
		quoteGsc.setAccessType("PSTN");
		quoteGsc.setQuoteToLe(getQuoteToLe());
		// quoteGsc.setQuote(createQuote());
		// quoteGsc.setQuoteGscDetails(getQuoteSummeryList());
		quoteGsc.setProductSolution(createProductSolutions());
		quoteGsc.setQuoteGscDetails(getQuoteGscDetails());
		// quoteGsc.setVariantName("ITFS");
		quoteGsc.setProductName("ITFS");
		return quoteGsc;
	}

	public List<QuoteGsc> createQuoteGscList() {
		List<QuoteGsc> quotegscList = new ArrayList<>();
		quotegscList.add(getQuoteGsc());
		return quotegscList;
	}

	public SlaUpdateRequest slaGscUpdateRequest() {
		SlaUpdateRequest slaUpdateRequest = new SlaUpdateRequest();
		slaUpdateRequest.setProductFamily("GSIP");
		slaUpdateRequest.setQuoteLeId(1);
		slaUpdateRequest.setSiteId(1);
		return slaUpdateRequest;
	}

	public List<QuoteGscSla> getQuoteGscSla() {
		List<QuoteGscSla> quoteGscSlaSet = new ArrayList<>();
		QuoteGscSla quoteGscSla = new QuoteGscSla();
		quoteGscSla.setQuoteGsc(getQuoteGsc());
		quoteGscSlaSet.add(quoteGscSla);
		return quoteGscSlaSet;
	}

	public OrderGscSla getorderGscSla() {
		OrderGscSla orderGscSla = new OrderGscSla();
		orderGscSla.setAttributeName("attr_name");
		orderGscSla.setSlaMaster(getSlaMaster());
		return orderGscSla;
	}
	
	public SlaMaster getSlaMaster() {
		SlaMaster slaMaster = new SlaMaster();
		slaMaster.setId(1);
		slaMaster.setSlaName("sla");
		slaMaster.setSlaDurationInDays(1);
		return slaMaster;
	}

	public Set<OrderGscSla> getorderGscSlaSet() {
		Set<OrderGscSla> orderGscSlas= new HashSet<>();
		orderGscSlas.add(getorderGscSla());
		return orderGscSlas;
	}

	public List<QuoteToLe> getQuoteToLeList() {
		List<QuoteToLe> quoteToLeList = new ArrayList<QuoteToLe>();
		quoteToLeList.add(getQuoteToLe());
		return quoteToLeList;
	}
	
	public Set<QuoteToLe> getQuoteToLeSet() {
		Set<QuoteToLe> quoteToLeList = new HashSet<QuoteToLe>();
		quoteToLeList.add(getQuoteToLe());
		return quoteToLeList;
	}

	public QuoteToLe getQuoteToLe() {
		quoteToLe.setId(1);
		quoteToLe.setCurrencyId(1);
		quoteToLe.setFinalMrc(45D);
		quoteToLe.setFinalNrc(67D);
		quoteToLe.setProposedMrc(78D);
		quoteToLe.setProposedNrc(98D);
		quoteToLe.setErfCusCustomerLegalEntityId(1);
		quoteToLe.setErfCusSpLegalEntityId(1);
		quoteToLe.setStage("Add Locations");
		quoteToLe.setTpsSfdcOptyId("12345");
		quoteToLe.setQuoteToLeProductFamilies(getQuoteToLeProductFamilies());
		quoteToLe.setStage(QuoteStageConstants.CHECKOUT.toString());
		return quoteToLe;
	}

	public Order getOrders() {
		Order orders = new Order();
		orders.setId(1);
		orders.setStatus((byte) 1);
		orders.setOrderCode("GSC12345678910");
		orders.setCustomer(getCustomer());
		orders.setStage("order created");
		orders.setQuote(getQuote());
		return orders;
	}

	public Quote getQuote() {
		Quote quote = new Quote();
		quote.setId(1);
		quote.setQuoteCode("TCXVE23CV");
		quote.setCreatedBy(0);
		quote.setEffectiveDate(new Date());
		quote.setStatus((byte) 1);
		quote.setTermInMonths(12);
		return quote;
	}
	public OrderToLe getOrderToLe() {
		OrderToLe orderToLe = new OrderToLe();
		orderToLe.setId(1);
		orderToLe.setOrder(getOrders());
		orderToLe.setFinalMrc(1234.0);
		orderToLe.setFinalArc(1234.0);
		orderToLe.setFinalNrc(1234.0);
		orderToLe.setOrderToLeProductFamilies(getOrderProductFamilyList());
		orderToLe.setOrdersLeAttributeValues(getOrdersLeAttributeArrayValueSet());
		orderToLe.setErfCusCustomerLegalEntityId(1);
		orderToLe.setOrder(getOrders());
		return orderToLe;
	}
	
	public List<OrderToLe> getOrderToLeList() {
		List<OrderToLe> orderToLes=new ArrayList<>();
		orderToLes.add(getOrderToLe());
		return orderToLes;
	}

	public List<QuoteLeAttributeValue> getQuoteLeAttributeValueList() {
		List<QuoteLeAttributeValue> quoteLeAttributeValueSet = new ArrayList<>();
		QuoteLeAttributeValue quoteLeAttributeValue = getQuoteLeAttributeValue();
		quoteLeAttributeValue
				.getMstOmsAttribute()
				.setName("TermInMonths");
		quoteLeAttributeValue.setQuoteToLe(getQuoteToLe());
		QuoteLeAttributeValue quoteLeAttributeValue1 = getQuoteLeAttributeValue();
		quoteLeAttributeValue1
				.getMstOmsAttribute()
				.setName("TermInMon");
		quoteLeAttributeValue1.setQuoteToLe(getQuoteToLe());
		quoteLeAttributeValueSet.add(quoteLeAttributeValue);
		quoteLeAttributeValueSet.add(quoteLeAttributeValue1);
		QuoteLeAttributeValue quoteLeAttributeValue2 = getQuoteLeAttributeValue();
		quoteLeAttributeValue2
				.getMstOmsAttribute()
				.setName("Payment Currency");
		quoteLeAttributeValue2.setQuoteToLe(getQuoteToLe());
		quoteLeAttributeValueSet.add(quoteLeAttributeValue);
		quoteLeAttributeValueSet.add(quoteLeAttributeValue2);
		return quoteLeAttributeValueSet;

	}
	
	public List<QuoteLeAttributeValue> getQuoteLeAttributeValueListForContactInfo(){
		List<QuoteLeAttributeValue> quoteLeAttributeValueSet = new ArrayList<>();
		List<String> attributesName = Arrays.asList("CONTACTNAME","CONTACTEMAIL","CONTACTID","CONTACTNO","DESIGNATION");
		attributesName.forEach(attr->{
			QuoteLeAttributeValue quoteLeAttributeValue = getQuoteLeAttributeValue();
			quoteLeAttributeValue
			.getMstOmsAttribute()
			.setName(attr);
	quoteLeAttributeValue.setQuoteToLe(getQuoteToLe());
	quoteLeAttributeValueSet.add(quoteLeAttributeValue);
		});
		return quoteLeAttributeValueSet;
	}
	
	public QuoteLeAttributeValue getQuoteLeAttributeValue() {
		QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
		quoteLeAttributeValue.setId(1);
		quoteLeAttributeValue.setAttributeValue("IAS");
		quoteLeAttributeValue.setMstOmsAttribute(getMstOmsAttribute());
		quoteLeAttributeValue.setDisplayValue("display Value");
		return quoteLeAttributeValue;

	}

	public MstOmsAttribute getMstOmsAttribute() {
		MstOmsAttribute mstOmsAttribute = new MstOmsAttribute();
		mstOmsAttribute.setId(1);
		mstOmsAttribute.setIsActive((byte) 1);
		mstOmsAttribute.setCreatedBy("Anandhi");
		mstOmsAttribute.setCreatedTime(new Date());
		mstOmsAttribute.setDescription("Description");
		mstOmsAttribute.setName("Description");
		return mstOmsAttribute;
	}
	
	public List<MstOmsAttribute> getMstOmsAttributeList() {
		return ImmutableList.of(getMstOmsAttribute());
	}

	public Optional<Quote> createOptionalQuote() {
		Quote quote = new Quote();
		quote.setId(1000);
		quote.setStatus((byte) 1);
		quote.setCustomer(getCustomer());
		return Optional.of(quote);
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
		return customer;
	}

	public Quote createQuote() {
		Quote quote = new Quote();
		quote.setId(1);
		quote.setStatus((byte) 1);
		quote.setCustomer(getCustomer());
		quote.setQuoteToLes(getQuoteToLeSet());
		quote.setQuoteCode("GSC12345678");
		return quote;
	}

	public QuoteToLe createQuoteToLe() {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setId(1000);
		quoteToLe.setQuote(createQuote());
		quoteToLe.setStage(QuoteStageConstants.CHECKOUT.toString());
		return quoteToLe;
	}

	public List<QuoteToLeProductFamily> getQuoteProductFamilies() {
		List<QuoteToLeProductFamily> quoteproductSolutionlist = new ArrayList<>();
		quoteProductFamily.setId(1);
		quoteProductFamily.toString();
		quoteProductFamily.setMstProductFamily(getMstProductFamily());
		quoteProductFamily.setProductSolutions(getProductSolution());
		quoteProductFamily.setQuoteToLe(getQuoteToLe());
		quoteproductSolutionlist.add(quoteProductFamily);
		return quoteproductSolutionlist;
	}

	public OrderToLeProductFamily getOrderProductFamily() {
		OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
		orderToLeProductFamily.setId(1);
		orderToLeProductFamily.toString();
		orderToLeProductFamily.setMstProductFamily(getMstProductFamily());
		orderToLeProductFamily.setOrderProductSolutions(getOrderProductSolutionSet());
		return orderToLeProductFamily;
	}

	public Set<OrderToLeProductFamily> getOrderProductFamilyList() {
		Set<OrderToLeProductFamily> orderToLeProductFamilyList = new HashSet<>();
		orderToLeProductFamilyList.add(getOrderProductFamily());
		return orderToLeProductFamilyList;
	}

	public MstProductFamily getMstProductFamily() {
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(1);
		mstProductFamily.setName("GSIP");
		mstProductFamily.setMstProductOfferings(getMstOfferings());
		return mstProductFamily;
	}

	public Set<ProductSolution> getProductSolution() {
		Set<ProductSolution> productSolutions = new HashSet<>();
		productSolution.setId(1);
		productSolution.setQuoteToLeProductFamily(quoteProductFamily);
		/*productSolution.setQuoteToLeProductFamily(getQuoteToLeFamily());*/
		productSolutions.add(productSolution);
		productSolution.setMstProductOffering(getMstOffering());
		productSolution.setProductProfileData("{\"solutionCode\": \"ITFSPSTN\"}");
		productSolution.setQuoteIllSites(getIllsitesList());
		return productSolutions;
	}
	
	public List<ProductSolution> getProductSolutionList() {
		List<ProductSolution> productSolutions = new ArrayList<>();
		productSolution.setId(1);
		productSolution.setQuoteToLeProductFamily(quoteProductFamily);
		/*productSolution.setQuoteToLeProductFamily(getQuoteToLeFamily());*/
		productSolutions.add(productSolution);
		productSolution.setMstProductOffering(getMstOffering());
		productSolution.setProductProfileData("{\"solutionCode\": \"ITFSPSTN\"}");
		return productSolutions;
	}

	public List<OrderProductSolution> getOrderProductSolutionList() {
		List<OrderProductSolution> list = new ArrayList<>();
		list.add(getOrderProductSolution());
		return list;
	}

	public OrderProductSolution getOrderProductSolution() {
		OrderProductSolution orderProductSolution = new OrderProductSolution();
		orderProductSolution.setId(1);
		orderProductSolution.setMstProductOffering(getMstOffering());
		orderProductSolution.setProductProfileData("{\"solutionCode\": \"ITFSPSTN\"}");
		return orderProductSolution;
	}

	public Set<OrderProductSolution> getOrderProductSolutionSet() {

		Set<OrderProductSolution> orderProductSolution = new HashSet<>();
		orderProductSolution.add(getOrderProductSolution());
		return orderProductSolution;
	}

	public Set<MstProductOffering> getMstOfferings() {
		Set<MstProductOffering> mstProductOfferings = new HashSet<>();
		mstProductOfferings.add(getMstOffering());
		return mstProductOfferings;
	}

	public MstProductOffering getMstOffering() {
		MstProductOffering mstProductOffering = new MstProductOffering();
		mstProductOffering.setId(1);
		mstProductOffering.setErfProductOfferingId(1);
		mstProductOffering.setProductDescription("ITFS with PSTN");
		mstProductOffering.setProductName("ITFS");
		mstProductOffering.setMstProductFamily(getProductFamily());
		return mstProductOffering;
	}

	public MstProductFamily getProductFamily() {
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(1);
		mstProductFamily.setName("Cpe");
		mstProductFamily.setStatus((byte) 0);
		return mstProductFamily;
	}

	public List<QuoteProductComponent> getQuoteProductComponent() {
		List<QuoteProductComponent> quoteProductComponents = new ArrayList<>();
		quoteProductComponent.setId(1);
		quoteProductComponent.setReferenceId(1);
		quoteProductComponent.setMstProductComponent(getMstProductComponent());
		quoteProductComponent.setMstProductFamily(getMstProductFamily());
		quoteProductComponents.add(quoteProductComponent);
		QuoteProductComponent quoteProductComponent1=new QuoteProductComponent();
		quoteProductComponent1.setId(2);
		MstProductComponent component=getMstProductComponent();
		component.setName("PSTN");
		quoteProductComponent1.setMstProductComponent(component);
		quoteProductComponent1.setMstProductFamily(getMstProductFamily());
		quoteProductComponent1.setReferenceId(2);
		quoteProductComponents.add(quoteProductComponent1);
		QuoteProductComponent quoteProductComponent2=new QuoteProductComponent();
		quoteProductComponent2.setId(3);
		MstProductComponent component2=getMstProductComponent();
		component2.setName("LNS");
		quoteProductComponent2.setMstProductComponent(component2);
		quoteProductComponent2.setMstProductFamily(getMstProductFamily());
		quoteProductComponent2.setReferenceId(3);
		quoteProductComponents.add(quoteProductComponent2);
		return quoteProductComponents;
	}
	
	public List<QuoteProductComponentsAttributeValue> getQuoteProductComponentAttributeValues() {
		List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = new ArrayList<>();
		QuoteProductComponentsAttributeValue attributeValue=new QuoteProductComponentsAttributeValue();
		attributeValue.setId(1);
		ProductAttributeMaster attribute=getProductAttributeMaster();
		attribute.setName("Rate per Minute(Fixed)");
		attribute.setId(1);
		attributeValue.setProductAttributeMaster(getProductAttributeMaster());
		attributeValue.setQuoteProductComponent(new QuoteProductComponent());
		attributeValue.setAttributeValues("");
		quoteProductComponentsAttributeValues.add(attributeValue);
		QuoteProductComponentsAttributeValue attributeValue1=new QuoteProductComponentsAttributeValue();
		attributeValue1.setId(2);
		ProductAttributeMaster attribute2=getProductAttributeMaster();
		attribute2.setName("Rate per Minute(special)");
		attribute2.setId(2);
		attributeValue1.setProductAttributeMaster(getProductAttributeMaster());
		attributeValue1.setQuoteProductComponent(new QuoteProductComponent());
		attributeValue1.setAttributeValues("");
		quoteProductComponentsAttributeValues.add(attributeValue1);
		QuoteProductComponentsAttributeValue attributeValue2=new QuoteProductComponentsAttributeValue();
		attributeValue2.setId(3);
		ProductAttributeMaster attribute3=getProductAttributeMaster();
		attribute3.setName("Rate per Minute(mobile)");
		attribute3.setId(3);
		attributeValue2.setProductAttributeMaster(getProductAttributeMaster());
		attributeValue2.setQuoteProductComponent(new QuoteProductComponent());
		attributeValue2.setAttributeValues("");
		quoteProductComponentsAttributeValues.add(attributeValue2);
		return quoteProductComponentsAttributeValues;
	}


	public QuotePrice getQuotePrice() {

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
		return quotePrice;
	}

	public List<QuoteProductComponentsAttributeValue> getQuoteComponentsAttributeValues() {
		List<QuoteProductComponentsAttributeValue> mockProductComponentAttr = new ArrayList<>();

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

	public QuoteGscDetail getQuoteGscDetail() {
		quotegscdetail.setId(1);
		quotegscdetail.setQuoteGsc(getQuoteGsc());
		return quotegscdetail;

	}

	public Set<OrderGscDetail> getOrderGscDetailSet() {
		Set<OrderGscDetail> ordergscdetaillist = new HashSet<>();
		ordergscdetaillist.add(getOrderGscDetail());
		return ordergscdetaillist;

	}

	public List<OrderGscDetail> getOrderGscDetailList() {
		List<OrderGscDetail> ordergscdetaillist = new ArrayList<>();
		ordergscdetaillist.add(getOrderGscDetail());
		return ordergscdetaillist;

	}

	public OrderGscDetail getOrderGscDetail() {
		ordergscdetail.setId(1);
		ordergscdetail.setArc((double) 1);
		ordergscdetail.setCreatedBy("1");
		ordergscdetail.setCreatedTime(new Timestamp((long) 1));
		ordergscdetail.setDest("india");
		ordergscdetail.setSrc("United States of America");
		ordergscdetail.setDestType("destType");
		ordergscdetail.setSrcType("srcType");
		ordergscdetail.setMrc((double) 1);
		ordergscdetail.setNrc((double) 1);
		ordergscdetail.setMstOrderSiteStage(getMstOrderSiteStage());
		ordergscdetail.setMstOrderSiteStatus(getMstOrderSiteStatus());
		ordergscdetail.setOrderGsc(getordergsc2());
		return ordergscdetail;

	}

	public List<QuoteGscDetail> getQuoteGscDetailSet() {
		List<QuoteGscDetail> quotegscdetaillist = new ArrayList<>();
		quotegscdetaillist.add(getQuoteGscDetail());
		return quotegscdetaillist;

	}

	public List<QuoteGsc> getQuoteGscList() {
		List<QuoteGsc> quotegscdetaillist = new ArrayList<>();
		quotegscdetaillist.add(getQuoteGsc());
		return quotegscdetaillist;

	}

	public ProductSolution createProductSolutions() {

		return getProductSolution()
				.stream()
				.findFirst()
				.get();
	}

	/**
	 * getQuoteToLeFamily-mock values
	 *
	 * @return {@link QuoteToLeProductFamily}
	 */
	public QuoteToLeProductFamily getQuoteToLeFamily() {
		quoteProductFamily.setId(1);
		quoteProductFamily.setMstProductFamily(getMstProductFamily());
		quoteProductFamily.setQuoteToLe(getQuoteToLe());
		quoteProductFamily.setProductSolutions(getProductSolution());
		return quoteProductFamily;
	}

	public OrderGsc getordergsc() {
		OrderGsc gsc = new OrderGsc();
		gsc.setAccessType("PSTN");
		gsc.setArc((double) 1);
		gsc.setNrc((double) 1);
		gsc.setImageUrl("url");
		gsc.setOrderProductSolution(getOrderProductSolution());
		gsc.setOrderGscDetails(getOrderGscDetailSet());
		gsc.setName("name");
		gsc.setCreatedBy("1");
		gsc.setProductName("LNS");
		gsc.setCreatedTime(new Timestamp((long) 1));
		return gsc;
	}
	
	public OrderGsc getordergsc2() {
		OrderGsc gsc = new OrderGsc();
		gsc.setAccessType("PSTN");
		gsc.setArc((double) 1);
		gsc.setNrc((double) 1);
		gsc.setImageUrl("url");
		gsc.setOrderProductSolution(getOrderProductSolution());
		//gsc.setOrderGscDetails(getOrderGscDetailSet());
		gsc.setName("name");
		gsc.setCreatedBy("1");
		gsc.setProductName("LNS");
		gsc.setCreatedTime(new Timestamp((long) 1));
		return gsc;
	}

	public Set<OrderGsc> getordergscList() {
		Set<OrderGsc> gsc = new HashSet<>();
		gsc.add(getordergsc());
		return gsc;
	}

	public Optional<OrderGsc> getordergscOptional() {
		OrderGsc gsc = new OrderGsc();
		gsc.setAccessType("pSTN");
		gsc.setArc((double) 1);
		gsc.setNrc((double) 1);
		gsc.setImageUrl("url");
		gsc.setOrderGscDetails(getOrderGscDetailSet());
		gsc.setOrderProductSolution(getOrderProductSolution());
		gsc.setName("name");
		gsc.setCreatedBy("1");
		return Optional.of(gsc);
	}

	public MstProductComponent getMstProductComponent() {
		MstProductComponent mstProductComponent = new MstProductComponent();
		mstProductComponent.setId(1);
		mstProductComponent.setName("ITFS");
		mstProductComponent.setStatus((byte) 1);
		return mstProductComponent;
	}
	
	public MstProductComponent getMstProductComponentPstn() {
		MstProductComponent mstProductComponent = new MstProductComponent();
		mstProductComponent.setId(1);
		mstProductComponent.setName("PSTN");
		mstProductComponent.setStatus((byte) 1);
		return mstProductComponent;
	}
	
	public MstProductComponent getMstProductComponentLns() {
		MstProductComponent mstProductComponent = new MstProductComponent();
		mstProductComponent.setId(1);
		mstProductComponent.setName("LNS");
		mstProductComponent.setStatus((byte) 1);
		return mstProductComponent;
	}

	public MstProductComponent getMstProductComponentAcans() {
		MstProductComponent mstProductComponent = new MstProductComponent();
		mstProductComponent.setId(1);
		mstProductComponent.setName("ACANS");
		mstProductComponent.setStatus((byte) 1);
		return mstProductComponent;
	}

	public List<MstProductComponent> getMstProductComponentList() {
		List<MstProductComponent> mstProductComponent = new ArrayList<>();
		mstProductComponent.add(getMstProductComponent());
		return mstProductComponent;
	}

	public OrderProductComponent getOrderProductComponent() {
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setId(1);
		orderProductComponent.setReferenceId(1);
		orderProductComponent.setMstProductComponent(getMstProductComponent());
		orderProductComponent.setMstProductFamily(getMstProductFamily());
		orderProductComponent.setOrderProductComponentsAttributeValues(getOrderProductComponentsAttributeValueSet());
		return orderProductComponent;
	}

	public Set<OrderProductComponent> getOrderProductComponentList() {
		Set<OrderProductComponent> orderProductComponent = new HashSet<>();
		orderProductComponent.add(getOrderProductComponent());
		return orderProductComponent;
	}

	public List<OrderProductComponent> getOrderProductComponentLists() {
		List<OrderProductComponent> orderProductComponent = new ArrayList<>();
		orderProductComponent.add(getOrderProductComponent());
		return orderProductComponent;
	}

	public ProductAttributeMaster getProductAttributeMaster() {
		ProductAttributeMaster productAttributeMaster = new ProductAttributeMaster();
		productAttributeMaster.setId(1);
		productAttributeMaster.setName("Rate per Minute(fixed)");
		return productAttributeMaster;
	}
	
	public ProductAttributeMaster getProductAttributeMaster2() {
		ProductAttributeMaster productAttributeMaster = new ProductAttributeMaster();
		productAttributeMaster.setId(2);
		productAttributeMaster.setName("Rate per Minute");
		return productAttributeMaster;
	}

	public List<ProductAttributeMaster> getProductAttributeMasterList() {
		List<ProductAttributeMaster> productAttributeMasterList = new ArrayList<>();
		productAttributeMasterList.add(getProductAttributeMaster());
		ProductAttributeMaster productAttributeMaster1 = new ProductAttributeMaster();
		productAttributeMaster1.setId(2);
		productAttributeMaster1.setName("Rate per Minute(special)");
		productAttributeMasterList.add(productAttributeMaster1);
		ProductAttributeMaster productAttributeMaster2 = new ProductAttributeMaster();
		productAttributeMaster2.setId(3);
		productAttributeMaster2.setName("Rate per Minute(mobile)");
		productAttributeMasterList.add(productAttributeMaster1);
		return productAttributeMasterList;
	}

	public OrderProductComponentsAttributeValue getOrderProductComponentsAttributeValue() {
		OrderProductComponentsAttributeValue orderProductComponentsAttributeValue = new OrderProductComponentsAttributeValue();
		orderProductComponentsAttributeValue.setId(1);
		orderProductComponentsAttributeValue.setAttributeValues("Interface");
		orderProductComponentsAttributeValue.setDisplayValue("");
		orderProductComponentsAttributeValue.setProductAttributeMaster(getProductAttributeMaster());
		return orderProductComponentsAttributeValue;
	}
	
	public OrderProductComponentsAttributeValue getOrderProductComponentsAttributeValue2() {
		OrderProductComponentsAttributeValue orderProductComponentsAttributeValue = new OrderProductComponentsAttributeValue();
		orderProductComponentsAttributeValue.setId(1);
		orderProductComponentsAttributeValue.setAttributeValues("Interface");
		orderProductComponentsAttributeValue.setDisplayValue("");
		orderProductComponentsAttributeValue.setProductAttributeMaster(getProductAttributeMaster2());
		return orderProductComponentsAttributeValue;
	}

	public List<OrderProductComponentsAttributeValue> getOrderProductComponentsAttributeValueList() {
		List<OrderProductComponentsAttributeValue> OrderProductComponentsAttributeValue = new ArrayList<>();
		OrderProductComponentsAttributeValue.add(getOrderProductComponentsAttributeValue());
		return OrderProductComponentsAttributeValue;
	}

	public List<OrderProductComponentsAttributeValue> getOrderProductComponentsAttributeValueLists() {
		List<OrderProductComponentsAttributeValue> OrderProductComponentsAttributeValue = new ArrayList<>();
		OrderProductComponentsAttributeValue.add(getOrderProductComponentsAttributeValue());
		OrderProductComponentsAttributeValue.add(getOrderProductComponentsAttributeValue2());
		return OrderProductComponentsAttributeValue;
	}

	public Set<OrderProductComponentsAttributeValue> getOrderProductComponentsAttributeValueSet() {
		Set<OrderProductComponentsAttributeValue> OrderProductComponentsAttributeValue = new HashSet<>();
		OrderProductComponentsAttributeValue.add(getOrderProductComponentsAttributeValue());
		return OrderProductComponentsAttributeValue;
	}

	public List<OrderGsc> getGscOrderList() {
		List<OrderGsc> orderGscsList = new ArrayList<>();
		OrderGsc orderGsc = new OrderGsc();
		orderGsc.setAccessType("pSTN");
		orderGsc.setArc((double) 1);
		orderGsc.setNrc((double) 1);
		orderGsc.setImageUrl("url");
		orderGsc.setOrderGscDetails(getOrderGscDetailSet());
		orderGsc.setOrderProductSolution(getOrderProductSolution());
		orderGsc.setMrc(5434.0);
		orderGsc.setNrc(1234.0);
		orderGsc.setArc(1345.0);
		orderGsc.setName("name");
		orderGsc.setCreatedBy("1");
		orderGsc.setCreatedTime(new Timestamp((long) 1));
		orderGsc.setOrderGscSlas(getorderGscSlaSet());
		orderGscsList.add(orderGsc);
		return orderGscsList;
	}

	public Set<OrderGscDetail> getorderGscDetailsSet() {
		Set<OrderGscDetail> gscDetails = new HashSet<>();
		return gscDetails;
	}

	public List<QuoteProductComponentsAttributeValue> getListOfQuoteProductComponentsAttributeValue() {
		List<QuoteProductComponentsAttributeValue> attributeValuesList = new ArrayList<>();
		attributeValue.setAttributeValues("Interface");
		attributeValue.setDisplayValue("");
		attributeValue.setId(198);
		attributeValue.setProductAttributeMaster(getProductAttributeMaster());
		attributeValue.setQuoteProductComponent(getQuoteProductComponent().get(0));
		attributeValuesList.add(attributeValue);
		return attributeValuesList;
	}

	public GscApiRequest<List<GscProductComponentBean>> getGscProductComponentBeanList() {
		GscApiRequest<List<GscProductComponentBean>> productComponentBeans = new GscApiRequest<>();
		List<GscProductComponentBean> componentBeans = new ArrayList<>();
		GscProductComponentBean componentBean = new GscProductComponentBean();
		componentBean.setId(20095);
		componentBean.setReferenceId(801);
		componentBean.setType("PSTN");
		componentBean.setProductFamily("GSIP");
		componentBean.setProductComponentName("ITFS");
		componentBean.setAttributes(getListOfAttributes());
		componentBeans.add(componentBean);
		productComponentBeans.setData(componentBeans);
		return productComponentBeans;
	}

	public List<GscQuoteProductComponentsAttributeValueBean> getListOfAttributes() {
		List<GscQuoteProductComponentsAttributeValueBean> attributeValueBeans = new ArrayList<>();
		GscQuoteProductComponentsAttributeSimpleValueBean attributeValueBean = new GscQuoteProductComponentsAttributeSimpleValueBean();
		attributeValueBean.setAttributeId(200);
		attributeValueBean.setAttributeName("Interface");
		attributeValueBean.setDescription("Interface");
		attributeValueBean.setDisplayValue("");
		attributeValueBeans.add(attributeValueBean);
		return attributeValueBeans;
	}
	
	public QuoteToLe getPricingQuoteLe()
	{
			quoteToLe.setId(1);
			quoteToLe.setCurrencyId(1);
			quoteToLe.setFinalMrc(45D);
			quoteToLe.setFinalNrc(67D);
			quoteToLe.setProposedMrc(78D);
			quoteToLe.setProposedNrc(98D);
			quoteToLe.setQuote(createQuote());
			quoteToLe.setErfCusCustomerLegalEntityId(1);
			quoteToLe.setErfCusSpLegalEntityId(1);
			quoteToLe.setStage("Add Locations");
		    quoteToLe.setQuoteToLeProductFamilies(getQuoteToLeProductFamilies());
			return quoteToLe;
		
	}
	
	public Set<QuoteToLeProductFamily> getQuoteToLeProductFamilies()
	{
		Set<QuoteToLeProductFamily> quoteToLeFamilies=new HashSet<>();
		quoteProductFamily.setId(1);
		quoteProductFamily.setMstProductFamily(getMstProductFamily());
		//quoteProductFamily.setQuoteToLe(getQuoteToLe());
		quoteProductFamily.setProductSolutions(getProductSolution());
		quoteToLeFamilies.add(quoteProductFamily);
		return quoteToLeFamilies;
	}
	
	public Set<QuoteGscDetail> getQuoteGscDetails() {
		Set<QuoteGscDetail> quoteGscDetails=new HashSet<>();
		quotegscdetail.setId(1);
		quotegscdetail.setArc(0D);
		quotegscdetail.setMrc(10D);
		quotegscdetail.setNrc(10D);
		quotegscdetail.setSrc("india");
		quotegscdetail.setDest("australia");
		quoteGscDetails.add(quotegscdetail);
		QuoteGscDetail quoteGscDetail2=new QuoteGscDetail();
		quoteGscDetail2.setId(2);
		quoteGscDetail2.setArc(0D);
		quoteGscDetail2.setMrc(10D);
		quoteGscDetail2.setNrc(10D);
		quoteGscDetail2.setSrc("india");
		quoteGscDetail2.setDest("Africa");
		quoteGscDetails.add(quoteGscDetail2);
		QuoteGscDetail quoteGscDetail3=new QuoteGscDetail();
		quoteGscDetail3.setId(3);
		quoteGscDetail3.setArc(0D);
		quoteGscDetail3.setMrc(10D);
		quoteGscDetail3.setNrc(10D);
		quoteGscDetail3.setSrc("india");
		quoteGscDetail3.setDest("Japan");
		quoteGscDetails.add(quoteGscDetail3);
		return quoteGscDetails;
	}

	/**
	 * createOrderAttributes
	 * @return
	 */
	public OrderProductComponentsAttributeValue createOrderAttributes() {
		OrderProductComponentsAttributeValue attributeValue = new OrderProductComponentsAttributeValue();
		attributeValue.setAttributeValues("Interface");
		attributeValue.setDisplayValue("");
		attributeValue.setId(198);
		attributeValue.setProductAttributeMaster(getProductAttributeMaster());
		attributeValue.setOrderProductComponent(getOrderProductComponent());
		return attributeValue;
	}

	public CustomerLeDetailsBean getCustomerLeDetailsBean() {
		CustomerLeDetailsBean customerLeDetailsBean = new CustomerLeDetailsBean();
		customerLeDetailsBean.setAccounCuId("1");
		customerLeDetailsBean.setAccountId("1");
		customerLeDetailsBean.setBillingContactId(1);
		customerLeDetailsBean.setLegalEntityName("test");
		customerLeDetailsBean.setAttributes(getAttributeList());
		return customerLeDetailsBean;
	}

	private List<Attributes> getAttributeList() {
		List<Attributes> attributes = new ArrayList<>();
		Attributes attribute = new Attributes();
		attribute.setAttributeName("Test");
		attribute.setAttributeValue("Test");
		attribute.setType("Test");
		attributes.add(attribute);
		return attributes;
	}

	public String getCustomerLeDetails() throws TclCommonException {
		return Utils.convertObjectToJson(getCustomerLeDetailsBean());
	}

	public List<MstOmsAttribute> getMstOmsAttributeListForCurrency(){
		List<MstOmsAttribute> attributes = new ArrayList<>();
		MstOmsAttribute attribute = new MstOmsAttribute();
		attribute.setId(14);
		attribute.setName("Payment Currency");
		attribute.setDescription("INR");
		attribute.setIsActive((byte)1);
		attribute.setCreatedBy("root");
		attributes.add(attribute);
		return attributes;
	}


	public CofDetails getCofDetails() {
		CofDetails cofDetails=new CofDetails();
		cofDetails.setId(1);
		return cofDetails;
	}
	
	public List<OrdersLeAttributeValue> getOrdersLeAttributeArrayValueList() {
		List<OrdersLeAttributeValue> ordersLeAttributeValueList = new ArrayList<>();
		ordersLeAttributeValueList.add(getOrdersLeAttributeValue());
		ordersLeAttributeValueList.add(getOrdersLeAttributeValue());
		return ordersLeAttributeValueList;
	}
	
	public Set<OrdersLeAttributeValue> getOrdersLeAttributeArrayValueSet() {
		Set<OrdersLeAttributeValue> ordersLeAttributeValueList = new HashSet<>();
		ordersLeAttributeValueList.add(getOrdersLeAttributeValue());
		ordersLeAttributeValueList.add(getOrdersLeAttributeValue());
		return ordersLeAttributeValueList;
	}
	
	public OrdersLeAttributeValue getOrdersLeAttributeValue() {

		OrdersLeAttributeValue slav = new OrdersLeAttributeValue();
		slav.setAttributeValue("Test");
		slav.setDisplayValue("Test");
		slav.setId(1);
		slav.setAttributeValue("rakesh");
		slav.setDisplayValue("Rakesh");
		slav.setMstOmsAttribute(getMstOmsAttribute());
		return slav;
	}

	public MstOrderSiteStage getMstOrderSiteStage() {
		MstOrderSiteStage mstOrderSiteStage=new MstOrderSiteStage();
		mstOrderSiteStage.setId(1);
		mstOrderSiteStage.setName("LM Delivery Intiated");
		return mstOrderSiteStage;
	}

	public MstOrderSiteStatus getMstOrderSiteStatus() {
		MstOrderSiteStatus mstOrderSiteStatus=new MstOrderSiteStatus();
		mstOrderSiteStatus.setId(1);
		mstOrderSiteStatus.setName("Order Placed");
		return mstOrderSiteStatus;
	}

	public GscQuoteAttributesBean getGscQuoteAttributesBean() {
		GscQuoteAttributesBean attributesBean = new GscQuoteAttributesBean();
		List<GscQuoteProductComponentsAttributeValueBean> attributes = new ArrayList<>();
		GscQuoteProductComponentsAttributeValueBean attributeValueBean = new GscQuoteProductComponentsAttributeValueBean();
		attributeValueBean.setAttributeId(30620);
		attributeValueBean.setAttributeName("TermInMonths");
		attributeValueBean.setDisplayValue("");
		attributes.add(attributeValueBean);
		attributesBean.setAttributes(attributes);
		return attributesBean;
	}

	public OrderToLeProductFamily getOrderToLeProductFamily() {
		OrderToLeProductFamily object = new OrderToLeProductFamily();
		object.setOrderToLe(getOrderToLe());
		object.setMstProductFamily(getMstProductFamily());
		object.setOrderProductSolutions(getOrderProductSolutionSet());
		object.setId(1);
		return object;
	}
	
	public RestResponse getRestResponse() {
		RestResponse response = new RestResponse();
	    response.setStatus(Status.SUCCESS);
	    return response;
	}

	public List<OrderProductComponent> getOrderProductComponentListsForLns() {
		List<OrderProductComponent> orderProductComponent = new ArrayList<>();
		orderProductComponent.add(getOrderProductComponentForLns());
		return orderProductComponent;
	}

	public List<OrderProductComponent> getOrderProductComponentListsForAcans() {
		List<OrderProductComponent> orderProductComponent = new ArrayList<>();
		orderProductComponent.add(getOrderProductComponentForAcans());
		return orderProductComponent;
	}

	public OrderProductComponent getOrderProductComponentForLns() {
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setId(1);
		orderProductComponent.setReferenceId(1);
		orderProductComponent.setMstProductComponent(getMstProductComponentLns());
		return orderProductComponent;
	}

	public OrderProductComponent getOrderProductComponentForAcans(){
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setId(1);
		orderProductComponent.setReferenceId(1);
		orderProductComponent.setMstProductComponent(getMstProductComponentAcans());
		return orderProductComponent;
	}

	public List<OrderProductComponentsAttributeValue> getOrderProductComponentsAttributeValueListForLns() {
		List<OrderProductComponentsAttributeValue> OrderProductComponentsAttributeValue = new ArrayList<>();
		OrderProductComponentsAttributeValue.add(getOrderProductComponentsAttributeValueForLnsAndAcans());
		return OrderProductComponentsAttributeValue;
	}

	public List<OrderProductComponentsAttributeValue> getOrderProductComponentsAttributeValueAcans() {
		List<OrderProductComponentsAttributeValue> OrderProductComponentsAttributeValue = new ArrayList<>();
		OrderProductComponentsAttributeValue.add(getOrderProductComponentsAttributeValueForLnsAndAcans());
		return OrderProductComponentsAttributeValue;
	}

	public OrderProductComponentsAttributeValue getOrderProductComponentsAttributeValueForLnsAndAcans() {
		OrderProductComponentsAttributeValue orderProductComponentsAttributeValue = new OrderProductComponentsAttributeValue();
		orderProductComponentsAttributeValue.setId(1);
		orderProductComponentsAttributeValue.setAttributeValues("CITYCODE:1:2:3:4");
		orderProductComponentsAttributeValue.setDisplayValue("CITYCODE:1:2:3:4");
		orderProductComponentsAttributeValue.setProductAttributeMaster(getProductAttributeMasterForLnsAndAcans());
		return orderProductComponentsAttributeValue;
	}

	public ProductAttributeMaster getProductAttributeMasterForLnsAndAcans() {
		ProductAttributeMaster productAttributeMaster = new ProductAttributeMaster();
		productAttributeMaster.setId(1);
		productAttributeMaster.setName("City wise Quantity Of Numbers");
		return productAttributeMaster;
	}

	public List<OrderGscTfn> getOrderGscTfnListForLns() {
		List<OrderGscTfn> orderGscTfns = new ArrayList<>();
		OrderGscTfn orderGscTfn = new OrderGscTfn();
		orderGscTfn.setOrderGscDetail(getOrderGscDetail());
		orderGscTfn.setCountryCode("COUNTRYCODE:CITYCODE");
		orderGscTfn.setTfnNumber("123456789");
		orderGscTfn.setIsPorted((byte)1);
		orderGscTfns.add(orderGscTfn);
		return orderGscTfns;
	}
	public List<OrderToLe> getOrderToLesList() {
		List<OrderToLe> list = new ArrayList<>();
		list.add(getOrderToLes());
		list.add(getOrderToLes());
		list.add(getOrderToLes());
		return list;
	}
	public OrderToLe getOrderToLes() {
		OrderToLe orderToLe = new OrderToLe();
		orderToLe.setId(1);
		orderToLe.setCurrencyId(1);
		orderToLe.setErfCusCustomerLegalEntityId(1);
		orderToLe.setErfCusSpLegalEntityId(1);
		orderToLe.setStage("stage");
		orderToLe.setFinalMrc(1.0);
		orderToLe.setFinalNrc(2.0);

		orderToLe.setOrder(getOrders());
		Set<OrdersLeAttributeValue> setAttri = new HashSet<>();
		setAttri.add(getOrdersLeAttributeValue());
		orderToLe.setOrdersLeAttributeValues(setAttri);
		//orderToLe.setOrderToLeProductFamilies(getOrderProductFamilyList());

		return orderToLe;
	}
	public OrderToLeProductFamily getorderToLeProductFamilies() {
		OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
		orderToLeProductFamily.setId(1);
		orderToLeProductFamily.setMstProductFamily(getMstProductFamily());
		Set<OrderProductSolution> set = new HashSet<>();
		set.add(getOrderProductSolution());
		orderToLeProductFamily.setOrderProductSolutions(set);
		orderToLeProductFamily.setOrderToLe(getOrderToLes());
		return orderToLeProductFamily;
	}
	public User getUser() {
		User user = new User();

		user.setCustomer(getCustomer());
		/* user.toString(); */
		user.setId(1);
		user.setEmailId("vivek@mail.com");
		user.setEmailId("abc@tata.com");
		user.setFirstName("abc");
		user.setUsername("abc");
		return user;
	}
	public String getCustomerDetailsBean() throws TclCommonException {
		CustomerDetailsBean customerDetailsBean = new CustomerDetailsBean();
		customerDetailsBean.setCustomerAttributes(getCustomerAttributeBean());
		String json = Utils.convertObjectToJson(customerDetailsBean);
		return json;
	}
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
	public Optional<QuoteToLe> returnQuoteToLeForUpdateStatus() {
		QuoteToLe le = new QuoteToLe();
		le.setId(1);
		le.setCurrencyId(1);
		le.setErfCusCustomerLegalEntityId(1);
		le.setErfCusSpLegalEntityId(1);
		le.setStage("Sample");
		le.setQuoteToLeProductFamilies(getQuoteToLeProductFamilies());
		le.setTpsSfdcOptyId("tpsSfdcOptyId");
		le.setQuote(getQuote());
		return Optional.of(le);
	}
	public List<MstOmsAttribute> getMstAttributeListWithNullAttr() {
		List<MstOmsAttribute> attributesList = new ArrayList<>();
		attributesList.add(null);
		return attributesList;
	}
	public List<User> getUserList(){
	List<User> users=new ArrayList<>();
	users.add(getUser());
	return users;
	}
	public List<Map<String, Object>> getProductLocations(){
		List<Map<String, Object>> productLocations=new ArrayList<>();
		Map<String, Object> value=new HashedMap<>();
		value.put("product_name", "GSC");
		value.put("origin", "originLocation");
		value.put("destination", "destinationLocation");
		productLocations.add(value);
		return productLocations;
	}
	public QuoteIllSite getIllsite() {
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
		site.setStatus((byte) 1);
		site.setErfLocSiteaLocationId(2);
		site.setErfLocSitebLocationId(2);
		site.setErfLocSitebSiteCode("2");
		return site;
	}
	public Set<QuoteIllSite> getIllsitesList() {
		Set<QuoteIllSite> value=new HashSet<>();
		value.add(getIllsite());
		return value;
		
	}

	public GscDocumentBean createDocumentBean() {
		GscDocumentBean documentBean = new GscDocumentBean();
		documentBean.setQuoteId(1);
		documentBean.setQuoteLeId(1234);
		documentBean.setCustomerId(223);
		documentBean.setProductName(WebexConstants.UCAAS_FAMILY_NAME);
		return documentBean;
	}

	public CustomerLeDetailsBean createCustomerLeDetailsBean() {
		CustomerLeDetailsBean customerLeDetailsBean = new CustomerLeDetailsBean();
		customerLeDetailsBean.setAccountId("123456789");
		customerLeDetailsBean.setAccounCuId("987654321");
		customerLeDetailsBean.setBillingContactId(12);
		return customerLeDetailsBean;
	}

	public DeleteConfigurationBean createDeleteConfigurationBean() {
		DeleteConfigurationBean deleteConfigurationBean = new DeleteConfigurationBean();
		deleteConfigurationBean.setConfigurationIds(new ArrayList<Integer>(1234));
		deleteConfigurationBean.setDealId("1234");
		return deleteConfigurationBean;
	}

	public QuoteUcaas createQuoteUcaasConfiguration() {
		QuoteUcaas quoteUcaas = new QuoteUcaas();
		quoteUcaas.setDealId(1234);
		quoteUcaas.setIsConfig((byte) 1);
		quoteUcaas.setQuantity(100);
		quoteUcaas.setId(1);
		return quoteUcaas;
	}

	public QuoteUcaas createUcaasQuote() {
		QuoteUcaas quoteUcaas = new QuoteUcaas();
		quoteUcaas.setDealId(1234);
		quoteUcaas.setAudioModel(WebexConstants.DEDICATED);
		quoteUcaas.setIsConfig((byte) 0);
		quoteUcaas.setQuantity(1000);
		quoteUcaas.setId(2);
		quoteUcaas.setDeal_status(CommonConstants.SUCCESS);
		return quoteUcaas;
	}

	public List<QuoteUcaas> getUcaasQuotes() {
		List<QuoteUcaas> ucaasQuotes = new ArrayList<>();
		ucaasQuotes.add(createQuoteUcaasConfiguration());
		ucaasQuotes.add(createUcaasQuote());
		return ucaasQuotes;
	}
}