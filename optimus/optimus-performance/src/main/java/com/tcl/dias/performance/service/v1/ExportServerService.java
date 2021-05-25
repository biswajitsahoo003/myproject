package com.tcl.dias.performance.service.v1;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;

/**
 * A class to connect with highchart export server for chart image
 * 
 * @author KRUTSRIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ExportServerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExportServerService.class);

	@Value("${nodejs.exportserver.url}")
	String exportURL;

	@Value("${system.proxy.host}")
	String proxyHost;

	@Value("${system.proxy.port}")
	Integer proxyPort;
	
	@Autowired
	RestClientService restClientService;

	public InputStream connectExportServer(String requestData) {
		LOGGER.info("Inside connectExportServer {}",exportURL);
		HttpPost post = new HttpPost(exportURL);
		InputStream is = null;
		HttpClient client = HttpClientBuilder.create().build();
// 		if (StringUtils.isNotBlank(proxyHost) && proxyPort != null) {
// 			HttpHost proxy = new HttpHost(proxyHost, proxyPort);
// 			RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
// 			post.setConfig(config);
// 		}
		LOGGER.info("requestData in connectExportServer{}",requestData);
		post.setEntity(new StringEntity(requestData, ContentType.create("application/json")));
		HttpResponse response;
		try {
			response = client.execute(post);
			is = response.getEntity().getContent();
		} catch (IOException e) {
			LOGGER.error("Error in caalling high charts", e);
		}
		return is;

	}

}
