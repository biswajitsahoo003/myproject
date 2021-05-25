package com.tcl.dias.oms.lr.service;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.OrderLrJob;
import com.tcl.dias.oms.entity.repository.OrderLrJobRepository;
import com.tcl.dias.oms.lr.beans.LrResponse;
import com.tcl.dias.oms.lr.utils.LrJobStage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This file contains the OrderLrService.java class.
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class OrderLrService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderLrService.class);

    @Autowired
    OrderLrJobRepository orderLrJobRepository;

    /**
     * getLrDownloads - This method is used to get the list of LR Download Url and
     * update the status to Inprogress
     *
     * @return
     */
    public List<LrResponse> getLrDownloads(String product) {
        List<LrResponse> lrResponses = new ArrayList<>();
        LOGGER.info("A pull request is called and the lr download list is being started for productName {}", product);
        try {
            List<OrderLrJob> orderLrJobs = null;
            if (StringUtils.isNotBlank(product)) {
                orderLrJobs = orderLrJobRepository.findByStageAndProductName(LrJobStage.NEW.toString(), product);
            } else {
                orderLrJobs = orderLrJobRepository.findByStage(LrJobStage.NEW.toString());
            }
            for (OrderLrJob orderLrJob : orderLrJobs) {
                LOGGER.info("Constructing the lr download url for order id {}", orderLrJob.getOrderRefId());
                LrResponse lrResponse = new LrResponse();
                lrResponse.setLrDownloadUrl(orderLrJob.getLrDownloadUrl());
                lrResponse.setProduct(orderLrJob.getProductName());
                lrResponse.setOrderId(orderLrJob.getOrderRefId());
                lrResponses.add(lrResponse);
                //orderLrJob.setStage(LrJobStage.INPROGRESS.toString()); -- TOBE REVERTED
                orderLrJob.setUpdatedBy(Utils.getSource());
                orderLrJob.setUpdatedTime(new Timestamp(new Date().getTime()));
                orderLrJobRepository.save(orderLrJob);
                LOGGER.info("Stage changed to inprogress  for order id {}", orderLrJob.getOrderRefId());
            }
        } catch (Exception e) {
            LOGGER.error("Error in Lr Download Job", e);
        }

        return lrResponses;

    }

    public void updateLrJobDownload(String jobId, String errorResponse) {
        LOGGER.info("Updating the Lr Download with jobId {}", jobId);
        if (StringUtils.isNotBlank(jobId)) {
            OrderLrJob orderLrJob = orderLrJobRepository.findByJobId(jobId);
            if (orderLrJob != null) {
                if (StringUtils.isNotBlank(errorResponse)) {
                    //orderLrJob.setStage(LrJobStage.LR_DOWNLOAD_FAILURE.toString());
                    orderLrJob.setErrorResponse(errorResponse);
                } else {
                    //orderLrJob.setStage(LrJobStage.LR_DOWNLOAD.toString());
                }
                orderLrJob.setUpdatedBy(Utils.getSource());
                orderLrJob.setLrDownloadCount(
                        orderLrJob.getLrDownloadCount() == null ? 1 : orderLrJob.getLrDownloadCount() + 1);
                orderLrJob.setUpdatedTime(new Timestamp(new Date().getTime()));
                orderLrJobRepository.save(orderLrJob);
            } else {
                LOGGER.warn("Job Id {} is not found", jobId);
            }
        } else {
            LOGGER.warn("Job Id is null");
        }

    }

    /**
     * initiateLrJob - This Method is used to save the lr Job
     *
     * @param orderRefId
     * @param productName
     * @param downloadUrl
     */
    public void initiateLrJob(String orderRefId, String productName, String downloadUrl) {
        try {
            OrderLrJob orderLrJob = new OrderLrJob();
            orderLrJob.setCreatedBy(Utils.getSource());
            orderLrJob.setCreatedTime(new Timestamp(new Date().getTime()));
            orderLrJob.setJobId(Utils.generateUid().toLowerCase());
            orderLrJob.setLrDownloadUrl(downloadUrl + "?jobId=" + orderLrJob.getJobId());
            orderLrJob.setOrderRefId(orderRefId);
            orderLrJob.setProductName(productName);
            orderLrJob.setRetryCount(0);
            orderLrJob.setStage(LrJobStage.NEW.toString());
            orderLrJobRepository.save(orderLrJob);
        } catch (Exception e) {
            LOGGER.error("Error in initiating lr job", e);
        }

    }

}
