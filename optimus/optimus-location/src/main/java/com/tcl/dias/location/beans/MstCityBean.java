package com.tcl.dias.location.beans;

import com.tcl.dias.location.entity.entities.MstCity;

/**
 * This file contains the MstCityBean.java class.
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MstCityBean {

    private Integer id;

    private String code;

    private String name;

    /**
     * Convert from master city entity to bean
     *
     * @param mstCity
     * @return {@link MstCityBean}
     */
    public static MstCityBean fromMstCity(final MstCity mstCity) {
        MstCityBean gscMstCityBean = new MstCityBean();
        gscMstCityBean.setId(mstCity.getId());
        gscMstCityBean.setName(mstCity.getName());
        gscMstCityBean.setCode(mstCity.getCode());
        return gscMstCityBean;
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
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }


}
