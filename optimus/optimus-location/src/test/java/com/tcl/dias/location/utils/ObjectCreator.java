package com.tcl.dias.location.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.tcl.dias.location.beans.MstCityBean;
import com.tcl.dias.location.beans.SiteBean;
import com.tcl.dias.location.beans.SolutionBean;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.CustomerLeLocationBean;
import com.tcl.dias.common.beans.DemarcationBean;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.LocationItContact;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.location.beans.DemarcationAndItContactBean;
import com.tcl.dias.location.beans.LocationDetails;
import com.tcl.dias.location.beans.LocationOfferingDetail;
import com.tcl.dias.location.constants.LocationUploadConstants;
import com.tcl.dias.location.constants.PincodeConstants;
import com.tcl.dias.location.entity.entities.CustomerLocation;
import com.tcl.dias.location.entity.entities.CustomerSiteLocationItContact;
import com.tcl.dias.location.entity.entities.Demarcation;
import com.tcl.dias.location.entity.entities.Location;
import com.tcl.dias.location.entity.entities.LocationLeCustomer;
import com.tcl.dias.location.entity.entities.MstAddress;
import com.tcl.dias.location.entity.entities.MstCity;
import com.tcl.dias.location.entity.entities.MstCountry;
import com.tcl.dias.location.entity.entities.MstLocality;
import com.tcl.dias.location.entity.entities.MstPincode;
import com.tcl.dias.location.entity.entities.MstState;

/**
 * 
 * This file contains the ObjectCreator.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ObjectCreator {

	/**
	 * getMstCountryEntity
	 */
	public MstCountry getMstCountryEntity() {
		MstCountry mstCountry = new MstCountry();
		mstCountry.setCode("IND");
		mstCountry.setId(1);
		mstCountry.setName("INDIA");
		return mstCountry;
	}

	public UserInformation getUserInformation() {
		UserInformation userInformation = new UserInformation();
		userInformation.setUserId("admin");
		userInformation.setCustomers(getCustomerList());
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
		cd.setCustomerLeId(25);
		return cd;

	}

	/**
	 * getPincode
	 */
	public List<Map<String, Object>> getPincodeRepo() {
		List<Map<String, Object>> pinMapper = new ArrayList<>();
		Map<String, Object> object = new HashMap<>();
		object.put(PincodeConstants.PINCODE.toString(), "60002");
		object.put(PincodeConstants.CITY.toString(), "Chennai");
		object.put(PincodeConstants.COUNTRY.toString(), "INDIA");
		object.put(PincodeConstants.LOCALITY.toString(), "GUINDY");
		object.put(PincodeConstants.STATE.toString(), "TAMILNADU");
		object.put(PincodeConstants.CITY_ID.toString(), 1);
		pinMapper.add(object);
		return pinMapper;
	}


	/**
	 * getMstCity
	 */
	public MstCity getMstCity() {
		MstCity mstCity = new MstCity();
		mstCity.setCode("CHN");
		mstCity.setId(1);
		mstCity.setName("CHENNAI");
		mstCity.setMstState(getMstState());
		return mstCity;
	}

	/**
	 * getState
	 */
	public MstState getMstState() {
		MstState mstState = new MstState();
		mstState.setId(1);
		mstState.setMstCountry(getMstCountryEntity());
		mstState.setName("TAMILNADU");
		return mstState;
	}
	
	public List<MstState> getMstStates(){
		List<MstState> states =new ArrayList<>();
		
		states.add(getMstState());
		
		return states;
	}
	
	public List<MstCity> getMstCitys(){
		List<MstCity> states =new ArrayList<>();
		
		states.add(getMstCity());
		
		return states;
	}

	/**
	 * getMstPincode
	 */
	public MstPincode getMstPincode() {
		MstPincode mstPincode = new MstPincode();
		mstPincode.setCode("60002");
		mstPincode.setId(1);
		mstPincode.setMstCity(getMstCity());
		return mstPincode;
	}

	public List<MstPincode> getMstPincodeList() {
		List<MstPincode> mstAdd = new ArrayList<>();
		MstPincode mstPincode = new MstPincode();
		mstPincode.setCode("60002");
		mstPincode.setId(1);
		mstPincode.setMstCity(getMstCity());
		mstAdd.add(mstPincode);
		return mstAdd;
	}

	/**
	 * getMstAddress
	 */
	public MstAddress getMstAddress() {
		MstAddress mstAddress = new MstAddress();
		mstAddress.setId(1);
		mstAddress.setAddressLineOne("Chennai JTP");
		mstAddress.setAddressLineTwo("Nandambakkam");
		mstAddress.setCity("1");
		mstAddress.setCountry("1");
		mstAddress.setLocality("1");
		mstAddress.setPincode("1");
		mstAddress.setPlotBuilding("Chennai-89");
		mstAddress.setSource("Source");
		mstAddress.setState("1");
		return mstAddress;
	}

	/**
	 * getMstLocality
	 */
	public MstLocality getMstLocality() {
		MstLocality mstLocality = new MstLocality();
		mstLocality.setCode("GND");
		mstLocality.setMstCity(getMstCity());
		mstLocality.setName("GUINDY");
		return mstLocality;
	}

	public List<MstLocality> getMstLocalityList() {
		List<MstLocality> locList = new ArrayList<>();
		MstLocality mstLocality = new MstLocality();
		mstLocality.setCode("GND");
		mstLocality.setMstCity(getMstCity());
		mstLocality.setName("GUINDY");
		locList.add(mstLocality);
		return locList;
	}

	/**
	 * getCustomerLocation
	 */
	public CustomerLocation getCustomerLocation() {
		CustomerLocation customerLocation = new CustomerLocation();
		customerLocation.setErfCusCustomerId(1);
		customerLocation.setId(1);
		customerLocation.setLocation(getLocation());
		return customerLocation;
	}

	/**
	 * getCustomerLocation
	 */
	public Optional<CustomerLocation> getCustomerLocationOptional() {
		CustomerLocation customerLocation = new CustomerLocation();
		customerLocation.setErfCusCustomerId(1);
		customerLocation.setId(1);
		customerLocation.setLocation(getLocation());
		return Optional.of(customerLocation);
	}

	/**
	 * getLocation
	 */
	public Location getLocation() {
		Location location = new Location();
		location.setAddressId(1);
		location.setApiAddressId(2);
		location.setId(1);
		location.setLatLong("5,6");
		return location;
	}

	public List<Location> getLocations() {
		List<Location> locations = new ArrayList<>();
		Location location = new Location();
		location.setAddressId(1);
		location.setApiAddressId(2);
		location.setId(1);
		location.setLatLong("5,6");
		locations.add(location);
		locations.add(location);
		return locations;
	}

	/**
	 * 
	 * getLocationDetail
	 * 
	 * @param locationId
	 * @return
	 */
	public LocationDetail getLocationDetail(Integer locationId) {
		LocationDetail locationDetail = new LocationDetail();
		AddressDetail userAddress = new AddressDetail();
		userAddress.setAddressLineOne("No 3 ,Jayanth Tech park");
		userAddress.setAddressLineTwo("Chennai Trade center opposite");
		userAddress.setCity("Chennai");
		userAddress.setCountry("INDIA");
		userAddress.setLocality("OMR");
		userAddress.setPlotBuilding("");
		userAddress.setPincode("60002");
		userAddress.setState("TAMILNADU");
		userAddress.setLocality("CHENNAI");
		AddressDetail apiAddress = new AddressDetail();
		apiAddress.setAddressLineOne("No 3 ,Jayanth Tech park");
		apiAddress.setAddressLineTwo("Chennai Trade center opposite");
		apiAddress.setCity("Chennai");
		apiAddress.setCountry("INDIA");
		apiAddress.setLocality("OMR");
		apiAddress.setPincode("60002");
		userAddress.setPlotBuilding("");
		apiAddress.setLatLong("5,8");
		apiAddress.setState("TAMILNADU");
		apiAddress.setLocality("CHENNAI");
		locationDetail.setApiAddress(apiAddress);
		locationDetail.setUserAddress(userAddress);
		locationDetail.setLocationId(locationId);
		locationDetail.setCustomerId(1);
		return locationDetail;
	}
	
	public List<LocationDetail> getLocationDetailList(){
		List<LocationDetail> locationDetailList = new ArrayList<LocationDetail>();
		locationDetailList.add(getLocationDetail(1));
		return locationDetailList;
	}
	/**
	 * 
	 * getLocationDetail- Without API Addredd
	 * 
	 * @param locationId
	 * @return
	 */
	public LocationDetail getLocationDetailWithoutApiAddress(Integer locationId) {
		LocationDetail locationDetail = new LocationDetail();
		AddressDetail userAddress = new AddressDetail();
		userAddress.setAddressLineOne("No 3 ,Jayanth Tech park");
		userAddress.setAddressLineTwo("Chennai Trade center opposite");
		userAddress.setCity("Chennai");
		userAddress.setCountry("INDIA");
		userAddress.setLocality("OMR");
		userAddress.setPlotBuilding("");
		userAddress.setPincode("60002");
		userAddress.setState("TAMILNADU");
		AddressDetail apiAddress = new AddressDetail();
		apiAddress.setAddressLineOne("No 3 ,Jayanth Tech park");
		apiAddress.setAddressLineTwo("Chennai Trade center opposite");
		apiAddress.setCity("Chennai");
		apiAddress.setCountry("INDIA");
		apiAddress.setLocality("OMR");
		apiAddress.setPincode("60002");
		userAddress.setPlotBuilding("");
		apiAddress.setLatLong("5,8");
		apiAddress.setState("TAMILNADU");
		locationDetail.setUserAddress(userAddress);
		locationDetail.setLocationId(locationId);
		locationDetail.setCustomerId(1);
		return locationDetail;
	}

	/**
	 * 
	 * getLocationDetail
	 * 
	 * @param locationId
	 * @return
	 */
	public LocationDetail getLocationDetailWithoutUserAddress(Integer locationId) {
		LocationDetail locationDetail = new LocationDetail();
		AddressDetail apiAddress = new AddressDetail();
		apiAddress.setAddressLineOne("No 3 ,Jayanth Tech park");
		apiAddress.setAddressLineTwo("Chennai Trade center opposite");
		apiAddress.setCity("Chennai");
		apiAddress.setCountry("INDIA");
		apiAddress.setLocality("OMR");
		apiAddress.setPincode("60002");
		apiAddress.setLatLong("5,8");
		apiAddress.setState("TAMILNADU");
		apiAddress.setLocality("CHENNAI");
		locationDetail.setApiAddress(apiAddress);
		locationDetail.setLocationId(locationId);
		locationDetail.setCustomerId(1);
		return locationDetail;
	}

	/**
	 * 
	 * getLocationDetail
	 * 
	 * @param locationId
	 * @return
	 */
	public LocationDetail getLocationDetailWithoutLocality(Integer locationId) {
		LocationDetail locationDetail = new LocationDetail();
		AddressDetail userAddress = new AddressDetail();
		userAddress.setAddressLineOne("No 3 ,Jayanth Tech park");
		userAddress.setAddressLineTwo("Chennai Trade center opposite");
		userAddress.setCity("Chennai");
		userAddress.setCountry("INDIA");
		userAddress.setLocality("OMR");
		userAddress.setPlotBuilding("");
		userAddress.setPincode("60002");
		userAddress.setState("TAMILNADU");
		userAddress.setLocality(null);
		AddressDetail apiAddress = new AddressDetail();
		apiAddress.setAddressLineOne("No 3 ,Jayanth Tech park");
		apiAddress.setAddressLineTwo("Chennai Trade center opposite");
		apiAddress.setCity("Chennai");
		apiAddress.setCountry("INDIA");
		apiAddress.setLocality("OMR");
		apiAddress.setPincode("60002");
		userAddress.setPlotBuilding("");
		apiAddress.setLatLong("5,8");
		apiAddress.setState("TAMILNADU");
		apiAddress.setLocality(null);
		locationDetail.setApiAddress(apiAddress);
		locationDetail.setUserAddress(userAddress);
		locationDetail.setLocationId(locationId);
		locationDetail.setCustomerId(1);
		return locationDetail;
	}

	public List<LocationItContact> getLocationItContactList() {
		List<LocationItContact> list = new ArrayList<>();
		LocationItContact locationItContact = new LocationItContact();
		locationItContact.setContactNo("9900990099");
		locationItContact.setEmail("abc@tatacommunications.com");
		locationItContact.setName("India");
		locationItContact.setLocationId(1);
		locationItContact.setErfCustomerLeId(1);
		list.add(locationItContact);
		return list;
	}

	public Optional<CustomerSiteLocationItContact> getCustomerLocationOptionalValue() {

		CustomerSiteLocationItContact contact = new CustomerSiteLocationItContact();
		contact.setContactNumber("3453534534");
		contact.setCustomerLocation(getCustomerLocation());
		contact.setEmailId("abc@tatacommunications.com");
		contact.setId(1);
		contact.setName("Site 1");
		return Optional.of(contact);
	}

	public LocationItContact getLocationItContact() {
		LocationItContact locationItContact = new LocationItContact();
		locationItContact.setContactNo("9900990099");
		locationItContact.setEmail("abc@tatacommunications.com");
		locationItContact.setName("India");
		locationItContact.setLocationId(1);
		locationItContact.setErfCustomerLeId(1);
		return locationItContact;
	}

	public Optional<LocationLeCustomer> returnLocationLeCustomer() {
		LocationLeCustomer locationLeCustomer = new LocationLeCustomer();
		locationLeCustomer.setErfCusCustomerLeId(1);
		locationLeCustomer.setId(1);
		return Optional.of(locationLeCustomer);
	}

	public LocationItContact getLocationItContactForNull() {
		LocationItContact locationItContact = new LocationItContact();
		locationItContact.setContactNo("9900990099");
		locationItContact.setEmail("abc@tatacommunications.com");
		locationItContact.setName("India");
		locationItContact.setLocationId(null);
		locationItContact.setErfCustomerLeId(1);
		return locationItContact;
	}

	public LocationItContact getLocationItContactForNameNull() {
		LocationItContact locationItContact = new LocationItContact();
		locationItContact.setContactNo("9900990099");
		locationItContact.setEmail("abc@tatacommunications.com");
		locationItContact.setName(null);
		locationItContact.setLocationId(1);
		locationItContact.setErfCustomerLeId(1);
		return locationItContact;
	}

	public LocationItContact getLocationItContactForEmailNull() {
		LocationItContact locationItContact = new LocationItContact();
		locationItContact.setContactNo("9900990099");
		locationItContact.setEmail(null);
		locationItContact.setName("India");
		locationItContact.setLocationId(1);
		locationItContact.setErfCustomerLeId(1);
		return locationItContact;
	}

	public LocationItContact getLocationItContactForContactNull() {
		LocationItContact locationItContact = new LocationItContact();
		locationItContact.setContactNo(null);
		locationItContact.setEmail("abc@tatacommunications.com");
		locationItContact.setName("India");
		locationItContact.setLocationId(1);
		locationItContact.setErfCustomerLeId(1);
		return locationItContact;
	}

	public LocationItContact getLocationItContactForErfCustomerNull() {
		LocationItContact locationItContact = new LocationItContact();
		locationItContact.setContactNo(null);
		locationItContact.setEmail("abc@tatacommunications.com");
		locationItContact.setName("India");
		locationItContact.setLocationId(1);
		locationItContact.setErfCustomerLeId(null);
		return locationItContact;
	}

	/**
	 * getCustomerLocationList
	 * 
	 * @return
	 */
	public List<CustomerSiteLocationItContact> getCustomerLocationList() {

		List<CustomerSiteLocationItContact> customerLocationList = new ArrayList<>();
		CustomerSiteLocationItContact contact = new CustomerSiteLocationItContact();
		contact.setContactNumber("3453534534");
		contact.setCustomerLocation(getCustomerLocation());
		contact.setEmailId("abc@tatacommunications.com");
		contact.setId(1);
		contact.setName("Site 1");
		customerLocationList.add(contact);
		return customerLocationList;
	}

	/**
	 * getCustomerLocationList2
	 * 
	 * @return
	 */
	public List<CustomerSiteLocationItContact> getCustomerLocationList2() {

		List<CustomerSiteLocationItContact> customerLocationList = new ArrayList<>();
		CustomerSiteLocationItContact contact = new CustomerSiteLocationItContact();
		contact.setContactNumber("3453534534");
		contact.setCustomerLocation(null);
		contact.setEmailId("abc@tatacommunications.com");
		contact.setId(null);
		contact.setName("Site 1");
		customerLocationList.add(contact);
		return customerLocationList;
	}

	/**
	 * createLocationLeCustomer
	 * 
	 * @return
	 */
	public List<LocationLeCustomer> createLocationLeCustomer() {
		List<LocationLeCustomer> locationLeCustList = new ArrayList<LocationLeCustomer>();
		LocationLeCustomer locationLeCust = new LocationLeCustomer();
		locationLeCust.setCustomerSiteLocationItContacts(
				new HashSet<CustomerSiteLocationItContact>(getCustomerLocationList2()));
		Location loc = new Location();
		loc.setId(1);
		loc.setAddressId(1);
		locationLeCust.setLocation(loc);
		locationLeCust.setErfCusCustomerLeId(1);
		locationLeCust.setId(1);
		locationLeCustList.add(locationLeCust);
		
		return locationLeCustList;
	}

	/**
	 * createLocationLeCustomerWithoutLocation
	 * 
	 * @return
	 */
	public List<LocationLeCustomer> createLocationLeCustomerWithoutLocation() {
		List<LocationLeCustomer> locationLeCustList = new ArrayList<LocationLeCustomer>();
		LocationLeCustomer locationLeCust = new LocationLeCustomer();
		locationLeCust.setCustomerSiteLocationItContacts(
				new HashSet<CustomerSiteLocationItContact>(getCustomerLocationList2()));
		locationLeCust.setErfCusCustomerLeId(1);
		locationLeCustList.add(locationLeCust);
		return locationLeCustList;
	}

	/**
	 * createLocationLeCustomerWithAddress
	 * 
	 * @return
	 */
	public List<LocationLeCustomer> createLocationLeCustomerWithAddress() {
		List<LocationLeCustomer> locationLeCustList = new ArrayList<LocationLeCustomer>();
		LocationLeCustomer locationLeCust = new LocationLeCustomer();
		locationLeCust.setCustomerSiteLocationItContacts(
				new HashSet<CustomerSiteLocationItContact>(getCustomerLocationList2()));
		Location loc = new Location();
		loc.setId(1);
		loc.setAddressId(1);
		locationLeCust.setLocation(loc);
		locationLeCust.setErfCusCustomerLeId(1);
		locationLeCustList.add(locationLeCust);
		return locationLeCustList;
	}

	/**
	 * getMstAddressList
	 * 
	 * @return
	 */
	public List<MstAddress> getMstAddressList() {
		List<MstAddress> mstAddrList = new ArrayList<MstAddress>();
		mstAddrList.add(getMstAddressWithAllDetails());
		return mstAddrList;
	}

	/**
	 * getMstAddress
	 */
	public MstAddress getMstAddressWithAllDetails() {
		MstAddress mstAddress = new MstAddress();
		mstAddress.setId(1);
		mstAddress.setAddressLineOne("line 1");
		mstAddress.setAddressLineTwo("line 2");
		mstAddress.setCity("1");
		mstAddress.setCountry("1");
		mstAddress.setLocality("1");
		mstAddress.setPincode("1");
		mstAddress.setPlotBuilding("building");
		mstAddress.setSource("manual");
		mstAddress.setState("1");
		return mstAddress;
	}

	public Optional<CustomerSiteLocationItContact> returnSiteLocation() {
		CustomerSiteLocationItContact siteLocation = new CustomerSiteLocationItContact();
		siteLocation.setId(1);
		siteLocation.setIsActive((byte) 1);
		siteLocation.setContactNumber("2342342341");
		siteLocation.setEmailId("sample@gmail.com");
		siteLocation.setName("Sample");
		return Optional.of(siteLocation);
	}

	/**
	 * getCustomerLocationList
	 * 
	 * @return
	 */
	public List<CustomerLocation> getCustomerLocationsList() {
		List<CustomerLocation> customerLocationList = new ArrayList<>();
		customerLocationList.add(getCustomerLocation());
		return customerLocationList;

	}
	
	public Set<Integer> createIntegerList(){
		Set<Integer> list=new HashSet<>();
		list.add(1);
		return list;
	}
	
	public DemarcationAndItContactBean createDemarcationAndItContactBean(){
		DemarcationAndItContactBean demarcationAndItContactBean=new DemarcationAndItContactBean();
		demarcationAndItContactBean.setContact(getLocationItContact());
		demarcationAndItContactBean.setDemarcation(createDemarcationBean());
		return demarcationAndItContactBean;
	}
	public DemarcationBean createDemarcationBean(){
		DemarcationBean demarcationBean=new DemarcationBean();
		demarcationBean.setId(1);
		demarcationBean.setLocationId(1);
		demarcationBean.setAppartment("jtp");
		demarcationBean.setBuildingAltitude("altitude");
		demarcationBean.setFloor("1");
		demarcationBean.setTower("tower");
		demarcationBean.setRoom("1");
		demarcationBean.setWing("wing");
		demarcationBean.setZone("zone");
		return demarcationBean;
	}
	
	/**
	 * getLocation
	 */
	public Location getLocationForDemarcation() {
		Location location = new Location();
		location.setAddressId(1);
		location.setApiAddressId(2);
		location.setId(1);
		location.setLatLong("5,6");
		location.setDemarcation(createDemarcation());
		return location;
	}
	
	public Demarcation createDemarcation(){
		Demarcation demarcationBean=new Demarcation();
		demarcationBean.setId(1);
		demarcationBean.setAppartment("jtp");
		demarcationBean.setBuildingAltitude("altitude");
		demarcationBean.setFloor("1");
		demarcationBean.setTower("tower");
		demarcationBean.setRoom("1");
		demarcationBean.setWing("wing");
		demarcationBean.setZone("zone");
		Set<Location> setLocation=new HashSet<>();
		setLocation.add(getLocation());
		demarcationBean.setLocations(setLocation);
		return demarcationBean;
	}
	
	public Demarcation createDemarcationWithoutLocation(){
		Demarcation demarcationBean=new Demarcation();
		demarcationBean.setId(1);
		demarcationBean.setAppartment("jtp");
		demarcationBean.setBuildingAltitude("altitude");
		demarcationBean.setFloor("1");
		demarcationBean.setTower("tower");
		demarcationBean.setRoom("1");
		demarcationBean.setWing("wing");
		demarcationBean.setZone("zone");
		return demarcationBean;
	}
	/**
	 * getLocation
	 */
	public Location getLocationForDemarcationWithoutLocation() {
		Location location = new Location();
		location.setAddressId(1);
		location.setApiAddressId(2);
		location.setId(1);
		location.setLatLong("5,6");
		location.setDemarcation(createDemarcationWithoutLocation());
		return location;
	}

	/**
	 * getLocation
	 */
	public CustomerLeLocationBean getCustomerLeLocationBean() {
		CustomerLeLocationBean customerLeLocationBean = new CustomerLeLocationBean();
		customerLeLocationBean.setErfCustomerLeId(1);
		List<Integer> listInteger=new ArrayList<>();
		listInteger.add(1);
		customerLeLocationBean.setLocationIds(listInteger);
		return customerLeLocationBean;
	}
	
	public CustomerSiteLocationItContact getCustomerSiteLocationItContact() {

		CustomerSiteLocationItContact contact = new CustomerSiteLocationItContact();
		contact.setContactNumber("3453534534");
		contact.setCustomerLocation(getCustomerLocation());
		contact.setEmailId("abc@tatacommunications.com");
		contact.setId(1);
		contact.setName("Site 1");
		return contact;
	}

	public byte[] getFileAsByteArray() throws IOException {
		List<String> countryList = new ArrayList<>();
		countryList.add("India");
		countryList.add("USA");
		List<String> profileLst = new ArrayList<>();
		profileLst.add("Profile 1");
		profileLst.add("Profile 2");
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(LocationUploadConstants.SITE_LOCATIONS);

		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue(LocationUploadConstants.HEADER_SERIAL_NO);
		header.createCell(1).setCellValue(LocationUploadConstants.HEADER_COUNTRY);
		header.createCell(2).setCellValue(LocationUploadConstants.HEADER_STATE);
		header.createCell(3).setCellValue(LocationUploadConstants.HEADER_CITY);
		header.createCell(4).setCellValue(LocationUploadConstants.HEADER_PINCODE);
		header.createCell(5).setCellValue(LocationUploadConstants.HEADER_LOCALITY);
		header.createCell(6).setCellValue(LocationUploadConstants.HEADER_ADDRESS);
		header.createCell(7).setCellValue(LocationUploadConstants.HEADER_PROFILES);

		
		Row contentRow = sheet.createRow(1);
		contentRow.createCell(0).setCellValue("1");
		contentRow.createCell(1).setCellValue("India");
		contentRow.createCell(2).setCellValue("Tamilnadu");
		contentRow.createCell(3).setCellValue("Chennai");
		contentRow.createCell(4).setCellValue("600089");
		contentRow.createCell(5).setCellValue("Nandambakkam");
		contentRow.createCell(6).setCellValue("Jeyant tech park, nandambakkam, chennai, 600089");
		contentRow.createCell(7).setCellValue("Single Unmanaged GVPN");
		
		for (int i = 0; i <= 7; i++) {
			CellStyle stylerowHeading = workbook.createCellStyle();
			stylerowHeading.setBorderBottom(BorderStyle.THICK);
			stylerowHeading.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
			stylerowHeading.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont font = workbook.createFont();
			font.setBold(true);
			font.setFontName(HSSFFont.FONT_ARIAL);
			font.setFontHeightInPoints((short) 10);
			font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
			stylerowHeading.setFont(font);
			stylerowHeading.setVerticalAlignment(VerticalAlignment.CENTER);
			stylerowHeading.setAlignment(HorizontalAlignment.CENTER);

			stylerowHeading.setWrapText(true);
			header.getCell(i).setCellStyle(stylerowHeading);

			DataFormat fmt = workbook.createDataFormat();
			CellStyle textStyle = workbook.createCellStyle();
			textStyle.setDataFormat(fmt.getFormat("@"));
			sheet.setDefaultColumnStyle(i, textStyle);
		}

		/* country validation begins */
		DataValidation countryDataValidation = null;
		DataValidationConstraint countryConstraint = null;
		DataValidationHelper countryValidationHelper = null;

		countryValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
		CellRangeAddressList countryAddressList = new CellRangeAddressList(1, 50, 1, 1);
		// populate with the list of countries from product catalog
		countryConstraint = countryValidationHelper
				.createExplicitListConstraint(profileLst.stream().toArray(String[]::new));
		countryDataValidation = countryValidationHelper.createValidation(countryConstraint, countryAddressList);
		countryDataValidation.setSuppressDropDownArrow(true);
		countryDataValidation.setShowErrorBox(true);
		countryDataValidation.setShowPromptBox(true);
		sheet.addValidationData(countryDataValidation);
		sheet.setColumnWidth(0, 1800);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 5000);
		sheet.setColumnWidth(4, 7500);
		sheet.setColumnWidth(5, 10000);
		sheet.setColumnWidth(6, 8000);

		/* profile validation begins */
		DataValidation profileDataValidation = null;
		DataValidationConstraint profileConstraint = null;
		DataValidationHelper profileValidationHelper = null;
		profileValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
		CellRangeAddressList profileAddressList = new CellRangeAddressList(1, 50, 6, 6);
		// add the selected profiles
		profileConstraint = profileValidationHelper
				.createExplicitListConstraint(countryList.stream().toArray(String[]::new));
		profileDataValidation = profileValidationHelper.createValidation(profileConstraint, profileAddressList);
		profileDataValidation.setSuppressDropDownArrow(true);
		profileDataValidation.setShowErrorBox(true);
		profileDataValidation.setShowPromptBox(true);
		sheet.addValidationData(profileDataValidation);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);

		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		workbook.write(outByteStream);
		byte[] outArray = outByteStream.toByteArray();
		return outArray;
	}
	
	
	public byte[] getFileAsByteArrayEmpty() throws IOException {
		List<String> countryList = new ArrayList<>();
		countryList.add("India");
		countryList.add("USA");
		List<String> profileLst = new ArrayList<>();
		profileLst.add("Profile 1");
		profileLst.add("Profile 2");
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(LocationUploadConstants.SITE_LOCATIONS);

		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue(LocationUploadConstants.HEADER_SERIAL_NO);
		header.createCell(1).setCellValue(LocationUploadConstants.HEADER_COUNTRY);
		header.createCell(2).setCellValue(LocationUploadConstants.HEADER_STATE);
		header.createCell(3).setCellValue(LocationUploadConstants.HEADER_CITY);
		header.createCell(4).setCellValue(LocationUploadConstants.HEADER_PINCODE);
		header.createCell(5).setCellValue(LocationUploadConstants.HEADER_LOCALITY);
		header.createCell(6).setCellValue(LocationUploadConstants.HEADER_ADDRESS);
		header.createCell(7).setCellValue(LocationUploadConstants.HEADER_PROFILES);

		Row emptyRow = sheet.createRow(1);
		emptyRow.createCell(0).setCellValue("");
		emptyRow.createCell(1).setCellValue("");
		emptyRow.createCell(2).setCellValue("");
		emptyRow.createCell(3).setCellValue("");
		emptyRow.createCell(4).setCellValue("");
		emptyRow.createCell(5).setCellValue("");
		emptyRow.createCell(7).setCellValue("");
		
		Row blankRow = sheet.createRow(2);
		blankRow.createCell(0).setCellValue("");
		blankRow.createCell(1).setCellValue("");
		blankRow.createCell(2).setCellValue("");
		blankRow.createCell(3).setCellValue("");
		blankRow.createCell(4).setCellValue("");
		blankRow.createCell(5).setCellValue("");
		blankRow.createCell(6).setCellValue("");
		blankRow.createCell(7).setCellValue("");
		
		
		for (int i = 0; i <= 7; i++) {
			CellStyle stylerowHeading = workbook.createCellStyle();
			stylerowHeading.setBorderBottom(BorderStyle.THICK);
			stylerowHeading.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
			stylerowHeading.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont font = workbook.createFont();
			font.setBold(true);
			font.setFontName(HSSFFont.FONT_ARIAL);
			font.setFontHeightInPoints((short) 10);
			font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
			stylerowHeading.setFont(font);
			stylerowHeading.setVerticalAlignment(VerticalAlignment.CENTER);
			stylerowHeading.setAlignment(HorizontalAlignment.CENTER);

			stylerowHeading.setWrapText(true);
			header.getCell(i).setCellStyle(stylerowHeading);

			DataFormat fmt = workbook.createDataFormat();
			CellStyle textStyle = workbook.createCellStyle();
			textStyle.setDataFormat(fmt.getFormat("@"));
			sheet.setDefaultColumnStyle(i, textStyle);
		}

		/* country validation begins */
		DataValidation countryDataValidation = null;
		DataValidationConstraint countryConstraint = null;
		DataValidationHelper countryValidationHelper = null;

		countryValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
		CellRangeAddressList countryAddressList = new CellRangeAddressList(1, 50, 1, 1);
		// populate with the list of countries from product catalog
		countryConstraint = countryValidationHelper
				.createExplicitListConstraint(profileLst.stream().toArray(String[]::new));
		countryDataValidation = countryValidationHelper.createValidation(countryConstraint, countryAddressList);
		countryDataValidation.setSuppressDropDownArrow(true);
		countryDataValidation.setShowErrorBox(true);
		countryDataValidation.setShowPromptBox(true);
		sheet.addValidationData(countryDataValidation);
		sheet.setColumnWidth(0, 1800);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 5000);
		sheet.setColumnWidth(4, 7500);
		sheet.setColumnWidth(5, 10000);
		sheet.setColumnWidth(6, 8000);

		/* profile validation begins */
		DataValidation profileDataValidation = null;
		DataValidationConstraint profileConstraint = null;
		DataValidationHelper profileValidationHelper = null;
		profileValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
		CellRangeAddressList profileAddressList = new CellRangeAddressList(1, 50, 6, 6);
		// add the selected profiles
		profileConstraint = profileValidationHelper
				.createExplicitListConstraint(countryList.stream().toArray(String[]::new));
		profileDataValidation = profileValidationHelper.createValidation(profileConstraint, profileAddressList);
		profileDataValidation.setSuppressDropDownArrow(true);
		profileDataValidation.setShowErrorBox(true);
		profileDataValidation.setShowPromptBox(true);
		sheet.addValidationData(profileDataValidation);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);

		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		workbook.write(outByteStream);
		byte[] outArray = outByteStream.toByteArray();
		return outArray;
	}

	public List<Map<String, Object>> getLocationsByLe() {
		List<Map<String, Object>> locationsByLe = new ArrayList<>();
		Map<String, Object> object = new HashMap<>();
		object.put("addressLineOne", "No. 33B Olympia Platina  9th Floor   Ambedkar Nagar");
		object.put("addressLineTwo", "Dhanakotti Raja Street	Dhanakotti Raja Street");
		object.put("city", "Chennai");
		object.put("state", "Tamil Nadu");
		object.put("country", "INDIA");
		object.put("locality", "Guindy");
		object.put("pincode", "600032");
		object.put("latLong", "13.013991 ,80.199964");
		object.put("locationId", 196);
		object.put("source", "system");
		locationsByLe.add(object);	 						 		
		return locationsByLe;
	}

	public List<CustomerDetail> getCustomerDetails() {
		UserInformation userInformation = new UserInformation();
		userInformation.setUserId("optimus_npl");
		CustomerDetail customer = new CustomerDetail();
		customer.setCustomerId(2);
		customer.setCustomerName("optimus_npl");
		customer.setErfCustomerId(2);
		customer.setCustomerLeId(1);
		customer.setStatus((byte) 1);
		List<CustomerDetail> custList = new ArrayList<>();
		custList.add(customer);
		userInformation.setCustomers(custList);
		return custList;
	}
	public List<Map<String, Object>> getLoclItContactsByLe() {
		List<Map<String, Object>> locationsByLe = new ArrayList<>();
		Map<String, Object> object = new HashMap<>();
		object.put("locationId", 1);
		object.put("email", "optimus@gmail.com");
		object.put("name", "optimus_regus");
		object.put("contactNo", "7898785678");
		object.put("erfCustomerLeId", 1);
		object.put("localItContactId", 10);
		locationsByLe.add(object);	 						 		
		return locationsByLe;
	}


	public List<MstCity> getMstCities() {
		return new ArrayList<MstCity>() {
			{
				add(getMstCity());
			}
		};
	}

	public List<MstCountry> getMstContryList() {
		List<MstCountry> mstContryList = new ArrayList<>();
		mstContryList.add(getMstCountryEntity());
		return mstContryList;

	}
	public LocationOfferingDetail getLocationOfferingDetail() {
		LocationOfferingDetail locationDetail = new LocationOfferingDetail();
		AddressDetail userAddress = new AddressDetail();
		userAddress.setAddressLineOne("No 3 ,Jayanth Tech park");
		userAddress.setAddressLineTwo("Chennai Trade center opposite");
		userAddress.setCity("Chennai");
		userAddress.setCountry("INDIA");
		userAddress.setLocality("OMR");
		userAddress.setPlotBuilding("");
		userAddress.setPincode("60002");
		userAddress.setState("TAMILNADU");
		userAddress.setLocality("CHENNAI");
		AddressDetail apiAddress = new AddressDetail();
		apiAddress.setAddressLineOne("No 3 ,Jayanth Tech park");
		apiAddress.setAddressLineTwo("Chennai Trade center opposite");
		apiAddress.setCity("Chennai");
		apiAddress.setCountry("INDIA");
		apiAddress.setLocality("OMR");
		apiAddress.setPincode("60002");
		userAddress.setPlotBuilding("");
		apiAddress.setLatLong("5,8");
		apiAddress.setState("TAMILNADU");
		apiAddress.setLocality("CHENNAI");
		locationDetail.setApiAddress(apiAddress);
		locationDetail.setUserAddress(userAddress);
		locationDetail.setLocationId(1);
		locationDetail.setCustomerId(1);
		return locationDetail;
	}
	

	public List<SolutionBean> getSolutionBeanList(){
		List<SolutionBean> solutions = new ArrayList<>();
		solutions.add(getSolution());
		return solutions;
	}
	
	public SolutionBean getSolution() {
		SolutionBean sol = new SolutionBean();
		sol.setId(1);
		
		SiteBean site = new SiteBean();
		site.setSiteId(1);
		site.setLocationId(1);
		List<SiteBean> sites = new ArrayList<>();
		sites.add(site);
		sol.setSites(sites);
		return sol;
	}

	public byte[] getFileAsBytArrayWithMissingCell() throws IOException{
		List<String> countryList = new ArrayList<>();
		countryList.add("India");
		countryList.add("USA");
		List<String> profileLst = new ArrayList<>();
		profileLst.add("Profile 1");
		profileLst.add("Profile 2");
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(LocationUploadConstants.SITE_LOCATIONS);

		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue(LocationUploadConstants.HEADER_SERIAL_NO);
		header.createCell(1).setCellValue(LocationUploadConstants.HEADER_COUNTRY);
		header.createCell(2).setCellValue(LocationUploadConstants.HEADER_STATE);
		header.createCell(3).setCellValue(LocationUploadConstants.HEADER_CITY);
		header.createCell(4).setCellValue(LocationUploadConstants.HEADER_PINCODE);
		header.createCell(5).setCellValue(LocationUploadConstants.HEADER_LOCALITY);
		header.createCell(6).setCellValue(LocationUploadConstants.HEADER_ADDRESS);
		header.createCell(7).setCellValue(LocationUploadConstants.HEADER_PROFILES);

		Row emptyRow = sheet.createRow(1);
		emptyRow.createCell(0).setCellValue("");
		emptyRow.createCell(1).setCellValue("");
		emptyRow.createCell(2).setCellValue("");
		emptyRow.createCell(3).setCellValue("");
		emptyRow.createCell(4).setCellValue("");
		emptyRow.createCell(5).setCellValue("");
		
		
		for (int i = 0; i <= 7; i++) {
			CellStyle stylerowHeading = workbook.createCellStyle();
			stylerowHeading.setBorderBottom(BorderStyle.THICK);
			stylerowHeading.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
			stylerowHeading.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont font = workbook.createFont();
			font.setBold(true);
			font.setFontName(HSSFFont.FONT_ARIAL);
			font.setFontHeightInPoints((short) 10);
			font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
			stylerowHeading.setFont(font);
			stylerowHeading.setVerticalAlignment(VerticalAlignment.CENTER);
			stylerowHeading.setAlignment(HorizontalAlignment.CENTER);

			stylerowHeading.setWrapText(true);
			header.getCell(i).setCellStyle(stylerowHeading);

			DataFormat fmt = workbook.createDataFormat();
			CellStyle textStyle = workbook.createCellStyle();
			textStyle.setDataFormat(fmt.getFormat("@"));
			sheet.setDefaultColumnStyle(i, textStyle);
		}

		/* country validation begins */
		DataValidation countryDataValidation = null;
		DataValidationConstraint countryConstraint = null;
		DataValidationHelper countryValidationHelper = null;

		countryValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
		CellRangeAddressList countryAddressList = new CellRangeAddressList(1, 50, 1, 1);
		// populate with the list of countries from product catalog
		countryConstraint = countryValidationHelper
				.createExplicitListConstraint(profileLst.stream().toArray(String[]::new));
		countryDataValidation = countryValidationHelper.createValidation(countryConstraint, countryAddressList);
		countryDataValidation.setSuppressDropDownArrow(true);
		countryDataValidation.setShowErrorBox(true);
		countryDataValidation.setShowPromptBox(true);
		sheet.addValidationData(countryDataValidation);
		sheet.setColumnWidth(0, 1800);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 5000);
		sheet.setColumnWidth(4, 7500);
		sheet.setColumnWidth(5, 10000);
		sheet.setColumnWidth(6, 8000);

		/* profile validation begins */
		DataValidation profileDataValidation = null;
		DataValidationConstraint profileConstraint = null;
		DataValidationHelper profileValidationHelper = null;
		profileValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
		CellRangeAddressList profileAddressList = new CellRangeAddressList(1, 50, 6, 6);
		// add the selected profiles
		profileConstraint = profileValidationHelper
				.createExplicitListConstraint(countryList.stream().toArray(String[]::new));
		profileDataValidation = profileValidationHelper.createValidation(profileConstraint, profileAddressList);
		profileDataValidation.setSuppressDropDownArrow(true);
		profileDataValidation.setShowErrorBox(true);
		profileDataValidation.setShowPromptBox(true);
		sheet.addValidationData(profileDataValidation);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);

		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		workbook.write(outByteStream);
		byte[] outArray = outByteStream.toByteArray();
		return outArray;
	}

	public LocationDetails getLocationDetails() {
		LocationDetails locationDetails = new LocationDetails();
		AddressDetail userAddress = new AddressDetail();
		userAddress.setAddressLineOne("No 3 ,Jayanth Tech park");
		userAddress.setAddressLineTwo("Chennai Trade center opposite");
		userAddress.setCity("Chennai");
		userAddress.setCountry("INDIA");
		userAddress.setLocality("OMR");
		userAddress.setPlotBuilding("");
		userAddress.setPincode("60002");
		userAddress.setState("TAMILNADU");
		userAddress.setLocality("CHENNAI");
		List<AddressDetail> addressList = new ArrayList<AddressDetail>();
		addressList.add(userAddress);
		locationDetails.setAddress(addressList);
		locationDetails.setCustomerId(3);
		locationDetails.setCustomerName("TCS");
		locationDetails.setLegalEntityId(333);
		locationDetails.setLegalEntityName("TCS");
		return locationDetails;
		
	}
	
	public byte[] getFileAsByteArrayNPL() throws IOException {
		List<String> countryList = new ArrayList<>();
		countryList.add("India");
		countryList.add("USA");
		List<String> profileLst = new ArrayList<>();
		profileLst.add("Profile 1");
		profileLst.add("Profile 2");
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(LocationUploadConstants.SITE_LOCATIONS);

		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue(LocationUploadConstants.HEADER_SERIAL_NO);
		header.createCell(1).setCellValue(LocationUploadConstants.HEADER_PROFILES);
		header.createCell(2).setCellValue(LocationUploadConstants.HEADER_COUNTRY_A);
		header.createCell(3).setCellValue(LocationUploadConstants.HEADER_STATE_A);
		header.createCell(4).setCellValue(LocationUploadConstants.HEADER_CITY_A);
		header.createCell(5).setCellValue(LocationUploadConstants.HEADER_PINCODE_A);
		header.createCell(6).setCellValue(LocationUploadConstants.HEADER_LOCALITY_A);
		header.createCell(7).setCellValue(LocationUploadConstants.HEADER_ADDRESS_A);
		header.createCell(8).setCellValue(LocationUploadConstants.HEADER_TYPE_A);
		header.createCell(9).setCellValue(LocationUploadConstants.HEADER_COUNTRY_B);
		header.createCell(10).setCellValue(LocationUploadConstants.HEADER_STATE_B);
		header.createCell(11).setCellValue(LocationUploadConstants.HEADER_CITY_B);
		header.createCell(12).setCellValue(LocationUploadConstants.HEADER_PINCODE_B);
		header.createCell(13).setCellValue(LocationUploadConstants.HEADER_LOCALITY_B);
		header.createCell(14).setCellValue(LocationUploadConstants.HEADER_ADDRESS_B);
		header.createCell(15).setCellValue(LocationUploadConstants.HEADER_TYPE_B);
		

		
		Row contentRow = sheet.createRow(1);
		contentRow.createCell(0).setCellValue("1");
		contentRow.createCell(1).setCellValue("Single Unmanaged GVPN");
		contentRow.createCell(2).setCellValue("India");
		contentRow.createCell(3).setCellValue("Tamilnadu");
		contentRow.createCell(4).setCellValue("Chennai");
		contentRow.createCell(5).setCellValue("600089");
		contentRow.createCell(6).setCellValue("Nandambakkam");
		contentRow.createCell(7).setCellValue("Jeyant tech park, nandambakkam, chennai, 600089");
		contentRow.createCell(8).setCellValue("SITE");
		contentRow.createCell(9).setCellValue("India");
		contentRow.createCell(10).setCellValue("Tamilnadu");
		contentRow.createCell(11).setCellValue("Chennai");
		contentRow.createCell(12).setCellValue("600089");
		contentRow.createCell(13).setCellValue("Nandambakkam");
		contentRow.createCell(14).setCellValue("Jeyant tech park, nandambakkam, chennai, 600089");
		contentRow.createCell(15).setCellValue("SITE");
		
		
		for (int i = 0; i < 16; i++) {
			CellStyle stylerowHeading = workbook.createCellStyle();
			stylerowHeading.setBorderBottom(BorderStyle.THICK);
			stylerowHeading.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
			stylerowHeading.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont font = workbook.createFont();
			font.setBold(true);
			font.setFontName(HSSFFont.FONT_ARIAL);
			font.setFontHeightInPoints((short) 10);
			font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
			stylerowHeading.setFont(font);
			stylerowHeading.setVerticalAlignment(VerticalAlignment.CENTER);
			stylerowHeading.setAlignment(HorizontalAlignment.CENTER);

			stylerowHeading.setWrapText(true);
			header.getCell(i).setCellStyle(stylerowHeading);

			DataFormat fmt = workbook.createDataFormat();
			CellStyle textStyle = workbook.createCellStyle();
			textStyle.setDataFormat(fmt.getFormat(CommonConstants.AT));
			sheet.setDefaultColumnStyle(i, textStyle);
		}
		
		sheet.setColumnWidth(0, 1800);
		sheet.setColumnWidth(1, 8000);
		sheet.setColumnWidth(2, 8000);
		sheet.setColumnWidth(3, 8000);
		sheet.setColumnWidth(4, 7500);
		sheet.setColumnWidth(5, 10000);
		sheet.setColumnWidth(6, 8000);
		sheet.setColumnWidth(7, 8000);
		sheet.setColumnWidth(8, 8000);
		sheet.setColumnWidth(9, 8000);
		sheet.setColumnWidth(10, 7500);
		sheet.setColumnWidth(11, 10000);
		sheet.setColumnWidth(12, 8000);
		sheet.setColumnWidth(13, 8000);
		sheet.setColumnWidth(14, 8000);
		sheet.setColumnWidth(15, 8000);
	
	
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		byte[] outArray = bos.toByteArray();
		return outArray;
	
	}
	
	
	public byte[] getFileAsByteArrayEmptyFile() throws IOException {
		List<String> countryList = new ArrayList<>();
		countryList.add("India");
		countryList.add("USA");
		List<String> profileLst = new ArrayList<>();
		profileLst.add("Profile 1");
		profileLst.add("Profile 2");
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(LocationUploadConstants.SITE_LOCATIONS);

		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue(LocationUploadConstants.HEADER_SERIAL_NO);
		header.createCell(1).setCellValue(LocationUploadConstants.HEADER_PROFILES);
		header.createCell(2).setCellValue(LocationUploadConstants.HEADER_COUNTRY_A);
		header.createCell(3).setCellValue(LocationUploadConstants.HEADER_STATE_A);
		header.createCell(4).setCellValue(LocationUploadConstants.HEADER_CITY_A);
		header.createCell(5).setCellValue(LocationUploadConstants.HEADER_PINCODE_A);
		header.createCell(6).setCellValue(LocationUploadConstants.HEADER_LOCALITY_A);
		header.createCell(7).setCellValue(LocationUploadConstants.HEADER_ADDRESS_A);
		header.createCell(8).setCellValue(LocationUploadConstants.HEADER_TYPE_A);
		header.createCell(9).setCellValue(LocationUploadConstants.HEADER_COUNTRY_B);
		header.createCell(10).setCellValue(LocationUploadConstants.HEADER_STATE_B);
		header.createCell(11).setCellValue(LocationUploadConstants.HEADER_CITY_B);
		header.createCell(12).setCellValue(LocationUploadConstants.HEADER_PINCODE_B);
		header.createCell(13).setCellValue(LocationUploadConstants.HEADER_LOCALITY_B);
		header.createCell(14).setCellValue(LocationUploadConstants.HEADER_ADDRESS_B);
		header.createCell(15).setCellValue(LocationUploadConstants.HEADER_TYPE_B);
		

		
		Row contentRow = sheet.createRow(1);
		contentRow.createCell(0).setCellValue("");
		contentRow.createCell(1).setCellValue("");
		contentRow.createCell(2).setCellValue("");
		contentRow.createCell(3).setCellValue("");
		contentRow.createCell(4).setCellValue("");
		contentRow.createCell(5).setCellValue("");
		contentRow.createCell(6).setCellValue("");
		contentRow.createCell(7).setCellValue("");
		contentRow.createCell(8).setCellValue("");
		contentRow.createCell(9).setCellValue("");
		contentRow.createCell(10).setCellValue("");
		contentRow.createCell(11).setCellValue("");
		contentRow.createCell(12).setCellValue("");
		contentRow.createCell(13).setCellValue("");
		contentRow.createCell(14).setCellValue("");
		contentRow.createCell(15).setCellValue("");
		
		
		for (int i = 0; i < 16; i++) {
			CellStyle stylerowHeading = workbook.createCellStyle();
			stylerowHeading.setBorderBottom(BorderStyle.THICK);
			stylerowHeading.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
			stylerowHeading.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont font = workbook.createFont();
			font.setBold(true);
			font.setFontName(HSSFFont.FONT_ARIAL);
			font.setFontHeightInPoints((short) 10);
			font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
			stylerowHeading.setFont(font);
			stylerowHeading.setVerticalAlignment(VerticalAlignment.CENTER);
			stylerowHeading.setAlignment(HorizontalAlignment.CENTER);

			stylerowHeading.setWrapText(true);
			header.getCell(i).setCellStyle(stylerowHeading);

			DataFormat fmt = workbook.createDataFormat();
			CellStyle textStyle = workbook.createCellStyle();
			textStyle.setDataFormat(fmt.getFormat(CommonConstants.AT));
			sheet.setDefaultColumnStyle(i, textStyle);
		}
		
		sheet.setColumnWidth(0, 1800);
		sheet.setColumnWidth(1, 8000);
		sheet.setColumnWidth(2, 8000);
		sheet.setColumnWidth(3, 8000);
		sheet.setColumnWidth(4, 7500);
		sheet.setColumnWidth(5, 10000);
		sheet.setColumnWidth(6, 8000);
		sheet.setColumnWidth(7, 8000);
		sheet.setColumnWidth(8, 8000);
		sheet.setColumnWidth(9, 8000);
		sheet.setColumnWidth(10, 7500);
		sheet.setColumnWidth(11, 10000);
		sheet.setColumnWidth(12, 8000);
		sheet.setColumnWidth(13, 8000);
		sheet.setColumnWidth(14, 8000);
		sheet.setColumnWidth(15, 8000);
	
	
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		byte[] outArray = bos.toByteArray();
		return outArray;
	
	}
	
}

