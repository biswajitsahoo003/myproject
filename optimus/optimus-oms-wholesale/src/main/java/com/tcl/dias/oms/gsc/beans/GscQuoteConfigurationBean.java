package com.tcl.dias.oms.gsc.beans;

import com.tcl.dias.oms.entity.entities.QuoteGscDetail;
import com.tcl.dias.oms.gsc.pdf.beans.GscTerminationBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration details for Quote
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscQuoteConfigurationBean {

    private Integer id;
    private String source;
    private String destination;
    private Double mrc;
    private Double nrc;
    private Double arc;
    private Double ratePerMinFixed;
    private Double ratePerMinSpecial;
    private Double ratePerMinMobile;
    private List<GscTerminationBean> terminations = new ArrayList<>();
    private List<String> terminationName = new ArrayList<>();
    private List<String> phoneType = new ArrayList<>();
    private List<String> terminationRate = new ArrayList<>();
    private List<GscProductComponentBean> productComponents;
    private Integer numbersPorted;
    private Integer quantityOfNumbers;
    private String orderSetupNRC;
    private String domesticDIDMRC;
    private String concurrentChannelMRC;
    private String noOfConcurrentChannel;
    private Double uifnRegistrationCharge;
    private String type;

    public static GscQuoteConfigurationBean fromGscQuoteDetail(QuoteGscDetail quoteDetail) {
        GscQuoteConfigurationBean bean = new GscQuoteConfigurationBean();
        bean.setId(quoteDetail.getId());
        bean.setSource(quoteDetail.getSrc());
        bean.setDestination(quoteDetail.getDest());
        bean.setArc(quoteDetail.getArc());
        bean.setMrc(quoteDetail.getMrc());
        bean.setNrc(quoteDetail.getNrc());
        bean.setType(quoteDetail.getType());
        return bean;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
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

    public Double getArc() {
        return arc;
    }

    public void setArc(Double arc) {
        this.arc = arc;
    }

    public List<GscProductComponentBean> getProductComponents() {
        return productComponents;
    }

    public void setProductComponents(List<GscProductComponentBean> productComponents) {
        this.productComponents = productComponents;
    }

    public Double getRatePerMinFixed() {
        return ratePerMinFixed;
    }

    public void setRatePerMinFixed(Double ratePerMinFixed) {
        this.ratePerMinFixed = ratePerMinFixed;
    }

    public Double getRatePerMinSpecial() {
        return ratePerMinSpecial;
    }

    public void setRatePerMinSpecial(Double ratePerMinSpecial) {
        this.ratePerMinSpecial = ratePerMinSpecial;
    }

    public Double getRatePerMinMobile() {
        return ratePerMinMobile;
    }

    public void setRatePerMinMobile(Double ratePerMinMobile) {
        this.ratePerMinMobile = ratePerMinMobile;
    }

    public List<GscTerminationBean> getTerminations() {
        return terminations;
    }

    public void setTerminations(List<GscTerminationBean> terminations) {
        this.terminations = terminations;
    }

    public List<String> getTerminationName() {
        return terminationName;
    }

    public void setTerminationName(List<String> terminationName) {
        this.terminationName = terminationName;
    }

    public List<String> getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(List<String> phoneType) {
        this.phoneType = phoneType;
    }

    public List<String> getTerminationRate() {
        return terminationRate;
    }

    public void setTerminationRate(List<String> terminationRate) {
        this.terminationRate = terminationRate;
    }

    public Integer getNumbersPorted() {
        return numbersPorted;
    }

    public void setNumbersPorted(Integer numbersPorted) {
        this.numbersPorted = numbersPorted;
    }

    public Integer getQuantityOfNumbers() {
        return quantityOfNumbers;
    }

    public void setQuantityOfNumbers(Integer quantityOfNumbers) {
        this.quantityOfNumbers = quantityOfNumbers;
    }

    public String getOrderSetupNRC() {
        return orderSetupNRC;
    }

    public void setOrderSetupNRC(String orderSetupNRC) {
        this.orderSetupNRC = orderSetupNRC;
    }

    public String getDomesticDIDMRC() {
        return domesticDIDMRC;
    }

    public void setDomesticDIDMRC(String domesticDIDMRC) {
        this.domesticDIDMRC = domesticDIDMRC;
    }

    public String getConcurrentChannelMRC() {
        return concurrentChannelMRC;
    }

    public void setConcurrentChannelMRC(String concurrentChannelMRC) {
        this.concurrentChannelMRC = concurrentChannelMRC;
    }

    public String getNoOfConcurrentChannel() {
        return noOfConcurrentChannel;
    }

    public void setNoOfConcurrentChannel(String noOfConcurrentChannel) {
        this.noOfConcurrentChannel = noOfConcurrentChannel;
    }

    public Double getUifnRegistrationCharge() {
        return uifnRegistrationCharge;
    }

    public void setUifnRegistrationCharge(Double uifnRegistrationCharge) {
        this.uifnRegistrationCharge = uifnRegistrationCharge;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
