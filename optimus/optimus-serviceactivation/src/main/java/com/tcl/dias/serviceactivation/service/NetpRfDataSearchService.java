package com.tcl.dias.serviceactivation.service;


import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;

@Service
public class NetpRfDataSearchService {
    private static final Logger logger = LoggerFactory.getLogger(NetpRfDataSearchService.class);

    @Value("${netp.rf.url}")
    String netpRfUrl;
//    String netpRfUrl = "http://121.244.244.68:8444/ftiserver/services/ServiceOrderRequestPort/";

    private String netpRadwinSearch(String serviceCode, String netpRefId) {
        logger.info("netpRadwinSearch invoked for serviceCode : {}", serviceCode);
        RestResponse response = null;
        try {
            RestClientService restClientService = new RestClientService();
            String netpRadwinSearchReq = createRadwinSearchRequest(serviceCode, netpRefId);
            logger.info("Request:: {}", netpRadwinSearchReq);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "text/xml");
            logger.info("NetpRfUrl:: {}", netpRfUrl);
            response = restClientService.post(netpRfUrl, netpRadwinSearchReq, headers);
            logger.info("netpRadwinSearch Response for serviceCode {} : {}", serviceCode, response);
            if (response != null && response.getStatus().equals(Status.SUCCESS) && response.getData() != null) {
                logger.info("netpRadwinSearch SUCCESS:: {}" , response.getData());
                return response.getData();
            } else {
                logger.info("Response failed for netpRadwinSearch for service Code {}", serviceCode);
                return "ERROR -> RESPONSE FAILURE";
            }

        } catch (Exception e) {
            logger.error("netpRadwinSearch for service code: {} and error:{}", serviceCode, e);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return "ERROR -> " + sw.toString();
        }
    }

    private String createRadwinSearchRequest(String serviceCode, String netpRefId) {

        String val ="hsrfu1='" + serviceCode + "' and hsrfu4='" + netpRefId + "'";

        return "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:axis=\"http://ftiserver.syndesis.com/messaging/ws/axis\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">\r\n" +
                "   <soapenv:Header/>\r\n" +
                "   <soapenv:Body>\r\n" +
                "      <axis:processRequest soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\r\n" +
                "         <document xsi:type=\"xsd:string\"><![CDATA[<ns1:Request SchemaVersion=\"26.0.0.000\" xmlns:ns1=\"http://www.syndesis.com/NN/XSNN\">\r\n" +
                "<OrderId>8765435</OrderId>\r\n" +
                "<ActivityId>1012</ActivityId>\r\n" +
                "<RequestItem>\r\n" +
                "<Name>Query:Generic</Name>\r\n" +
                "<Element>\r\n" +
                "<Name>class</Name>\r\n" +
                "<Value>HSNdNode_RO</Value>\r\n" +
                "</Element>\r\n" +
                "<Element>\r\n" +
                "<Name>attributeList</Name>\r\n" +
                "<Value>sy_objectid,srname,srmanaged,hsrfu4</Value>\r\n" +
                "</Element>\r\n" +
                "<Element>\r\n" +
                "<Name>criteria</Name>\r\n" +
                "<Value>" + val + "</Value>\r\n" +
                "</Element>\r\n" +
                "<ServiceRequest>Query</ServiceRequest>\r\n" +
                "<ServiceOperation></ServiceOperation>\r\n" +
                "<Action>Do</Action>\r\n" +
                "</RequestItem>\r\n" +
                "</ns1:Request>]]></document>\r\n" +
                "         <controlData xsi:type=\"axis:ControlData\" soapenc:arrayType=\"axis:NVPair[]\"/>\r\n" +
                "      </axis:processRequest>\r\n" +
                "   </soapenv:Body>\r\n" +
                "</soapenv:Envelope>\r\n";
    }

    public boolean netpRadwinSearchNodeResult(String serviceCode, String netpRefId) {
        boolean flag = false;
        String response = netpRadwinSearch(serviceCode, netpRefId);
        if(response.contains("srname"))
            flag=true;
        return flag;
    }

    private String netpCambiumSearch(String serviceCode, String netpRefId) {
        logger.info("netpCambiumSearch invoked for serviceCode : {}", serviceCode);
        RestResponse response = null;
        try {
            RestClientService restClientService = new RestClientService();
            String netpCambiumSearchReq = createCambiumSearchRequest(serviceCode, netpRefId);
            logger.info("Request:: {}", netpCambiumSearchReq);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "text/xml");
            logger.info("NetpRfUrl:: {}", netpRfUrl);
            response = restClientService.post(netpRfUrl, netpCambiumSearchReq, headers);
            logger.info("netpCambiumSearch Response for serviceCode {} : {}", serviceCode, response);
            if (response != null && response.getStatus().equals(Status.SUCCESS) && response.getData() != null) {
                logger.info("netpCambiumSearch SUCCESS:: {}" , response.getData());
                return response.getData();
            } else {
                logger.info("Response failed for netpCambiumSearch for service Code {}", serviceCode);
                return "ERROR -> RESPONSE FAILURE";
            }

        } catch (Exception e) {
            logger.error("netpCambiumSearch for service code: {} and error:{}", serviceCode, e);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return "ERROR -> " + sw.toString();
        }
    }

    private String createCambiumSearchRequest(String serviceCode, String netpRefId) {
        String val ="cwrfu1='" + serviceCode + "' and cwrfu4='" + netpRefId + "'";
        return "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:axis=\"http://ftiserver.syndesis.com/messaging/ws/axis\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">\r\n" +
                "   <soapenv:Header/>\r\n" +
                "   <soapenv:Body>\r\n" +
                "      <axis:processRequest soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\r\n" +
                "         <document xsi:type=\"xsd:string\"><![CDATA[<ns1:Request SchemaVersion=\"26.0.0.000\" xmlns:ns1=\"http://www.syndesis.com/NN/XSNN\">\r\n" +
                "<OrderId>8765435</OrderId>\r\n" +
                "<ActivityId>1012</ActivityId>\r\n" +
                "<RequestItem>\r\n" +
                "<Name>Query:Generic</Name>\r\n" +
                "<Element>\r\n" +
                "<Name>class</Name>\r\n" +
                "<Value>CWNdNode_RO</Value>\r\n" +
                "</Element>\r\n" +
                "<Element>\r\n" +
                "<Name>attributeList</Name>\r\n" +
                "<Value>sy_objectid,srname,cwdevicename</Value>\r\n" +
                "</Element>\r\n" +
                "<Element>\r\n" +
                "<Name>criteria</Name>\r\n" +
                "<Value>" + val + "</Value>\r\n" +
                "</Element>\r\n" +
                "<ServiceRequest>Query</ServiceRequest>\r\n" +
                "<ServiceOperation></ServiceOperation>\r\n" +
                "<Action>Do</Action>\r\n" +
                "</RequestItem>\r\n" +
                "</ns1:Request>]]></document>\r\n" +
                "         <controlData xsi:type=\"axis:ControlData\" soapenc:arrayType=\"axis:NVPair[]\"/>\r\n" +
                "      </axis:processRequest>\r\n" +
                "   </soapenv:Body>\r\n" +
                "</soapenv:Envelope>\r\n";
    }

    public boolean netpCambiumSearchNodeResult(String serviceCode, String netpRefId) {
        boolean flag = false;
        String response = netpCambiumSearch(serviceCode, netpRefId);
        if(response.contains("srname"))
            flag=true;
        return flag;
    }

    @Test
    public void radwinTest()
    {
        System.out.println(netpRadwinSearchNodeResult("A", "B"));
    }

    @Test
    public void CambiumTest()
    {
        System.out.println(netpCambiumSearchNodeResult("091NORT623010432629", "MIGR_NETP_LM_CAMB_JUN2020_02_03857"));
    }
}
