package com.tcl.dias.oms.impl;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.sfdc.bean.FeasibilityRequestBean;
import com.tcl.dias.common.sfdc.bean.OpportunityBean;
import com.tcl.dias.common.sfdc.bean.ProductServiceBean;
import com.tcl.dias.common.sfdc.bean.SiteSolutionOpportunityBean;
import com.tcl.dias.common.sfdc.bean.UpdateOpportunityStage;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.AttributeConstants;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.gsc.macd.MACDOrderRequest;
import com.tcl.dias.oms.gsc.util.GscConstants;
//import com.tcl.dias.oms.gvpn.pricing.bean.NotFeasible;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.core.IOmsSfdcInputHandler;
import com.tcl.dias.oms.sfdc.core.OmsAbstractSfdcHandler;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * This file contains the OmsSfdcOpportunityUpdateInputMapper.java class.
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class OmsSfdcGscMapper extends OmsAbstractSfdcHandler implements IOmsSfdcInputHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OmsSfdcGscMapper.class);

    @Autowired
    QuoteGscRepository quoteGscRepository;

    @Autowired
    QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

    @Autowired
    QuoteProductComponentRepository quoteProductComponentRepository;

    @Autowired
    QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

    @Autowired
    SiteFeasibilityRepository siteFeasibilityRepository;

    private static final String SUPPLIER_CONTRACTING_ENTITY = "Supplier Contracting Entity";

    /**
     * Get Opportunity Bean
     *
     * @param quoteToLe
     * @param opportunityBean
     * @return
     */
    @Override
    public OpportunityBean getOpportunityBean(QuoteToLe quoteToLe, OpportunityBean opportunityBean) {
        opportunityBean.setSelectProductType(SFDCConstants.GSC);
        if(quoteToLe.getTermInMonths()!=null) {
			Integer months = 12;
			Double months1 = 0.0;
			String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
			if (termsInMonth.contains("year")) {
				months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;
			}
			else if (termsInMonth.contains(".") && termsInMonth.contains("months")){
				months1 = Double.valueOf(termsInMonth.replace("months", "").trim());
			}
			else if (termsInMonth.contains("months")) {
				months = Integer.valueOf(termsInMonth.replace("months", "").trim());
			} else if (termsInMonth.contains("month")) {
				months = Integer.valueOf(termsInMonth.replace("month", "").trim());
			}

			int compare = Double.compare(months1, 0.0);
			if(compare==0){
				LOGGER.info("Inside 'Integer' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months);
				opportunityBean.setTermOfMonths(String.valueOf(months));
			}
			else {
				LOGGER.info("Inside 'Double' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months1);
				opportunityBean.setTermOfMonths(String.valueOf(months1));
			}

			LOGGER.info("Final set value for term in months in Update Opty block is ----> {} " , opportunityBean.getTermOfMonths());

		}

        if(GscConstants.ORDER_TYPE_MACD.equalsIgnoreCase(quoteToLe.getQuoteType())) {
            opportunityBean.setType(SFDCConstants.CHANGE_ORDER);
            opportunityBean.setSubType(SFDCConstants.CHANGE_ORDER);
            if(MACDOrderRequest.REQUEST_TYPE_NUMBER_REMOVE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
                opportunityBean.setSubType(SFDCConstants.ADDITION_DELETION);
            }
        }
        opportunityBean.setCurrencyIsoCode(quoteToLe.getCurrencyCode());

        LOGGER.info("Input  for create Opportunity GSC {}", opportunityBean);

        return opportunityBean;
    }

    /**
     * Get product service as input
     *
     * @param quoteToLe
     * @param productServiceBean
     * @param productSolution
     * @return {@link ProductServiceBean}
     */
    @Override
    public ProductServiceBean getProductServiceInput(QuoteToLe quoteToLe, ProductServiceBean productServiceBean, ProductSolution productSolution) {
        constructProductServiceBean(productServiceBean, quoteToLe);
        LOGGER.info("Input  for product Service GSC {}", productServiceBean);
        return productServiceBean;
    }

    /**
     * Update Product Service as Input
     *
     * @param quoteToLe
     * @param productServiceBean
     * @param solution
     * @return {@link ProductServiceBean}
     */
    @Override
    public ProductServiceBean updateProductServiceInput(QuoteToLe quoteToLe, ProductServiceBean productServiceBean, ProductSolution solution) {
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
        //String currencyIsoCode = attributeMapper.get(LeAttributesConstants.CURRENCY_ISO_CODE);
        //productServiceBean.setCurrencyIsoCode(currencyIsoCode != null ? currencyIsoCode : SFDCConstants.INR);
        productServiceBean.setCurrencyIsoCode(null != quoteToLe.getCurrencyCode() ? quoteToLe.getCurrencyCode() : SFDCConstants.USD);
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
        LOGGER.info("Input  for update  product Service GSC {}", productServiceBean);
        return productServiceBean;
    }

    /**
     * getSiteBeanInput
     *
     * @param quoteToLe
     * @param solutionOpportunityBean
     * @return
     */
    @Override
    public SiteSolutionOpportunityBean getSiteBeanInput(QuoteToLe quoteToLe, SiteSolutionOpportunityBean solutionOpportunityBean) {
        // TODO Auto-generated method stub
        return solutionOpportunityBean;
    }

    /**
     * getOpportunityUpdate
     *
     * @param quoteToLe
     * @param updateOpportunityStage
     * @return
     */
    @Override
    public UpdateOpportunityStage getOpportunityUpdate(QuoteToLe quoteToLe, UpdateOpportunityStage updateOpportunityStage) {
        final String supplierContractEntity = quoteLeAttributeValueRepository
                .findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, SUPPLIER_CONTRACTING_ENTITY).stream().findFirst()
                .map(QuoteLeAttributeValue::getAttributeValue).orElse("");

        updateOpportunityStage.setBillingEntity(supplierContractEntity);
        updateOpportunityStage.setSalesAdministratorRegion(SFDCConstants.SALES_ADMIN_REGION);
        updateOpportunityStage.setSalesAdministrator(SFDCConstants.SALES_ADMIN);
        updateOpportunityStage.setCurrencyIsoCode(null != quoteToLe.getCurrencyCode() ? quoteToLe.getCurrencyCode() : SFDCConstants.INR);
        return updateOpportunityStage;
    }

    @Override
    public void processSiteDetails(QuoteToLe quoteToLe, SiteSolutionOpportunityBean siteSolutionOpportunityBean, ProductSolution productSolution) throws TclCommonException {
        // TODO Auto-generated method stub
    }

    /**
     * Method to construct product service bean
     *
     * @param productServiceBean
     * @param quoteToLe
     * @return {@link ProductServiceBean}
     */
    private ProductServiceBean constructProductServiceBean(ProductServiceBean productServiceBean, QuoteToLe quoteToLe) {
        productServiceBean.setRecordTypeName(SFDCConstants.GSC);
        productServiceBean.setProductType(SFDCConstants.GSC);
        productServiceBean.setProductLineOfBusiness(SFDCConstants.VOICE);
        Map<String, String> attributeMapper = getQuoteLeAttributes(quoteToLe);
        if (quoteToLe.getTermInMonths() != null) {
			Integer months = 12;
			Double months1 = 0.0;
			String termsInMonth = quoteToLe.getTermInMonths().toLowerCase();
			if (termsInMonth.contains("year")) {
				months = Integer.valueOf(termsInMonth.replace("year", "").trim()) * 12;
			}
			else if (termsInMonth.contains(".") && termsInMonth.contains("months")){
				months1 = Double.valueOf(termsInMonth.replace("months", "").trim());
			}
			else if (termsInMonth.contains("months")) {
				months = Integer.valueOf(termsInMonth.replace("months", "").trim());
			} else if (termsInMonth.contains("month")) {
				months = Integer.valueOf(termsInMonth.replace("month", "").trim());
			}

			int compare = Double.compare(months1, 0.0);
			if(compare==0){
				LOGGER.info("Inside 'Integer' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months);
				productServiceBean.setTermInMonthsC(String.valueOf(months));
			}
			else {
				LOGGER.info("Inside 'Double' block, for setting contract term for quote ----> {} , value is ----> {} " , quoteToLe.getQuote().getQuoteCode(), months1);
				productServiceBean.setTermInMonthsC(String.valueOf(months1));
			}

			LOGGER.info("Final set value for term in months in Update Opty block is ----> {} " , productServiceBean.getTermInMonthsC());

		}
        if(GscConstants.ORDER_TYPE_MACD.equalsIgnoreCase(quoteToLe.getQuoteType())) {
            productServiceBean.setOrderType(SFDCConstants.CHANGE_ORDER);
        }

        productServiceBean.setOpportunityNameC(SFDCConstants.GSC);
        productServiceBean.setDataOrMobileC(SFDCConstants.DATA);
        productServiceBean.setpOCAttachedC(SFDCConstants.POC_ATTACHED);
        productServiceBean.setEnabledForUnifiedAccessC(SFDCConstants.NO);
        productServiceBean.setProductCategoryC(SFDCConstants.MANAGED_SERVICES);
        productServiceBean.setCallTypeC(SFDCConstants.ENTERPRISE);
        productServiceBean.setProductNRC(quoteToLe.getFinalNrc());
        productServiceBean.setProductMRC(quoteToLe.getFinalMrc());

        processPrimaryFeaturesAndAccessType(quoteToLe, productServiceBean);
        return productServiceBean;

    }

    /**
     * Method to process primary features and access type
     *
     * @param quoteToLe
     * @param productServiceBean
     */
    private void processPrimaryFeaturesAndAccessType(QuoteToLe quoteToLe, ProductServiceBean productServiceBean) {
        StringBuilder primaryFeatures = new StringBuilder();
        productServiceBean.setItfsC(SFDCConstants.FALSE);
        productServiceBean.setLnsC(SFDCConstants.FALSE);
        productServiceBean.setItfsC(SFDCConstants.FALSE);
        productServiceBean.setUifnC(SFDCConstants.FALSE);
        productServiceBean.setAudioConferencingAccessNoServiceC(SFDCConstants.FALSE);
        productServiceBean.setAudioConferencingDTFServiceC(SFDCConstants.FALSE);

        List<QuoteGsc> quoteGscs = quoteGscRepository.findByQuoteToLeId(quoteToLe.getId());


        quoteGscs.forEach(quoteGsc -> {
            // based on access type
            setAccessType(quoteGsc, productServiceBean);

            // based on product service name
            setPrimaryFeatures(quoteGsc, productServiceBean, primaryFeatures);
        });
    }

    /**
     * Method to set access type
     *
     * @param quoteGsc
     * @param productServiceBean
     */
    private void setAccessType(QuoteGsc quoteGsc, ProductServiceBean productServiceBean) {
        switch (quoteGsc.getAccessType()) {
            case GscConstants.PSTN:
                productServiceBean.setInterconnectTypeC(SFDCConstants.PSTN);
                break;
            case GscConstants.PUBLIC_IP:
                productServiceBean.setInterconnectTypeC(SFDCConstants.PUBLIC_IP);
                break;
            case GscConstants.MPLS:
                productServiceBean.setInterconnectTypeC(SFDCConstants.MPLS_BUNDLED);
                break;
            case GscConstants.NNI:
                productServiceBean.setInterconnectTypeC(SFDCConstants.MPLS_BUNDLED);
                break;
            default:
                break;
        }
    }

    /**
     * Method to set primary features
     *
     * @param quoteGsc
     * @param productServiceBean
     */
    private void setPrimaryFeatures(QuoteGsc quoteGsc, ProductServiceBean productServiceBean, StringBuilder primaryFeatures) {
        switch (quoteGsc.getProductName()) {
            case GscConstants.ITFS:
                if (!primaryFeatures.toString().contains(SFDCConstants.GLOBAL_INBOUND_OFFNET_VOICE)) {
                    primaryFeatures.append(SFDCConstants.GLOBAL_INBOUND_OFFNET_VOICE)
                            .append(CommonConstants.SEMI_COMMA);
                }
                productServiceBean.setItfsC(SFDCConstants.TRUE);
                break;
            case GscConstants.UIFN:
                if (!primaryFeatures.toString().contains(SFDCConstants.GLOBAL_INBOUND_OFFNET_VOICE)) {
                    primaryFeatures.append(SFDCConstants.GLOBAL_INBOUND_OFFNET_VOICE)
                            .append(CommonConstants.SEMI_COMMA);
                }
                productServiceBean.setUifnC(SFDCConstants.TRUE);
                break;
            case GscConstants.LNS:
                if (!primaryFeatures.toString().contains(SFDCConstants.GLOBAL_INBOUND_OFFNET_VOICE)) {
                    primaryFeatures.append(SFDCConstants.GLOBAL_INBOUND_OFFNET_VOICE)
                            .append(CommonConstants.SEMI_COMMA);
                }
                productServiceBean.setLnsC(SFDCConstants.TRUE);
                break;
            case GscConstants.GLOBAL_OUTBOUND:
                if (!primaryFeatures.toString().contains(SFDCConstants.GLOBAL_OUTBOUND_OFFNET_VOICE)) {
                    primaryFeatures.append(SFDCConstants.GLOBAL_OUTBOUND_OFFNET_VOICE)
                            .append(CommonConstants.SEMI_COMMA);
                }
                break;
            case GscConstants.DOMESTIC_VOICE:
                if (!primaryFeatures.toString().contains(SFDCConstants.DOMESTIC_VOICE)) {
                    primaryFeatures.append(SFDCConstants.DOMESTIC_VOICE).append(CommonConstants.SEMI_COMMA);
                }
                break;
            case GscConstants.ACDTFS:
                if (!primaryFeatures.toString().contains(SFDCConstants.AC_DTF_SERVICE)) {
                    primaryFeatures.append(SFDCConstants.GLOBAL_INBOUND_OFFNET_VOICE).append(CommonConstants.SEMI_COMMA)
                            .append(SFDCConstants.AC_DTF_SERVICE).append(CommonConstants.SEMI_COMMA);
                }
                productServiceBean.setAudioConferencingDTFServiceC(SFDCConstants.TRUE);
                productServiceBean.setCurrencyIsoCode(SFDCConstants.INR);
                break;
            case GscConstants.ACANS:
                if (!primaryFeatures.toString().contains(SFDCConstants.AC_ACCESS_NO_SERVICE)) {
                    primaryFeatures.append(SFDCConstants.GLOBAL_INBOUND_OFFNET_VOICE).append(CommonConstants.SEMI_COMMA)
                            .append(SFDCConstants.AC_ACCESS_NO_SERVICE).append(CommonConstants.SEMI_COMMA);
                }
                productServiceBean.setAudioConferencingAccessNoServiceC(SFDCConstants.TRUE);
                productServiceBean.setCurrencyIsoCode(SFDCConstants.INR);
                break;
            default:
                break;

        }
        productServiceBean.setPrimaryFeaturesC(primaryFeatures.toString());

    }

    /**
     * getFeasibilityBean
     *
     * @param quoteIllSiteId
     * @param feasibilityBean
     * @return
     * @throws TclCommonException
     */
    @Override
    public FeasibilityRequestBean getFeasibilityBean(Integer quoteIllSiteId,
                                                     FeasibilityRequestBean feasibilityBean) throws TclCommonException {
//        List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndType
//                (quoteIllSiteId, ComponentConstants.VPN_PORT.getComponentsValue(), "primary");
//        List<QuoteProductComponentsAttributeValue> quotatt = quoteProductComponentsAttributeValueRepository.
//                findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponents.get(0).getId(),
//                        ComponentConstants.PORT_BANDWIDTH.getComponentsValue());
//        feasibilityBean.setPortCircuitCapacityC(quotatt.get(0).getAttributeValues() + " Mbps");
//        List<QuoteProductComponentsAttributeValue> quotatt1 = quoteProductComponentsAttributeValueRepository.
//                findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponents.get(0).getId(),
//                        AttributeConstants.INTERFACE.toString());
//        feasibilityBean.setInterfaceC(quotatt1.get(0).getAttributeValues());
//        List<QuoteProductComponent> quoteProductComponents1 = quoteProductComponentRepository
//                .findByReferenceIdAndMstProductComponent_NameAndType(
//                        quoteIllSiteId, AttributeConstants.LAST_MILE.toString(),
//                        "primary");
//        List<QuoteProductComponentsAttributeValue> quoteattribute = quoteProductComponentsAttributeValueRepository
//                .findByQuoteProductComponent_IdAndProductAttributeMaster_Name(
//                        quoteProductComponents1.get(0).getId(),
//                        ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue());
//        if (!quoteattribute.isEmpty()) {
//            String localLoopCapacity = quoteattribute.get(0)
//                    .getAttributeValues();
//
//            String[] spliter = localLoopCapacity.split("\\s+");
//            String locLoopCap = spliter[0];
//            String unit = "Mbps";
//            if (spliter.length > 1) {
//                unit = spliter[1];
//            }
//            feasibilityBean.setIllLocalLoopCapacityC(locLoopCap);
//            feasibilityBean.setIllLocalLoopCapacityUnitC(unit);
//        }
//
//
//        String feasiblityResponseJson = siteFeasibilityRepository.findByQuoteIllSite_IdAndType(quoteIllSiteId, "primary")
//                .stream().findFirst()
//                //.min(Comparator.comparing(SiteFeasibility::getRank))
//                .map(SiteFeasibility::getResponseJson)
//                .orElse(StringUtils.EMPTY);
//        NotFeasible nonFeasible = (NotFeasible) Utils.convertJsonToObject(feasiblityResponseJson, NotFeasible.class);
//        feasibilityBean.setOtherPOPAEndC(nonFeasible.getPopName() != null ? nonFeasible.getPopName() : SFDCConstants.DEFAULT_POP_NAME);

        return feasibilityBean;
    }

}
