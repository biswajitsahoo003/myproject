package com.tcl.dias.serviceactivation.activation.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.serviceactivation.cramer.reverseisc.beans.Attribute;
import com.tcl.dias.serviceactivation.cramer.reverseisc.beans.GetReverseISCData;
import com.tcl.dias.serviceactivation.cramer.reverseisc.beans.GetReverseISCDataResponse;
import com.tcl.dias.serviceactivation.cramer.reverseisc.beans.NniProtectedPort;
import com.tcl.dias.serviceactivation.cramer.reverseisc.beans.ReverseISCInput;
import com.tcl.dias.serviceactivation.cramer.reverseisc.beans.ReverseISCOutput;
import com.tcl.dias.serviceactivation.cramer.reverseisc.beans.Security;
import com.tcl.dias.serviceactivation.cramer.reverseisc.beans.VpnIllPortAttributes;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.CGWBean;
import com.tcl.dias.servicefulfillmentutils.beans.UseCaseDetailBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskDataService;
import com.tcl.dias.webserviceclient.beans.SoapRequest;
import com.tcl.dias.webserviceclient.service.GenericWebserviceClient;

/**
 * This class has methods for sat soc
 * 
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

@Service
public class SatSocService {

	private static final Logger logger = LoggerFactory.getLogger(SatSocService.class);

	@Autowired
	private GenericWebserviceClient genericWebserviceClient;

	@Value("${cramer.reverseisc.url}")
	String reverseiscurl;
	
	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;
	
	@Autowired
	TaskDataService taskDataService;
	
	@Transactional
	public VpnIllPortAttributes getReverseIsc(String serviceId, String page) {

		logger.info("Inside ReverseISC for serviceID :" + serviceId);

		VpnIllPortAttributes illPortAttributes = new VpnIllPortAttributes();
		try {

			GetReverseISCData reverseISCData = new GetReverseISCData();
			ReverseISCInput input = new ReverseISCInput();
			input.setRequestId("Optimus-" + serviceId);
			if (StringUtils.isNotBlank(page) && "inventory".equalsIgnoreCase(page)) {
				input.setRelationShip("ACTIVE");
			} else {
				input.setRelationShip("ISSUED");
			}

			Security security = new Security();
			security.setSourceSystem("OPSPORTALBPM");
			security.setUniqueKey("7924677767825276");
			input.setSecurity(security);
			input.setServiceId(serviceId);

			reverseISCData.setReverseISCInput(input);
			SoapRequest soapRequest = new SoapRequest();
			soapRequest.setUrl(reverseiscurl);
			soapRequest.setRequestObject(reverseISCData);
			soapRequest.setContextPath("com.tcl.dias.serviceactivation.cramer.reverseisc.beans");
			soapRequest.setSoapUserName("");
			soapRequest.setSoapPwd("");
			soapRequest.setConnectionTimeout(60000);
			soapRequest.setReadTimeout(60000);
			JAXBElement jaxbResponse = genericWebserviceClient.doSoapCallForObject(soapRequest, JAXBElement.class);
			if (Objects.nonNull(jaxbResponse)) {
				logger.info("SatSoc Reverse Isc Jaxb Response : {}", jaxbResponse.getValue().toString());
				GetReverseISCDataResponse iscDataResponse = (GetReverseISCDataResponse) jaxbResponse.getValue();
				illPortAttributes = iscDataResponse.getReverseISCOutput().getVpnIllPortAttributes();
				if (Objects.nonNull(illPortAttributes)) {
					logger.info("SatSoc Reverse Isc IllPortAttributes : {}", illPortAttributes);
				}
			}

		} catch (Exception e) {
			logger.error("for serviceId: {} Error in SatSoc ReverseISc : {} ", serviceId, e);
		}

		return illPortAttributes;
	}

	public CGWBean getCGWDetails(Integer serviceId,String serviceCode) {
		logger.info("GetCGWDetails invoked for serviceID :" + serviceCode);
		ReverseISCOutput reverseISCOutput = null;
		CGWBean cgwBean = new CGWBean();
		try {
			GetReverseISCData reverseISCData = new GetReverseISCData();
			ReverseISCInput input = new ReverseISCInput();
			input.setRequestId("Optimus-" + serviceId);
			Security security = new Security();
			security.setSourceSystem("OPSPORTALBPM");
			security.setUniqueKey("7924677767825276");
			input.setSecurity(security);
			input.setServiceId(serviceCode);
			String useCase = "";
			Optional<ScServiceDetail> scServiceDetailOptional = scServiceDetailRepository.findById(serviceId);
			Map<String, String> scComponentAttributesMap = commonFulfillmentUtils.getComponentAttributes(serviceId,
					AttributeConstants.COMPONENT_LM, "A");
			if (scComponentAttributesMap.containsKey("useCase") && scComponentAttributesMap.get("useCase") != null
					&& !scComponentAttributesMap.get("useCase").isEmpty()) {
				logger.info("UseCase exists for serviceId: {} ", scComponentAttributesMap.get("useCase"));
				input.setUseCaseCombination(scComponentAttributesMap.get("useCase"));
				useCase = scComponentAttributesMap.get("useCase");
			} else {
				logger.info("UseCase not exists for serviceId: {} ", serviceId);
				return null;
			}
			reverseISCData.setReverseISCInput(input);
			SoapRequest soapRequest = new SoapRequest();
			soapRequest.setUrl(reverseiscurl);
			soapRequest.setRequestObject(reverseISCData);
			soapRequest.setContextPath("com.tcl.dias.serviceactivation.cramer.reverseisc.beans");
			soapRequest.setSoapUserName("");
			soapRequest.setSoapPwd("");
			soapRequest.setConnectionTimeout(60000);
			soapRequest.setReadTimeout(60000);
			JAXBElement jaxbResponse = genericWebserviceClient.doSoapCallForObject(soapRequest, JAXBElement.class);
			if (Objects.nonNull(jaxbResponse)) {
				logger.info("Reverse Isc Jaxb Response : {}", jaxbResponse.getValue().toString());
				GetReverseISCDataResponse iscDataResponse = (GetReverseISCDataResponse) jaxbResponse.getValue();
				reverseISCOutput = iscDataResponse.getReverseISCOutput();
			}
			List<String> notInCategories = new ArrayList<String>();
			notInCategories.add("CGW Common");
			List<ScServiceAttribute> serviceDetailsAttributesList = scServiceAttributeRepository
					.findByScServiceDetail_idAndIsActiveAndCategoryIsNullOrCategoryNotIn(serviceId, "Y",
							notInCategories);
			Map<String, Object> cgwDataMap = new HashMap<>();
			cgwDataMap.put("reverseISCOutput", reverseISCOutput);
			if (scComponentAttributesMap != null && !scComponentAttributesMap.isEmpty()) {
				logger.info("scComponentAttributesMap exists : {}", scComponentAttributesMap.size());
				scComponentAttributesMap.entrySet().stream()
						.forEach(compAttr -> cgwDataMap.put(compAttr.getKey(), compAttr.getValue()));
			}
			if (serviceDetailsAttributesList != null && !serviceDetailsAttributesList.isEmpty()) {
				logger.info("serviceDetailsAttributesList exists : {}", serviceDetailsAttributesList.size());
				serviceDetailsAttributesList.stream().forEach(
						serviceAttr -> cgwDataMap.put(serviceAttr.getAttributeName(), serviceAttr.getAttributeValue()));
			}
			cgwDataMap.put("serviceTopology", "Full Mesh");
			if (scServiceDetailOptional.isPresent()) {
				logger.info("ServiceDetail exists");
				ScServiceDetail scServiceDetail = scServiceDetailOptional.get();
				logger.info("cloudGatewayType", scServiceDetail.getPrimarySecondary());
				cgwDataMap.put("cloudGatewayType", scServiceDetail.getPrimarySecondary());
				if ("Primary".equalsIgnoreCase(scServiceDetail.getPrimarySecondary())) {
					cgwDataMap.put("secondaryCGWServiceId", scServiceDetail.getPriSecServiceLink());
				} else if ("Secondary".equalsIgnoreCase(scServiceDetail.getPrimarySecondary())) {
					cgwDataMap.put("primaryCGWServiceId", scServiceDetail.getPriSecServiceLink());
				}
				ScOrder scOrder = scServiceDetail.getScOrder();
				if (scOrder != null) {
					logger.info("ScOrder exists : {}", scOrder.getId());
					cgwDataMap.put("customerLeName", scOrder.getErfCustLeName());
					cgwDataMap.put("customerName", scOrder.getErfCustCustomerName());
					cgwDataMap.put("cuid", scOrder.getErfCustLeName());
				}
			}
			String heteroBw = StringUtils.trimToEmpty(scComponentAttributesMap.get("heteroBw"));
			String migrationBw = StringUtils.trimToEmpty(scComponentAttributesMap.get("migrationBw"));
			List<UseCaseDetailBean> useCaseDetailBeanList = new ArrayList<>();
			cgwDataMap.put("useCaseDetails", null);
			if (!useCase.isEmpty()) {
				logger.info("Use Case exists::{}", useCase);
				String useCases[] = useCase.split(";");
				for (String useCaseSplit : useCases) {
					UseCaseDetailBean useCaseDetailBean = new UseCaseDetailBean();
					useCaseDetailBean.setName(useCaseSplit);
					useCaseDetailBean.setBandwidthUnit("Mbps");
					if (useCaseSplit != null) {
						logger.info("Use Case Split exists::{}", useCaseSplit);
						if (replaceSpace(useCaseSplit).toLowerCase().contains("usecase2")) {
							logger.info("Use Case 2 exists");
							useCaseDetailBean.setBandwidth(migrationBw);
						} else if (replaceSpace(useCaseSplit).toLowerCase().contains("usecase4")) {
							logger.info("Use Case 4 exists");
							useCaseDetailBean.setBandwidth(heteroBw);
						}
					}
					useCaseDetailBeanList.add(useCaseDetailBean);
				}
				cgwDataMap.put("useCaseDetails", useCaseDetailBeanList);
			}
			taskDataService.getCosDatailsAttributes(serviceId, cgwDataMap);
			cgwBean.setCommonData(cgwDataMap);
		} catch (Exception e) {
			logger.error("Error in getCGWDetails : {} ", e);
		}
		return cgwBean;
	}
	
	private String replaceSpace(String input){
		return input.replaceAll("\\s+","");
	}
	
	@Transactional
	public NniProtectedPort getSecondaryReverseIsc(String serviceId, String page) {
		logger.info("Inside getSecondaryReverseIsc for serviceID :{} with page:{}" + serviceId, page);
		NniProtectedPort nniProtectedPort = new NniProtectedPort();
		try {
			GetReverseISCData reverseISCData = new GetReverseISCData();
			ReverseISCInput input = new ReverseISCInput();
			input.setRequestId("Optimus-" + serviceId);
			if (StringUtils.isNotBlank(page) && "inventory".equalsIgnoreCase(page)) {
				input.setRelationShip("ACTIVE");
			} else {
				input.setRelationShip("ISSUED");
			}

			Security security = new Security();
			security.setSourceSystem("OPSPORTALBPM");
			security.setUniqueKey("7924677767825276");
			input.setSecurity(security);
			input.setServiceId(serviceId);

			/*
			 * List<Attribute> attribute= new ArrayList<>(); Attribute innerVlan = new
			 * Attribute(); innerVlan.setKey("VLAN"); innerVlan.setValue("DimpleVLAN");
			 * 
			 * Attribute PEName = new Attribute(); PEName.setKey("PEName");
			 * PEName.setValue("DimplePEName");
			 * 
			 * Attribute PEMgmt_IP = new Attribute(); PEMgmt_IP.setKey("PEMgmt_IP");
			 * PEMgmt_IP.setValue("DimplePEMgmtIP");
			 * 
			 * Attribute PELogicalPort = new Attribute();
			 * PELogicalPort.setKey("PELogicalPort");
			 * PELogicalPort.setValue("DimplePELogicalPort");
			 * 
			 * Attribute PEPhysicalPort = new Attribute();
			 * PEPhysicalPort.setKey("PEPhysicalPort");
			 * PEPhysicalPort.setValue("DimplePEPhyscialPort");
			 * 
			 * Attribute BusinessSwitchHostName = new Attribute();
			 * BusinessSwitchHostName.setKey("BusinessSwitchHostName");
			 * BusinessSwitchHostName.setValue("DimpleSyBusinessSwitchHostName");
			 * 
			 * Attribute BusinessSwitch_IP = new Attribute();
			 * BusinessSwitch_IP.setKey("BusinessSwitch_IP");
			 * BusinessSwitch_IP.setValue("DimpleBusinessSwitch_IP");
			 * 
			 * Attribute BusinessSwitchHandoffPort = new Attribute();
			 * BusinessSwitchHandoffPort.setKey("BusinessSwitchHandoffPort");
			 * BusinessSwitchHandoffPort.setValue("DimpleBusinessSwitchHandoffPort");
			 * 
			 * attribute.add(innerVlan); attribute.add(PEName); attribute.add(PEMgmt_IP);
			 * attribute.add(PELogicalPort); attribute.add(PEPhysicalPort);
			 * attribute.add(BusinessSwitchHostName); attribute.add(BusinessSwitch_IP);
			 * attribute.add(BusinessSwitchHandoffPort);
			 * nniProtectedPort.setAttribute(attribute);
			 */
			reverseISCData.setReverseISCInput(input);
			SoapRequest soapRequest = new SoapRequest();
			soapRequest.setUrl(reverseiscurl);
			soapRequest.setRequestObject(reverseISCData);
			soapRequest.setContextPath("com.tcl.dias.serviceactivation.cramer.reverseisc.beans");
			soapRequest.setSoapUserName("");
			soapRequest.setSoapPwd("");
			soapRequest.setConnectionTimeout(60000);
			soapRequest.setReadTimeout(60000);
			JAXBElement jaxbResponse = genericWebserviceClient.doSoapCallForObject(soapRequest, JAXBElement.class);

			if (Objects.nonNull(jaxbResponse)) {
				logger.info("SatSoc Secondary Reverse Isc Jaxb Response : {}", jaxbResponse.getValue().toString());

				GetReverseISCDataResponse iscDataResponse = (GetReverseISCDataResponse) jaxbResponse.getValue();
				nniProtectedPort = iscDataResponse.getReverseISCOutput().getNniProtectedPort();

				if (Objects.nonNull(nniProtectedPort)) {
					logger.info("SatSoc Secondary Reverse Isc nniProtectedPort : {}", nniProtectedPort);
				}
			}

		} catch (Exception e) {
			logger.error("SatSoc Secondary Reverse Isc for serviceId: {} with exception : {} ", serviceId, e);
		}
		return nniProtectedPort;
	}

}
