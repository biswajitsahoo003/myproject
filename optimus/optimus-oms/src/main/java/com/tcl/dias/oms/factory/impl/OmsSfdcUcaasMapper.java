package com.tcl.dias.oms.factory.impl;

import static com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants.SUPPLIER_CONTRACTING_ENTITY;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.AttributeConstants;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.gvpn.pricing.bean.NotFeasible;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.sfdc.bean.FeasibilityRequestBean;
import com.tcl.dias.common.sfdc.bean.OpportunityBean;
import com.tcl.dias.common.sfdc.bean.ProductServiceBean;
import com.tcl.dias.common.sfdc.bean.SiteSolutionOpportunityBean;
import com.tcl.dias.common.sfdc.bean.UpdateOpportunityStage;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.sfdc.core.IOmsSfdcInputHandler;
import com.tcl.dias.oms.sfdc.core.OmsAbstractSfdcHandler;
import com.tcl.dias.oms.webex.util.WebexConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the SFDC operations related to UCAAS.
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Component
public class OmsSfdcUcaasMapper extends OmsAbstractSfdcHandler implements IOmsSfdcInputHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(OmsSfdcUcaasMapper.class);

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

	/**
	 * Get Opportunity Bean
	 *
	 * @param quoteToLe
	 * @param opportunityBean
	 * @return
	 */
	@Override
	public OpportunityBean getOpportunityBean(QuoteToLe quoteToLe, OpportunityBean opportunityBean) {
		opportunityBean.setSelectProductType(SFDCConstants.CISCO_WEBEX_CCA);
		if (quoteToLe.getTermInMonths() != null) {
			Integer months = 12;
			String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
			if (termsInMonth.contains(WebexConstants.YEAR)) {
				months = Integer.valueOf(termsInMonth.replace(WebexConstants.YEAR, CommonConstants.EMPTY).trim()) * 12;
			} else if (termsInMonth.contains(WebexConstants.MONTHS)) {
				months = Integer.valueOf(termsInMonth.replace(WebexConstants.MONTHS, CommonConstants.EMPTY).trim());
			} else if (termsInMonth.contains(WebexConstants.MONTH)) {
				months = Integer.valueOf(termsInMonth.replace(WebexConstants.MONTH, CommonConstants.EMPTY).trim());
			}
			opportunityBean.setTermOfMonths(String.valueOf(months));
		}

		// To be enabled when macd in placed.
//		if(GscConstants.ORDER_TYPE_MACD.equalsIgnoreCase(quoteToLe.getQuoteType())) {
//			opportunityBean.setType(SFDCConstants.CHANGE_ORDER);
//			opportunityBean.setSubType(SFDCConstants.CHANGE_ORDER);
//			if(MACDOrderRequest.REQUEST_TYPE_NUMBER_REMOVE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
//				opportunityBean.setSubType(SFDCConstants.ADDITION_DELETION);
//			}
//		}
		opportunityBean.setCurrencyIsoCode(quoteToLe.getCurrencyCode());

		LOGGER.info("Input  for create Opportunity UCAAS {}", opportunityBean);

		return opportunityBean;
	}

	/**
	 * Method to construct product service bean
	 *
	 * @param productServiceBean
	 * @param quoteToLe
	 * @return {@link ProductServiceBean}
	 */
	private ProductServiceBean constructProductServiceBean(ProductServiceBean productServiceBean, QuoteToLe quoteToLe) {
		productServiceBean.setRecordTypeName(SFDCConstants.CISCO_WEBEX_CCA);
		productServiceBean.setProductType(SFDCConstants.CISCO_WEBEX_CCA);
		productServiceBean.setProductLineOfBusiness(SFDCConstants.GMMS);
		if (quoteToLe.getTermInMonths() != null) {
			Integer months = 12;
			String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
			if (termsInMonth.contains(WebexConstants.YEAR)) {
				months = Integer.valueOf(termsInMonth.replace(WebexConstants.YEAR, CommonConstants.EMPTY).trim()) * 12;
			} else if (termsInMonth.contains(WebexConstants.MONTHS)) {
				months = Integer.valueOf(termsInMonth.replace(WebexConstants.MONTHS, CommonConstants.EMPTY).trim());
			} else if (termsInMonth.contains(WebexConstants.MONTH)) {
				months = Integer.valueOf(termsInMonth.replace(WebexConstants.MONTH, CommonConstants.EMPTY).trim());
			}
			productServiceBean.setTermInMonthsC(String.valueOf(months));
		}

		// When macd in place.
//		if(GscConstants.ORDER_TYPE_MACD.equalsIgnoreCase(quoteToLe.getQuoteType())) {
//			productServiceBean.setOrderType(SFDCConstants.CHANGE_ORDER);
//		}

		productServiceBean.setOpportunityNameC(SFDCConstants.WEBEX_A_FLEX_LICENCE_NEW);
		productServiceBean.setDataOrMobileC(SFDCConstants.DATA);
		productServiceBean.setpOCAttachedC(SFDCConstants.POC_ATTACHED);
		productServiceBean.setEnabledForUnifiedAccessC(SFDCConstants.NO);
		productServiceBean.setProductCategoryC(SFDCConstants.MANAGED_SERVICES);
		productServiceBean.setCallTypeC(SFDCConstants.ENTERPRISE);
		productServiceBean.setProductNRC(quoteToLe.getFinalNrc());
		productServiceBean.setProductMRC(quoteToLe.getFinalMrc());

		return productServiceBean;

	}

	/**
	 *
	 * @param quoteIllSiteId
	 * @param feasibilityBean
	 * @return
	 * @throws TclCommonException
	 */
	@Override
	public FeasibilityRequestBean getFeasibilityBean(Integer quoteIllSiteId,
													 FeasibilityRequestBean feasibilityBean)
			throws TclCommonException {
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndType
				(quoteIllSiteId, ComponentConstants.VPN_PORT.getComponentsValue(), "primary");
		List<QuoteProductComponentsAttributeValue> quotatt = quoteProductComponentsAttributeValueRepository.
				findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponents.get(0).getId(),
						ComponentConstants.PORT_BANDWIDTH.getComponentsValue());
		feasibilityBean.setPortCircuitCapacityC(quotatt.get(0).getAttributeValues() + " Mbps");
		List<QuoteProductComponentsAttributeValue> quotatt1 = quoteProductComponentsAttributeValueRepository.
				findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponents.get(0).getId(),
						AttributeConstants.INTERFACE.toString());
		feasibilityBean.setInterfaceC(quotatt1.get(0).getAttributeValues());
		List<QuoteProductComponent> quoteProductComponents1 = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndType(
						quoteIllSiteId, AttributeConstants.LAST_MILE.toString(),
						"primary");
		List<QuoteProductComponentsAttributeValue> quoteattribute = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(
						quoteProductComponents1.get(0).getId(),
						ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue());
		if (!quoteattribute.isEmpty()) {
			String localLoopCapacity = quoteattribute.get(0)
					.getAttributeValues();

			String[] spliter = localLoopCapacity.split("\\s+");
			String locLoopCap = spliter[0];
			String unit = "Mbps";
			if (spliter.length > 1) {
				unit = spliter[1];
			}
			feasibilityBean.setIllLocalLoopCapacityC(locLoopCap);
			feasibilityBean.setIllLocalLoopCapacityUnitC(unit);
		}


		String feasiblityResponseJson = siteFeasibilityRepository.findByQuoteIllSite_IdAndType(quoteIllSiteId, "primary")
				.stream().findFirst()
				//.min(Comparator.comparing(SiteFeasibility::getRank))
				.map(SiteFeasibility::getResponseJson)
				.orElse(StringUtils.EMPTY);
		NotFeasible nonFeasible = (NotFeasible) Utils.convertJsonToObject(feasiblityResponseJson, NotFeasible.class);
		feasibilityBean.setOtherPOPAEndC(nonFeasible.getPopName() != null ? nonFeasible.getPopName() : SFDCConstants.DEFAULT_POP_NAME);

		return feasibilityBean;
	}

	/**
	 * Method to product service input
	 *
	 * @param quoteToLe
	 * @param productServiceBean
	 * @param solution
	 * @return {@link ProductServiceBean}
	 */
	@Override
	public ProductServiceBean getProductServiceInput(QuoteToLe quoteToLe, ProductServiceBean productServiceBean,
			ProductSolution solution) throws TclCommonException {
		if (!solution.getProductProfileData().contains(WebexConstants.WEBEX))
			omsSfdcGscMapper.getProductServiceInput(quoteToLe, productServiceBean, solution);
		else
			constructProductServiceBean(productServiceBean, quoteToLe);
		LOGGER.info("Input  for product Service UCAAS {}", productServiceBean);
		return productServiceBean;
	}

	/**
	 * Method to update product service input
	 *
	 * @param quoteToLe
	 * @param productServiceBean
	 * @param solution
	 * @return {@link ProductServiceBean}
	 */
	@Override
	public ProductServiceBean updateProductServiceInput(QuoteToLe quoteToLe, ProductServiceBean productServiceBean,
														ProductSolution solution) throws TclCommonException {
		productServiceBean = Objects.isNull(productServiceBean)?new ProductServiceBean():productServiceBean;
		Map<String, String> attributeMapper = getQuoteLeAttributes(quoteToLe);
		productServiceBean.setProductName(solution.getTpsSfdcProductName());
		productServiceBean.setProductId(solution.getTpsSfdcProductId());
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
		productServiceBean.setProductSolutionCode(solution.getSolutionCode());
		productServiceBean
				.setL2feasibilityCommercialManagerName(attributeMapper.get(LeAttributesConstants.PROGRAM_MANAGER));
		constructProductServiceBean(productServiceBean, quoteToLe);
		LOGGER.info("Input  for update  product Service UCAAS {}", productServiceBean);
		return productServiceBean;
	}

	/**
	 * Update Product Service as Input
	 *
	 * @param quoteToLe
	 * @param productSolution
	 * @return {@link ProductServiceBean}
	 */
	public ProductServiceBean updateProductServiceInput(QuoteToLe quoteToLe, ProductSolution productSolution) {
		ProductServiceBean productServiceBean = new ProductServiceBean();
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
		constructProductServiceBean(productServiceBean, quoteToLe);
		LOGGER.info("Input  for update  product Service UCAAS {}", productServiceBean);
		return productServiceBean;
	}

	/**
	 * Method to get site bean input
	 *
	 * @param quoteToLe
	 * @param solutionOpportunityBean
	 * @return {@link ProductServiceBean}
	 */
	@Override
	public SiteSolutionOpportunityBean getSiteBeanInput(QuoteToLe quoteToLe,
			SiteSolutionOpportunityBean solutionOpportunityBean) throws TclCommonException {
		return null;
	}

	/**
	 * getOpportunityUpdate
	 *
	 * @param quoteToLe
	 * @param updateOpportunityStage
	 * @return
	 */
	@Override
	public UpdateOpportunityStage getOpportunityUpdate(QuoteToLe quoteToLe,
			UpdateOpportunityStage updateOpportunityStage) {
		final String supplierContractEntity = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, SUPPLIER_CONTRACTING_ENTITY).stream().findFirst()
				.map(QuoteLeAttributeValue::getAttributeValue).orElse(CommonConstants.EMPTY);

		updateOpportunityStage.setBillingEntity(supplierContractEntity);
		updateOpportunityStage.setSalesAdministratorRegion(SFDCConstants.SALES_ADMIN_REGION);
		updateOpportunityStage.setSalesAdministrator(SFDCConstants.SALES_ADMIN);
		updateOpportunityStage.setCurrencyIsoCode(
				null != quoteToLe.getCurrencyCode() ? quoteToLe.getCurrencyCode() : SFDCConstants.INR);
		return updateOpportunityStage;
	}

	/**
	 * Method to construct product service bean
	 *
	 * @param quoteToLe
	 * @param siteSolutionOpportunityBean
	 * @param productSolution
	 * @return {@link ProductServiceBean}
	 */
	@Override
	public void processSiteDetails(QuoteToLe quoteToLe, SiteSolutionOpportunityBean siteSolutionOpportunityBean,
			ProductSolution productSolution) throws TclCommonException {

	}

}
