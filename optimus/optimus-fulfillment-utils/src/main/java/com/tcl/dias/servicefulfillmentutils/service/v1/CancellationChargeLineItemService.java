package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.servicefulfillment.entity.entities.ScChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.ScChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.constants.BillingConstants;

/**
 * @author ramalingam
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * used for the document related service
 */
@Service
public class CancellationChargeLineItemService {
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	TaskRepository taskRepository;
	
	@Autowired
	ScChargeLineitemRepository scChargeLineitemRepository;

	@Transactional
	public String loadLineItems(String orderCode, String serviceCode, Integer serviceId) {
		Map<String, String> serviceTermMap = commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("effectiveDateOfChange", "cancellationReason", "cancellationCharges"), serviceId, "LM", "A");
		Task task = taskRepository.findFirstByOrderCodeAndServiceIdAndServiceCodeOrderByIdDesc(orderCode, serviceId, serviceCode);
		if (serviceTermMap != null && task != null) {
			ScChargeLineitem chargeLineitem = new ScChargeLineitem();
			chargeLineitem.setNrc(serviceTermMap.get("cancellationCharges"));
			chargeLineitem.setChargeLineitem("Cancellation Charges");
			chargeLineitem.setServiceId(task.getServiceId().toString());
			chargeLineitem.setServiceCode(task.getServiceCode());
			chargeLineitem.setServiceType(task.getServiceType());
			chargeLineitem.setBillingType(getBillingType(task.getServiceType()));
			chargeLineitem.setHsnCode(BillingConstants.HSN_CODE);
			chargeLineitem.setTermDate(serviceTermMap.get("effectiveDateOfChange"));
			chargeLineitem.setComponentDesc(serviceTermMap.get("cancellationReason"));
			scChargeLineitemRepository.save(chargeLineitem); 
		}
		scChargeLineitemRepository.flush();
		return Constants.SUCCESS;
	}

	@Transactional
	public void deleteExistingLineItems(Integer serviceId) {
		scChargeLineitemRepository.deleteLineItems(serviceId.toString());
	}
	
	private String getBillingType(String serviceType) {
		String billingType = serviceType;
		switch (serviceType) {
		case BillingConstants.NPL:
			billingType = BillingConstants.NPL_INTRACITY;
			break;
		case BillingConstants.UCAAS:
			billingType = BillingConstants.CISCO_WEBEX;
			break;
		case BillingConstants.IZO_SDWAN:
			billingType = BillingConstants.IZO_SDWAN;
			break;
		case BillingConstants.IZOSDWAN:
			billingType = BillingConstants.IZO_SDWAN;
			break;

		default:
			break;
		}
		return billingType;
	}
}
