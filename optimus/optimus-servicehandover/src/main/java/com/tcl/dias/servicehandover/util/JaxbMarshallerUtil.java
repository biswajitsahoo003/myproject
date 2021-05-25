package com.tcl.dias.servicehandover.util;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicehandover.beans.sureaddress.SoapRequest;
import com.tcl.dias.servicehandover.beans.taxcapture.SetActualTax;
import com.tcl.dias.servicehandover.intl.account.beans.SetSECSProfile;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrder;
import com.tcl.dias.servicehandover.ipc.beans.invoice.CreateInvoice;

/**
 * Jaxb JaxbMarshaller for Soap Request- IPC Billing
 * @author yomagesh
 */
public class JaxbMarshallerUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(JaxbMarshallerUtil.class);
	
	public static ServicehandoverAudit jaxbObjectToXML(Object obj, ServicehandoverAudit audit) {
		try {
			JAXBContext jaxbContext = null;
			if(obj instanceof CreateOrder) {
				CreateOrder createOrd = (CreateOrder) obj;
				obj = createOrd;
				jaxbContext = JAXBContext.newInstance(CreateOrder.class);
			}
			if(obj instanceof SetActualTax) {
				SetActualTax setActualTax = (SetActualTax) obj;
				obj = setActualTax;
				jaxbContext = JAXBContext.newInstance(SetActualTax.class);
			}
			if(obj instanceof SetSECSProfile) {
				SetSECSProfile createOrd = (SetSECSProfile) obj;
				obj = createOrd;
				jaxbContext = JAXBContext.newInstance(SetSECSProfile.class);
			}
			if(obj instanceof CreateInvoice) {
				CreateInvoice createInvoice = (CreateInvoice) obj;
				obj = createInvoice;
				jaxbContext = JAXBContext.newInstance(CreateInvoice.class);
			}
			if(obj instanceof SoapRequest) {
				SoapRequest soapRequest = (SoapRequest) obj;
				obj = soapRequest;
				jaxbContext = JAXBContext.newInstance(SoapRequest.class);
			}
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			jaxbMarshaller.marshal(obj, sw);
			String xmlContent = sw.toString();
			LOGGER.info("inside jaxbObjectToXML {} ",xmlContent);
			audit.setRequest(xmlContent);
			

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return audit;
	}
	
}
