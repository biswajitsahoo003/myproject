package com.tcl.dias.oms.gsc.macd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class GscMACDService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GscMACDService.class);

    @Autowired
    GscMACDUtils gscMACDUtils;

    @Transactional
    public List<MACDOrderResponse> handleMACDRequest(MACDOrderRequest macdRequest,
                                                     HttpServletResponse httpServletResponse) {
        return gscMACDUtils.handleMACDRequest(macdRequest, httpServletResponse);
    }

}
