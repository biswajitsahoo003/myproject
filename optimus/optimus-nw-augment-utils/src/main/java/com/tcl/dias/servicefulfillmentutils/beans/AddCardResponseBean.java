package com.tcl.dias.servicefulfillmentutils.beans;

public class AddCardResponseBean {

    private String id;
    private String name;
    private String alias1;
    private String alias2;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias1() {
        return alias1;
    }

    public void setAlias1(String alias1) {
        this.alias1 = alias1;
    }

    public String getAlias2() {
        return alias2;
    }

    public void setAlias2(String alias2) {
        this.alias2 = alias2;
    }

    @Override
    public String toString() {
        return "AddCardResponseBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", alias1='" + alias1 + '\'' +
                ", alias2='" + alias2 + '\'' +
                '}';
    }
}
