package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.graphbuilder.math.func.LnFunction;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.servicefulfillment.beans.CpeBomResource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.GenevaIpcOrderEntry;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceCommercial;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicefulfillment.entity.repository.GenevaIpcOrderEntryRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceCommercialRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.beans.AccountInputData;
import com.tcl.dias.servicefulfillmentutils.beans.LineItemDetailsBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.BillingConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;

/**
 * @author yogesh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * used for the document related service
 */
@Service
@Transactional(readOnly = true,isolation=Isolation.READ_COMMITTED)
public class BillingChargeLineItemService {
	
	@Autowired
	ScServiceCommercialRepository scServiceCommercialRepository;
	
	@Autowired
	GenevaIpcOrderEntryRepository genevaIpcOrderEntryRepository;
	
	@Autowired
	ServicehandoverAuditRepository servicehandoverAuditRepository;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	boolean isCpeOutright = false;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	ScServiceAttributeRepository serviceAttributeRepository;
	
	@Autowired
	ScChargeLineitemRepository chargeLineitemRepository;
	
	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	LineItemDetailsBean lineItembean = null;
	
	@Autowired
	ScContractInfoRepository scContractInfoRepository;
	
	@Autowired
	ScOrderRepository scOrderRepository; 
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BillingChargeLineItemService.class);
	
	public List<LineItemDetailsBean> loadLineItems(Integer serviceId, CpeBomResource bomResource,String billingMethod) {	
		List<ScServiceCommercial> commercials = scServiceCommercialRepository.findByScServiceId(serviceId);
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
		String noOfAdditionalIps = scServiceDetail.getNoOfAdditionalIps();
		List<LineItemDetailsBean> lineItembeanList= new ArrayList<LineItemDetailsBean>();
		ScServiceAttribute serviceType = serviceAttributeRepository
				.findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(scServiceDetail.getId(), "Service type");
		commercials.forEach(lineItem->{
			Double nrc = lineItem.getNrc() != null ? lineItem.getNrc() : 0;
			Double mrc = lineItem.getMrc() != null ? lineItem.getMrc() : 0;
			Double arc = lineItem.getArc() != null ? lineItem.getArc() : 0;
			if((Objects.nonNull(lineItem.getComponentReferenceName())&& lineItem.getComponentReferenceName().equals("Last mile"))&& !lineItem.getReferenceName().contains("Mast")) {
			    lineItembean = new LineItemDetailsBean();
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setLineitem(BillingConstants.LOCAL_ACCESS_CHARGE);
				lineItembeanList.add(lineItembean);
				
			}
			if(((Objects.nonNull(lineItem.getComponentReferenceName())&& lineItem.getComponentReferenceName().equals("Last mile"))&& lineItem.getReferenceName().contains("Mast"))) {
			    lineItembean = new LineItemDetailsBean();
			    lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setLineitem(BillingConstants.MAST_CHARGE);
				lineItembeanList.add(lineItembean);
				
			}
			if(Objects.nonNull(lineItem.getComponentReferenceName())&& lineItem.getComponentReferenceName().equals("CPE")) {
				if(lineItem.getReferenceName().contains("Outright") && (arc>0 || mrc >0 || nrc>0)) {
					lineItembean = new LineItemDetailsBean();
					lineItembean.setMrc(String.format("%.2f", mrc));
					lineItembean.setNrc(String.format("%.2f", nrc));
					lineItembean.setArc(String.format("%.2f", arc));
					lineItembean.setLineitem(BillingConstants.CPE_OUTRIGHT_CHARGE);
					lineItembeanList.add(lineItembean);
				}
			}
			if(Objects.nonNull(lineItem.getComponentReferenceName())&& lineItem.getComponentReferenceName().equals("CPE")) {
				if(lineItem.getReferenceName().contains("Management") && (arc>0 || mrc >0 || nrc>0)) {
					lineItembean = new LineItemDetailsBean();
					lineItembean.setMrc(String.format("%.2f", mrc));
					lineItembean.setNrc(String.format("%.2f", nrc));
					lineItembean.setArc(String.format("%.2f", arc));
					lineItembean.setLineitem(BillingConstants.CPE_MANAGEMENT_CHARGE);
					lineItembeanList.add(lineItembean);
				}
			}
			if(Objects.nonNull(lineItem.getComponentReferenceName())&& lineItem.getComponentReferenceName().equals("CPE")) {
				if(lineItem.getReferenceName().contains("Install") && (arc>0 || mrc >0 || nrc>0)) {
					lineItembean = new LineItemDetailsBean();
					lineItembean.setMrc(String.format("%.2f", mrc));
					lineItembean.setNrc(String.format("%.2f", nrc));
					lineItembean.setArc(String.format("%.2f", arc));
					lineItembean.setLineitem(BillingConstants.CPE_INSTALLATION_CHARGE);
					lineItembeanList.add(lineItembean);
				}
			}
			if(Objects.nonNull(lineItem.getComponentReferenceName())&& lineItem.getComponentReferenceName().equals("CPE")) {
				if(lineItem.getReferenceName().contains("Rental") && (arc>0 || mrc >0 || nrc>0)) {
					lineItembean = new LineItemDetailsBean();
					lineItembean.setMrc(String.format("%.2f", mrc));
					lineItembean.setNrc(String.format("%.2f", nrc));
					lineItembean.setArc(String.format("%.2f", arc));
					lineItembean.setLineitem(BillingConstants.CPE_RENTAL_CHARGE);
					lineItembeanList.add(lineItembean);
				}
			}
			if(lineItem.getReferenceName().equals("Internet Port")) {
				lineItembean = new LineItemDetailsBean();
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setLineitem(serviceType.getAttributeValue() != null && "Usage Based".equals(serviceType.getAttributeValue())
								? BillingConstants.BURSTABLE_CHARGE	: BillingConstants.FIXED_PORT_CHARGE);
				lineItembeanList.add(lineItembean);
								
			}
			if(lineItem.getReferenceName().equals("VPN Port") && "GVPN".equals(scServiceDetail.getErfPrdCatalogProductName())) {
				lineItembean = new LineItemDetailsBean();
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setLineitem(serviceType.getAttributeValue() != null && "Usage Based".equals(serviceType.getAttributeValue())
						? BillingConstants.BURSTABLE_CHARGE	: BillingConstants.GVPN_PORT_CHARGE);
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getReferenceName().equals("IZO Private Connect Port")) {
				lineItembean = new LineItemDetailsBean();
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setLineitem(BillingConstants.GVPN_PORT_CHARGE);
				lineItembeanList.add(lineItembean);
			}
			if(lineItem.getReferenceName().equals("Additional IPs")) {
			    lineItembean = new LineItemDetailsBean();
			    lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setLineitem(BillingConstants.ADDITIONAL_IP);
				lineItembeanList.add(lineItembean);
				
			}
			if(lineItem.getReferenceName().equals("Shifting Charges")) {
			    lineItembean = new LineItemDetailsBean();
			    lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setLineitem(BillingConstants.SHIFTING_CHARGE_IAS);
				lineItembeanList.add(lineItembean);
				
			}
			if (lineItem.getReferenceName().contains("Burstable")) {
			    lineItembean = new LineItemDetailsBean();
			    lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setLineitem(BillingConstants.BURSTABLE_CHARGE);
				lineItembeanList.add(lineItembean);
				
			}
			if (lineItem.getReferenceName().equals("GDIA Port Charges")) {
				lineItembean = new LineItemDetailsBean();
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setLineitem(BillingConstants.GDIA_PORT_CHARGE);
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getReferenceName().equals("IZO Internet WAN Port Charges")) {
				lineItembean = new LineItemDetailsBean();
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setLineitem(BillingConstants.IWAN_PORT_CHARGE);
				lineItembeanList.add(lineItembean);
			}	
			
		});
		
		return groupLineItemsForDisplay(lineItembeanList,noOfAdditionalIps,bomResource,scServiceDetail,billingMethod);
	}
	
	public List<LineItemDetailsBean> groupLineItemsForDisplay(List<LineItemDetailsBean> lineitems, String noOfAdditionalIps, CpeBomResource bomResource, ScServiceDetail scServiceDetail, String billingMethod){
		
		Map<String, Double> nrc = lineitems.stream().collect(Collectors.groupingBy(LineItemDetailsBean::getLineitem,
				Collectors.summingDouble(detail->Double.parseDouble(detail.getNrc()))));
		Map<String, Double> mrc = lineitems.stream().collect(Collectors.groupingBy(LineItemDetailsBean::getLineitem,
				Collectors.summingDouble(detail->Double.parseDouble(detail.getMrc()))));
		Map<String, Double> arc = lineitems.stream().collect(Collectors.groupingBy(LineItemDetailsBean::getLineitem,
				Collectors.summingDouble(detail->Double.parseDouble(detail.getArc()))));
		List<LineItemDetailsBean> finalChargeLineItems = new ArrayList<>(); 
		
		Double usageArc = 0.0;
		ScServiceAttribute usageChargingRate = serviceAttributeRepository
				.findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(scServiceDetail.getId(), "usage_price_per_mb");
		ScServiceAttribute cloudProvider = serviceAttributeRepository
				.findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(scServiceDetail.getId(), "Cloud Provider");
		if (usageChargingRate != null && StringUtils.isNotEmpty(usageChargingRate.getAttributeValue())) {
			usageArc = Double.parseDouble(usageChargingRate.getAttributeValue()); 
		}
		
		for (Entry<String, Double> entry : nrc.entrySet()) {
			LineItemDetailsBean lineitem = new LineItemDetailsBean();
			String accountNumber = "OPTACC_".concat(scServiceDetail.getId().toString());
			lineitem.setNrc(String.format("%.2f", entry.getValue()));
			lineitem.setArc(String.format("%.2f", arc.get(entry.getKey())));
			lineitem.setMrc(String.format("%.2f", mrc.get(entry.getKey())));
			lineitem.setUsageArc((entry.getKey().contains("Burstable") ? String.format("%.2f", usageArc) : "0.0"));
			lineitem.setLineitem(entry.getKey());
			if(entry.getKey().contains(BillingConstants.CPE_OUTRIGHT_CHARGE)) {
				lineitem.setServiceType("CPE");
				lineitem.setBillingType("CPE");
				lineitem.setHsnCode(cpeHsnCode(bomResource));
				lineitem.setCpeModel(BillingConstants.CISCO.concat("/").concat(bomResource.getBomName()));
				lineitem.setAccountNumber(accountNumber.concat("_CPE"));
			}else {
				lineitem.setServiceType(scServiceDetail.getErfPrdCatalogProductName());
				lineitem.setBillingType(scServiceDetail.getErfPrdCatalogProductName().equals(BillingConstants.IZOPC)
						? BillingConstants.GVPN
						: scServiceDetail.getErfPrdCatalogProductName());
				lineitem.setHsnCode(BillingConstants.HSN_CODE);
				lineitem.setAccountNumber(accountNumber.concat("_Non_CPE"));
			}
			
			lineitem.setBillingMethod(StringUtils.trimToEmpty(billingMethod));
			if(entry.getKey().contains(BillingConstants.ADDITIONAL_IP)) {
				lineitem.setQuantity(noOfAdditionalIps);
				lineitem.setUnitOfMeasurement("No.of.Ips");
			}else {
				lineitem.setQuantity("1");
				lineitem.setUnitOfMeasurement("NA");
			}
			lineitem.setIsProrated("Yes");
			if(Double.parseDouble(lineitem.getMrc())>0 ||Double.parseDouble(lineitem.getNrc())>0 || Double.parseDouble(lineitem.getArc())>0) {
				finalChargeLineItems.add(lineitem);
			}
			if(scServiceDetail.getErfPrdCatalogProductName().equals(BillingConstants.IZOPC)) {
				lineitem.setBillingType(BillingConstants.GVPN);
				lineitem.setDescription(StringUtils.isNotEmpty(cloudProvider.getAttributeValue()) ? cloudProvider.getAttributeValue(): "");
			}
		}
		return finalChargeLineItems;
	}
	
	public String cpeHsnCode(CpeBomResource bomResource) {
		String[] hsnCode = { "" };
		if (Objects.nonNull(bomResource)) {
			bomResource.getResources().forEach(bomHsnCode -> {
				if (bomHsnCode.getProductCode().equals(bomResource.getBomName())
						|| bomHsnCode.getProductCode().equals(bomResource.getUniCode())
						|| bomResource.getUniCode().contains(bomHsnCode.getProductCode())
						|| bomResource.getBomName().contains(bomHsnCode.getProductCode())) {
					hsnCode[0] = bomHsnCode.getHsnCode();
				}
			});
		}
		return hsnCode[0];
	}

	@Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW)
	public List<LineItemDetailsBean> loadLineItemsNPL(Integer serviceId,
			String billingMethod) {
		List<ScServiceCommercial> commercials = scServiceCommercialRepository.findByScServiceId(serviceId);
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
		Map<String, String> accountSplitMap = nplAccountSplit(scServiceDetail);
		List<LineItemDetailsBean> lineItembeanList = new ArrayList<LineItemDetailsBean>();
		if (accountSplitMap != null) {
			accountSplitMap.forEach((account,ratio)->{
				commercials.forEach(lineItem -> {
					Double nrc = lineItem.getNrc() != null ? lineItem.getNrc() : 0;
					Double mrc = lineItem.getMrc() != null ? lineItem.getMrc() : 0;
					Double arc = lineItem.getArc() != null ? lineItem.getArc() : 0;
					if (scServiceDetail != null && "NPL".equals(scServiceDetail.getErfPrdCatalogProductName())
							&& lineItem.getReferenceName().contains("Link Management Charges")&& (arc>0 || mrc>0 || nrc>0) ) {
						lineItembean = new LineItemDetailsBean();
						lineItembean.setMrc(String.format("%.2f",Integer.parseInt(ratio) * mrc / 100));
						lineItembean.setNrc(String.format("%.2f",Integer.parseInt(ratio) * nrc / 100));
						lineItembean.setArc(String.format("%.2f",Integer.parseInt(ratio) * arc / 100));
						lineItembean.setLineitem(BillingConstants.LINK_MANGEMENT_CHARGE);
						lineItembean.setServiceType(scServiceDetail.getErfPrdCatalogProductName());
						lineItembean.setHsnCode(BillingConstants.HSN_CODE);
						lineItembean.setAccountNumber(account);
						lineItembean.setQuantity("1");
						lineItembean.setUnitOfMeasurement("NA");
						lineItembean.setIsProrated("Yes");
						lineItembean.setSiteType(account.contains("_A_")?"A":"B");
						lineItembean.setBillingMethod(billingMethod);
						lineItembean.setUsageArc("0.0");
						lineItembean.setBillingRatio(ratio.concat("%"));
						lineItembeanList.add(lineItembean);
					}
					if (scServiceDetail != null && "NPL".equals(scServiceDetail.getErfPrdCatalogProductName())
							&& lineItem.getReferenceName().contains("National Connectivity")&& (arc>0 || mrc>0 || nrc>0) ) {
						lineItembean = new LineItemDetailsBean();
						lineItembean.setMrc(String.format("%.2f",Integer.parseInt(ratio) * mrc / 100));
						lineItembean.setNrc(String.format("%.2f",Integer.parseInt(ratio) * nrc / 100));
						lineItembean.setArc(String.format("%.2f",Integer.parseInt(ratio) * arc / 100));
						lineItembean.setLineitem(BillingConstants.BANDWIDTH_CHARGE);
						lineItembean.setServiceType(scServiceDetail.getErfPrdCatalogProductName());
						lineItembean.setHsnCode(BillingConstants.HSN_CODE);
						lineItembean.setAccountNumber(account);
						lineItembean.setQuantity("1");
						lineItembean.setUnitOfMeasurement("NA");
						lineItembean.setIsProrated("Yes");
						lineItembean.setSiteType(account.contains("_A_")?"A":"B");
						lineItembean.setBillingMethod(billingMethod);
						lineItembean.setUsageArc("0.0");
						lineItembean.setBillingRatio(ratio.concat("%"));
						lineItembeanList.add(lineItembean);
					}
					if (scServiceDetail != null && "NPL".equals(scServiceDetail.getErfPrdCatalogProductName())
							&& lineItem.getReferenceName().contains("Shifting Charges")&& (arc>0 || mrc>0 || nrc>0) ) {
						lineItembean = new LineItemDetailsBean();
						lineItembean.setMrc(String.format("%.2f",Integer.parseInt(ratio) * mrc / 100));
						lineItembean.setNrc(String.format("%.2f",Integer.parseInt(ratio) * nrc / 100));
						lineItembean.setArc(String.format("%.2f",Integer.parseInt(ratio) * arc / 100));
						lineItembean.setLineitem(BillingConstants.SHIFTING_CHARGE_NPL);
						lineItembean.setServiceType(scServiceDetail.getErfPrdCatalogProductName());
						lineItembean.setHsnCode(BillingConstants.HSN_CODE);
						lineItembean.setAccountNumber(account);
						lineItembean.setQuantity("1");
						lineItembean.setUnitOfMeasurement("NA");
						lineItembean.setIsProrated("Yes");
						lineItembean.setSiteType(account.contains("_A_")?"A":"B");
						lineItembean.setBillingMethod(billingMethod);
						lineItembean.setUsageArc("0.0");
						lineItembean.setBillingRatio(ratio.concat("%"));
						lineItembeanList.add(lineItembean);
					}
					if (scServiceDetail != null && "NPL".equals(scServiceDetail.getErfPrdCatalogProductName())
							&& lineItem.getReferenceName().contains("Cross Connect")&& (arc>0 || mrc>0 || nrc>0) ) {
						lineItembean = new LineItemDetailsBean();
						lineItembean.setMrc(String.format("%.2f",Integer.parseInt(ratio) * mrc / 100));
						lineItembean.setNrc(String.format("%.2f",Integer.parseInt(ratio) * nrc / 100));
						lineItembean.setArc(String.format("%.2f",Integer.parseInt(ratio) * arc / 100));
						lineItembean.setLineitem(BillingConstants.CROSS_CONNECTION_CHARGE);
						lineItembean.setServiceType(scServiceDetail.getErfPrdCatalogProductName());
						lineItembean.setHsnCode(BillingConstants.HSN_CODE);
						lineItembean.setAccountNumber(account);
						lineItembean.setQuantity("1");
						lineItembean.setUnitOfMeasurement("NA");
						lineItembean.setIsProrated("Yes");
						lineItembean.setSiteType(account.contains("_A_")?"A":"B");
						lineItembean.setBillingMethod(billingMethod);
						lineItembean.setUsageArc("0.0");
						lineItembean.setBillingRatio(ratio.concat("%"));
						lineItembeanList.add(lineItembean);
					}
					if (scServiceDetail != null && "NPL".equals(scServiceDetail.getErfPrdCatalogProductName())
							&& lineItem.getReferenceName().contains("Fiber Entry")&& (arc>0 || mrc>0 || nrc>0) ) {
						lineItembean = new LineItemDetailsBean();
						lineItembean.setMrc(String.format("%.2f",Integer.parseInt(ratio) * mrc / 100));
						lineItembean.setNrc(String.format("%.2f",Integer.parseInt(ratio) * nrc / 100));
						lineItembean.setArc(String.format("%.2f",Integer.parseInt(ratio) * arc / 100));
						lineItembean.setLineitem(BillingConstants.FIBER_ENTRY_CHARGE);
						lineItembean.setServiceType(scServiceDetail.getErfPrdCatalogProductName());
						lineItembean.setHsnCode(BillingConstants.HSN_CODE);
						lineItembean.setAccountNumber(account);
						lineItembean.setQuantity("1");
						lineItembean.setUnitOfMeasurement("NA");
						lineItembean.setIsProrated("Yes");
						lineItembean.setSiteType(account.contains("_A_")?"A":"B");
						lineItembean.setBillingMethod(billingMethod);
						lineItembean.setUsageArc("0.0");
						lineItembean.setBillingRatio(ratio.concat("%"));
						lineItembeanList.add(lineItembean);
					}
				});
			});
		}
		return lineItembeanList;
	}

	public List<LineItemDetailsBean> loadServiceTerminationLineItem(Integer serviceId) {
		Map<String, String> serviceTermMap = commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("terminationEffectiveDate", "etcValue", "etcWaiver"), serviceId, "LM", "A");
		List<LineItemDetailsBean> itemDetailsBeans = new ArrayList<>();
		if (serviceTermMap != null) {
			LineItemDetailsBean itemDetailsBean = new LineItemDetailsBean();
			itemDetailsBean.setLineitem("Early Termination Charges");
			itemDetailsBean.setEtcCharge(serviceTermMap.get("etcValue"));
		//	itemDetailsBean.setEtcWaiver(serviceTermMap.get("etcWaiver"));
			itemDetailsBean.setTerminationDate(serviceTermMap.get("terminationEffectiveDate"));
			itemDetailsBeans.add(itemDetailsBean);
		}
		return itemDetailsBeans;
	}
	public List<LineItemDetailsBean> loadServiceCancellationLineItem(Integer serviceId) {
		Map<String, String> serviceTermMap = commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("effectiveDateOfChange", "cancellationReason", "cancellationCharges"), serviceId, "LM", "A");
		List<LineItemDetailsBean> itemDetailsBeans = new ArrayList<>();
		if (serviceTermMap != null) {
			LineItemDetailsBean itemDetailsBean = new LineItemDetailsBean();
			itemDetailsBean.setAccountNumber("OPTACC_".concat(serviceId.toString()).concat("_Non_CPE"));
			itemDetailsBean.setLineitem("Cancellation Charges");
			itemDetailsBean.setNrc(serviceTermMap.get("cancellationCharges"));
			itemDetailsBean.setCancellationDate(serviceTermMap.get("effectiveDateOfChange"));
			itemDetailsBean.setDescription(serviceTermMap.get("cancellationReason"));
			itemDetailsBeans.add(itemDetailsBean);
		}
		return itemDetailsBeans;
	}
	
	public String getActiveAccounts(String serviceId, String serviceType) {
		Set<String> accountList = chargeLineitemRepository.findByTotalAccountNumbers(serviceId, serviceType);
		return accountList.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(","));
	}
	
	public Map<String, String> nplAccountSplit(ScServiceDetail scServiceDetail) {
		ScOrderAttribute scOrderAttribute = scOrderAttributeRepository
				.findFirstByAttributeNameAndScOrder(LeAttributesConstants.BILLING_RATIO, scServiceDetail.getScOrder());
		Map<String, String> attrMap = commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("cpeManagementType", "cpeType", "destinationState", "siteGstNumber", "serviceSubType"),
				scServiceDetail.getId(), "LM", "A");
		Map<String, String> attrBMap = commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("destinationState", "siteGstNumber"), scServiceDetail.getId(), "LM", "B");
		Map<String, String> atMap = new HashMap<>();
		Map<String, String> accountMap = new HashMap<>();
		
		String siteAState = attrMap.get("destinationState");
		String siteBState = attrBMap.get("destinationState");
		String siteAGstNumber = attrMap.get("siteGstNumber");
		String siteBGstNumber = attrBMap.get("siteGstNumber");
		String splitRatio = "50:50";
		String serviceType =attrMap.get("serviceSubType");
		if(serviceType!=null && "Intercity".equals(serviceType)) {
			atMap.put("nplBillingType", "NPLC");
		}else {
			atMap.put("nplBillingType", "NPL Intracity");
		}
		
		if(scOrderAttribute!=null && scOrderAttribute.getAttributeValue()!= null) {
			 splitRatio = scOrderAttribute.getAttributeValue();
		}
		if (siteAState != null & siteBState != null && siteAState.equalsIgnoreCase(siteBState)
				&& siteAGstNumber != null && siteBGstNumber != null && siteAGstNumber.equals(siteBGstNumber)) {

			String accountNumber = "OPTACC_A_".concat(scServiceDetail.getId().toString()).concat("_Non_CPE");
			atMap.put("accountNoRequired", "One");
			accountMap.put(accountNumber,"100");

		} else {
			String accountNumber = "OPTACC_A_".concat(scServiceDetail.getId().toString()).concat("_Non_CPE");
			accountMap.put(accountNumber,splitRatio.split(":")[0]);

			String accountBNumber = "OPTACC_B_".concat(scServiceDetail.getId().toString()).concat("_Non_CPE");
			atMap.put("accountNoRequired", "Two");
			accountMap.put(accountBNumber,splitRatio.split(":")[1]);

		}
		
		componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap, AttributeConstants.COMPONENT_LM, "A");
		
		
		return accountMap;
	}

	@Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW)
	public Map<String, String> loadDemoOrderBillDate(Integer serviceId) {
		Map<String, String> attrMap = commonFulfillmentUtils
				.getComponentAttributesDetails(Arrays.asList(BillingConstants.PROD_BILL_START_DATE,
						BillingConstants.DEMO_DAYS, BillingConstants.DEMO_BILL_START_DATE,BillingConstants.PARENT_DEMO_BILL_START_DATE), serviceId, "LM", "A");
		ScServiceDetail scServiceDetail= scServiceDetailRepository.findById(serviceId).get();
		Map<String, String> atMap = new HashMap<>();
		atMap.put(BillingConstants.DEMO_BILL_START_DATE, attrMap.get(BillingConstants.PROD_BILL_START_DATE));
		
		if (scServiceDetail != null && scServiceDetail.getOrderType().equalsIgnoreCase("MACD")
				&& scServiceDetail.getOrderCategory().equalsIgnoreCase("DEMO_EXTENSION")) {
			atMap.put(BillingConstants.DEMO_BILL_START_DATE, addDays(attrMap.get(BillingConstants.PARENT_DEMO_BILL_START_DATE),1));
			atMap.put(BillingConstants.DEMO_BILL_END_DATE, addDays(attrMap.get(BillingConstants.DEMO_BILL_START_DATE),Integer.valueOf(attrMap.get(BillingConstants.DEMO_DAYS))));
		} else {
			atMap.put(BillingConstants.DEMO_BILL_END_DATE, addDays(attrMap.get(BillingConstants.PROD_BILL_START_DATE),Integer.valueOf(attrMap.get(BillingConstants.DEMO_DAYS))));
		}
		componentAndAttributeService.updateAttributes(serviceId, atMap, AttributeConstants.COMPONENT_LM,"A");
		
		return atMap;
	}
	
	public String addDays(String commDate, Integer days) {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(commDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).plusDays(days);
		Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(out);
	}

	@Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW)
	public void loadAdditionalAttributes(Integer serviceId, Integer orderId) {
		Map<String, String> atMap = new HashMap<>();
		ScContractInfo scContractInfo = scContractInfoRepository.findFirstByScOrder_id(orderId);
		Map<String, String> attrMap = commonFulfillmentUtils
				.getComponentAttributesDetails(Arrays.asList(BillingConstants.PROD_COMM_DATE,BillingConstants.PROD_BILL_START_DATE), serviceId, "LM", "A");
		String commissioningDate = attrMap.get(BillingConstants.PROD_COMM_DATE);
		String billStartDate = attrMap.get(BillingConstants.PROD_BILL_START_DATE);
		SimpleDateFormat formatter = null;
		Date contractEndtOut = null;
		Date contractStartOut = null;
		if(scContractInfo!=null && commissioningDate!=null) {
			int totalDays = 0;
			double totalTermMonths = Double.parseDouble(scContractInfo.getOrderTermInMonths() != null
					? String.valueOf(scContractInfo.getOrderTermInMonths())	: "12");
			int month = (int) totalTermMonths;
			double days = totalTermMonths - month;
			LOGGER.info("Bill Start Date is : {} and Contract Term is {} ",commissioningDate,totalTermMonths);
			if (days == 0.5) {
				totalDays = 15;
			}
			LOGGER.info("Total Contract Term in Months {} and Days {} ",month,totalDays);
			try {
				Date date = new SimpleDateFormat("yyyy-MM-dd").parse(commissioningDate);
				formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
							    
			    LocalDateTime contractStartLocal = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
			    contractStartOut =  Date.from(contractStartLocal.atZone(ZoneId.systemDefault()).toInstant());
			 
				
				LocalDateTime contractEndLocal = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).plusMonths(month)
						.plusDays(totalDays).minusDays(1);
				contractEndtOut = Date.from(contractEndLocal.atZone(ZoneId.systemDefault()).toInstant());
			       
			    
			} catch (ParseException e) {
				e.printStackTrace();
			}
			LOGGER.info("Contract Start Date is : {} ",Timestamp.valueOf(formatter.format(contractStartOut)));
			LOGGER.info("Contract End Date is : {} ",Timestamp.valueOf(formatter.format(contractEndtOut)));
			
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
			if(scServiceDetail!=null) {
				scServiceDetail.setContractStartDate(Timestamp.valueOf(formatter.format(contractStartOut)));
				scServiceDetail.setContractEndDate(Timestamp.valueOf(formatter.format(contractEndtOut)));
				scServiceDetailRepository.save(scServiceDetail);
				
				ScServiceAttribute additionalIPs = serviceAttributeRepository
						.findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(scServiceDetail.getId(), "Additional IPs");
				if(additionalIPs!=null && additionalIPs.getAttributeValue()!=null) {
					atMap.put("additionalIpsReq", additionalIPs.getAttributeValue());
				}
			}
			scContractInfo.setContractStartDate(Timestamp.valueOf(formatter.format(contractStartOut)));
			scContractInfo.setContractEndDate(Timestamp.valueOf(formatter.format(contractEndtOut)));
			scContractInfoRepository.save(scContractInfo);
		}
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
		ScOrder scOrder= scOrderRepository.findById(orderId).get();

		if (scOrder != null && scServiceDetail != null) {
			String orderType = OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);
			if ("MACD".equalsIgnoreCase(orderType) && Objects.nonNull(scServiceDetail.getOrderSubCategory())
					&& scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel")) {
				int parallelRundays = 0;
				ScServiceAttribute scServiceDownTimeAttr = serviceAttributeRepository
						.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(), "downtime_duration");
				if (scServiceDownTimeAttr != null && !scServiceDownTimeAttr.getAttributeValue().isEmpty()) {
					parallelRundays = Integer.parseInt(scServiceDownTimeAttr.getAttributeValue());
				}
				atMap.put("terminationDate", optimusTerminationDateCalculation(billStartDate, parallelRundays));
			}
		}
		componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap, AttributeConstants.COMPONENT_LM, "A");

	}
	
	public String optimusTerminationDateCalculation(String commDate, Integer parallelRunDays) {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(commDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		LocalDateTime ldt = null;
		if (parallelRunDays > 0) {
			ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).plusDays(parallelRunDays);
		} else {
			ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).minusDays(parallelRunDays);
		}
		
		Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(out);
	}
}
