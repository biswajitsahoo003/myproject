package com.tcl.dias.servicehandover.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component
public class ProductDetails {

	@Autowired
	MQUtils mqUtils;
	
	@Value("${pricing.ipc.location}")
	private String ipcLocationQueue;
	
	@Value("${rabbitmq.mstaddress.detail}")
	private String mstAddressQueue;
	
	@Value("${pricing.ipc.datatransfer}")
	private String ipcDatatransferQueue;

	public AddressDetail getProductLocation(String cityCode) {
		
			String address = null;
			try {
				address = (String) mqUtils.sendAndReceive(ipcLocationQueue, cityCode);
				System.out.println("address" + address);
				
				Map<String, String> map = new LinkedHashMap<String, String>();
				for(String keyValue : address.split(" *, *")) {
				   String[] pairs = keyValue.split(" *: *", 2);
				   map.put(pairs[0], pairs.length == 1 ? "" : pairs[1]);
				}
				
				//need to update it as address_id
				String addressId = null;
				for(Entry<String, String> entry:map.entrySet()) {
					if("\"addressId\"".equals(entry.getKey())) {
						addressId = entry.getValue();
						break; 
					}					
				}
				return getMstAddress(addressId);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (TclCommonException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			return null;
			
	}
	
	public AddressDetail getMstAddress(String addressId) throws TclCommonException {
		
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(addressId);
		while (m.find()) {
			addressId = m.group();
		}
		String address = (String) mqUtils.sendAndReceive(mstAddressQueue,addressId);
		AddressDetail addressDetail= Utils.convertJsonToObject(address, AddressDetail.class);
		System.out.println("PRODUCT/STATE ADDRESS:" + addressDetail.toString());	
		return addressDetail;

	}
	
	
	public String getBandWidth(String cityBandwidthLimit)throws TclCommonException, JSONException {
		
        String bandwidth = (String) mqUtils.sendAndReceive(ipcDatatransferQueue, cityBandwidthLimit);
        JSONObject jsonObject = new JSONObject(bandwidth);
        String band = jsonObject.getString("bandwidth");
        if(band !=null)
        	return band;
        	
       return null;
	}
	
	
}
