package com.tcl.dias.batch.izosdwan.service.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.ByonBulkUploadDetail;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIzoSdwanAttributeValues;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanByonUploadDetail;
import com.tcl.dias.oms.entity.repository.QuoteIzoSdwanAttributeValuesRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzosdwanByonUploadDetailRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;

/**
 * 
 * This is the service class for IZOSDWAN in batch
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class IzosdwanBatchService {
	@Autowired
	QuoteIzosdwanByonUploadDetailRepository quoteIzosdwanByonUploadDetailRepository;
	
	@Autowired
	QuoteRepository quoteRepository;
	
	@Autowired
	QuoteIzoSdwanAttributeValuesRepository quoteIzoSdwanAttributeValuesRepository;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IzosdwanBatchService.class);

	public void updateLocationDetailsForByonInOms(List<ByonBulkUploadDetail> byonBulkUploadDetails) {
		LOGGER.info("Inside updateLocationDetailsForByonInOms ");
		List<QuoteIzosdwanByonUploadDetail> quoteIzosdwanByonUploadDetails = new ArrayList<QuoteIzosdwanByonUploadDetail>();
		if (byonBulkUploadDetails != null && !byonBulkUploadDetails.isEmpty()) {
			byonBulkUploadDetails.stream().forEach(byon -> {
				Optional<QuoteIzosdwanByonUploadDetail> quoteIzosdwanByonUploadDetail = quoteIzosdwanByonUploadDetailRepository
						.findById(byon.getId());
				if (quoteIzosdwanByonUploadDetail.isPresent()) {

					if (byon.getLocationId() != null) {
						quoteIzosdwanByonUploadDetail.get().setLocationId(byon.getLocationId());
					}
					if (byon.getLatLong() != null) {
						quoteIzosdwanByonUploadDetail.get().setLatLong(byon.getLatLong());
						quoteIzosdwanByonUploadDetail.get().setStatus(IzosdwanCommonConstants.CLOSED);
					} else {
						if (byon.getRetriggerCount() == 2) {
							quoteIzosdwanByonUploadDetail.get().setStatus(IzosdwanCommonConstants.FAILURE);
							quoteIzosdwanByonUploadDetail.get()
									.setLocationErrorDetails(byon.getLocationErrorDetails());
						} else {
							if(StringUtils.isNotBlank(quoteIzosdwanByonUploadDetail.get().getErrorDetails())) {
								quoteIzosdwanByonUploadDetail.get().setStatus(IzosdwanCommonConstants.CLOSED);
							}else {
								quoteIzosdwanByonUploadDetail.get().setStatus(IzosdwanCommonConstants.OPEN);
								quoteIzosdwanByonUploadDetail.get()
								.setRetriggerCount(quoteIzosdwanByonUploadDetail.get().getRetriggerCount() + 1);
							}
							quoteIzosdwanByonUploadDetail.get()
									.setLocationErrorDetails(byon.getLocationErrorDetails());
						}
					}
					quoteIzosdwanByonUploadDetails.add(quoteIzosdwanByonUploadDetail.get());
				}
			});
			if (quoteIzosdwanByonUploadDetails != null && !quoteIzosdwanByonUploadDetails.isEmpty()) {
				quoteIzosdwanByonUploadDetailRepository.saveAll(quoteIzosdwanByonUploadDetails);
			}
		}
		updateInQuoteIfUploadIsDone();
	}
	
	private void updateInQuoteIfUploadIsDone() {
		LOGGER.info("Inside updateInQuoteIfUploadIsDone");
		List<Integer> quoteIds = quoteIzosdwanByonUploadDetailRepository.getUploadSuccessQuotes();
		LOGGER.info("Got quote ids {}");
		if (quoteIds != null && !quoteIds.isEmpty()) {
			LOGGER.info("Got quote ids {}", quoteIds.toString());
			List<QuoteIzoSdwanAttributeValues> list = new ArrayList<>();
			quoteIds.stream().forEach(id -> {
				Quote quote = quoteRepository.findByIdAndStatus(id, CommonConstants.BACTIVE);
				if (quote != null) {
					List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
							.findByDisplayValueAndQuote(IzosdwanCommonConstants.BYON_INTERNET, quote);
					if (quoteIzoSdwanAttributeValues != null && !quoteIzoSdwanAttributeValues.isEmpty() && quoteIzoSdwanAttributeValues.get(0).getAttributeValue()!=null && quoteIzoSdwanAttributeValues.get(0).getAttributeValue().equalsIgnoreCase("false")) {
						LOGGER.info("Got attribute details for quote {}", quote.getId());
						quoteIzoSdwanAttributeValues.get(0).setAttributeValue("true");
						list.add(quoteIzoSdwanAttributeValues.get(0));
					}
				}
			});
			if (list != null && !list.isEmpty()) {
				quoteIzoSdwanAttributeValuesRepository.saveAll(list);
			}
			List<QuoteIzosdwanByonUploadDetail> quoteIzosdwanByonUploadDetails = quoteIzosdwanByonUploadDetailRepository.findByQuote_idIn(quoteIds);
			updateStatus(IzosdwanCommonConstants.COMPLETED, quoteIzosdwanByonUploadDetailRepository, quoteIzosdwanByonUploadDetails);
		}
	}
	
	private void updateStatus(String toStatus,
			QuoteIzosdwanByonUploadDetailRepository quoteIzosdwanByonUploadDetailRepository,
			List<QuoteIzosdwanByonUploadDetail> quoteIzosdwanByonUploadDetails) {
		LOGGER.info("Updating status for {} with status {}", quoteIzosdwanByonUploadDetails, toStatus);
		if (quoteIzosdwanByonUploadDetails != null && !quoteIzosdwanByonUploadDetails.isEmpty()) {
			List<QuoteIzosdwanByonUploadDetail> quoteIzosdwanByonUploadDetails2 = new ArrayList<>();
			quoteIzosdwanByonUploadDetails.stream().forEach(entity -> {
				entity.setStatus(toStatus);
				quoteIzosdwanByonUploadDetails2.add(entity);
			});
			quoteIzosdwanByonUploadDetailRepository.saveAll(quoteIzosdwanByonUploadDetails2);
		}
	}
}
