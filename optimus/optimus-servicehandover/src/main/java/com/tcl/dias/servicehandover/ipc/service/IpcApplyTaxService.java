package com.tcl.dias.servicehandover.ipc.service;

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

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.servicefulfillment.entity.entities.IpcChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScChargeLineitem;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicefulfillment.entity.repository.IpcChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.AccountInputData;
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
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
public class IpcApplyTaxService {

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
	IpcChargeLineitemRepository ipcChargeLineitemRepository;

	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	ScServiceAttributeRepository serviceAttributeRepository;

	@Autowired
	IpcBillingAccountAndLineItemService ipcBillingAccountAndLineItemService;
	
	@Value("${oms.o2c.macd.queue}")
	String omsO2CMacdQueue;

	@Autowired
	MQUtils mqUtils;
	
	String taxExemption =null;
	
	String taxExemptionReason=null;

	private static final Logger LOGGER = LoggerFactory.getLogger(IpcApplyTaxService.class);

	/**
	 * 
	 * This method Applies Tax post product commissioning.
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
		Map<String, String> srvAttrMap = commonFulfillmentUtils.getServiceAttributesAttributes(serviceAttributeRepository.findByScServiceDetail_idAndAttributeNameIn(scServiceDetail.getId(), Arrays.asList("sapCode","billStartDate",CommonConstants.FINAL_CPS)));
		Map<String, Object> odrAttrMap = commonFulfillmentUtils.getOrderAttributes(scOrderAttributeRepository.findByAttributeNameInAndScOrder(Arrays.asList("isTaxExemption","taxExemptionReason"), scServiceDetail.getScOrder()));
		taxExemption= odrAttrMap!=null && odrAttrMap.get("isTaxExemption")!=null && !String.valueOf(odrAttrMap.get("isTaxExemption")).trim().isEmpty() ? String.valueOf(odrAttrMap.get("isTaxExemption")) : "no";
		if("yes".equals(taxExemption)) {
			taxExemption=IpcConstants.IPC_TAX_EXEMPT;
			if (odrAttrMap.containsKey("taxExemptionReason") && odrAttrMap.get("taxExemptionReason") != null) {
				taxExemptionReason=WordUtils.capitalize(String.valueOf(odrAttrMap.containsKey("taxExemptionReason")).toLowerCase());
			}
		}
		
		SetActualTaxResponse setActualTaxResponse = new SetActualTaxResponse();

		SetActualTax setActualTax = new SetActualTax();
		SetActualTaxInput setActualTaxInput = new SetActualTaxInput();
		Security security = new Security();
		CommissionDetails commissionDetails = new CommissionDetails();

		security.setSourceSystem("OPSPORTALBPM");
		security.setUniqueKey(String.valueOf(System.currentTimeMillis()));

		List<IpcChargeLineitem> chargeLineitems = ipcChargeLineitemRepository.findByServiceIdAndStatus(serviceId, IpcConstants.SUCCESS+IpcConstants.UNDERSCORE+IpcConstants.COMPLETE_THE_TAX_CAPTURE_TASK);
		chargeLineitems.stream().forEach(lineitem -> {
			CommissionDetail commissionDetail = new CommissionDetail();
			commissionDetail.setCRNId(srvAttrMap.get("sapCode"));
			commissionDetail.setAccountNumber(lineitem.getAccountNumber());
			commissionDetail.setPOSProductSequence(lineitem.getSourceProductSequence());
			commissionDetail.setProductName(lineitem.getChargeLineitem());
			commissionDetail.setCommissionDate(srvAttrMap.get("billStartDate"));
			commissionDetail.setTaxAttributes(generateTaxString(srvAttrMap.get("finalCps"),taxExemption,taxExemptionReason));
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
			status = setActualTaxResponse.getSetActualTaxOutput().getResponseHeader().getStatus() == 0 ? IpcConstants.SUCCESS
					: IpcConstants.FAILURE;
		}
		LOGGER.info("Apply tax status {} with errorMessage if any :{}", status, errorMessage);
		if(status.equals(IpcConstants.SUCCESS)) {
			chargeLineitems.stream().forEach(lI -> lI.setStatus(IpcConstants.SUCCESS));
			ipcBillingAccountAndLineItemService.triggerPayPerUseCommissionedInfoToCatalyst(scServiceDetail);
			ScOrder scOrder= scOrderRepository.findByOpOrderCodeAndIsActive(scServiceDetail.getScOrderUuid(), IpcConstants.Y);
			if(scOrder!=null) {
				scServiceDetail.setServiceStatus(IpcConstants.COMPLETED);
				scServiceDetailRepository.save(scServiceDetail);
				try {
					mqUtils.send(omsO2CMacdQueue, scOrder.getUuid()+IpcConstants.SPECIAL_CHARACTER_COMMA+scServiceDetail.getPopSiteCode());
				}catch (Exception ex) {
					LOGGER.error("Exception while trigerring mqutils call for updating the stage of macd order to MACD_ORDER_COMMISSIONED");
				}
			}
		} else {
			chargeLineitems.stream().forEach(lI -> lI.setStatus(IpcConstants.FAILURE));
		}
		ipcChargeLineitemRepository.saveAll(chargeLineitems);
		return setActualTaxResponse;
	}

	private String generateTaxString(String cpsName, String taxExemption, String taxExemptionReason) {
	
		TaxAttributeString taxAttributeString = new TaxAttributeString();
		taxAttributeString.setCpsName(cpsName);
		if(IpcConstants.IPC_TAX_EXEMPT.equals(taxExemption)) {
			taxAttributeString.setTaxExemptTxt(taxExemption);
			taxAttributeString.setTaxExemptRef(taxExemptionReason);
		}
		return taxAttributeString.toString();
	}

}
