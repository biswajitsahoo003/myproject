package com.tcl.dias.oms.gsc.service.v1;

import static com.tcl.dias.oms.gsc.util.GscConstants.ACCESS_TYPE;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_GSC_NULL_MESSAGE;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.ProductSlaBean;
import com.tcl.dias.common.beans.SLaDetailsBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.GscSlaConstants;
import com.tcl.dias.common.gsc.beans.GscSlaBeanListener;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteGscSla;
import com.tcl.dias.oms.entity.entities.SlaMaster;
import com.tcl.dias.oms.entity.repository.QuoteGscSlaRepository;
import com.tcl.dias.oms.entity.repository.SlaMasterRepository;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Services to handle all quote sla related functionality
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GscSlaService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GscSlaService.class);

	private final MQUtils mqUtils;
	private final SlaMasterRepository slaMasterRepository;
	private final QuoteGscSlaRepository quoteGscSlaRepository;
	@Value("${rabbitmq.gsi.sla.queue}")
	String productSlaQueue;
	@Value("${rabbitmq.gsc.sla.queue}")
	String getProductSlaQueue;

	@Autowired
	public GscSlaService(final MQUtils mqUtils, final SlaMasterRepository slaMasterRepository,
			final QuoteGscSlaRepository quoteGscSlaRepository) {
		this.mqUtils = mqUtils;
		this.slaMasterRepository = slaMasterRepository;
		this.quoteGscSlaRepository = quoteGscSlaRepository;
	}

	/**
	 * Process SLA for MPLS / Public IP
	 *
	 * @param quoteGsc
	 * @throws IllegalArgumentException
	 * @throws TclCommonException
	 */
	public void processSla(QuoteGsc quoteGsc) throws TclCommonException, IllegalArgumentException {
		Objects.requireNonNull(quoteGsc, QUOTE_GSC_NULL_MESSAGE);
		ProductSlaBean productBean = null;

		final String slaRequest = quoteGsc.getAccessType() + CommonConstants.COMMA;
		LOGGER.info("MDC Filter token value in before Queue call processSla {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		final String response = (String) mqUtils.sendAndReceive(productSlaQueue, slaRequest);
		LOGGER.info("Output Payload for for sla with tier {}", response);

		if (StringUtils.isNotBlank( response)) {
			productBean = GscUtils.fromJson(response, ProductSlaBean.class);
		}

		if (Objects.nonNull(productBean)) {
			final List<SLaDetailsBean> slaDetailBeans = productBean.getsLaDetails();
			createSlaDetails(quoteGsc, slaDetailBeans);
		}
	}

	/**
	 * Create Sla details by Bean
	 *
	 * @param quoteGsc
	 * @param slaDetailBeans
	 */
	private void createSlaDetails(QuoteGsc quoteGsc, List<SLaDetailsBean> slaDetailBeans) {
		for (SLaDetailsBean slaDetailsBean : slaDetailBeans) {
			final String slaName = slaDetailsBean.getName();

			if (GscSlaConstants.PACKET_DROP.getName().equalsIgnoreCase(slaName)) {
				persistSlaDetail(quoteGsc, slaDetailsBean.getSlaValue(), GscSlaConstants.PACKET_DROP.getName());
			}

			if (GscSlaConstants.JITTER_SERVICE_LEVEL_TGT.getName().equalsIgnoreCase(slaName)) {
				persistSlaDetail(quoteGsc, slaDetailsBean.getSlaValue(),
						GscSlaConstants.JITTER_SERVICE_LEVEL_TGT.getName());
			}

			if (GscSlaConstants.LATENCY.getName().equalsIgnoreCase(slaName)) {
				persistSlaDetail(quoteGsc, slaDetailsBean.getSlaValue(), GscSlaConstants.LATENCY.getName());
			}

			if (GscSlaConstants.POST_DIAL_DELAY.getName().equalsIgnoreCase(slaName)) {
				persistSlaDetail(quoteGsc, slaDetailsBean.getSlaValue(), GscSlaConstants.POST_DIAL_DELAY.getName());
			}

			if (GscSlaConstants.SERVICE_UPTIME.getName().equalsIgnoreCase(slaName)) {
				persistSlaDetail(quoteGsc, slaDetailsBean.getSlaValue(), GscSlaConstants.SERVICE_UPTIME.getName());
			}
		}
	}

	/**
	 * Persist SLA Detail
	 *
	 * @param quoteGsc
	 * @param slaValue
	 * @param slaAttribute
	 */
	private void persistSlaDetail(final QuoteGsc quoteGsc, final String slaValue, final String slaAttribute) {
		SlaMaster slaMaster = slaMasterRepository.findBySlaName(slaAttribute);
		if (Objects.nonNull(slaMaster)) {
			final List<QuoteGscSla> quoteGscSlas = quoteGscSlaRepository.findByQuoteGscAndSlaMaster(quoteGsc,
					slaMaster);

			if (Objects.isNull(quoteGscSlas) || quoteGscSlas.isEmpty()) {
				saveSlaDetail(new QuoteGscSla(), quoteGsc, slaMaster, slaValue);
			} else {
				quoteGscSlas.forEach(quoteGscSla -> saveSlaDetail(quoteGscSla, quoteGsc, slaMaster, slaValue));
			}
		}
	}

	/**
	 * Save Sla Details
	 *
	 * @param quoteGscSla
	 * @param quoteGsc
	 * @param slaMaster
	 * @param slaValue
	 */
	private void saveSlaDetail(final QuoteGscSla quoteGscSla, final QuoteGsc quoteGsc, final SlaMaster slaMaster,
			final String slaValue) {
		quoteGscSla.setQuoteGsc(quoteGsc);
		quoteGscSla.setSlaMaster(slaMaster);
		// TODO : Need to verify this
		quoteGscSla.setAttributeName(slaValue);
		quoteGscSla.setAttributeValue(slaValue);
		quoteGscSlaRepository.save(quoteGscSla);
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
		if (StringUtils.isNotBlank( response)) {
			gscSlaBeanListener = GscUtils.fromJson(response, GscSlaBeanListener.class);
		}
		return gscSlaBeanListener;
	}
}