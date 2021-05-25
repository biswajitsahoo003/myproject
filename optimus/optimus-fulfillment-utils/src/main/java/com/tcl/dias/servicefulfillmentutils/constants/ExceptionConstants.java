package com.tcl.dias.servicefulfillmentutils.constants;

/**
 * This class contains all the exception constants of Service fulfillment project.
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ExceptionConstants {

    public static final String TASK_NOT_FOUND = "Task not found";
    public static final String MST_STATUS_NOT_FOUND = "Mst status not found";
	public static final String COMMON_ERROR = "Error processing data";
    public static final String ATTACHMENT_NOT_PRESENT = "Attachment not present";
    public static final String APPOINTMENT_NOT_FOUND = "Appointment not found";
    public static final String MST_APPOINTMENT_NOT_FOUND = "Mst Appointment not found";
    public static final String MST_VENDOR_NOT_FOUND = "Mst vendor not found";
    public static final String MST_APPOINTMENT_DOCUMENT_NOT_FOUND = "Mst Appointment document not found";
    public static final String CANNOT_CLAIM = "task.already.claimed";
    
    public static final String TASK_CLOSED = "task.already.closed";


    public static final String DATES_AND_MONTH_FOR_TASK_SUMMARY_NULL = "dates.and.month.null";
    public static final String TASK_DETAILS_EMPTY = "tasks.details.empty";
    public static final String TASK_CREATED_DATE_NULL = "task.created.date.null";
    public static final String SUIP_NOT_FOUND = "SUIP not found";
    public static final String NOT_ELIGIBLE_FOR_ORDER_AMENDMENT = "not.eligible.for.order.amendment";
    public static final String CANT_PUT_THIS_ON_HOLD = "cant.put.this.on.hold";
    public static final String OTHER_SERVICES_INPROGRESS = "other.services.inprogress";
    public static final String CANT_PUT_THIS_ON_UNHOLD = "cant.put.this.on.unhold";
    public static final String NOT_ELIGIBLE_FOR_CANCELLATION = "not.eligible.for.order.cancellation";
    public static final String INVALID_INPUT = "invalid.input";
    public static final String CANT_PUT_UNHOLD_SINCE_NEGOTIATION = "cant.put.unhold.since.negotiation";
    public static final String CANT_PUT_UNHOLD_SINCE_RESOURCE_RELEASED = "cant.put.unhold.since.resource.released";
    public static final String CANT_FETCH_RESOURCE_RELEASED_TASK = "cant.fetch.resource.released.task";
    
    public static final String GL_REQUEST_TO_SAP_FAILED = "gl.request.to.sap.failed";
    
    public static final String CANCELLLATION_NOT_PROCESSED_IN_O2C = "cancellation.not.processed.o2c";
    
    public static final String ORDER_AMENDMENT_ISSUE = "order.amendment.issue";


    public static final String NOT_CONNECTED_TO_VPN = "not.connected.vpn";
    
    public static final String BILLINGTASK_OPEN = "billing.task.opened";

    public static final String PROCUREMENT_VALIDATION_ERROR = "procurement.validation.error";

    public static final String TERMINATION_CANCELLATION_RESTRICTED = "termination.cancellation.restriction";
    
    public static final String ORDER_AMENDMENT_RESTRICT = "order.amendment.restrict";


    private ExceptionConstants() {

	}


}
