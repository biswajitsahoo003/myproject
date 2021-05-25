package com.tcl.dias.oms.gsc.tiger;

import static com.tcl.dias.oms.gsc.GscTestUtil.readStringFromFile;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class MockTigerServer extends Dispatcher {

	@Override
	public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
		if (request.getPath().contains("/VanityNumberAPI_SIP/v2.2/numbers") && request.getMethod().equals("GET")) {
			return new MockResponse().setResponseCode(200)
					.setBody(readStringFromFile("com/tcl/dias/oms/gsc/tiger/number_list_response_001.json"));
		} else if (request.getPath().contains("/VanityNumberAPI_SIP/v2.2/numbers")
				&& request.getMethod().equals("PUT")) {
			return new MockResponse().setResponseCode(200)
					.setBody(readStringFromFile("com/tcl/dias/oms/gsc/tiger/number_reservation_response_001.json"));
		} else if (request.getPath().contains("/SECSLegalEntityAPIs/v1.1/organizations")) {
			return new MockResponse().setResponseCode(200)
					.setBody(readStringFromFile("com/tcl/dias/oms/gsc/tiger/organisation_list_response_001.json"));
		}
		return new MockResponse().setResponseCode(404);
	}

	public static MockWebServer createServer() {
		MockWebServer mockWebServer = new MockWebServer();
		mockWebServer.setDispatcher(new MockTigerServer());
		return mockWebServer;
	}
}
