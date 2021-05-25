package com.tcl.dias.oms.izosdwan.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.serviceinventory.beans.SIAttributeBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailsBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * 
 * This file contains the OmsIzosdwanUtils.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class OmsIzosdwanUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OmsIzosdwanUtils.class);
	
	@Value("${rabbitmq.si.order.details.queue}")
	String siOrderDetailsQueue;

	@Autowired
	MQUtils mqUtils;
	
	
	public SIServiceDetailDataBean getServiceDetailIzosdwan(String serviceId)throws TclCommonException
	{
		SIServiceDetailsBean serviceDetail = null;
		List<SIServiceDetailsBean> serviceDetailsList = null;
		LOGGER.info("Inside getServiceDetail to fetch SIServiceDetailDataBean for serviceid {} ", serviceId);
		String orderDetailsQueueResponse = (String) mqUtils.sendAndReceive(siOrderDetailsQueue, serviceId);
		SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
				.convertJsonToObject(orderDetailsQueueResponse, SIServiceDetailsBean[].class);
		serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
		 
		if(serviceDetailsList.isEmpty())
			throw new TclCommonRuntimeException(ExceptionConstants.SI_ORDER_RETRIEVAL_ERROR, ResponseResource.R_CODE_ERROR);
		serviceDetail=serviceDetailsList.stream().findFirst().get(); 
		

		SIServiceDetailDataBean serviceDetailDataBean=mapServiceDetailBeanIzosdwan(serviceDetail);

		return serviceDetailDataBean;
	}
	
	private SIServiceDetailDataBean mapServiceDetailBeanIzosdwan(SIServiceDetailsBean siServiceDetail)
	{
		LOGGER.info("Inside mapServiceDetailBean to construct SIServiceDetailDataBean for serviceid {} ",
				siServiceDetail.getTpsServiceId());
		SIServiceDetailDataBean serviceDetail = new SIServiceDetailDataBean();
		List<SIAttributeBean> serviceAttributesList = new ArrayList<>();
		serviceDetail.setAccessType(siServiceDetail.getAccessType());
		serviceDetail.setAccessProvider(siServiceDetail.getAccessProvider());
		serviceDetail.setArc(siServiceDetail.getArc());
		serviceDetail.setBillingFrequency(siServiceDetail.getBillingFrequency());
		serviceDetail.setBillingMethod(siServiceDetail.getBillingMethod());
		serviceDetail.setContractTerm(siServiceDetail.getContractTerm());
		serviceDetail.setErfPrdCatalogOfferingId(siServiceDetail.getErfPrdCatalogOfferingId());
		serviceDetail.setErfPrdCatalogOfferingName(siServiceDetail.getErfPrdCatalogOfferingName());
		serviceDetail.setLastmileBw(siServiceDetail.getLastmileBw());
		serviceDetail.setLastmileBwUnit(siServiceDetail.getLastmileBwUnit());
		serviceDetail.setPortBw(siServiceDetail.getPortBw());
		serviceDetail.setPortBwUnit(siServiceDetail.getPortBwUnit());
		if (siServiceDetail.getLinkType() != null)
			serviceDetail.setLinkType(siServiceDetail.getLinkType().toLowerCase());
		serviceDetail.setTpsServiceId(siServiceDetail.getTpsServiceId());
		serviceDetail.setPriSecServLink(siServiceDetail.getPriSecServLink());
		serviceDetail.setNrc(siServiceDetail.getNrc());
		serviceDetail.setReferenceOrderId(Objects.nonNull(siServiceDetail.getReferenceOrderId())
				? Integer.toString(siServiceDetail.getReferenceOrderId())
				: null);
		serviceDetail.setVpnName(siServiceDetail.getVpnName());
		LOGGER.info("mapServiceDetailBean locationId : {} for the serviceId : {}",
				siServiceDetail.getErfLocSiteAddressId(), siServiceDetail.getTpsServiceId());

		serviceDetail.setErfLocSiteAddressId(Integer.toString(siServiceDetail.getErfLocSiteAddressId()));

		serviceDetail.setLatLong(siServiceDetail.getLatLong());
		serviceDetail.setId(siServiceDetail.getId());
		LOGGER.info("Mrc inventory value " + siServiceDetail.getMrc());
		serviceDetail.setMrc(siServiceDetail.getMrc());
		serviceDetail.setArc(siServiceDetail.getArc());
		serviceDetail.setBillingType(siServiceDetail.getBillingType());
		serviceDetail.setContractStartDate(siServiceDetail.getContractStartDate());
		serviceDetail.setContractEndDate(siServiceDetail.getContractEndDate());
		serviceDetail.setContractTerm(siServiceDetail.getContractTerm());
		serviceDetail.setServiceCommissionedDate(siServiceDetail.getServiceCommissionedDate());
		serviceDetail.setDemarcationRoom(siServiceDetail.getDemarcationRoom());
		serviceDetail.setDemarcationFloor(siServiceDetail.getDemarcationFloor());
		serviceDetail.setDemarcationApartment(siServiceDetail.getDemarcationApartment());
		serviceDetail.setDemarcationRack(siServiceDetail.getDemarcationRack());
		if (Objects.nonNull(siServiceDetail.getAssetAttributes()) && !siServiceDetail.getAssetAttributes().isEmpty()) {
			siServiceDetail.getAssetAttributes().stream().forEach(serviceAttribute -> {
				SIAttributeBean siAttribute = new SIAttributeBean();
				siAttribute.setName(serviceAttribute.getAttributeName());
				siAttribute.setValue(serviceAttribute.getAttributeValue());
				serviceAttributesList.add(siAttribute);

			});
			serviceDetail.setAttributes(serviceAttributesList);
		}
		return serviceDetail;
	}

}
