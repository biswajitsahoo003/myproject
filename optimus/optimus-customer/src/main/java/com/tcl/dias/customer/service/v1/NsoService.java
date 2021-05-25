package com.tcl.dias.customer.service.v1;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.customer.bean.BillingAttributeResponse;
import com.tcl.dias.customer.constants.BillingAttributeConstants;
import com.tcl.dias.customer.entity.entities.MstLeAttribute;
import com.tcl.dias.customer.entity.repository.CustomerLeAttributeValueRepository;
import com.tcl.dias.customer.entity.repository.MstLeAttributeRepository;

/**
 * 
 * Nso Customer master data
 * 
 *
 * @author KarMani
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Service
public class NsoService {

	@Autowired
	MstLeAttributeRepository mstLeAttributeRepository;

	@Autowired
	CustomerLeAttributeValueRepository customerLeAttributeValueRepository;

	/**
	 * 
	 * getBillingAttributeValues
	 * 
	 * @return
	 */
	public BillingAttributeResponse getBillingAttributeValues() {
		BillingAttributeResponse billingAttributeResponse = new BillingAttributeResponse();
		Optional<MstLeAttribute> mstLeAttributeBillingType = mstLeAttributeRepository
				.findByName(BillingAttributeConstants.BILLING_TYPE);
		List<String> attributeBillingType = customerLeAttributeValueRepository
				.findByMstLeAttributeId(mstLeAttributeBillingType.get().getId());
		billingAttributeResponse.setBillingType(attributeBillingType);

		Optional<MstLeAttribute> mstLeAttributeBillingMethod = mstLeAttributeRepository
				.findByName(BillingAttributeConstants.BILLING_METHOD);
		List<String> attributeBillingMethod = customerLeAttributeValueRepository
				.findByMstLeAttributeId(mstLeAttributeBillingMethod.get().getId());
		billingAttributeResponse.setBillingMethod(attributeBillingMethod);

		Optional<MstLeAttribute> mstLeAttributeBillingFrequency = mstLeAttributeRepository
				.findByName(BillingAttributeConstants.BILLING_FREQUENCY);
		List<String> attributeBillingFrequency = customerLeAttributeValueRepository
				.findByMstLeAttributeId(mstLeAttributeBillingFrequency.get().getId());
		billingAttributeResponse.setBillingFrequency(attributeBillingFrequency);

		Optional<MstLeAttribute> mstLeAttributeBillingCurrency = mstLeAttributeRepository
				.findByName(BillingAttributeConstants.BILLING_CURRENCY);
		List<String> attributeBillingCurrency = customerLeAttributeValueRepository
				.findByMstLeAttributeId(mstLeAttributeBillingCurrency.get().getId());
		billingAttributeResponse.setBillingCurrency(attributeBillingCurrency);

		Optional<MstLeAttribute> mstLeAttributePaymentCurrency = mstLeAttributeRepository
				.findByName(BillingAttributeConstants.PAYMENT_CURRENCY);
		List<String> attributePaymentCurrency = customerLeAttributeValueRepository
				.findByMstLeAttributeId(mstLeAttributePaymentCurrency.get().getId());
		billingAttributeResponse.setPaymentCurrency(attributePaymentCurrency);

		Optional<MstLeAttribute> mstLeAttributeInvoiceMethod = mstLeAttributeRepository
				.findByName(BillingAttributeConstants.INVOICE_METHOD);
		List<String> attributeInvoiceMethod = customerLeAttributeValueRepository
				.findByMstLeAttributeId(mstLeAttributeInvoiceMethod.get().getId());
		billingAttributeResponse.setInvoiceMethod(attributeInvoiceMethod);

		Optional<MstLeAttribute> mstLeAttributePaymentTerm = mstLeAttributeRepository
				.findByName(BillingAttributeConstants.PAYMENT_TERM);
		List<String> attributePaymentTerm = customerLeAttributeValueRepository
				.findByMstLeAttributeId(mstLeAttributePaymentTerm.get().getId());
		billingAttributeResponse.setPaymentTerm(attributePaymentTerm);

		return billingAttributeResponse;
	}

}
