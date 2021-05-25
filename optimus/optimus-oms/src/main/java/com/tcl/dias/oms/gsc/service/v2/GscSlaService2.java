package com.tcl.dias.oms.gsc.service.v2;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.gsc.beans.GscSlaBeanListener;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.repository.QuoteGscSlaRepository;
import com.tcl.dias.oms.entity.repository.SlaMasterRepository;
import com.tcl.dias.oms.gsc.service.v1.GscSlaService;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.tcl.dias.oms.gsc.util.GscConstants.ACCESS_TYPE;

/*
 *
 *  @author Syed Ali
 *  @link http://www.tatacommunications.com/
 *  @copyright 2020 Tata Communications Limited
 *
 */
@Service
public class GscSlaService2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(GscSlaService.class);

    private final MQUtils mqUtils;

    private final SlaMasterRepository slaMasterRepository;

    private final QuoteGscSlaRepository quoteGscSlaRepository;

    @Value("${rabbitmq.gsi.sla.queue}")
    String productSlaQueue;

    @Value("${rabbitmq.gsc.sla.queue}")
    String getProductSlaQueue;

    @Autowired
    public GscSlaService2(final MQUtils mqUtils, final SlaMasterRepository slaMasterRepository,
                          final QuoteGscSlaRepository quoteGscSlaRepository) {
        this.mqUtils = mqUtils;
        this.slaMasterRepository = slaMasterRepository;
        this.quoteGscSlaRepository = quoteGscSlaRepository;
    }

    /**
     * Get Sla details based on access type
     *
     * @param accessType
     * @return {@link GscSlaBeanListener}
     * @throws TclCommonException
     *
     */
    public GscSlaBeanListener getSlaDetails(String accessType) throws TclCommonException {
        Objects.requireNonNull(accessType, ACCESS_TYPE);
        GscSlaBeanListener gscSlaBeanListener = new GscSlaBeanListener();
        gscSlaBeanListener.setAccessType(accessType);
        LOGGER.info("MDC Filter token value in before Queue call processSla {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        String gscSlaBeanListenerRequest = Utils.convertObjectToJson(gscSlaBeanListener);
        final String response = (String) mqUtils.sendAndReceive(getProductSlaQueue, gscSlaBeanListenerRequest);
        LOGGER.info("Output Payload for for sla with tier {}", response);
        if (StringUtils.isNotBlank(response)) {
            gscSlaBeanListener = GscUtils.fromJson(response, GscSlaBeanListener.class);
        }
        return gscSlaBeanListener;
    }

}
