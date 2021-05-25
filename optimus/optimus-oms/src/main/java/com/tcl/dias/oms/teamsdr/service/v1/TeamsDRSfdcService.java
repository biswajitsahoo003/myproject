package com.tcl.dias.oms.teamsdr.service.v1;

import static com.tcl.dias.common.sfdc.constants.SfdcServiceTypeConstants.UPDATE_OPPORTUNITY;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.CLOSED_DROPPED;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.CREATE_OPTY;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.DELETE_PRODUCT;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.GSIP;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.INPROGRESS;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.SFDC;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.UPDATE_PRODUCT;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.MICROSOFT_CLOUD_SOLUTIONS;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.CustomerLeDetailsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.sfdc.bean.OpportunityBean;
import com.tcl.dias.common.sfdc.bean.ProductServiceBean;
import com.tcl.dias.common.sfdc.bean.UpdateOpportunityStage;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteTeamsDRRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * Service class for TeamsDR SFDC
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */

@Service
public class TeamsDRSfdcService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TeamsDRSfdcService.class);

	@Autowired
	OmsSfdcService omsSfdcService;

	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	TeamsDRQuoteService teamsDRQuoteService;

	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;

	@Autowired
	QuoteTeamsDRRepository quoteTeamsDRRepository;

	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	/**
	 * create Opportunity in sfdc
	 *
	 * @param quoteToLe
	 * @param engagementOptyId
	 * @param productFamily
	 */
	public void createOpportunityInSfdc(QuoteToLe quoteToLe, String engagementOptyId, String productFamily) {
		try {
			// Triggering Sfdc Opportunity Creation
			if (Objects.nonNull(quoteToLe) && StringUtils.isEmpty(engagementOptyId)) {
				omsSfdcService.processCreateOpty(quoteToLe, productFamily);
			}
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.SFDC_VALIDATION_ERROR, e,
					ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Process update product for teamsdr.
	 *
	 * @param quoteToLe
	 */
	public void updateProductServiceInSfdc(QuoteToLe quoteToLe) {
		try {
			if (Objects.nonNull(quoteToLe)) {
				omsSfdcService.processUpdateProductForTeamsDR(quoteToLe, null);
			}
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.SFDC_VALIDATION_ERROR,
					ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Process update product for teamsdr.
	 *
	 * @param quoteToLe
	 */
	public void updateProductServiceInSfdc(QuoteToLe quoteToLe, ProductSolution productSolution) {
		try {
			if (Objects.nonNull(quoteToLe)) {
				omsSfdcService.processUpdateProductForTeamsDR(quoteToLe, productSolution);
			}
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.SFDC_VALIDATION_ERROR,
					ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Method to drop opportunity in sfdc
	 *
	 * @param quoteToLe
	 */
	public void deleteOpportunity(QuoteToLe quoteToLe) {
		try {
			if (Objects.nonNull(quoteToLe)) {
				List<ThirdPartyServiceJob> createOptyJobs = thirdPartyServiceJobsRepository
						.findByRefIdAndServiceTypeAndThirdPartySource(quoteToLe.getQuote().getQuoteCode(), CREATE_OPTY,
								SFDC);

				if (createOptyJobs.stream().anyMatch(thirdPartyServiceJob -> {
					try {
						OpportunityBean opportunityBean = Utils
								.convertJsonToObject(thirdPartyServiceJob.getRequestPayload(), OpportunityBean.class);
						if (Objects.nonNull(opportunityBean) && Objects.nonNull(opportunityBean.getQuoteToLeId())
								&& opportunityBean.getQuoteToLeId().equals(quoteToLe.getId())) {
							return true;
						}
					} catch (TclCommonException e) {
						e.printStackTrace();
					}
					return false;
				})) {
					LOGGER.info("Opty exists , So triggering delete opty...");
					omsSfdcService.processUpdateOpportunity(new Date(), quoteToLe.getTpsSfdcOptyId(),
							SFDCConstants.CLOSED_DROPPED, quoteToLe);
				}
//				else{
//					quoteToLeProductFamilyRepository.findByQuoteToLe(quoteToLe.getId())
//							.stream()
//							.map(quoteToLeProductFamily->
//									productSolutionRepository.findByQuoteToLeProductFamily(quoteToLeProductFamily))
//							.flatMap(Collection::stream)
//							.forEach(productSolution -> {
//								if(Objects.nonNull(productSolution.getTpsSfdcProductName())){
//									deleteProductServiceInSfdc(quoteToLe,productSolution);
//								}
//							});
//				}
			}
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.SFDC_VALIDATION_ERROR,
					ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Method to delete product service in sfdc.
	 *
	 * @param quoteToLe
	 * @param productSolution
	 */
	public void deleteProductServiceInSfdc(QuoteToLe quoteToLe, ProductSolution productSolution) {
		try {
			if (Objects.nonNull(quoteToLe) && Objects.nonNull(productSolution)) {
				omsSfdcService.processDeleteProduct(quoteToLe, productSolution);
			}
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.SFDC_VALIDATION_ERROR,
					ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Method to create product service in sfdc.
	 *
	 * @param quoteToLe
	 * @param productSolution
	 */
	public void createProductServiceInSfdc(QuoteToLe quoteToLe, ProductSolution productSolution) {
		try {
			if (Objects.nonNull(quoteToLe) && Objects.nonNull(productSolution)) {
				productSolution.setTpsSfdcProductName("");
				productSolutionRepository.save(productSolution);
				omsSfdcService.processProductServiceForSolution(quoteToLe, productSolution,
						quoteToLe.getTpsSfdcOptyId());
			}
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.SFDC_VALIDATION_ERROR,
					ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Method to update opportunity
	 *
	 * @param quoteToLe
	 */
	public void updateOpportunityInSfdc(QuoteToLe quoteToLe, String stage) {
		try {
			if (Objects.nonNull(quoteToLe)) {
				omsSfdcService.processUpdateOpportunity(new Date(), quoteToLe.getTpsSfdcOptyId(), stage, quoteToLe);
			}
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.SFDC_VALIDATION_ERROR,
					ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Method to create product services in sfdc.
	 *
	 * @param quoteToLe
	 * @param sfdcOpportunityId
	 */
	public void createProductServicesInSfdc(QuoteToLe quoteToLe, String sfdcOpportunityId) {
		try {
			if (Objects.nonNull(quoteToLe)) {
				omsSfdcService.processProductServicesForTeamsDR(quoteToLe, sfdcOpportunityId);
			}
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.SFDC_VALIDATION_ERROR,
					ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * To create or update product services for parent.
	 * 
	 * @param parentQuoteToLe
	 */
	private void createOrUpdateProductServicesForParent(QuoteToLe parentQuoteToLe,
			List<ThirdPartyServiceJob> createOptyJobs, List<QuoteToLe> quoteToLes) {
		quoteTeamsDRRepository.findByQuoteToLeIdAndServiceName(parentQuoteToLe.getId(), null)
				.ifPresent(quoteTeamsDR -> {
					ProductSolution planProductSolution = quoteTeamsDR.getProductSolution();
					if (Objects.nonNull(planProductSolution.getTpsSfdcProductName())) {
						// update product services for parent qtle.
						LOGGER.info("Updating prod services for parentqtle");
						updateProductServiceInSfdc(parentQuoteToLe);
						if (teamsDRQuoteService.containsGsc(parentQuoteToLe)) {
							Optional<List<ProductSolution>> productSolutions = quoteToLeProductFamilyRepository
									.findByQuoteToLe(parentQuoteToLe.getId()).stream()
									.filter(quoteToLeProductFamily -> GSIP
											.equals(quoteToLeProductFamily.getMstProductFamily().getName()))
									.map(quoteToLeProductFamily -> productSolutionRepository
											.findByQuoteToLeProductFamily(quoteToLeProductFamily))
									.findAny();

							if (productSolutions.isPresent()) {
								LOGGER.info("Product Solutions present for :: {}", parentQuoteToLe.getId());
								if (!productSolutions.get().stream().anyMatch(productSolution -> {
									if (Objects.nonNull(productSolution.getTpsSfdcProductName())) {
										LOGGER.info("Product Solution with tpsSfdcProductname non null is :: {}",
												productSolution.getId());
										return true;
									}
									return false;
								})) {
									ProductSolution solution = productSolutions.get().stream().findAny().get();
									solution.setTpsSfdcProductName("");
									productSolutionRepository.save(solution);
									createProductServiceInSfdc(parentQuoteToLe, solution);
								}
							}
						}

						if (createOptyJobs.size() == 1) {
							deleteProdServices(parentQuoteToLe, quoteToLes);
						}

					}
				});
	}

	/**
	 * Method to delete prod services from child and parent..
	 * 
	 * @param parentQuoteToLe
	 * @param quoteToLes
	 */
	private void deleteProdServices(QuoteToLe parentQuoteToLe, List<QuoteToLe> quoteToLes) {
//		// Deleting gsc prod services from parent qtle
//		quoteToLeProductFamilyRepository.findByQuoteToLe(parentQuoteToLe.getId())
//				.stream()
//				.filter(quoteToLeProductFamily -> GSIP.equals(quoteToLeProductFamily.getMstProductFamily().getName()))
//				.map(quoteToLeProductFamily -> productSolutionRepository.findByQuoteToLeProductFamily(quoteToLeProductFamily))
//				.flatMap(Collection::stream)
//				.forEach(productSolution -> {
//					if (Objects.nonNull(productSolution.getTpsSfdcProductName())
//							&& !StringUtils.isEmpty(productSolution.getTpsSfdcProductName())) {
//						deleteProductServiceInSfdc(parentQuoteToLe, productSolution);
//					}
//				});

		// Deleting prod services from child qtle...
		AtomicReference<QuoteToLe> currentQtle = new AtomicReference<>();
		quoteToLes.stream().filter(quoteToLe -> !quoteToLe.getId().equals(parentQuoteToLe.getId())).map(quoteToLe -> {
			currentQtle.set(quoteToLe);
			return quoteToLeProductFamilyRepository.findByQuoteToLe(quoteToLe.getId());
		}).flatMap(Collection::stream)
				.map(quoteToLeProductFamily -> productSolutionRepository
						.findByQuoteToLeProductFamily(quoteToLeProductFamily))
				.flatMap(Collection::stream).forEach(productSolution -> {
					if (Objects.nonNull(productSolution.getTpsSfdcProductName())) {
						LOGGER.info("Child qtle for delete :: {}", currentQtle.get().getId());
						deleteProductServiceInSfdc(parentQuoteToLe, productSolution);
					}
				});
	}

	/**
	 * Method To trigger sfdc
	 *
	 * @param quoteToLes
	 */
	public void triggerSFDC(List<QuoteToLe> quoteToLes) {
		if (Objects.nonNull(quoteToLes) && !quoteToLes.isEmpty()) {
			QuoteToLe parentQuoteToLe = teamsDRQuoteService.findParentQuoteToLe(quoteToLes);
			List<ThirdPartyServiceJob> createOptyJobs = thirdPartyServiceJobsRepository
					.findByRefIdAndServiceTypeAndThirdPartySource(parentQuoteToLe.getQuote().getQuoteCode(),
							CREATE_OPTY, SFDC);

			createOrUpdateProductServicesForParent(parentQuoteToLe, createOptyJobs, quoteToLes);

			quoteToLes.stream().filter(quoteToLe -> !quoteToLe.getId().equals(parentQuoteToLe.getId()))
					.forEach(quoteToLe -> {
						if (!createOptyJobs.stream().anyMatch(optyJob -> {
							try {
								OpportunityBean opportunityBean = Utils.convertJsonToObject(optyJob.getRequestPayload(),
										OpportunityBean.class);
								if (Objects.nonNull(opportunityBean)
										&& Objects.nonNull(opportunityBean.getQuoteToLeId())
										&& opportunityBean.getQuoteToLeId().equals(quoteToLe.getId())) {
									return true;
								}
							} catch (TclCommonException e) {
								e.printStackTrace();
							}
							return false;
						})) {
							// Creating new opportunity if no opty is already created in thirdpartyservice
							// job
							LOGGER.info("Creating new opty for qtle :: {}", quoteToLe.getId());
							createOpportunityInSfdc(quoteToLe, null, MICROSOFT_CLOUD_SOLUTIONS);
							if (quoteToLe.getQuoteToLeProductFamilies().stream()
									.anyMatch(quoteToLeProductFamily -> MICROSOFT_CLOUD_SOLUTIONS
											.equals(quoteToLeProductFamily.getMstProductFamily().getName()))) {
								// to trigger credit check only if it contains teams solution.
								LOGGER.info("Triggering credit check for qtle :: {}", quoteToLe.getId());
								CustomerLeDetailsBean customerLeDetailsBean;
								try {
									customerLeDetailsBean = teamsDRQuoteService.fetchCustomerLeAttributes(
											quoteToLe.getErfCusCustomerLegalEntityId(), MICROSOFT_CLOUD_SOLUTIONS);
									teamsDRQuoteService.triggerCreditCheck(quoteToLe.getErfCusCustomerLegalEntityId(),
											quoteToLe, customerLeDetailsBean, null);
								} catch (TclCommonException e) {
									e.printStackTrace();
								}
							}
							// to trigger sfdc update..
							updateOpportunityInSfdc(quoteToLe, SFDCConstants.VERBAL_AGREEMENT_STAGE);
						} else {
							LOGGER.info("Opty Already exists...");
							LOGGER.info("Updating prod services for qtle :: {}", quoteToLe.getId());
							updateProductServiceInSfdc(quoteToLe);
						}
					});
		}
	}

	/**
	 * Method to delete incomplete requests in thirdpartyservicejobs before deleting
	 * the quotetole..
	 * 
	 * @param quoteToLe
	 */
	public void deleteIncompleteRequests(QuoteToLe quoteToLe) {
		LOGGER.info("Deleting incomplete requests...");
		List<String> serviceTypes = Arrays.asList(UPDATE_OPPORTUNITY, UPDATE_PRODUCT, DELETE_PRODUCT);
		List<String> serviceStatus = Arrays.asList(CommonConstants.NEW, INPROGRESS);
		List<ThirdPartyServiceJob> jobs = thirdPartyServiceJobsRepository
				.findAllByRefIdAndServiceTypeInAndThirdPartySourceIn(quoteToLe.getQuote().getQuoteCode(), serviceTypes,
						Collections.singletonList(SFDC));
		LOGGER.info("No of jobs retrieved :: {}", jobs.size());
		jobs.stream().filter(thirdPartyServiceJob -> serviceStatus.contains(thirdPartyServiceJob.getServiceStatus()))
				.forEach(thirdPartyServiceJob -> {
					LOGGER.info("Job id :: {}, type :: {}, status :: {}", thirdPartyServiceJob.getId(),
							thirdPartyServiceJob.getServiceType(), thirdPartyServiceJob.getServiceStatus());
					Integer quoteToLeId = null;
					try {
						if (UPDATE_OPPORTUNITY.equals(thirdPartyServiceJob.getServiceType())) {
							UpdateOpportunityStage updateOpportunityStage = (UpdateOpportunityStage) Utils
									.convertJsonToObject(thirdPartyServiceJob.getRequestPayload(),
											UpdateOpportunityStage.class);
							if (!CLOSED_DROPPED.equals(updateOpportunityStage.getStageName())) {
								quoteToLeId = updateOpportunityStage.getQuoteToLeId();
							}
						} else if (UPDATE_PRODUCT.equals(thirdPartyServiceJob.getServiceType())
								|| DELETE_PRODUCT.equals(thirdPartyServiceJob.getServiceType())) {
							ProductServiceBean productServiceBean = (ProductServiceBean) Utils.convertJsonToObject(
									thirdPartyServiceJob.getRequestPayload(), ProductServiceBean.class);
							quoteToLeId = productServiceBean.getQuoteToLeId();
						}
						LOGGER.info("QuoteToLeId fetched from bean :: {}", quoteToLeId);
						if (Objects.nonNull(quoteToLeId) && quoteToLeId.equals(quoteToLe.getId())) {
							thirdPartyServiceJobsRepository.delete(thirdPartyServiceJob);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
	}
}
