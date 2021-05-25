package com.tcl.dias.cramer.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.tcl.dias.cramer.controller.CramerConfigs;
import com.tcl.dias.cramer.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.tcl.dias.cramer.controller.CramerConstants;

@Service
public class CramerService {

	@Autowired
	RestTemplate resttemplate;

	@Autowired
	CramerConfigs configs;
	
	
	public List<Country> getCountryList() {
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "text/xml;charset=UTF-8");
		headers.set("SOAPAction", "");

		HttpEntity<String> requestEntity = new HttpEntity<>(CramerConstants.countryreq, headers);
		
		 ResponseEntity<String> responseentity =
		 resttemplate.exchange(configs.getCramerBaseUrl()+configs.getCountryEndpoint(), HttpMethod.POST,requestEntity, String.class);
		 String response=responseentity.getBody();

		/*String response=null;
		try {
			 response=FileUtils.readFileToString(new File("C:\\Users\\rvenkataravanapp\\Downloads\\testxml.xml"),"UTF-8");
		}catch(Exception ex) {
			
		}*/
		List<Country> countrylist=parseResponse(response);
		
		
		return countrylist;
	}


	private List<Country> parseResponse(String response) {
		
		List<Country> countrylist=new ArrayList<>();
		
		try {
		
			JSONObject json=XML.toJSONObject(response);
		    JSONObject envelopjson=json.getJSONObject("env:Envelope");
		    
		    JSONObject bodyjson=envelopjson.getJSONObject("env:Body");
		    JSONObject countryListJson=bodyjson.getJSONObject("ns2:getCountryListResponse");
		    JSONObject responsejson=countryListJson.getJSONObject("response");
		    
		    JSONArray countryjsonarr=responsejson.getJSONArray("CountryList");
		    
		    for(int i=0;i<countryjsonarr.length();i++) {
		    	
		    	JSONObject countryJsonobj=countryjsonarr.getJSONObject(i);
		    	
		    	String countryname=countryJsonobj.getString("countryName");
		    	
		    	Country country=new Country(countryname);
		    	
		    	countrylist.add(country);
		
		    }
		    
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return countrylist;
	}


	public List<State> getStateList(String countryname) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "text/xml;charset=UTF-8");
		headers.set("Accept-Encoding", "gzip,deflate");
		headers.set("SOAPAction", "");
		
		
		String statereq=CramerConstants.statereq;
		
		Object params[]= {countryname};
		
		String statereqstr=MessageFormat.format(statereq, params);
		
		System.out.println(statereqstr);
		HttpEntity<String> requestEntity = new HttpEntity<>(statereqstr, headers);
		
		
		ResponseEntity<String> responseentity = resttemplate.exchange(configs.getCramerBaseUrl()+configs.getStateEndpoint(), HttpMethod.POST,
				requestEntity, String.class);
		String response = responseentity.getBody();


		/*String response=null;
		try {
			 response=FileUtils.readFileToString(new File("C:\\Users\\rvenkataravanapp\\Downloads\\list-response.xml"),"UTF-8");
		}catch(Exception ex) {
			
		}*/
		List<State> statelist=parsestateResponse(response);
		
		return statelist;
	}

	private List<State> parsestateResponse(String response) {
		List<State> statelist=new ArrayList<>();
		
		try {
		
			JSONObject json=XML.toJSONObject(response);
		    JSONObject envelopjson=json.getJSONObject("env:Envelope");
		    
		    JSONObject bodyjson=envelopjson.getJSONObject("env:Body");
		    JSONObject countryListJson=bodyjson.getJSONObject("ns2:getStateListResponse");
		    JSONObject responsejson=countryListJson.getJSONObject("response");
		    
		    JSONArray statejsonarr=responsejson.getJSONArray("results");
		    
		    for(int i=0;i<statejsonarr.length();i++) {
		    	
		    	JSONObject stateJsonobj=statejsonarr.getJSONObject(i);
		    	
		    	String statename=stateJsonobj.getString("stateName");
		    	String statecode=stateJsonobj.getString("stateCode");
		    	State state=new State(statecode, statename);
		    	
		    	statelist.add(state);
		
		    }
		    
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return statelist;
	}
	
	public List<City> getCityList(String locationName, String locationType, String cityname) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "text/xml;charset=UTF-8");
		headers.set("Accept-Encoding", "gzip,deflate");
		headers.set("SOAPAction", "");
		
		
		String cityreq=CramerConstants.cityreq;
		String newCityname = "";
		if (!cityname.isEmpty()){newCityname=cityname;}

		Object params[]= {locationName,locationType,newCityname};
		
		String cityreqstr=MessageFormat.format(cityreq, params);
		
		System.out.println(cityreqstr);
		HttpEntity<String> requestEntity = new HttpEntity<>(cityreqstr, headers);
		
		
		ResponseEntity<String> responseentity = resttemplate.exchange(configs.getCramerBaseUrl()+configs.getCityEndpoint(), HttpMethod.POST,
				requestEntity, String.class);
		String response = responseentity.getBody();

		List<City> citylist=parsecityResponse(response);
		
		return citylist;
	}
	
	private List<City> parsecityResponse(String response) {
		List<City> citylist=new ArrayList<>();
		
		try {

			JSONObject json=XML.toJSONObject(response);
		    JSONObject envelopjson=json.getJSONObject("env:Envelope");

		    JSONObject bodyjson=envelopjson.getJSONObject("env:Body");
		    JSONObject cityListJson=bodyjson.getJSONObject("ns2:getCityListResponse");
		    JSONObject responsejson=cityListJson.getJSONObject("response");
		    try {
				JSONArray cityjsonarr = responsejson.getJSONArray("result");


				for(int i=0;i<cityjsonarr.length();i++) {

					JSONObject stateJsonobj=cityjsonarr.getJSONObject(i);

					String cityName=stateJsonobj.getString("cityName");
					String cityCode=stateJsonobj.getString("cityCode");
					City city=new City(cityCode, cityName);

					citylist.add(city);

				}

			}catch (Exception ex) {
				JSONObject stateJsonobj=responsejson.getJSONObject("result");

				String cityName=stateJsonobj.getString("cityName");
				String cityCode=stateJsonobj.getString("cityCode");
				City city=new City(cityCode, cityName);

				citylist.add(city);
			}


		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return citylist;
	}
	
	public List<Area> getAreaList(String cityName) {
		
		HttpHeaders headers = new HttpHeaders();
		//headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
		headers.set("Content-Type", "text/xml;charset=UTF-8");
		headers.set("Accept-Encoding", "gzip,deflate");
		headers.set("SOAPAction", "");
		
		
		String areareq=CramerConstants.areareq;
		
		Object params[]= {cityName};
		
		String areareqstr=MessageFormat.format(areareq, params);
		
		System.out.println(areareqstr);
		HttpEntity<String> requestEntity = new HttpEntity<>(areareqstr, headers);
		
		
		ResponseEntity<String> responseentity = resttemplate.exchange(configs.getCramerBaseUrl()+configs.getAreaEndpoint(), HttpMethod.POST,
				requestEntity, String.class);
		String response = responseentity.getBody();

		List<Area> arealist=parseareaResponse(response);
		
		return arealist;
	}
	
	private List<Area> parseareaResponse(String response) {
		List<Area> arealist=new ArrayList<>();
		
		try {
		
			JSONObject json=XML.toJSONObject(response);
		    JSONObject envelopjson=json.getJSONObject("env:Envelope");
		    
		    JSONObject bodyjson=envelopjson.getJSONObject("env:Body");
		    JSONObject areaListJson=bodyjson.getJSONObject("ns2:getArea4CityByNameResponse");
		    JSONObject responsejson=areaListJson.getJSONObject("response");
		    
		    JSONArray areajsonarr=responsejson.getJSONArray("areas");
		    
		    for(int i=0;i<areajsonarr.length();i++) {
		    	
		    	JSONObject areaJsonobj=areajsonarr.getJSONObject(i);
		    	
		    	String areaId=areaJsonobj.getString("areaId");
		    	String areaName=areaJsonobj.getString("areaName");
		    	Area area=new Area(areaId, areaName);
		    	
		    	arealist.add(area);
		
		    }
		    
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return arealist;
	}

	public List<IsDeviceExist> getisdeviceexistsList(String deviceName) {
		
		HttpHeaders headers = new HttpHeaders();
		//headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
		headers.set("Content-Type", "text/xml;charset=UTF-8");
		headers.set("Accept-Encoding", "gzip,deflate");
		headers.set("SOAPAction", "");
		
		
		String isdevicenamereq=CramerConstants.isdeviceexistsendpointreq;
		
		Object params[]= {deviceName};
		
		String isdevicereqstr=MessageFormat.format(isdevicenamereq, params);
		
		System.out.println(isdevicereqstr);
		HttpEntity<String> requestEntity = new HttpEntity<>(isdevicereqstr, headers);
		
		
		ResponseEntity<String> responseentity = resttemplate.exchange(configs.getCramerBaseUrl()+configs.getIsDeviceExistsEndpoint(), HttpMethod.POST,
				requestEntity, String.class);
		String response = responseentity.getBody();

		List<IsDeviceExist> isdeviceexixtslist=parseisdeviceexixtsResponse(response);
		
		return isdeviceexixtslist;
	}
	
	private List<IsDeviceExist> parseisdeviceexixtsResponse(String response) {
		List<IsDeviceExist> isDevicelist=new ArrayList<>();

		try {

			JSONObject json=XML.toJSONObject(response);
			JSONObject envelopjson=json.getJSONObject("env:Envelope");

			JSONObject bodyjson=envelopjson.getJSONObject("env:Body");
			JSONObject isDeviceListJson=bodyjson.getJSONObject("ns2:isDeviceExistsResponse");
			JSONObject responsejson=isDeviceListJson.getJSONObject("response");

			JSONObject isDevicejsonarr=responsejson.getJSONObject("results");


			String deviceName = isDevicejsonarr.getString("deviceName");
			String nodeAlias1=isDevicejsonarr.getString("nodeAlias1");
			String nodeAlias2=isDevicejsonarr.getString("nodeAlias2");
			String nodeType=isDevicejsonarr.getString("nodeType");
			String nodeDef=isDevicejsonarr.getString("nodeDef");
			String locationType=isDevicejsonarr.getString("locationType");
			String buildingName=isDevicejsonarr.getString("buildingName");
			String areaName=isDevicejsonarr.getString("areaName");
			String cityName=isDevicejsonarr.getString("cityName");
			String stateName=isDevicejsonarr.getString("stateName");
			String countryName=isDevicejsonarr.getString("countryName");
			String address=isDevicejsonarr.getString("address");
			String lOB=isDevicejsonarr.getString("LOB");

			IsDeviceExist isDeviceExist=new IsDeviceExist(deviceName, nodeAlias1, nodeAlias2, nodeType,
					nodeDef, locationType,buildingName, areaName, cityName, stateName, countryName, address, lOB);

			isDevicelist.add(isDeviceExist);



		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return isDevicelist;
	}
	
public List<Building4area> getbuilding4areaList(String areaId) {
		
		HttpHeaders headers = new HttpHeaders();
		//headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
		headers.set("Content-Type", "text/xml;charset=UTF-8");
		headers.set("Accept-Encoding", "gzip,deflate");
		headers.set("SOAPAction", "");
		
		
		String building4areanamereq=CramerConstants.building4areareq;
		
		Object params[]= {areaId};
		
		String building4areareqstr=MessageFormat.format(building4areanamereq, params);
		
		System.out.println(building4areareqstr);
		HttpEntity<String> requestEntity = new HttpEntity<>(building4areareqstr, headers);
		
		
		ResponseEntity<String> responseentity = resttemplate.exchange(configs.getCramerBaseUrl()+configs.getBuilding4AreaEndpoint(), HttpMethod.POST,
				requestEntity, String.class);
		String response = responseentity.getBody();

		List<Building4area> building4arealist=parsebuilding4areaResponse(response);
		
		return building4arealist;
	}

	private List<Building4area> parsebuilding4areaResponse(String response) {
		List<Building4area> building4arealist=new ArrayList<>();

		try {

			JSONObject json=XML.toJSONObject(response);
			JSONObject envelopjson=json.getJSONObject("env:Envelope");

			JSONObject bodyjson=envelopjson.getJSONObject("env:Body");
			JSONObject Building4areaListJson=bodyjson.getJSONObject("ns2:getBuilding4AreaByIdResponse");
			JSONObject responsejson=Building4areaListJson.getJSONObject("response");
			try {
				JSONArray building4areaarr = responsejson.getJSONArray("buildings");

				for(int i=0;i<building4areaarr.length();i++) {

					JSONObject building4areaJsonobj=building4areaarr.getJSONObject(i);

					String buildingId=building4areaJsonobj.getString("buildingId");
					String buildingName=building4areaJsonobj.getString("buildingName");
					Building4area building4area=new Building4area(buildingId, buildingName);

					building4arealist.add(building4area);

				}

			}catch (Exception ex) {
				JSONObject stateJsonobj=responsejson.getJSONObject("buildings");

				String buildingId=stateJsonobj.getString("buildingId");
				String buildingName=stateJsonobj.getString("buildingName");
				Building4area building4area=new Building4area(buildingId, buildingName);

				building4arealist.add(building4area);
			}

		}catch(Exception ex) {
			ex.printStackTrace();
		}

		return building4arealist;
	}

	/*public List<CreateObject> createObject(String orderId, String orderType, String technology) {

		List<CreateObject> createObjectList=new ArrayList<>();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "text/xml;charset=UTF-8");
		headers.set("SOAPAction", "");

		HttpEntity<String> requestEntity = new HttpEntity<>(CramerConstants.createObject, headers);

		ResponseEntity<String> responseentity =
				resttemplate.exchange(CramerConstants.createObject, HttpMethod.POST,requestEntity, String.class);
		String response=responseentity.getBody();


		String createObjectResponse = String.valueOf(parseResponse(response));

		return createObjectList;
	}*/

	/*public Boolean createObject(String orderId, String orderType, String technology) {

		List<CreateObject> createObjectList=new ArrayList<>();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "text/xml;charset=UTF-8");
		headers.set("SOAPAction", "");

		HttpEntity<String> requestEntity = new HttpEntity<>(CramerConstants.createObject, headers);

		ResponseEntity<String> responseentity =
				resttemplate.exchange(CramerConstants.createObjectendpoint, HttpMethod.POST,requestEntity, String.class);

		String response=responseentity.getBody();
		System.out.println("==================" + response);

		String createObjectResponse = String.valueOf(parseResponse(response));

		return true;
	}*/
	public Boolean createObject(String orderId, String orderType, String technology) {

		List<CreateObject> createObjectList=new ArrayList<>();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "text/xml;charset=UTF-8");
		headers.set("SOAPAction", "");
		String createResult=CramerConstants.createObject;
		Object params[]={orderId,orderType,technology};
		String createResultStr=MessageFormat.format(createResult, params);
		System.out.println("============== Result Str "+createResultStr);

		HttpEntity<String> requestEntity = new HttpEntity<>(createResultStr, headers);

		try {
			ResponseEntity<String> responseentity =
					resttemplate.exchange(configs.getCramerBaseUrl()+configs.getCreateObjectEndpoint(), HttpMethod.POST, requestEntity, String.class);
			String response = responseentity.getBody();
			System.out.println("==================" + response);
			//String createObjectResponse = String.valueOf(parseResponses(response));
			List<CreateObject> createObjectResponse = parseResponses(response);
			System.out.println("=========" + createObjectResponse);
		}catch (Exception ex){
			ex.printStackTrace();
		}

		return true;
	}
	private List<CreateObject> parseResponses(String response) {

		List<CreateObject> createObjectList=new ArrayList<>();

		try {

			JSONObject json=XML.toJSONObject(response);
			JSONObject envelopjson=json.getJSONObject("env:Envelope");

			JSONObject bodyjson=envelopjson.getJSONObject("env:Body");
			JSONObject createObjectJson=bodyjson.getJSONObject("ns2:getCreateObjectList");
			JSONObject responsejson=createObjectJson.getJSONObject("response");

			JSONArray createObjecjsonarr=responsejson.getJSONArray("CreateObject");

			for(int i=0;i<createObjecjsonarr.length();i++) {

				JSONObject createObjecjJsonobj=createObjecjsonarr.getJSONObject(i);

				String createObjectResult=createObjecjJsonobj.getString("createObjectResult");

				CreateObject createObject=new CreateObject();

				createObjectList.add(createObject);

			}

		}catch(Exception ex) {
			ex.printStackTrace();
		}

		return createObjectList;
	}

	public Boolean updateObject(String orderId, String orderType, String provisioningStatus) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "text/xml;charset=UTF-8");
		headers.set("SOAPAction", "");
		String updateResult=CramerConstants.updateObject;
		Object params[]={orderId,orderType,provisioningStatus};
		String updateResultStr=MessageFormat.format(updateResult, params);

		HttpEntity<String> requestEntity = new HttpEntity<>(updateResultStr, headers);

		ResponseEntity<String> responseentity =
				resttemplate.exchange(configs.getCramerBaseUrl()+configs.getUpdateObjectEndpoint(), HttpMethod.POST,requestEntity, String.class);
		String response=responseentity.getBody();
		System.out.println("==================" + response);
		//String createObjectResponse = String.valueOf(parseResponses(response));
		List<CreateObject> updateObjectResponse = parseUpdateResponses(response);
		System.out.println("========="+updateObjectResponse);

		return true;
	}

	private List<CreateObject> parseUpdateResponses(String response) {
		List<CreateObject> createObjectList=new ArrayList<>();

		try {

			JSONObject json=XML.toJSONObject(response);
			JSONObject envelopjson=json.getJSONObject("env:Envelope");

			JSONObject bodyjson=envelopjson.getJSONObject("env:Body");
			JSONObject createObjectJson=bodyjson.getJSONObject("ns2:getUpdateObjectList");
			JSONObject responsejson=createObjectJson.getJSONObject("response");

			JSONArray createObjecjsonarr=responsejson.getJSONArray("UpdateObject");

			for(int i=0;i<createObjecjsonarr.length();i++) {

				JSONObject createObjecjJsonobj=createObjecjsonarr.getJSONObject(i);

				String createObjectResult=createObjecjJsonobj.getString("updateObjectResult");

				CreateObject createObject=new CreateObject();
				createObject.setCreateObjectResult(createObjectResult);
				createObjectList.add(createObject);

			}

		}catch(Exception ex) {
			ex.printStackTrace();
		}

		return createObjectList;
	}

	public SupportedCardTypeResult getSupportCardTypes(String nodeName) {
		SupportedCardTypeResult result = new SupportedCardTypeResult();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "text/xml;charset=UTF-8");
		headers.set("SOAPAction", "");
		String getSupportedCardType=CramerConstants.getSupportedCardType;
		String nodeNameStr = "";
		if(!StringUtils.isEmpty(nodeName)) {
			nodeNameStr = String.format("<!--Optional:--><nodeName>%s</nodeName>", nodeName);
		}
		String supportedTypeStr=MessageFormat.format(getSupportedCardType, nodeNameStr);

		HttpEntity<String> requestEntity = new HttpEntity<>(supportedTypeStr, headers);

		ResponseEntity<String> responseentity =
				resttemplate.exchange(configs.getCramerBaseUrl()+configs.getGetSupportedCardTypeEndpoint(), HttpMethod.POST,requestEntity, String.class);
		if(responseentity.getStatusCodeValue()!=200) {
			throw new RuntimeException("Cannot get card type response from url");
		}
		String response=responseentity.getBody();

		//XML parsing of response.
		try {
			JSONObject respObj = XML.toJSONObject(response);
			JSONObject envelopjson=respObj.getJSONObject("env:Envelope");

			JSONObject bodyjson=envelopjson.getJSONObject("env:Body");
			JSONObject getSupportedCardTypes4NodeResponse=bodyjson.getJSONObject("ns2:getSupportedCardTypes4NodeResponse");
			JSONObject responsejson=getSupportedCardTypes4NodeResponse.getJSONObject("response");

			JSONArray cardTypeJsonArr=responsejson.getJSONArray("cardType");
			List<String> cardTypeList = new ArrayList<>();
			for(int i = 0; i<cardTypeJsonArr.length(); i++) {
				String cardType=cardTypeJsonArr.get(i).toString();
				cardTypeList.add(cardType);
			}
			result.setCardTypes(cardTypeList);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return result;
	}
}
