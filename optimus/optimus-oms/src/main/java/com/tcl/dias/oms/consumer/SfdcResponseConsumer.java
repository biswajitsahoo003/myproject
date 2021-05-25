package com.tcl.dias.oms.consumer;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.WaiverResponseBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.sfdc.response.bean.BundledOpportunityResponseBean;
import com.tcl.dias.common.sfdc.response.bean.FeasibilityResponseBean;
import com.tcl.dias.common.sfdc.response.bean.OpportunityResponseBean;
import com.tcl.dias.common.sfdc.response.bean.PartnerEntityContactResponseBean;
import com.tcl.dias.common.sfdc.response.bean.PartnerEntityResponseBean;
import com.tcl.dias.common.sfdc.response.bean.ProductServicesResponseBean;
import com.tcl.dias.common.sfdc.response.bean.SiteResponseBean;
import com.tcl.dias.common.sfdc.response.bean.StagingResponseBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.QuoteIzoSdwanAttributeValuesRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.service.v1.BundleOmsSfdcService;
import com.tcl.dias.oms.service.v1.PartnerSfdcService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;

/**
 * Consumer Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class SfdcResponseConsumer {

	@Autowired
	private OmsSfdcService omsSfdcService;

	@Autowired
	PartnerSfdcService partnerSfdcService;

	@Autowired
	BundleOmsSfdcService bundleOmsSfdcService;

	@Autowired
	QuoteIzoSdwanAttributeValuesRepository quoteIzoSdwanAttributeValuesRepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(SfdcResponseConsumer.class);

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.opportunity.response.create}") })
	@Transactional(readOnly = false)
	@Async
	public void createOptyResponseWrapper(String responseBody) {
		LOGGER.info("createOptyResponseWrapper - Started");
		createOptyResponse(responseBody,0);
		LOGGER.info("createOptyResponseWrapper - Completed");
	}
	
	
	public void createOptyResponse(String responseBody,int count) {
		try {
			LOGGER.info("reponse message received from SFDC with retry count {}-> {}", count,responseBody);
			if(responseBody.contains(CommonConstants.SDWAN)) {
				BundledOpportunityResponseBean bundledOpportunityResponseBean=(BundledOpportunityResponseBean)Utils.convertJsonToObject(responseBody, BundledOpportunityResponseBean.class);
				bundleOmsSfdcService.createOpportunityResponse(bundledOpportunityResponseBean);
			}else {
				OpportunityResponseBean opportunityResponseBean = (OpportunityResponseBean) Utils
						.convertJsonToObject(responseBody, OpportunityResponseBean.class);
				omsSfdcService.processSfdcOpportunityCreateResponse(opportunityResponseBean);
			}

		} catch (Exception e) {
			LOGGER.error("Error in getting the create opportunity response", e);
			if(count<3) {
				LOGGER.info(
						"Waiting for 30 seconds");
				try {
					TimeUnit.SECONDS.sleep(30);
				} catch (InterruptedException e1) {
					LOGGER.warn("Sleep Intrepted");
				}
				LOGGER.info("Since it failed we will retry for {}",count);
				createOptyResponse(responseBody, count+1);	
			}else {
				LOGGER.warn("Retry exhasted");
			}
		}
		LOGGER.info("createOptyResponse - Completed with retry {}",count);
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.opportunity.response.productservices}") })
	@Transactional
	public void productServiceResponse(String responseBody) {
		try {
			LOGGER.info("update response in oms:{}",responseBody);
			ProductServicesResponseBean productServicesResponseBean = (ProductServicesResponseBean) Utils
					.convertJsonToObject(responseBody,
							ProductServicesResponseBean.class);
			productServicesResponseBean.getProductId();
			if (productServicesResponseBean.getIsCancel() != null && productServicesResponseBean.getIsCancel()) {
				omsSfdcService.processSfdcProductServiceForCancel(productServicesResponseBean);
			}
			
			if (productServicesResponseBean.getProductSolutionCode() != null && 
					productServicesResponseBean.getProductSolutionCode().contains("MLC-")) {
				LOGGER.info("multicircuit flow");
				omsSfdcService.processSfdcProductServiceMulticircuit(productServicesResponseBean);
			} else if (productServicesResponseBean.getProductSolutionCode() != null &&
					productServicesResponseBean.getProductSolutionCode().contains("TERM-")) {
				LOGGER.info("termination child opty create product response flow");
				omsSfdcService.processSfdcProductServiceTermination(productServicesResponseBean);
			}else {
				if (productServicesResponseBean.getQuoteToLeId() != null) {
					Optional<QuoteToLe> quoteToLe = quoteToLeRepository
							.findById(productServicesResponseBean.getQuoteToLeId());
					if (quoteToLe.isPresent()) {
						if (quoteToLe.get().getQuote().getQuoteCode().startsWith(IzosdwanCommonConstants.IZOSDWAN)) {
							bundleOmsSfdcService.processSfdcProductService(productServicesResponseBean);
						} else {
							omsSfdcService.processSfdcProductService(productServicesResponseBean);
						}
					} else {
						omsSfdcService.processSfdcProductService(productServicesResponseBean);
					}
				}else {
					omsSfdcService.processSfdcProductService(productServicesResponseBean);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting the product services response", e);
		}
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.opportunity.response.site}") })
	public void updateSiteResponse(String responseBody) {
		try {
			LOGGER.info("Input payload for update site {} ", responseBody);
			SiteResponseBean siteResponseBean = (SiteResponseBean) Utils.convertJsonToObject(responseBody,
					SiteResponseBean.class);
			if (siteResponseBean.getIsCancel() != null && siteResponseBean.getIsCancel()) {
				omsSfdcService.processSfdcSitesCancel(siteResponseBean);
			} else {
				omsSfdcService.processSfdcSites(siteResponseBean);
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting the update site response", e);
		}
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.opportunity.response.update}") })
	public void updateOptyStatus(String responseBody) {
		try {
			LOGGER.info("Input payload for update status {} ", responseBody);
			StagingResponseBean opportunityResponseBean = (StagingResponseBean) Utils
					.convertJsonToObject(responseBody, StagingResponseBean.class);
			omsSfdcService.processSfdcUpdateOpty(opportunityResponseBean);
		} catch (Exception e) {
			LOGGER.error("Error in getting the update opportunity status response", e);
		}
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.sfdc.feasibility.response}") })
	public void feasibilityResponse(String responseBody) {
		try {
			FeasibilityResponseBean feasibilityResponseBean = (FeasibilityResponseBean) Utils
					.convertJsonToObject(responseBody, FeasibilityResponseBean.class);
			omsSfdcService.processSfdcFeasibilityResponse(feasibilityResponseBean);
		} catch (Exception e) {
			LOGGER.error("Error in processing the create feasibility response", e);
		}
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.create.partner.entity.response.update}") })
	public void updateCreatePartnerEntityStatus(String responseBody) {
		try {
			LOGGER.info("Input payload for update status {} ", responseBody);
			PartnerEntityResponseBean partnerEntityResponseBean = (PartnerEntityResponseBean) Utils
					.convertJsonToObject(responseBody, PartnerEntityResponseBean.class);
			partnerSfdcService.processSfdcUpdatePartnerCreateEntity(partnerEntityResponseBean);
		} catch (Exception e) {
			LOGGER.error("Error in getting the update opportunity status response", e);
		}
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.sfdc.waiver.response.create}") })
	public void updateWaiverStatus(String responseBody) {
		try {
			LOGGER.info("Input payload for update status {} ", responseBody);
			WaiverResponseBean waiverResponseBean = (WaiverResponseBean) Utils
					.convertJsonToObject(responseBody, WaiverResponseBean.class);
			omsSfdcService.processSfdcPersistCreateWaiver(waiverResponseBean);
		} catch (Exception e) {
			LOGGER.error("Error in getting the create waiver status response", e);
		}
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.sfdc.waiver.response.update}") })
	public void updateWaiverUpdateStatus(String responseBody) {
		try {
			LOGGER.info("Input payload for update status {} ", responseBody);
			WaiverResponseBean waiverResponseBean = (WaiverResponseBean) Utils
					.convertJsonToObject(responseBody, WaiverResponseBean.class);
			omsSfdcService.processSfdcPersistUpdateWaiver(waiverResponseBean);
		} catch (Exception e) {
			LOGGER.error("Error in getting the update waiver status response", e);
		}
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.create.partner.entity.contact.response.update}") })
	public void updateCreatePartnerEntityContactStatus(String responseBody) {
		try {
			LOGGER.info("Input payload for update status {} ", responseBody);
			PartnerEntityContactResponseBean partnerEntityContactResponseBean = (PartnerEntityContactResponseBean) Utils
					.convertJsonToObject(responseBody, PartnerEntityContactResponseBean.class);
			partnerSfdcService.processSfdcUpdatePartnerCreateEntityContact(partnerEntityContactResponseBean);
		} catch (Exception e) {
			LOGGER.error("Error in getting the update opportunity status for create contact response", e);
		}
	}

}
