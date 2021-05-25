package com.tcl.dias.oms.gsc.macd.helpers;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.gsc.beans.GscQuoteConfigurationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GscITFSMACDHelper extends GscBaseMACDHelper {

    public static final Logger LOGGER = LoggerFactory.getLogger(GscITFSMACDHelper.class);

    @Override
    public Quote cloneQuoteFromConfiguration(String requestType, SIServiceDetailDataBean serviceDetail,
                                             SIOrderDataBean orderDataBean, Map<String, Object> data) {
        Quote newQuote = super.cloneQuoteFromConfiguration(requestType, serviceDetail, orderDataBean, data);
        Integer newQuoteId = newQuote.getId();
        return gscQuoteService.getGscQuoteById(newQuoteId).map(quoteDataBean -> {
            Integer newSolutionId = quoteDataBean.getSolutions().get(0).getSolutionId();
            Integer newQuoteGscId = quoteDataBean.getSolutions().get(0).getGscQuotes().get(0).getId();
            // create configuration
            GscQuoteConfigurationBean configurationBean = new GscQuoteConfigurationBean();
            if (!Strings.isNullOrEmpty(serviceDetail.getSourceCountryCode())) {
                configurationBean.setSource(gscMACDUtils.getCountryNameForCode(serviceDetail.getSourceCountryCode()));
            }
            if (!Strings.isNullOrEmpty(serviceDetail.getDestinationCountryCode())) {
                configurationBean
                        .setDestination(gscMACDUtils.getCountryNameForCode(serviceDetail.getDestinationCountryCode()));
            }
            Integer newConfigurationId = gscQuoteDetailService
                    .createConfiguration(newQuoteId, newSolutionId, newQuoteGscId, ImmutableList.of(configurationBean))
                    .get(0).getId();
            LOGGER.info("MACD successfully created quote: {}, solution: {}, quoteGsc: {},configuration: {}", newQuoteId,
                    newSolutionId, newQuoteGscId, newConfigurationId);
            return newQuote;
        }).get();
    }
}
