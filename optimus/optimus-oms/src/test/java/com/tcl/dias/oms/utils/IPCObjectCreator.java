package com.tcl.dias.oms.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.Approver;
import com.tcl.dias.common.beans.CustomerAttributeBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.ApproverListBean;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.QuoteToLeBean;
import com.tcl.dias.oms.beans.SolutionDetail;
import com.tcl.dias.oms.beans.TriggerEmailRequest;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.UserStatusConstants;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.DocusignAudit;
import com.tcl.dias.oms.entity.entities.Engagement;
import com.tcl.dias.oms.entity.entities.MacdDetail;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteCloud;
import com.tcl.dias.oms.entity.entities.QuoteDelegation;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.ipc.beans.pricebean.Cloudvm;
import com.tcl.dias.oms.ipc.beans.pricebean.PricingBean;
import com.tcl.dias.oms.ipc.beans.pricebean.RootStorage;
import com.tcl.dias.oms.ipc.constants.OrderStagingConstants;
import com.tcl.dias.oms.macd.beans.MacdQuoteRequest;
import com.tcl.dias.oms.macd.beans.MacdQuoteResponse;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class IPCObjectCreator {

	public MacdQuoteResponse getMacdQuoteResponse() {
		MacdQuoteResponse response = new MacdQuoteResponse();
		response.setQuoteCategory(MACDConstants.ADD_CLOUDVM_SERVICE);
		response.setQuoteType(MACDConstants.MACD_QUOTE_TYPE);
		response.setQuoteResponse(getQuoteResponse());
		return response;
	}

	public QuoteBean getMacdQuoteResponseObj() {
		QuoteBean quote = new QuoteBean();
		quote.setQuoteId(2);
		quote.setQuoteCode("IPCVE23CV");
		quote.setCreatedBy(0);
		quote.setEffectiveDate(new Date());
		quote.setStatus((byte) 1);
		quote.setTermInMonths(12);
		quote.setCustomerId(1907);
		quote.setCustomerName("dimple");
		quote.setContractTerm(12);
		quote.setLegalEntities((Set<QuoteToLeBean>) getQuoteToLe());
		return quote;
	}

	public QuoteResponse getQuoteResponse() {
		QuoteResponse response = new QuoteResponse();
		response.setQuoteId(2);
		response.setQuoteleId(2);
		return response;
	}

	public MacdQuoteRequest getMacdQuoteRequest() {
		MacdQuoteRequest request = new MacdQuoteRequest();
		request.setRequestType(MACDConstants.ADD_CLOUDVM_SERVICE);
		QuoteDetail quoteDetail = new QuoteDetail();
		quoteDetail.setProductName("IPC");
		List<SolutionDetail> solutionDetailList = new ArrayList<>();
		SolutionDetail solutionDetail = new SolutionDetail();
		solutionDetail.setOfferingName("L.Nickel");
		solutionDetail.setDcLocationId("EP_V2_SG_TCX");
		List<ComponentDetail> compDetailList = new ArrayList<>();
		compDetailList.add(getFlavorComponentDetail());
		compDetailList.add(getOSComponentDetail());
		solutionDetail.setComponents(compDetailList);
		solutionDetailList.add(solutionDetail);
		quoteDetail.setSolutions(solutionDetailList);
		request.setQuoteRequest(quoteDetail);
		/*
		 * request.setDownstreamOrderId("1"); request.setServiceId("000981655989898");
		 * request.setServiceDetailId(1);
		 */
		return request;
	}
	public MacdQuoteRequest getMacdQuoteRequestNegative() {
		MacdQuoteRequest request = new MacdQuoteRequest();
//		request.setRequestType(MACDConstants.ADD_CLOUDVM_SERVICE);
		QuoteDetail quoteDetail = new QuoteDetail();
		quoteDetail.setProductName("IPC");
		List<SolutionDetail> solutionDetailList = new ArrayList<>();
		SolutionDetail solutionDetail = new SolutionDetail();
		solutionDetail.setOfferingName("L.Nickel");
		solutionDetail.setDcLocationId("EP_V2_SG_TCX");
		List<ComponentDetail> compDetailList = new ArrayList<>();
		compDetailList.add(getFlavorComponentDetail());
		compDetailList.add(getOSComponentDetail());
		solutionDetail.setComponents(compDetailList);
		solutionDetailList.add(solutionDetail);
		quoteDetail.setSolutions(solutionDetailList);
		request.setQuoteRequest(quoteDetail);
		return request;
	}

	public MacdQuoteRequest getMacdQuoteRequestNegative1() {
		MacdQuoteRequest request = getMacdQuoteRequest();
		request.setRequestType(MACDConstants.ADD_CLOUDVM_SERVICE);
		return request;
	}

	private ComponentDetail getFlavorComponentDetail() {
		ComponentDetail componentDetail1 = new ComponentDetail();
		componentDetail1.setName("Flavor");
		componentDetail1.setComponentMasterId(1);
		AttributeDetail attributeDetail1 = new AttributeDetail();
		attributeDetail1.setAttributeMasterId(1);
		attributeDetail1.setValue("1");
		attributeDetail1.setName("VCPU");
		AttributeDetail attributeDetail2 = new AttributeDetail();
		attributeDetail2.setAttributeMasterId(2);
		attributeDetail2.setValue("1");
		attributeDetail2.setName("vRAM");
		AttributeDetail attributeDetail3 = new AttributeDetail();
		attributeDetail3.setAttributeMasterId(3);
		attributeDetail3.setValue("50");
		attributeDetail3.setName("Storage");
		AttributeDetail attributeDetail4 = new AttributeDetail();
		attributeDetail4.setAttributeMasterId(4);
		attributeDetail4.setValue("EsXi");
		attributeDetail4.setName("Hypervisor");
		List<AttributeDetail> attrDetailList = new ArrayList<>();
		attrDetailList.add(attributeDetail1);
		attrDetailList.add(attributeDetail2);
		attrDetailList.add(attributeDetail3);
		attrDetailList.add(attributeDetail4);
		componentDetail1.setAttributes(attrDetailList);
		return componentDetail1;
	}

	private ComponentDetail getOSComponentDetail() {
		ComponentDetail componentDetail1 = new ComponentDetail();
		componentDetail1.setName("OS");
		componentDetail1.setComponentMasterId(2);
		AttributeDetail attributeDetail1 = new AttributeDetail();
		attributeDetail1.setAttributeMasterId(12);
		attributeDetail1.setValue("");
		attributeDetail1.setName("Type");
		AttributeDetail attributeDetail2 = new AttributeDetail();
		attributeDetail2.setAttributeMasterId(13);
		attributeDetail2.setValue("");
		attributeDetail2.setName("version");
		List<AttributeDetail> attrDetailList = new ArrayList<>();
		attrDetailList.add(attributeDetail1);
		attrDetailList.add(attributeDetail2);
		componentDetail1.setAttributes(attrDetailList);
		return componentDetail1;
	}

	public Map<String, Object> getUserInfo() {
		UserInformation value = new UserInformation();
		value.setUserId("1");
		value.setCustomers(getCustomerList());
		value.setUserType("sales");
		Map<String, Object> map = new HashMap<>();
		map.put("USER_INFORMATION", value);
		return map;
	}

	public String userType() {
		Map<String, Object> map = getUserInfo();
		UserInformation value = (UserInformation) map.get("USER_INFORMATION");
		return value.getUserType();
	}

	public UserInformation getUserInformation() {
		UserInformation userInformation = new UserInformation();
		userInformation.setUserId("admin");
		userInformation.setCustomers(getCustomerList());
		userInformation.setRole(Arrays.asList("OPTIMUS_MU"));
		userInformation.setUserType("sales");
		return userInformation;
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

	public SIOrderDataBean getSiOrderDataBean() {
		SIOrderDataBean siOrderDataBean = new SIOrderDataBean();
		siOrderDataBean.setId(11);
		siOrderDataBean.setServiceDetails(siServiceDetailBean());
		siOrderDataBean.setErfCustCustomerId("1");
		return siOrderDataBean;
	}

	public List<SIServiceDetailDataBean> siServiceDetailBean() {
		List<SIServiceDetailDataBean> siServiceDetailDataBeanList = new ArrayList<>();
		siServiceDetailDataBeanList.add(createSiServiceDetailBean());
		return siServiceDetailDataBeanList;
	}

	public SIServiceDetailDataBean createSiServiceDetailBean() {
		SIServiceDetailDataBean siServiceDetailDataBean = new SIServiceDetailDataBean();
		siServiceDetailDataBean.setArc(200d);
		siServiceDetailDataBean.setTpsServiceId("091GADC623029807467");
		return siServiceDetailDataBean;
	}

	public User getUser() {
		User user = new User();
		user.setCustomer(getCustomer());
		user.setId(1);
		user.setEmailId("dimples@mail.com");
		user.setEmailId("abc@tata.com");
		user.setFirstName("abc");
		user.setUsername("abc");
		return user;
	}

	public Customer getCustomer() {
		Customer customer = new Customer();
		customer.setCustomerCode("123");
		customer.setStatus((byte) 0);
		customer.setId(1);
		customer.setCustomerEmailId("dimple@gmail.com");
		customer.setErfCusCustomerId(1);
		customer.setCustomerName("Dimple");
		customer.setUsers(getUserEntity());
		Set<Quote> set = new HashSet<>();
		set.add(getQuote());
		customer.setQuotes(set);
		return customer;
	}

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

	public Quote getQuote() {
		Quote quote = new Quote();
		quote.setId(2);
		quote.setQuoteCode("IPCVE23CV");
		quote.setCreatedBy(0);
		quote.setEffectiveDate(new Date());
		quote.setQuoteToLes(getQuotesToLead());
		for (QuoteToLe quoteToLe : quote.getQuoteToLes()) {
			quoteToLe.setQuote(quote);
		}
		quote.setCreatedTime(new Date());
		quote.setStatus((byte) 1);
		quote.setTermInMonths(12);
		Customer customer = new Customer();
		customer.setCustomerCode("123");
		customer.setStatus((byte) 0);
		customer.setId(1);
		customer.setCustomerEmailId("dimples@gmail.com");
		customer.setCustomerName("dimple");
		customer.setUsers(getUserEntity());
		quote.setCustomer(customer);
		quote.setCustomer(getCustomerWithOutQuote());
		return quote;
	}
	public Quote getQuoteWithDocusign() {
		Quote quote = new Quote();
		quote.setId(2);
		quote.setQuoteCode("IPCVE23CV1");
		quote.setCreatedBy(0);
		quote.setEffectiveDate(new Date());
		quote.setQuoteToLes(getQuotesToLead());
		for (QuoteToLe quoteToLe : quote.getQuoteToLes()) {
			quoteToLe.setQuote(quote);
		}
		quote.setStatus((byte) 1);
		quote.setTermInMonths(12);
		Customer customer = new Customer();
		customer.setCustomerCode("123");
		customer.setStatus((byte) 0);
		customer.setId(1);
		customer.setCustomerEmailId("dimples@gmail.com");
		customer.setCustomerName("dimple");
		customer.setUsers(getUserEntity());
		quote.setCustomer(customer);
		quote.setCustomer(getCustomerWithOutQuote());
		return quote;
	}

	public Customer getCustomerWithOutQuote() {
		Customer customer = new Customer();
		customer.setCustomerCode("123");
		customer.setStatus((byte) 0);
		customer.setId(1);
		customer.setCustomerEmailId("dimples@gmail.com");
		customer.setCustomerName("dimple");
		customer.toString();
		customer.setUsers(getUserEntity());
		customer.setErfCusCustomerId(1);
		return customer;
	}

	public Set<QuoteToLe> getQuotesToLead() {
		Set<QuoteToLe> mockQuotesSet = new HashSet<>();
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setCurrencyId(1);
		quoteToLe.setFinalMrc(3.5D);
		quoteToLe.setFinalNrc(34.5D);
		quoteToLe.setProposedMrc(23.6D);
		quoteToLe.setId(2);
		quoteToLe.setFinalNrc(67.4D);
		quoteToLe.setFinalArc(102.1D);
		quoteToLe.setProposedMrc(78.6D);
		quoteToLe.setProposedNrc(98.7D);
		quoteToLe.setQuoteToLeProductFamilies(getQuoteFamiles());
		quoteToLe.setStage("Get Quote");
		quoteToLe.setQuoteType("MACD");
		quoteToLe.setQuoteCategory("CONNECTIVITY_UPGRADE");
		quoteToLe.setSourceSystem("abc");
		quoteToLe.setTpsSfdcParentOptyId(12345);
		mockQuotesSet.add(quoteToLe);
		return mockQuotesSet;
	}

	public Set<QuoteToLeProductFamily> getQuoteFamiles() {
		Set<QuoteToLeProductFamily> mockQoteFamily = new HashSet<>();
		QuoteToLeProductFamily quoTefamily = new QuoteToLeProductFamily();
		quoTefamily.setId(1);
		quoTefamily.setProductSolutions(getProductSolution());
		quoTefamily.setMstProductFamily(getMstProductFamily());
		mockQoteFamily.add(quoTefamily);
		return mockQoteFamily;
	}

	public Set<ProductSolution> getProductSolution() {
		Set<ProductSolution> productSolutions = new HashSet<>();
		ProductSolution productSolution = new ProductSolution();
		productSolution.setId(1);
		productSolution.setProductProfileData(
				"{\"offeringName\":\"C.Silver\",\"dcLocationId\":\"EP_V2_MUM\",\"parentCloudCode\":\"DBDBDWC39BC7PBIO\",\"components\":[{\"componentMasterId\":1,\"name\":\"Flavor\",\"attributes\":[{\"attributeMasterId\":1,\"name\":\"vCPU\",\"value\":\"4\"},{\"attributeMasterId\":2,\"name\":\"vRAM\",\"value\":\"8\"},{\"attributeMasterId\":3,\"name\":\"Storage\",\"value\":\"100\"},{\"attributeMasterId\":4,\"name\":\"Hypervisor\",\"value\":\"ESXi\"}]},{\"componentMasterId\":2,\"name\":\"OS\",\"attributes\":[{\"attributeMasterId\":12,\"name\":\"Type\",\"value\":\"Windows\"},{\"attributeMasterId\":13,\"name\":\"Version\",\"value\":\"Windows 2012\"}]},{\"componentMasterId\":9,\"name\":\"IPC Common\",\"attributes\":[{\"attributeMasterId\":19,\"name\":\"Host Name\",\"value\":\"\"},{\"attributeMasterId\":20,\"name\":\"Zone name\",\"value\":\"default\"},{\"attributeMasterId\":21,\"name\":\"Environment name\",\"value\":\"default\"},{\"attributeMasterId\":22,\"name\":\"Businessunit name\",\"value\":\"default\"},{\"attributeMasterId\":23,\"name\":\"Public IP\",\"value\":\"no\"},{\"attributeMasterId\":30,\"name\":\"Additional Storage Path\",\"value\":\"\"}]},{\"componentMasterId\":17,\"name\":\"Storage Partition\",\"attributes\":[{\"attributeMasterId\":24,\"name\":\"boot\",\"value\":\"1\"},{\"attributeMasterId\":27,\"name\":\"swap\",\"value\":\"4\"},{\"attributeMasterId\":28,\"name\":\"kdump\",\"value\":\"0\"},{\"attributeMasterId\":31,\"name\":\"usr\",\"value\":\"10\"},{\"attributeMasterId\":43,\"name\":\"root\",\"value\":\"30\"}]}]}");
		productSolution.setMstProductOffering(getMstOffering());
		ProductSolution productSolution1 = new ProductSolution();
		productSolution1.setId(2);
		productSolution1.setProductProfileData(
				"{\"offeringName\":\"L.Bronze\",\"dcLocationId\":\"EP_V2_MUM\",\"parentCloudCode\":\"GYJKAIXMU8DSEYVC\",\"components\":[{\"componentMasterId\":37,\"name\":\"Flavor\",\"attributes\":[{\"attributeMasterId\":256,\"name\":\"vCPU\",\"value\":\"2\"},{\"attributeMasterId\":257,\"name\":\"vRAM\",\"value\":\"2\"},{\"attributeMasterId\":258,\"name\":\"Storage\",\"value\":\"100\"},{\"attributeMasterId\":259,\"name\":\"Hypervisor\",\"value\":\"ESXi\"}]},{\"componentMasterId\":38,\"name\":\"OS\",\"attributes\":[{\"attributeMasterId\":260,\"name\":\"Type\",\"value\":\"Windows\"},{\"attributeMasterId\":261,\"name\":\"Version\",\"value\":\"Windows 2012\"}]},{\"componentMasterId\":47,\"name\":\"IPC Common\",\"attributes\":[{\"attributeMasterId\":281,\"name\":\"Host Name\"},{\"attributeMasterId\":282,\"name\":\"Zone name\",\"value\":\"default\"},{\"attributeMasterId\":283,\"name\":\"Environment name\",\"value\":\"default\"},{\"attributeMasterId\":284,\"name\":\"Businessunit name\",\"value\":\"default\"},{\"attributeMasterId\":285,\"name\":\"Public IP\",\"value\":\"no\"},{\"attributeMasterId\":294,\"name\":\"Additional Storage Path\"}]},{\"componentMasterId\":48,\"name\":\"Storage Partition\",\"attributes\":[{\"attributeMasterId\":286,\"name\":\"boot\",\"value\":\"1\"},{\"attributeMasterId\":287,\"name\":\"swap\",\"value\":\"4\"},{\"attributeMasterId\":288,\"name\":\"kdump\",\"value\":\"0\"},{\"attributeMasterId\":297,\"name\":\"usr\",\"value\":\"10\"},{\"attributeMasterId\":299,\"name\":\"root\",\"value\":\"100\"}]}]}");
		productSolution1.setMstProductOffering(getMstOffering());
		productSolutions.add(productSolution);
		productSolutions.add(productSolution1);
		return productSolutions;
	}

	public MstProductOffering getMstOffering() {
		MstProductOffering mstProductOffering = new MstProductOffering();
		mstProductOffering.setId(1);
		mstProductOffering.setErfProductOfferingId(1);
		mstProductOffering.setProductDescription("test");
		mstProductOffering.setProductName("IPCTest");
		
		mstProductOffering.setMstProductFamily(getProductFamily());
		return mstProductOffering;
	}

	public MstProductFamily getProductFamily() {
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(1);
		mstProductFamily.setName("IPC");
		mstProductFamily.setStatus((byte) 0);
		mstProductFamily.setEngagements(getEngagementSet());
		mstProductFamily.setQuoteToLeProductFamilies(getQuoteFamilesWithoutProductSolutions());
		return mstProductFamily;
	}

	public Set<Engagement> getEngagementSet() {
		Set<Engagement> setAttri = new HashSet<>();
		setAttri.add(getEngagement());
		return setAttri;
	}

	public Engagement getEngagement() {
		Engagement engagement = new Engagement();
		engagement.setId(1);
		engagement.setEngagementName("test");
		engagement.setStatus((byte) 1);
		return engagement;
	}

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

	public MstProductFamily getMstProductFamily() {
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(1);
		mstProductFamily.setName("IPC");
		return mstProductFamily;
	}

	public Set<ProductSolution> getProductSolutionwithoutMstOffering() {
		Set<ProductSolution> productSolutions = new HashSet<>();
		ProductSolution productSolution = new ProductSolution();
		productSolution.setId(1);
		
		productSolution.setProductProfileData(
				"{\"offeringName\":\"C.Silver\",\"dcLocationId\":\"EP_V2_MUM\",\"parentCloudCode\":\"DBDBDWC39BC7PBIO\",\"components\":[{\"componentMasterId\":1,\"name\":\"Flavor\",\"attributes\":[{\"attributeMasterId\":1,\"name\":\"vCPU\",\"value\":\"4\"},{\"attributeMasterId\":2,\"name\":\"vRAM\",\"value\":\"8\"},{\"attributeMasterId\":3,\"name\":\"Storage\",\"value\":\"100\"},{\"attributeMasterId\":4,\"name\":\"Hypervisor\",\"value\":\"ESXi\"}]},{\"componentMasterId\":2,\"name\":\"OS\",\"attributes\":[{\"attributeMasterId\":12,\"name\":\"Type\",\"value\":\"Windows\"},{\"attributeMasterId\":13,\"name\":\"Version\",\"value\":\"Windows 2012\"}]},{\"componentMasterId\":9,\"name\":\"IPC Common\",\"attributes\":[{\"attributeMasterId\":19,\"name\":\"Host Name\",\"value\":\"\"},{\"attributeMasterId\":20,\"name\":\"Zone name\",\"value\":\"default\"},{\"attributeMasterId\":21,\"name\":\"Environment name\",\"value\":\"default\"},{\"attributeMasterId\":22,\"name\":\"Businessunit name\",\"value\":\"default\"},{\"attributeMasterId\":23,\"name\":\"Public IP\",\"value\":\"no\"},{\"attributeMasterId\":30,\"name\":\"Additional Storage Path\",\"value\":\"\"}]},{\"componentMasterId\":17,\"name\":\"Storage Partition\",\"attributes\":[{\"attributeMasterId\":24,\"name\":\"boot\",\"value\":\"1\"},{\"attributeMasterId\":27,\"name\":\"swap\",\"value\":\"4\"},{\"attributeMasterId\":28,\"name\":\"kdump\",\"value\":\"0\"},{\"attributeMasterId\":31,\"name\":\"usr\",\"value\":\"10\"},{\"attributeMasterId\":43,\"name\":\"root\",\"value\":\"30\"}]}]}");
		productSolutions.add(productSolution);
		return productSolutions;
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

	public QuoteToLe getQuoteToLe() {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setId(1);
		quoteToLe.setCurrencyId(1);
		quoteToLe.setFinalMrc(45D);
		quoteToLe.setFinalNrc(67D);
		quoteToLe.setProposedMrc(78D);
		quoteToLe.setProposedNrc(98D);
		quoteToLe.setQuote(getQuote());
		quoteToLe.setCurrencyCode("INR");
		quoteToLe.setQuoteCategory("CONNECTIVITY_UPGRADE");
		quoteToLe.setErfCusCustomerLegalEntityId(1);
		quoteToLe.setQuote(getQuote());
		quoteToLe.setErfCusSpLegalEntityId(1);
		quoteToLe.setQuoteLeAttributeValues(getQuoteLeAttributeValueSet());
		quoteToLe.setQuoteToLeProductFamilies(getQuoteFamilesWithoutProductSolutions());
		quoteToLe.setStage("Add Locations");
		quoteToLe.setQuoteToLeProductFamilies(getQuoteToLeProductFamily());
		return quoteToLe;
	}

	public Set<QuoteLeAttributeValue> getQuoteLeAttributeValueSet() {
		Set<QuoteLeAttributeValue> quoteLeAttributeValueSet = new HashSet<>();
		QuoteLeAttributeValue quoteLeAttributeValue = getQuoteLeAttributeValue();
		quoteLeAttributeValue.getMstOmsAttribute().setName("TermInMonths");

		QuoteLeAttributeValue quoteLeAttributeValue1 = getQuoteLeAttributeValue();
		quoteLeAttributeValue1.setAttributeValue("1");
		quoteLeAttributeValue1.getMstOmsAttribute().setName(LeAttributesConstants.CONTACT_ID);

		QuoteLeAttributeValue quoteLeAttributeValue2 = getQuoteLeAttributeValue();
		quoteLeAttributeValue2.setAttributeValue("ipc");
		quoteLeAttributeValue2.getMstOmsAttribute().setName(LeAttributesConstants.CONTACT_NAME);

		QuoteLeAttributeValue quoteLeAttributeValue3 = getQuoteLeAttributeValue();
		quoteLeAttributeValue3.setAttributeValue("1");
		quoteLeAttributeValue3.getMstOmsAttribute().setName(LeAttributesConstants.CONTACT_EMAIL);

		QuoteLeAttributeValue quoteLeAttributeValue4 = getQuoteLeAttributeValue();
		quoteLeAttributeValue4.setAttributeValue("1");
		quoteLeAttributeValue4.getMstOmsAttribute().setName(LeAttributesConstants.CONTACT_NO);

		QuoteLeAttributeValue quoteLeAttributeValue5 = getQuoteLeAttributeValue();
		quoteLeAttributeValue5.setAttributeValue("1");
		quoteLeAttributeValue5.getMstOmsAttribute().setName(LeAttributesConstants.DESIGNATION);
		quoteLeAttributeValueSet.add(quoteLeAttributeValue);
		quoteLeAttributeValueSet.add(quoteLeAttributeValue1);
		quoteLeAttributeValueSet.add(quoteLeAttributeValue2);

		quoteLeAttributeValueSet.add(quoteLeAttributeValue3);

		quoteLeAttributeValueSet.add(quoteLeAttributeValue4);
		quoteLeAttributeValueSet.add(quoteLeAttributeValue5);

		return quoteLeAttributeValueSet;

	}

	public QuoteLeAttributeValue getQuoteLeAttributeValue() {
		QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
		quoteLeAttributeValue.setId(1);
		quoteLeAttributeValue.setAttributeValue("IPC");
		quoteLeAttributeValue.setMstOmsAttribute(getMstOmsAttribute());
		quoteLeAttributeValue.setDisplayValue("display Value");
		return quoteLeAttributeValue;
	}

	public MstOmsAttribute getMstOmsAttribute() {
		MstOmsAttribute mstOmsAttribute = new MstOmsAttribute();
		mstOmsAttribute.setId(1);
		mstOmsAttribute.setIsActive((byte) 1);
		mstOmsAttribute.setCreatedBy("DimpleS");
		mstOmsAttribute.setCreatedTime(new Date());
		mstOmsAttribute.setDescription("Description");
		mstOmsAttribute.setName("Test");

		return mstOmsAttribute;
	}

	public Set<QuoteToLeProductFamily> getQuoteToLeProductFamily() {
		Set<QuoteToLeProductFamily> quoteToLeProductFamilySet = new HashSet<>();
		QuoteToLeProductFamily qFamily = new QuoteToLeProductFamily();
		qFamily.setId(1);
		MstProductFamily mstProductFamily = new MstProductFamily();
		mstProductFamily.setId(1);
		mstProductFamily.setName("IPC");
		mstProductFamily.setStatus((byte) 2);
		qFamily.setMstProductFamily(mstProductFamily);
		qFamily.setProductSolutions(getProductSolution());
		quoteToLeProductFamilySet.add(qFamily);
		return quoteToLeProductFamilySet;
	}
	public List<QuoteToLeProductFamily> getQuoteToLeProductFamilyList() {
		List<QuoteToLeProductFamily> quoteToLeProductFamilySet = new ArrayList<>();
		QuoteToLeProductFamily qFamily = new QuoteToLeProductFamily();
		qFamily.setId(1);
		qFamily.setMstProductFamily(getMstProductFamily());
		qFamily.setProductSolutions(getProductSolution());
		quoteToLeProductFamilySet.add(qFamily);
		return quoteToLeProductFamilySet;
	}

	public QuoteToLeProductFamily getQuoteToLeFamily() {
		QuoteToLeProductFamily quoTefamily = new QuoteToLeProductFamily();
		quoTefamily.setId(1);
		quoTefamily.setId(1);
		quoTefamily.setProductSolutions(getProductSolution());
		quoTefamily.setMstProductFamily(getMstProductFamily());
		quoTefamily.setQuoteToLe(getQuoteToLe());
		return quoTefamily;
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
		p.setProductProfileData(
				"{\"offeringName\":\"C.Silver\",\"dcLocationId\":\"EP_V2_MUM\",\"parentCloudCode\":\"DBDBDWC39BC7PBIO\",\"components\":[{\"componentMasterId\":1,\"name\":\"Flavor\",\"attributes\":[{\"attributeMasterId\":1,\"name\":\"vCPU\",\"value\":\"4\"},{\"attributeMasterId\":2,\"name\":\"vRAM\",\"value\":\"8\"},{\"attributeMasterId\":3,\"name\":\"Storage\",\"value\":\"100\"},{\"attributeMasterId\":4,\"name\":\"Hypervisor\",\"value\":\"ESXi\"}]},{\"componentMasterId\":2,\"name\":\"OS\",\"attributes\":[{\"attributeMasterId\":12,\"name\":\"Type\",\"value\":\"Windows\"},{\"attributeMasterId\":13,\"name\":\"Version\",\"value\":\"Windows 2012\"}]},{\"componentMasterId\":9,\"name\":\"IPC Common\",\"attributes\":[{\"attributeMasterId\":19,\"name\":\"Host Name\",\"value\":\"\"},{\"attributeMasterId\":20,\"name\":\"Zone name\",\"value\":\"default\"},{\"attributeMasterId\":21,\"name\":\"Environment name\",\"value\":\"default\"},{\"attributeMasterId\":22,\"name\":\"Businessunit name\",\"value\":\"default\"},{\"attributeMasterId\":23,\"name\":\"Public IP\",\"value\":\"no\"},{\"attributeMasterId\":30,\"name\":\"Additional Storage Path\",\"value\":\"\"}]},{\"componentMasterId\":17,\"name\":\"Storage Partition\",\"attributes\":[{\"attributeMasterId\":24,\"name\":\"boot\",\"value\":\"1\"},{\"attributeMasterId\":27,\"name\":\"swap\",\"value\":\"4\"},{\"attributeMasterId\":28,\"name\":\"kdump\",\"value\":\"0\"},{\"attributeMasterId\":31,\"name\":\"usr\",\"value\":\"10\"},{\"attributeMasterId\":43,\"name\":\"root\",\"value\":\"30\"}]}]}");
		return p;
	}

	public List<QuoteToLe> getQuoteToLeList() {
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setId(1);
		quoteToLe.setCurrencyId(1);
		quoteToLe.setFinalMrc(5d);
		quoteToLe.setFinalNrc(67d);
		quoteToLe.setProposedMrc(78d);
		quoteToLe.setProposedNrc(98d);
		quoteToLe.setFinalMrc(5d);
		quoteToLe.setFinalNrc(67d);
		quoteToLe.setProposedMrc(78d);
		quoteToLe.setQuoteToLeProductFamilies(getQuoteToLeProductFamily());
		quoteToLe.setQuote(getQuote());
		List<QuoteToLe> quoteToLeList = new ArrayList<>();
		quoteToLeList.add(quoteToLe);
		return quoteToLeList;
	}

	public String getPricingBean() throws TclCommonException {
		PricingBean pricingBean = new PricingBean();
		List<com.tcl.dias.oms.ipc.beans.pricebean.Quote> quoteList = new ArrayList<>();
		quoteList.add(getPricingQuote());
		pricingBean.setQuotes(quoteList);
		return Utils.convertObjectToJson(pricingBean);
	}

	private com.tcl.dias.oms.ipc.beans.pricebean.Quote getPricingQuote() {
		com.tcl.dias.oms.ipc.beans.pricebean.Quote quote = new com.tcl.dias.oms.ipc.beans.pricebean.Quote();
		quote.setQuoteId("2");
		quote.setTerm(12);
		List<Cloudvm> clouds = new ArrayList<Cloudvm>();
		Cloudvm cloud1 = new Cloudvm();
		cloud1.setItemId("11858");
		cloud1.setType("new");
		cloud1.setRegion("EP_V2_SG_TCX");
		cloud1.setPerGBAdditionalIOPSForSSD("50");
		cloud1.setVariant("L.Nickel");
		cloud1.setVcpu("1");
		cloud1.setVram("1");
		RootStorage rootStorage = new RootStorage();
		rootStorage.setSize("50");
		cloud1.setRootStorage(rootStorage);
		cloud1.setHypervisor("ESXI");
		cloud1.setMrc(73.58);
		Cloudvm cloud2 = new Cloudvm();
		cloud2.setItemId("11859");
		cloud2.setType("new");
		cloud2.setRegion("EP_V2_SG_TCX");
		cloud2.setPerGBAdditionalIOPSForSSD("50");
		cloud2.setVariant("L.Bronze");
		cloud2.setVcpu("2");
		cloud2.setVram("2");
		rootStorage.setSize("50");
		cloud2.setRootStorage(rootStorage);
		cloud2.setHypervisor("ESXI");
		cloud2.setMrc(81.86);
		clouds.add(cloud1);
		clouds.add(cloud2);
		quote.setCloudvm(clouds);
		quote.setManagementEnabled(true);
		quote.setCrossBorderWhTaxPercentage(0.0);
		return quote;
	}

	public List<QuoteCloud> getQuoteCloud() {
		QuoteCloud quoteCloud1 = new QuoteCloud();
		quoteCloud1.setResourceDisplayName("FLAVOR");
		quoteCloud1.setQuoteId(1);
		quoteCloud1.setId(1);
		quoteCloud1.setQuoteToLeId(1);
		QuoteCloud quoteCloud2 = new QuoteCloud();
		quoteCloud2.setResourceDisplayName("Access");
		quoteCloud2.setId(2);
		quoteCloud2.setQuoteId(1);
		quoteCloud2.setQuoteToLeId(1);
		QuoteCloud quoteCloud3 = new QuoteCloud();
		quoteCloud3.setResourceDisplayName("IPC addon");
		quoteCloud3.setId(3);
		quoteCloud2.setQuoteId(1);
		quoteCloud2.setQuoteToLeId(1);
		List<QuoteCloud> qCloudList = new ArrayList<>();
		qCloudList.add(quoteCloud1);
		qCloudList.add(quoteCloud2);
		qCloudList.add(quoteCloud3);
		return qCloudList;
	}

	public List<QuoteCloud> getQuoteCloudMacd() {
		QuoteCloud quoteCloud2 = new QuoteCloud();
		quoteCloud2.setResourceDisplayName("Access");
		quoteCloud2.setId(2);
		quoteCloud2.setQuoteId(1);
		quoteCloud2.setQuoteToLeId(1);
		quoteCloud2.setDcLocationId("EP_V2_MUM");
		quoteCloud2.setMrc(10.00);
		quoteCloud2.setNrc(0.00);
		quoteCloud2.setArc(100.05);

		QuoteCloud quoteCloud3 = new QuoteCloud();
		quoteCloud3.setResourceDisplayName("IPC addon");
quoteCloud3.setMrc(10.00);
quoteCloud3.setNrc(0.00);
quoteCloud3.setArc(100.05);
		quoteCloud3.setId(3);
		quoteCloud3.setQuoteId(1);
		quoteCloud3.setQuoteToLeId(1);
		quoteCloud3.setDcLocationId("EP_V2_MUM");
		List<QuoteCloud> qCloudList = new ArrayList<>();

		qCloudList.add(quoteCloud2);
		qCloudList.add(quoteCloud3);
		return qCloudList;
	}

	public List<QuoteCloud> getQuoteCloudMacdAdd() {

		QuoteCloud quoteCloud3 = new QuoteCloud();
		quoteCloud3.setResourceDisplayName("IPC addon");
		quoteCloud3.setId(2);
		List<QuoteCloud> qCloudList = new ArrayList<>();
		qCloudList.add(quoteCloud3);
		return qCloudList;
	}

	public List<QuoteProductComponent> getQuoteProductComponents() {
		List<QuoteProductComponent> qpcList = new ArrayList<>();
		qpcList.addAll(getVMQuoteProductComponents());
		qpcList.addAll(getAccessQuoteProductComponents());
		qpcList.addAll(getAddOnQuoteProductComponents());
		return qpcList;
	}

	public List<QuoteProductComponent> getQuoteProductComponentsConnectivityUpgrade() {
		List<QuoteProductComponent> qpcList = new ArrayList<>();
		qpcList.addAll(getAccessQuoteProductComponents());
		qpcList.addAll(getAddOnQuoteProductComponents());
		return qpcList;
	}

	public Set<QuoteProductComponent> getQuoteProductComponentsSet() {
		Set<QuoteProductComponent> qpcList = new HashSet<>();
		qpcList.addAll(getVMQuoteProductComponents());
		qpcList.addAll(getAccessQuoteProductComponents());
		qpcList.addAll(getAddOnQuoteProductComponents());
		return qpcList;
	}

	public List<QuoteProductComponent> getVMQuoteProductComponents() {
		List<QuoteProductComponent> qpcList = new ArrayList<>();
		QuoteProductComponent quoteProductComponent1 = new QuoteProductComponent();
		MstProductComponent mstProductComponent1 = new MstProductComponent();
		quoteProductComponent1.setId(1);
		mstProductComponent1.setName("FLAVOR");
		quoteProductComponent1.setQuoteProductComponentsAttributeValues(getQuoteProductComponentsAttributeValueSet());
		quoteProductComponent1.setMstProductComponent(mstProductComponent1);
		QuoteProductComponent quoteProductComponent2 = new QuoteProductComponent();
		MstProductComponent mstProductComponent2 = new MstProductComponent();
		mstProductComponent2.setName("OS");
		quoteProductComponent2.setQuoteProductComponentsAttributeValues(getQuoteProductComponentsAttributeValueSet());
		quoteProductComponent2.setMstProductComponent(mstProductComponent2);
		quoteProductComponent2.setId(2);
		QuoteProductComponent quoteProductComponent3 = new QuoteProductComponent();
		MstProductComponent mstProductComponent3 = new MstProductComponent();
		mstProductComponent3.setName("ADDITIONAL STORAGE");
		quoteProductComponent3.setQuoteProductComponentsAttributeValues(getQuoteProductComponentsAttributeValueSet());
		quoteProductComponent3.setId(3);
		quoteProductComponent3.setMstProductComponent(mstProductComponent3);
		qpcList.add(quoteProductComponent1);
		qpcList.add(quoteProductComponent2);
		qpcList.add(quoteProductComponent3);
		return qpcList;
	}

	public List<QuoteProductComponent> getAccessQuoteProductComponents() {
		List<QuoteProductComponent> qpcList = new ArrayList<>();
		QuoteProductComponent quoteProductComponent1 = new QuoteProductComponent();
		MstProductComponent mstProductComponent1 = new MstProductComponent();
		mstProductComponent1.setName("ACCESS TYPE");
		quoteProductComponent1.setMstProductComponent(mstProductComponent1);
		quoteProductComponent1.setId(8);
		quoteProductComponent1.setQuoteProductComponentsAttributeValues(getQuoteProductComponentsAttributeValueSet());
		qpcList.add(quoteProductComponent1);
		return qpcList;
	}

	public MstProductComponent getMstProductComponent() {
		MstProductComponent mstProductComponent1 = new MstProductComponent();
		mstProductComponent1.setName("ACCESS TYPE");
		mstProductComponent1.setId(1);
		mstProductComponent1.setQuoteProductComponents(getQuoteProductComponentsSet());
		return mstProductComponent1;
	}

	public List<QuoteProductComponent> getAddOnQuoteProductComponents() {
		List<QuoteProductComponent> qpcList = new ArrayList<>();
		QuoteProductComponent quoteProductComponent1 = new QuoteProductComponent();
		MstProductComponent mstProductComponent1 = new MstProductComponent();
		mstProductComponent1.setName("VDOM");
		quoteProductComponent1.setId(4);
		quoteProductComponent1.setQuoteProductComponentsAttributeValues(getQuoteProductComponentsAttributeValueSet());
		quoteProductComponent1.setMstProductComponent(mstProductComponent1);
		QuoteProductComponent quoteProductComponent2 = new QuoteProductComponent();
		MstProductComponent mstProductComponent2 = new MstProductComponent();
		mstProductComponent2.setName("Additional Ip");
		quoteProductComponent2.setQuoteProductComponentsAttributeValues(getQuoteProductComponentsAttributeValueSet());
		quoteProductComponent2.setMstProductComponent(mstProductComponent2);
		quoteProductComponent2.setId(5);
		QuoteProductComponent quoteProductComponent3 = new QuoteProductComponent();
		MstProductComponent mstProductComponent3 = new MstProductComponent();
		mstProductComponent3.setName("Backup");
		quoteProductComponent3.setId(6);
		quoteProductComponent3.setQuoteProductComponentsAttributeValues(getQuoteProductComponentsAttributeValueSet());
		quoteProductComponent3.setMstProductComponent(mstProductComponent3);
		QuoteProductComponent quoteProductComponent4 = new QuoteProductComponent();
		MstProductComponent mstProductComponent4 = new MstProductComponent();
		mstProductComponent4.setName("managed");
		quoteProductComponent4.setId(7);
		quoteProductComponent4.setQuoteProductComponentsAttributeValues(getQuoteProductComponentsAttributeValueSet());
		quoteProductComponent4.setMstProductComponent(mstProductComponent4);
		qpcList.add(quoteProductComponent1);
		qpcList.add(quoteProductComponent2);
		qpcList.add(quoteProductComponent3);
		qpcList.add(quoteProductComponent4);
		return qpcList;
	}

	public List<QuoteProductComponentsAttributeValue> getQuoteProductComponentsAttributeValue() {
		List<QuoteProductComponentsAttributeValue> qpcavList = new ArrayList<>();
		qpcavList.addAll(getFlavorQuoteProductComponentsAttributeValue());
		qpcavList.addAll(getOSQuoteProductComponentsAttributeValue());
		qpcavList.addAll(getAccessQuoteProductComponentsAttributeValue());
		qpcavList.addAll(getAddOnQuoteProductComponentsAttributeValue());
		return qpcavList;
	}

	public List<QuoteProductComponentsAttributeValue> getOSQuoteProductComponentsAttributeValue() {
		List<QuoteProductComponentsAttributeValue> qpcavList = new ArrayList<>();
		QuoteProductComponentsAttributeValue qpcav1 = new QuoteProductComponentsAttributeValue();
		ProductAttributeMaster pam1 = new ProductAttributeMaster();
		pam1.setName("TYPE");
		qpcav1.setProductAttributeMaster(pam1);
		qpcav1.setAttributeValues("Windows");
		return qpcavList;
	}

	public List<QuoteProductComponentsAttributeValue> getFlavorQuoteProductComponentsAttributeValue() {
		List<QuoteProductComponentsAttributeValue> qpcavList = new ArrayList<>();
		QuoteProductComponentsAttributeValue qpcav1 = new QuoteProductComponentsAttributeValue();
		ProductAttributeMaster pam1 = new ProductAttributeMaster();
		pam1.setName("VCPU");
		qpcav1.setProductAttributeMaster(pam1);
		qpcav1.setAttributeValues("1");
		QuoteProductComponentsAttributeValue qpcav2 = new QuoteProductComponentsAttributeValue();
		ProductAttributeMaster pam2 = new ProductAttributeMaster();
		pam2.setName("VRAM");
		qpcav2.setProductAttributeMaster(pam2);
		qpcav2.setAttributeValues("1");
		QuoteProductComponentsAttributeValue qpcav3 = new QuoteProductComponentsAttributeValue();
		ProductAttributeMaster pam3 = new ProductAttributeMaster();
		pam3.setName("Storage");
		qpcav3.setProductAttributeMaster(pam3);
		qpcav3.setAttributeValues("50");
		QuoteProductComponentsAttributeValue qpcav4 = new QuoteProductComponentsAttributeValue();
		ProductAttributeMaster pam4 = new ProductAttributeMaster();
		pam4.setName("Hypervisor");
		qpcav4.setProductAttributeMaster(pam4);
		qpcav4.setAttributeValues("EsXi");
		qpcavList.add(qpcav1);
		qpcavList.add(qpcav2);
		qpcavList.add(qpcav3);
		qpcavList.add(qpcav4);
		return qpcavList;
	}

	public List<QuoteProductComponentsAttributeValue> getAccessQuoteProductComponentsAttributeValue() {
		List<QuoteProductComponentsAttributeValue> qpcavList = new ArrayList<>();
		QuoteProductComponentsAttributeValue qpcav1 = new QuoteProductComponentsAttributeValue();
		ProductAttributeMaster pam1 = new ProductAttributeMaster();
		pam1.setName("accessOption");
		qpcav1.setProductAttributeMaster(pam1);
		qpcav1.setAttributeValues("16");
		QuoteProductComponentsAttributeValue qpcav2 = new QuoteProductComponentsAttributeValue();
		ProductAttributeMaster pam2 = new ProductAttributeMaster();
		pam2.setName("MINIMUMCOMMITMENT");
		qpcav2.setProductAttributeMaster(pam2);
		qpcav2.setAttributeValues("590");
		QuoteProductComponentsAttributeValue qpcav3 = new QuoteProductComponentsAttributeValue();
		ProductAttributeMaster pam3 = new ProductAttributeMaster();
		pam3.setName("PORTBANDWIDTH");
		qpcav3.setProductAttributeMaster(pam3);
		qpcav3.setAttributeValues("6");
		qpcavList.add(qpcav1);
		qpcavList.add(qpcav2);
		qpcavList.add(qpcav3);
		return qpcavList;
	}

	public List<QuoteProductComponentsAttributeValue> getAddOnQuoteProductComponentsAttributeValue() {
		List<QuoteProductComponentsAttributeValue> qpcavList = new ArrayList<>();
		QuoteProductComponentsAttributeValue qpcav1 = new QuoteProductComponentsAttributeValue();
		ProductAttributeMaster pam1 = new ProductAttributeMaster();
		pam1.setName("IPQUANTITY");
		qpcav1.setProductAttributeMaster(pam1);
		qpcav1.setAttributeValues("1");
		QuoteProductComponentsAttributeValue qpcav2 = new QuoteProductComponentsAttributeValue();
		ProductAttributeMaster pam2 = new ProductAttributeMaster();
		pam2.setName("FRONTVOLUMESIZE");
		qpcav2.setProductAttributeMaster(pam2);
		qpcav2.setAttributeValues("590");
		QuoteProductComponentsAttributeValue qpcav3 = new QuoteProductComponentsAttributeValue();
		ProductAttributeMaster pam3 = new ProductAttributeMaster();
		pam3.setName("MANAGED");
		qpcav3.setProductAttributeMaster(pam3);
		qpcav3.setAttributeValues("true");
		QuoteProductComponentsAttributeValue qpcav4 = new QuoteProductComponentsAttributeValue();
		ProductAttributeMaster pam4 = new ProductAttributeMaster();
		pam4.setName("TARGETDATASTORAGE");
		qpcav4.setProductAttributeMaster(pam4);
		qpcav4.setAttributeValues("16");
		qpcavList.add(qpcav1);
		qpcavList.add(qpcav2);
		qpcavList.add(qpcav3);
		qpcavList.add(qpcav4);
		return qpcavList;
	}

	public List<ComponentDetail> getComponentList() {
		List<ComponentDetail> components = new ArrayList<>();
		components.add(getFlavorComponentDetail());
		components.add(getOSComponentDetail());
		return components;
	}

	public com.tcl.dias.oms.ipc.beans.QuoteCloud getQuoteCloudAddRequest() {
		com.tcl.dias.oms.ipc.beans.QuoteCloud request = new com.tcl.dias.oms.ipc.beans.QuoteCloud();
		request.setProductName("IPC");
		List<SolutionDetail> solutionDetailList = new ArrayList<>();
		SolutionDetail solution = new SolutionDetail();
		solution.setOfferingName("L.Nickel");
		solution.setDcLocationId("EP_V2_MUM");
		solution.setComponents(this.getComponentList());
		solution.setParentCloudCode("EDRT43");
		solutionDetailList.add(solution);
		request.setSolutions(solutionDetailList);
		return request;

	}

	public Optional<ProductSolution> getOptionalProductSolution() {
		ProductSolution productSolution = new ProductSolution();
		productSolution.setId(1);
		productSolution.setProductProfileData(
				"{\"offeringName\":\"C.Silver\",\"dcLocationId\":\"EP_V2_MUM\",\"parentCloudCode\":\"DBDBDWC39BC7PBIO\",\"components\":[{\"componentMasterId\":1,\"name\":\"Flavor\",\"attributes\":[{\"attributeMasterId\":1,\"name\":\"vCPU\",\"value\":\"4\"},{\"attributeMasterId\":2,\"name\":\"vRAM\",\"value\":\"8\"},{\"attributeMasterId\":3,\"name\":\"Storage\",\"value\":\"100\"},{\"attributeMasterId\":4,\"name\":\"Hypervisor\",\"value\":\"ESXi\"}]},{\"componentMasterId\":2,\"name\":\"OS\",\"attributes\":[{\"attributeMasterId\":12,\"name\":\"Type\",\"value\":\"Windows\"},{\"attributeMasterId\":13,\"name\":\"Version\",\"value\":\"Windows 2012\"}]},{\"componentMasterId\":9,\"name\":\"IPC Common\",\"attributes\":[{\"attributeMasterId\":19,\"name\":\"Host Name\",\"value\":\"\"},{\"attributeMasterId\":20,\"name\":\"Zone name\",\"value\":\"default\"},{\"attributeMasterId\":21,\"name\":\"Environment name\",\"value\":\"default\"},{\"attributeMasterId\":22,\"name\":\"Businessunit name\",\"value\":\"default\"},{\"attributeMasterId\":23,\"name\":\"Public IP\",\"value\":\"no\"},{\"attributeMasterId\":30,\"name\":\"Additional Storage Path\",\"value\":\"\"}]},{\"componentMasterId\":17,\"name\":\"Storage Partition\",\"attributes\":[{\"attributeMasterId\":24,\"name\":\"boot\",\"value\":\"1\"},{\"attributeMasterId\":27,\"name\":\"swap\",\"value\":\"4\"},{\"attributeMasterId\":28,\"name\":\"kdump\",\"value\":\"0\"},{\"attributeMasterId\":31,\"name\":\"usr\",\"value\":\"10\"},{\"attributeMasterId\":43,\"name\":\"root\",\"value\":\"30\"}]}]}");
		productSolution.setMstProductOffering(getMstOffering());
		return Optional.of(productSolution);
	}

	public Optional<QuoteCloud> getOptionalQuoteCloud() {
		QuoteCloud quoteCloud = new QuoteCloud();
		quoteCloud = getQuoteCloud().get(0);
		return Optional.of(quoteCloud);
	}

	public com.tcl.dias.oms.ipc.beans.SolutionDetail getSolutionDetail() {
		com.tcl.dias.oms.ipc.beans.SolutionDetail solutionDetail = new com.tcl.dias.oms.ipc.beans.SolutionDetail();
		solutionDetail.setOfferingName("L.Nickel");
		solutionDetail.setDcLocationId("EP_V2_SG_TCX");
		List<ComponentDetail> compDetailList = new ArrayList<>();
		compDetailList.add(getFlavorComponentDetail());
		compDetailList.add(getOSComponentDetail());
		solutionDetail.setComponents(compDetailList);
		return solutionDetail;
	}

	public List<String> getProductNames() {
		List<String> productNames = new ArrayList<String>();
//		productNames.add("L.Nickel");
		productNames.add("L.Bronze");
		productNames.add("C.Bronze");
		return productNames;
	}

	public List<QuoteProductComponent> getProductComponent() {
		List<QuoteProductComponent> quoteProducts = new ArrayList<QuoteProductComponent>();
		QuoteProductComponent product = new QuoteProductComponent();
		product.setId(1);
		product.setMstProductFamily(getMstProductFamily());
		product.setQuoteProductComponentsAttributeValues(getQuoteProductComponentsAttributeValueSet());
		product.setReferenceId(1);
		product.setReferenceName("IPC");
		product.setType("MACD");
		quoteProducts.add(product);
		return quoteProducts;
	}

	public Set<QuoteProductComponentsAttributeValue> getQuoteProductComponentsAttributeValueSet() {
		Set<QuoteProductComponentsAttributeValue> qpcavList = new HashSet();
		qpcavList.addAll(getFlavorQuoteProductComponentsAttributeValue());
		qpcavList.addAll(getOSQuoteProductComponentsAttributeValue());
		qpcavList.addAll(getAccessQuoteProductComponentsAttributeValue());
		qpcavList.addAll(getAddOnQuoteProductComponentsAttributeValue());
		return qpcavList;
	}

	public UpdateRequest getUpdateRequest() {
		UpdateRequest request = new UpdateRequest();
		request.setTermInMonths("6");
		request.setQuoteId(1);
		request.setQuoteToLe(1);
		request.setAttributeDetails(getAttributeList());

		return request;
	}

	public UpdateRequest getUpdateRequestAttr() {
		UpdateRequest request = new UpdateRequest();
		request.setTermInMonths("6");
		request.setQuoteId(1);
		request.setQuoteToLe(1);
		request.setAttributeDetails(getAttributeList());
		request.setIsTaxExempted(null);
		return request;
	}

	public List<AttributeDetail> getAttributeList() {
		AttributeDetail attributeDetail1 = new AttributeDetail();
		attributeDetail1.setAttributeMasterId(12);
		attributeDetail1.setValue("");
		attributeDetail1.setName("Type");
		AttributeDetail attributeDetail2 = new AttributeDetail();
		attributeDetail2.setAttributeMasterId(13);
		attributeDetail2.setValue("");
		attributeDetail2.setName("version");
		List<AttributeDetail> attrDetailList = new ArrayList<>();
		attrDetailList.add(attributeDetail1);
		attrDetailList.add(attributeDetail2);
		return attrDetailList;
	}

	public TriggerEmailRequest getEmailRequest() {
		TriggerEmailRequest request = new TriggerEmailRequest();
		request.setEmailId("abcd@tatacommunications.com");
		request.setQuoteToLeId(1);
		return request;
	}

	public Optional<QuoteDelegation> getDelegateRequest() {
		QuoteDelegation delegateReq = new QuoteDelegation();
		delegateReq.setId(1);
		delegateReq.setAssignTo(getUser().getId());
		delegateReq.setInitiatedBy(getUser().getCustomer().getId());
		delegateReq.setParentId(0);
		delegateReq.setStatus(UserStatusConstants.OPEN.toString());
		delegateReq.setType(UserStatusConstants.OTHERS.toString());
		delegateReq.setRemarks("");
		delegateReq.setIpAddress("1");
		delegateReq.setIsActive((byte) 1);
		delegateReq.setQuoteToLe(getQuoteToLe());
		return Optional.of(delegateReq);
	}

	public List<QuoteDelegation> getDelegateRequestList() {
		QuoteDelegation delegateReq = new QuoteDelegation();
		delegateReq.setId(1);
		delegateReq.setAssignTo(getUser().getId());
		delegateReq.setInitiatedBy(getUser().getCustomer().getId());
		delegateReq.setParentId(0);
		delegateReq.setStatus(UserStatusConstants.OPEN.toString());
		delegateReq.setType(UserStatusConstants.OTHERS.toString());
		delegateReq.setRemarks("");
		delegateReq.setIpAddress("1");
		delegateReq.setIsActive((byte) 1);
		delegateReq.setQuoteToLe(getQuoteToLe());
		List<QuoteDelegation> delegateList=new ArrayList<QuoteDelegation>();
		delegateList.add(delegateReq);
		return delegateList;
	}

	public List<QuoteLeAttributeValue> getQuoteLeAttributeValueList() {
		List<QuoteLeAttributeValue> quoteLeAttributeValueSet = new ArrayList<>();
		QuoteLeAttributeValue quoteLeAttributeValue = getQuoteLeAttributeValue();
		quoteLeAttributeValue.getMstOmsAttribute().setName("TermInMonths");

		QuoteLeAttributeValue quoteLeAttributeValue1 = getQuoteLeAttributeValue();
		quoteLeAttributeValue1.setAttributeValue("1");
		quoteLeAttributeValue1.getMstOmsAttribute().setName(LeAttributesConstants.CONTACT_ID);

		QuoteLeAttributeValue quoteLeAttributeValue2 = getQuoteLeAttributeValue();
		quoteLeAttributeValue2.setAttributeValue("ipc");
		quoteLeAttributeValue2.getMstOmsAttribute().setName(LeAttributesConstants.CONTACT_NAME);

		QuoteLeAttributeValue quoteLeAttributeValue3 = getQuoteLeAttributeValue();
		quoteLeAttributeValue3.setAttributeValue("1");
		quoteLeAttributeValue3.getMstOmsAttribute().setName(LeAttributesConstants.CONTACT_EMAIL);

		QuoteLeAttributeValue quoteLeAttributeValue4 = getQuoteLeAttributeValue();
		quoteLeAttributeValue4.setAttributeValue("1");
		quoteLeAttributeValue4.getMstOmsAttribute().setName(LeAttributesConstants.CONTACT_NO);

		QuoteLeAttributeValue quoteLeAttributeValue5 = getQuoteLeAttributeValue();
		quoteLeAttributeValue5.setAttributeValue("1");
		quoteLeAttributeValue5.getMstOmsAttribute().setName(LeAttributesConstants.DESIGNATION);
		quoteLeAttributeValueSet.add(quoteLeAttributeValue);
		quoteLeAttributeValueSet.add(quoteLeAttributeValue1);
		quoteLeAttributeValueSet.add(quoteLeAttributeValue2);

		quoteLeAttributeValueSet.add(quoteLeAttributeValue3);

		quoteLeAttributeValueSet.add(quoteLeAttributeValue4);
		quoteLeAttributeValueSet.add(quoteLeAttributeValue5);

		return quoteLeAttributeValueSet;

	}

	public ApproverListBean getApproversList() {
		ApproverListBean request = new ApproverListBean();
		Approver approvers = new Approver();
		approvers.setEmail("abc@tatacommunications.com");
		approvers.setName("Test");
		List<Approver> approverList = new ArrayList<Approver>();
		approverList.add(approvers);
		request.setApprovers(approverList);
		return request;
	}

	public DocusignAudit getDocusignAudit() {
		DocusignAudit docusignAudit = new DocusignAudit();
		docusignAudit.setCustomerEmail("abc@tatacommunications.com");
		docusignAudit.setStage("CUSTOMER");
		docusignAudit.setCustomerEnvelopeId("1");
		return docusignAudit;
	}

	public String getSIQueueResponse() {
		String response = "[{\"orderId\":95323,\"commercialId\":1785,\"city\":\"EP_V2_DEL\",\"serviceId\":\"2\",\"status\":\"Active\",\"assetId\":41047,\"zone\":null,\"businessUnit\":null,\"type\":\"ADDON\",\"productCatalogId\":\"7\",\"customerLeId\":\"1950\",\"name\":\"IPC addon\",\"serviceCreatedDate\":1573566313000,\"mrc\":3235.53,\"nrc\":0.0,\"arc\":38826.36,\"cloudCode\":\"V8EMQVPAHJ4WUQV3\",\"parentCloudCode\":null,\"siAssetComponentList\":[{\"name\":\"Quantity\",\"value\":\"1\",\"category\":\"VDOM\",\"mrc\":3235.53,\"nrc\":0.0,\"arc\":38826.37},{\"name\":\"clientToSite,siteToSite\",\"value\":\"0,0\",\"category\":\"VPN Connection\",\"mrc\":null,\"nrc\":null,\"arc\":null},{\"name\":\"managed\",\"value\":\"true\",\"category\":\"managed\",\"mrc\":null,\"nrc\":null,\"arc\":null}]},{\"orderId\":95323,\"commercialId\":1787,\"city\":\"EP_V2_DEL\",\"serviceId\":\"0910000A00000001840\",\"status\":\"Active\",\"assetId\":41049,\"zone\":null,\"businessUnit\":null,\"type\":\"ACCESS\",\"productCatalogId\":\"7\",\"customerLeId\":\"1950\",\"name\":\"Access\",\"serviceCreatedDate\":1573566313000,\"mrc\":1285.07,\"nrc\":0.0,\"arc\":15420.84,\"cloudCode\":\"SSZKCFLROCPOZWHO\",\"parentCloudCode\":null,\"siAssetComponentList\":[{\"name\":\"Data Transfer\",\"value\":\"100\",\"category\":\"Access Type\",\"mrc\":1285.07,\"nrc\":0.0,\"arc\":15420.84}]}]";
		return response;
	}

	public Set<QuoteToLe> getQuotesToLeadMACD() {
		Set<QuoteToLe> mockQuotesSet = new HashSet<>();
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setCurrencyId(1);
		quoteToLe.setFinalMrc(3.5D);
		quoteToLe.setFinalNrc(34.5D);
		quoteToLe.setProposedMrc(23.6D);
		quoteToLe.setId(2);
		quoteToLe.setTermInMonths("12");
		quoteToLe.setFinalNrc(67.4D);
		quoteToLe.setFinalArc(102.1D);
		quoteToLe.setProposedMrc(78.6D);
		quoteToLe.setProposedNrc(98.7D);
		quoteToLe.setQuoteToLeProductFamilies(getQuoteFamiles());
		quoteToLe.setStage("Get Quote");
		quoteToLe.setQuoteType("MACD");
		quoteToLe.setQuoteCategory("CONNECTIVITY_UPGRADE");
		mockQuotesSet.add(quoteToLe);
		return mockQuotesSet;
	}

	public Set<QuoteToLe> getQuotesToLeadMACDAddon() {
		Set<QuoteToLe> mockQuotesSet = new HashSet<>();
		QuoteToLe quoteToLe = new QuoteToLe();
		quoteToLe.setCurrencyId(1);
		quoteToLe.setFinalMrc(3.5D);
		quoteToLe.setFinalNrc(34.5D);
		quoteToLe.setProposedMrc(23.6D);
		quoteToLe.setId(2);
		quoteToLe.setTermInMonths("12");
		quoteToLe.setFinalNrc(67.4D);
		quoteToLe.setFinalArc(102.1D);
		quoteToLe.setProposedMrc(78.6D);
		quoteToLe.setProposedNrc(98.7D);
		quoteToLe.setQuoteToLeProductFamilies(getQuoteFamiles());
		quoteToLe.setStage("Get Quote");
		quoteToLe.setQuoteType("MACD");
		quoteToLe.setQuoteCategory("ADDITIONAL_SERVICE_UPGRADE");
		mockQuotesSet.add(quoteToLe);
		return mockQuotesSet;
	}

	public Optional<QuoteToLe> getOptionalQuoteCloudMACDAdd() {
		Optional<QuoteToLe> quoteToLe = getQuotesToLeadMACDAddon().stream().findFirst();
		return quoteToLe;
	}

	public Optional<QuoteToLe> getOptionalQuoteCloudMACD() {
		Optional<QuoteToLe> quoteToLe = getQuotesToLeadMACD().stream().findFirst();
		return quoteToLe;
	}

	public List<MstProductComponent> getListOfProductComponent() {
		List<MstProductComponent> list = new ArrayList<MstProductComponent>();
		list.add(getMstProductComponent());
		MstProductComponent mstProductComponent2 = new MstProductComponent();
		mstProductComponent2.setName("Additional Ip");
		MstProductComponent mstProductComponent1 = new MstProductComponent();
		mstProductComponent1.setName("VDOM");
		mstProductComponent1.setQuoteProductComponents(getQuoteProductComponentsSet());
		mstProductComponent2.setQuoteProductComponents(getQuoteProductComponentsSet());
		list.add(mstProductComponent2);
		list.add(mstProductComponent1);

		return list;
	}

	public List<QuotePrice> getQuotePrice() {
		List<QuotePrice> quoteList = new ArrayList<QuotePrice>();
		QuotePrice price1 = new QuotePrice();
		price1.setReferenceId("1");
		price1.setEffectiveArc(1200.00);
		price1.setEffectiveMrc(100.00);
		price1.setCatalogNrc(0.00);
		QuotePrice price2 = new QuotePrice();
		price2.setReferenceId("2");
		price2.setEffectiveArc(1200.00);
		price2.setEffectiveMrc(100.00);
		price2.setCatalogNrc(0.00);
		QuotePrice price3 = new QuotePrice();
		price3.setReferenceId("3");
		price3.setEffectiveArc(1200.00);
		price3.setEffectiveMrc(100.00);
		price3.setCatalogNrc(0.00);
		quoteList.add(price1);
		quoteList.add(price2);
		quoteList.add(price3);
		return quoteList;
	}

	public String getCurrentServiceListAccess() {
		String list = "[{\"orderId\":null,\"commercialId\":null,\"city\":null,\"serviceId\":null,\"status\":null,\"assetId\":13059,\"zone\":null,\"businessUnit\":null,\"type\":\"Access\",\"productCatalogId\":null,\"customerLeId\":null,\"name\":null,\"serviceCreatedDate\":null,\"mrc\":128.51,\"nrc\":0.0,\"arc\":1542.12,\"cloudCode\":null,\"parentCloudCode\":null,\"siAssetComponentList\":[{\"name\":\"Data Transfer\",\"value\":\"10\",\"category\":\"Access Type\",\"mrc\":128.51,\"nrc\":0.0,\"arc\":1542.12}]},{\"orderId\":null,\"commercialId\":null,\"city\":null,\"serviceId\":null,\"status\":null,\"assetId\":13060,\"zone\":null,\"businessUnit\":null,\"type\":\"IPC addon\",\"productCatalogId\":null,\"customerLeId\":null,\"name\":null,\"serviceCreatedDate\":null,\"mrc\":3235.53,\"nrc\":0.0,\"arc\":38826.36,\"cloudCode\":null,\"parentCloudCode\":null,\"siAssetComponentList\":[{\"name\":\"Quantity\",\"value\":\"1\",\"category\":\"VDOM\",\"mrc\":3235.53,\"nrc\":null,\"arc\":38826.37}]}]\r\n";
		return list;
	}

	public List<MstOmsAttribute> getMstOmsAttributeList() {
		List<MstOmsAttribute> mstOmsAttribute = new ArrayList<MstOmsAttribute>();
		mstOmsAttribute.add(getMstOmsAttribute());
		return mstOmsAttribute;
	}

	public Order getOrder() {
		Order order=new Order();
		order.setId(1);
		order.setOrderCode("IPC1342KSLDU");
		return order;
	}
	public Order getOrderWithId() {
		Order order=new Order();
		Quote quote= getQuote();
		order.setCreatedBy(quote.getCreatedBy());
		order.setCreatedTime(new Date());
		order.setStatus(quote.getStatus());
		order.setTermInMonths(quote.getTermInMonths());
		order.setCustomer(quote.getCustomer());
		order.setEffectiveDate(quote.getEffectiveDate());
		order.setStatus(quote.getStatus());
		order.setQuote(quote);
		order.setStage(OrderStagingConstants.ORDER_CREATED.name());
		order.setEndDate(quote.getEffectiveDate());
		order.setStartDate(quote.getEffectiveDate());
		order.setStatus(quote.getStatus());
		order.setOrderCode(quote.getQuoteCode());
		order.setQuoteCreatedBy(quote.getCreatedBy());
		order.setId(1);
		return order;
	}
	public Order[] getOrderArray() {
		Order[] orders = null;
		return orders;
		
	}
	public CofDetails getCofDetails() {
		CofDetails cofdetail=new CofDetails();
		cofdetail.setOrderUuid("1");
		cofdetail.setId(1);
		cofdetail.setUriPath("abc/test");
		return cofdetail;
		
	}

	public MacdDetail getMacdDetail() {
		MacdDetail macdDetail=new MacdDetail();
		macdDetail.setId(1);
		macdDetail.setOrderType("MACD");
		macdDetail.setQuoteToLeId(1);
		macdDetail.setTpsServiceId("091IPC45600");
		macdDetail.setStage("MACD_ORDER_INITIATED");
		return macdDetail;
	}
}
