package com.tcl.dias.oms.renewals.serviceV1;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.UpdateGstRequest;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.UpdateGstRequestRepository;
import com.tcl.dias.oms.gst.service.GstInService;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.npl.beans.NplUpdateRequest;
import com.tcl.dias.oms.npl.service.v1.NplQuoteService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class RenewalsGstUpdateService {
	
	@Autowired
	UpdateGstRequestRepository updateGstRequestRepository;
	
	@Autowired
	GvpnQuoteService gvpnQuoteService;
	
	@Autowired
	IllQuoteService illQuoteService;
	
	@Autowired
	GstInService gstInService;
	
	@Autowired
	protected MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;
	
	@Autowired
	protected QuoteProductComponentRepository quoteProductComponentRepository;
	
	@Autowired
	protected ProductAttributeMasterRepository productAttributeMasterRepository;
	
	@Autowired
	NplQuoteService nplQuoteService;
	
	
	public static final Logger LOGGER = LoggerFactory.getLogger(RenewalsGstUpdateService.class);
	@RabbitListener(queuesToDeclare = { @Queue("${renewals.update.gst}") })
	public void fetchServicedetails(Message<String> request) {
		try {
			LOGGER.info("Received Request :: {}", request);
			processRequest();
	       } catch (Exception e) {
			LOGGER.error("error in changing outpulse value :: {} ", e);
		}
	}
	
	public void processRequest() throws TclCommonException, JsonProcessingException {
   
		List<UpdateGstRequest> updateGstRequests = updateGstRequestRepository.findByIsGstUpdated("N");
		
		if (!updateGstRequests.isEmpty()) {
			for (int i = 0; i < updateGstRequests.size(); i++) {
				updateGstRequests.get(i).setIsGstUpdated("P");
			}
			updateGstRequestRepository.saveAll(updateGstRequests);
			List<UpdateRequest> updateList = new ArrayList<UpdateRequest>();
			for (int i = 0; i < updateGstRequests.size(); i++) {

				if ((updateGstRequests.get(i).getFamilyName().equalsIgnoreCase("IAS") || updateGstRequests.get(i).getFamilyName().equalsIgnoreCase("GVPN"))
						&& updateGstRequests.get(i).getSiteAgst() !=null) {

					UpdateRequest update = new UpdateRequest();
					update.setFamilyName(updateGstRequests.get(i).getFamilyName());
					update.setQuoteToLe(updateGstRequests.get(i).getQuoteToLeId());
					List<AttributeDetail> attrList = new ArrayList<AttributeDetail>();
					AttributeDetail attr = new AttributeDetail();
					attr.setName("GSTNO");
					attr.setValue(updateGstRequests.get(i).getSiteAgst());
					attrList.add(attr);
					update.setAttributeDetails(attrList);
					update.setSiteId(updateGstRequests.get(i).getSiteId());
					update.setUserName("root");
					updateList.add(update);
					if(updateGstRequests.get(i).getFamilyName().equalsIgnoreCase("GVPN")){
					gvpnQuoteService.updateSiteProperties(update);
					}else {
					illQuoteService.updateSiteProperties(update);
					}
					MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(updateGstRequests.get(i).getFamilyName(), (byte) 1);
					List<MstProductComponent> mstProductComponents = mstProductComponentRepository.findByNameAndStatus(
							IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(), (byte) 1);
					List<QuoteProductComponent> orderProductComponents = quoteProductComponentRepository
							.findByReferenceIdAndMstProductComponentAndMstProductFamily(
									updateGstRequests.get(i).getSiteId(), mstProductComponents.get(0),
									mstProductFamily);
					illQuoteService.updateGstAddress(attr, orderProductComponents.get(0));

					updateGstRequests.get(i).setIsGstUpdated("Y");
				}else if((updateGstRequests.get(i).getFamilyName().equalsIgnoreCase("NPL")
						|| updateGstRequests.get(i).getFamilyName().equalsIgnoreCase("NDE")) && (updateGstRequests.get(i).getSiteAgst() !=null || updateGstRequests.get(i).getSiteBgst() !=null) ){
					NplUpdateRequest npl = new NplUpdateRequest();
					UpdateRequest update = new UpdateRequest();
					update.setFamilyName(updateGstRequests.get(i).getFamilyName());
					update.setQuoteToLe(updateGstRequests.get(i).getQuoteToLeId());
					update.setUserName("root");
					
					List<AttributeDetail> attrList = new ArrayList<AttributeDetail>();
					AttributeDetail attr = new AttributeDetail();
					attr.setName("GSTNO");
					String type;
					if(updateGstRequests.get(i).getSiteAgst()!=null) {
						type = CommonConstants.SITEA;
						attr.setValue(updateGstRequests.get(i).getSiteAgst());
					}else {
						type = CommonConstants.SITEB;
						attr.setValue(updateGstRequests.get(i).getSiteBgst());
					}		
					attrList.add(attr);
					update.setAttributeDetails(attrList);
					update.setSiteId(updateGstRequests.get(i).getSiteId());
					updateList.add(update);
					npl.setUpdateRequest(updateList);
					nplQuoteService.updateSiteProperties(npl, type);
					
					MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(updateGstRequests.get(i).getFamilyName(), (byte) 1);
					
					List<MstProductComponent> mstProductComponents = mstProductComponentRepository.findByNameAndStatus(
							IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(), (byte) 1);
					
					Optional<QuoteProductComponent> orderProductComponents = quoteProductComponentRepository.findByReferenceIdAndMstProductComponentAndType(updateGstRequests.get(i).getSiteId(), mstProductComponents.get(0), type);
					

//					List<QuoteProductComponent> orderProductComponents = quoteProductComponentRepository
//							.findByReferenceIdAndMstProductComponentAndMstProductFamily(
//									updateGstRequests.get(i).getSiteId(), mstProductComponents.get(0),
//									mstProductFamily);
					illQuoteService.updateGstAddress(attr, orderProductComponents.get());
					updateGstRequests.get(i).setIsGstUpdated("Y");					
				}else {
					updateGstRequests.get(i).setIsGstUpdated("NA");
				}
			}
		}
		updateGstRequestRepository.saveAll(updateGstRequests);
	}
	
}
