package com.tcl.dias.l2oworkflowutils.beans.drools;

/**
 * Manual feasibility drool Bean
 *
 * @author ANUSHA UNNI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class ManualFeasibility {

    private Integer bandwidth = new Integer(0);
    private String lastMile="";
    private String connectiontype="";
    private String category="";
    private int Rank;
    private String srchcondition="";

    public Integer getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(Integer bandwidth) {
        this.bandwidth = bandwidth;
    }

    public String getLastMile() {
        return lastMile;
    }

    public void setLastMile(String lastMile) {
        this.lastMile = lastMile;
    }

    public String getConnectiontype() {
        return connectiontype;
    }

    public void setConnectiontype(String connectiontype) {
        this.connectiontype = connectiontype;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getRank() {
        return Rank;
    }

    public void setRank(int rank) {
        Rank = rank;
    }

    public String getSrchcondition() {
        return srchcondition;
    }

    public void setSrchcondition(String srchcondition) {
        this.srchcondition = srchcondition;
    }
}
