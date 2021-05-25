package com.tcl.dias.oms.gsc.beans;

import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.gsc.util.annotations.ComponentAttributeValue;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_APPLY_TO_ALL_CONFIGURATIONS;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_APPLY_TO_ALL_INBOUND_PRODUCTS;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_EXPECTED_DELIVERY_DATE;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_REQUESTOR_DATE_FOR_SERVICE;

/**
 * Order Configuration Details specific to GSC products
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscOrderConfigurationBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String source;
    private String destination;
    private Double mrc;
    private Double nrc;
    private Double arc;
    private String stage;
    private String status;
    @ComponentAttributeValue(attributeName = ATTR_REQUESTOR_DATE_FOR_SERVICE)
    private String requestorDateForService;
    @ComponentAttributeValue(attributeName = ATTR_EXPECTED_DELIVERY_DATE)
    private String expectedDeliveryDate;
    @ComponentAttributeValue(attributeName = ATTR_APPLY_TO_ALL_CONFIGURATIONS)
    private String applyToAllConfigurations = "false";
    @ComponentAttributeValue(attributeName = ATTR_APPLY_TO_ALL_INBOUND_PRODUCTS)
    private String applyToAllInboundProducts = "false";
    private String type;

    private List<GscOrderProductComponentBean> productComponents;

    /**
     * fromOrderGscDetail
     *
     * @param orderGscDetail
     * @return
     */
    public static GscOrderConfigurationBean fromOrderGscDetail(OrderGscDetail orderGscDetail) {
        GscOrderConfigurationBean gscOrderConfigurationBean = new GscOrderConfigurationBean();
        gscOrderConfigurationBean.setArc(orderGscDetail.getArc());
        gscOrderConfigurationBean.setId(orderGscDetail.getId());
        gscOrderConfigurationBean.setDestination(orderGscDetail.getDest());
        gscOrderConfigurationBean.setMrc(orderGscDetail.getMrc());
        gscOrderConfigurationBean.setNrc(orderGscDetail.getNrc());
        gscOrderConfigurationBean.setSource(orderGscDetail.getSrc());
        gscOrderConfigurationBean.setType(orderGscDetail.getType());
        if (Objects.nonNull(orderGscDetail.getMstOrderSiteStage()))
            gscOrderConfigurationBean.setStage(orderGscDetail.getMstOrderSiteStage().getName());
        if (Objects.nonNull(orderGscDetail.getMstOrderSiteStatus()))
            gscOrderConfigurationBean.setStatus(orderGscDetail.getMstOrderSiteStatus().getName());
        return gscOrderConfigurationBean;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * @return the mrc
     */
    public Double getMrc() {
        return mrc;
    }

    /**
     * @param mrc the mrc to set
     */
    public void setMrc(Double mrc) {
        this.mrc = mrc;
    }

    /**
     * @return the nrc
     */
    public Double getNrc() {
        return nrc;
    }

    /**
     * @param nrc the nrc to set
     */
    public void setNrc(Double nrc) {
        this.nrc = nrc;
    }

    /**
     * @return the arc
     */
    public Double getArc() {
        return arc;
    }

    /**
     * @param arc the arc to set
     */
    public void setArc(Double arc) {
        this.arc = arc;
    }

    public String getRequestorDateForService() {
        return requestorDateForService;
    }

    public void setRequestorDateForService(String requestorDateForService) {
        this.requestorDateForService = requestorDateForService;
    }

    public String getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(String expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    /**
     * @return the productComponents
     */
    public List<GscOrderProductComponentBean> getProductComponents() {
        return productComponents;
    }

    /**
     * @param productComponents the productComponents to set
     */
    public void setProductComponents(List<GscOrderProductComponentBean> productComponents) {
        this.productComponents = productComponents;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getApplyToAllConfigurations() {
        return applyToAllConfigurations;
    }

    public void setApplyToAllConfigurations(String applyToAllConfigurations) {
        this.applyToAllConfigurations = applyToAllConfigurations;
    }

    public String getApplyToAllInboundProducts() {
        return applyToAllInboundProducts;
    }

    public void setApplyToAllInboundProducts(String applyToAllInboundProducts) {
        this.applyToAllInboundProducts = applyToAllInboundProducts;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
