package com.tcl.dias.location.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.location.entity.entities.MstCountry;

/**
 * This file contains the MstCountryBean.java class.
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MstCountryBean {

    private Integer id;

    private String code;

    private String name;

    private String source;

    private Byte status;

    /**
     * Convert master country entity to bean
     *
     * @param mstCountry
     * @return {@link MstCountryBean}
     */
    public static MstCountryBean fromMstCountry(final MstCountry mstCountry) {
        MstCountryBean gscCountryDetail = new MstCountryBean();
        gscCountryDetail.setId(mstCountry.getId());
        gscCountryDetail.setName(mstCountry.getName());
        gscCountryDetail.setCode(mstCountry.getCode());
        gscCountryDetail.setSource(mstCountry.getSource());
        gscCountryDetail.setStatus(mstCountry.getStatus());
        return gscCountryDetail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MstCountryBean{" + "id=" + id + ", code='" + code + '\'' + ", name='" + name + '\'' + ", source='"
                + source + '\'' + ", status=" + status + '}';
    }
}
