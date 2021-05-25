package com.tcl.dias.batch.sfdc.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import com.tcl.dias.common.sfdc.bean.OpportunityBean;
import com.tcl.dias.common.beans.TerminationWaiverBean;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.batch.dao.ThirdPartyServiceJobDao;
import com.tcl.dias.common.beans.COPFOmsRequest;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.constants.SfdcServiceStatus;
import com.tcl.dias.common.sfdc.bean.BCROmsRequest;
import com.tcl.dias.common.sfdc.bean.BCROmsResponse;
import com.tcl.dias.common.sfdc.bean.FeasibilityRequestBean;
import com.tcl.dias.common.sfdc.bean.OpportunityBean;
import com.tcl.dias.common.sfdc.bean.ProductServiceBean;
import com.tcl.dias.common.sfdc.bean.SiteSolutionOpportunityBean;
import com.tcl.dias.common.sfdc.bean.UpdateOpportunityStage;
import com.tcl.dias.common.sfdc.constants.SfdcServiceTypeConstants;
import com.tcl.dias.common.sfdc.response.bean.FeasibilityResponseBean;
import com.tcl.dias.common.sfdc.response.bean.OpportunityResponseBean;
import com.tcl.dias.common.sfdc.response.bean.ProductServicesResponseBean;
import com.tcl.dias.common.sfdc.response.bean.ProductsserviceResponse;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.ThirdPartySource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteIzoSdwanAttributeValues;
import com.tcl.dias.oms.entity.entities.QuoteSiteServiceTerminationDetails;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzoSdwanAttributeValuesRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteSiteServiceTerminationDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import static com.tcl.dias.common.sfdc.constants.SfdcServiceTypeConstants.CREATE_OPPORTUNITY;
import static com.tcl.dias.common.sfdc.constants.SfdcServiceTypeConstants.CREATE_PRODUCT;
import static com.tcl.dias.common.sfdc.constants.SfdcServiceTypeConstants.SFDC;
import static com.tcl.dias.common.sfdc.constants.SfdcServiceTypeConstants.UCDR;

/**
 * This file contains the SfdcServiceJob.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@DisallowConcurrentExecution
public class SfdcJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(SfdcJob.class);

	/**
	 * execute
	 * 
	 * @param context
	 * @throws JobExecutionException
	 */
	@Override
	@Transactional
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			ApplicationContext appCtx = (ApplicationContext) jobDataMap.get("appCtx");
			MQUtils mqUtils = appCtx.getBean(MQUtils.class);
			ThirdPartyServiceJobDao sfdcServiceJobDao = appCtx.getBean(ThirdPartyServiceJobDao.class);
			ThirdPartyServiceJobsRepository thirdServiceJobsRepository = appCtx
					.getBean(ThirdPartyServiceJobsRepository.class);
			LOGGER.info("Before fetching the full query for jobid {}",context.getJobDetail().getKey());
			Utils.logMemory();
			List<ThirdPartyServiceJob> sfdcServiceJobs = thirdServiceJobsRepository
					.findSfdcByServiceStatusBySeq(SfdcServiceStatus.NEW.toString());
			Utils.logMemory();
			LOGGER.info("After fetching {}",sfdcServiceJobs.size());
			List<String> statuses = new ArrayList<>();
			statuses.add(SfdcServiceStatus.INPROGRESS.toString());
			statuses.add(SfdcServiceStatus.FAILURE.toString());
			statuses.add(SfdcServiceStatus.STRUCK.toString());
			Utils.logMemory();
			LOGGER.info("Before fetching status query with {} ",statuses);
			List<ThirdPartyServiceJob> inprogressSfdcBeans = thirdServiceJobsRepository
					.findByServiceStatusInAndThirdPartySourceAndIsActive(statuses, ThirdPartySource.SFDC.toString(),CommonConstants.BACTIVE);
			LOGGER.info("After fetching inprogress query with {} ",inprogressSfdcBeans.size());
			Utils.logMemory();
			Map<String, ThirdPartyServiceJob> inprogressMapper = new HashMap<>();
			inprogressSfdcBeans.forEach(sfdcServiceJob -> {
				inprogressMapper.put(sfdcServiceJob.getRefId(), sfdcServiceJob);
			});
			List<String> serviceTypesToIgnore = new ArrayList<>();
			serviceTypesToIgnore.add(SfdcServiceTypeConstants.CREATE_FEASIBILITY);
			serviceTypesToIgnore.add(SfdcServiceTypeConstants.UPDATE_FEASIBILITY);
			serviceTypesToIgnore.add(SfdcServiceTypeConstants.CREATE_OPEN_BCR);
			serviceTypesToIgnore.add(SfdcServiceTypeConstants.CREATE_CLOSED_BCR);
			serviceTypesToIgnore.add(SfdcServiceTypeConstants.CREATE_INPROGRESS_BCR);
			serviceTypesToIgnore.add(SfdcServiceTypeConstants.CREATE_UPDATE_BCR);
			List<String> refIdMapper=new ArrayList<>();
			sfdcServiceJobs.forEach(sfdcServiceJob -> {
				try {
					boolean isIgnore=false;
					if(sfdcServiceJob.getIsDropped()!=null && sfdcServiceJob.getIsDropped().equals(CommonConstants.BACTIVE)) {
						LOGGER.info("drop request so proceeding");
						isIgnore=true;
					} else {
						if (!sfdcServiceJob.getServiceType()
								.equalsIgnoreCase(SfdcServiceTypeConstants.CREATE_OPPORTUNITY)) {
							List<ThirdPartyServiceJob> dropSfdcBeans = thirdServiceJobsRepository
									.findByRefIdAndIsDroppedAndThirdPartySource(sfdcServiceJob.getRefId(),
											CommonConstants.BACTIVE, ThirdPartySource.SFDC.toString());
							for (ThirdPartyServiceJob dropRequest : dropSfdcBeans) {
								if(dropRequest.getServiceStatus().equals(SfdcServiceStatus.SUCCESS.toString())) {
									thirdServiceJobsRepository
									.updateServiceStatusByRefIdAndThirdPartySourceAndServiceStatus(
											sfdcServiceJob.getRefId(), SfdcServiceStatus.SUCCESS.toString(),
											ThirdPartySource.SFDC.toString(),
											"dropped so updating the status as success");
									return;
								}
								sfdcServiceJob = dropRequest;
								isIgnore = true;
								thirdServiceJobsRepository
										.updateServiceStatusByRefIdAndThirdPartySourceAndServiceStatus(
												sfdcServiceJob.getRefId(), SfdcServiceStatus.SUCCESS.toString(),
												ThirdPartySource.SFDC.toString(),
												"dropped so updating the status as success");
								break;
							}
						}
					}
					
					if (!isIgnore) {
						ThirdPartyServiceJob inprogressFailureJob = inprogressMapper.get(sfdcServiceJob.getRefId());
						if (inprogressFailureJob != null
								&& !serviceTypesToIgnore.contains(inprogressFailureJob.getServiceType())) {
							if (inprogressFailureJob.getServiceStatus().equals(SfdcServiceStatus.FAILURE.toString())) {
								sfdcServiceJobDao.updateStruckStatus(sfdcServiceJob.getRefId(),
										ThirdPartySource.SFDC.toString());
							}
							return;
						}
					}
					
					
					if (sfdcServiceJob.getIsComplete() == CommonConstants.BDEACTIVATE) {
						LOGGER.info("Got incomplete records!!");
						Utils.logMemory();
						processIncompleteRequests(appCtx, sfdcServiceJob);
						Utils.logMemory();
						LOGGER.info("After incomplete records!!");
					}
					
					if(sfdcServiceJob.getServiceType().equalsIgnoreCase(SfdcServiceTypeConstants.UPDATE_FEASIBILITY)) {
						FeasibilityRequestBean requestBean = Utils.convertJsonToObject(sfdcServiceJob.getRequestPayload(),FeasibilityRequestBean.class);
						if(requestBean == null || StringUtils.isEmpty(requestBean.getId())) {
							sfdcServiceJob.setServiceStatus(SfdcServiceStatus.FAILURE.toString());
							sfdcServiceJob.setResponsePayload("Create Feasibility failed for the site");
							sfdcServiceJob.setUpdatedBy("system");
							sfdcServiceJob.setUpdatedTime(new Date());
							thirdServiceJobsRepository.save(sfdcServiceJob);
							return;
						}	
					}
					if (refIdMapper.contains(sfdcServiceJob.getRefId())) {
						LOGGER.info("Already {} is done for  {} so stacking for the next pick with id {}",
								sfdcServiceJob.getRefId(), sfdcServiceJob.getServiceType(), sfdcServiceJob.getId());
						return;
					} else {
						refIdMapper.add(sfdcServiceJob.getRefId());
					}
					LOGGER.info("SFDC Job triggered for service_type :: {}",sfdcServiceJob.getServiceType());
					mqUtils.send(sfdcServiceJob.getQueueName(), sfdcServiceJob.getRequestPayload());
					sfdcServiceJob.setServiceStatus(SfdcServiceStatus.INPROGRESS.toString());
					sfdcServiceJob.setUpdatedBy("system");
					sfdcServiceJob.setUpdatedTime(new Date());
					thirdServiceJobsRepository.save(sfdcServiceJob);
					LOGGER.info("SFDC JOB Triggered for {}-{}", sfdcServiceJob.getRefId(), sfdcServiceJob.getId());
				} catch (Exception e) {
					LOGGER.error("Error in processing the id " + sfdcServiceJob, e);
					sfdcServiceJob.setServiceStatus(SfdcServiceStatus.FAILURE.toString());
					sfdcServiceJob.setResponsePayload(e.getMessage());
					sfdcServiceJob.setUpdatedBy("system");
					sfdcServiceJob.setUpdatedTime(new Date());
					thirdServiceJobsRepository.save(sfdcServiceJob);
				}
			});
		} catch (Exception e) {
			LOGGER.error("Error in executing component " + context.getJobDetail().getKey(), e);
		}
		LOGGER.trace("DatabaseNormalizerJob.execute method end.");

	}

	/**
	 * processIncompleteRequests
	 * 
	 * @param appCtx
	 * @param sfdcServiceJob
	 * @throws TclCommonException
	 */
	private void processIncompleteRequests(ApplicationContext appCtx, ThirdPartyServiceJob sfdcServiceJob)
			throws TclCommonRuntimeException {
		QuoteRepository quoteRepository = appCtx.getBean(QuoteRepository.class);

		LOGGER.info("Incoming service type {} for quote {} in incomplete request",sfdcServiceJob.getServiceType(),sfdcServiceJob.getRefId());

		try {
			switch (sfdcServiceJob.getServiceType()) {
			case SfdcServiceTypeConstants.CREATE_OPPORTUNITY: {
				OpportunityBean opportunityBean = (OpportunityBean) Utils
						.convertJsonToObject(sfdcServiceJob.getRequestPayload(), OpportunityBean.class);
				opportunityBean
						.setParentTerminationOpportunityName(getChildSfdcId(sfdcServiceJob.getRefId(), quoteRepository,sfdcServiceJob.getServiceRefId()));
				sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(opportunityBean));
			}
				break;
			case SfdcServiceTypeConstants.CREATE_PRODUCT: {
				if (sfdcServiceJob.getRefId().contains("-C")) {
					ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository = appCtx
							.getBean(ThirdPartyServiceJobsRepository.class);
					ProductServiceBean productSolution = (ProductServiceBean) Utils
							.convertJsonToObject(sfdcServiceJob.getRequestPayload(), ProductServiceBean.class);
					productSolution
							.setOpportunityId(getOptyId(sfdcServiceJob.getRefId(), thirdPartyServiceJobsRepository));
					sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(productSolution));
				} else {
					ProductServiceBean productSolution = (ProductServiceBean) Utils
							.convertJsonToObject(sfdcServiceJob.getRequestPayload(), ProductServiceBean.class);
					String optyId=getSfdcId(sfdcServiceJob.getRefId(), quoteRepository,sfdcServiceJob.getServiceRefId());
					if (StringUtils.isNotBlank(optyId)) {
						productSolution.setOpportunityId(optyId);
					} else {
						ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository = appCtx
								.getBean(ThirdPartyServiceJobsRepository.class);
						productSolution.setOpportunityId(
								getOptyId(sfdcServiceJob.getRefId(), thirdPartyServiceJobsRepository));
					}
					sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(productSolution));
				}
			}
				break;
			case SfdcServiceTypeConstants.UPDATE_PRODUCT:
				LOGGER.info("Inside update product {}", sfdcServiceJob.getRefId());
				if (sfdcServiceJob.getRefId().startsWith("GSC")) {
					ProductSolutionRepository productSolutionRepositor = appCtx
							.getBean(ProductSolutionRepository.class);
					ProductServiceBean productSolution = (ProductServiceBean) Utils
							.convertJsonToObject(sfdcServiceJob.getRequestPayload(), ProductServiceBean.class);
					productSolution.setOpportunityId(getSfdcId(sfdcServiceJob.getRefId(), quoteRepository,sfdcServiceJob.getServiceRefId()));
					List<ProductSolution> prodSol = productSolutionRepositor
							.findByReferenceCode(sfdcServiceJob.getRefId());
					prodSol.forEach(productSolution2 -> {

						if (Objects.nonNull(productSolution2.getTpsSfdcProductName())) {
							productSolution.setProductName(productSolution2.getTpsSfdcProductName());
							productSolution.setProductId(productSolution2.getTpsSfdcProductId());
							return;
						}

					});
					sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(productSolution));
				} else if (sfdcServiceJob.getRefId().startsWith("IZOSDWAN")) {
					QuoteIzoSdwanAttributeValuesRepository quoteIzoSdwanAttributeValuesRepository = appCtx
							.getBean(QuoteIzoSdwanAttributeValuesRepository.class);
					ProductServiceBean productSolution = (ProductServiceBean) Utils
							.convertJsonToObject(sfdcServiceJob.getRequestPayload(), ProductServiceBean.class);
					productSolution.setOpportunityId(getSfdcId(sfdcServiceJob.getRefId(), quoteRepository,sfdcServiceJob.getServiceRefId()));
					Quote quote = quoteRepository.findByQuoteCode(sfdcServiceJob.getRefId());
					if (productSolution.getSdwanProductName() != null) {

						String displayValue = CommonConstants.EMPTY;
						String displayValueProdName = CommonConstants.EMPTY;

						switch (productSolution.getSdwanProductName()) {
						case IzosdwanCommonConstants.IZOSDWAN_NAME:
							displayValue = IzosdwanCommonConstants.SDWANSFDCProductId;
							displayValueProdName = IzosdwanCommonConstants.SDWANSFDCProductName;
							break;
						case "IAS":
							displayValue = IzosdwanCommonConstants.IASSFDCProductId;
							displayValueProdName = IzosdwanCommonConstants.IASSFDCProductName;
							break;
						case IzosdwanCommonConstants.IZO_INTERNET_WAN_PRODUCT:
							displayValue = IzosdwanCommonConstants.IWANSFDCProductId;
							displayValueProdName = IzosdwanCommonConstants.IWANSFDCProductName;
							break;
						case "GVPN":
							displayValue = IzosdwanCommonConstants.GVPNSFDCProductId;
							displayValueProdName = IzosdwanCommonConstants.GVPNSFDCProductName;
							break;
						case IzosdwanCommonConstants.IP_TRANSIT_PRODUCT:
							displayValue = IzosdwanCommonConstants.IPTRANSITSFDCProductId;
							displayValueProdName = IzosdwanCommonConstants.IPTRANSITSFDCProductName;
							break;
						case IzosdwanCommonConstants.BYON_INTERNET_PRODUCT:
							displayValue = IzosdwanCommonConstants.ILLBYONSFDCProductId;
							displayValueProdName = IzosdwanCommonConstants.ILLBYONSFDCProductName;
							break;
						case IzosdwanCommonConstants.BYON_MPLS_PRODUCT:
							displayValue = IzosdwanCommonConstants.GVPNBYONSFDCPRODUCTID;
							displayValueProdName = IzosdwanCommonConstants.GVPNBYONSFDCPRODUCTNAME;
							break;
						case IzosdwanCommonConstants.VPROXY:
							displayValue = IzosdwanCommonConstants.VproxySFDCProductId;
							displayValueProdName = IzosdwanCommonConstants.VproxySFDCProductName;
							break;
						case IzosdwanCommonConstants.VUTM:
							displayValue = IzosdwanCommonConstants.VutmSFDCProductId;
							displayValueProdName = IzosdwanCommonConstants.VutmSFDCProductName;
							break;
						default:
							break;
						}
						List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
								.findByDisplayValueAndQuote_id(displayValue, quote.getId());
						if (quoteIzoSdwanAttributeValues != null && !quoteIzoSdwanAttributeValues.isEmpty()) {
							productSolution.setProductId(quoteIzoSdwanAttributeValues.get(0).getAttributeValue());
						}
						List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues1 = quoteIzoSdwanAttributeValuesRepository
								.findByDisplayValueAndQuote_id(displayValueProdName, quote.getId());
						if (quoteIzoSdwanAttributeValues1 != null && !quoteIzoSdwanAttributeValues1.isEmpty()) {
							productSolution.setProductName(quoteIzoSdwanAttributeValues1.get(0).getAttributeValue());
						}
					}

					sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(productSolution));
				} else {
					ProductSolutionRepository productSolutionRepositor = appCtx
							.getBean(ProductSolutionRepository.class);
					ProductServiceBean productSolution = (ProductServiceBean) Utils
							.convertJsonToObject(sfdcServiceJob.getRequestPayload(), ProductServiceBean.class);
					if (productSolution.getProductSolutionCode().contains("MLC-")) {
						String quoteCode = productSolution.getProductSolutionCode().replace("MLC-", "").trim();
						productSolution.setOpportunityId(getSfdcId(sfdcServiceJob.getRefId(), quoteRepository,sfdcServiceJob.getServiceRefId()));
						List<ProductSolution> prodSol = productSolutionRepositor.findByReferenceCode(quoteCode);
						prodSol.forEach(productSolution2 -> {
							productSolution.setProductName(productSolution2.getTpsSfdcProductName());
							productSolution.setProductId(productSolution2.getTpsSfdcProductId());
						});
					}else if (productSolution.getProductSolutionCode().contains("TERM-")) {
						QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository = appCtx
								.getBean(QuoteIllSiteToServiceRepository.class);
						QuoteToLeRepository quoteToLeRepository = appCtx
								.getBean(QuoteToLeRepository.class);
						String quoteCodeRaw=productSolution.getProductSolutionCode().replaceAll("TERM-", "").trim();
						String splitter[]=quoteCodeRaw.split("--");
						String quoteCode=splitter[0];
						String serviceId=splitter[1];
						List<QuoteToLe> quoteLes = quoteToLeRepository.findByQuote_QuoteCode(quoteCode);
						for (QuoteToLe quoteToLe : quoteLes) {
							List<QuoteIllSiteToService> quoteIllSiteToServices= quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quoteToLe);
							for (QuoteIllSiteToService quoteIllSiteToService : quoteIllSiteToServices) {
								productSolution.setProductName(quoteIllSiteToService.getTpsSfdcProductName());
								productSolution.setProductId(quoteIllSiteToService.getTpsSfdcProductId());
							}
						}
					} else {
						productSolution.setOpportunityId(getSfdcId(sfdcServiceJob.getRefId(), quoteRepository,sfdcServiceJob.getServiceRefId()));
						List<ProductSolution> prodSol = productSolutionRepositor
								.findBySolutionCode(productSolution.getProductSolutionCode());
						prodSol.forEach(productSolution2 -> {
							productSolution.setProductName(productSolution2.getTpsSfdcProductName());
							productSolution.setProductId(productSolution2.getTpsSfdcProductId());
						});
					}
					sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(productSolution));
				}
				break;
			case SfdcServiceTypeConstants.DELETE_PRODUCT: {
				ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository = appCtx
						.getBean(ThirdPartyServiceJobsRepository.class);
				ProductSolutionRepository productSolutionRepository = appCtx.getBean(ProductSolutionRepository.class);
				ProductServiceBean productSolution = (ProductServiceBean) Utils
						.convertJsonToObject(sfdcServiceJob.getRequestPayload(), ProductServiceBean.class);
				productSolution.setOpportunityId(getSfdcId(sfdcServiceJob.getRefId(), quoteRepository,sfdcServiceJob.getServiceRefId()));
				if (productSolution.getProductSolutionCode().contains("MLC-")) {
					String quoteCode = productSolution.getProductSolutionCode().replace("MLC-", "").trim();
					List<ProductSolution> prodSol = productSolutionRepository.findByReferenceCode(quoteCode);
					List<String> productIds = new ArrayList<>();
					prodSol.forEach(productSolution2 -> {
						if (StringUtils.isNotBlank(productSolution2.getTpsSfdcProductId()))
							productIds.add(productSolution2.getTpsSfdcProductId());
					});
					if (!productIds.isEmpty())
						productSolution.setProductIds(productIds);
				}else if (productSolution.getProductSolutionCode().contains("TERM-")) {
					QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository = appCtx
							.getBean(QuoteIllSiteToServiceRepository.class);
					QuoteToLeRepository quoteToLeRepository = appCtx
							.getBean(QuoteToLeRepository.class);
					String quoteCodeRaw=productSolution.getProductSolutionCode().replaceAll("TERM-", "").trim();
					String splitter[]=quoteCodeRaw.split("--");
					List<String> productIds = new ArrayList<>();
					String quoteCode=splitter[0];
					String serviceId=splitter[1];
					List<QuoteToLe> quoteLes = quoteToLeRepository.findByQuote_QuoteCode(quoteCode);
					for (QuoteToLe quoteToLe : quoteLes) {
						List<QuoteIllSiteToService> quoteIllSiteToServices= quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quoteToLe);
						for (QuoteIllSiteToService quoteIllSiteToService : quoteIllSiteToServices) {
							productIds.add(quoteIllSiteToService.getTpsSfdcProductId());
						}
					}
					if (!productIds.isEmpty())
						productSolution.setProductIds(productIds);
				} else {
					List<ProductSolution> prodSol = productSolutionRepository
							.findBySolutionCode(productSolution.getProductSolutionCode());
					List<String> productIds = new ArrayList<>();
					prodSol.forEach(productSolution2 -> {
						if (StringUtils.isNotBlank(productSolution2.getTpsSfdcProductId()))
							productIds.add(productSolution2.getTpsSfdcProductId());
					});

					// For teamsdr
					if(prodSol.isEmpty() && sfdcServiceJob.getRefId().startsWith(UCDR) &&
							Objects.nonNull(productSolution.getParentQuoteToLeId())){
						QuoteToLeRepository quoteToLeRepository = appCtx
								.getBean(QuoteToLeRepository.class);
						QuoteToLe parentQuoteToLe = quoteToLeRepository.findById(productSolution.getParentQuoteToLeId()).get();
						getTpsSfdcProductId(parentQuoteToLe,thirdPartyServiceJobsRepository,productSolution,productIds);
					}

					if (!productIds.isEmpty())
						productSolution.setProductIds(productIds);
				}

				sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(productSolution));

			}
				break;
			case SfdcServiceTypeConstants.UPDATE_OPPORTUNITY: {
				UpdateOpportunityStage updateOpportunityStage = (UpdateOpportunityStage) Utils
						.convertJsonToObject(sfdcServiceJob.getRequestPayload(), UpdateOpportunityStage.class);
				if(Objects.nonNull(sfdcServiceJob.getSeqNum()) && sfdcServiceJob.getSeqNum().equals(8)) {
					updateOpportunityStage.setCloseDate(DateUtil.convertDateToString(new Date()));
				}
				if (sfdcServiceJob.getRefId().contains("-C")) {
					ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository = appCtx
							.getBean(ThirdPartyServiceJobsRepository.class);
					updateOpportunityStage
							.setOpportunityId(getOptyId(sfdcServiceJob.getRefId(), thirdPartyServiceJobsRepository));
				} else if(sfdcServiceJob.getRefId().startsWith(UCDR)){
					ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository = appCtx
							.getBean(ThirdPartyServiceJobsRepository.class);
					QuoteToLeRepository quoteToLeRepository = appCtx
							.getBean(QuoteToLeRepository.class);
					Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(updateOpportunityStage.getQuoteToLeId());
					if(quoteToLe.isPresent()){
						updateOpportunityStage.setOpportunityId(quoteToLe.get().getTpsSfdcOptyId());
					}else{
						// QuoteTole might got deleted ...
						// so fetching opty from from create opty...
						String optyId =  getCreateOptyId(thirdPartyServiceJobsRepository
								,updateOpportunityStage.getQuoteToLeId(),sfdcServiceJob.getRefId());
						LOGGER.info("OptyId fetched is :: {}",optyId);
						updateOpportunityStage.setOpportunityId(optyId);

					}
				} else {
					QuoteToLeRepository quoteToLeRepository = appCtx
							.getBean(QuoteToLeRepository.class);
					List<QuoteToLe> quoteLes = quoteToLeRepository.findByQuote_QuoteCode(sfdcServiceJob.getRefId());
					QuoteToLe quoteLe = quoteLes.get(0);
					
					if("TERMINATION".equalsIgnoreCase(quoteLe.getQuoteType())) {
						LOGGER.info("Termination opty - update opty");
						if(Objects.nonNull(quoteLe.getIsMultiCircuit()) && CommonConstants.BACTIVE.equals(quoteLe.getIsMultiCircuit())) {
							LOGGER.info("Multicircuit quote true? {}", quoteLe.getIsMultiCircuit());
							if(updateOpportunityStage.getDummyParentTerminationOpportunity() != null 
									&& "true".equalsIgnoreCase(updateOpportunityStage.getDummyParentTerminationOpportunity())) {
								LOGGER.info("dummy parent opty");
								updateOpportunityStage.setOpportunityId(getChildSfdcId(sfdcServiceJob.getRefId(), quoteRepository,sfdcServiceJob.getServiceRefId()));
								
							} else {
								LOGGER.info("Child opty");
								updateOpportunityStage.setOpportunityId(getSfdcId(sfdcServiceJob.getRefId(), quoteRepository,sfdcServiceJob.getServiceRefId()));
							}
						} else {
							LOGGER.info("not a multicircuit quote {}", quoteLe.getIsMultiCircuit());
							updateOpportunityStage.setOpportunityId(getChildSfdcId(sfdcServiceJob.getRefId(), quoteRepository,sfdcServiceJob.getServiceRefId()));
							
						}
					} else {
					updateOpportunityStage.setOpportunityId(getSfdcId(sfdcServiceJob.getRefId(), quoteRepository,sfdcServiceJob.getServiceRefId()));
					}
				}
				sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(updateOpportunityStage));

			}
				break;
			case SfdcServiceTypeConstants.UPDATE_SITE: {

				ProductSolutionRepository productSolutionRepositor = appCtx.getBean(ProductSolutionRepository.class);
				SiteSolutionOpportunityBean siteSolutionOpportunityBean = (SiteSolutionOpportunityBean) Utils
						.convertJsonToObject(sfdcServiceJob.getRequestPayload(), SiteSolutionOpportunityBean.class);
				if (sfdcServiceJob.getRefId().startsWith(IzosdwanCommonConstants.IZOSDWAN)) {
					QuoteIzoSdwanAttributeValuesRepository quoteIzoSdwanAttributeValuesRepository = appCtx
							.getBean(QuoteIzoSdwanAttributeValuesRepository.class);
					Quote quote = quoteRepository.findByQuoteCode(sfdcServiceJob.getRefId());
					if (quote != null && siteSolutionOpportunityBean != null
							&& siteSolutionOpportunityBean.getSiteProduct() != null) {
						String displayValue = CommonConstants.EMPTY;

						switch (siteSolutionOpportunityBean.getSiteProduct()) {
						case IzosdwanCommonConstants.IZOSDWAN_NAME:
							displayValue = IzosdwanCommonConstants.SDWANSFDCProductId;
							break;
						case IzosdwanCommonConstants.CGW:
							displayValue = IzosdwanCommonConstants.SDWANSFDCProductId;
							break;
						case "IAS":
							displayValue = IzosdwanCommonConstants.IASSFDCProductId;
							break;
						case "GVPN":
							displayValue = IzosdwanCommonConstants.GVPNSFDCProductId;
							break;
						case IzosdwanCommonConstants.BYON_INTERNET_PRODUCT:
							displayValue = IzosdwanCommonConstants.ILLBYONSFDCProductId;
							break;
						default:
							break;
						}
						List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
								.findByDisplayValueAndQuote_id(displayValue, quote.getId());
						if (quoteIzoSdwanAttributeValues != null && !quoteIzoSdwanAttributeValues.isEmpty()) {
							siteSolutionOpportunityBean
									.setProductServiceId(quoteIzoSdwanAttributeValues.get(0).getAttributeValue());
						}
					}

				} else {
					if (siteSolutionOpportunityBean.getProductSolutionCode().contains("MLC-")) {
						String quoteCode = siteSolutionOpportunityBean.getProductSolutionCode().replace("MLC-", "")
								.trim();
						siteSolutionOpportunityBean
								.setOpportunityId(getSfdcId(sfdcServiceJob.getRefId(), quoteRepository,sfdcServiceJob.getServiceRefId()));
						List<ProductSolution> prodSol = productSolutionRepositor.findByReferenceCode(quoteCode);
						prodSol.forEach(productSolution2 -> siteSolutionOpportunityBean
								.setProductServiceId(productSolution2.getTpsSfdcProductId()));
					} else if (siteSolutionOpportunityBean.getProductSolutionCode().contains("TERM-")) {
						
						QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository = appCtx
								.getBean(QuoteIllSiteToServiceRepository.class);
						QuoteToLeRepository quoteToLeRepository = appCtx
								.getBean(QuoteToLeRepository.class);
						String quoteCodeRaw=siteSolutionOpportunityBean.getProductSolutionCode().replaceAll("TERM-", "").trim();
						String splitter[]=quoteCodeRaw.split("--");
						String quoteCode=splitter[0];
						String serviceId=splitter[1];
						siteSolutionOpportunityBean
						.setOpportunityId(getSfdcId(sfdcServiceJob.getRefId(), quoteRepository,serviceId));
						List<QuoteToLe> quoteLes = quoteToLeRepository.findByQuote_QuoteCode(quoteCode);
						for (QuoteToLe quoteToLe : quoteLes) {
							List<QuoteIllSiteToService> quoteIllSiteToServices= quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quoteToLe);
							for (QuoteIllSiteToService quoteIllSiteToService : quoteIllSiteToServices) {
								siteSolutionOpportunityBean
								.setProductServiceId(quoteIllSiteToService.getTpsSfdcProductId());
							}
						}
					}else {
						QuoteToLeRepository quoteToLeRepository = appCtx
								.getBean(QuoteToLeRepository.class);
						List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_QuoteCode(sfdcServiceJob.getRefId());
						QuoteToLe quoteLe = quoteToLe.get(0);
						if("TERMINATION".equalsIgnoreCase(quoteLe.getQuoteType())) {
							siteSolutionOpportunityBean.setOpportunityId(getChildSfdcId(sfdcServiceJob.getRefId(), quoteRepository,sfdcServiceJob.getServiceRefId()));
							
						} else {
						siteSolutionOpportunityBean
								.setOpportunityId(getSfdcId(sfdcServiceJob.getRefId(), quoteRepository,sfdcServiceJob.getServiceRefId()));
						}
						List<ProductSolution> prodSol = productSolutionRepositor
								.findBySolutionCode(siteSolutionOpportunityBean.getProductSolutionCode());
						prodSol.forEach(productSolution2 -> siteSolutionOpportunityBean
								.setProductServiceId(productSolution2.getTpsSfdcProductId()));
					}
				}
				sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(siteSolutionOpportunityBean));
			}
				break;
			case SfdcServiceTypeConstants.CREATE_FEASIBILITY: {

				FeasibilityRequestBean requestBean = (FeasibilityRequestBean) Utils
						.convertJsonToObject(sfdcServiceJob.getRequestPayload(), FeasibilityRequestBean.class);
				requestBean.setSfdcServiceJobId(sfdcServiceJob.getId());
				if (StringUtils.isEmpty(requestBean.getProductsServices())) {
					ThirdPartyServiceJobsRepository thirdServiceJobsRepository = appCtx
							.getBean(ThirdPartyServiceJobsRepository.class);
					Optional<ThirdPartyServiceJob> productServiceJob = thirdServiceJobsRepository
							.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
									SfdcServiceStatus.SUCCESS.toString(), sfdcServiceJob.getRefId(),
									SfdcServiceTypeConstants.CREATE_PRODUCT, "SFDC")
							.stream().findFirst();
					if (productServiceJob.isPresent()) {
						ProductServicesResponseBean productResponse = (ProductServicesResponseBean) Utils
								.convertJsonToObject(productServiceJob.get().getResponsePayload(),
										ProductServicesResponseBean.class);
						requestBean.setProductsServices(productResponse.getProductId());
					}
				}
				sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(requestBean));
			}
				break;
			case SfdcServiceTypeConstants.CREATE_OPEN_BCR: {

				BCROmsRequest bcrOmsRequest = (BCROmsRequest) Utils
						.convertJsonToObject(sfdcServiceJob.getRequestPayload(), BCROmsRequest.class);
				bcrOmsRequest.setTpdId(sfdcServiceJob.getId());

				if (StringUtils.isEmpty(bcrOmsRequest.getBcrOmsRecords().get(0).getOpportunityId())) {
					ThirdPartyServiceJobsRepository thirdServiceJobsRepository = appCtx
							.getBean(ThirdPartyServiceJobsRepository.class);
					Optional<ThirdPartyServiceJob> oppurtunityServiceJob = thirdServiceJobsRepository
							.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
									SfdcServiceStatus.SUCCESS.toString(), sfdcServiceJob.getRefId(),
									SfdcServiceTypeConstants.CREATE_OPPORTUNITY, "SFDC")
							.stream().findFirst();
					if (oppurtunityServiceJob.isPresent()) {
						OpportunityResponseBean optyResponse = (OpportunityResponseBean) Utils.convertJsonToObject(
								oppurtunityServiceJob.get().getResponsePayload(), OpportunityResponseBean.class);
						bcrOmsRequest.getBcrOmsRecords().get(0).setOpportunityId(optyResponse.getCustomOptyId());
					}
				}
				sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(bcrOmsRequest));
			}
				break;
			case SfdcServiceTypeConstants.CREATE_CLOSED_BCR: {
				BCROmsRequest bcrOmsRequest = (BCROmsRequest) Utils
						.convertJsonToObject(sfdcServiceJob.getRequestPayload(), BCROmsRequest.class);
				bcrOmsRequest.setTpdId(sfdcServiceJob.getId());
				sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(bcrOmsRequest));
			}
				break;
			case SfdcServiceTypeConstants.CREATE_INPROGRESS_BCR: {
				BCROmsRequest bcrOmsRequest = (BCROmsRequest) Utils
						.convertJsonToObject(sfdcServiceJob.getRequestPayload(), BCROmsRequest.class);
				bcrOmsRequest.setTpdId(sfdcServiceJob.getId());
				if (StringUtils.isEmpty(bcrOmsRequest.getBcrOmsRecords().get(0).getBcrOptyProperitiesBeans().getId())) {
					BCROmsResponse response = null;
					String bcrResponse = "";
					String Id = "";
					ThirdPartyServiceJobsRepository thirdServiceJobsRepository = appCtx
							.getBean(ThirdPartyServiceJobsRepository.class);
					List<ThirdPartyServiceJob> getPrevOpenBcr = thirdServiceJobsRepository
							.findByRefIdAndServiceTypeAndThirdPartySourceOrderByIdDesc(sfdcServiceJob.getRefId(),
									CommonConstants.CREATE_OPEN_BCR, ThirdPartySource.SFDC.toString());
					bcrResponse = getPrevOpenBcr.get(0).getResponsePayload();
					if (!StringUtils.isEmpty(bcrResponse)) {
						response = (BCROmsResponse) Utils
								.convertJsonToObject(getPrevOpenBcr.get(0).getResponsePayload(), BCROmsResponse.class);
						if (response != null) {
							if (response.getCustomBCRId().get(0) != null
									&& !StringUtils.isEmpty(response.getCustomBCRId().get(0))) {
								Id = response.getCustomBCRId().get(0);
								bcrOmsRequest.getBcrOmsRecords().get(0).getBcrOptyProperitiesBeans().setId(Id);
							}
						}
					}

				}
				sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(bcrOmsRequest));
			}
				break;
			case SfdcServiceTypeConstants.CREATE_UPDATE_BCR: {
				BCROmsRequest bcrOmsRequest = (BCROmsRequest) Utils
						.convertJsonToObject(sfdcServiceJob.getRequestPayload(), BCROmsRequest.class);
				bcrOmsRequest.setTpdId(sfdcServiceJob.getId());
				String serviceType = CommonConstants.CREATE_INPROGRESS_BCR;
				ThirdPartyServiceJobsRepository thirdServiceJobsRepository = appCtx
						.getBean(ThirdPartyServiceJobsRepository.class);
				List<ThirdPartyServiceJob> getPrevOpenBcr = null;

				if (StringUtils.isEmpty(bcrOmsRequest.getBcrOmsRecords().get(0).getBcrOptyProperitiesBeans().getId())) {
					if (bcrOmsRequest.getBcrOmsRecords().get(0).getBcrOptyProperitiesBeans().getOpen()) {
						serviceType = CommonConstants.CREATE_OPEN_BCR;
						getPrevOpenBcr = thirdServiceJobsRepository
								.findByRefIdAndServiceTypeAndThirdPartySourceOrderByIdAsc(sfdcServiceJob.getRefId(),
										serviceType, ThirdPartySource.SFDC.toString());
					} else {
						getPrevOpenBcr = thirdServiceJobsRepository
								.findByRefIdAndServiceTypeAndThirdPartySourceOrderByIdDesc(sfdcServiceJob.getRefId(),
										serviceType, ThirdPartySource.SFDC.toString());
						serviceType = CommonConstants.CREATE_INPROGRESS_BCR;
					}
					BCROmsResponse response = null;
					String bcrResponse = "";
					String Id = "";
					bcrResponse = getPrevOpenBcr.get(0).getResponsePayload();
					if (!StringUtils.isEmpty(bcrResponse) && getPrevOpenBcr != null) {
						response = (BCROmsResponse) Utils
								.convertJsonToObject(getPrevOpenBcr.get(0).getResponsePayload(), BCROmsResponse.class);
						if (response != null) {
							if (response.getCustomBCRId().get(0) != null
									&& !StringUtils.isEmpty(response.getCustomBCRId().get(0))) {
								Id = response.getCustomBCRId().get(0);
								bcrOmsRequest.getBcrOmsRecords().get(0).getBcrOptyProperitiesBeans().setId(Id);
							}
						}
					}

				}
				sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(bcrOmsRequest));
			}
				break;
			case SfdcServiceTypeConstants.CREATE_COPF_ID: {
				COPFOmsRequest copfOmsRequest = (COPFOmsRequest) Utils
						.convertJsonToObject(sfdcServiceJob.getRequestPayload(), COPFOmsRequest.class);
				
				copfOmsRequest.setTpsId(sfdcServiceJob.getId());
				
				QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository = appCtx
						.getBean(QuoteIllSiteToServiceRepository.class);
				QuoteToLeRepository quoteToLeRepository = appCtx
						.getBean(QuoteToLeRepository.class);

				List<QuoteToLe> quoteLes = quoteToLeRepository.findByQuote_QuoteCode(sfdcServiceJob.getRefId());
				for (QuoteToLe quoteToLe : quoteLes) {
					if("TERMINATION".equalsIgnoreCase(quoteToLe.getQuoteType())) {
						if(CommonConstants.BACTIVE.equals(quoteToLe.getIsMultiCircuit())) {
							LOGGER.info("create copf id request for multicircuit");
					copfOmsRequest
					.setOpportunityId(getSfdcId(sfdcServiceJob.getRefId(), quoteRepository,sfdcServiceJob.getServiceRefId()));
					List<QuoteIllSiteToService> quoteIllSiteToServices= quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(sfdcServiceJob.getServiceRefId(), quoteToLe);
					for (QuoteIllSiteToService quoteIllSiteToService : quoteIllSiteToServices) {
						QuoteSiteServiceTerminationDetailsRepository quoteSiteServiceTermdDetailsRepository = appCtx
								.getBean(QuoteSiteServiceTerminationDetailsRepository.class);
						QuoteSiteServiceTerminationDetails termDetails = quoteSiteServiceTermdDetailsRepository.findByQuoteIllSiteToService(quoteIllSiteToService);
						copfOmsRequest
						.setProductServiceId(quoteIllSiteToService.getTpsSfdcProductName());
						copfOmsRequest.getLinkCOPFDetails().stream().forEach(linkDetail -> {
							linkDetail.setEffectiveDateOfTermination(DateUtil.convertDateToString(termDetails.getEffectiveDateOfChange()));
						});
					}
				} else {
					LOGGER.info("create copf id request for single circuit");
					copfOmsRequest
					.setOpportunityId(getChildSfdcId(sfdcServiceJob.getRefId(), quoteRepository,sfdcServiceJob.getServiceRefId()));
					List<QuoteIllSiteToService> quoteIllSiteToServices= quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(sfdcServiceJob.getServiceRefId(), quoteToLe);
					for (QuoteIllSiteToService quoteIllSiteToService : quoteIllSiteToServices) {
						QuoteSiteServiceTerminationDetailsRepository quoteSiteServiceTermdDetailsRepository = appCtx
								.getBean(QuoteSiteServiceTerminationDetailsRepository.class);
						QuoteSiteServiceTerminationDetails termDetails = quoteSiteServiceTermdDetailsRepository.findByQuoteIllSiteToService(quoteIllSiteToService);
						copfOmsRequest.getLinkCOPFDetails().stream().forEach(linkDetail ->{
							linkDetail.setEffectiveDateOfTermination(DateUtil.convertDateToString(termDetails.getEffectiveDateOfChange()));
						});
					}
				
				}
						}
				}
				sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(copfOmsRequest));
			}
				break;
			case SfdcServiceTypeConstants.UPDATE_FEASIBILITY: {
				FeasibilityRequestBean requestBean = (FeasibilityRequestBean) Utils
						.convertJsonToObject(sfdcServiceJob.getRequestPayload(), FeasibilityRequestBean.class);
				requestBean.setSfdcServiceJobId(sfdcServiceJob.getId());
				ThirdPartyServiceJobsRepository thirdServiceJobsRepository = appCtx
						.getBean(ThirdPartyServiceJobsRepository.class);
				List<ThirdPartyServiceJob> createFeasJobList = thirdServiceJobsRepository
						.findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
								SfdcServiceStatus.SUCCESS.toString(), sfdcServiceJob.getRefId(),
								SfdcServiceTypeConstants.CREATE_FEASIBILITY, "SFDC");
				String id[] = new String[1];

				if (createFeasJobList.isEmpty()) {
					LOGGER.info("No successful feasibility request created for siteId : {}" , requestBean.getSiteId());
				} else {
					createFeasJobList.stream().forEach(createFeasJob -> {
						try {
							FeasibilityResponseBean feasResponse = (FeasibilityResponseBean) Utils.convertJsonToObject(
									createFeasJob.getResponsePayload(), FeasibilityResponseBean.class);
							if (requestBean.getSiteId().equals(feasResponse.getSiteId())) {
								id[0] = feasResponse.getfReqResponseList().get(0).getId();
								LOGGER.info("Create feasibility request Id for site Id: {} is : {}" ,requestBean.getSiteId(), id[0]);
								return;
							}
						} catch (TclCommonException e) {
							throw new TclCommonRuntimeException(
									ExceptionConstants.ERROR_FETCHING_SFDCFESIBILITY_RESPONSE, e);
						}
					});
				}

				requestBean.setId(id[0]);
				sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(requestBean));
			}
				break;
				case SfdcServiceTypeConstants.CREATE_WAIVER: {
					LOGGER.info("inside create waiver condition for batch");
					TerminationWaiverBean waiverOmsRequest = (TerminationWaiverBean) Utils
							.convertJsonToObject(sfdcServiceJob.getRequestPayload(), TerminationWaiverBean.class);

					waiverOmsRequest.setTpsId(sfdcServiceJob.getId());

					QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository = appCtx
							.getBean(QuoteIllSiteToServiceRepository.class);
					QuoteToLeRepository quoteToLeRepository = appCtx
							.getBean(QuoteToLeRepository.class);

					List<QuoteToLe> quoteLes = quoteToLeRepository.findByQuote_QuoteCode(sfdcServiceJob.getRefId());
					for (QuoteToLe quoteToLe : quoteLes) {
						if("TERMINATION".equalsIgnoreCase(quoteToLe.getQuoteType())) {
							if(CommonConstants.BACTIVE.equals(quoteToLe.getIsMultiCircuit())) {
								LOGGER.info("create waiver request for multicircuit");
								waiverOmsRequest.setOpportunityId(getSfdcId(sfdcServiceJob.getRefId(), quoteRepository,sfdcServiceJob.getServiceRefId()));
							} else {
								LOGGER.info("create waiver request for single circuit");
								waiverOmsRequest.getEtcRecord().setOpportunityName(getChildSfdcId(sfdcServiceJob.getRefId(), quoteRepository,sfdcServiceJob.getServiceRefId()));
							}
						}
					}
					sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(waiverOmsRequest));
				}
				break;
				case SfdcServiceTypeConstants.UPDATE_WAIVER: {
					LOGGER.info("inside update waiver condition for batch");
					TerminationWaiverBean waiverOmsUpdateRequest = (TerminationWaiverBean) Utils
							.convertJsonToObject(sfdcServiceJob.getRequestPayload(), TerminationWaiverBean.class);

					waiverOmsUpdateRequest.setTpsId(sfdcServiceJob.getId());

					QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository = appCtx
							.getBean(QuoteIllSiteToServiceRepository.class);
					QuoteToLeRepository quoteToLeRepository = appCtx
							.getBean(QuoteToLeRepository.class);

					List<QuoteToLe> quoteLes = quoteToLeRepository.findByQuote_QuoteCode(sfdcServiceJob.getRefId());
					for (QuoteToLe quoteToLe : quoteLes) {
						if("TERMINATION".equalsIgnoreCase(quoteToLe.getQuoteType())) {
								LOGGER.info("update waiver request for multicircuit - updating waiver id in request");
								List<String> waiverId = quoteRepository
										.findWaiverIdByQuoteToLeAndServiceId(quoteToLe.getId(),sfdcServiceJob.getServiceRefId());
								if(waiverId != null && !waiverId.isEmpty()){
									waiverOmsUpdateRequest.getEtcRecord().setId(waiverId.get(0));
								}			
						}
					}
					sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(waiverOmsUpdateRequest));
				}
				break;
			}
		} catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.ERROR_PROCESSING_SFDC_REQUEST,e);
		}
	}

	/**
	 * Method to get opty id from create opty..
	 * @param thirdPartyServiceJobsRepository
	 * @param quoteToLeId
	 * @return
	 */
	private String getCreateOptyId(ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository,
			Integer quoteToLeId,String refId) {
		List<ThirdPartyServiceJob> createOptyJobs = thirdPartyServiceJobsRepository
				.findByRefIdAndServiceTypeAndThirdPartySource(refId,
						CREATE_OPPORTUNITY, SFDC);
		for (ThirdPartyServiceJob createOptyJob : createOptyJobs) {
			try {
				LOGGER.info("Create opty job id :: {}",createOptyJob.getId());
				OpportunityBean opportunityBean = Utils.convertJsonToObject(createOptyJob.getRequestPayload(),
						OpportunityBean.class);
				if (Objects.nonNull(opportunityBean) && Objects.nonNull(opportunityBean.getQuoteToLeId())
						&& opportunityBean.getQuoteToLeId().equals(quoteToLeId)) {
					return createOptyJob.getTpsId();
				}
			} catch (TclCommonException e) {
				throw new TclCommonRuntimeException(
						ExceptionConstants.COMMON_ERROR, e);
			}
		}
		return null;
	}

	/**
	 * Method to fetch tpsSfdcProductId.
	 * @param quoteToLe
	 * @param thirdPartyServiceJobsRepository
	 * @param deleteProductServiceBean
	 * @param productIds
	 */
	private void getTpsSfdcProductId(QuoteToLe quoteToLe,ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository,
									 ProductServiceBean deleteProductServiceBean,List<String> productIds){
		List<ThirdPartyServiceJob> createProductJobs = thirdPartyServiceJobsRepository.
				findByRefIdAndServiceTypeAndThirdPartySource(quoteToLe.getQuote().getQuoteCode(),CREATE_PRODUCT,
						SFDC);
		createProductJobs.forEach(thirdPartyServiceJob -> {
			try {
				LOGGER.info("CreateProductJob ID :: {}",thirdPartyServiceJob.getId());
				ProductServiceBean productServiceBean = (ProductServiceBean) Utils
						.convertJsonToObject(thirdPartyServiceJob.getRequestPayload(), ProductServiceBean.class);
				LOGGER.info("CreateProductServicebean solutioncode :: {}, DeleteProductServicebean solutionCode :: {}",
						productServiceBean.getProductSolutionCode(),deleteProductServiceBean.getProductSolutionCode());
				if(productServiceBean.getProductSolutionCode().equals(deleteProductServiceBean.getProductSolutionCode())){
					ProductServicesResponseBean responseBean = (ProductServicesResponseBean) Utils
							.convertJsonToObject(thirdPartyServiceJob.getResponsePayload(),ProductServicesResponseBean.class);
					if(!responseBean.getProductsservices().isEmpty()){
						responseBean.getProductsservices().forEach(productsserviceResponse -> {
							productIds.add(productsserviceResponse.getId());
						});
					}
				}
			} catch (TclCommonException e) {
				throw new TclCommonRuntimeException(
						ExceptionConstants.COMMON_ERROR, e);
			}
		});
	}

	/**
	 * getSfdcId
	 * 
	 * @return
	 */
	private String getSfdcId(String refId, QuoteRepository quoteRepository,String serviceId) {
		List<Map<String, Object>> sfdcIds = quoteRepository.findTpsSfdcOptyIdAndQuoteTypeByQuoteCode(refId);
		for (Map<String, Object> sfdc : sfdcIds) {
			LOGGER.info("inside id condition getSfdcId");
			if (sfdc.get("quoteType") == null || !((String) sfdc.get("quoteType")).equalsIgnoreCase("termination")) {
				return sfdc.get("optyId") != null ? (String) sfdc.get("optyId") : null;
			} else {
				LOGGER.info("inside else condition getSfdcId");
				List<String> optyIds = quoteRepository
						.findTpsSfdcOptyIdByQuoteToLeAndServiceId((Integer) sfdc.get("id"), serviceId);
				LOGGER.info("termination opty idss {}", optyIds.toString());
				for (String optyId : optyIds) {
					return optyId;
				}
			}
		}
		return null;
	}
	
	private String getChildSfdcId(String refId, QuoteRepository quoteRepository,String serviceId) {
		List<Map<String, Object>> sfdcIds = quoteRepository.findTpsSfdcOptyIdAndQuoteTypeByQuoteCode(refId);
		for (Map<String, Object> sfdc : sfdcIds) {
			LOGGER.info("inside id condition getSfdcId");
				return sfdc.get("optyId") != null ? (String) sfdc.get("optyId") : null;
			
		}
		return null;
	}
	
	private String getOptyId(String refId, ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository) {
		List<ThirdPartyServiceJob> createOpty = thirdPartyServiceJobsRepository
				.findByRefIdAndServiceTypeAndThirdPartySource(refId, "CREATE_OPPORTUNITY", "SFDC");
		for (ThirdPartyServiceJob thirdPartyServiceJob : createOpty) {
			return thirdPartyServiceJob.getTpsId();
		}
		return null;
	}
	
	private Map<String, String> getOptyProdNameAndId(String refId,
			ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository,String prodSolutionId) {
		List<ThirdPartyServiceJob> createProduct = thirdPartyServiceJobsRepository
				.findByRefIdAndServiceTypeAndThirdPartySource(refId, "CREATE_PRODUCT", "SFDC");
		for (ThirdPartyServiceJob thirdPartyServiceJob : createProduct) {
			try {
				String response = thirdPartyServiceJob.getResponsePayload();
				ProductServicesResponseBean productServicesResponseBean = Utils.convertJsonToObject(response,
						ProductServicesResponseBean.class);
				Map<String, String> mapper = new HashMap<>();
				String prodServiceId = null;
				String productServiceName = productServicesResponseBean.getProductId();
				for (ProductsserviceResponse prodService : productServicesResponseBean.getProductsservices()) {
					prodServiceId = prodService.getId();
				}
				if (prodSolutionId.equals(productServicesResponseBean.getProductSolutionCode())) {
					mapper.put("prodName", productServiceName);
					mapper.put("prodId", prodServiceId);
					return mapper;
				}
			} catch (Exception e) {
				LOGGER.error("Error in parsing ", e);
			}
		}
		return null;
	}
	
	private String getProductNameBasedOnProductCategory(String productCategory) {
		String productName = null;
		switch(productCategory){
		case IzosdwanCommonConstants.IAS:
			productName = "IAS";
			break;
		case IzosdwanCommonConstants.GVPN:
			productName = "GVPN";
			break;
		case IzosdwanCommonConstants.MANAGED_SERVICES:
			productName = IzosdwanCommonConstants.VPROXY_SFDC;
			break;
		case IzosdwanCommonConstants.IZOSDWAN_NAME:
			productName = IzosdwanCommonConstants.SDWAN;
			break;
		default:
			break;
		}
		
		return productName;
	}
}
