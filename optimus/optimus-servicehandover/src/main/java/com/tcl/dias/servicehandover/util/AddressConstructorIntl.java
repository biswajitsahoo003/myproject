package com.tcl.dias.servicehandover.util;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.GeoCodeAddress;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.service.SureTaxService;

@Component
public class AddressConstructorIntl extends ServiceFulfillmentBaseService{
	
	@Autowired
	CustomerAndSupplierAddress customerAndSupplierAddress;
	
	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	ScOrderRepository orderRepository;
	
	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;
	
	@Autowired
	ScComponentRepository scComponentRepository;
	
	@Autowired
	SureTaxService sureTaxService;
	
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AddressConstructorIntl.class);
	
	
	public ContractAddressIntl getAddress(String taskId) {
		Task task = getTaskById(Integer.parseInt(taskId));
		ContractAddressIntl contractAddressIntl = new ContractAddressIntl();
		LOGGER.info("Get Internation Contract Address Address Started {}", LocalTime.now());
		if (task != null) {
			ScOrder scOrder = orderRepository.findById(task.getScOrderId()).get();
			if (scOrder != null) {
				LOGGER.info("Loading order repository started {}", LocalTime.now());
				ScOrderAttribute scOrderAttribute = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.Vat_Number, scOrder);
				if (scOrderAttribute != null) {
					contractAddressIntl.setVatNumber(scOrderAttribute.getAttributeValue());
				}
				
				Map<String, String> contractingAddressMap = commonFulfillmentUtils.getScOrdertAttributesDetails(
						Arrays.asList("ContractingAddressLineOne", "ContractingAddressLineTwo",
								"ContractingAddressLineThree", "ContractingAddressCity", "ContractingAddressState",
								"ContractingAddressCountry", "ContractingAddressPincode"),scOrder);
				
				if(contractingAddressMap!=null) {
					ContractingAddress contractingAddress = new ContractingAddress();
					contractingAddress.setContractingAddressOne(contractingAddressMap.get("ContractingAddressLineOne"));
					contractingAddress.setContractingAddressTwo(contractingAddressMap.get("ContractingAddressLineTwo"));
					contractingAddress.setContractingAddressThree(contractingAddressMap.get("ContractingAddressLineThree"));
					contractingAddress.setContractingCity(contractingAddressMap.get("ContractingAddressCity"));
					contractingAddress.setContractingState(contractingAddressMap.get("ContractingAddressState"));
					contractingAddress.setContractingCountry(contractingAddressMap.get("ContractingAddressCountry"));
					contractingAddress.setContractingPincode(contractingAddressMap.get("ContractingAddressPincode"));
					contractAddressIntl.setContractingAddress(contractingAddress);
				}
			}

			Map<String, String> siteAddressMap = commonFulfillmentUtils
					.getComponentAttributesDetails(
							Arrays.asList("destinationAddressLineOne",
									"destinationAddressLineTwo", "destinationLocality", "destinationCity",
									"destinationState", "destinationCountry", "destinationPincode"),
							task.getServiceId(), "LM", "A");
			LOGGER.info("Site Address {}" ,siteAddressMap);
			if (!siteAddressMap.isEmpty()) {
				SiteAddress siteAAddress = new SiteAddress();
				siteAAddress.setSiteAddressLineOne(
						StringUtils.trimToEmpty(siteAddressMap.get("destinationAddressLineOne")));
				siteAAddress.setSiteAddressLineTwo(
						StringUtils.trimToEmpty(siteAddressMap.get("destinationAddressLineTwo")));
				siteAAddress
						.setSiteAddressLineThree(StringUtils.trimToEmpty(siteAddressMap.get("destinationLocality")));
				siteAAddress.setSiteCity(StringUtils.trimToEmpty(siteAddressMap.get("destinationCity")));
				siteAAddress
						.setSiteState(StringUtils.trimToEmpty(siteAddressMap.get("destinationState")));
				siteAAddress.setSiteCountry(StringUtils.trimToEmpty(siteAddressMap.get("destinationCountry")));
				siteAAddress.setSitePincode(StringUtils.trimToEmpty(siteAddressMap.get("destinationPincode")));
				contractAddressIntl.setSiteAAddress(siteAAddress);

			}else {
				Map<String, String> contractingAddressMap = commonFulfillmentUtils.getScOrdertAttributesDetails(
						Arrays.asList("ContractingAddressLineOne", "ContractingAddressLineTwo",
								"ContractingAddressLineThree", "ContractingAddressCity", "ContractingAddressState",
								"ContractingAddressCountry", "ContractingAddressPincode"),scOrder);
				LOGGER.info("Using Contract Address as Site Address as site is not applicable {}" ,contractingAddressMap );
				if (contractingAddressMap != null) {
					SiteAddress siteAAddress = new SiteAddress();
					siteAAddress.setSiteAddressLineOne(
							StringUtils.trimToEmpty(contractingAddressMap.get("ContractingAddressLineOne")));
					siteAAddress.setSiteAddressLineTwo(
							StringUtils.trimToEmpty(contractingAddressMap.get("ContractingAddressLineTwo")));
					siteAAddress.setSiteAddressLineThree(
							StringUtils.trimToEmpty(contractingAddressMap.get("ContractingAddressLineThree")));
					siteAAddress
							.setSiteCity(StringUtils.trimToEmpty(contractingAddressMap.get("ContractingAddressCity")));
					siteAAddress.setSiteState(
							StringUtils.trimToEmpty(contractingAddressMap.get("ContractingAddressState")));
					siteAAddress.setSiteCountry(
							StringUtils.trimToEmpty(contractingAddressMap.get("ContractingAddressCountry")));
					siteAAddress.setSitePincode(
							StringUtils.trimToEmpty(contractingAddressMap.get("ContractingAddressPincode")));
					contractAddressIntl.setSiteAAddress(siteAAddress);
				}
			}
		}

		LOGGER.info("Get Gst Address Ended {}", LocalTime.now());
		return contractAddressIntl;
	}

	public String saveAddress(String serviceId, ContractAddressIntl contractAddressIntl) {
		Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
		ScOrder scOrder = scServiceDetail.get().getScOrder();
		Map<String, String> attributeAMap = new HashMap<>();
		if (scOrder != null) {
			if (contractAddressIntl != null) {
				String vatNumber = contractAddressIntl.getVatNumber();
				SiteAddress siteAddress = contractAddressIntl.getSiteAAddress();
				ContractingAddress contractingAddress = contractAddressIntl.getContractingAddress();
				if (StringUtils.isNotEmpty(vatNumber)) {
					ScOrderAttribute scOrderAttribute = scOrderAttributeRepository
							.findFirstByAttributeNameAndScOrder(LeAttributesConstants.Vat_Number, scOrder);
					if (scOrderAttribute != null) {
						scOrderAttribute.setAttributeAltValueLabel(StringUtils.trimToEmpty(vatNumber));
						scOrderAttribute.setAttributeValue(StringUtils.trimToEmpty(vatNumber));
					} else {
						scOrderAttribute = new ScOrderAttribute();
						scOrderAttribute.setAttributeName(LeAttributesConstants.Vat_Number);
						scOrderAttribute.setAttributeValue(StringUtils.trimToEmpty(vatNumber));
						scOrderAttribute.setAttributeAltValueLabel(StringUtils.trimToEmpty(vatNumber));
						scOrderAttribute.setIsActive(NetworkConstants.Y);
						scOrderAttribute.setScOrder(scOrder);
					}
					scOrderAttributeRepository.save(scOrderAttribute);
				}
				if (siteAddress != null) {
					LOGGER.info(
							"Site A Address updated is line 1:{} line 2:{} line 3:{} City:{} state:{} County:{} Pincode:{}",
							siteAddress.getSiteAddressLineOne(), siteAddress.getSiteAddressLineTwo(),
							siteAddress.getSiteAddressLineThree(), siteAddress.getSiteCity(),
							siteAddress.getSiteState(), siteAddress.getSiteCountry(), siteAddress.getSitePincode());
					attributeAMap.put("destinationAddressLineOne", siteAddress.getSiteAddressLineOne());
					attributeAMap.put("destinationAddressLineTwo", siteAddress.getSiteAddressLineTwo());
					attributeAMap.put("destinationLocality", siteAddress.getSiteAddressLineThree());
					attributeAMap.put("destinationCity", siteAddress.getSiteCity());
					attributeAMap.put("destinationState", siteAddress.getSiteState());
					attributeAMap.put("destinationCountry", siteAddress.getSiteCountry());
					attributeAMap.put("destinationPincode", siteAddress.getSitePincode());
					attributeAMap.put("siteAddress",
							StringUtils.trimToEmpty(siteAddress.getSiteAddressLineOne()) + " "
									+ StringUtils.trimToEmpty(siteAddress.getSiteAddressLineTwo()) + " "
									+ StringUtils.trimToEmpty(siteAddress.getSiteAddressLineThree()) + " "
									+ StringUtils.trimToEmpty(siteAddress.getSiteCity()) + " "
									+ StringUtils.trimToEmpty(siteAddress.getSiteState()) + " "
									+ StringUtils.trimToEmpty(siteAddress.getSiteCountry()) + " "
									+ StringUtils.trimToEmpty(siteAddress.getSitePincode()));
					if (contractingAddress.getContractingCountry() != null && (contractingAddress
							.getContractingCountry().equalsIgnoreCase("Canada")
							|| contractingAddress.getContractingCountry().toLowerCase().contains("united states"))) {
						attributeAMap.put("geoCode", sureTaxService.generateGeoCode(copyAddress(siteAddress)));
					}
					

				}
				if (contractingAddress != null) {
					ScOrderAttribute noticeAddress = scOrderAttributeRepository
							.findFirstByAttributeNameAndScOrder(LeAttributesConstants.NOTICE_ADDRESS, scOrder);
					if (noticeAddress != null) {
						noticeAddress.setAttributeValue(getNoticeAddress(contractingAddress));
						noticeAddress.setUpdatedDate(new Timestamp(new Date().getTime()));
					} else {
						noticeAddress = new ScOrderAttribute();
						noticeAddress.setAttributeName(LeAttributesConstants.NOTICE_ADDRESS);
						noticeAddress.setAttributeValue(getNoticeAddress(contractingAddress));
						noticeAddress.setAttributeAltValueLabel(LeAttributesConstants.NOTICE_ADDRESS);
						noticeAddress.setScOrder(scOrder);
						noticeAddress.setIsActive("Y");
						noticeAddress.setCreatedDate(new Timestamp(new Date().getTime()));
					}
					scOrderAttributeRepository.save(noticeAddress);
					if (contractingAddress.getContractingCountry().equalsIgnoreCase("Canada")
							|| contractingAddress.getContractingCountry().toLowerCase().contains("united states")
							|| (contractingAddress.getContractingCountry().equalsIgnoreCase("China")
									&& contractingAddress.getContractingCity().equalsIgnoreCase("Hong Kong"))) {
						attributeAMap.put("isTaxCaptureRequired", "N");
					}else {
						attributeAMap.put("isTaxCaptureRequired", "Y");
					}
				
				}
				updateAttributes(scServiceDetail.get().getId(), attributeAMap, AttributeConstants.COMPONENT_LM, "A");

			}
		}
		return "Gst Number & Address Updated";
	}
	
	public String contractAddress(ScOrder scOrder) {

		Map<String, String> contractingAddressMap = commonFulfillmentUtils
				.getScOrdertAttributesDetails(Arrays.asList("ContractingAddressLineOne", "ContractingAddressLineTwo",
						"ContractingAddressLineThree", "ContractingAddressCity", "ContractingAddressState",
						"ContractingAddressCountry", "ContractingAddressPincode"), scOrder);
		ContractingAddress contractingAddress = new ContractingAddress();
		if (contractingAddressMap != null) {
			contractingAddress.setContractingAddressOne(contractingAddressMap.get("ContractingAddressLineOne"));
			contractingAddress.setContractingAddressTwo(contractingAddressMap.get("ContractingAddressLineTwo"));
			contractingAddress.setContractingAddressThree(contractingAddressMap.get("ContractingAddressLineThree"));
			contractingAddress.setContractingCity(contractingAddressMap.get("ContractingAddressCity"));
			contractingAddress.setContractingState(contractingAddressMap.get("ContractingAddressState"));
			contractingAddress.setContractingCountry(contractingAddressMap.get("ContractingAddressCountry"));
			contractingAddress.setContractingPincode(contractingAddressMap.get("ContractingAddressPincode"));
		}
		return contractingAddress.toString();
	}

	public String getNoticeAddress(ContractingAddress contractingAddress) {

		return StringUtils.trimToEmpty(contractingAddress.getContractingAddressOne()) + " "
				+ StringUtils.trimToEmpty(contractingAddress.getContractingAddressTwo()) + " "
				+ StringUtils.trimToEmpty(contractingAddress.getContractingAddressThree()) + " "
				+ StringUtils.trimToEmpty(contractingAddress.getContractingCity()) + " "
				+ StringUtils.trimToEmpty(contractingAddress.getContractingState()) + " "
				+ StringUtils.trimToEmpty(contractingAddress.getContractingCountry()) + " "
				+ StringUtils.trimToEmpty(contractingAddress.getContractingPincode());
	}
	
	/**
	 * @author Yogesh
	 * @param serviceId
	 * @param atMap
	 * @param componentName used to update attributes
	 */
	public void updateAttributes(Integer serviceId, Map<String, String> atMap, String componentName,String siteType) {

		ScComponent scComponent = scComponentRepository
				.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(serviceId, componentName,siteType);

		String userName = Utils.getSource();
		if (scComponent != null) {
			atMap.forEach((key, value) -> {
				ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceId, key, componentName, siteType);
				if (scComponentAttribute == null) {
					createComponentAttr(key, value, serviceId, scComponent, userName);
				} else {
					updateComponentAttr(key, value, scComponentAttribute, userName);
				}

			});
		}

	}
	
	/**
	 * @author Yogesh
	 * @param attrName
	 * @param attrValue
	 * @param scServiceDetail
	 * @param scComponent
	 * @param userName        used to create attribute
	 */
	private void createComponentAttr(String attrName, String attrValue, Integer serviceId, ScComponent scComponent,
									 String userName) {
		if(Objects.nonNull(attrValue)) {
			ScComponentAttribute attribute = new ScComponentAttribute();
			attribute.setAttributeName(attrName);
			attribute.setAttributeValue(attrValue);
			attribute.setAttributeAltValueLabel(attrName);
			attribute.setScComponent(scComponent);
			attribute.setScServiceDetailId(serviceId);
			attribute.setIsActive("Y");
			attribute.setCreatedDate(new Timestamp(new Date().getTime()));
			attribute.setCreatedBy(userName);
			scComponentAttributesRepository.save(attribute);
		}
	}
	
	
	/**
	 * @author Yogesh
	 * @param attrName
	 * @param attrValue
	 * @param scComponentAttribute
	 * @param userName  used to update component attr
	 */
	private void updateComponentAttr(String attrName, String attrValue, ScComponentAttribute scComponentAttribute,
			String userName) {

		if (!attrValue.equals(scComponentAttribute.getAttributeValue())) {
			scComponentAttribute.setAttributeName(attrName);
			scComponentAttribute.setAttributeValue(attrValue);
			scComponentAttribute.setAttributeAltValueLabel(attrValue);
			scComponentAttribute.setUpdatedBy(userName);
			scComponentAttributesRepository.save(scComponentAttribute);
		}
	}
	
	private GeoCodeAddress copyAddress(SiteAddress siteAddress) {
		GeoCodeAddress geoCodeAddress = new GeoCodeAddress();
		geoCodeAddress.setAddressLineOne(siteAddress.getSiteAddressLineOne());
		geoCodeAddress.setAddressLineTwo(siteAddress.getSiteAddressLineTwo());
		geoCodeAddress.setAddressLineThree(siteAddress.getSiteAddressLineThree());
		geoCodeAddress.setAddressCity(siteAddress.getSiteCity());
		geoCodeAddress.setAddressState(siteAddress.getSiteState());
		geoCodeAddress.setAddressCountry(siteAddress.getSiteCountry());
		geoCodeAddress.setAddressZipCode(siteAddress.getSitePincode());
		return geoCodeAddress;
	}
}
