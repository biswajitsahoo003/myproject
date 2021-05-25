package com.tcl.dias.productcatelog.entity.entities;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Entity for cpe gsc selection rule
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Immutable
@Table(name="vw_cpe_gsc_selection_rule")
public class CpeGscSelectionRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id")
    private Integer id;

    @Column(name="cpe_bom_id")
    private Integer cpeBomId;

    @Column(name="bm_code")
    private String bomCode;

    @Column(name="form_factor_ru_desktop_num")
    private Integer formFactorRuDesktopNum;

    @Column(name="bandwidth_mbps")
    private Integer bandWidthMbps;

    @Column(name="bandwidth_perf_licence_mbps")
    private Integer bandwidthPerfLicenseMbps;

    @Column(name="min_perf_boost_licence_mbps")
    private Integer minPerfBoostLicenseMbps;

    @Column(name="local_conference_indicator_cd")
    private String localConferenceIndicatorCd;

    @Column(name="is_passthrough_flg")
    private String isPassthroughFlag;

    @Column(name="digital_signal_processor_cd")
    private String digitalSignalProcessorCd;

    @Column(name="cube_sessions_num")
    private Integer cubeSessionsNum;

    @Column(name="max_mft_card_num")
    private Integer maxMFTCardNum;

    @Column(name="max_nim_card_num")
    private Integer maxNimCardNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCpeBomId() {
        return cpeBomId;
    }

    public void setCpeBomId(Integer cpeBomId) {
        this.cpeBomId = cpeBomId;
    }

    public String getBomCode() {
        return bomCode;
    }

    public void setBomCode(String bomCode) {
        this.bomCode = bomCode;
    }

    public Integer getFormFactorRuDesktopNum() {
        return formFactorRuDesktopNum;
    }

    public void setFormFactorRuDesktopNum(Integer formFactorRuDesktopNum) {
        this.formFactorRuDesktopNum = formFactorRuDesktopNum;
    }

    public Integer getBandWidthMbps() {
        return bandWidthMbps;
    }

    public void setBandWidthMbps(Integer bandWidthMbps) {
        this.bandWidthMbps = bandWidthMbps;
    }

    public Integer getBandwidthPerfLicenseMbps() {
        return bandwidthPerfLicenseMbps;
    }

    public void setBandwidthPerfLicenseMbps(Integer bandwidthPerfLicenseMbps) {
        this.bandwidthPerfLicenseMbps = bandwidthPerfLicenseMbps;
    }

    public Integer getMinPerfBoostLicenseMbps() {
        return minPerfBoostLicenseMbps;
    }

    public void setMinPerfBoostLicenseMbps(Integer minPerfBoostLicenseMbps) {
        this.minPerfBoostLicenseMbps = minPerfBoostLicenseMbps;
    }

    public String getLocalConferenceIndicatorCd() {
        return localConferenceIndicatorCd;
    }

    public void setLocalConferenceIndicatorCd(String localConferenceIndicatorCd) {
        this.localConferenceIndicatorCd = localConferenceIndicatorCd;
    }

    public String getDigitalSignalProcessorCd() {
        return digitalSignalProcessorCd;
    }

    public void setDigitalSignalProcessorCd(String digitalSignalProcessorCd) {
        this.digitalSignalProcessorCd = digitalSignalProcessorCd;
    }

    public Integer getCubeSessionsNum() {
        return cubeSessionsNum;
    }

    public void setCubeSessionsNum(Integer cubeSessionsNum) {
        this.cubeSessionsNum = cubeSessionsNum;
    }

    public Integer getMaxMFTCardNum() {
        return maxMFTCardNum;
    }

    public void setMaxMFTCardNum(Integer maxMFTCardNum) {
        this.maxMFTCardNum = maxMFTCardNum;
    }

    public Integer getMaxNimCardNum() {
        return maxNimCardNum;
    }

    public void setMaxNimCardNum(Integer maxNimCardNum) {
        this.maxNimCardNum = maxNimCardNum;
    }

    public String getIsPassthroughFlag() {
        return isPassthroughFlag;
    }

    public void setIsPassthroughFlag(String isPassthroughFlag) {
        this.isPassthroughFlag = isPassthroughFlag;
    }
}
