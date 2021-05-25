package com.tcl.dias.oms.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.tcl.dias.common.beans.AttachmentBean;
import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.CustomerLeContactDetailBean;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.SPDetails;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.redis.service.AuthTokenService;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoGVPNBean;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.common.webex.beans.SIExistingGVPNBean;
import com.tcl.dias.common.webex.beans.SkuDetailsResponseBean;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.cisco.beans.ShowQuoteType;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStage;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStatus;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderGsc;
import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.entity.entities.OrderGscTfn;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderSiteFeasibility;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrderUcaas;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteDelegation;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteGscDetail;
import com.tcl.dias.oms.entity.entities.QuoteGscTfn;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.QuoteUcaas;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeSimpleValueBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.pricing.beans.GscOutboundPriceBean;
import com.tcl.dias.oms.gsc.util.GscAttributeConstants;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.webex.beans.WebexPricingResponse;
import com.tcl.dias.oms.webex.util.WebexConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.javaswift.joss.client.mock.AccountMock;
import org.javaswift.joss.client.mock.ContainerMock;
import org.javaswift.joss.client.mock.StoredObjectMock;
import org.javaswift.joss.model.StoredObject;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
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

import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.INPROGRESS;
import static com.tcl.dias.oms.gsc.GscTestUtil.fromJsonFile;

/**
 * This file contains the creating objects.
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class WebexObjectCreator {

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

	public List<CustomerDetail> getCustomerList() {
		UserInformation userInformation = new UserInformation();
		userInformation.setUserId("optimus_regus");
		List<CustomerDetail> list = new ArrayList<>();
		list.add(createCustomerDetails());
		list.add(createCustomerDetails());
		userInformation.setCustomers(list);
		return list;

	}

	public List<QuoteToLe> getQuoteToLeList() {
		List<QuoteToLe> quoteToLeList = new ArrayList<QuoteToLe>();
		quoteToLeList.add(getQuoteToLe());
		return quoteToLeList;
	}

	public QuoteToLe getQuoteToLe1() {
		QuoteToLe quoteToLe = new QuoteToLe();
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
		quoteToLe.setTermInMonths("12 Months");
		quoteToLe.setQuote(createOptionalQuote().get());
		return quoteToLe;
	}

	public QuoteToLe getQuoteToLe() {
		QuoteToLe quoteToLe = new QuoteToLe();
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
		quoteToLe.setTermInMonths("12 Months");
		quoteToLe.setQuote(createOptionalQuote().get());
		quoteToLe.setQuoteToLeProductFamilies(getQuoteToLeProductFamilies());
		quoteToLe.setErfServiceInventoryTpsServiceId("1234");
		return quoteToLe;
	}

	public Set<QuoteToLeProductFamily> getQuoteToLeProductFamilies() {
		QuoteToLeProductFamily quoteProductFamily = new QuoteToLeProductFamily();
		Set<QuoteToLeProductFamily> quoteToLeFamilies = new HashSet<>();
		quoteProductFamily.setId(1);
		quoteProductFamily.setQuoteToLe(getQuoteToLe1());
		quoteProductFamily.setMstProductFamily(getMstProductFamily());
		quoteProductFamily.setProductSolutions(getProductSolution());
		quoteToLeFamilies.add(quoteProductFamily);
		return quoteToLeFamilies;
	}

	public MstProductFamily getMstProductFamily() {
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(1);
		mstProductFamily.setName("UCAAS");
		mstProductFamily.setMstProductOfferings(getMstOfferings());
		return mstProductFamily;
	}

	public ProductSolution createProductSolution() {
		QuoteToLeProductFamily quoteProductFamily = new QuoteToLeProductFamily();
		quoteProductFamily.setId(1);
		ProductSolution productSolution = new ProductSolution();
		productSolution.setId(1);
		productSolution.setQuoteToLeProductFamily(quoteProductFamily);
		productSolution.setProductProfileData(GscConstants.GSIP_PRODUCT_NAME);
		productSolution.setMstProductOffering(getMstOffering());
		productSolution.setSolutionCode("UCWB2910195VXKJH");
		return productSolution;
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
		productSolution.setProductProfileData(WebexConstants.WEBEX);
		productSolution.setSolutionCode("UCWB2910195VXKJH");
		return productSolutions;
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

	public Set<MstProductOffering> getMstOfferings() {
		Set<MstProductOffering> mstProductOfferings = new HashSet<>();
		mstProductOfferings.add(getMstOffering());
		return mstProductOfferings;
	}

	public Set<QuoteIllSite> getIllsitesList() {
		Set<QuoteIllSite> value = new HashSet<>();
		value.add(getIllsite());
		return value;

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

	public Set<QuoteToLe> getQuoteToLeSet() {
		Set<QuoteToLe> quoteToLeList = new HashSet<QuoteToLe>();
		quoteToLeList.add(getQuoteToLe());
		return quoteToLeList;
	}

	public Quote createQuote() {
		Quote quote = new Quote();
		quote.setId(1);
		quote.setStatus((byte) 1);
		quote.setCustomer(getCustomer());
		quote.setQuoteToLes(getQuoteToLeSet());
		quote.setQuoteCode("UCWB2910195VXKJH");
		return quote;
	}

	public QuoteToLe createQuoteToLe() {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setId(1000);
		quoteToLe.setTpsSfdcOptyId("987654");
		quoteToLe.setTermInMonths("12 months");
		quoteToLe.setQuote(createQuote());
		return quoteToLe;
	}

	public QuoteLeAttributeValue getQuoteLeAttributeValue() {
		QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
		quoteLeAttributeValue.setId(1);
		quoteLeAttributeValue.setAttributeValue("test");
		quoteLeAttributeValue.setMstOmsAttribute(getMstOmsAttribute());
		quoteLeAttributeValue.setDisplayValue("display Value");
		quoteLeAttributeValue.setQuoteToLe(getQuoteToLe());
		return quoteLeAttributeValue;

	}

	public List<QuoteLeAttributeValue> getQuoteLeAttributeValues() {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = new ArrayList<>();
		quoteLeAttributeValues.add(getQuoteLeAttributeValue());
		quoteLeAttributeValues.add(getQuoteLeAttributeValue());
		return quoteLeAttributeValues;
	}

	public MstOmsAttribute getMstOmsAttribute() {
		MstOmsAttribute mstOmsAttribute = new MstOmsAttribute();
		mstOmsAttribute.setId(1);
		mstOmsAttribute.setIsActive((byte) 1);
		mstOmsAttribute.setCreatedBy("syed");
		mstOmsAttribute.setCreatedTime(new Date());
		mstOmsAttribute.setDescription("Description");
		mstOmsAttribute.setName(LeAttributesConstants.LEGAL_ENTITY_NAME);
		return mstOmsAttribute;
	}

	public List<MstOmsAttribute> getMstOmsAttributeList() {
		return ImmutableList.of(getMstOmsAttribute());
	}

	public QuoteUcaas createUcaas() {
		QuoteUcaas quoteUcaas = new QuoteUcaas();
		quoteUcaas.setId(39079);
		quoteUcaas.setCreatedBy("syed");
		quoteUcaas.setProductSolutionId(createProductSolution());
		quoteUcaas.setQuoteToLe(createQuoteToLe());
		quoteUcaas.setAudioModel("test");
		quoteUcaas.setDescription("testdescription");
		quoteUcaas.setIsConfig((byte) 1);
		quoteUcaas.setDialIn((byte) 1);
		quoteUcaas.setDialOut((byte) 1);
		quoteUcaas.setDialBack((byte) 1);
		quoteUcaas.setCugRequired((byte) 0);
		quoteUcaas.setPrimaryRegion(WebexConstants.EMEA);
		quoteUcaas.setGvpnMode("no");
		quoteUcaas.setIsLns((byte) 1);
		quoteUcaas.setIsItfs((byte) 0);
		quoteUcaas.setIsOutbound((byte) 0);
		quoteUcaas.setArc(0.0);
		quoteUcaas.setUnitNrc(0.0);
		quoteUcaas.setUnitMrc(0.0);
		quoteUcaas.setTcv(0.0);
		quoteUcaas.setNrc(0.0);
		quoteUcaas.setMrc(0.0);
		return quoteUcaas;

	}

	public QuoteToLeProductFamily createQuoteToLeProductFamily() {
		QuoteToLeProductFamily quoteToLeProductFamily = new QuoteToLeProductFamily();
		quoteToLeProductFamily.setId(1);
		quoteToLeProductFamily.setMstProductFamily(getMstProductFamily());
		quoteToLeProductFamily.setQuoteToLe(getQuoteToLe());
		return quoteToLeProductFamily;
	}

	public List<ProductSolution> createProductSolutionList() {
		List<ProductSolution> productSolutionList = new ArrayList<>();
		productSolutionList.add(createProductSolutions());
		productSolutionList.add(getGscProductSolution().get(0));
		return productSolutionList;
	}

	public ProductSolution createProductSolutions() {

		return getProductSolution().stream().findFirst().get();
	}

	public Set<QuoteGscDetail> getQuoteGscDetails() {
		Set<QuoteGscDetail> quoteGscDetails = new HashSet<>();
		QuoteGscDetail quotegscdetail = new QuoteGscDetail();
		quotegscdetail.setId(1);
		quotegscdetail.setArc(0D);
		quotegscdetail.setMrc(10D);
		quotegscdetail.setNrc(10D);
		quotegscdetail.setSrc("india");
		quotegscdetail.setDest("australia");
		quoteGscDetails.add(quotegscdetail);
		QuoteGscDetail quoteGscDetail2 = new QuoteGscDetail();
		quoteGscDetail2.setId(2);
		quoteGscDetail2.setArc(0D);
		quoteGscDetail2.setMrc(10D);
		quoteGscDetail2.setNrc(10D);
		quoteGscDetail2.setSrc("india");
		quoteGscDetail2.setDest("Africa");
		quoteGscDetails.add(quoteGscDetail2);
		QuoteGscDetail quoteGscDetail3 = new QuoteGscDetail();
		quoteGscDetail3.setId(3);
		quoteGscDetail3.setArc(0D);
		quoteGscDetail3.setMrc(10D);
		quoteGscDetail3.setNrc(10D);
		quoteGscDetail3.setSrc("india");
		quoteGscDetail3.setDest("Japan");
		quoteGscDetails.add(quoteGscDetail3);
		return quoteGscDetails;
	}

	public QuoteGsc getQuoteGsc() {
		QuoteGsc quoteGsc = new QuoteGsc();
		quoteGsc.setId(1005);
		quoteGsc.setStatus((byte) 1);
		quoteGsc.setAccessType("PSTN");
		quoteGsc.setQuoteToLe(getQuoteToLe());
		quoteGsc.setProductSolution(getGscProductSolution().stream().findAny().get());
		quoteGsc.setQuoteGscDetails(getQuoteGscDetails());
		quoteGsc.setProductName("UCAAS");
		return quoteGsc;
	}

	public List<QuoteGsc> createQuoteGscList() {
		List<QuoteGsc> quoteGscList = new ArrayList<>();
		quoteGscList.add(getQuoteGsc());
		quoteGscList.add(getQuoteGsc());
		return quoteGscList;
	}

	public List<QuoteGscDetail> createQuoteGscDetailsList() {
		List<QuoteGscDetail> quoteGscDetails = new ArrayList<>();
		QuoteGscDetail quotegscdetail = new QuoteGscDetail();
		quotegscdetail.setQuoteGsc(getQuoteGsc());
		quotegscdetail.setId(1);
		quotegscdetail.setArc(0D);
		quotegscdetail.setMrc(10D);
		quotegscdetail.setNrc(10D);
		quotegscdetail.setSrc("india");
		quotegscdetail.setDest("australia");
		quoteGscDetails.add(quotegscdetail);
		QuoteGscDetail quoteGscDetail2 = new QuoteGscDetail();
		quoteGscDetail2.setQuoteGsc(getQuoteGsc());
		quoteGscDetail2.setId(2);
		quoteGscDetail2.setArc(0D);
		quoteGscDetail2.setMrc(10D);
		quoteGscDetail2.setNrc(10D);
		quoteGscDetail2.setSrc("india");
		quoteGscDetail2.setDest("Africa");
		quoteGscDetails.add(quoteGscDetail2);
		QuoteGscDetail quoteGscDetail3 = new QuoteGscDetail();
		quoteGscDetail3.setQuoteGsc(getQuoteGsc());
		quoteGscDetail3.setId(3);
		quoteGscDetail3.setArc(0D);
		quoteGscDetail3.setMrc(10D);
		quoteGscDetail3.setNrc(10D);
		quoteGscDetail3.setSrc("india");
		quoteGscDetail3.setDest("Japan");
		quoteGscDetails.add(quoteGscDetail3);
		return quoteGscDetails;
	}

	public QuoteUcaas getQuoteUcaasConfiguration() {
		QuoteUcaas quoteUcaas = new QuoteUcaas();
		quoteUcaas.setId(1005);
		quoteUcaas.setQuoteToLe(getQuoteToLe());
		quoteUcaas.setName("Configuration");
		quoteUcaas.setDescription("Configuration Details");
		quoteUcaas.setLicenseProvider(WebexConstants.WEBEX);
		quoteUcaas.setAudioType(WebexConstants.TOLL_DIAL_IN);
		quoteUcaas.setPrimaryRegion(WebexConstants.EMEA);
		quoteUcaas.setAudioModel("Shared");
		quoteUcaas.setPaymentModel("PayPer Seat");
		quoteUcaas.setIsConfig((byte) 1);
		quoteUcaas.setDialIn((byte) 1);
		quoteUcaas.setDialOut((byte) 1);
		quoteUcaas.setDialBack((byte) 1);
		quoteUcaas.setCugRequired((byte) 1);
		quoteUcaas.setGvpnMode("no");
		quoteUcaas.setIsLns((byte) 1);
		quoteUcaas.setIsItfs((byte) 0);
		quoteUcaas.setIsOutbound((byte) 0);
		quoteUcaas.setMrc(0.0);
		quoteUcaas.setNrc(0.0);
		quoteUcaas.setArc(0.0);
		quoteUcaas.setTcv(0.0);
		quoteUcaas.setProductSolutionId(getProductSolution().stream().findFirst().get());
		return quoteUcaas;
	}

	public QuoteUcaas getQuoteUcaas() {
		QuoteUcaas quoteUcaas = new QuoteUcaas();
		quoteUcaas.setId(1006);
		quoteUcaas.setDealId(42293222);
		quoteUcaas.setQuoteToLe(getQuoteToLe());
		quoteUcaas.setDeal_status(Constants.SUCCESS);
		quoteUcaas.setName("A-FLEX");
		quoteUcaas.setDescription("Collaboration Flex Plan");
		quoteUcaas.setUnitMrc(100.0);
		quoteUcaas.setTcv(0.0);
		quoteUcaas.setUnitNrc(0.0);
		quoteUcaas.setArc(0.0);
		quoteUcaas.setMrc(200.0);
		quoteUcaas.setQuantity(10);
		quoteUcaas.setIsConfig((byte) 0);
		quoteUcaas.setProductSolutionId(getProductSolution().stream().findFirst().get());
		return quoteUcaas;
	}

	public List<QuoteUcaas> getQuoteLines() {
		List<QuoteUcaas> ucaasQuotes = new ArrayList<>();
		ucaasQuotes.add(getQuoteUcaasConfiguration());
		ucaasQuotes.add(getQuoteUcaas());
		ucaasQuotes.add(getQuoteUcaas());
		return ucaasQuotes;
	}

	public Order createOrder() {
		Order order = new Order();
		Quote quote = createQuote();
		order.setId(quote.getId());
		order.setCreatedBy(quote.getCreatedBy());
		order.setCreatedTime(new Date());
		order.setStatus(quote.getStatus());
		order.setTermInMonths(quote.getTermInMonths());
		order.setCustomer(quote.getCustomer());
		order.setEffectiveDate(quote.getEffectiveDate());
		order.setQuote(quote);
		order.setStage(OrderStagingConstants.ORDER_CREATED.getStage());
		order.setStartDate(new Date());
		order.setOrderCode(quote.getQuoteCode());
		order.setQuoteCreatedBy(quote.getCreatedBy());
		order.setEngagementOptyId(quote.getEngagementOptyId());
		return order;
	}

	public QuoteProductComponent getProductComponent() {
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setId(3214);
		quoteProductComponent.setReferenceId(1);
		quoteProductComponent.setMstProductComponent(getMstProductComponent());
		quoteProductComponent.setMstProductFamily(getMstProductFamily());
		return quoteProductComponent;
	}

	public List<QuoteProductComponent> createQuoteProductComponents() {
		List<QuoteProductComponent> quoteProductComponents = new ArrayList<>();
		quoteProductComponents.add(getProductComponent());
		quoteProductComponents.add(getProductComponent());
		return quoteProductComponents;
	}

	public QuoteProductComponentsAttributeValue getQuoteProductComponentsAttributeValue() {
		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue.setId(1);
		quoteProductComponentsAttributeValue.setQuoteProductComponent(getProductComponent());
		quoteProductComponentsAttributeValue.setDisplayValue(GscAttributeConstants.ATTR_TERMINATION_NUMBER_OUTPULSE);
		quoteProductComponentsAttributeValue.setAttributeValues(GscAttributeConstants.ATTR_TERMINATION_NUMBER_OUTPULSE);
		quoteProductComponentsAttributeValue.setProductAttributeMaster(getAttributeMaster());
		return quoteProductComponentsAttributeValue;
	}

	public List<QuoteProductComponentsAttributeValue> createQuoteProductComponentsAttributeValuesWithDiffAttr() {
		List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = new ArrayList<>();

		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue.setId(1);
		quoteProductComponentsAttributeValue.setDisplayValue("2");
		quoteProductComponentsAttributeValue.setAttributeValues("123,4");
		quoteProductComponentsAttributeValue.setProductAttributeMaster(getAttributeMaster());
		quoteProductComponentsAttributeValue.setDisplayValue("2");
		quoteProductComponentsAttributeValue.getProductAttributeMaster().setName(LeAttributesConstants.PHONE_TYPE);
		quoteProductComponentsAttributeValues.add(quoteProductComponentsAttributeValue);

		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue10 = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue10.setId(1);
		quoteProductComponentsAttributeValue10.setDisplayValue("2");
		quoteProductComponentsAttributeValue10.setAttributeValues("123,4");
		quoteProductComponentsAttributeValue10.setProductAttributeMaster(getAttributeMaster());
		quoteProductComponentsAttributeValue10.setDisplayValue("2");
		quoteProductComponentsAttributeValue10.getProductAttributeMaster()
				.setName(LeAttributesConstants.TERMINATION_NAME);
		quoteProductComponentsAttributeValues.add(quoteProductComponentsAttributeValue10);

		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue2 = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue2.setId(1);
		quoteProductComponentsAttributeValue2.setDisplayValue("2");
		quoteProductComponentsAttributeValue2.setAttributeValues("123,4");
		quoteProductComponentsAttributeValue2.setProductAttributeMaster(getAttributeMaster());
		quoteProductComponentsAttributeValue2.getProductAttributeMaster()
				.setName(LeAttributesConstants.TERMINATION_RATE);
		quoteProductComponentsAttributeValues.add(quoteProductComponentsAttributeValue2);

		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue3 = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue3.setId(1);
		quoteProductComponentsAttributeValue3.setDisplayValue("2");
		quoteProductComponentsAttributeValue3.setAttributeValues("123,4");
		quoteProductComponentsAttributeValue3.setProductAttributeMaster(getAttributeMaster());
		quoteProductComponentsAttributeValue3.getProductAttributeMaster().setName(LeAttributesConstants.CHANNEL_MRC);
		quoteProductComponentsAttributeValues.add(quoteProductComponentsAttributeValue3);

		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue4 = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue4.setId(1);
		quoteProductComponentsAttributeValue4.setDisplayValue("2");
		quoteProductComponentsAttributeValue4.setAttributeValues("123,4");
		quoteProductComponentsAttributeValue4.setProductAttributeMaster(getAttributeMaster());
		quoteProductComponentsAttributeValue4.getProductAttributeMaster()
				.setName(LeAttributesConstants.ORDER_SETUP_NRC);
		quoteProductComponentsAttributeValues.add(quoteProductComponentsAttributeValue4);

		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue5 = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue5.setId(1);
		quoteProductComponentsAttributeValue5.setDisplayValue("2");
		quoteProductComponentsAttributeValue5.setAttributeValues("14");
		quoteProductComponentsAttributeValue5.setProductAttributeMaster(getAttributeMaster());
		quoteProductComponentsAttributeValue5.getProductAttributeMaster()
				.setName(LeAttributesConstants.QUANTITY_OF_NUMBERS);
		quoteProductComponentsAttributeValues.add(quoteProductComponentsAttributeValue5);

		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue6 = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue6.setId(1);
		quoteProductComponentsAttributeValue6.setDisplayValue("2");
		quoteProductComponentsAttributeValue6.setAttributeValues("4");
		quoteProductComponentsAttributeValue6.setProductAttributeMaster(getAttributeMaster());
		quoteProductComponentsAttributeValue6.getProductAttributeMaster()
				.setName(LeAttributesConstants.RATE_PER_MIN_MOBILE);
		quoteProductComponentsAttributeValues.add(quoteProductComponentsAttributeValue6);

		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue7 = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue7.setId(1);
		quoteProductComponentsAttributeValue7.setDisplayValue("2");
		quoteProductComponentsAttributeValue7.setAttributeValues("124");
		quoteProductComponentsAttributeValue7.setProductAttributeMaster(getAttributeMaster());
		quoteProductComponentsAttributeValue7.getProductAttributeMaster().setName(LeAttributesConstants.CHANNEL_ARC);
		quoteProductComponentsAttributeValues.add(quoteProductComponentsAttributeValue7);

		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue8 = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue8.setId(1);
		quoteProductComponentsAttributeValue8.setDisplayValue("2");
		quoteProductComponentsAttributeValue8.setAttributeValues("14");
		quoteProductComponentsAttributeValue8.setProductAttributeMaster(getAttributeMaster());
		quoteProductComponentsAttributeValue8.getProductAttributeMaster()
				.setName(LeAttributesConstants.RATE_PER_MIN_FIXED);
		quoteProductComponentsAttributeValues.add(quoteProductComponentsAttributeValue8);

		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue9 = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue9.setId(1);
		quoteProductComponentsAttributeValue9.setDisplayValue("2");
		quoteProductComponentsAttributeValue9.setAttributeValues("1234");
		quoteProductComponentsAttributeValue9.setProductAttributeMaster(getAttributeMaster());
		quoteProductComponentsAttributeValue9.getProductAttributeMaster()
				.setName(LeAttributesConstants.RATE_PER_MIN_SPECIAL);
		quoteProductComponentsAttributeValues.add(quoteProductComponentsAttributeValue9);

		return quoteProductComponentsAttributeValues;
	}

	public List<QuoteProductComponentsAttributeValue> createQuoteProductComponentsAttributeValue() {
		List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = new ArrayList<>();
		quoteProductComponentsAttributeValues.add(getQuoteProductComponentsAttributeValue());
		quoteProductComponentsAttributeValues.add(getQuoteProductComponentsAttributeValue());
		return quoteProductComponentsAttributeValues;
	}

	public OrderToLe getOrderToLe() {
		OrderToLe orderToLe = new OrderToLe();
		orderToLe.setId(4321);
		orderToLe.setOrderToLeProductFamilies(createOrderToLeProductFamiliesSet());
		orderToLe.setOrder(createOrder());
		orderToLe.setOrderType("New");
		return orderToLe;
	}

	public List<OrderToLe> createOrderToLe() {
		List<OrderToLe> orderToLeList = new ArrayList<>();
		orderToLeList.add(getOrderToLe());
		orderToLeList.add(getOrderToLe());
		return orderToLeList;
	}

	public Optional<OrderToLe> createOptionalOrderToLe() {
		return Optional.of(getOrderToLe());
	}

	public OrderToLeProductFamily getOrderToLeProductFamily() {
		OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
		orderToLeProductFamily.setId(2134);
		orderToLeProductFamily.setMstProductFamily(getMstProductFamily());
		OrderToLe orderToLe = new OrderToLe();
		orderToLe.setId(1);
		orderToLe.setOrder(createOrder());
		orderToLe.setFinalMrc(0.0);
		orderToLe.setFinalNrc(0.0);
		orderToLe.setStage(OrderStagingConstants.ORDER_CREATED.toString());
		orderToLe.setTermInMonths("12 months");
		OrdersLeAttributeValue ordersLeAttributeValue = new OrdersLeAttributeValue();
		ordersLeAttributeValue.setId(2);
		ordersLeAttributeValue.setOrderToLe(orderToLe);
		ordersLeAttributeValue.setAttributeValue("2.0");
		ordersLeAttributeValue.setDisplayValue(GscConstants.RATE_PER_MINUTE_FIXED);
		ordersLeAttributeValue.setMstOmsAttribute(getMstOmsAttribute());
		orderToLe.setOrdersLeAttributeValues(Sets.newHashSet(ordersLeAttributeValue));
		orderToLe.setOrderToLeProductFamilies(getOrderToLeProductFamilies());
		orderToLe.setOrderType("New");
		orderToLeProductFamily.setOrderToLe(orderToLe);
		return orderToLeProductFamily;
	}

	public Set<OrderToLeProductFamily> createOrderToLeProductFamiliesSet() {
		Set<OrderToLeProductFamily> orderToLeProductFamilies = new HashSet<>();
		orderToLeProductFamilies.add(getOrderToLeProductFamily());
		orderToLeProductFamilies.add(getOrderToLeProductFamily());
		return orderToLeProductFamilies;
	}

	public MstProductComponent getMstProductComponent() {
		MstProductComponent mstProductComponent = new MstProductComponent();
		mstProductComponent.setId(8);
		mstProductComponent.setName("LNS");
		mstProductComponent.setStatus((byte) 1);
		return mstProductComponent;
	}

	public MstProductComponent getMstProductComponentITFS() {
		MstProductComponent mstProductComponent = new MstProductComponent();
		mstProductComponent.setId(8);
		mstProductComponent.setName("ITFS");
		mstProductComponent.setStatus((byte) 1);
		return mstProductComponent;
	}

	public MstProductComponent getMstProductComponentGO() {
		MstProductComponent mstProductComponent = new MstProductComponent();
		mstProductComponent.setId(8);
		mstProductComponent.setName("Global Outbound");
		mstProductComponent.setStatus((byte) 1);
		return mstProductComponent;
	}

	public MstProductComponent getMstProductComponentMPLS() {
		MstProductComponent mstProductComponent = new MstProductComponent();
		mstProductComponent.setId(8);
		mstProductComponent.setName("MPLS");
		mstProductComponent.setStatus((byte) 1);
		return mstProductComponent;
	}

	public MstProductComponent getMstProductComponentPSTN() {
		MstProductComponent mstProductComponent = new MstProductComponent();
		mstProductComponent.setId(8);
		mstProductComponent.setName("PSTN");
		mstProductComponent.setStatus((byte) 1);
		return mstProductComponent;
	}

	public MstProductComponent getMstProductComponentCPE() {
		MstProductComponent mstProductComponent = new MstProductComponent();
		mstProductComponent.setId(8);
		mstProductComponent.setName(OrderDetailsExcelDownloadConstants.CPE);
		mstProductComponent.setStatus((byte) 1);
		return mstProductComponent;
	}

	public MstProductComponent getMstProductComponentCPEManagement() {
		MstProductComponent mstProductComponent = new MstProductComponent();
		mstProductComponent.setId(8);
		mstProductComponent.setName(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT);
		mstProductComponent.setStatus((byte) 1);
		return mstProductComponent;
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

	public List<ProductSolution> getGscProductSolution() {
		QuoteToLeProductFamily quoteProductFamily = new QuoteToLeProductFamily();
		ProductSolution productSolution = new ProductSolution();
		List<ProductSolution> productSolutions = new ArrayList<>();
		productSolution.setId(1);
		productSolution.setQuoteToLeProductFamily(quoteProductFamily);
		productSolution.setMstProductOffering(getMstOffering());
		productSolution.setQuoteIllSites(getIllsitesList());
		productSolution.setProductProfileData(
				"{\"solutionId\":\"VZFNFN98M7GOPKEI\",\"offeringName\":\"LNS on MPLS\",\"solutionCode\":\"LNSMPLS\",\"accessType\":\"MPLS\",\"productName\":\"LNS\",\"gscQuotes\":null,\"rateColumn\":null}");
		productSolution.setSolutionCode("VZFNFN98M7GOPKEI");
		productSolutions.add(productSolution);
		return productSolutions;
	}

	public UserInformation getUserInformation() {
		UserInformation userInformation = new UserInformation();
		userInformation.setFirstName("ANONYMOUS");
		userInformation.setLastName("USER");
		userInformation.setUserId("1234");
		return userInformation;
	}

	public Map<String, Object> getUserInfo() {
		Map<String, Object> userInfo = new HashMap<>();
		userInfo.put(AuthTokenService.USER_INFORMATION, getUserInformation());
		return userInfo;
	}

	public CofDetails getCofDetails() {
		CofDetails cofDetails = new CofDetails();
		cofDetails.setId(1);
		String filename = "ucaas_quote_tables.sql";
		Path path = Paths.get(filename);
		cofDetails.setUriPath(path.toAbsolutePath().toString());
		return cofDetails;
	}

	public Order getOrders() {
		Order orders = new Order();
		orders.setId(1);
		orders.setStatus((byte) 1);
		orders.setOrderCode("UCWB12345678910");
		orders.setCustomer(getCustomer());
		orders.setStage("order created");
		Quote quote = new Quote();
		quote.setId(1);
		quote.setQuoteCode("UCWB12345678910");
		orders.setQuote(quote);
		orders.setOrderToLes(getOrderToLeSet());
		return orders;
	}

	public OrderProductSolution getOrderProductSolution() {
		OrderProductSolution orderProductSolution = new OrderProductSolution();
		orderProductSolution.setId(1);
		orderProductSolution.setMstProductOffering(getMstOffering());
		orderProductSolution.setProductProfileData("{\"solutionCode\": \"ITFSPSTN\"}");
		return orderProductSolution;
	}

    public OrderProductSolution getWebexOrderProductSolution() {
        OrderProductSolution orderProductSolution = new OrderProductSolution();
        orderProductSolution.setId(1);
        orderProductSolution.setMstProductOffering(getMstOffering());
        orderProductSolution.setProductProfileData("{\"solutionCode\": \"WEBEX\"}");
        return orderProductSolution;
    }


    public ProductAttributeMaster getAttributeMaster() {
		ProductAttributeMaster productAttributeMaster = new ProductAttributeMaster();
		productAttributeMaster.setName(LeAttributesConstants.TERMINATION_NAME);
		productAttributeMaster.setCategory("testcategory");
		productAttributeMaster.setDescription("testdesc");
		return productAttributeMaster;
	}

	public ProductAttributeMaster getAttributeMasterCPE() {
		ProductAttributeMaster productAttributeMaster = new ProductAttributeMaster();
		productAttributeMaster.setName(ComponentConstants.CPE_BASIC_CHASSIS.getComponentsValue());
		productAttributeMaster.setCategory("testcategory");
		productAttributeMaster.setDescription("testdesc");
		return productAttributeMaster;
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

	public SIExistingGVPNBean getGVPNRequestBean() {
		SIExistingGVPNBean gvpnRequestBean = new SIExistingGVPNBean();
		gvpnRequestBean.setPage(1);
		gvpnRequestBean.setSize(10);
		gvpnRequestBean.setStatus(CommonConstants.SUCCESS);

		SIServiceInfoGVPNBean serviceInfoGVPNBean1 = new SIServiceInfoGVPNBean();
		serviceInfoGVPNBean1.setOrderCustomer("TCTS");
		serviceInfoGVPNBean1.setOrderCustLeName("TCTSL");
		serviceInfoGVPNBean1.setServiceId("091MUMB623012870459");
		serviceInfoGVPNBean1.setBandwidth("2 Mbps");
		serviceInfoGVPNBean1.setSourceCity("Mumbai");
		serviceInfoGVPNBean1.setIsActive("Y");
		serviceInfoGVPNBean1.setProductOfferingName("GVPN");
		serviceInfoGVPNBean1.setOrderSysId(12345);

		gvpnRequestBean.setServiceInfos(Arrays.asList(serviceInfoGVPNBean1));
		gvpnRequestBean.setMessage(WebexConstants.INVENTORY_RECEIVED_MESSAGE);
		return gvpnRequestBean;
	}

	public RestResponse getPricingResponse() throws TclCommonException {
		RestResponse restResponse = new RestResponse();
		WebexPricingResponse pricingResponse = fromJsonFile("com/tcl/dias/oms/webex/controller/pricing_response.json",
				WebexPricingResponse.class);
		restResponse.setData(Utils.convertObjectToJson(pricingResponse));
		restResponse.setStatus(Status.SUCCESS);
		return restResponse;
	}

	public PricingEngineResponse createPricingDetailsResponse() {
		PricingEngineResponse pricingEngineResponse = new PricingEngineResponse();
		pricingEngineResponse.setRequestData("test");
		pricingEngineResponse.setPricingType("test");
		return pricingEngineResponse;
	}

	public Set<OrderToLeProductFamily> getOrderToLeProductFamilies() {
		OrderToLeProductFamily orderProductFamily = new OrderToLeProductFamily();
		Set<OrderToLeProductFamily> orderToLeFamilies = new HashSet<>();
		orderProductFamily.setId(1);
		orderProductFamily.setMstProductFamily(getMstProductFamily());
		orderProductFamily.setOrderProductSolutions(getOrderProductSolutions());
		orderToLeFamilies.add(orderProductFamily);
		return orderToLeFamilies;
	}

	public OrderProductSolution createOrderProductSolution() {
		OrderToLeProductFamily orderProductFamily = new OrderToLeProductFamily();
		OrderProductSolution productSolution = new OrderProductSolution();
		productSolution.setId(1);
		productSolution.setOrderToLeProductFamily(orderProductFamily);
		productSolution.setProductProfileData(GscConstants.GSIP_PRODUCT_NAME);
		productSolution.setMstProductOffering(getMstOffering());
		return productSolution;
	}

	public Set<OrderProductSolution> getOrderProductSolutions() {
		OrderToLeProductFamily orderProductFamily = new OrderToLeProductFamily();
		OrderProductSolution productSolution = new OrderProductSolution();
		Set<OrderProductSolution> productSolutions = new HashSet<>();
		productSolution.setId(1);
		productSolution.setOrderToLeProductFamily(orderProductFamily);
		productSolutions.add(productSolution);
		productSolution.setMstProductOffering(getMstOffering());
		productSolution.setOrderIllSites(geOrdertIllsitesList());
		productSolution.setProductProfileData(WebexConstants.WEBEX);
		return productSolutions;
	}

	public List<OrderProductSolution> getOrderProductSolutionsList() {
		OrderToLeProductFamily orderProductFamily = new OrderToLeProductFamily();
		OrderProductSolution productSolution = new OrderProductSolution();
		List<OrderProductSolution> productSolutions = new ArrayList<>();
		productSolution.setId(1);
		productSolution.setOrderToLeProductFamily(orderProductFamily);
		productSolution.setMstProductOffering(getMstOffering());
		productSolution.setOrderIllSites(geOrdertIllsitesList());
		productSolution.setProductProfileData(WebexConstants.WEBEX);
		productSolutions.add(productSolution);

		OrderProductSolution productSolution1 = new OrderProductSolution();
		productSolution1.setId(1);
		productSolution1.setOrderToLeProductFamily(orderProductFamily);
		productSolutions.add(productSolution1);
		productSolution1.setMstProductOffering(getMstOffering());
		productSolution1.setOrderIllSites(geOrdertIllsitesList());
		productSolution1.setProductProfileData(GscConstants.GSIP_PRODUCT_NAME);
		productSolutions.add(productSolution1);

		return productSolutions;
	}

	public Set<OrderIllSite> geOrdertIllsitesList() {
		Set<OrderIllSite> value = new HashSet<>();
		value.add(getOrderIllsite());
		return value;

	}

	public OrderIllSite getOrderIllsite() {
		OrderIllSite site = new OrderIllSite();
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

	public Set<OrderToLe> getOrderToLeSet() {
		Set<OrderToLe> orderToLeList = new HashSet<OrderToLe>();
		orderToLeList.add(getOrderToLe());
		return orderToLeList;
	}

	public OrdersLeAttributeValue getOrderLeAttributeValue() {
		OrdersLeAttributeValue orderLeAttributeValue = new OrdersLeAttributeValue();
		orderLeAttributeValue.setId(1);
		orderLeAttributeValue.setAttributeValue("test");
		orderLeAttributeValue.setMstOmsAttribute(getMstOmsAttribute());
		orderLeAttributeValue.setDisplayValue("display Value");
		orderLeAttributeValue.setOrderToLe(getOrderToLe());
		return orderLeAttributeValue;
	}

	public Set<OrderGscDetail> getOrderGscDetails() {
		OrderGsc orderGsc = new OrderGsc();
		orderGsc.setAccessType(GscConstants.PSTN);
		orderGsc.setId(10);

		Set<OrderGscDetail> orderGscDetails = new HashSet<>();
		OrderGscDetail ordergscdetail = new OrderGscDetail();
		ordergscdetail.setId(1);
		ordergscdetail.setArc(0D);
		ordergscdetail.setMrc(10D);
		ordergscdetail.setNrc(10D);
		ordergscdetail.setSrc("india");
		ordergscdetail.setDest("australia");
		ordergscdetail.setOrderGsc(orderGsc);
		orderGscDetails.add(ordergscdetail);
		OrderGscDetail orderGscDetail2 = new OrderGscDetail();
		orderGscDetail2.setId(2);
		orderGscDetail2.setArc(0D);
		orderGscDetail2.setMrc(10D);
		orderGscDetail2.setNrc(10D);
		orderGscDetail2.setSrc("india");
		orderGscDetail2.setDest("Africa");
		orderGscDetail2.setOrderGsc(orderGsc);
		orderGscDetails.add(orderGscDetail2);
		OrderGscDetail orderGscDetail3 = new OrderGscDetail();
		orderGscDetail3.setId(3);
		orderGscDetail3.setArc(0D);
		orderGscDetail3.setMrc(10D);
		orderGscDetail3.setNrc(10D);
		orderGscDetail3.setSrc("india");
		orderGscDetail3.setDest("Japan");
		orderGscDetail3.setOrderGsc(orderGsc);
		orderGscDetails.add(orderGscDetail3);
		return orderGscDetails;
	}

	public OrderGsc getOrderGsc() {
		OrderGsc orderGsc = new OrderGsc();
		orderGsc.setId(1005);
		orderGsc.setStatus((byte) 1);
		orderGsc.setAccessType("PSTN");
		orderGsc.setOrderToLe(getOrderToLe());
		orderGsc.setOrderProductSolution(getGscOrderProductSolution().stream().findAny().get());
		orderGsc.setOrderGscDetails(getOrderGscDetails());
		orderGsc.setProductName("ITFS");
		return orderGsc;
	}

	public OrderUcaas getOrderUcaasConfiguration() {
		OrderUcaas orderUcaas = new OrderUcaas();
		orderUcaas.setId(1005);
		orderUcaas.setName("Configuration");
		orderUcaas.setDescription("Configuration Details");
		orderUcaas.setLicenseProvider(WebexConstants.WEBEX);
		orderUcaas.setAudioModel("Shared");
		orderUcaas.setPaymentModel("PayPer Seat");
		orderUcaas.setIsConfig((byte) 1);
		orderUcaas.setDialIn((byte) 1);
		orderUcaas.setDialOut((byte) 1);
		orderUcaas.setDialBack((byte) 1);
		orderUcaas.setCugRequired((byte) 0);
		orderUcaas.setPrimaryRegion(WebexConstants.EMEA);
		orderUcaas.setGvpnMode("no");
		orderUcaas.setIsLns((byte) 1);
		orderUcaas.setIsItfs((byte) 0);
		orderUcaas.setIsOutbound((byte) 0);
		return orderUcaas;
	}

	public OrderUcaas getOrderUcaas() {
		OrderUcaas orderUcaas = new OrderUcaas();
		orderUcaas.setId(1006);
		orderUcaas.setDealId(42293222);
		orderUcaas.setDeal_status(Constants.SUCCESS);
		orderUcaas.setName("A-FLEX");
		orderUcaas.setDescription("Collaboration Flex Plan");
		orderUcaas.setMrc(200.0);
		orderUcaas.setQuantity(10);
		orderUcaas.setIsConfig((byte) 0);
		return orderUcaas;
	}

	public List<OrderUcaas> getOrderLines() {
		List<OrderUcaas> ucaasOrders = new ArrayList<>();
		ucaasOrders.add(getOrderUcaasConfiguration());
		ucaasOrders.add(getOrderUcaas());
		ucaasOrders.add(getOrderUcaas());
		return ucaasOrders;
	}

	public OrderProductComponent getOrderProductComponent() {
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setId(3214);
		orderProductComponent.setReferenceId(1);
		orderProductComponent.setType("test");
		orderProductComponent.setMstProductComponent(getMstProductComponentITFS());
		orderProductComponent.setMstProductFamily(getMstProductFamily());
		orderProductComponent
				.setOrderProductComponentsAttributeValues(Sets.newHashSet(getOrderProductComponentsAttributeValue()));
		return orderProductComponent;
	}

	public MstProductComponent getMstProductComponentGVPNCOMMON() {
		MstProductComponent mstProductComponent = new MstProductComponent();
		mstProductComponent.setId(8);
		mstProductComponent.setName(PDFConstants.GVPN_COMMON);
		mstProductComponent.setStatus((byte) 1);
		return mstProductComponent;
	}

	public List<OrderProductComponent> getOrderProductComponentList() {
		List<OrderProductComponent> orderProductComponents = new ArrayList<>();
//		orderProductComponents.add(getOrderProductComponentITFS());
//		orderProductComponents.add(getOrderProductComponentMPLS());
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setId(3214);
		orderProductComponent.setReferenceId(1234);
		orderProductComponent.setMstProductComponent(getMstProductComponentGVPNCOMMON());
		orderProductComponent.setType("primary");
		orderProductComponent
				.setOrderProductComponentsAttributeValues(Sets.newHashSet(getOrderProductComponentsAttributeValue()));
		orderProductComponents.add(orderProductComponent);
		OrderProductComponent orderProductComponent1 = new OrderProductComponent();
		orderProductComponent1.setId(3215);
		orderProductComponent1.setReferenceId(1234);
		orderProductComponent1.setMstProductComponent(getMstProductComponentGVPNCOMMON());
		orderProductComponent1.setType("secondary");
		orderProductComponent1
				.setOrderProductComponentsAttributeValues(Sets.newHashSet(getOrderProductComponentsAttributeValue()));
		orderProductComponents.add(orderProductComponent1);
		OrderProductComponent orderProductComponent2 = new OrderProductComponent();
		orderProductComponent2.setId(3214);
		orderProductComponent2.setReferenceId(1234);
		orderProductComponent2.setMstProductComponent(getMstProductComponentCPE());
		orderProductComponent2.getMstProductComponent().setName("test");
		orderProductComponent2.setType("primary");
		orderProductComponent2.setOrderProductComponentsAttributeValues(
				Sets.newHashSet(getOrderProductComponentsAttributeValueCPE()));
		orderProductComponents.add(orderProductComponent2);
		OrderProductComponent orderProductComponent3 = new OrderProductComponent();
		orderProductComponent3.setId(3214);
		orderProductComponent3.setReferenceId(1234);
		orderProductComponent3.setMstProductComponent(getMstProductComponentCPE());
		orderProductComponent3.setType("secondary");
		orderProductComponent3.setOrderProductComponentsAttributeValues(
				Sets.newHashSet(getOrderProductComponentsAttributeValueCPE()));
		orderProductComponents.add(orderProductComponent3);
		OrderProductComponent orderProductComponent4 = new OrderProductComponent();
		orderProductComponent4.setId(3214);
		orderProductComponent4.setReferenceId(1234);
		orderProductComponent4.setMstProductComponent(getMstProductComponentCPEManagement());
		orderProductComponent4.setType("secondary");
		OrderProductComponentsAttributeValue orderProductComponentsAttributeValue = getOrderProductComponentsAttributeValueCPE();
		ProductAttributeMaster productAttributeMaster = getProductAttributeMaster();
		productAttributeMaster.setName(ComponentConstants.CPE_BASIC_CHASSIS.getComponentsValue());
		orderProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
		orderProductComponent4
				.setOrderProductComponentsAttributeValues(Sets.newHashSet(orderProductComponentsAttributeValue));
		orderProductComponents.add(orderProductComponent4);
		return orderProductComponents;
	}

	public OrderProductComponent getOrderProductComponentMPLS() {
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setId(3214);
		orderProductComponent.setReferenceId(1234);
		orderProductComponent.setMstProductComponent(getMstProductComponentGO());
		return orderProductComponent;
	}

	public OrderProductComponent getOrderProductComponentITFS() {
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setId(3214);
		orderProductComponent.setReferenceId(1234);
		orderProductComponent.setMstProductComponent(getMstProductComponent());
		return orderProductComponent;
	}

	public List<OrderProductComponentsAttributeValue> getOrderProductComponentsAttributeValueList() {
		List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = new ArrayList<>();
		orderProductComponentsAttributeValues.add(getOrderProductComponentsAttributeValue());
		orderProductComponentsAttributeValues.add(getOrderProductComponentsAttributeValueCPE());
		return orderProductComponentsAttributeValues;
	}

	public OrderProductComponentsAttributeValue getOrderProductComponentsAttributeValueCPE() {
		OrderProductComponentsAttributeValue orderProductComponentsAttributeValue = new OrderProductComponentsAttributeValue();
		orderProductComponentsAttributeValue.setId(1);
		orderProductComponentsAttributeValue.setDisplayValue(ComponentConstants.CPE_BASIC_CHASSIS.getComponentsValue());
		orderProductComponentsAttributeValue
				.setAttributeValues(ComponentConstants.CPE_BASIC_CHASSIS.getComponentsValue());
		orderProductComponentsAttributeValue.setProductAttributeMaster(getAttributeMasterCPE());
		return orderProductComponentsAttributeValue;
	}

	public OrderProductComponentsAttributeValue getOrderProductComponentsAttributeValue() {
		OrderProductComponentsAttributeValue orderProductComponentsAttributeValue = new OrderProductComponentsAttributeValue();
		orderProductComponentsAttributeValue.setId(1);
		orderProductComponentsAttributeValue.setDisplayValue(GscAttributeConstants.ATTR_TERMINATION_NUMBER_OUTPULSE);
		orderProductComponentsAttributeValue.setAttributeValues(GscAttributeConstants.ATTR_TERMINATION_NUMBER_OUTPULSE);
		orderProductComponentsAttributeValue.setProductAttributeMaster(getAttributeMaster());
		return orderProductComponentsAttributeValue;
	}

	public List<OrderProductSolution> getGscOrderProductSolution() {
		OrderToLeProductFamily orderProductFamily = new OrderToLeProductFamily();
		OrderProductSolution productSolution = new OrderProductSolution();
		List<OrderProductSolution> productSolutions = new ArrayList<>();
		productSolution.setId(1);
		productSolution.setOrderToLeProductFamily(orderProductFamily);
		productSolutions.add(productSolution);
		productSolution.setMstProductOffering(getMstOffering());
		productSolution.setProductProfileData("{\"solutionCode\": \"ITFSPSTN\"}");
		return productSolutions;
	}

	public List<OrdersLeAttributeValue> createOrdersLeAttributeValues() {
		List<OrdersLeAttributeValue> objs = new ArrayList<>();
		objs.add(createOrdersLeAttributeValue());
		objs.add(createOrdersLeAttributeValue());
		return objs;
	}

	public OrdersLeAttributeValue createOrdersLeAttributeValue() {
		OrdersLeAttributeValue obj1 = new OrdersLeAttributeValue();
		obj1.setAttributeValue("testvalue");
		obj1.setDisplayValue("test");
		obj1.setMstOmsAttribute(getMstOmsAttribute());
		obj1.setOrderToLe(getOrderToLe());
		obj1.setId(1);
		return obj1;
	}

	public RestResponse getRestResponse() {
		RestResponse restResponse = new RestResponse();
		restResponse.setStatus(Status.SUCCESS);
		restResponse.setData("{\"access_token\": \"55MHzUL7UKAZBKx8eip3buc8tk3a\",\"token_type\": \"Bearer\",\n"
				+ "    \"expires_in\": 3599\n" + "}");
		return restResponse;
	}

	public ShowQuoteType getListQuoteResponse() throws JAXBException, XMLStreamException {
		ShowQuoteType showQuoteType = new ShowQuoteType();
		String response = "<ShowQuote releaseID=\"\" xmlns=\"http://www.openapplications.org/oagis/9\" xmlns:ns2=\"urn:cisco:b2bngc:services:1.0\"><ApplicationArea><CreationDateTime>1967-08-13</CreationDateTime></ApplicationArea><DataArea><Show/><Quote><QuoteHeader><DocumentID><ID>4722826637</ID></DocumentID><LastModificationDateTime>2019-10-24T02:50:25-07:00</LastModificationDateTime><DocumentDateTime>2019-10-24T02:05:29-07:00</DocumentDateTime><Description type=\"ShareAccessKey\">WLAE4722826637</Description><Status><Code listAgencyName=\"Cisco\" listName=\"QuoteStatus\">Approved</Code></Status><Party role=\"End Customer\"><Name>TATA COMMUNICATIONS</Name><Location><Address><AddressLine sequence=\"1\">10 FENCHURCH STREET</AddressLine><CityName>LONDON</CityName><CountrySubDivisionCode>LONDON</CountrySubDivisionCode><CountryCode>GB</CountryCode><PostalCode>EC3M 3BE</PostalCode></Address></Location></Party><EffectiveTimePeriod><EndDateTime>2020-04-21T00:00:00-07:00</EndDateTime></EffectiveTimePeriod><QualificationTerm><ID schemeAgencyName=\"Cisco\">42293222</ID></QualificationTerm><UserArea><ns2:CiscoExtensions><ns2:CiscoHeader><ns2:PriceList><Description>Global Price List EMEA Availability</Description></ns2:PriceList><ns2:ConfigurationMessages><ID>LSTQCT00</ID><Description>Success</Description><Reason></Reason></ns2:ConfigurationMessages></ns2:CiscoHeader></ns2:CiscoExtensions></UserArea><Extension><Amount typeCode=\"DealConsumption\">0</Amount><Amount typeCode=\"DealTotal\">714600</Amount><Text typeCode=\"DealType\">PDR Deal</Text><Text typeCode=\"IntendedUse\">Resale</Text></Extension></QuoteHeader></Quote></DataArea></ShowQuote>";
		showQuoteType = Utils.convertXmlToObject(response, ShowQuoteType.class);
		return showQuoteType;
	}

	public ShowQuoteType getAcquireQuoteResponse() throws JAXBException, XMLStreamException {
		ShowQuoteType showQuoteType = new ShowQuoteType();
		String response = "<ShowQuote xmlns=\"http://www.openapplications.org/oagis/9\" xmlns:ns2=\"urn:cisco:b2bngc:services:1.0\"><ApplicationArea><Sender><LogicalID schemeAgencyName=\"847291911\"></LogicalID><ReferenceID></ReferenceID></Sender><Receiver><LogicalID schemeAgencyName=\"847291911\"></LogicalID></Receiver><CreationDateTime>2020-01-22T20:55:28Z</CreationDateTime><BODID>2c9d02c6-af97-409a-b3de-6a20c1ec4ea7</BODID></ApplicationArea><DataArea><Show><ResponseCriteria><ChangeStatus><Reason>Success</Reason></ChangeStatus></ResponseCriteria></Show><Quote><QuoteHeader><DocumentID><ID>4722826637</ID></DocumentID><LastModificationDateTime>2019-10-24</LastModificationDateTime><DocumentDateTime>2019-10-24</DocumentDateTime><Description type=\"TradeInIdentifier\"></Description><Description type=\"ShareAccessKey\">WLAE4722826637</Description><Status><Code listAgencyName=\"Cisco\" listName=\"QuoteStatus\">Approved</Code></Status><Party role=\"QuoteOwner\"><Contact><ID>matt.fredrickson</ID></Contact></Party><Party role=\"End Customer\"><Name>TATA COMMUNICATIONS</Name><Location><Address><LineOne>10 FENCHURCH STREET</LineOne><LineTwo></LineTwo><LineThree></LineThree><CityName>LONDON</CityName><CountrySubDivisionCode>LONDON</CountrySubDivisionCode><CountryCode>GB</CountryCode><PostalCode>EC3M 3BE</PostalCode></Address></Location></Party><Party role=\"Partner\"><PartyIDs><ID>Tata Communications UK Ltd</ID></PartyIDs><Location><Address><LineOne>UPPER THAMES STREET</LineOne><LineTwo>62 VINTNERS PLACE</LineTwo><LineThree></LineThree><CityName>LONDON</CityName><CountrySubDivisionCode>LONDON</CountrySubDivisionCode><CountryCode>GB</CountryCode><PostalCode>EC4V 3BJ</PostalCode></Address></Location><Contact><Name sequenceName=\"First Name\">Matt</Name><Name sequenceName=\"Last Name\">Fredrickson</Name><JobTitle>NA</JobTitle><TelephoneCommunication><FormattedNumber>44 07773325042</FormattedNumber></TelephoneCommunication><EMailAddressCommunication><EMailAddressID>matt.fredrickson@tatacommunications.com</EMailAddressID></EMailAddressCommunication></Contact></Party><BillToParty><Location><Address><ID>1001126166</ID></Address><Description type=\"OperatingUnit\">CISCO AUSTRALIA OPERATING UNIT</Description></Location></BillToParty><EffectiveTimePeriod><EndDateTime>2020-04-21</EndDateTime></EffectiveTimePeriod><QualificationTerm typeAttribute=\"Deal\"><ID schemeAgencyName=\"Cisco\">42293222</ID></QualificationTerm><UserArea><ns2:CiscoExtensions><ns2:CiscoHeader><ns2:IntendedUseCode>Resale</ns2:IntendedUseCode><ns2:PriceList><ns2:ID>1110</ns2:ID><Description>Global Price List EMEA Availability</Description></ns2:PriceList><ns2:PriceList><ns2:ShortName>GLEMEA</ns2:ShortName></ns2:PriceList><ns2:ConfigurationMessages><ID></ID><Description></Description><Reason></Reason></ns2:ConfigurationMessages></ns2:CiscoHeader></ns2:CiscoExtensions></UserArea><Extension/></QuoteHeader><QuoteLine><LineNumber>5331606632</LineNumber><Item><ItemID><ID schemeName=\"PartNumber\" schemeAgencyName=\"Cisco\">A-FLEX</ID></ItemID><Description>Collaboration Flex Plan</Description><Description type=\"ServiceLevelName\"></Description><Description type=\"ServiceType\"></Description><Classification><Codes><Code listAgencyName=\"UNSPSC\">43222600</Code></Codes><Type listAgencyName=\"Cisco\" listName=\"ProductType\">CONFIGURABLE</Type></Classification><Specification><Property><ParentID>0</ParentID><NameValue name=\"BundleIndicator\">N</NameValue><Description type=\"Redundancy\"></Description><Description type=\"Default\"></Description><Description type=\"OrderedSkuIndicator\">N</Description><Effectivity/><Effectivity><Type>ServiceDuration</Type><EffectiveTimePeriod><Duration>P0Y0M0DT0H0M</Duration></EffectiveTimePeriod></Effectivity></Property><Property><NameValue name=\"CCWLineNumber\">1.0</NameValue></Property><Property><NameValue name=\"UnitNetPrice\">.00</NameValue></Property><Property><NameValue name=\"UnitNetPriceBeforeCredits\">.00</NameValue></Property></Specification></Item><Quantity>1</Quantity><UnitPrice><Amount currencyID=\"USD\">0.00</Amount></UnitPrice><ExtendedAmount currencyID=\"USD\">0.00</ExtendedAmount><TotalAmount currencyID=\"USD\">0.00</TotalAmount><PaymentTerm><Discount><Type listName=\"CiscoDiscountType\">TotalDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">StandardDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PromotionalDiscount</Type><DiscountPercent>18.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">ContractualDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">NonStandardDiscount</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PrePay</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">EffectiveDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount></PaymentTerm><Allowance><Type>Trade in</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Additional Credit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>TotalCredit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Subscription Credits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>MonthlyCredits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><UserArea><ns2:CiscoExtensions><ns2:CiscoLine><ns2:BuyMethod>Cisco</ns2:BuyMethod><Party category=\"InstallAddress\"><Location><Address><LineOne></LineOne><LineTwo></LineTwo><LineThree></LineThree><CityName></CityName><CountrySubDivisionCode></CountrySubDivisionCode><CountryCode>GB</CountryCode><PostalCode></PostalCode></Address></Location></Party><ns2:ConfigurationReference><Status><Reason></Reason></Status></ns2:ConfigurationReference><ns2:ConfigurationReference><ns2:VerifiedConfigurationIndicator></ns2:VerifiedConfigurationIndicator></ns2:ConfigurationReference><ns2:ConfiguratorInformation><ns2:ConfigurationPath>A-FLEX</ns2:ConfigurationPath><ns2:ProductConfigurationReference>C68730559</ns2:ProductConfigurationReference><ns2:ConfigurationSelectCode>USER</ns2:ConfigurationSelectCode></ns2:ConfiguratorInformation><ns2:MagicKey>Qa13553d5-fc0e-4354-b63c-ebc38f71e6eb</ns2:MagicKey><ns2:InitialTerm>12</ns2:InitialTerm></ns2:CiscoLine></ns2:CiscoExtensions></UserArea></QuoteLine><QuoteLine><LineNumber>5331606633</LineNumber><Item><ItemID><ID schemeName=\"PartNumber\" schemeAgencyName=\"Cisco\">SVS-SPK-SUPT-BAS</ID></ItemID><Description>Basic Support for Cisco Spark</Description><Description type=\"ServiceLevelName\">SWCS</Description><Description type=\"ServiceType\"></Description><Classification><Codes><Code listAgencyName=\"UNSPSC\">43222600</Code></Codes><Type listAgencyName=\"Cisco\" listName=\"ProductType\"></Type></Classification><Specification><Property><ParentID>5331606632</ParentID><NameValue name=\"BundleIndicator\">N</NameValue><Description type=\"Redundancy\"></Description><Description type=\"Default\"></Description><Description type=\"OrderedSkuIndicator\">N</Description><Effectivity/><Effectivity><Type>ServiceDuration</Type><EffectiveTimePeriod><Duration>P0Y0M0DT0H0M</Duration></EffectiveTimePeriod></Effectivity></Property><Property><NameValue name=\"CCWLineNumber\">1.1</NameValue></Property><Property><NameValue name=\"UnitNetPrice\">.00</NameValue></Property><Property><NameValue name=\"UnitNetPriceBeforeCredits\">.00</NameValue></Property></Specification></Item><Quantity>1</Quantity><UnitPrice><Amount currencyID=\"USD\">0.00</Amount></UnitPrice><ExtendedAmount currencyID=\"USD\">0.00</ExtendedAmount><TotalAmount currencyID=\"USD\">0.00</TotalAmount><PaymentTerm><Discount><Type listName=\"CiscoDiscountType\">TotalDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">StandardDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PromotionalDiscount</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">ContractualDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">NonStandardDiscount</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PrePay</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">EffectiveDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount></PaymentTerm><Allowance><Type>Trade in</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Additional Credit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>TotalCredit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Subscription Credits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>MonthlyCredits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><UserArea><ns2:CiscoExtensions><ns2:CiscoLine><ns2:BuyMethod>Cisco</ns2:BuyMethod><Party category=\"InstallAddress\"><Location><Address><LineOne></LineOne><LineTwo></LineTwo><LineThree></LineThree><CityName></CityName><CountrySubDivisionCode></CountrySubDivisionCode><CountryCode>GB</CountryCode><PostalCode></PostalCode></Address></Location></Party><ns2:ConfigurationReference><Status><Reason></Reason></Status></ns2:ConfigurationReference><ns2:ConfigurationReference><ns2:VerifiedConfigurationIndicator></ns2:VerifiedConfigurationIndicator></ns2:ConfigurationReference><ns2:ConfiguratorInformation><ns2:ConfigurationPath>SVS-SPK-SUPT-BAS</ns2:ConfigurationPath><ns2:ProductConfigurationReference>C60360706</ns2:ProductConfigurationReference><ns2:ConfigurationSelectCode>USER</ns2:ConfigurationSelectCode></ns2:ConfiguratorInformation><ns2:MagicKey>Q73a89833-e33b-4b30-9901-43d2dbb5aa98</ns2:MagicKey><ns2:InitialTerm>12</ns2:InitialTerm></ns2:CiscoLine></ns2:CiscoExtensions></UserArea></QuoteLine><QuoteLine><LineNumber>5331606634</LineNumber><Item><ItemID><ID schemeName=\"PartNumber\" schemeAgencyName=\"Cisco\">A-FLEX-SEC-PK-ENT</ID></ItemID><Description>Extended Security Pack Entitlement</Description><Description type=\"ServiceLevelName\"></Description><Description type=\"ServiceType\"></Description><Classification><Codes><Code listAgencyName=\"UNSPSC\">43222600</Code></Codes><Type listAgencyName=\"Cisco\" listName=\"ProductType\">NONCONFIGURABLE</Type></Classification><Specification><Property><ParentID>5331606632</ParentID><NameValue name=\"BundleIndicator\">N</NameValue><Description type=\"Redundancy\"></Description><Description type=\"Default\"></Description><Description type=\"OrderedSkuIndicator\">N</Description><Effectivity/><Effectivity><Type>ServiceDuration</Type><EffectiveTimePeriod><Duration>P0Y0M0DT0H0M</Duration></EffectiveTimePeriod></Effectivity></Property><Property><NameValue name=\"CCWLineNumber\">1.2</NameValue></Property><Property><NameValue name=\"UnitNetPrice\">.00</NameValue></Property><Property><NameValue name=\"UnitNetPriceBeforeCredits\">.00</NameValue></Property></Specification></Item><Quantity>10000</Quantity><UnitPrice><Amount currencyID=\"USD\">0.00</Amount></UnitPrice><ExtendedAmount currencyID=\"USD\">0.00</ExtendedAmount><TotalAmount currencyID=\"USD\">0.00</TotalAmount><PaymentTerm><Discount><Type listName=\"CiscoDiscountType\">TotalDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">StandardDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PromotionalDiscount</Type><DiscountPercent>18.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">ContractualDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">NonStandardDiscount</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PrePay</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">EffectiveDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount></PaymentTerm><Allowance><Type>Trade in</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Additional Credit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>TotalCredit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Subscription Credits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>MonthlyCredits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><UserArea><ns2:CiscoExtensions><ns2:CiscoLine><ns2:BuyMethod>Cisco</ns2:BuyMethod><Party category=\"InstallAddress\"><Location><Address><LineOne></LineOne><LineTwo></LineTwo><LineThree></LineThree><CityName></CityName><CountrySubDivisionCode></CountrySubDivisionCode><CountryCode>GB</CountryCode><PostalCode></PostalCode></Address></Location></Party><ns2:ConfigurationReference><Status><Reason></Reason></Status></ns2:ConfigurationReference><ns2:ConfigurationReference><ns2:VerifiedConfigurationIndicator></ns2:VerifiedConfigurationIndicator></ns2:ConfigurationReference><ns2:ConfiguratorInformation><ns2:ConfigurationPath>A-FLEX-SEC-PK-ENT</ns2:ConfigurationPath><ns2:ProductConfigurationReference>C54042960</ns2:ProductConfigurationReference><ns2:ConfigurationSelectCode>USER</ns2:ConfigurationSelectCode></ns2:ConfiguratorInformation><ns2:MagicKey>Qe5f66486-85c3-4607-93e8-bf86ba576083</ns2:MagicKey><ns2:InitialTerm>12</ns2:InitialTerm></ns2:CiscoLine></ns2:CiscoExtensions></UserArea></QuoteLine><QuoteLine><LineNumber>5331606635</LineNumber><Item><ItemID><ID schemeName=\"PartNumber\" schemeAgencyName=\"Cisco\">A-FLEX-EDGAUD-USER</ID></ItemID><Description>A-FLEX Webex Edge Audio</Description><Description type=\"ServiceLevelName\"></Description><Description type=\"ServiceType\"></Description><Classification><Codes><Code listAgencyName=\"UNSPSC\">43222600</Code></Codes><Type listAgencyName=\"Cisco\" listName=\"ProductType\">NONCONFIGURABLE</Type></Classification><Specification><Property><ParentID>5331606632</ParentID><NameValue name=\"BundleIndicator\">N</NameValue><Description type=\"Redundancy\"></Description><Description type=\"Default\"></Description><Description type=\"OrderedSkuIndicator\">N</Description><Effectivity/><Effectivity><Type>ServiceDuration</Type><EffectiveTimePeriod><Duration>P0Y0M0DT0H0M</Duration></EffectiveTimePeriod></Effectivity></Property><Property><NameValue name=\"CCWLineNumber\">1.3</NameValue></Property><Property><NameValue name=\"UnitNetPrice\">.00</NameValue></Property><Property><NameValue name=\"UnitNetPriceBeforeCredits\">.00</NameValue></Property></Specification></Item><Quantity>1500</Quantity><UnitPrice><Amount currencyID=\"USD\">0.00</Amount></UnitPrice><ExtendedAmount currencyID=\"USD\">0.00</ExtendedAmount><TotalAmount currencyID=\"USD\">0.00</TotalAmount><PaymentTerm><Discount><Type listName=\"CiscoDiscountType\">TotalDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">StandardDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PromotionalDiscount</Type><DiscountPercent>18.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">ContractualDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">NonStandardDiscount</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PrePay</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">EffectiveDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount></PaymentTerm><Allowance><Type>Trade in</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Additional Credit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>TotalCredit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Subscription Credits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>MonthlyCredits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><UserArea><ns2:CiscoExtensions><ns2:CiscoLine><ns2:BuyMethod>Cisco</ns2:BuyMethod><Party category=\"InstallAddress\"><Location><Address><LineOne></LineOne><LineTwo></LineTwo><LineThree></LineThree><CityName></CityName><CountrySubDivisionCode></CountrySubDivisionCode><CountryCode>GB</CountryCode><PostalCode></PostalCode></Address></Location></Party><ns2:ConfigurationReference><Status><Reason></Reason></Status></ns2:ConfigurationReference><ns2:ConfigurationReference><ns2:VerifiedConfigurationIndicator></ns2:VerifiedConfigurationIndicator></ns2:ConfigurationReference><ns2:ConfiguratorInformation><ns2:ConfigurationPath>A-FLEX-EDGAUD-USER</ns2:ConfigurationPath><ns2:ProductConfigurationReference>C29134752</ns2:ProductConfigurationReference><ns2:ConfigurationSelectCode>USER</ns2:ConfigurationSelectCode></ns2:ConfiguratorInformation><ns2:MagicKey>Q42f1eed2-ea95-428d-8480-35eaba8cb131</ns2:MagicKey><ns2:InitialTerm>12</ns2:InitialTerm></ns2:CiscoLine></ns2:CiscoExtensions></UserArea></QuoteLine><QuoteLine><LineNumber>5331606636</LineNumber><Item><ItemID><ID schemeName=\"PartNumber\" schemeAgencyName=\"Cisco\">A-SPK-VOIP</ID></ItemID><Description>Included  VoIP (1)</Description><Description type=\"ServiceLevelName\"></Description><Description type=\"ServiceType\"></Description><Classification><Codes><Code listAgencyName=\"UNSPSC\">43222600</Code></Codes><Type listAgencyName=\"Cisco\" listName=\"ProductType\">NONCONFIGURABLE</Type></Classification><Specification><Property><ParentID>5331606632</ParentID><NameValue name=\"BundleIndicator\">N</NameValue><Description type=\"Redundancy\"></Description><Description type=\"Default\"></Description><Description type=\"OrderedSkuIndicator\">N</Description><Effectivity/><Effectivity><Type>ServiceDuration</Type><EffectiveTimePeriod><Duration>P0Y0M0DT0H0M</Duration></EffectiveTimePeriod></Effectivity></Property><Property><NameValue name=\"CCWLineNumber\">1.4</NameValue></Property><Property><NameValue name=\"UnitNetPrice\">.00</NameValue></Property><Property><NameValue name=\"UnitNetPriceBeforeCredits\">.00</NameValue></Property></Specification></Item><Quantity>1</Quantity><UnitPrice><Amount currencyID=\"USD\">0.00</Amount></UnitPrice><ExtendedAmount currencyID=\"USD\">0.00</ExtendedAmount><TotalAmount currencyID=\"USD\">0.00</TotalAmount><PaymentTerm><Discount><Type listName=\"CiscoDiscountType\">TotalDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">StandardDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PromotionalDiscount</Type><DiscountPercent>18.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">ContractualDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">NonStandardDiscount</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PrePay</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">EffectiveDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount></PaymentTerm><Allowance><Type>Trade in</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Additional Credit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>TotalCredit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Subscription Credits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>MonthlyCredits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><UserArea><ns2:CiscoExtensions><ns2:CiscoLine><ns2:BuyMethod>Cisco</ns2:BuyMethod><Party category=\"InstallAddress\"><Location><Address><LineOne></LineOne><LineTwo></LineTwo><LineThree></LineThree><CityName></CityName><CountrySubDivisionCode></CountrySubDivisionCode><CountryCode>GB</CountryCode><PostalCode></PostalCode></Address></Location></Party><ns2:ConfigurationReference><Status><Reason></Reason></Status></ns2:ConfigurationReference><ns2:ConfigurationReference><ns2:VerifiedConfigurationIndicator></ns2:VerifiedConfigurationIndicator></ns2:ConfigurationReference><ns2:ConfiguratorInformation><ns2:ConfigurationPath>A-SPK-VOIP</ns2:ConfigurationPath><ns2:ProductConfigurationReference>C29491644</ns2:ProductConfigurationReference><ns2:ConfigurationSelectCode>USER</ns2:ConfigurationSelectCode></ns2:ConfiguratorInformation><ns2:MagicKey>Qb8a1e8e7-db55-4a79-a8ef-22c8897aad1c</ns2:MagicKey><ns2:InitialTerm>12</ns2:InitialTerm></ns2:CiscoLine></ns2:CiscoExtensions></UserArea></QuoteLine><QuoteLine><LineNumber>5331606637</LineNumber><Item><ItemID><ID schemeName=\"PartNumber\" schemeAgencyName=\"Cisco\">A-FLEX-MSG-ENT</ID></ItemID><Description>Messaging Entitlement</Description><Description type=\"ServiceLevelName\"></Description><Description type=\"ServiceType\"></Description><Classification><Codes><Code listAgencyName=\"UNSPSC\">43222600</Code></Codes><Type listAgencyName=\"Cisco\" listName=\"ProductType\">NONCONFIGURABLE</Type></Classification><Specification><Property><ParentID>5331606632</ParentID><NameValue name=\"BundleIndicator\">N</NameValue><Description type=\"Redundancy\"></Description><Description type=\"Default\"></Description><Description type=\"OrderedSkuIndicator\">N</Description><Effectivity/><Effectivity><Type>ServiceDuration</Type><EffectiveTimePeriod><Duration>P0Y0M0DT0H0M</Duration></EffectiveTimePeriod></Effectivity></Property><Property><NameValue name=\"CCWLineNumber\">1.5</NameValue></Property><Property><NameValue name=\"UnitNetPrice\">.00</NameValue></Property><Property><NameValue name=\"UnitNetPriceBeforeCredits\">.00</NameValue></Property></Specification></Item><Quantity>12000</Quantity><UnitPrice><Amount currencyID=\"USD\">0.00</Amount></UnitPrice><ExtendedAmount currencyID=\"USD\">0.00</ExtendedAmount><TotalAmount currencyID=\"USD\">0.00</TotalAmount><PaymentTerm><Discount><Type listName=\"CiscoDiscountType\">TotalDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">StandardDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PromotionalDiscount</Type><DiscountPercent>18.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">ContractualDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">NonStandardDiscount</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PrePay</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">EffectiveDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount></PaymentTerm><Allowance><Type>Trade in</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Additional Credit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>TotalCredit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Subscription Credits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>MonthlyCredits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><UserArea><ns2:CiscoExtensions><ns2:CiscoLine><ns2:BuyMethod>Cisco</ns2:BuyMethod><Party category=\"InstallAddress\"><Location><Address><LineOne></LineOne><LineTwo></LineTwo><LineThree></LineThree><CityName></CityName><CountrySubDivisionCode></CountrySubDivisionCode><CountryCode>GB</CountryCode><PostalCode></PostalCode></Address></Location></Party><ns2:ConfigurationReference><Status><Reason></Reason></Status></ns2:ConfigurationReference><ns2:ConfigurationReference><ns2:VerifiedConfigurationIndicator></ns2:VerifiedConfigurationIndicator></ns2:ConfigurationReference><ns2:ConfiguratorInformation><ns2:ConfigurationPath>A-FLEX-MSG-ENT</ns2:ConfigurationPath><ns2:ProductConfigurationReference>C41605192</ns2:ProductConfigurationReference><ns2:ConfigurationSelectCode>USER</ns2:ConfigurationSelectCode></ns2:ConfiguratorInformation><ns2:MagicKey>Qd3d9643f-5cec-4944-8218-4090d23660ae</ns2:MagicKey><ns2:InitialTerm>12</ns2:InitialTerm></ns2:CiscoLine></ns2:CiscoExtensions></UserArea></QuoteLine><QuoteLine><LineNumber>5331606638</LineNumber><Item><ItemID><ID schemeName=\"PartNumber\" schemeAgencyName=\"Cisco\">A-FLEX-STORAGE-1TB</ID></ItemID><Description>Messaging File Storage 1TB Add-on (1)</Description><Description type=\"ServiceLevelName\"></Description><Description type=\"ServiceType\"></Description><Classification><Codes><Code listAgencyName=\"UNSPSC\">43222600</Code></Codes><Type listAgencyName=\"Cisco\" listName=\"ProductType\">NONCONFIGURABLE</Type></Classification><Specification><Property><ParentID>5331606632</ParentID><NameValue name=\"BundleIndicator\">N</NameValue><Description type=\"Redundancy\"></Description><Description type=\"Default\"></Description><Description type=\"OrderedSkuIndicator\">N</Description><Effectivity/><Effectivity><Type>ServiceDuration</Type><EffectiveTimePeriod><Duration>P0Y0M0DT0H0M</Duration></EffectiveTimePeriod></Effectivity></Property><Property><NameValue name=\"CCWLineNumber\">1.6</NameValue></Property><Property><NameValue name=\"UnitNetPrice\">62.00</NameValue></Property><Property><NameValue name=\"UnitNetPriceBeforeCredits\">62.00</NameValue></Property></Specification></Item><Quantity>10</Quantity><UnitPrice><Amount currencyID=\"USD\">100.00</Amount></UnitPrice><ExtendedAmount currencyID=\"USD\">12000.00</ExtendedAmount><TotalAmount currencyID=\"USD\">7440.00</TotalAmount><PaymentTerm><Discount><Type listName=\"CiscoDiscountType\">TotalDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">StandardDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PromotionalDiscount</Type><DiscountPercent>18.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">ContractualDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">NonStandardDiscount</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PrePay</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">EffectiveDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount></PaymentTerm><Allowance><Type>Trade in</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Additional Credit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>TotalCredit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Subscription Credits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>MonthlyCredits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><UserArea><ns2:CiscoExtensions><ns2:CiscoLine><ns2:BuyMethod>Cisco</ns2:BuyMethod><Party category=\"InstallAddress\"><Location><Address><LineOne></LineOne><LineTwo></LineTwo><LineThree></LineThree><CityName></CityName><CountrySubDivisionCode></CountrySubDivisionCode><CountryCode>GB</CountryCode><PostalCode></PostalCode></Address></Location></Party><ns2:ConfigurationReference><Status><Reason></Reason></Status></ns2:ConfigurationReference><ns2:ConfigurationReference><ns2:VerifiedConfigurationIndicator></ns2:VerifiedConfigurationIndicator></ns2:ConfigurationReference><ns2:ConfiguratorInformation><ns2:ConfigurationPath>A-FLEX-STORAGE-1TB</ns2:ConfigurationPath><ns2:ProductConfigurationReference>C66027358</ns2:ProductConfigurationReference><ns2:ConfigurationSelectCode>USER</ns2:ConfigurationSelectCode></ns2:ConfiguratorInformation><ns2:MagicKey>Qf37759de-4f79-4398-8a46-1a4fd6e65b5a</ns2:MagicKey><ns2:InitialTerm>12</ns2:InitialTerm></ns2:CiscoLine></ns2:CiscoExtensions></UserArea></QuoteLine><QuoteLine><LineNumber>5331606639</LineNumber><Item><ItemID><ID schemeName=\"PartNumber\" schemeAgencyName=\"Cisco\">A-FLEX-MEET-ENT</ID></ItemID><Description>Cloud Meetings Entitlement</Description><Description type=\"ServiceLevelName\"></Description><Description type=\"ServiceType\"></Description><Classification><Codes><Code listAgencyName=\"UNSPSC\">43222600</Code></Codes><Type listAgencyName=\"Cisco\" listName=\"ProductType\">NONCONFIGURABLE</Type></Classification><Specification><Property><ParentID>5331606632</ParentID><NameValue name=\"BundleIndicator\">N</NameValue><Description type=\"Redundancy\"></Description><Description type=\"Default\"></Description><Description type=\"OrderedSkuIndicator\">N</Description><Effectivity/><Effectivity><Type>ServiceDuration</Type><EffectiveTimePeriod><Duration>P0Y0M0DT0H0M</Duration></EffectiveTimePeriod></Effectivity></Property><Property><NameValue name=\"CCWLineNumber\">1.7</NameValue></Property><Property><NameValue name=\"UnitNetPrice\">.00</NameValue></Property><Property><NameValue name=\"UnitNetPriceBeforeCredits\">.00</NameValue></Property></Specification></Item><Quantity>12000</Quantity><UnitPrice><Amount currencyID=\"USD\">0.00</Amount></UnitPrice><ExtendedAmount currencyID=\"USD\">0.00</ExtendedAmount><TotalAmount currencyID=\"USD\">0.00</TotalAmount><PaymentTerm><Discount><Type listName=\"CiscoDiscountType\">TotalDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">StandardDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PromotionalDiscount</Type><DiscountPercent>18.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">ContractualDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">NonStandardDiscount</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PrePay</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">EffectiveDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount></PaymentTerm><Allowance><Type>Trade in</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Additional Credit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>TotalCredit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Subscription Credits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>MonthlyCredits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><UserArea><ns2:CiscoExtensions><ns2:CiscoLine><ns2:BuyMethod>Cisco</ns2:BuyMethod><Party category=\"InstallAddress\"><Location><Address><LineOne></LineOne><LineTwo></LineTwo><LineThree></LineThree><CityName></CityName><CountrySubDivisionCode></CountrySubDivisionCode><CountryCode>GB</CountryCode><PostalCode></PostalCode></Address></Location></Party><ns2:ConfigurationReference><Status><Reason></Reason></Status></ns2:ConfigurationReference><ns2:ConfigurationReference><ns2:VerifiedConfigurationIndicator></ns2:VerifiedConfigurationIndicator></ns2:ConfigurationReference><ns2:ConfiguratorInformation><ns2:ConfigurationPath>A-FLEX-MEET-ENT</ns2:ConfigurationPath><ns2:ProductConfigurationReference>C38251335</ns2:ProductConfigurationReference><ns2:ConfigurationSelectCode>USER</ns2:ConfigurationSelectCode></ns2:ConfiguratorInformation><ns2:MagicKey>Qd6a1bf88-8dff-4131-a992-c913428c9e08</ns2:MagicKey><ns2:InitialTerm>12</ns2:InitialTerm></ns2:CiscoLine></ns2:CiscoExtensions></UserArea></QuoteLine><QuoteLine><LineNumber>5331606640</LineNumber><Item><ItemID><ID schemeName=\"PartNumber\" schemeAgencyName=\"Cisco\">A-FLEX-DEVREG-ENT</ID></ItemID><Description>Cloud Device Registration Entitlement</Description><Description type=\"ServiceLevelName\"></Description><Description type=\"ServiceType\"></Description><Classification><Codes><Code listAgencyName=\"UNSPSC\">43222600</Code></Codes><Type listAgencyName=\"Cisco\" listName=\"ProductType\">NONCONFIGURABLE</Type></Classification><Specification><Property><ParentID>5331606632</ParentID><NameValue name=\"BundleIndicator\">N</NameValue><Description type=\"Redundancy\"></Description><Description type=\"Default\"></Description><Description type=\"OrderedSkuIndicator\">N</Description><Effectivity/><Effectivity><Type>ServiceDuration</Type><EffectiveTimePeriod><Duration>P0Y0M0DT0H0M</Duration></EffectiveTimePeriod></Effectivity></Property><Property><NameValue name=\"CCWLineNumber\">1.8</NameValue></Property><Property><NameValue name=\"UnitNetPrice\">.00</NameValue></Property><Property><NameValue name=\"UnitNetPriceBeforeCredits\">.00</NameValue></Property></Specification></Item><Quantity>12000</Quantity><UnitPrice><Amount currencyID=\"USD\">0.00</Amount></UnitPrice><ExtendedAmount currencyID=\"USD\">0.00</ExtendedAmount><TotalAmount currencyID=\"USD\">0.00</TotalAmount><PaymentTerm><Discount><Type listName=\"CiscoDiscountType\">TotalDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">StandardDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PromotionalDiscount</Type><DiscountPercent>18.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">ContractualDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">NonStandardDiscount</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PrePay</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">EffectiveDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount></PaymentTerm><Allowance><Type>Trade in</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Additional Credit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>TotalCredit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Subscription Credits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>MonthlyCredits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><UserArea><ns2:CiscoExtensions><ns2:CiscoLine><ns2:BuyMethod>Cisco</ns2:BuyMethod><Party category=\"InstallAddress\"><Location><Address><LineOne></LineOne><LineTwo></LineTwo><LineThree></LineThree><CityName></CityName><CountrySubDivisionCode></CountrySubDivisionCode><CountryCode>GB</CountryCode><PostalCode></PostalCode></Address></Location></Party><ns2:ConfigurationReference><Status><Reason></Reason></Status></ns2:ConfigurationReference><ns2:ConfigurationReference><ns2:VerifiedConfigurationIndicator></ns2:VerifiedConfigurationIndicator></ns2:ConfigurationReference><ns2:ConfiguratorInformation><ns2:ConfigurationPath>A-FLEX-DEVREG-ENT</ns2:ConfigurationPath><ns2:ProductConfigurationReference>C58875089</ns2:ProductConfigurationReference><ns2:ConfigurationSelectCode>USER</ns2:ConfigurationSelectCode></ns2:ConfiguratorInformation><ns2:MagicKey>Q0ba8ff3c-19c1-4f1c-8229-a71125ee34d1</ns2:MagicKey><ns2:InitialTerm>12</ns2:InitialTerm></ns2:CiscoLine></ns2:CiscoExtensions></UserArea></QuoteLine><QuoteLine><LineNumber>5331606641</LineNumber><Item><ItemID><ID schemeName=\"PartNumber\" schemeAgencyName=\"Cisco\">A-FLEX-CCA-SP-USER</ID></ItemID><Description>CCA SP User</Description><Description type=\"ServiceLevelName\"></Description><Description type=\"ServiceType\"></Description><Classification><Codes><Code listAgencyName=\"UNSPSC\">43222600</Code></Codes><Type listAgencyName=\"Cisco\" listName=\"ProductType\">NONCONFIGURABLE</Type></Classification><Specification><Property><ParentID>5331606632</ParentID><NameValue name=\"BundleIndicator\">N</NameValue><Description type=\"Redundancy\"></Description><Description type=\"Default\"></Description><Description type=\"OrderedSkuIndicator\">N</Description><Effectivity/><Effectivity><Type>ServiceDuration</Type><EffectiveTimePeriod><Duration>P0Y0M0DT0H0M</Duration></EffectiveTimePeriod></Effectivity></Property><Property><NameValue name=\"CCWLineNumber\">1.9</NameValue></Property><Property><NameValue name=\"UnitNetPrice\">.00</NameValue></Property><Property><NameValue name=\"UnitNetPriceBeforeCredits\">.00</NameValue></Property></Specification></Item><Quantity>1500</Quantity><UnitPrice><Amount currencyID=\"USD\">0.00</Amount></UnitPrice><ExtendedAmount currencyID=\"USD\">0.00</ExtendedAmount><TotalAmount currencyID=\"USD\">0.00</TotalAmount><PaymentTerm><Discount><Type listName=\"CiscoDiscountType\">TotalDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">StandardDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PromotionalDiscount</Type><DiscountPercent>18.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">ContractualDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">NonStandardDiscount</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PrePay</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">EffectiveDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount></PaymentTerm><Allowance><Type>Trade in</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Additional Credit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>TotalCredit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Subscription Credits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>MonthlyCredits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><UserArea><ns2:CiscoExtensions><ns2:CiscoLine><ns2:BuyMethod>Cisco</ns2:BuyMethod><Party category=\"InstallAddress\"><Location><Address><LineOne></LineOne><LineTwo></LineTwo><LineThree></LineThree><CityName></CityName><CountrySubDivisionCode></CountrySubDivisionCode><CountryCode>GB</CountryCode><PostalCode></PostalCode></Address></Location></Party><ns2:ConfigurationReference><Status><Reason></Reason></Status></ns2:ConfigurationReference><ns2:ConfigurationReference><ns2:VerifiedConfigurationIndicator></ns2:VerifiedConfigurationIndicator></ns2:ConfigurationReference><ns2:ConfiguratorInformation><ns2:ConfigurationPath>A-FLEX-CCA-SP-USER</ns2:ConfigurationPath><ns2:ProductConfigurationReference>C21698977</ns2:ProductConfigurationReference><ns2:ConfigurationSelectCode>USER</ns2:ConfigurationSelectCode></ns2:ConfiguratorInformation><ns2:MagicKey>Qe2f300e0-9a6a-4e2a-a2e9-3abbc0d55db4</ns2:MagicKey><ns2:InitialTerm>12</ns2:InitialTerm></ns2:CiscoLine></ns2:CiscoExtensions></UserArea></QuoteLine><QuoteLine><LineNumber>5331606642</LineNumber><Item><ItemID><ID schemeName=\"PartNumber\" schemeAgencyName=\"Cisco\">A-FLEX-FILESTG-ENT</ID></ItemID><Description>File Storage Entitlement</Description><Description type=\"ServiceLevelName\"></Description><Description type=\"ServiceType\"></Description><Classification><Codes><Code listAgencyName=\"UNSPSC\">43222600</Code></Codes><Type listAgencyName=\"Cisco\" listName=\"ProductType\">NONCONFIGURABLE</Type></Classification><Specification><Property><ParentID>5331606632</ParentID><NameValue name=\"BundleIndicator\">N</NameValue><Description type=\"Redundancy\"></Description><Description type=\"Default\"></Description><Description type=\"OrderedSkuIndicator\">N</Description><Effectivity/><Effectivity><Type>ServiceDuration</Type><EffectiveTimePeriod><Duration>P0Y0M0DT0H0M</Duration></EffectiveTimePeriod></Effectivity></Property><Property><NameValue name=\"CCWLineNumber\">1.10</NameValue></Property><Property><NameValue name=\"UnitNetPrice\">.00</NameValue></Property><Property><NameValue name=\"UnitNetPriceBeforeCredits\">.00</NameValue></Property></Specification></Item><Quantity>240000</Quantity><UnitPrice><Amount currencyID=\"USD\">0.00</Amount></UnitPrice><ExtendedAmount currencyID=\"USD\">0.00</ExtendedAmount><TotalAmount currencyID=\"USD\">0.00</TotalAmount><PaymentTerm><Discount><Type listName=\"CiscoDiscountType\">TotalDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">StandardDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PromotionalDiscount</Type><DiscountPercent>18.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">ContractualDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">NonStandardDiscount</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PrePay</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">EffectiveDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount></PaymentTerm><Allowance><Type>Trade in</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Additional Credit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>TotalCredit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Subscription Credits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>MonthlyCredits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><UserArea><ns2:CiscoExtensions><ns2:CiscoLine><ns2:BuyMethod>Cisco</ns2:BuyMethod><Party category=\"InstallAddress\"><Location><Address><LineOne></LineOne><LineTwo></LineTwo><LineThree></LineThree><CityName></CityName><CountrySubDivisionCode></CountrySubDivisionCode><CountryCode>GB</CountryCode><PostalCode></PostalCode></Address></Location></Party><ns2:ConfigurationReference><Status><Reason></Reason></Status></ns2:ConfigurationReference><ns2:ConfigurationReference><ns2:VerifiedConfigurationIndicator></ns2:VerifiedConfigurationIndicator></ns2:ConfigurationReference><ns2:ConfiguratorInformation><ns2:ConfigurationPath>A-FLEX-FILESTG-ENT</ns2:ConfigurationPath><ns2:ProductConfigurationReference>C46774672</ns2:ProductConfigurationReference><ns2:ConfigurationSelectCode>USER</ns2:ConfigurationSelectCode></ns2:ConfiguratorInformation><ns2:MagicKey>Qcfee5c68-b6e0-4ecf-ad0d-9acc5f12244b</ns2:MagicKey><ns2:InitialTerm>12</ns2:InitialTerm></ns2:CiscoLine></ns2:CiscoExtensions></UserArea></QuoteLine><QuoteLine><LineNumber>5331606643</LineNumber><Item><ItemID><ID schemeName=\"PartNumber\" schemeAgencyName=\"Cisco\">A-FLEX-EDGECON-1GB</ID></ItemID><Description>Webex Edge Connect 1GB Peering Link US/UK/AMSTERDAM</Description><Description type=\"ServiceLevelName\"></Description><Description type=\"ServiceType\"></Description><Classification><Codes><Code listAgencyName=\"UNSPSC\">43222600</Code></Codes><Type listAgencyName=\"Cisco\" listName=\"ProductType\">NONCONFIGURABLE</Type></Classification><Specification><Property><ParentID>5331606632</ParentID><NameValue name=\"BundleIndicator\">N</NameValue><Description type=\"Redundancy\"></Description><Description type=\"Default\"></Description><Description type=\"OrderedSkuIndicator\">N</Description><Effectivity/><Effectivity><Type>ServiceDuration</Type><EffectiveTimePeriod><Duration>P0Y0M0DT0H0M</Duration></EffectiveTimePeriod></Effectivity></Property><Property><NameValue name=\"CCWLineNumber\">1.11</NameValue></Property><Property><NameValue name=\"UnitNetPrice\">2356.00</NameValue></Property><Property><NameValue name=\"UnitNetPriceBeforeCredits\">2356.00</NameValue></Property></Specification></Item><Quantity>1</Quantity><UnitPrice><Amount currencyID=\"USD\">3800.00</Amount></UnitPrice><ExtendedAmount currencyID=\"USD\">45600.00</ExtendedAmount><TotalAmount currencyID=\"USD\">28272.00</TotalAmount><PaymentTerm><Discount><Type listName=\"CiscoDiscountType\">TotalDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">StandardDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PromotionalDiscount</Type><DiscountPercent>18.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">ContractualDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">NonStandardDiscount</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PrePay</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">EffectiveDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount></PaymentTerm><Allowance><Type>Trade in</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Additional Credit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>TotalCredit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Subscription Credits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>MonthlyCredits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><UserArea><ns2:CiscoExtensions><ns2:CiscoLine><ns2:BuyMethod>Cisco</ns2:BuyMethod><Party category=\"InstallAddress\"><Location><Address><LineOne></LineOne><LineTwo></LineTwo><LineThree></LineThree><CityName></CityName><CountrySubDivisionCode></CountrySubDivisionCode><CountryCode>GB</CountryCode><PostalCode></PostalCode></Address></Location></Party><ns2:ConfigurationReference><Status><Reason></Reason></Status></ns2:ConfigurationReference><ns2:ConfigurationReference><ns2:VerifiedConfigurationIndicator></ns2:VerifiedConfigurationIndicator></ns2:ConfigurationReference><ns2:ConfiguratorInformation><ns2:ConfigurationPath>A-FLEX-EDGECON-1GB</ns2:ConfigurationPath><ns2:ProductConfigurationReference>C73118847</ns2:ProductConfigurationReference><ns2:ConfigurationSelectCode>USER</ns2:ConfigurationSelectCode></ns2:ConfiguratorInformation><ns2:MagicKey>Q5038771c-6b37-45d1-b229-88017af6d317</ns2:MagicKey><ns2:InitialTerm>12</ns2:InitialTerm></ns2:CiscoLine></ns2:CiscoExtensions></UserArea></QuoteLine><QuoteLine><LineNumber>5331606644</LineNumber><Item><ItemID><ID schemeName=\"PartNumber\" schemeAgencyName=\"Cisco\">A-FLEX-AUCM3</ID></ItemID><Description>AU Cloud Meetings Tier 3 (1)</Description><Description type=\"ServiceLevelName\"></Description><Description type=\"ServiceType\"></Description><Classification><Codes><Code listAgencyName=\"UNSPSC\">43222600</Code></Codes><Type listAgencyName=\"Cisco\" listName=\"ProductType\">NONCONFIGURABLE</Type></Classification><Specification><Property><ParentID>5331606632</ParentID><NameValue name=\"BundleIndicator\">N</NameValue><Description type=\"Redundancy\"></Description><Description type=\"Default\"></Description><Description type=\"OrderedSkuIndicator\">N</Description><Effectivity/><Effectivity><Type>ServiceDuration</Type><EffectiveTimePeriod><Duration>P0Y0M0DT0H0M</Duration></EffectiveTimePeriod></Effectivity></Property><Property><NameValue name=\"CCWLineNumber\">1.12</NameValue></Property><Property><NameValue name=\"UnitNetPrice\">19.53</NameValue></Property><Property><NameValue name=\"UnitNetPriceBeforeCredits\">19.53</NameValue></Property></Specification></Item><Quantity>1500</Quantity><UnitPrice><Amount currencyID=\"USD\">31.50</Amount></UnitPrice><ExtendedAmount currencyID=\"USD\">567000.00</ExtendedAmount><TotalAmount currencyID=\"USD\">351540.00</TotalAmount><PaymentTerm><Discount><Type listName=\"CiscoDiscountType\">TotalDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">StandardDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PromotionalDiscount</Type><DiscountPercent>18.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">ContractualDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">NonStandardDiscount</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PrePay</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">EffectiveDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount></PaymentTerm><Allowance><Type>Trade in</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Additional Credit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>TotalCredit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Subscription Credits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>MonthlyCredits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><UserArea><ns2:CiscoExtensions><ns2:CiscoLine><ns2:BuyMethod>Cisco</ns2:BuyMethod><Party category=\"InstallAddress\"><Location><Address><LineOne></LineOne><LineTwo></LineTwo><LineThree></LineThree><CityName></CityName><CountrySubDivisionCode></CountrySubDivisionCode><CountryCode>GB</CountryCode><PostalCode></PostalCode></Address></Location></Party><ns2:ConfigurationReference><Status><Reason></Reason></Status></ns2:ConfigurationReference><ns2:ConfigurationReference><ns2:VerifiedConfigurationIndicator></ns2:VerifiedConfigurationIndicator></ns2:ConfigurationReference><ns2:ConfiguratorInformation><ns2:ConfigurationPath>A-FLEX-AUCM3</ns2:ConfigurationPath><ns2:ProductConfigurationReference>C59296957</ns2:ProductConfigurationReference><ns2:ConfigurationSelectCode>USER</ns2:ConfigurationSelectCode></ns2:ConfiguratorInformation><ns2:MagicKey>Q66793a66-9140-4392-8b44-8c2d346043df</ns2:MagicKey><ns2:InitialTerm>12</ns2:InitialTerm></ns2:CiscoLine></ns2:CiscoExtensions></UserArea></QuoteLine><QuoteLine><LineNumber>5331606645</LineNumber><Item><ItemID><ID schemeName=\"PartNumber\" schemeAgencyName=\"Cisco\">A-FLEX-AU3-SEC-PK</ID></ItemID><Description>Extended Security Pack Active User add-on for 10,000+ KWs</Description><Description type=\"ServiceLevelName\"></Description><Description type=\"ServiceType\"></Description><Classification><Codes><Code listAgencyName=\"UNSPSC\">43222600</Code></Codes><Type listAgencyName=\"Cisco\" listName=\"ProductType\">NONCONFIGURABLE</Type></Classification><Specification><Property><ParentID>5331606632</ParentID><NameValue name=\"BundleIndicator\">N</NameValue><Description type=\"Redundancy\"></Description><Description type=\"Default\"></Description><Description type=\"OrderedSkuIndicator\">N</Description><Effectivity/><Effectivity><Type>ServiceDuration</Type><EffectiveTimePeriod><Duration>P0Y0M0DT0H0M</Duration></EffectiveTimePeriod></Effectivity></Property><Property><NameValue name=\"CCWLineNumber\">1.13</NameValue></Property><Property><NameValue name=\"UnitNetPrice\">3.10</NameValue></Property><Property><NameValue name=\"UnitNetPriceBeforeCredits\">3.10</NameValue></Property></Specification></Item><Quantity>1500</Quantity><UnitPrice><Amount currencyID=\"USD\">5.00</Amount></UnitPrice><ExtendedAmount currencyID=\"USD\">90000.00</ExtendedAmount><TotalAmount currencyID=\"USD\">55800.00</TotalAmount><PaymentTerm><Discount><Type listName=\"CiscoDiscountType\">TotalDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">StandardDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PromotionalDiscount</Type><DiscountPercent>18.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">ContractualDiscount</Type><DiscountPercent>20.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">NonStandardDiscount</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">PrePay</Type><DiscountPercent>0.00</DiscountPercent></Discount><Discount><Type listName=\"CiscoDiscountType\">EffectiveDiscount</Type><DiscountPercent>38.00</DiscountPercent></Discount></PaymentTerm><Allowance><Type>Trade in</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Additional Credit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>TotalCredit</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>Subscription Credits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><Allowance><Type>MonthlyCredits</Type><Amount currencyID=\"USD\">0.00</Amount></Allowance><UserArea><ns2:CiscoExtensions><ns2:CiscoLine><ns2:BuyMethod>Cisco</ns2:BuyMethod><Party category=\"InstallAddress\"><Location><Address><LineOne></LineOne><LineTwo></LineTwo><LineThree></LineThree><CityName></CityName><CountrySubDivisionCode></CountrySubDivisionCode><CountryCode>GB</CountryCode><PostalCode></PostalCode></Address></Location></Party><ns2:ConfigurationReference><Status><Reason></Reason></Status></ns2:ConfigurationReference><ns2:ConfigurationReference><ns2:VerifiedConfigurationIndicator></ns2:VerifiedConfigurationIndicator></ns2:ConfigurationReference><ns2:ConfiguratorInformation><ns2:ConfigurationPath>A-FLEX-AU3-SEC-PK</ns2:ConfigurationPath><ns2:ProductConfigurationReference>C58295879</ns2:ProductConfigurationReference><ns2:ConfigurationSelectCode>USER</ns2:ConfigurationSelectCode></ns2:ConfiguratorInformation><ns2:MagicKey>Qcdf28dbc-887c-43d4-9546-0514e6966093</ns2:MagicKey><ns2:InitialTerm>12</ns2:InitialTerm></ns2:CiscoLine></ns2:CiscoExtensions></UserArea></QuoteLine></Quote></DataArea></ShowQuote>";
		showQuoteType = Utils.convertXmlToObject(response, ShowQuoteType.class);
		return showQuoteType;
	}

	public SkuDetailsResponseBean getSkuDetailsResponse() {
		SkuDetailsResponseBean skuDetailsResponseBean = new SkuDetailsResponseBean();
		skuDetailsResponseBean.setDescription("SKU for License");
		skuDetailsResponseBean.setSkuName("TAAP-AU-TDI-APJ Bridge");
		skuDetailsResponseBean.setMrc(BigDecimal.valueOf(22.0));
		return skuDetailsResponseBean;
	}

	public List<GscOutboundPriceBean> getGscOutboundpriceBean() throws TclCommonException {
		List<GscOutboundPriceBean> gscOutboundPriceBeans = new ArrayList<>();
		GscOutboundPriceBean gscOutboundPriceBean = new GscOutboundPriceBean();
		gscOutboundPriceBean.setCdaFloor(0.0);
		gscOutboundPriceBean.setComments("test");
		gscOutboundPriceBean.setCountry("test");
		gscOutboundPriceBean.setCountryCode("test");
		gscOutboundPriceBean.setDestinationName("test");
		gscOutboundPriceBeans.add(gscOutboundPriceBean);
		return gscOutboundPriceBeans;
	}

	public MstProductFamily getMstProductFamilyGVPN() {
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(1);
		mstProductFamily.setName("GVPN");
		mstProductFamily.setMstProductOfferings(getMstOfferings());
		return mstProductFamily;
	}

	public QuoteToLeProductFamily getQuoteToLeProductFamilyGVPN() {
		QuoteToLeProductFamily quoteToLeProductFamily = new QuoteToLeProductFamily();
		quoteToLeProductFamily.setId(1234);
		quoteToLeProductFamily.setMstProductFamily(getMstProductFamilyGVPN());
		quoteToLeProductFamily.setQuoteToLe(getQuoteToLe());
		return quoteToLeProductFamily;
	}

	public List<QuoteToLeProductFamily> getQuoteToLeProductFamilyList() {
		List<QuoteToLeProductFamily> quoteToLeProductFamilies = new ArrayList<>();
		quoteToLeProductFamilies.add(createQuoteToLeProductFamily());
		quoteToLeProductFamilies.add(getQuoteToLeProductFamilyGVPN());
		return quoteToLeProductFamilies;
	}

	public ProductAttributeMaster getProductAttributeMaster() {
		ProductAttributeMaster productAttributeMaster = new ProductAttributeMaster();
		productAttributeMaster.setName(GscConstants.RATE_PER_MINUTE_FIXED);
		productAttributeMaster.setId(1);
		productAttributeMaster.setDescription(GscConstants.RATE_PER_MINUTE_FIXED);
		productAttributeMaster
				.setOrderProductComponentsAttributeValues(Sets.newHashSet(getOrderProductComponentsAttributeValue()));
		return productAttributeMaster;
	}

	public OrderGscTfn getOrderGscTfn() {
		OrderGscTfn orderGscTfn = new OrderGscTfn();
		orderGscTfn.setId(1);
		orderGscTfn.setTfnNumber("123456");
		orderGscTfn.setIsPorted((byte) 1);
		orderGscTfn.setCountryCode("IND:chennai");
		return orderGscTfn;
	}

	public QuoteGscTfn getQuoteGscTfn() {
		QuoteGscTfn quoteGscTfn = new QuoteGscTfn();
		quoteGscTfn.setId(1);
		quoteGscTfn.setTfnNumber("123456");
		quoteGscTfn.setIsPorted((byte) 0);
		quoteGscTfn.setCountryCode("IND");
		return quoteGscTfn;
	}

	public List<QuoteGscTfn> getQuoteGscTfnList() {
		List<QuoteGscTfn> quoteGscTfnList = new ArrayList<>();
		quoteGscTfnList.add(getQuoteGscTfn());
		quoteGscTfnList.add(getQuoteGscTfn());
		return quoteGscTfnList;
	}

	public OrderPrice getOrderPrice() {
		OrderPrice orderPrice = new OrderPrice();
		orderPrice.setQuoteId(1);
		orderPrice.setId(1);
		orderPrice.setCatalogMrc(0.0);
		orderPrice.setCatalogNrc(0.0);
		orderPrice.setComputedMrc(0.0);
		orderPrice.setComputedNrc(0.0);
		orderPrice.setEffectiveArc(0.0);
		orderPrice.setEffectiveMrc(0.0);
		orderPrice.setEffectiveNrc(0.0);
		orderPrice.setEffectiveUsagePrice(0.0);
		return orderPrice;
	}

	public ThirdPartyServiceJob getThirdPartyServiceJob() {
		ThirdPartyServiceJob thirdPartyServiceJob = new ThirdPartyServiceJob();
		thirdPartyServiceJob.setId(123);
		thirdPartyServiceJob.setServiceStatus(INPROGRESS);
		return thirdPartyServiceJob;
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
		orderIllSite.setOrderProductSolution(getOrderProductSolution());
		return orderIllSite;
	}

	public List<OrderIllSite> getOrderIllSiteList() {
		List<OrderIllSite> list = new ArrayList<>();
		list.add(getOrderIllSite());
		list.add(getOrderIllSite());
		return list;
	}

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

	public List<QuoteLeAttributeValue> getQuoteLeAttributesWithDiffMstAttribute() {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = new ArrayList<>();

		QuoteLeAttributeValue quoteLeAttributeValue31 = getQuoteLeAttributeValue();
		quoteLeAttributeValue31.getMstOmsAttribute().setName(LeAttributesConstants.CUSTOMER_CODE);
		quoteLeAttributeValues.add(quoteLeAttributeValue31);

		QuoteLeAttributeValue quoteLeAttributeValue32 = getQuoteLeAttributeValue();
		quoteLeAttributeValue32.getMstOmsAttribute().setName(LeAttributesConstants.CUSTOMER_LE_CODE);
		quoteLeAttributeValues.add(quoteLeAttributeValue32);

		QuoteLeAttributeValue quoteLeAttributeValue = getQuoteLeAttributeValue();

		quoteLeAttributeValue.getMstOmsAttribute().setName(LeAttributesConstants.BILLING_CONTACT_ID);
		quoteLeAttributeValues.add(quoteLeAttributeValue);

		QuoteLeAttributeValue quoteLeAttributeValue2 = getQuoteLeAttributeValue();
		quoteLeAttributeValue2.getMstOmsAttribute().setName(LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY);
		quoteLeAttributeValues.add(quoteLeAttributeValue2);

		QuoteLeAttributeValue quoteLeAttributeValue3 = getQuoteLeAttributeValue();
		quoteLeAttributeValue3.getMstOmsAttribute().setName(LeAttributesConstants.GST_NUMBER);
		quoteLeAttributeValues.add(quoteLeAttributeValue3);

		QuoteLeAttributeValue quoteLeAttributeValue4 = getQuoteLeAttributeValue();
		quoteLeAttributeValue4.getMstOmsAttribute().setName(LeAttributesConstants.VAT_NUMBER);
		quoteLeAttributeValues.add(quoteLeAttributeValue4);

		QuoteLeAttributeValue quoteLeAttributeValue6 = getQuoteLeAttributeValue();
		quoteLeAttributeValue6.getMstOmsAttribute().setName(LeAttributesConstants.SERVICE_SCHEDULE);
		quoteLeAttributeValues.add(quoteLeAttributeValue6);

		QuoteLeAttributeValue quoteLeAttributeValue7 = getQuoteLeAttributeValue();
		quoteLeAttributeValue7.getMstOmsAttribute().setName(LeAttributesConstants.CREDIT_LIMIT);
		quoteLeAttributeValues.add(quoteLeAttributeValue7);

		QuoteLeAttributeValue quoteLeAttributeValue8 = getQuoteLeAttributeValue();
		quoteLeAttributeValue8.getMstOmsAttribute().setName(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY);
		quoteLeAttributeValues.add(quoteLeAttributeValue8);

		QuoteLeAttributeValue quoteLeAttributeValue9 = getQuoteLeAttributeValue();
		quoteLeAttributeValue9.getMstOmsAttribute().setName(LeAttributesConstants.LE_NAME);
		quoteLeAttributeValues.add(quoteLeAttributeValue9);

		QuoteLeAttributeValue quoteLeAttributeValue10 = getQuoteLeAttributeValue();
		quoteLeAttributeValue10.getMstOmsAttribute().setName(LeAttributesConstants.LE_EMAIL);
		quoteLeAttributeValues.add(quoteLeAttributeValue10);

		QuoteLeAttributeValue quoteLeAttributeValue11 = getQuoteLeAttributeValue();
		quoteLeAttributeValue11.getMstOmsAttribute().setName(LeAttributesConstants.BILLING_METHOD);
		quoteLeAttributeValues.add(quoteLeAttributeValue11);

		QuoteLeAttributeValue quoteLeAttributeValue12 = getQuoteLeAttributeValue();
		quoteLeAttributeValue12.getMstOmsAttribute().setName(LeAttributesConstants.BILLING_TYPE);
		quoteLeAttributeValues.add(quoteLeAttributeValue12);

		QuoteLeAttributeValue quoteLeAttributeValue13 = getQuoteLeAttributeValue();
		quoteLeAttributeValue13.getMstOmsAttribute().setName(LeAttributesConstants.BILLING_FREQUENCY);
		quoteLeAttributeValues.add(quoteLeAttributeValue13);

		QuoteLeAttributeValue quoteLeAttributeValue14 = getQuoteLeAttributeValue();
		quoteLeAttributeValue14.getMstOmsAttribute().setName(LeAttributesConstants.PAYMENT_CURRENCY);
		quoteLeAttributeValues.add(quoteLeAttributeValue14);

		QuoteLeAttributeValue quoteLeAttributeValue15 = getQuoteLeAttributeValue();
		quoteLeAttributeValue15.getMstOmsAttribute().setName(LeAttributesConstants.PAYMENT_TERM);
		quoteLeAttributeValues.add(quoteLeAttributeValue15);

		QuoteLeAttributeValue quoteLeAttributeValue16 = getQuoteLeAttributeValue();
		quoteLeAttributeValue16.getMstOmsAttribute().setName(LeAttributesConstants.INVOICE_METHOD);
		quoteLeAttributeValues.add(quoteLeAttributeValue16);

		QuoteLeAttributeValue quoteLeAttributeValue17 = getQuoteLeAttributeValue();
		quoteLeAttributeValue17.getMstOmsAttribute().setName(LeAttributesConstants.TERM_IN_MONTHS);
		quoteLeAttributeValues.add(quoteLeAttributeValue17);

		QuoteLeAttributeValue quoteLeAttributeValue18 = getQuoteLeAttributeValue();
		quoteLeAttributeValue18.getMstOmsAttribute().setName(LeAttributesConstants.TERM_IN_MONTHS);
		quoteLeAttributeValues.add(quoteLeAttributeValue18);

		QuoteLeAttributeValue quoteLeAttributeValue19 = getQuoteLeAttributeValue();
		quoteLeAttributeValue19.getMstOmsAttribute().setName(LeAttributesConstants.BILLING_INCREMENT);
		quoteLeAttributeValues.add(quoteLeAttributeValue19);

		QuoteLeAttributeValue quoteLeAttributeValue20 = getQuoteLeAttributeValue();
		quoteLeAttributeValue20.getMstOmsAttribute().setName(LeAttributesConstants.TIMEZONE);
		quoteLeAttributeValues.add(quoteLeAttributeValue20);

		QuoteLeAttributeValue quoteLeAttributeValue21 = getQuoteLeAttributeValue();
		quoteLeAttributeValue21.getMstOmsAttribute().setName(LeAttributesConstants.APPLICABLE_TIMEZONE);
		quoteLeAttributeValues.add(quoteLeAttributeValue21);

		QuoteLeAttributeValue quoteLeAttributeValue22 = getQuoteLeAttributeValue();
		quoteLeAttributeValue22.getMstOmsAttribute().setName(LeAttributesConstants.PAYMENT_OPTIONS);
		quoteLeAttributeValues.add(quoteLeAttributeValue22);

		QuoteLeAttributeValue quoteLeAttributeValue23 = getQuoteLeAttributeValue();
		quoteLeAttributeValue23.getMstOmsAttribute().setName(LeAttributesConstants.NOTICE_ADDRESS);
		quoteLeAttributeValues.add(quoteLeAttributeValue23);

		QuoteLeAttributeValue quoteLeAttributeValue24 = getQuoteLeAttributeValue();
		quoteLeAttributeValue24.getMstOmsAttribute().setName(LeAttributesConstants.MSA);
		quoteLeAttributeValues.add(quoteLeAttributeValue24);

		QuoteLeAttributeValue quoteLeAttributeValue25 = getQuoteLeAttributeValue();
		quoteLeAttributeValue25.getMstOmsAttribute().setName(LeAttributesConstants.DEPOSIT_AMOUNT);
		quoteLeAttributeValues.add(quoteLeAttributeValue25);

		QuoteLeAttributeValue quoteLeAttributeValue26 = getQuoteLeAttributeValue();
		quoteLeAttributeValue26.getMstOmsAttribute().setName(LeAttributesConstants.PO_NUMBER);
		quoteLeAttributeValues.add(quoteLeAttributeValue26);

		QuoteLeAttributeValue quoteLeAttributeValue27 = getQuoteLeAttributeValue();
		quoteLeAttributeValue27.getMstOmsAttribute().setName(LeAttributesConstants.PO_DATE);
		quoteLeAttributeValues.add(quoteLeAttributeValue27);

		QuoteLeAttributeValue quoteLeAttributeValue28 = getQuoteLeAttributeValue();
		quoteLeAttributeValue28.getMstOmsAttribute().setName(LeAttributesConstants.CONTACT_NAME);
		quoteLeAttributeValues.add(quoteLeAttributeValue28);

		QuoteLeAttributeValue quoteLeAttributeValue29 = getQuoteLeAttributeValue();
		quoteLeAttributeValue29.getMstOmsAttribute().setName(LeAttributesConstants.CONTACT_NO);
		quoteLeAttributeValues.add(quoteLeAttributeValue29);

		QuoteLeAttributeValue quoteLeAttributeValue30 = getQuoteLeAttributeValue();
		quoteLeAttributeValue30.getMstOmsAttribute().setName(LeAttributesConstants.CONTACT_EMAIL);
		quoteLeAttributeValues.add(quoteLeAttributeValue30);

		return quoteLeAttributeValues;
	}

	public List<ProductSolution> createProductSolutionWithGscAndWebex() {
		List<ProductSolution> productSolutionList = new ArrayList<>();
		productSolutionList.add(createProductSolutions());
		productSolutionList.add(createProductSolution());
		return productSolutionList;
	}

	public OrdersLeAttributeValue getOrderLeAttributevalue() {
		OrdersLeAttributeValue ordersLeAttributeValue = new OrdersLeAttributeValue();
		ordersLeAttributeValue.setId(2);
		ordersLeAttributeValue.setOrderToLe(getOrderToLe());
		ordersLeAttributeValue.setAttributeValue("2.0");
		ordersLeAttributeValue.setDisplayValue(GscConstants.RATE_PER_MINUTE_FIXED);
		ordersLeAttributeValue.setMstOmsAttribute(getMstOmsAttribute());
		return ordersLeAttributeValue;
	}

	public List<OrderProductComponent> createOrderProductComponents() {
		List<OrderProductComponent> quoteProductComponents = new ArrayList<>();
		quoteProductComponents.add(getOrderProductComponent());
		quoteProductComponents.add(getOrderProductComponent());
		return quoteProductComponents;
	}

	public MstOrderSiteStage getMstOrderSiteStage() {
		MstOrderSiteStage mstOrderSiteStage = new MstOrderSiteStage();
		mstOrderSiteStage.setId(1);
		mstOrderSiteStage.setName("test");
		mstOrderSiteStage.setCode("testcode");
		mstOrderSiteStage.setIsActive("0");
		mstOrderSiteStage.setOrderIllSites(Sets.newHashSet(getOrderIllSite()));
		return mstOrderSiteStage;
	}

	public MstOrderSiteStatus getMstOrderSiteStatus() {
		MstOrderSiteStatus mstOrderSiteStatus = new MstOrderSiteStatus();
		mstOrderSiteStatus.setId(1);
		mstOrderSiteStatus.setName("test");
		mstOrderSiteStatus.setCode("testcode");
		mstOrderSiteStatus.setIsActive((byte) 0);
		return mstOrderSiteStatus;
	}

	public BillingContact getBillingContact(){
		BillingContact billingContact = new BillingContact();
		billingContact.setBillAccNo("1234");
		billingContact.setCustomerId(1);
		return billingContact;
	}

	public List<CustomerLeContactDetailBean> getCustomerLeContactDetailBeans(){
		List<CustomerLeContactDetailBean> customerLeContactDetailBeans = new ArrayList<>();
		CustomerLeContactDetailBean customerLeContactDetailBean = new CustomerLeContactDetailBean();
		customerLeContactDetailBean.setId(1);
		customerLeContactDetailBean.setName("test");
		customerLeContactDetailBean.setMobilePhone("1234");
		customerLeContactDetailBeans.add(customerLeContactDetailBean);
		return customerLeContactDetailBeans;
	}

	public List<OmsAttachment> getOmsAttachments(){
		List<OmsAttachment> omsAttachments = new ArrayList<>();
		OmsAttachment omsAttachment = new OmsAttachment();
		omsAttachment.setReferenceId(12);
		omsAttachment.setId(1);
		omsAttachment.setOrderToLe(getOrderToLe());
		omsAttachment.setQuoteToLe(getQuoteToLe());
		omsAttachments.add(omsAttachment);
		return omsAttachments;
	}

	public AttachmentBean getAttachmentBean(){
		AttachmentBean attachmentBean = new AttachmentBean();
		attachmentBean.setId(1);
		attachmentBean.setFileName("test");
		attachmentBean.setExpiryWindow(1L);
		return attachmentBean;
	}

	public AddressDetail getAddressDetail(){
		AddressDetail addressDetail = new AddressDetail();
		addressDetail.setCity("Chennai");
		return addressDetail;
	}

	public SPDetails getSPDetails(){
		SPDetails spDetails =  new SPDetails();
		spDetails.setAddress("test");
		spDetails.setEntityName("test");
		spDetails.setGstnDetails("test");
		spDetails.setNoticeAddress("test");
		return spDetails;
	}

	public QuoteDelegation createQuoteDelagation(){
		QuoteDelegation quoteDelegation = new QuoteDelegation();
		quoteDelegation.setStatus("1");
		quoteDelegation.setQuoteToLe(getQuoteToLe());
		quoteDelegation.setAssignTo(1);
		quoteDelegation.setIpAddress("127.0.0.1");
		quoteDelegation.setInitiatedBy(1);
		quoteDelegation.setRemarks("idk");
		return quoteDelegation;
	}

	public List<QuoteDelegation> getQuoteDelagations(){
		List<QuoteDelegation> quoteDelegations =  new ArrayList<>();
		quoteDelegations.add(createQuoteDelagation());
		quoteDelegations.add(createQuoteDelagation());
		return quoteDelegations;
	}

	public StoredObject getStoredObject() {
		AccountMock accountMock = new AccountMock();
		ContainerMock containerMock = new ContainerMock(accountMock, "test_account");
		StoredObjectMock storedObjectMock = new StoredObjectMock(containerMock, "test_container");
		return storedObjectMock;
	}

    public QuoteUcaas getQuoteUcaasWithResponse() {
        QuoteUcaas quoteUcaas = new QuoteUcaas();
        quoteUcaas.setId(1006);
        quoteUcaas.setDealId(42293222);
        quoteUcaas.setQuoteToLe(getQuoteToLe());
        quoteUcaas.setDeal_status(Constants.SUCCESS);
        quoteUcaas.setName("A-FLEX");
        quoteUcaas.setDescription("Collaboration Flex Plan");
        quoteUcaas.setUnitMrc(100.0);
        quoteUcaas.setTcv(0.0);
        quoteUcaas.setUnitNrc(0.0);
        quoteUcaas.setArc(0.0);
        quoteUcaas.setMrc(200.0);
        quoteUcaas.setQuantity(10);
        quoteUcaas.setIsConfig((byte) 0);
        quoteUcaas.setProductSolutionId(getProductSolution().stream().findFirst().get());
        return quoteUcaas;
    }
}
