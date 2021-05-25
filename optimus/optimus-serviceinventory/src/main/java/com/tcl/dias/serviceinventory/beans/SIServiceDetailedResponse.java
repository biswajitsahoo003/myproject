package com.tcl.dias.serviceinventory.beans;

/**
 * Class for devailed view response
 *
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SIServiceDetailedResponse {

    private String productName;

    private SISolutionDataOffering solutions;

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the solutions
     */
    public SISolutionDataOffering getSolutions() {
        return solutions;
    }

    /**
     * @param solutions the solutions to set
     */
    public void setSolutions(SISolutionDataOffering solutions) {
        this.solutions = solutions;
    }


}
