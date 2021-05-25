package com.tcl.dias.servicehandover.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.LeStateInfo;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.Attachment;
import com.tcl.dias.servicefulfillment.entity.entities.MstBillingStatesAndCities;
import com.tcl.dias.servicefulfillment.entity.entities.ScAttachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScGstAddress;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.MstBillingStatesAndCitiesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScGstAddressRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.AccountInputData;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.RenewalCommercialVettingDetails;
import com.tcl.dias.servicefulfillmentutils.beans.SiteBBillingAddress;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.FlowableBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.dias.servicehandover.beans.renewal.RenewalAttachments;
import com.tcl.dias.servicehandover.beans.renewal.RenewalContractSiteDetails;
import com.tcl.dias.servicehandover.beans.renewal.RenewalValidateDocumentDetails;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.createorder.AccountCreationInputBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.ProductCreationInputBO;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component
public class AddressConstructor extends ServiceFulfillmentBaseService {

	@Autowired
	MstBillingStatesAndCitiesRepository mstBillingStatesAndCitiesRepository;

	@Autowired
	CustomerAndSupplierAddress customerAndSupplierAddress;

	Map<String, String> locationDataMap;

	@Autowired
	LoadCustomerDetails loadCustomerDetails;

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

	String contractGstno = "";

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	ScGstAddressRepository gstAddressRepository;

	@Autowired
	ScComponentRepository scComponentRepository;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	FlowableBaseService flowableBaseService;

	private static final Logger LOGGER = LoggerFactory.getLogger(AddressConstructor.class);

	public AccountCreationInputBO accountAddress(ScContractInfo scContractInfo,
			AccountCreationInputBO accountCreationInputBO) {
		accountCreationInputBO
				.setAccAddr1(StringUtils.trimToEmpty(scContractInfo.getBillingAddressLine1()).length() >= 120
						? scContractInfo.getBillingAddressLine1().substring(0, 120)
						: scContractInfo.getBillingAddressLine1());
		accountCreationInputBO
				.setAccAddr2(StringUtils.trimToEmpty(scContractInfo.getBillingAddressLine2()).length() >= 120
						? scContractInfo.getBillingAddressLine2().substring(0, 120)
						: scContractInfo.getBillingAddressLine2());

		accountCreationInputBO
				.setAccAddr3(StringUtils.trimToEmpty(scContractInfo.getBillingAddressLine3()).length() >= 120
						? scContractInfo.getBillingAddressLine3().substring(0, 120)
						: scContractInfo.getBillingAddressLine3());
		accountCreationInputBO
				.setAccCity(getLocationData(locationDataMap, StringUtils.trimToEmpty(scContractInfo.getBillingCity())));
		accountCreationInputBO.setAccState(
				getLocationData(locationDataMap, StringUtils.trimToEmpty(scContractInfo.getBillingState())));
		accountCreationInputBO.setAccCountry(
				getLocationData(locationDataMap, StringUtils.trimToEmpty(scContractInfo.getBillingCountry())));
		accountCreationInputBO.setAccZipcode(StringUtils.trimToEmpty(scContractInfo.getBillingPincode()));
		return accountCreationInputBO;
	}

	public ProductCreationInputBO productAddress(ScServiceDetail scServiceDetail,
			ProductCreationInputBO productCreationInputBO) {

		Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("destinationCity", "destinationState", "destinationCountry", "destinationAddressLineOne",
						"destinationAddressLineTwo", "destinationLocality", "destinationPincode"),
				scServiceDetail.getId(), "LM", "A");

		String destinationAddressLineOne = StringUtils
				.trimToEmpty(scComponentAttributesAMap.get("destinationAddressLineOne"));
		String destinationAddressLineTwo = StringUtils
				.trimToEmpty(scComponentAttributesAMap.get("destinationAddressLineTwo"));
		String destinationLocality = StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationLocality"));
		String destinationCity = StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity"));
		String destinationState = StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState"));
		String destinationCountry = StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry"));
		String destinationPincode = StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationPincode"));

		productCreationInputBO
				.setProdAddr1(destinationAddressLineOne.length() >= 120 ? destinationAddressLineOne.substring(0, 120)
						: destinationAddressLineOne);
		productCreationInputBO
				.setProdAddr2(destinationAddressLineTwo.length() >= 120 ? destinationAddressLineTwo.substring(0, 120)
						: destinationAddressLineTwo);
		productCreationInputBO.setProdAddr3(StringUtils.trimToEmpty(destinationLocality));
		productCreationInputBO.setProdCity(getLocationData(locationDataMap, StringUtils.trimToEmpty(destinationCity)));
		productCreationInputBO
				.setProdState(getLocationData(locationDataMap, StringUtils.trimToEmpty(destinationState)));
		productCreationInputBO.setProdCountry(StringUtils.trimToEmpty(destinationCountry));
		if (productCreationInputBO.getProdCountry().toLowerCase().contains("united states")) {
			productCreationInputBO.setProdCountry("United States");
		}
		productCreationInputBO.setProdZipcode(StringUtils.trimToEmpty(destinationPincode));

		return productCreationInputBO;

	}

	public String contractAddress(String customerAddressLocationId) {
		AddressDetail addressDetail = customerAndSupplierAddress.getAddressLocationId(customerAddressLocationId);
		ContractingAddress contractingAddress = new ContractingAddress();
		contractingAddress.setContractingAddressOne(StringUtils.trimToEmpty(addressDetail.getAddressLineOne()));
		contractingAddress.setContractingAddressTwo(StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()));
		contractingAddress.setContractingAddressThree(StringUtils.trimToEmpty(addressDetail.getLocality()));
		contractingAddress
				.setContractingCity(getLocationData(locationDataMap, StringUtils.trimToEmpty(addressDetail.getCity())));
		contractingAddress.setContractingState(
				getLocationData(locationDataMap, StringUtils.trimToEmpty(addressDetail.getState())));
		contractingAddress.setContractingCountry(StringUtils.trimToEmpty(addressDetail.getCountry()));
		contractingAddress.setContractingPincode(StringUtils.trimToEmpty(addressDetail.getPincode()));
		return contractingAddress.toString();
	}

	public String getNoticeAddress(String customerAddressLocationId) {
		AddressDetail addressDetail = customerAndSupplierAddress.getAddressLocationId(customerAddressLocationId);
		if (addressDetail != null) {
			return StringUtils.trimToEmpty(addressDetail.getAddressLineOne()) + " "
					+ StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()) + " "
					+ StringUtils.trimToEmpty(addressDetail.getLocality()) + " "
					+ StringUtils.trimToEmpty(addressDetail.getCity()) + " "
					+ StringUtils.trimToEmpty(addressDetail.getState()) + " "
					+ StringUtils.trimToEmpty(addressDetail.getCountry()) + " "
					+ StringUtils.trimToEmpty(addressDetail.getPincode());
		}
		return "";
	}

	public String contractGstAddress(String GstNo, Integer custLeId, ScOrder order) {
		if (order.getScGstAddress() != null && !"No Registered GST".equalsIgnoreCase(GstNo)) {
			ContractGstAddress contractGstAddress = new ContractGstAddress();
			contractGstAddress.setContractGstinAddressOne(order.getScGstAddress().getBuildingName());
			contractGstAddress.setContractGstinAddressTwo(order.getScGstAddress().getStreet());
			contractGstAddress
					.setContractGstinAddressThree(StringUtils.trimToEmpty(order.getScGstAddress().getLocality()));
			contractGstAddress
					.setContractGstinAddressCity(StringUtils.trimToEmpty(order.getScGstAddress().getDistrict()));
			contractGstAddress
					.setContractGstinAddressState(StringUtils.trimToEmpty(order.getScGstAddress().getState()));
			contractGstAddress.setContractGstinAddressCountry(NetworkConstants.INDIA);
			contractGstAddress
					.setContractGstinAddressPincode(StringUtils.trimToEmpty(order.getScGstAddress().getPincode()));
			return contractGstAddress.toString();
		} else {
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

	public String siteGstAddress(ScServiceDetail scServiceDetail, String serviceType, String siteType) { // String
																											// GstNo,
																											// Integer
																											// custLeId)
																											// {
		LOGGER.info("Site Gst Construction started for service type {}", serviceType);
		Map<String, String> siteGstMap = commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("siteGstAddressId", "siteGstNumber"), scServiceDetail.getId(), "LM", siteType);
		if (siteGstMap != null && !"No Registered GST".equalsIgnoreCase(siteGstMap.get("siteGstNumber"))
				&& siteGstMap.get("siteGstAddressId") != null) {
			ScGstAddress scGstAddress = gstAddressRepository
					.findById(Integer.parseInt(siteGstMap.get("siteGstAddressId"))).get();
			if (scGstAddress != null) {
				LOGGER.info("Site Gst from component attributes with id {}", siteGstMap.get("siteGstAddressId"));
				SiteGstAddress siteGstAddress = new SiteGstAddress();
				String addressLineOne = StringUtils.trimToEmpty(scGstAddress.getFlatNumber()) + " "
						+ StringUtils.trimToEmpty(scGstAddress.getBuildingNumber()) + " "
						+ StringUtils.trimToEmpty(scGstAddress.getBuildingName());
				String addressLineTwo = StringUtils.trimToEmpty(scGstAddress.getStreet());
				siteGstAddress.setSiteGstinAddressOne(addressLineOne);
				if (addressLineTwo.equals(scGstAddress.getLocality())) {
					siteGstAddress.setSiteGstinAddressTwo(addressLineTwo);
					siteGstAddress.setSiteGstinAddressThree("");
				} else {
					siteGstAddress.setSiteGstinAddressTwo(addressLineTwo);
					siteGstAddress.setSiteGstinAddressThree(StringUtils.trimToEmpty(scGstAddress.getLocality()));
				}
				siteGstAddress.setSiteGstinAddressCity(StringUtils.trimToEmpty(scGstAddress.getDistrict()));
				siteGstAddress.setSiteGstinAddressState(StringUtils.trimToEmpty(scGstAddress.getState()));
				siteGstAddress.setSiteGstinAddressCountry(NetworkConstants.INDIA);
				siteGstAddress.setSiteGstinAddressPincode(StringUtils.trimToEmpty(scGstAddress.getPincode()));
				return siteGstAddress.toString(serviceType);
			}

		}
		return "";
	}

	public String siteGstAddressForCpe(ScServiceDetail scServiceDetail) { // String GstNo, Integer custLeId) {
		Map<String, String> siteGstMap = commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("siteGstAddressId", "siteGstNumber"), scServiceDetail.getId(), "LM", "A");
		if (siteGstMap != null && !"No Registered GST".equalsIgnoreCase(siteGstMap.get("siteGstNumber"))
				&& siteGstMap.get("siteGstAddressId") != null) {
			ScGstAddress scGstAddress = gstAddressRepository
					.findById(Integer.parseInt(siteGstMap.get("siteGstAddressId"))).get();
			if (scGstAddress != null) {
				LOGGER.info("Site Gst Cpe from component attributes with id {}", siteGstMap.get("siteGstAddressId"));
				SiteGstAddressCpe siteGstAddressCpe = new SiteGstAddressCpe();
				String addressLineOne = StringUtils.trimToEmpty(scGstAddress.getFlatNumber()) + " "
						+ StringUtils.trimToEmpty(scGstAddress.getBuildingNumber()) + " "
						+ StringUtils.trimToEmpty(scGstAddress.getBuildingName());
				String addressLineTwo = StringUtils.trimToEmpty(scGstAddress.getStreet());
				siteGstAddressCpe.setSiteGstinAddressOne(addressLineOne);
				if (addressLineTwo.equals(scGstAddress.getLocality())) {
					siteGstAddressCpe.setSiteGstinAddressTwo(addressLineTwo);
					siteGstAddressCpe.setSiteGstinAddressThree("");
				} else {
					siteGstAddressCpe.setSiteGstinAddressTwo(addressLineTwo);
					siteGstAddressCpe.setSiteGstinAddressThree(StringUtils.trimToEmpty(scGstAddress.getLocality()));
				}
				siteGstAddressCpe.setSiteGstinAddressCity(StringUtils.trimToEmpty(scGstAddress.getDistrict()));
				siteGstAddressCpe.setSiteGstinAddressState(StringUtils.trimToEmpty(scGstAddress.getState()));
				siteGstAddressCpe.setSiteGstinAddressCountry(NetworkConstants.INDIA);
				siteGstAddressCpe.setSiteGstinAddressPincode(StringUtils.trimToEmpty(scGstAddress.getPincode()));
				return siteGstAddressCpe.toString();
			}
		}
		return "";
	}

	public String siteAddressGen(ScServiceDetail serviceDetail, String siteType) {
		String siteFullAddress = "";
		Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("destinationCity", "destinationState", "destinationCountry", "destinationAddressLineOne",
						"destinationAddressLineTwo", "destinationLocality", "destinationPincode"),
				serviceDetail.getId(), "LM", siteType);

		String addressFull = StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationAddressLineOne")) + " "
				+ StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationAddressLineTwo")) + " "
				+ StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationLocality"));

		String destinationAddressLineOne = "";
		String destinationAddressLineTwo = "";
		String destinationLocality = "";

		String destinationCity = StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity"));
		String destinationState = StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState"));
		String destinationCountry = StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry"));
		String destinationPincode = StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationPincode"));

		if (addressFull.length() <= 80) {
			destinationAddressLineOne = addressFull.substring(0, addressFull.length());
		}
		if (addressFull.length() >= 81 && addressFull.length() <= 160) {
			destinationAddressLineOne = addressFull.substring(0, 80);
			destinationAddressLineTwo = addressFull.substring(80, addressFull.length());
		}
		if (addressFull.length() >= 161 && addressFull.length() <= 240) {
			destinationAddressLineOne = addressFull.substring(0, 80);
			destinationAddressLineTwo = addressFull.substring(80, 160);
			destinationLocality = addressFull.substring(160, addressFull.length());
		}
		if (addressFull.length() > 240) {
			destinationAddressLineOne = addressFull.substring(0, 80);
			destinationAddressLineTwo = addressFull.substring(80, 160);
			destinationLocality = addressFull.substring(160, 240);
		}

		if (siteType.equals("A")) {
			SiteAddress siteAddress = new SiteAddress();
			siteAddress.setSiteAddressLineOne(destinationAddressLineOne);
			siteAddress.setSiteAddressLineTwo(destinationAddressLineTwo);
			siteAddress.setSiteAddressLineThree(destinationLocality);
			siteAddress.setSiteCity(destinationCity);
			siteAddress.setSiteState(destinationState);// getLocationData(locationDataMap, destinationState));
			siteAddress.setSitePincode(destinationPincode);
			siteAddress.setSiteCountry(WordUtils.capitalizeFully(destinationCountry));
			if (siteAddress.getSiteCountry().toLowerCase().contains("united states")) {
				siteAddress.setSiteCountry("United States");
			}
			siteFullAddress = siteAddress.toString();
		} else {
			SiteBAddress siteBddress = new SiteBAddress();
			siteBddress.setSiteBAddressLine1(destinationAddressLineOne);
			siteBddress.setSiteBAddressLine2(destinationAddressLineTwo);
			siteBddress.setSiteBAddressLine3(destinationLocality);
			siteBddress.setSiteBCity(destinationCity);
			siteBddress.setSiteBState(destinationState);
			siteBddress.setSiteBPincode(destinationPincode);
			siteBddress.setSiteBCountry(WordUtils.capitalizeFully(destinationCountry));
			siteFullAddress = siteBddress.toString();

		}

		return siteFullAddress;
	}

	public String siteGstAddressForCommVetting(ScServiceDetail scServiceDetail) {
		if (scServiceDetail.getScGstAddress() != null) {
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

			return addressLineOne + " " + addressLineTwo + " " + addressLinethree + " " + addressCity + " "
					+ addressState + " " + addressCountry + " " + addressPincode;
		} else {
			return "";
		}
	}

	public String siteGstAddressForNpl(ScServiceDetail scServiceDetail, String siteType) {

		Map<String, String> siteGstMap = commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("siteGstAddressId"), scServiceDetail.getId(), "LM", siteType);
		if (siteGstMap != null) {
			Optional<ScGstAddress> siteGstAddressA = gstAddressRepository
					.findById(Integer.parseInt(siteGstMap.getOrDefault("siteGstAddressId", "0")));
			if (siteGstAddressA.isPresent()) {
				String addressLineOne = StringUtils.trimToEmpty(siteGstAddressA.get().getFlatNumber()) + " "
						+ StringUtils.trimToEmpty(siteGstAddressA.get().getBuildingNumber()) + " "
						+ StringUtils.trimToEmpty(siteGstAddressA.get().getBuildingName());
				String addressLineTwo = StringUtils.trimToEmpty(siteGstAddressA.get().getStreet());
				String addressLinethree = "";
				if (!addressLineTwo.equals(siteGstAddressA.get().getLocality())) {
					addressLinethree = StringUtils.trimToEmpty(siteGstAddressA.get().getLocality());
				}
				String addressCity = StringUtils.trimToEmpty(siteGstAddressA.get().getDistrict());
				String addressState = StringUtils.trimToEmpty(siteGstAddressA.get().getState());
				String addressCountry = NetworkConstants.INDIA;
				String addressPincode = StringUtils.trimToEmpty(siteGstAddressA.get().getPincode());

				return addressLineOne + " " + addressLineTwo + " " + addressLinethree + " " + addressCity + " "
						+ addressState + " " + addressCountry + " " + addressPincode;
			}
		} else {
			return "";
		}
		return "";
	}

	public AccountInputData accountAddress(ScContractInfo scContractInfo, AccountInputData accountInputData,
			boolean isSiteWiseBilling, String serviceId, boolean isTwoAccountsRequired) {

		String addressLineOne = "";
		String addressLineTwo = "";
		String addressLineThree = "";
		String addressCity = "";
		String addressState = "";
		String addressCountry = "";
		String addressZipCode = "";
		LOGGER.info("Site Wise Billing is {} ", isSiteWiseBilling);
		if (isSiteWiseBilling) {
			Map<String, String> billingAddressAttributes = commonFulfillmentUtils
					.getComponentAttributesDetails(Arrays.asList("billingAddressLineOne", "billingAddressLineTwo",
							"billingAddressLineThree", "billingAddressCity", "billingAddressState",
							"billingAddressCountry", "billingAddressPincode"), Integer.parseInt(serviceId), "LM", "A");
			if (billingAddressAttributes != null) {

				addressLineOne = StringUtils.trimToEmpty(billingAddressAttributes.get("billingAddressLineOne"));
				addressLineTwo = StringUtils.trimToEmpty(billingAddressAttributes.get("billingAddressLineTwo"));
				addressLineThree = StringUtils.trimToEmpty(billingAddressAttributes.get("billingAddressLineThree"));
				addressCity = StringUtils.trimToEmpty(billingAddressAttributes.get("billingAddressCity"));
				addressState = StringUtils.trimToEmpty(billingAddressAttributes.get("billingAddressState"));
				addressCountry = StringUtils.trimToEmpty(billingAddressAttributes.get("billingAddressCountry"));
				addressZipCode = StringUtils.trimToEmpty(billingAddressAttributes.get("billingAddressPincode"));

			}
			LOGGER.info("isTwoAccountsRequired {}", isTwoAccountsRequired);
			if (isTwoAccountsRequired) {
				Map<String, String> billingAddressAttributesB = commonFulfillmentUtils
						.getComponentAttributesDetails(
								Arrays.asList("billingAddressLineOne", "billingAddressLineTwo",
										"billingAddressLineThree", "billingAddressCity", "billingAddressState",
										"billingAddressCountry", "billingAddressPincode"),
								Integer.parseInt(serviceId), "LM", "B");
				if (billingAddressAttributesB != null) {
					SiteBBillingAddress siteBBillingAddress = new SiteBBillingAddress();
					siteBBillingAddress.setSiteBAccountAddr1(
							StringUtils.trimToEmpty(billingAddressAttributesB.get("billingAddressLineOne")));
					siteBBillingAddress.setSiteBAccountAddr2(
							StringUtils.trimToEmpty(billingAddressAttributesB.get("billingAddressLineTwo")));
					siteBBillingAddress.setSiteBAccountAddr3(
							StringUtils.trimToEmpty(billingAddressAttributesB.get("billingAddressLineThree")));
					siteBBillingAddress.setSiteBAccountAddrCity(
							StringUtils.trimToEmpty(billingAddressAttributesB.get("billingAddressCity")));
					siteBBillingAddress.setSiteBAccountAddrState(
							StringUtils.trimToEmpty(billingAddressAttributesB.get("billingAddressState")));
					siteBBillingAddress.setSiteBAccountAddrCountry(
							StringUtils.trimToEmpty(billingAddressAttributesB.get("billingAddressCountry")));
					siteBBillingAddress.setSiteBAccountAddrZipCode(
							StringUtils.trimToEmpty(billingAddressAttributesB.get("billingAddressPincode")));
					accountInputData.setSiteBBillingAddress(siteBBillingAddress);

				}
			}

		} else {

			addressLineOne = StringUtils.trimToEmpty(scContractInfo.getBillingAddressLine1());
			addressLineTwo = StringUtils.trimToEmpty(scContractInfo.getBillingAddressLine2());
			addressLineThree = StringUtils.trimToEmpty(scContractInfo.getBillingAddressLine3());
			addressCity = StringUtils.trimToEmpty(scContractInfo.getBillingCity());
			addressState = StringUtils.trimToEmpty(scContractInfo.getBillingState());
			addressCountry = StringUtils.trimToEmpty(scContractInfo.getBillingCountry());
			addressZipCode = StringUtils.trimToEmpty(scContractInfo.getBillingPincode());
		}

		accountInputData
				.setAccountAddr1(addressLineOne.length() >= 120 ? addressLineOne.substring(0, 120) : addressLineOne);
		accountInputData
				.setAccountAddr2(addressLineTwo.length() >= 120 ? addressLineTwo.substring(0, 120) : addressLineTwo);
		accountInputData.setAccountAddr3(
				addressLineThree.length() >= 120 ? addressLineThree.substring(0, 120) : addressLineThree);
		accountInputData.setAccountAddrCity(getLocationData(locationDataMap, addressCity));
		accountInputData.setAccountAddrState(getLocationData(locationDataMap, addressState));
		accountInputData.setAccountAddrCountry(addressCountry);
		accountInputData.setAccountAddrZipCode(addressZipCode);

		return accountInputData;
	}

	@PostConstruct
	public Map<String, String> locationMap() {
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

	public ContractSiteGstAddress getGstAddress(String taskId) {
		Task task = getTaskById(Integer.parseInt(taskId));
		ContractSiteGstAddress contractSiteGstAddress = new ContractSiteGstAddress();
		LOGGER.info("Get Gst Address Started {}", LocalTime.now());
		if (task != null) {
			ScOrder scOrder = orderRepository.findById(task.getScOrderId()).get();
			if (scOrder != null) {
				LOGGER.info("Loading order repository started {}", LocalTime.now());
				ScOrderAttribute scOrderAttribute = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.LE_STATE_GST_NO, scOrder);
				if (scOrderAttribute != null) {
					contractGstno = scOrderAttribute.getAttributeValue();
				}
				LOGGER.info("Loading order repository completed{}", LocalTime.now());
				if (scOrder.getScGstAddress() != null) {
					ContractGstAddress contractGstAddress = new ContractGstAddress();
					contractGstAddress.setContractGstinAddressOne(scOrder.getScGstAddress().getBuildingName());
					contractGstAddress.setContractGstinAddressTwo(scOrder.getScGstAddress().getStreet());
					contractGstAddress.setContractGstinAddressThree(scOrder.getScGstAddress().getLocality());
					contractGstAddress.setContractGstinAddressCity(scOrder.getScGstAddress().getDistrict());
					contractGstAddress.setContractGstinAddressState(scOrder.getScGstAddress().getState().toUpperCase());
					contractGstAddress.setContractGstinAddressCountry(NetworkConstants.INDIA);
					contractGstAddress.setContractGstinAddressPincode(scOrder.getScGstAddress().getPincode());
					contractGstAddress.setContractGstNumber(contractGstno);
					contractSiteGstAddress.setContractGstAddress(contractGstAddress);
				} else {
					ContractGstAddress contractGstAddress = new ContractGstAddress();
					contractGstAddress.setContractGstNumber(contractGstno);
					contractGstAddress.setContractGstinAddressCountry(NetworkConstants.INDIA);
					contractSiteGstAddress.setContractGstAddress(contractGstAddress);
				}
				if (NetworkConstants.NPL.equals(task.getServiceType())) {
					Map<String, String> siteBAddressMap = commonFulfillmentUtils.getComponentAttributesDetails(
							Arrays.asList("siteGstAddressId", "siteGstNumber", "destinationAddressLineOne",
									"destinationAddressLineTwo", "destinationLocality", "destinationCity",
									"destinationState", "destinationCountry", "destinationPincode"),
							task.getServiceId(), "LM", "B");
					if (siteBAddressMap != null) {
						SiteGstAddress siteGstAddress = new SiteGstAddress();
						if (siteBAddressMap.get("siteGstAddressId") != null) {
							LOGGER.info("Get Site B Gst Address Started {}", LocalTime.now());
							ScGstAddress siteGstAddressA = gstAddressRepository
									.findById(Integer.parseInt(siteBAddressMap.get("siteGstAddressId"))).get();
							LOGGER.info("Get Site B Gst Address Started {}", LocalTime.now());
							if (siteGstAddressA != null) {
								String addressLineOne = StringUtils.trimToEmpty(siteGstAddressA.getFlatNumber()) + " "
										+ StringUtils.trimToEmpty(siteGstAddressA.getBuildingNumber()) + " "
										+ StringUtils.trimToEmpty(siteGstAddressA.getBuildingName());
								String addressLineTwo = StringUtils.trimToEmpty(siteGstAddressA.getStreet());
								siteGstAddress.setSiteGstinAddressOne(addressLineOne);
								if (addressLineTwo.equals(siteGstAddressA.getLocality())) {
									siteGstAddress.setSiteGstinAddressTwo(addressLineTwo);
									siteGstAddress.setSiteGstinAddressThree("");
								} else {
									siteGstAddress.setSiteGstinAddressTwo(addressLineTwo);
									siteGstAddress.setSiteGstinAddressThree(
											StringUtils.trimToEmpty(siteGstAddressA.getLocality()));
								}
								siteGstAddress.setSiteGstinAddressCity(
										StringUtils.trimToEmpty(siteGstAddressA.getDistrict()));
								siteGstAddress.setSiteGstinAddressState(
										StringUtils.trimToEmpty(siteGstAddressA.getState()).toUpperCase());
								siteGstAddress.setSiteGstinAddressCountry(NetworkConstants.INDIA);
								siteGstAddress.setSiteGstinAddressPincode(
										StringUtils.trimToEmpty(siteGstAddressA.getPincode()));
								siteGstAddress.setSiteGstNumber(siteBAddressMap.get("siteGstNumber"));
								contractSiteGstAddress.setSiteGstAddressB(siteGstAddress);
							}

						} else {
							String gstNumber = siteBAddressMap.get("siteGstNumber");
							siteGstAddress.setSiteGstNumber(gstNumber != null ? gstNumber : "No Registered GST");
							contractSiteGstAddress.setSiteGstAddressB(siteGstAddress);
						}

						SiteAddress siteBAddress = new SiteAddress();
						siteBAddress.setSiteAddressLineOne(
								StringUtils.trimToEmpty(siteBAddressMap.get("destinationAddressLineOne")));
						siteBAddress.setSiteAddressLineTwo(
								StringUtils.trimToEmpty(siteBAddressMap.get("destinationAddressLineTwo")));
						siteBAddress.setSiteAddressLineThree(
								StringUtils.trimToEmpty(siteBAddressMap.get("destinationLocality")));
						siteBAddress.setSiteCity(StringUtils.trimToEmpty(siteBAddressMap.get("destinationCity")));
						siteBAddress.setSiteState(
								StringUtils.trimToEmpty(siteBAddressMap.get("destinationState")).toUpperCase());
						siteBAddress.setSiteCountry(NetworkConstants.INDIA);
						siteBAddress.setSitePincode(StringUtils.trimToEmpty(siteBAddressMap.get("destinationPincode")));
						contractSiteGstAddress.setSiteBAddress(siteBAddress);

					}
				}

				Map<String, String> siteAAddressMap = commonFulfillmentUtils.getComponentAttributesDetails(
						Arrays.asList("siteGstAddressId", "siteGstNumber", "destinationAddressLineOne",
								"destinationAddressLineTwo", "destinationLocality", "destinationCity",
								"destinationState", "destinationCountry", "destinationPincode"),
						task.getServiceId(), "LM", "A");

				if (siteAAddressMap != null) {
					SiteGstAddress siteGstAddress = new SiteGstAddress();
					if (siteAAddressMap.get("siteGstAddressId") != null) {
						LOGGER.info("Get Site A Gst Address Started {}", LocalTime.now());
						Optional<ScGstAddress> siteGstAddressAOpt = gstAddressRepository
								.findById(Integer.parseInt(siteAAddressMap.get("siteGstAddressId")));
						LOGGER.info("Get Site A Gst Address Ended {}", LocalTime.now());
						if (siteGstAddressAOpt.isPresent()) {
							ScGstAddress siteGstAddressA = siteGstAddressAOpt.get();
							String addressLineOne = StringUtils.trimToEmpty(siteGstAddressA.getFlatNumber()) + " "
									+ StringUtils.trimToEmpty(siteGstAddressA.getBuildingNumber()) + " "
									+ StringUtils.trimToEmpty(siteGstAddressA.getBuildingName());
							String addressLineTwo = StringUtils.trimToEmpty(siteGstAddressA.getStreet());
							siteGstAddress.setSiteGstinAddressOne(addressLineOne);
							if (addressLineTwo.equals(siteGstAddressA.getLocality())) {
								siteGstAddress.setSiteGstinAddressTwo(addressLineTwo);
								siteGstAddress.setSiteGstinAddressThree("");
							} else {
								siteGstAddress.setSiteGstinAddressTwo(addressLineTwo);
								siteGstAddress.setSiteGstinAddressThree(
										StringUtils.trimToEmpty(siteGstAddressA.getLocality()));
							}
							siteGstAddress
									.setSiteGstinAddressCity(StringUtils.trimToEmpty(siteGstAddressA.getDistrict()));
							siteGstAddress.setSiteGstinAddressState(
									StringUtils.trimToEmpty(siteGstAddressA.getState()).toUpperCase());
							siteGstAddress.setSiteGstinAddressCountry(NetworkConstants.INDIA);
							siteGstAddress
									.setSiteGstinAddressPincode(StringUtils.trimToEmpty(siteGstAddressA.getPincode()));
							siteGstAddress.setSiteGstNumber(siteAAddressMap.get("siteGstNumber"));
							contractSiteGstAddress.setSiteGstAddress(siteGstAddress);
						}

					} else {
						String gstNumber = siteAAddressMap.get("siteGstNumber");
						siteGstAddress.setSiteGstNumber(gstNumber != null ? gstNumber : "No Registered GST");
						contractSiteGstAddress.setSiteGstAddress(siteGstAddress);
					}

					SiteAddress siteAAddress = new SiteAddress();
					siteAAddress.setSiteAddressLineOne(
							StringUtils.trimToEmpty(siteAAddressMap.get("destinationAddressLineOne")));
					siteAAddress.setSiteAddressLineTwo(
							StringUtils.trimToEmpty(siteAAddressMap.get("destinationAddressLineTwo")));
					siteAAddress.setSiteAddressLineThree(
							StringUtils.trimToEmpty(siteAAddressMap.get("destinationLocality")));
					siteAAddress.setSiteCity(StringUtils.trimToEmpty(siteAAddressMap.get("destinationCity")));
					siteAAddress.setSiteState(
							StringUtils.trimToEmpty(siteAAddressMap.get("destinationState")).toUpperCase());
					siteAAddress.setSiteCountry(NetworkConstants.INDIA);
					siteAAddress.setSitePincode(StringUtils.trimToEmpty(siteAAddressMap.get("destinationPincode")));
					contractSiteGstAddress.setSiteAAddress(siteAAddress);

				}

			}
		}
		LOGGER.info("Get Gst Address Ended {}", LocalTime.now());
		return contractSiteGstAddress;
	}

	public String saveGstAddress(String serviceId, ContractSiteGstAddress contractSiteGstAddress) {
		Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
		ScOrder scOrder = scServiceDetail.get().getScOrder();
		Map<String, String> attributeAMap = new HashMap<>();
		Map<String, String> attributeBMap = new HashMap<>();

		if (scOrder != null) {
			if (contractSiteGstAddress != null) {
				ContractGstAddress contractGstAddress = contractSiteGstAddress.getContractGstAddress();
				SiteGstAddress siteGstAddress = contractSiteGstAddress.getSiteGstAddress();
				SiteGstAddress siteBGstAddress = contractSiteGstAddress.getSiteGstAddressB();
				SiteAddress siteAAddress = contractSiteGstAddress.getSiteAAddress();
				SiteAddress siteBAddress = contractSiteGstAddress.getSiteBAddress();
				if (contractGstAddress != null) {
					if (!contractGstAddress.getContractGstNumber().contains("No Registered GST")) {
						ScGstAddress scContractGstAddress = new ScGstAddress();
						scContractGstAddress.setBuildingName(contractGstAddress.getContractGstinAddressOne());
						scContractGstAddress.setStreet(contractGstAddress.getContractGstinAddressTwo());
						scContractGstAddress.setLocality(contractGstAddress.getContractGstinAddressThree());
						scContractGstAddress.setDistrict(contractGstAddress.getContractGstinAddressCity());
						scContractGstAddress.setState(contractGstAddress.getContractGstinAddressState());
						scContractGstAddress.setPincode(contractGstAddress.getContractGstinAddressPincode());
						scOrder.setScGstAddress(scContractGstAddress);
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
				}
				if (NetworkConstants.NPL.equals(scServiceDetail.get().getErfPrdCatalogProductName())) {
					if (siteBGstAddress != null) {
						if (NetworkConstants.NO_REGISTERED_GST.equals(siteBGstAddress.getSiteGstNumber())) {
							attributeBMap.put("siteGstAddress", null);
							attributeBMap.put("siteGstAddressId", null);
							attributeBMap.put("siteGstNumber", NetworkConstants.NO_REGISTERED_GST);
						} else {
							ScGstAddress scSiteGstAddress = new ScGstAddress();
							scSiteGstAddress.setBuildingName(siteBGstAddress.getSiteGstinAddressOne());
							scSiteGstAddress.setStreet(siteBGstAddress.getSiteGstinAddressTwo());
							scSiteGstAddress.setLocality(siteBGstAddress.getSiteGstinAddressThree());
							scSiteGstAddress.setDistrict(siteBGstAddress.getSiteGstinAddressCity());
							scSiteGstAddress.setState(siteBGstAddress.getSiteGstinAddressState());
							scSiteGstAddress.setPincode(siteBGstAddress.getSiteGstinAddressPincode());
							scSiteGstAddress = gstAddressRepository.save(scSiteGstAddress);
							attributeBMap.put("siteGstAddressId", scSiteGstAddress.getId().toString());
							attributeBMap.put("siteGstNumber", siteBGstAddress.getSiteGstNumber());

							String gstFullAddress = StringUtils.trimToEmpty(siteBGstAddress.getSiteGstinAddressOne())
									+ " " + StringUtils.trimToEmpty(siteBGstAddress.getSiteGstinAddressTwo()) + " "
									+ StringUtils.trimToEmpty(siteBGstAddress.getSiteGstinAddressThree()) + " "
									+ StringUtils.trimToEmpty(siteBGstAddress.getSiteGstinAddressCity()) + " "
									+ StringUtils.trimToEmpty(siteBGstAddress.getSiteGstinAddressState()) + " "
									+ StringUtils.trimToEmpty(siteBGstAddress.getSiteGstinAddressPincode());
							attributeBMap.put("siteGstAddress", gstFullAddress);

						}

					}
					if (siteBAddress != null) {
						LOGGER.info(
								"Site B Address updated is line 1:{} line 2:{} line 3:{} City:{} state:{} County:{} Pincode:{}",
								siteBAddress.getSiteAddressLineOne(), siteBAddress.getSiteAddressLineTwo(),
								siteBAddress.getSiteAddressLineThree(), siteBAddress.getSiteCity(),
								siteBAddress.getSiteState(), siteBAddress.getSiteCountry(),
								siteBAddress.getSitePincode());
						attributeBMap.put("destinationAddressLineOne", siteBAddress.getSiteAddressLineOne());
						attributeBMap.put("destinationAddressLineTwo", siteBAddress.getSiteAddressLineTwo());
						attributeBMap.put("destinationLocality", siteBAddress.getSiteAddressLineThree());
						attributeBMap.put("destinationCity", siteBAddress.getSiteCity());
						attributeBMap.put("destinationState", siteBAddress.getSiteState());
						attributeBMap.put("destinationCountry", siteBAddress.getSiteCountry());
						attributeBMap.put("destinationPincode", siteBAddress.getSitePincode());
						attributeBMap.put("siteAddress",
								StringUtils.trimToEmpty(siteBAddress.getSiteAddressLineOne()) + " "
										+ StringUtils.trimToEmpty(siteBAddress.getSiteAddressLineTwo()) + " "
										+ StringUtils.trimToEmpty(siteBAddress.getSiteAddressLineThree()) + " "
										+ StringUtils.trimToEmpty(siteBAddress.getSiteCity()) + " "
										+ StringUtils.trimToEmpty(siteBAddress.getSiteState()) + " "
										+ StringUtils.trimToEmpty(siteBAddress.getSiteCountry()) + " "
										+ StringUtils.trimToEmpty(siteBAddress.getSitePincode()));

					}
					updateAttributes(scServiceDetail.get().getId(), attributeBMap, AttributeConstants.COMPONENT_LM,
							"B");

				}

				if (siteGstAddress != null) {
					if (NetworkConstants.NO_REGISTERED_GST.equals(siteGstAddress.getSiteGstNumber())) {
						attributeAMap.put("siteGstAddressId", null);
						attributeAMap.put("siteGstNumber", NetworkConstants.NO_REGISTERED_GST);
						attributeAMap.put("siteGstAddress", null);
					} else {
						ScGstAddress scSiteGstAddress = new ScGstAddress();
						scSiteGstAddress.setBuildingName(siteGstAddress.getSiteGstinAddressOne());
						scSiteGstAddress.setStreet(siteGstAddress.getSiteGstinAddressTwo());
						scSiteGstAddress.setLocality(siteGstAddress.getSiteGstinAddressThree());
						scSiteGstAddress.setDistrict(siteGstAddress.getSiteGstinAddressCity());
						scSiteGstAddress.setState(siteGstAddress.getSiteGstinAddressState());
						scSiteGstAddress.setPincode(siteGstAddress.getSiteGstinAddressPincode());
						scSiteGstAddress = gstAddressRepository.save(scSiteGstAddress);
						attributeAMap.put("siteGstAddressId", scSiteGstAddress.getId().toString());
						attributeAMap.put("siteGstNumber", siteGstAddress.getSiteGstNumber());

						String gstFullAddress = StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressOne()) + " "
								+ StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressTwo()) + " "
								+ StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressThree()) + " "
								+ StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressCity()) + " "
								+ StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressState()) + " "
								+ StringUtils.trimToEmpty(siteGstAddress.getSiteGstinAddressPincode());
						attributeAMap.put("siteGstAddress", gstFullAddress);
					}

				}

				if (siteAAddress != null) {
					LOGGER.info(
							"Site A Address updated is line 1:{} line 2:{} line 3:{} City:{} state:{} County:{} Pincode:{}",
							siteAAddress.getSiteAddressLineOne(), siteAAddress.getSiteAddressLineTwo(),
							siteAAddress.getSiteAddressLineThree(), siteAAddress.getSiteCity(),
							siteAAddress.getSiteState(), siteAAddress.getSiteCountry(), siteAAddress.getSitePincode());
					attributeAMap.put("destinationAddressLineOne", siteAAddress.getSiteAddressLineOne());
					attributeAMap.put("destinationAddressLineTwo", siteAAddress.getSiteAddressLineTwo());
					attributeAMap.put("destinationLocality", siteAAddress.getSiteAddressLineThree());
					attributeAMap.put("destinationCity", siteAAddress.getSiteCity());
					attributeAMap.put("destinationState", siteAAddress.getSiteState());
					attributeAMap.put("destinationCountry", siteAAddress.getSiteCountry());
					attributeAMap.put("destinationPincode", siteAAddress.getSitePincode());
					attributeAMap.put("siteAddress",
							StringUtils.trimToEmpty(siteAAddress.getSiteAddressLineOne()) + " "
									+ StringUtils.trimToEmpty(siteAAddress.getSiteAddressLineTwo()) + " "
									+ StringUtils.trimToEmpty(siteAAddress.getSiteAddressLineThree()) + " "
									+ StringUtils.trimToEmpty(siteAAddress.getSiteCity()) + " "
									+ StringUtils.trimToEmpty(siteAAddress.getSiteState()) + " "
									+ StringUtils.trimToEmpty(siteAAddress.getSiteCountry()) + " "
									+ StringUtils.trimToEmpty(siteAAddress.getSitePincode()));

				}
				attributeAMap.put("supplierAddress",
						"Videsh Sanchar Bhavan, MG Road, Opp Cross Maidan, Fort, Mumbai Maharashtra India 400001");
				updateAttributes(scServiceDetail.get().getId(), attributeAMap, AttributeConstants.COMPONENT_LM, "A");

			}
			ScOrderAttribute contractingEntity = scOrderAttributeRepository
					.findFirstByAttributeNameAndScOrder(LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY, scOrder);
			if (contractingEntity != null & contractingEntity.getAttributeValue() != null) {
				ScOrderAttribute noticeAddress = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.NOTICE_ADDRESS, scOrder);
				if (noticeAddress != null) {
					noticeAddress.setAttributeValue(getNoticeAddress(contractingEntity.getAttributeValue()));
					noticeAddress.setUpdatedDate(new Timestamp(new Date().getTime()));
				} else {
					noticeAddress = new ScOrderAttribute();
					noticeAddress.setAttributeName(LeAttributesConstants.NOTICE_ADDRESS);
					noticeAddress.setAttributeValue(getNoticeAddress(contractingEntity.getAttributeValue()));
					noticeAddress.setAttributeAltValueLabel(LeAttributesConstants.NOTICE_ADDRESS);
					noticeAddress.setScOrder(scOrder);
					noticeAddress.setIsActive("Y");
					noticeAddress.setCreatedDate(new Timestamp(new Date().getTime()));
				}
				scOrderAttributeRepository.save(noticeAddress);
			}
		}
		return "Gst Number & Address Updated";
	}

	public String getContractGstAddress(String GstNo, Integer custLeId) {

		LeStateInfo contractGstAddressDetail = loadCustomerDetails.getGstDetails(GstNo, custLeId);
		if (contractGstAddressDetail != null) {
			return StringUtils.trimToEmpty(contractGstAddressDetail.getAddresslineOne()) + " "
					+ StringUtils.trimToEmpty(contractGstAddressDetail.getAddresslineTwo()) + " "
					+ StringUtils.trimToEmpty(contractGstAddressDetail.getAddresslineThree()) + " "
					+ getLocationData(locationDataMap, StringUtils.trimToEmpty(contractGstAddressDetail.getCity()))
					+ " "
					+ getLocationData(locationDataMap, StringUtils.trimToEmpty(contractGstAddressDetail.getState()))
					+ " "
					+ getLocationData(locationDataMap, StringUtils.trimToEmpty(contractGstAddressDetail.getCountry()))
					+ " " + StringUtils.trimToEmpty(contractGstAddressDetail.getPincode());
		} else {
			return "NA";
		}
	}

	public String contractAddressforCommercialVetting(String customerAddressLocationId) {
		AddressDetail addressDetail = customerAndSupplierAddress.getAddressLocationId(customerAddressLocationId);
		if (addressDetail != null) {
			String addressLineOne = StringUtils.trimToEmpty(addressDetail.getAddressLineOne());
			String addressLineTwo = StringUtils.trimToEmpty(addressDetail.getAddressLineTwo());
			String addressLineThree = StringUtils.trimToEmpty(addressDetail.getLocality());
			String addressLinecity = getLocationData(locationDataMap, StringUtils.trimToEmpty(addressDetail.getCity()));
			String addressLineState = getLocationData(locationDataMap,
					StringUtils.trimToEmpty(addressDetail.getState()));
			String addressLineCountry = getLocationData(locationDataMap,
					StringUtils.trimToEmpty(addressDetail.getCountry()));
			String addressLineZipcode = StringUtils.trimToEmpty(addressDetail.getPincode());
			return addressLineOne + " " + addressLineTwo + " " + addressLineThree + " " + addressLinecity + " "
					+ addressLineState + " " + addressLineCountry + " " + addressLineZipcode;
		} else {
			return "";
		}

	}

	public String siteGstAddressNPL(Integer id) {
		Optional<ScGstAddress> scGstAddress = gstAddressRepository.findById(id);
		if (scGstAddress.isPresent()) {
			SiteGstAddress siteGstAddress = new SiteGstAddress();
			String addressLineOne = StringUtils.trimToEmpty(scGstAddress.get().getFlatNumber()) + " "
					+ StringUtils.trimToEmpty(scGstAddress.get().getBuildingNumber()) + " "
					+ StringUtils.trimToEmpty(scGstAddress.get().getBuildingName());
			String addressLineTwo = StringUtils.trimToEmpty(scGstAddress.get().getStreet());
			siteGstAddress.setSiteGstinAddressOne(addressLineOne);
			if (addressLineTwo.equals(scGstAddress.get().getLocality())) {
				siteGstAddress.setSiteGstinAddressTwo(addressLineTwo);
				siteGstAddress.setSiteGstinAddressThree("");
			} else {
				siteGstAddress.setSiteGstinAddressTwo(addressLineTwo);
				siteGstAddress.setSiteGstinAddressThree(StringUtils.trimToEmpty(scGstAddress.get().getLocality()));
			}
			siteGstAddress.setSiteGstinAddressCity(StringUtils.trimToEmpty(scGstAddress.get().getDistrict()));
			siteGstAddress.setSiteGstinAddressState(StringUtils.trimToEmpty(scGstAddress.get().getState()));
			siteGstAddress.setSiteGstinAddressCountry(NetworkConstants.INDIA);
			siteGstAddress.setSiteGstinAddressPincode(StringUtils.trimToEmpty(scGstAddress.get().getPincode()));
			return siteGstAddress.toString();
		} else {
			return "";
		}
	}

	/**
	 * @author Yogesh
	 * @param serviceId
	 * @param atMap
	 * @param componentName used to update attributes
	 */
	public void updateAttributes(Integer serviceId, Map<String, String> atMap, String componentName, String siteType) {

		ScComponent scComponent = scComponentRepository
				.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(serviceId, componentName,
						siteType);

		String userName = Utils.getSource();
		if (scComponent != null) {
			atMap.forEach((key, value) -> {
				ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
								serviceId, key, componentName, siteType);
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
		if (Objects.nonNull(attrValue)) {
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
	 * @param userName             used to update component attr
	 */
	private void updateComponentAttr(String attrName, String attrValue, ScComponentAttribute scComponentAttribute,
			String userName) {

		if (attrValue != null && !attrValue.equals(scComponentAttribute.getAttributeValue())) {
			scComponentAttribute.setAttributeName(attrName);
			scComponentAttribute.setAttributeValue(attrValue);
			scComponentAttribute.setAttributeAltValueLabel(attrValue);
			scComponentAttribute.setUpdatedBy(userName);
			scComponentAttributesRepository.save(scComponentAttribute);
		}
	}

	@Transactional(readOnly = false)
	public List<Map<String, Object>> getRenewalAddress(String orderCode) {
		LOGGER.info("getRenewalAddress method invoked for::{}", orderCode);
		List<Map<String, Object>> results = null;
		try {
			if (Utils.validateAlphaNumberic(orderCode)) {
				results = this.jdbcTemplate.queryForList("call proc_service_underprov('" + orderCode + "')");
				LOGGER.info("Output result::{}", results);
			} else {
				LOGGER.warn("Invalid OrderCode::{}", orderCode);
			}
		} catch (Exception e) {
			LOGGER.error("Error in getRenewalAddress", e);
		}
		return results;
	}

	public RenewalValidateDocumentDetails saveRenewalContractSiteGstAddressPODetails(
			RenewalValidateDocumentDetails renewalValidateDocumentDetails) throws TclCommonException {
		LOGGER.info("saveRenewalContractSiteGstAddressPODetails method invoked");
		// try {
		if (renewalValidateDocumentDetails != null) {
			LOGGER.info("RenewalValidateDocumentDetails input json::{} with ServiceId::{}",Utils.convertObjectToJson(renewalValidateDocumentDetails),renewalValidateDocumentDetails.getServiceId());
			Optional<ScServiceDetail> parentScServiceDetail = scServiceDetailRepository
					.findById(renewalValidateDocumentDetails.getServiceId());
			ScOrder scOrder = parentScServiceDetail.get().getScOrder();
			Boolean isValidDocuments = true;
			Map<String, Object> map = new HashMap<>();
			List<RenewalContractSiteDetails> renewalContractAddressDetailList = renewalValidateDocumentDetails
					.getRenewalContractSiteDetailList();
			if (renewalContractAddressDetailList != null && !renewalContractAddressDetailList.isEmpty()) {
				LOGGER.info("renewalContractAddressDetailList exists::{}", renewalContractAddressDetailList.size());
				RenewalContractSiteDetails renewalContractAddressDetails = renewalContractAddressDetailList.get(0);
				if (renewalContractAddressDetails != null
						&& !renewalContractAddressDetails.getContractGstNumber().contains("No Registered GST")) {
					ScGstAddress scContractGstAddress = null;
					if (scOrder.getScGstAddress() != null) {
						scContractGstAddress = scOrder.getScGstAddress();
					} else {
						scContractGstAddress = new ScGstAddress();
					}
					scContractGstAddress.setBuildingName(renewalContractAddressDetails.getContractGstAddressOne());
					scContractGstAddress.setStreet(renewalContractAddressDetails.getContractGstAddressTwo());
					scContractGstAddress.setLocality(renewalContractAddressDetails.getContractGstAddressThree());
					scContractGstAddress.setDistrict(renewalContractAddressDetails.getContractGstAddressCity());
					scContractGstAddress.setState(renewalContractAddressDetails.getContractGstAddressState());
					scContractGstAddress.setPincode(renewalContractAddressDetails.getContractGstAddressPincode());
					scOrder.setScGstAddress(scContractGstAddress);
					orderRepository.saveAndFlush(scOrder);
					ScOrderAttribute scOrderAttribute = scOrderAttributeRepository
							.findFirstByAttributeNameAndScOrder(LeAttributesConstants.LE_STATE_GST_NO, scOrder);
					if (scOrderAttribute != null) {
						scOrderAttribute.setAttributeAltValueLabel(
								StringUtils.trimToEmpty(renewalContractAddressDetails.getContractGstNumber()));
						scOrderAttribute.setAttributeValue(
								StringUtils.trimToEmpty(renewalContractAddressDetails.getContractGstNumber()));
					} else {
						scOrderAttribute = new ScOrderAttribute();
						scOrderAttribute.setAttributeName(LeAttributesConstants.LE_STATE_GST_NO);
						scOrderAttribute.setAttributeValue(
								StringUtils.trimToEmpty(renewalContractAddressDetails.getContractGstNumber()));
						scOrderAttribute.setAttributeAltValueLabel(
								StringUtils.trimToEmpty(renewalContractAddressDetails.getContractGstNumber()));
						scOrderAttribute.setIsActive(NetworkConstants.Y);
						scOrderAttribute.setScOrder(scOrder);
					}
					scOrderAttributeRepository.saveAndFlush(scOrderAttribute);
				}
				if (scOrder.getOpOrderCode().startsWith("NPL")) {
					LOGGER.info("renewalValidateDocumentDetails for NPL");
					for (RenewalContractSiteDetails renewalContractSiteDetail : renewalContractAddressDetailList) {
						Map<String, String> attributeAMap = new HashMap<>();
						Map<String, String> attributeBMap = new HashMap<>();
						Map<String, String> serviceMap = new HashMap<>();
						updateSiteBGstDetails(renewalContractSiteDetail, attributeBMap);
						updateSiteBAddressDetails(renewalContractSiteDetail, attributeBMap);
						attributeBMap.put("TAX_EXCEMPTED_REASON", renewalContractSiteDetail.getSiteATaxExemptionReason());
						updateAttributes(renewalContractSiteDetail.getServiceId(), attributeBMap,
								AttributeConstants.COMPONENT_LM, "B");
						updateSiteAGstDetails(renewalContractSiteDetail, attributeAMap);
						updateSiteAAddressDetails(renewalContractSiteDetail, attributeAMap);
						attributeAMap.put("TAX_EXCEMPTED_REASON", renewalContractSiteDetail.getSiteATaxExemptionReason());
						updateAttributes(renewalContractSiteDetail.getServiceId(), attributeAMap,
								AttributeConstants.COMPONENT_LM, "A");
						serviceMap.put("TAX_EXCEMPTED_REASON", renewalContractSiteDetail.getSiteATaxExemptionReason());
						componentAndAttributeService.updateServiceAttributes(renewalContractSiteDetail.getServiceId(),
								serviceMap, "SITE_PROPERTIES");
						isValidDocuments = saveAttachment(renewalContractSiteDetail, isValidDocuments);
					}
				} else {
					LOGGER.info("Other than NPL renewalValidateDocumentDetails");
					for (RenewalContractSiteDetails renewalContractSiteDetail : renewalContractAddressDetailList) {
						Map<String, String> attributeAMap = new HashMap<>();
						Map<String, String> serviceMap = new HashMap<>();
						updateSiteAGstDetails(renewalContractSiteDetail, attributeAMap);
						updateSiteAAddressDetails(renewalContractSiteDetail, attributeAMap);
						attributeAMap.put("TAX_EXCEMPTED_REASON", renewalContractSiteDetail.getSiteATaxExemptionReason());
						updateAttributes(renewalContractSiteDetail.getServiceId(), attributeAMap,
								AttributeConstants.COMPONENT_LM, "A");
						serviceMap.put("TAX_EXCEMPTED_REASON", renewalContractSiteDetail.getSiteATaxExemptionReason());
						componentAndAttributeService.updateServiceAttributes(renewalContractSiteDetail.getServiceId(),
								serviceMap, "SITE_PROPERTIES");
						isValidDocuments = saveAttachment(renewalContractSiteDetail, isValidDocuments);
					}
				}
				ScOrderAttribute contractingEntity = scOrderAttributeRepository
						.findFirstByAttributeNameAndScOrder(LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY, scOrder);
				if (contractingEntity != null & contractingEntity.getAttributeValue() != null) {
					ScOrderAttribute noticeAddress = scOrderAttributeRepository
							.findFirstByAttributeNameAndScOrder(LeAttributesConstants.NOTICE_ADDRESS, scOrder);
					if (noticeAddress != null) {
						noticeAddress.setAttributeValue(getNoticeAddress(contractingEntity.getAttributeValue()));
						noticeAddress.setUpdatedDate(new Timestamp(new Date().getTime()));
					} else {
						noticeAddress = new ScOrderAttribute();
						noticeAddress.setAttributeName(LeAttributesConstants.NOTICE_ADDRESS);
						noticeAddress.setAttributeValue(getNoticeAddress(contractingEntity.getAttributeValue()));
						noticeAddress.setAttributeAltValueLabel(LeAttributesConstants.NOTICE_ADDRESS);
						noticeAddress.setScOrder(scOrder);
						noticeAddress.setIsActive("Y");
						noticeAddress.setCreatedDate(new Timestamp(new Date().getTime()));
					}
					scOrderAttributeRepository.saveAndFlush(noticeAddress);
				}
				map.put("isValidDocuments", isValidDocuments);
				LOGGER.info("Save renewalValidateDocuments for serviceId::{}",
						renewalValidateDocumentDetails.getServiceId());
				if ("CLOSED".equalsIgnoreCase(renewalValidateDocumentDetails.getAction())) {
					Task task = getTaskByIdAndWfTaskId(renewalValidateDocumentDetails.getTaskId(),
							renewalValidateDocumentDetails.getWfTaskId());
					LOGGER.info("Close renewalValidateDocuments for serviceId::{}", task.getServiceId());
					Integer modifiedCOFAttachmentId[] = { 0 };
					if (renewalContractAddressDetails.getDocuments() != null
							&& !renewalContractAddressDetails.getDocuments().isEmpty()) {
						renewalContractAddressDetails.getDocuments().stream()
								.filter(ra -> "SCOF".equals(ra.getCategory())).forEach(ra -> {
									modifiedCOFAttachmentId[0] = ra.getId();
								});
					}
					if (modifiedCOFAttachmentId[0] != 0) {
						LOGGER.info("renewalValidateDocumentDetails.modifiedCOFAttachmentId exists::{}",
								modifiedCOFAttachmentId[0]);
						makeEntryInScAttachment(task, modifiedCOFAttachmentId[0]);
					}
					map.put("renewal-validate-supporting-document" + "_validate_attachment",
							Utils.convertObjectToJson(renewalValidateDocumentDetails));
					map.put("isPODetailExists", false);
					if (renewalValidateDocumentDetails.getIsPODetailExists()) {
						map.put("isPODetailExists", true);
					}
					processTaskLogDetails(task, "CLOSED", renewalValidateDocumentDetails.getDelayReason(), null,
							renewalValidateDocumentDetails);
					flowableBaseService.taskDataEntry(task, renewalValidateDocumentDetails, map);
				}
			}

		}
		// }catch(Exception e) {
		// LOGGER.error("Error in saveRenewalContractSiteGstAddressPODetails", e);
		// }
		return renewalValidateDocumentDetails;
	}

	private Boolean saveAttachment(RenewalContractSiteDetails renewalContractSiteDetail, Boolean isValidDocuments) {
		if (Objects.nonNull(renewalContractSiteDetail.getDocuments())
				&& !renewalContractSiteDetail.getDocuments().isEmpty()) {
			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository
					.findById(renewalContractSiteDetail.getServiceId());
			for (AttachmentBean attachmentBean : renewalContractSiteDetail.getDocuments()) {
				String isVerified = StringUtils.trimToEmpty(attachmentBean.getVerified());
				if (isVerified.equalsIgnoreCase("N"))
					isValidDocuments = false;
				if (Objects.isNull(attachmentBean.getId())) { // Making an entry of attachment requested from customer
					Attachment attachment = UploadAttachmentAsRequested(attachmentBean);
					makeEntryInScAttachment(scServiceDetail.get(), attachmentBean.getId());
				} else
					saveAttachment(attachmentBean, scServiceDetail.get());
			}
		}
		return isValidDocuments;
	}

	private void updateSiteAGstDetails(RenewalContractSiteDetails renewalContractSiteDetail,
			Map<String, String> attributeAMap) {
		if (NetworkConstants.NO_REGISTERED_GST.equals(renewalContractSiteDetail.getSiteAGstNumber())) {
			attributeAMap.put("siteGstAddressId", null);
			attributeAMap.put("siteGstNumber", NetworkConstants.NO_REGISTERED_GST);
			attributeAMap.put("siteGstAddress", null);
		} else {
			ScGstAddress scSiteGstAddress = null;
			if (renewalContractSiteDetail.getSiteAGstAddressId() != null && !renewalContractSiteDetail.getSiteAGstAddressId().isEmpty()) {
				Optional<ScGstAddress> scGstAddressDetails = gstAddressRepository.findById(Integer.valueOf(renewalContractSiteDetail.getSiteAGstAddressId()));
				if (scGstAddressDetails.isPresent()) {
					scSiteGstAddress = scGstAddressDetails.get();
				}
			} else {
				scSiteGstAddress = new ScGstAddress();
			}
			scSiteGstAddress.setBuildingName(renewalContractSiteDetail.getSiteAGstAddressOne());
			scSiteGstAddress.setStreet(renewalContractSiteDetail.getSiteAGstAddressTwo());
			scSiteGstAddress.setLocality(renewalContractSiteDetail.getSiteAGstAddressThree());
			scSiteGstAddress.setDistrict(renewalContractSiteDetail.getSiteAGstDistrict());
			scSiteGstAddress.setState(renewalContractSiteDetail.getSiteAGstState());
			scSiteGstAddress.setPincode(renewalContractSiteDetail.getSiteAGstPincode());
			scSiteGstAddress = gstAddressRepository.save(scSiteGstAddress);
			renewalContractSiteDetail.setSiteAGstAddressId(scSiteGstAddress.getId().toString());
			attributeAMap.put("siteGstAddressId", scSiteGstAddress.getId().toString());
			attributeAMap.put("siteGstNumber", renewalContractSiteDetail.getSiteAGstNumber());
			String gstFullAddress = StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteAGstAddressOne()) + " "
					+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteAGstAddressTwo()) + " "
					+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteAGstAddressThree()) + " "
					+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteAGstDistrict()) + " "
					+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteAGstState()) + " "
					+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteAGstPincode());
			attributeAMap.put("siteGstAddress", gstFullAddress);
		}
		attributeAMap.put("poDocType", renewalContractSiteDetail.getSiteAPODocType());
		attributeAMap.put("taxExemption", renewalContractSiteDetail.getSiteATaxExemption());
		attributeAMap.put("PO_NUMBER", renewalContractSiteDetail.getSiteACustomerPoNumber());
		attributeAMap.put("PO_DATE", renewalContractSiteDetail.getSiteACustomerPoDate());
	}

	private void updateSiteBGstDetails(RenewalContractSiteDetails renewalContractSiteDetail,
			Map<String, String> attributeBMap) {

		if (NetworkConstants.NO_REGISTERED_GST.equals(renewalContractSiteDetail.getSiteBGstNumber())) {
			attributeBMap.put("siteGstAddress", null);
			attributeBMap.put("siteGstAddressId", null);
			attributeBMap.put("siteGstNumber", NetworkConstants.NO_REGISTERED_GST);
		} else {
			ScGstAddress scSiteGstAddress = null;
			if (renewalContractSiteDetail.getSiteBGstAddressId() != null && !renewalContractSiteDetail.getSiteBGstAddressId().isEmpty()) {
				Optional<ScGstAddress> scGstAddressDetails = gstAddressRepository.findById(Integer.valueOf(renewalContractSiteDetail.getSiteBGstAddressId()));
				if (scGstAddressDetails.isPresent()) {
					scSiteGstAddress = scGstAddressDetails.get();
				}
			} else {
				scSiteGstAddress = new ScGstAddress();
			}
			scSiteGstAddress.setBuildingName(renewalContractSiteDetail.getSiteBGstAddressOne());
			scSiteGstAddress.setStreet(renewalContractSiteDetail.getSiteBGstAddressTwo());
			scSiteGstAddress.setLocality(renewalContractSiteDetail.getSiteBGstAddressThree());
			scSiteGstAddress.setDistrict(renewalContractSiteDetail.getSiteBGstDistrict());
			scSiteGstAddress.setState(renewalContractSiteDetail.getSiteBGstState());
			scSiteGstAddress.setPincode(renewalContractSiteDetail.getSiteBGstPincode());
			scSiteGstAddress = gstAddressRepository.save(scSiteGstAddress);
			renewalContractSiteDetail.setSiteBGstAddressId(scSiteGstAddress.getId().toString());
			attributeBMap.put("siteGstAddressId", scSiteGstAddress.getId().toString());
			attributeBMap.put("siteGstNumber", renewalContractSiteDetail.getSiteBGstNumber());

			String gstFullAddress = StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteBGstAddressOne()) + " "
					+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteBGstAddressTwo()) + " "
					+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteBGstAddressThree()) + " "
					+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteBGstDistrict()) + " "
					+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteBGstState()) + " "
					+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteBGstPincode());
			attributeBMap.put("siteGstAddress", gstFullAddress);

		}
		attributeBMap.put("poDocType", renewalContractSiteDetail.getSiteAPODocType());
		attributeBMap.put("taxExemption", renewalContractSiteDetail.getSiteATaxExemption());
		attributeBMap.put("PO_NUMBER", renewalContractSiteDetail.getSiteACustomerPoNumber());
		attributeBMap.put("PO_DATE", renewalContractSiteDetail.getSiteACustomerPoDate());
	}

	private Attachment UploadAttachmentAsRequested(AttachmentBean attachmentBean) {
		LOGGER.info("Uploading requested attachment with details {}", attachmentBean.toString());
		Attachment attachment = new Attachment();
		attachment.setCategory(attachmentBean.getCategory());
		attachment.setType(attachmentBean.getType());
		attachment.setIsActive("Y");
		attachment.setVerified("N");
		attachment.setCreatedBy(Utils.getSource());
		attachment.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
		attachment.setVerificationFailureReason(attachmentBean.getVerificationFailureReason());
		return attachmentRepository.save(attachment);
	}

	/**
	 * save the attachment with updates values
	 *
	 * @param attachmentBean
	 */
	public void saveAttachment(AttachmentBean attachmentBean, ScServiceDetail scServiceDetail) {
		Optional<Attachment> optAttachment = attachmentRepository.findById(attachmentBean.getId());
		if (optAttachment.isPresent()) {
			Attachment attachment = optAttachment.get();
			updateAttachmentData(attachment, attachmentBean);
			attachmentRepository.save(attachment);
			if (attachmentBean.getIsNew() != null && attachmentBean.getIsNew().equalsIgnoreCase("Y")) {
				ScAttachment scAttachment = new ScAttachment();
				scAttachment.setAttachment(attachment);
				scAttachment.setScServiceDetail(scServiceDetail);
				scAttachment.setIsActive("Y");
				scAttachment.setSiteType(scServiceDetail.getSiteType() == null ? "A" : scServiceDetail.getSiteType());
				scAttachment
						.setOrderId(scServiceDetail.getScOrder() != null ? scServiceDetail.getScOrder().getId() : null);
				scAttachmentRepository.save(scAttachment);
			}
		}
	}

	/**
	 * @param attachment
	 * @param attachmentBean
	 * @return Attachment
	 */

	private Attachment updateAttachmentData(Attachment attachment, AttachmentBean attachmentBean) {
		attachment.setCategory(attachmentBean.getCategory());
		attachment.setType(attachmentBean.getType());
		attachment.setVerified(attachmentBean.getVerified());
		attachment.setVerificationFailureReason(attachmentBean.getVerificationFailureReason());
		attachment.setUpdatedBy(Utils.getSource());
		attachment.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));

		return attachment;
	}

	protected ScAttachment makeEntryInScAttachment(ScServiceDetail scServiceDetail, Integer attachmentId) {
		return scAttachmentRepository.save(constructScAttachment(scServiceDetail, attachmentId));
	}

	protected ScAttachment constructScAttachment(ScServiceDetail scServiceDetail, Integer attachmentId) {
		ScAttachment scAttachment = new ScAttachment();
		scAttachment.setAttachment(getAttachmentById(attachmentId));
		scAttachment.setScServiceDetail(scServiceDetail);
		scAttachment.setIsActive("Y");
		scAttachment.setSiteType(scServiceDetail.getSiteType() == null ? "A" : scServiceDetail.getSiteType());
		scAttachment.setOrderId(scServiceDetail.getScOrder().getId());
		return scAttachment;
	}

	private void updateSiteAAddressDetails(RenewalContractSiteDetails renewalContractSiteDetail,
			Map<String, String> attributeAMap) {
		attributeAMap.put("destinationAddressLineOne", renewalContractSiteDetail.getSiteAAddressOne());
		attributeAMap.put("destinationAddressLineTwo", renewalContractSiteDetail.getSiteAAddressTwo());
		attributeAMap.put("destinationLocality", renewalContractSiteDetail.getSiteAAddressThree());
		attributeAMap.put("destinationCity", renewalContractSiteDetail.getSiteACity());
		attributeAMap.put("destinationState", renewalContractSiteDetail.getSiteAState());
		attributeAMap.put("destinationCountry", renewalContractSiteDetail.getSiteACountry());
		attributeAMap.put("destinationPincode", renewalContractSiteDetail.getSiteAPincode());
		attributeAMap.put("siteAddress",
				StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteAAddressOne()) + " "
						+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteAAddressTwo()) + " "
						+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteAAddressThree()) + " "
						+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteACity()) + " "
						+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteAState()) + " "
						+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteACountry()) + " "
						+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteAPincode()));
		attributeAMap.put("supplierAddress",
				"Videsh Sanchar Bhavan, MG Road, Opp Cross Maidan, Fort, Mumbai Maharashtra India 400001");
	}

	private void updateSiteBAddressDetails(RenewalContractSiteDetails renewalContractSiteDetail,
			Map<String, String> attributeBMap) {
		attributeBMap.put("destinationAddressLineOne", renewalContractSiteDetail.getSiteBAddressOne());
		attributeBMap.put("destinationAddressLineTwo", renewalContractSiteDetail.getSiteBAddressTwo());
		attributeBMap.put("destinationLocality", renewalContractSiteDetail.getSiteBAddressThree());
		attributeBMap.put("destinationCity", renewalContractSiteDetail.getSiteBCity());
		attributeBMap.put("destinationState", renewalContractSiteDetail.getSiteBState());
		attributeBMap.put("destinationCountry", renewalContractSiteDetail.getSiteBCountry());
		attributeBMap.put("destinationPincode", renewalContractSiteDetail.getSiteBPincode());
		attributeBMap.put("siteAddress",
				StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteBAddressOne()) + " "
						+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteBAddressTwo()) + " "
						+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteBAddressThree()) + " "
						+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteBCity()) + " "
						+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteBState()) + " "
						+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteBCountry()) + " "
						+ StringUtils.trimToEmpty(renewalContractSiteDetail.getSiteBPincode()));
	}


	
	public RenewalAttachments getRenewalAttachments(String orderCode) throws TclCommonException{
		LOGGER.info("getRenewalAttachments method invoked for orderCode::{}",orderCode);
		RenewalAttachments renewalAttachments = new RenewalAttachments();
		ScOrder scOrder = orderRepository.findDistinctByOpOrderCode(orderCode);
		if(scOrder!=null) {
			LOGGER.info("getRenewalAttachments scOrder exists::{}",scOrder.getId());
			List<ScServiceDetail> scServiceDetails = scServiceDetailRepository.findByScOrder_idAndUuidNot(scOrder.getId(),scOrder.getUuid());
			List<RenewalCommercialVettingDetails> renewalCommercialVettingDetailsList = new ArrayList<>();
			for (ScServiceDetail scServiceDetail : scServiceDetails) {
				RenewalCommercialVettingDetails	renewalCommercialVettingDetails = new RenewalCommercialVettingDetails();
				renewalCommercialVettingDetails.setServiceId(scServiceDetail.getId());
				renewalCommercialVettingDetails.setServiceCode(scServiceDetail.getUuid());
				List<AttachmentBean> attachmentBeanList = new ArrayList<>();
				List<ScAttachment> scAttachments = scAttachmentRepository.findAllByScServiceDetail_Id(scServiceDetail.getId());
				scAttachments = scAttachments.stream().filter(sc -> sc.getAttachment().getCategory() != null)
						.collect(Collectors.toList());
				Map<String, List<Attachment>> attachmentMap = scAttachments.stream().map(ScAttachment::getAttachment)
						.collect(Collectors.groupingBy(Attachment::getCategory));
				attachmentMap.keySet().forEach(category -> {
					attachmentBeanList
							.add(attachmentMap.get(category).stream().sorted(Comparator.comparing(Attachment::getId).reversed())
									.findFirst().map(AttachmentBean::mapToBean).get());
				});
				LOGGER.info("RenewalAttachments size::{} for serviceId::{}",attachmentBeanList.size(),scServiceDetail.getId());
				renewalCommercialVettingDetails.setAttachmentDetails(attachmentBeanList);
				renewalCommercialVettingDetailsList.add(renewalCommercialVettingDetails);
			}
			LOGGER.info("RenewalAttachments available for servicedetails with size::{}",renewalCommercialVettingDetailsList.size());
			renewalAttachments.setRenewalCommercialVettingDetails(renewalCommercialVettingDetailsList);
		}
		return renewalAttachments;
	}
}
