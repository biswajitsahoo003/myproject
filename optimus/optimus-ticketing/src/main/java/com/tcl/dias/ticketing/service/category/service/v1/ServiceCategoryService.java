package com.tcl.dias.ticketing.service.category.service.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tcl.dias.beans.CategoriesBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.constants.ExceptionConstants;
import com.tcl.dias.constants.ServiceCategoryConstants;
import com.tcl.dias.response.beans.ServiceCategoryResponse;
import com.tcl.dias.response.beans.ServiceResponseBean;
import com.tcl.dias.ticketing.response.ErrorResponseDetails;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the ServiceCategoryService.java class is used to have all
 * the ServiceCategory API
 * 
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ServiceCategoryService extends TicketingAbstractService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceCategoryService.class);

	@Autowired
	RestClientService restClientService;

	@Value("${service.details.url}")
	String serviceDetailsUrl;

	@Value("${service.categories.details.url}")
	String serviceCategoriesDetailsUrl;

	@Value("${app.id}")
	String appId;

	@Value("${secret.key}")
	String appSecret;
	
	@Autowired
	MQUtils mqUtils;
	
	@Value("${rabbitmq.get.customer.service.id}")
	String customerServiceIdQueue;

	/**
	 * @author vivek getServiceDetails used to get the service details
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 * @throws ParseException
	 */
	public ServiceResponseBean getServiceDetails(String serviceId) throws TclCommonException {

		ServiceResponseBean serviceResponse = null;
		try {
			if (Objects.isNull(serviceId))
				throw new TclCommonException(ExceptionConstants.SERVICEID_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);
			LOGGER.info("calling rest client service  serviceDetailsUrl{}",serviceDetailsUrl);
			RestResponse response = restClientService.getWithQueryParam(serviceDetailsUrl + serviceId, null,
					getBasicAuth(appId, appSecret, getHeader(), null));
			LOGGER.info("response of rest client service  response{}",response);
			if (response.getStatus() == Status.SUCCESS) {
				return (ServiceResponseBean) Utils.convertJsonToObject(response.getData(), ServiceResponseBean.class);
			} else {
				serviceResponse = new ServiceResponseBean();
				ErrorResponseDetails errorResponseDetails = createErrorResponse(response);
				serviceResponse.setStatus(errorResponseDetails.getStatus());
				serviceResponse.setMessage(errorResponseDetails.getMessage());
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return serviceResponse;
	}

	/**
	 * @author vivek getCategories used to get the categories
	 * @param impact
	 * @param serviceId
	 * @param productType
	 * @return
	 * @throws TclCommonException
	 * @throws ParseException
	 */
	public ServiceCategoryResponse getCategories(List<String> serviceId) throws TclCommonException {

		ServiceCategoryResponse serviceCategoryResponse = new ServiceCategoryResponse();
		try {
			if (Objects.isNull(serviceId) || serviceId.isEmpty())
				throw new TclCommonException(ExceptionConstants.SERVICEID_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);
			if (serviceId.size() == 1) {
				String responseId="";
				LOGGER.info("Performing queue call to fetch customer_service_id by passing serviceId {}",serviceId.get(0));
				String queueResponse = (String) mqUtils.sendAndReceive(customerServiceIdQueue, serviceId.get(0));
				LOGGER.info(" queue response after fetching customer_service_id {}",queueResponse);
				if(!StringUtils.isBlank(queueResponse)) {
					responseId=queueResponse;
				}
				else {
					responseId=serviceId.get(0);
				}
				LOGGER.info("  response after validating customer_service_id {}",responseId);
				ServiceResponseBean impactResponse = getServiceDetails(responseId);
				if(impactResponse.getStatus().equals("ERROR")) {
					serviceCategoryResponse.setStatus("ERROR");
					serviceCategoryResponse.setMessage(impactResponse.getMessage());
					return  serviceCategoryResponse;
				}

				if (impactResponse != null && impactResponse.getImpacts() != null
						&& !impactResponse.getImpacts().isEmpty()) {

					impactResponse.getImpacts().forEach(impact -> {
						RestResponse response = restClientService.getWithQueryParam(serviceCategoriesDetailsUrl,
								getCategoriesParam(impact, serviceId.get(0), impactResponse.getProductType()),
								getBasicAuth(appId, appSecret, getHeader(), null));
						if (response.getStatus() == Status.SUCCESS) {

							try {
								ServiceResponseBean responseBean = (ServiceResponseBean) Utils
										.convertJsonToObject(response.getData(), ServiceResponseBean.class);
								CategoriesBean bean = new CategoriesBean();
								bean.setImpactName(impact);
								bean.getCategories().addAll(responseBean.getCategories());
								serviceCategoryResponse.setStatus(response.getStatus().toString());
								serviceCategoryResponse.getCategories().add(bean);
							} catch (TclCommonException e) {
								LOGGER.warn("Error in get Categories for this impact{}", impact);

							}
						}

					});
				}

			} else {
				List<CategoriesBean> categoriesBeanList = new ArrayList<>();
				constructCategoriesBean(categoriesBeanList);
				serviceCategoryResponse.setCategories(categoriesBeanList);

			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return serviceCategoryResponse;

	}

	private void constructCategoriesBean(List<CategoriesBean> categoriesBeanList) {
		List<String> categoriesTL = new ArrayList<>();
		CategoriesBean categoriesBeanTotalLoss = new CategoriesBean();
		categoriesBeanTotalLoss.setImpactName(ServiceCategoryConstants.TOTAL_LOSS);
		categoriesTL.add(ServiceCategoryConstants.LINK_DOWN);
		categoriesTL.add(ServiceCategoryConstants.SITE_NOT_REACHABLE);
		categoriesTL.add(ServiceCategoryConstants.PACKET_LOSS);
		categoriesBeanTotalLoss.setCategories(categoriesTL);
		categoriesBeanList.add(categoriesBeanTotalLoss);

		List<String> categoriesPL = new ArrayList<>();
		CategoriesBean categoriesBeanPartialLoss = new CategoriesBean();
		categoriesBeanPartialLoss.setImpactName(ServiceCategoryConstants.PARTIAL_LOSS);
		categoriesPL.add(ServiceCategoryConstants.MARGINAL_PACKET_LOSS);
		categoriesPL.add(ServiceCategoryConstants.LINK_OR_PROTOCOL_FLAPPING);
		categoriesPL.add(ServiceCategoryConstants.IP_NOT_REACHABLE);
		categoriesPL.add(ServiceCategoryConstants.APPLICATION_PERFORMANCE_ISSUES);
		categoriesPL.add(ServiceCategoryConstants.LATENCY);
		categoriesBeanPartialLoss.setCategories(categoriesPL);
		categoriesBeanList.add(categoriesBeanPartialLoss);

		List<String> categoriesNL = new ArrayList<>();
		CategoriesBean categoriesBeanNoLoss = new CategoriesBean();
		categoriesBeanNoLoss.setImpactName(ServiceCategoryConstants.LOW_NO_IMPACT);
		categoriesNL.add(ServiceCategoryConstants.TRAFFIC_REPORT_RELATED);
		categoriesNL.add(ServiceCategoryConstants.PERFORMANCE_MONITORING);
		categoriesNL.add(ServiceCategoryConstants.SCHEDULED_TESTING);
		categoriesBeanNoLoss.setCategories(categoriesNL);
		categoriesBeanList.add(categoriesBeanNoLoss);

	}

}
