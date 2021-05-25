package com.tcl.dias.common.beans;

/**
 * Approver bean class to get email and name for approver
 *
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class Approver {

    private String email;
    private String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Approver{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
