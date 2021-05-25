package com.tcl.dias.oms.service;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.sfdc.bean.ProductServiceBean;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.impl.OmsSfdcGscMapper;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcUtilService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_PRODUCT_NAME;

@Service
@Transactional
public class OmsSfdcService extends OmsSfdcUtilService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OmsSfdcService.class);

    @Autowired
    OmsSfdcGscMapper omsSfdcGscMapper;

    @Autowired
    ProductSolutionRepository productSolutionRepository;

    @Value("${rabbitmq.opportunity.updateproductservices}")
    String sfdcUpdatedService;

    /**
     * /** Update Product for GSC
     *
     * @param quoteToLe
     * @throws TclCommonException
     */
    public void processUpdateProductForWholesaleGSC(QuoteToLe quoteToLe) throws TclCommonException {
        List<QuoteToLeProductFamily> quoteToLeProductFamilies = quoteToLeProductFamilyRepository
                .findByQuoteToLe(quoteToLe.getId());
        for (QuoteToLeProductFamily quoteToLeProductFamily : quoteToLeProductFamilies) {
            if (GSC_PRODUCT_NAME.equalsIgnoreCase(quoteToLeProductFamily.getMstProductFamily().getName())
                    || GSC_ORDER_PRODUCT_COMPONENT_TYPE
                    .equalsIgnoreCase(quoteToLeProductFamily.getMstProductFamily().getName())) {
                List<ProductSolution> productSolutions = productSolutionRepository
                        .findByQuoteToLeProductFamily(quoteToLeProductFamily);
                ProductSolution productSolution = productSolutions.stream().findFirst().get();
                ProductServiceBean productServiceBean = omsSfdcGscMapper.updateProductServiceInput(quoteToLe,
                        productSolution);
                String request = Utils.convertObjectToJson(productServiceBean);
                LOGGER.info("Input for updating the product Details {}", request);
                persistSfdcServiceJob(quoteToLe.getQuote().getQuoteCode(), sfdcUpdatedService, request,
                        StringUtils.isNotBlank(productSolution.getTpsSfdcProductId()) ? CommonConstants.BACTIVE
                                : CommonConstants.BDEACTIVATE,
                        SFDCConstants.UPDATE_PRODUCT, getSequenceNumber(SFDCConstants.UPDATE_PRODUCT));
                break;
            }
        }

    }
}
