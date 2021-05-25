package com.tcl.dias.customer.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.ServiceScheduleBean;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.customer.bean.CustomerLeBillingRequestBean;
import com.tcl.dias.customer.bean.CustomerLegalEntityRequestBean;
import com.tcl.dias.customer.bean.SiteCountryBean;
import com.tcl.dias.customer.entity.entities.Attachment;
import com.tcl.dias.customer.entity.entities.CurrencyMaster;
import com.tcl.dias.customer.entity.entities.Customer;
import com.tcl.dias.customer.entity.entities.CustomerAttributeValue;
import com.tcl.dias.customer.entity.entities.CustomerLeAttributeValue;
import com.tcl.dias.customer.entity.entities.CustomerLeBillingInfo;
import com.tcl.dias.customer.entity.entities.CustomerLeContact;
import com.tcl.dias.customer.entity.entities.CustomerLeCountry;
import com.tcl.dias.customer.entity.entities.CustomerLeCurrency;
import com.tcl.dias.customer.entity.entities.CustomerLegalDataCenters;
import com.tcl.dias.customer.entity.entities.CustomerLegalEntity;
import com.tcl.dias.customer.entity.entities.LeStateGst;
import com.tcl.dias.customer.entity.entities.MstCountry;
import com.tcl.dias.customer.entity.entities.MstCustomerSpAttribute;
import com.tcl.dias.customer.entity.entities.MstLeAttribute;
import com.tcl.dias.customer.entity.entities.ServiceProvider;
import com.tcl.dias.customer.entity.entities.ServiceProviderLegalEntity;
import com.tcl.dias.customer.entity.entities.SpLeAttributeValue;
import com.tcl.dias.customer.entity.entities.SpLeCountry;

/**
 * 
 * This file contains the Object Creator class.
 * 
 *
 * @author Vinodk
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ObjectCreator {

	public ObjectCreator() {

	}

	/*
	 * This is method return List of CustomerLeAttributeValue to test.
	 * 
	 */
	public List<CustomerLeAttributeValue> getBillingDetails() {
		List<CustomerLeAttributeValue> customerLeAttributeValueList = new ArrayList<>();
		CustomerLeAttributeValue customerLeAttributeValue = new CustomerLeAttributeValue();
		MstLeAttribute mstLeAttribute = new MstLeAttribute();
		mstLeAttribute.setName("Billing Method");
		mstLeAttribute.setStatus((byte) 1);
		mstLeAttribute.setType("billing");
		customerLeAttributeValue.setAttributeValues("30/30");
		customerLeAttributeValue.setMstLeAttribute(mstLeAttribute);
		customerLeAttributeValueList.add(customerLeAttributeValue);
		return customerLeAttributeValueList;
	}

	public CustomerLegalEntity craeteCustomerLegalEntity() {

		CustomerLegalEntity cle = new CustomerLegalEntity();
		cle.setAgreementId("FFF");
		cle.setId(1);
		cle.setCustomer(createCustomer());
		// cle.setCountry("INDIA");
		cle.setTpsSfdcCuid("TSFCF");

		return cle;

	}

	public Customer createCustomer() {
		Customer customer = new Customer();
		customer.setId(1);

		return customer;

	}

	public List<CustomerLeAttributeValue> createcustomerLeAttributeValue() {
		List<CustomerLeAttributeValue> cal = new ArrayList<>();
		cal.add(createCustomerLeAttributeValue());
		cal.add(createCustomerLeAttributeValue1());
		return cal;

	}

	public CustomerLeAttributeValue createCustomerLeAttributeValue() {
		CustomerLeAttributeValue clv = new CustomerLeAttributeValue();
		clv.setAttributeValues("1");
		clv.setId(1);
		clv.setMstLeAttribute(craeteMstLeAttributeNew());
		clv.setCustomerLegalEntity(createCustomlegalEntity());
		clv.setProductName("IAS");

		return clv;
	}

	public CustomerLegalEntity createCustomlegalEntity() {
		CustomerLegalEntity cle = new CustomerLegalEntity();
		cle.setAgreementId("FFF");
		cle.setId(112);
		cle.setEntityName("TATA");
		cle.setCustomer(createCustomer());
		cle.setTpsSfdcCuid("TSFCF");
		CustomerLeCountry clc = new CustomerLeCountry();
		clc.setIsDefault((byte) 1);
		Set<CustomerLeCountry> setClc = new HashSet<>();
		setClc.add(clc);
		cle.setCustomerLeCountries(setClc);
		Set<CustomerLeCurrency>  customerLeCurrencies = new HashSet<>();
		CustomerLeCurrency customerLeCurrency=new CustomerLeCurrency();
		customerLeCurrency.setCurrencyMaster(new CurrencyMaster());
		customerLeCurrencies.add(customerLeCurrency);
		cle.setCustomerLeCurrencies(customerLeCurrencies);
		MstCountry mstCountry = new MstCountry();
		mstCountry.setName("india");
		clc.setMstCountry(mstCountry);
		return cle;
	}

	public CustomerLegalEntity createCustomlegalEntity2() {
		CustomerLegalEntity cle = new CustomerLegalEntity();
		cle.setAgreementId("FFF");
		cle.setId(112);
		cle.setEntityName("TATA");
		cle.setCustomer(createCustomer());
		cle.setTpsSfdcCuid("TSFCF");
		CustomerLeCountry clc = new CustomerLeCountry();
		clc.setIsDefault((byte) 1);
		Set<CustomerLeCountry> setClc = new HashSet<>();
		setClc.add(clc);
		cle.setCustomerLeCountries(setClc);
		MstCountry mstCountry = new MstCountry();
		mstCountry.setName("Singapore");
		clc.setMstCountry(mstCountry);
		return cle;
	}

	public MstLeAttribute craeteMstLeAttribute() {
		MstLeAttribute mla = new MstLeAttribute();
		mla.setId(1);
		mla.setName("Test");
		mla.setType("Billing");
		mla.setName("Supplier Contracting Entity");
		mla.setDescription("PaymentType");
		return mla;
	}

	public MstLeAttribute craeteMstLeAttributeNew() {
		MstLeAttribute mla = new MstLeAttribute();
		mla.setId(1);
		mla.setName("Test");
		mla.setType("Billing");
		mla.setName("MSA");
		mla.setDescription("PaymentType");
		return mla;
	}

	public SpLeAttributeValue craeteSpLeAttributeValue() {
		SpLeAttributeValue sav = new SpLeAttributeValue();
		sav.setAttributeValues("Customer");
		sav.setMstLeAttribute(craeteMstLeAttribute());
		sav.setId(1);
		sav.setServiceProviderLegalEntity(createserviceProviderLegalEntity());
		return sav;
	}

	public ServiceProviderLegalEntity createserviceProviderLegalEntity() {
		ServiceProviderLegalEntity sple = new ServiceProviderLegalEntity();
		sple.setId(1);
		sple.setTpsSfdcCuid("TSFCF");
		sple.setEntityName("TATA");
		return sple;

	}

	public List<SpLeAttributeValue> createsupplierLeAttributeValue() {
		List<SpLeAttributeValue> splv = new ArrayList<>();
		splv.add(craeteSpLeAttributeValue());

		return splv;

	}

	public List<CustomerLegalEntity> createCustomerLegalEntity() {
		CustomerLegalEntity customerLegalEntityDto = new CustomerLegalEntity();
		List<CustomerLegalEntity> list = new ArrayList<>();
		Customer customer = new Customer();
		customer.setId(1);
		customerLegalEntityDto.setAgreementId("fidexo");
		// customerLegalEntityDto.setCountry("India");
		// customerLegalEntityDto.setCreatedTime(new Date());
		customerLegalEntityDto.setCustomer(customer);
		customerLegalEntityDto.setStatus((byte) 1);
		customerLegalEntityDto.setId(1);
		customerLegalEntityDto.setCustomerLeAttributeValues(new HashSet<CustomerLeAttributeValue>(createcustomerLeAttributeValue()));
		list.add(customerLegalEntityDto);
		return list;
	}

	public ServiceProvider craeteServiceLeAttribute() {
		ServiceProvider provider = new ServiceProvider();
		Set<ServiceProviderLegalEntity> serviceProviderLegalEntitySet = new HashSet<>();
		serviceProviderLegalEntitySet.add(createserviceProviderLegalEntity());
		provider.setCreatedBy("admin");
		provider.setName("TATA");
		provider.setServiceProviderLegalEntities(serviceProviderLegalEntitySet);
		return provider;
	}

	public SpLeAttributeValue craeteSPLAttrLeAttribute() {
		SpLeAttributeValue spLeAttributeValue = new SpLeAttributeValue();
		spLeAttributeValue.setMstLeAttribute(craeteMstLeAttribute());
		spLeAttributeValue.setServiceProviderLegalEntity(createserviceProviderLegalEntity());
		return spLeAttributeValue;
	}

	public Attachment createAttachMent() {

		Attachment attachment = new Attachment();
		attachment.setCreatedTime(new Timestamp(new Date().getTime()));
		attachment.setId(2);
		attachment.setUriPathOrUrl("D:/files/20180827182147");

		return attachment;

	}

	public List<SpLeCountry> createServiceProviderLeCountry() {

		List<SpLeCountry> spleCountryList = new ArrayList<>();
		MstCountry mstCountry = new MstCountry();
		mstCountry.setId(1);
		mstCountry.setName("INDIA");
		mstCountry.setShortName("IND");
		mstCountry.setStatus((byte) 1);

		SpLeCountry spLeCountry = new SpLeCountry();
		spLeCountry.setId(1);
		spLeCountry.setIsDefault((byte) 1);
		spLeCountry.setMstCountry(mstCountry);
		spLeCountry.setServiceProviderLegalEntity(createserviceProviderLegalEntity());
		spleCountryList.add(spLeCountry);
		return spleCountryList;

	}

	public CustomerLeAttributeValue createCustomerLeAttributeValue1() {
		CustomerLeAttributeValue clv = new CustomerLeAttributeValue();
		clv.setAttributeValues("1");
		clv.setId(1);
		clv.setMstLeAttribute(craeteMstLeAttributeNew1());
		clv.setCustomerLegalEntity(createCustomlegalEntity());

		return clv;
	}

	public MstLeAttribute craeteMstLeAttributeNew1() {
		MstLeAttribute mla = new MstLeAttribute();
		mla.setId(1);
		mla.setName("Customer Contracting Entity");
		mla.setType("Billing");
		mla.setDescription("PaymentType");
		return mla;
	}

	public Attachment createAttachmentResponse() {
		Attachment attachment = new Attachment();
		attachment.setId(1);
		attachment.setName("1");
		attachment.setDisplayName("1.pdf");
		attachment.setUriPathOrUrl("/optimus/1/");
		return attachment;
	}

	public List<Attachment> createAttachmentList() {
		List<Attachment> attachmentList = new ArrayList<Attachment>();
		attachmentList.add(createAttachMent());
		attachmentList.add(createAttachMent());
		return attachmentList;
	}

	public List<CustomerLegalEntity> createCustomerLegalEntityWithAttributes() {
		List<CustomerLegalEntity> customerLegalEntityList = new ArrayList<>();
		CustomerLegalEntity customerLegalEntity = new CustomerLegalEntity();
		CustomerLeAttributeValue customerAttributeValue = new CustomerLeAttributeValue();
		customerAttributeValue.setAttributeValues("1");
		customerAttributeValue.setId(1);
		MstLeAttribute mstLeAttribute = new MstLeAttribute();
		mstLeAttribute.setType("Attachment");
		mstLeAttribute.setStatus((byte) 1);
		Customer cust = new Customer();
		cust.setId(1);
		cust.setCustomerName("TCS");
		customerLegalEntity.setCustomer(cust);
		customerAttributeValue.setMstLeAttribute(mstLeAttribute);
		Set<CustomerLeAttributeValue> customerAttributeValueSet = new HashSet<CustomerLeAttributeValue>();
		customerAttributeValueSet.add(customerAttributeValue);
		// mstLeAttribute.setCustomerLeAttributeValues(customerAttributeValueSet);
		customerAttributeValue.setCustomerLegalEntity(customerLegalEntity);
		customerLegalEntity.setCustomerLeAttributeValues(customerAttributeValueSet);
		customerLegalEntityList.add(customerLegalEntity);
		return customerLegalEntityList;
	}

	public List<CustomerLegalEntity> createCustomerLegalEntityWOAttachments() {
		List<CustomerLegalEntity> customerLegalEntityList = new ArrayList<>();
		CustomerLegalEntity customerLegalEntity = new CustomerLegalEntity();
		CustomerLeAttributeValue customerAttributeValue = new CustomerLeAttributeValue();
		customerAttributeValue.setAttributeValues("1");
		customerAttributeValue.setId(1);
		MstLeAttribute mstLeAttribute = new MstLeAttribute();
		mstLeAttribute.setStatus((byte) 1);
		Customer cust = new Customer();
		cust.setId(1);
		cust.setCustomerName("TCS");
		customerLegalEntity.setCustomer(cust);
		customerAttributeValue.setMstLeAttribute(mstLeAttribute);
		Set<CustomerLeAttributeValue> customerAttributeValueSet = new HashSet<CustomerLeAttributeValue>();
		customerAttributeValueSet.add(customerAttributeValue);
		// mstLeAttribute.setCustomerLeAttributeValues(customerAttributeValueSet);
		customerAttributeValue.setCustomerLegalEntity(customerLegalEntity);
		customerLegalEntity.setCustomerLeAttributeValues(customerAttributeValueSet);
		customerLegalEntityList.add(customerLegalEntity);
		return customerLegalEntityList;
	}
	
	@SuppressWarnings("rawtypes")
	public List<CustomerLegalEntity> returnCustomerLegalEntity(){
		List<CustomerLegalEntity> entityList=new ArrayList<>();
		CustomerLegalEntity entity=new CustomerLegalEntity();
		entity.setId(1);
		entity.setStatus((byte)1);
		Customer cust = new Customer();
		cust.setId(1);
		cust.setCustomerName("TCS");
		entity.setCustomer(cust);
		entity.setCreatedTime(new Date());
		entity.setAgreementId("Sample");
		entity.setEntityName("Entity");
		entity.setCustomerLeAttributeValues(new HashSet(createcustomerLeAttributeValue()));
		entityList.add(entity);
		return entityList;
	}
	
	@SuppressWarnings("rawtypes")
	public List<CustomerLegalEntity> returnCustomerLegalEntityInsufficient(){
		List<CustomerLegalEntity> entityList=new ArrayList<>();
		CustomerLegalEntity entity=new CustomerLegalEntity();
		entity.setId(1);
		entity.setStatus((byte)1);
		Customer cust = new Customer();
		cust.setId(1);
		cust.setCustomerName("TCS");
		entity.setCustomer(cust);
		entity.setCreatedTime(new Date());
		entity.setAgreementId("Sample");
		entity.setEntityName("Entity");
		entityList.add(entity);
		return entityList;
	}
	
	public UserInformation getUserInformation() {
		UserInformation userInformation = new UserInformation();
		userInformation.setUserId("admin");
		userInformation.setCustomers(getCustomerList());
		userInformation.setRole(Arrays.asList("OPTIMUS_MU"));
		return userInformation;

	}
	
	public List<CustomerDetail> getCustomerList()
	{
		List<CustomerDetail>  list=new ArrayList<>();
		list.add(createCustomerDetails());
		return list;
		
		
		
	}
	
	public CustomerDetail createCustomerDetails()
	{
		CustomerDetail cd=new CustomerDetail();
		cd.setCustomerAcId("test");
		cd.setCustomerId(1);
		cd.setCustomerLeId(1);
		cd.setErfCustomerId(1);
		cd.setStatus((byte)1);
		return cd;	
		
	}
	
	public List<CustomerLeBillingInfo> getCustomerLeBillingInfoList(){
		List<CustomerLeBillingInfo> customerLeBillingInfoList = new ArrayList<>();
		customerLeBillingInfoList.add(getCustomerLeBillingInfo());
		return customerLeBillingInfoList;
	}

	public CustomerLeBillingInfo getCustomerLeBillingInfo() {
		CustomerLeBillingInfo customerLeBillingInfo = new CustomerLeBillingInfo();
		customerLeBillingInfo.setId(1);
		customerLeBillingInfo.setFname("customer1");
		customerLeBillingInfo.setBillAddr("billnn");
		customerLeBillingInfo.setCustomerLegalEntity(craeteCustomerLegalEntity());
		return customerLeBillingInfo;
	}
	public LeStateGst getLeStateGst() {
		LeStateGst leStateGst = new LeStateGst();
		leStateGst.setAddress("address");
		leStateGst.setId(1);
		leStateGst.setGstNo("12");
		return leStateGst;
	}
	
	public List<LeStateGst> getLeStateGstList() {
		List<LeStateGst> listLeStateGst = new ArrayList();
		listLeStateGst.add(getLeStateGst());
		return listLeStateGst;
	}
	
	public MstCountry createMstCountry() {
		MstCountry mstCountry = new MstCountry();
		mstCountry.setId(1);
		mstCountry.setName("INDIA");
		mstCountry.setShortName("IND");
		mstCountry.setStatus((byte) 1);
		return mstCountry;
	}
	
	public List<MstCountry> createMstCountryList(){
		List<MstCountry> mstCountryList = new ArrayList();
		mstCountryList.add(createMstCountry());
		return mstCountryList;
	}
	
	public MstCustomerSpAttribute createMstCustomerSpAttribute() {
		MstCustomerSpAttribute mstCustomerSpAttribute = new MstCustomerSpAttribute();
		mstCustomerSpAttribute.setId(1);
		mstCustomerSpAttribute.setName("test");
		return mstCustomerSpAttribute;
	}
	
	public CustomerAttributeValue createCustomerAttributeValue() {
		CustomerAttributeValue customerAttributeValue = new CustomerAttributeValue();
		
		customerAttributeValue.setAttributeValues("1");
		customerAttributeValue.setCustomer(createCustomer());
		customerAttributeValue.setId(1);
		return customerAttributeValue;
	}
	
	public List<CustomerAttributeValue> createCustomerAttributeValueList(){
		List<CustomerAttributeValue> customerAttributeValueList = new ArrayList();
		customerAttributeValueList.add(createCustomerAttributeValue());
		return customerAttributeValueList;
	}
	
	public List<MstCustomerSpAttribute> createMstCustomerSpAttributeList() {
		List<MstCustomerSpAttribute> mstCustomerSpAttributeList = new ArrayList();
		mstCustomerSpAttributeList.add(createMstCustomerSpAttribute());
		return mstCustomerSpAttributeList;
	}

	/**
	 * createCustomerLegalEntityRequestBean
	 * @return
	 */
	public CustomerLegalEntityRequestBean createCustomerLegalEntityRequestBean() {
		CustomerLegalEntityRequestBean legalEntityRequestBean=new CustomerLegalEntityRequestBean();
		legalEntityRequestBean.setCustomerLegalEntityId(1);
		legalEntityRequestBean.setProductName("GVPN");
		legalEntityRequestBean.setSiteCountry(getSiteCountryBean());
		return legalEntityRequestBean;
	}

	/**
	 * getSiteCountryBean
	 * @return
	 */
	private List<SiteCountryBean> getSiteCountryBean() {
		List<SiteCountryBean> siteCountryBean =new ArrayList<>();
		SiteCountryBean bean=new SiteCountryBean();
		bean.setSiteId(1);
		bean.setCountry("India");
		siteCountryBean.add(bean);
		return siteCountryBean;
	}
	public List<Customer> createCustomerList() {
		List<Customer> customerList =new ArrayList<>();
		customerList.add(createCustomer());
		return customerList;
	}
	public  List<CustomerLeContact> getCustomerLeContactList(){
		List<CustomerLeContact> customerLeContactList=new ArrayList<>();
		customerLeContactList.add(getCustomerLeContact());
		return customerLeContactList;
	}
	public  CustomerLeContact getCustomerLeContact(){
		CustomerLeContact customerLeContact=new CustomerLeContact();
		customerLeContact.setId(1);
		customerLeContact.setAddress("JTP");
		customerLeContact.setCustomerLeId(2);
		customerLeContact.setName("regus");
		customerLeContact.setEmailId("regus@tatacommunications.com");
		return customerLeContact;
	}
	public ServiceScheduleBean getServiceScheduleBean() {
		ServiceScheduleBean serviceScheduleBean=new ServiceScheduleBean();
		serviceScheduleBean.setCustomerLeId(1);
		serviceScheduleBean.setDisplayName("ACT");
		serviceScheduleBean.setIsSSUploaded(true);
		serviceScheduleBean.setName("ACT");
		serviceScheduleBean.setProductName("NPL");
		return serviceScheduleBean;
	}
	
	public CustomerLeBillingRequestBean getCustomerLeBillingRequestBean() {
		CustomerLeBillingRequestBean customerLeBillingRequestBean=new CustomerLeBillingRequestBean();
		customerLeBillingRequestBean.setCustomerId(1);
		customerLeBillingRequestBean.setBillAccNo("GHT560JK");
		customerLeBillingRequestBean.setBillAddr("TM");
		customerLeBillingRequestBean.setEmailId("tata@com");
		customerLeBillingRequestBean.setFname("tata");
		customerLeBillingRequestBean.setLname("tata");
		customerLeBillingRequestBean.setPhoneNumber("3445678909");
		customerLeBillingRequestBean.setQuoteToLeId(1);
		return customerLeBillingRequestBean;
		
	}
	public CustomerLegalDataCenters getCustomerLegalDataCenters() {
		CustomerLegalDataCenters customerLegalDataCenters=new CustomerLegalDataCenters();
		customerLegalDataCenters.setCustomerleId(1);
		customerLegalDataCenters.setDataCenterId(2);
		customerLegalDataCenters.setId(3);
		return customerLegalDataCenters;
	}
	public List<CustomerLegalDataCenters> getCustomerLegalDataCenterList() {
		List<CustomerLegalDataCenters> customerLegalDataCenters=new ArrayList<>();
		customerLegalDataCenters.add(getCustomerLegalDataCenters());
		return customerLegalDataCenters;
	}
	public List<CustomerDetail> getCustomerDetailList(){
		List <CustomerDetail> custList = new ArrayList<>();
		UserInformation userInformation = new UserInformation();
		userInformation.setUserId("optimus_regus");
		CustomerDetail c = new CustomerDetail();
		c.setCustomerId(3);
		c.setCustomerAcId("SFDC - 000430");
		c.setErfCustomerId(4);
		c.setStatus((byte) 1);
		custList.add(c);
		userInformation.setCustomers(custList);
		return custList;
	}
}
