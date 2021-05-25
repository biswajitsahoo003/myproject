package com.tcl.dias.beans;

import java.util.List;

import com.tcl.dias.beans.conferenceUsageReport.UsageData;

public class ConferenceUsageReponse {
	private String totalParticipants;
	private String monthlyAverage;
	private String totalParticipantsMinutes;
	private String monthlyAverageMinutes;
	private String totalConferences;
	private String monthlyAverageConference;
	private String totalConferenceMinutes;
	private String monthlyAverageConfMinutes;
	private UsageData usageData;
	public ConferenceUsageReponse() {
		this.totalParticipants = "";
		this.monthlyAverage = "";
		this.totalParticipantsMinutes = "";
		this.monthlyAverageMinutes = "";
		this.totalConferences = "";
		this.monthlyAverageConference = "";
		this.totalConferenceMinutes = "";
		this.monthlyAverageConfMinutes = "";
	}
	public String getTotalParticipants() {
		return totalParticipants;
	}
	public void setTotalParticipants(String totalParticipants) {
		this.totalParticipants = totalParticipants;
	}
	public String getMonthlyAverage() {
		return monthlyAverage;
	}
	public void setMonthlyAverage(String monthlyAverage) {
		this.monthlyAverage = monthlyAverage;
	}
	public String getTotalParticipantsMinutes() {
		return totalParticipantsMinutes;
	}
	public void setTotalParticipantsMinutes(String totalParticipantsMinutes) {
		this.totalParticipantsMinutes = totalParticipantsMinutes;
	}
	public String getMonthlyAverageMinutes() {
		return monthlyAverageMinutes;
	}
	public void setMonthlyAverageMinutes(String monthlyAverageMinutes) {
		this.monthlyAverageMinutes = monthlyAverageMinutes;
	}
	public String getTotalConferences() {
		return totalConferences;
	}
	public void setTotalConferences(String totalConferences) {
		this.totalConferences = totalConferences;
	}
	public String getMonthlyAverageConference() {
		return monthlyAverageConference;
	}
	public void setMonthlyAverageConference(String monthlyAverageConference) {
		this.monthlyAverageConference = monthlyAverageConference;
	}
	public String getTotalConferenceMinutes() {
		return totalConferenceMinutes;
	}
	public void setTotalConferenceMinutes(String totalConferenceMinutes) {
		this.totalConferenceMinutes = totalConferenceMinutes;
	}
	public String getMonthlyAverageConfMinutes() {
		return monthlyAverageConfMinutes;
	}
	public void setMonthlyAverageConfMinutes(String monthlyAverageConfMinutes) {
		this.monthlyAverageConfMinutes = monthlyAverageConfMinutes;
	}
	public UsageData getUsageData() {
		return usageData;
	}
	public void setUsageData(UsageData usageData) {
		this.usageData = usageData;
	}
	@Override
	public String toString() {
		return "ConferenceUsageReponse [totalParticipants=" + totalParticipants + ", monthlyAverage=" + monthlyAverage
				+ ", totalParticipantsMinutes=" + totalParticipantsMinutes + ", monthlyAverageMinutes="
				+ monthlyAverageMinutes + ", totalConferences=" + totalConferences + ", monthlyAverageConference="
				+ monthlyAverageConference + ", totalConferenceMinutes=" + totalConferenceMinutes
				+ ", monthlyAverageConfMinutes=" + monthlyAverageConfMinutes + ", usageData=" + usageData + "]";
	}
	
	
	
	

}
