package com.tcl.dias.serviceactivation.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tcl.dias.serviceactivation.activation.netp.beans.ACEHeader;
import com.tcl.dias.serviceactivation.activation.netp.beans.ALUASPathConfig;
import com.tcl.dias.serviceactivation.activation.netp.beans.ALUDefaultOriginateConfig;
import com.tcl.dias.serviceactivation.activation.netp.beans.ALUSchedulerPolicy;
import com.tcl.dias.serviceactivation.activation.netp.beans.ASNumber;
import com.tcl.dias.serviceactivation.activation.netp.beans.AccessControlList;
import com.tcl.dias.serviceactivation.activation.netp.beans.AddonFeatures;
import com.tcl.dias.serviceactivation.activation.netp.beans.Address;
import com.tcl.dias.serviceactivation.activation.netp.beans.BGPRoutingProtocol;
import com.tcl.dias.serviceactivation.activation.netp.beans.Bandwidth;
import com.tcl.dias.serviceactivation.activation.netp.beans.BandwidthUnit;
import com.tcl.dias.serviceactivation.activation.netp.beans.CPEInitConfigParams;
import com.tcl.dias.serviceactivation.activation.netp.beans.CambiumAAAConfig;
import com.tcl.dias.serviceactivation.activation.netp.beans.CambiumAntennaParameters;
import com.tcl.dias.serviceactivation.activation.netp.beans.CambiumLastmile;
import com.tcl.dias.serviceactivation.activation.netp.beans.CambiumSite;
import com.tcl.dias.serviceactivation.activation.netp.beans.CambiumVLANAttributes;
import com.tcl.dias.serviceactivation.activation.netp.beans.CiscoImportMap;
import com.tcl.dias.serviceactivation.activation.netp.beans.Customer;
import com.tcl.dias.serviceactivation.activation.netp.beans.CustomerPremiseEquipment;
import com.tcl.dias.serviceactivation.activation.netp.beans.DMVPNService;
import com.tcl.dias.serviceactivation.activation.netp.beans.DataMDT;
import com.tcl.dias.serviceactivation.activation.netp.beans.EIGRPRoutingProtocol;
import com.tcl.dias.serviceactivation.activation.netp.beans.EthernetAccessService;
import com.tcl.dias.serviceactivation.activation.netp.beans.ExtendedLAN;
import com.tcl.dias.serviceactivation.activation.netp.beans.GVPNService;
import com.tcl.dias.serviceactivation.activation.netp.beans.HBSConfigParameters;
import com.tcl.dias.serviceactivation.activation.netp.beans.HSRPProtocol;
import com.tcl.dias.serviceactivation.activation.netp.beans.HSUConfigParameters;
import com.tcl.dias.serviceactivation.activation.netp.beans.HVPLS;
import com.tcl.dias.serviceactivation.activation.netp.beans.HostedExchangeCEConfig;
import com.tcl.dias.serviceactivation.activation.netp.beans.IASGIPVCService;
import com.tcl.dias.serviceactivation.activation.netp.beans.IASMVOIPService;
import com.tcl.dias.serviceactivation.activation.netp.beans.IAService;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPSECService;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPV4Address;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPV6Address;
import com.tcl.dias.serviceactivation.activation.netp.beans.IRSRouter;
import com.tcl.dias.serviceactivation.activation.netp.beans.IZO;
import com.tcl.dias.serviceactivation.activation.netp.beans.Interface;
import com.tcl.dias.serviceactivation.activation.netp.beans.LoopbackInterface;
import com.tcl.dias.serviceactivation.activation.netp.beans.MaximumRoutes;
import com.tcl.dias.serviceactivation.activation.netp.beans.Multicasting;
import com.tcl.dias.serviceactivation.activation.netp.beans.OSPFRoutingProtocol;
import com.tcl.dias.serviceactivation.activation.netp.beans.OrderCategory;
import com.tcl.dias.serviceactivation.activation.netp.beans.OrderInfo3;
import com.tcl.dias.serviceactivation.activation.netp.beans.OrderType2;
import com.tcl.dias.serviceactivation.activation.netp.beans.P2PL2VPNService;
import com.tcl.dias.serviceactivation.activation.netp.beans.PartnerDevice;
import com.tcl.dias.serviceactivation.activation.netp.beans.PerformIPServiceActivation;
import com.tcl.dias.serviceactivation.activation.netp.beans.PrefixList;
import com.tcl.dias.serviceactivation.activation.netp.beans.QoS;
import com.tcl.dias.serviceactivation.activation.netp.beans.RIPRoutingProtocol;
import com.tcl.dias.serviceactivation.activation.netp.beans.RPLoopbackList;
import com.tcl.dias.serviceactivation.activation.netp.beans.Radwin5KLastmile;
import com.tcl.dias.serviceactivation.activation.netp.beans.RemoteSite;
import com.tcl.dias.serviceactivation.activation.netp.beans.RoadWarriorService;
import com.tcl.dias.serviceactivation.activation.netp.beans.RoutesExchanged;
import com.tcl.dias.serviceactivation.activation.netp.beans.RoutingPolicy;
import com.tcl.dias.serviceactivation.activation.netp.beans.RoutingPolicyMatchCriteria;
import com.tcl.dias.serviceactivation.activation.netp.beans.RoutingPolicySetCriteria;
import com.tcl.dias.serviceactivation.activation.netp.beans.SDPConfig;
import com.tcl.dias.serviceactivation.activation.netp.beans.SLARWConfig;
import com.tcl.dias.serviceactivation.activation.netp.beans.SNMPParameters;
import com.tcl.dias.serviceactivation.activation.netp.beans.StaticRoutes;
import com.tcl.dias.serviceactivation.activation.netp.beans.StaticRoutingProtocol;
import com.tcl.dias.serviceactivation.activation.netp.beans.UAImportMap;
import com.tcl.dias.serviceactivation.activation.netp.beans.VPLSService;
import com.tcl.dias.serviceactivation.activation.netp.beans.VPNSolutionTable;
import com.tcl.dias.serviceactivation.activation.netp.beans.VUTMService;
import com.tcl.dias.serviceactivation.activation.netp.beans.VirtualRouteForwardingServiceInstance;
import com.tcl.dias.serviceactivation.activation.netp.beans.WANInterface;
import com.tcl.dias.serviceactivation.activation.netp.beans.WimaxLastMile;
import com.tcl.dias.serviceactivation.activation.netp.beans.IPService.WanRoutingProtocol;
import com.tcl.dias.serviceactivation.activation.netp.beans.RemoteSite.NniCPEWANRouting;
import com.tcl.dias.serviceactivation.constants.CramerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class NetpService {
	private static final Logger logger = LoggerFactory.getLogger(NetpService.class);

	/*public static void main(String[] args)
			throws JsonParseException, JsonMappingException, TclCommonException, IOException {
		System.out.println("Main started");
		NetpService netpService = new NetpService();
		netpService.constructNetpIPServiceActivationRequest();
		System.out.println("Main ended");
	}*/

	public void constructNetpIPServiceActivationRequest()
			throws TclCommonException, JsonParseException, JsonMappingException, IOException {

		PerformIPServiceActivation performIPServiceActivation = new PerformIPServiceActivation();
		performIPServiceActivation.setHeader(constructAceHeader());
		performIPServiceActivation.setOrderDetails(constructOrderInfo3());
		ObjectMapper xmlMapper = new ObjectMapper();
		String jsonString = xmlMapper.writeValueAsString(performIPServiceActivation);
		System.out.println("json => " + jsonString);
		System.out.println("xml => " + jaxbObjectToXML(performIPServiceActivation));
	}

	private static String jaxbObjectToXML(PerformIPServiceActivation performIPServiceActivation) {
	    String xmlString = "";
	    try {
	        JAXBContext context = JAXBContext.newInstance(PerformIPServiceActivation.class);
	        Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML
	        StringWriter sw = new StringWriter();
	        m.marshal(performIPServiceActivation, sw);
	        xmlString = sw.toString();
	    } catch (JAXBException e) {
	        e.printStackTrace();
	    }
	    return xmlString;
	}
	
	private ACEHeader constructAceHeader() throws TclCommonException {
		ACEHeader aceHeader = new ACEHeader();
		try {
			aceHeader.setAuthUser("");
			aceHeader.setOriginatingSystem(CramerConstants.OPTIMUS);
			GregorianCalendar gcal = new GregorianCalendar();
			XMLGregorianCalendar xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
			aceHeader.setOriginationTime(xgcal);
			aceHeader.setRequestID("123456");
		} catch (Exception e) {
			throw new TclCommonException("Exception in constructAceHeader {}", e);
		}
		return aceHeader;
	}

	private OrderInfo3 constructOrderInfo3() {
		OrderInfo3 orderInfo3 = new OrderInfo3();
		orderInfo3.setAddOns(constructAddOnFeatures());
		orderInfo3.setBurstableBandwidth(constructBandwidth());
		orderInfo3.setCopfId("");
		orderInfo3.setCustomerDetails(constructCustomer());
		orderInfo3.setIsChildObjectInstanceModified(false);
		orderInfo3.setIsDowntimeRequired(false);
		orderInfo3.setIsObjectInstanceModified(false);
		orderInfo3.setOldServiceId("");
		orderInfo3.setOptyBidCategory("");
		orderInfo3.setOrderCategory(OrderCategory.CUSTOMER_ORDER);
		orderInfo3.setOrderType(OrderType2.NEW);
		orderInfo3.setScheduleId("");
		orderInfo3.setService(constructService());
		orderInfo3.setServiceBandwidth(constructBandwidth());
		orderInfo3.setServiceId("");
		orderInfo3.setSolutionId("");
		return orderInfo3;
	}

	private com.tcl.dias.serviceactivation.activation.netp.beans.OrderInfo3.Service constructService() {
		com.tcl.dias.serviceactivation.activation.netp.beans.OrderInfo3.Service service = new com.tcl.dias.serviceactivation.activation.netp.beans.OrderInfo3.Service();
		service.setCambiumLastmile(constructCambiumLastmile());
		service.setDmvpnService(constructDMVPNService());
		service.setErsService(constructP2PL2VPNService());
		service.setEwsService(constructP2PL2VPNService());
		service.setGvpnService(constructGVPNService());
		service.setHuaweiEthernetAccessService(constructEthernetAccessService());
		service.setHvpls(constructHVPLS());
		service.setIaService(constructIAService());
		service.setIasGipvcService(constructIASGIPVCService());
		service.setIasMVoipService(constructIASMVOIPService());
		service.setIpsecService(constructIPSECService());
		service.setRadwin5KLastmile(constructRadwin5KLastmile());
		service.setRoadWarriorService(constructRoadWarriorService());
		service.setVplsService(constructVPLSService());
		service.setVutmService(constructVutmService());
		service.setWimaxLastmile(constructWimaxLastmile());
		return service;
	}

	private WimaxLastMile constructWimaxLastmile() {
		WimaxLastMile wimaxLastMile = new WimaxLastMile();
		wimaxLastMile.setBSName("");
		wimaxLastMile.setBTSIP("");
		wimaxLastMile.setGatewayMgmtIP(constructIPV4Address());
		wimaxLastMile.setInstanceID("");
		wimaxLastMile.setIsChildObjectInstanceModified(false);
		wimaxLastMile.setIsObjectInstanceModified(false);
		wimaxLastMile.setPortSpeed(constructBandwidth());
		wimaxLastMile.setServiceSubType("");
		wimaxLastMile.setServiceType("");
		wimaxLastMile.setSUMACAddress("");
		wimaxLastMile.setSUMgmtIP(constructIPV4Address());
		wimaxLastMile.setSUMgmtSubnet(constructIPV4Address());
		return wimaxLastMile;
	}

	private VUTMService constructVutmService() {
		VUTMService vutmService = new VUTMService();
		vutmService.setBurstableBandwidth(constructBandwidth());
		vutmService.setCSSSAMID("");
		vutmService.setInstanceID("");
		vutmService.setIsObjectInstanceModified(false);
		vutmService.setQos(constructQoS());
		vutmService.setRedundancyRole("");
		vutmService.setServiceBandwidth(constructBandwidth());
		vutmService.setServiceSubType("");
		vutmService.setServiceType("");
		vutmService.setSolutionTable(constructVPNSolutionTable());
		return vutmService;
	}

	private VPNSolutionTable constructVPNSolutionTable() {
		VPNSolutionTable vpnSolutionTable = new VPNSolutionTable();
		vpnSolutionTable.setIsObjectInstanceModified(false);
		vpnSolutionTable.setSolutionID("");
		return vpnSolutionTable;
	}

	private VPLSService constructVPLSService() {
		VPLSService vplsService = new VPLSService();
		vplsService.setALUServiceID("");
		vplsService.setALUServiceName("");
		vplsService.setBurstableBandwidth(constructBandwidth());
		vplsService.setCSSSAMID("");
		vplsService.setDataTransferCommit("");
		vplsService.setDataTransferCommitUnit("");
		vplsService.setDescriptionFreeText("");
		vplsService.setExtendedLAN(constructExtendedLAN());
		vplsService.setFlexiCOSIdentifier("");
		vplsService.setInstanceID("");
		vplsService.setInterfaceDescriptionServiceTag("");
		vplsService.setIrsRouter(constructIrsRouter());
		vplsService.setIsAPSEnabled(false);
		vplsService.setIsChildObjectInstanceModified(false);
		vplsService.setIsConfigManaged(false);
		vplsService.setIsIDCService(false);
		vplsService.setIsObjectInstanceModified(false);
		vplsService.setL3NNIunManagedPartnerDevice(constructPartnerDevice());
		vplsService.setPEWANAdditionalStaticRoutes(constructStaticRoutes());
		vplsService.setQos(constructQoS());
		vplsService.setRedundancyRole("");
		vplsService.setSAMCustomerDescription("");
		vplsService.setSDPConfig(constructSDPConfig());
		vplsService.setServiceBandwidth(constructBandwidth());
		vplsService.setServiceSubType("");
		vplsService.setServiceType("");
		vplsService.setSolutionTable(constructVPNSolutionTable());
		vplsService.setUAExportMap(constructRoutingPolicy());
		vplsService.setUAFlag("");
		vplsService.setUAImportMap(constructUAImportMap());
		vplsService.setUsageModel("");
		vplsService.setVrf(constructVirtualRouteForwardingServiceInstance());
		vplsService.setWanRoutingProtocol(constructWANRoutingProtocol());
		return vplsService;
	}

	private IRSRouter constructIrsRouter() {
		// TODO Auto-generated method stub
		return null;
	}

	private WanRoutingProtocol constructWANRoutingProtocol() {
		WanRoutingProtocol wanRoutingProtocol = new WanRoutingProtocol();
		wanRoutingProtocol.setBgpRoutingProtocol(constructBGPRoutingProtocol());
		wanRoutingProtocol.setEigprRoutingProtocol(constructEIGRPRoutingProtocol());
		wanRoutingProtocol.setOspfRoutingProtocol(constructOspfRoutingProtocol());
		wanRoutingProtocol.setRipRoutingProtocol(constructRipRoutingProtocol());
		wanRoutingProtocol.setStaticRoutingProtocol(constructStaticRoutingProtocol());
		return wanRoutingProtocol;
	}

	private RIPRoutingProtocol constructRipRoutingProtocol() {
		// TODO Auto-generated method stub
		return null;
	}

	private OSPFRoutingProtocol constructOspfRoutingProtocol() {
		// TODO Auto-generated method stub
		return null;
	}

	private EIGRPRoutingProtocol constructEIGRPRoutingProtocol() {
		// TODO Auto-generated method stub
		return null;
	}

	private UAImportMap constructUAImportMap() {
		// TODO Auto-generated method stub
		return null;
	}

	private SDPConfig constructSDPConfig() {
		SDPConfig sdpConfig = new SDPConfig();
		sdpConfig.setIsObjectInstanceModified(false);
		sdpConfig.setMode("");
		sdpConfig.setMtu("");
		sdpConfig.setSDPLSPName("");
		return sdpConfig;
	}

	private ExtendedLAN constructExtendedLAN() {
		ExtendedLAN extendedLAN = new ExtendedLAN();
		extendedLAN.setIsEnabled(false);
		extendedLAN.setIsObjectInstanceModified(false);
		extendedLAN.setNumberOfMacAddresses(0);
		return extendedLAN;
	}

	private RoadWarriorService constructRoadWarriorService() {
		RoadWarriorService roadWarriorService = new RoadWarriorService();
		roadWarriorService.setALUServiceID("");
		roadWarriorService.setALUServiceName("");
		roadWarriorService.setBurstableBandwidth(constructBandwidth());
		roadWarriorService.setCSSSAMID("");
		roadWarriorService.setDataTransferCommit("");
		roadWarriorService.setDataTransferCommitUnit("");
		roadWarriorService.setDescriptionFreeText("");
		roadWarriorService.setExtendedLAN(constructExtendedLAN());
		roadWarriorService.setFlexiCOSIdentifier("");
		roadWarriorService.setInstanceID("");
		roadWarriorService.setInterfaceDescriptionServiceTag("");
		roadWarriorService.setIrsRouter(constructIrsRouter());
		roadWarriorService.setIsAPSEnabled(false);
		roadWarriorService.setIsChildObjectInstanceModified(false);
		roadWarriorService.setIsConfigManaged(false);
		roadWarriorService.setIsIDCService(false);
		roadWarriorService.setIsObjectInstanceModified(false);
		roadWarriorService.setL3NNIunManagedPartnerDevice(constructPartnerDevice());
		roadWarriorService.setPEWANAdditionalStaticRoutes(constructStaticRoutes());
		roadWarriorService.setQos(constructQoS());
		roadWarriorService.setRedundancyRole("");
		roadWarriorService.setSAMCustomerDescription("");
		roadWarriorService.setServiceBandwidth(constructBandwidth());
		roadWarriorService.setServiceSubType("");
		roadWarriorService.setServiceType("");
		roadWarriorService.setSolutionTable(constructVPNSolutionTable());
		roadWarriorService.setUAExportMap(constructRoutingPolicy());
		roadWarriorService.setUAFlag("");
		roadWarriorService.setUAImportMap(constructUAImportMap());
		roadWarriorService.setUsageModel("");
		roadWarriorService.setVrf(constructVirtualRouteForwardingServiceInstance());
		roadWarriorService.setWanRoutingProtocol(constructWANRoutingProtocol());
		return roadWarriorService;
	}

	private Radwin5KLastmile constructRadwin5KLastmile() {
		Radwin5KLastmile radwin5kLastmile = new Radwin5KLastmile();
		radwin5kLastmile.setBSName("");
		radwin5kLastmile.setBTSIP("");
		radwin5kLastmile.setField1("");
		radwin5kLastmile.setField2("");
		radwin5kLastmile.setField3("");
		radwin5kLastmile.setField4("");
		radwin5kLastmile.setHBSConfigParameters(constructHBSConfigParameters());
		radwin5kLastmile.setHSUConfigParameters(constructHSUConfigParameters());
		radwin5kLastmile.setInstanceID("");
		radwin5kLastmile.setIsChildObjectInstanceModified(false);
		radwin5kLastmile.setIsObjectInstanceModified(false);
		radwin5kLastmile.setPortSpeed(constructBandwidth());
		radwin5kLastmile.setServiceSubType("");
		radwin5kLastmile.setServiceType("");
		return radwin5kLastmile;
	}

	private HBSConfigParameters constructHBSConfigParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	private HSUConfigParameters constructHSUConfigParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	private IPSECService constructIPSECService() {
		IPSECService ipsecService = new IPSECService();
		ipsecService.setALUServiceID("");
		ipsecService.setALUServiceName("");
		ipsecService.setBurstableBandwidth(constructBandwidth());
		ipsecService.setCSSSAMID("");
		ipsecService.setDataTransferCommit("");
		ipsecService.setDataTransferCommitUnit("");
		ipsecService.setDescriptionFreeText("");
		ipsecService.setExtendedLAN(constructExtendedLAN());
		ipsecService.setFlexiCOSIdentifier("");
		ipsecService.setInstanceID("");
		ipsecService.setInterfaceDescriptionServiceTag("");
		ipsecService.setIrsRouter(constructIrsRouter());
		ipsecService.setIsAPSEnabled(false);
		ipsecService.setIsChildObjectInstanceModified(false);
		ipsecService.setIsConfigManaged(false);
		ipsecService.setIsIDCService(false);
		ipsecService.setIsObjectInstanceModified(false);
		ipsecService.setL3NNIunManagedPartnerDevice(constructPartnerDevice());
		ipsecService.setPEWANAdditionalStaticRoutes(constructStaticRoutes());
		ipsecService.setQos(constructQoS());
		ipsecService.setRedundancyRole("");
		ipsecService.setSAMCustomerDescription("");
		ipsecService.setServiceBandwidth(constructBandwidth());
		ipsecService.setServiceSubType("");
		ipsecService.setServiceType("");
		ipsecService.setSolutionTable(constructVPNSolutionTable());
		ipsecService.setUAExportMap(constructRoutingPolicy());
		ipsecService.setUAFlag("");
		ipsecService.setUAImportMap(constructUAImportMap());
		ipsecService.setUsageModel("");
		ipsecService.setVrf(constructVirtualRouteForwardingServiceInstance());
		ipsecService.setWanRoutingProtocol(constructWANRoutingProtocol());
		return ipsecService;
	}

	private IASMVOIPService constructIASMVOIPService() {
		IASMVOIPService iasmvoipService = new IASMVOIPService();
		iasmvoipService.setALUServiceID("");
		iasmvoipService.setALUServiceName("");
		iasmvoipService.setBurstableBandwidth(constructBandwidth());
		iasmvoipService.setCSSSAMID("");
		iasmvoipService.setDataTransferCommit("");
		iasmvoipService.setDataTransferCommitUnit("");
		iasmvoipService.setDescriptionFreeText("");
		iasmvoipService.setExtendedLAN(constructExtendedLAN());
		iasmvoipService.setFlexiCOSIdentifier("");
		iasmvoipService.setInstanceID("");
		iasmvoipService.setInterfaceDescriptionServiceTag("");
		iasmvoipService.setIrsRouter(constructIrsRouter());
		iasmvoipService.setIsAPSEnabled(false);
		iasmvoipService.setIsChildObjectInstanceModified(false);
		iasmvoipService.setIsConfigManaged(false);
		iasmvoipService.setIsIDCService(false);
		iasmvoipService.setIsObjectInstanceModified(false);
		iasmvoipService.setL3NNIunManagedPartnerDevice(constructPartnerDevice());
		iasmvoipService.setPEWANAdditionalStaticRoutes(constructStaticRoutes());
		iasmvoipService.setQos(constructQoS());
		iasmvoipService.setRedundancyRole("");
		iasmvoipService.setSAMCustomerDescription("");
		iasmvoipService.setServiceBandwidth(constructBandwidth());
		iasmvoipService.setServiceSubType("");
		iasmvoipService.setServiceType("");
		iasmvoipService.setSolutionTable(constructVPNSolutionTable());
		iasmvoipService.setUAExportMap(constructRoutingPolicy());
		iasmvoipService.setUAFlag("");
		iasmvoipService.setUAImportMap(constructUAImportMap());
		iasmvoipService.setUsageModel("");
		iasmvoipService.setVrf(constructVirtualRouteForwardingServiceInstance());
		iasmvoipService.setWanRoutingProtocol(constructWANRoutingProtocol());
		return iasmvoipService;
	}

	private IASGIPVCService constructIASGIPVCService() {
		IASGIPVCService iasgipvcService = new IASGIPVCService();
		iasgipvcService.setALUServiceID("");
		iasgipvcService.setALUServiceName("");
		iasgipvcService.setBurstableBandwidth(constructBandwidth());
		iasgipvcService.setCSSSAMID("");
		iasgipvcService.setDataTransferCommit("");
		iasgipvcService.setDataTransferCommitUnit("");
		iasgipvcService.setDescriptionFreeText("");
		iasgipvcService.setExtendedLAN(constructExtendedLAN());
		iasgipvcService.setFlexiCOSIdentifier("");
		iasgipvcService.setInstanceID("");
		iasgipvcService.setInterfaceDescriptionServiceTag("");
		iasgipvcService.setIrsRouter(constructIrsRouter());
		iasgipvcService.setIsAPSEnabled(false);
		iasgipvcService.setIsChildObjectInstanceModified(false);
		iasgipvcService.setIsConfigManaged(false);
		iasgipvcService.setIsIDCService(false);
		iasgipvcService.setIsObjectInstanceModified(false);
		iasgipvcService.setL3NNIunManagedPartnerDevice(constructPartnerDevice());
		iasgipvcService.setPEWANAdditionalStaticRoutes(constructStaticRoutes());
		iasgipvcService.setQos(constructQoS());
		iasgipvcService.setRedundancyRole("");
		iasgipvcService.setSAMCustomerDescription("");
		iasgipvcService.setServiceBandwidth(constructBandwidth());
		iasgipvcService.setServiceSubType("");
		iasgipvcService.setServiceType("");
		iasgipvcService.setSolutionTable(constructVPNSolutionTable());
		iasgipvcService.setUAExportMap(constructRoutingPolicy());
		iasgipvcService.setUAFlag("");
		iasgipvcService.setUAImportMap(constructUAImportMap());
		iasgipvcService.setUsageModel("");
		iasgipvcService.setVrf(constructVirtualRouteForwardingServiceInstance());
		iasgipvcService.setWanRoutingProtocol(constructWANRoutingProtocol());
		return iasgipvcService;
	}

	private IAService constructIAService() {
		IAService iaService = new IAService();
		iaService.setALUServiceID("");
		iaService.setALUServiceName("");
		iaService.setBurstableBandwidth(constructBandwidth());
		iaService.setCSSSAMID("");
		iaService.setDataTransferCommit("");
		iaService.setDataTransferCommitUnit("");
		iaService.setDescriptionFreeText("");
		iaService.setExtendedLAN(constructExtendedLAN());
		iaService.setFlexiCOSIdentifier("");
		iaService.setInstanceID("");
		iaService.setInterfaceDescriptionServiceTag("");
		iaService.setIrsRouter(constructIrsRouter());
		iaService.setIsAPSEnabled(false);
		iaService.setIsChildObjectInstanceModified(false);
		iaService.setIsConfigManaged(false);
		iaService.setIsIDCService(false);
		iaService.setIsObjectInstanceModified(false);
		iaService.setL3NNIunManagedPartnerDevice(constructPartnerDevice());
		iaService.setPEWANAdditionalStaticRoutes(constructStaticRoutes());
		iaService.setQos(constructQoS());
		iaService.setRedundancyRole("");
		iaService.setSAMCustomerDescription("");
		iaService.setServiceBandwidth(constructBandwidth());
		iaService.setServiceSubType("");
		iaService.setServiceType("");
		iaService.setSolutionTable(constructVPNSolutionTable());
		iaService.setUAExportMap(constructRoutingPolicy());
		iaService.setUAFlag("");
		iaService.setUAImportMap(constructUAImportMap());
		iaService.setUsageModel("");
		iaService.setVrf(constructVirtualRouteForwardingServiceInstance());
		iaService.setWanRoutingProtocol(constructWANRoutingProtocol());
		return iaService;
	}

	private HVPLS constructHVPLS() {
		HVPLS hvpls = new HVPLS();
		hvpls.setALUServiceID("");
		hvpls.setALUServiceName("");
		hvpls.setBurstableBandwidth(constructBandwidth());
		hvpls.setCSSSAMID("");
		hvpls.setDataTransferCommit("");
		hvpls.setDataTransferCommitUnit("");
		hvpls.setDescriptionFreeText("");
		hvpls.setExtendedLAN(constructExtendedLAN());
		hvpls.setFlexiCOSIdentifier("");
		hvpls.setInstanceID("");
		hvpls.setInterfaceDescriptionServiceTag("");
		hvpls.setIrsRouter(constructIrsRouter());
		hvpls.setIsAPSEnabled(false);
		hvpls.setIsChildObjectInstanceModified(false);
		hvpls.setIsConfigManaged(false);
		hvpls.setIsIDCService(false);
		hvpls.setIsObjectInstanceModified(false);
		hvpls.setL3NNIunManagedPartnerDevice(constructPartnerDevice());
		hvpls.setPEWANAdditionalStaticRoutes(constructStaticRoutes());
		hvpls.setQos(constructQoS());
		hvpls.setRedundancyRole("");
		hvpls.setSAMCustomerDescription("");
		hvpls.setServiceBandwidth(constructBandwidth());
		hvpls.setServiceSubType("");
		hvpls.setServiceType("");
		hvpls.setSolutionTable(constructVPNSolutionTable());
		hvpls.setUAExportMap(constructRoutingPolicy());
		hvpls.setUAFlag("");
		hvpls.setUAImportMap(constructUAImportMap());
		hvpls.setUsageModel("");
		hvpls.setWanRoutingProtocol(constructWANRoutingProtocol());
		return hvpls;
	}

	private EthernetAccessService constructEthernetAccessService() {
		EthernetAccessService ethernetAccessService = new EthernetAccessService();
		ethernetAccessService.setBurstableBandwidth(constructBandwidth());
		ethernetAccessService.setInstanceID("");
		ethernetAccessService.setIsDowntimeRequired(false);
		ethernetAccessService.setIsObjectInstanceModified(false);
		ethernetAccessService.setNetworkServiceType("");
		ethernetAccessService.setOverrideOutofBandConfig("");
		ethernetAccessService.setServiceBandwidth(constructBandwidth());
		ethernetAccessService.setServiceSubType("");
		ethernetAccessService.setServiceType("");
		// TODO Auto-generated method stub
		return ethernetAccessService;
	}

	private GVPNService constructGVPNService() {
		GVPNService gvpnService = new GVPNService();
		gvpnService.setALUServiceID("");
		gvpnService.setALUServiceName("");
		gvpnService.setBurstableBandwidth(constructBandwidth());
		gvpnService.setCSSSAMID("");
		gvpnService.setCurrentCustomerSiteId("");
		gvpnService.setCurrentLegID("");
		gvpnService.setDataTransferCommit("");
		gvpnService.setDataTransferCommitUnit("");
		gvpnService.setDescriptionFreeText("");
		gvpnService.setExtendedLAN(constructExtendedLAN());
		gvpnService.setFlexiCOSIdentifier("");
		gvpnService.setInstanceID("");
		gvpnService.setInterfaceDescriptionServiceTag("");
		gvpnService.setIrsRouter(constructIrsRouter());
		gvpnService.setIsAPSEnabled(false);
		gvpnService.setIsChildObjectInstanceModified(false);
		gvpnService.setIsConfigManaged(false);
		gvpnService.setIsIDCService(false);
		gvpnService.setIsObjectInstanceModified(false);
		gvpnService.setIsProactiveMonitoringEnabled("");
		gvpnService.setIsVRFLiteEnabled(false);
		gvpnService.setL3NNIunManagedPartnerDevice(constructPartnerDevice());
		gvpnService.setPEWANAdditionalStaticRoutes(constructStaticRoutes());
		gvpnService.setPingV4IPAddress(constructIPV4Address());
		gvpnService.setPingV6IPAddress(constructIPV6Address());
		gvpnService.setQos(constructQoS());
		gvpnService.setRedundancyRole("");
		gvpnService.setSAMCustomerDescription("");
		gvpnService.setServiceBandwidth(constructBandwidth());
		gvpnService.setServiceSubType("");
		gvpnService.setServiceType("");
		gvpnService.setSolutionTable(constructVPNSolutionTable());
		gvpnService.setUAExportMap(constructRoutingPolicy());
		gvpnService.setUAFlag("");
		gvpnService.setUAImportMap(constructUAImportMap());
		gvpnService.setUsageModel("");
		gvpnService.setVrf(constructVirtualRouteForwardingServiceInstance());
		gvpnService.setWanRoutingProtocol(constructWANRoutingProtocol());
		return gvpnService;
	}

	private P2PL2VPNService constructP2PL2VPNService() {
		P2PL2VPNService p2pl2vpnService = new P2PL2VPNService();
		p2pl2vpnService.setALUServiceID("");
		p2pl2vpnService.setALUServiceName("");
		p2pl2vpnService.setBurstableBandwidth(constructBandwidth());
		p2pl2vpnService.setCSSSAMID("");
		p2pl2vpnService.setCurrentCustomerSiteId("");
		p2pl2vpnService.setCurrentLegID("");
		p2pl2vpnService.setDataTransferCommit("");
		p2pl2vpnService.setDataTransferCommitUnit("");
		p2pl2vpnService.setDescriptionFreeText("");
		p2pl2vpnService.setExtendedLAN(constructExtendedLAN());
		p2pl2vpnService.setFlexiCOSIdentifier("");
		p2pl2vpnService.setInstanceID("");
		p2pl2vpnService.setInterfaceDescriptionServiceTag("");
		p2pl2vpnService.setIrsRouter(constructIrsRouter());
		p2pl2vpnService.setIsAPSEnabled(false);
		p2pl2vpnService.setIsChildObjectInstanceModified(false);
		p2pl2vpnService.setIsConfigManaged(false);
		p2pl2vpnService.setIsIDCService(false);
		p2pl2vpnService.setIsObjectInstanceModified(false);
		p2pl2vpnService.setIsProactiveMonitoringEnabled("");
		p2pl2vpnService.setIsVRFLiteEnabled(false);
		p2pl2vpnService.setL3NNIunManagedPartnerDevice(constructPartnerDevice());
		p2pl2vpnService.setPeerPEhostname("");
		p2pl2vpnService.setPEWANAdditionalStaticRoutes(constructStaticRoutes());
		p2pl2vpnService.setPingV4IPAddress(constructIPV4Address());
		p2pl2vpnService.setPingV6IPAddress(constructIPV6Address());
		p2pl2vpnService.setQos(constructQoS());
		p2pl2vpnService.setRedundancyRole("");
		p2pl2vpnService.setSAMCustomerDescription("");
		p2pl2vpnService.setSDPConfig(constructSDPConfig());
		p2pl2vpnService.setServiceBandwidth(constructBandwidth());
		p2pl2vpnService.setServiceSubType("");
		p2pl2vpnService.setServiceType("");
		p2pl2vpnService.setSolutionTable(constructVPNSolutionTable());
		p2pl2vpnService.setUAExportMap(constructRoutingPolicy());
		p2pl2vpnService.setUAFlag("");
		p2pl2vpnService.setUAImportMap(constructUAImportMap());
		p2pl2vpnService.setUsageModel("");
		p2pl2vpnService.setVCID(0);
		p2pl2vpnService.setVrf(constructVirtualRouteForwardingServiceInstance());
		p2pl2vpnService.setWanRoutingProtocol(constructWANRoutingProtocol());
		return p2pl2vpnService;
	}

	private DMVPNService constructDMVPNService() {
		DMVPNService dmvpnService = new DMVPNService();
		dmvpnService.setALUServiceID("");
		dmvpnService.setALUServiceName("");
		dmvpnService.setBurstableBandwidth(constructBandwidth());
		dmvpnService.setCSSSAMID("");
		dmvpnService.setCurrentCustomerSiteId("");
		dmvpnService.setCurrentLegID("");
		dmvpnService.setDataTransferCommit("");
		dmvpnService.setDataTransferCommitUnit("");
		dmvpnService.setDescriptionFreeText("");
		dmvpnService.setExtendedLAN(constructExtendedLAN());
		dmvpnService.setFlexiCOSIdentifier("");
		dmvpnService.setInstanceID("");
		dmvpnService.setInterfaceDescriptionServiceTag("");
		dmvpnService.setIrsRouter(constructIrsRouter());
		dmvpnService.setIsAPSEnabled(false);
		dmvpnService.setIsChildObjectInstanceModified(false);
		dmvpnService.setIsConfigManaged(false);
		dmvpnService.setIsIDCService(false);
		dmvpnService.setIsObjectInstanceModified(false);
		dmvpnService.setIsProactiveMonitoringEnabled("");
		dmvpnService.setIsVRFLiteEnabled(false);
		dmvpnService.setL3NNIunManagedPartnerDevice(constructPartnerDevice());
		dmvpnService.setPEWANAdditionalStaticRoutes(constructStaticRoutes());
		dmvpnService.setPingV4IPAddress(constructIPV4Address());
		dmvpnService.setPingV6IPAddress(constructIPV6Address());
		dmvpnService.setQos(constructQoS());
		dmvpnService.setRedundancyRole("");
		dmvpnService.setSAMCustomerDescription("");
		dmvpnService.setServiceBandwidth(constructBandwidth());
		dmvpnService.setServiceSubType("");
		dmvpnService.setServiceType("");
		dmvpnService.setSolutionTable(constructVPNSolutionTable());
		dmvpnService.setUAExportMap(constructRoutingPolicy());
		dmvpnService.setUAFlag("");
		dmvpnService.setUAImportMap(constructUAImportMap());
		dmvpnService.setUsageModel("");
		dmvpnService.setVrf(constructVirtualRouteForwardingServiceInstance());
		dmvpnService.setWanRoutingProtocol(constructWANRoutingProtocol());
		return dmvpnService;
	}

	private CambiumLastmile constructCambiumLastmile() {
		CambiumLastmile cambiumLastmile = new CambiumLastmile();
		cambiumLastmile.setAPIVersion("");
		cambiumLastmile.setBridgeEntryTimeout("");
		cambiumLastmile.setBSName("");
		cambiumLastmile.setBTSIP("");
		cambiumLastmile.setCambiumAAAConfig(constructCambiumAAAConfig());
		cambiumLastmile.setCambiumAntennaParameters(contructCambiumAntennaParameters());
		cambiumLastmile.setCambiumSiteDetails(constructCambiumSiteDetails());
		cambiumLastmile.setCambiumVLANAttributes(constructCambiumVLANAttributes());
		cambiumLastmile.setColourCode("");
		cambiumLastmile.setDeviceDefaultReset("");
		cambiumLastmile.setDeviceType("");
		cambiumLastmile.setDHCPState("");
		cambiumLastmile.setDynamicRateAdapt("");
		cambiumLastmile.setEnableLargeDataVCQ("");
		cambiumLastmile.setEthernetLink("");
		cambiumLastmile.setField11("");
		cambiumLastmile.setField12("");
		cambiumLastmile.setFrameTimingPulseGated("");
		cambiumLastmile.setGatewayMgmtIP(constructIPV4Address());
		cambiumLastmile.setInstallationColourCode("");
		cambiumLastmile.setInstanceID("");
		cambiumLastmile.setIsChildObjectInstanceModified(false);
		cambiumLastmile.setIsObjectInstanceModified(false);
		cambiumLastmile.setLinkSpeed("");
		cambiumLastmile.setMulticastDestAddress("");
		cambiumLastmile.setPortSpeed(constructBandwidth());
		cambiumLastmile.setPowerupMode("");
		cambiumLastmile.setRegionCode("");
		cambiumLastmile.setRFScanList("");
		cambiumLastmile.setServiceSubType("");
		cambiumLastmile.setServiceType("");
		cambiumLastmile.setSNMPParameters(constructSNMPParameters());
		cambiumLastmile.setSUMACAddress("");
		cambiumLastmile.setSUMgmtIP(constructIPV4Address());
		cambiumLastmile.setSUMgmtSubnet(constructIPV4Address());
		cambiumLastmile.setTransmitterOutputPower(0);
		cambiumLastmile.setViewableToGuestUsers("");
		cambiumLastmile.setWebpageAutoUpdate(0);
		return cambiumLastmile;
	}

	private SNMPParameters constructSNMPParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	private CambiumVLANAttributes constructCambiumVLANAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	private CambiumSite constructCambiumSiteDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	private CambiumAntennaParameters contructCambiumAntennaParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	private CambiumAAAConfig constructCambiumAAAConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	private Customer constructCustomer() {
		Customer customer = new Customer();
		customer.setAddress(constructAddress());
		customer.setALUCustomerID("");
		customer.setCategory("");
		customer.setCustomerContact("");
		customer.setEmailID("");
		customer.setId("");
		customer.setIsChildObjectInstanceModified(false);
		customer.setIsObjectInstanceModified(false);
		customer.setName("");
		customer.setPhone("");
		customer.setType("");
		return customer;
	}

	private Address constructAddress() {
		Address address = new Address();
		address.setAddressLine1("");
		address.setAddressLine2("");
		address.setCity("");
		address.setCityCode("");
		address.setCountry("");
		address.setLocation("");
		address.setPincode("");
		address.setState("");
		return address;
	}

	private Bandwidth constructBandwidth() {
		Bandwidth bandwidth = new Bandwidth();
		bandwidth.setIsObjectInstanceModified(false);
		bandwidth.setSpeed(0f);
		bandwidth.setUnit(BandwidthUnit.MBPS);
		return bandwidth;
	}

	private AddonFeatures constructAddOnFeatures() {
		AddonFeatures addonFeatures = new AddonFeatures();
		addonFeatures.setHostedExchangeCEConfig(constructHostedExchangeCEConfig());
		addonFeatures.setIsITIMSNATConfigEnabled("");
		addonFeatures.setIsTelepresenceEnabled("");
		addonFeatures.setIZODetails(constructIZO());
		addonFeatures.setMulticasting(constructMulticasting());
		addonFeatures.setNNICPEConfig(constructRemoteSite());
		addonFeatures.setSLARWConfig(constructSLARWConfig());
		addonFeatures.setVsatCPEConfig(constructRemoteSite());
		return addonFeatures;
	}

	private SLARWConfig constructSLARWConfig() {
		SLARWConfig sLARWConfig = new SLARWConfig();
		sLARWConfig.setCpeMode("");
		sLARWConfig.setIsFirstSite(false);
		sLARWConfig.setIsObjectInstanceModified(false);
		sLARWConfig.setMCPEWANIPPool(constructIPV4Address());
		sLARWConfig.setOldmCPEWANIPPool(constructIPV4Address());
		sLARWConfig.setOldsrpWANIPPool(constructIPV4Address());
		sLARWConfig.setPeWANIPPool(constructIPV4Address());
		sLARWConfig.setRemoteCPE(constructRemoteSite());
		sLARWConfig.setSrpWANIPPool(constructIPV4Address());
		return sLARWConfig;
	}

	private RemoteSite constructRemoteSite() {
		RemoteSite remoteSite = new RemoteSite();
		remoteSite.setCPE(constructCustomerPremiseEquipment());
		remoteSite.setNniCPEQoS(constructQoS());
		remoteSite.setNniCPEWANRouting(constructNniCPEWANRouting());
		remoteSite.setUnManagedCEFacingPartnerDevice(constructPartnerDevice());
		return remoteSite;
	}

	private PartnerDevice constructPartnerDevice() {
		PartnerDevice partnerDevice = new PartnerDevice();
		partnerDevice.setHostname("");
		partnerDevice.setIsObjectInstanceModified(false);
		partnerDevice.setPartnerdeviceWANIPv4Address(constructIPV4Address());
		partnerDevice.setPartnerdeviceWANIPv6Address(constructIPV6Address());
		partnerDevice.setType("");
		return partnerDevice;
	}

	private NniCPEWANRouting constructNniCPEWANRouting() {
		NniCPEWANRouting nniCPEWANRouting = new NniCPEWANRouting();
		nniCPEWANRouting.setBgpRoutingProtocol(constructBGPRoutingProtocol());
		nniCPEWANRouting.setStaticRoutes(constructStaticRoutingProtocol());
		return nniCPEWANRouting;
	}

	private BGPRoutingProtocol constructBGPRoutingProtocol() {
		BGPRoutingProtocol bGPRoutingProtocol = new BGPRoutingProtocol();
		bGPRoutingProtocol.setALUBackupPath("");
		bGPRoutingProtocol.setALUBGPMEDValue("");
		bGPRoutingProtocol.setALUBGPPeerName("");
		bGPRoutingProtocol.setALUBGPPeerType("");
		bGPRoutingProtocol.setALUDefaultOriginateConfig(constructALUDefaultOriginateConfig());
		bGPRoutingProtocol.setALUDefaultOriginateV6Config(constructALUDefaultOriginateConfig());
		bGPRoutingProtocol.setALUDisableBGPPeerGrpExtCommunity(false);
		bGPRoutingProtocol.setALUDisableBGPPeerGrpExtCommunity(false);
		bGPRoutingProtocol.setALUkeepAlive("");
		bGPRoutingProtocol.setALUMultiHopValue("");
		bGPRoutingProtocol.setALUmultiPathCost("");
		bGPRoutingProtocol.setASPATH("");
		bGPRoutingProtocol.setAuthenticationMode("");
		bGPRoutingProtocol.setAuthenticationPassword("");
		bGPRoutingProtocol.setBGPNeighborInboundRouteMap("");
		bGPRoutingProtocol.setBGPNeighborInboundRouteMapV6("");
		bGPRoutingProtocol.setBGPNeighborOutboundRouteMap("");
		bGPRoutingProtocol.setBGPNeighborOutboundRouteMapV6("");
		bGPRoutingProtocol.setBgpNeighbourLocalASNumber(constructASNumber());
		bGPRoutingProtocol.setBgpSoftConfigInboundEnabled("");
		bGPRoutingProtocol.setCPEASNumber(constructASNumber());
		bGPRoutingProtocol.setHelloTime(0);
		bGPRoutingProtocol.setHoldTime(0);
		bGPRoutingProtocol.setInboundBGPNeighbourRoutemapName("");
		bGPRoutingProtocol.setInboundBGPv4PrefixesAllowed(constructPrefixList());
		bGPRoutingProtocol.setInboundBGPv6PrefixesAllowed(constructPrefixList());
		bGPRoutingProtocol.setIsASOverriden("");
		bGPRoutingProtocol.setIsAuthenticationRequired(false);
		bGPRoutingProtocol.setIsChildObjectInstanceModified(false);
		bGPRoutingProtocol.setIsEBGPMultihopRequired("");
		bGPRoutingProtocol.setIsLPInVPNPolicyConfigured(false);
		bGPRoutingProtocol.setIsNeighbourShutdownRequired(false);
		bGPRoutingProtocol.setIsObjectInstanceModified(false);
		bGPRoutingProtocol.setIsOriginateDefaultEnabled(false);
		bGPRoutingProtocol.setIsRedistributeConnectedEnabled(false);
		bGPRoutingProtocol.setIsRedistributeStaticEnabled(false);
		bGPRoutingProtocol.setIsSOORequired(false);
		bGPRoutingProtocol.setLocalBGPNeighbourUpdateSource(constructLoopBackInterface());
		bGPRoutingProtocol.setLocalPreference("");
		bGPRoutingProtocol.setLocalPreferenceInVPNPolicy("");
		bGPRoutingProtocol.setLocalPreferenceV6InVPNPolicy("");
		bGPRoutingProtocol.setMaxPrefix(0);
		bGPRoutingProtocol.setMaxPrefixThreshold(0);
		bGPRoutingProtocol.setMetric("");
		bGPRoutingProtocol.setMultihopTTL(0);
		bGPRoutingProtocol.setOutboundBGPv4PrefixesAllowed(constructPrefixList());
		bGPRoutingProtocol.setOutboundBGPv6PrefixesAllowed(constructPrefixList());
		bGPRoutingProtocol.setRemoteASNumber(constructASNumber());
		bGPRoutingProtocol.setRemoteBGPNeighbourUpdateSource(constructLoopBackInterface());
		bGPRoutingProtocol.setRemoteIPV6Address(constructIPV6Address());
		bGPRoutingProtocol.setRemoteIPV6Address(constructIPV6Address());
		bGPRoutingProtocol.setRoutesExchanged(constructRoutesExchanged());
		bGPRoutingProtocol.setSplitHorizon(false);
		return bGPRoutingProtocol;
	}

	private RoutesExchanged constructRoutesExchanged() {
		RoutesExchanged routesExchanged = new RoutesExchanged();
		routesExchanged.setIsObjectInstanceModified(false);
		routesExchanged.setRoute("");
		return routesExchanged;
	}

	private ASNumber constructASNumber() {
		ASNumber asNumber = new ASNumber();
		asNumber.setCategory("");
		asNumber.setIsASNumberCustomerOwned(false);
		asNumber.setIsObjectInstanceModified(false);
		asNumber.setNumber("");
		return asNumber;
	}

	private ALUDefaultOriginateConfig constructALUDefaultOriginateConfig() {
		ALUDefaultOriginateConfig aluDefaultOriginateConfig = new ALUDefaultOriginateConfig();
		aluDefaultOriginateConfig.setDefaultOriginatePrefixList(constructPrefixList());
		return aluDefaultOriginateConfig;
	}

	private CustomerPremiseEquipment constructCustomerPremiseEquipment() {
		CustomerPremiseEquipment customerPremiseEquipment = new CustomerPremiseEquipment();
		customerPremiseEquipment.setCEVRFName(constructVirtualRouteForwardingServiceInstance());
		customerPremiseEquipment.setCpeInitConfigParams(constructCPEInitConfigParams());
		customerPremiseEquipment.setCpeType("");
		customerPremiseEquipment.setHostName("");
		customerPremiseEquipment.setIsCEACEConfigurable(false);
		customerPremiseEquipment.setIsChildObjectInstanceModified(false);
		customerPremiseEquipment.setIsCPEReachable(false);
		customerPremiseEquipment.setLoopbackInterface(constructInterface());
		customerPremiseEquipment.setSnmpServerCommunity("");
		customerPremiseEquipment.setWanInterface(constructWANInterface());
		return customerPremiseEquipment;
	}

	private WANInterface constructWANInterface() {
		WANInterface wanInterface = new WANInterface();
		wanInterface.setHSRP(constructHSRPProtocol());
		// wanInterface.setInterface(constructWANInterface().getInterface());
		wanInterface.setIsChildObjectInstanceModified(false);
		wanInterface.setIsObjectInstanceModified(false);
		wanInterface.setSecondaryV4WANIPAddress(constructIPV4Address());
		wanInterface.setStaticRoutes(constructStaticRoutingProtocol());
		return wanInterface;
	}

	private StaticRoutingProtocol constructStaticRoutingProtocol() {
		StaticRoutingProtocol staticRoutingProtocol = new StaticRoutingProtocol();
		staticRoutingProtocol.setCEWANStaticRoutes(constructStaticRoutes());
		staticRoutingProtocol.setIsChildObjectInstanceModified(false);
		staticRoutingProtocol.setIsObjectInstanceModified(false);
		staticRoutingProtocol.setLocalPreference("");
		staticRoutingProtocol.setLocalPreferenceV6("");
		staticRoutingProtocol.setPEWANStaticRoutes(constructStaticRoutes());
		staticRoutingProtocol.setRedistributionRouteMapName("");
		return staticRoutingProtocol;
	}

	private StaticRoutes constructStaticRoutes() {
		StaticRoutes staticRoutes = new StaticRoutes();
		staticRoutes.setIsObjectInstanceModified(false);
		staticRoutes.setPopCommunity("");
		staticRoutes.setRegionalCommunity("");
		staticRoutes.setServiceCommunity("");
		return staticRoutes;
	}

	private HSRPProtocol constructHSRPProtocol() {
		HSRPProtocol hSRPProtocol = new HSRPProtocol();
		hSRPProtocol.setHelloValue("");
		hSRPProtocol.setHoldValue("");
		hSRPProtocol.setIsObjectInstanceModified(false);
		hSRPProtocol.setPriority(0);
		hSRPProtocol.setRole("");
		hSRPProtocol.setTimerUnit("");
		hSRPProtocol.setVirtualIPAddress("");
		hSRPProtocol.setVirtualIPv6Address("");
		return hSRPProtocol;
	}

	private Interface constructInterface() {
		Interface interfaceObj = new Interface();
		interfaceObj.setInboundAccessControlList(constructAccessControlList());
		interfaceObj.setInboundAccessControlListV6(constructAccessControlList());
		interfaceObj.setIsChildObjectInstanceModified(false);
		interfaceObj.setIsObjectInstanceModified(false);
		interfaceObj.setName("");
		interfaceObj.setOutboundAccessControlList(constructAccessControlList());
		interfaceObj.setOutboundAccessControlListV6(constructAccessControlList());
		interfaceObj.setPhysicalPortName("");
		interfaceObj.setQoS(constructQoS());
		interfaceObj.setV4IpAddress(constructIPV4Address());
		interfaceObj.setV6IpAddress(constructIPV6Address());
		return interfaceObj;
	}

	private CPEInitConfigParams constructCPEInitConfigParams() {
		CPEInitConfigParams cpeInitConfigParams = new CPEInitConfigParams();
		cpeInitConfigParams.setInitEnablePwd("");
		cpeInitConfigParams.setInitLoginPwd("");
		cpeInitConfigParams.setInitLoginUserID("");
		cpeInitConfigParams.setIsSendInitTemplate(false);
		return cpeInitConfigParams;
	}

	private VirtualRouteForwardingServiceInstance constructVirtualRouteForwardingServiceInstance() {
		VirtualRouteForwardingServiceInstance virtualRouteForwardingServiceInstance = new VirtualRouteForwardingServiceInstance();
		virtualRouteForwardingServiceInstance.setALUVPRNExportPolicy(constructRoutingPolicy());
		virtualRouteForwardingServiceInstance.setALUVPRNImportPolicy(constructRoutingPolicy());
		virtualRouteForwardingServiceInstance.setCiscoImportPolicy(constructCiscoImportMap());
		virtualRouteForwardingServiceInstance.setMaximumRoutes(constructMaximumRoutes());
		virtualRouteForwardingServiceInstance.setName("");
		return virtualRouteForwardingServiceInstance;
	}

	private MaximumRoutes constructMaximumRoutes() {
		MaximumRoutes maximumRoutes = new MaximumRoutes();
		maximumRoutes.setThresholdValue(0);
		maximumRoutes.setValue(0);
		maximumRoutes.setWarnOn("");
		return maximumRoutes;
	}

	private CiscoImportMap constructCiscoImportMap() {
		CiscoImportMap ciscoImportMap = new CiscoImportMap();
		ciscoImportMap.setIsPreprovisioned(true);
		ciscoImportMap.setIsStandardPolicy(true);
		ciscoImportMap.setName("");
		return ciscoImportMap;
	}

	private RoutingPolicy constructRoutingPolicy() {
		RoutingPolicy routingPolicy = new RoutingPolicy();
		routingPolicy.setIsObjectInstanceModified(false);
		routingPolicy.setIsPreprovisioned(false);
		routingPolicy.setIsStandardPolicy(false);
		routingPolicy.setMatchCriteria(constructRoutingpolicyMatchCriteria());
		routingPolicy.setName("");
		routingPolicy.setSetCriteria(constructRoutingPolicySetCriteria());
		return routingPolicy;
	}

	private RoutingPolicySetCriteria constructRoutingPolicySetCriteria() {
		RoutingPolicySetCriteria routingPolicySetCriteria = new RoutingPolicySetCriteria();
		routingPolicySetCriteria.setASPathPrepend("");
		routingPolicySetCriteria.setASPathPrependIndex(0);
		routingPolicySetCriteria.setIsObjectInstanceModified(false);
		routingPolicySetCriteria.setLocalPreference("");
		routingPolicySetCriteria.setMetric("");
		routingPolicySetCriteria.setRegexASPath(constructALUAsPathConfig());
		return routingPolicySetCriteria;
	}

	private RoutingPolicyMatchCriteria constructRoutingpolicyMatchCriteria() {
		RoutingPolicyMatchCriteria routingPolicyMatchCriteria = new RoutingPolicyMatchCriteria();
		routingPolicyMatchCriteria.setIsObjectInstanceModified(false);
		routingPolicyMatchCriteria.setPrefixList(constructPrefixList());
		routingPolicyMatchCriteria.setProtocol("");
		routingPolicyMatchCriteria.setRegexASPath(constructALUAsPathConfig());
		return routingPolicyMatchCriteria;
	}

	private ALUASPathConfig constructALUAsPathConfig() {
		ALUASPathConfig aluASPathConfig = new ALUASPathConfig();
		aluASPathConfig.setASPath("");
		aluASPathConfig.setIsObjectInstanceModified(false);
		aluASPathConfig.setName("");
		return aluASPathConfig;
	}

	private PrefixList constructPrefixList() {
		PrefixList prefixList = new PrefixList();
		prefixList.setIsChildObjectInstanceModified(false);
		prefixList.setIsObjectInstanceModified(false);
		prefixList.setIsPreprovisioned(false);
		prefixList.setName("");
		return prefixList;
	}

	private Multicasting constructMulticasting() {
		Multicasting multicasting = new Multicasting();
		multicasting.setAutoDiscoveryOption("");
		multicasting.setDataMDT(constructDataMDT());
		multicasting.setDefaultMDT(constructIPV4Address());
		multicasting.setRPLocation("");
		multicasting.setRPLoopbackList(constructRPLoopbackList());
		multicasting.setType("");
		multicasting.setWanPIMMode("");
		return multicasting;
	}

	private RPLoopbackList constructRPLoopbackList() {
		RPLoopbackList rpLoopbackList = new RPLoopbackList();
		rpLoopbackList.setIsObjectInstanceModified(true);
		return rpLoopbackList;
	}

	private DataMDT constructDataMDT() {
		DataMDT dataMDT = new DataMDT();
		dataMDT.setIPAddress(constructIPV4Address());
		dataMDT.setThreshold(0);
		return dataMDT;
	}

	private IZO constructIZO() {
		IZO izo = new IZO();
		izo.setCloudProvider("");
		izo.setCloudType("");
		izo.setIzoPrivate(false);
		return izo;
	}

	private HostedExchangeCEConfig constructHostedExchangeCEConfig() {
		HostedExchangeCEConfig hostedExchangeCEConfig = new HostedExchangeCEConfig();
		hostedExchangeCEConfig.setNatLoopbackInterface(constructLoopBackInterface());
		hostedExchangeCEConfig.setHostedExchangeACL(constructAccessControlList());
		return hostedExchangeCEConfig;
	}

	private LoopbackInterface constructLoopBackInterface() {
		LoopbackInterface loopbackInterface = new LoopbackInterface();
		loopbackInterface.setInboundAccessControlList(constructAccessControlList());
		loopbackInterface.setInboundAccessControlListV6(constructAccessControlList());
		loopbackInterface.setIsChildObjectInstanceModified(true);
		loopbackInterface.setIsObjectInstanceModified(true);
		loopbackInterface.setName("");
		loopbackInterface.setOutboundAccessControlList(constructAccessControlList());
		loopbackInterface.setOutboundAccessControlListV6(constructAccessControlList());
		loopbackInterface.setPhysicalPortName("");
		loopbackInterface.setQoS(constructQoS());
		loopbackInterface.setV4IpAddress(constructIPV4Address());
		loopbackInterface.setV6IpAddress(constructIPV6Address());
		return loopbackInterface;
	}

	private QoS constructQoS() {
		QoS qoS = new QoS();
		qoS.setALUEgressPolicy(constructALUSchedulerPolicy());
		qoS.setALUIngressPolicy(constructALUSchedulerPolicy());
		qoS.setALUSchedulerPolicy(constructALUSchedulerPolicy());
		qoS.setCosType("6COS");
		qoS.setIsBandwidthApplicable("");
		qoS.setIsChildObjectInstanceModified(false);
		qoS.setIsObjectInstanceModified(false);
		qoS.setQoSProfile("PREMIUM");
		qoS.setQoSTrafficMode("UNICAST");
		return qoS;
	}

	private ALUSchedulerPolicy constructALUSchedulerPolicy() {
		ALUSchedulerPolicy aLUSchedulerPolicy = new ALUSchedulerPolicy();
		aLUSchedulerPolicy.setIsObjectInstanceModified(true);
		aLUSchedulerPolicy.setIsPreprovisioned(false);
		aLUSchedulerPolicy.setName("ACE_34Mb");
		aLUSchedulerPolicy.setTotalCIRbandwidth("34816");
		aLUSchedulerPolicy.setTotalPIRbandwidth("34816");
		return aLUSchedulerPolicy;
	}

	private IPV4Address constructIPV4Address() {
		IPV4Address ipV4Address = new IPV4Address();
		ipV4Address.setAddress("14.141.158.217/29");
		ipV4Address.setIsObjectModifiedInstance(true);
		return ipV4Address;
	}

	private IPV6Address constructIPV6Address() {
		IPV6Address ipV6Address = new IPV6Address();
		ipV6Address.setAddress("14.141.158.217/29");
		ipV6Address.setIsObjectModifiedInstance(true);
		return ipV6Address;
	}

	private AccessControlList constructAccessControlList() {
		AccessControlList accessControlList = new AccessControlList();
		accessControlList.setIsChildObjectInstanceModified(true);
		accessControlList.setIsObjectInstanceModified(true);
		accessControlList.setName("");
		return accessControlList;
	}
}
