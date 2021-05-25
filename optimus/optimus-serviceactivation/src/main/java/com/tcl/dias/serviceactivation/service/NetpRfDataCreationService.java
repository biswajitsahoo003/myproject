package com.tcl.dias.serviceactivation.service;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;

@Service
public class NetpRfDataCreationService {

	private static final Logger logger = LoggerFactory.getLogger(NetpRfDataCreationService.class);

	@Value("${netp.rf.url}")
	String netpRfUrl;

	private String createRadwinCreateRequest(String serviceCode, String hsuMgmtIp, String srNw, String srName,
			String netpRefId, String hsuMac, String manageType) {
		return "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:axis=\"http://ftiserver.syndesis.com/messaging/ws/axis\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">\r\n"
				+ "   <soapenv:Header/>\r\n" + "   <soapenv:Body>\r\n"
				+ "      <axis:processRequest soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\r\n"
				+ "         <document xsi:type=\"xsd:string\"><![CDATA[<ns1:Request SchemaVersion=\"26.0.0.000\" xmlns:ns1=\"http://www.syndesis.com/NN/XSNN\">\r\n"
				+ "  <OrderId>ManRad0001</OrderId>					\r\n"
				+ "  <ActivityId>Radwin_New_Node_Create</ActivityId>			\r\n" + "  <RequestItem>\r\n"
				+ "    <Name>Create:Node</Name>				\r\n" + "   <Element>\r\n"
				+ "      <Name>hsumgmtip</Name>\r\n" + "      <Value>" + hsuMgmtIp + "</Value>					\r\n"
				+ "    </Element>\r\n" + "<Element>\r\n" + "      <Name>srnetwork</Name>						\r\n"
				+ "      <Value>" + srNw + "</Value>\r\n" + "    </Element>\r\n" + "    <Element>\r\n"
				+ "      <Name>srname</Name>\r\n" + "      <Value>" + srName + "</Value>	 \r\n"
				+ "    </Element>\r\n" + "   <Element>\r\n" + "      <Name>hsrfu1</Name>\r\n" + "      <Value>"
				+ serviceCode + "</Value>			\r\n" + "    </Element>\r\n" + "    <Element>\r\n"
				+ "      <Name>hsrfu4</Name>\r\n" + "      <Value>" + netpRefId + "</Value>		\r\n"
				+ "    </Element>\r\n" + "    <Element>\r\n" + "      <Name>srmanaged</Name>					\r\n"
				+ "      <Value>" + manageType + "</Value>\r\n" + "    </Element>\r\n" + "    <Element>\r\n"
				+ "      <Name>hsumacaddress</Name>					\r\n" + "      <Value>" + hsuMac + "</Value>\r\n"
				+ "    </Element>\r\n" + "    <Element>\r\n" + "      <Name>hsugatewayip</Name>					\r\n"
				+ "      <Value></Value>\r\n" + "    </Element>\r\n" + "   <ServiceRequest></ServiceRequest>\r\n"
				+ "    <ServiceOperation>Create</ServiceOperation>		\r\n" + "    <Action>Do</Action>\r\n"
				+ "	<ClassName>HSNdNode_RO</ClassName>			\r\n" + "  </RequestItem>		\r\n"
				+ "</ns1:Request>]]></document>\r\n"
				+ "         <controlData xsi:type=\"axis:ControlData\" soapenc:arrayType=\"axis:NVPair[]\"/>\r\n"
				+ "      </axis:processRequest>\r\n" + "   </soapenv:Body>\r\n" + "</soapenv:Envelope>";
	}

	public String netpRadwinCreate(String serviceCode, String hsuMgmtIp, String srNw, String srName, String netpRefId,
			String hsuMac, String manageType) {
		logger.info("netpRadwinCreate invoked for serviceCode : {}", serviceCode);
		RestResponse response = null;
		try {
			RestClientService restClientService = new RestClientService();
			String radwinCreateNetpReq = createRadwinCreateRequest(serviceCode, hsuMgmtIp, srNw, srName, netpRefId,
					hsuMac, manageType);
			logger.info("Request:: {}", radwinCreateNetpReq);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "text/xml");
			logger.info("NetpRfUrl:: {}", netpRfUrl);
			response = restClientService.post(netpRfUrl, radwinCreateNetpReq, headers);
			logger.info("radwinCreateNetP Response for serviceCode {} : {}", serviceCode, response);
			if (response != null && response.getStatus().equals(Status.SUCCESS) && response.getData() != null) {
				logger.info("radwinCreateNetP SUCCESS::" + response.getData());
				return response.getData();
			} else {
				logger.info("Response failed for netpRadwinCreate for service Code {}", serviceCode);
				return "ERROR -> RESPONSE FAILURE";
			}

		} catch (Exception e) {
			logger.error("netpRadwinCreate for service code: {} and error:{}", serviceCode, e);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return "ERROR -> " + sw.toString();
		}

	}
	
	public String netpRadwinUpdate(String serviceCode, String hsuMgmtIp, String srNw, String srName, String netpRefId,
			String hsuMac, String manageType) {
		logger.info("netpRadwinUpdate invoked for serviceCode : {}", serviceCode);
		RestResponse response = null;
		try {
			RestClientService restClientService = new RestClientService();
			String radwinCreateNetpReq = createRadwinCreateRequest(serviceCode, hsuMgmtIp, srNw, srName, netpRefId,
					hsuMac, manageType);
			logger.info("Request:: {}", radwinCreateNetpReq);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "text/xml");
			logger.info("NetpRfUrl:: {}", netpRfUrl);
			response = restClientService.post(netpRfUrl, radwinCreateNetpReq, headers);
			logger.info("radwinUpdateNetP Response for serviceCode {} : {}", serviceCode, response);
			if (response != null && response.getStatus().equals(Status.SUCCESS) && response.getData() != null) {
				logger.info("radwinCreateNetP SUCCESS::" + response.getData());
				return response.getData();
			} else {
				logger.info("Response failed for netpRadwinUpdate for service Code {}", serviceCode);
				return "ERROR -> RESPONSE FAILURE";
			}

		} catch (Exception e) {
			logger.error("netpRadwinUpdate for service code: {} and error:{}", serviceCode, e);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return "ERROR -> " + sw.toString();
		}

	}

	private String createCambiumCreateRequest(String serviceCode,String srNw, String deviceName,
			String srName, String netpRefId, String manageType) { // manage type not used in Cambium create
																  // Doc sent

		return "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:axis=\"http://ftiserver.syndesis.com/messaging/ws/axis\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">\r\n"
				+ "   <soapenv:Header/>\r\n" + "   <soapenv:Body>\r\n"
				+ "      <axis:processRequest soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\r\n"
				+ "         <document xsi:type=\"xsd:string\"><![CDATA[<ns1:Request SchemaVersion=\"26.0.0.000\" xmlns:ns1=\"http://www.syndesis.com/NN/XSNN\">\r\n"
				+ "  <OrderId>ManCam0001</OrderId>\r\n" + "  <ActivityId>Cambium_New_Node_Create</ActivityId>\r\n"
				+ "  <RequestItem>\r\n" + "    <Name>Create:Node</Name>\r\n" + "   <Element>\r\n"
				+ "      <Name>srnetwork</Name>\r\n" + "      <Value>" + srNw + "</Value>				\r\n"
				+ "    </Element>\r\n" + "    <Element>\r\n" + "      <Name>cwdevicename</Name>\r\n" + "      <Value>"
				+ deviceName + "</Value>	\r\n" + "    </Element>\r\n" + "   <Element>\r\n"
				+ "      <Name>cwrfu1</Name>\r\n" + "      <Value>" + serviceCode + "</Value>			\r\n"
				+ "    </Element>\r\n" + "    <Element>\r\n" + "      <Name>cwrfu4</Name>\r\n" + "      <Value>"
				+ netpRefId + "</Value>	\r\n" + "    </Element>\r\n" + "    <Element>\r\n"
				+ "      <Name>srname</Name>\r\n" + "      <Value>" + srName + "</Value>				\r\n"
				+ "    </Element>\r\n" + "   <ServiceRequest></ServiceRequest>\r\n"
				+ "    <ServiceOperation>Create</ServiceOperation>		\r\n" + "    <Action>Do</Action>\r\n"
				+ "	<ClassName>CWNdNode_RO</ClassName>	\r\n" + "  </RequestItem>\r\n"
				+ "</ns1:Request>]]></document>\r\n"
				+ "         <controlData xsi:type=\"axis:ControlData\" soapenc:arrayType=\"axis:NVPair[]\"/>\r\n"
				+ "      </axis:processRequest>\r\n" + "   </soapenv:Body>\r\n" + "</soapenv:Envelope>";

	}

	public String netpCambiumCreate(String serviceCode, String srNw, String deviceName, String srName,
			String netpRefId, String manageType) {
		logger.info("netpCambiumCreate invoked for serviceCode : {}", serviceCode);
		RestResponse response = null;
		try {
			RestClientService restClientService = new RestClientService();
			String cambiumCreateNetpReq = createCambiumCreateRequest(serviceCode,srNw, deviceName, srName,
					netpRefId, manageType);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "text/xml");
			response = restClientService.post(netpRfUrl, cambiumCreateNetpReq, headers);
			logger.info("cambiumCreateNetP Response for serviceCode {} : {}", serviceCode, response);
			if (response != null && response.getStatus().equals(Status.SUCCESS) && response.getData() != null) {
				logger.info("radwinCreateNetP SUCCESS::" + response.getData());
				return response.getData();
			} else {
				logger.error("Response failed for netpCambiumCreate for service Code {}", serviceCode);
				return "ERROR -> RESPONSE FAILURE";
			}

		} catch (Exception e) {
			logger.error("netpCambiumCreate for service code: {} and error:{}", serviceCode, e);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return "ERROR -> " + sw.toString();
		}

	}

	private String createCambiumModifyMacdRequest(String serviceCode, String mgmtIp, String srNw, String deviceName,
			String srName, String netpRefId, String mac, String manageType) {

		return "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:axis=\"http://ftiserver.syndesis.com/messaging/ws/axis\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">\r\n"
				+ "	   <soapenv:Header/>\r\n" + "	   <soapenv:Body>\r\n"
				+ "	      <axis:processRequest soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\r\n"
				+ "	         <document xsi:type=\"xsd:string\"><![CDATA[<ns1:Request SchemaVersion=\"26.0.0.000\" xmlns:ns1=\"http://www.syndesis.com/NN/XSNN\">\r\n"
				+ "	  <OrderId>ManCam0001</OrderId>				\r\n"
				+ "	  <ActivityId>Cambium_Network_Modify</ActivityId>	\r\n" + "	  <RequestItem>\r\n"
				+ "	    <Name>Modify:Network</Name>			\r\n" + "	   <Element>\r\n"
				+ "	      <Name>srname</Name>\r\n" + "	      <Value>" + srName + "</Value>				\r\n"
				+ "	    </Element>\r\n" + "	    <Element>\r\n" + "	      <Name>cwmanaged</Name>				\r\n"
				+ "	      <Value>" + manageType + "</Value>				\r\n" + "	    </Element>\r\n"
				+ "	   <ServiceRequest></ServiceRequest>\r\n"
				+ "	    <ServiceOperation>Modify</ServiceOperation>		\r\n" + "	    <Action>Do</Action>\r\n"
				+ "		<ClassName>CWntNetwork</ClassName>		\r\n" + "	  </RequestItem>\r\n"
				+ "	</ns1:Request>]]></document>\r\n"
				+ "	         <controlData xsi:type=\"axis:ControlData\" soapenc:arrayType=\"axis:NVPair[]\"/>\r\n"
				+ "	      </axis:processRequest>\r\n" + "	   </soapenv:Body>\r\n" + "	</soapenv:Envelope>";

	}

	private String createRadwinModifyMacdRequest(String serviceCode,String hsuMgmtIp, String srNw, String srName, String netpRefId,
			String macMofidied, String manageType) {
		return "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:axis=\"http://ftiserver.syndesis.com/messaging/ws/axis\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">\r\n"
				+ "   <soapenv:Header/>\r\n" + "   <soapenv:Body>\r\n"
				+ "      <axis:processRequest soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\r\n"
				+ "         <document xsi:type=\"xsd:string\"><![CDATA[<ns1:Request SchemaVersion=\"26.0.0.000\" xmlns:ns1=\"http://www.syndesis.com/NN/XSNN\">\r\n"
				+ "  <OrderId>ManRad0001</OrderId>					\r\n"
				+ "  <ActivityId>Radwin_New_Node_Create</ActivityId>			\r\n" + "  <RequestItem>\r\n"
				+ "    <Name>Modify:Node</Name>					\r\n" + "   <Element>\r\n"
				+ "      <Name>hsumgmtip</Name>\r\n" + "      <Value>" + hsuMgmtIp + "</Value>				\r\n"
				+ "    </Element>\r\n" + "<Element>\r\n" + "      <Name>srnetwork</Name>					\r\n"
				+ "      <Value>" + srNw + "</Value>\r\n" + "    </Element>\r\n" + "    <Element>\r\n"
				+ "      <Name>srname</Name>\r\n" + "      <Value>" + srName + "</Value>	\r\n" + "    </Element>\r\n"
				+ "   <Element>\r\n" + "      <Name>hsrfu1</Name>\r\n" + "      <Value>" + serviceCode
				+ "</Value>			\r\n" + "    </Element>\r\n" + "    <Element>\r\n" + "      <Name>hsrfu4</Name>\r\n"
				+ "      <Value>" + netpRefId + "</Value>		\r\n" + "    </Element>\r\n" + "    <Element>\r\n"
				+ "      <Name>srmanaged</Name>					\r\n" + "      <Value>" + manageType + "</Value>\r\n"
				+ "    </Element>\r\n" + "    <Element>\r\n"
				+ "      <Name>hsumacaddress</Name>					\r\n" + "      <Value>" + macMofidied
				+ "</Value>\r\n" + "    </Element>\r\n" + "    <Element>\r\n"
				+ "      <Name>hsugatewayip</Name>					\r\n" + "      <Value></Value>\r\n"
				+ "    </Element>\r\n" + "   <ServiceRequest></ServiceRequest>\r\n"
				+ "    <ServiceOperation>Modify</ServiceOperation>			\r\n" + "    <Action>Do</Action>\r\n"
				+ "	<ClassName>HSNdNode_RO</ClassName>			\r\n" + "  </RequestItem>\r\n"
				+ "</ns1:Request>]]></document>\r\n"
				+ "         <controlData xsi:type=\"axis:ControlData\" soapenc:arrayType=\"axis:NVPair[]\"/>\r\n"
				+ "      </axis:processRequest>\r\n" + "   </soapenv:Body>\r\n" + "</soapenv:Envelope>";
	}

	public String netpRadwinModifyMacd(String serviceCode, String hsuMgmtIp, String srNw, String srName, String netpRefId,
			String macMofidied, String manageType) {
		logger.info("netpRadwinModifyMacd invoked for serviceCode : {}", serviceCode);
		RestResponse response = null;
		try {
			RestClientService restClientService = new RestClientService();
			String radwinModifyMacdNetpReq = createRadwinModifyMacdRequest(serviceCode,hsuMgmtIp,srNw, srName, netpRefId, macMofidied, manageType);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "text/xml");
			response = restClientService.post(netpRfUrl, radwinModifyMacdNetpReq, headers);
			logger.info("radwinModifyMacdNetP Response for serviceCode {} : {}", serviceCode, response);
			if (response != null && response.getStatus().equals(Status.SUCCESS) && response.getData() != null) {
				logger.info("radwinModifyMacdNetP SUCCESS::" + response.getData());
				return response.getData();
			} else {
				logger.error("Response failed for netpRadwinModifyMacd for service Code {}", serviceCode);
				return "ERROR -> RESPONSE FAILURE";
			}

		} catch (Exception e) {
			logger.error("netpRadwinModifyMacd for service code: {} and error:{}", serviceCode, e);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return "ERROR -> " + sw.toString();
		}

	}
	
	private String createCambiumModifyMacdRequest(String serviceCode, String srNw, String deviceName, String srName,
			String netpRefId, String manageType) {
		return "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:axis=\"http://ftiserver.syndesis.com/messaging/ws/axis\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">\r\n"
		+ "   <soapenv:Header/>\r\n" + "   <soapenv:Body>\r\n"
		+ "      <axis:processRequest soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\r\n"
		+ "         <document xsi:type=\"xsd:string\"><![CDATA[<ns1:Request SchemaVersion=\"26.0.0.000\" xmlns:ns1=\"http://www.syndesis.com/NN/XSNN\">\r\n"
		+ "  <OrderId>ManCam0001</OrderId>\r\n" + "  <ActivityId>Cambium_New_Node_Create</ActivityId>\r\n"
		+ "  <RequestItem>\r\n" + "    <Name>Create:Node</Name>\r\n" + "   <Element>\r\n"
		+ "      <Name>srnetwork</Name>\r\n" + "      <Value>" + srNw + "</Value>				\r\n"
		+ "    </Element>\r\n" + "    <Element>\r\n" + "      <Name>cwdevicename</Name>\r\n" + "      <Value>"
		+ deviceName + "</Value>	\r\n" + "    </Element>\r\n" + "   <Element>\r\n"
		+ "      <Name>cwrfu1</Name>\r\n" + "      <Value>" + serviceCode + "</Value>			\r\n"
		+ "    </Element>\r\n" + "    <Element>\r\n" + "      <Name>cwrfu4</Name>\r\n" + "      <Value>"
		+ netpRefId + "</Value>	\r\n" + "    </Element>\r\n" + "    <Element>\r\n"
		+ "      <Name>srname</Name>\r\n" + "      <Value>" + srName + "</Value>				\r\n"
		+ "    </Element>\r\n" + "   <ServiceRequest></ServiceRequest>\r\n"
		+ "    <ServiceOperation>Modify</ServiceOperation>		\r\n" + "    <Action>Do</Action>\r\n"
		+ "	<ClassName>CWNdNode_RO</ClassName>	\r\n" + "  </RequestItem>\r\n"
		+ "</ns1:Request>]]></document>\r\n"
		+ "         <controlData xsi:type=\"axis:ControlData\" soapenc:arrayType=\"axis:NVPair[]\"/>\r\n"
		+ "      </axis:processRequest>\r\n" + "   </soapenv:Body>\r\n" + "</soapenv:Envelope>";
	}
	
	
	public String netpCambiumModifyMacd(String serviceCode, String srNw, String deviceName, String srName,
			String netpRefId, String manageType) {
		logger.info("netpCambiumModifyMacd invoked for serviceCode : {}", serviceCode);
		RestResponse response = null;
		try {
			RestClientService restClientService = new RestClientService();
			String cambiumModifyMacdNetpReq = createCambiumModifyMacdRequest(serviceCode,srNw, deviceName,srName, netpRefId,manageType);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "text/xml");
			response = restClientService.post(netpRfUrl, cambiumModifyMacdNetpReq, headers);
			logger.info("cambiumModifyMacdNetP Response for serviceCode {} : {}", serviceCode, response);
			if (response != null && response.getStatus().equals(Status.SUCCESS) && response.getData() != null) {
				logger.info("cambiumModifyMacdNetP SUCCESS::" + response.getData());
				return response.getData();
			} else {
				logger.error("Response failed for netpCambiumModifyMacd for service Code {}", serviceCode);
				return "ERROR -> RESPONSE FAILURE";
			}

		} catch (Exception e) {
			logger.error("netpCambiumModifyMacd for service code: {} and error:{}", serviceCode, e);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return "ERROR -> " + sw.toString();
		}

	}
}
