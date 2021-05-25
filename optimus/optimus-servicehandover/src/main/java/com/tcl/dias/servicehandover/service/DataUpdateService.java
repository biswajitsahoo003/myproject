package com.tcl.dias.servicehandover.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScGstAddress;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScGstAddressRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.util.BillingData;
import com.tcl.dias.servicehandover.util.SiteGst;
import com.tcl.dias.servicehandover.util.SiteGstAddress;

@Service
public class DataUpdateService {

	@Autowired
	ScOrderRepository scOrderRepository;

	@Autowired
	ScContractInfoRepository contractInfoRepository;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScOrderAttributeRepository orderAttributeRepository;
	
	@Autowired
	ScComponentAttributesRepository componentAttributesRepository;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	ScGstAddressRepository gstAddressRepository;

	public String updateBillingData(BillingData billingData, String orderCode) {
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
		if (scOrder != null) {
			ScContractInfo contractInfo = contractInfoRepository.findFirstByScOrder_id(scOrder.getId());
			if (contractInfo != null) {
				contractInfo.setBillingAddressLine1(StringUtils.trimToEmpty(billingData.getAddressLine1()));
				contractInfo.setBillingAddressLine2(StringUtils.trimToEmpty(billingData.getAddressLine2()));
				contractInfo.setBillingAddressLine3(StringUtils.trimToEmpty(billingData.getAddressLine3()));
				contractInfo.setBillingCity(StringUtils.trimToEmpty(billingData.getCity()));
				contractInfo.setBillingState(StringUtils.trimToEmpty(billingData.getState()));
				contractInfo.setBillingCountry(StringUtils.trimToEmpty(billingData.getCountry()));
				contractInfo.setBillingPincode(StringUtils.trimToEmpty(billingData.getZipcode()));
				contractInfoRepository.save(contractInfo);
			}
		}
		if (billingData.getPhoneNo() != null && scOrder != null) {
			ScOrderAttribute attribute = orderAttributeRepository
					.findFirstByAttributeNameAndScOrder(LeAttributesConstants.BILLING_CONTACT_MOBILE, scOrder);
			if (attribute != null) {
				attribute.setAttributeValue(StringUtils.trimToEmpty(billingData.getPhoneNo()));
				attribute.setAttributeAltValueLabel(StringUtils.trimToEmpty(billingData.getPhoneNo()));

			} else {
				attribute = new ScOrderAttribute();
				attribute.setAttributeName(LeAttributesConstants.BILLING_CONTACT_MOBILE);
				attribute.setAttributeValue(StringUtils.trimToEmpty(billingData.getPhoneNo()));
				attribute.setAttributeAltValueLabel(StringUtils.trimToEmpty(billingData.getPhoneNo()));
				attribute.setIsActive("Y");
				attribute.setScOrder(scOrder);
			}
			orderAttributeRepository.save(attribute);
		}
		if (billingData.getEmail() != null && scOrder != null) {

			ScOrderAttribute attribute = orderAttributeRepository
					.findFirstByAttributeNameAndScOrder(LeAttributesConstants.BILLING_CONTACT_EMAIL, scOrder);
			if (attribute != null) {
				attribute.setAttributeValue(StringUtils.trimToEmpty(billingData.getEmail()));
				attribute.setAttributeAltValueLabel(StringUtils.trimToEmpty(billingData.getEmail()));
			} else {
				attribute = new ScOrderAttribute();
				attribute.setAttributeName(LeAttributesConstants.BILLING_CONTACT_EMAIL);
				attribute.setAttributeValue(StringUtils.trimToEmpty(billingData.getEmail()));
				attribute.setAttributeAltValueLabel(StringUtils.trimToEmpty(billingData.getEmail()));
				attribute.setIsActive("Y");
				attribute.setScOrder(scOrder);
			}
			orderAttributeRepository.save(attribute);
		}
		if (billingData.getContactName() != null && scOrder != null) {
			ScOrderAttribute attribute = orderAttributeRepository
					.findFirstByAttributeNameAndScOrder(LeAttributesConstants.BILLING_CONTACT_NAME, scOrder);
			if (attribute != null) {
				attribute.setAttributeValue(StringUtils.trimToEmpty(billingData.getContactName()));
				attribute.setAttributeAltValueLabel(StringUtils.trimToEmpty(billingData.getContactName()));
			} else {
				attribute = new ScOrderAttribute();
				attribute.setAttributeName(LeAttributesConstants.BILLING_CONTACT_NAME);
				attribute.setAttributeValue(StringUtils.trimToEmpty(billingData.getContactName()));
				attribute.setAttributeAltValueLabel(StringUtils.trimToEmpty(billingData.getContactName()));
				attribute.setIsActive("Y");
				attribute.setScOrder(scOrder);
			}
			orderAttributeRepository.save(attribute);
		}
		if (billingData.getPoDate() != null && scOrder != null) {
			ScOrderAttribute attribute = orderAttributeRepository
					.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_DATE, scOrder);
			if (attribute != null) {
				attribute.setAttributeValue(StringUtils.trimToEmpty(billingData.getPoDate()));
				attribute.setAttributeAltValueLabel(StringUtils.trimToEmpty(billingData.getPoDate()));
			} else {
				attribute = new ScOrderAttribute();
				attribute.setAttributeName(LeAttributesConstants.PO_DATE);
				attribute.setAttributeValue(StringUtils.trimToEmpty(billingData.getPoDate()));
				attribute.setAttributeAltValueLabel(StringUtils.trimToEmpty(billingData.getPoDate()));
				attribute.setIsActive("Y");
				attribute.setScOrder(scOrder);
			}
			orderAttributeRepository.save(attribute);
		}
		if (billingData.getPoNumber() != null && scOrder != null) {
			ScOrderAttribute attribute = orderAttributeRepository
					.findFirstByAttributeNameAndScOrder(LeAttributesConstants.PO_NUMBER, scOrder);
			if (attribute != null) {
				attribute.setAttributeValue(StringUtils.trimToEmpty(billingData.getPoNumber()));
				attribute.setAttributeAltValueLabel(StringUtils.trimToEmpty(billingData.getPoNumber()));
			} else {
				attribute = new ScOrderAttribute();
				attribute.setAttributeName(LeAttributesConstants.PO_NUMBER);
				attribute.setAttributeValue(StringUtils.trimToEmpty(billingData.getPoNumber()));
				attribute.setAttributeAltValueLabel(StringUtils.trimToEmpty(billingData.getPoNumber()));
				attribute.setIsActive("Y");
				attribute.setScOrder(scOrder);
			}
			orderAttributeRepository.save(attribute);
		}
		return NetworkConstants.SUCCESS;
	}
	
	public String updateSiteGstAddressLatest(SiteGst siteGstAddress, String serviceId) {
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId)).get();
		if (scServiceDetail != null && !siteGstAddress.getSiteGstNumber().equals("No Registered GST")) {
			Map<String, String> siteGstMap = commonFulfillmentUtils.getComponentAttributesDetails(
					Arrays.asList("siteGstAddressId", "siteGstNumber"), scServiceDetail.getId(), "LM", siteGstAddress.getSiteType());
			if (siteGstMap != null) {
				Map<String, String> atMap = new HashMap<>();
				ScGstAddress scSiteGstAddress = new ScGstAddress();
				scSiteGstAddress.setBuildingName(StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressOne()));
				scSiteGstAddress.setStreet(StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressTwo()));
				scSiteGstAddress.setLocality(StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressThree()));
				scSiteGstAddress.setDistrict(StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressCity()));
				scSiteGstAddress.setState(StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressState()));
				scSiteGstAddress.setPincode(StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressPincode()));
				scSiteGstAddress = gstAddressRepository.save(scSiteGstAddress);
				atMap.put("siteGstAddressId", scSiteGstAddress.getId().toString());
				atMap.put("siteGstNumber", siteGstAddress.getSiteGstNumber());

				String fullSiteGstAddress = StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressOne()) + " "
						+ StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressTwo()) + " "
						+ StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressThree()) + " "
						+ StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressCity()) + " "
						+ StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressState()) + " "
						+ NetworkConstants.INDIA + " "
						+ StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressPincode());

				atMap.put("siteGstAddress", fullSiteGstAddress);
				componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
						AttributeConstants.COMPONENT_LM, siteGstAddress.getSiteType());
			}
		}
		if (siteGstAddress.getSiteGstNumber().equals("No Registered GST")) {
			Map<String, String> atMap = new HashMap<>();
			atMap.put("siteGstNumber","No Registered GST");
			atMap.put("siteGstAddress", "");
			componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
					AttributeConstants.COMPONENT_LM, siteGstAddress.getSiteType());
		}
		return NetworkConstants.SUCCESS;
	}

	/*public String updateSiteGstAddress(SiteGstAddress siteGstAddress, String serviceCode) {
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(serviceCode, "INPROGRESS");
		if (scServiceDetail != null && !siteGstAddress.getSiteGstNumber().equals("No Registered GST")) {
			ScGstAddress scSiteGstAddress = new ScGstAddress();
			scSiteGstAddress.setBuildingName(StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressOne()));
			scSiteGstAddress.setStreet(StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressTwo()));
			scSiteGstAddress.setLocality(StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressThree()));
			scSiteGstAddress.setDistrict(StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressCity()));
			scSiteGstAddress.setState(StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressState()));
			scSiteGstAddress.setPincode(StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressPincode()));
			scServiceDetail.setScGstAddress(scSiteGstAddress);
			scServiceDetail.setBillingGstNumber(siteGstAddress.getSiteGstNumber());
			scServiceDetailRepository.save(scServiceDetail);

			String addressLineOne = StringUtils.trimToEmpty(scServiceDetail.getScGstAddress().getFlatNumber()) + " "
					+ StringUtils.trimToEmpty(scServiceDetail.getScGstAddress().getBuildingNumber()) + " "
					+ StringUtils.trimToEmpty(scServiceDetail.getScGstAddress().getBuildingName());
			String addressLineTwo = StringUtils.trimToEmpty(scServiceDetail.getScGstAddress().getStreet());
			String addressLinethree = "";
			if (!addressLineTwo.equals(scServiceDetail.getScGstAddress().getLocality()))
				addressLinethree = StringUtils.trimToEmpty(scServiceDetail.getScGstAddress().getLocality());
			String addressCity = StringUtils.trimToEmpty(scServiceDetail.getScGstAddress().getDistrict());
			String addressState = StringUtils.trimToEmpty(scServiceDetail.getScGstAddress().getState());
			String addressCountry = NetworkConstants.INDIA;
			String addressPincode = StringUtils.trimToEmpty(scServiceDetail.getScGstAddress().getPincode());

			String fullSiteGstAddress = addressLineOne + "," + addressLineTwo + "," + addressLinethree + ","
					+ addressCity + "," + addressState + "," + addressCountry + "," + addressPincode;

			ScComponentAttribute componentAttribute = componentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							scServiceDetail.getId(), "siteGstAddress", "LM", "A");
			if (componentAttribute != null) {
				componentAttribute.setAttributeValue(fullSiteGstAddress);
				componentAttribute.setAttributeAltValueLabel(fullSiteGstAddress);
			} else {
				componentAttribute = new ScComponentAttribute();
				componentAttribute.setAttributeValue(fullSiteGstAddress);
				componentAttribute.setAttributeAltValueLabel(fullSiteGstAddress);
				componentAttribute.setAttributeValue(fullSiteGstAddress);
				componentAttribute.setAttributeName("siteGstAddress");

			}
			componentAttributesRepository.save(componentAttribute);
		}
		if (siteGstAddress.getSiteGstNumber().equals("No Registered GST")) {
			ScComponentAttribute componentAttribute = componentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							scServiceDetail.getId(), "siteGstAddress", "LM", "A");
			if (componentAttribute != null) {
				componentAttribute.setAttributeValue("");
				componentAttribute.setAttributeAltValueLabel("");
			} else {
				componentAttribute = new ScComponentAttribute();
				componentAttribute.setAttributeValue("");
				componentAttribute.setAttributeAltValueLabel("");
				componentAttribute.setAttributeValue("");
				componentAttribute.setAttributeName("siteGstAddress");

			}
			componentAttributesRepository.save(componentAttribute);
		}
		return NetworkConstants.SUCCESS;
	}*/

	
	public String updateContractGstAddress(SiteGst siteGstAddress, String orderCode) {
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
		if (scOrder != null & !siteGstAddress.getSiteGstNumber().equals("No Registered GST")) {
			ScGstAddress scSiteGstAddress = new ScGstAddress();
			scSiteGstAddress.setBuildingName(StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressOne()));
			scSiteGstAddress.setStreet(StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressTwo()));
			scSiteGstAddress.setLocality(StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressThree()));
			scSiteGstAddress.setDistrict(StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressCity()));
			scSiteGstAddress.setState(StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressState()));
			scSiteGstAddress.setPincode(StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressPincode()));
			gstAddressRepository.save(scSiteGstAddress);
			scOrder.setScGstAddress(scSiteGstAddress);
			scOrderRepository.save(scOrder);
			ScOrderAttribute attribute = orderAttributeRepository.findFirstByAttributeNameAndScOrder(LeAttributesConstants.LE_STATE_GST_NO, scOrder);
			if (attribute != null) {
				attribute.setAttributeValue(StringUtils.trimToEmpty(siteGstAddress.getSiteGstNumber()));
				attribute.setAttributeAltValueLabel(StringUtils.trimToEmpty(siteGstAddress.getSiteGstNumber()));
			} else {
				attribute = new ScOrderAttribute();
				attribute.setAttributeName(LeAttributesConstants.LE_STATE_GST_NO);
				attribute.setAttributeValue(StringUtils.trimToEmpty(siteGstAddress.getSiteGstNumber()));
				attribute.setAttributeAltValueLabel(StringUtils.trimToEmpty(siteGstAddress.getSiteGstNumber()));
				attribute.setIsActive("Y");
				attribute.setScOrder(scOrder);
			}
			orderAttributeRepository.save(attribute);
		}
		if (siteGstAddress.getSiteGstNumber().equals("No Registered GST")) {
			scOrder.setScGstAddress(null);
			scOrderRepository.save(scOrder);
			ScOrderAttribute attribute = orderAttributeRepository
					.findFirstByAttributeNameAndScOrder(LeAttributesConstants.LE_STATE_GST_NO, scOrder);
			if (attribute != null) {
				attribute.setAttributeValue("No Registered GST");
				attribute.setAttributeAltValueLabel("No Registered GST");
			} else {
				attribute = new ScOrderAttribute();
				attribute.setAttributeName(LeAttributesConstants.LE_STATE_GST_NO);
				attribute.setAttributeValue("No Registered GST");
				attribute.setAttributeAltValueLabel("No Registered GST");
				attribute.setIsActive("Y");
				attribute.setScOrder(scOrder);
			}
			orderAttributeRepository.save(attribute);

		}
		return NetworkConstants.SUCCESS;
	}
}
