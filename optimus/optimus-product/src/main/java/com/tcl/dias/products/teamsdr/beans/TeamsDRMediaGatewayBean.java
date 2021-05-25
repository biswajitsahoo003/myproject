package com.tcl.dias.products.teamsdr.beans;

/**
 * Bean to store media gateway details
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class TeamsDRMediaGatewayBean{
    private String vendorName;
    private String mediaGatewayName;
    private Integer quantity;
    private String mediaGatewayWithQuantity;

    public TeamsDRMediaGatewayBean() {
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getMediaGatewayName() {
        return mediaGatewayName;
    }

    public void setMediaGatewayName(String mediaGatewayName) {
        this.mediaGatewayName = mediaGatewayName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getMediaGatewayWithQuantity() {
        return mediaGatewayWithQuantity;
    }

    public void setMediaGatewayWithQuantity(String mediaGatewayWithQuantity) {
        this.mediaGatewayWithQuantity = mediaGatewayWithQuantity;
    }

    @Override
    public String toString() {
        return "TeamsDRMediaGatewayBean{" +
                "mediaGatewayName='" + mediaGatewayName + '\'' +
                ", quantity=" + quantity +
                ", mediaGatewayWithQuantity='" + mediaGatewayWithQuantity + '\'' +
                '}';
    }
}
