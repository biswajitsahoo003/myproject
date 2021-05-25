package com.tcl.dias.serviceactivation.service;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;

import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.serviceactivation.entity.entities.Vrf;
import com.tcl.dias.servicefulfillment.entity.entities.ServiceStatusDetails;
import com.tcl.dias.servicefulfillment.entity.repository.ServiceStatusDetailsRepository;
import com.tcl.dias.servicefulfillment.entity.entities.*;
import com.tcl.dias.servicefulfillment.entity.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapFaultDetailElement;
import org.springframework.ws.soap.client.SoapFaultClientException;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.beans.CheckIpClrSyncBean;
import com.tcl.dias.serviceactivation.beans.CramerServiceHeader;
import com.tcl.dias.serviceactivation.beans.DownTimeBean;
import com.tcl.dias.serviceactivation.beans.GetCLRSyncBean;
import com.tcl.dias.serviceactivation.beans.IPServiceSyncBean;
import com.tcl.dias.serviceactivation.beans.IsValidBtsSyncBean;
import com.tcl.dias.serviceactivation.beans.MuxInfoSyncBean;
import com.tcl.dias.serviceactivation.beans.ObjectType;
import com.tcl.dias.serviceactivation.beans.SetCLRSyncBean;
import com.tcl.dias.serviceactivation.constants.CramerConstants;
import com.tcl.dias.serviceactivation.cramer.checkclrinfo.beans.CheckClrInfo;
import com.tcl.dias.serviceactivation.cramer.checkclrinfo.beans.CheckClrInfoResponse;
import com.tcl.dias.serviceactivation.cramer.checkclrinfo.beans.ObjectFactory;
import com.tcl.dias.serviceactivation.cramer.checkclrinfo.beans.ServiceFault;
import com.tcl.dias.serviceactivation.cramer.checkipclr.beans.CheckIPCLRDetail;
import com.tcl.dias.serviceactivation.cramer.checkipclr.beans.CheckIPCLRDetailResponse;
import com.tcl.dias.serviceactivation.cramer.checkipclr.beans.RequestHeader;
import com.tcl.dias.serviceactivation.cramer.createservice.beans.CreateService;
import com.tcl.dias.serviceactivation.cramer.createservice.beans.CreateServiceResponse;
import com.tcl.dias.serviceactivation.cramer.createservicegvpn.beans.Component;
import com.tcl.dias.serviceactivation.cramer.createservicegvpn.beans.ComponentList;
import com.tcl.dias.serviceactivation.cramer.createservicegvpn.beans.MapEntry;
import com.tcl.dias.serviceactivation.cramer.createservicegvpn.beans.ServiceInfo;
import com.tcl.dias.serviceactivation.cramer.downtime.beans.CheckDownTime;
import com.tcl.dias.serviceactivation.cramer.downtime.beans.CheckDownTimeResponse;
import com.tcl.dias.serviceactivation.cramer.eoriordetails.beans.CramerRequestHeader;
import com.tcl.dias.serviceactivation.cramer.eoriordetails.beans.EorIorDependencyBean;
import com.tcl.dias.serviceactivation.cramer.eoriordetails.beans.GetIEOR;
import com.tcl.dias.serviceactivation.cramer.eoriordetails.beans.GetIEORResponse;
import com.tcl.dias.serviceactivation.cramer.eoriordetails.beans.IoreorDependancyOutput;
import com.tcl.dias.serviceactivation.cramer.getclrsync.beans.AsyncCLRInfoFault;
import com.tcl.dias.serviceactivation.cramer.getclrsync.beans.GetCLR;
import com.tcl.dias.serviceactivation.cramer.getclrsync.beans.GetCLRResponse;
import com.tcl.dias.serviceactivation.cramer.getclrsync.beans.RelationShip;
import com.tcl.dias.serviceactivation.cramer.ipservicesync.beans.AssignDummyWANIP;
import com.tcl.dias.serviceactivation.cramer.ipservicesync.beans.AssignDummyWANIPFault;
import com.tcl.dias.serviceactivation.cramer.ipservicesync.beans.AssignDummyWANIPResponse;
import com.tcl.dias.serviceactivation.cramer.ipservicesync.beans.FinalRelationShip;
import com.tcl.dias.serviceactivation.cramer.ipservicesync.beans.GetIPServiceInfo;
import com.tcl.dias.serviceactivation.cramer.ipservicesync.beans.GetIPServiceInfoFault;
import com.tcl.dias.serviceactivation.cramer.ipservicesync.beans.GetIPServiceInfoResponse;
import com.tcl.dias.serviceactivation.cramer.ipservicesync.beans.InitialRelationShip;
import com.tcl.dias.serviceactivation.cramer.ipservicesync.beans.ReleaseDummyWANIP;
import com.tcl.dias.serviceactivation.cramer.ipservicesync.beans.ReleaseDummyWANIPFault;
import com.tcl.dias.serviceactivation.cramer.ipservicesync.beans.ReleaseDummyWANIPResponse;
import com.tcl.dias.serviceactivation.cramer.ipservicesync.beans.SetCLR;
import com.tcl.dias.serviceactivation.cramer.ipservicesync.beans.SetCLRFault;
import com.tcl.dias.serviceactivation.cramer.ipservicesync.beans.SetCLRResponseClr;
import com.tcl.dias.serviceactivation.cramer.isvalidbts.beans.BtsValidationRequest;
import com.tcl.dias.serviceactivation.cramer.isvalidbts.beans.IsValidBTS;
import com.tcl.dias.serviceactivation.cramer.isvalidbts.beans.IsValidBTSResponse;
import com.tcl.dias.serviceactivation.cramer.muxsync.GetMuxDetails;
import com.tcl.dias.serviceactivation.cramer.muxsync.GetMuxDetailsResponse;
import com.tcl.dias.serviceactivation.cramer.muxsync.HandOffDetail;
import com.tcl.dias.serviceactivation.cramer.servicedesign.beans.Acknowledgement;
import com.tcl.dias.serviceactivation.cramer.servicedesign.beans.AdditionalAttr;
import com.tcl.dias.serviceactivation.cramer.servicedesign.beans.Attributes;
import com.tcl.dias.serviceactivation.cramer.servicedesign.beans.ClrDesignDtls;
import com.tcl.dias.serviceactivation.cramer.servicedesign.beans.EndDetails;
import com.tcl.dias.serviceactivation.cramer.servicedesign.beans.IldDtls;
import com.tcl.dias.serviceactivation.cramer.servicedesign.beans.InitiateCLRCreation;
import com.tcl.dias.serviceactivation.cramer.servicedesign.beans.InitiateCLRCreationResponse;
import com.tcl.dias.serviceactivation.cramer.servicedesign.beans.IpServiceAttributes;
import com.tcl.dias.serviceactivation.cramer.servicedesign.beans.NldDtls;
import com.tcl.dias.serviceactivation.cramer.servicedesign.beans.OrderDetails;
import com.tcl.dias.serviceactivation.cramer.servicedesign.beans.UaApplicable;
import com.tcl.dias.serviceactivation.cramer.servicedesign.beans.UccServDetails;
import com.tcl.dias.serviceactivation.cramer.servicedesign.beans.UccService;
import com.tcl.dias.serviceactivation.cramer.servicedesign.beans.WrkrOffnetIfaceDtls;
import com.tcl.dias.serviceactivation.entity.entities.IpDummyDetail;
import com.tcl.dias.serviceactivation.entity.entities.MstCambiumDetails;
import com.tcl.dias.serviceactivation.entity.entities.MstClrVpnSolution;
import com.tcl.dias.serviceactivation.entity.entities.MstP2PDetails;
import com.tcl.dias.serviceactivation.entity.entities.MstRadwinDetails;
import com.tcl.dias.serviceactivation.entity.entities.NetworkInventory;
import com.tcl.dias.serviceactivation.entity.entities.OrderDetail;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.serviceactivation.entity.entities.VpnMetatData;
import com.tcl.dias.serviceactivation.entity.repository.IpDummyDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.LmComponentRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstCambiumDetailsRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstClrVpnSolutionRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstP2PDetailsRepository;
import com.tcl.dias.serviceactivation.entity.repository.MstRadwinDetailsRepository;
import com.tcl.dias.serviceactivation.entity.repository.NetworkInventoryRepository;
import com.tcl.dias.serviceactivation.entity.repository.OrderDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.entities.MstStatus;
import com.tcl.dias.servicefulfillment.entity.entities.MstTaskRegion;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScSolutionComponent;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.MstStatusRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScSolutionComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.beans.CramerInfoBean;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import com.tcl.dias.servicefulfillmentutils.beans.wireless.SSDumpBean;
import com.tcl.dias.servicefulfillmentutils.beans.wireless.SSDumpResponseBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.NotificationService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskDataService;
import com.tcl.dias.webserviceclient.beans.SoapRequest;
import com.tcl.dias.webserviceclient.service.GenericWebserviceClient;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class CramerService {
	private static final Logger logger = LoggerFactory.getLogger(CramerService.class);
	@Autowired
	private GenericWebserviceClient genericWebserviceClient;

	@Value("${cramer.createservice.url}")
	String createServiceUrl;

	@Value("${cramer.createservice.gvpn.url}")
	String createServiceGvpnUrl;

	@Value("${cramer.createclr.url}")
	String createClrUrl;
	
	@Value("${application.env:DEV}")
	String appEnv;

	@Value("${cramer.getmuxinfo.url}")
	String getMuxInfoUrl;

	@Value("${cramer.ipservice.ill.url}")
	String ipServiceIllUrl;

    @Value("${cramer.ipservice.gvpn.url}")
    String ipServiceGvpnUrl;
    
    @Value("${cramer.assign.dummy.ipservice.ill.url}")
   	String ipAssignDummyServiceIllUrl;

    @Value("${cramer.assign.dummy.ipservice.gvpn.url}")
    String ipAssignDummyServiceGvpnUrl;
       
    @Value("${cramer.release.dummy.ipservice.ill.url}")
    String ipReleaseDummyServiceIllUrl;

    @Value("${cramer.release.dummy.ipservice.gvpn.url}")
    String ipReleaseDummyServiceGvpnUrl;

	@Value("${cramer.getclrsync.url}")
	String getClrSyncUrl;

	@Value("${cramer.isvalidbtssync.url}")
	String isValidBtsSyncUrl;

	@Value("${cramer.checkclrinfosync.url}")
	String checkClrInfoSyncUrl;

	@Value("${cramer.checkipclrsync.url}")
	String checkIPClrSyncUrl;

	@Value("${cramer.eoriordetails.url}")
	String eorIorDetailUrl;

	@Value("${cramer.setclr.url}")
    String setClrUrl;

	@Value("${cramer.txdowntime.url}")
	String downtimeUrl;
	
	@Value("${cramer.source.system}")
	String cramerSourceSystem;

    @Autowired
	NetworkInventoryRepository networkInventoryRepository;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	TaskDataService taskDataService;

	@Autowired
	ServiceActivationService serviceActivationService;

	@Autowired
	ServiceDetailRepository serviceDetailRepository;
	
	@Autowired
	OrderDetailRepository orderDetailRepository;
	
	@Autowired
	LmComponentRepository lmComponentRepository;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	MstStatusRepository mstStatusRepository;
	
	@Autowired
	MstCambiumDetailsRepository mstCambiumDetailsRepository;
	
	@Autowired
	MstRadwinDetailsRepository mstRadwinDetailsRepository;
	
	@Autowired
	ScOrderRepository scOrderRepository;
	
	@Autowired
	MstClrVpnSolutionRepository mstClrVpnSolutionRepository;

	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;
	
	@Autowired
	ScServiceAttributeRepository serviceAttributeRepository;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	IpDummyDetailRepository ipDummyDetailRepository;

	@Autowired
	MstP2PDetailsRepository mstP2PDetailsRepository;
	
	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;
	
	@Autowired
	ActivationService activationService;
	
	@Autowired
	NotificationService notificationService;
	
	@Autowired
	NetpRfDataCreationService netpRfDataCreationService;	

	@Autowired
	ServiceStatusDetailsRepository serviceStatusDetailsRepository;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	NetpRfDataSearchService netpRfDataSearchService;
	
	@Autowired
	ScSolutionComponentRepository scSolutionComponentRepository;

	@Autowired
	StageRegionMappingRepository stageRegionMappingRepository;

	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;
	/**
	 * Cramer create service synchronous SOAP call
	 *
	 * @param createServiceJsonAsString
	 * @return
	 * @throws TclCommonException
	 */
	public Response createService(Map<String, String> request) throws TclCommonException, JAXBException {
        Response response = new Response();
		

		com.tcl.dias.serviceactivation.entity.entities.NetworkInventory networkInventory = new NetworkInventory();
		SoapRequest soapRequest = new SoapRequest();
		try {
			
			String prisecLink = request.get("prisecLink");
			if(prisecLink !=null && StringUtils.isNotBlank(prisecLink) && !prisecLink.equals("null")) return createServiceNew(request);
			
			CreateService createServiceReq = new CreateService();
			createServiceReq.setServiceId(request.get("SERVICE_ID"));
			createServiceReq.setCOPFID(request.get("COPF_ID"));
			createServiceReq.setCustomerName(request.get("CUSTOMER_NAME"));
			createServiceReq.setServiceType(request.get("SERVICE_TYPE"));
			createServiceReq.setServiceBandwidthValue(request.get("SERVICE_BANDWIDTH_VALUE"));
			createServiceReq.setServiceBandwidthUnit(request.get("SERVICE_BANDWIDTH_UNIT"));
			createServiceReq.setLMBandwidthValue(request.get("LM_BANDWIDTH_VALUE"));
			createServiceReq.setLMBandwidthUnit(request.get("LM_BANDWIDTH_UNIT"));
			createServiceReq.setServiceOption(request.get("SERVICE_OPTION"));
			if (Objects.nonNull(request.get("SCOPE_OF_MANAGEMENT")) && !request.get("SCOPE_OF_MANAGEMENT").isEmpty()) {
				createServiceReq.setScopeOfManagement(request.get("SCOPE_OF_MANAGEMENT"));
			}
			createServiceReq.setRequestID(request.get("REQUEST_ID"));
			//createServiceReq.setRequestingSystem(request.get("REQUESTING_SYSTEM"));
			createServiceReq.setRequestingSystem(cramerSourceSystem);
			
			// Save request & response
			networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
			networkInventory.setServiceCode(request.get("SERVICE_ID"));
			networkInventory.setRequestId(request.get("REQUEST_ID"));
			networkInventory.setRequest(Utils.convertObjectToXmlString(createServiceReq, CreateService.class));
			networkInventory.setType(CramerConstants.CREATE_SERVICE);
			soapRequest.setUrl(createServiceUrl);
			soapRequest.setRequestObject(createServiceReq);
			soapRequest.setContextPath("com.tcl.dias.serviceactivation.cramer.createservice.beans");
			soapRequest.setSoapUserName("");
			soapRequest.setSoapPwd("");
			soapRequest.setConnectionTimeout(60000);
			soapRequest.setReadTimeout(60000);
			CreateServiceResponse createServiceResponse = genericWebserviceClient.doSoapCallForObject(soapRequest,
					CreateServiceResponse.class);
			logger.info("Create Service acknowledgement Response=> {}", createServiceResponse.getResponse());
			if (createServiceResponse != null && createServiceResponse.getResponse() != null) {
				networkInventory.setResponse(
						Utils.convertObjectToXmlString(createServiceResponse, CreateServiceResponse.class));
				networkInventoryRepository.save(networkInventory);
//				return Boolean.toString(createServiceResponse.getResponse().isStatus());
                response.setStatus(createServiceResponse.getResponse().isStatus());
			}

		} catch (SoapFaultClientException ex) {
			logger.error("SoapFaultClientException in Cramer createService Service ", ex);
			logger.error("getFaultCode {} , getFaultString {}", ex.getSoapFault().getFaultCode());
			SoapFaultDetail soapFaultDetail = ex.getSoapFault().getFaultDetail();
			if (soapFaultDetail != null) {
				SoapFaultDetailElement detailElementChild = soapFaultDetail.getDetailEntries().next();
				Source detailSource = detailElementChild.getSource();
				Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
				marshaller.setContextPath(soapRequest.getContextPath());
				JAXBElement<com.tcl.dias.serviceactivation.cramer.createservice.beans.ServiceFault> source = (JAXBElement<com.tcl.dias.serviceactivation.cramer.createservice.beans.ServiceFault>) marshaller
						.unmarshal(detailSource);
				logger.error("ErrorCode:: {} , ErrorLongDescription:: {},ErrorShortDescription:: {}",
						source.getValue().getErrorCode(), source.getValue().getErrorLongDescription(),
						source.getValue().getErrorShortDescription());
				networkInventory.setResponse(source.getValue().getErrorCode() +" : "+ source.getValue().getErrorLongDescription());
				response.setErrorCode(source.getValue().getErrorCode());
				response.setErrorMessage(source.getValue().getErrorLongDescription());
			}else{
				networkInventory.setResponse(ex.getFaultStringOrReason());
				response.setErrorCode(String.valueOf(ex.getFaultCode()));
				response.setErrorMessage(ex.getFaultStringOrReason());
			}
			networkInventoryRepository.save(networkInventory);
		
			return response;
		} catch (Exception e) {
			logger.error("Exception in Cramer Create Service ", e);
			networkInventory.setResponse(e.getMessage());
			networkInventoryRepository.save(networkInventory);
//			return e.getMessage();
            response.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
            response.setErrorMessage(e.getMessage());
		}
		return response;
	}
	
	public Response createServiceNew(Map<String, String> request) throws TclCommonException, JAXBException {
        Response response = new Response();
       

		com.tcl.dias.serviceactivation.entity.entities.NetworkInventory networkInventory = new NetworkInventory();
		SoapRequest soapRequest = new SoapRequest();
		try {
			
			com.tcl.dias.serviceactivation.cramer.createservicenew.beans.CreateService createServiceReq = new com.tcl.dias.serviceactivation.cramer.createservicenew.beans.CreateService();
			createServiceReq.setServiceId(request.get("SERVICE_ID"));
			
			 logger.info("createServiceNew for Service={} ", request.get("SERVICE_ID"));
			 
			createServiceReq.setCOPFID(request.get("COPF_ID"));
			createServiceReq.setCustomerName(request.get("CUSTOMER_NAME"));
			createServiceReq.setServiceType(request.get("SERVICE_TYPE"));
			createServiceReq.setServiceBandwidthValue(request.get("SERVICE_BANDWIDTH_VALUE"));
			createServiceReq.setServiceBandwidthUnit(request.get("SERVICE_BANDWIDTH_UNIT"));
			createServiceReq.setLMBandwidthValue(request.get("LM_BANDWIDTH_VALUE"));
			createServiceReq.setLMBandwidthUnit(request.get("LM_BANDWIDTH_UNIT"));
			createServiceReq.setServiceOption(request.get("SERVICE_OPTION"));
						
			createServiceReq.setPriSecMapping(request.get(CramerConstants.PRIMARY_SECONDARY));
			createServiceReq.setServiceLink(request.get("prisecLink"));
			
			if (Objects.nonNull(request.get("SCOPE_OF_MANAGEMENT")) && !request.get("SCOPE_OF_MANAGEMENT").isEmpty()) {
				createServiceReq.setScopeOfManagement(request.get("SCOPE_OF_MANAGEMENT"));
			}
			createServiceReq.setRequestID(request.get("REQUEST_ID"));
			//createServiceReq.setRequestingSystem(request.get("REQUESTING_SYSTEM"));
			createServiceReq.setRequestingSystem(cramerSourceSystem);
			
			// Save request & response
			networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
			networkInventory.setServiceCode(request.get("SERVICE_ID"));
			networkInventory.setRequestId(request.get("REQUEST_ID"));
			networkInventory.setRequest(Utils.convertObjectToXmlString(createServiceReq, com.tcl.dias.serviceactivation.cramer.createservicenew.beans.CreateService.class));
			networkInventory.setType(CramerConstants.CREATE_SERVICE);
			
			String createServiceUrlNew = "http://cramicubeprd.vsnl.co.in:8102/CramerCS/ws/CreateService.wsdl";
			
			if (appEnv!=null && "DEV".equalsIgnoreCase(appEnv)) {
				createServiceUrlNew = "http://uswv1vusp002a.vsnl.co.in:8102/CramerCS/ws/CreateService.wsdl";
			}
				
			soapRequest.setUrl(createServiceUrlNew);
			soapRequest.setRequestObject(createServiceReq);
			soapRequest.setContextPath("com.tcl.dias.serviceactivation.cramer.createservicenew.beans");
			soapRequest.setSoapUserName("");
			soapRequest.setSoapPwd("");
			soapRequest.setConnectionTimeout(60000);
			soapRequest.setReadTimeout(60000);
			com.tcl.dias.serviceactivation.cramer.createservicenew.beans.CreateServiceResponse createServiceResponse = genericWebserviceClient.doSoapCallForObject(soapRequest,
					com.tcl.dias.serviceactivation.cramer.createservicenew.beans.CreateServiceResponse.class);
			logger.info("Create Service acknowledgement Response=> {}", createServiceResponse.getResponse());
			if (createServiceResponse != null && createServiceResponse.getResponse() != null) {
				networkInventory.setResponse(
						Utils.convertObjectToXmlString(createServiceResponse, com.tcl.dias.serviceactivation.cramer.createservicenew.beans.CreateServiceResponse.class));
				networkInventoryRepository.save(networkInventory);
//				return Boolean.toString(createServiceResponse.getResponse().isStatus());
                response.setStatus(createServiceResponse.getResponse().isStatus());
			}

		} catch (SoapFaultClientException ex) {
			logger.error("SoapFaultClientException in Cramer createService Service ", ex);
			logger.error("getFaultCode {} , getFaultString {}", ex.getSoapFault().getFaultCode());
			SoapFaultDetail soapFaultDetail = ex.getSoapFault().getFaultDetail();
			if (soapFaultDetail != null) {
				SoapFaultDetailElement detailElementChild = soapFaultDetail.getDetailEntries().next();
				Source detailSource = detailElementChild.getSource();
				Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
				marshaller.setContextPath(soapRequest.getContextPath());
				JAXBElement<com.tcl.dias.serviceactivation.cramer.createservicenew.beans.ServiceFault> source = (JAXBElement<com.tcl.dias.serviceactivation.cramer.createservicenew.beans.ServiceFault>) marshaller
						.unmarshal(detailSource);
				logger.error("ErrorCode:: {} , ErrorLongDescription:: {},ErrorShortDescription:: {}",
						source.getValue().getErrorCode(), source.getValue().getErrorLongDescription(),
						source.getValue().getErrorShortDescription());
				networkInventory.setResponse(source.getValue().getErrorCode() +" : "+ source.getValue().getErrorLongDescription());
				response.setErrorCode(source.getValue().getErrorCode());
				response.setErrorMessage(source.getValue().getErrorLongDescription());
			}else{
				networkInventory.setResponse(ex.getFaultStringOrReason());
				response.setErrorCode(String.valueOf(ex.getFaultCode()));
				response.setErrorMessage(ex.getFaultStringOrReason());
			}
			networkInventoryRepository.save(networkInventory);
		
			return response;
		} catch (Exception e) {
			logger.error("Exception in Cramer Create Service ", e);
			networkInventory.setResponse(e.getMessage());
			networkInventoryRepository.save(networkInventory);
//			return e.getMessage();
            response.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
            response.setErrorMessage(e.getMessage());
		}
		return response;
	}


	public Response createServiceGvpn(Map<String, String> request) throws TclCommonException {
        Response response = new Response();
		ServiceInfo serviceInfo = new com.tcl.dias.serviceactivation.cramer.createservicegvpn.beans.ObjectFactory().createServiceInfo();
		logger.info("CREATE-SERVICE:createServiceGvpn-ServiceId={} GVPN_ORDER_TYPE:{} GVPN_ORDER_CATEGORY:{}",request.get("SERVICE_ID"),request.get("GVPN_ORDER_TYPE"),request.get("GVPN_ORDER_CATEGORY"));
		com.tcl.dias.serviceactivation.cramer.createservicegvpn.beans.Service service = new com.tcl.dias.serviceactivation.cramer.createservicegvpn.beans.Service();
		if("NEW".equalsIgnoreCase(request.get("GVPN_ORDER_TYPE")) || 
				("MACD".equals(request.get("GVPN_ORDER_TYPE")) && "ADD_SITE".equals(request.get("GVPN_ORDER_CATEGORY")))
				|| ("MACD".equals(request.get("GVPN_ORDER_TYPE")) && Objects.nonNull(request.get("GVPN_ORDER_CATEGORY")) 
						&& request.get("GVPN_ORDER_CATEGORY").toLowerCase().contains("parallel"))){
			logger.info("NEW OR ADD SITE OR PARALLEL");
			service.setActionType("CREATE");
			service.setSolutionId(request.get("SOLUTION_ID"));
		}else if("MACD".equals(request.get("GVPN_ORDER_TYPE"))){
			response.setStatus(true);
			return response;
		}
		
		ScServiceDetail scServiceDetail=scServiceDetailRepository.findByUuidAndMstStatus_code(request.get("SERVICE_ID"), "INPROGRESS");
		ScServiceAttribute currentVpnTopolyAttr = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(),"VPN Topology");
		ScServiceAttribute siteTypeAttr = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(),"Site Type");
		if("MACD".equals(request.get("GVPN_ORDER_TYPE")) && (("ADD_SITE".equals(request.get("GVPN_ORDER_CATEGORY")))
				|| (Objects.nonNull(request.get("GVPN_ORDER_CATEGORY")) && request.get("GVPN_ORDER_CATEGORY").toLowerCase().contains("parallel")))){
			if(Objects.nonNull(scServiceDetail) && Objects.nonNull(scServiceDetail.getParentUuid()) && !scServiceDetail.getParentUuid().isEmpty()){
				logger.info("CREATE-SERVICE:ParentUUId-is-not-empty-for-ADD-SITE-ServiceId={} ParentServiceId:{}",scServiceDetail.getUuid(),scServiceDetail.getParentUuid());
				MstClrVpnSolution mstClrVpnSolution=null;
				if(currentVpnTopolyAttr !=null && StringUtils.isNotBlank(currentVpnTopolyAttr.getAttributeValue())){
					logger.info("CREATE-SERVICE:CurrentVpnTopology-exists-ServiceId={} ParentServiceId:{} VpnTopoly:{}",scServiceDetail.getUuid(),scServiceDetail.getParentUuid(),currentVpnTopolyAttr.getAttributeValue());
				
					mstClrVpnSolution=mstClrVpnSolutionRepository.findFirstByServiceCodeAndVpnTopologyIgnoreCase(scServiceDetail.getParentUuid(), currentVpnTopolyAttr.getAttributeValue());
					if(Objects.nonNull(mstClrVpnSolution) && Objects.nonNull(mstClrVpnSolution.getSolutionId()) && !mstClrVpnSolution.getSolutionId().isEmpty()){
						logger.info("CREATE-SERVICE:Clr-Vpn-Solution-exists-for-ServiceId:{} oldVpnName:{} ParentServiceId:{}", scServiceDetail.getUuid(),mstClrVpnSolution.getSolutionId(),scServiceDetail.getParentUuid());
						service.setSolutionId(mstClrVpnSolution.getSolutionId());
						createMstClrVpnSolution(scServiceDetail, mstClrVpnSolution, currentVpnTopolyAttr, siteTypeAttr,null);
					}
				}
				
				ServiceDetail parentServiceDetail=serviceDetailRepository.findFirstByServiceIdOrderByVersionDesc(scServiceDetail.getParentUuid());
				if(mstClrVpnSolution == null && parentServiceDetail != null){
					logger.info("CREATE-SERVICE:ParentServiceDetail-Exists-for-Add-Site-ServiceId:{} ParentServiceId:{}", scServiceDetail.getUuid(),scServiceDetail.getParentUuid());
					Set<VpnMetatData> parentVpnMetaDatas=parentServiceDetail.getVpnMetatDatas();
					if(Objects.nonNull(parentVpnMetaDatas) && !parentVpnMetaDatas.isEmpty()){
						logger.info("CREATE-SERVICE:ParentVPNMetatdata-exists-ServiceId:{} ParentServiceId:{}", scServiceDetail.getUuid(),scServiceDetail.getParentUuid());
						Optional<VpnMetatData> parentVpnMetatDataOptional=parentVpnMetaDatas.stream().findFirst();
						if(parentVpnMetatDataOptional.isPresent()){
							logger.info("CREATE-SERVICE:parentVpnMetatData-exists-for-ServiceId:{} ParentServiceId:{}", scServiceDetail.getUuid(),scServiceDetail.getParentUuid());
							VpnMetatData parentVpnMetatData=parentVpnMetatDataOptional.get();
							if(Objects.nonNull(parentVpnMetatData.getVpnName()) && !parentVpnMetatData.getVpnName().isEmpty()){
								logger.info("CREATE-SERVICE:Taking-solutionId-from-Previous-record-for-Add-Site-ServiceId:{} oldVpnName:{} ParentServiceId:{}",scServiceDetail.getUuid(),parentVpnMetatData.getVpnName(),scServiceDetail.getParentUuid());
								service.setSolutionId(parentVpnMetatData.getVpnName());
							}
							createMstClrVpnSolution(scServiceDetail, mstClrVpnSolution, currentVpnTopolyAttr, siteTypeAttr,parentVpnMetatData);
						}
					}
				}
			}else {
				logger.info("CREATE-SERVICE:ParentUUid Not-exists- for ServiceId:{}", scServiceDetail.getId());
				createMstClrVpnSolution(scServiceDetail, null, currentVpnTopolyAttr, siteTypeAttr,null);
			}
		}else if(scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("IZOPC")) {
			logger.info("CREATE-SERVICE:IZOPC-ServiceId={}",scServiceDetail.getId());
			ScServiceAttribute referenceServiceIdAttribute=scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(),"Service Id");
			if(referenceServiceIdAttribute!=null && referenceServiceIdAttribute.getAttributeValue()!=null && !referenceServiceIdAttribute.getAttributeValue().isEmpty()) {
				logger.info("CREATE-SERVICE:IZOPC-ServiceId={} with Reference:{}",scServiceDetail.getId(),referenceServiceIdAttribute.getAttributeValue());
				MstClrVpnSolution mstClrVpnSolution=null;
				if(currentVpnTopolyAttr !=null && StringUtils.isNotBlank(currentVpnTopolyAttr.getAttributeValue())){
					logger.info("CREATE-SERVICE:IZOPC CurrentVpnTopology-exists-ServiceId={} ReferenceServiceId:{} VpnTopoly:{}",scServiceDetail.getUuid(),referenceServiceIdAttribute.getAttributeValue(),currentVpnTopolyAttr.getAttributeValue());
					mstClrVpnSolution=mstClrVpnSolutionRepository.findFirstByServiceCodeAndVpnTopologyIgnoreCase(referenceServiceIdAttribute.getAttributeValue(), currentVpnTopolyAttr.getAttributeValue());
					if(Objects.nonNull(mstClrVpnSolution) && Objects.nonNull(mstClrVpnSolution.getSolutionId()) && !mstClrVpnSolution.getSolutionId().isEmpty()){
						logger.info("CREATE-SERVICE:IZOPC Clr-Vpn-Solution-exists-for-ServiceId:{} oldVpnName:{} ReferenceServiceId:{}", scServiceDetail.getUuid(),mstClrVpnSolution.getSolutionId(),referenceServiceIdAttribute.getAttributeValue());
						service.setSolutionId(mstClrVpnSolution.getSolutionId());
						createMstClrVpnSolution(scServiceDetail, mstClrVpnSolution, currentVpnTopolyAttr, siteTypeAttr,null);
					}
				}
			}
		}
		service.setCustomerName(request.get("CUSTOMER_NAME"));
		service.setType("GVPN");
		service.setId(request.get("SERVICE_ID"));
		List<MapEntry> mapEntryList = new ArrayList<>();
		MapEntry mapEntry = new MapEntry();
		mapEntry.setAttrName("Service Topology");
		mapEntry.setAttrValue(request.get("SERVICE_TOPOLOGY"));
		mapEntryList.add(mapEntry);
		service.getParam().addAll(mapEntryList);
		serviceInfo.setService(service);
		ComponentList componentList = new ComponentList();
		List<Component> Components = new ArrayList<>();
		Component cmp = new Component();
		cmp.setId("?");
		cmp.setType("?");
		Components.add(cmp);
		componentList.getComponent().addAll(Components);
		serviceInfo.setComponentList(componentList);

		JAXBElement<ServiceInfo> createClrGvpnElement = new com.tcl.dias.serviceactivation.cramer.createservicegvpn.beans.ObjectFactory().createServiceInfo(serviceInfo);

		com.tcl.dias.serviceactivation.entity.entities.NetworkInventory networkInventory = new NetworkInventory();
		SoapRequest soapRequest = new SoapRequest();
		try {
			// Save request & response
			networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
			networkInventory.setServiceCode(request.get("SERVICE_ID"));
			networkInventory.setRequestId(request.get("SERVICE_ID"));
			networkInventory.setRequest(Utils.convertObjectToXmlString(serviceInfo, ServiceInfo.class));
			networkInventory.setType(CramerConstants.CREATE_SERVICE_GVPN);
			soapRequest.setUrl(createServiceGvpnUrl);
			soapRequest.setRequestObject(createClrGvpnElement);
			soapRequest.setContextPath("com.tcl.dias.serviceactivation.cramer.createservicegvpn.beans");
			soapRequest.setSoapUserName("");
			soapRequest.setSoapPwd("");
			soapRequest.setConnectionTimeout(60000);
			soapRequest.setReadTimeout(60000);
			logger.info("Create Service gvpn request Response=> {}", Utils.convertObjectToJson(soapRequest));
			JAXBElement jaxBResponse = genericWebserviceClient.doSoapCallForObject(soapRequest, JAXBElement.class);
			Boolean createServiceResponse = (Boolean) jaxBResponse.getValue();

			logger.info("Create Service gvpn acknowledgement Response=> {}", createServiceResponse);
			if (createServiceResponse != null) {
				networkInventory.setResponse(String.valueOf(createServiceResponse));
				networkInventoryRepository.save(networkInventory);
//				return Boolean.toString(createServiceResponse.getResponse().isStatus());
                response.setStatus(true);
			}else {
				networkInventory.setResponse("Null or invalid response");
				networkInventoryRepository.save(networkInventory);
			}

		} catch (SoapFaultClientException ex) {
			logger.error("SoapFaultClientException in Cramer createService Service ", ex);
			logger.error("getFaultCode {} , getFaultString {}", ex.getSoapFault().getFaultCode());
			SoapFaultDetail soapFaultDetail = ex.getSoapFault().getFaultDetail();
			if (soapFaultDetail != null) {
				SoapFaultDetailElement detailElementChild = soapFaultDetail.getDetailEntries().next();
				Source detailSource = detailElementChild.getSource();
				Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
				marshaller.setContextPath(soapRequest.getContextPath());
				JAXBElement<com.tcl.dias.serviceactivation.cramer.createservice.beans.ServiceFault> source = (JAXBElement<com.tcl.dias.serviceactivation.cramer.createservice.beans.ServiceFault>) marshaller
						.unmarshal(detailSource);
				logger.error("ErrorCode:: {} , ErrorLongDescription:: {},ErrorShortDescription:: {}",
						source.getValue().getErrorCode(), source.getValue().getErrorLongDescription(),
						source.getValue().getErrorShortDescription());
				networkInventory.setResponse(source.getValue().getErrorCode() +" : "+ source.getValue().getErrorLongDescription());
				response.setErrorCode(source.getValue().getErrorCode());
				response.setErrorMessage(source.getValue().getErrorLongDescription());
			}else {
				networkInventory.setResponse(ex.getMessage());
				response.setErrorCode(String.valueOf(ex.getFaultCode()));
				response.setErrorMessage(ex.getFaultStringOrReason());
			}
			networkInventoryRepository.save(networkInventory);
			
			return response;
		} catch (Exception e) {
			logger.error("Exception in Cramer Create Service ", e);
			networkInventory.setResponse(e.getMessage());
			networkInventoryRepository.save(networkInventory);
//			return e.getMessage();
            response.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
            response.setErrorMessage(e.getMessage());
		}
		return response;
	}

 @Transactional(isolation = Isolation.READ_UNCOMMITTED)
 public Response createCLRByTaskId(CramerInfoBean cramerInfoBean) throws TclCommonException {
    Response response = new Response();
    NetworkInventory networkInventory = new NetworkInventory();
	try {
		Map<String, Object> taskDataMap = new HashMap<>();
		Map<String, String> attributesMap = new HashMap<String, String>();
		
		Utils.callGc();
		
		Optional<Task> taskOptional = taskRepository.findById(cramerInfoBean.getTaskId());
		if (taskOptional.isPresent()) {
			Task task = taskOptional.get();
			taskDataMap = taskDataService.getTaskData(task);
			logger.info("taskDataMap in Create CLR {}", taskDataMap);
			
			  if(task.getServiceType()!=null && task.getServiceType().equalsIgnoreCase("IZOSDWAN_CGW")) {
					return createCLRForCGW(cramerInfoBean, task, taskDataMap, attributesMap);
			  }
			
			  if(task.getServiceType()!=null && task.getServiceType().equalsIgnoreCase("NPL")) {
					return createCLRForNpl(cramerInfoBean, task, taskDataMap, attributesMap);
				}
			 
			  ScComponentAttribute skipAutoClrAttr = scComponentAttributesRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
								task.getServiceId(),"skipAutoClr" , "LM", "A");
			  if(Objects.nonNull(skipAutoClrAttr) && Objects.nonNull(skipAutoClrAttr.getAttributeValue()) 
						&& !skipAutoClrAttr.getAttributeValue().isEmpty()
						&& "Yes".equalsIgnoreCase(skipAutoClrAttr.getAttributeValue())) {
					logger.info("Skip Auto Clr exists {}", skipAutoClrAttr.getAttributeValue());
					saveServiceDetails(task);
					networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
					networkInventory.setServiceCode(task.getServiceCode());
					networkInventory.setRequestId(cramerInfoBean.getProcessInstanceId());
					networkInventory.setRequest("Skip CLR Request");
					networkInventory.setResponse("Skip CLR Response");
					networkInventory.setType(CramerConstants.SKIP_CLR);
					networkInventoryRepository.save(networkInventory);
					response.setStatus(true);
					response.setData("skip");
					return response;
				}
			  	logger.info("Skip Auto Clr doesn't exists {}");
				ServiceDetail serviceDetail =saveServiceDetails(task);
				Map<String, String> scServiceAttributesMap = new HashMap<>();

				List<ScServiceAttribute> scServiceAttributes = serviceAttributeRepository
						.findByScServiceDetail_idAndAttributeNameIn(task.getServiceId(),
								Arrays.asList("WAN IP Provided By", "LAN IP Provided By"));
				scServiceAttributes.forEach(attr -> {

					scServiceAttributesMap.put(attr.getAttributeName(), attr.getAttributeValue());

				});
				
				
			
         
            String lmType = String.valueOf(taskDataMap.get("lmType"));
            InitiateCLRCreation initiateCLRCreation = new InitiateCLRCreation();
			initiateCLRCreation.setSERVICEID(task.getServiceCode());
			IpServiceAttributes ipServiceAttributes = new IpServiceAttributes();

			logger.info("Set ORDER TYPE, ORDER CATEGORY Dynamically");
			String orderType=(String)taskDataMap.get(CramerConstants.ORDER_TYPE);
			String orderCategory=Objects.nonNull(taskDataMap.get(CramerConstants.ORDER_CATEGORY))?(String)taskDataMap.get(CramerConstants.ORDER_CATEGORY):"NEW";
			String orderSubCategory=Objects.nonNull(taskDataMap.get(CramerConstants.ORDER_SUB_CATEGORY))?(String)taskDataMap.get(CramerConstants.ORDER_SUB_CATEGORY):null;
			orderCategory = OrderCategoryMapping.getOrderCategory(orderCategory, orderSubCategory);
			orderSubCategory = OrderCategoryMapping.getOrderSubCategory(orderCategory, orderSubCategory);
			String serviceVariant = StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.SERVICE_VARIANT));
			String productName = StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.PRODUCT_NAME));
			String ipArrangement ="";
			ScServiceDetail previousActiveServiceDetails=null;
			if (orderType.equalsIgnoreCase(CramerConstants.TYPE_MACD) && !"ADD_SITE".equalsIgnoreCase(orderCategory)
					&& (Objects.isNull(orderSubCategory) || (Objects.nonNull(orderSubCategory) && !orderSubCategory.toLowerCase().contains("parallel")))) {
				 previousActiveServiceDetails = scServiceDetailRepository
						.findByUuidAndMstStatus_code(task.getServiceCode(), TaskStatusConstants.ACTIVE);
			}
			
			 //updateOnnetWirelessDetails Details
			  if (orderType.equalsIgnoreCase(CramerConstants.TYPE_MACD) && !"ADD_SITE".equalsIgnoreCase(orderCategory)
						&& ((Objects.nonNull(orderSubCategory) && !orderSubCategory.toLowerCase().contains("parallel")))
						&& (lmType.equalsIgnoreCase("OnnetRF") || lmType.equalsIgnoreCase("onnet wireless"))) {
				  logger.info("Onnet RF MACD: {}", task.getServiceCode());
				  updateOnnetWirelessDetails(task.getServiceCode(),serviceDetail, orderSubCategory);
			  }else{
				  logger.info("Onnet RF NEW or PARALLEL or ADD_SITE: {}", task.getServiceCode());
				  constructOnnetWirelessDetails(task.getServiceCode(),serviceDetail.getScServiceDetailId());
			  }
			  
			//MACD ADDITIONAL IP CHANGES
			if(orderType.equals(CramerConstants.TYPE_MACD) && !"ADD_SITE".equalsIgnoreCase(orderCategory) && (Objects.isNull(orderSubCategory) || (Objects.nonNull(orderSubCategory) && !orderSubCategory.toLowerCase().contains("parallel")))
					&& (productName.equalsIgnoreCase(CramerConstants.IAS) || productName.equalsIgnoreCase(CramerConstants.ILL)|| productName.equalsIgnoreCase(CramerConstants.GVPN))){
				ipArrangement = StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.ADDITIONAL_IP_ADDR_MGMT));
				if (ipArrangement == null || ipArrangement.isEmpty()) {
					logger.info("IPArrangement not exists");
					ScServiceAttribute scServiceAttribute = serviceAttributeRepository
							.findByScServiceDetail_idAndAttributeName(previousActiveServiceDetails.getId(),
									CramerConstants.ADDITIONAL_IP_ATTR_NAME);
						if (scServiceAttribute != null) {
							logger.info("Prev Additional IPArrangement exists");
							ipArrangement=scServiceAttribute.getAttributeValue();
							updateServiceAttr(ipArrangement,scServiceAttribute,serviceDetail);
						}else{
							ScServiceAttribute scIpServiceAttribute = serviceAttributeRepository
									.findByScServiceDetail_idAndAttributeName(previousActiveServiceDetails.getId(),
											CramerConstants.IP_ADDR_ATTR_NAME);
								if (scIpServiceAttribute != null) {
									logger.info("Prev IPArrangement exists");
									ipArrangement=scIpServiceAttribute.getAttributeValue();
									updateServiceAttr(ipArrangement,scIpServiceAttribute,serviceDetail);
								}
						}
						logger.info("IPArrangement {}::",ipArrangement);
				}
			}else if(orderType.equals(CramerConstants.TYPE_NEW) || (orderType.equals(CramerConstants.TYPE_MACD) && "ADD_SITE".equalsIgnoreCase(orderCategory))
					||  (orderType.equals(CramerConstants.TYPE_MACD) && Objects.nonNull(orderSubCategory) && orderSubCategory.toLowerCase().contains("parallel"))){
				ipArrangement = StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.IP_ADDR_MGMT));
			}
			
			
			if (productName.equalsIgnoreCase(CramerConstants.IAS) ||productName.equalsIgnoreCase(CramerConstants.ILL)) {
				initiateCLRCreation.setSERVICETYPE(CramerConstants.ILL);
				if(ipArrangement.equalsIgnoreCase("Dual")) {
					ipServiceAttributes.setIPAddressArrangement("TCL IPv4 AND TCL IPv6 Addresses (Dual Stack)");
				}else if(ipArrangement.equalsIgnoreCase("IPv6")) {
					ipServiceAttributes.setIPAddressArrangement(CramerConstants.TCL_IPV6_ADDRESSES);
				}else {
					ipServiceAttributes.setIPAddressArrangement(CramerConstants.TCL_IPV4_ADDRESSES);
				}
				ipServiceAttributes.setPathType(CramerConstants.EMPTY);
			}else if (productName.equalsIgnoreCase(CramerConstants.GVPN)) {
				initiateCLRCreation.setSERVICETYPE(CramerConstants.GVPN);
					if (scServiceAttributesMap.containsKey("WAN IP Provided By")
							&& scServiceAttributesMap.get("WAN IP Provided By").equalsIgnoreCase("customer")) {
						ipServiceAttributes.setCustomerWANIPSpecified(true);
					}
				ipServiceAttributes.setIpPoolTypeRequired("VPN");
				if(ipArrangement.equalsIgnoreCase("Dual")) {
					ipServiceAttributes.setPathType("Dual Stack");
				}else if(ipArrangement.equalsIgnoreCase("IPv6")) {
					ipServiceAttributes.setPathType("IPv6");
				}else {
					ipServiceAttributes.setPathType("IPv4");
				}
				logger.info("WanIpProvidedByCust Check");
				Map<String, String> scComponentAttributesAMap =	commonFulfillmentUtils.getComponentAttributes(task.getServiceId(), AttributeConstants.COMPONENT_LM, "A");
				if(Objects.nonNull(scComponentAttributesAMap) && !scComponentAttributesAMap.isEmpty() && scComponentAttributesAMap.containsKey("wanIpProvidedByCust")
						&& Objects.nonNull(scComponentAttributesAMap.get("wanIpProvidedByCust")) && "Yes".equalsIgnoreCase(scComponentAttributesAMap.get("wanIpProvidedByCust"))){
					logger.info("wanIpProvidedByCust");
					ipServiceAttributes.setCustomerWANIPSpecified(true);
				}
			}
			
			
			if(orderType.equals(CramerConstants.TYPE_NEW) || (orderType.equals(CramerConstants.TYPE_MACD) && "ADD_SITE".equalsIgnoreCase(orderCategory))
					||  (orderType.equals(CramerConstants.TYPE_MACD) && Objects.nonNull(orderSubCategory) && orderSubCategory.toLowerCase().contains("parallel"))){
				ipServiceAttributes.setIsIpPathTypeChanged(false);
			}else if(orderType.equals(CramerConstants.TYPE_MACD) && (Objects.isNull(orderSubCategory) || (Objects.nonNull(orderSubCategory) && !orderSubCategory.toLowerCase().contains("parallel"))) && 
					(productName.equalsIgnoreCase(CramerConstants.IAS) || productName.equalsIgnoreCase(CramerConstants.ILL)|| productName.equalsIgnoreCase(CramerConstants.GVPN))){
				logger.info("Validating MACD IsIpPathTypeChanged {}::",previousActiveServiceDetails.getId());
				String prevIpArrangement=null;
				ScServiceAttribute scServiceAttribute = serviceAttributeRepository
						.findByScServiceDetail_idAndAttributeName(previousActiveServiceDetails.getId(),
								CramerConstants.ADDITIONAL_IP_ATTR_NAME);
					if (Objects.nonNull(scServiceAttribute)) {
						logger.info("ADD IP");
						prevIpArrangement=scServiceAttribute.getAttributeValue();
					}else{
						ScServiceAttribute scIpServiceAttribute = serviceAttributeRepository
								.findByScServiceDetail_idAndAttributeName(previousActiveServiceDetails.getId(),
										CramerConstants.IP_ADDR_ATTR_NAME);
							if (Objects.nonNull(scIpServiceAttribute)) {
								logger.info("IP");
								prevIpArrangement=scIpServiceAttribute.getAttributeValue();
							}
					}
					
					if((Objects.nonNull(prevIpArrangement) && "DUAL".equalsIgnoreCase(prevIpArrangement)) || (prevIpArrangement==null)){
						logger.info("Ip Path Type doesn't changed for Dual");
						ipServiceAttributes.setIsIpPathTypeChanged(false);
					}else{
						if(Objects.nonNull(prevIpArrangement) && Objects.nonNull(ipArrangement) && !prevIpArrangement.equals(ipArrangement)){
							logger.info("Ip Path Type Changed");
							ipServiceAttributes.setIsIpPathTypeChanged(true);
						}else if(Objects.nonNull(prevIpArrangement) && Objects.nonNull(ipArrangement) && prevIpArrangement.equals(ipArrangement)){
							logger.info("Ip Path Type doesn't changed");
							ipServiceAttributes.setIsIpPathTypeChanged(false);
						}
					}
					
			}
			
				if (productName.equalsIgnoreCase(CramerConstants.GVPN)) {
					List<ScServiceAttribute> cosAttributes = serviceAttributeRepository
							.findByScServiceDetail_idAndAttributeNameInAndCategory(serviceDetail.getScServiceDetailId(),
									Arrays.asList("cos 1", "cos 2", "cos 6", "cos 5", "cos 3", "cos 4"), "GVPN Common");

					logger.info("saveServiceCosCriteraForGvpn for GVPN with scServiceAttributes :{}and serviceCode:{} ",
							cosAttributes.size(), serviceDetail.getServiceId());

					Map<String, String> cosMap = new HashMap<>();

					cosAttributes.stream().forEach(scAttr -> {
						if (scAttr.getAttributeValue() != null && !scAttr.getAttributeValue().isEmpty()
								&& Integer.valueOf(scAttr.getAttributeValue().replaceAll("%","")) > 0) {
							cosMap.put(scAttr.getAttributeName(), scAttr.getAttributeValue().replaceAll("%", ""));
						}
					});

					if (cosMap.size() > 1) {
						ipServiceAttributes.setIsMultiCosRequired(true);
					}
					
					ScServiceAttribute multiCastServiceAttr = serviceAttributeRepository
							.findFirstByScServiceDetail_idAndAttributeNameAndCategory(task.getServiceId(), "isMultiCastExists",
									"GVPN Common");
					if(Objects.nonNull(multiCastServiceAttr) && Objects.nonNull(multiCastServiceAttr.getAttributeValue()) && !multiCastServiceAttr.getAttributeValue().isEmpty() && "Yes".equalsIgnoreCase(multiCastServiceAttr.getAttributeValue())){
						logger.info("MutliCast exists ");
						ipServiceAttributes.setIsMultiCastIPRequired(true);
					}else{
						logger.info("MutliCast doesn't exist exists ");
						ipServiceAttributes.setIsMultiCastIPRequired(false);
					}
				}
				

			initiateCLRCreation.setCOPFID(String.valueOf((Integer)taskDataMap.get(CramerConstants.ORDER_ID)));
			initiateCLRCreation.setScenarioType(CramerConstants.CLR_CREATION);
			initiateCLRCreation.getListOfFeasibilityId().add(StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.FEASIBILITY_ID)));
			initiateCLRCreation.setRequestID(cramerInfoBean.getProcessInstanceId());

			OrderDetails orderDetails = new OrderDetails();
			orderDetails.setBANDWIDTHVALUE(StringUtils.trimToEmpty((String)taskDataMap.get("localLoopBandwidth")).replaceAll(CramerConstants.mbps, CramerConstants.EMPTY));
			orderDetails.setBANDWIDTHUNIT(CramerConstants.Mbps);
			orderDetails.setCUSTOMERNAME(StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.CUSTOMER_NAME)));
			orderDetails.setCOVERAGE(CramerConstants.CUSTOMER_ACCESS);

			if(orderType.equals(CramerConstants.TYPE_MACD)){
				if((CramerConstants.ADD_SITE_SERVICE.equals(orderCategory)) || (Objects.nonNull(orderSubCategory) && orderSubCategory.toLowerCase().contains("parallel"))){
					logger.info("CREATE CRAMER:ADD SITE");
					orderDetails.setRequestType(CramerConstants.TYPE_NEW);
					orderDetails.setOrderType(CramerConstants.TYPE_NEW);
					/*ScServiceAttribute scServiceDownTimeAttr = scServiceAttributeRepository
							.findFirstByScServiceDetail_idAndAttributeName(task.getServiceId(), "downtime_duration");
					if(Objects.nonNull(orderSubCategory) && orderSubCategory.toLowerCase().contains("parallel") && Objects.nonNull(scServiceDownTimeAttr) 
							&& (Objects.isNull(scServiceDownTimeAttr.getAttributeValue()) || scServiceDownTimeAttr.getAttributeValue().isEmpty()
									|| (Objects.nonNull(scServiceDownTimeAttr.getAttributeValue())
											&& !scServiceDownTimeAttr.getAttributeValue().isEmpty() && Integer.valueOf(scServiceDownTimeAttr.getAttributeValue())==0))){
						logger.info("CREATE CRAMER: PARALLEL DAYS 0");
						orderDetails.setRequestType(CramerConstants.CHANGE);
						orderDetails.setOrderType(orderSubCategory.toUpperCase());
						ipServiceAttributes.setOldServiceId(serviceDetail.getOldServiceId());
					}*/

				}else if(Objects.nonNull(orderSubCategory) && (orderSubCategory.toLowerCase().contains("bso") && (lmType.toLowerCase().contains("onnet wireline") || lmType.toLowerCase().contains("onnetwl")))){
					logger.info("CREATE CRAMER:BSO Change");
					orderDetails.setRequestType(CramerConstants.CHANGE);
					orderDetails.setOrderType("BSO CHANGE");
				}else if(Objects.nonNull(orderSubCategory) && (orderSubCategory.toLowerCase().contains("lm") || orderSubCategory.equalsIgnoreCase("Shifting") || orderSubCategory.toLowerCase().contains("bso"))){
					logger.info("CREATE CRAMER:LM SHIFTING");
					orderDetails.setRequestType(CramerConstants.CHANGE);
					orderDetails.setOrderType("LM SHIFTING");
				}else{
					logger.info("CREATE CRAMER:CHANGE");
					orderDetails.setRequestType(CramerConstants.CHANGE);
					formOrderType(orderCategory,orderDetails,task,orderSubCategory);
				}
				
				if(task.getOrderCode().toLowerCase().contains("izosdwan") && "CHANGE_ORDER".equalsIgnoreCase(orderCategory)){
					logger.info("IZOSDWAN Order::{} for Service Id::{}",task.getOrderCode(),task.getServiceId());
					orderDetails.setRequestType(CramerConstants.CHANGE);
					orderDetails.setOrderType("HOT UPGRADE");
				}
				
				/*else if(Objects.nonNull(orderSubCategory) && orderSubCategory.toLowerCase().contains("parallel")){
					logger.info("CREATE CRAMER:PARALLEL");
					orderDetails.setRequestType(CramerConstants.CHANGE);
					orderDetails.setOrderType(orderSubCategory.toUpperCase());
					ipServiceAttributes.setOldServiceId(serviceDetail.getOldServiceId());
				}*/
			}else if(orderType.equals(CramerConstants.TYPE_NEW)){
				orderDetails.setRequestType(orderType);
				orderDetails.setOrderType(CramerConstants.TYPE_NEW);
			}
			orderDetails.setPRERESERVED(true);
			initiateCLRCreation.setOrderDetails(orderDetails);

			String interfaceType =  (String)taskDataMap.get(CramerConstants.INTERFACE);
			EndDetails aEndDetails = new EndDetails();
			EndDetails zEndDetails = new EndDetails();

			interfaceType = getInterface(interfaceType);
					
			
			ipServiceAttributes.setLastMileInterface(interfaceType);
			aEndDetails.setINTERFACE(interfaceType);
			zEndDetails.setINTERFACE(interfaceType);


			String lastmileProvider = StringUtils.trimToEmpty((String)taskDataMap.get("lmType"));
			Map<String, String> componentMap = new HashMap<>();

			ScComponentAttribute scComponentAttributebtsIp = scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(task.getServiceId(), "btsIp",  AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
			ScServiceAttribute scServiceAttributebtsIp = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(task.getServiceId(),"bts_IP_PMP");
			if((Objects.isNull(scComponentAttributebtsIp) ||
					(Objects.isNull(scComponentAttributebtsIp) && StringUtils.isEmpty(scComponentAttributebtsIp.getAttributeValue())))
							&& (Objects.nonNull(scServiceAttributebtsIp)))
			{
				componentMap.put("btsIp", scServiceAttributebtsIp.getAttributeValue());
				logger.info("btsIp updated as {}", scServiceAttributebtsIp.getAttributeValue());
			}

			ScComponentAttribute scComponentAttributesectorId = scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(task.getServiceId(), "sectorId",  AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
			 ScServiceAttribute scServiceAttributesectorId = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(task.getServiceId(),"sector_id");
			if((Objects.isNull(scComponentAttributesectorId) ||
					(Objects.isNull(scComponentAttributesectorId) && StringUtils.isEmpty(scComponentAttributesectorId.getAttributeValue())))
							&& (Objects.nonNull(scServiceAttributesectorId)))
			{
				componentMap.put("sectorId", scServiceAttributesectorId.getAttributeValue());
				logger.info("sectorId updated as {}", scServiceAttributesectorId.getAttributeValue());
			}

			componentAndAttributeService.updateAttributes(task.getServiceId(),componentMap, AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);

			if(lmType.equalsIgnoreCase("OnnetRF") || lmType.equalsIgnoreCase("onnet wireless")) {
				String rfTechnology = StringUtils.trimToEmpty((String)taskDataMap.get("rfTechnology")).toUpperCase(); //COMPONENT_ATTRIBUTES
				String wirelessScenario = null;
				String lastMileProvider = null;
				if(Objects.nonNull(taskDataMap.get("wirelessScenario"))){
					logger.info("wirelessScenario");
					wirelessScenario=StringUtils.trimToEmpty((String)taskDataMap.get("wirelessScenario")).toLowerCase();
				}else if(Objects.nonNull(taskDataMap.get("offnetProviderName"))){
					logger.info("offnetProviderName");
					wirelessScenario=StringUtils.trimToEmpty((String)taskDataMap.get("offnetProviderName")).toLowerCase();
				}
				
				if(Objects.nonNull(taskDataMap.get("lastMileProvider"))){
					logger.info("lastMileProvider");
					lastMileProvider=StringUtils.trimToEmpty((String)taskDataMap.get("lastMileProvider")).toLowerCase();
				}
				
				//SERVICE_ATTRIBUTES
				ipServiceAttributes.setNetworkCompType("WIRELESS");
				aEndDetails.setONNET(true);
				zEndDetails.setONNET(true);
				if((Objects.nonNull(wirelessScenario) && !wirelessScenario.isEmpty() && wirelessScenario.toLowerCase().contains("pmp"))
						|| (Objects.nonNull(lastMileProvider) && !lastMileProvider.isEmpty() && lastMileProvider.toLowerCase().contains("pmp"))){
					logger.info("UBR");
					ipServiceAttributes.setProvider("TCL UBR PMP");
				}else{
					logger.info("Other than UBR");
					ipServiceAttributes.setProvider(rfTechnology);
				}
				if(orderType.equals(CramerConstants.TYPE_MACD) && !CramerConstants.ADD_SITE_SERVICE.equals(orderCategory)
						&& ( Objects.isNull(orderSubCategory ) || (Objects.nonNull(orderSubCategory) 
								&& !orderSubCategory.toLowerCase().contains("parallel")))){
					logger.info("OnnetRF MACD ORDER");
					if(Objects.nonNull(wirelessScenario) && (wirelessScenario.equalsIgnoreCase("Radwin from TCL POP") || !wirelessScenario.toLowerCase().contains("pmp"))){
						logger.info("OnnetRF MACD ORDER P2P");
						MstP2PDetails mstP2PDetails=mstP2PDetailsRepository.findFirstByServiceCodeAndIsActiveOrderByIdDesc(task.getServiceCode(), "Y");
						if(Objects.nonNull(mstP2PDetails)){
							logger.info("MASTER P2P Detail exists");
							
							attributesMap.put("ProviderSupplier", "RADWIN");
							
							if(Objects.nonNull(mstP2PDetails.getBsConverterIp()) && !mstP2PDetails.getBsConverterIp().isEmpty()){
								logger.info("bsConverterIp from p2p");
								attributesMap.put(CramerConstants.CONVERTER_IP, mstP2PDetails.getBsConverterIp());
								attributesMap.put("Converter Required", "yes");
							}else if(Objects.nonNull(taskDataMap.get("btsConverterIp")) && !taskDataMap.get("btsConverterIp").toString().isEmpty()){
								logger.info("bsConverterIp from SF");
								String btsConverterIp = StringUtils.trimToEmpty((String)taskDataMap.get("btsConverterIp")); 
								attributesMap.put(CramerConstants.CONVERTER_IP, btsConverterIp);
							}
							
							if(Objects.nonNull(taskDataMap.get("btsConverterRequired")) && !taskDataMap.get("btsConverterRequired").toString().isEmpty()){
								logger.info("btsConverterRequired from SF");
								String btsConverterRequired = StringUtils.trimToEmpty((String)taskDataMap.get("btsConverterRequired")); 
								attributesMap.put("Converter Required", btsConverterRequired);
							}
							
							if(Objects.nonNull(mstP2PDetails.getBhBso()) && !mstP2PDetails.getBhBso().isEmpty()){
								logger.info("BackhaulProvider from p2p");
								attributesMap.put("BackhaulProvider", mstP2PDetails.getBhBso());
							}else if(Objects.nonNull(taskDataMap.get("bhProviderName")) && !taskDataMap.get("bhProviderName").toString().isEmpty()) {
								logger.info("BackhaulProvider from SF");
								String providerName = StringUtils.trimToEmpty((String)taskDataMap.get("bhProviderName"));
								attributesMap.put("BackhaulProvider", providerName);
							}
							
							if(Objects.nonNull(mstP2PDetails.getBsSwitchIp()) && !mstP2PDetails.getBsSwitchIp().isEmpty()){
								logger.info("bsSwitchIp from p2p");
								attributesMap.put("Bussiness Switch IP", mstP2PDetails.getBsSwitchIp());
							}else if(Objects.nonNull(taskDataMap.get("bsSwitchIp")) && !taskDataMap.get("bsSwitchIp").toString().isEmpty()) {
								logger.info("bsSwitchIp from SF");
								String bsSwitchIp = StringUtils.trimToEmpty((String)taskDataMap.get("bsSwitchIp"));
								attributesMap.put("Bussiness Switch IP", bsSwitchIp);
							}
							
							if(Objects.nonNull(mstP2PDetails.getPeHostname()) && !mstP2PDetails.getPeHostname().isEmpty()){
								logger.info("bsSwitchHostName from p2p");
								attributesMap.put("Bussiness Switch HostName", mstP2PDetails.getPeHostname());
							}else if(Objects.nonNull(taskDataMap.get("bsSwitchHostName")) && !taskDataMap.get("bsSwitchHostName").toString().isEmpty()) {
								logger.info("bsSwitchHostName from SF");
								String bsSwitchHostName = StringUtils.trimToEmpty((String)taskDataMap.get("bsSwitchHostName"));
								attributesMap.put("Bussiness Switch HostName", bsSwitchHostName);
							}
							
							if(Objects.nonNull(mstP2PDetails.getIp()) && !mstP2PDetails.getIp().isEmpty()){
								logger.info("btsIp from p2p");
								ipServiceAttributes.setBTSIP(mstP2PDetails.getIp());
							}else if(Objects.nonNull(taskDataMap.get("btsIp")) && !taskDataMap.get("btsIp").toString().isEmpty()) {
								logger.info("btsIp from SF");
								String btsIp = StringUtils.trimToEmpty((String)taskDataMap.get("btsIp"));
								ipServiceAttributes.setBTSIP(btsIp);
							}
							
						}else{
							getP2PDetails(taskDataMap,attributesMap,ipServiceAttributes);
							if(Objects.nonNull(taskDataMap.get("btsIp")) && !taskDataMap.get("btsIp").toString().isEmpty()) {
								logger.info("btsIp from SF");
								String btsIp = StringUtils.trimToEmpty((String)taskDataMap.get("btsIp"));
								ipServiceAttributes.setBTSIP(btsIp);
							}
						}
				    }else{
				    	logger.info("OnnetRF MACD ORDER NOT P2P");
				    	String btsIp = StringUtils.trimToEmpty((String)taskDataMap.get("btsIp"));
						String sectorId = StringUtils.trimToEmpty((String)taskDataMap.get("sectorId"));
						if(Objects.nonNull(btsIp) && Objects.nonNull(sectorId) && !btsIp.isEmpty() && !sectorId.isEmpty()){
							logger.info("Sc component attr exists");
							ipServiceAttributes.setBTSIP(btsIp);
					    	ipServiceAttributes.setSectorID(sectorId);
						}else{
							
							MstRadwinDetails mstRadwinDetails=mstRadwinDetailsRepository.findFirstByServiceCodeAndIsActiveOrderByIdDesc(task.getServiceCode(),"Y");
							if(Objects.nonNull(mstRadwinDetails)){
								logger.info("RADWIN");
								ipServiceAttributes.setBTSIP(mstRadwinDetails.getBtsIp());
								ipServiceAttributes.setSectorID(mstRadwinDetails.getSectorId());
							}
							
							MstCambiumDetails mstCambiumDetails=mstCambiumDetailsRepository.findFirstByServiceCodeAndIsActiveOrderByIdDesc(task.getServiceCode(),"Y");
							if(Objects.nonNull(mstCambiumDetails)){
								logger.info("CAMBIUM");
								ipServiceAttributes.setBTSIP(mstCambiumDetails.getBtsIp());
								ipServiceAttributes.setSectorID("1");
							}
							
							/*if("RADWIN".equalsIgnoreCase(rfTechnology)) {
								logger.info("RADWIN");
								MstRadwinDetails mstRadwinDetails=mstRadwinDetailsRepository.findFirstByServiceCodeAndIsActiveOrderByIdDesc(task.getServiceCode(),"Y");
								if(Objects.nonNull(mstRadwinDetails)){
									logger.info("RADWIN");
									ipServiceAttributes.setBTSIP(mstRadwinDetails.getBtsIp());
									ipServiceAttributes.setSectorID(mstRadwinDetails.getBtsName());
								}
							}//else if("CAMBIUM".equalsIgnoreCase(rfTechnology)) {
					    	else{
								logger.info("CAMBIUM");
								MstCambiumDetails mstCambiumDetails=mstCambiumDetailsRepository.findFirstByServiceCodeAndIsActiveOrderByIdDesc(task.getServiceCode(),"Y");
								if(Objects.nonNull(mstCambiumDetails)){
									logger.info("CAMBIUM");
									ipServiceAttributes.setBTSIP(mstCambiumDetails.getBtsIp());
									ipServiceAttributes.setSectorID("1");
								}
							}*/
						}
				    	
				    }
					
				}else{
					logger.info("OnnetRF NEW ORDER OR ADD SITE");
					String btsIp = StringUtils.trimToEmpty((String)taskDataMap.get("btsIp")); //COMPONENT_ATTRIBUTES
					String sectorId = StringUtils.trimToEmpty((String)taskDataMap.get("sectorId")); //COMPONENT_ATTRIBUTES
				    if(Objects.nonNull(wirelessScenario) && (wirelessScenario.equalsIgnoreCase("Radwin from TCL POP") || !wirelessScenario.toLowerCase().contains("pmp"))) {
				    	logger.info("P2P");
				    	getP2PDetails(taskDataMap,attributesMap,ipServiceAttributes);
				    }else {
				    	logger.info("PMP");
				    	if(StringUtils.isNotBlank(btsIp))ipServiceAttributes.setBTSIP(btsIp);
				    	if(StringUtils.isNotBlank(sectorId))ipServiceAttributes.setSectorID(sectorId);
				    }
				}
            }else  if(lmType.equalsIgnoreCase("OnnetWL") || lmType.equalsIgnoreCase("Onnet Wireline")) {
            	aEndDetails.setONNET(true);
				zEndDetails.setONNET(true);
				String endMuxNodeName = StringUtils.trimToEmpty((String)taskDataMap.get("endMuxNodeName"));
				zEndDetails.setNODEW(endMuxNodeName);
            	ipServiceAttributes.setNetworkCompType(CramerConstants.ONNET_WIRELINE);
            	ipServiceAttributes.setProvider(CramerConstants.MAN);
            }else if(lmType.equalsIgnoreCase("OffnetRF") || lmType.equalsIgnoreCase("offnet wireless")) {
            	aEndDetails.setONNET(false);
				zEndDetails.setONNET(false);
				String offnetProvider = StringUtils.trimToEmpty((String)taskDataMap.get("offnetProviderName"));
				String nniId = StringUtils.trimToEmpty((String)taskDataMap.get("nniId"));
				String customerInnerVlan = StringUtils.trimToEmpty((String)taskDataMap.get("customerInnerVlan"));
				//zEndDetails.setNODEW(nniId);

				WrkrOffnetIfaceDtls aOffnet = new WrkrOffnetIfaceDtls();
				WrkrOffnetIfaceDtls zOffnet = new WrkrOffnetIfaceDtls();
				aOffnet.setOFFNETPROVIDER(offnetProvider);
				zOffnet.setOFFNETPROVIDER(offnetProvider);
				initiateCLRCreation.setAWrkrOffnetIfaceDtls(aOffnet);
				initiateCLRCreation.setZWrkrOffnetIfaceDtls(zOffnet);
				String cloudName = StringUtils.trimToEmpty((String)taskDataMap.get("cloudName"));
				if(StringUtils.isNotBlank(cloudName)) {
					zEndDetails.setNODEW(cloudName);
					zEndDetails.setINTERFACE("Optical");
					ipServiceAttributes.setNetworkCompType("OFFNET WIRELINE");
            	}else {
					ipServiceAttributes.setNNIID(nniId);
					if(StringUtils.isNotBlank(customerInnerVlan))ipServiceAttributes.setVLANLM(customerInnerVlan);
					ipServiceAttributes.setVLANLM(customerInnerVlan);
					ipServiceAttributes.setNetworkCompType("OFFNET WIRELESS");
            	}
				ipServiceAttributes.setProvider(offnetProvider);
				
            }else if(lmType.equalsIgnoreCase("OffnetWL") || lmType.equalsIgnoreCase("offnet Wireline")) {
            	aEndDetails.setONNET(false);
				zEndDetails.setONNET(false);
				String offnetProvider = StringUtils.trimToEmpty((String)taskDataMap.get("offnetProviderName"));
				//String providerReferenceId = StringUtils.trimToEmpty((String)taskDataMap.get("providerReferenceId"));
				//String providerNodeName = StringUtils.trimToEmpty((String)taskDataMap.get("providerNodeName"));
				//handoverType
				
				String cloudName = StringUtils.trimToEmpty((String)taskDataMap.get("cloudName"));
				zEndDetails.setNODEW(cloudName);
				zEndDetails.setINTERFACE("Optical");
			
				//if(StringUtils.isBlank(providerNodeName))providerNodeName=providerReferenceId;
				//zEndDetails.setNODEW(providerNodeName);
				//zEndDetails.setINTERFACE("Optical");
				WrkrOffnetIfaceDtls aOffnet = new WrkrOffnetIfaceDtls();
				WrkrOffnetIfaceDtls zOffnet = new WrkrOffnetIfaceDtls();
				aOffnet.setOFFNETPROVIDER(offnetProvider);
				zOffnet.setOFFNETPROVIDER(offnetProvider);
				initiateCLRCreation.setAWrkrOffnetIfaceDtls(aOffnet);
				initiateCLRCreation.setZWrkrOffnetIfaceDtls(zOffnet);
				ipServiceAttributes.setProvider(offnetProvider);
				ipServiceAttributes.setNetworkCompType("OFFNET WIRELINE");
            }
			
			if((lmType.equalsIgnoreCase("OnnetRF") || lmType.equalsIgnoreCase("onnet wireless")
					|| lmType.equalsIgnoreCase("OnnetWL") || lmType.equalsIgnoreCase("Onnet Wireline")) && "ECO INTERNET".equalsIgnoreCase(serviceVariant)) {
				logger.info("Create Clr for Econet service id::{}",task.getServiceId());
				ipServiceAttributes.setProvider("ECO INTERNET");
			}


			aEndDetails.setISMODIFIED(false);
			aEndDetails.setNODEW(CramerConstants.EMPTY);
			aEndDetails.setNODEIDW(0L);
			initiateCLRCreation.setAEndDetails(aEndDetails);

			zEndDetails.setISMODIFIED(false);
			zEndDetails.setNODEIDW(0L);
			initiateCLRCreation.setZEndDetails(zEndDetails);

			UccServDetails uccServDetails = new UccServDetails();
			// uccServDetails.s
			UccService uccService = new UccService();
			uccService.getAttribute().add(new Attributes());
			List<UccService> uccServiceList = new ArrayList<>();
			uccServiceList.add(uccService);
			initiateCLRCreation.setUCCServDtls(uccServDetails);

			UaApplicable uaApplicable = new UaApplicable();
			initiateCLRCreation.setUAApplicable(uaApplicable);

			ClrDesignDtls clrDesignDtls = new ClrDesignDtls();
			IldDtls ildDtls = new IldDtls();
			ildDtls.setSTATICPROTECTION("");
			clrDesignDtls.setILDDtls(ildDtls);
			clrDesignDtls.setMAXHOP(8);


			String primarySeconday=StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.PRIMARY_SECONDARY));
			
			String link=(String) taskDataMap.getOrDefault("prisecLink",null);
			
			if(primarySeconday.equalsIgnoreCase(CramerConstants.PRIMARY) && link==null) {
				clrDesignDtls.setPRIMARYSECONDARY(false);
				ipServiceAttributes.setPriSecMapping(CramerConstants.SINGLE);
			}
			else if(primarySeconday.equalsIgnoreCase(CramerConstants.SECONDARY)) {
				clrDesignDtls.setPRIMARYSECONDARY(true);
				ipServiceAttributes.setPriSecMapping("secondary");
				ipServiceAttributes.setServiceLink(link);

			}
			else if(primarySeconday.equalsIgnoreCase(CramerConstants.PRIMARY)) {
				clrDesignDtls.setPRIMARYSECONDARY(true);
				ipServiceAttributes.setPriSecMapping("primary");
				ipServiceAttributes.setServiceLink(link);
			}

		

			clrDesignDtls.setUSEHANGINGNTWKCIRCUITS(false);

			NldDtls nldDtls = new NldDtls();
			nldDtls.setALLOWEDTIER1HOPS(3);
			nldDtls.setALLOWTTSLNODES(false);

			// TODO: set other segment
			nldDtls.setOTHERSEGMENT(CramerConstants.EMPTY);

			clrDesignDtls.setNLDDtls(nldDtls);
			initiateCLRCreation.setClrDesignDtls(clrDesignDtls);

			ipServiceAttributes.setCUID(StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.CUID)));
			Boolean isExtendedLanRequired = StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.IS_EXTENDED_LAN_REQUIRED)).equalsIgnoreCase(CramerConstants.YES);
			
				if (orderType.equalsIgnoreCase(CramerConstants.TYPE_MACD) && !"ADD_SITE".equalsIgnoreCase(orderCategory)
						&& (Objects.isNull(orderSubCategory) || (Objects.nonNull(orderSubCategory) && !orderSubCategory.toLowerCase().contains("parallel")))) {
					

					Map<String, String> prevScComponentAttributesAMap =	commonFulfillmentUtils.getComponentAttributes(
							previousActiveServiceDetails.getId(), AttributeConstants.COMPONENT_LM, "A");
					
					boolean isPreviousisExtendedLanRequired =false;
					if(prevScComponentAttributesAMap.containsKey("extendedLanRequired") && Objects.nonNull(prevScComponentAttributesAMap.get("extendedLanRequired"))
							&& !prevScComponentAttributesAMap.get("extendedLanRequired").isEmpty()){
						isPreviousisExtendedLanRequired = prevScComponentAttributesAMap.get("extendedLanRequired")
								.equalsIgnoreCase(CramerConstants.YES);
					}

					if (isPreviousisExtendedLanRequired != isExtendedLanRequired) {
						ipServiceAttributes.setIsExtendedLanChanged(true);

					}
					else {
						ipServiceAttributes.setIsExtendedLanChanged(false);

					}
					
					/*ScServiceAttribute scServiceDownTimeAttr = scServiceAttributeRepository
							.findFirstByScServiceDetail_idAndAttributeName(task.getServiceId(), "downtime_duration");
					if(Objects.nonNull(orderSubCategory) && orderSubCategory.toLowerCase().contains("parallel") && Objects.nonNull(scServiceDownTimeAttr) 
							&& (Objects.isNull(scServiceDownTimeAttr.getAttributeValue()) || scServiceDownTimeAttr.getAttributeValue().isEmpty()
									|| (Objects.nonNull(scServiceDownTimeAttr.getAttributeValue())
											&& !scServiceDownTimeAttr.getAttributeValue().isEmpty() && Integer.valueOf(scServiceDownTimeAttr.getAttributeValue())==0))){
						logger.info("CREATE CRAMER: LMType Changed");
						ipServiceAttributes.setIsLMTypeChanged(true);
					}else */if(orderType.equalsIgnoreCase(CramerConstants.TYPE_MACD) && !"ADD_SITE".equalsIgnoreCase(orderCategory)
							&& Objects.nonNull(orderSubCategory) && (orderSubCategory.toLowerCase().contains("lm")
									|| orderSubCategory.toLowerCase().contains("bso")
									|| orderSubCategory.equalsIgnoreCase("Shifting"))){
						logger.info("CREATE CRAMER: LMType Changed for BSO");
						ipServiceAttributes.setIsLMTypeChanged(true);
					}else{
						logger.info("CREATE CRAMER: LMType validation for other scenarios");
						if(prevScComponentAttributesAMap.containsKey("lmType") && Objects.nonNull(prevScComponentAttributesAMap.get("lmType")) && 
								!prevScComponentAttributesAMap.get("lmType").equalsIgnoreCase(lastmileProvider)) {
							logger.info("CREATE CRAMER: LMType Changed");
							ipServiceAttributes.setIsLMTypeChanged(true);
						}
						else {
							logger.info("CREATE CRAMER: LMType not changed");
							ipServiceAttributes.setIsLMTypeChanged(false);
						}
					}
					
					
					if(!task.getOrderCode().toLowerCase().contains("izosdwan")){
						logger.info("Other than izosdwan");
						if (prevScComponentAttributesAMap.containsKey("cpeManagementType") && Objects.nonNull(prevScComponentAttributesAMap.get("cpeManagementType")) &&  
								prevScComponentAttributesAMap.get("cpeManagementType")
								.equalsIgnoreCase((String) taskDataMap.get(CramerConstants.CPE_MANAGEMENT_TYPE))) {
							ipServiceAttributes.setIsServiceOptionChanged(true);

						} else {
							ipServiceAttributes.setIsServiceOptionChanged(false);

						}
					}
					
					
					if(task.getOrderCode().toLowerCase().contains("izosdwan")){
						logger.info("izosdwan ordercode::{} with service id::{} for Service Option",task.getOrderCode(),task.getServiceId());
						ScComponentAttribute scComponentAttributeCpeManagedAlready = scComponentAttributesRepository.
								findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(task.getServiceId(), 
										"isCPEManagedAlready",  AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
						if(scComponentAttributeCpeManagedAlready!=null && scComponentAttributeCpeManagedAlready.getAttributeValue()!=null 
								&& !scComponentAttributeCpeManagedAlready.getAttributeValue().isEmpty() 
								&& "false".equalsIgnoreCase(scComponentAttributeCpeManagedAlready.getAttributeValue())){
							logger.info("izosdwan service option");
							ipServiceAttributes.setIsServiceOptionChanged(true);
						}
					}
					
				}else if (orderType.equalsIgnoreCase(CramerConstants.TYPE_NEW)) {
					ipServiceAttributes.setIsExtendedLanChanged(false);
				}
			ipServiceAttributes.setIsExtendedLAN(isExtendedLanRequired);
			ipServiceAttributes.setRoutingProtocol(StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.ROUTING_PROTOCOL)));
			ipServiceAttributes.setIsBFDEnabled(StringUtils.trimToEmpty((String)taskDataMap.get("bfdRequired")).equalsIgnoreCase(CramerConstants.YES));

			ipServiceAttributes.setBaseRate(StringUtils.trimToEmpty((String)taskDataMap.get("localLoopBandwidth")));
			ipServiceAttributes.setBaseRateUnit(StringUtils.trimToEmpty(CramerConstants.Mbps));

			String burstableBandwidth = StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.Burstable_Bandwidth));
			if(StringUtils.isNotBlank(burstableBandwidth)) {
				ipServiceAttributes.setBurstRate(burstableBandwidth);
				ipServiceAttributes.setBurstRateUnit(StringUtils.trimToEmpty(CramerConstants.Mbps));
			}

			

			ipServiceAttributes.setMultiVRFFlag(false);
			ipServiceAttributes.setMultiVRFSolution(false);
			ipServiceAttributes.setMultilink(CramerConstants.EMPTY);

			ipServiceAttributes.setNoOfVRFs("0");
			ipServiceAttributes.setNumberOfIpAddresses(CramerConstants.EMPTY);			
			ipServiceAttributes.setPortBandwidth(StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.PORT_BANDWIDTH)).replaceAll(CramerConstants.Mbps, CramerConstants.EMPTY));
			ipServiceAttributes.setPortBandwidthUnit(CramerConstants.Mbps);


			if (productName.equalsIgnoreCase(CramerConstants.IAS) ||productName.equalsIgnoreCase(CramerConstants.ILL)) {
				if ("standard".equalsIgnoreCase(serviceVariant)) {
					ipServiceAttributes.setServiceCategory(CramerConstants.STDILL);
				}else if ("ECO INTERNET".equalsIgnoreCase(serviceVariant)) {
					ipServiceAttributes.setServiceCategory(CramerConstants.ECOINTERNET);
				}else {
					ipServiceAttributes.setServiceCategory(CramerConstants.PILL);
				}
			} else if (productName.equalsIgnoreCase(CramerConstants.GVPN)) {
				ipServiceAttributes.setServiceCategory("VPN PORT");
			}

			String cpeManagementType = (String)taskDataMap.get(CramerConstants.CPE_MANAGEMENT_TYPE);
			if(cpeManagementType.equalsIgnoreCase(CramerConstants.FULLY_MANAGED)
					|| cpeManagementType.equalsIgnoreCase(CramerConstants.PHYSICALLY_MANAGED)
					|| cpeManagementType.equalsIgnoreCase(CramerConstants.PROACTIVE_MONITORED)
					|| cpeManagementType.equalsIgnoreCase(CramerConstants.PROACTIVE_MONITORING)
					|| cpeManagementType.toLowerCase().contains("config")
					|| cpeManagementType.equalsIgnoreCase(CramerConstants.PROACTIVE_SERVICES)) {
				ipServiceAttributes.setServiceOption(CramerConstants.MANAGED);
			}else {
				ipServiceAttributes.setServiceOption(CramerConstants.UNMANAGED);
			}
			
			ipServiceAttributes.setIsServiceOptionChanged(false);
			ipServiceAttributes.setSharedCPERequired(false);
			ipServiceAttributes.setSharedLMRequired(false);
			
			if (task.getOrderCode().toLowerCase().contains("izosdwan") && 
					(orderType.equalsIgnoreCase(CramerConstants.TYPE_NEW)  
							|| (orderType.equalsIgnoreCase(CramerConstants.TYPE_MACD) 
							  && ("ADD_SITE".equalsIgnoreCase(orderCategory) || 
									(Objects.nonNull(orderSubCategory) && orderSubCategory.toLowerCase().contains("parallel")))))) {
				logger.info("izosdwan ordercode::{} with service id::{} for Shared CPE",task.getOrderCode(),task.getServiceId());
				ScComponentAttribute scComponentSharedCPEAttribute = scComponentAttributesRepository.
						findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(task.getServiceId(), 
								"Shared CPE",  AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
				if(scComponentSharedCPEAttribute!=null && scComponentSharedCPEAttribute.getAttributeValue()!=null 
						&& !scComponentSharedCPEAttribute.getAttributeValue().isEmpty() 
						&& "Y".equalsIgnoreCase(scComponentSharedCPEAttribute.getAttributeValue())){
					logger.info("izosdwan service option");
					ScSolutionComponent scSolutionComponent=scSolutionComponentRepository.findByScServiceDetail1_idAndOrderCodeAndIsActive(task.getServiceId(),task.getOrderCode(),"Y");
					if(scSolutionComponent!=null && scSolutionComponent.getCpeComponentId()!=null){
						logger.info("ScSolutionComponent exists with Id::{},CpeComponentId::{}",scSolutionComponent.getId(),scSolutionComponent.getCpeComponentId());
						List<String> internationalProductList= new ArrayList<>();
						internationalProductList.add("BYON Internet");
						internationalProductList.add("IZO Internet WAN");
						internationalProductList.add("DIA");
						internationalProductList.add("GVPN");
						List<String> sharedServiceCodeList=scSolutionComponentRepository.getServiceCodeBasedOnCpeComponentId(scSolutionComponent.getCpeComponentId(),task.getOrderCode(),task.getServiceId(),internationalProductList);
						if(sharedServiceCodeList!=null && !sharedServiceCodeList.isEmpty()){
							logger.info("Shared Service Code exists::{} and sent to Cramer::{}",sharedServiceCodeList.size(),sharedServiceCodeList.get(0));
							ipServiceAttributes.setSharedCPERequired(true);
							ipServiceAttributes.setSharedCPE(sharedServiceCodeList.get(0));
						}
					}
				}
			}
			//Slave payload for create clr in Multi VRF
			Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(serviceDetail.getScServiceDetailId());
			if (productName.equalsIgnoreCase(CramerConstants.GVPN) && optionalScServiceDetail.isPresent() && CommonConstants.Y.equalsIgnoreCase(optionalScServiceDetail.get().getMultiVrfSolution())) {
				logger.info("Inside Multi VRF create CLR block : {}", serviceDetail.getServiceId());
					ipServiceAttributes.setMultiVRFFlag(true);
					ipServiceAttributes.setMultiVRFSolution(true);
					if (optionalScServiceDetail.get().getIsMultiVrf()!= null && CommonConstants.N.equalsIgnoreCase(optionalScServiceDetail.get().getIsMultiVrf())) {
						logger.info("Inside Slave paylod for create clr payload with slave id : {}", optionalScServiceDetail.get().getUuid());
						Optional<ScServiceDetail> masterServiceDetail = scServiceDetailRepository.findById(optionalScServiceDetail.get().getMasterVrfServiceId());
						ipServiceAttributes.setMasterVRFServiceId(masterServiceDetail.get().getUuid());
						ipServiceAttributes.setSharedCPE(masterServiceDetail.get().getUuid());
						ipServiceAttributes.setSharedLM(masterServiceDetail.get().getUuid());
						ipServiceAttributes.setSharedCPERequired(true);
						ipServiceAttributes.setSharedLMRequired(true);
					}else {
						logger.info("Inside Master paylod for create clr payload with master id : {}", optionalScServiceDetail.get().getUuid());
						ipServiceAttributes.setSharedCPERequired(false);
						ipServiceAttributes.setSharedLMRequired(false);
						List<ScServiceAttribute> vrfAttributes=scServiceAttributeRepository.findByScServiceDetail_idAndAttributeNameIn(serviceDetail.getScServiceDetailId(),Arrays.asList("NUMBER_OF_VRFS","TOTAL_VRF_BANDWIDTH_MBPS"));
						logger.info("Vrf attributte size : {}",vrfAttributes.size());
						if(vrfAttributes!=null && !vrfAttributes.isEmpty()) {
							if(vrfAttributes.stream().filter(vrfAttribute->vrfAttribute.getAttributeName().equalsIgnoreCase("NUMBER_OF_VRFS") && vrfAttribute.getAttributeValue()!=null).findFirst().isPresent()) {
								ipServiceAttributes.setNoOfVRFs(vrfAttributes.stream().filter(vrfAttribute->vrfAttribute.getAttributeName().equalsIgnoreCase("NUMBER_OF_VRFS")).findFirst().get().getAttributeValue());
							}
							if(vrfAttributes.stream().filter(vrfAttribute->vrfAttribute.getAttributeName().equalsIgnoreCase("TOTAL_VRF_BANDWIDTH_MBPS") && vrfAttribute.getAttributeValue()!=null).findFirst().isPresent()) {
								ipServiceAttributes.setTotalVRFBandwidth(vrfAttributes.stream().filter(vrfAttribute->vrfAttribute.getAttributeName().equalsIgnoreCase("TOTAL_VRF_BANDWIDTH_MBPS")).findFirst().get().getAttributeValue());
							}
						}
					}
			}


			String asNumber = StringUtils.trimToEmpty((String)taskDataMap.get("asNumber"));
			String bgpAsNumber = StringUtils.trimToEmpty((String)taskDataMap.get("bgpAsNumber"));
			if(asNumber.equalsIgnoreCase("Customer public AS Number") && StringUtils.isNotBlank(bgpAsNumber))ipServiceAttributes.setASNumber(bgpAsNumber);

			ipServiceAttributes.setTclPopCity(StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.SOURCE_CITY)));
			ipServiceAttributes.setUniquePopId(StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.POP_SITE_CODE)));


			initiateCLRCreation.setIPServiceAttributes(ipServiceAttributes);

			AdditionalAttr additionalAttr = new AdditionalAttr();



			String additionalIPsRequired = StringUtils.trimToNull((String)taskDataMap.get("additionalIPsRequired"));
				if (null != additionalIPsRequired && (additionalIPsRequired.equalsIgnoreCase("yes")
						|| additionalIPsRequired.equalsIgnoreCase("y"))) {
					int totalAdditionalIps = 0;
					// check the ip type
					String additionalIPType = StringUtils
							.trimToEmpty((String) taskDataMap.get("additionalIPsArrangementType"));
					if (additionalIPType.equalsIgnoreCase("ipv4")) {
						String additionalIPv4IPs = StringUtils
								.trimToEmpty((String) taskDataMap.get("additionalIPv4IPs"));
						totalAdditionalIps = getTotalAdditionalIps(additionalIPv4IPs, 32);
						ipServiceAttributes.setNumberOfIpAddresses(String.valueOf(totalAdditionalIps));
					} else if (additionalIPType.equalsIgnoreCase("ipv6")) {
						String additionalIPv6IPs = StringUtils
								.trimToEmpty((String) taskDataMap.get("additionalIPv6IPs"));
						totalAdditionalIps = getTotalAdditionalIps(additionalIPv6IPs, 64);
						ipServiceAttributes.setAdditionalLanV6PoolSize(String.valueOf(totalAdditionalIps));
					} else {
						// for dual
						String additionalIPv4IPs = StringUtils
								.trimToEmpty((String) taskDataMap.get("additionalIPv4IPs"));
						String additionalIPv6IPs = StringUtils
								.trimToEmpty((String) taskDataMap.get("additionalIPv6IPs"));
						totalAdditionalIps = getTotalAdditionalIps(additionalIPv4IPs, 32);
						ipServiceAttributes.setNumberOfIpAddresses(String.valueOf(totalAdditionalIps));

						int ipAddrV6 = getTotalAdditionalIps(additionalIPv6IPs, 64);

						ipServiceAttributes.setAdditionalLanV6PoolSize(String.valueOf(ipAddrV6));

						totalAdditionalIps = totalAdditionalIps + ipAddrV6;
					}
					attributesMap.put(CramerConstants.IP_ADDRESS_TO_BE_PROVIDED, CramerConstants.YES);
					attributesMap.put(CramerConstants.NO_OF_IP_ADDRESS, String.valueOf(totalAdditionalIps));
				}
			
			 



			//TODO:
			//attributesMap.put(CramerConstants.IDC_BANDWIDTH, CramerConstants.NO);
			//attributesMap.put(CramerConstants.FALVOR_CHANGE, CramerConstants.NO);
			//attributesMap.put(CramerConstants.REQUESTING_SYSTEM, CramerConstants.OPTIMUS);
			attributesMap.put(CramerConstants.REQUESTING_SYSTEM, cramerSourceSystem);

			attributesMap.entrySet().stream().forEach(map -> {
				Attributes attr = new Attributes();
				attr.setKey(map.getKey());
				attr.setValue(map.getValue());
				additionalAttr.getAttributes().add(attr);
			});

			if (Objects.nonNull(attributesMap)
					&& !attributesMap.isEmpty()) {
				initiateCLRCreation.setAdditionalAttributes(additionalAttr);
			}

			// Save request & response
			networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
			networkInventory.setServiceCode(task.getServiceCode());
			networkInventory.setRequestId(cramerInfoBean.getProcessInstanceId());
			networkInventory.setRequest(Utils.convertObjectToXmlString(initiateCLRCreation, InitiateCLRCreation.class));
			networkInventory.setType(CramerConstants.CREATE_CLR);

			SoapRequest soapRequest = new SoapRequest();
			soapRequest.setUrl(createClrUrl);
			soapRequest.setRequestObject(initiateCLRCreation);
			soapRequest.setContextPath("com.tcl.dias.serviceactivation.cramer.servicedesign.beans");
			try {
				JAXBElement jaxBResponse = genericWebserviceClient.doSoapCallForObject(soapRequest, JAXBElement.class);
				InitiateCLRCreationResponse initiateCLRCreationResponse = (InitiateCLRCreationResponse)jaxBResponse.getValue();
				if (initiateCLRCreationResponse != null && initiateCLRCreationResponse.getClrCreationResult() != null) {

					try {
						networkInventory.setResponse(Utils.convertObjectToXmlString(initiateCLRCreationResponse,InitiateCLRCreationResponse.class));
						networkInventoryRepository.save(networkInventory);
					} catch (Exception ex) {
						logger.error("Exception in Cramer Create CLR  ", ex);
					}

					Acknowledgement acknowledgement = initiateCLRCreationResponse.getClrCreationResult().getAck();
					if (acknowledgement != null) {
						logger.info("createCLR acknowledgement RequestID=> {}, ServiceID=> {} Status={}",
								acknowledgement.getRequestID(), acknowledgement.getServiceID(),acknowledgement.isStatus());
                        response.setStatus(acknowledgement.isStatus());
					}

				}
			} catch (SoapFaultClientException ex) {
				if (Objects.nonNull(ex.getSoapFault().getFaultDetail())) {
					logger.error("SoapFaultClientException in Cramer createCLR ", ex);
					SoapFaultDetail soapFaultDetail = ex.getSoapFault().getFaultDetail();
					if (soapFaultDetail != null) {
						SoapFaultDetailElement detailElementChild = soapFaultDetail.getDetailEntries().next();
						Source detailSource = detailElementChild.getSource();
						Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
						marshaller.setContextPath(soapRequest.getContextPath());
						@SuppressWarnings("unchecked")
						JAXBElement<com.tcl.dias.serviceactivation.cramer.servicedesign.beans.ServiceFault> source = (JAXBElement<com.tcl.dias.serviceactivation.cramer.servicedesign.beans.ServiceFault>) marshaller
								.unmarshal(detailSource);
						logger.error("ErrorCode:: {} , ErrorLongDescription:: {},ErrorShortDescription:: {}",
								source.getValue().getErrorCode(), source.getValue().getErrorLongDescription(),
								source.getValue().getErrorShortDescription());
						/*if(source.getValue().getErrorCode().equalsIgnoreCase("CBIAS100093")){
							response.setStatus(true);
							response.setData("skipExistClr");
						}else if(source.getValue().getErrorCode().equalsIgnoreCase("CBIAS100010")
								|| source.getValue().getErrorCode().equalsIgnoreCase("CBIAS100020")
								|| source.getValue().getErrorCode().equalsIgnoreCase("CBIAS100030")) {
							response.setStatus(true);
						} else {*/
							response.setStatus(false);
							response.setErrorCode(source.getValue().getErrorCode());
							response.setErrorMessage(source.getValue().getErrorLongDescription());
						//}
						networkInventory.setResponse(source.getValue().getErrorCode() +":"+ source.getValue().getErrorLongDescription());
						networkInventoryRepository.save(networkInventory);
					}

				} else {
					logger.error("SoapFaultClientException in Cramer createCLR Service ", ex);
					logger.error("getFaultCode {} , getFaultString {}", ex.getSoapFault().getFaultCode());
					response.setErrorCode(String.valueOf(ex.getFaultCode()));
					response.setErrorMessage(ex.getFaultStringOrReason());
					response.setStatus(false);
					networkInventory.setResponse(ex.getFaultStringOrReason());
					networkInventoryRepository.save(networkInventory);
				}
			  }
			  ScServiceAttribute additionalIpServiceAttributes = serviceAttributeRepository
						.findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(task.getServiceId(),"Additional IPs");
				if(Objects.nonNull(additionalIpServiceAttributes) && Objects.nonNull(additionalIpServiceAttributes.getAttributeValue())
						&& !additionalIpServiceAttributes.getAttributeValue().isEmpty() && "Yes".equalsIgnoreCase(additionalIpServiceAttributes.getAttributeValue())){
					logger.info("Additional Ip exists");
					response.setData("additionalIpScenario");
					response.setStatus(true);
				}
			} else {
				logger.info("Task is null in CramerService > createCLRByTaskId Method");
			}
			
			return response;
		} catch (Exception e) {
			logger.error("Exception in Cramer Create CLR  ", e);
			networkInventory.setResponse(e.getMessage());
			networkInventoryRepository.save(networkInventory);
			response.setStatus(false);
            response.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
            response.setErrorMessage(e.getMessage());
            return response;
		}
	}
 
 	private Response createCLRForCGW(CramerInfoBean cramerInfoBean, Task task, Map<String, Object> taskDataMap,
		Map<String, String> attributesMap) throws TclCommonException{
 		logger.info("createCLRForCGW method invoked::{}",task.getId());
		Response response = new Response();
		NetworkInventory networkInventory = new NetworkInventory();
		try {
			if (task != null) {				
				Map<String, String> siteAcompMap=	commonFulfillmentUtils.getComponentAttributes(task.getServiceId(), "LM", "A");
				if(Objects.nonNull(siteAcompMap) && siteAcompMap.containsKey("skipAutoClr") 
						  && Objects.nonNull(siteAcompMap.get("skipAutoClr")) && !siteAcompMap.get("skipAutoClr").isEmpty() 
						&& "Yes".equalsIgnoreCase(siteAcompMap.get("skipAutoClr"))) {
					logger.info("CGW Skip Auto Clr exists {}", siteAcompMap.get("skipAutoClr"));
					networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
					networkInventory.setServiceCode(task.getServiceCode());
					networkInventory.setRequestId(cramerInfoBean.getProcessInstanceId());
					networkInventory.setRequest("Skip CLR Request");
					networkInventory.setResponse("Skip CLR Response");
					networkInventory.setType(CramerConstants.SKIP_CLR);
					networkInventoryRepository.save(networkInventory);
					response.setStatus(true);
					response.setData("skip");
					return response;
				}
				logger.info("CGW Skip Auto Clr not exists");
				InitiateCLRCreation initiateCLRCreation = new InitiateCLRCreation();
				initiateCLRCreation.setSERVICEID(task.getServiceCode());
				initiateCLRCreation.setSERVICETYPE(CramerConstants.GVPN);
				initiateCLRCreation.setScenarioType(CramerConstants.CLR_CREATION);
				initiateCLRCreation.getListOfFeasibilityId()
				.add(StringUtils.trimToEmpty((String) taskDataMap.get(CramerConstants.FEASIBILITY_ID)));
				initiateCLRCreation.setCOPFID(String.valueOf((Integer) taskDataMap.get(CramerConstants.ORDER_ID)));
				initiateCLRCreation.setRequestID(cramerInfoBean.getProcessInstanceId());
				
				OrderDetails orderDetails = new OrderDetails();
				orderDetails.setBANDWIDTHVALUE(
						StringUtils.trimToEmpty((String) taskDataMap.get(CramerConstants.AGGREGATION_BANDWIDTH))
								.replaceAll(CramerConstants.mbps, CramerConstants.EMPTY));
				orderDetails.setBANDWIDTHUNIT(CramerConstants.Mbps);
				orderDetails.setCUSTOMERNAME(
						StringUtils.trimToEmpty((String) taskDataMap.get(CramerConstants.CUSTOMER_NAME)));
				orderDetails.setCOVERAGE(CramerConstants.CUSTOMER_ACCESS);
				orderDetails.setRequestType(CramerConstants.TYPE_NEW);
				orderDetails.setOrderType(CramerConstants.TYPE_NEW);
				orderDetails.setPRERESERVED(true);
				initiateCLRCreation.setOrderDetails(orderDetails);
				
				UccServDetails uccServDetails = new UccServDetails();
				UccService uccService = new UccService();
				uccService.getAttribute().add(new Attributes());
				List<UccService> uccServiceList = new ArrayList<>();
				uccServiceList.add(uccService);
				initiateCLRCreation.setUCCServDtls(uccServDetails);

				UaApplicable uaApplicable = new UaApplicable();
				initiateCLRCreation.setUAApplicable(uaApplicable);

				ClrDesignDtls clrDesignDtls = new ClrDesignDtls();
				IldDtls ildDtls = new IldDtls();
				ildDtls.setSTATICPROTECTION("");
				clrDesignDtls.setILDDtls(ildDtls);
				clrDesignDtls.setMAXHOP(8);
				clrDesignDtls.setPRIMARYSECONDARY(true);
				clrDesignDtls.setUSEHANGINGNTWKCIRCUITS(false);

				NldDtls nldDtls = new NldDtls();
				nldDtls.setALLOWEDTIER1HOPS(3);
				nldDtls.setALLOWTTSLNODES(false);
				nldDtls.setOTHERSEGMENT(CramerConstants.EMPTY);

				clrDesignDtls.setNLDDtls(nldDtls);
				initiateCLRCreation.setClrDesignDtls(clrDesignDtls);
				
				IpServiceAttributes ipServiceAttributes = new IpServiceAttributes();
				ipServiceAttributes.setIsBFDEnabled(false);
				ipServiceAttributes.setBaseRate(CramerConstants.EMPTY);
				ipServiceAttributes.setBaseRateUnit(CramerConstants.EMPTY);
				ipServiceAttributes.setBurstRate(CramerConstants.EMPTY);
				ipServiceAttributes.setBurstRateUnit(CramerConstants.EMPTY);
				ipServiceAttributes.setBTSIP(CramerConstants.EMPTY);
				ipServiceAttributes.setCUID(StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.CUID)));
				ipServiceAttributes.setCustomerRequirement(CramerConstants.EMPTY);
				ipServiceAttributes.setCustomerType(CramerConstants.EMPTY);
				ipServiceAttributes.setCustomerWANIPSpecified(false);
				ipServiceAttributes.setIsExtendedLAN(false);
				ipServiceAttributes.setIsExtendedLanChanged(false);
				ipServiceAttributes.setIPAddressArrangement(CramerConstants.EMPTY);
				ipServiceAttributes.setIsIpPathTypeChanged(false);
				ipServiceAttributes.setIpPoolTypeRequired("VPN");
				ipServiceAttributes.setIsLANIPCustProvided(false);
				ipServiceAttributes.setLANIP(CramerConstants.EMPTY);
				ipServiceAttributes.setLANProvidedByCustomer(CramerConstants.EMPTY);
				ipServiceAttributes.setLastMileInterface("Fast Ethernet");
				ipServiceAttributes.setLINKTYPE(CramerConstants.EMPTY);
				ipServiceAttributes.setIsLMTypeChanged(false);
				ipServiceAttributes.setMasterVRFServiceId(CramerConstants.EMPTY);
				ipServiceAttributes.setIsMultiCosRequired(true);
				ipServiceAttributes.setMultiVRFFlag(false);
				ipServiceAttributes.setMultiVRFSolution(false);
				ipServiceAttributes.setMultilink(CramerConstants.EMPTY);
				ipServiceAttributes.setNatNMSPoolRequired("FALSE"); 
				ipServiceAttributes.setNetworkCompType("OFFNET WIRELESS");
				String nniId = StringUtils.trimToEmpty((String)taskDataMap.get("nniId"));
				ipServiceAttributes.setNNIID(nniId);
				ipServiceAttributes.setNoOfVRFs("0");
				ipServiceAttributes.setNosLANIPRequired(CramerConstants.EMPTY);
				ipServiceAttributes.setNumberOfIpAddresses(CramerConstants.EMPTY);
				ipServiceAttributes.setOldServiceId(CramerConstants.EMPTY);
				ipServiceAttributes.setPathType(CramerConstants.EMPTY);
				ipServiceAttributes.setPortBandwidth(StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.AGGREGATION_BANDWIDTH)).replaceAll(CramerConstants.Mbps, CramerConstants.EMPTY));
				ipServiceAttributes.setPortBandwidthUnit(CramerConstants.Mbps);
				String primarySeconday=StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.PRIMARY_SECONDARY));
				String link=(String) taskDataMap.getOrDefault("prisecLink",null);
				if(primarySeconday.equalsIgnoreCase(CramerConstants.SECONDARY)) {
					clrDesignDtls.setPRIMARYSECONDARY(true);
					ipServiceAttributes.setPriSecMapping("SECONDARY");
					ipServiceAttributes.setServiceLink(link);

				}else if(primarySeconday.equalsIgnoreCase(CramerConstants.PRIMARY)) {
					clrDesignDtls.setPRIMARYSECONDARY(true);
					ipServiceAttributes.setPriSecMapping("PRIMARY");
					ipServiceAttributes.setServiceLink(link);
				}
				ipServiceAttributes.setProvider("MAN");
				ipServiceAttributes.setIsRoutableToNonRoutable(false);
				ipServiceAttributes.setRoutingProtocol(StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.ROUTING_PROTOCOL)));
				ipServiceAttributes.setSectorID(CramerConstants.EMPTY);
				ipServiceAttributes.setServiceCategory("VPN PORT");
				ipServiceAttributes.setServiceOption(CramerConstants.MANAGED);
				ipServiceAttributes.setIsServiceOptionChanged(false);
				ipServiceAttributes.setSharedCPE(CramerConstants.EMPTY);
				ipServiceAttributes.setSharedCPERequired(false);
				ipServiceAttributes.setSharedLM(CramerConstants.EMPTY);
				ipServiceAttributes.setSharedLMRequired(false);
				if(taskDataMap.containsKey(CramerConstants.SOURCE_CITY) && taskDataMap.get(CramerConstants.SOURCE_CITY)!=null){
					ipServiceAttributes.setTclPopCity(String.valueOf(taskDataMap.get(CramerConstants.SOURCE_CITY)).toUpperCase());
				}
				ipServiceAttributes.setUniquePopId(StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.POP_SITE_CODE)));
				ipServiceAttributes.setTotalVRFBandwidth(CramerConstants.EMPTY);
				ipServiceAttributes.setTYPEOFPEPLINK(CramerConstants.EMPTY);
				ipServiceAttributes.setVLANLM(CramerConstants.EMPTY);
				ipServiceAttributes.setIsVRFLiteRequired(false);
				ipServiceAttributes.setASNumber(CramerConstants.EMPTY);
				initiateCLRCreation.setIPServiceAttributes(ipServiceAttributes);
				AdditionalAttr additionalAttr = new AdditionalAttr();
				String useCase="";
				if(taskDataMap.containsKey(CramerConstants.USECASE) && taskDataMap.get(CramerConstants.USECASE)!=null){
					useCase=(String)taskDataMap.get(CramerConstants.USECASE);
					logger.info("UseCase::{}",useCase);
				}
				attributesMap.put(CramerConstants.CURRENT_CG_USECASE, useCase);
				attributesMap.put(CramerConstants.REQUESTING_SYSTEM, cramerSourceSystem);
				attributesMap.entrySet().stream().forEach(map -> {
					Attributes attr = new Attributes();
					attr.setKey(map.getKey());
					attr.setValue(map.getValue());
					additionalAttr.getAttributes().add(attr);
				});
				if (Objects.nonNull(attributesMap) && !attributesMap.isEmpty()) {
					initiateCLRCreation.setAdditionalAttributes(additionalAttr);
				}

				// Save request & response
				networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
				networkInventory.setServiceCode(task.getServiceCode());
				networkInventory.setRequestId(cramerInfoBean.getProcessInstanceId());
				networkInventory
						.setRequest(Utils.convertObjectToXmlString(initiateCLRCreation, InitiateCLRCreation.class));
				networkInventory.setType(CramerConstants.CREATE_CLR);

				SoapRequest soapRequest = new SoapRequest();
				soapRequest.setUrl(createClrUrl);
				soapRequest.setRequestObject(initiateCLRCreation);
				soapRequest.setContextPath("com.tcl.dias.serviceactivation.cramer.servicedesign.beans");
				soapRequest.setSoapUserName("");
				soapRequest.setSoapPwd("");
				soapRequest.setConnectionTimeout(60000);
				soapRequest.setReadTimeout(60000);
				try {
					JAXBElement jaxBResponse = genericWebserviceClient.doSoapCallForObject(soapRequest,
							JAXBElement.class);
					InitiateCLRCreationResponse initiateCLRCreationResponse = (InitiateCLRCreationResponse) jaxBResponse
							.getValue();
					if (initiateCLRCreationResponse != null
							&& initiateCLRCreationResponse.getClrCreationResult() != null) {

						try {
							networkInventory.setResponse(Utils.convertObjectToXmlString(initiateCLRCreationResponse,
									InitiateCLRCreationResponse.class));
							networkInventoryRepository.save(networkInventory);
						} catch (Exception ex) {
							logger.error("Exception in Cramer CreateCLRForCGW  ", ex);
						}

						Acknowledgement acknowledgement = initiateCLRCreationResponse.getClrCreationResult().getAck();
						if (acknowledgement != null) {
							logger.info("CreateCLRForCGW acknowledgement RequestID=> {}, ServiceID=> {} Status={}",
									acknowledgement.getRequestID(), acknowledgement.getServiceID(),
									acknowledgement.isStatus());
							response.setStatus(acknowledgement.isStatus());
							return response;
						}

					}
				} catch (SoapFaultClientException ex) {
					if (Objects.nonNull(ex.getSoapFault().getFaultDetail())) {
						logger.error("SoapFaultClientException in Cramer CreateCLRForCGW ", ex);
						SoapFaultDetail soapFaultDetail = ex.getSoapFault().getFaultDetail();
						if (soapFaultDetail != null) {
							SoapFaultDetailElement detailElementChild = soapFaultDetail.getDetailEntries().next();
							Source detailSource = detailElementChild.getSource();
							Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
							marshaller.setContextPath(soapRequest.getContextPath());
							@SuppressWarnings("unchecked")
							JAXBElement<com.tcl.dias.serviceactivation.cramer.servicedesign.beans.ServiceFault> source = (JAXBElement<com.tcl.dias.serviceactivation.cramer.servicedesign.beans.ServiceFault>) marshaller
									.unmarshal(detailSource);
							logger.error("ErrorCode:: {} , ErrorLongDescription:: {},ErrorShortDescription:: {}",
									source.getValue().getErrorCode(), source.getValue().getErrorLongDescription(),
									source.getValue().getErrorShortDescription());
							/*if(source.getValue().getErrorCode().equalsIgnoreCase("CBIAS100093")){
								response.setStatus(true);
								response.setData("skipExistClr");
							}else if (source.getValue().getErrorCode().equalsIgnoreCase("CBIAS100010")
									|| source.getValue().getErrorCode().equalsIgnoreCase("CBIAS100020")
									|| source.getValue().getErrorCode().equalsIgnoreCase("CBIAS100030")) {
								response.setStatus(true);
							} else {*/
								response.setStatus(false);
								response.setErrorCode(source.getValue().getErrorCode());
								response.setErrorMessage(source.getValue().getErrorLongDescription());
							//}
							networkInventory.setResponse(source.getValue().getErrorCode() + ":"
									+ source.getValue().getErrorLongDescription());
							networkInventoryRepository.save(networkInventory);
						}

					} else {
						logger.error("SoapFaultClientException in Cramer CreateCLRForCGW Service ", ex);
						logger.error("getFaultCode {} , getFaultString {}", ex.getSoapFault().getFaultCode());
						response.setErrorCode(String.valueOf(ex.getFaultCode()));
						response.setErrorMessage(ex.getFaultStringOrReason());
						response.setStatus(false);
						networkInventory.setResponse(ex.getFaultStringOrReason());
						networkInventoryRepository.save(networkInventory);
					}
				}

			} else {
				logger.info("Task is null in CramerService > CreateCLRForCGW Method");
			}
			return response;
		} catch (Exception e) {
			logger.error("Exception in Cramer CreateCLRForCGW  ", e);
			networkInventory.setResponse(e.getMessage());
			networkInventoryRepository.save(networkInventory);
			response.setStatus(false);
			response.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
			response.setErrorMessage(e.getMessage());
			return response;
		}
	}


	public void constructOnnetWirelessDetails(String serviceCode,Integer serviceDetailId) {
 		logger.info("constructOnnetWirelessDetails invoked");
 		 ScComponentAttribute rfMake = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetailId,
							"rfMake", "LM", "A");
			String deviceType = null;
			if (rfMake != null && Objects.nonNull(rfMake.getAttributeValue()) && !rfMake.getAttributeValue().isEmpty()) {
				deviceType = rfMake.getAttributeValue();
			}else{
				ScComponentAttribute rfTechnology = scComponentAttributesRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetailId,
								"rfTechnology", "LM", "A");
				if(Objects.nonNull(rfTechnology) && Objects.nonNull(rfTechnology.getAttributeValue()) && !rfTechnology.getAttributeValue().isEmpty()){
					deviceType = rfTechnology.getAttributeValue();
				}
			}					
			if (deviceType !=null && !deviceType.isEmpty() &&  "radwin".equalsIgnoreCase(deviceType)) {
				logger.info("Radwin");
				MstRadwinDetails mstRadwinDetails= new MstRadwinDetails();
				mstRadwinDetails.setNetpRefId(generateNetpRefId("RADW"));
				mstRadwinDetails.setServiceCode(serviceCode);
				mstRadwinDetails.setIsActive("Y");
				mstRadwinDetails.setLastModifiedBy("OPTIMUS_INITIAL");
				mstRadwinDetails.setLastModifiedDate(new Timestamp(new Date().getTime()).toString());
				mstRadwinDetailsRepository.save(mstRadwinDetails);
			}else if (deviceType !=null && !deviceType.isEmpty() && ("cambium".equalsIgnoreCase(deviceType) || "wimax".equalsIgnoreCase(deviceType))) {
				logger.info("Cambium");
				MstCambiumDetails mstCambiumDetails= new MstCambiumDetails();
				mstCambiumDetails.setNetpRefId(generateNetpRefId("CAMB"));
				mstCambiumDetails.setServiceCode(serviceCode);
				mstCambiumDetails.setIsActive("Y");
				mstCambiumDetails.setLastModifiedBy("OPTIMUS_INITIAL");
				mstCambiumDetails.setLastModifiedDate(new Timestamp(new Date().getTime()).toString());
				mstCambiumDetailsRepository.save(mstCambiumDetails);
			}
 	}


	private String generateNetpRefId(String deviceType){
 		logger.info("generateNetpRefId invoked");
 		String netPRefId="MIGR_NETP_LM_";
 		String pattern = "MMMyyyy_dd_HHSS";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
 		netPRefId=netPRefId+deviceType+"_"+date;
 		logger.info("NetpRefId ::{}",netPRefId);
		return netPRefId;
 	}
 
	private ServiceDetail saveServiceDetails(Task task) {
		logger.info("saveServiceDetails invoked");
		ServiceDetail serviceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(
				task.getServiceCode(), TaskStatusConstants.ISSUED);
		if (serviceDetail!=null) {
			serviceDetail.setServiceState(TaskStatusConstants.CANCELLED);
			serviceDetailRepository.saveAndFlush(serviceDetail);
		}
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(task.getOrderCode(), "Y");
		ServiceDetail activeServiceDetail = serviceDetailRepository
				.findFirstByServiceIdAndServiceStateOrderByVersionDesc(task.getServiceCode(),
						TaskStatusConstants.ACTIVE);
		Integer version = null;
		if (Objects.nonNull(activeServiceDetail)) {
			version = activeServiceDetail.getVersion();
		}
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(task.getServiceId()).get();

		Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
				.getComponentAttributes(task.getServiceId(), AttributeConstants.COMPONENT_LM, "A");

		serviceDetail = serviceActivationService.saveServiceDetailsActivation(task.getServiceCode(), version,
				scOrder, scServiceDetail, scComponentAttributesAMap);
		return serviceDetail;
	}
	
	@Transactional(readOnly = false)
	public MstP2PDetails getMstP2PDetails(String serviceId) {
		MstP2PDetails mstP2PDetails = mstP2PDetailsRepository.findFirstByServiceCodeAndIsActiveOrderByIdDesc(serviceId, "Y");
		return mstP2PDetails;
	}
	
	@Transactional(readOnly = false)
	public SSDumpResponseBean updateOnnetWirelessDetails(String serviceCode) throws TclCommonException {
		ServiceDetail serviceDetail = serviceDetailRepository
				.findFirstByServiceIdAndServiceStateOrderByVersionDesc(serviceCode, TaskStatusConstants.ACTIVE);
		if (serviceDetail != null) {
			return updateOnnetWirelessDetails(serviceCode, serviceDetail,serviceDetail.getOrderSubCategory());
		}
		else {
			return updateOnnetWirelessDetails(serviceCode, null,null);

		}
		
	}


public SSDumpResponseBean updateOnnetWirelessDetails(String serviceCode, ServiceDetail serviceDetail, String orderSubCategory) throws TclCommonException {
		logger.info("updateOnnetWirelessDetails invoked");
		SSDumpResponseBean ssDumpResponseBean = null;

		if(Objects.nonNull(serviceCode) && !serviceCode.isEmpty()){
			logger.info("SERVICE CODE: {}",serviceCode);
			ssDumpResponseBean=activationService.getSSDumpDetails("testing#"+serviceCode);
			if(Objects.nonNull(ssDumpResponseBean) && Objects.nonNull(ssDumpResponseBean.getResponse()) && serviceDetail!=null ){
				logger.info("Ss dump api response is present for Service Code :{}", serviceCode);
				if( orderSubCategory!=null && !orderSubCategory.toLowerCase().contains("bso") && !orderSubCategory.toLowerCase().contains("shifting"))
					updateOnnetWirelessDetailsWithSSDump(serviceCode, serviceDetail, ssDumpResponseBean);
				else
					updateOnnetWirelessDetailsWithRfData(serviceCode, serviceDetail);
			}
			else{
				logger.error("Ss dump api response not present for Service Code :{}", serviceCode);
				try{
					notifyTeamForNoSSDump(serviceCode);
				}
				catch (Exception e) {
					logger.error("Error in notifyTeamForNoSSDump for serviceCode {} as {} ", serviceCode, e);
				}
			}
		}
		return ssDumpResponseBean;
	}

	public void updateOnnetWirelessDetailsWithSSDump(String serviceCode, ServiceDetail serviceDetail, SSDumpResponseBean ssDumpResponseBean) {
		logger.info("updateOnnetWirelessDetailsWithSSDump invoked");
		logger.info("Ss dump api success response");
		SSDumpBean response= ssDumpResponseBean.getResponse();
		if(Objects.nonNull(response.getVendor()) && !response.getVendor().isEmpty()){
			String wirelessType=response.getVendor();
			logger.info("wirelessType:{}",wirelessType);
			String customerName="";
			String hsuMac="";
			String hsuIp="NA";
			List<String> netpRefIdList= new ArrayList<>();
			if(Objects.nonNull(response.getCustomerName()) && !response.getCustomerName().isEmpty()){
				logger.info("customerName:{}",customerName);
				customerName=response.getCustomerName();
			}
			if(wirelessType.toLowerCase().contains("radwin")){
				Map<String,Boolean> differAttributeMap = new HashMap<>();
				boolean isCreated=updateRadwinDetails(response, serviceCode,netpRefIdList,differAttributeMap, serviceDetail);
				if(Objects.nonNull(response.getHsuMac()) && !response.getHsuMac().isEmpty()){
					hsuMac=response.getHsuMac();
					logger.info("hsuMac:{}",hsuMac);
				}
				if(Objects.nonNull(response.getHsuIp()) && !response.getHsuIp().isEmpty()){
					hsuIp=response.getHsuIp();
					logger.info("hsuIp:{}",hsuIp);
				}

					logger.info("Radwin newly created service id");
					try {
						String srName=null;
						String custServiceString = serviceCode + "_" + customerName;
						logger.info("Radwin newly created service id custServiceString:{}",custServiceString);
						String custServiceString2 = custServiceString.replaceAll(" ", "_");
						logger.info("Radwin newly created service id custServiceString2:{}",custServiceString2);
						if(custServiceString2!=null && custServiceString2.length()>59) {
							srName = custServiceString2.substring(0, 58);
						}
						else {
							srName=custServiceString2;
						}
						logger.info("Radwin newly created service id srName:{}",srName);
						boolean isRadwinNodeFound = netpRfDataSearchService.netpRadwinSearchNodeResult(serviceCode, netpRefIdList.get(0));
						logger.info("Radwin searching node in NetP for service code {} netpRefId {} triggering NetpRfDataSearchService-netpRadwinSearch : {}", serviceCode,netpRefIdList.get(0), isRadwinNodeFound);
						if(!isRadwinNodeFound) {
							logger.info("Radwin newly created service id for service code {} srname {} triggering netpRfDataCreationService-netpRadwinCreate", serviceCode, srName);
							netpRfDataCreationService.netpRadwinCreate(serviceCode, hsuIp, "HSntNetwork:1002",
									srName, netpRefIdList.get(0), hsuMac, "FALSE");
						}else if(differAttributeMap.containsKey("isHsuMacChanged") && differAttributeMap.get("isHsuMacChanged") && !hsuMac.isEmpty()){
							logger.info("Radwin exists for service code {} srname {} triggering netpRfDataUpdationService-netpRadwinUpdate as HsuMac differs", serviceCode, srName);
							String status=netpRfDataCreationService.netpRadwinModifyMacd(serviceCode, hsuIp,"HSntNetwork:1002",srName,netpRefIdList.get(0),hsuMac,"FALSE");
							if(status.contains("ERROR")) {
								logger.info("Radwin error occured for service code {} srname {} triggering netpRfDataUpdationService-netpRadwinUpdate as HsuMac differs", serviceCode, srName);
								notifyNetworkTeamForConfiguration(serviceCode, "Radwin", customerName, hsuMac, hsuIp,
										netpRefIdList.get(0));
							}
						}
					} catch (Exception e) {
						logger.error("Error in netpRadwinCreate for serviceCode {} as {} ", serviceCode,e);
					}

				//}
			}else if(wirelessType.toLowerCase().contains("cambium")){
				Map<String,Boolean> differAttributeMap = new HashMap<>();
				boolean isCreated=updateCambiumDetails(response, serviceCode,netpRefIdList,differAttributeMap, serviceDetail);
				if(Objects.nonNull(response.getMac()) && !response.getMac().isEmpty()){
					hsuMac=response.getMac().replace(":", " ");
					logger.info("hsuMac:{}",hsuMac);
				}
				if(Objects.nonNull(response.getHsuIp()) && !response.getHsuIp().isEmpty()){
					hsuIp=response.getHsuIp();
					logger.info("hsuIp:{}",hsuIp);
				}
				//if (!netpRefIdList.isEmpty()) {
					logger.info("Cambium newly created service id");
					try {
						String deviceName=null;
						String custServiceString = serviceCode + "_" + customerName;
						logger.info("Cambium newly created service id custServiceString:{}", custServiceString);
						String custServiceString2 = custServiceString.replaceAll(" ", "_");
						logger.info("Cambium newly created service id custServiceString2:{}",
								custServiceString2);
						if(custServiceString2!=null && custServiceString2.length()>59) {

						 deviceName = custServiceString2.substring(0, 58);
						}
						else {
							deviceName=custServiceString2;
						}
						logger.info("Cambium newly created service id deviceName:{}", deviceName);
						String srName = hsuMac.replaceAll(":", " ");
						logger.info("Cambium newly created service id srName:{}", srName);
						boolean isCambiumNodeFound = netpRfDataSearchService.netpCambiumSearchNodeResult(serviceCode, netpRefIdList.get(0));
						logger.info("Cambium searching node in NetP for service code {} netpRefId {} triggering NetpRfDataSearchService-netpCambiumSearch : {}", serviceCode,netpRefIdList.get(0), isCambiumNodeFound);
						if(!isCambiumNodeFound) {
							logger.info(
									"Cambium newly created service id for service code {} deviceName {} and srname {} triggering netpRfDataCreationService-netpCambiumCreate",
									serviceCode, deviceName, srName);
							netpRfDataCreationService.netpCambiumCreate(serviceCode, "CWnt0000Ff", deviceName,
									srName, netpRefIdList.get(0), "FALSE");
						}else if(differAttributeMap.containsKey("isHsuMacChanged") && differAttributeMap.get("isHsuMacChanged") && !hsuMac.isEmpty()){
							logger.info("Cambium exists for service code {} srname {} triggering netpRfDataUpdationService-netpCambiumUpdate as HsuMac differs", serviceCode, srName);
							String status=netpRfDataCreationService.netpCambiumModifyMacd(serviceCode, "CWnt0000Ff",deviceName,
									srName, netpRefIdList.get(0), "FALSE");
							if(status.contains("ERROR")) {
								logger.info("Cambium error occured for service code {} srname {} triggering netpRfDataUpdationService-netpCambiumUpdate as HsuMac differs", serviceCode, srName);
								notifyNetworkTeamForConfiguration(serviceCode, "Cambium", customerName, hsuMac, hsuIp,
										netpRefIdList.get(0));
							}
						}
						logger.info(
								"Cambium newly created service id for service code {} deviceName {} and srname {} exited netpRfDataCreationService-netpCambiumCreate",
								serviceCode, deviceName, srName);

					} catch (Exception e) {
						logger.error("Error in netpCambiumCreate for serviceCode {} as {} ", serviceCode,e);
					}
				//}
			}
		}
	}


	public void updateOnnetWirelessDetailsWithRfData(String serviceCode, ServiceDetail serviceDetail) {
		logger.info("updateOnnetWirelessDetailsWithRfData invoked");
		ScComponentAttribute rfMake = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
						"rfMake", "LM", "A");
		String wirelessType = null;
		if (rfMake != null && Objects.nonNull(rfMake.getAttributeValue()) && !rfMake.getAttributeValue().isEmpty()) {
			wirelessType = rfMake.getAttributeValue();
		}
		else{
			ScComponentAttribute rfTechnology = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(),
							"rfTechnology", "LM", "A");
			if(Objects.nonNull(rfTechnology) && Objects.nonNull(rfTechnology.getAttributeValue()) && !rfTechnology.getAttributeValue().isEmpty()){
				wirelessType = rfTechnology.getAttributeValue();
			}
		}

		if(Objects.nonNull(wirelessType) && !wirelessType.isEmpty()){
			logger.info("wirelessType:{}",wirelessType);
			String customerName="";
			String hsuMac="";
			String hsuIp="NA";
			List<String> netpRefIdList= new ArrayList<>();
			if(Objects.nonNull(serviceDetail.getOrderDetail().getCustomerName()) && !serviceDetail.getOrderDetail().getCustomerName().isEmpty()){
				logger.info("customerName:{}",customerName);
				customerName=serviceDetail.getOrderDetail().getCustomerName();
			}
			if(wirelessType.toLowerCase().contains("radwin")){
				boolean isCreated=updateRadwinDetails(null, serviceCode,netpRefIdList,null, serviceDetail);
				ScComponentAttribute hsuIpScComponentAttribute = scComponentAttributesRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(), "suIp", "LM", "A");

				//if (!netpRefIdList.isEmpty()) {
				logger.info("Radwin newly created service id");
				try {
					String srName=null;
					String custServiceString = serviceCode + "_" + customerName;
					logger.info("Radwin newly created service id custServiceString:{}",custServiceString);
					String custServiceString2 = custServiceString.replaceAll(" ", "_");
					logger.info("Radwin newly created service id custServiceString2:{}",custServiceString2);
					if(custServiceString2!=null && custServiceString2.length()>59) {
						srName = custServiceString2.substring(0, 58);
					}
					else {
						srName=custServiceString2;
					}
					logger.info("Radwin newly created service id srName:{}",srName);
					boolean isRadwinNodeFound = netpRfDataSearchService.netpRadwinSearchNodeResult(serviceCode, netpRefIdList.get(0));
					logger.info("Radwin searching node in NetP for service code {} netpRefId {} triggering NetpRfDataSearchService-netpRadwinSearch : {}", serviceCode,netpRefIdList.get(0), isRadwinNodeFound);
					if(!isRadwinNodeFound) {
						logger.info("Radwin newly created service id for service code {} srname {} triggering netpRfDataCreationService-netpRadwinCreate", serviceCode, srName);
						netpRfDataCreationService.netpRadwinCreate(serviceCode, hsuIp, "HSntNetwork:1002",
								srName, netpRefIdList.get(0), hsuMac, "FALSE");
					}
				} catch (Exception e) {
					logger.error("Error in netpRadwinCreate for serviceCode {} as {} ", serviceCode,e);
				}

				//}
			}else if(wirelessType.toLowerCase().contains("cambium")){
				boolean isCreated=updateCambiumDetails(null, serviceCode,netpRefIdList,null, serviceDetail);
				ScComponentAttribute macScComponentAttribute = scComponentAttributesRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceDetail.getScServiceDetailId(), "mac", "LM", "A");
				if(Objects.nonNull(macScComponentAttribute) && !macScComponentAttribute.getAttributeValue().isEmpty()){
					hsuMac=macScComponentAttribute.getAttributeValue().replace(":", " ");
					logger.info("hsuMac:{}",hsuMac);
				}

				//if (!netpRefIdList.isEmpty()) {
				logger.info("Cambium newly created service id");
				try {
					String deviceName=null;
					String custServiceString = serviceCode + "_" + customerName;
					logger.info("Cambium newly created service id custServiceString:{}", custServiceString);
					String custServiceString2 = custServiceString.replaceAll(" ", "_");
					logger.info("Cambium newly created service id custServiceString2:{}",
							custServiceString2);
					if(custServiceString2!=null && custServiceString2.length()>59) {

						deviceName = custServiceString2.substring(0, 58);
					}
					else {
						deviceName=custServiceString2;
					}
					logger.info("Cambium newly created service id deviceName:{}", deviceName);
					String srName = hsuMac.replaceAll(":", " ");
					logger.info("Cambium newly created service id srName:{}", srName);
					boolean isCambiumNodeFound = netpRfDataSearchService.netpCambiumSearchNodeResult(serviceCode, netpRefIdList.get(0));
					logger.info("Cambium searching node in NetP for service code {} netpRefId {} triggering NetpRfDataSearchService-netpCambiumSearch : {}", serviceCode,netpRefIdList.get(0), isCambiumNodeFound);
					if(!isCambiumNodeFound) {
						logger.info(
								"Cambium newly created service id for service code {} deviceName {} and srname {} triggering netpRfDataCreationService-netpCambiumCreate",
								serviceCode, deviceName, srName);
						netpRfDataCreationService.netpCambiumCreate(serviceCode, "CWnt0000Ff", deviceName,
								srName, netpRefIdList.get(0), "FALSE");
					}
					logger.info(
							"Cambium newly created service id for service code {} deviceName {} and srname {} exited netpRfDataCreationService-netpCambiumCreate",
							serviceCode, deviceName, srName);

				} catch (Exception e) {
					logger.error("Error in netpCambiumCreate for serviceCode {} as {} ", serviceCode,e);
				}
				//}
			}
		}
	}


	private void notifyNetworkTeamForConfiguration(String serviceCode,String type, String customerName, String hsuMac, String hsuIp, String netpRefId) throws TclCommonException{
		List<String> tempCcMailList= new ArrayList<>(Arrays.asList("Ashish.Kumar4@tatacommunications.com"));
		List<String> tempToMailList= new ArrayList<>(Arrays.asList("Dimple.Subburaj@tatacommunications.com"));
		notificationService.notifyNetworkTeamForConfiguration(tempCcMailList,tempToMailList,serviceCode,type,customerName,hsuMac,hsuIp,netpRefId);
	}

	private void notifyTeamForNoSSDump(String serviceCode) throws TclCommonException{
		List<String> tempCcMailList= new ArrayList<>(Arrays.asList("Ashish.Kumar4@tatacommunications.com"));
		List<String> tempToMailList= new ArrayList<>(Arrays.asList("Dimple.Subburaj@tatacommunications.com"));
		notificationService.notifyTeamForNoSSDump(tempCcMailList,tempToMailList,serviceCode);
	}
	
	private boolean updateRadwinDetails(SSDumpBean response, String serviceCode, List<String> netpRefIdList, Map<String, Boolean> differAttributeMap, ServiceDetail serviceDetail) {
		boolean isCreated=false;
		logger.info("updateRadwinDetails invoked");
		MstRadwinDetails mstRadwinDetails=mstRadwinDetailsRepository.findFirstByServiceCodeAndIsActiveOrderByIdDesc(serviceCode,"Y");
		if(Objects.nonNull(mstRadwinDetails)){
			logger.info("Radwin exists");
			if(Objects.nonNull(response))
				mstRadwinDetails=constructRadwinDetailsWithSSDump(mstRadwinDetails,response,differAttributeMap);
			else
				mstRadwinDetails=constructRadwinDetailsWithRfData(mstRadwinDetails,serviceDetail);
			mstRadwinDetails.setLastModifiedBy("OPTIMUS_INITIAL");
			mstRadwinDetails.setLastModifiedDate(new Timestamp(new Date().getTime()).toString());
			netpRefIdList.add(mstRadwinDetails.getNetpRefId());
			mstRadwinDetailsRepository.save(mstRadwinDetails);
		}else{
			logger.info("Radwin doesn't exists");
			mstRadwinDetails= new MstRadwinDetails();
			if(Objects.nonNull(response))
				mstRadwinDetails=constructRadwinDetailsWithSSDump(mstRadwinDetails,response,differAttributeMap);
			else
				mstRadwinDetails=constructRadwinDetailsWithRfData(mstRadwinDetails,serviceDetail);
			mstRadwinDetails.setNetpRefId(generateNetpRefId("RADW"));
			netpRefIdList.add(mstRadwinDetails.getNetpRefId());
			mstRadwinDetails.setServiceCode(serviceCode);
			mstRadwinDetails.setIsActive("Y");
			mstRadwinDetails.setLastModifiedBy("OPTIMUS_INITIAL");
			mstRadwinDetails.setLastModifiedDate(new Timestamp(new Date().getTime()).toString());
			mstRadwinDetailsRepository.save(mstRadwinDetails);
			isCreated=true;
		}
		return isCreated;
	}

	private MstRadwinDetails constructRadwinDetailsWithSSDump(MstRadwinDetails mstRadwinDetails, SSDumpBean response, Map<String, Boolean> differAttributeMap) {
		logger.info("constructRadwinDetailsWithSSDump invoked");
		if(Objects.nonNull(response.getCustomerName()) && !response.getCustomerName().isEmpty()){
			mstRadwinDetails.setCustomerName(response.getCustomerName());
		}
		if(Objects.nonNull(response.getVendor()) && !response.getVendor().isEmpty()){
			mstRadwinDetails.setDeviceType(response.getVendor());
		}
		if(Objects.nonNull(response.getHsuMac()) && !response.getHsuMac().isEmpty()){
			if(Objects.nonNull(mstRadwinDetails.getHsuMac()) && !mstRadwinDetails.getHsuMac().isEmpty() 
					&& !response.getHsuMac().toLowerCase().equals(mstRadwinDetails.getHsuMac().toLowerCase())){
				differAttributeMap.put("isHsuMacChanged", true);
			}
			mstRadwinDetails.setHsuMac(response.getHsuMac().toLowerCase());
		}
		if(Objects.nonNull(response.getHbsIp()) && !response.getHbsIp().isEmpty()){
			mstRadwinDetails.setBtsIp(response.getHbsIp());
		}
		if(Objects.nonNull(response.getBsName()) && !response.getBsName().isEmpty()){
			mstRadwinDetails.setBtsName(response.getBsName());
		}
		if(Objects.nonNull(response.getSectorId()) && !response.getSectorId().isEmpty()){
			mstRadwinDetails.setSectorId(response.getSectorId());
		}
		if(Objects.nonNull(response.getLatitude()) && !response.getLatitude().isEmpty()){
			mstRadwinDetails.setLatitude(response.getLatitude());
		}
		if(Objects.nonNull(response.getLongitude()) && !response.getLongitude().isEmpty()){
			mstRadwinDetails.setLongitude(response.getLongitude());
		}
		if(Objects.nonNull(response.getState()) && !response.getState().isEmpty()){
			mstRadwinDetails.setState(response.getState());
		}
		if(Objects.nonNull(response.getRegion()) && !response.getRegion().isEmpty()){
			mstRadwinDetails.setRegion(response.getRegion());
		}
		if(Objects.nonNull(response.getAntennaHeight()) && !response.getAntennaHeight().isEmpty()){
			mstRadwinDetails.setAntennaHeight(response.getAntennaHeight());
		}
		if(Objects.nonNull(response.getHsuIp()) && !response.getHsuIp().isEmpty()){
			mstRadwinDetails.setHsuIp(response.getHsuIp());
		}
		if(Objects.nonNull(response.getDataVlan()) && !response.getDataVlan().isEmpty()){
			if(response.getDataVlan().contains(".")){
				String dataVlan[]=response.getDataVlan().split("\\.");
				mstRadwinDetails.setDataVlan(dataVlan[0]);
			}else{
				mstRadwinDetails.setDataVlan(response.getDataVlan());
			}
		}
		if(Objects.nonNull(response.getManagementVlan()) && !response.getManagementVlan().isEmpty()){
			mstRadwinDetails.setMgmtVlan(response.getManagementVlan());
		}
		if(Objects.nonNull(response.getFrequency()) && !response.getFrequency().isEmpty()){
			mstRadwinDetails.setFrequency(response.getFrequency());
		}
		if(Objects.nonNull(response.getSerialNumber()) && !response.getSerialNumber().isEmpty()){
			mstRadwinDetails.setSerialNumber(response.getSerialNumber());
		}
		if(Objects.nonNull(response.getTowerPoleHeight()) && !response.getTowerPoleHeight().isEmpty()){
			mstRadwinDetails.setPoleHeight(response.getTowerPoleHeight());
		}
		if(Objects.nonNull(response.getBuildingHeight()) && !response.getBuildingHeight().isEmpty()){
			mstRadwinDetails.setBuildingHeight(response.getBuildingHeight());
		}
		return mstRadwinDetails;
	}

	private MstRadwinDetails constructRadwinDetailsWithRfData(MstRadwinDetails mstRadwinDetails, ServiceDetail serviceDetail) {
		logger.info("constructRadwinDetailsWithRfData invoked");
		if(Objects.nonNull(serviceDetail.getOrderDetail().getCustomerName()) && !serviceDetail.getOrderDetail().getCustomerName().isEmpty()){
			mstRadwinDetails.setCustomerName(serviceDetail.getOrderDetail().getCustomerName());
		}

		Map<String, String> componentAttributesMap =commonFulfillmentUtils.getComponentAttributes(
				serviceDetail.getScServiceDetailId(), AttributeConstants.COMPONENT_LM, "A");

		String wirelessType = null;
		if (componentAttributesMap.get("rfMake") != null && ! componentAttributesMap.get("rfMake").isEmpty())
			wirelessType =  componentAttributesMap.get("rfMake");
		else{
			if (componentAttributesMap.get("rfTechnology") != null && ! componentAttributesMap.get("rfTechnology").isEmpty())
				wirelessType =  componentAttributesMap.get("rfTechnology");
			}
		mstRadwinDetails.setDeviceType(wirelessType);

			String mac = null;
			if (componentAttributesMap.get("mac") != null && ! componentAttributesMap.get("mac").isEmpty()){
				mac =  componentAttributesMap.get("mac");
				mstRadwinDetails.setHsuMac(mac.toLowerCase());
			}

		if (componentAttributesMap.get("btsIp") != null && ! componentAttributesMap.get("btsIp").isEmpty())
			mstRadwinDetails.setBtsIp(componentAttributesMap.get("btsIp"));

		if (componentAttributesMap.get("btsName") != null && ! componentAttributesMap.get("btsName").isEmpty())
			mstRadwinDetails.setBtsName(componentAttributesMap.get("btsName"));

		if (componentAttributesMap.get("sectorId") != null && ! componentAttributesMap.get("sectorId").isEmpty())
			mstRadwinDetails.setSectorId(componentAttributesMap.get("sectorId"));

		if (componentAttributesMap.get("latLong") != null && ! componentAttributesMap.get("latLong").isEmpty()){
			String[] latlong = componentAttributesMap.get("latLong").split(",");
			mstRadwinDetails.setLatitude(latlong[0]);
			mstRadwinDetails.setLongitude(latlong[1]);
		}

		if (componentAttributesMap.get("destinationState") != null && ! componentAttributesMap.get("destinationState").isEmpty())
			mstRadwinDetails.setState(componentAttributesMap.get("destinationState"));

		StateRegionMapping stateRegionMappin = stageRegionMappingRepository.findByStateLike(serviceDetail.getState());
		if (stateRegionMappin != null)
			mstRadwinDetails.setRegion(stateRegionMappin.getRegion());

		if (componentAttributesMap.get("suIp") != null && ! componentAttributesMap.get("suIp").isEmpty())
			mstRadwinDetails.setHsuIp(componentAttributesMap.get("suIp"));

//		if(Objects.nonNull(response.getDataVlan()) && !response.getDataVlan().isEmpty()){
//			if(response.getDataVlan().contains(".")){
//				String dataVlan[]=response.getDataVlan().split("\\.");
//				mstRadwinDetails.setDataVlan(dataVlan[0]);
//			}else{
//				mstRadwinDetails.setDataVlan(response.getDataVlan());
//			}
//		}
//		if(Objects.nonNull(response.getManagementVlan()) && !response.getManagementVlan().isEmpty()){
//			mstRadwinDetails.setMgmtVlan(response.getManagementVlan());
//		}
		if (componentAttributesMap.get("customerRadioFrequency") != null && ! componentAttributesMap.get("customerRadioFrequency").isEmpty())
			mstRadwinDetails.setFrequency(componentAttributesMap.get("customerRadioFrequency"));

//		if(Objects.nonNull(response.getSerialNumber()) && !response.getSerialNumber().isEmpty()){
//			mstRadwinDetails.setSerialNumber(response.getSerialNumber());
//		}
		mstRadwinDetails.setPoleHeight(setTowerOrPoleHeight(componentAttributesMap.get("towerHeight"), componentAttributesMap.get("poleHeight")));
		mstRadwinDetails.setAntennaHeight(setAntennaHeight(componentAttributesMap.get("towerHeight"), componentAttributesMap.get("poleHeight"), componentAttributesMap.get("mastHeight")));
//		if(Objects.nonNull(response.getBuildingHeight()) && !response.getBuildingHeight().isEmpty()){
//			mstRadwinDetails.setBuildingHeight(response.getBuildingHeight());
//		}
		return mstRadwinDetails;
	}

	private boolean updateCambiumDetails(SSDumpBean response, String serviceCode, List<String> netpRefIdList, Map<String, Boolean> differAttributeMap, ServiceDetail serviceDetail) {
		logger.info("updateCambiumDetails invoked");
		boolean isCreated=false;
		MstCambiumDetails mstCambiumDetails=mstCambiumDetailsRepository.findFirstByServiceCodeAndIsActiveOrderByIdDesc(serviceCode,"Y");
		if(Objects.nonNull(mstCambiumDetails)){
			logger.info("Cambium exists");
			if(Objects.nonNull(response))
				mstCambiumDetails=constructCambiumDetailsWithSSDump(mstCambiumDetails,response,differAttributeMap);
			else
				mstCambiumDetails=constructCambiumDetailsWithRfData(mstCambiumDetails,serviceDetail);
			mstCambiumDetails.setLastModifiedBy("OPTIMUS_INITIAL");
			mstCambiumDetails.setLastModifiedDate(new Timestamp(new Date().getTime()).toString());
			netpRefIdList.add(mstCambiumDetails.getNetpRefId());
			mstCambiumDetailsRepository.save(mstCambiumDetails);
		}else{
			logger.info("Cambium doesn't exists");
			mstCambiumDetails= new MstCambiumDetails();
			if(Objects.nonNull(response))
				mstCambiumDetails=constructCambiumDetailsWithSSDump(mstCambiumDetails,response,differAttributeMap);
			else
				mstCambiumDetails=constructCambiumDetailsWithRfData(mstCambiumDetails,serviceDetail);
			mstCambiumDetails.setNetpRefId(generateNetpRefId("CAMB"));
			netpRefIdList.add(mstCambiumDetails.getNetpRefId());
			mstCambiumDetails.setServiceCode(serviceCode);
			mstCambiumDetails.setIsActive("Y");
			mstCambiumDetails.setLastModifiedBy("OPTIMUS_INITIAL");
			mstCambiumDetails.setLastModifiedDate(new Timestamp(new Date().getTime()).toString());
			mstCambiumDetailsRepository.save(mstCambiumDetails);
			isCreated=true;
		}
		return isCreated;
	}

	private MstCambiumDetails constructCambiumDetailsWithRfData(MstCambiumDetails mstCambiumDetails, ServiceDetail serviceDetail) {

		logger.info("constructCambiumDetailsWithRfData invoked");

		if(Objects.nonNull(serviceDetail.getOrderDetail().getCustomerName()) && !serviceDetail.getOrderDetail().getCustomerName().isEmpty()){
			mstCambiumDetails.setCustomerName(serviceDetail.getOrderDetail().getCustomerName());
		}

		Map<String, String> componentAttributesMap =commonFulfillmentUtils.getComponentAttributes(
				serviceDetail.getScServiceDetailId(), AttributeConstants.COMPONENT_LM, "A");


		String wirelessType = null;
		if (componentAttributesMap.get("rfMake") != null && ! componentAttributesMap.get("rfMake").isEmpty())
			wirelessType =  componentAttributesMap.get("rfMake");
		else{
			if (componentAttributesMap.get("rfTechnology") != null && ! componentAttributesMap.get("rfTechnology").isEmpty())
				wirelessType =  componentAttributesMap.get("rfTechnology");
		}
		mstCambiumDetails.setDeviceType(wirelessType);

		String mac = null;
		if (componentAttributesMap.get("mac") != null && ! componentAttributesMap.get("mac").isEmpty()){
			mac =  componentAttributesMap.get("mac");
			mstCambiumDetails.setHsuMac(mac.toLowerCase().replace(":", " "));
		}

		if (componentAttributesMap.get("btsIp") != null && ! componentAttributesMap.get("btsIp").isEmpty())
			mstCambiumDetails.setBtsIp(componentAttributesMap.get("btsIp"));

		if (componentAttributesMap.get("btsName") != null && ! componentAttributesMap.get("btsName").isEmpty())
			mstCambiumDetails.setBtsName(componentAttributesMap.get("btsName"));

		if (componentAttributesMap.get("sectorId") != null && ! componentAttributesMap.get("sectorId").isEmpty())
			mstCambiumDetails.setSectorId(componentAttributesMap.get("sectorId"));

		if (componentAttributesMap.get("latLong") != null && ! componentAttributesMap.get("latLong").isEmpty()){
			String[] latlong = componentAttributesMap.get("latLong").split(",");
			mstCambiumDetails.setLatitude(latlong[0]);
			mstCambiumDetails.setLongitude(latlong[1]);
		}

		if (componentAttributesMap.get("destinationState") != null && ! componentAttributesMap.get("destinationState").isEmpty())
			mstCambiumDetails.setState(componentAttributesMap.get("destinationState"));

		StateRegionMapping stateRegionMappin = stageRegionMappingRepository.findByStateLike(serviceDetail.getState());
		if (stateRegionMappin != null)
			mstCambiumDetails.setRegion(stateRegionMappin.getRegion());

		if (componentAttributesMap.get("suIp") != null && ! componentAttributesMap.get("suIp").isEmpty())
			mstCambiumDetails.setHsuIp(componentAttributesMap.get("suIp"));

		if (componentAttributesMap.get("customerRadioFrequency") != null && ! componentAttributesMap.get("customerRadioFrequency").isEmpty())
			mstCambiumDetails.setFrequency(componentAttributesMap.get("customerRadioFrequency"));

//		if(Objects.nonNull(response.getDataVlan()) && !response.getDataVlan().isEmpty()){
//			if(response.getDataVlan().contains(".")){
//				String dataVlan[]=response.getDataVlan().split(".");
//				mstCambiumDetails.setDataVlan(dataVlan[0]);
//			}else{
//				mstCambiumDetails.setDataVlan(response.getDataVlan());
//			}
//		}
//		if(Objects.nonNull(response.getManagementVlan()) && !response.getManagementVlan().isEmpty()){
//			mstCambiumDetails.setMgmtVlan(response.getManagementVlan());
//		}
//		if(Objects.nonNull(response.getSerialNumber()) && !response.getSerialNumber().isEmpty()){
//			mstCambiumDetails.setSerialNumber(response.getSerialNumber());
//		}
		mstCambiumDetails.setPoleHeight(setTowerOrPoleHeight(componentAttributesMap.get("towerHeight"), componentAttributesMap.get("poleHeight")));
		mstCambiumDetails.setAntennaHeight(setAntennaHeight(componentAttributesMap.get("towerHeight"), componentAttributesMap.get("poleHeight"), componentAttributesMap.get("mastHeight")));

//		if(Objects.nonNull(response.getBuildingHeight()) && !response.getBuildingHeight().isEmpty()){
//			mstCambiumDetails.setBuildingHeight(response.getBuildingHeight());
//		}
		return mstCambiumDetails;
	}

	private MstCambiumDetails constructCambiumDetailsWithSSDump(MstCambiumDetails mstCambiumDetails,SSDumpBean response, Map<String, Boolean> differAttributeMap){
		logger.info("constructCambiumDetailsWithSSDump invoked");
		if(Objects.nonNull(response.getCustomerName()) && !response.getCustomerName().isEmpty()){
			mstCambiumDetails.setCustomerName(response.getCustomerName());
		}
		if(Objects.nonNull(response.getVendor()) && !response.getVendor().isEmpty()){
			mstCambiumDetails.setDeviceType(response.getVendor());
		}
		if(Objects.nonNull(response.getHsuMac()) && !response.getHsuMac().isEmpty()){
			if(Objects.nonNull(mstCambiumDetails.getHsuMac()) && !mstCambiumDetails.getHsuMac().isEmpty()) {
				String masterHsuMac=mstCambiumDetails.getHsuMac().replace(":","");
				String responseHsuMac=response.getMac().replace(":","");
				logger.info("Cambium SSDump HsuMac::{} and Master HsuMac::{} exists",responseHsuMac,masterHsuMac);
				if(!responseHsuMac.toLowerCase().equals(masterHsuMac.toLowerCase())){
					differAttributeMap.put("isHsuMacChanged", true);
				}
			}
			mstCambiumDetails.setHsuMac(response.getMac().toLowerCase().replace(":", " "));
		}
		if(Objects.nonNull(response.getBsIp()) && !response.getBsIp().isEmpty()){
			mstCambiumDetails.setBtsIp(response.getBsIp());
		}
		if(Objects.nonNull(response.getBsName()) && !response.getBsName().isEmpty()){
			mstCambiumDetails.setBtsName(response.getBsName());
		}
		if(Objects.nonNull(response.getSectorId()) && !response.getSectorId().isEmpty()){
			mstCambiumDetails.setSectorId(response.getSectorId());
		}
		if(Objects.nonNull(response.getLatitude()) && !response.getLatitude().isEmpty()){
			mstCambiumDetails.setLatitude(response.getLatitude());
		}
		if(Objects.nonNull(response.getLongitude()) && !response.getLongitude().isEmpty()){
			mstCambiumDetails.setLongitude(response.getLongitude());
		}
		if(Objects.nonNull(response.getState()) && !response.getState().isEmpty()){
			mstCambiumDetails.setState(response.getState());
		}
		if(Objects.nonNull(response.getRegion()) && !response.getRegion().isEmpty()){
			mstCambiumDetails.setRegion(response.getRegion());
		}
		if(Objects.nonNull(response.getAntennaHeight()) && !response.getAntennaHeight().isEmpty()){
			mstCambiumDetails.setAntennaHeight(response.getAntennaHeight());
		}
		if(Objects.nonNull(response.getSsIp()) && !response.getSsIp().isEmpty()){
			mstCambiumDetails.setHsuIp(response.getSsIp());
		}
		if(Objects.nonNull(response.getDataVlan()) && !response.getDataVlan().isEmpty()){
			if(response.getDataVlan().contains(".")){
				String dataVlan[]=response.getDataVlan().split(".");
				mstCambiumDetails.setDataVlan(dataVlan[0]);
			}else{
				mstCambiumDetails.setDataVlan(response.getDataVlan());
			}
		}
		if(Objects.nonNull(response.getManagementVlan()) && !response.getManagementVlan().isEmpty()){
			mstCambiumDetails.setMgmtVlan(response.getManagementVlan());
		}
		if(Objects.nonNull(response.getFrequency()) && !response.getFrequency().isEmpty()){
			mstCambiumDetails.setFrequency(response.getFrequency());
		}
		if(Objects.nonNull(response.getSerialNumber()) && !response.getSerialNumber().isEmpty()){
			mstCambiumDetails.setSerialNumber(response.getSerialNumber());
		}
		if(Objects.nonNull(response.getTowerPoleHeight()) && !response.getTowerPoleHeight().isEmpty()){
			mstCambiumDetails.setPoleHeight(response.getTowerPoleHeight());
		}
		if(Objects.nonNull(response.getBuildingHeight()) && !response.getBuildingHeight().isEmpty()){
			mstCambiumDetails.setBuildingHeight(response.getBuildingHeight());
		}
		return mstCambiumDetails;
	}

	private void getP2PDetails(Map<String, Object> taskDataMap, Map<String, String> attributesMap, IpServiceAttributes ipServiceAttributes) {
		logger.info("MASTER P2P Detail doesn't exists");
		attributesMap.put("ProviderSupplier", "RADWIN");
		if(Objects.nonNull(taskDataMap.get("btsConverterIp")) && !taskDataMap.get("btsConverterIp").toString().isEmpty()){
			logger.info("btsConverterIp from SF");
			String btsConverterIp = StringUtils.trimToEmpty((String)taskDataMap.get("btsConverterIp")); 
			attributesMap.put(CramerConstants.CONVERTER_IP, btsConverterIp);
		}
		if(Objects.nonNull(taskDataMap.get("btsConverterRequired")) && !taskDataMap.get("btsConverterRequired").toString().isEmpty()){
			logger.info("btsConverterRequired from SF");
			String btsConverterRequired = StringUtils.trimToEmpty((String)taskDataMap.get("btsConverterRequired")); 
			attributesMap.put("Converter Required", btsConverterRequired);
		}
		if(Objects.nonNull(taskDataMap.get("wirelessScenario")) && !taskDataMap.get("wirelessScenario").toString().isEmpty()) {
			logger.info("BackhaulProvider from SF");
			String providerName = StringUtils.trimToEmpty((String)taskDataMap.get("wirelessScenario"));
			attributesMap.put("BackhaulProvider", providerName);
		}
		
		if(Objects.nonNull(taskDataMap.get("bsSwitchIp")) && !taskDataMap.get("bsSwitchIp").toString().isEmpty()) {
			logger.info("bsSwitchIp from SF");
			String bsSwitchIp = StringUtils.trimToEmpty((String)taskDataMap.get("bsSwitchIp"));
			attributesMap.put("Bussiness Switch IP", bsSwitchIp);
		}
		if(Objects.nonNull(taskDataMap.get("bsSwitchHostName")) && !taskDataMap.get("bsSwitchHostName").toString().isEmpty()) {
			logger.info("bsSwitchHostName from SF");
			String bsSwitchHostName = StringUtils.trimToEmpty((String)taskDataMap.get("bsSwitchHostName"));
			attributesMap.put("Bussiness Switch HostName", bsSwitchHostName);
		}
	}


	private void updateServiceAttr(String ipArrangement, ScServiceAttribute scServiceAttribute, ServiceDetail serviceDetail) {
		ScServiceAttribute serviceAttribute = new ScServiceAttribute();
		serviceAttribute.setAttributeAltValueLabel("IP Address Arrangement for Additional IPs");
		serviceAttribute.setAttributeName("IP Address Arrangement for Additional IPs");
		serviceAttribute.setAttributeValue(ipArrangement);
		serviceAttribute.setCategory("Additional IPs");
		serviceAttribute.setScServiceDetail(scServiceDetailRepository.findByUuidAndMstStatus_code(
				serviceDetail.getServiceId(), TaskStatusConstants.INPROGRESS));
		serviceAttribute.setCreatedBy("MACD_RULE");
		serviceAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
		serviceAttribute.setIsActive("Y");
		serviceAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		serviceAttributeRepository.save(serviceAttribute);
}


	private void formOrderType(String orderCategory, OrderDetails orderDetails,Task task, String orderSubCategory) {
		//String orderCategory=(String)taskDataMap.get(CramerConstants.ORDER_CATEGORY);
		ServiceDetail prevActiveServiceDetail = serviceDetailRepository
				.findFirstByServiceIdAndServiceStateOrderByIdDesc(task.getServiceCode(),TaskStatusConstants.ACTIVE);
		if(orderCategory.equals(CramerConstants.ADD_IP_SERVICE)){
			orderDetails.setOrderType(CramerConstants.ORDER_CATEGORY_MAP.get(orderCategory));
		}else if(orderCategory.equals(CramerConstants.SHIFT_SITE_SERVICE)){
			//TO DO
		}else if(orderCategory.equals(CramerConstants.CHANGE_BANDWIDTH_SERVICE)){
			//formOrderTypeBasedOnBandwidth(prevActiveServiceDetail,taskDataMap,orderDetails);
			if("Hot Upgrade".equalsIgnoreCase(orderSubCategory)){
				orderDetails.setOrderType(orderSubCategory.toUpperCase());
			}else if("Hot Downgrade".equalsIgnoreCase(orderSubCategory)){
				orderDetails.setOrderType(orderSubCategory.toUpperCase());
			}
		}
	}
	
	private void formOrderType(MuxInfoSyncBean muxInfoSyncBean) {
		String orderCategory=muxInfoSyncBean.getOrderType();
		ServiceDetail prevActiveServiceDetail = serviceDetailRepository
				.findFirstByServiceIdAndServiceStateOrderByIdDesc(muxInfoSyncBean.getServiceId(),TaskStatusConstants.ACTIVE);
		if(orderCategory.equals(CramerConstants.CHANGE_BANDWIDTH_SERVICE)){
			formOrderTypeBasedOnBandwidth(prevActiveServiceDetail,muxInfoSyncBean);
		}else if(orderCategory.equals(CramerConstants.ADD_IP_SERVICE)){
			muxInfoSyncBean.setOrderType(CramerConstants.ORDER_CATEGORY_MAP.get(orderCategory));
		}
	}
	
	private void formOrderTypeBasedOnBandwidth(ServiceDetail prevActiveServiceDetail,MuxInfoSyncBean muxInfoSyncBean) {
		String oldBw="0",currentBw="0";
		if(Objects.nonNull(prevActiveServiceDetail.getServiceBandwidth())&&Objects.nonNull(prevActiveServiceDetail.getServiceBandwidthUnit())){
			logger.info("Prev ServiceBw exists");
			oldBw=setBandwidthConversion(String.valueOf(prevActiveServiceDetail.getServiceBandwidth()),prevActiveServiceDetail.getServiceBandwidthUnit());
		}
		if(Objects.nonNull(muxInfoSyncBean.getBandwidthValue())&&Objects.nonNull(muxInfoSyncBean.getBandwidthUnit())){
			logger.info("Current ServiceBw exists");
			currentBw=setBandwidthConversion(String.valueOf(muxInfoSyncBean.getBandwidthValue()),String.valueOf(muxInfoSyncBean.getBandwidthUnit()));
		}
		
		String updatedOrderType=compareBwValues("", oldBw, currentBw);
		if(!updatedOrderType.equals(CommonConstants.EQUAL)){
			muxInfoSyncBean.setOrderType("HOT "+updatedOrderType.toUpperCase());
		}else{
			String oldBurstableBw="0",currentBurstableBw="0";
			if(Objects.nonNull(prevActiveServiceDetail.getBurstableBw())&&Objects.nonNull(prevActiveServiceDetail.getBurstableBwUnit())){
				logger.info("Prev BurstableBw exists");
				oldBurstableBw=setBandwidthConversion(String.valueOf(prevActiveServiceDetail.getBurstableBw()),prevActiveServiceDetail.getBurstableBwUnit());
			}
			if(Objects.nonNull(muxInfoSyncBean.getBurstableBandwidthValue())&&Objects.nonNull(muxInfoSyncBean.getBurstableBandwidthUnit())){
				logger.info("Current BurstableBw exists");
				currentBurstableBw=setBandwidthConversion(muxInfoSyncBean.getBurstableBandwidthValue(),muxInfoSyncBean.getBurstableBandwidthUnit());
			}
			updatedOrderType=compareBwValues("", oldBurstableBw, currentBurstableBw);
			muxInfoSyncBean.setOrderType(updatedOrderType.equals(CommonConstants.EQUAL)?"HOT UPGRADE":"HOT "+updatedOrderType.toUpperCase());
		}
	}

	private void formOrderTypeBasedOnBandwidth(ServiceDetail prevActiveServiceDetail, Map<String, Object> taskDataMap, OrderDetails orderDetails) {
		String oldBw="0",currentBw="0";
		if(Objects.nonNull(prevActiveServiceDetail.getServiceBandwidth())&&Objects.nonNull(prevActiveServiceDetail.getServiceBandwidthUnit())){
			logger.info("Prev ServiceBw exists");
			oldBw=setBandwidthConversion(String.valueOf(prevActiveServiceDetail.getServiceBandwidth()),prevActiveServiceDetail.getServiceBandwidthUnit());
		}
		if(Objects.nonNull(taskDataMap.get(CramerConstants.PORT_BANDWIDTH))&&Objects.nonNull(taskDataMap.get(CramerConstants.BW_UNIT))){
			logger.info("Current ServiceBw exists");
			currentBw=setBandwidthConversion(String.valueOf(taskDataMap.get(CramerConstants.PORT_BANDWIDTH)),String.valueOf(taskDataMap.get(CramerConstants.BW_UNIT)));
		}
		
		String updatedOrderType=compareBwValues("", oldBw, currentBw);
		if(!updatedOrderType.equals(CommonConstants.EQUAL)){
			orderDetails.setOrderType("HOT "+updatedOrderType.toUpperCase());
		}else{
			String oldBurstableBw="0",currentBurstableBw="0";
			if(Objects.nonNull(prevActiveServiceDetail.getBurstableBw())&&Objects.nonNull(prevActiveServiceDetail.getBurstableBwUnit())){
				logger.info("Prev BurstableBw exists");
				oldBurstableBw=setBandwidthConversion(String.valueOf(prevActiveServiceDetail.getBurstableBw()),prevActiveServiceDetail.getBurstableBwUnit());
			}
			if(Objects.nonNull(taskDataMap.get(CramerConstants.Burstable_Bandwidth))&&Objects.nonNull(taskDataMap.get(CramerConstants.Burstable_Bandwidth_Unit))){
				logger.info("Current BurstableBw exists");
				currentBurstableBw=setBandwidthConversion(String.valueOf(taskDataMap.get(CramerConstants.Burstable_Bandwidth)),String.valueOf(taskDataMap.get(CramerConstants.Burstable_Bandwidth_Unit)));
			}
			updatedOrderType=compareBwValues("", oldBurstableBw, currentBurstableBw);
			orderDetails.setOrderType(updatedOrderType.equals(CommonConstants.EQUAL)?"HOT UPGRADE":"HOT "+updatedOrderType.toUpperCase());
		}
	}


	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public Response createClr(String cramerInfoBeanAsString) throws TclCommonException {
		logger.info("createCLRByTaskId task id Request {} ", cramerInfoBeanAsString);
		CramerInfoBean cramerInfoBean = Utils.convertJsonToObject(cramerInfoBeanAsString, CramerInfoBean.class);
		return createCLRByTaskId(cramerInfoBean);
	}
	
	/**
	 * Cramer assign dummy wan ip synchronous SOAP call
	 *
	 * @param ipServiceInputJsonAsString
	 * @return
	 * @throws TclCommonException
	 */
	public Response assignDummyWanIp(String assignIpServiceInputJsonAsString) throws TclCommonException {
		logger.info("assignDummyWanIp {} ", assignIpServiceInputJsonAsString);
        Response response = new Response();
		IPServiceSyncBean ipServiceSyncBean = (IPServiceSyncBean) Utils
				.convertJsonToObject(assignIpServiceInputJsonAsString, IPServiceSyncBean.class);
		AssignDummyWANIP assignDummyWANIP = new AssignDummyWANIP();
		CramerServiceHeader header = new CramerServiceHeader();
		header.setAuthUser(ipServiceSyncBean.getCramerServiceHeader().getAuthUser());
		header.setClientSystemIP(ipServiceSyncBean.getCramerServiceHeader().getClientSystemIP());
		//header.setOriginatingSystem(ipServiceSyncBean.getCramerServiceHeader().getOriginatingSystem());
		header.setOriginatingSystem(cramerSourceSystem);
		header.setOriginationTime(ipServiceSyncBean.getCramerServiceHeader().getOriginationTime());
		header.setRequestID(ipServiceSyncBean.getCramerServiceHeader().getRequestID());
		assignDummyWANIP.setHeader(header);
		assignDummyWANIP.setServiceID(ipServiceSyncBean.getServiceID());
		assignDummyWANIP.setVPNID(ipServiceSyncBean.getVpnID());
		String ipServiceUrl = "";
        if(ipServiceSyncBean.getServiceType().equalsIgnoreCase("ILL") || ipServiceSyncBean.getServiceType().equalsIgnoreCase("IAS") )
            ipServiceUrl = ipAssignDummyServiceIllUrl;
        else if(ipServiceSyncBean.getServiceType().equalsIgnoreCase("GVPN"))
            ipServiceUrl=ipAssignDummyServiceGvpnUrl;
        
        SoapRequest soapRequest = new SoapRequest();
		NetworkInventory networkInventory = new NetworkInventory();
		try {
			networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
			networkInventory.setServiceCode(StringUtils.trimToEmpty(ipServiceSyncBean.getServiceID()));
			if(Objects.nonNull(ipServiceSyncBean.getCramerServiceHeader())&& Objects.nonNull(ipServiceSyncBean.getCramerServiceHeader().getRequestID()))
				networkInventory.setRequestId(StringUtils.trimToEmpty(ipServiceSyncBean.getCramerServiceHeader().getRequestID()));
			networkInventory.setRequest(Utils.convertObjectToXmlString(assignDummyWANIP, AssignDummyWANIP.class));
			networkInventory.setType(CramerConstants.GET_ASSIGN_DUMMY_IP_SYNC);
			soapRequest.setUrl(ipServiceUrl);
			soapRequest.setRequestObject(assignDummyWANIP);
			soapRequest.setContextPath("com.tcl.dias.serviceactivation.cramer.ipservicesync.beans");
			soapRequest.setSoapUserName("");
			soapRequest.setSoapPwd("");
			soapRequest.setConnectionTimeout(60000);
			soapRequest.setReadTimeout(60000);
			JAXBElement jaxBResponse = genericWebserviceClient.doSoapCallForObject(soapRequest, JAXBElement.class);
			AssignDummyWANIPResponse assignDummyIPServiceInfoResponse = (AssignDummyWANIPResponse) jaxBResponse.getValue();
			logger.info("assignDummyWanIp Sync Response {} ", assignDummyIPServiceInfoResponse);
			if (assignDummyIPServiceInfoResponse != null && assignDummyIPServiceInfoResponse.getReturn() != null
					&& assignDummyIPServiceInfoResponse.getReturn().getAck() != null) {
				logger.info("assignDummyWanIp Sync RequestID=> {}, RequestReceivedTime=> {}, ServiceID=> {} ",
						assignDummyIPServiceInfoResponse.getReturn().getAck().getRequestID(),
						assignDummyIPServiceInfoResponse.getReturn().getAck().getRequestRecievedDateTime(),
						assignDummyIPServiceInfoResponse.getReturn().getAck().getServiceID());
                response.setStatus(true);
                networkInventory.setResponse(Utils.convertObjectToXmlString(assignDummyIPServiceInfoResponse,AssignDummyWANIPResponse.class));
				networkInventoryRepository.save(networkInventory);
			}
		} catch (SoapFaultClientException ex) {
			logger.error("SoapFaultClientException in Cramer assignDummyWanIp Service  : {}", ex);
			logger.error("getFaultCode {} , getFaultString {}", ex.getSoapFault().getFaultCode());
			SoapFaultDetail soapFaultDetail = ex.getSoapFault().getFaultDetail();
			if (soapFaultDetail != null) {
				SoapFaultDetailElement detailElementChild = soapFaultDetail.getDetailEntries().next();
				Source detailSource = detailElementChild.getSource();
				Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
				marshaller.setContextPath(soapRequest.getContextPath());
				JAXBElement<AssignDummyWANIPFault> source = (JAXBElement<AssignDummyWANIPFault>) marshaller
						.unmarshal(detailSource);
				logger.error("ErrorCode:: {} , ErrorLongDescription:: {},ErrorShortDescription:: {}",
						source.getValue().getErrorCode(), source.getValue().getErrorLongDescription(),
						source.getValue().getErrorShortDescription());
				networkInventory.setResponse(source.getValue().getErrorCode() +" : "+ source.getValue().getErrorLongDescription());
				response.setErrorCode(source.getValue().getErrorCode());
				response.setErrorMessage(source.getValue().getErrorLongDescription());
			}else {
				networkInventory.setResponse(ex.getFaultStringOrReason());
				response.setErrorCode(String.valueOf(ex.getSoapFault()));
				response.setErrorMessage(ex.getFaultStringOrReason());
			}
			 response.setStatus(false);
			
			networkInventoryRepository.save(networkInventory);
			return response;
		} catch (Exception e) {
			logger.error("Exception in Cramer assignDummyWanIp Service ", e);
			response.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
			response.setErrorMessage(e.getMessage());
			networkInventory.setResponse(e.getMessage());
			response.setStatus(false);
			networkInventoryRepository.save(networkInventory);
			return response;
		}
		return response;
	
	}
	
	/**
	 * Cramer release dummy wan ip synchronous SOAP call
	 *
	 * @param ipServiceInputJsonAsString
	 * @return
	 * @throws TclCommonException
	 */
	public Response releaseDummyWanIp(String releaseIpServiceInputJsonAsString) throws TclCommonException {
		logger.info("releaseDummyWanIp {} ", releaseIpServiceInputJsonAsString);
        Response response = new Response();
		IPServiceSyncBean ipServiceSyncBean = (IPServiceSyncBean) Utils
				.convertJsonToObject(releaseIpServiceInputJsonAsString, IPServiceSyncBean.class);
		ReleaseDummyWANIP releaseDummyWANIP = new ReleaseDummyWANIP();
		CramerServiceHeader header = new CramerServiceHeader();
		header.setAuthUser(ipServiceSyncBean.getCramerServiceHeader().getAuthUser());
		header.setClientSystemIP(ipServiceSyncBean.getCramerServiceHeader().getClientSystemIP());
		//header.setOriginatingSystem(ipServiceSyncBean.getCramerServiceHeader().getOriginatingSystem());
		header.setOriginatingSystem(cramerSourceSystem);
		header.setOriginationTime(ipServiceSyncBean.getCramerServiceHeader().getOriginationTime());
		header.setRequestID(ipServiceSyncBean.getCramerServiceHeader().getRequestID());
		releaseDummyWANIP.setHeader(header);
		releaseDummyWANIP.setServiceID(ipServiceSyncBean.getServiceID());
		releaseDummyWANIP.setVPNID(ipServiceSyncBean.getVpnID());
		ServiceDetail serviceDetail=serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByIdDesc(ipServiceSyncBean.getServiceID(), "ISSUED");
		if(Objects.nonNull(serviceDetail)){
			IpDummyDetail ipDummyDetail=ipDummyDetailRepository.findFirstByServiceDetail_IdOrderByIdDesc(serviceDetail.getId());
			if(Objects.nonNull(ipDummyDetail)){
				logger.info("Set WAN IP from DB");
				releaseDummyWANIP.setWANIPAddress(ipDummyDetail.getDummyWANIpAddressSubnet());
			}
		}else{
			logger.info("Set WAN IP from Request");
			releaseDummyWANIP.setWANIPAddress(ipServiceSyncBean.getWanIPAddress());
		}
		String ipServiceUrl = "";
        if(ipServiceSyncBean.getServiceType().equalsIgnoreCase("ILL") || ipServiceSyncBean.getServiceType().equalsIgnoreCase("IAS") )
            ipServiceUrl = ipReleaseDummyServiceIllUrl;
        else if(ipServiceSyncBean.getServiceType().equalsIgnoreCase("GVPN"))
            ipServiceUrl=ipReleaseDummyServiceGvpnUrl;
        
        SoapRequest soapRequest = new SoapRequest();
		NetworkInventory networkInventory = new NetworkInventory();
		try {
			networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
			networkInventory.setServiceCode(StringUtils.trimToEmpty(ipServiceSyncBean.getServiceID()));
			if(Objects.nonNull(ipServiceSyncBean.getCramerServiceHeader())&& Objects.nonNull(ipServiceSyncBean.getCramerServiceHeader().getRequestID()))
				networkInventory.setRequestId(StringUtils.trimToEmpty(ipServiceSyncBean.getCramerServiceHeader().getRequestID()));
			networkInventory.setRequest(Utils.convertObjectToXmlString(releaseDummyWANIP, ReleaseDummyWANIP.class));
			networkInventory.setType(CramerConstants.GET_RELEASE_DUMMY_IP_SYNC);
			soapRequest.setUrl(ipServiceUrl);
			soapRequest.setRequestObject(releaseDummyWANIP);
			soapRequest.setContextPath("com.tcl.dias.serviceactivation.cramer.ipservicesync.beans");
			soapRequest.setSoapUserName("");
			soapRequest.setSoapPwd("");
			soapRequest.setConnectionTimeout(60000);
			soapRequest.setReadTimeout(60000);
			JAXBElement jaxBResponse = genericWebserviceClient.doSoapCallForObject(soapRequest, JAXBElement.class);
			ReleaseDummyWANIPResponse releaseDummyIPServiceInfoResponse = (ReleaseDummyWANIPResponse) jaxBResponse.getValue();
			logger.info("releaseDummyWanIp Sync Response {} ", releaseDummyIPServiceInfoResponse);
			if (releaseDummyIPServiceInfoResponse != null && releaseDummyIPServiceInfoResponse.getReturn() != null
					&& releaseDummyIPServiceInfoResponse.getReturn().getAck() != null) {
				logger.info("releaseDummyWanIp Sync RequestID=> {}, RequestReceivedTime=> {}, ServiceID=> {} ",
						releaseDummyIPServiceInfoResponse.getReturn().getAck().getRequestID(),
						releaseDummyIPServiceInfoResponse.getReturn().getAck().getRequestRecievedDateTime(),
						releaseDummyIPServiceInfoResponse.getReturn().getAck().getServiceID());
                response.setStatus(true);
                networkInventory.setResponse(Utils.convertObjectToXmlString(releaseDummyIPServiceInfoResponse,ReleaseDummyWANIPResponse.class));
				networkInventoryRepository.save(networkInventory);
			}
		} catch (SoapFaultClientException ex) {
			logger.error("SoapFaultClientException in Cramer releaseDummyWanIp Service  : {}", ex);
			logger.error("getFaultCode {} , getFaultString {}", ex.getSoapFault().getFaultCode());
			SoapFaultDetail soapFaultDetail = ex.getSoapFault().getFaultDetail();
			if (soapFaultDetail != null) {
				SoapFaultDetailElement detailElementChild = soapFaultDetail.getDetailEntries().next();
				Source detailSource = detailElementChild.getSource();
				Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
				marshaller.setContextPath(soapRequest.getContextPath());
				JAXBElement<ReleaseDummyWANIPFault> source = (JAXBElement<ReleaseDummyWANIPFault>) marshaller
						.unmarshal(detailSource);
				logger.error("ErrorCode:: {} , ErrorLongDescription:: {},ErrorShortDescription:: {}",
						source.getValue().getErrorCode(), source.getValue().getErrorLongDescription(),
						source.getValue().getErrorShortDescription());
				networkInventory.setResponse(source.getValue().getErrorCode() +" : "+ source.getValue().getErrorLongDescription());
				response.setErrorCode(source.getValue().getErrorCode());
				response.setErrorMessage(source.getValue().getErrorLongDescription());
			}else {
				networkInventory.setResponse(ex.getFaultStringOrReason());
				response.setErrorCode(String.valueOf(ex.getSoapFault()));
				response.setErrorMessage(ex.getFaultStringOrReason());
			}
			 response.setStatus(false);
		
			networkInventoryRepository.save(networkInventory);
			return response;
		} catch (Exception e) {
			logger.error("Exception in Cramer releaseDummyWanIp Service ", e);
			response.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
			response.setErrorMessage(e.getMessage());
			networkInventory.setResponse(e.getMessage());
			response.setStatus(false);
			networkInventoryRepository.save(networkInventory);
			return response;
		}
		return response;
	
	}


	/**
	 * getMuxInfo - Cramer Mux info synchronous SOAP call.
	 *
	 * @param muxInfoSyncBeanAsJsonString
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public Response getMuxInfo(String muxInfoSyncBeanAsJsonString) throws TclCommonException {
        Response response = new Response();
        NetworkInventory networkInventory = new NetworkInventory();
		try {
			
			Utils.callGc();
			
			logger.info("getMuxInfo Request {} ", muxInfoSyncBeanAsJsonString);
			MuxInfoSyncBean muxInfoSyncBean = (MuxInfoSyncBean) Utils
					.convertJsonToObject(muxInfoSyncBeanAsJsonString, MuxInfoSyncBean.class);
			GetMuxDetails getMuxDetails = new GetMuxDetails();
			getMuxDetails.setServiceID(muxInfoSyncBean.getServiceId());
			getMuxDetails.setRequestID(muxInfoSyncBean.getRequestId());
			getMuxDetails.setCOPFID(muxInfoSyncBean.getCopfId());
			String serviceType = StringUtils.trimToEmpty(muxInfoSyncBean.getServiceType());
			logger.info("getMuxInfo-ServiceId={} serviceType={} ", muxInfoSyncBean.getServiceId(),serviceType);
			/*if(!"NPL".equalsIgnoreCase(serviceType) && CramerConstants.CHANGE.equals(muxInfoSyncBean.getRequestType())){
				formOrderType(muxInfoSyncBean);
			}*/
			getMuxDetails.setOrderType(muxInfoSyncBean.getOrderType());
			getMuxDetails.setRequestType(muxInfoSyncBean.getRequestType());
			//getMuxDetails.setRequestingSystem(muxInfoSyncBean.getRequestingSystem());
			getMuxDetails.setRequestingSystem(cramerSourceSystem);

			HandOffDetail handOffDetail = new HandOffDetail();
			handOffDetail.setBandwidthUnit(muxInfoSyncBean.getBandwidthUnit());
			handOffDetail.setBandwidthValue(muxInfoSyncBean.getBandwidthValue());
			handOffDetail.setFeasibilityId(muxInfoSyncBean.getFeasibilityId());
			handOffDetail.setMuxIP(muxInfoSyncBean.getMuxIp());
			handOffDetail.setMuxName(muxInfoSyncBean.getMuxName());
			handOffDetail.setPortType(muxInfoSyncBean.getPortType());

			getMuxDetails.setAEndHandOffDtls(handOffDetail);
			HandOffDetail zHandOffDetail = new HandOffDetail();
			getMuxDetails.setZEndHandOffDtls(zHandOffDetail);

			// Save request & response
			networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
			networkInventory.setServiceCode(muxInfoSyncBean.getServiceId());
			networkInventory.setRequestId(muxInfoSyncBean.getRequestId());
			networkInventory.setRequest(Utils.convertObjectToXmlString(getMuxDetails, GetMuxDetails.class));
			networkInventory.setType(CramerConstants.GET_MUX_INFO_SYNC);
			SoapRequest soapRequest = new SoapRequest();
			// soapRequest.setUrl("http://uswv1vuap003a.vsnl.co.in:8080/Ace_E2E/GetMuxDetail.cnms");
			soapRequest.setUrl(getMuxInfoUrl);
			soapRequest.setRequestObject(getMuxDetails);
			soapRequest.setContextPath("com.tcl.dias.serviceactivation.cramer.muxsync");
			soapRequest.setSoapUserName("");
			soapRequest.setSoapPwd("");
			soapRequest.setConnectionTimeout(60000);
			soapRequest.setReadTimeout(60000);

			try {
				JAXBElement jaxBResponse = genericWebserviceClient.doSoapCallForObject(soapRequest, JAXBElement.class);
				GetMuxDetailsResponse getMuxDetailsResponse = (GetMuxDetailsResponse) jaxBResponse.getValue();
				logger.info("getMuxInfo sync Response {} ", getMuxDetailsResponse);
				if (getMuxDetailsResponse != null && getMuxDetailsResponse.getMuxDetailResponse().getAck() != null) {
					networkInventory.setResponse(
							Utils.convertObjectToXmlString(getMuxDetailsResponse, GetMuxDetailsResponse.class));
					networkInventoryRepository.save(networkInventory);
					logger.info("getMuxInfo sync acknowledgement RequestID=> {}",
							getMuxDetailsResponse.getMuxDetailResponse().getAck().isStatus());
					//return Boolean.toString(getMuxDetailsResponse.getMuxDetailResponse().getAck().isStatus());
                    response.setStatus(getMuxDetailsResponse.getMuxDetailResponse().getAck().isStatus());
				}
			} catch (SoapFaultClientException ex) {
				logger.error("SoapFaultClientException in Cramer getMuxInfo Service ", ex);
				logger.error("getFaultCode {} , getFaultString {}", ex.getSoapFault().getFaultCode());
				SoapFaultDetail soapFaultDetail = ex.getSoapFault().getFaultDetail();
				if (soapFaultDetail != null) {
					SoapFaultDetailElement detailElementChild = soapFaultDetail.getDetailEntries().next();
					Source detailSource = detailElementChild.getSource();
					Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
					marshaller.setContextPath(soapRequest.getContextPath());
					JAXBElement<com.tcl.dias.serviceactivation.cramer.muxsync.ServiceFault> source = (JAXBElement<com.tcl.dias.serviceactivation.cramer.muxsync.ServiceFault>) marshaller
							.unmarshal(detailSource);
					logger.error("ErrorCode:: {} , ErrorLongDescription:: {},ErrorShortDescription:: {}",
							source.getValue().getErrorCode(), source.getValue().getErrorLongDescription(),
							source.getValue().getErrorShortDescription());
					networkInventory.setResponse(source.getValue().getErrorCode() +" : "+ source.getValue().getErrorLongDescription());
					response.setErrorCode(source.getValue().getErrorCode());
					response.setErrorMessage(source.getValue().getErrorLongDescription());
				}else{
					networkInventory.setResponse(ex.getFaultStringOrReason());
					response.setErrorMessage(ex.getFaultStringOrReason());
					response.setErrorCode(String.valueOf(ex.getSoapFault()));
				}
				networkInventoryRepository.save(networkInventory);
				
				return response;
			}
			return response;
		} catch (Exception e) {
			networkInventory.setResponse(e.getMessage());
			networkInventoryRepository.save(networkInventory);
			logger.error("Exception in Cramer getMuxInfo ", e);
			response.setErrorMessage(e.getMessage());
			return response;
		}
	}


	/**
	 * Cramer setCLR synchronous SOAP call
	 *
	 * @param setCLRInputJsonAsString
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public Response setCLR(String setCLRInputJsonAsString) throws TclCommonException {
		logger.info("setCLR Sync Request {} ", setCLRInputJsonAsString);
		Response response = new Response();
		SoapRequest soapRequest = new SoapRequest();
		NetworkInventory networkInventory = new NetworkInventory();
		try {
			SetCLRSyncBean setCLRSyncBean =
					(SetCLRSyncBean) Utils.convertJsonToObject(setCLRInputJsonAsString, SetCLRSyncBean.class);
			
			SetCLR setCLRRequest = new SetCLR();
			CramerServiceHeader header = new
					CramerServiceHeader();
			header.setAuthUser(setCLRSyncBean.getCramerServiceHeader().getAuthUser());
			header.setClientSystemIP(setCLRSyncBean.getCramerServiceHeader().getClientSystemIP());
			//header.setOriginatingSystem(setCLRSyncBean.getCramerServiceHeader().getOriginatingSystem());
			header.setOriginatingSystem(cramerSourceSystem);
			header.setOriginationTime(setCLRSyncBean.getCramerServiceHeader().getOriginationTime());
			header.setRequestID(setCLRSyncBean.getCramerServiceHeader().getRequestID());
			setCLRRequest.setHeader(header);
	
			// Must be any of these [ SERVICE,CIRCUIT,IOR]
			setCLRRequest.setObjectType(ObjectType.valueOf(setCLRSyncBean.getObjectType()));
			setCLRRequest.setObjectName(setCLRSyncBean.getObjectName()); // Must be any of these [ACTIVE,ISSUED,MARKEDFORDELETE]
			setCLRRequest.setInitialRelationship(InitialRelationShip.valueOf(
					setCLRSyncBean.getInitialRelationship())); // Must be any of these [ACTIVE,ISSUED,MARKEDFORDELETE,TERMINATE]
			setCLRRequest.setFinalRelationship(FinalRelationShip.valueOf(setCLRSyncBean.
					getFinalRelationship()));
		
			networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
			networkInventory.setServiceCode(StringUtils.trimToEmpty(setCLRSyncBean.getObjectName()));
			if (Objects.nonNull(setCLRSyncBean.getCramerServiceHeader()) && Objects.nonNull(setCLRSyncBean.getCramerServiceHeader().getRequestID()))
				networkInventory.setRequestId(StringUtils.trimToEmpty(setCLRSyncBean.getCramerServiceHeader().getRequestID()));
			networkInventory.setRequest(Utils.convertObjectToXmlString(setCLRRequest, SetCLR.class));
			networkInventory.setType(CramerConstants.SET_CLR_SYNC);
			soapRequest.setUrl(setClrUrl);
			soapRequest.setRequestObject(setCLRRequest);
			soapRequest.setContextPath(
					"com.tcl.dias.serviceactivation.cramer.ipservicesync.beans");
			soapRequest.setSoapUserName("");
			soapRequest.setSoapPwd("");
			soapRequest.setConnectionTimeout(60000);
			soapRequest.setReadTimeout(60000);
			
			SetCLRResponseClr getIPServiceInfoResponse  = (SetCLRResponseClr)
					genericWebserviceClient.doSoapCallForObject(soapRequest, SetCLRResponseClr.class);
			try {
				logger.info("setCLR Response {} ", getIPServiceInfoResponse);
				if (getIPServiceInfoResponse != null &&
						getIPServiceInfoResponse!= null &&
						getIPServiceInfoResponse.getResult().getAck() != null) {
					logger.info("setCLR RequestID=> {}, RequestReceivedTime=> {}, ServiceID=> {} "
							, getIPServiceInfoResponse.getResult().getAck().getRequestID(),
							getIPServiceInfoResponse.getResult().getAck().getRequestRecievedDateTime(),
							getIPServiceInfoResponse.getResult().getAck().getServiceID());
					networkInventory.setResponse(Utils.convertObjectToXmlString(getIPServiceInfoResponse, SetCLRResponseClr.class));
					response.setStatus(true);
					networkInventoryRepository.save(networkInventory);
				}
			} catch	(Exception e) {
				logger.error("Exception in Cramer setClr Service ", e);
			}
			
			
		} catch (SoapFaultClientException ex) {
			logger.error("SoapFaultClientException in Cramer setCLR Service {}", ex);
			logger.error("getFaultCode {} , getFaultString {}", ex.getSoapFault().getFaultCode());
			SoapFaultDetail soapFaultDetail = ex.getSoapFault().getFaultDetail();
			if (soapFaultDetail != null) {
				SoapFaultDetailElement detailElementChild = soapFaultDetail.getDetailEntries().next();
				Source detailSource = detailElementChild.getSource();
				Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
				marshaller.setContextPath(soapRequest.getContextPath());
				JAXBElement<SetCLRFault> source = (JAXBElement<SetCLRFault>)marshaller.unmarshal(detailSource);
				logger.error("ErrorCode:: {} , ErrorLongDescription:: {},ErrorShortDescription:: {}"
								, source.getValue().getErrorCode(),
								source.getValue().getErrorLongDescription(),
								source.getValue().getErrorShortDescription());
				networkInventory.setResponse(source.getValue().getErrorCode() + " : " + source.getValue().getErrorLongDescription());
				response.setErrorCode(source.getValue().getErrorCode());
				response.setErrorMessage(source.getValue().getErrorLongDescription());
			} else {
				networkInventory.setResponse(ex.getFaultStringOrReason());
				response.setErrorCode(String.valueOf(ex.getSoapFault()));
				response.setErrorMessage(ex.getFaultStringOrReason());
			}
			response.setStatus(false);
			
			networkInventoryRepository.save(networkInventory);
		} catch	(Exception e) {
			logger.error("Exception in Cramer setClr Service {}", e);
			response.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
			response.setErrorMessage(e.getMessage());
			networkInventory.setResponse(e.getMessage());
			response.setStatus(false);
			networkInventoryRepository.save(networkInventory);
			return response;
		}
		return response;
	}
	
	@Transactional(readOnly = false)
	public void setActivationServiceActive(Integer scServiceId,String serviceCode){
		logger.info("setActivationServiceActive serviceCode={} scServiceId={}",serviceCode,scServiceId);
		try {
			ServiceDetail serviceDetail = serviceDetailRepository
					.findFirstByScServiceDetailIdAndServiceStateOrderByIdDesc(scServiceId,
							"ISSUED");
			if (serviceDetail != null) {
				logger.info("ServiceDetail issue record exists::{}", serviceDetail.getServiceId());
				ServiceDetail activeServiceDetail = serviceDetailRepository
						.findFirstByServiceIdAndServiceStateOrderByIdDesc(serviceCode, "ACTIVE");
				if (Objects.nonNull(activeServiceDetail)) {
					logger.info("ServiceDetail active record exists::{}", activeServiceDetail.getServiceId());
					activeServiceDetail.setServiceState("INACTIVE");
					activeServiceDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
					serviceDetailRepository.saveAndFlush(activeServiceDetail);
					if (Objects.nonNull(activeServiceDetail.getOrderDetail())
							&& Objects.nonNull(activeServiceDetail.getOrderDetail().getOrderStatus())
							&& "IN PROGRESS".equalsIgnoreCase(activeServiceDetail.getOrderDetail().getOrderStatus())) {
						logger.info("Setting active order to Close");
						OrderDetail orderDetail = activeServiceDetail.getOrderDetail();
						orderDetail.setOrderStatus("CLOSED");
						orderDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
						orderDetailRepository.saveAndFlush(orderDetail);
					}
					if (Objects.nonNull(activeServiceDetail.getLmComponents())
							&& !activeServiceDetail.getLmComponents().isEmpty()) {
						logger.info("LmComponent exists for active record");
						activeServiceDetail.getLmComponents().stream().forEach(lmComp -> {
							logger.info("LmComponent setting end date for Active Service Detail");
							lmComp.setEndDate(new Timestamp(new Date().getTime()));
							lmComponentRepository.saveAndFlush(lmComp);
						});
					}
				}
				serviceDetail.setServiceState("ACTIVE");
				serviceDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
				serviceDetailRepository.saveAndFlush(serviceDetail);
				logger.info("Updated ISSUED to ACTIVE");			
			}
		}catch(Exception e) {
			logger.error("setActivationServiceActive-Exception serviceCode={} scServiceId={} error={}",serviceCode,scServiceId,e.getMessage());
			logger.error("Exception in Cramer setActivationServiceActive", e);
		}
	}
	
	@Transactional(readOnly = false)
	public void setServiceActive(Integer scServiceId,String serviceCode, String orderCode){
		logger.info("setServiceActive serviceCode={} scServiceId={}",serviceCode,scServiceId);
		try {
			Integer activeServiceId = scServiceDetailRepository.findByUuidAndStatus(serviceCode, 5);
			logger.info("Changing SC INPROGRESS TO ACTIVE::{}", scServiceId);
			updateServiceStatusAndCreatedNewStatus(scServiceId, "ACTIVE");
			scServiceDetailRepository.updateActiveDeliveredStatus(5, "ACTIVE",
					new Timestamp(System.currentTimeMillis()), scServiceId);
			scServiceDetailRepository.flush();
			if (Objects.nonNull(activeServiceId)) {
				logger.info("Changing SC ACTIVE TO INACTIVE::{}", activeServiceId);
				/*
				 * MstStatus mstStatus =
				 * mstStatusRepository.findByCode("INACTIVE");
				 * activeScServiceDetail.setMstStatus(mstStatus);
				 * activeScServiceDetail.setUpdatedDate(new Timestamp(new
				 * Date().getTime()));
				 * logger.info("Save scServiceDetail as INACTIVE");
				 */
				updateServiceStatusAndCreatedNewStatus(activeServiceId, "INACTIVE");
				scServiceDetailRepository.updateStatus(6, activeServiceId);
				scServiceDetailRepository.flush();
			}
			if(orderCode!=null){
				logger.info("Order Code exists::{}",orderCode);
				if(orderCode.toLowerCase().contains("ucwb") || orderCode.toLowerCase().contains("ucdr")){
					logger.info("Webex Order Code size::{}",orderCode);
					List<Integer> serviceIds=scSolutionComponentRepository.findByOrderCodeAndIsActiveAndNotInServiceId(orderCode,"Y",scServiceId);
					if(serviceIds!=null && !serviceIds.isEmpty()){
						logger.info("Webex Service List size::{}",serviceIds.size());
						List<ScServiceDetail> scServiceDetailList=scServiceDetailRepository.findByIdInAndMstStatus_code(serviceIds,"ACTIVE");
						if(scServiceDetailList!=null && !scServiceDetailList.isEmpty() && scServiceDetailList.size()==serviceIds.size()){
							logger.info("Webex Update Solution Service Id as Overlay condition matched for serviceId::{}",scServiceId);
							ScServiceDetail scSolutionServiceDetail = scServiceDetailRepository.findFirstByScOrderUuidAndErfPrdCatalogProductNameOrderByIdDesc(orderCode,"WEBEX_SOLUTION");
							if(scSolutionServiceDetail!=null){
								Integer scSolutionServiceId=scSolutionServiceDetail.getId();
								logger.info("Webex Solution Service Id::{}",scSolutionServiceId);
								scServiceDetailRepository.updateActiveDeliveredStatus(5, "ACTIVE",
										new Timestamp(System.currentTimeMillis()), scSolutionServiceId);
								scServiceDetailRepository.flush();
							}
						}
					}
				}else if(orderCode.toLowerCase().contains("izosdwan")){
					logger.info("OrderCode::{},serviceCode={} scServiceId={}",orderCode,serviceCode,scServiceId);
					ScSolutionComponent isOverlaySolutionComponent=scSolutionComponentRepository.findByScServiceDetail1_idAndOrderCodeAndIsActive(scServiceId,orderCode,"Y");
					List<Integer> overlayServiceIds=null;
					if(isOverlaySolutionComponent!=null && isOverlaySolutionComponent.getComponentGroup().equalsIgnoreCase("OVERLAY")){
						logger.info("Overlay Service Id::{}",scServiceId);
						overlayServiceIds=scSolutionComponentRepository.findByOrderCodeAndComponentGroupAndIsActiveAndNotInServiceId(orderCode, "OVERLAY", "Y",scServiceId);
					}else{
						logger.info("Other than Overlay Service Id::{}",scServiceId);
						overlayServiceIds=scSolutionComponentRepository.getServiceDetailIdByOrderCodeAndComponentGroupAndIsActive(orderCode, "OVERLAY", "Y");
					}
					if(overlayServiceIds!=null && !overlayServiceIds.isEmpty()){
						logger.info("Overlay Service List size::{}",overlayServiceIds.size());
						List<ScServiceDetail> scServiceDetailList=scServiceDetailRepository.findByIdInAndMstStatus_code(overlayServiceIds,"ACTIVE");
						if(scServiceDetailList!=null && !scServiceDetailList.isEmpty() && scServiceDetailList.size()==overlayServiceIds.size()){
							logger.info("Update Solution Service Id as Overlay condition matched for serviceId::{}",scServiceId);
							updateSolutionServiceIdStatus(orderCode,"IZOSDWAN_SOLUTION");
						}
					}else {
						logger.info("Last Overlay Service Id::{}",scServiceId);
						List<ScServiceDetail> scServiceDetailList=scServiceDetailRepository.findByIdAndMstStatus_code(scServiceId,"ACTIVE");
						if(scServiceDetailList!=null && !scServiceDetailList.isEmpty()){
							logger.info("Update Solution Service Id as Overlay condition matched for serviceId::{}",scServiceId);
							updateSolutionServiceIdStatus(orderCode,"IZOSDWAN_SOLUTION");
						}
					}
				}else if(orderCode.toLowerCase().startsWith("ias") || orderCode.toLowerCase().startsWith("gvpn")
						  || orderCode.toLowerCase().startsWith("npl")){
					logger.info("OrderCode::{},serviceCode={} scServiceId={}",orderCode,serviceCode,scServiceId);
					Optional<ScServiceDetail> scServiceDetailOptional=scServiceDetailRepository.findById(scServiceId);
					if(scServiceDetailOptional.isPresent()) {
						logger.info("ScServiceDetail exists::{}",scServiceId);
						ScServiceDetail scServiceDetail=scServiceDetailOptional.get();
						if("RENEWALS".equalsIgnoreCase(scServiceDetail.getOrderType())) {
							logger.info("Renewal ServiceId::{}",scServiceId);
							List<String> serviceCodeList = new ArrayList<String>();
							serviceCodeList.add(orderCode);
							serviceCodeList.add(serviceCode);
							List<ScServiceDetail> scServiceDetailList=scServiceDetailRepository.getServiceDetailsByUuidAndScOrderUuidAndUuids(orderCode,serviceCodeList);
							if(scServiceDetailList==null || scServiceDetailList.isEmpty()){
								logger.info("Update Renewal Base Service Code::{} to Active",orderCode);
								Integer renewalBaseServiceId=scServiceDetailRepository.findByUuidAndStatus(orderCode,4);
								logger.info("Renewal Base ServiceId::{} to Active",renewalBaseServiceId);
								updateServiceStatusAndCreatedNewStatus(renewalBaseServiceId, "ACTIVE");
								scServiceDetailRepository.updateActiveDeliveredStatus(5, "ACTIVE",
										new Timestamp(System.currentTimeMillis()), renewalBaseServiceId);
								scServiceDetailRepository.flush();
							}
						}
					}
				}
			}
		}catch(Exception e) {
			logger.error("setServiceActive-Exception serviceCode={} scServiceId={} error={}",serviceCode,scServiceId,e.getMessage());
			logger.error("Exception in Cramer setServiceActive", e);
		}
	}
	
	
	@Transactional(readOnly = false)
	public void setRenewalServiceActive(Integer scServiceId,String serviceCode, String orderCode){
		logger.info("setRenewalServiceActive orderCode={} serviceCode={} scServiceId={}",orderCode,serviceCode,scServiceId);
		try {
			List<Integer> renewalServiceIds = scServiceDetailRepository.getIdByScOrderUuid(orderCode);
			for(Integer renewalServiceId:renewalServiceIds) {
				logger.info("Changing SC INPROGRESS TO ACTIVE::{}", scServiceId);
				updateServiceStatusAndCreatedNewStatus(renewalServiceId, "ACTIVE");
				scServiceDetailRepository.updateActiveDeliveredStatus(5, "ACTIVE",
						new Timestamp(System.currentTimeMillis()), renewalServiceId);
				scServiceDetailRepository.flush();
			}
		}catch(Exception e) {
			logger.error("setRenewalServiceActive-Exception serviceCode={} scServiceId={} error={}",serviceCode,scServiceId,e.getMessage());
			logger.error("Exception in Cramer setRenewalServiceActive", e);
		}
	}
	
	private void updateSolutionServiceIdStatus(String orderCode, String solutionType) {
		logger.info("updateSolutionServiceIdStatus method invoked for ordercode::{}",orderCode);
		ScServiceDetail scSolutionServiceDetail = scServiceDetailRepository
				.findFirstByScOrderUuidAndErfPrdCatalogProductNameOrderByIdDesc(orderCode, solutionType);
		if (scSolutionServiceDetail != null) {
			Integer scSolutionServiceId = scSolutionServiceDetail.getId();
			logger.info("Sdwan Solution Service Id::{}", scSolutionServiceId);
			scServiceDetailRepository.updateActiveDeliveredStatus(5, "ACTIVE",
					new Timestamp(System.currentTimeMillis()), scSolutionServiceId);
			scServiceDetailRepository.flush();
		}
	}

	@Transactional(readOnly = false)
	public void generateBillStartDate(ScServiceDetail scServiceDetail,String commissioningDate, String uuid,Integer serviceId) throws TclCommonException{
		 logger.info("generateBillStartDate invoked");
		Map<String, String> atMap = new HashMap<>();
		
		if(scServiceDetail==null){
			scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
		}
		
		Date commDate = new Date(); 
		try {
			if(commissioningDate!=null) commDate = new SimpleDateFormat("yyyy-MM-dd").parse(commissioningDate);
		}catch(Exception ee) {
			logger.error("commissioningDateException {}",ee);
		}
		
		atMap.put("commissioningDate",DateUtil.convertDateToString(commDate));
		LocalDateTime commissioningDateLD = LocalDateTime.ofInstant(commDate.toInstant(), ZoneId.systemDefault());
		ScComponentAttribute billFreePeriodComponet = scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetail.getId(),"billFreePeriod","LM","A");
		LocalDateTime terminationDate =commissioningDateLD.minusDays(1);
		String terminationDateStr ="";
		if(billFreePeriodComponet!=null) {
			int billFreePeriod= 0;
			try {
				if(StringUtils.isNotBlank(billFreePeriodComponet.getAttributeValue()))billFreePeriod=Integer.parseInt(billFreePeriodComponet.getAttributeValue());	
			}catch(Exception ee) {						
			}
			atMap.put("billStartDate", DateUtil.convertDateToString(Timestamp.valueOf(commissioningDateLD.plusDays(billFreePeriod))));


			terminationDate = commissioningDateLD.plusDays(billFreePeriod).minusDays(1);
		}else {
			atMap.put("billStartDate",DateUtil.convertDateToString(Timestamp.valueOf(commissioningDateLD)));
		}
		
		String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder());

		if("MACD".equalsIgnoreCase(orderType) && Objects.nonNull(scServiceDetail.getOrderSubCategory()) 
				&& scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel")){			
			terminationDateStr = DateUtil.convertDateToString(Timestamp.valueOf(terminationDate));	
			ScServiceAttribute scServiceDownTimeAttr = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(), "downtime_duration");
			ScServiceAttribute scServiceDownTimeIndAttr = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(), "downtime_needed_ind");
			logger.info("parallel days scServiceDownTimeAttr={},scServiceDownTimeIndAttr={}",scServiceDownTimeAttr,scServiceDownTimeIndAttr);
			
			if (Objects.nonNull(scServiceDownTimeAttr) && Objects.nonNull(scServiceDownTimeIndAttr) 
					&& !scServiceDownTimeAttr.getAttributeValue().isEmpty()
						&& "yes".equalsIgnoreCase(scServiceDownTimeIndAttr.getAttributeValue())) {				
				try {
					Integer parallelDays =Integer.parseInt(scServiceDownTimeAttr.getAttributeValue());
					logger.info("parallel days ={}",parallelDays);
					if(parallelDays>0)terminationDate = terminationDate.plusDays((parallelDays+1));
				}catch(Exception ee) {
					logger.error("terminationDate Exception {}",ee);
				}
				terminationDateStr=DateUtil.convertDateToString(Timestamp.valueOf(terminationDate));	
			}else{
				terminationDateStr=DateUtil.convertDateToString(Timestamp.valueOf(terminationDate));				
			}
			atMap.put("terminationDate",terminationDateStr);
			logger.info("serviceCode={}, terminationDateStr ={}",scServiceDetail.getUuid(),terminationDateStr);
		}
		if(scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("IZOPC") && scServiceDetail.getPrimarySecondary().equalsIgnoreCase("Secondary")) {
			logger.info("IZOPC Sy Service Id:{}",scServiceDetail.getId());
			ScServiceAttribute cloudProviderAttribute=scServiceAttributeRepository.findByScServiceDetail_idAndAttributeName(scServiceDetail.getId(), "Cloud Provider");
			if(cloudProviderAttribute!=null && cloudProviderAttribute.getAttributeValue()!=null && cloudProviderAttribute.getAttributeValue().equalsIgnoreCase("IBM Direct Link")) {
				logger.info("IZOPC Sy Service Id for IBM Direct Link:{}",scServiceDetail.getId());
				atMap.put("billStartDate", null);
			}
		}
		if(scServiceDetail.getScOrder().getDemoFlag()!=null && scServiceDetail.getScOrder().getDemoFlag().equalsIgnoreCase("Y")) {
			logger.info("Demo Order Id={}, Service Id ={}",scServiceDetail.getScOrder().getId(),scServiceDetail.getId());
			ScOrderAttribute billingTypeAttribute=scOrderAttributeRepository.findByScOrder_IdAndAttributeName(scServiceDetail.getScOrder().getId(), "Billing Type");
			if(billingTypeAttribute!=null && billingTypeAttribute.getAttributeValue()!=null && 
					billingTypeAttribute.getAttributeValue().equalsIgnoreCase("Non-billable demo")) {
				logger.info("Non Billable Demo Order with Service Id ={}",scServiceDetail.getId());
				atMap.put("billStartDate",null);
			}
		}
		updateServiceStatus(scServiceDetail,atMap.get("billStartDate"),atMap.get("commissioningDate"));
		componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap, AttributeConstants.COMPONENT_LM,"A");
	}
	 
	private void updateServiceStatus(ScServiceDetail scServiceDetail, String billstartDate, String commissionedDate) {
		try {

			if (scServiceDetail != null) {
				scServiceDetail.setActivationConfigStatus(TaskStatusConstants.ACTIVE);
				scServiceDetail.setActivationConfigDate(new Timestamp(System.currentTimeMillis()));
				
				if(scServiceDetail.getServiceConfigDate()==null) {
					scServiceDetail.setServiceConfigStatus(TaskStatusConstants.ACTIVE);
					scServiceDetail.setServiceConfigDate(new Timestamp(System.currentTimeMillis()));
				}
				scServiceDetail
						.setBillStartDate(billstartDate!=null?new Timestamp(DateUtil.convertStringToDateYYMMDD(billstartDate).getTime()):null);
				scServiceDetail.setCommissionedDate(
						new Timestamp(DateUtil.convertStringToDateYYMMDD(commissionedDate).getTime()));
				scServiceDetailRepository.save(scServiceDetail);
			}

		} catch (Exception e) {
			logger.error("error in updating service config status with  error:{}", e);
		}
	}
	
	
	/**
	 * Cramer setCLR synchronous SOAP call
	 *
	 * @param setCLRInputJsonAsString
	 * @return
	 * @throws TclCommonException
	 */
	public Response setMFDCLR(String setCLRInputJsonAsString) throws TclCommonException {
		logger.info("setMFDCLR Sync Request {} ", setCLRInputJsonAsString);
		Response response = new Response();
		SetCLRSyncBean setCLRSyncBean =
				(SetCLRSyncBean) Utils.convertJsonToObject(setCLRInputJsonAsString, SetCLRSyncBean.class);
		NetworkInventory networkInventory = new NetworkInventory();
		SetCLR setCLRRequest = new SetCLR();
		CramerServiceHeader header = new
				CramerServiceHeader();
		header.setAuthUser(setCLRSyncBean.getCramerServiceHeader().getAuthUser());
		header.setClientSystemIP(setCLRSyncBean.getCramerServiceHeader().getClientSystemIP());
		//header.setOriginatingSystem(setCLRSyncBean.getCramerServiceHeader().getOriginatingSystem());
		header.setOriginatingSystem(cramerSourceSystem);
		header.setOriginationTime(setCLRSyncBean.getCramerServiceHeader().getOriginationTime());
		header.setRequestID(setCLRSyncBean.getCramerServiceHeader().getRequestID());
		setCLRRequest.setHeader(header);

		// Must be any of these [ SERVICE,CIRCUIT,IOR]
		setCLRRequest.setObjectType(ObjectType.valueOf(setCLRSyncBean.getObjectType()));
		setCLRRequest.setObjectName(setCLRSyncBean.getObjectName()); // Must be any of these [ACTIVE,ISSUED,MARKEDFORDELETE]
		setCLRRequest.setInitialRelationship(InitialRelationShip.valueOf(
				setCLRSyncBean.getInitialRelationship())); // Must be any of these [ACTIVE,ISSUED,MARKEDFORDELETE,TERMINATE]
		setCLRRequest.setFinalRelationship(FinalRelationShip.valueOf(setCLRSyncBean.
				getFinalRelationship()));
		SoapRequest soapRequest = new SoapRequest();
		try {
			networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
			networkInventory.setServiceCode(StringUtils.trimToEmpty(setCLRSyncBean.getObjectName()));
			if (Objects.nonNull(setCLRSyncBean.getCramerServiceHeader()) && Objects.nonNull(setCLRSyncBean.getCramerServiceHeader().getRequestID()))
				networkInventory.setRequestId(StringUtils.trimToEmpty(setCLRSyncBean.getCramerServiceHeader().getRequestID()));
			networkInventory.setRequest(Utils.convertObjectToXmlString(setCLRRequest, SetCLR.class));
			networkInventory.setType(CramerConstants.SET_MFD_CLR_SYNC);
			soapRequest.setUrl(setClrUrl);
			soapRequest.setRequestObject(setCLRRequest);
			soapRequest.setContextPath(
					"com.tcl.dias.serviceactivation.cramer.ipservicesync.beans");
			soapRequest.setSoapUserName("");
			soapRequest.setSoapPwd("");
			soapRequest.setConnectionTimeout(60000);
			soapRequest.setReadTimeout(60000);

			SetCLRResponseClr getIPServiceInfoResponse  = (SetCLRResponseClr)
					genericWebserviceClient.doSoapCallForObject(soapRequest, SetCLRResponseClr.class);
			try {
				logger.info("ipService MFD Sync Response {} ", getIPServiceInfoResponse);
				if (getIPServiceInfoResponse != null &&
						getIPServiceInfoResponse!= null &&
						getIPServiceInfoResponse.getResult().getAck() != null) {
					logger.info("ipService MFD Sync RequestID=> {}, RequestReceivedTime=> {}, ServiceID=> {} "
							, getIPServiceInfoResponse.getResult().getAck().getRequestID(),
							getIPServiceInfoResponse.getResult().getAck().getRequestRecievedDateTime(),
							getIPServiceInfoResponse.getResult().getAck().getServiceID());
					networkInventory.setResponse(Utils.convertObjectToXmlString(getIPServiceInfoResponse, SetCLRResponseClr.class));
					response.setStatus(true);
					networkInventoryRepository.save(networkInventory);
				}
			} catch	(Exception e) {
				logger.error("Exception in Cramer setMFDCLR Service ", e);
			}
		} catch (SoapFaultClientException ex) {
			logger.error("SoapFaultClientException in Cramer setMFDCLR Service ", ex);
			logger.error("getFaultCode {} , getFaultString {}", ex.getSoapFault().getFaultCode());
			SoapFaultDetail soapFaultDetail = ex.getSoapFault().getFaultDetail();
			if (soapFaultDetail != null) {
				SoapFaultDetailElement detailElementChild = soapFaultDetail.getDetailEntries().next();
				Source detailSource = detailElementChild.getSource();
				Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
				marshaller.setContextPath(soapRequest.getContextPath());
				JAXBElement<SetCLRFault> source = (JAXBElement<SetCLRFault>)marshaller.unmarshal(detailSource);
				logger.error("ErrorCode:: {} , ErrorLongDescription:: {},ErrorShortDescription:: {}"
								, source.getValue().getErrorCode(),
								source.getValue().getErrorLongDescription(),
								source.getValue().getErrorShortDescription());
				networkInventory.setResponse(source.getValue().getErrorCode() + " : " + source.getValue().getErrorLongDescription());
				response.setErrorCode(source.getValue().getErrorCode());
				response.setErrorMessage(source.getValue().getErrorLongDescription());
			} else {
				networkInventory.setResponse(ex.getFaultStringOrReason());
				response.setErrorCode(String.valueOf(ex.getSoapFault()));
				response.setErrorMessage(ex.getFaultStringOrReason());
			}
			response.setStatus(false);
			
			networkInventoryRepository.save(networkInventory);
		} catch	(Exception e) {
			logger.error("Exception in Cramer setMFDClr Service ", e);
			response.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
			response.setErrorMessage(e.getMessage());
			networkInventory.setResponse(e.getMessage());
			response.setStatus(false);
			networkInventoryRepository.save(networkInventory);
			return response;
		}
		return response;
	}

	/**
	 * Cramer ipService synchronous SOAP call
	 *
	 * @param ipServiceInputJsonAsString
	 * @return
	 * @throws TclCommonException
	 */
	public Response ipService(String ipServiceInputJsonAsString) throws TclCommonException, JAXBException {
		logger.info("ipService Sync Request {} ", ipServiceInputJsonAsString);
        Response response = new Response();
		IPServiceSyncBean ipServiceSyncBean = (IPServiceSyncBean) Utils
				.convertJsonToObject(ipServiceInputJsonAsString, IPServiceSyncBean.class);

		GetIPServiceInfo ipServiceReq = new GetIPServiceInfo();
		CramerServiceHeader header = new CramerServiceHeader();
		header.setAuthUser(ipServiceSyncBean.getCramerServiceHeader().getAuthUser());
		header.setClientSystemIP(ipServiceSyncBean.getCramerServiceHeader().getClientSystemIP());
		//header.setOriginatingSystem(ipServiceSyncBean.getCramerServiceHeader().getOriginatingSystem());
		header.setOriginatingSystem(cramerSourceSystem);
		header.setOriginationTime(ipServiceSyncBean.getCramerServiceHeader().getOriginationTime());
		header.setRequestID(ipServiceSyncBean.getCramerServiceHeader().getRequestID());
		ipServiceReq.setHeader(header);

		ipServiceReq.setServiceID(ipServiceSyncBean.getServiceID());
		ipServiceReq.setServiceType(ipServiceSyncBean.getServiceType());
		ipServiceReq.setVPNID(ipServiceSyncBean.getVpnID());
		ipServiceReq.setRelationshipType(ipServiceSyncBean.getRelationshipType());

		String ipServiceUrl = "";
        if(ipServiceSyncBean.getServiceType().equalsIgnoreCase("ILL") || ipServiceSyncBean.getServiceType().equalsIgnoreCase("IAS") )
            ipServiceUrl = ipServiceIllUrl;
        else if(ipServiceSyncBean.getServiceType().equalsIgnoreCase("GVPN"))
            ipServiceUrl=ipServiceGvpnUrl;

        SoapRequest soapRequest = new SoapRequest();
		NetworkInventory networkInventory = new NetworkInventory();
		try {
			networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
			networkInventory.setServiceCode(StringUtils.trimToEmpty(ipServiceSyncBean.getServiceID()));
			if(Objects.nonNull(ipServiceSyncBean.getCramerServiceHeader())&& Objects.nonNull(ipServiceSyncBean.getCramerServiceHeader().getRequestID()))
				networkInventory.setRequestId(StringUtils.trimToEmpty(ipServiceSyncBean.getCramerServiceHeader().getRequestID()));
			networkInventory.setRequest(Utils.convertObjectToXmlString(ipServiceReq, GetIPServiceInfo.class));
			networkInventory.setType(CramerConstants.GET_IP_SYNC);
			soapRequest.setUrl(ipServiceUrl);
			soapRequest.setRequestObject(ipServiceReq);
			soapRequest.setContextPath("com.tcl.dias.serviceactivation.cramer.ipservicesync.beans");
			soapRequest.setSoapUserName("");
			soapRequest.setSoapPwd("");
			soapRequest.setConnectionTimeout(60000);
			soapRequest.setReadTimeout(60000);
			JAXBElement jaxBResponse = genericWebserviceClient.doSoapCallForObject(soapRequest, JAXBElement.class);
			GetIPServiceInfoResponse getIPServiceInfoResponse = (GetIPServiceInfoResponse) jaxBResponse.getValue();
			logger.info("ipService Sync Response {} ", getIPServiceInfoResponse);
			if (getIPServiceInfoResponse != null && getIPServiceInfoResponse.getReturn() != null
					&& getIPServiceInfoResponse.getReturn().getAck() != null) {
				logger.info("ipService Sync RequestID=> {}, RequestReceivedTime=> {}, ServiceID=> {} ",
						getIPServiceInfoResponse.getReturn().getAck().getRequestID(),
						getIPServiceInfoResponse.getReturn().getAck().getRequestRecievedDateTime(),
						getIPServiceInfoResponse.getReturn().getAck().getServiceID());
				//return "true";
                response.setStatus(true);
                networkInventory.setResponse(Utils.convertObjectToXmlString(getIPServiceInfoResponse,GetIPServiceInfoResponse.class));
				networkInventoryRepository.save(networkInventory);
			}
		} catch (SoapFaultClientException ex) {
			logger.error("SoapFaultClientException in Cramer ipService Service  : {}", ex);
			logger.error("getFaultCode {} , getFaultString {}", ex.getSoapFault().getFaultCode());
			SoapFaultDetail soapFaultDetail = ex.getSoapFault().getFaultDetail();
			if (soapFaultDetail != null) {
				SoapFaultDetailElement detailElementChild = soapFaultDetail.getDetailEntries().next();
				Source detailSource = detailElementChild.getSource();
				Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
				marshaller.setContextPath(soapRequest.getContextPath());
				JAXBElement<GetIPServiceInfoFault> source = (JAXBElement<GetIPServiceInfoFault>) marshaller
						.unmarshal(detailSource);
				logger.error("ErrorCode:: {} , ErrorLongDescription:: {},ErrorShortDescription:: {}",
						source.getValue().getErrorCode(), source.getValue().getErrorLongDescription(),
						source.getValue().getErrorShortDescription());
				networkInventory.setResponse(source.getValue().getErrorCode() +" : "+ source.getValue().getErrorLongDescription());
				response.setStatus(false);
				response.setErrorCode(source.getValue().getErrorCode());
				response.setErrorMessage(source.getValue().getErrorLongDescription());
			}else {
				networkInventory.setResponse(ex.getFaultStringOrReason());
				response.setStatus(false);
				response.setErrorCode(String.valueOf(ex.getSoapFault()));
				response.setErrorMessage(ex.getFaultStringOrReason());
			}
			networkInventoryRepository.save(networkInventory);
			return response;
		} catch (Exception e) {
			logger.error("Exception in Cramer ipService Service ", e);
			response.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
			response.setErrorMessage(e.getMessage());
			networkInventory.setResponse(e.getMessage());
			response.setStatus(false);
			networkInventoryRepository.save(networkInventory);
			return response;
		}
		return response;
	}

	/**
	 * Cramer getCLRSync SOAP call
	 *
	 * @param getCLRSyncBeanAsJsonString
	 * @return
	 * @throws TclCommonException
	 */
	public Response getCLRSync(String getCLRSyncBeanAsJsonString) {
		logger.info("getCLRSync Request {} ", getCLRSyncBeanAsJsonString);
        Response response = new Response();
        SoapRequest soapRequest = new SoapRequest();
		NetworkInventory networkInventory = new NetworkInventory();
		try {
			GetCLRSyncBean getCLRSyncBean = (GetCLRSyncBean) Utils
					.convertJsonToObject(getCLRSyncBeanAsJsonString, GetCLRSyncBean.class);
			GetCLR getCLRRequest = new GetCLR();
			CramerServiceHeader header = new CramerServiceHeader();
			header.setAuthUser(getCLRSyncBean.getCramerServiceHeader().getAuthUser());
			header.setClientSystemIP(getCLRSyncBean.getCramerServiceHeader().getClientSystemIP());
			//header.setOriginatingSystem(getCLRSyncBean.getCramerServiceHeader().getOriginatingSystem());
			header.setOriginatingSystem(cramerSourceSystem);
			header.setOriginationTime(getCLRSyncBean.getCramerServiceHeader().getOriginationTime());
			header.setRequestID(getCLRSyncBean.getCramerServiceHeader().getRequestID());
			getCLRRequest.setHeader(header);
			getCLRRequest.setObjectName(getCLRSyncBean.getObjectName());
			getCLRRequest.setObjectType(ObjectType.valueOf(getCLRSyncBean.getObjectType()));
			// [ACTIVE,ISSUED, MARKEDFORDELETE]
			getCLRRequest.setRelationshipType(RelationShip.valueOf(getCLRSyncBean.getRelationshipType()));

			networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
			networkInventory.setServiceCode(StringUtils.trimToEmpty(getCLRSyncBean.getObjectName()));

			if(Objects.nonNull(getCLRRequest.getHeader())&& Objects.nonNull(getCLRRequest.getHeader().getRequestID()))
				networkInventory.setRequestId(StringUtils.trimToEmpty(getCLRRequest.getHeader().getRequestID()));
			networkInventory.setRequest(Utils.convertObjectToXmlString(getCLRRequest, GetCLR.class));
			networkInventory.setType(CramerConstants.GET_CLR_SYNC);

			soapRequest.setUrl(getClrSyncUrl);
			soapRequest.setRequestObject(getCLRRequest);
			soapRequest.setContextPath("com.tcl.dias.serviceactivation.cramer.getclrsync.beans");
			soapRequest.setSoapUserName("");
			soapRequest.setSoapPwd("");
			soapRequest.setConnectionTimeout(60000);
			soapRequest.setReadTimeout(60000);

			JAXBElement jaxBResponse = genericWebserviceClient.doSoapCallForObject(soapRequest,JAXBElement.class);
			GetCLRResponse getCLRResponse = (GetCLRResponse)jaxBResponse.getValue();

			logger.info("getCLRSync Response {} ", getCLRResponse);
			if (getCLRResponse != null && getCLRResponse.getReturn() != null
					&& getCLRResponse.getReturn().getAck() != null) {
				logger.info("getCLRSync Sync RequestID=> {}, RequestReceivedTime=> {}, ServiceID=> {} ",
						getCLRResponse.getReturn().getAck().getRequestID(),
						getCLRResponse.getReturn().getAck().getRequestRecievedDateTime(),
						getCLRResponse.getReturn().getAck().getObjectName());
				//return "true";
                response.setStatus(Boolean.TRUE);
			}
		} catch (SoapFaultClientException ex) {
			logger.error("SoapFaultClientException Exception in Cramer getCLRSync ", ex);
			logger.error("getFaultCode {} , getFaultString {}", ex.getSoapFault().getFaultCode());
			SoapFaultDetail soapFaultDetail = ex.getSoapFault().getFaultDetail();
			if (soapFaultDetail != null) {
				SoapFaultDetailElement detailElementChild = soapFaultDetail.getDetailEntries().next();
				Source detailSource = detailElementChild.getSource();
				Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
				marshaller.setContextPath(soapRequest.getContextPath());
				JAXBElement<AsyncCLRInfoFault> source = (JAXBElement<AsyncCLRInfoFault>) marshaller
						.unmarshal(detailSource);
				logger.error("ErrorCode:: {} , ErrorLongDescription:: {},ErrorShortDescription:: {}",
						source.getValue().getErrorCode(), source.getValue().getErrorLongDescription(),
						source.getValue().getErrorShortDescription());
				
				response.setErrorCode(source.getValue().getErrorCode());
				response.setErrorMessage(source.getValue().getErrorLongDescription());
			}else {
				response.setErrorCode(String.valueOf(ex.getFaultCode()));
				response.setErrorMessage(ex.getFaultStringOrReason());
			}
			return response;
		} catch (Exception e) {
			logger.error("Exception in Cramer getCLRSync ", e);
			response.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
			response.setErrorMessage(e.getMessage());
			return response;
		}
		return response;
	}

	/**
	 * Cramer isValidBTS synchronous SOAP call
	 *
	 * @param validBtsJsonInput
	 * @return
	 * @throws TclCommonException
	 */
	public Response isValidBTSSync(String validBtsJsonInput) throws TclCommonException, JAXBException {
		logger.info("isValidBTSSync Request {} ", validBtsJsonInput);

		String errorCode="";
		String errorMessage="";
        Response response = new Response();
		IsValidBtsSyncBean isValidBtsSyncBean = (IsValidBtsSyncBean) Utils
				.convertJsonToObject(validBtsJsonInput, IsValidBtsSyncBean.class);

		IsValidBTS isValidBTS = new IsValidBTS();
		BtsValidationRequest btsValidationRequest = new BtsValidationRequest();
		btsValidationRequest.setBTSIP(StringUtils.trimToEmpty(isValidBtsSyncBean.getBtsIP()));
		btsValidationRequest.setCOPFId(StringUtils.trimToEmpty(isValidBtsSyncBean.getCopfId()));
		btsValidationRequest.setProvider(StringUtils.trimToEmpty(isValidBtsSyncBean.getProvider()));
		btsValidationRequest.setRequestId(StringUtils.trimToEmpty(isValidBtsSyncBean.getRequestId()));
		btsValidationRequest.setScenarioType(StringUtils.trimToEmpty(isValidBtsSyncBean.getScenarioType()));
		btsValidationRequest.setSectorId(StringUtils.trimToEmpty(isValidBtsSyncBean.getSectorId()));
		btsValidationRequest.setServiceId(StringUtils.trimToEmpty(isValidBtsSyncBean.getServiceId()));
		isValidBTS.setRequestAttr(btsValidationRequest);

		NetworkInventory networkInventory = new NetworkInventory();
		SoapRequest soapRequest = new SoapRequest();
		try {
			// Save request & response
			networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
			networkInventory.setServiceCode(StringUtils.trimToEmpty(isValidBtsSyncBean.getServiceId()));
			networkInventory.setRequestId(StringUtils.trimToEmpty(isValidBtsSyncBean.getRequestId()));
			networkInventory.setRequest(Utils.convertObjectToXmlString(isValidBTS, IsValidBTS.class));
			networkInventory.setType(CramerConstants.IS_VALID_BTS);
			soapRequest.setUrl(isValidBtsSyncUrl);
			soapRequest.setRequestObject(isValidBTS);
			soapRequest.setContextPath("com.tcl.dias.serviceactivation.cramer.isvalidbts.beans");
			soapRequest.setSoapUserName("");
			soapRequest.setSoapPwd("");
			soapRequest.setConnectionTimeout(60000);
			soapRequest.setReadTimeout(60000);
			JAXBElement jaxBResponse = genericWebserviceClient.doSoapCallForObject(soapRequest, JAXBElement.class);
			IsValidBTSResponse isValidBTSResponse = (IsValidBTSResponse) jaxBResponse.getValue();

			if (Objects.nonNull(isValidBTSResponse) && Objects.nonNull(isValidBTSResponse.getValidationOutput())) {
				networkInventory
						.setResponse(Utils.convertObjectToXmlString(isValidBTSResponse, IsValidBTSResponse.class));
				networkInventoryRepository.save(networkInventory);
//				return Boolean.toString(isValidBTSResponse.getValidationOutput().isStatus());
                response.setStatus(isValidBTSResponse.getValidationOutput().isStatus());
			}

		} catch (SoapFaultClientException ex) {
			logger.error("SoapFaultClientException in Cramer createService Service ", ex);
			logger.error("getFaultCode {} , getFaultString {}", ex.getSoapFault().getFaultCode());
			SoapFaultDetail soapFaultDetail = ex.getSoapFault().getFaultDetail();
			if (soapFaultDetail != null) {
				SoapFaultDetailElement detailElementChild = soapFaultDetail.getDetailEntries().next();
				Source detailSource = detailElementChild.getSource();
				Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
				marshaller.setContextPath(soapRequest.getContextPath());
				JAXBElement<com.tcl.dias.serviceactivation.cramer.isvalidbts.beans.ServiceFault> source = (JAXBElement<com.tcl.dias.serviceactivation.cramer.isvalidbts.beans.ServiceFault>) marshaller
						.unmarshal(detailSource);
				logger.error("ErrorCode:: {} , ErrorLongDescription:: {},ErrorShortDescription:: {}",
						source.getValue().getErrorCode(), source.getValue().getErrorLongDescription(),
						source.getValue().getErrorShortDescription());
				//networkInventory.setResponse(source.getValue().getErrorLongDescription());
				errorCode = source.getValue().getErrorCode();
				errorMessage = source.getValue().getErrorLongDescription();
				networkInventory.setResponse(source.getValue().getErrorCode() +" : "+ source.getValue().getErrorLongDescription());
			} else {
				errorCode = String.valueOf(ex.getFaultCode());
				errorMessage = ex.getFaultStringOrReason();
				networkInventory.setResponse(ex.getFaultStringOrReason());
			}
			networkInventoryRepository.save(networkInventory);
			response.setStatus(Boolean.FALSE);
			response.setErrorCode(errorCode);
			response.setErrorMessage(errorMessage);
			return response;
		} catch (Exception e) {
			logger.error("Exception in Cramer isValidBTSSync ", e);
			networkInventory.setResponse(e.getMessage());
			networkInventoryRepository.save(networkInventory);
			response.setStatus(Boolean.FALSE);
			response.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
			response.setErrorMessage(e.getMessage());
			return response;
		}
		return response;
	}

	/**
	 * Cramer checkClrInfoSync synchronous SOAP call
	 *
	 * @param checkClrInfoJsonInput
	 * @return
	 * @throws TclCommonException
	 */
	public Response checkClrInfoSync(String checkClrInfoJsonInput) throws TclCommonException, JAXBException {
		logger.info("checkClrInfoSync Request {} ", checkClrInfoJsonInput);
        Response response = new Response();
		CramerInfoBean cramerInfoBean = Utils
				.convertJsonToObject(checkClrInfoJsonInput, CramerInfoBean.class);

		CheckClrInfo checkClrInfo = new ObjectFactory().createCheckClrInfo();
		checkClrInfo.setObjectType(com.tcl.dias.serviceactivation.cramer.checkclrinfo.beans.ObjectType.SERVICE);
		checkClrInfo.setObjectName(StringUtils.trimToEmpty(cramerInfoBean.getServiceCode()));
		checkClrInfo.setRelationshipType(com.tcl.dias.serviceactivation.cramer.checkclrinfo.beans.RelationShip.ISSUED);

		checkClrInfo.setRequestId(StringUtils.trimToEmpty(cramerInfoBean.getProcessInstanceId()));
		//checkClrInfo.setRequestingSystem(CramerConstants.OPTIMUS);
		checkClrInfo.setRequestingSystem(cramerSourceSystem);

		 JAXBElement<CheckClrInfo> checkClrInfoElement = new ObjectFactory().createCheckClrInfo(checkClrInfo);

		NetworkInventory networkInventory = new NetworkInventory();
		SoapRequest soapRequest = new SoapRequest();
		try {
			// Save request & response
			networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
			networkInventory.setServiceCode(StringUtils.trimToEmpty(cramerInfoBean.getServiceCode()));
			networkInventory.setRequestId(StringUtils.trimToEmpty(cramerInfoBean.getProcessInstanceId()));
			networkInventory.setRequest(Utils.convertObjectToXmlString(checkClrInfo, CheckClrInfo.class));
			networkInventory.setType(CramerConstants.CHECK_CLR_INFO);
			soapRequest.setUrl(checkClrInfoSyncUrl);
			soapRequest.setRequestObject(checkClrInfoElement);
			soapRequest.setContextPath("com.tcl.dias.serviceactivation.cramer.checkclrinfo.beans");
			soapRequest.setSoapUserName("");
			soapRequest.setSoapPwd("");
			soapRequest.setConnectionTimeout(60000);
			soapRequest.setReadTimeout(60000);
			JAXBElement jaxBResponse = genericWebserviceClient.doSoapCallForObject(soapRequest, JAXBElement.class);
			CheckClrInfoResponse checkClrInfoResponse = (CheckClrInfoResponse) jaxBResponse.getValue();

			logger.info("checkClrInfoResponse={}",checkClrInfoResponse);

			if (checkClrInfoResponse !=null &&  checkClrInfoResponse.getResponse() !=null) {
				networkInventory.setResponse(Utils.convertObjectToXmlString(checkClrInfoResponse, CheckClrInfoResponse.class));
				networkInventoryRepository.save(networkInventory);
				if(checkClrInfoResponse.getResponse().getServiceContents() !=null) {
				    response.setStatus(Boolean.TRUE);
				    response.setData(checkClrInfoResponse.getResponse().getServiceContents().toString());
				}else {
                    response.setStatus(Boolean.FALSE);
                    if(checkClrInfoResponse.getResponse().getErrorDetails() !=null) {
	                    response.setErrorCode(checkClrInfoResponse.getResponse().getErrorDetails().getErrorCode());
	                    response.setErrorMessage(checkClrInfoResponse.getResponse().getErrorDetails().getErrorLongDescription());
                    }
                }
			} else {
				networkInventory.setResponse(null);
				networkInventoryRepository.save(networkInventory);
				response.setStatus(Boolean.FALSE);
				response.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
			}
		} catch (SoapFaultClientException ex) {
			logger.error("SoapFaultClientException in Cramer checkClrInfoSync Service ", ex);
			logger.error("getFaultCode {} , getFaultString {}", ex.getSoapFault().getFaultCode());
			SoapFaultDetail soapFaultDetail = ex.getSoapFault().getFaultDetail();
			if (soapFaultDetail != null) {
				SoapFaultDetailElement detailElementChild = soapFaultDetail.getDetailEntries().next();
				Source detailSource = detailElementChild.getSource();
				Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
				marshaller.setContextPath(soapRequest.getContextPath());
				JAXBElement<ServiceFault> source = (JAXBElement) marshaller.unmarshal(detailSource);
				networkInventory.setResponse(source.getValue().getErrorCode() +" : "+ source.getValue().getErrorLongDescription());
				
				response.setErrorCode(source.getValue().getErrorCode());
				response.setErrorMessage(source.getValue().getErrorLongDescription());
			}else {
				response.setErrorCode(String.valueOf(ex.getFaultCode()));
				response.setErrorMessage(ex.getFaultStringOrReason());
				networkInventory.setResponse(ex.getFaultStringOrReason());
			}
			
			networkInventoryRepository.save(networkInventory);
			response.setStatus(Boolean.FALSE);
			
			return response;
		} catch (Exception e) {
			logger.error("Exception in Cramer checkClrInfoSync ", e);
			networkInventory.setResponse(e.getMessage());
			networkInventoryRepository.save(networkInventory);
			response.setStatus(Boolean.FALSE);
			response.setErrorMessage(e.getMessage());
			response.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
			return response;
		}

		return response;
	}

	/**
	 * Cramer checkIpClrSync synchronous SOAP call
	 *
	 * @param checkIpClrSyncJsonInput
	 * @return
	 * @throws TclCommonException
	 */
	public String checkIpClrSync(String checkIpClrSyncJsonInput) throws TclCommonException, JAXBException {
		logger.info("checkIpClrSync Request {} ", checkIpClrSyncJsonInput);
		CheckIpClrSyncBean checkIpClrSyncBean = (CheckIpClrSyncBean) Utils
				.convertJsonToObject(checkIpClrSyncJsonInput, CheckIpClrSyncBean.class);

		CheckIPCLRDetail checkIPCLRDetail = new CheckIPCLRDetail();
		RequestHeader requestHeader = new RequestHeader();
		requestHeader.setRequestId(StringUtils.trimToEmpty(checkIpClrSyncBean.getRequestId()));
		//requestHeader.setRequestingSystem(StringUtils.trimToEmpty(checkIpClrSyncBean.getRequestingSystem()));
		requestHeader.setRequestingSystem(cramerSourceSystem);
		requestHeader.setRequestTime(StringUtils.trimToEmpty(checkIpClrSyncBean.getRequestTime()));
		requestHeader.setScenarioType(StringUtils.trimToEmpty(checkIpClrSyncBean.getScenarioType()));
		checkIPCLRDetail.setHeader(requestHeader);
		checkIPCLRDetail.setServiceId(checkIpClrSyncBean.getServiceId());
		// TODO: required during MACD developments
		// checkIPCLRDetail.setOldServiceID(value);

		NetworkInventory networkInventory = new NetworkInventory();
		SoapRequest soapRequest = new SoapRequest();
		try {
			// Save request & response
			networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
			networkInventory.setServiceCode(StringUtils.trimToEmpty(checkIpClrSyncBean.getServiceId()));
			networkInventory.setRequestId(StringUtils.trimToEmpty(checkIpClrSyncBean.getRequestId()));
			networkInventory.setRequest(Utils.convertObjectToXmlString(checkIPCLRDetail, CheckIPCLRDetail.class));
			networkInventory.setType(CramerConstants.CHECK_IP_CLR);
			soapRequest.setUrl(checkIPClrSyncUrl);
			soapRequest.setRequestObject(checkIPCLRDetail);
			soapRequest.setContextPath("com.tcl.dias.serviceactivation.cramer.checkipclr.beans");
			soapRequest.setSoapUserName("");
			soapRequest.setSoapPwd("");
			soapRequest.setConnectionTimeout(60000);
			soapRequest.setReadTimeout(60000);

			JAXBElement jaxBResponse = genericWebserviceClient.doSoapCallForObject(soapRequest, JAXBElement.class);
			CheckIPCLRDetailResponse checkIPCLRDetailResponse = (CheckIPCLRDetailResponse) jaxBResponse.getValue();

			if (Objects.nonNull(checkIPCLRDetailResponse) && Objects.nonNull(checkIPCLRDetailResponse.getReturn())) {
				networkInventory.setResponse(
						Utils.convertObjectToXmlString(checkIPCLRDetailResponse, CheckIPCLRDetailResponse.class));
				networkInventoryRepository.save(networkInventory);
				return Utils.convertObjectToJson(checkIPCLRDetailResponse.getReturn());
			}

		} catch (SoapFaultClientException ex) {
			logger.error("SoapFaultClientException in Cramer checkIpClrSync Service ", ex);
			logger.error("getFaultCode {} , getFaultString {}", ex.getSoapFault().getFaultCode());
			SoapFaultDetail soapFaultDetail = ex.getSoapFault().getFaultDetail();
			if (soapFaultDetail != null) {
				SoapFaultDetailElement detailElementChild = soapFaultDetail.getDetailEntries().next();
				Source detailSource = detailElementChild.getSource();
				Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
				marshaller.setContextPath(soapRequest.getContextPath());
				JAXBElement<com.tcl.dias.serviceactivation.cramer.checkipclr.beans.ServiceFault> source = (JAXBElement<com.tcl.dias.serviceactivation.cramer.checkipclr.beans.ServiceFault>) marshaller
						.unmarshal(detailSource);
				logger.error("ErrorCode:: {} , ErrorLongDescription:: {},ErrorShortDescription:: {}",
						source.getValue().getErrorCode(), source.getValue().getErrorLongDescription(),
						source.getValue().getErrorShortDescription());
				networkInventory.setResponse(source.getValue().getErrorCode() +" : "+ source.getValue().getErrorLongDescription());
			} else {
				networkInventory.setResponse(ex.getFaultStringOrReason());
			}
			networkInventoryRepository.save(networkInventory);
			return "false";
		} catch (Exception e) {
			logger.error("Exception in Cramer checkIpClrSync ", e);
			networkInventory.setResponse(e.getMessage());
			networkInventoryRepository.save(networkInventory);
			return e.getMessage();
		}
		return "false";
	}

	public static int getTotalAdditionalIps(String input, int ipTypeValue) {
		int totalAdditionalIps = 0;
		try {
		String[] splitBySlashArr = input.split("\\/");
		int calculatedValue = ipTypeValue - Integer.parseInt(splitBySlashArr[1]);
		totalAdditionalIps = (int) Math.pow(2, calculatedValue);
		}catch(Exception e) {
			logger.error("Exception in getTotalAdditionalIps >> input is {} | exception is {}", input, e);
			return 0;
		}
		return totalAdditionalIps;
	}

	public Response getEORIORDetails(String getEorIorDetailRequest) throws TclCommonException {
		logger.info(String.format("Entering CramerService :: getEORIORDetails method with request : %s", getEorIorDetailRequest));

		com.tcl.dias.servicefulfillmentutils.beans.EorIorDependencyBean dependencyBean =
				(com.tcl.dias.servicefulfillmentutils.beans.EorIorDependencyBean) Utils
				.convertJsonToObject(getEorIorDetailRequest,
						com.tcl.dias.servicefulfillmentutils.beans.EorIorDependencyBean.class);

		Response response = new Response();
		response.setStatus(false);
		NetworkInventory networkInventory = new NetworkInventory();
		GetIEOR getIEOR = new GetIEOR();
		CramerRequestHeader cramerRequestHeader = new CramerRequestHeader();
		cramerRequestHeader.setObjectName(dependencyBean.getServiceCode());
		cramerRequestHeader.setObjectType(com.tcl.dias.serviceactivation.cramer.eoriordetails.beans.ObjectType.SERVICE);
		cramerRequestHeader.setRequestID(dependencyBean.getProcessInstanceId());
		//cramerRequestHeader.setRequestingSystem("optimus");
		cramerRequestHeader.setRequestingSystem(cramerSourceSystem);
		cramerRequestHeader.setRequestTime(LocalDateTime.now().toString());
		getIEOR.setRequestHeader(cramerRequestHeader);
		SoapRequest soapRequest = new SoapRequest();
		try {
			networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
			networkInventory.setServiceCode(StringUtils.trimToEmpty(dependencyBean.getServiceCode()));
			networkInventory.setRequestId(StringUtils.trimToEmpty(dependencyBean.getProcessInstanceId()));
			networkInventory.setRequest(Utils.convertObjectToXmlString(getIEOR, GetIEOR.class));
			networkInventory.setType(CramerConstants.EOR_IOR_DEPENDENCY_CHECK);

			soapRequest.setUrl(eorIorDetailUrl);
			soapRequest.setRequestObject(getIEOR);
			soapRequest.setContextPath("com.tcl.dias.serviceactivation.cramer.eoriordetails.beans");
			soapRequest.setSoapUserName("");
			soapRequest.setSoapPwd("");
			soapRequest.setConnectionTimeout(60000);
			soapRequest.setReadTimeout(60000);

			JAXBElement jaxBResponse = genericWebserviceClient.doSoapCallForObject(soapRequest, JAXBElement.class);
			GetIEORResponse getIEORResponse = (GetIEORResponse) jaxBResponse.getValue();

			if (Objects.nonNull(getIEORResponse) && Objects.nonNull(getIEORResponse.getIOREORDependancyOutput())) {
				networkInventory.setResponse(
						Utils.convertObjectToXmlString(getIEORResponse, GetIEORResponse.class));
				networkInventoryRepository.save(networkInventory);
				IoreorDependancyOutput ioreorDependancyOutput = getIEORResponse.getIOREORDependancyOutput();

				if (ioreorDependancyOutput.isDependancyFlag()) {
					response.setStatus(true);
					if (!CollectionUtils.isEmpty(ioreorDependancyOutput.getEORList()) || !CollectionUtils.isEmpty(ioreorDependancyOutput.getIORList())) {
						EorIorDependencyBean eorIorDependencyBean = new EorIorDependencyBean();
						eorIorDependencyBean.setMessage(ioreorDependancyOutput.getObjectName() + " service depends on below EOR or IOR dependencies");
						eorIorDependencyBean.setEorLists(ioreorDependancyOutput.getEORList());
						eorIorDependencyBean.setIorLists(ioreorDependancyOutput.getIORList());
						response.setErrorMessage(Utils.convertObjectToJson(eorIorDependencyBean));
						logger.info("getErrorMessage={}  getObjectName={}", response.getErrorMessage(),ioreorDependancyOutput.getObjectName());
						saveEorAsComponentAdditionalParam(StringUtils.trimToEmpty(ioreorDependancyOutput.getObjectName()), eorIorDependencyBean);
						networkInventoryRepository.save(networkInventory);
					}
				}else if(ioreorDependancyOutput.isDependancyFlag()==null)
					response.setStatus(true);
				
			}
		} catch (SoapFaultClientException ex) {
			logger.error("SoapFaultClientException in CramerService::getEORIORDetail {}", ex);
			logger.error("getEORIORDetailsFaultCode {} , getFaultString {}", ex.getSoapFault().getFaultCode(),ex.getFaultStringOrReason());
			SoapFaultDetail soapFaultDetail = ex.getSoapFault().getFaultDetail();
			if (soapFaultDetail != null) {
				SoapFaultDetailElement detailElementChild = soapFaultDetail.getDetailEntries().next();
				Source detailSource = detailElementChild.getSource();
				Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
				marshaller.setContextPath(soapRequest.getContextPath());
				JAXBElement<com.tcl.dias.serviceactivation.cramer.eoriordetails.beans.ServiceFault> source = (JAXBElement<com.tcl.dias.serviceactivation.cramer.eoriordetails.beans.ServiceFault>) marshaller
						.unmarshal(detailSource);
				logger.error("ErrorCode:: {} , ErrorLongDescription:: {},ErrorShortDescription:: {}",
						source.getValue().getErrorCode(), source.getValue().getErrorLongDescription(),
						source.getValue().getErrorShortDescription());
				networkInventory.setResponse(source.getValue().getErrorCode() +" : "+ source.getValue().getErrorLongDescription());
				response.setErrorCode(source.getValue().getErrorCode());
				response.setErrorMessage(source.getValue().getErrorLongDescription());
			} else {
				networkInventory.setResponse(ex.getFaultStringOrReason());
				response.setErrorCode(ex.getFaultCode().getLocalPart());
				response.setErrorMessage(ex.getFaultStringOrReason());
			}
			networkInventoryRepository.save(networkInventory);
			response.setStatus(true);
			
			return response;
		} catch (Exception e) {
			logger.error("getEORIORDetailsException in Cramer {}", e);
			networkInventory.setResponse(e.getMessage());
			networkInventoryRepository.save(networkInventory);
			response.setStatus(true);
			response.setErrorMessage(e.getMessage());
			response.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			return response;
		}
		return response;
	}

	private void saveErrorAsComponentAdditionalParam(String serviceCode, EorIorDependencyBean eorIorDependencyBean) throws TclCommonException {
		String details = Utils.convertObjectToJson(eorIorDependencyBean);
		Optional.ofNullable(scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS"))
				.ifPresent(scServiceDetail -> {
					try {
						componentAndAttributeService.updateAdditionalAttributes(scServiceDetail,"dependencyDetails",
								componentAndAttributeService.getErrorMessageDetails(details,"00000"),
								AttributeConstants.ERROR_MESSAGE,"check-eor-ior-dependency");
					} catch (TclCommonException e) {
						logger.error(String.format("Exception occurred in saveErrorAsComponentAdditionalParam : %s",e));
					}
				});
	}
	
	private void saveEorAsComponentAdditionalParam(String serviceCode, EorIorDependencyBean eorIorDependencyBean) throws TclCommonException {
		String details = Utils.convertObjectToJson(eorIorDependencyBean);
		Optional.ofNullable(scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS"))
				.ifPresent(scServiceDetail -> {
					try {
						componentAndAttributeService.updateAdditionalAttributes(scServiceDetail,"dependencyDetails",details,
								AttributeConstants.ERROR_MESSAGE,"check-eor-ior-dependency");
					} catch (Exception e) {
						logger.error(String.format("Exception occurred in saveErrorAsComponentAdditionalParam : %s",e));
					}
				});
	}
	
	public String setBandwidthConversion(String bandwidth, String bandwidthUnit)
	{
		Double bandwidthValue=Double.parseDouble(bandwidth.trim());
		logger.info("Bandwidth Value in setBandwidthConversion {}",bandwidth);
		logger.info("Bandwidth Unit in setBandwidthConversion {}",bandwidthUnit);

		if(Objects.nonNull(bandwidth)&&Objects.nonNull(bandwidthUnit))
		{
			switch (bandwidthUnit.trim().toLowerCase())
			{
				case "kbps": {
					bandwidthValue = Double.parseDouble(bandwidth.trim());
					bandwidthValue = bandwidthValue / 1024;
					bandwidth = bandwidthValue.toString();
					break;
				}
				case "gbps": {
					bandwidthValue = Double.parseDouble(bandwidth.trim());
					bandwidthValue = bandwidthValue * 1000;
					bandwidth = bandwidthValue.toString();
					break;
				}
				default:
					break;
			}

			int index=bandwidth.indexOf(".");
			if(index>0) {
				logger.info("bandwidth value" + bandwidth);
				String precisions = bandwidth.substring(index + 1);
				logger.info("precision value" + precisions);
				if (precisions.length() > 3) {
					DecimalFormat df = new DecimalFormat("#.###");
					df.setRoundingMode(RoundingMode.CEILING);
					String value = df.format(bandwidthValue);
					logger.info("Formatted value" + value);
					bandwidth = value;
				}
			}
		}
		logger.info("Resultant Bandwidth in setBandwidthConversion: {}",bandwidth);
		return bandwidth;
	}
	
	private String compareBwValues(String changeInLlBw,String oldValue,String newValue){
		Double value1=Double.valueOf(oldValue.trim());
		Double value2=Double.valueOf(newValue.trim());
		if(value2>value1)
			return CommonConstants.UPGRADE;
		else if(value2<value1)
			return CommonConstants.DOWNGRADE;
		else
			return CommonConstants.EQUAL;
	}
	
	public Response createCLRForNpl(CramerInfoBean cramerInfoBean, Task task, Map<String, Object> taskDataMap,
			Map<String, String> attributesMap) throws TclCommonException {
		Response response = new Response();
		NetworkInventory networkInventory = new NetworkInventory();
		try {

			if (task != null) {				
				
				Map<String, String> siteAcompMap=	commonFulfillmentUtils.getComponentAttributes(task.getServiceId(), "LM", "A");
				
				if(Objects.nonNull(siteAcompMap) && siteAcompMap.containsKey("skipAutoClr") 
						  && Objects.nonNull(siteAcompMap.get("skipAutoClr")) && !siteAcompMap.get("skipAutoClr").isEmpty() 
						&& "Yes".equalsIgnoreCase(siteAcompMap.get("skipAutoClr"))) {
					logger.info("NPL Skip Auto Clr exists {}", siteAcompMap.get("skipAutoClr"));
					saveNPLServiceDetails(task, siteAcompMap);
					networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
					networkInventory.setServiceCode(task.getServiceCode());
					networkInventory.setRequestId(cramerInfoBean.getProcessInstanceId());
					networkInventory.setRequest("Skip CLR Request");
					networkInventory.setResponse("Skip CLR Response");
					networkInventory.setType(CramerConstants.SKIP_CLR);
					networkInventoryRepository.save(networkInventory);
					response.setStatus(true);
					response.setData("skip");
					return response;
				}
				logger.info("NPL Skip Auto Clr not exists");
				ServiceDetail serviceDetail =saveNPLServiceDetails(task,siteAcompMap);
				InitiateCLRCreation initiateCLRCreation = new InitiateCLRCreation();
				initiateCLRCreation.setSERVICEID(task.getServiceCode());

				logger.info("Set ORDER TYPE, ORDER CATEGORY Dynamically");
				String orderType = (String) taskDataMap.get(CramerConstants.ORDER_TYPE);
				String productOfferingName = (String) taskDataMap.get("productOfferingName");
				String crossConnectType = (String) taskDataMap.get("crossConnectType");
				String fiberType = (String) taskDataMap.get("fiberType");
				String mediaType = (String) taskDataMap.get("mediaType");
				
				
				String serviceSubType = StringUtils.trimToEmpty(siteAcompMap.get("serviceSubType"));
				
				
				initiateCLRCreation.setSERVICETYPE(CramerConstants.NPL_INTRACITY);
				
				

				initiateCLRCreation.setCOPFID(String.valueOf((Integer) taskDataMap.get(CramerConstants.ORDER_ID)));
				initiateCLRCreation.setScenarioType(CramerConstants.CLR_CREATION);
				initiateCLRCreation.getListOfFeasibilityId()
						.add(StringUtils.trimToEmpty((String) taskDataMap.get(CramerConstants.FEASIBILITY_ID)));
				initiateCLRCreation.setRequestID(cramerInfoBean.getProcessInstanceId());

				OrderDetails orderDetails = new OrderDetails();
				orderDetails.setBANDWIDTHVALUE(
						StringUtils.trimToEmpty((String) taskDataMap.get(CramerConstants.LOCAL_LOOP_BANDWIDTH))
								.replaceAll(CramerConstants.mbps, CramerConstants.EMPTY));
				orderDetails.setBANDWIDTHUNIT(CramerConstants.Mbps);
				orderDetails.setCUSTOMERNAME(
						StringUtils.trimToEmpty((String) taskDataMap.get(CramerConstants.CUSTOMER_NAME)));
				orderDetails.setCOVERAGE(CramerConstants.CUSTOMER_ACCESS);
				
				if(!productOfferingName.isEmpty() && "MMR Cross Connect".equalsIgnoreCase(productOfferingName) 
						&& crossConnectType!=null && !crossConnectType.isEmpty() &&  "Passive".equalsIgnoreCase(crossConnectType)){
					logger.info("CRAMER CLR:MMR CROSS CONNECT:: PASSIVE BW VALUE");
					orderDetails.setBANDWIDTHVALUE(null);
					orderDetails.setBANDWIDTHUNIT(null);
				}
//
//				if (orderType.equals(CramerConstants.TYPE_MACD)) {
//					orderDetails.setRequestType(CramerConstants.CHANGE);
//					formOrderType(taskDataMap, orderDetails, task);
//				} else if (orderType.equals(CramerConstants.TYPE_NEW)) {
//					orderDetails.setRequestType(orderType);
//					orderDetails.setOrderType(CramerConstants.TYPE_NEW);
//				}
//				
				String orderCategory=Objects.nonNull(taskDataMap.get(CramerConstants.ORDER_CATEGORY))?(String)taskDataMap.get(CramerConstants.ORDER_CATEGORY):"NEW";
				String orderSubCategory=Objects.nonNull(taskDataMap.get(CramerConstants.ORDER_SUB_CATEGORY))?(String)taskDataMap.get(CramerConstants.ORDER_SUB_CATEGORY):"";
				String lmType = String.valueOf(taskDataMap.get("lmType"));
				if(orderType.equals(CramerConstants.TYPE_MACD)){
					logger.info("CREATE CRAMER:MACD");
					if(CramerConstants.ADD_SITE_SERVICE.equals(orderCategory) 
							|| (Objects.nonNull(orderSubCategory) && (orderSubCategory.toLowerCase().contains("parallel")))){
						logger.info("CREATE CRAMER:ADD SITE OR PARALLEL");
						orderDetails.setRequestType(CramerConstants.TYPE_NEW);
						orderDetails.setOrderType(CramerConstants.TYPE_NEW);
					}else if(Objects.nonNull(orderSubCategory) && (orderSubCategory.toLowerCase().contains("bso") && (lmType.toLowerCase().contains("onnet wireline") || lmType.toLowerCase().contains("onnetwl")))){
					logger.info("CREATE CRAMER:BSO Change");
					orderDetails.setRequestType(CramerConstants.CHANGE);
					orderDetails.setOrderType("BSO CHANGE");
					}else if(Objects.nonNull(orderSubCategory) && (orderSubCategory.toLowerCase().contains("lm") || orderSubCategory.equalsIgnoreCase("Shifting") || orderSubCategory.toLowerCase().contains("bso"))){
					logger.info("CREATE CRAMER:LM SHIFTING");
					orderDetails.setRequestType(CramerConstants.CHANGE);
					orderDetails.setOrderType("LM SHIFTING");
					}else{
						logger.info("CREATE CRAMER:CHANGE");
						orderDetails.setRequestType(CramerConstants.CHANGE);
						orderDetails.setOrderType(orderSubCategory.toUpperCase());
					}
					}else if(orderType.equals(CramerConstants.TYPE_NEW)){
					logger.info("CREATE CRAMER:NEW");
					orderDetails.setRequestType(orderType);
					orderDetails.setOrderType(CramerConstants.TYPE_NEW);
				}
				
				orderDetails.setPRERESERVED(true);
				initiateCLRCreation.setOrderDetails(orderDetails);

				EndDetails aEndDetails = new EndDetails();
				EndDetails zEndDetails = new EndDetails();
				Map<String, String> siteBcompMap=	commonFulfillmentUtils.getComponentAttributesDetails(
						Arrays.asList("endMuxNodeName", "lmType","offnetProviderName","providerReferenceId","providerNodeName","interface","cloudName","interfaceType"), task.getServiceId(), "LM", "B");
			
				String lmTypeSiteA = StringUtils.trimToEmpty(siteAcompMap.get("lmType")).toLowerCase();
				String lmTypeSiteB = StringUtils.trimToEmpty(siteBcompMap.get("lmType")).toLowerCase();
								

				if (lmTypeSiteA.contains("onnet")) {
					aEndDetails.setONNET(true);
					String interfaceSiteA = StringUtils.trimToEmpty(siteAcompMap.get("interface"));
					aEndDetails.setINTERFACE(getInterface(interfaceSiteA));
					String endMuxNodeName = StringUtils.trimToEmpty(siteAcompMap.get("endMuxNodeName"));
					aEndDetails.setNODEW(endMuxNodeName);
				}
				if (lmTypeSiteB.contains("onnet")) {					
					String interfaceTypeB = StringUtils.trimToEmpty(siteBcompMap.get("interfaceType"));
					if(interfaceTypeB.equalsIgnoreCase("Optical")) {
						String cloudName = StringUtils.trimToEmpty(siteBcompMap.get("cloudName"));
						zEndDetails.setNODEW(cloudName);
						zEndDetails.setINTERFACE("Optical");
						zEndDetails.setONNET(false);
					}else {
						zEndDetails.setONNET(true);
						String interfaceSiteB = StringUtils.trimToEmpty(siteBcompMap.get("interface"));						
						zEndDetails.setINTERFACE(getInterface(interfaceSiteB));
						String endMuxNodeName = StringUtils.trimToEmpty(siteBcompMap.get("endMuxNodeName"));
						zEndDetails.setNODEW(endMuxNodeName);
					}					
				}
				
				if (lmTypeSiteA.contains("offnet")) {
					aEndDetails.setONNET(false);
					//String offnetProvider = StringUtils.trimToEmpty(siteAcompMap.get("offnetProviderName"));
					String cloudName = StringUtils.trimToEmpty(siteAcompMap.get("cloudName"));
					aEndDetails.setNODEW(cloudName);
					aEndDetails.setINTERFACE("Optical");
					WrkrOffnetIfaceDtls aOffnet = new WrkrOffnetIfaceDtls();
					//aOffnet.setOFFNETPROVIDER(offnetProvider);
					initiateCLRCreation.setAWrkrOffnetIfaceDtls(aOffnet);
				}

				if (lmTypeSiteB.contains("offnet")) {
					zEndDetails.setONNET(false);
					String cloudName = StringUtils.trimToEmpty(siteBcompMap.get("cloudName"));

					zEndDetails.setNODEW(cloudName);
					zEndDetails.setINTERFACE("Optical");
					WrkrOffnetIfaceDtls zOffnet = new WrkrOffnetIfaceDtls();
					//zOffnet.setOFFNETPROVIDER(offnetProvider);
					initiateCLRCreation.setZWrkrOffnetIfaceDtls(zOffnet);
				}

				if(!productOfferingName.isEmpty() && "MMR Cross Connect".equalsIgnoreCase(productOfferingName) 
						&& !crossConnectType.isEmpty() &&  "Passive".equalsIgnoreCase(crossConnectType)){
					logger.info("CREATE CRAMER:NEW ORDER MMR CROSS CONNECT FOR PASSIVE INTERFACE DETAILS");
					if(!fiberType.isEmpty() && !mediaType.isEmpty() && "Fiber pair".equalsIgnoreCase(mediaType)){				
						logger.info("Fiber Type Exists::{}",fiberType);
						aEndDetails.setINTERFACE(fiberType+" Fiber");
						zEndDetails.setINTERFACE(fiberType+" Fiber");
					}else{
						logger.info("Fiber Type not Exists");
						aEndDetails.setINTERFACE("Single Mode Fiber");
						zEndDetails.setINTERFACE("Single Mode Fiber");
					}
					aEndDetails.setNODEW(null);
					zEndDetails.setNODEW(null);
					aEndDetails.setONNET(false);
					zEndDetails.setONNET(false);
				}
				
				aEndDetails.setISMODIFIED(false);
				aEndDetails.setNODEIDW(0L);
				initiateCLRCreation.setAEndDetails(aEndDetails);

				zEndDetails.setISMODIFIED(false);
				zEndDetails.setNODEIDW(0L);
				initiateCLRCreation.setZEndDetails(zEndDetails);

				UccServDetails uccServDetails = new UccServDetails();
				// uccServDetails.s
				UccService uccService = new UccService();
				uccService.getAttribute().add(new Attributes());
				List<UccService> uccServiceList = new ArrayList<>();
				uccServiceList.add(uccService);
				initiateCLRCreation.setUCCServDtls(uccServDetails);

				UaApplicable uaApplicable = new UaApplicable();
				initiateCLRCreation.setUAApplicable(uaApplicable);

				ClrDesignDtls clrDesignDtls = new ClrDesignDtls();
				IldDtls ildDtls = new IldDtls();
				ildDtls.setSTATICPROTECTION("");
				clrDesignDtls.setILDDtls(ildDtls);
				clrDesignDtls.setMAXHOP(8);
				clrDesignDtls.setPRIMARYSECONDARY(false);
			
				clrDesignDtls.setUSEHANGINGNTWKCIRCUITS(false);

				NldDtls nldDtls = new NldDtls();
				nldDtls.setALLOWEDTIER1HOPS(3);
				nldDtls.setALLOWTTSLNODES(false);
				nldDtls.setOTHERSEGMENT(CramerConstants.EMPTY);

				clrDesignDtls.setNLDDtls(nldDtls);
				initiateCLRCreation.setClrDesignDtls(clrDesignDtls);

				AdditionalAttr additionalAttr = new AdditionalAttr();
				//attributesMap.put(CramerConstants.REQUESTING_SYSTEM, CramerConstants.OPTIMUS);
				attributesMap.put(CramerConstants.REQUESTING_SYSTEM, cramerSourceSystem);
				
				if(!productOfferingName.isEmpty() && "MMR Cross Connect".equalsIgnoreCase(productOfferingName)
						&& !crossConnectType.isEmpty()){
					logger.info("CREATE CRAMER:NEW ORDER MMR CROSS CONNECT");
					if("Active".equalsIgnoreCase(crossConnectType)){
						logger.info("CREATE CRAMER:NEW ORDER MMR ACTIVE");
						attributesMap.put(CramerConstants.CROSSCONNECTTYPE, "Active");
					}else if("Passive".equalsIgnoreCase(crossConnectType)){
						logger.info("CREATE CRAMER:NEW ORDER MMR PASSIVE");
						attributesMap.put(CramerConstants.CROSSCONNECTTYPE, "Passive");
						
						attributesMap.put("ISEXCEPTIONSCENARIO", "TRUE");
						attributesMap.put("EXCEPTIONREASON", "No TX orders - Direct Fiber");
						attributesMap.put("NPL_INTRACITY_NOTX", "TRUE");
						
						Map<String,String> attrMap=new HashMap<>();
						attrMap.put("ISEXCEPTIONSCENARIO", "TRUE");
						attrMap.put("EXCEPTIONREASON", "No TX orders - Direct Fiber");
						attrMap.put("NPL_INTRACITY_NOTX", "TRUE");
						
						attrMap.entrySet().stream().forEach(map -> {
							Attributes attr = new Attributes();
							attr.setKey(map.getKey());
							attr.setValue(map.getValue());
							additionalAttr.getAttributes().add(attr);
						});
						
						Map<String,String> attributeMap=new HashMap<>();
						attributeMap.put("ISEXCEPTIONSCENARIO", "TRUE");
						attributeMap.put("EXCEPTIONREASON", "No TX orders - Direct Fiber");
						attributeMap.put("NPL_INTRACITY_NOTX", "TRUE");
						
						attributeMap.entrySet().stream().forEach(map -> {
							Attributes attr = new Attributes();
							attr.setKey(map.getKey());
							attr.setValue(map.getValue());
							additionalAttr.getAttributes().add(attr);
						});
					}
				}
				attributesMap.entrySet().stream().forEach(map -> {
					Attributes attr = new Attributes();
					attr.setKey(map.getKey());
					attr.setValue(map.getValue());
					additionalAttr.getAttributes().add(attr);
				});

				if (Objects.nonNull(attributesMap) && !attributesMap.isEmpty()) {
					initiateCLRCreation.setAdditionalAttributes(additionalAttr);
				}

				// Save request & response
				networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
				networkInventory.setServiceCode(task.getServiceCode());
				networkInventory.setRequestId(cramerInfoBean.getProcessInstanceId());
				networkInventory
						.setRequest(Utils.convertObjectToXmlString(initiateCLRCreation, InitiateCLRCreation.class));
				networkInventory.setType(CramerConstants.CREATE_CLR);

				SoapRequest soapRequest = new SoapRequest();
				soapRequest.setUrl(createClrUrl);
				soapRequest.setRequestObject(initiateCLRCreation);
				soapRequest.setContextPath("com.tcl.dias.serviceactivation.cramer.servicedesign.beans");
				soapRequest.setSoapUserName("");
				soapRequest.setSoapPwd("");
				soapRequest.setConnectionTimeout(60000);
				soapRequest.setReadTimeout(60000);
				try {
					JAXBElement jaxBResponse = genericWebserviceClient.doSoapCallForObject(soapRequest,
							JAXBElement.class);
					InitiateCLRCreationResponse initiateCLRCreationResponse = (InitiateCLRCreationResponse) jaxBResponse
							.getValue();
					if (initiateCLRCreationResponse != null
							&& initiateCLRCreationResponse.getClrCreationResult() != null) {

						try {
							networkInventory.setResponse(Utils.convertObjectToXmlString(initiateCLRCreationResponse,
									InitiateCLRCreationResponse.class));
							networkInventoryRepository.save(networkInventory);
						} catch (Exception ex) {
							logger.error("Exception in Cramer Create CLR  ", ex);
						}

						Acknowledgement acknowledgement = initiateCLRCreationResponse.getClrCreationResult().getAck();
						if (acknowledgement != null) {
							logger.info("createCLR acknowledgement RequestID=> {}, ServiceID=> {} Status={}",
									acknowledgement.getRequestID(), acknowledgement.getServiceID(),
									acknowledgement.isStatus());
							response.setStatus(acknowledgement.isStatus());
							return response;
						}

					}
				} catch (SoapFaultClientException ex) {
					if (Objects.nonNull(ex.getSoapFault().getFaultDetail())) {
						logger.error("SoapFaultClientException in Cramer createCLR ", ex);
						SoapFaultDetail soapFaultDetail = ex.getSoapFault().getFaultDetail();
						if (soapFaultDetail != null) {
							SoapFaultDetailElement detailElementChild = soapFaultDetail.getDetailEntries().next();
							Source detailSource = detailElementChild.getSource();
							Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
							marshaller.setContextPath(soapRequest.getContextPath());
							@SuppressWarnings("unchecked")
							JAXBElement<com.tcl.dias.serviceactivation.cramer.servicedesign.beans.ServiceFault> source = (JAXBElement<com.tcl.dias.serviceactivation.cramer.servicedesign.beans.ServiceFault>) marshaller
									.unmarshal(detailSource);
							logger.error("ErrorCode:: {} , ErrorLongDescription:: {},ErrorShortDescription:: {}",
									source.getValue().getErrorCode(), source.getValue().getErrorLongDescription(),
									source.getValue().getErrorShortDescription());
							/*if(source.getValue().getErrorCode().equalsIgnoreCase("CBIAS100093")){
								response.setStatus(true);
								response.setData("skipExistClr");
							}else if (source.getValue().getErrorCode().equalsIgnoreCase("CBIAS100010")
									|| source.getValue().getErrorCode().equalsIgnoreCase("CBIAS100020")
									|| source.getValue().getErrorCode().equalsIgnoreCase("CBIAS100030")) {
								response.setStatus(true);
							} else {*/
								response.setStatus(false);
								response.setErrorCode(source.getValue().getErrorCode());
								response.setErrorMessage(source.getValue().getErrorLongDescription());
							//}
							networkInventory.setResponse(source.getValue().getErrorCode() + ":"
									+ source.getValue().getErrorLongDescription());
							networkInventoryRepository.save(networkInventory);
						}

					} else {
						logger.error("SoapFaultClientException in Cramer createCLR Service ", ex);
						logger.error("getFaultCode {} , getFaultString {}", ex.getSoapFault().getFaultCode());
						response.setErrorCode(String.valueOf(ex.getFaultCode()));
						response.setErrorMessage(ex.getFaultStringOrReason());
						response.setStatus(false);
						networkInventory.setResponse(ex.getFaultStringOrReason());
						networkInventoryRepository.save(networkInventory);
					}
				}

			} else {
				logger.info("Task is null in CramerService > createCLRByTaskId Method");
			}
			return response;
		} catch (Exception e) {
			logger.error("Exception in Cramer Create CLR  ", e);
			networkInventory.setResponse(e.getMessage());
			networkInventoryRepository.save(networkInventory);
			response.setStatus(false);
			response.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
			response.setErrorMessage(e.getMessage());
			return response;
		}
	}
	
	private ServiceDetail saveNPLServiceDetails(Task task,Map<String, String> siteAcompMap) {
		logger.info("saveNPLServiceDetails invoked");
		ServiceDetail serviceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(task.getServiceCode(),TaskStatusConstants.ISSUED);
        if(null==serviceDetail) {
        	logger.info("NPL serviceDetail not exists");
            // populating service activation logics
        	ScOrder scOrder=scOrderRepository.findByOpOrderCodeAndIsActive(task.getOrderCode(), "Y");
        	ServiceDetail activeServiceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(task.getServiceCode(),TaskStatusConstants.ACTIVE);
            Integer version=null;
        	if(Objects.nonNull(activeServiceDetail)){
        		version=activeServiceDetail.getVersion();
        	}
    		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(task.getServiceId()).get();
    		        		

    		serviceDetail=serviceActivationService.saveNPLServiceDetailsActivation(task.getServiceCode(),version,scOrder,scServiceDetail,siteAcompMap);
        }		
        return serviceDetail;
	}


	private String getInterface(String interfaceType) {
		if(interfaceType.equalsIgnoreCase(CramerConstants.FE) || interfaceType.equalsIgnoreCase(CramerConstants.FAST_ETHERNET)) {
			interfaceType = CramerConstants.FAST_ETHERNET;
		}else if(interfaceType.equalsIgnoreCase(CramerConstants.GE) || interfaceType.toLowerCase().contains("gig ethernet") || interfaceType.toLowerCase().contains("gigabit ethernet")){
			interfaceType = CramerConstants.GIGABIT_ETHERNET;
		}else if(interfaceType.contains("10G")){
			interfaceType = "10 G";
		}else if(interfaceType.contains("G.957")){
			interfaceType = "G.957 Optical";
		}else if(interfaceType.contains("1000base LX (Optical)")){
			interfaceType = "1000-BASE-LX";
		}else if(interfaceType.toLowerCase().contains("10-base") || interfaceType.toLowerCase().contains("10 base") || interfaceType.toLowerCase().contains("10base")){
			interfaceType = CramerConstants.FAST_ETHERNET;
		}else if((interfaceType.toLowerCase().contains("100-base") || interfaceType.toLowerCase().contains("100 base") || interfaceType.toLowerCase().contains("100base")) && !interfaceType.toLowerCase().contains("lx") ){
			interfaceType = CramerConstants.FAST_ETHERNET;
		}else if((interfaceType.toLowerCase().contains("100-base") || interfaceType.toLowerCase().contains("100 base") || interfaceType.toLowerCase().contains("100base")) && interfaceType.toLowerCase().contains("lx") ){
			interfaceType = "100-BASE-LX";
		}else if(interfaceType.contains("BASE")){
			interfaceType = interfaceType.trim().replaceAll(" ", "-");
		}else if(interfaceType.toUpperCase().contains("BASE")){
			interfaceType = interfaceType.toUpperCase().trim().replaceAll(" ", "-");
		}
		return interfaceType;
	}



	public Response getTxDownTime(String setDownTimeInputJsonAsString) throws TclCommonException {
		logger.error("getTxDownTime invoked");
		SoapRequest soapRequest = new SoapRequest();
		Response response = new Response();
		CheckDownTime checkDownTime = new CheckDownTime();
		NetworkInventory networkInventory = new NetworkInventory();
		try {
			DownTimeBean downTimeBean =
					(DownTimeBean) Utils.convertJsonToObject(setDownTimeInputJsonAsString, DownTimeBean.class);
			checkDownTime.setServiceId(downTimeBean.getServiceId());
			checkDownTime.setRequestId(downTimeBean.getRequestID());
			//checkDownTime.setRequestingSystem(CramerConstants.OPTIMUS);
			checkDownTime.setRequestingSystem(cramerSourceSystem);
			
			networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
			networkInventory.setServiceCode(downTimeBean.getServiceId());
			networkInventory.setRequestId(downTimeBean.getRequestID());
			networkInventory.setRequest(Utils.convertObjectToXmlString(checkDownTime, CheckDownTime.class));
			networkInventory.setType(CramerConstants.GET_DOWNTIME);

			soapRequest.setUrl(downtimeUrl);
			soapRequest.setRequestObject(checkDownTime);
			soapRequest.setContextPath(
					"com.tcl.dias.serviceactivation.cramer.downtime.beans");
			soapRequest.setSoapUserName("");
			soapRequest.setSoapPwd("");
			soapRequest.setConnectionTimeout(60000);
			soapRequest.setReadTimeout(60000);
			JAXBElement jaxBResponse = genericWebserviceClient.doSoapCallForObject(soapRequest, JAXBElement.class);
			CheckDownTimeResponse checkDownTimeResponse = (CheckDownTimeResponse) jaxBResponse.getValue();
			try {
				logger.info("getTxDownTime Response {} ", checkDownTimeResponse);
				if (checkDownTimeResponse != null &&
						checkDownTimeResponse.getDownTimeResponse().getAck() != null) {
					logger.info(" RequestID=> {}, Status=> {}, ServiceID=> {} "
							, checkDownTimeResponse.getDownTimeResponse().getAck().getRequestID(),
							checkDownTimeResponse.getDownTimeResponse().getAck().isStatus(),
							checkDownTimeResponse.getDownTimeResponse().getAck().getServiceID());
					networkInventory.setResponse(Utils.convertObjectToXmlString(checkDownTimeResponse, CheckDownTimeResponse.class));
					response.setStatus(true);
					networkInventoryRepository.save(networkInventory);
				}
			} catch (Exception e) {
				logger.error("Exception in getting getTxDownTime response ", e);

			}
		} catch (Exception e) {
			logger.error("Exception in Cramer getTxDownTime Service {}", e);
			networkInventory.setResponse(e.getMessage());
			networkInventoryRepository.save(networkInventory);
			response.setStatus(false);
			response.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
			response.setErrorMessage(e.getMessage());
			return response;
		}
		return response;
	}


	public Response setTERMINATECLR(String setCLRInputJsonAsString) throws TclCommonException {
		logger.info("setTERMINATECLR Sync Request {} ", setCLRInputJsonAsString);
		Response response = new Response();
		SetCLRSyncBean setCLRSyncBean =
				(SetCLRSyncBean) Utils.convertJsonToObject(setCLRInputJsonAsString, SetCLRSyncBean.class);
		NetworkInventory networkInventory = new NetworkInventory();
		SetCLR setCLRRequest = new SetCLR();
		CramerServiceHeader header = new
				CramerServiceHeader();
		header.setAuthUser(setCLRSyncBean.getCramerServiceHeader().getAuthUser());
		header.setClientSystemIP(setCLRSyncBean.getCramerServiceHeader().getClientSystemIP());
		//header.setOriginatingSystem(setCLRSyncBean.getCramerServiceHeader().getOriginatingSystem());
		header.setOriginatingSystem(cramerSourceSystem);
		header.setOriginationTime(setCLRSyncBean.getCramerServiceHeader().getOriginationTime());
		header.setRequestID(setCLRSyncBean.getCramerServiceHeader().getRequestID());
		setCLRRequest.setHeader(header);

		// Must be any of these [ SERVICE,CIRCUIT,IOR]
		setCLRRequest.setObjectType(ObjectType.valueOf(setCLRSyncBean.getObjectType()));
		setCLRRequest.setObjectName(setCLRSyncBean.getObjectName()); // Must be any of these [ACTIVE,ISSUED,MARKEDFORDELETE]
		setCLRRequest.setInitialRelationship(InitialRelationShip.valueOf(
				setCLRSyncBean.getInitialRelationship())); // Must be any of these [ACTIVE,ISSUED,MARKEDFORDELETE,TERMINATE]
		setCLRRequest.setFinalRelationship(FinalRelationShip.valueOf(setCLRSyncBean.
				getFinalRelationship()));
		SoapRequest soapRequest = new SoapRequest();
		try {
			networkInventory.setCreatedDate(new Timestamp(new Date().getTime()));
			networkInventory.setServiceCode(StringUtils.trimToEmpty(setCLRSyncBean.getObjectName()));
			if (Objects.nonNull(setCLRSyncBean.getCramerServiceHeader()) && Objects.nonNull(setCLRSyncBean.getCramerServiceHeader().getRequestID()))
				networkInventory.setRequestId(StringUtils.trimToEmpty(setCLRSyncBean.getCramerServiceHeader().getRequestID()));
			networkInventory.setRequest(Utils.convertObjectToXmlString(setCLRRequest, SetCLR.class));
			networkInventory.setType(CramerConstants.SET_MFD_CLR_SYNC);
			soapRequest.setUrl(setClrUrl);
			soapRequest.setRequestObject(setCLRRequest);
			soapRequest.setContextPath(
					"com.tcl.dias.serviceactivation.cramer.ipservicesync.beans");
			soapRequest.setSoapUserName("");
			soapRequest.setSoapPwd("");
			soapRequest.setConnectionTimeout(60000);
			soapRequest.setReadTimeout(60000);

			SetCLRResponseClr getIPServiceInfoResponse  = (SetCLRResponseClr)
					genericWebserviceClient.doSoapCallForObject(soapRequest, SetCLRResponseClr.class);
			try {
				logger.info("ipService setTERMINATECLR Sync Response {} ", getIPServiceInfoResponse);
				if (getIPServiceInfoResponse != null &&
						getIPServiceInfoResponse!= null &&
						getIPServiceInfoResponse.getResult().getAck() != null) {
					logger.info("ipService TERMINATE SET CLR Sync RequestID=> {}, RequestReceivedTime=> {}, ServiceID=> {} "
							, getIPServiceInfoResponse.getResult().getAck().getRequestID(),
							getIPServiceInfoResponse.getResult().getAck().getRequestRecievedDateTime(),
							getIPServiceInfoResponse.getResult().getAck().getServiceID());
					networkInventory.setResponse(Utils.convertObjectToXmlString(getIPServiceInfoResponse, SetCLRResponseClr.class));
					response.setStatus(true);
					networkInventoryRepository.save(networkInventory);
				}
			} catch	(Exception e) {
				logger.error("Exception in Cramer setTERMINATECLR Service ", e);
			}
		/*	ServiceDetail oldActiveServiceDetail=serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByIdDesc(setCLRSyncBean.getObjectName(),"ACTIVE");
			if (oldActiveServiceDetail != null) {
				oldActiveServiceDetail.setServiceState("TERMINATED");
				oldActiveServiceDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
				serviceDetailRepository.save(oldActiveServiceDetail);
				ScServiceDetail oldActiveScServiceDetail=scServiceDetailRepository.findByUuidAndMstStatus_code(setCLRSyncBean.getObjectName(), "ACTIVE");
				if(Objects.nonNull(oldActiveScServiceDetail)){
					logger.info("Changing SC OLD SERVICE ID ACTIVE TO TERMINATE");
					MstStatus mstStatus=mstStatusRepository.findByCode("TERMINATE");
					oldActiveScServiceDetail.setMstStatus(mstStatus);
					oldActiveScServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
					scServiceDetailRepository.save(oldActiveScServiceDetail);
				}
			}*/
		} catch (SoapFaultClientException ex) {
			logger.error("SoapFaultClientException in Cramer setTERMINATECLR Service ", ex);
			logger.error("getFaultCode {} , getFaultString {}", ex.getSoapFault().getFaultCode());
			SoapFaultDetail soapFaultDetail = ex.getSoapFault().getFaultDetail();
			if (soapFaultDetail != null) {
				SoapFaultDetailElement detailElementChild = soapFaultDetail.getDetailEntries().next();
				Source detailSource = detailElementChild.getSource();
				Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
				marshaller.setContextPath(soapRequest.getContextPath());
				JAXBElement<SetCLRFault> source = (JAXBElement<SetCLRFault>)marshaller.unmarshal(detailSource);
				logger.error("ErrorCode:: {} , ErrorLongDescription:: {},ErrorShortDescription:: {}"
								, source.getValue().getErrorCode(),
								source.getValue().getErrorLongDescription(),
								source.getValue().getErrorShortDescription());
				networkInventory.setResponse(source.getValue().getErrorCode() + " : " + source.getValue().getErrorLongDescription());
				response.setErrorCode(source.getValue().getErrorCode());
				response.setErrorMessage(source.getValue().getErrorLongDescription());
			} else {
				networkInventory.setResponse(ex.getFaultStringOrReason());
				response.setErrorCode(String.valueOf(ex.getSoapFault()));
				response.setErrorMessage(ex.getFaultStringOrReason());
			}
			response.setStatus(false);
			
			networkInventoryRepository.save(networkInventory);
		} catch	(Exception e) {
			logger.error("Exception in Cramer setTERMINATECLR Service ", e);
			response.setErrorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
			response.setErrorMessage(e.getMessage());
			networkInventory.setResponse(e.getMessage());
			response.setStatus(false);
			networkInventoryRepository.save(networkInventory);
			return response;
		}
		return response;
	}


	private void updateServiceStatusAndCreatedNewStatus(Integer scServiceDetailId,String status) {

		Optional<ScServiceDetail> oScServiceDetail = scServiceDetailRepository.findById(scServiceDetailId);
		if(oScServiceDetail.isPresent()){
			ScServiceDetail scServiceDetail = oScServiceDetail.get();
			ServiceStatusDetails serviceStatusDetails =  serviceStatusDetailsRepository.findFirstByScServiceDetail_idOrderByIdDesc(scServiceDetail.getId());

			if(serviceStatusDetails!=null) {
				serviceStatusDetails.setEndTime(new Timestamp(new Date().getTime()));
				serviceStatusDetails.setUpdateTime(new Timestamp(new Date().getTime()));
				serviceStatusDetailsRepository.save(serviceStatusDetails);
			}

			createServiceStaus(scServiceDetail, status);
		}
	}

	private ServiceStatusDetails createServiceStaus(ScServiceDetail scServiceDetail, String mstStatus) {
		ServiceStatusDetails serviceStatusDetails = new ServiceStatusDetails();
		serviceStatusDetails.setScServiceDetail(scServiceDetail);
		if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

			serviceStatusDetails.setUserName(userInfoUtils.getUserInformation().getUserId());
		}
		serviceStatusDetails.setCreatedTime(new Timestamp(new Date().getTime()));
		serviceStatusDetails.setStartTime(new Timestamp(new Date().getTime()));;
		serviceStatusDetails.setStatus(mstStatus);
		serviceStatusDetailsRepository.save(serviceStatusDetails);

		return serviceStatusDetails;
	}

	public void createNodeManualInNetP(String serviceId) throws TclCommonException {
		ServiceDetail issuedServiceDetail = serviceDetailRepository
				.findFirstByServiceIdAndServiceStateOrderByIdDesc(serviceId,TaskStatusConstants.ISSUED);
		updateOnnetWirelessDetails(serviceId, issuedServiceDetail, issuedServiceDetail.getOrderSubCategory());
	}

	private String setTowerOrPoleHeight(String towerHeight, String poleHeight) {
		String height=null;
		if(towerHeight!=null && !towerHeight.isEmpty())
		{
			if(poleHeight==null || poleHeight.isEmpty())
				return height + "" + towerHeight;
		}
		else
		{
			if(poleHeight!=null && !poleHeight.isEmpty())
				return height + "" + poleHeight;
		}
		return height;
	}

	private String setAntennaHeight(String mastHeight, String towerHeight, String poleHeight){
		String height=null;
		if(setTowerOrPoleHeight(towerHeight, poleHeight)!=null)
			return height + "" + Integer.parseInt(setTowerOrPoleHeight(towerHeight, poleHeight)) + Integer.parseInt(mastHeight);
		else
			return mastHeight;
	}
	
	private void createMstClrVpnSolution(ScServiceDetail scServiceDetail,MstClrVpnSolution parentMstClrVpnSolution,ScServiceAttribute currentVpnTopolyAttr,ScServiceAttribute siteTypeAttr, VpnMetatData parentVpnMetatData) {
		logger.info("createMstClrVpnSolution method invoked:{}", scServiceDetail.getUuid());
		MstClrVpnSolution currentMstClrVpnSolution=mstClrVpnSolutionRepository.findFirstByServiceCodeAndVpnTopologyIgnoreCase(scServiceDetail.getUuid(), currentVpnTopolyAttr.getAttributeValue());
		if(currentMstClrVpnSolution==null) {
			logger.info("createMstClrVpnSolution currentMstClrVpnSolution not exists for:{}", scServiceDetail.getUuid());
			currentMstClrVpnSolution = new MstClrVpnSolution();
		}
		if(parentMstClrVpnSolution!=null) {
			logger.info("parentMstClrVpnSolution exists for:{}", scServiceDetail.getUuid());
			currentMstClrVpnSolution.setSolutionId(parentMstClrVpnSolution.getSolutionId()!=null?parentMstClrVpnSolution.getSolutionId():null);
		}else if(parentVpnMetatData!=null) {
			logger.info("parentVpnMetatData exists for:{}", scServiceDetail.getUuid());
			currentMstClrVpnSolution.setSolutionId(parentVpnMetatData.getVpnName()!=null?parentVpnMetatData.getVpnName():null);
		}
		currentMstClrVpnSolution.setCustomerName(scServiceDetail.getScOrder()!=null?scServiceDetail.getScOrder().getErfCustLeName():null);
		currentMstClrVpnSolution.setServiceCode(scServiceDetail.getUuid());
		if (siteTypeAttr != null && siteTypeAttr.getAttributeValue() != null) {
			logger.info("CREATE-SERVICE:ServiceId:{} Site Type exists:{}", scServiceDetail.getUuid(),siteTypeAttr.getAttributeValue());
			currentMstClrVpnSolution.setSiteTopology(siteTypeAttr.getAttributeValue());
		}
		currentMstClrVpnSolution.setVpnTopology(currentVpnTopolyAttr.getAttributeValue());
		currentMstClrVpnSolution.setUpdatedBy("Optimus-O2C");
		mstClrVpnSolutionRepository.saveAndFlush(currentMstClrVpnSolution);
		
	}
}
