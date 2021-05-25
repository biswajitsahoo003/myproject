package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.servicefulfillment.beans.CpeBomResource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.MstCostCatalogue;
import com.tcl.dias.servicefulfillment.entity.entities.ScAdditionalServiceParam;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceCommercial;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScSolutionComponent;
import com.tcl.dias.servicefulfillment.entity.repository.GenevaIpcOrderEntryRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstCostCatalogueRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceCommercialRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScSolutionComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicefulfillmentutils.beans.LineItemDetailsBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.BillingConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author yogesh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited used for the document related
 *            service
 */
@Service
@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
public class BillingSdwanChargeLineItemService {

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

	@Autowired
	ScComponentRepository scComponentRepository;

	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;

	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;

	@Autowired
	ScSolutionComponentRepository scSolutionComponentRepository;

	LineItemDetailsBean lineItembean = null;
	
	@Autowired
	MstCostCatalogueRepository mstCostCatalogueRepository;

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public List<LineItemDetailsBean> loadSdwanLineItems(Integer serviceId, String billingMethod) {
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
		List<LineItemDetailsBean> lineItembeanList = new ArrayList<LineItemDetailsBean>();
		String cpeType[]= {null};
		Map<String, String> attrMap = commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("isBillingInternational"), serviceId, "LM","A");
		List<ScComponent> scComponents = scComponentRepository.findByScServiceDetailId(scServiceDetail.getId());
		scComponents.forEach(scComponent -> {
			List<ScServiceCommercial> commercials = scServiceCommercialRepository
					.findByScServiceIdAndComponentId(serviceId, scComponent.getId());
			ScComponentAttribute componentAttribute = scComponentAttributesRepository.findFirstByScComponent_idAndAttributeName(scComponent.getId(), "cpeType");
			if(componentAttribute!=null && componentAttribute.getAttributeValue()!=null) {
				cpeType[0] = componentAttribute.getAttributeValue();
			}
			commercials.forEach(lineItem -> {
				Double nrc = lineItem.getNrc() != null ? lineItem.getNrc() : 0;
				Double arc = lineItem.getArc() != null ? lineItem.getArc() : 0;
				Double mrc = arc > 0 ? arc / 12 : 0;
				String accountNumberNonCpe = "OPTACC_".concat(scServiceDetail.getId().toString()).concat("_Non_CPE");
				String accountNumberCpe = "OPTACC_".concat(scComponent.getSiteType())
						.concat(scServiceDetail.getId().toString()).concat("_CPE");
				if (Objects.nonNull(lineItem.getReferenceName())
						&& lineItem.getReferenceName().equals("IZO SDWAN service charges")
						&& "COMPONENTS".equals(lineItem.getReferenceType()) && (arc > 0 || mrc > 0 || nrc > 0)) {
					lineItembean = new LineItemDetailsBean();
					lineItembean.setMrc(String.format("%.2f", mrc));
					lineItembean.setNrc(String.format("%.2f", nrc));
					lineItembean.setArc(String.format("%.2f", arc));
					lineItembean.setLineitem(BillingConstants.SDWAN_SERVICE_CHARGE);
					lineItembean.setServiceType(IzosdwanCommonConstants.IZOSDWAN_NAME);
					lineItembean.setBillingType(IzosdwanCommonConstants.IZOSDWAN_NAME);
					lineItembean.setHsnCode(BillingConstants.HSN_CODE);
					lineItembean.setBillingMethod(billingMethod);
					lineItembean.setQuantity("1");
					lineItembean.setUnitOfMeasurement("NA");
					lineItembean.setIsProrated("Yes");
					lineItembean.setAccountNumber(accountNumberNonCpe);
					lineItembeanList.add(lineItembean);
				}
				if (Objects.nonNull(lineItem.getReferenceName()) && lineItem.getReferenceName().equals("CPE")
						&& (arc > 0 || mrc > 0 || nrc > 0)) {
					lineItembean = new LineItemDetailsBean();
					if (cpeType[0]!=null && cpeType[0].contains("Rental")) {
						lineItembean.setLineitem(BillingConstants.CPE_RENTAL_CHARGE);
						lineItembean.setServiceType(IzosdwanCommonConstants.IZOSDWAN_NAME);
						lineItembean.setBillingType(IzosdwanCommonConstants.IZOSDWAN_NAME);
						lineItembean.setHsnCode(BillingConstants.HSN_CODE);
						lineItembean.setAccountNumber(accountNumberNonCpe);
					} else {
						CpeBomResource bomResource = getBomResourcesSdWan(scComponent);
						MstCostCatalogue mstCostCatalogue = mstCostCatalogueRepository.findFirstByBundledBomAndCategory(bomResource.getBomName(), "Router");
						if(Objects.nonNull(mstCostCatalogue)) {
							lineItembean.setHsnCode(mstCostCatalogue.getHsnCode());
							lineItembean.setCpeModel(mstCostCatalogue.getBundledBom());
						}
						lineItembean.setSiteType(scComponent.getSiteType());
						if(attrMap!=null && "Y".equals(attrMap.get("isBillingInternational"))) {
							lineItembean.setServiceType(IzosdwanCommonConstants.IZOSDWAN_NAME);
							lineItembean.setBillingType(IzosdwanCommonConstants.IZOSDWAN_NAME);
							lineItembean.setSiteType(null);
						}else {
							lineItembean.setServiceType("CPE");
							lineItembean.setBillingType("CPE");
						}
						lineItembean.setServiceType("CPE");
						lineItembean.setBillingType("CPE");
						lineItembean.setLineitem(BillingConstants.CPE_OUTRIGHT_CHARGE);
						lineItembean.setAccountNumber(accountNumberCpe);
					}
					lineItembean.setMrc(String.format("%.2f", mrc));
					lineItembean.setNrc(String.format("%.2f", nrc));
					lineItembean.setArc(String.format("%.2f", arc));
					lineItembean.setBillingMethod(billingMethod);
					lineItembean.setQuantity("1");
					lineItembean.setUnitOfMeasurement("NA");
					lineItembean.setIsProrated("Yes");
					lineItembeanList.add(lineItembean);
				}
			});
		});

		return lineItembeanList;
	}

	public List<LineItemDetailsBean> loadCgwLineItems(Integer serviceId, String billingMethod) {
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
		List<ScComponent> scComponents = scComponentRepository.findByScServiceDetailId(scServiceDetail.getId());
		List<LineItemDetailsBean> lineItembeanList = new ArrayList<LineItemDetailsBean>();
		scComponents.forEach(scComponent -> {
			List<ScServiceCommercial> commercials = scServiceCommercialRepository
					.findByScServiceIdAndComponentId(serviceId, scComponent.getId());
			commercials.forEach(lineItem -> {
				Double nrc = lineItem.getNrc() != null ? lineItem.getNrc() : 0;
				Double mrc = lineItem.getMrc() != null ? lineItem.getMrc() : 0;
				Double arc = lineItem.getArc() != null ? lineItem.getArc() : 0;
				String accountNumber = "OPTACC_".concat(scComponent.getSiteType() + "_")
						.concat(scServiceDetail.getId().toString());
				if (((Objects.nonNull(lineItem.getReferenceName())
						&& lineItem.getReferenceName().equals("Cloud Gateway Port")))
						&& (arc > 0 || mrc > 0 || nrc > 0)) {
					lineItembean = new LineItemDetailsBean();
					lineItembean.setMrc(String.format("%.2f", mrc));
					lineItembean.setNrc(String.format("%.2f", nrc));
					lineItembean.setArc(String.format("%.2f", arc));
					lineItembean.setLineitem(BillingConstants.SDWAN_CLOUD_GATEWAY_CHARGE);
					lineItembean.setServiceType(IzosdwanCommonConstants.IZOSDWAN_CGW);
					lineItembean.setBillingType(IzosdwanCommonConstants.IZOSDWAN_NAME);
					lineItembean.setHsnCode(BillingConstants.HSN_CODE);
					lineItembean.setBillingMethod(billingMethod);
					lineItembean.setQuantity("1");
					lineItembean.setUnitOfMeasurement("NA");
					lineItembean.setIsProrated("Yes");
					lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
					lineItembeanList.add(lineItembean);
				}
			});
		});

		return lineItembeanList;
	}

	public CpeBomResource getBomResourcesSdWan(ScComponent scComponent) {
		ScComponentAttribute scComp = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndScComponent_idAndAttributeName(scComponent.getScServiceDetailId(),
						scComponent.getId(), "CPE Basic Chassis");
		CpeBomResource bomResources = null;
		if (scComp != null && scComp.getAttributeValue() != null) {
			Optional<ScAdditionalServiceParam> scAdditionalServiceParam = scAdditionalServiceParamRepository
					.findById(Integer.valueOf(scComp.getAttributeValue()));
			if (scAdditionalServiceParam.isPresent()) {
				String bomResponse = scAdditionalServiceParam.get().getValue();
				CpeBomResource[] bomResourcess = null;
				try {
					bomResourcess = Utils.convertJsonToObject(bomResponse, CpeBomResource[].class);
				} catch (TclCommonException e) {
				}
				bomResources = bomResourcess[0];
			}
		}

		return bomResources;
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
}
