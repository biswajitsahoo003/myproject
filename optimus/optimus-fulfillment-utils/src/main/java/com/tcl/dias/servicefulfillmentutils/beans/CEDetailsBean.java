package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CEDetailsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Set<EthernetInterfaceBean> ethernetInterfaceBeans;

    private Set<ChannelizedSdhInterfaceBean> channelizedSdhInterfaceBeans;

    private Set<ChannelizedE1serialInterfaceBean> channelizedE1serialInterfaceBeans;

    private Set<EigrpBean> eigrpBeans;

    private Set<BgpBean> bgpBeans;

    private Set<OspfBean> ospfBeans;

    private Set<StaticProtocolBean> staticProtocolBeans;

    private Set<RipBean> ripBeans;

    private Set<RouterDetailBean> routerDetailBeans;

    private Set<CpeBean> cpeBeans;

    public Set<EthernetInterfaceBean> getEthernetInterfaceBeans() {
        if(ethernetInterfaceBeans==null){
            ethernetInterfaceBeans = new HashSet<>();
        }
        return ethernetInterfaceBeans;
    }

    public void setEthernetInterfaceBeans(Set<EthernetInterfaceBean> ethernetInterfaceBeans) {
        this.ethernetInterfaceBeans = ethernetInterfaceBeans;
    }

    public Set<ChannelizedSdhInterfaceBean> getChannelizedSdhInterfaceBeans() {
        if(channelizedSdhInterfaceBeans==null){
            channelizedSdhInterfaceBeans = new HashSet<>();
        }
        return channelizedSdhInterfaceBeans;
    }

    public void setChannelizedSdhInterfaceBeans(Set<ChannelizedSdhInterfaceBean> channelizedSdhInterfaceBeans) {
        this.channelizedSdhInterfaceBeans = channelizedSdhInterfaceBeans;
    }

    public Set<ChannelizedE1serialInterfaceBean> getChannelizedE1serialInterfaceBeans() {
        if(channelizedE1serialInterfaceBeans==null){
            channelizedE1serialInterfaceBeans = new HashSet<>();
        }
        return channelizedE1serialInterfaceBeans;
    }

    public void setChannelizedE1serialInterfaceBeans(Set<ChannelizedE1serialInterfaceBean> channelizedE1serialInterfaceBeans) {
        this.channelizedE1serialInterfaceBeans = channelizedE1serialInterfaceBeans;
    }

    public Set<EigrpBean> getEigrpBeans() {
        if(eigrpBeans == null){
            eigrpBeans = new HashSet<>();
        }
        return eigrpBeans;
    }

    public void setEigrpBeans(Set<EigrpBean> eigrpBeans) {
        this.eigrpBeans = eigrpBeans;
    }

    public Set<BgpBean> getBgpBeans() {
        if(bgpBeans==null){
            bgpBeans = new HashSet<>();
        }
        return bgpBeans;
    }

    public void setBgpBeans(Set<BgpBean> bgpBeans) {
        this.bgpBeans = bgpBeans;
    }

    public Set<OspfBean> getOspfBeans() {
        if(ospfBeans==null){
            ospfBeans = new HashSet<>();
        }
        return ospfBeans;
    }

    public void setOspfBeans(Set<OspfBean> ospfBeans) {
        this.ospfBeans = ospfBeans;
    }

    public Set<StaticProtocolBean> getStaticProtocolBeans() {
        if(staticProtocolBeans == null){
            staticProtocolBeans = new HashSet<>();
        }
        return staticProtocolBeans;
    }

    public void setStaticProtocolBeans(Set<StaticProtocolBean> staticProtocolBeans) {
        this.staticProtocolBeans = staticProtocolBeans;
    }

    public Set<RipBean> getRipBeans() {
        if(ripBeans == null){
            ripBeans = new HashSet<>();
        }
        return ripBeans;
    }

    public void setRipBeans(Set<RipBean> ripBeans) {
        this.ripBeans = ripBeans;
    }

    public Set<RouterDetailBean> getRouterDetailBeans() {

        if (routerDetailBeans==null) {
            routerDetailBeans=new HashSet<>();

        }
        return routerDetailBeans;
    }

    public void setRouterDetailBeans(Set<RouterDetailBean> routerDetailBeans) {
        this.routerDetailBeans = routerDetailBeans;
    }

    public Set<CpeBean> getCpeBeans() {
        if(cpeBeans==null){
            cpeBeans = new HashSet<>();
        }
        return cpeBeans;
    }

    public void setCpeBeans(Set<CpeBean> cpeBeans) {
        this.cpeBeans = cpeBeans;
    }
}

