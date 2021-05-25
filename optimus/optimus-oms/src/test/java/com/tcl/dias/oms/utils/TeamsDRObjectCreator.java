package com.tcl.dias.oms.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteDirectRouting;
import com.tcl.dias.oms.entity.entities.QuoteDirectRoutingCity;
import com.tcl.dias.oms.entity.entities.QuoteDirectRoutingMediaGateways;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteTeamsDR;
import com.tcl.dias.oms.entity.entities.QuoteTeamsLicense;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.teamsdr.util.TeamsDRConstants;

/**
 * This file contains the object creation methods
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Service
public class TeamsDRObjectCreator {

	public Set<QuoteToLe> getQuoteToLeSet() {
		Set<QuoteToLe> quoteToLeList = new HashSet<QuoteToLe>();
		quoteToLeList.add(getQuoteToLe(true));
		return quoteToLeList;
	}

	public QuoteToLe getQuoteToLe(boolean isNew) {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setId(1);
		quoteToLe.setCurrencyId(1);
		quoteToLe.setFinalMrc(45D);
		quoteToLe.setFinalNrc(67D);
		quoteToLe.setProposedMrc(78D);
		quoteToLe.setProposedNrc(98D);
		quoteToLe.setErfCusCustomerLegalEntityId(1);
		quoteToLe.setErfCusSpLegalEntityId(1);
		quoteToLe.setStage("Select Services");
		quoteToLe.setTpsSfdcOptyId("12345");
		quoteToLe.setTermInMonths("36 Months");
		quoteToLe.setQuote(createOptionalQuote().get());
		if (isNew)
			quoteToLe.setQuoteToLeProductFamilies(getQuoteToLeProductFamilies(!isNew));
		return quoteToLe;
	}

	public Set<QuoteToLeProductFamily> getQuoteToLeProductFamilies(boolean isNew) {
		QuoteToLeProductFamily quoteProductFamily = new QuoteToLeProductFamily();
		Set<QuoteToLeProductFamily> quoteToLeFamilies = new HashSet<>();
		quoteProductFamily.setId(1);
		if (isNew)
			quoteProductFamily.setQuoteToLe(getQuoteToLe(!isNew));
		quoteProductFamily.setMstProductFamily(getMstProductFamily());
		quoteProductFamily.setProductSolutions(getProductSolution());
		quoteToLeFamilies.add(quoteProductFamily);
		return quoteToLeFamilies;
	}

	public MstProductFamily getMstProductFamily() {
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(9);
		mstProductFamily.setName("TEAMSDR");
		mstProductFamily.setMstProductOfferings(getMstOfferings());
		return mstProductFamily;
	}

	public Set<MstProductOffering> getMstOfferings() {
		Set<MstProductOffering> mstProductOfferings = new HashSet<>();
		mstProductOfferings.add(getMstOffering());
		return mstProductOfferings;
	}

	public Set<ProductSolution> getProductSolution() {
		QuoteToLeProductFamily quoteProductFamily = new QuoteToLeProductFamily();
		quoteProductFamily.setId(1);
		ProductSolution productSolution = new ProductSolution();
		Set<ProductSolution> productSolutions = new HashSet<>();
		productSolution.setId(1);
		productSolution.setQuoteToLeProductFamily(quoteProductFamily);
		productSolutions.add(productSolution);
		productSolution.setMstProductOffering(getMstOffering());
		productSolution.setQuoteIllSites(getIllsitesList());
		productSolution.setProductProfileData("TEAMSDR");
		productSolution.setSolutionCode("UCDR010420XU9JYQ");
		return productSolutions;
	}

	public MstProductOffering getMstOffering() {
		MstProductOffering mstProductOffering = new MstProductOffering();
		mstProductOffering.setId(1);
		mstProductOffering.setErfProductOfferingId(1);
		mstProductOffering.setProductDescription("Media Gateway");
		mstProductOffering.setProductName("Media Gateway");
		mstProductOffering.setMstProductFamily(getProductFamily());
		return mstProductOffering;
	}

	public MstProductOffering getLicenseMstOffering() {
		MstProductOffering mstProductOffering = new MstProductOffering();
		mstProductOffering.setId(1);
		mstProductOffering.setErfProductOfferingId(1);
		mstProductOffering.setProductDescription("MS License");
		mstProductOffering.setProductName("MS License");
		mstProductOffering.setMstProductFamily(getProductFamily());
		return mstProductOffering;
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

	public MstProductFamily getProductFamily() {
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(1);
		mstProductFamily.setName("Cpe");
		mstProductFamily.setStatus((byte) 0);
		return mstProductFamily;
	}

	public Set<QuoteIllSite> getIllsitesList() {
		Set<QuoteIllSite> value = new HashSet<>();
		value.add(getIllsite());
		return value;

	}

	public Optional<Quote> createOptionalQuote() {
		Quote quote = new Quote();
		quote.setId(1000);
		quote.setStatus((byte) 1);
		quote.setCustomer(getCustomer());
		return Optional.of(quote);
	}

	public Customer getCustomer() {
		Customer customer = new Customer();
		customer.setCustomerCode("123");
		customer.setStatus((byte) 0);
		customer.setId(1);
		customer.setCustomerEmailId("test@gmail.com");
		customer.setErfCusCustomerId(1);
		customer.setCustomerName("test");
		return customer;
	}

	public Quote createQuote() {
		Quote quote = new Quote();
		quote.setId(1);
		quote.setStatus((byte) 1);
		quote.setCustomer(getCustomer());
		quote.setQuoteToLes(getQuoteToLeSet());
		quote.setQuoteCode("UCDR010420XU9JYQ");
		return quote;
	}

	public List<QuoteToLe> getQuoteToLeList() {
		List<QuoteToLe> quoteToLeList = new ArrayList<QuoteToLe>();
		quoteToLeList.add(getQuoteToLe(true));
		return quoteToLeList;
	}

	public QuoteDirectRoutingCity getCity() {
		QuoteDirectRoutingCity quoteDirectRoutingCity = new QuoteDirectRoutingCity();
		quoteDirectRoutingCity.setId(1);
		quoteDirectRoutingCity.setMediaGatewayType("NONE");
		quoteDirectRoutingCity.setName("chennai");
		return quoteDirectRoutingCity;
	}

	public List<QuoteDirectRoutingCity> getCityDetails() {
		List<QuoteDirectRoutingCity> cities = new ArrayList<>();
		cities.add(getCity());
		cities.add(getCity());
		return cities;
	}

	public QuoteDirectRouting getQuoteDirectRouting() {
		QuoteDirectRouting quoteDirectRouting = new QuoteDirectRouting();
		quoteDirectRouting.setId(1);
		quoteDirectRouting.setCountry("India");
		quoteDirectRouting.setQuoteDirectRoutingCityDetails(getCityDetails());
		return quoteDirectRouting;

	}

	public List<QuoteDirectRouting> getQuoteDirectRoutings() {
		List<QuoteDirectRouting> quoteDirectRoutings = new ArrayList<>();
		quoteDirectRoutings.add(getQuoteDirectRouting());
		quoteDirectRoutings.add(getQuoteDirectRouting());
		return quoteDirectRoutings;
	}

	public User getUser() {
		User user = new User();
		user.setId(111);
		user.setUsername("test");
		user.setContactNo("1234567890");
		user.setDesignation("test");
		user.setCustomer(getCustomer());
		user.setEmailId("cts@legomail.com");
		user.setFirstName("testFirstName");
		user.setLastName("testLastName");
		return user;
	}

	public MstProductOffering getCustomPlanOffering() {
		MstProductOffering mstProductOffering = new MstProductOffering();
		mstProductOffering.setId(1);
		mstProductOffering.setErfProductOfferingId(1);
		mstProductOffering.setProductDescription("Media Gateway");
		mstProductOffering.setProductName("Media Gateway");
		mstProductOffering.setMstProductFamily(getProductFamily());
		return mstProductOffering;
	}

	public List<QuoteTeamsDR> getQuoteTeamsDRs() {
		List<QuoteTeamsDR> quoteTeamsDRs = new ArrayList<>();
		QuoteTeamsDR quoteTeamsDR = new QuoteTeamsDR();
		quoteTeamsDR.setId(1);
		quoteTeamsDR.setProfileName(TeamsDRConstants.CUSTOM_PLAN);
		quoteTeamsDR.setNoOfUsers(100);
		quoteTeamsDR.setServiceName("GSIP Voice");
		quoteTeamsDRs.add(quoteTeamsDR);
		return quoteTeamsDRs;
	}

	public ProductSolution createProductSolution() {
		QuoteToLeProductFamily quoteProductFamily = new QuoteToLeProductFamily();
		quoteProductFamily.setId(1);
		ProductSolution productSolution = new ProductSolution();
		productSolution.setId(1);
		productSolution.setQuoteToLeProductFamily(quoteProductFamily);
		productSolution.setProductProfileData(TeamsDRConstants.MICROSOFT_CLOUD_SOLUTIONS);
		productSolution.setMstProductOffering(getMstOffering());
		productSolution.setSolutionCode("UCDR2910195VXKJH");
		return productSolution;
	}

	public RestResponse getRestResponseFailure() {
		RestResponse restResponse = new RestResponse();
		restResponse.setStatus(Status.FAILURE);
		return restResponse;
	}

	public QuoteTeamsLicense createQuoteTeamLicense() {
		QuoteTeamsLicense quoteTeamsLicense = new QuoteTeamsLicense();
		quoteTeamsLicense.setId(1);
		quoteTeamsLicense.setContractPeriod(TeamsDRConstants.TWELVE_MONTHS);
		quoteTeamsLicense.setNoOfLicenses(10);
		quoteTeamsLicense.setProvider("Microsoft");
		quoteTeamsLicense.setAgreementType("Corporate");
		return quoteTeamsLicense;
	}

	public List<QuoteTeamsLicense> getQuoteTeamsLicense() {
		List<QuoteTeamsLicense> quoteTeamsLicenses = new ArrayList<>();
		quoteTeamsLicenses.add(createQuoteTeamLicense());
		quoteTeamsLicenses.add(createQuoteTeamLicense());
		return quoteTeamsLicenses;
	}

	public QuoteDirectRoutingMediaGateways createMediagateway() {
		QuoteDirectRoutingMediaGateways quoteDirectRoutingMediaGateways = new QuoteDirectRoutingMediaGateways();
		quoteDirectRoutingMediaGateways.setId(1);
		quoteDirectRoutingMediaGateways.setName("test");
		quoteDirectRoutingMediaGateways.setQuantity(10);
		return quoteDirectRoutingMediaGateways;
	}

	public List<QuoteDirectRoutingMediaGateways> getMediaGateways() {
		List<QuoteDirectRoutingMediaGateways> mediaGateways = new ArrayList<>();
		mediaGateways.add(createMediagateway());
		mediaGateways.add(createMediagateway());
		return mediaGateways;
	}

	public List<ProductSolution> getProductSolutions(){
		List<ProductSolution> productSolutions = new ArrayList<>();
		productSolutions.add(createProductSolution());
		ProductSolution productSolution = createProductSolution();
		productSolution.setMstProductOffering(getLicenseMstOffering());
		productSolutions.add(productSolution);
		return productSolutions;
	}

	public MstProductComponent getMstProductComponent(){
		MstProductComponent mstProductComponent = new MstProductComponent();
		mstProductComponent.setDescription("test");
		mstProductComponent.setName("test attr");
		mstProductComponent.setStatus((byte) 1);
		mstProductComponent.setId(12);
		return mstProductComponent;
	}

	public QuoteProductComponent getQuoteProductComponent(String referenceName){
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setMstProductFamily(getMstProductFamily());
		quoteProductComponent.setMstProductComponent(getMstProductComponent());
		quoteProductComponent.setReferenceId(123);
		quoteProductComponent.setReferenceName(referenceName);
		return quoteProductComponent;
	}

	public ProductAttributeMaster getProductAttributeMaster(){
		ProductAttributeMaster productAttributeMaster = new ProductAttributeMaster();
		productAttributeMaster.setName("test");
		productAttributeMaster.setDescription("test desc");
		productAttributeMaster.setCategory("test category");
		productAttributeMaster.setStatus(CommonConstants.BACTIVE);
		productAttributeMaster.setId(12);
		return productAttributeMaster;
	}

	public List<QuoteProductComponentsAttributeValue> getQuoteProductComponentAttrValues(String referenceName){
		List<QuoteProductComponentsAttributeValue> attributeValues = new ArrayList<>();
		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue =
				new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue.setId(12);
		quoteProductComponentsAttributeValue.setQuoteProductComponent(getQuoteProductComponent(referenceName));
		quoteProductComponentsAttributeValue.setProductAttributeMaster(getProductAttributeMaster());
		return attributeValues;
	}

	public MstOmsAttribute getMstOmsAttribute(String name) {
		MstOmsAttribute mstOmsAttribute = new MstOmsAttribute();
		mstOmsAttribute.setId(3);
		mstOmsAttribute.setCreatedBy("test");
		mstOmsAttribute.setCreatedTime(new Date());
		mstOmsAttribute.setIsActive((byte) 1);
		mstOmsAttribute.setDescription(name);
		mstOmsAttribute.setName(name);
		mstOmsAttribute.setCategory("NEW");
		return mstOmsAttribute;
	}

	public QuoteLeAttributeValue getQuoteLeAttributeValue(String attributeName, String attributeValue) {
		QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
		quoteLeAttributeValue.setId(2);
		quoteLeAttributeValue.setAttributeValue(attributeValue);
		quoteLeAttributeValue.setDisplayValue(attributeValue);
		quoteLeAttributeValue.setQuoteToLe(getQuoteToLe(false));
		if (Objects.isNull(quoteLeAttributeValue.getMstOmsAttribute()))
			quoteLeAttributeValue.setMstOmsAttribute(getMstOmsAttribute(attributeName));
		return quoteLeAttributeValue;
	}

	public List<QuoteLeAttributeValue> getQuoteToLeAttributeValues() {
		List<String> attributes = Arrays.asList(LeAttributesConstants.LE_STATE_GST_ADDRESS,
				LeAttributesConstants.GST_ADDR, LeAttributesConstants.LE_STATE_GST_NO, LeAttributesConstants.GST_NUMBER,
				LeAttributesConstants.LE_STATE_GST_ADDRESS, CommonConstants.INDIA_INTERNATIONAL_SITES,
				CommonConstants.INTERNATIONAL_SITES, LeAttributesConstants.LEGAL_ENTITY_NAME,
				LeAttributesConstants.VAT_NUMBER, LeAttributesConstants.CONTACT_NAME, LeAttributesConstants.CONTACT_NO,
				LeAttributesConstants.CONTACT_EMAIL, LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY,
				LeAttributesConstants.LE_NAME, LeAttributesConstants.LE_CONTACT, LeAttributesConstants.LE_EMAIL,
				LeAttributesConstants.BILLING_METHOD, LeAttributesConstants.BILLING_TYPE);
		List<QuoteLeAttributeValue> quoteLeAttributeValues = new ArrayList<QuoteLeAttributeValue>();
		attributes.forEach(attribute -> quoteLeAttributeValues.add(getQuoteLeAttributeValue(attribute, attribute)));
		return quoteLeAttributeValues;
	}

	public OrdersLeAttributeValue getOrderLeAttributeValue(String attributeName, String attributeValue, boolean isNew) {
		OrdersLeAttributeValue ordersLeAttributeValue = new OrdersLeAttributeValue();
		if (isNew)
			ordersLeAttributeValue.setOrderToLe(getOrderToLeWithAttributeValues(!isNew));
		ordersLeAttributeValue.setId(10);
		ordersLeAttributeValue.setMstOmsAttribute(getMstOmsAttribute(attributeName));
		ordersLeAttributeValue.setAttributeValue(attributeValue);
		ordersLeAttributeValue.setDisplayValue(attributeValue);
		return ordersLeAttributeValue;
	}

	public Set<OrdersLeAttributeValue> getOrderLeAttributeValues(boolean isNew) {
		List<String> attributes = Arrays.asList(LeAttributesConstants.LE_STATE_GST_ADDRESS,
				LeAttributesConstants.GST_ADDR, LeAttributesConstants.LE_STATE_GST_NO, LeAttributesConstants.GST_NUMBER,
				LeAttributesConstants.LE_STATE_GST_ADDRESS, CommonConstants.INDIA_INTERNATIONAL_SITES,
				CommonConstants.INTERNATIONAL_SITES, LeAttributesConstants.LEGAL_ENTITY_NAME,
				LeAttributesConstants.VAT_NUMBER, LeAttributesConstants.CONTACT_NAME, LeAttributesConstants.CONTACT_NO,
				LeAttributesConstants.CONTACT_EMAIL, LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY,
				LeAttributesConstants.LE_NAME, LeAttributesConstants.LE_CONTACT, LeAttributesConstants.LE_EMAIL,
				LeAttributesConstants.BILLING_METHOD, LeAttributesConstants.BILLING_TYPE);
		Set<OrdersLeAttributeValue> ordersLeAttributeValues = new HashSet<>();
		attributes.forEach(
				attribute -> ordersLeAttributeValues.add(getOrderLeAttributeValue(attribute, attribute, isNew)));
		return ordersLeAttributeValues;
	}

	public OrderToLe getOrderToLe(boolean isNew) {
		OrderToLe orderToLe = new OrderToLe();
		orderToLe.setOrderType("NEW");
		if (isNew)
			orderToLe.setOrder(getOrder(!isNew));
		orderToLe.setOrderCategory("NEW");
		orderToLe.setTpsSfdcCopfId("12345");
		orderToLe.setErfCusCustomerLegalEntityId(536);
		orderToLe.setTermInMonths("12 months");
		orderToLe.setOrdersLeAttributeValues(new HashSet<>());
		return orderToLe;
	}

	public OrderToLe getOrderToLeWithAttributeValues(boolean isNew) {
		OrderToLe orderToLe = new OrderToLe();
		orderToLe.setOrderType("NEW");
		if (isNew)
			orderToLe.setOrder(getOrderWithAttributes(!isNew));
		orderToLe.setOrderCategory("NEW");
		orderToLe.setTpsSfdcCopfId("12345");
		orderToLe.setErfCusCustomerLegalEntityId(536);
		orderToLe.setTermInMonths("12 months");
		if (isNew)
			orderToLe.setOrdersLeAttributeValues(getOrderLeAttributeValues(!isNew));
		return orderToLe;
	}

	public Order getOrder(boolean isNew) {
		Order order = new Order();
		order.setId(1);
		if (isNew)
			order.setOrderToLes(Collections.singleton(getOrderToLe(!isNew)));
		order.setStatus((byte) 1);
		order.setQuote(createQuote());
		order.setCreatedBy(536);
		order.setCreatedTime(new Date());
		order.setCustomer(getCustomer());
		order.setEffectiveDate(new Date());
		order.setOrderCode("UCDR" + Utils.generateUid());
		order.setTermInMonths(12);
		return order;
	}

	public Order getOrderWithAttributes(boolean isNew) {
		Order order = new Order();
		order.setId(1);
		if (isNew)
			order.setOrderToLes(Collections.singleton(getOrderToLeWithAttributeValues(!isNew)));
		order.setStatus((byte) 1);
		order.setQuote(createQuote());
		order.setCreatedBy(536);
		order.setCreatedTime(new Date());
		order.setCustomer(getCustomer());
		order.setEffectiveDate(new Date());
		order.setOrderCode("UCDR" + Utils.generateUid());
		order.setTermInMonths(12);
		return order;
	}

	public OrderProductComponent getOrderProductComponent(boolean isNew) {
		OrderProductComponent productComponent = new OrderProductComponent();
		productComponent.setType(TeamsDRConstants.ATTRIBUTES);
		return productComponent;
	}

	public OrderToLeProductFamily getOrderToLeProductFamily(boolean isNew) {
		OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
		if (isNew)
			orderToLeProductFamily.setOrderToLe(getOrderToLe(isNew));
		orderToLeProductFamily.setMstProductFamily(getMstProductFamily());
		orderToLeProductFamily.setId(10);
		return orderToLeProductFamily;
	}
}
