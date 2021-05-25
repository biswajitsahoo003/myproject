package com.tcl.dias.servicefulfillmentutils.beans;

public class DependencyDetails {
    private String team;
    private String description;

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DependencyDetails{" +
                "team='" + team + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
