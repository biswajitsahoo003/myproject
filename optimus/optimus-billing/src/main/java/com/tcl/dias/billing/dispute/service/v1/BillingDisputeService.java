package com.tcl.dias.billing.dispute.service.v1;

import static com.tcl.dias.billing.constants.BillingConstants.PARTNER;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tcl.dias.billing.constants.BillingConstants;
import com.tcl.dias.billing.constants.ExceptionConstants;
import com.tcl.dias.billing.dispute.ticket.beans.AddComments;
import com.tcl.dias.billing.dispute.ticket.beans.AddCommentsResponse;
import com.tcl.dias.billing.dispute.ticket.beans.BulkTicketCreation;
import com.tcl.dias.billing.dispute.ticket.beans.BulkTicketCreationResponse;
import com.tcl.dias.billing.dispute.ticket.beans.CreateSingleTicket;
import com.tcl.dias.billing.dispute.ticket.beans.CreateSingleTicketResponse;
import com.tcl.dias.billing.dispute.ticket.beans.NGPCESBulkTicketDetails;
import com.tcl.dias.billing.dispute.ticket.beans.NGPCESSingleTicketDetails;
import com.tcl.dias.billing.dispute.ticket.beans.UpdateCommentsAttachment;
import com.tcl.dias.billing.dispute.ticket.beans.UpdateCommentsAttachmentResponse;
import com.tcl.dias.billing.utils.BillingUtils;
import com.tcl.dias.common.beans.LeSapCodeBean;
import com.tcl.dias.common.beans.LeSapCodeResponse;
import com.tcl.dias.common.beans.SapCodeRequest;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.PartnerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.webserviceclient.beans.SoapRequest;
import com.tcl.dias.webserviceclient.service.GenericWebserviceClient;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Class BillingDisputeService
 * 	provides
 * 		- get all billing disputes for all the sap codes the logged in user is entitled to
 * 		- get related billing disputes for an invoice the logged in user is entitled to
 * 		- raise a ticket on an invoice the logged in user is entitled to
 * 		- attach files on an raised ticket on an invoice the logged in user is entitled to
 * @author amuthiah
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class BillingDisputeService {
	
	@Value("${ces.portal.url}")
	private String cesPortalUrl;
	
	@Value("${ces.base.url}")
	private String cesBaseUrl;
	
	@Value("${ces.single.ticket}")
	private String cesSingleTicket;
	
	@Value("${ces.bulk.ticket}")
	private String cesBulkBicket;
	
	@Value("${ces.add.comments}")
	private String cesAddComments;
	
	@Value("${ces.update.comments.attachment}")
	private String cesUpdateUommentsAttachment;

	@Value("${rabbitmq.le.sap.queue}")
	private String leSapQueue;
	
	@Value("${rabbitmq.le.cpny.sap.queue}")
	private String leCpnySapQueue;

	@Value("${rabbitmq.partner.le.sap.queue}")
	private String partnerLeSapQueue;
	
	@Autowired
	MQUtils mqUtils;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	@Qualifier("invoicesTemplate")
	JdbcTemplate invoicesTemplate;
	
	@Autowired
	@Qualifier("gbsInvoicesTemplate")
	JdbcTemplate gbsInvoicesTemplate;

	@Autowired
	@Qualifier("billingDisputeTicketsTemplate")
	JdbcTemplate billingDisputeTicketsTemplate;

	@Autowired
    private GenericWebserviceClient genericWebserviceClient;

	
	private static final Logger LOGGER = LoggerFactory.getLogger(BillingDisputeService.class);

	/**
	 * getLegalEntityIds -
	 * 		Returns the associated legal entity ids for the logged in user
	 * @return List<Integer>
	 * @throws TclCommonException
	 */
	private List<Integer> getLegalEntityIds() throws TclCommonException{
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
			throw new TclCommonException(ExceptionConstants.ERROR_FETCHING_USERINFO);
		}
		return userInfo;
	}
	
	/**
	 * createBillingDisputeTicket -
	 * 		creates a billing dispute ticket on an invoice
	 * 
	 * @param NGPCESSingleTicketDetails ticket
	 * @return CreateSingleTicketResponse
	 * @throws TclCommonException
	 */
	public CreateSingleTicketResponse createBillingDisputeTicket(
			NGPCESSingleTicketDetails ticket) throws TclCommonException{
		
		if("CBF".equals(ticket.getSource())) {
			//validate invoice
			List<String> sapCodes = getSAPCodes();
			validateInvoice(sapCodes, ticket.getInvoiceNo());
		}
		//prepare the request
		CreateSingleTicket createSingleTicket = new CreateSingleTicket();
		createSingleTicket.setInputDetails(ticket);
		SoapRequest soapRequest = prepareSoapRequest(createSingleTicket, cesSingleTicket);

		//invoke the webservice
		CreateSingleTicketResponse createSingleTicketResponse = genericWebserviceClient.
				doSoapCallForObject(soapRequest, CreateSingleTicketResponse.class);
		
		//return the response
		return createSingleTicketResponse;
	}
	
	/**
	 * createBulkBillingDisputeTicket -
	 * 		creates multiple billing dispute tickets one for each invoice supplied
	 * 
	 * @param NGPCESBulkTicketDetails tickets
	 * @return BulkTicketCreationResponse
	 * @throws TclCommonException
	 */
	public BulkTicketCreationResponse createBulkBillingDisputeTicket(
			NGPCESBulkTicketDetails tickets) throws TclCommonException{
		
		tickets.getSource().getItem().stream().forEach(source -> {
			if("CBF".equals(source)) {
				try {
					//validate invoice - only for CBF as of now. GBS will be taken care when GBS sap codes is taken into CES systems.
					List<String> sapCodes = getSAPCodes();
					validateInvoice(sapCodes, tickets.getInvoiceNo().getItem());
				} catch (TclCommonException ex) {
					LOGGER.error(ExceptionConstants.INVALID_SOURCE_TYPE, ExceptionUtils.getStackTrace(ex));
				}
			}
		});

		//prepare the request
		BulkTicketCreation bulkTicketCreation = new BulkTicketCreation();
		bulkTicketCreation.setInputDetails(tickets);
		SoapRequest soapRequest =  prepareSoapRequest(bulkTicketCreation, cesBulkBicket);

		//invoke the webservice
		BulkTicketCreationResponse bulkTicketCreationResponse = genericWebserviceClient.
				doSoapCallForObject(soapRequest, BulkTicketCreationResponse.class);
		
		//return the response
		return bulkTicketCreationResponse;
	}
	
	/**
	 * addComments -
	 * 		add comments to the ticket 
	 * 
	 * @param List<String> invoices
	 * @param AddComments tickets
	 * @return AddCommentsResponse
	 * @throws TclCommonException
	 */
	public AddCommentsResponse addComments(AddComments comments) throws TclCommonException{
		//validate ticket number
		List<String> sapCodes = getSAPCodes();
		validateTicketNumber(sapCodes, comments.getTicketNo());
		
		//prepare the request
		SoapRequest soapRequest = prepareSoapRequest(comments, cesAddComments);

		//invoke the request
		AddCommentsResponse addCommentsResponse = genericWebserviceClient.
				doSoapCallForObject(soapRequest, AddCommentsResponse.class);
		
		//return the response
		return addCommentsResponse;
	}

	/**
	 * updateCommentsAttachment -
	 * 		update the ticket with comments or attachments or both
	 * 
	 * @param List<String> invoices
	 * @param UpdateCommentsAttachment tickets
	 * @return UpdateCommentsAttachmentResponse
	 * @throws TclCommonException
	 */
	public UpdateCommentsAttachmentResponse updateCommentsAttachment(
			UpdateCommentsAttachment updates) throws TclCommonException{
		
		//validate ticket number
		List<String> sapCodes = getSAPCodes();
		validateTicketNumber(sapCodes, updates.getTicketNo());

		//prepare the request
		SoapRequest soapRequest =  prepareSoapRequest(updates, cesUpdateUommentsAttachment);

		//invoke the request
		UpdateCommentsAttachmentResponse updateCommentsAttachmentResponse = genericWebserviceClient.
				doSoapCallForObject(soapRequest, UpdateCommentsAttachmentResponse.class);
		
		//return the response
		return updateCommentsAttachmentResponse;
	}
	
	/**
	 * validateTicketNumber
	 * @param sapCodes
	 * @param ticketNo
	 * @throws TclCommonException
	 */
	private void validateTicketNumber(List<String> sapCodes, String ticketNo) throws TclCommonException{
		BillingUtils.validateSqlInjectionInput(ticketNo);//handle sql injection
		StringBuilder query = new StringBuilder(200).append("select bdt.sap_code from BPMLDAP.VSOL_ISSUE_DETAILS bdt");
		query.append(" where bdt.ticket_no='");
		query.append(ticketNo).append("'");
		
		List<Map<String, Object>> qres = billingDisputeTicketsTemplate.queryForList(query.toString());
		for(Map<String, Object> ticket: qres) {
			String sapCode = (String)ticket.get("sap_code");
			if (sapCodes.contains(sapCode)) {
				return;
			}
		}
		throw new TclCommonException(String.format(BillingConstants.TICKET_AUTH_MESSAGE, ticketNo));
	}

	/**
	 * Get billing dispute tickets for the current logged in user
	 * After validations 
	 * @return List<Map<String, Object>> containing all the ticket details.
	 * @throws TclCommonException
	 */
	
	public List<Map<String, Object>> queryBillingDisputeTickets(
			Map<String, String> queryParams) throws TclCommonException{
		
		// Get the associated SAP codes
		List<String> sapCodes = getSAPCodes();
		if(sapCodes.isEmpty()) {
			LOGGER.info("sap code empty");
		return new ArrayList<Map<String, Object>>();
		}
		
		StringBuilder query = new StringBuilder(200).append("select bdt.*, " +
				"DECODE (bdt.TICKET_STATUS, 'REJECT', 'Rejected', 'CLOSED', 'Resolved', 'FORWARD', 'WIP', 'OPEN', 'WIP', 'UNKNOWN') OPTIMUS_STATUS " +
				"from BPMLDAP.VSOL_ISSUE_DETAILS bdt");

		// Get invoice related billing disputes
		if (queryParams.containsKey("invoice_no")) {
			String invoiceNumber = queryParams.get("invoice_no");
			BillingUtils.validateSqlInjectionInput(invoiceNumber);//handle sql injection
			validateInvoice(sapCodes, invoiceNumber);
			query.append(" where bdt.DISPUTE_INVOICE_NO='").append(queryParams.get("invoice_no")).append("'");
			query.append(" and bdt.TICKET_STATUS in ('REJECT', 'CLOSED', 'FORWARD', 'OPEN')");
			//query.append(" and bdt.TICKET_STATUS not in ('CLOSED-SIGN OFF', 'CLOSED-DUP', 'Closure- Future date action', 'REJECT-REOPEN', 'Closed - NBD', 'REOPEN')");
			List<Map<String, Object>> billingDisputeDetails = billingDisputeTicketsTemplate.queryForList(query.toString());
			return billingDisputeDetails;
		}
		
		// Get all invoice disputes for 1 year
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, -1);
		c.add(Calendar.DAY_OF_MONTH, +1);
		Timestamp beforeDate = new Timestamp(c.getTimeInMillis());
		
		// make the list of sapids to sapid1,sapid2,sapid3 ... like string
		String bas = commaSeparatedString(sapCodes);
		
		// add condition for ticket created or updated is within a year
		query.append(" where bdt.sap_code in (" + bas + ")")
		.append(" and bdt.TICKET_STATUS in ('REJECT', 'CLOSED', 'FORWARD', 'OPEN')")
		.append(" and (bdt.ISSUE_REG_DATE >= '" + format.format(beforeDate) + "'")
		.append(" or bdt.LAST_UPDATED_DATE >= '" + format.format(beforeDate) + "')")
		.append(" order by bdt.ISSUE_REG_DATE desc");
		LOGGER.info(query.toString());
		List<Map<String, Object>> billingDisputeDetails = billingDisputeTicketsTemplate.queryForList(query.toString());
		return billingDisputeDetails;
	}
	
	/**
	 * queryBillingDisputeTicketComments -
	 * 		validates the ticket against sap codes
	 * 		and retrieves the comments
	 * 
	 * @param ticketNo - ticket No for which the comments needs to be retrieved
	 * @return List<Map<String, Object>>
	 * @throws TclCommonException 
	 */
	public List<Map<String, Object>> queryBillingDisputeTicketComments(String ticketNo) throws TclCommonException {
		//validate ticket number
		BillingUtils.validateSqlInjectionInput(ticketNo);//handle sql injection
		List<String> sapCodes = getSAPCodes();
		validateTicketNumber(sapCodes, ticketNo);
		StringBuilder query = new StringBuilder(200)
				.append("select bdt.ticket_no, rtrim(bdt.remarks) trimComments, bdt.last_updated_by, ")
				.append("bdt.last_updated_date from BPMLDAP.VSOL_REMARKS_LOG bdt where bdt.TICKET_NO='")
				.append(ticketNo)
				.append("'");
		List<Map<String, Object>> ticketComments = billingDisputeTicketsTemplate.queryForList(query.toString());
		return ticketComments;
	}

	/**
	 * validateInvoice -
	 * 		utility to validate single invoice
	 * @param sapIds
	 * @param invoiceNumber
	 * @throws TclCommonException
	 */
	private void validateInvoice(List<String> sapIds, String invoiceNumber) throws TclCommonException {
		List<String> invoices = new LinkedList<>();
		// Validate invoice_no
		invoices.add(invoiceNumber);
		validateInvoice(sapIds, invoices);
	}

	/**
	 * validateInvoice -
	 * 		utility to validate multiple invoice
	 * @param sapCodes - SAP codes associated with the logged in user
	 * @param invoices - user supplied invoices
	 * @throws TclCommonException
	 */
	private void validateInvoice(List<String> sapCodes, List<String> invoices) throws TclCommonException{
		
		if (invoices != null) {
			for (String invoice : invoices) {
				BillingUtils.validateSqlInjectionInput(invoice);// handle sql injection
			}
		}
		
		// validate inputs
		validateList(invoices, BillingConstants.SELECT_INVOICES);
		validateList(sapCodes, BillingConstants.NO_SAP_CODES_FOUND);
		
		// make the list of invoices to invoice1,invoice2,invoice3 ...
		String invoicesString = commaSeparatedString(invoices);

		// CBF - Get sapcode for the given invoices
		List<Map<String, Object>> invoiceDetails = invoicesTemplate.queryForList(
				"select BM.INVOICE_NO, BM.SAP_CODE, BM.GBS_SAP_CODE, BM.CAM_SAP_CODE "
				+ "from \"CBF\".bill_master BM where BM.INVOICE_NO in ("
				+ invoicesString + ")");
		
		// GBS - Get entitycode (companyId + orgNo) for the given invoices
		List<Map<String, Object>> gbsInvoiceDetails = gbsInvoicesTemplate.queryForList(
				"select CONCAT(BM.CPNY_ID, BM.org_no) entity_code from SEBS.GBS_INVCE_FOR_NGP BM "
				+ "where BM.full_invce_no in ("
				+ invoicesString + ")");
				
		// Validate the query result
		boolean isCbfEmpty = validateList(invoiceDetails, BillingConstants.NO_SAP_CODE_FOUND_FOR_INVOICENO);
		boolean isGbsEmpty = validateList(gbsInvoiceDetails, BillingConstants.NO_SAP_CODE_FOUND_FOR_INVOICENO);

		if(isCbfEmpty && isGbsEmpty)
			throw new TclCommonException(BillingConstants.NO_SAP_CODE_FOUND_FOR_INVOICENO);

		// check the sap code from invoice match the authorized sap code of this user, from both CBF & GBS
		// if no throw exception
		boolean isFoundInCbf = false;
		boolean isFoundInGbs = false;

		// check if valid invoice no's using sap/entity codes. if not, throw error.
		if(!invoiceDetails.isEmpty()) {
			for (Map<String, Object> invoice: invoiceDetails) {
				isFoundInCbf = (sapCodes.contains(((String)invoice.get("sap_code")).trim())
						|| sapCodes.contains(((String)invoice.get("gbs_sap_code")).trim())
						|| sapCodes.contains(((String)invoice.get("cam_sap_code")).trim()));
				if(isFoundInCbf)
					break;
			}
		}
		if(!gbsInvoiceDetails.isEmpty()) {
			for (Map<String, Object> invoice: gbsInvoiceDetails) {
				isFoundInGbs = (sapCodes.contains(((String) invoice.get("entity_code")).trim()));
				if(isFoundInGbs)
					break;
			}
		}
		if(!isFoundInCbf && !isFoundInGbs) {
			throw new TclCommonException("Error finding tickets for this invoices...");
		}
	}
	
	/**
	 * getSAPCodes -
	 * 		Get SAPIds for the current logged in user.
	 * 
	 * @return List<Map<String, Object>> containing the list of SAP Codes.
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unchecked")
	private List<String> getSAPCodes() throws TclCommonException {
		List<String> sapCodes=new ArrayList<>();
		List<Integer> leIds = new ArrayList<>();
		String resp = null;
		List<String> custResp = null;
		// Get Legal Entity Ids from the customer list
		SapCodeRequest request=new SapCodeRequest();
		if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			//Partner Code Starts
			leIds = userInfoUtils.getPartnerDetails().stream().map(PartnerDetail::getPartnerLeId)
					.collect(Collectors.toList());
			request.setCustomerLeIds(leIds);
			resp = (String) mqUtils.sendAndReceive(partnerLeSapQueue, Utils.convertObjectToJson(request));
			if (StringUtils.isNotBlank(resp)) {
				LeSapCodeResponse response = Utils.convertJsonToObject(resp, LeSapCodeResponse.class);
				sapCodes.addAll(response.getLeSapCodes().stream().map(LeSapCodeBean::getCodeValue).collect(Collectors.toList()));
			}
			return sapCodes;
		}
		
		// Customer Code starts
		request.setCustomerLeIds(getLegalEntityIds());
		
		resp = (String) mqUtils.sendAndReceive(leSapQueue, Utils.convertObjectToJson(request));
		if(resp!=null) {
			LeSapCodeResponse response = Utils.convertJsonToObject(resp, LeSapCodeResponse.class);
			sapCodes.addAll(response.getLeSapCodes().stream().map(LeSapCodeBean::getCodeValue).collect(Collectors.toList()));
		}

		request.setType("SECS Code");
		String qResponse=(String) mqUtils.sendAndReceive(leCpnySapQueue, Utils.convertObjectToJson(request));
		if (qResponse != null) {
			custResp = Utils.convertJsonToObject(qResponse, List.class);
			sapCodes.addAll(custResp);
		}
		return sapCodes;
	}
	
	/**
	 * commaSeparatedStrings -
	 * 		returns a single comma separated string for the given list of Strings.
	 * 
	 * @return String
	 */
	private String commaSeparatedString(List<String> strings) {
		Predicate<String> isEmpty = String::isEmpty;
		Predicate<String> isNull = Objects::isNull;
		Predicate<String> notNullorEmpty = (isEmpty.or(isNull)).negate();
		String css = strings.stream()
				.filter(notNullorEmpty) // leave out empty strings if any
				.map(inv -> "'" + inv + "'") // ab -> 'ab'
				.collect(Collectors.joining(",")); 
		return css;
	}
	
	/**
	 * validateList -
	 * 		throws exception if a list is null or empty
	 * 
	 * @param list - input list
	 * @param errorMsg - ErrorMessage that should be thrown
	 * @throws TclCommonException
	 */
	private boolean validateList(List<?> list, String errorMsg) throws TclCommonException{
		if(Objects.isNull(list) || list.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * prepareSoapRequest -
	 * 		prepares the soap request with payload, url
	 * 
	 * @param payload - request payload
	 * @param target - target web service
	 * @return
	 */
	private SoapRequest prepareSoapRequest(Object payload, String target) {
		SoapRequest soapRequest =  new SoapRequest();
		soapRequest.setUrl(cesPortalUrl+cesBaseUrl+target);
		soapRequest.setRequestObject(payload);
		soapRequest.setContextPath("com.tcl.dias.billing.dispute.ticket.beans");
		soapRequest.setSoapUserName("");
		soapRequest.setSoapPwd("");
		soapRequest.setConnectionTimeout(600000);
		soapRequest.setReadTimeout(600000);
		return soapRequest;
	}

}
