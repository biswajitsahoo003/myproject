package com.tcl.dias.billing.service.v1;

import static com.tcl.dias.billing.constants.BillingConstants.PARTNER;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tcl.dias.billing.constants.ExceptionConstants;
import com.tcl.dias.billing.dispute.service.beans.BalanceSheet;
import com.tcl.dias.billing.dispute.service.beans.SOAPDFDetail;
import com.tcl.dias.billing.dispute.service.beans.SoaPDFBean;
import com.tcl.dias.billing.utils.BillingUtils;
import com.tcl.dias.common.beans.LeSapCodeResponse;
import com.tcl.dias.common.beans.SapCodeRequest;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.PartnerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class FinanceService {

	@Autowired
	UserInfoUtils userInfoUtils;

	private @Autowired
	@Qualifier("financeTemplate")
	JdbcTemplate financeTemplate;

	@Value("${rabbitmq.le.sap.queue}")
	private String leSapQueue;
	
	@Value("${statement.date}")
	private String statementDate;

	@Value("${rabbitmq.partner.le.sap.queue}")
	private String partnerLeSapQueue;

	@Autowired
	MQUtils mqUtils;

	private static final Logger LOGGER = LoggerFactory.getLogger(FinanceService.class);

	public List<Map<String, Object>> getAllCustomerLedgerDetails(String requestParam) throws TclCommonException {
		String resp = null;
		BillingUtils.validateSqlInjectionInput(requestParam);//handle sql injection
		List<String> sapCodes = new ArrayList<>();
		List<Integer> leIds = new ArrayList<>();
		SapCodeRequest request = new SapCodeRequest();
		if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			leIds = userInfoUtils.getPartnerDetails().stream().map(PartnerDetail::getPartnerLeId)
					.collect(Collectors.toList());
			request.setCustomerLeIds(leIds);
			resp = (String) mqUtils.sendAndReceive(partnerLeSapQueue, Utils.convertObjectToJson(request));
		} else {
			leIds = getLegalEntityIds();
			request.setCustomerLeIds(leIds);
			resp = (String) mqUtils.sendAndReceive(leSapQueue, Utils.convertObjectToJson(request));
		}

		if (resp != null) {
			LeSapCodeResponse response = (LeSapCodeResponse) Utils.convertJsonToObject(resp, LeSapCodeResponse.class);
			if (response != null && !response.getLeSapCodes().isEmpty()) {
				response.getLeSapCodes().forEach(sap -> {
					sapCodes.add(sap.getCodeValue());
				});
			}
		}
		String sap = sapCodes.stream().map(ba -> "'" + ba + "'").collect(Collectors.joining(","));
		if (requestParam != null && !requestParam.isEmpty()) {
			String p = "'" + requestParam + "'";
			return financeTemplate.queryForList("select * from ac_customer_ledger_v2 where bill_payment in ("
					+ p + ") and customer in(" + sap + ")" );
		} else {
			return new JdbcTemplate()
					.queryForList("select * from ac_customer_ledger_v2 where customer in(" + sap + ") ");
		}
	}

	public SoaPDFBean getSOA_PDF(List<String> requestParams, String sap) throws TclCommonException {
		BillingUtils.validateSqlInjectionInput(sap);//handle sql injection
		SoaPDFBean soaPDFBean = new SoaPDFBean();
		soaPDFBean.setStatementDate(statementDate);

		if (requestParams != null && !requestParams.isEmpty()) {
			requestParams.forEach((param -> {

				if (param.equals("CREDIT NOTE")) {
					List<Map<String, Object>> creditDetails = buildSOA(financeTemplate, "'" + param + "'",
							"'" + sap + "'");
					if (creditDetails != null && !creditDetails.isEmpty()) {
						soaPDFBean.setCreditNotes(mapDBValTOPDFBean(creditDetails, soaPDFBean));
					}
				}
				if (param.equals("INVOICE")) {
					List<Map<String, Object>> invoiceDetailsList = buildSOA(financeTemplate, "'" + param + "'",
							"'" + sap + "'");
					if (invoiceDetailsList != null && !invoiceDetailsList.isEmpty()) {
						soaPDFBean.setInvoices(mapDBValTOPDFBean(invoiceDetailsList, soaPDFBean));
					}
				}
				if (param.equals("PAYMENT")) {
					List<Map<String, Object>> paymentDetailsList = buildSOA(financeTemplate, "'" + param + "'",
							"'" + sap + "'");
					if (paymentDetailsList != null && !paymentDetailsList.isEmpty()) {
						soaPDFBean.setPayments(mapDBValTOPDFBean(paymentDetailsList, soaPDFBean));
					}
				}
				if (param.equals("TDS?PROVISION")) {
					List<Map<String, Object>> tdsDetails = buildSOA(financeTemplate, "'" + param + "'", "'" + sap + "'");
					if (tdsDetails != null && !tdsDetails.isEmpty()) {
						soaPDFBean.setTdsProvisions(mapDBValTOPDFBean(tdsDetails, soaPDFBean));
					}
				}
				
				if(param.equals("TDS RECEIVED")) {
					
					List<Map<String, Object>> tdsDetails = buildSOA(financeTemplate, "'" + param + "'", "'" + sap + "'");
					if (tdsDetails != null && !tdsDetails.isEmpty()) {
						soaPDFBean.setTdsCertReceivedList(mapDBValTOPDFBean(tdsDetails, soaPDFBean));
					}
					
				}
			}));

		}

		if (soaPDFBean != null) {

			Map<String, BalanceSheet> balMap = new HashMap<>();

			if (soaPDFBean.getInvoices() != null) {

				Map<String, List<SOAPDFDetail>> invoiceCurrencySpitMap = soaPDFBean.getInvoices().stream()
						.collect(Collectors.groupingBy(w -> w.getCurrency()));

				for (Map.Entry<String, List<SOAPDFDetail>> entry : invoiceCurrencySpitMap.entrySet()) {
					BigDecimal billingSum = entry.getValue().stream().map(SOAPDFDetail::getOutAmtCalc)
							.reduce(BigDecimal::add).get();

					BalanceSheet bsheet = new BalanceSheet();
					bsheet.setCocd(soaPDFBean.getName());
					bsheet.setSapcode(soaPDFBean.getSapcode());
					bsheet.setCocd(soaPDFBean.getCocd());
					bsheet.setCurrency(entry.getKey());
					bsheet.setName(soaPDFBean.getName());

					if (balMap.containsKey(entry.getKey())) {
						BalanceSheet bal = balMap.get(entry.getKey());
						bal.setBilling(billingSum);
					} else {
						bsheet.setBilling(billingSum);
						balMap.put(entry.getKey(), bsheet);
					}
				}

			}
			if (soaPDFBean.getTdsProvisions() != null) {
				Map<String, List<SOAPDFDetail>> tdsCurrencySpitMap = soaPDFBean.getTdsProvisions().stream()
						.collect(Collectors.groupingBy(w -> w.getCurrency()));

				for (Map.Entry<String, List<SOAPDFDetail>> entry : tdsCurrencySpitMap.entrySet()) {
					BigDecimal tdsSum = entry.getValue().stream().map(SOAPDFDetail::getOutAmtCalc)
							.reduce(BigDecimal::add).get();

					BalanceSheet bsheet = new BalanceSheet();
					bsheet.setCocd(soaPDFBean.getName());
					bsheet.setSapcode(soaPDFBean.getSapcode());
					bsheet.setCocd(soaPDFBean.getCocd());
					bsheet.setCurrency(entry.getKey());
					bsheet.setName(soaPDFBean.getName());

					if (balMap.containsKey(entry.getKey())) {
						BalanceSheet bal = balMap.get(entry.getKey());
						bal.setTdsProvisions(tdsSum);
					} else {
						bsheet.setTdsProvisions(tdsSum);
						balMap.put(entry.getKey(), bsheet);
					}
				}
			}
			if (soaPDFBean.getCreditNotes() != null) {

				Map<String, List<SOAPDFDetail>> creditCurrencySpitMap = soaPDFBean.getCreditNotes().stream()
						.collect(Collectors.groupingBy(w -> w.getCurrency()));

				for (Map.Entry<String, List<SOAPDFDetail>> entry : creditCurrencySpitMap.entrySet()) {
					BigDecimal creditSum = entry.getValue().stream().map(SOAPDFDetail::getOutAmtCalc)
							.reduce(BigDecimal::add).get();

					BalanceSheet bsheet = new BalanceSheet();
					bsheet.setCocd(soaPDFBean.getName());
					bsheet.setSapcode(soaPDFBean.getSapcode());
					bsheet.setCocd(soaPDFBean.getCocd());
					bsheet.setCurrency(entry.getKey());
					bsheet.setName(soaPDFBean.getName());

					if (balMap.containsKey(entry.getKey())) {
						BalanceSheet bal = balMap.get(entry.getKey());
						bal.setCreditNotes(creditSum);
						balMap.put(entry.getKey(), bal);
					} else {
						bsheet.setCreditNotes(creditSum);
						balMap.put(entry.getKey(), bsheet);
					}
				}
			}
			if (soaPDFBean.getPayments() != null) {

				Map<String, List<SOAPDFDetail>> payCurrencySpitMap = soaPDFBean.getPayments().stream()
						.collect(Collectors.groupingBy(w -> w.getCurrency()));

				for (Map.Entry<String, List<SOAPDFDetail>> entry : payCurrencySpitMap.entrySet()) {
					BigDecimal collectSum = entry.getValue().stream().map(SOAPDFDetail::getOutAmtCalc)
							.reduce(BigDecimal::add).get();

					BalanceSheet bsheet = new BalanceSheet();
					bsheet.setCocd(soaPDFBean.getName());
					bsheet.setSapcode(soaPDFBean.getSapcode());
					bsheet.setCocd(soaPDFBean.getCocd());
					bsheet.setCurrency(entry.getKey());
					bsheet.setCurrency(entry.getKey());
					bsheet.setName(soaPDFBean.getName());
					
					if (balMap.containsKey(entry.getKey())) {
						BalanceSheet bal = balMap.get(entry.getKey());
						bal.setCollection(collectSum);
						balMap.put(entry.getKey(), bal);
					} else {
						bsheet.setCollection(collectSum);
						balMap.put(entry.getKey(), bsheet);
					}
				}
			}
			
			if (soaPDFBean.getTdsCertReceivedList() != null) {

				Map<String, List<SOAPDFDetail>> tdsCertReceivedSpitMap = soaPDFBean.getTdsCertReceivedList().stream()
						.collect(Collectors.groupingBy(w -> w.getCurrency()));

				for (Map.Entry<String, List<SOAPDFDetail>> entry : tdsCertReceivedSpitMap.entrySet()) {
					BigDecimal collectSum = entry.getValue().stream().map(SOAPDFDetail::getOutAmtCalc).reduce(BigDecimal::add).get();

					BalanceSheet bsheet = new BalanceSheet();
					bsheet.setCocd(soaPDFBean.getName());
					bsheet.setSapcode(soaPDFBean.getSapcode());
					bsheet.setCocd(soaPDFBean.getCocd());
					bsheet.setCurrency(entry.getKey());
					bsheet.setCurrency(entry.getKey());
					bsheet.setName(soaPDFBean.getName());
					
					if (balMap.containsKey(entry.getKey())) {
						BalanceSheet bal = balMap.get(entry.getKey());
						bal.setTdsCertReceived(collectSum);
						balMap.put(entry.getKey(), bal);
					} else {
						bsheet.setTdsCertReceived(collectSum);
						balMap.put(entry.getKey(), bsheet);
					}
					
				}
			}
			
			
		List<BalanceSheet> balanceSheetList = balMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
		
		balanceSheetList.stream().forEach(
				x ->
				{
					if(x.getBilling() == null) {
						x.setBilling(new BigDecimal(0.0));
					}
					if(x.getCollection() == null) {
						x.setCollection(new BigDecimal(0.0));
					}
					if(x.getTdsCertReceived() == null) {
						x.setTdsCertReceived(new BigDecimal(0.0));
					}
					if(x.getTdsProvisions() == null) {
						x.setTdsProvisions(new BigDecimal(0.0));
					}
					if(x.getCreditNotes() == null) {
						x.setCreditNotes(new BigDecimal(0.0));
					}
					x.setClosingBal(x.getBilling().add( x.getCollection()).add( x.getTdsCertReceived()).add(x.getTdsProvisions() ).add( x.getCreditNotes()));
				}
				
				);
		
			soaPDFBean.setBalSheet(balanceSheetList);
		}
		return soaPDFBean;
	}

	private List<SOAPDFDetail> mapDBValTOPDFBean(List<Map<String, Object>> invoiceDetailsList, SoaPDFBean soaPDFBean) {
		List<SOAPDFDetail> invoicesListForPDF = invoiceDetailsList.stream().map(map -> {
			SOAPDFDetail invoiceDetl = new SOAPDFDetail();
			if (map.get("customer") != null) {
				invoiceDetl.setSapcode(String.valueOf(map.get("customer")));
				soaPDFBean.setSapcode(String.valueOf(map.get("customer")));
			}
			if (map.get("invoice_num_cheque_num_tds_cert_num") != null) {
				invoiceDetl.setInvoiceNum(String.valueOf(map.get("invoice_num_cheque_num_tds_cert_num")));
			}
			if (map.get("invoice_receipt_date") != null) {
				invoiceDetl.setInvoiceDate(BillingUtils.getDate(map.get("invoice_receipt_date")));
			}
			if (map.get("invoice_receipt_curreny") != null) {
				invoiceDetl.setCurrency(String.valueOf(map.get("invoice_receipt_curreny")));
			}
			if (map.get("orignal_invoice_amount") != null) {
				invoiceDetl.setInvoiceAmt(String.valueOf(map.get("orignal_invoice_amount")));
			}
			if (map.get("due_date") != null) {
				invoiceDetl.setDueDate(BillingUtils.getDate(map.get("due_date")));
			}
			if (map.get("outstanding_amount") != null) {
				String outamt = String.valueOf(map.get("outstanding_amount"));
				invoiceDetl.setOutAmt(String.valueOf(map.get("outstanding_amount"))); // for PDF generation
				if (outamt.charAt(outamt.length() - 1) == '-') {
					invoiceDetl.setOutAmtCalc(new BigDecimal("-" + outamt.substring(0, outamt.length() - 1)));
				} else {
					invoiceDetl.setOutAmtCalc(new BigDecimal(outamt)); // for calculation
				}
			}
			// Balance sheet mappings
			if (map.get("customer_name") != null) {
				soaPDFBean.setName(String.valueOf(map.get("customer_name")));

			}
			if (map.get("company_code") != null) {
				soaPDFBean.setCocd(String.valueOf(map.get("company_code")));
			}
			return invoiceDetl;
		}).collect(Collectors.toList());
		return invoicesListForPDF;
	}

	public List<Map<String, Object>> buildSOA(JdbcTemplate jdbcTemplate, String param, String sap) {
		
		return jdbcTemplate.queryForList(
				"select * from ac_customer_ledger_v2 where bill_payment in (" + param + ") and customer =" + sap);
	}

	private List<Integer> getLegalEntityIds() throws TclCommonException {
		// Get the associated customers info
		List<CustomerDetail> customers = getCustomerDetails();
		
		List<Integer> leIds = customers.stream()
				.filter(((Predicate<CustomerDetail>)(Objects::isNull)).negate()) // filter null values
				.map(CustomerDetail::getCustomerLeId) // extract LE id
				.collect(Collectors.toList()); // add to list
		return leIds;
	}

	/**
	 * getCustomerDetails -
	 * 		Returns the list of associated customer details for the logged in user 
	 * @return List<CustomerDetail>
	 * @throws TclCommonException
	 */
	private List<CustomerDetail> getCustomerDetails() throws TclCommonException {
		// Get the logged in user info
		UserInformation userInfo = getUserInformation();
		
		// Get the Legal Entity Ids from the user information
		List<CustomerDetail> customers = userInfo.getCustomers();
		if (Objects.isNull(customers)) {
			LOGGER.info("Customer information is null for user : {}", Utils.getSource());
			throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMERS_FOR_USER);
		}
		return customers;
	}
	
	/**
	 * @return
	 * @throws TclCommonException
	 */
	private UserInformation getUserInformation() throws TclCommonException {
		// Get the information of logged in user and validate
		UserInformation userInfo = userInfoUtils.getUserInformation(Utils.getSource());
		if (Objects.isNull(userInfo)) {
			LOGGER.info("UserInformation is null for user : {} ", Utils.getSource());
			throw new TclCommonException(ExceptionConstants.ERROR_FECTHING_CUSTOMERS_FOR_USER);
		}
		return userInfo;
	}

}
