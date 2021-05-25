package com.tcl.dias.common.constants;

/**
 * Sla constants related GSC Products
 *
 * @author Anusha Vallapil
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum GscSlaConstants {

    PACKET_DROP(2, "Packet Drop %"),
    SERVICE_UPTIME(3, "Service Uptime %"),
    JITTER_SERVICE_LEVEL_TGT(9, "Jitter Servicer Level Target (ms)"),
    POST_DIAL_DELAY(11, "Post dial delay (Seconds)"),
    LATENCY(12, "Latency (ms)");

    private int id;
    private String name;

    private GscSlaConstants(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
