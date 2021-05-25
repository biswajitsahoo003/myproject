package com.tcl.dias.servicehandover.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.servicefulfillment.entity.entities.ScChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicefulfillment.entity.repository.ScChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.AccountInputData;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicehandover.beans.taxcapture.CommissionDetail;
import com.tcl.dias.servicehandover.beans.taxcapture.CommissionDetails;
import com.tcl.dias.servicehandover.beans.taxcapture.Security;
import com.tcl.dias.servicehandover.beans.taxcapture.SetActualTax;
import com.tcl.dias.servicehandover.beans.taxcapture.SetActualTaxInput;
import com.tcl.dias.servicehandover.beans.taxcapture.SetActualTaxResponse;
import com.tcl.dias.servicehandover.config.SOAPConnector;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.util.JaxbMarshallerUtil;
import com.tcl.dias.servicehandover.util.TaxAttributeString;

/**
 * This service Applies Tax for Europe and ROW intl Orders
 * 
 * @author yogesh
 */

@Service
public class ApplyTaxService {

	@Autowired
	@Qualifier("ApplyTax")
	SOAPConnector applyTaxConnector;

	@Value("${applyTax}")
	private String applyTaxOperation;

	@Autowired
	ScOrderRepository scOrderRepository;

	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScComponentAttributesRepository componentAttributesRepository;

	@Autowired
	ScChargeLineitemRepository chargeLineitemRepository;

	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	ScServiceAttributeRepository serviceAttributeRepository;
	
	String taxExemption =null;
	
	String taxExemptionReason=null;

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplyTaxService.class);

	/**
	 * 
	 * This method Applies Tax for Europe and ROW post product commissioning.
	 * 
	 * @param orderCode
	 * @param serviceCode
	 * @param serviceId
	 * @param userName 
	 * @param serviceType
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public SetActualTaxResponse applyTax(String orderCode, String serviceCode, String serviceId, String userName)
			throws IllegalAccessException, InvocationTargetException {
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId)).get();
		String errorMessage = "";
		String status = "";
		Map<String, String> attrMap = commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("sapCode", "billStartDate","finalCps","taxExemption"), scServiceDetail.getId(), "LM", "A");
		 taxExemption= attrMap!=null && attrMap.get("taxExemption")!=null ?attrMap.get("taxExemption"):"N";
		if("Y".equals(taxExemption)) {
			taxExemption="Tax-Exempt";
			ScServiceAttribute scServiceAttribute = serviceAttributeRepository.findByScServiceDetail_idAndAttributeName(
					scServiceDetail.getId(), LeAttributesConstants.TAX_EXEMPTION_REASON);
			if (scServiceAttribute != null) {
				taxExemptionReason=WordUtils.capitalize(scServiceAttribute.getAttributeValue().toLowerCase());
			}
		}
		
		SetActualTaxResponse setActualTaxResponse = new SetActualTaxResponse();

		SetActualTax setActualTax = new SetActualTax();
		SetActualTaxInput setActualTaxInput = new SetActualTaxInput();
		Security security = new Security();
		CommissionDetails commissionDetails = new CommissionDetails();

		security.setSourceSystem("OPSPORTALBPM");
		security.setUniqueKey(String.valueOf(System.currentTimeMillis()));

		List<ScChargeLineitem> chargeLineitems = chargeLineitemRepository.findByServiceId(serviceId);
		chargeLineitems.stream().forEach(lineitem -> {
			CommissionDetail commissionDetail = new CommissionDetail();
			commissionDetail.setCRNId(attrMap.get("sapCode"));
			commissionDetail.setAccountNumber(lineitem.getAccountNumber());
			commissionDetail.setPOSProductSequence(Integer.parseInt(lineitem.getSourceProdSequence()));
			commissionDetail.setProductName(lineitem.getChargeLineitem());
			commissionDetail.setCommissionDate(attrMap.get("billStartDate"));
			commissionDetail.setTaxAttributes(generateTaxString(attrMap.get("finalCps"),taxExemption,taxExemptionReason));
			commissionDetail.setSystemFlag("NEW");
			commissionDetails.getCommissionDetail().add(commissionDetail);
		});
		setActualTaxInput.setCommissionDetails(commissionDetails);
		setActualTaxInput.setSecurity(security);
		setActualTaxInput.setUsername(StringUtils.isNotEmpty(userName) ? userName : "testuser");
		setActualTax.setSetActualTaxInput(setActualTaxInput);
		JaxbMarshallerUtil.jaxbObjectToXML(setActualTax, new ServicehandoverAudit());
		setActualTaxResponse = (SetActualTaxResponse) applyTaxConnector.callWebService(applyTaxOperation,
				setActualTax);
		if (Objects.nonNull(setActualTaxResponse)) {
			errorMessage = setActualTaxResponse.getSetActualTaxOutput().getResponseHeader().getErrorMessage();
			status = setActualTaxResponse.getSetActualTaxOutput().getResponseHeader().getStatus() == 0 ? "Success"
					: "Failure";
		}
		LOGGER.info("Apply tax status {} with error {}", status, errorMessage);
		return setActualTaxResponse;
	}

	private String generateTaxString(String cpsName, String taxExemption, String taxExemptionReason) {
	
		TaxAttributeString taxAttributeString = new TaxAttributeString();
		taxAttributeString.setCpsName(cpsName);
		if("yes".equals(taxExemption)) {
			taxAttributeString.setTaxExemptTxt(taxExemption);
			taxAttributeString.setTaxExemptRef(taxExemptionReason);
		}
		return taxAttributeString.toString();
	}

}
