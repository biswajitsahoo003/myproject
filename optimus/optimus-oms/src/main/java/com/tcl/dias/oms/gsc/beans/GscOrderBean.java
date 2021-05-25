package com.tcl.dias.oms.gsc.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.tcl.dias.oms.beans.OrderSlaBean;
import com.tcl.dias.oms.entity.entities.OrderGsc;
import com.tcl.dias.oms.gsc.util.GscAttributeConstants;
import com.tcl.dias.oms.gsc.util.annotations.ComponentAttributeValue;

/**
 * Order Bean for GSC Products which is contains order details and configuration
 * details
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscOrderBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String accessType;
	private String serviceName;
	private Double arc;
	private Double mrc;
	private Double nrc;
	private Double tcv;
	private String productName;
	@ComponentAttributeValue(attributeName = GscAttributeConstants.GSC_INTERNATIONAL_DOWNSTREAM_ORDER_ID)
	private String internationalDownStreamOrderId;
	@ComponentAttributeValue(attributeName = GscAttributeConstants.GSC_DOMESTIC_DOWNSTREAM_ORDER_ID)
	private String domesticDownStreamOrderId;
	@ComponentAttributeValue(attributeName = GscAttributeConstants.GSC_INTERCONNECT_DOWNSTREAM_ORDER_ID)
	private String interConnectDownStreamOrderId;
	@ComponentAttributeValue(attributeName = GscAttributeConstants.GSC_INTERNATIONAL_DOWNSTREAM_SUB_ORDER_ID)
	private String internationalDownStreamSubOrderId;
	@ComponentAttributeValue(attributeName = GscAttributeConstants.GSC_DOMESTIC_DOWNSTREAM_SUB_ORDER_ID)
	private String domesticDownStreamSubOrderId;
	@ComponentAttributeValue(attributeName = GscAttributeConstants.GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID)
	private String interConnectDownStreamSubOrderId;
    @ComponentAttributeValue(attributeName = GscAttributeConstants.GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID_DOMESTIC_VTS)
    private String interConnectDownStreamDomesticVTSSubOrderId;
    @ComponentAttributeValue(attributeName = GscAttributeConstants.GSC_INTERCONNECT_DOWNSTREAM_SUB_ORDER_ID_DOMESTIC_NVT)
    private String interConnectDownStreamDomesticNVTSubOrderId;
	private List<GscOrderConfigurationBean> configurations;
	private List<OrderSlaBean> orderSla;

	/**
	 * fromOrderGsc
	 *
	 * @param orderGsc
	 * @return
	 */
	public static GscOrderBean fromOrderGsc(OrderGsc orderGsc) {
		GscOrderBean gscOrderBean = new GscOrderBean();
		gscOrderBean.setId(orderGsc.getId());
		gscOrderBean.setAccessType(orderGsc.getAccessType());
		gscOrderBean.setArc(orderGsc.getArc());
		gscOrderBean.setMrc(orderGsc.getMrc());
		gscOrderBean.setNrc(orderGsc.getNrc());
		gscOrderBean.setTcv(orderGsc.getTcv());
		gscOrderBean.setConfigurations(orderGsc.getOrderGscDetails().stream()
				.map(GscOrderConfigurationBean::fromOrderGscDetail).collect(Collectors.toList()));
		gscOrderBean.setOrderSla(Objects.nonNull(orderGsc.getOrderGscSlas())
				? orderGsc.getOrderGscSlas().stream().map(OrderSlaBean::fromOrderGscSla).collect(Collectors.toList())
				: null);
		gscOrderBean.setProductName(orderGsc.getProductName());
		gscOrderBean.setServiceName(orderGsc.getProductName());
		return gscOrderBean;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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
	 * @return the accessType
	 */
	public String getAccessType() {
		return accessType;
	}

	/**
	 * @param accessType the accessType to set
	 */
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	/**
	 * @return the configurations
	 */
	public List<GscOrderConfigurationBean> getConfigurations() {
		return configurations;
	}

	/**
	 * @param configurations the configurations to set
	 */
	public void setConfigurations(List<GscOrderConfigurationBean> configurations) {
		this.configurations = configurations;
	}

	public List<OrderSlaBean> getOrderSla() {
		return orderSla;
	}

	public void setOrderSla(List<OrderSlaBean> orderSla) {
		this.orderSla = orderSla;
	}

	public String getInternationalDownStreamSubOrderId() {
		return internationalDownStreamSubOrderId;
	}

	public void setInternationalDownStreamSubOrderId(String internationalDownStreamSubOrderId) {
		this.internationalDownStreamSubOrderId = internationalDownStreamSubOrderId;
	}

	public String getDomesticDownStreamSubOrderId() {
		return domesticDownStreamSubOrderId;
	}

	public void setDomesticDownStreamSubOrderId(String domesticDownStreamSubOrderId) {
		this.domesticDownStreamSubOrderId = domesticDownStreamSubOrderId;
	}

	public String getInterConnectDownStreamSubOrderId() {
		return interConnectDownStreamSubOrderId;
	}

	public void setInterConnectDownStreamSubOrderId(String interConnectDownStreamSubOrderId) {
		this.interConnectDownStreamSubOrderId = interConnectDownStreamSubOrderId;
	}

	public String getInternationalDownStreamOrderId() {
		return internationalDownStreamOrderId;
	}

	public void setInternationalDownStreamOrderId(String internationalDownStreamOrderId) {
		this.internationalDownStreamOrderId = internationalDownStreamOrderId;
	}

	public String getDomesticDownStreamOrderId() {
		return domesticDownStreamOrderId;
	}

	public void setDomesticDownStreamOrderId(String domesticDownStreamOrderId) {
		this.domesticDownStreamOrderId = domesticDownStreamOrderId;
	}

	public String getInterConnectDownStreamOrderId() {
		return interConnectDownStreamOrderId;
	}

	public void setInterConnectDownStreamOrderId(String interConnectDownStreamOrderId) {
		this.interConnectDownStreamOrderId = interConnectDownStreamOrderId;
	}

    public String getInterConnectDownStreamDomesticVTSSubOrderId() {
        return interConnectDownStreamDomesticVTSSubOrderId;
    }

    public void setInterConnectDownStreamDomesticVTSSubOrderId(String interConnectDownStreamDomesticVTSSubOrderId) {
        this.interConnectDownStreamDomesticVTSSubOrderId = interConnectDownStreamDomesticVTSSubOrderId;
    }

    public String getInterConnectDownStreamDomesticNVTSubOrderId() {
        return interConnectDownStreamDomesticNVTSubOrderId;
    }

    public void setInterConnectDownStreamDomesticNVTSubOrderId(String interConnectDownStreamDomesticNVTSubOrderId) {
        this.interConnectDownStreamDomesticNVTSubOrderId = interConnectDownStreamDomesticNVTSubOrderId;
    }
}
