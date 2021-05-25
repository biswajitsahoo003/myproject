package com.tcl.dias.oms.factory.impl;


import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.sfdc.bean.Opportunity;
import com.tcl.dias.common.sfdc.bean.OpportunityBean;
import com.tcl.dias.common.sfdc.bean.ProductServiceBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.SolutionDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.repository.MacdDetailRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzosdwanSiteRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.core.OmsAbstractSfdcHandler;
import com.tcl.dias.oms.sfdc.core.OmsSfdcBundleInputHandler;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * @author vpachava
 *
 */

@Component
public class OmsSfdcSdwanMapper extends OmsAbstractSfdcHandler implements OmsSfdcBundleInputHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(OmsSfdcSdwanMapper.class);

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	OmsSfdcService omsSfdcService;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	SiteFeasibilityRepository siteFeasibilityRepository;

	@Autowired
	MACDUtils macdUtils;

	@Autowired
	MacdDetailRepository macdDetailRepository;

	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;
	
	@Autowired
	ProductSolutionRepository productSolutionRepository;
	
	@Autowired
	QuoteIzosdwanSiteRepository quoteIzosdwanSiteRepository;
	
	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Override
	public Opportunity getOpportunityBean(QuoteToLe quoteToLe, Opportunity opportunityBean) throws TclCommonException {

		opportunityBean.setSelectProductTypeC(SFDCConstants.IZOSDWAN);

		if (quoteToLe.getTermInMonths() != null) {
			Integer months = 12;
			String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
			if (termsInMonth.contains("year")) {
				months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;
			} else if (termsInMonth.contains("months")) {
				months = Integer.valueOf(termsInMonth.replace("months", "").trim());
			} else if (termsInMonth.contains("month")) {
				months = Integer.valueOf(termsInMonth.replace("month", "").trim());
			}
			opportunityBean.setTermsOfMonthsC(String.valueOf(months));
		}

		if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			if (MACDConstants.ADD_CLOUDVM_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				opportunityBean.setType(SFDCConstants.ADD_OR_UPGRADE_COMPONENT);
				opportunityBean.setSubTypeC(SFDCConstants.UPGRADE_LICENSE);
			} else if (MACDConstants.UPGRADE_VM.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				opportunityBean.setType(SFDCConstants.UPGRADE_OR_DOWNGRADE);
				opportunityBean.setSubTypeC(SFDCConstants.UPGRADE_LICENSE);
			} else if (MACDConstants.CONNECTIVITY_UPGRADE.equalsIgnoreCase(quoteToLe.getQuoteCategory())
					|| MACDConstants.ADDITIONAL_SERVICE_UPGRADE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				opportunityBean.setType(SFDCConstants.UPGRADE_OR_DOWNGRADE);
				opportunityBean.setSubTypeC(SFDCConstants.UPGRADE_LICENSE);
			} else if (MACDConstants.DELETE_VM.equalsIgnoreCase(quoteToLe.getQuoteCategory())
					|| MACDConstants.REQUEST_FOR_TERMINATION.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				opportunityBean.setType(SFDCConstants.TERMINATION);
				opportunityBean.setSubTypeC(SFDCConstants.ONCUSTOMERDEMAND);
			}
		}
		return opportunityBean;
	}

	@Override
	public ProductServiceBean getProductServiceInput(QuoteToLe quoteToLe, ProductServiceBean productServiceBean,
			ProductSolution solution) throws TclCommonException {
		String variant = CommonConstants.EMPTY;
		//productServiceBean.setProductType(SFDCConstants.IZOSDWAN.toString());
		SolutionDetail solutionDet = (SolutionDetail) Utils.convertJsonToObject(solution.getProductProfileData(),
				SolutionDetail.class);

//		for (ComponentDetail component : solutionDet.getComponents()) {
//			for (AttributeDetail attr : component.getAttributes()) {
//				if (attr.getName().equals(FPConstants.SERVICE_VARIANT.toString())) {
//					variant = attr.getValue();
//					break;
//				}
//			}
//
//		}
//		if (variant.contains(FPConstants.STANDARD.toString())) {
//			variant = SFDCConstants.STANDARD_INTERNET_ACCESS.toString();// need to change shoul be "Global VPN"
//		} else {
//			variant = SFDCConstants.PREMIUM_INTERNET_ACCESS.toString();// need to change shoul be "Global VPN"
//		}
//		productServiceBean.setType(variant);
//		productServiceBean.setRecordTypeName(SFDCConstants.INTERNET_ACCESS_SERVICE.toString());
//
//		if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
//			if (MACDConstants.REQUEST_TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
//				productServiceBean.setOrderType(SFDCConstants.TERMINATION);
//				productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
//			} else if (MACDConstants.ADD_IP_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
//				productServiceBean.setOrderType(SFDCConstants.HOTUPGRADE);
//				productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
//			} else if (MACDConstants.SHIFT_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
//				productServiceBean.setOrderType(SFDCConstants.PARALLELUPGRADE);
//				productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
//			} else if (MACDConstants.CHANGE_BANDWIDTH_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
//				productServiceBean.setOrderType(SFDCConstants.PARALLELUPGRADE);
//				productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
//			} else if (MACDConstants.OTHERS.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
//				productServiceBean.setOrderType(SFDCConstants.OTHERS_SFDC);
//				productServiceBean.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
//			}
//		}
		// LOGGER.info("ordertype"+productServiceBean.getOrderType());

		
		return productServiceBean;
	}

	@Override
	public ProductServiceBean updateProductServiceInput(QuoteToLe quoteToLe, ProductServiceBean productServiceBean,
			ProductSolution solution) throws TclCommonException {

		String variant = CommonConstants.EMPTY;
		productServiceBean.setProductLineOfBusiness(SFDCConstants.IPVPN.toString());

		SolutionDetail solutionDet = (SolutionDetail) Utils.convertJsonToObject(solution.getProductProfileData(),
				SolutionDetail.class);
		for (ComponentDetail component : solutionDet.getComponents()) {
			for (AttributeDetail attr : component.getAttributes()) {
				if (attr.getName().equals(FPConstants.SERVICE_VARIANT.toString())) {
					variant = attr.getValue();
					LOGGER.info("Variant is ---> {} ", variant);
					break;
				}
			}

		}
		if (variant.contains(FPConstants.STANDARD.toString())) {
			variant = SFDCConstants.STANDARD_INTERNET_ACCESS.toString();
		} else {
			variant = SFDCConstants.PREMIUM_INTERNET_ACCESS.toString();
		}
		productServiceBean.setType(variant);
		productServiceBean.setRecordTypeName(SFDCConstants.INTERNET_ACCESS_SERVICE.toString());
		if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			UpdateRequest updateRequest = new UpdateRequest();
			OpportunityBean opportunityBean = new OpportunityBean();
			Quote quote = quoteToLe.getQuote();
			// Quote quote = quoteToLe.getQuote();
			if (Objects.nonNull(quote)) {
				Optional<QuoteIllSite> illSiteOpt = quoteToLe.getQuote().getQuoteToLes().stream().findFirst()
						.map(QuoteToLe::getQuoteToLeProductFamilies).get().stream()
						.map(QuoteToLeProductFamily::getProductSolutions).findFirst().get().stream()
						.map(ProductSolution::getQuoteIllSites).findFirst().get().stream().findFirst();
				LOGGER.info("Site before update opportunity is ::::: {}", illSiteOpt.get().getId());
				updateRequest.setSiteId(Objects.nonNull(illSiteOpt.get().getId()) ? illSiteOpt.get().getId() : -1);
				updateRequest.setQuoteToLe(quoteToLe.getId());
				opportunityBean = omsSfdcService.updateOrderTypeAndSubtype(updateRequest);
				String orderType = opportunityBean.getType();
				if (orderType != null) {
					orderType = orderType.replace("-", "\u2013");
				}
				productServiceBean.setOrderType(orderType);
			}

		}
		return productServiceBean;
	}
}
