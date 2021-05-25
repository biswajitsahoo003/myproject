package com.tcl.dias.common.beans;

import java.util.Map;

/**
 * this contains MacdFlagService Request contact detais.
 *
 * @author Thamizhselvi Perumal
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MacdFlagServiceRequest {
    private Map<String,String> macdServiceIds;

    public Map<String, String> getMacdServiceIds() {
        return macdServiceIds;
    }

    public void setMacdServiceIds(Map<String, String> macdServiceIds) {
        this.macdServiceIds = macdServiceIds;
    }
}
