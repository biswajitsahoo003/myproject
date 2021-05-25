package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This class is used to  Define Scope of Work & Project Plan
 *
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class DefineScopeWorkProjectPlanBeanList extends TaskDetailsBaseBean  {

    private List<DefineScopeWorkProjectPlanBean> defineScopeWorkProjectPlanBeanList;

    public List<DefineScopeWorkProjectPlanBean> getDefineScopeWorkProjectPlanBeanList() {
        return defineScopeWorkProjectPlanBeanList;
    }

    public void setDefineScopeWorkProjectPlanBeanList(List<DefineScopeWorkProjectPlanBean> defineScopeWorkProjectPlanBeanList) {
        this.defineScopeWorkProjectPlanBeanList = defineScopeWorkProjectPlanBeanList;
    }
}
