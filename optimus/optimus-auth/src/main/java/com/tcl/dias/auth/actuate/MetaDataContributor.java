package com.tcl.dias.auth.actuate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Manojkumar R
 *
 */
@Component
public class MetaDataContributor implements InfoContributor {

	@Autowired
	private ApplicationContext ctx;

	@Override
	public void contribute(Info.Builder builder) {
		Map<String, Object> details = new HashMap<>();
		details.put("bean-definition-count", ctx.getBeanDefinitionCount());
		details.put("startup-date", ctx.getStartupDate());

		builder.withDetail("context", details);
	}

	public static void main(String[] args) throws MalformedURLException, IOException, ParseException {
		PrintStream fileOut = new PrintStream("D://CodeReviewComments.html");
		System.setOut(fileOut);
		URLConnection urlConn = getUrlConnection(
				"http://10.149.64.95:8080/reports/table.json?projectId=10008&viewId=10039");

		urlConn.connect();
		BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
		String inputLine;
		StringBuilder masterData = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
			masterData.append(inputLine);
		in.close();
		Object obj = new JSONParser().parse(masterData.toString());
		JSONObject jo = (JSONObject) obj;
		Map<String, Object> resultSet = (Map<String, Object>) jo.get("resultSet");
		JSONArray resultsSet = (JSONArray) resultSet.get("results");
		Iterator itr2 = resultsSet.iterator();
		while (itr2.hasNext()) {
			Map<String, Object> issueList = (Map<String, Object>) itr2.next();
			System.out.println("<b>Group Name : </b>" + issueList.get("groupKey")+"<br/>");
			System.out.println("<b>Total Group Count :</b>"+resultSet.get("totalCount")+"<br/>");
			System.out.println("========================================"+"<br/>");
			URLConnection urlConnSpe = getUrlConnection(
					"http://10.149.64.95:8080/reports/table.json?projectId=10008&viewId=10039&groupKey="
							+ issueList.get("groupKey"));
			urlConnSpe.connect();
			BufferedReader inSpe = new BufferedReader(new InputStreamReader(urlConnSpe.getInputStream()));
			StringBuilder specData = new StringBuilder();
			while ((inputLine = inSpe.readLine()) != null)
				specData.append(inputLine);
			inSpe.close();
			Object specObj = new JSONParser().parse(specData.toString());
			JSONObject spJo = (JSONObject) specObj;
			Map<String, Object> resultSetSpe = (Map<String, Object>) spJo.get("resultSet");
			System.out.println("<b>Issue Count :</b>"+resultSetSpe.get("totalCount")+"<br/>");
			JSONArray resultsSetSpe = (JSONArray) resultSetSpe.get("results");
			Iterator itr2Spe = resultsSetSpe.iterator();
			while (itr2Spe.hasNext()) {
				Map<String, Object> issueListSpe = (Map<String, Object>) itr2Spe.next();
				URLConnection urlConnpoint = getUrlConnection(
						"http://10.149.64.95:8080/sourcebrowser/source.json?projectId=10008&fileInstanceId="
								+ issueListSpe.get("fileInstanceId") + "&defectInstanceId="
								+ issueListSpe.get("lastDefectInstanceId") + "&mergedDefectId="
								+ issueListSpe.get("id"));
				urlConnpoint.connect();
				BufferedReader inPoint = new BufferedReader(new InputStreamReader(urlConnpoint.getInputStream()));
				StringBuilder specDataPoint = new StringBuilder();
				while ((inputLine = inPoint.readLine()) != null)
					specDataPoint.append(inputLine);
				inPoint.close();
				Object specObjPoint = new JSONParser().parse(specDataPoint.toString());
				JSONObject spJoPoint = (JSONObject) specObjPoint;
				System.out.println("<b>Filepath :</b>" + spJoPoint.get("filePath")+"<br/>");
				JSONArray defectsSet = (JSONArray) spJoPoint.get("defects");
				Long fileStart=(Long) spJoPoint.get("fileStart");
				Long fileEnd=(Long) spJoPoint.get("fileEnd");
				String lineIdPrefix=(String) spJoPoint.get("lineIdPrefix");
				Iterator itrDef = defectsSet.iterator();
				while (itrDef.hasNext()) {
					Map<String, Object> defectSetSp = (Map<String, Object>) itrDef.next();
					Map<String, Object> mainEvent = (Map<String, Object>) defectSetSp.get("mainEvent");
					System.out.println("<b>Defect Id :</b>" + issueListSpe.get("id")+"<br/>");
					System.out.println("<b>Description :</b>" + mainEvent.get("description")+"<br/>");
					System.out.println("<b>LineNumber :</b>" + mainEvent.get("lineNumber")+"<br/>");
					URLConnection sourceConnpoint = getUrlConnection(
							"http://10.149.64.95:8080/sourcebrowser/source.htm?projectId=10008&fileInstanceId="
									+ issueListSpe.get("fileInstanceId") + "&fileStart="
									+ fileStart + "&fileEnd="
									+ fileEnd+"&lineIdPrefix="+lineIdPrefix);
					sourceConnpoint.connect();
					BufferedReader inSourcePoint = new BufferedReader(new InputStreamReader(sourceConnpoint.getInputStream()));
					StringBuilder specSourceDataPoint = new StringBuilder();
					while ((inputLine = inSourcePoint.readLine()) != null)
						specSourceDataPoint.append(inputLine).append("\n\r");
					System.out.println("<b>Source Code</b>"+"<br/>");
					System.out.println("<b>------------</b>"+"<br/>");
					System.out.println(Jsoup.parse(specSourceDataPoint.toString())+"<br/>");
					System.out.println("#########################################################################################"+"<br/>");
				}

			}
			System.out.println("*********************************************************************************************"+"<br/>");
			System.out.println("*********************************************************************************************"+"<br/>");
		}
	}

	/**
	 * getUrlConnection
	 * 
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private static URLConnection getUrlConnection(String urlPath) throws MalformedURLException, IOException {
		URL url = new URL(urlPath);
		URLConnection urlConn = url.openConnection();

		urlConn.setRequestProperty("Cookie",
				"COVJSESSIONID8080DJ=C42481F71E096201780CFA52A3F3439D; domain=10.149.64.95; path=/");
		urlConn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
		urlConn.setRequestProperty("Host", "10.149.64.95:8080");
		urlConn.setUseCaches(true);
		return urlConn;
	}
}
