package com.tcl.dias.servicehandover.ipc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.flowable.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.servicefulfillment.entity.entities.IpcChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.IpcChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.repository.GnvOrderEntryTabRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class IpcBillingManualService {
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	GnvOrderEntryTabRepository gnvOrderEntryTabRepository;
	
	@Autowired
	IpcChargeLineitemRepository ipcChargeLineitemRepository;

	@Autowired
	BillingHandoverService billingHandoverService;

	@Autowired
	TaskRepository taskRepository;
	
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	ScOrderRepository scOrderRepository;
	
	@Value("${oms.o2c.macd.queue}")
	String omsO2CMacdQueue;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	IpcBillingAccountAndLineItemService ipcBillingAccountAndLineItemService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IpcBillingManualService.class);
	
	@Transactional(isolation = Isolation.DEFAULT)
	public String closeAccountAsync(String serviceCode) {
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findLatestServiceDetailbyServiceCode(serviceCode);
		if(scServiceDetail!=null) {
			String accountNumber = gnvOrderEntryTabRepository.findAccountNumberByInputGroupIdRegex("%"+IpcConstants.IPC+IpcConstants.UNDERSCORE+scServiceDetail.getId()+IpcConstants.UNDERSCORE+"%");
			LOGGER.info("ServiceCode: {}, ServiceId: {}, AccountNumber: {}", scServiceDetail.getUuid(), scServiceDetail.getId(), accountNumber);
			if(accountNumber!=null) {
				ipcChargeLineitemRepository.updateAccountNumber(accountNumber, scServiceDetail.getId().toString(), "IPC");
				Map<String, String> attrMap = new HashMap<>();
				attrMap.put(IpcConstants.IPC_BILLING_ACCOUNT_NUMBER, accountNumber);
				billingHandoverService.updateServiceAttributes(scServiceDetail.getId(), attrMap);
				List<Task> task = taskRepository.findByServiceIdAndMstTaskDef_key(scServiceDetail.getId(), "ipc_billing_account_creation_async");
				return task.get(0).getWfTaskId();
			}
		}
		return null;
	}
	
	@Transactional(isolation = Isolation.DEFAULT)
	public String closeOrderAsync(String serviceCode, String actionType, String mstKey) {
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findLatestServiceDetailbyServiceCode(serviceCode);
		if(scServiceDetail!=null) {
			List<IpcChargeLineitem> ipcChargeLineItemL = ipcChargeLineitemRepository.findByServiceCodeAndActionTypeAndStatus(serviceCode, actionType, NetworkConstants.IN_PROGRESS);
			LOGGER.info("ServiceCode: {}, ServiceId: {}, ActionType: {}", scServiceDetail.getUuid(), scServiceDetail.getId(), actionType);
			ipcChargeLineItemL.stream().forEach(ipcChargeLineItem -> {
				String status = gnvOrderEntryTabRepository.findStatusByServiceCodeAndSourceProdSeqAndActionType(serviceCode,ipcChargeLineItem.getSourceProductSequence(), actionType);
				LOGGER.info("SourceProdSeq: {}, Status: {}", ipcChargeLineItem.getSourceProductSequence(), status);
				ipcChargeLineItem.setStatus(status);
			});
			ipcChargeLineitemRepository.saveAll(ipcChargeLineItemL);
			List<Task> task = taskRepository.findByServiceIdAndMstTaskDef_key(scServiceDetail.getId(), mstKey);
			if(actionType.equals(NetworkConstants.CREATE)) {
        		ScOrder scOrder= scOrderRepository.findByOpOrderCodeAndIsActive(scServiceDetail.getScOrderUuid(), NetworkConstants.Y);
				if(scOrder!=null) {
					scServiceDetail.setServiceStatus(IpcConstants.COMPLETED);
					scServiceDetailRepository.save(scServiceDetail);
					ipcBillingAccountAndLineItemService.triggerPayPerUseCommissionedInfoToCatalyst(scServiceDetail);
					try {
						mqUtils.send(omsO2CMacdQueue, scServiceDetail.getScOrderUuid()+IpcConstants.SPECIAL_CHARACTER_COMMA+scServiceDetail.getPopSiteCode());
					}catch (Exception ex) {
						LOGGER.error("Exception while trigerring mqutils call for updating the stage of macd order to MACD_ORDER_COMMISSIONED");
					}
				}
        	}
			return task.get(0).getWfTaskId();
		}
		return null;
	}
	
	public String closeWpsCall(String process, String serviceCode) {
		String wfId = null;
		if(process.equalsIgnoreCase("account")) {
			wfId = closeAccountAsync(serviceCode);
		} else if(process.equalsIgnoreCase("terminateProduct")) {
			wfId = closeOrderAsync(serviceCode, NetworkConstants.TERMINATE, "ipc-product-termination-async");
		} else if(process.equalsIgnoreCase("createProduct")) {
			wfId = closeOrderAsync(serviceCode, NetworkConstants.CREATE, "ipc_product_commissioning_async");
		}
		if( wfId!= null) {
			runtimeService.trigger(wfId);
			return "SUCCESS";
		}
		return "FAILURE";
	}
	
	public String isOrderPresent( String orderCode, String sourceProdSeq) {
		ScServiceDetail scServiceDetail = null;
		List<IpcChargeLineitem> chrgeLineItmL = ipcChargeLineitemRepository.findByServiceTypeAndSourceProductSequence(IpcConstants.IPC, Integer.parseInt(sourceProdSeq));
		if(!chrgeLineItmL.isEmpty()) {
			scServiceDetail = scServiceDetailRepository.findByScOrderUuidAndId(orderCode, Integer.parseInt(chrgeLineItmL.get(0).getServiceId()));
		}
        if(scServiceDetail != null) {
			return "SUCCESS";
		}
		return "FAILURE";
	}
	
	public String saveLineItems(Integer taskId, String accountNumber) throws TclCommonException {
		ipcBillingAccountAndLineItemService.saveBillingLineItems(taskId, accountNumber);
		return "SUCCESS";
	}

}
