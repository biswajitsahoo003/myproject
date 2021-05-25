package com.tcl.dias.servicehandover.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.servicefulfillment.entity.entities.IpcChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicefulfillment.entity.repository.IpcChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.dias.servicehandover.constants.NetworkConstants;

@Component
public class IPCChargeLineItemsUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(IPCChargeLineItemsUtil.class);
	
	@Autowired
	ServicehandoverAuditRepository servicehandoverAuditRepository;
	
	@Autowired
	IpcChargeLineitemRepository ipcChargeLineitemRepository;
	
	private IpcChargeLineitem ipcChargeLineitemTemp;
	
	private String category;

	public Double addDoubleValueAndRoundOff(String valueOne, String valueTwo) {
		return round(Double.parseDouble(valueOne) + Double.parseDouble(valueTwo));
	}

	public static double round(double value) {
		return (double) Math.round(value * 100) / 100;
	}

	public String frameKeyUsingIpcChargeLineItemsForCommissioning(IpcChargeLineitem ipcChargeLineItem) {
		StringBuilder key = new StringBuilder("");
		key.append(ipcChargeLineItem.getChargeLineitem()).append(IpcConstants.UNDERSCORE);
		key.append(ipcChargeLineItem.getProductDescription()).append(IpcConstants.UNDERSCORE);
		key.append(ipcChargeLineItem.getDescription()).append(IpcConstants.UNDERSCORE);
		key.append(ipcChargeLineItem.getAccountNumber()).append(IpcConstants.UNDERSCORE);
		key.append(ipcChargeLineItem.getMrc()).append(IpcConstants.UNDERSCORE);
		key.append(ipcChargeLineItem.getNrc()).append(IpcConstants.UNDERSCORE);
		key.append(ipcChargeLineItem.getArc()).append(IpcConstants.UNDERSCORE);
		key.append(ipcChargeLineItem.getPpuRate()).append(IpcConstants.UNDERSCORE);
		key.append(ipcChargeLineItem.getPricingModel()).append(IpcConstants.UNDERSCORE);
		key.append(ipcChargeLineItem.getQuantity()).append(IpcConstants.UNDERSCORE);
		if(ipcChargeLineItem.getActionType().equals(NetworkConstants.TERMINATE)) {
			key.append(ipcChargeLineItem.getSourceProductSequence()).append(IpcConstants.UNDERSCORE);
		}
		if(ipcChargeLineItem.getPricingModel() != null && !ipcChargeLineItem.getPricingModel().equals(IpcConstants.PRICING_MODEL_RESERVED)) {
			key.append(ipcChargeLineItem.getCloudCode()).append(IpcConstants.UNDERSCORE);
		}
		return key.toString();
	}
	
	@Transactional
	public String updateLineItems(
			List<IpcChargeLineitem> ipcChargeLineItems) {
		Map<String, IpcChargeLineitem> ipcChargeLineItemMap = new HashMap<>();
		ipcChargeLineItems.forEach(ipcChargeLineItem -> {
			String lineItemKey = frameKeyUsingIpcChargeLineItemsForCommissioning(ipcChargeLineItem);
			if(ipcChargeLineItemMap.containsKey(lineItemKey)) {
				ipcChargeLineItem.setSourceProductSequence(ipcChargeLineItemMap.get(lineItemKey).getSourceProductSequence());
			} else {
				if(ipcChargeLineItem.getActionType().equals(NetworkConstants.CREATE) && Objects.isNull(ipcChargeLineItem.getSourceProductSequence())) {
					ServicehandoverAudit servicehandoverAudit = new ServicehandoverAudit();
					ipcChargeLineItem.setSourceProductSequence(servicehandoverAuditRepository.save(servicehandoverAudit).getId());
				}
			}
			if(ipcChargeLineItem.getActionType().equals(NetworkConstants.CREATE)) {
				ipcChargeLineItem.setCommissionedFlag(IpcConstants.Y);
			} else {
				ipcChargeLineItem.setTerminatedFlag(IpcConstants.Y);
			}
			ipcChargeLineItem.setStatus(NetworkConstants.IN_PROGRESS);
			ipcChargeLineitemRepository.save(ipcChargeLineItem);
			ipcChargeLineitemRepository.flush();
			ipcChargeLineItemMap.put(lineItemKey, ipcChargeLineItem);
		});
		if(!ipcChargeLineItems.isEmpty()) {
			return IpcConstants.SUCCESS;
		} else {
			return IpcConstants.NO_RECORD_FOUND;
		}
	}
	
	@Transactional
	public List<IpcChargeLineitem> formatLineItems(
			List<IpcChargeLineitem> ipcChargeLineItems) {
		Map<Integer, IpcChargeLineitem> ipcChargeLineItemMap = new HashMap<>();
		ipcChargeLineitemTemp = null;
		ipcChargeLineItems.forEach(ipcChargeLineItem -> {
			if(ipcChargeLineItemMap.containsKey(ipcChargeLineItem.getSourceProductSequence())) {
				ipcChargeLineitemTemp = ipcChargeLineItemMap.get(ipcChargeLineItem.getSourceProductSequence());
				ipcChargeLineitemTemp.setQuantity(String.valueOf(Integer.parseInt(ipcChargeLineItem.getQuantity())+Integer.parseInt(ipcChargeLineitemTemp.getQuantity())));
				ipcChargeLineitemTemp.setMrc(String.valueOf(addDoubleValueAndRoundOff(ipcChargeLineItem.getMrc(),ipcChargeLineitemTemp.getMrc())));
				ipcChargeLineitemTemp.setNrc(String.valueOf(addDoubleValueAndRoundOff(ipcChargeLineItem.getNrc(),ipcChargeLineitemTemp.getNrc())));
				ipcChargeLineitemTemp.setArc(String.valueOf(addDoubleValueAndRoundOff(ipcChargeLineItem.getArc(),ipcChargeLineitemTemp.getArc())));
			} else {
				ipcChargeLineitemTemp = new IpcChargeLineitem(ipcChargeLineItem.getAccountNumber(), ipcChargeLineItem.getArc(), ipcChargeLineItem.getChargeLineitem(), ipcChargeLineItem.getDescription(),
						ipcChargeLineItem.getMrc(), ipcChargeLineItem.getNrc(), ipcChargeLineItem.getPpuRate(), ipcChargeLineItem.getPricingModel(), ipcChargeLineItem.getProductDescription(), 
						ipcChargeLineItem.getServiceId(), ipcChargeLineItem.getServiceType(), ipcChargeLineItem.getBillingMethod(), ipcChargeLineItem.getUnitOfMeasurement(), ipcChargeLineItem.getQuantity(), 
						ipcChargeLineItem.getIsProrated(), ipcChargeLineItem.getComponent(), ipcChargeLineItem.getCpeModel(), ipcChargeLineItem.getHsnCode(), ipcChargeLineItem.getServiceCode(), 
						ipcChargeLineItem.getMigParentServiceCode(), ipcChargeLineItem.getCloudCode(), ipcChargeLineItem.getParentCloudCode(), ipcChargeLineItem.getAdditionalParam(), ipcChargeLineItem.getVersion(), 
						ipcChargeLineItem.getActionType(), ipcChargeLineItem.getStatus(), ipcChargeLineItem.getCommissionedFlag(), ipcChargeLineItem.getTerminatedFlag(), ipcChargeLineItem.getScenarioType());
				ipcChargeLineitemTemp.setSourceProductSequence(ipcChargeLineItem.getSourceProductSequence());
			}
			ipcChargeLineItemMap.put(ipcChargeLineItem.getSourceProductSequence(), ipcChargeLineitemTemp);
		});
		ipcChargeLineItemMap.entrySet().forEach(entry -> LOGGER.info(" Key: {}, value: {}", entry.getKey(), entry.getValue()));
		return new ArrayList<IpcChargeLineitem>(ipcChargeLineItemMap.values());
	}

	@Transactional
	public void checkAndUpdateLineItemsIfSameAccountChoosenForMACD(String serviceId, String serviceCode, String serviceType) {
		
		List<IpcChargeLineitem> terminateLineItemL = ipcChargeLineitemRepository.findByServiceCodeAndServiceTypeAndActionTypeAndStatusAndTerminatedFlag(serviceCode, serviceType, IpcConstants.TERMINATE, IpcConstants.IN_PROGRESS, IpcConstants.Y);
		List<IpcChargeLineitem> createLineItemL = ipcChargeLineitemRepository.findByServiceCodeAndServiceTypeAndActionTypeAndStatusAndCommissionedFlag(serviceCode, serviceType, IpcConstants.CREATE, IpcConstants.NEW, IpcConstants.N);
		if(!terminateLineItemL.isEmpty() && !createLineItemL.isEmpty()) {
			if(terminateLineItemL.stream().findAny().get().getAccountNumber().equals(createLineItemL.stream().findAny().get().getAccountNumber())) {
				LOGGER.info("Account Number is Same");
				Map<Integer, Integer> lineItemsToBeCreatedSrcProdSeqVsQuantityMap = new HashMap<>();
				Set<String> createLineItemsWithNoSrcProdSeqKeySet= new HashSet<>();
				createLineItemL.forEach(lineItem -> {
					if(lineItem.getSourceProductSequence() != null) {
						Integer quantity = Integer.parseInt(lineItem.getQuantity());
						if(lineItemsToBeCreatedSrcProdSeqVsQuantityMap.containsKey(lineItem.getSourceProductSequence())) {
							quantity += lineItemsToBeCreatedSrcProdSeqVsQuantityMap.get(lineItem.getSourceProductSequence());
						}
						lineItemsToBeCreatedSrcProdSeqVsQuantityMap.put(lineItem.getSourceProductSequence(), quantity);
					} else {
						createLineItemsWithNoSrcProdSeqKeySet.add(frameKeyUsingIpcChargeLineItemsForCommissioning(lineItem));
					}
				});
				
				Map<Integer, Integer> lineItemsToBeTerminatedSrcProdSeqVsQuantityMap = new HashMap<>();
				terminateLineItemL.stream().filter(x -> lineItemsToBeCreatedSrcProdSeqVsQuantityMap.containsKey(x.getSourceProductSequence())).forEach(lineItem -> {
					Integer quantity = Integer.parseInt(lineItem.getQuantity());
					if(lineItemsToBeTerminatedSrcProdSeqVsQuantityMap.containsKey(lineItem.getSourceProductSequence())) {
						quantity += lineItemsToBeTerminatedSrcProdSeqVsQuantityMap.get(lineItem.getSourceProductSequence());
					}
					lineItemsToBeTerminatedSrcProdSeqVsQuantityMap.put(lineItem.getSourceProductSequence(), quantity);
				});
				
				createLineItemL.stream().filter(x -> x.getSourceProductSequence() != null).forEach(lineItem -> {
					if (createLineItemsWithNoSrcProdSeqKeySet.contains(frameKeyUsingIpcChargeLineItemsForCommissioning(lineItem))) {
						lineItemsToBeCreatedSrcProdSeqVsQuantityMap.remove(lineItem.getSourceProductSequence());
						lineItemsToBeTerminatedSrcProdSeqVsQuantityMap.remove(lineItem.getSourceProductSequence());
					}
				});
				
				lineItemsToBeTerminatedSrcProdSeqVsQuantityMap.entrySet().stream().filter(x -> (lineItemsToBeCreatedSrcProdSeqVsQuantityMap.containsKey(x.getKey()) 
																									&& x.getValue() == lineItemsToBeCreatedSrcProdSeqVsQuantityMap.get(x.getKey()))).forEach(sourceProdSeq -> {
					createLineItemL.stream().filter(createLI -> (sourceProdSeq.getKey().equals(createLI.getSourceProductSequence()))).forEach(crLineItem -> {
						crLineItem.setStatus(IpcConstants.SUCCESS);
						crLineItem.setCommissionedFlag(IpcConstants.Y);
						ipcChargeLineitemRepository.saveAndFlush(crLineItem);
					});
					terminateLineItemL.stream().filter(terminateLI -> (sourceProdSeq.getKey().equals(terminateLI.getSourceProductSequence()))).forEach(trLineItem -> {
						trLineItem.setStatus(IpcConstants.SUCCESS);
						trLineItem.setTerminatedFlag(IpcConstants.Y);
						ipcChargeLineitemRepository.saveAndFlush(trLineItem);
					});
				});
				
				createLineItemL.stream().filter(x -> (x.getStatus().equals(IpcConstants.NEW) && x.getSourceProductSequence() != null)).forEach(lineItem -> {
					lineItem.setSourceProductSequence(null);
				});
			} else {
				createLineItemL.stream().filter(x -> (x.getStatus().equals(IpcConstants.NEW) && x.getSourceProductSequence() != null)).forEach(lineItem -> {
					lineItem.setSourceProductSequence(null);
				});
			}
			ipcChargeLineitemRepository.saveAll(createLineItemL);
			ipcChargeLineitemRepository.saveAll(terminateLineItemL);
		}
	}
	
	public String getCategory(List<IpcChargeLineitem> chargeLineitems) {
		category = null;
		chargeLineitems.stream().filter(x -> x.getChargeLineitem().contains(IpcConstants.VM)).forEach(lineItem -> {
			if ( category == null && lineItem.getDescription().contains(IpcConstants.UNMANAGED_VM)) {
				category = IpcConstants.UNMANAGED;
			}
		});
		return category != null ? category : IpcConstants.FULLY_MANAGED;
	}
	
}