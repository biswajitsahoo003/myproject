package com.tcl.dias.servicehandover.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.LeStateInfo;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.servicefulfillment.entity.entities.MstBillingStatesAndCities;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScGstAddress;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.MstBillingStatesAndCitiesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScGstAddressRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.dias.servicehandover.constants.Constants;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.createorder.AccountCreationInputBO;

@Component
public class IPCAddressConstructor extends ServiceFulfillmentBaseService{
	
	@Autowired
	MstBillingStatesAndCitiesRepository mstBillingStatesAndCitiesRepository;
	
	@Autowired
	CustomerAndSupplierAddress customerAndSupplierAddress;
	
	Map<String, String> locationDataMap;
	
	@Autowired 
	LoadCustomerDetails loadCustomerDetails;
	
	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;
	
	@Autowired
	ScOrderRepository orderRepository;
	
	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;
	
	@Autowired
	ScOrderAttributeRepository orderAttributeRepository;
	
	@Autowired
	ScGstAddressRepository scGstAddressRepository;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IPCAddressConstructor.class);
	
	@PostConstruct
	public Map<String, String> locationMap(){
		locationDataMap = new HashMap<String, String>();
		List<MstBillingStatesAndCities> billingStatesAndCities = mstBillingStatesAndCitiesRepository.findAll();
		locationDataMap = billingStatesAndCities.stream()
				.collect(Collectors.toMap(states -> states.getValidStatesAndCities().replaceAll(" ", "").toLowerCase(),
						MstBillingStatesAndCities::getValidStatesAndCities));
		return locationDataMap;
	}

	public static String getLocationData(Map<String, String> locationMap, String location) {
		String updatedLocation = location.replaceAll(" ", "").toLowerCase();
		if (locationMap.containsKey(updatedLocation)) {
			location = locationMap.get(updatedLocation);
			return location;
		}
		return WordUtils.capitalizeFully(location);
	}
	
	public String contractAddress(String customerAddressLocationId) {
		AddressDetail addressDetail = customerAndSupplierAddress.getAddressLocationId(customerAddressLocationId);
		ContractingAddress contractingAddress = new ContractingAddress();
		contractingAddress.setContractingAddressOne(StringUtils.trimToEmpty(addressDetail.getAddressLineOne()));
		contractingAddress.setContractingAddressTwo(StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()));
		contractingAddress.setContractingAddressThree(StringUtils.trimToEmpty(addressDetail.getLocality()));
		contractingAddress.setContractingCity(getLocationData(locationDataMap, StringUtils.trimToEmpty(addressDetail.getCity())));
		contractingAddress.setContractingState(getLocationData(locationDataMap, StringUtils.trimToEmpty(addressDetail.getState())));
		contractingAddress.setContractingCountry(getLocationData(locationDataMap, StringUtils.trimToEmpty(addressDetail.getCountry())));
		contractingAddress.setContractingPincode(StringUtils.trimToEmpty(addressDetail.getPincode()));
		return contractingAddress.toString();
	}
	
	public String contractGstAddress(String GstNo, Integer custLeId, ScOrder order) {
		if (order.getScGstAddress() != null && !"No Registered GST".equalsIgnoreCase(GstNo)) {
			ScGstAddress scGstAddress= scGstAddressRepository.findById(order.getScGstAddress().getId()).get();
			ContractGstAddress contractGstAddress = new ContractGstAddress();
			contractGstAddress.setContractGstinAddressOne(scGstAddress.getBuildingName());
			contractGstAddress.setContractGstinAddressTwo(scGstAddress.getStreet());
			contractGstAddress
					.setContractGstinAddressThree(StringUtils.trimToEmpty(scGstAddress.getLocality()));
			contractGstAddress
					.setContractGstinAddressCity(StringUtils.trimToEmpty(scGstAddress.getDistrict()));
			contractGstAddress
					.setContractGstinAddressState(StringUtils.trimToEmpty(scGstAddress.getState()));
			contractGstAddress.setContractGstinAddressCountry(NetworkConstants.INDIA);
			contractGstAddress
					.setContractGstinAddressPincode(StringUtils.trimToEmpty(scGstAddress.getPincode()));
			return contractGstAddress.toString();
		} else {
			LOGGER.info("Queue for Contracting Address");
			LeStateInfo contractGstAddressDetail = loadCustomerDetails.getGstDetails(GstNo, custLeId);
			if (contractGstAddressDetail != null) {
				ContractGstAddress contractGstAddress = new ContractGstAddress();
				contractGstAddress.setContractGstinAddressOne(
						StringUtils.trimToEmpty(contractGstAddressDetail.getAddresslineOne()));
				contractGstAddress.setContractGstinAddressTwo(
						StringUtils.trimToEmpty(contractGstAddressDetail.getAddresslineTwo()));
				contractGstAddress.setContractGstinAddressThree(
						StringUtils.trimToEmpty(contractGstAddressDetail.getAddresslineThree()));
				contractGstAddress.setContractGstinAddressCity(
						getLocationData(locationDataMap, StringUtils.trimToEmpty(contractGstAddressDetail.getCity())));
				contractGstAddress.setContractGstinAddressState(
						getLocationData(locationDataMap, StringUtils.trimToEmpty(contractGstAddressDetail.getState())));
				contractGstAddress.setContractGstinAddressCountry(getLocationData(locationDataMap,
						StringUtils.trimToEmpty(contractGstAddressDetail.getCountry())));
				contractGstAddress
						.setContractGstinAddressPincode(StringUtils.trimToEmpty(contractGstAddressDetail.getPincode()));
				return contractGstAddress.toString();
			}
		}
		return "";
	}
	
	public AccountCreationInputBO accountAddress(ScContractInfo scContractInfo, AccountCreationInputBO accountCreationInputBO) {
		accountCreationInputBO.setAccAddr1(StringUtils.trimToEmpty(scContractInfo.getBillingAddressLine1()).length() >= 120
						? scContractInfo.getBillingAddressLine1().substring(0, 120)
						: scContractInfo.getBillingAddressLine1());
		accountCreationInputBO.setAccAddr2(StringUtils.trimToEmpty(scContractInfo.getBillingAddressLine2()).length() >= 120
						? scContractInfo.getBillingAddressLine2().substring(0, 120)
						: scContractInfo.getBillingAddressLine2());

		accountCreationInputBO.setAccAddr3("");
		accountCreationInputBO.setAccCity(getLocationData(locationDataMap, StringUtils.trimToEmpty(scContractInfo.getBillingCity())));
		accountCreationInputBO.setAccState(getLocationData(locationDataMap, StringUtils.trimToEmpty(scContractInfo.getBillingState())));
		accountCreationInputBO.setAccCountry(getLocationData(locationDataMap, StringUtils.trimToEmpty(scContractInfo.getBillingCountry())));
		accountCreationInputBO.setAccZipcode(StringUtils.trimToEmpty(scContractInfo.getBillingPincode()));
		return accountCreationInputBO;
	}
	
	public ContractSiteGstAddress getGstAddress(String taskId) {
		Task task = getTaskById(Integer.parseInt(taskId));
		ContractSiteGstAddress contractSiteGstAddress = new ContractSiteGstAddress();
		if(task!=null) {
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(task.getServiceId()).get();
			ScOrder scOrder = null;
			if (scServiceDetail != null) {
				scOrder = scServiceDetail.getScOrder();
				List<ScOrderAttribute> scOrderAttributeList = scOrderAttributeRepository
						.findByScOrder_IdAndIsActive(scOrder.getId(), NetworkConstants.Y);
				
				ContractGstAddress contractGstAddress = new ContractGstAddress();
				scOrderAttributeList.forEach((scOrderAttribute) -> {
					switch (scOrderAttribute.getAttributeName()) {
					case LeAttributesConstants.LE_STATE_GST_NO:
						contractGstAddress.setContractGstNumber(scOrderAttribute.getAttributeValue());
						break;
					}
				});
				
				if (scOrder.getScGstAddress() != null) {
					contractGstAddress.setContractGstinAddressOne(scOrder.getScGstAddress().getBuildingName());
					contractGstAddress.setContractGstinAddressTwo(scOrder.getScGstAddress().getStreet());
					contractGstAddress.setContractGstinAddressThree(scOrder.getScGstAddress().getLocality());
					contractGstAddress.setContractGstinAddressCity(scOrder.getScGstAddress().getDistrict());
					contractGstAddress.setContractGstinAddressState(scOrder.getScGstAddress().getState());
					contractGstAddress.setContractGstinAddressCountry(NetworkConstants.INDIA);
					contractGstAddress.setContractGstinAddressPincode(scOrder.getScGstAddress().getPincode());
				}
				contractSiteGstAddress.setContractGstAddress(contractGstAddress);
			}
		}
		return contractSiteGstAddress;
	}
	
	public String saveGstAddress(String serviceId, ContractSiteGstAddress contractSiteGstAddress) {
		Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
		ScOrder scOrder = scServiceDetail.get().getScOrder();
		
		if (scOrder != null && contractSiteGstAddress != null && contractSiteGstAddress.getContractGstAddress() != null) {
			ContractGstAddress contractGstAddress = contractSiteGstAddress.getContractGstAddress();
			if (!contractGstAddress.getContractGstNumber().equals("No Registered GST")) {
				ScGstAddress scContractGstAddress = new ScGstAddress();
				scContractGstAddress.setBuildingName(
						StringUtils.trimToEmpty(contractGstAddress.getContractGstinAddressOne()));
				scContractGstAddress
						.setStreet(StringUtils.trimToEmpty(contractGstAddress.getContractGstinAddressTwo()));
				scContractGstAddress.setLocality(
						StringUtils.trimToEmpty(contractGstAddress.getContractGstinAddressThree()));
				scContractGstAddress
						.setDistrict(StringUtils.trimToEmpty(contractGstAddress.getContractGstinAddressCity()));
				scContractGstAddress
						.setState(StringUtils.trimToEmpty(contractGstAddress.getContractGstinAddressState()));
				scContractGstAddress.setPincode(
						StringUtils.trimToEmpty(contractGstAddress.getContractGstinAddressPincode()));
				scOrder.setScGstAddress(scContractGstAddress);
			
				String fullContractGstAddress = StringUtils.trimToEmpty(contractGstAddress.getContractGstinAddressOne()) + IpcConstants.SINGLE_SPACE
						+ StringUtils.trimToEmpty(contractGstAddress.getContractGstinAddressTwo()) + IpcConstants.SINGLE_SPACE
						+ StringUtils.trimToEmpty(contractGstAddress.getContractGstinAddressThree()) + IpcConstants.SINGLE_SPACE
						+ StringUtils.trimToEmpty(contractGstAddress.getContractGstinAddressCity()) + IpcConstants.SINGLE_SPACE
						+ StringUtils.trimToEmpty(contractGstAddress.getContractGstinAddressState()) + IpcConstants.SINGLE_SPACE
						+ StringUtils.trimToEmpty(contractGstAddress.getContractGstinAddressCountry()) + IpcConstants.SINGLE_SPACE
						+ StringUtils.trimToEmpty(contractGstAddress.getContractGstinAddressPincode());
				
				
				ScOrderAttribute scOrderAttribute = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(IpcConstants.CONTRACT_GST_ADDRESS_ATTRIBUTE, scOrder);
				if (scOrderAttribute!= null) {
					scOrderAttribute.setAttributeValue(fullContractGstAddress);
					scOrderAttribute.setAttributeAltValueLabel(fullContractGstAddress);
					scOrderAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
				} else {
					scOrderAttribute = new ScOrderAttribute();
					scOrderAttribute.setAttributeName(IpcConstants.CONTRACT_GST_ADDRESS_ATTRIBUTE);
					scOrderAttribute.setAttributeValue(fullContractGstAddress);
					scOrderAttribute.setAttributeAltValueLabel(fullContractGstAddress);
					scOrderAttribute.setScOrder(scOrder);
					scOrderAttribute.setIsActive("Y");
					scOrderAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
				}
				orderAttributeRepository.save(scOrderAttribute);
				orderRepository.save(scOrder);
			}
			ScOrderAttribute scOrderAttribute = scOrderAttributeRepository
					.findFirstByAttributeNameAndScOrder(LeAttributesConstants.LE_STATE_GST_NO, scOrder);
			if (scOrderAttribute != null) {
				scOrderAttribute.setAttributeAltValueLabel(
						StringUtils.trimToEmpty(contractGstAddress.getContractGstNumber()));
				scOrderAttribute
						.setAttributeValue(StringUtils.trimToEmpty(contractGstAddress.getContractGstNumber()));
			} else {
				scOrderAttribute = new ScOrderAttribute();
				scOrderAttribute.setAttributeName(LeAttributesConstants.LE_STATE_GST_NO);
				scOrderAttribute
						.setAttributeValue(StringUtils.trimToEmpty(contractGstAddress.getContractGstNumber()));
				scOrderAttribute.setAttributeAltValueLabel(
						StringUtils.trimToEmpty(contractGstAddress.getContractGstNumber()));
				scOrderAttribute.setIsActive(NetworkConstants.Y);
				scOrderAttribute.setScOrder(scOrder);
			}
			scOrderAttributeRepository.save(scOrderAttribute);
			
			ScOrderAttribute scOrderContractIdAttribute = scOrderAttributeRepository.findFirstByAttributeNameAndScOrder(LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY, scOrder);
			String contractingAddress = "";
			if (scOrderContractIdAttribute != null && scOrderContractIdAttribute.getAttributeValue() != null) {
				contractingAddress = contractAddress(scOrderContractIdAttribute.getAttributeValue());
			}
			ScOrderAttribute scOrderContractAddressAttribute = scOrderAttributeRepository
					.findFirstByAttributeNameAndScOrder(Constants.CONTRACTING_ADDRESS, scOrder);
			if (scOrderContractAddressAttribute!= null) {
				scOrderContractAddressAttribute.setAttributeValue(contractingAddress);
				scOrderContractAddressAttribute.setAttributeAltValueLabel(contractingAddress);
				scOrderContractAddressAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
			} else {
				scOrderContractAddressAttribute = new ScOrderAttribute();
				scOrderContractAddressAttribute.setAttributeName(Constants.CONTRACTING_ADDRESS);
				scOrderContractAddressAttribute.setAttributeValue(contractingAddress);
				scOrderContractAddressAttribute.setAttributeAltValueLabel(contractingAddress);
				scOrderContractAddressAttribute.setScOrder(scOrder);
				scOrderContractAddressAttribute.setIsActive("Y");
				scOrderContractAddressAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
			}
			orderAttributeRepository.save(scOrderContractAddressAttribute);
		}
		return "Gst Number & Address Updated";
	}
}
