package com.tcl.dias.serviceinventory.consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.tcl.dias.common.gsc.beans.GscWholesaleInterconnectBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.ProductInformationBean;
import com.tcl.dias.common.beans.SiSearchBean;
import com.tcl.dias.common.beans.SiServiceDetailBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceinventory.beans.SIServiceDetailBean;
import com.tcl.dias.serviceinventory.beans.SIServiceDetailedResponse;
import com.tcl.dias.serviceinventory.service.v1.IzosdwanServiceInventoryService;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceinventory.service.v1.ServiceInventoryService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import static com.tcl.dias.common.constants.CommonConstants.COMMA;
import static com.tcl.dias.common.constants.CommonConstants.HYPHEN;

/**
 * This file contains the consumer calls for returning Inventory based queries.
 * 
 * @author Deepika Sivalingam.
 * @link http://www.tatacommunications.com
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ServiceInventoryConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInventoryConsumer.class);

	@Autowired
	ServiceInventoryService serviceInventoryService;
	
	@Autowired
	IzosdwanServiceInventoryService izosdwanServiceInventoryService;
	
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.user.products.queue}")})
	public List<String> getProductWithCount() throws TclCommonException {
		List<ProductInformationBean> list = null;
		try {
				list = serviceInventoryService.getAllProductServiceInformation(null,null);
				return list.stream().map(l -> l.getProductName()).collect(Collectors.toList());

		} catch (Exception e) {
			LOGGER.error("Unable to get the product list...");
			LOGGER.error("Error in prodCount",e);
		}
		
		return new ArrayList<String>();
	}
	
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.si.details.by.customer.queue}")})
	public String getInventoryDetailsOfTheCustomer(String request) throws TclCommonException {
		String response = "";
		try {
			if(request!=null) {
				SiSearchBean siSearchBean = Utils.convertJsonToObject(request, SiSearchBean.class);
				List<SiServiceDetailBean> siServiceDetailBeans = izosdwanServiceInventoryService
						.findAllServicesByCustomer(siSearchBean);

				response = Utils.convertObjectToJson(siServiceDetailBeans);
			}
			
		} catch (Exception e) {
			LOGGER.error("error in getting customer LE contact details by email ", e);
		}
		return response;
	}
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.get.service.details}")})
	public String getLastMileProvider(String serviceDetailRequest) throws TclCommonException {
		String lmProvider = null;
		try {
			Map<String,Object> requestMap = (HashMap)Utils.convertJsonToObject(serviceDetailRequest, HashMap.class);
			
			lmProvider = serviceInventoryService.getLastMileProvider((String)requestMap.get("serviceId"), (String)requestMap.get("product"), null, null);
				
		} catch (Exception e) {
			LOGGER.error("Unable to get service detail info...");
		}
		
		return lmProvider;
	}

	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.wholesale.customer.interconnect}")})
	public String getWholesaleCustomerInterconnect(String request) {
		String response = null;
		try {
			LOGGER.info("Customer SECS ID :: {}", request);

			List<GscWholesaleInterconnectBean> gscWholesaleInterconnectBean = serviceInventoryService.getWholesaleCustomerInterconnect(Integer.valueOf(request));

			response = Utils.convertObjectToJson(gscWholesaleInterconnectBean);

		} catch (Exception e) {
			LOGGER.error("Unable to get interconnect details...", e.getStackTrace());
		}

		return response;
	}

	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.wholesale.customer.interconnect.name}")})
	public String getInterconnectNameById(String request) {
		String response = null;
		try {
			LOGGER.info("Interconnect Id :: {}", request);
			response = serviceInventoryService.getInterconnectNameById(request);
		} catch (Exception e) {
			LOGGER.error("Unable to get interconnect details...", e.getStackTrace());
		}

		return response;
	}

	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.service.gsc.countries}")})
	public String getGscPulseChargeCountries(String request) {
		String response = null;
		try {
			LOGGER.info("Customer SECS ID :: {}", request);

			Set<Map<String, Object>> gscWholesaleInterconnectBean = serviceInventoryService
					.getGscPulseChargeCountries(request.split(HYPHEN)[0], request.split(HYPHEN)[1]);

			response = Utils.convertObjectToJson(gscWholesaleInterconnectBean);
			LOGGER.info("Response Inventory :: {}", response);
		} catch (Exception e) {
			LOGGER.error("Unable to get gsc countries details...", e.getStackTrace());
		}

		return response;
	}

	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.service.gsc.product.names}")})
	public String getGscProductNames(String request) {
		String response = null;
		try {
			LOGGER.info("Customer SECS ID :: {}", request);

			List<String> gscProductNames = serviceInventoryService.getGscProductNames(request);
			response = Utils.convertObjectToJson(gscProductNames);

			LOGGER.info("Gsc Product Response response :: {}", response);

		} catch (Exception e) {
			LOGGER.error("Unable to get gsc product name details...", e.getStackTrace());
		}

		return response;
	}

	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.get.customer.service.id}")})
	public String getCustomerServiceIdbasedonServiceId(String serviceId) throws TclCommonException {
		String response = "";
		
		try {
				response = serviceInventoryService.getCustomerServiceId(serviceId);
				//response = Utils.convertObjectToJson(list);

		} catch (Exception e) {
			LOGGER.error("Unable to get the service Id detail list {} {}...",serviceId,e);
		}
		
		return response;
	}
		

	/**
	 * PIPF-373
	 * @param serviceDetailRequest
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.get.lm.provider.access.type}")})
	public String getLMAndAccessType(String serviceDetailRequest) throws TclCommonException {
		String response = null;
		LOGGER.info("Queue request {}", serviceDetailRequest);
		try {
			Map<String,Object> requestMap = (HashMap)Utils.convertJsonToObject(serviceDetailRequest, HashMap.class);

		 	Map<String,String> lmAndAccessType = serviceInventoryService.getLMProviderAndAccesstype((String)requestMap.get("serviceId"), (String)requestMap.get("product"), null, null);
			response = Utils.convertObjectToJson(lmAndAccessType);


		} catch (Exception e) {
			LOGGER.error("Unable to get service detail info...");
		}

		return response;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.service.detailsByServiceId}") })
	public String getServiceDetailsByServiceId(String serviceId) throws TclCommonException {
		String response = null;
		try {

			LOGGER.info("Queue request {}", serviceId);
			SiServiceDetailBean sIServiceDetailBean = serviceInventoryService.getSIServiceDetail(serviceId);
			response = Utils.convertObjectToJson(sIServiceDetailBean);
		} catch (Exception e) {
			LOGGER.error("Unable to get service detail info... {}", e);
		}
		return response;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.si.service.details.by.serviceId.and.product}") })
	public String getServiceDetailsByServiceIdAndProduct(String serviceDetailRequest) throws TclCommonException {
		String response = null;
		LOGGER.info("Queue request {}", serviceDetailRequest);
		try {
			Map<String,Object> requestMap = (HashMap)Utils.convertJsonToObject(serviceDetailRequest, HashMap.class);
			SIServiceDetailedResponse siServiceDetailedResponse = serviceInventoryService.getDetailedSIInfo((String)requestMap.get("serviceId"), (String)requestMap.get("product"), null);
			response = Utils.convertObjectToJson(siServiceDetailedResponse);
		} catch (Exception e) {
			LOGGER.error("Unable to get service detail info... {}", e);
		}
		return response;
	}
	
}
