package com.tcl.dias.serviceactivation;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang3.StringUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.serviceactivation.constants.CramerConstants;
import com.tcl.dias.serviceactivation.cramer.checkclrinfo.beans.CheckClrInfo;
import com.tcl.dias.serviceactivation.cramer.checkclrinfo.beans.ObjectFactory;
import com.tcl.dias.serviceactivation.service.CramerService;
import com.tcl.dias.webserviceclient.beans.SoapRequest;
import com.tcl.dias.webserviceclient.service.GenericWebserviceClient;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CramerServiceTest {
	
	@Autowired
	private CramerService cramerService;
	
	@Autowired
	private GenericWebserviceClient genericWebserviceClient;
	
	@Test
	public void testProcessData() throws TclCommonException {
		CheckClrInfo checkClrInfo = new ObjectFactory().createCheckClrInfo();
		checkClrInfo.setObjectType(com.tcl.dias.serviceactivation.cramer.checkclrinfo.beans.ObjectType.SERVICE);
		checkClrInfo.setObjectName("091CHEN0300I0QGDRVZ");
		checkClrInfo.setRelationshipType(com.tcl.dias.serviceactivation.cramer.checkclrinfo.beans.RelationShip.ISSUED);

		checkClrInfo.setRequestId("c0aaccae-a182-11e9-9188-0242ac110019");
		checkClrInfo.setRequestingSystem(CramerConstants.OPTIMUS);
		
		 JAXBElement<CheckClrInfo> checkClrInfoElement = new ObjectFactory().createCheckClrInfo(checkClrInfo);
		 
		SoapRequest soapRequest = new SoapRequest();
		soapRequest.setUrl("http://uswv1vuap003a.vsnl.co.in:8080/CheckClrInfo/CheckClrStatus?wsdl");
		soapRequest.setRequestObject(checkClrInfoElement);
		soapRequest.setContextPath("com.tcl.dias.serviceactivation.cramer.checkclrinfo.beans");
		soapRequest.setSoapUserName("");
		soapRequest.setSoapPwd("");
		soapRequest.setConnectionTimeout(60000);
		soapRequest.setReadTimeout(60000);
		JAXBElement jaxBResponse = genericWebserviceClient.doSoapCallForObject(soapRequest, JAXBElement.class);
	}

}
