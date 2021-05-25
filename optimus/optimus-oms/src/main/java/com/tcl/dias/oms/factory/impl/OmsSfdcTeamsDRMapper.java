package com.tcl.dias.oms.factory.impl;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.sfdc.bean.FeasibilityRequestBean;
import com.tcl.dias.common.sfdc.bean.OpportunityBean;
import com.tcl.dias.common.sfdc.bean.ProductServiceBean;
import com.tcl.dias.common.sfdc.bean.SiteSolutionOpportunityBean;
import com.tcl.dias.common.sfdc.bean.UpdateOpportunityStage;
import com.tcl.dias.common.teamsdr.beans.TeamsDRCumulativePricesBean;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteTeamsDR;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteTeamsDRRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.core.IOmsSfdcInputHandler;
import com.tcl.dias.oms.sfdc.core.OmsAbstractSfdcHandler;
import com.tcl.dias.oms.teamsdr.service.v1.TeamsDRQuoteService;
import com.tcl.dias.oms.teamsdr.util.TeamsDRConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SUPPLIER_CONTRACTING_ENTITY;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.GMMS;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.GSC;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.MEDIA_GATEWAY;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.MICROSOFT_CLOUD_SOLUTIONS;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.TEAMSDR;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.MICROSOFT_LICENSE;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.MONTH;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.MONTHS;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.YEAR;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRUtils.checkForNull;

/**
 * This file contains the SFDC operations related to TeamsDR.
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component
public class OmsSfdcTeamsDRMapper extends OmsAbstractSfdcHandler implements IOmsSfdcInputHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(OmsSfdcTeamsDRMapper.class);

	@Autowired
	QuoteGscRepository quoteGscRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	OmsSfdcGscMapper omsSfdcGscMapper;

	@Autowired
	SiteFeasibilityRepository siteFeasibilityRepository;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	QuoteTeamsDRRepository quoteTeamsDRRepository;

	@Autowired
	TeamsDRQuoteService teamsDRQuoteService;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	/**
	 * Method to find product type.
	 *
	 * @param quoteToLe
	 * @return
	 */
	private String findProductType(QuoteToLe quoteToLe){
		List<QuoteTeamsDR> quoteTeamsDRS = Optional.ofNullable(quoteTeamsDRRepository.findByQuoteToLeId(quoteToLe.getId()))
				.orElse(Collections.emptyList());
		if(!quoteTeamsDRS.isEmpty()){
			return TEAMSDR;
		}else{
			return GSC;
		}
	}

	/**
	 * Method to map to months
	 *
	 * @param quoteToLe
	 * @return
	 */
	private String findInMonths(QuoteToLe quoteToLe) {
		Integer months = 12;
		String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
		if (termsInMonth.contains(YEAR)) {
			months = Integer.valueOf(termsInMonth.replace(YEAR, CommonConstants.EMPTY).trim()) * 12;
		} else if (termsInMonth.contains(MONTHS)) {
			months = Integer.valueOf(termsInMonth.replace(MONTHS, CommonConstants.EMPTY).trim());
		} else if (termsInMonth.contains(MONTH)) {
			months = Integer.valueOf(termsInMonth.replace(MONTH, CommonConstants.EMPTY).trim());
		}
		return String.valueOf(months);
	}

	/**
	 * Method to add cummulative price.
	 *
	 * @param quoteToLe
	 * @param productServiceBean
	 */
	private void addCummulativePrice(QuoteToLe quoteToLe, ProductServiceBean productServiceBean) {
		TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();
		Optional.ofNullable(quoteTeamsDRRepository.findByQuoteToLeId(quoteToLe.getId())).orElse(Collections.emptyList())
				.stream().filter(qteTeamsDR -> Objects.isNull(qteTeamsDR.getServiceName()) ||
				TeamsDRConstants.MEDIA_GATEWAY.equals(qteTeamsDR.getServiceName()))
				.forEach(quoteTeamsDR1 -> {
					teamsDRCumulativePricesBean
							.setMrc(teamsDRCumulativePricesBean.getMrc() + checkForNull(quoteTeamsDR1.getMrc()));
					teamsDRCumulativePricesBean
							.setNrc(teamsDRCumulativePricesBean.getNrc() + checkForNull(quoteTeamsDR1.getNrc()));
				});
		productServiceBean.setProductMRC(teamsDRCumulativePricesBean.getMrc());
		productServiceBean.setProductNRC(teamsDRCumulativePricesBean.getNrc());
	}

	/**
	 * Method to set product type and price
	 *
	 * @param quoteToLe
	 * @param solution
	 * @param productServiceBean
	 */
	private void setProductTypeAndPrice(QuoteToLe quoteToLe, ProductSolution solution,
										ProductServiceBean productServiceBean) {
		Optional.ofNullable(quoteTeamsDRRepository.findByProductSolutionAndStatus(solution, CommonConstants.BACTIVE))
				.orElse(Collections.emptyList()).stream().findAny().ifPresent(quoteTeamsDR -> {
			if (Objects.isNull(quoteTeamsDR.getServiceName())) {
				// TeamsDR
				productServiceBean.setRecordTypeName(TEAMSDR);
				productServiceBean.setProductType(TEAMSDR);
				addCummulativePrice(quoteToLe, productServiceBean);
			} else if (MICROSOFT_LICENSE.equals(quoteTeamsDR.getServiceName())) {
				// Ms License
				productServiceBean.setRecordTypeName(TEAMSDR);
				productServiceBean.setProductType(MICROSOFT_LICENSE);
				productServiceBean.setProductNRC(quoteTeamsDR.getNrc());
				productServiceBean.setProductMRC(quoteTeamsDR.getMrc());
			}
		});
	}

	/**
	 * Method to add cummulative gsc prices.
	 *
	 * @param quoteToLe
	 * @param productServiceBean
	 */
	private void getGSCCummulativeServicesPrice(QuoteToLe quoteToLe, ProductServiceBean productServiceBean) {
		TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();
		Optional.ofNullable(quoteGscRepository.findByQuoteToLe(quoteToLe)).orElse(Collections.emptyList())
				.forEach(quoteGsc -> {
					teamsDRCumulativePricesBean
							.setMrc(teamsDRCumulativePricesBean.getMrc() + checkForNull(quoteGsc.getMrc()));
					teamsDRCumulativePricesBean
							.setNrc(teamsDRCumulativePricesBean.getNrc() + checkForNull(quoteGsc.getNrc()));
				});

		productServiceBean.setProductMRC(teamsDRCumulativePricesBean.getMrc());
		productServiceBean.setProductNRC(teamsDRCumulativePricesBean.getNrc());
	}

	/**
	 * Method to construct product service bean
	 *
	 * @param productServiceBean
	 * @param quoteToLe
	 * @return {@link ProductServiceBean}
	 */
	private ProductServiceBean constructProductServiceBean(ProductServiceBean productServiceBean, QuoteToLe quoteToLe,
														   ProductSolution solution) {

		setProductTypeAndPrice(quoteToLe, solution, productServiceBean);
		productServiceBean.setProductLineOfBusiness(GMMS);

		if (quoteToLe.getTermInMonths() != null) {
			productServiceBean.setTermInMonthsC(findInMonths(quoteToLe));
		}
		productServiceBean.setOpportunityNameC(MICROSOFT_CLOUD_SOLUTIONS);
		productServiceBean.setDataOrMobileC(SFDCConstants.DATA);
		productServiceBean.setpOCAttachedC(SFDCConstants.POC_ATTACHED);
		productServiceBean.setEnabledForUnifiedAccessC(SFDCConstants.NO);
		productServiceBean.setProductCategoryC(SFDCConstants.MANAGED_SERVICES);
		productServiceBean.setCallTypeC(SFDCConstants.ENTERPRISE);

		// When macd in place.
//		if(GscConstants.ORDER_TYPE_MACD.equalsIgnoreCase(quoteToLe.getQuoteType())) {
//			productServiceBean.setOrderType(SFDCConstants.CHANGE_ORDER);
//		}

		return productServiceBean;

	}

	/**
	 * To get required data for create opporutnity..
	 *
	 * @param quoteToLe
	 * @param opportunityBean
	 * @return
	 * @throws TclCommonException
	 */
	@Override
	public OpportunityBean getOpportunityBean(QuoteToLe quoteToLe, OpportunityBean opportunityBean)
			throws TclCommonException {

		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(quoteToLe.getQuote());
		QuoteToLe parentQuoteToLe = teamsDRQuoteService.findParentQuoteToLe(quoteToLes);

		opportunityBean.setSelectProductType(findProductType(quoteToLe));
		opportunityBean.setOpportunitySpecification(TEAMSDR);
		opportunityBean.setOrderCategory(SFDCConstants.CAT_3);
		if (!parentQuoteToLe.getId().equals(quoteToLe.getId())) {
			opportunityBean.setParentOpportunityId(Objects.nonNull(parentQuoteToLe.getTpsSfdcOptyId())
					? parentQuoteToLe.getTpsSfdcOptyId()
					: null);
			opportunityBean.setParentServiceName(TEAMSDR);
		}

		opportunityBean.setQuoteToLeId(quoteToLe.getId());

		if (quoteToLe.getTermInMonths() != null) {
			opportunityBean.setTermOfMonths(findInMonths(quoteToLe));
		}

		// To be enabled when macd in placed.
//		if(GscConstants.ORDER_TYPE_MACD.equalsIgnoreCase(quoteToLe.getQuoteType())) {
//			opportunityBean.setType(SFDCConstants.CHANGE_ORDER);
//			opportunityBean.setSubType(SFDCConstants.CHANGE_ORDER);
//			if(MACDOrderRequest.REQUEST_TYPE_NUMBER_REMOVE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
//				opportunityBean.setSubType(SFDCConstants.ADDITION_DELETION);
//			}
//		}

		LOGGER.info("Input  for create Opportunity TEAMSDR :: {}", opportunityBean);
		return opportunityBean;
	}

	@Override
	public FeasibilityRequestBean getFeasibilityBean(Integer id, FeasibilityRequestBean feasibilityBean)
			throws TclCommonException {
		return null;
	}

	@Override
	public ProductServiceBean getProductServiceInput(QuoteToLe quoteToLe, ProductServiceBean productServiceBean,
													 ProductSolution solution) throws TclCommonException {
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(quoteToLe.getQuote());
		// For gsc
		if (SFDCConstants.GSIP.equals(solution.getQuoteToLeProductFamily().getMstProductFamily().getName())) {
			omsSfdcGscMapper.getProductServiceInput(quoteToLe, productServiceBean, solution);
			getGSCCummulativeServicesPrice(quoteToLe, productServiceBean);
		} else {
			// for teamsdr
			constructProductServiceBean(productServiceBean, quoteToLe, solution);
		}
		productServiceBean.setParentQuoteToLeId(teamsDRQuoteService.findParentQuoteToLe(quoteToLes).getId());
		LOGGER.info("Input  for product Service TEAMSDR :: {}", productServiceBean);
		return productServiceBean;
	}

	@Override
	public ProductServiceBean updateProductServiceInput(QuoteToLe quoteToLe, ProductServiceBean productServiceBean,
														ProductSolution productSolution) throws TclCommonException {
		productServiceBean = new ProductServiceBean();
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(quoteToLe.getQuote());
		// For gsc
		if (SFDCConstants.GSIP.equals(productSolution.getQuoteToLeProductFamily().getMstProductFamily().getName())) {
			productServiceBean = omsSfdcGscMapper.updateProductServiceInput(quoteToLe, productSolution);
			getGSCCummulativeServicesPrice(quoteToLe, productServiceBean);
		} else {
			// for teamsdr
			Map<String, String> attributeMapper = getQuoteLeAttributes(quoteToLe);
			productServiceBean.setProductName(productSolution.getTpsSfdcProductName());
			productServiceBean.setProductId(productSolution.getTpsSfdcProductId());
			productServiceBean.setCurrencyIsoCode(
					quoteToLe.getCurrencyCode() != null ? quoteToLe.getCurrencyCode() : SFDCConstants.USD);
			String variant = CommonConstants.EMPTY;
			productServiceBean.setType(variant);
			productServiceBean.setIdcBandwidth(SFDCConstants.NO);
			productServiceBean.setBigMachinesArc(0D);
			productServiceBean.setBigMachinesMrc(0D);
			productServiceBean.setBigMachinesNrc(0D);
			productServiceBean.setBigMachinesTcv(0D);
			productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
			productServiceBean.setProductSolutionCode(productSolution.getSolutionCode());
			productServiceBean
					.setL2feasibilityCommercialManagerName(attributeMapper.get(LeAttributesConstants.PROGRAM_MANAGER));
			constructProductServiceBean(productServiceBean, quoteToLe, productSolution);
		}
		productServiceBean.setParentQuoteToLeId(teamsDRQuoteService.findParentQuoteToLe(quoteToLes).getId());
		LOGGER.info("Input  for update  product Service UCAAS {}", productServiceBean);
		return productServiceBean;
	}

	@Override
	public SiteSolutionOpportunityBean getSiteBeanInput(QuoteToLe quoteToLe,
														SiteSolutionOpportunityBean solutionOpportunityBean) throws TclCommonException {
		return null;
	}

	@Override
	public UpdateOpportunityStage getOpportunityUpdate(QuoteToLe quoteToLe,
													   UpdateOpportunityStage updateOpportunityStage) throws TclCommonException {
		String supplierContractEntity = Optional
				.ofNullable(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe,
						SUPPLIER_CONTRACTING_ENTITY))
				.orElse(Collections.emptyList()).stream().findFirst().map(QuoteLeAttributeValue::getAttributeValue)
				.orElse(CommonConstants.EMPTY);

		updateOpportunityStage.setBillingEntity(supplierContractEntity);
		updateOpportunityStage.setSalesAdministratorRegion(SFDCConstants.SALES_ADMIN_REGION);
		updateOpportunityStage.setSalesAdministrator(SFDCConstants.SALES_ADMIN);
		updateOpportunityStage.setCurrencyIsoCode(
				null != quoteToLe.getCurrencyCode() ? quoteToLe.getCurrencyCode() : SFDCConstants.INR);
		updateOpportunityStage.setQuoteToLeId(quoteToLe.getId());
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(quoteToLe.getQuote());
		updateOpportunityStage.setParentQuoteToLeId(teamsDRQuoteService.findParentQuoteToLe(quoteToLes).getId());
		return updateOpportunityStage;
	}

	@Override
	public void processSiteDetails(QuoteToLe quoteToLe, SiteSolutionOpportunityBean siteSolutionOpportunityBean,
								   ProductSolution productSolution) throws TclCommonException {

	}
}
