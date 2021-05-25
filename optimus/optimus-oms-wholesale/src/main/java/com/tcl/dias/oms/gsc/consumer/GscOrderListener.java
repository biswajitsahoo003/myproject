package com.tcl.dias.oms.gsc.consumer;

import com.google.common.base.Stopwatch;
import com.google.common.base.Supplier;
import com.tcl.dias.common.constants.SfdcServiceStatus;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.gsc.service.v1.GscOrderService;
import com.tcl.dias.oms.gsc.tiger.TigerServiceHelper;
import com.tcl.dias.oms.gsc.tiger.beans.BaseOrderManagementResponse;
import com.tcl.dias.oms.gsc.tiger.beans.DomesticOrderManagementRequest;
import com.tcl.dias.oms.gsc.tiger.beans.DomesticOrderManagementResponse;
import com.tcl.dias.oms.gsc.tiger.beans.InterConnectOrderManagementRequest;
import com.tcl.dias.oms.gsc.tiger.beans.InternationalOrderManagementRequest;
import com.tcl.dias.oms.gsc.tiger.beans.InternationalOrderManagementResponse;
import com.tcl.dias.oms.gsc.util.GscOrderManagementRequestBuilder;
import com.tcl.dias.oms.gsc.util.GscUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.tcl.dias.oms.gsc.util.GscConstants.*;

/**
 * Queue listener to listen to Tiger API service order management requests
 * originating from Optimus Batch framework and handle accordingly
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GscOrderListener {

    public static final Logger LOGGER = LoggerFactory.getLogger(GscOrderListener.class);

    @Autowired
    GscOrderService gscOrderService;

    @Autowired
    TigerServiceHelper tigerServiceHelper;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    GscOrderManagementRequestBuilder gscOrderManagementRequestBuilder;

    @Autowired
    ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;

    private void executeAndHandleOrderManagementResponse(Supplier<BaseOrderManagementResponse> responseCall,
                                                         ThirdPartyServiceJob job, Integer orderId, String attributeName) {
        try {
            LOGGER.debug("Tiger Response call {} ", responseCall.get());
            LOGGER.debug("Tiger job id {} and ref code {} ", job.getId(), job.getRefId());
            BaseOrderManagementResponse response = responseCall.get();
            job.setServiceStatus(SfdcServiceStatus.SUCCESS.toString());
            job.setResponsePayload(GscUtils.toJson(response));
            job.setUpdatedBy(Utils.getSource());
            job.setUpdatedTime(new Date());
            thirdPartyServiceJobsRepository.save(job);
            gscOrderService.updateResponseDataOrderGscAttributes(response, orderId, attributeName);
        } catch (Exception e) {
            job.setServiceStatus(SfdcServiceStatus.STRUCK.toString());
            job.setRetryCount(Optional.ofNullable(job.getRetryCount()).orElse(0) + 1);
            job.setUpdatedBy(Utils.getSource());
            job.setUpdatedTime(new Date());
            thirdPartyServiceJobsRepository.save(job);
            LOGGER.warn(
                    String.format("Error occurred while processing order code: %s with Tiger systems", job.getRefId()),
                    e);
        }
    }

    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.gsc.ngp.order.process.queue}")})
    public void processOrderCompletionWithTigerService(String request) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            String payload = request;
            ThirdPartyServiceJob job = GscUtils.fromJson(payload, ThirdPartyServiceJob.class);
            LOGGER.info(String.format("Started processing order code: %s, order type: %s with downstream systems",
                    job.getRefId(), job.getServiceType()));

//            if (checkSfdcStageForOrder(job.getRefId())) {
                LOGGER.info("Tiger Service Type {}", job.getServiceType());
                switch (job.getServiceType()) {
                    case WHOLESALE_ORDER:
                        InternationalOrderManagementRequest orderManagementRequest = GscUtils
                                .fromJson(job.getRequestPayload(), InternationalOrderManagementRequest.class);
                        LOGGER.info("NGP order Management Request :: {}", orderManagementRequest.toString());
                        Integer orderId = gscOrderManagementRequestBuilder.getOrderId(orderManagementRequest);
                        InternationalOrderManagementResponse placeInternationalOrder = tigerServiceHelper.placeInternationalOrder(orderManagementRequest, orderId);
                        executeAndHandleOrderManagementResponse(() -> placeInternationalOrder, job, orderId, WHOLESALE_ORDER);
                        break;
                    case TIGER_SERVICE_TYPE_DOMESTIC_ORDER:
                        DomesticOrderManagementRequest domesticOrderManagementRequest = GscUtils
                                .fromJson(job.getRequestPayload(), DomesticOrderManagementRequest.class);
                        Integer domesticOrderId = gscOrderManagementRequestBuilder
                                .getOrderId(domesticOrderManagementRequest);
                        LOGGER.info("Tiger Domestic order Management Request :: {}", domesticOrderManagementRequest.toString());
                        DomesticOrderManagementResponse placeDomesticOrder = tigerServiceHelper.placeDomesticOrder(domesticOrderManagementRequest, domesticOrderId);
                        executeAndHandleOrderManagementResponse(() -> placeDomesticOrder, job, domesticOrderId,
                                TIGER_SERVICE_TYPE_DOMESTIC_ORDER);
                        break;
                    case TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_ACCESS_SERVICES:
                        InterConnectOrderManagementRequest interConnectOrderManagementRequest = GscUtils
                                .fromJson(job.getRequestPayload(), InterConnectOrderManagementRequest.class);
                        Integer interConnectOrderId = gscOrderManagementRequestBuilder.getOrderId(interConnectOrderManagementRequest);
                        LOGGER.info("Tiger Interconnect order access service Management Request :: {}", interConnectOrderManagementRequest.toString());
                        InternationalOrderManagementResponse placeInterConnectOrder = tigerServiceHelper.placeInterConnectOrder(interConnectOrderManagementRequest, interConnectOrderId);
                        executeAndHandleOrderManagementResponse(() -> placeInterConnectOrder, job, interConnectOrderId,
                                TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_ACCESS_SERVICES);
                        break;
                    case TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_GLOBAL_OUTBOUND:
                        InterConnectOrderManagementRequest interConnectGlobalOutboundOrderManagementRequest = GscUtils
                                .fromJson(job.getRequestPayload(), InterConnectOrderManagementRequest.class);
                        Integer interConnectGlobalOutboundOrderId = gscOrderManagementRequestBuilder.getOrderId(interConnectGlobalOutboundOrderManagementRequest);
                        LOGGER.info("Tiger Interconnect order global outbound Management Request :: {}", interConnectGlobalOutboundOrderManagementRequest.toString());
                        InternationalOrderManagementResponse placeInterConnectOrder1 = tigerServiceHelper.placeInterConnectOrder(interConnectGlobalOutboundOrderManagementRequest, interConnectGlobalOutboundOrderId);
                        executeAndHandleOrderManagementResponse(() -> placeInterConnectOrder1, job, interConnectGlobalOutboundOrderId,
                                TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_GLOBAL_OUTBOUND);
                        break;
                    case TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_NVT:
                        InterConnectOrderManagementRequest interConnectDomesticNVTOrderManagementRequest = GscUtils
                                .fromJson(job.getRequestPayload(), InterConnectOrderManagementRequest.class);
                        Integer interConnectDomesticNVTOrderId = gscOrderManagementRequestBuilder.getOrderId(interConnectDomesticNVTOrderManagementRequest);
                        LOGGER.info("Tiger Interconnect order domestic voice nvt Management Request :: {}", interConnectDomesticNVTOrderManagementRequest.toString());
                        InternationalOrderManagementResponse placeInterConnectOrder2 = tigerServiceHelper.placeInterConnectOrder(interConnectDomesticNVTOrderManagementRequest, interConnectDomesticNVTOrderId);
                        executeAndHandleOrderManagementResponse(() -> placeInterConnectOrder2, job, interConnectDomesticNVTOrderId,
                                TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_NVT);
                        break;
                    case TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_VTS:
                        InterConnectOrderManagementRequest interConnectDomesticVTSOrderManagementRequest = GscUtils
                                .fromJson(job.getRequestPayload(), InterConnectOrderManagementRequest.class);
                        Integer interConnectDomesticVTSOrderId = gscOrderManagementRequestBuilder.getOrderId(interConnectDomesticVTSOrderManagementRequest);
                        LOGGER.info("Tiger Interconnect order domestic voice vts Management Request :: {}", interConnectDomesticVTSOrderManagementRequest.toString());
                        InternationalOrderManagementResponse placeInterConnectOrder3 = tigerServiceHelper.placeInterConnectOrder(interConnectDomesticVTSOrderManagementRequest, interConnectDomesticVTSOrderId);
                        executeAndHandleOrderManagementResponse(() -> placeInterConnectOrder3, job, interConnectDomesticVTSOrderId,
                                TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_VTS);
                        break;
                    default:
                        throw new RuntimeException(
                                String.format("Unsupported service type: %s in tiger service", job.getServiceType()));
                }
//            } else {
//                LOGGER.warn(String.format(
//                        "SFDC stage is not 95 percent for mentioned order ref: %s, marking job as struck for now",
//                        job.getRefId()));
//                job.setRetryCount(3);
//                job.setServiceStatus(SfdcServiceStatus.STRUCK.toString());
//                job.setUpdatedTime(new Date());
//                job.setUpdatedBy(Utils.getSource());
//                thirdPartyServiceJobsRepository.save(job);
//            }
            LOGGER.info(String.format("Completed processing order code: %s, order type: %s with downstream systems",
                    job.getRefId(), job.getServiceType()));
        } catch (Exception e) {
            LOGGER.warn(String.format("Error occurred while processing order id: %s with downstream systems", request),
                    e);
        } finally {
            LOGGER.info(String.format("Time taken to process order with downstream systems is %s ms",
                    stopwatch.stop().elapsed(TimeUnit.MILLISECONDS)));
        }
    }

    @RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.gsc.ngp.order.change.outpulse.process.queue}")})
    public void processChangeOutpulseReverseUpdate(String request) {
//        String payload = request;
//        ThirdPartyServiceJob job = GscUtils.fromJson(payload, ThirdPartyServiceJob.class);
        try {
            String orderCode = request.replaceAll("\"", "");
            LOGGER.info(String.format("Started processing change outpulse code: %s", orderCode));
            gscOrderService.checkChangeOutpulseStatusAndUpdate(orderCode);
        } catch (Exception e) {
            LOGGER.warn(String.format("Error occurred while processing order id: %s with downstream systems", request),
                    e);
        }
    }
}
