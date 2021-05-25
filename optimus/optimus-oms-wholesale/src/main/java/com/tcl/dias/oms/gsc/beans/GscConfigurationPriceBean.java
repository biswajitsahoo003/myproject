package com.tcl.dias.oms.gsc.beans;

import com.tcl.dias.oms.gsc.pricing.beans.DomesticVoicePriceBean;
import com.tcl.dias.oms.gsc.pricing.beans.GlobalOutboundPriceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Pricing Configuration Bean for pricing engine operation
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscConfigurationPriceBean {

    private Integer id;
    private String src;
    private String srcType;
    private String dest;
    private String destType;
    private Double arc;
    private Double mrc;
    private Double nrc;
    private List<String> termName = new ArrayList<>();
    private List<Double> rpmFixed = new ArrayList<>();
    private List<Double> rpmMobile = new ArrayList<>();
    private List<Double> rpmSpecial = new ArrayList<>();
    private List<Double> rpm = new ArrayList<>();
    private DomesticVoicePriceBean domesticVoicePrice;
    private List<GlobalOutboundPriceBean> globalOutboundPrices = new ArrayList<>();
    private Integer index;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSrcType() {
        return srcType;
    }

    public void setSrcType(String srcType) {
        this.srcType = srcType;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getDestType() {
        return destType;
    }

    public void setDestType(String destType) {
        this.destType = destType;
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

    public List<String> getTermName() {
        return termName;
    }

    public void setTermName(List<String> termName) {
        this.termName = termName;
    }

    public List<Double> getRpmFixed() {
        return rpmFixed;
    }

    public void setRpmFixed(List<Double> rpmFixed) {
        this.rpmFixed = rpmFixed;
    }

    public List<Double> getRpmMobile() {
        return rpmMobile;
    }

    public void setRpmMobile(List<Double> rpmMobile) {
        this.rpmMobile = rpmMobile;
    }

    public List<Double> getRpmSpecial() {
        return rpmSpecial;
    }

    public void setRpmSpecial(List<Double> rpmSpecial) {
        this.rpmSpecial = rpmSpecial;
    }

    public DomesticVoicePriceBean getDomesticVoicePrice() {
        return domesticVoicePrice;
    }

    public void setDomesticVoicePrice(DomesticVoicePriceBean domesticVoicePrice) {
        this.domesticVoicePrice = domesticVoicePrice;
    }

    public List<GlobalOutboundPriceBean> getGlobalOutboundPrices() {
        return globalOutboundPrices;
    }

    public void setGlobalOutboundPrices(List<GlobalOutboundPriceBean> globalOutboundPrices) {
        this.globalOutboundPrices = globalOutboundPrices;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public List<Double> getRpm() {
        return rpm;
    }

    public void setRpm(List<Double> rpm) {
        this.rpm = rpm;
    }

    @Override
    public String toString() {
        return "GscConfigurationPriceBean{" +
                "id=" + id +
                ", src='" + src + '\'' +
                ", srcType='" + srcType + '\'' +
                ", dest='" + dest + '\'' +
                ", destType='" + destType + '\'' +
                ", arc=" + arc +
                ", mrc=" + mrc +
                ", nrc=" + nrc +
                ", termName=" + termName +
                ", rpmFixed=" + rpmFixed +
                ", rpmMobile=" + rpmMobile +
                ", rpmSpecial=" + rpmSpecial +
                ", rpm=" + rpm +
                ", domesticVoicePrice=" + domesticVoicePrice +
                ", globalOutboundPrices=" + globalOutboundPrices +
                ", index=" + index +
                '}';
    }
}
