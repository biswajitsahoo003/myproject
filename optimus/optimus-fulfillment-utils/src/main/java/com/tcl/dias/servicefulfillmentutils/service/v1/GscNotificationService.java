package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Splitter;
import com.tcl.dias.common.beans.MailNotificationRequest;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.custom.GscScAsset;
import com.tcl.dias.servicefulfillment.entity.custom.GscScAssetReserved;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.PlaceOrderDetails;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.PlaceOrderSupplierBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.PlaceOrderToSupplierBean;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
public class GscNotificationService {
	private static final Logger logger = LoggerFactory.getLogger(GscNotificationService.class);

	@Value("${notification.template.placeordertosupplier}")
	String notifyPlaceOrderToSupplierTemplate;

	@Value("${notification.mail.queue}")
	String notificationMailQueue;

	@Autowired
	ScAssetRepository scAssetRepository;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	ScAssetAttributeRepository scAssetAttributeRepository;
	
	@Autowired
	GscService gscService;

	public PlaceOrderToSupplierBean notifyPlaceOrderToSupplier(String email, String serviceCode, Integer gscFlowGroupId) {
		logger.info("Inside notifyPlaceOrderToSupplier method");
		PlaceOrderToSupplierBean placeOrderToSupplierBean = new PlaceOrderToSupplierBean();
		Map<String,String> mailInfo = new HashMap<>();
		Map<String, String> scComponentAttributesmap = new HashMap<>();

		try {
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
			
			placeOrderToSupplierBean.setServiceID(scServiceDetail.getUuid());
			placeOrderToSupplierBean.setCustomerName(scServiceDetail.getScOrder().getErfCustSpLeName());
			placeOrderToSupplierBean.setDueDate("ASAP");
			
			Integer serviceId = scServiceDetail.getId();
			
			List<GscScAssetReserved> gscAssetsAll = scAssetRepository.findByServiceIdandAssetId(serviceId, gscFlowGroupId);
			HashMap<String, List<GscScAssetReserved>> suppliers = new HashMap<>();
			gscAssetsAll.forEach(gscAsset -> {
				if(suppliers.get(gscAsset.getSupplierId()) == null) {
					suppliers.put(gscAsset.getSupplierId(), new ArrayList<GscScAssetReserved>());
				}
				suppliers.get(gscAsset.getSupplierId()).add(gscAsset);
			});
			
			//List<String> supplierIds = scAssetAttributeRepository.findSupplierIdsByServiceId(serviceId);
			
			for(Entry<String, List<GscScAssetReserved>> entry : suppliers.entrySet()) {
				
				List<GscScAssetReserved> gscAssets = entry.getValue();
				
				if (Objects.nonNull(gscAssets)) {
				//	scComponentAttributesmap = commonFulfillmentUtils.getComponentAttributes(serviceId, "LM", "A");
					scComponentAttributesmap = commonFulfillmentUtils.getComponentAndAdditionalAttributes(serviceId, "LM", "A");
					List<PlaceOrderDetails> placeOrderDtlsList = getplaceOrderDtlsList(gscAssets,scComponentAttributesmap,scServiceDetail);
					placeOrderToSupplierBean.setOrderDtls(placeOrderDtlsList);
				}
				
				List<PlaceOrderDetails> placeOrderDetails = placeOrderToSupplierBean.getOrderDtls();
				if(Objects.nonNull(placeOrderDetails) && !placeOrderDetails.isEmpty()) {
					mailInfo = getMailInfo(entry.getKey(), scComponentAttributesmap);
					HashMap<String, Object> map = new HashMap<>();
					
					List<String> toAddresses = new ArrayList<>();
					List<String> toAddrList = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(mailInfo.get(GscConstants.TO_ADDR));
					toAddresses.addAll(toAddrList);
					
					List<String> ccAddresses = new ArrayList<>();
					List<String> ccAddrList = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(mailInfo.get(GscConstants.CC_ADDR));
					ccAddresses.addAll(ccAddrList);

					map.put("placeOrderDetails", placeOrderToSupplierBean);

					MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
					mailNotificationRequest.setFrom(mailInfo.get(GscConstants.FROM_ADDR));


					mailNotificationRequest.setCc(ccAddresses);
					mailNotificationRequest.setSubject("Place Order to supplier");
					mailNotificationRequest.setTemplateId(notifyPlaceOrderToSupplierTemplate);
					mailNotificationRequest.setTo(toAddresses);
					mailNotificationRequest.setVariable(map);
					logger.info("MDC Filter token value in before Queue call notifyPlaceOrderToSupplier {} and map value {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
					mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
				}else {
					logger.info("No Routing Details Found..!!!");
				}
			}

		} catch (Exception e) {
			logger.error("notifyPlaceOrdertoSupplierException for service Code {}. Exception occured : {} ",
					serviceCode, e);
		}
		
		logger.info("Exit notifyPlaceOrderToSupplier method");
		return placeOrderToSupplierBean;
	}

	
	
	private Map<String, String> getMailInfo(String supplierId, Map<String, String> scComponentAttributesmap) throws TclCommonException {
		
		String placeOrderResponse = scComponentAttributesmap.get(GscConstants.PLACE_ORDER_RESPONSE);
		Map<String,String> mailInfo = new LinkedHashMap<>();
		if(Objects.nonNull(scComponentAttributesmap.get(GscConstants.PLACE_ORDER_RESPONSE))) {
			List<PlaceOrderSupplierBean> supplierDetails = Utils.fromJson(placeOrderResponse, new TypeReference<List<PlaceOrderSupplierBean>>() {});
			supplierDetails.stream().filter(placeOrderSupplierBean -> supplierId.equals(String.valueOf(placeOrderSupplierBean.getSupplierId())))
				.forEach(placeOrderSupplierBean -> {
					mailInfo.put(GscConstants.FROM_ADDR, placeOrderSupplierBean.getEmailFrom());
					mailInfo.put(GscConstants.TO_ADDR, placeOrderSupplierBean.getEmailTo());
					mailInfo.put(GscConstants.CC_ADDR, placeOrderSupplierBean.getEmailCc());
				});
		}
		
		return mailInfo;
	}



	private List<PlaceOrderDetails> getplaceOrderDtlsList(List<GscScAssetReserved> gscAssets, Map<String, String> scComponentAttributesmap,ScServiceDetail scServiceDetail) {
		List<PlaceOrderDetails> placeOrderDtlsList = new LinkedList<>();
		
		for (GscScAsset gscAsset : gscAssets) {
				
			PlaceOrderDetails placeOrderDetails = new PlaceOrderDetails();
			
			if (GscConstants.YES.equalsIgnoreCase(gscAsset.getIsPortingNum())) {
				placeOrderDetails.setRequestType("Porting");
			} else {
				placeOrderDetails.setRequestType("New");
			}
			
			String callType = "";
			List<String> callTypesList = gscService.getRepcCallTypeList(scServiceDetail.getId());
			if(Objects.nonNull(callTypesList))
				callType = callTypesList.stream().collect(Collectors.joining(","));
			
			placeOrderDetails.setCallType(callType);
			placeOrderDetails.setCountry(scServiceDetail.getSourceCountry());
			placeOrderDetails.setTollFreeNumber(gscAsset.getTollfreeName());
			placeOrderDetails.setRoutingNumber(gscAsset.getRoutingName().replace("N/A", ""));
			Set<ScServiceAttribute> serviceAttributes = scServiceDetail.getScServiceAttributes();
			Optional<ScServiceAttribute> serviceAttr = serviceAttributes.stream().filter(servAttr -> "serviceType".equalsIgnoreCase(servAttr.getAttributeName())).findFirst();
			placeOrderDetails.setProductType(serviceAttr.get().getAttributeValue());
			placeOrderDtlsList.add(placeOrderDetails);
		}
		return placeOrderDtlsList;
	}

}
