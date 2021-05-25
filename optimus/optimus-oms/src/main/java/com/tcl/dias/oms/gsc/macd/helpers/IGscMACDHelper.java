package com.tcl.dias.oms.gsc.macd.helpers;

import java.util.List;
import java.util.Map;

import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.gsc.macd.MACDOrderResponse;

public interface IGscMACDHelper {

	Quote cloneQuoteFromConfiguration(String requestType, SIServiceDetailDataBean configuration,
			SIOrderDataBean orderDataBean, Map<String, Object> data);

	Order cloneOrderFromConfiguration(SIServiceDetailDataBean configuration, SIOrderDataBean orderData,
			Map<String, Object> data);

	List<OrderGscDetail> cloneConfiguration(SIServiceDetailDataBean configuration, SIOrderDataBean orderDataBean, Order newOrder, Map<String, Object> data);

	MACDOrderResponse handleAddCountryRequest(String requestType, SIServiceDetailDataBean detailDataBean,
			SIOrderDataBean orderDataBean, Map<String, Object> data);
}
