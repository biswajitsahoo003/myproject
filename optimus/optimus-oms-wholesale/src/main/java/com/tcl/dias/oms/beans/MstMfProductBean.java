package com.tcl.dias.oms.beans;

/**
 * @author Chetan Chaudhary
 */
public class MstMfProductBean {
    private Integer Id;
    private String ProductName;
    private Integer status;

    @Override
    public String toString() {
        return "MstMfProductBean{" +
                "Id=" + Id +
                ", ProductName='" + ProductName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
