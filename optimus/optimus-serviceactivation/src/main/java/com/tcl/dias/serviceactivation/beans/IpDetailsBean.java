package com.tcl.dias.serviceactivation.beans;

import com.tcl.dias.serviceactivation.entity.entities.OrderDetail;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.servicefulfillmentutils.beans.BgpBean;
import com.tcl.dias.servicefulfillmentutils.beans.IpAddressDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.RouterDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.StaticProtocolBean;

import com.tcl.dias.servicefulfillmentutils.beans.*;

import java.io.Serializable;
import java.util.List;

public class IpDetailsBean implements Serializable {

    private List<IpAddressDetailBean> ipAddressDetailBeans;
    private List<StaticProtocolBean> staticProtocolBeans;
    private List<BgpBean> bgpBeans;
    private List<RouterDetailBean> routerDetailBeans;
    private ServiceOrderDetailsBean serviceOrderDetailsBean;
    private String muxPortNo;

    public List<IpAddressDetailBean> getIpAddressDetailBeans() {
        return ipAddressDetailBeans;
    }

    public void setIpAddressDetailBeans(List<IpAddressDetailBean> ipAddressDetailBeans) {
        this.ipAddressDetailBeans = ipAddressDetailBeans;
    }

    public List<StaticProtocolBean> getStaticProtocolBeans() {
        return staticProtocolBeans;
    }

    public void setStaticProtocolBeans(List<StaticProtocolBean> staticProtocolBeans) {
        this.staticProtocolBeans = staticProtocolBeans;
    }

    public List<BgpBean> getBgpBeans() {
        return bgpBeans;
    }

    public void setBgpBeans(List<BgpBean> bgpBeans) {
        this.bgpBeans = bgpBeans;
    }

    public List<RouterDetailBean> getRouterDetailBeans() {
        return routerDetailBeans;
    }

    public void setRouterDetailBeans(List<RouterDetailBean> routerDetailBeans) {
        this.routerDetailBeans = routerDetailBeans;
    }

    public String getMuxPortNo() {
        return muxPortNo;
    }

    public void setMuxPortNo(String muxPortNo) {
        this.muxPortNo = muxPortNo;
    }

    public ServiceOrderDetailsBean getServiceOrderDetailsBean() { return serviceOrderDetailsBean; }

    public void setServiceOrderDetailsBean(ServiceOrderDetailsBean serviceOrderDetailsBean) { this.serviceOrderDetailsBean = serviceOrderDetailsBean; }
}
