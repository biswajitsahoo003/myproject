//package com.tcl.dias.networkaugmentation.service;
//
//import com.tcl.dias.common.beans.RestResponse;
//import com.tcl.dias.common.utils.Status;
//import org.apache.tomcat.util.codec.binary.Base64;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//import org.springframework.http.HttpHeaders;
//
//import java.nio.charset.Charset;
//import java.util.Map;
//
//public abstract class NwaAttachmentAbstactService {
//
//    protected HttpHeaders getBasicAuth(String appId, String appSecret, Map<String, String> contentTypes,
//                                       String YAuthUser) {
//
//        HttpHeaders headers = new HttpHeaders();
//        if (appId != null && appSecret != null) {
//            String auth = appId + ":" + appSecret;
//            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
//            String authHeader = "Basic " + new String(encodedAuth);
//            headers.set("Authorization", authHeader);
//        }
//        if (YAuthUser != null) {
//            headers.set("YAuthorization", YAuthUser);
//
//        } else {
////            headers.set("YAuthorization", userInfoUtils.getUserInformation().getUserId());
//        }
//        if (contentTypes != null && !contentTypes.isEmpty()) {
//
//            contentTypes.forEach((key, value) -> {
//                headers.set(key, value);
//
//            });
//        }
//        return headers;
//
//    }
//
//}
