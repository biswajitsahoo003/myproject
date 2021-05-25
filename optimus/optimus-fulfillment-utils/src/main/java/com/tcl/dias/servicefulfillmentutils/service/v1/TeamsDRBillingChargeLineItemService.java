package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.servicefulfillment.entity.entities.MstCostCatalogue;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScTeamsDRServiceCommercial;
import com.tcl.dias.servicefulfillment.entity.repository.MstCostCatalogueRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScTeamsDRServiceCommercialRepository;
import com.tcl.dias.servicefulfillmentutils.beans.LineItemDetailsBean;
import com.tcl.dias.servicefulfillmentutils.constants.BillingConstants;

/**
 * Service class for teamsdr billing charge line items.
 *
 * @author Syed Ali.
 * @createdAt 29/01/2021, Friday, 15:15
 */
@Service
public class TeamsDRBillingChargeLineItemService {

	@Autowired
	ScTeamsDRServiceCommercialRepository scTeamsDRServiceCommercialRepository;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScComponentRepository scComponentRepository;
	
	LineItemDetailsBean lineItembean = null;
	
	@Autowired
	MstCostCatalogueRepository mstCostCatalogueRepository;

	@Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW)
	public List<LineItemDetailsBean> loadLineItems(Integer serviceId, String billingMethod) {
		List<ScTeamsDRServiceCommercial> commercials = scTeamsDRServiceCommercialRepository
				.findByScServiceDetailId(serviceId);
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
		List<LineItemDetailsBean> lineItembeanList = new ArrayList<>();
		commercials.forEach(lineItem -> {
			Double nrc = lineItem.getNrc() != null ? lineItem.getNrc() : 0;
			Double mrc = lineItem.getMrc() != null ? lineItem.getMrc() : 0;
			Double arc = lineItem.getArc() != null ? lineItem.getArc() : 0;
			Double unitNrc = lineItem.getUnitNrc() != null ? lineItem.getUnitNrc() : 0;
			Double effectiveUsage = lineItem.getUsage() != null ? lineItem.getUsage() : 0;
			Double effectiveOverage = lineItem.getOverage() != null ? lineItem.getOverage() : 0;
			String accountNumber = "OPTACC_".concat(scServiceDetail.getId().toString());

			lineItembean = new LineItemDetailsBean();
			
			lineItembean.setServiceType(scServiceDetail.getErfPrdCatalogProductName());
			lineItembean.setBillingMethod(billingMethod);
			lineItembean.setUnitOfMeasurement("NA");
			lineItembean.setIsProrated("Yes");
			
			if ((Objects.nonNull(lineItem.getComponentType())
					&& (lineItem.getChargeItem() != null && lineItem.getChargeItem().contains("Outright")))
					&& (arc > 0 || mrc > 0 || nrc > 0 || unitNrc > 0)) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setAccountNumber(accountNumber.concat("_CPE"));
				lineItembean.setBillingType(BillingConstants.CPE);
				lineItembean.setLineitem(BillingConstants.CPE_OUTRIGHT_CHARGE);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembeanList.add(cpeOutright(lineItembean, lineItem));

			}

			if ((Objects.nonNull(lineItem.getComponentType())
					&& (lineItem.getChargeItem() != null && lineItem.getChargeItem().contains("Management")))
					&& (arc > 0 || mrc > 0 || nrc > 0 || unitNrc > 0)) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.CPE_MANAGEMENT_CHARGE);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);

			}

			if ((Objects.nonNull(lineItem.getComponentType())
					&& (lineItem.getChargeItem() != null && lineItem.getChargeItem().contains("Rental")))
					&& (arc > 0 || mrc > 0 || nrc > 0 || unitNrc > 0)) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.CPE_RENTAL_CHARGE);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);

			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Managed Plan")
					&& "Recurring".equals(lineItem.getChargeItem())) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", mrc * 12));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.MANAGED_RECURRING);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Managed Plan")
					&& "Overage".equals(lineItem.getChargeItem())) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.MANAGED_OVERAGE);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Managed Plan")
					&& "Usage".equals(lineItem.getChargeItem())) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.MANAGED_USAGE);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Connect Plan")
					&& "Recurring".equals(lineItem.getChargeItem())) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", mrc * 12));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.MANAGED_RECURRING);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Connect Plan")
					&& "Overage".equals(lineItem.getChargeItem())) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.MANAGED_OVERAGE);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Connect Plan")
					&& "Usage".equals(lineItem.getChargeItem())) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.MANAGED_USAGE);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}

			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Professional Services")
					&& "T1 Technician".equals(lineItem.getChargeItem())) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.PROFESSIONAL_T1_TECHNICIAN);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Professional Services")
					&& "T2 Technician".equals(lineItem.getChargeItem())) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.PROFESSIONAL_T2_TECHNICIAN);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Professional Services")
					&& "T3 Technician".equals(lineItem.getChargeItem())) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.PROFESSIONAL_T3_TECHNICIAN);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Professional Services")
					&& "Design Engineer".equals(lineItem.getChargeItem())) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.PROFESSIONAL_SERVICES_DESIGN);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Professional Services")
					&& "Project Management".equals(lineItem.getChargeItem())) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.PROFESSIONAL_SERVICES_PROJECT);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Simple Services")
					&& "Remote".equals(lineItem.getChargeItem())) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.REMOTE_SERVICE);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Simple Services")
					&& "Expedited".equals(lineItem.getChargeItem())) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.EXPEDITED_SERVICE);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("GSC Setup and Management")
					&& (mrc > 0)) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.CUSTOM_GSC_RECURRING);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("GSC Setup and Management")
					&& "Usage".equals(lineItem.getChargeItem()) && (effectiveUsage > 0)) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.CUSTOM_GSC_USAGE);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("GSC Setup and Management")
					&& "Overage".equals(lineItem.getChargeItem()) && (effectiveOverage > 0)) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.CUSTOM_GSC_OVERAGE);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Monitoring & Management")
					&& (mrc > 0)) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.CUSTOM_MONITORING_RECURRING);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Monitoring & Management")
					&& "Usage".equals(lineItem.getChargeItem()) && (effectiveUsage > 0)) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.CUSTOM_MONITORING_USAGE);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Monitoring & Management")
					&& "Overage".equals(lineItem.getChargeItem()) && (effectiveOverage > 0)) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.CUSTOM_MONITORING_OVERAGE);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Tenant Management")
					&& (mrc > 0)) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.CUSTOM_TENANT_RECURRING);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Tenant Management")
					&& "Usage".equals(lineItem.getChargeItem()) && (effectiveUsage > 0)) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.CUSTOM_TENANT_USAGE);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Tenant Management")
					&& "Overage".equals(lineItem.getChargeItem()) && (effectiveOverage > 0)) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.CUSTOM_TENANT_OVERAGE);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Training") && (mrc > 0)) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.CUSTOM_TRAINING_RECURRING);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Training")
					&& "Usage".equals(lineItem.getChargeItem()) && (effectiveUsage > 0)) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.CUSTOM_TRAINING_USAGE);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("Training")
					&& "Overage".equals(lineItem.getChargeItem()) && (effectiveOverage > 0)) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.CUSTOM_TRAINING_OVERAGE);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}

			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("User Enablement")
					&& (mrc > 0)) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.CUSTOM_ENABLEMENT_RECURRING);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("User Enablement")
					&& "Usage".equals(lineItem.getChargeItem()) && (effectiveUsage > 0)) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.CUSTOM_ENABLEMENT_USAGE);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("User Enablement")
					&& "Overage".equals(lineItem.getChargeItem()) && (effectiveOverage > 0)) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.CUSTOM_ENABLEMENT_OVERAGE);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("24*7 Managed Support")
					&& (mrc > 0)) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.CUSTOM_MANAGED_RECURRING);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("24*7 Managed Support")
					&& "Usage".equals(lineItem.getChargeItem()) && (effectiveUsage > 0)) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.CUSTOM_MANAGED_USAGE);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
			if (lineItem.getChargeItem() != null && lineItem.getComponentName().contains("24*7 Managed Support")
					&& "Overage".equals(lineItem.getChargeItem()) && (effectiveOverage > 0)) {
				lineItembean.setMrc(String.format("%.2f", mrc));
				lineItembean.setNrc(String.format("%.2f", nrc));
				lineItembean.setArc(String.format("%.2f", arc));
				lineItembean.setEffectiveUsage(String.format("%.2f", effectiveUsage));
				lineItembean.setEffectiveOverage(String.format("%.2f", effectiveOverage));
				lineItembean.setAccountNumber(accountNumber.concat("_Non_CPE"));
				lineItembean.setBillingType(BillingConstants.MIRCOSOFT_CLOUD_SOLNS);
				lineItembean.setLineitem(BillingConstants.CUSTOM_MANAGED_OVERAGE);
				lineItembean.setDescription(lineItem.getComponentDesc());
				lineItembean.setQuantity(lineItem.getQuantity() != null ? lineItem.getQuantity().toString() : "1");
				lineItembean.setHsnCode(lineItem.getHsnCode());
				lineItembeanList.add(lineItembean);
			}
		});


		return lineItembeanList.stream().sorted((prod1,prod2)->prod1.getLineitem().compareTo(prod2.getLineitem())).collect(Collectors.toList());
	}
	
	private LineItemDetailsBean cpeOutright(LineItemDetailsBean lineItemDetailsBean,
			ScTeamsDRServiceCommercial lineItem) {

		MstCostCatalogue mstCostCatalogue = mstCostCatalogueRepository
				.findByBundledBomAndCategoryForOutright(lineItem.getComponentName(), lineItemDetailsBean.getLineitem())
				.stream().findFirst().get();
		if (mstCostCatalogue != null) {
			String cpeMake = mstCostCatalogue.getVendorName() != null ? mstCostCatalogue.getVendorName() : "NA";
			String cpeModel = mstCostCatalogue.getProductCode() != null ? mstCostCatalogue.getProductCode() : "NA";
			lineItemDetailsBean.setCpeModel(cpeMake + "/" + cpeModel);
			lineItemDetailsBean.setHsnCode(mstCostCatalogue.getHsnCode() != null ? mstCostCatalogue.getHsnCode() : "NA");
		}
		return lineItemDetailsBean;
	}
}
