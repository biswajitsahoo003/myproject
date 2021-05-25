package com.tcl.dias.products.gsc.consumer.v1;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.gsc.beans.GscCountrySpecificDocumentBean;
import com.tcl.dias.common.gsc.beans.GscCountrySpecificDocumentListener;
import com.tcl.dias.common.gsc.beans.GscDocumentsByProductAndCountryRequest;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.products.constants.ExceptionConstants;
import com.tcl.dias.products.gsc.service.v1.GscCountrySpecificDocumentListService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Service contains listeners related to GscCountrySpecificDocumentListe View
 * 
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */
@Service
public class GscCountrySpecificDocumentListListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(GscCountrySpecificDocumentListListener.class);

	@Autowired
	GscCountrySpecificDocumentListService gscCountrySpecificDocumentListService;

	/**
	 * Get documentId by document name
	 *
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${country.specific.file.queue}") })
	public String processCountrySpecificDocument(String request) throws TclCommonException {
		String documentID = "";
		try {
			if (StringUtils.isBlank(request)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			GscCountrySpecificDocumentListener documentBean = (GscCountrySpecificDocumentListener) Utils
					.convertJsonToObject(request,
							GscCountrySpecificDocumentListener.class);
			documentID = gscCountrySpecificDocumentListService.processDocumentName(documentBean);
		} catch (Exception e) {
			LOGGER.warn("Error in process document Name ", e);
		}
		return documentID!=null?documentID:"";
	}

	/**
	 * Fetch list of applicable documents by product name and country combination
	 * 
	 * @param mqRequest
	 * @return
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.gsc.country.specific.documents.queue}") })
	public String getDocumentsByProductAndCountry(String mqRequest) {
		try {
			GscDocumentsByProductAndCountryRequest request = (GscDocumentsByProductAndCountryRequest) Utils
					.convertJsonToObject(mqRequest,
							GscDocumentsByProductAndCountryRequest.class);
			List<GscCountrySpecificDocumentBean> documents = gscCountrySpecificDocumentListService
					.getDocumentsForProductAndCountry(request.getProductName(), request.getIso3CountryCode());
			return Utils.convertObjectToJson(documents);
		} catch (Exception e) {
			LOGGER.warn("Error occurred while fetching document list for country and product", e);
			return "";
		}
	}
}
