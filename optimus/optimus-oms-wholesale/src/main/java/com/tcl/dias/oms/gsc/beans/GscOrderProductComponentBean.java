package com.tcl.dias.oms.gsc.beans;

import com.tcl.dias.oms.entity.entities.OrderProductComponent;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Product Component details of Order and its attributes
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscOrderProductComponentBean {

    private Integer id;
    private Integer referenceId;
    private String type;
    private String productFamily;
    private String productComponentName;
    private List<GscOrderProductComponentsAttributeValueBean> attributes;

    /**
     * fromOrderProductComponent
     *
     * @param orderProductComponent
     * @return
     */
    public static GscOrderProductComponentBean fromOrderProductComponent(OrderProductComponent orderProductComponent) {
        GscOrderProductComponentBean gscOrderProductComponentBean = new GscOrderProductComponentBean();
        gscOrderProductComponentBean.setId(orderProductComponent.getId());
        gscOrderProductComponentBean.setProductComponentName(orderProductComponent.getMstProductComponent().getName());
        gscOrderProductComponentBean.setProductFamily(orderProductComponent.getMstProductFamily().getName());
        gscOrderProductComponentBean.setReferenceId(orderProductComponent.getReferenceId());
        gscOrderProductComponentBean.setType(orderProductComponent.getType());
        gscOrderProductComponentBean.setAttributes(orderProductComponent.getOrderProductComponentsAttributeValues()
                .stream().map(GscOrderProductComponentsAttributeSimpleValueBean::fromAttribute)
                .collect(Collectors.toList()));
        return gscOrderProductComponentBean;
    }

    /**
     * Convert OrderProductComponent to GscOrderProductComponentBean
     *
     * @param orderProductComponent
     * @return GscOrderProductComponentBean
     */
    public static GscOrderProductComponentBean fromOrderProductComponentWithoutAttributes(
            OrderProductComponent orderProductComponent) {
        GscOrderProductComponentBean gscOrderProductComponentBean = new GscOrderProductComponentBean();
        gscOrderProductComponentBean.setId(orderProductComponent.getId());
        gscOrderProductComponentBean.setProductComponentName(orderProductComponent.getMstProductComponent().getName());
        gscOrderProductComponentBean.setProductFamily(orderProductComponent.getMstProductFamily().getName());
        gscOrderProductComponentBean.setReferenceId(orderProductComponent.getReferenceId());
        gscOrderProductComponentBean.setType(orderProductComponent.getType());
        return gscOrderProductComponentBean;
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
     * @return the referenceId
     */
    public Integer getReferenceId() {
        return referenceId;
    }

    /**
     * @param referenceId the referenceId to set
     */
    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the productFamily
     */
    public String getProductFamily() {
        return productFamily;
    }

    /**
     * @param productFamily the productFamily to set
     */
    public void setProductFamily(String productFamily) {
        this.productFamily = productFamily;
    }

    /**
     * @return the productComponentName
     */
    public String getProductComponentName() {
        return productComponentName;
    }

    /**
     * @param productComponentName the productComponentName to set
     */
    public void setProductComponentName(String productComponentName) {
        this.productComponentName = productComponentName;
    }

    /**
     * @return the attributes
     */
    public List<GscOrderProductComponentsAttributeValueBean> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(List<GscOrderProductComponentsAttributeValueBean> attributes) {
        this.attributes = attributes;
    }
}
