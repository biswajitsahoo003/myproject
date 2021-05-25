package com.tcl.dias.servicehandover.ipc.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.dias.servicefulfillmentutils.utils.FulfillmentUtils;
import com.tcl.dias.servicehandover.util.TimeStampUtil;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class IpcDataUpdateService {

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;

	@Value("${rabbitmq.si.o2c.update.commission.date.queue}")
	String srvInvCommissionDateUpdate;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	IpcBillingAccountAndLineItemService ipcBillingAccountAndLineItemService;

	private static final Logger LOGGER = LoggerFactory.getLogger(IpcDataUpdateService.class);

	@Transactional(isolation = Isolation.DEFAULT)
	public String updateCommissionDateAndDeliveryDate(String serviceCode, String orderCode, Map<String, Object> inputM)
			throws TclCommonException {
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndScOrderUuid(serviceCode, orderCode);
		if (null == scServiceDetail) {
			return "Invalid Order Code.";
		}
		inputM.entrySet().forEach(m -> {
			if (IpcConstants.ATTRIBUTE_COMMISSIONING_DATE.equals(m.getKey())) {
				Timestamp commDate = TimeStampUtil.formatStringToDate(String.valueOf(m.getValue()));
				scServiceDetail.setCommissionedDate(commDate);
				scServiceDetail.setServiceCommissionedDate(commDate);
				scServiceDetail.setActualDeliveryDate(commDate);
				ScServiceAttribute billStartDateAttr = scServiceAttributeRepository
						.findByScServiceDetail_idAndAttributeName(scServiceDetail.getId(),
								IpcConstants.BILL_START_DATE);
				scServiceDetailRepository.save(scServiceDetail);
				if (null != billStartDateAttr) {
					ScServiceAttribute billFreePeriodAttr = scServiceAttributeRepository
							.findByScServiceDetail_idAndAttributeName(scServiceDetail.getId(),
									IpcConstants.BILL_FREE_PERIOD);
					String billStartDate = FulfillmentUtils.formatWithTimeStampForCommPlusDays(
							scServiceDetail.getServiceCommissionedDate().toString(),
							StringUtils.isNoneBlank(billFreePeriodAttr.getAttributeValue())
									? Integer.valueOf(billFreePeriodAttr.getAttributeValue())
									: 0);
					billStartDateAttr.setAttributeValue(billStartDate);
					billStartDateAttr.setAttributeAltValueLabel(billStartDate);
					scServiceAttributeRepository.save(billStartDateAttr);
					try {
						Map<String, Object> requestParam = new HashMap<>();
						requestParam.put("serviceCode", scServiceDetail.getUuid());
						requestParam.put("orderCode", scServiceDetail.getScOrderUuid());
						requestParam.put("commissionDate", m.getValue());
						mqUtils.sendAndReceive(srvInvCommissionDateUpdate, Utils.convertObjectToJson(requestParam));
					} catch (Exception ex) {
						LOGGER.error(
								"Exception while trigerring mqutils call for updating the stage of macd order to MACD_ORDER_COMMISSIONED");
					}
				}
			}
		});
		return "SUCCESS";
	}
}