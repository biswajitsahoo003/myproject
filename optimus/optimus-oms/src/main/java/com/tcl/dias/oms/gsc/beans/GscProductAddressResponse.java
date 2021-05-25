package com.tcl.dias.oms.gsc.beans;

import java.util.List;

/**
 * <Comments>
 *
 * @author Gnana prakash
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class GscProductAddressResponse {

    private List<Integer> addressIds;

    private List<Long> UniqueAddressCount;

    public List<Integer> getAddressIds() {
        return addressIds;
    }

    public void setAddressIds(List<Integer> addressIds) {
        this.addressIds = addressIds;
    }

    public List<Long> getUniqueAddressCount() {
        return UniqueAddressCount;
    }

    public void setUniqueAddressCount(List<Long> uniqueAddresscount) {
        UniqueAddressCount = uniqueAddresscount;
    }
}
