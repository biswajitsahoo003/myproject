package com.tcl.dias.cramer.controller;

public interface CramerConstants {

	//String countryendpoint="http://uswv1vuap003a.vsnl.co.in:8080/ListCramerResources/GetCountryList";
	String countryreq="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.cnms.tcomm.com\"><soapenv:Header/><soapenv:Body><ws:getCountryList><maskCountryName></maskCountryName></ws:getCountryList></soapenv:Body></soapenv:Envelope>";

	//String stateendpoint="http://uswv1vuap003a.vsnl.co.in:8080/ListCramerResources/GetStateList";
	String statereq="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.cnms.tcomm.com\"><soapenv:Header/><soapenv:Body><ws:getStateList><!--Optional:--><countryName>{0}</countryName><!--Optional:--><maskStateName></maskStateName></ws:getStateList></soapenv:Body></soapenv:Envelope>";
	
	//String cityendpoint="http://uswv1vuap003a.vsnl.co.in:8080/ListCramerResources/GetCityList";
	String cityreq="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.cnms.tcomm.com\"><soapenv:Header/><soapenv:Body><ws:getCityList><locationName>{0}</locationName><locationType>{1}</locationType><maskCityName>{2}</maskCityName></ws:getCityList> </soapenv:Body></soapenv:Envelope>";

	//String areaendpoint="http://uswv1vuap003a.vsnl.co.in:8080/CramerBPMIntegration/GetArea4City";
	String areareq="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"http://com.tatacommunications.cramerbpmintegration.ws\"><soapenv:Header/><soapenv:Body><com:getArea4CityByName><cityName>{0}</cityName></com:getArea4CityByName></soapenv:Body></soapenv:Envelope>";

	//String isdeviceexistsendpoint="http://uswv1vuap003a.vsnl.co.in:8080/CheckCramerResources/IsDeviceExist";
	String isdeviceexistsendpointreq="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.cnms.tcomm.com\"><soapenv:Header/><soapenv:Body><ws:isDeviceExists><deviceName>{0}</deviceName><orderType></orderType></ws:isDeviceExists></soapenv:Body></soapenv:Envelope>";

	//String building4areaendpoint="http://uswv1vuap003a.vsnl.co.in:8080/CramerBPMIntegration/GetBuilding4Area";
	String building4areareq="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"http://com.tatacommunications.cramerbpmintegration.ws\"><soapenv:Header/><soapenv:Body><com:getBuilding4AreaById><!--Optional:--><areaId>{0}</areaId></com:getBuilding4AreaById></soapenv:Body></soapenv:Envelope>";

	//String createObjectendpoint="http://uswv1vuap003a.vsnl.co.in:8080/IMSOMSInregrationServices/CreateObject";
	String createObject="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.cnms.tcomm.com\"><soapenv:Header/><soapenv:Body><ws:createObject><orderId>{0}</orderId><orderType>{1}</orderType><technology>{2}</technology></ws:createObject></soapenv:Body></soapenv:Envelope>";

	String updateObject = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.cnms.tcomm.com\"><soapenv:Header/><soapenv:Body><ws:updateObjectProvisioningStatus><!--Optional:--><orderId>{0}</orderId><!--Optional:--><orderType>{1}</orderType><!--Optional:--><provisioningStatus>{2}</provisioningStatus></ws:updateObjectProvisioningStatus></soapenv:Body></soapenv:Envelope>";
	//String updateObjectendpoint = "http://uswv1vuap003a.vsnl.co.in:8080/IMSOMSInregrationServices/UpdateObjectProvisioningStatus";

	String getSupportedCardType = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.cnms.tcomm.com\"><soapenv:Header/><soapenv:Body><ws:getSupportedCardTypes4Node>{0}</ws:getSupportedCardTypes4Node></soapenv:Body></soapenv:Envelope>";
	//String getSupportedCardTypeEndpoint = "http://uswv1vuap003a.vsnl.co.in:8080/ListCramerResources/GetSupportedCardTypes4Node";
}

