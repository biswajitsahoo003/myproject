package com.tcl.dias.oms.constants;

import com.tcl.dias.common.sfdc.bean.FeasibilityRequestBean;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * This file contains the PortBandwithConstants.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class PortBandwithConstants {

	

	public static String getPortBandWidth(String bandwidth) {
		if (getBandwidthMap().containsKey(bandwidth)) {
			return getBandwidthMap().get(bandwidth);
		}
		return "";
	}
	
	public static Map<String, String> getBandwidthMap(){
		Map<String, String> map = new HashMap<>();
			map.put("0.25", "256 Kbps");
			map.put("0.5", "512 Kbps");
			map.put("1", "1 Mbps");
			map.put("2", "2 Mbps");
			map.put("3", "3 Mbps");
			map.put("4", "4 Mbps");
			map.put("6", "6 Mbps");
			map.put("8", "8 Mbps");
			map.put("10", "10 Mbps");
			map.put("12", "12 Mbps");
			map.put("13", "13 Mbps");
			map.put("14", "14 Mbps");
			map.put("16", "16 Mbps");
			map.put("18", "18 Mbps");
			map.put("20", "20 Mbps");
			map.put("25", "25 Mbps");
			map.put("30", "30 Mbps");
			map.put("35", "35 Mbps");
			map.put("36", "35 Mbps");
			map.put("40", "40 Mbps");
			map.put("45", "45 Mbps");
			map.put("50", "50 Mbps");
			map.put("70", "70 Mbps");
			map.put("80", "80 Mbps");
			map.put("100", "100 Mbps");
			map.put("110", "110 Mbps");
			map.put("120", "120 Mbps");
			map.put("130", "130 Mbps");
			map.put("140", "140 Mbps");
			map.put("150", "150 Mbps");
			map.put("200", "200 Mbps");
			map.put("300", "300 Mbps");
			map.put("400", "400 Mbps");
			map.put("500", "500 Mbps");
			map.put("600", "600 Mbps");
			map.put("700", "700 Mbps");
			map.put("800", "800 Mbps");
			map.put("900", "900 Mbps");
			map.put("1000", "1 Gbps");
			map.put("2000", "2 Gbps");
			map.put("5000", "5 Gbps");
			map.put("10000", "10 Gbps");
		return map;
	}

	public static String handleBwVales(String bwValue){

		switch(bwValue){
			case "0.5" :
				bwValue="512 Kbps";
				break;
			case "0.125":
				bwValue="128 Kbps";
				break;
			case "0.25":
				bwValue="256 Kbps";
				break;
			case "1000":
				bwValue="1 Gbps";
				break;
			case "2000":
				bwValue="2 Gbps";
				break;
			case "3000":
				bwValue="3 Gbps";
				break;
			case "4000":
				bwValue="4 Gbps";
				break;
			case "5000":
				bwValue="5 Gbps";
				break;
			case "6000":
				bwValue="6 Gbps";
				break;
			case "7000":
				bwValue="7 Gbps";
				break;
			case "8000":
				bwValue="8 Gbps";
				break;
			case "9000":
				bwValue="9 Gbps";
				break;
			case "10000":
				bwValue="10 Gbps";
				break;
			default:
				bwValue= bwValue + " Mbps";
		}
		return bwValue;
	}

}
