package com.tcl.dias.performance.constants;


/**
 * A Class to hold all highcharts - PDF generation constants.
 * 
 * @author KRUTSRIN 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class HighChartConstants {
	
	
	public static final String HIGHCHARTS = "highcharts";

	public static final String ZIP_FILE_NAME = "performanceReports.zip";

	public static final String MUST_REVALIDATE_POST_CHECK_0_PRE_CHECK_0 = "must-revalidate, post-check=0, pre-check=0";

	public static final String HYPHEN = "-";

	public static final String SINGLE_QUOTE = "'";

	
	public static final String APPLICATION_ZIP = "application/zip";
	public static final String MM_YY = "MM-yy";
	public static final String FAULT_RATE_YTITLE = "fault Rate (%)";
	public static final String FAULT_RATE_X_TITLE = "Fault Rate = (No. of Circuits Impacted last month/Total Circuits)*100% ";
	public static final String BREACH_TITLE = "Breach (%)";
	public static final String SLA_BREACH_CASES_XTITLE = "SLA Breach cases";
	public static final String UPTIME_YTITLE = "uptime (%)";
	public static final String PRODUCT_WISE_UPTIME_TREND_XTITLE = "Product Wise Uptime Trend";
	public static final String COLUMN_SLA = "columnSLABase64";
	public static final String FAULT_RATE = "faultBase64";
	public static final String UPTIME_COLUMN = "columnBase64";
	public static final String LINE_MTTR_TATA_TREND = "lineMTTRTataTrend";
	public static final String QUERY_PARAM_PARTIAL_LOSS_WITH_QUOTE = "'Partial Loss'";
	public static final String QUERY_PARAM_TOTAL_LOSS_WITH_QUOTE = "'Total Loss of Service'";
	public static final String MTTR_FOR_INCIDENTS_RFO_TATA_COMM = "MTTR (For Incidents with RFO responsible as Tata comm)";
	public static final String MTTR_INCIDENT_RFO_TATA = "MttrIncidentRFOtata";
	public static final String MTTR_FOR_INCIDENTS_RFO_CUST_TITLE = "MTTR (For Incidents with RFO responsible as Customer)";
	public static final String MTTR_INCIDENT_RFO_CUST = "MttrIncidentRFOCust";
	public static final String RFO_OTHER_THAN_CUSTOMER_TITLE = "RFO Responsible - Other than Customer";
	public static final String RFO_CUSTOMER_END_TITLE = "RFO Responsible - Customer End";
	public static final String RFO_NON_CUST_END = "rfoNonCustEnd";
	public static final String RFO_CUST_END = "rfoCustEnd";
	public static final String TOTAL = "Total";
	public static final String LINE_CHART = "lineBase64";
	public static final String ALL_INCIDENTS_MTTR = "AllIncidentsMTTR";
	public static final String MTTR_VALUES_Y_TITLE = "MTTR Values";
	public static final String PARTIAL_LOSS = "Partial Loss";
	public static final String TOTAL_LOSS_OF_SERVICE = "Total Loss of Service";
	public static final String MTTR_FOR_ALL_INCIDENTS_X_TITLE = "MTTR (For All Incidents)";
	public static final String TICKET_TREND_RFO_TATA_TITLE = "Impacts  Wise Incidents Trend- RFO responsible- TATA Comm";
	public static final String TICKET_TEND_RFO_TATA = "ticketTendStackedColumnRFOTATA";
	public static final String QUERY_PARAM_TATA_COMM = "'Tata Communications','Tata Communications - 3rd Party','TATA COMMUNICATIONS LTD.'";
	public static final String TICKET_TREND_RFO_CUSTOMER_TITLE = "Impacts  Wise Incidents Trend- RFO responsible- Customer";
	public static final String TICKET_TEND_RFO_CUSTOMER = "ticketTendStackedColumnRFOCustomer";
	public static final String QUERT_PARAM_TATA_WITH_QOUTE = "'TATA'";
	public static final String QUERT_PARAM_CUST_WITH_QUOTE = "'Customer'";
	public static final String IMPACTS_WISE_INCIDENTS_TREND_TITLE = "Impacts Wise Incidents Trend";
	public static final String TICKET_TREND_SEVERITY = "ticketTrendSeverityBase64";
	public static final String SERVICE_WISE_INCIDENT_PER_MONTH = "serviceWiseIncidentPerMonthBase64";
	public static final String SERVICE_WISE_INCIDENT_FOR_MONTH_TITLE = "Service Wise Incident for Month";
	public static final String PIECHART ="pieBase64";
	public static final String PIECHART_TITLE ="RFO Overall for given month";
	public static final String LINE_MTTR_CUST_TREND = "lineMTTRCustTrend";
	public static final String CONTEXT_VAR_SLA_REACH_CASES_REPORTS ="slaBreachList";
	public static final String OTHER_KEYS ="Other-Keys";
	public static final String SERVICE_INVENTORY_CONTEXT = "siBase64";
	public static final String SERVICE_WISE_INVENTORY = "Service Wise Inventory";
	public static final String SERVICE_INVENTORY_STATUS_CONTEXT = "siStatusWiseBase64";
	public static final String SERVICE_STATUS_WISE_INVENTORY = "Service Status Wise Inventory";



	public static String columnchart =
			"{  \"infile\": \"{" + 
			  "      colors: ['#3d86c6','#ea7926','#a2a2a2'],"+
			"        chart: {" + 
			"            type: 'column'," + 
            "height: 600, width: 800"+
			"        }," + 
			
"title: { text: 'TITLE' },"+
			"       " + 
			"        xAxis: {" + 
			"            categories: [KEYS]," + 
			"            crosshair: true" + 
			"        }," + 
			"        yAxis: {" + 
			"            min: 0,minRange : 0.1," + 
			"            title: {" + 
			"                text: 'YAXIZ'" + 
			"            }" + 
			"        }," +
			
"plotOptions: {line: { softThreshold: false }, column: { pointPadding: 0, borderWidth: 0,dataLabels: { enabled: true } } },"+
			"        series: [VALUES]," + 
			"exporting: { enabled: false },"+
			"credits: { enabled: false }"+
			"    }\" ," + 
			"\"type\": \"image/png\"," + 
			"\"constr\": \"Chart\"" + 
			"}";
	public static String piechart =  "{  \"infile\": \"{" + 
		
"chart: {"+
      "  plotBackgroundColor: null, plotBorderWidth: null,plotShadow: false,type: 'pie',height: 550, width: 800}, "+
  "  title: {"+
   "    text: 'CHART_TITLE'"+
  "  },"+
   
  "plotOptions: { pie: { allowPointSelect: true, cursor: 'pointer', dataLabels: { enabled: true, format: '<b>{point.name}</b>: {point.y}', style: { color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black' } } } },"+
	"        series: [{" + 
	"            name: 'RFO Reports'," + 
	"        colorByPoint: true," + 
	"            data: [VALUES]" + 
	"" + 
	"        }]," + 
	"exporting: { enabled: false },"+
	"credits: { enabled: false }"+
	"    }\" ," + 
			
			"\"type\": \"image/png\"," + 
			"\"constr\": \"Chart\"" + 
			"}";
	
	
	public static String stackedColumnchart =  "{  \"infile\": \"{" + 
	" colors: ['#50B432','yellow','#ea7926']," + 
			
	"chart: {"+
	      " type: 'column',height: 600, width: 800}, "+
	  "  title: {"+
	   "    text: 'TITLE'"+
	  "  },"+
		"        xAxis: {" + 
		"            categories: [KEYS]," + 
		"        }," + 
		"        yAxis: {" + 
		"            min: 0,minRange : 0.1," + 
		"            title: {" + 
		"                text: 'YAXIZ'" + 
		"            }," + 
		
       " stackLabels: { "+
          "  enabled: true,"+
         "   style: { "+
          "      fontWeight: 'bold', "+
            "    color: 'black'  }}},"+
"legend: { align: 'right', x: -30, verticalAlign: 'top', y: 25, floating: true, backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white', borderColor: '#CCC', borderWidth: 1, shadow: false },"+
	
"plotOptions: { line: { softThreshold: false },column: { stacking: 'normal', dataLabels: { enabled: true, style: { textOutline: 0, color:'black',fontWeight: 'normal' } } } },"+
		"        series: [" + 
		
		"SERIES_ARR" +
		
		"        ]," + 
		"exporting: { enabled: false },"+
		"credits: { enabled: false }"+
		"    }\" ," + 
		"\"type\": \"image/png\"," + 
		"\"constr\": \"Chart\"" + 
		"}";
	
	
	public static String mttrLine= "{  \"infile\": \"{ colors: ['#3d86c6','#ea7926','#a2a2a2'],height: 600, width: 800," +"title: { text: 'MTTR_TITLE' }, "
			+ "yAxis: { title: { text: 'MTTR' } }, legend: { layout: 'vertical', align: 'right', verticalAlign: 'middle' },"
			+ " plotOptions: { line: { lineWidth:2, dataLabels: { enabled: true }, enableMouseTracking: true },series: { label: { connectorAllowed: false }} "
			+ "}, "
			+"xAxis: { categories: [ XAXIS_CATEGORIES ] },"
			+ "series: [ SERIES_ARR], "
			+ "responsive: { rules: [{ condition: { maxWidth: 500 }, "
			+ "chartOptions: { legend: { layout: 'horizontal', align: 'center', verticalAlign: 'bottom' } } }] },"
			+ "exporting: { enabled: false },"
			+ "credits: { enabled: false }"
			+"    }\" ," 
	        +"\"type\": \"image/png\"," 
	        +"\"constr\": \"Chart\"" 
	        +"}";
			;
	
			public static String dochart =  "{  \"infile\": \"{ colors: ['#3d86c6','#ea7926','#a2a2a2','green','yellow','brown']," + 
					
			"chart: {"+
			      "  plotBackgroundColor: null, plotBorderWidth: null,plotShadow: false,type: 'pie',height: 550, width: 800}, "+
			  "  title: {"+
			   "    text: 'CHART_TITLE'"+
			  "  },"+
			   
			  "plotOptions: { pie: { allowPointSelect: true, cursor: 'pointer', dataLabels: { enabled: true, format: '<b>{point.name}</b>: {point.y}', style: { color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black' } } } },"+
				"        series: [{ innerSize: '50%'," + 
				"            name: 'Service Inventory'," + 
				"        colorByPoint: true," + 
				"            data: [VALUES]" + 
				"" + 
				"        }]," + 
				"exporting: { enabled: false },"+
				"credits: { enabled: false }"+
				"    }\" ," + 
						
						"\"type\": \"image/png\"," + 
						"\"constr\": \"Chart\"" + 
						"}";
	
}

