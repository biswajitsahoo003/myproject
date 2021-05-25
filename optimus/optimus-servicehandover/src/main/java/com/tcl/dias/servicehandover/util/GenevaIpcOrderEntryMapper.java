package com.tcl.dias.servicehandover.util;

import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.GenevaIpcOrderEntry;
import com.tcl.dias.servicehandover.ipc.beans.createorder.AccountCreationInputBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CommonBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderRequestBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.ProductCreationInputBO;

@Component
public class GenevaIpcOrderEntryMapper {

	public GenevaIpcOrderEntry createAccountToGenevaIpcOrderMapper(CreateOrderRequestBO createOrderRequestBO) {
		CommonBO commonBO = createOrderRequestBO.getCommonInput().getComReqAttributes();
		AccountCreationInputBO accountCreationInputBO = createOrderRequestBO.getAccCreationInput().getAccountInput();
		GenevaIpcOrderEntry genevaIpcOrderEntry = new GenevaIpcOrderEntry();
		genevaIpcOrderEntry.setAccountingCurrency(commonBO.getAccountingCurrency());
		genevaIpcOrderEntry.setInfoCurrency(commonBO.getInfoCurrency());
		genevaIpcOrderEntry.setDayTelephone(accountCreationInputBO.getDayTelephone());
		genevaIpcOrderEntry.setBillHandlingCode(accountCreationInputBO.getBillHandlingCode());
		genevaIpcOrderEntry.setInvoicing_coName(commonBO.getInvoicingCoName());
		genevaIpcOrderEntry.setAdvanceBoo(commonBO.getAdvanceBoo());
		genevaIpcOrderEntry.setBillingEntity(commonBO.getBillingEntity());
		genevaIpcOrderEntry.setCustomerRef(commonBO.getCustomerRef());
		genevaIpcOrderEntry.setContactName(accountCreationInputBO.getContactName());
		genevaIpcOrderEntry.setBillingPeriod(accountCreationInputBO.getBillingPeriod());
		genevaIpcOrderEntry.setEmail(accountCreationInputBO.getEmail());
		genevaIpcOrderEntry.setFirstName(accountCreationInputBO.getFirstName());
		genevaIpcOrderEntry.setLastName(accountCreationInputBO.getLastName());
		genevaIpcOrderEntry.setCreditClass(accountCreationInputBO.getCreditClass());
		genevaIpcOrderEntry.setPayment_dueDate(commonBO.getPaymentDueDate());
		genevaIpcOrderEntry.setAccAddr1(accountCreationInputBO.getAccAddr1());
		genevaIpcOrderEntry.setAccAddr2(accountCreationInputBO.getAccAddr2());
		genevaIpcOrderEntry.setAccAddr3(accountCreationInputBO.getAccAddr3());
		genevaIpcOrderEntry.setAccCity(accountCreationInputBO.getAccCity());
		genevaIpcOrderEntry.setAccState(accountCreationInputBO.getAccState());
		genevaIpcOrderEntry.setAccZipcode(accountCreationInputBO.getAccZipcode());
		genevaIpcOrderEntry.setAccCountry(accountCreationInputBO.getAccCountry());
		genevaIpcOrderEntry.setGroupId(commonBO.getGroupId());
		genevaIpcOrderEntry.setSourceSystem(commonBO.getSourceSystem());
		genevaIpcOrderEntry.setRequestType(commonBO.getRequestType());
		genevaIpcOrderEntry.setActionType(commonBO.getActionType());
		genevaIpcOrderEntry.setServiceType(commonBO.getServiceType());
		genevaIpcOrderEntry.setProviderSegment(commonBO.getProviderSegment());
		genevaIpcOrderEntry.setSecsId(commonBO.getSecsId());
		genevaIpcOrderEntry.setCreationDate(accountCreationInputBO.getCreationDate());
		return genevaIpcOrderEntry;
		
	}
	
	public GenevaIpcOrderEntry createOrderToGenevaIpcOrderMapper(CreateOrderRequestBO createOrderRequestBO) {
		GenevaIpcOrderEntry genevaIpcOrderEntry = new GenevaIpcOrderEntry();
		CommonBO commonBO = createOrderRequestBO.getCommonInput().getComReqAttributes();
		ProductCreationInputBO prodBo = createOrderRequestBO.getProdCreationInput().getProductInput();
		genevaIpcOrderEntry.setGroupId(commonBO.getGroupId());
		genevaIpcOrderEntry.setSourceSystem(commonBO.getSourceSystem());
		genevaIpcOrderEntry.setRequestType(commonBO.getRequestType());
		genevaIpcOrderEntry.setActionType(commonBO.getActionType());
		genevaIpcOrderEntry.setCustomerRef(commonBO.getCustomerRef());
		genevaIpcOrderEntry.setAccountNum(commonBO.getAccountNum());
		genevaIpcOrderEntry.setInvoicing_coName(commonBO.getInvoicingCoName());
		genevaIpcOrderEntry.setBillingEntity(commonBO.getBillingEntity());
		genevaIpcOrderEntry.setProviderSegment(commonBO.getProviderSegment());
		genevaIpcOrderEntry.setAdvanceBoo(commonBO.getAdvanceBoo());
		genevaIpcOrderEntry.setServiceType(commonBO.getServiceType());
		genevaIpcOrderEntry.setProductName(prodBo.getProductName());
		genevaIpcOrderEntry.setAccountingCurrency(commonBO.getAccountingCurrency());
		genevaIpcOrderEntry.setCurrencyCode(prodBo.getCurrencyCode());
		genevaIpcOrderEntry.setCurrencyCodePrd(prodBo.getCurrencyCodePrd());
		genevaIpcOrderEntry.setInfoCurrency(commonBO.getInfoCurrency());
		genevaIpcOrderEntry.setContractGstinNo(prodBo.getCONTRACTINGGSTINNO());
		genevaIpcOrderEntry.setContractGstinAddress(prodBo.getCONTRACTGSTINADDRESS());
		genevaIpcOrderEntry.setTaxExempt_ref(prodBo.getTaxExemptRef());
		genevaIpcOrderEntry.setTaxExempt_txt(prodBo.getTaxExemptTxt());
		genevaIpcOrderEntry.setPayment_dueDate(commonBO.getPaymentDueDate());
		genevaIpcOrderEntry.setCustomerName(prodBo.getCustomerName());
		//genevaIpcOrderEntry.setContractDuration(Integer.parseInt(prodBo.getContractDuration()));
		genevaIpcOrderEntry.setCustomer_orderNum(prodBo.getCustomerOrderNum());
		genevaIpcOrderEntry.setContractingAddress(prodBo.getCONTRACTINGADDRESS());
		genevaIpcOrderEntry.setTotalArc(prodBo.getTotalArc());
		genevaIpcOrderEntry.setTotalNrc(prodBo.getTotalNrc());
		genevaIpcOrderEntry.setOrderType(prodBo.getOrderType());
		genevaIpcOrderEntry.setChange_orderType(prodBo.getChangeOrderType());
		genevaIpcOrderEntry.setCreationDatePrd(prodBo.getCreationDatePrd());
		genevaIpcOrderEntry.setCommissionDate(prodBo.getCommissionDate());
		genevaIpcOrderEntry.setBillGeneration_date(prodBo.getBillGenerationDate());
		genevaIpcOrderEntry.setBillActivation_date(prodBo.getBillActivationDate());
		genevaIpcOrderEntry.setSiteGstinNo(prodBo.getSITEGSTINNO());
		genevaIpcOrderEntry.setSiteGstinAddress(prodBo.getSITEGSTINADDRESS());
		genevaIpcOrderEntry.setSiteEnd(prodBo.getSITEEND());
		genevaIpcOrderEntry.setCustomerType(prodBo.getCustomerType());
		genevaIpcOrderEntry.setCircuitCount(prodBo.getCiruitCount());
		genevaIpcOrderEntry.setInvoicing_coName_prd(prodBo.getInvocingCoNamePrd());
		genevaIpcOrderEntry.setProdAddr1(prodBo.getProdAddr1());
		genevaIpcOrderEntry.setProdAddr2(prodBo.getProdAddr2());
		genevaIpcOrderEntry.setProdAddr3(prodBo.getProdAddr3());
		genevaIpcOrderEntry.setProdCity(prodBo.getProdCity());
		genevaIpcOrderEntry.setProdState(prodBo.getProdState());
		genevaIpcOrderEntry.setProdCountry(prodBo.getProdCountry());
		genevaIpcOrderEntry.setProdZipcode(prodBo.getProdZipcode());
		genevaIpcOrderEntry.setProdQuantity(Integer.parseInt(prodBo.getProdQuantity()));
		genevaIpcOrderEntry.setSiteAAddress(prodBo.getSITEAADDRESS());
		genevaIpcOrderEntry.setSourceProductSeq(commonBO.getSourceProductSeq());
		genevaIpcOrderEntry.setSourceOldProdSeq(prodBo.getSourceOldProdSeq());
		genevaIpcOrderEntry.setCopfId(prodBo.getCopfId());
		genevaIpcOrderEntry.setProrateBoo(prodBo.getProrateBoo());
		genevaIpcOrderEntry.setChargePeriod(prodBo.getChargePeriod());
		genevaIpcOrderEntry.setRefundBoo(prodBo.getRefundBoo());
		genevaIpcOrderEntry.setServiceId(prodBo.getServiceId());
		genevaIpcOrderEntry.setOvrdn_initPrice(prodBo.getOvrdnIntPrice());
		genevaIpcOrderEntry.setOvrdn_periodicPrice(prodBo.getOvrdnPeriodicPrice().toString());
		genevaIpcOrderEntry.setAttributeString(prodBo.getAttributeString());
		return genevaIpcOrderEntry;
		
	}
	
	
}
