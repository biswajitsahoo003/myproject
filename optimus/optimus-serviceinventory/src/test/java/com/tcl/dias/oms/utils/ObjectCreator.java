package com.tcl.dias.oms.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;


import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ServiceDetailBean;
import com.tcl.dias.serviceinventory.beans.ContactBeans;

import com.tcl.dias.common.beans.ProductInformationBean;

import com.tcl.dias.serviceinventory.beans.SIOrderBean;
import com.tcl.dias.serviceinventory.beans.SIServiceInformationBean;
import com.tcl.dias.serviceinventory.entity.entities.SIOrder;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceDetail;

/**
 * Class to create mock objects for test cases
 * 
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class ObjectCreator {

	
	public List<Map<String, Object>> getProductInformationBeans() {
        List<Map<String, Object>> productInformationBeans = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("productId", 1);
        map.put("count", 1);
        map.put("productName", "IAS");
        
              productInformationBeans.add(map);
        return productInformationBeans;
 }
	

	public SIOrderBean getSIOrderBean() {
		SIOrderBean bean = new SIOrderBean();
		bean.setErfCustomerId(1);
		bean.setId(1);
		// bean.setServiceId("serv id");
		// bean.setTopology("Topology");
		return bean;
	}

	public SIOrder getSIOrder() {
		SIOrder bean = new SIOrder();
		bean.setErfCustCustomerId("1");
		bean.setId(1);
//		bean.setServiceId("serv id");
//		bean.setServiceTopology("Topology");
		return bean;
	}

	public List<SIOrderBean> getSIOrderBeanList() {
		List<SIOrderBean> beanList = new ArrayList<>();
		beanList.add(getSIOrderBean());
		return beanList;
	}

	public List<SIOrder> getSIOrderList() {
		List<SIOrder> beanList = new ArrayList<>();
		beanList.add(getSIOrder());
		return beanList;
	}


	public List<ServiceDetailBean> getServiceDetailBeans() {
		List<ServiceDetailBean> serviceDetailBeans = new ArrayList<>();
		ServiceDetailBean bean = new ServiceDetailBean();
		bean.setOrderCode("1772555");
		bean.setSiServiceDetailId(102508);
		bean.setCustomerId("904");
		bean.setCustomerName("Inventurus Knowledge Solutions Pvt Ltd");
		bean.setSfdcAccountId("0012000000LxhbSAAR");
		bean.setSfdcCuid("104646");
		bean.setSourceCity("Mumbai");
		bean.setAlias("Nilkamal");
		bean.setServiceId("091MUMB623029393221");
		serviceDetailBeans.add(bean);
		return serviceDetailBeans;

	}

	public SIServiceInformationBean getSiServiceInfo() {
		SIServiceInformationBean siServiceInfo = new SIServiceInformationBean();
		siServiceInfo.setTotalPages(1);
		siServiceInfo.setTotalItems(1);
		List<String> alias = new ArrayList<>();
		List<String> cities = new ArrayList<>();
		String[] aliasList = { "Nilkamal", "Hyderabad office", "Tata Mumbai ", "Hyderabad " };
		alias = Arrays.asList(aliasList);
		siServiceInfo.setAlias(alias);
		String[] citiesList = { "Mumbai", "Hyderabad", "Navi Mumbai" };
		cities = Arrays.asList(citiesList);
		siServiceInfo.setCities(cities);
		siServiceInfo.setContacts(new ArrayList<ContactBeans>());
		siServiceInfo.setServiceDetailBeans(getServiceDetailBeans());

		return siServiceInfo;
	}

	public PagedResult<List<SIServiceInformationBean>> getSIServiceInformationList() {
		List<SIServiceInformationBean> siServiceList = new ArrayList<>();
		siServiceList.add(getSiServiceInfo());
		siServiceList.add(getSiServiceInfo());
		siServiceList.add(getSiServiceInfo());
		return new PagedResult(siServiceList, getSiServiceInfo().getTotalItems(), getSiServiceInfo().getTotalPages());

	}

	public String getProductSlaDetailsJSON() {
		return "[{\"factor\":\"Service Availability %\",\"value\":\">= 99.9%\"}]";
	}

	public List<Map<String, Object>> getServiceInformationBeans() {
		List<Map<String, Object>> productInformationBeans = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("orderCode", "1772555");
        map.put("siServiceDetailId",102508);
        map.put("customerId","904");
        map.put("customerName","Inventurus Knowledge Solutions Pvt Ltd");
        map.put("sfdcAccountId", "0012000000LxhbSAAR");
        map.put("sfdcCuid","104646");
        map.put("leId", "1950");
        map.put("leName", "Inventurus Knowledge Solutions Private Limited");
        map.put("customerSegment","Enterprise – Silver");
        map.put("opportunityClassification", "Sell To");
        map.put("supplierLeId", "5");
       map.put("supplierLeName", "Tata Communications Limited");
       map.put("serviceId", "091MUMB623029393221");
       map.put("productId", 1);
       map.put("offeringId", null);
       map.put("productName", "GVPN");
       map.put("offeringName","ENTERPRISE GVPN");
       map.put("accessType", null);
       map.put("siteLinkLabel", null);
       map.put("latLong", "19.121772000000000,72.872192000000000");
       map.put("siteAddress", "Nilkamal Limited 77/78 Nilkamal House Road No. 13-14 M.I.D.C Andheri-East  College  Prabhadevi    Mumbai 400093");
       map.put("siteTopology", "Spoke");
       map.put("serviceTopology","Hub & Spoke");
       map.put("serviceClass","100:0:0:0:0:0");
       map.put("smName", null);
       map.put("smEmail", null);
       map.put("serviceClassification", null);
       map.put("siteType", null);
       map.put("commissionedDate",null);
       map.put("sourceCity", "Mumbai");
       map.put("destinationCity", null);
       map.put("portSpeed", "30");
       map.put("portSpeedUnit", "Mbps");
       map.put("alias", "Nilkamal");
       map.put("linkType", "Single");
       map.put("primaryServiceId", "091MUMB623029393221");
       map.put("secondaryServiceId", null);
       map.put("vpnId", "Inv29393217");
       map.put("serviceAssuranceContacts", null);
		productInformationBeans.add(map);
		return productInformationBeans;
	}

	public List<Integer> getCustomerLedIds() {
		List<Integer> cusLeIds = new ArrayList<>();
		cusLeIds.add(1);
		return cusLeIds;
	}

	public List<String> getAlias()
	{
		List<String> alias=new ArrayList<>();
		alias.add("Nilkamal");
		alias.add("Tata mumbai");
		return alias;
	}
	
	public List<String> getCities()
	{
		List<String> cities=new ArrayList<>();
		cities.add("Mumbai");
		cities.add("Chennai");
		return cities;
	}
	
	public List<SIServiceDetail> getServiceDetails()
	{
		List<SIServiceDetail> serviceDetails=new ArrayList<>();
		SIServiceDetail serviceDetail=new SIServiceDetail();
		serviceDetail.setAccessType("XXX");
		serviceDetail.setArc(100D);
		serviceDetail.setErfPrdCatalogProductName("IAS");
		serviceDetails.add(serviceDetail);
		return serviceDetails;
	}

	public List<ProductInformationBean> createProductInformationBeanList(){
		ProductInformationBean productInformationBean = new ProductInformationBean();
		List<ProductInformationBean> productInformationBeanList = new ArrayList<>();
		productInformationBean.setCount(1);
		productInformationBean.setProductId(11);
		productInformationBean.setProductName("gsip");
		productInformationBeanList.add(productInformationBean);
		return productInformationBeanList;
	}
	
	public List<Integer> createCustomerIdsList(){
		List<Integer> customerIdsList = new ArrayList();
		customerIdsList.add(1);
		customerIdsList.add(2);
		customerIdsList.add(3);
		return customerIdsList;
	}

}
