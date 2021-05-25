package com.tcl.dias.common.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IzosdwanCommonConstants {
	private IzosdwanCommonConstants() {
		
	}
	
	public static final String CPE="CPE";
	public static final String FP="FP";
	public static final String SITE_PROPERTIES="SITE_PROPERTIES";
	public static final String IZOSDWAN_SITES="IZOSDWAN_SITES";
	public static final String ACCESS_TOPOLOGY="Access Topology";
	public static final String ACCESS_TYPE="Access Type";
	public static final String ADDITIONAL_IP_REQ="Additional IPs";
	public static final String PORT_BANDWIDTH="Port Bandwidth";
	public static final String LOCAL_LOOP_BANDWIDTH="Local Loop Bandwidth";
	public static final String BILLING_FREQUENCY="Billing Frequency";
	public static final String BILLING_METHOD="Billing Method";
	public static final String COS1="cos 1";
	public static final String COS2="cos 2";
	public static final String COS3="cos 3";
	public static final String COS4="cos 4";
	public static final String COS5="cos 5";
	public static final String COS6="cos 6";
	public static final String CURRENCY_ID="CURRENCY_ID";
	public static final String CUSTOMER_ID="CUSTOMER_ID";
	public static final String CUSTOMER_LE_ID="CUSTOMER_LE_ID";
	public static final String INTERFACE = "Interface";
	public static final String LASTMILE_PROVIDER = "LASTMILE_PROVIDER";
	public static final String PORT_MODE="Port Mode";
	public static final String ROUTING_PROTOCOL="Routing Protocol";
	public static final String ROUTING_PROTOCOL_VALUE="BGP";
	public static final String SERVICE_TYPE = "Service type";
	public static final String SERVICE_VARIANT = "Service Variant";
	public static final String SERVICE_ID = "Service Id";
	public static final String SITE_TOPOLOGY = "SITE_TOPOLOGY";
	public static final String SUPPLIER_ID = "SUPPLIER_ID";
	public static final String TERM_IN_MONTHS = "TermInMonths";
	public static final String VPN_TOPOLOGY = "VPN Topology";
	public static final String VPN_NAME = "VPN name/ VPN ID";
	public static final String CPE_BASIC_CHASSIS="CPE Basic Chassis";
	public static final String CPE_SERIAL_NO = "CPE_SERIAL_NO";
	public static final String CPE_SCOPE = "CPE Management Type";
	public static final String BYON_SCOPE = "byonManagementType";
	public static final String INTERNET_QUALITY = "Internet Quality";
//	public static final String COS_TOPOLOGY = "COS_TOPOLOGY";
	public static final String THIRDPARTY_PROVIDER_NAME = "3rd Party Provider Name";
	public static final String THIRDPARTY_LINK_UPTIME = "3rd Party Link Uptime Agreement";
	public static final String THIRDPARTY_SERVICE_ID = "3rd Party Service Id";
	public static final String BYON_4G_LTE = "BYON 4G/LTE";
	public static final String  THIRDPARTY_IP_ADDRESS= "3rd Party IP Address";
	public static final String CPE_SUPPORT_TYPE = "CPE";
	public static final String CPE_SHARED_OR_NOT = "Shared CPE";
	public static final String PRI_SEC="PRI_SEC";
	//public static final String MANAGEMENT_TYPE="MANAGEMENT_TYPE";
	public static final String IP_ADDRESS_ARRANGEMENT="IP Address Arrangement";
	public static final String IPV4_POOL_SIZE="IPv4 Address Pool Size";
	public static final String IPV6_POOL_SIZE="IPv6 Address Pool Size";
	public static final String IZOSDWAN_TOPOLOGY = "IZOSDWAN_TOPOLOGY";
	public static final String FULL_MESH="Full Mesh";
	public static final String IZOSDWAN = "IZOSDWAN";
	public static final String IZOSDWAN_SOLUTION = "IZOSDWAN_SOLUTION";
	public static final String RENTAL="TATACOMM(Rental)";
	public static final String OUTRIGHT_SALE="Outright Sale";
	public static final String OUTRIGHT="outright";
	public static final String OUTRIGHT_CHARGE ="CPE Outright Charges";
	public static final String RENTAL_CHARGE = "CPE Rental Charges";
	public static final String MACD="MACD";
	public static final String  MAN="MAN";
	public static final String CPE_NAME = "Physical resource code for CPE";
	public static final String NMC = "Physical resource code for NMC";
	public static final String RACKMOUNT = "Physical resource code for Rackmount";
	public static final String SFP = "Physical resource code for SFP";
	public static final String SFP_PLUS = "Physical resource code for SFP_PLUS";
	public static final String POWER_CORD = "PowerCord";
	public static final String CPE_DESC="Cpe Description";
	public static final String RACKMOUNT_DESC="Rackmount Description";
	public static final String NMC_DESC="NMC Description";
	public static final String SFP_DESC="SFP Description";
	public static final String SFP_PLUS_DESC="SFP_PLUS Description";
	public static final String POWERCORD_DESC="PowerCord Description";
	public static final String LANNER = "lanner";
	public static final String NMC_COST = "NMC Cost";
	public static final String RACKMOUNT_COST = "Rackmount Cost";
	public static final String SFP_COST = "SFP Cost";
	public static final String SFP_PLUS_COST = "SFP+ Cost";
	public static final String POWER_CORD_COST = "PowerCord Cost";
	public static final String ROUTER_COST="Router Cost";
	
	public static final String UNMANAGED="Unmanaged";
	public static final String MANAGED="Managed";
	public static final String FULLY_MANAGED="Fully Managed";
	public static final String CONTRACT_START_DATE="CONTRACT_START_DATE";
	public static final String CONTRACT_END_DATE="CONTRACT_END_DATE";
	public static final String MBPS = " Mbps";
	public static final String BLANK_TEXT=" ";
	public static final String SUB_TOTAL="Sub Total";
	public static final String ORDER_SUB_CATEGORY="orderSubCategory";
	public static final String ORDER_CATEGORY="orderCategory";
	
	public static final String OPEN="OPEN";
	public static final String CLOSED="CLOSED";
	public static final String FAILURE="FAILURE";
	public static final String INPROGRESS="INPROGRESS";
	public static final String COMPLETED="COMPLETED";
	public static final String MIGRATED = "MIGRATED";
	public static final String PASSIVE = "Passive";
	public static final String HEADER_PORTMODE="Port Mode";
	public static final String HEADER_PORTMODEACTIVE="Active";
	public static final String HEADER_PORTMODEPASSIVE ="Passive";
	public static final String HEADER_ACESSTYPEWIRELINE = "Wireline";
	public static final String HEADER_ACCESSTYPEWIRELESS ="Wireless";
	public static final String HEADER_ACCESSTYPE="Access Type";
	public static final String BYON_UPLOAD="Byon Upload";
	public static final String HEADER_SERIAL_NO = "SR#";
	public static final String HEADER_COUNTRY = "Country";
	public static final String HEADER_STATE = "State";
	public static final String HEADER_CITY = "City";
	public static final String HEADER_PINCODE ="Pin/Zip Code";
	public static final String HEADER_LOCALITY ="Locality";
	public static final String HEADER_ADDRESS = "Address";
	public static final String HEADER_INTERNETQUALITY = "Internet Quality";
	public static final String HEADER_SITETYPE="Site Type";
	public static final String HEADER_PRIMARY ="Primary";
	public static final String HEADER_SECONDARY ="Secondary";
	public static final String HEADER_PORTBANDWIDTH="Port Bandwidth";
	public static final String HEADER_LOCALLOOPBANDWDTH="Local Loop Bandwidth";
	public static final String HEADER_USEEXISTINGADDRESS="Use Existing Address";
	public static final String HEADER_EXISTINGADDRESS="Exisiting Address";
	public static final String HEADER_USEEXISTINGCPE="Use Existing CPE";

	public static final String HEADER_INTERFACETYPE="Interface Type";
	public static final String HEADER_THIRD_PARTY_SERVICEID="3rd Party Service ID";
	public static final String HEADER_THIRD_PARTY_WAN_IP="IP Address";
	public static final String HEADER_THIRD_PARTY_PROVIDER_NAME="Provider Name";
	public static final String HEADER_BYON_4G_LTE="BYON 4G/LTE";
	public static final String HEADER_THIRD_PARTY_LINKTYPEAGGREEMENT="Link Uptime Agreement (%)";
	public static final String MANAGEMENT_OPTION="Management Option";
	public static final String TOPOLOGY="Topology";
	public static final String BYON_INTERNET="ISBYONUPLOADSUCCESS";
	public static final String RETAILGRADE="Retail Grade";
	public static final String ERRORFORRETAIL="Retail grade internet is not supported please remove this site or change to Enterprise grade";
	public static final String ERRORFORBANDWIDTH="Local Loop Bandwith Should be greater than Port Bandwidth";
	public static final String ERRORFORPRIMARYINTERFACETYPE=" Invalid Primary Interface Type for the selected Local Loop Bandwidth ";
	public static final String ERRORFORSECONDARYINTERFACETYPE=" Invalid Secondary Interface Type for the selected Local Loop Bandwidth ";
	public static final String ERRORSECONDARYPORTMODE="Invalid Secondary Port Mode";
	public static final String ERRORPRIMARYPORTMODE="Invalid Primary Port Mode";
	public static final String ERRORFORCOUNTRY=" Invalid Country. Please select country from the dropdown list ";
	public static final String SINGLECPE="Single CPE";
	public static final String BYON_INTERNET_PRODUCT = "BYON Internet";
	public static final String BYON_MPLS = "BYON MPLS";
	public static final String IZO_INTERNET_WAN_PRODUCT = "IZO Internet WAN";
	public static final String DIA_PRODUCT = "DIA";
	public static final String GVPN_PRODUCT = "GVPN";
	public static final String ASSISTANCE_REQUIRED ="Assistance Required";
	public static final String CONFIGURATION_ONLY= "Configuration Only";
	public static final String RENTAL_TYPE = "TATACOMM(Rental)";
	public static final String PROACTIVE_MONITORING = "Proactive Monitoring";
//	public static final String HUB = "Hub";
//	public static final String SPOKE = "Spoke";
	public static final String YES = "Yes";
	public static final String NO = "No";
	
	public static final String BYON100P="BYON100P";
	public static final String IS_CREATE_PRODUCT_BYON="isCreateProductByon";
	public static final String CGW_MIGRATION_SUGGESTED_BANDWIDTH="cgwMigSuggestedBW";

	
	public static final String HASERROR="HASERROR";
	public static final String ISPROFILEVALID="ISPROFILEVALID";
	public static final String MINCONTRACTTERM="minContractTerm";
	public static final String CGW_MIGRATION_USERMODIFIED_BANDWIDTH="cgwMigUserModifiedBW";
	public static final String SGSC="Single GVPN Single CPE";
	public static final String SISC="Single IAS Single CPE";
	public static final String IS_CGW_HETERO="isCGWHetero";
	public static final String CGW_HETERO_BANDWIDTH="CGWHeteroBw";
	public static final String GVPN_IAS="Single GVPN Single IAS";
	public static final String GVPN_BYON="Single GVPN Single BYON Internet";
	public static final String SINGLE_BYON="Single BYON Internet Single CPE";
	public static final String SIAS="Single IAS";
	public static final String SGVPN="Single GVPN";
	public static final String DIAS="Dual IAS";
	public static final String DGVPN="Dual GVPN";
	public static final String SBYON="Single BYON";
	public static final String DBYON="Dual BYON";
	public static final String DCPE="Dual CPE";
	public static final String USECASE4="SDWAN on Different underlay (IZO IW & MPLS)";
	public static final String USECASE2="Cisco Domain Connect";
	public static final String INDIA_ONLY="INDIA_ONLY";
	public static final String INDIA_INTL="INDIA_INTL";
	public static final String INDIA_INTL_NE="INDIA_INTL_NE";
	public static final String INTL_ONLY="INTL_ONLY";
	public static final String NE_ONLY="NE_ONLY";
	public static final String SINTL_ONLY="SINTL_ONLY";
	public static final String SINTL_OINTL_INDIA="SINTL_OINTL_INDIA";
	public static final String SINTL_OINTL_NE="SINTL_OINTL_NE";
	public static final String SINTL_NE="SINTL_NE";
	public static final String OINTL_ONLY="OINTL_ONLY";
	public static final String OTHER_TYPES="OTHER_TYPES";
	public static final String INDIA = "India";
	public static final List<String> BYONSHAREDSITETYPES=Arrays.asList("Dual BYON Internet Single CPE",	
			"Single IAS Single BYON Internet Single CPE",			
			"Single GVPN Single BYON Internet Single CPE");
    public static final List<String> BYONUNDERLAYSHAREDSITES=Arrays.asList("Single GVPN Single IAS",
            "Single IAS Single BYON Internet Single CPE",            
            "Single GVPN Single BYON Internet Single CPE","Single IAS Single GVPN");
    public static final ArrayList<String> PROVIDER = new ArrayList<>(Arrays.asList("1&1 VERSATEL DEUTSCHLAND GMBH","1ASIA ALLIANCE COMMUNICATION LTD","A1 Telekom Austria AG","AAPT LTD.","ABASKAR CONSTRUCTION PVT LTD","ABOVENET COMMUNICATIONS UK LTD","ACCESSKENYA GROUP LIMITED","Accounts Officer (Cash) , BSNL,","ACE INFRANET SOLUTIOS","ACS TELECOMMUNICATION","ADACELL TECHNOLOGIES PVT LTD","ADIF-ALTA VELOCIDAD","ADN TELECOM LIMITED","AGARIK, SA","AIMS DATA CENTRE SDN. BHD.","AIN GLOBALCOMM COMPANY LIMITED","AIRCEL LIMITED","AIRSPEED COMMUNICATIONS","AIRTEL BURKINA FASO S.A.","AIRTEL TANZANIA LIMITED","AIRX TECHNOLOGIES LIMITED","AJ INFRA SOLUTIONS","AJAY ENTERPRISES PVT LTD","AKSHYAN POWER SOLUTIONS PVT LTD","AKTON DOO","ALASKA COMMUNICATIONS SYSTEM","ALGORITHM","ALPHABET CORPORATION (OPC)","AMSTERDAM INTERNET EXCHANGE BV","ANCOTEL GMBH","ANDREAS VALERIAS","ANGOLA TELECOM","AO (Cash), BSNL, PGM HTD, Hyderabad","AO (CASH),BSNL,O/O GMTD","AO(Cash) O/o GMTD, BSNL, Hardwar","AO(CASH), BSNL AHMEDNAGAR","AO(CASH), BSNL O/o GMTD KANPUR","AO(CASH), BSNL, KANNUR-2","AO(CASH),BSNL, O/O GMTD, NOIDA","APARNA HDD SERVICES","Aperi Corporation","ARCHETYPE AGENCY PRIVATE LIMITED","ARQIVA LTD","ARTERIA NETWORKS CORPORATION","ASACA INDIA PRIVATE LIMITED","Ascenty Data Centers E","Ashok Kumar Lakhotia & Sons (HUF)","Asian Teleconstruction Engineers","ASIANET COMMUNICATIONS LIMITED","ASIANET SATELLITE COMMUNICATIONS L","Asianetcom","ASOCIACION  ESPANIX","AT & T","ATC HOLDING FIBRA MEXICO S DE RL","ATC INFRASTRUCTURE SERVICES LIMITE","ATC INFRASTRUCTURE SERVICES LIMITED","ATC INFRASTRUCTURE SERVICES PVT LTD","ATM S.A.","ATS NETWORK","AVANTEL S DE RL DE CV","AWALNET","AXIONE SAS","AZERONLINE LTD BM","AZERTELECOM LLC","AZZURRO HD LLC",
			"B2B BEELINE","BAHAMAS TELECOMMUNICATIONS","BAHRAIN TELECOMMUNICATIONS COMPANY","BALAJI SERVICES","BALTNETOS KOMUNIKACIJOS JSC", "BANGLADESH EXPORT IMPORT COMPANY", "BATELCO JORDAN", "BAYAN TELECOMMUNICATIONS, INC.", "BCE NEXXIA INC.", "BELGACOM", "BELGACOM INTERNATIONAL CARRIER", "BELL CANADA", "BELL TELESERVICES INDIA PVT LTD", "BEST REALTORS INDIA PVT LTD", "BEZEQ INTERNATIONAL LTD.", "BHARAT SANCHAR NIGAM LIMITED", "BHARAT SANCHAR NIGAM LTD", "BHARTI AIRTEL LIMITED", "BHARTI AXA GENERAL INSURANCE", "BHARTI HEXACOM LIMITED", "BHARTI HEXACOM LIMTED", "Bharti International Singapore", "BHARTIYA MATTRESSES AND FURNISHING", "BITE LIETUVA, UAB", "BLUETEL NETWORKS PTE LTD", "BRINGCOM INCORPORATED", "BRITISH TELECOMMUNICATIONS PLC", "BROADCAST MEDIA COMMUNICATIONS LTD", "BRODYNT GLOBAL SERVICES, S.L.", "BS TEL DIGITAL SOLUTIONS PVT LTD", "BSNL", "BT", "Bulgarian Telecommunications", "BUSINESS TELECOMMUNICATIONS", "C&W", "CABLEMAN PTE LTD", "CABLEVINE SOLUTIONS LIMITED", "CABLEVISION LIGHTPATH INC", "CACHE TECHNOLOGIES", "CANIX COLO INC.", "CANON IT SOLUTIONS KK TOKYO", "CARRIER MIDEA INDIA PVT LTD", "CAT TELECOM PUBLIC COMPANY LIMITED", "CAUCASUS ONLINE LLC", "CAVALIER TELEPHONE LLC", "CB Information Services, Inc.", "CEC TEL CO., LTD", "CELLULOID STRUCTURES INDIA HOLDINGS", "CENTURYLINK", "Centurylink Communications UK Ltd", "CHETAN CONSTRUCTION", "CHIEF TELECOM INC.", "CHINA BROADBAND COMMUNICATIONS", "CHINA ENTERPRISE NETCOM CORPORATION", "CHINA MOBILE INTERNATIONAL LIMITED", "CHINA TELECOM GLOBAL LIMITED", "CHINA TELECOMMUNICATIONS", "CHINA UNICOM (HONG KONG) OPERATIONS", "CHUNGHWA TELECOM CO., LTD.", "CHUNGHWA TELECOM SINGAPORE PTE LTD", "CINECA CONSORZIO INTERUNIVERSITARIO", "CITIC TELECOM CPC NETHERLANDS B.V.", "CITY OF COMMERCE", "CJSC SYNTERRA MEDIA", "CLARA.NET GMBH", "CLARANET LTD", "CLEAR SECURED SERVICES PVT LTD", "CMC INVESTMENTS LIMITED", "COASTCOM, INC.", "COGECO PEER 1 (CANADA) INC", "COGENT COMMUNICATIONS AUSTRIA", "COLO PROPERTIES ATLANTA, LLC", "COLOGIX CANADA, INC", "COLOGIX, INC.", "COLT TECHNOLOGY SERVICES", "COLUMBUS NETWORKS LTD", "COMCAST BUSINESS COMMUNICATIONS", "COMPAGNIE GENERALE DE CABLAGE", "COMPLETEL SAS", "COMSAT INC", "CONCEPTO MOVIL LLC", "CONGO TELECOM", "CONNECTIVITY ARCHITECTS LTD", "CONSOLIDATED COMMUNICATIONS", "CORESITE SERVICES, INC.", "COX COMMUNICATIONS INC", "CREAVISION INT", "CRYSTAL COMMUNICATIONS", "CSR CONSULTING", "CYPRUS TELECOMMUNICATIONS", "CYRUSONE INC", "CYRUSONE LLC", "Datacom Systems Limited", "DE-CIX MANAGEMENT GMBH", "DEHRADUN E NET SOLUTIONS PVT LTD", "DEUTSCHE TELEKOM AG", "DEUTSCHE TELEKOM NORTH AMERICA INC.", "DEV POWER COOL", "DEVELOPMENT LOGICS SOLUTIONS", "DEWCOMM COMMUNICATIONS ENTERPRISES", "DHIVEHI RAAJJEYGE GULHUN PUBLIC", "DIAL TELECOM, A.S.", "DIGICEL CARIBBEAN LIMITED", "DIGIPLEX NORWAY AS", "DIGITAL LONDON LIMITED", "DIGITAL REALTY TRUST, LP", "DISHNET  WIRELESS LIMITED", "Dishnet Wireless Limited", "DISHNET WIRELESS LTD", "DJIBOUTI TELECOM S.A.", "DNA LTD", "DOCOMO PACIFIC", "E SQUARE SYSTEMS", "EASYNET LTD", "EIRCOM LTD", "ELETS TECHNOMEDIA PRIVATE LIMITED", "ELISA CORPORATION", "ENCOMPASS DIGITAL MEDIA (ASIA) PTE", "ENTEL CHILE S.A.", "EPSILON TELECOMMUNICATIONS (SP) PTE", "EPSILON TELECOMMUNICATIONS LTD", "EQUINIX", "ESTO INTERNET PRIVATE LIMITED", "ETIHAD ETISALAT COMPANY", "ETISALAT", "EUNETWORK", "Eurocom CJSC", "EUROFIBER NEDERLAND BV", "EUROPRONET BOSNIA D.O.O.", "E-VIA SPA", "EVOSWITCH NETHERLANDS B.V.", "EXCEL TELESONIC INDIA PVT LTD", "Executive Engineer, MIDC,", "Executive Engineer,MIDC,I.T.", "Expereo Do Brasil Servicos De", "Expereo Singapore Pte Ltd", "EXPEREO USA, INC.", "EXPONENTIAL E LIMITED", "FAIR TRADING FACTOR FZE", "FAST TELECOMMUNICATIONS COMPANY WLL", "FASTWEB SPA", "FIBERAIL SDN BHD", "FIBERLINK", "FIBERNET DIRECT FLORIDA LLC", "FIBERNET LIMITED", "FIBERRING BV", "FIBRE NOIRE INTERNET INC.", "FLEX NETWORK", "FPL FIBERNET LLC", "FPT INTERNATIONAL TELECOM COMPANY", "FRANCE TELECOM", "FRANCE TELECOM LONG DISTANCE USA,", "FUJISOFT TECHNOLOGY LLC", "GAIL INDIA LIMITED", "GAIL(INDIA) LTD", "GAMBIA TELECOMMUNICATIONS COMPNAY", "GC PIVOTAL, LLC", "GE SOLUTIONS", "GEE AAR ENGINEERING", "GHANA TELECOMMUNICATIONS COMPANY", "GLOBAL IEEE INSTITUTE FOR ENGINEERS", "Global Net", "GLOBAL SWITCH (PROP.) SINGAPORE PTE", "GLOBAL SWITCH ESTATE 1 LIMITED", "GLOBAL SWITCH FM GMBH", "GLOBAL TELECOM SERVICES B.V", "GLOBALCOM IDC LIMITED", "GLOBALONE ENTERPRISES PVT LTD", "GLOBENET CABOS SUBMARINOS AMERICA,", "GRAND MALL CONDOMINIUM ASSOCIATION", "Green House Data Inc", "GREESHMA PROJECTS", "GROUPE MASKATEL LP", "GTA TELEGUAM HOLDINGS,LLC", "GTS ENERGIS SP. Z O.O.", "GTS POLAND SP. Z O.O.", "GTT AMERICAS, LLC", "GTT-EMEA LTD", "GULF ELECTRONIC TAWASUL CO", "HANUMA MARKETING", "HARI OM ENTERPRISES", "HARI SAI CONSTRUCTIONS", "HARYANA CITY GAS (KAPIL CHOPRA", "HEXABYTE", "HGC GLOBAL COMMUNICATIONS LTD", "HIBERNIA ATLANTIC US LLC", "HIGHSPEED OFFICE LIMITED", "HONG KONG BROADBAND NETWORK LIMITED", "HUB TELECOM", "Hughes Communications India Li", "HUGHES COMMUNICATIONS INDIA LIMITED", "HUNTER COMMUNICATIONS, INC.", "HUTCHISON GLOBAL COMMUNICATIONS", "I.NET S.P.A.", "I2K2 NETWORKS PRIVATE LIMITED", "I4 COMMUNICATION PVT LTD", "IA ASSOCIATES", "IADVANTAGE AGENCY SERVICES LIMITED", "IADVANTAGE LIMITED", "IAXCESS FZE", "ICOSNET SPA", "IDEA CELLULAR LIMITED", "IDEAL CAR RENTAL SERVICES", "IDEAZ", "IDM ENVIRONMENTAL SERVICES", "IFX NETWORKS INC", "INA TECHFM GLOBAL SOLUTION PVT LTD", "INCONET DATA MANAGEMENT SAL", "INDOSAT SINGAPORE PTE LTD", "Infinera Corporation", "INFRACOM ITALIA S.P.A.", "INNERCITY FIBERNET, LLC", "INNOVE COMMUNICATIONS INC", "INSTANT CABLE NETWORKS PVT LTD", "INSURANCE REGULATORY AND", "INTELCOM LLC", "INTELEPEER CLOUD COMMUNICATIONS LLC", "INTELIGLOBE COMMUNICATIONS LTD", "INTELSAT GLOBAL SALES AND MARKETING", "INTERACTIVE PTY LTD", "INTERNATIONAL BUSINESS GROUP", "INTERNEXA S.A.", "INTEROUTE", "INTERXION", "INVITECH SOLUTIONS LTD", "IRIDEOS S.P.A.", "ISKON INTERNET D.D.", "ITCONIC SA", "ITENOS GMBH", "ITRON INDIA PVT LTD", "JASTEL NETWORK CO., LTD", "JNET TECHNOLOGIES PRIVATE LIMITED", "JSC KAZTRANSCOM", "K G R TELECOMS PRIVATE LIMITED", "KAKATIYA ENERGY SYSTEMS PVT LTD", "KAMIKAZE B2B MEDIA", "KARMA CONSTRUCTIONS", "KASH INFRASTRUCTURE", "KAZAKHTELECOM JSC", "KDDI CORPORATION", "KINGSTON COMMUNICATIONS", "KORDIA LIMITED", "KPN", "KPN B.V.", "KPN TELECOM", "KT CORPORATION", "KYOWA EXEO CORPORATION", "LA FIBRE PALOISE", "LATTELECOM", "Level 3 Communications GmbH", "LEVEL 3 COMMUNICATIONS LLC", "LG UPlus Corporation", "LIAZO SARL", "LIGHTOWER FIBER LI, LLC", "LINEAR ELECTRIC CO. INC.", "LINKDOTNET TELECOM LIMITED", "LIQUID TELECOM", "LODGY CELL ENGINEERS PVT LTD", "LOOKING GLASS NETWORKS, INC.", "LUNAXO", "M.D.SINHA & SONS", "MACOMNET", "MAGNET NETWORKS LIMITED", "MAIN ONE CABLE COMPANY LIMITED", "MALINFO SYSTEM DEVELOPER PVT LTD", "MANDEEP TRADING COMPANY", "MANIKANTA NETWORK COMMUNICATIONS", "MANVI SERVICES", "MARKET HALSEY URBAN RENEWAL, LLC", "MAS INFRATECH", "MATRIX NETWORKS PTE LTD", "MAURITIUS TELECOM LTD", "MCALLEN DATA CENTER LLC", "MCI", "MCI INTERNATIONAL SERVICES, INC.", "MCI WORLDCOM, INC", "MCNICHOLAS CONSTRUCTION (HOLDINGS)", "MEDIARING NETWORKS SERVICES PTE LTD", "MEGATELECOM TELECOMUNICACOES S.A.", "MENA TELECOM WLL", "MENDU ENTERPRISE PRIVATE LIMITED", "MEO-SERVICOS DE COMUNICACOES E", "METCOM NETWORK SERVICES, INC.", "METRO NET S.A.P. I DE C.V", "M-NET TELEKOMMUNIKATIONS GMBH", "MOBILE TELECOMMUNICATIONS COMPANY -", "MONACO TELECOM INTERNATIONAL", "MORATEL INTERNATIONAL PTE LTD", "MOVITEL S.A", "MRIGPANI ENGINEERING CONSTRUCTION", "MTDS", "MTI TELEPORT MUENCHEN GMBH", "MTN COTE D IVOIRE", "MTNL-DELHI-DBW", "MUCOSO BV", "MUKAND SYSTEMS & NETWORKING PRIVATE", "MULTINET PAKISTAN PVT LTD", "MUNDO STARTEL SA", "MYSTAIR HYGIENE CARE PVT LTD", "NAAGAR INFRASTRUCTURE PVT LTD", "NAVEGA.COM, S.A.", "NEECO S.R.O.", "NETCOM AFRICA LIMITED", "NETERRA LTD", "NETIA S.A.", "NETNAM CORPORATION", "NETSMART BILISIM SISTEMLERI VE", "NETWOLVES NETWORK SERVICES, LLC", "NETWORK INNOVATIONS INC", "NEUTRAL DATA CENTERS CORP.", "NEUTRONA NETWORKS INTERNATIONAL", "NEWTELCO GMBH", "NEXION CORPORATION", "NEXTDC LIMITED", "NEXTGEN NETWORKS PTY LTD", "NJFX, LLC", "NOS COMUNICACOES, S.A.", "NOUR COMMUNICATIONS CO.LTD", "NOVOCOM LIMITED", "NTT COMMUNICATIONS CORPORATION", "NTT FINANCE CORPORATION", "NUCLEUS CONNECT PTE. LTD.", "NXDATA S.R.L.", "OCTEL CLOUD SOLUTIONS PVT LTD", "OITA DESENVOLVIMENTO TECHNOLOGIES", "OJSC MEGAFON", "OM SAI TELECOM & CONSTRUCTIONS", "OMANI QATARI TELECOMMUNICATIONS", "ONLINE SAS", "OOREDOO Q. S. C.", "OPTIMAL TELEMEDIA PVT LTD", "OPTUS BILLING SERVICES PTY LIMITED", "ORANGE", "ORANGE COTE D'IVOIRE S.A.", "ORANGE DATA S.A.E.", "ORANGE POLSKA S.A.", "ORIXCOM LIMITED", "OSWAL GROWTH FUND PVT LTD", "PACE BUSINESS MACHINES PVT LTD", "PACIFIC NORTHWEST GIGAPOP", "PACNET CABLE LTD", "PACNET GLOBAL (SINGAPORE) PTE LTD", "PANTEL INTERNATIONAL AG", "PARKALGAR - PARQUES TECNOLOGICOS E", "PAWAN ENTERPRISES", "PAWAN INFRA", "PCCW GLOBAL LIMITED", "PGN INFRATECH", "PHILIPPINE LONG DISTANCE TELEPHONE", "PHOENIX NAP, LLC", "PIPE NETWORKS PTY LTD", "PLTPRO DATA CENTRE SDN BHD", "PLUSNET GMBH", "POLKOMTEL SP. Z.O.O.", "PORTLAND NAP", "POSIDON INFRATEL SOLUTIONS PVT LTD", "POST TELECOM S.A.", "POWER GRID CORP OF INDIA LTD", "POWER GRID CORPORATION OF INDIA", "POWER GRID CORPORATION OF INDIA LTD", "PRAGNYA ASSOCIATES", "PRECISE LOGISTIC SOLUTIONS", "Prime Enterprises", "PRIMUS TELECOMMUNICATIONS AUSTRALIA", "PRODUBAN SERVICIOS INFORMATICOS", "PSAT PROJECTS", "PT COMUNICACOES S.A.", "PT INDOSAT TBK.", "PT PRIME - SOLUCOES EMPRESARIAIS DE", "PT XL AXIATA TBK.", "PT. SUPRA PRIMATAMA NUSANTARA", "PT. TELEKOMUNIKASI INDONESIA", "Public Work Division", "R. K. INFRATEL LTD", "R.B.CONSTRUCTION", "R.P WORLD TELECOM PRIVATE LIMITED", "R.P WORLD TELECOM PVT LTD", "RABBITFOOTS DIGITAL CONSULTING", "RADIANT COMMUNICATIONS CORP", "RAILTEL CORPORATION OF INDIA", "RAPID COMMUNICATIONS", "RAVI BHARTI BAGGA", "RCD Buxar Road division", "REACH NETWORKS HONG KONG LIMITED", "RED TECHNOLOGIES (S) PTE LTD", "RELIANCE COMMERCIAL TRADING PVT LTD", "RELIANCE COMMUNICATION LIMITED", "Reliance Communications", "Relined B.V.", "RETN LTD", "RIDGEVILLE TELEPHONE COMPANY", "ROGERS BUSINESS SOLUTIONS", "ROKE TELKOM LIMITED", "ROSTELECOM", "ROZVYTOK LLC", "RTCOMM. RU OPEN JOINT-STOCK COMPANY", "S.A.S. VIDEO SYNTHESE PRODUCTIONS", "SADA SHIV TELECOM", "SAFARICOM LIMITED", "Sahasra Constrtuctions", "SAMITEL LIMITED", "SAMPARK INFOTAINMENT PVT LTD", "SAMSUNG SDS", "SARENET S.A.U.", "SAS EXCELIS", "SAUDI TELECOM COMPANY", "SAVVIS, INC", "SB INTERACTIVE, INC", "SCHBANG DIGITAL SOLUTIONS PVT LTD", "SEACOM LIMITED", "SEJONG TELECOM INC", "SEMAFOR 77", "SERVICE DES IMPÔTS DES PARTICULIERS", "SERVICES INDUSTRIELS DE GENEVE", "Servicios Audiovisuales Overon S.L", "SFR(FORMELY NEUF CEGETEL)", "SHARVAN KUMAR JHA", "SHAW BUSINESS U.S. INC.", "SHENG LI TELECOM INDIA PVT LTD", "SHRADDHA UTILITIES PVT LTD", "SHREE ANNAPURNA CONTRACTORS PVT LTD", "SHREE EVENTS AND VENTURES PVT LTD", "SHREE SAI HITECH CONSTRUCTION", "SHRI VENKATESWARA INFRATECH", "SHYAM TELECOM", "SIFY TECHNOLOGIES LIMITED", "SIGMA IT PARK PREMISES COOP SOC LTD", "SIGNUM TELECOM", "SIMBANET T LIMITED", "SINGAPORE EXCHANGE LIMITED", "SINGAPORE TELECOMMUNICATIONS LTD.", "SINHAYANA INFRASTRUCTURE PVT LTD", "SIRIUS TELECOMMUNICATIONS INC.", "SISTEMA SHYAM TELESERVICES LIMITED", "SISTEMA SHYAM TELESERVICES LTD", "SIVARAMA ENGINEERS", "SIX TELECOMS COMPANY LIMITED", "SK BROADBAND CO. LTD.", "Sky Plc", "SKYNET BROADBAND PRIVATE LIMITED", "SLOVANET, A.S.", "SMARTONE MOBILE COMMUNICATIONS LTD", "SNET DIVERSIFIED GROUP, INC.", "SOCIETE FRANCAISE DE RADIOTELEPH", "SOFT BANK TELECOM K.K.", "SPARK NEW ZEALAND TRADING LIMITED", "SPARKLE SERVICES PRIVATE LIMITED", "SPIDER FIBER PRIVATE LIMITED", "SPTEL PTE LTD", "SRI LAKSHMI CONSTRUCTIONS", "SRI LAKSHMI VENKATESHWARA", "SRINAGAR NET TECH PVT. LTD.", "SSE TELECOMMUNICATIONS LTD", "STARHUB LTD", "STT GLOBAL DATA CENTRES INDIA", "STT TAI SENG PTE. LTD.", "SUBISU CABLENET PVT LTD", "SUDHIST ENTERPRISES", "SUMMIT COMMUNICATIONS LTD", "SUMMITIG, LLC", "SUNRISE COMMUNICATIONS AG", "SUNRISE ESTATE MANAGEMENT SERVICES", "SUPERNET FZC", "SUPERONLINE ILETISIM HIZMETLERI A.S", "SURYA CONSTRUCTIONS", "SURYA ENTERPRISES", "SVENSKA RYMDAKTIEBOLAGET (SWEDISH", "SWITCH, LTD", "SYDNEY TELEPORT SERVICES PTY LTD", "SYN HF", "SYSWALL TELECOM PVT LTD", "TAIWAN FIXED NETWORK CO., LTD.", "TALK TALK COMMUNICATIONS LTD", "TASHI INFOCOMM LIMITED", "TATA COMMUNICATIONS DATA CENTERS", "TATA COMMUNICATIONS INTERNATIONAL", "Tata Communications Limited", "TATA TELESERVICES", "Tatanet Services Limited", "TDC A/S", "TE DATA", "TECHAXIS INC", "TEJAS NETWORKS  LIMITED", "TEJAS TELECOM SERVICES PVT LTD", "TEJAYS DYNAMIC LIMITED", "TEKISHUB CONSULTING SERVICES", "TELANGANA STATE INDUSTRIAL", "TELE2  NEDERLAND B.V.", "Tele2 Telecommunication GmbH", "Telecard Limited", "TELECOM EGYPT", "TELECOM ITALIA S.P.A.", "TELECOM ITALIA SPARKLE EUR", "TELECOM ITALIA SPARKLE SINGAPORE", "TELECOM NAMIBIA LIMITED", "TELEENA UK LTD", "TELEFONICA DE ARGENTINA S.A.", "TELEFONICA DE ESPANA, S.A.", "TELEFONICA INTERNATIONAL WHOLESALE", "TELEHOUSE INT. CORP. EUROPE", "TELEHOUSE INT. CORP. EUROPE LTD", "TELEKOM AUSTRIA", "TELEKOM MALAYSIA BERHAD", "TELEMACH D.O.O.", "TELESPAZIO S.P.A.", "TELIA CARRIER FRANCE", "TELIA CARRIER U.S. INC", "TELIA CARRIER UK LIMITED", "TELKOM SA SOC LTD", "TELMA GLOBAL NET", "TELNES LLC", "TELSTRA CORPORATION LIMITED", "TELSTRA GLOBAL (SINGAPORE) PTE LTD", "TELSTRA SINGAPORE PTE LTD", "TELSTRA WHOLESALE", "TELTECH TELECOMMUNICATION INC", "TELUS COMMUNICATIONS INC.", "TELXIUS CABLE BRASIL LTDA", "TELXIUS CABLE USA, INC", "TERREMARK NORTH AMERICA, INC", "TEXES CONNECT PRIVATE LIMITED", "TEXES TELECOM PRIVATE LIMITED", "THANATOS SYSTEMS CO LTD", "THE CHICAGO SWITCH, LLC", "THE JEWEL OF PIMPRI PREMISES", "THE TELX GROUP, INC.", "TIKONA DIGITAL NETWORKS PVT LTD", "TIKONA INFINET LIMITED", "TIKONA INFINET PRIVATE LIMITED", "TIME WARNER CABLE", "TIRDEV ENGICON AND SERVICES", "TISCALI ITALIA SPA", "TISCALI UK LIMITED", "T-MOBILE POLSKA S.A.", "TOMARS ENGINEERING SERVICES", "TRANSACTION NETWORK SOLUTIONS, INC.", "TRANSCOM TECHNOLOGIES PVT LTD", "TRIPATHI ENTERPRISES", "TRUE INTERNATIONAL GATEWAY CO., LTD", "T-SYSTEMS LTD", "T-SYSTEMS MAGYARORSZAG ZRT.", "TT DOTCOM SDN BHD", "TTML PUNE", "TTML(CIRCLE)", "TUNISIE TELECOM", "TURK TELEKOM INTERNATIONAL AT GMBH", "TURK TELEKOM INTERNATIONAL HU KFT", "TURK TELEKOMUNIKASYON A.S.", "TURKNET ILETISIM HIZMETLERI A.S.", "TV PLUS PTY LTD", "TV2GO INC", "TW TELECOM HOLDING INC.", "UCLIX INFRA LTD", "UCOM LLC", "UMABHARTI ENTERPRISES", "UNINET HOLDINGS LIMITED", "UNIQUE NETWORK SERVICES", "UNITED TELECOM", "United Telecom Limited", "UNITED TELECOMS LIMITED", "UNITED TELECOMS LTD", "UNITEL MEDIA PVT LTD", "UPAJ BUILDCON PRIVATE LIMITED", "VADS BERHAD", "VAIBHAV GOPAL CONSTRUCTION PVT LTD", "VAINAVI INDUSTRIES LIMITED", "V-CON INTEGRATED SOLUTIONS PVT LTD", "VEREMAX TECHNOLOGIE SERVICES LTD", "VERIZON", "VERIZON DEUTSCHLAND GMBH", "VERIZON FRANCE SA", "VERIZON NEW YORK INC", "VERIZON UK LIMITED", "VIDEOTRON LIMITEE", "VIDI GMBH", "VIETTEL GROUP", "VIPNET INTERNATIONAL", "VIREN TELECOM PVT LTD", "VIRGIN MEDIA LIMITED", "VIRGIN MEDIA LTD", "VISHWA MANAGEMENT SERVICES", "VOCUS NZ LTD", "VOCUS PTY LIMITED", "VODAFONE", "Vtesse Network Limited", "WANA CORPORATE SA", "WE SERVE PROP MANAGEMENT LLP", "WELKIN ENTERPRISE", "WHARF T & T LIMITED", "WILSHIRE CONNECTION , LLC", "WINDSTREAM HOLDING INC.", "WORLDLINK COMMUNICATION LIMITED", "XFIBER AS", "XIN NETWORKS PTE LTD", "XO COMMUNICATIONS", "Yash Telecom Pvt. Ltd.", "YEMEN INTERNATIONAL", "Zain Global Communications Services", "ZAJIL INTERNATIONAL TELECOM COMPANY", "ZAYO", "Zenith Drilling Private Limited","ZNN ENGINEERING PRIVATE LIMITED"));
	public static final String CLOUD_GATEWAY_PORT="Cloud Gateway Port";
	public static final String CLOUD_GATEWAY_MIGRATION="Cloud Gateway Migration";
	public static final String IZOSDWAN_CGW="IZOSDWAN_CGW";
	public static final String COMPONENTS ="COMPONENTS";
	public static final String CLOUD_SERVICE_GATEWAY = "Cloud Service Gateway";
	public static final String NEW="New";
	public static final String CLOUD_GATEWAY_PORT_NAME="Cloud Gateway Port Charges";
	public static final String CLOUD_GATEWAY_MIGRATION_NAME="Migration Charges";
	public static final String VERSA="IZO SDWAN Select";
	public static final String CISCO ="IZO SDWAN Cisco";
	public static final String VPROXY="vProxy";
	public static final String ISSWG ="isSWGVproxy";
	public static final String ISSPA="isSPAVproxy";
	public static final String OEM="OEM Premium Support";
	public static final String SPA="SPA";
	public static final String SWG="SWG";
	public static final String Secure="Secure Web Gateway";
	public static final String Private="Secure Private Access";
	public static final String TOTALNOOFUSERS="Total No. Of Users";
	public static final String TOTALNOOFUSERSMIDDLEEAST="Total No. Of Users in Middle East";
	public static final String OTHERUSERS="Australia";
	public static final String BANDWIDTH_ROW ="Bandwidth - ROW";
    public static final String SURCHARGE ="surcharge";
    public static final String BANDWIDTH_OTHER_USERS = "Bandwidth Surcharge - ANZ, LATAM, Africa, Korea, China";
	public static final String BANDWIDTH_SURCHARGE_MIDDLE_EAST = "Bandwidth Surcharge - Middle East";
	public static final String OFFERING="offering";
	public static final String QUESTION="question";
	public static final String SOLUTION="solution";
	public static final String ADDON="addon";

	public static final String USERS="users";	
	public static final String ORCH_CONNECTION="Wireline";
	public static final String ORCH_CATEGORY="Connected_customer";
	public static final String ORCH_LM_TYPE="Onnet";
	public static final String COUNTRY="India";
	public static final String PRDT_SOLUTION="IZO SDWAN";
	public static final String SDWAN="sdwan";
	public static final String NO_VALUE="NONE";
	public static final String SITE_FLAG="ID";

	
	public static final String ISOTIREQ="isOTIRequired";
	public static final String ISSUPPORTREQ="isSupportRequired";
	public static final String isOTIRequired="Tata Communications one time implementation";
	public static final String STANDARDVAL="Tata Communications - Standard support";
	public static final String PREMIUMVAL="Tata Communications - Premium support";
	public static final String PREMIUM="Premium";
	public static final String STANDARD="Standard";
	public static final String SUPPORT="support";
	public static final String SUPPORTTYPE="tataSupportType";
	public static final String LICENSE="License";
	
	public static final String SDWAN_COST="SDWAN_COST";
	public static final String SDWAN_PRICE="SDWAN_PRICE";
	public static final String SDWAN_DCF="SDWAN_DCF";
	public static final String SDWAN_VPROXY_COST="SDWAN_VPROXY_COST";
	public static final String SDWAN_VPROXY_PRICE="SDWAN_VPROXY_PRICE";
	public static final String MARKUP_PCT="MARKUP_PCT";
	
	public static final String IASSFDCProductId="IASSFDCProductId";
	public static final String IASSFDCProductName="IASSFDCProductName";
	public static final String ILLBYONSFDCProductId="ILLBYONSFDCProductId";
	public static final String ILLBYONSFDCProductName="ILLBYONSFDCProductName";
	public static final String GVPNSFDCProductId="GVPNSFDCProductId";
	public static final String GVPNSFDCProductName="GVPNSFDCProductName";
	public static final String ManagedServices="Managed Services";
	public static final String VproxySFDCProductId="VproxySFDCProductId";
	public static final String VproxySFDCProductName="VproxySFDCProductName";
	public static final String SDWANSFDCProductId="SDWANSFDCProductId";
	public static final String SDWANSFDCProductName="SDWANSFDCProductName";
	public static final String BYON_MPLS_PRODUCT = "BYON MPLS";
	public static final String GVPNBYONSFDCPRODUCTID="GVPNBYONSFDCProductId";
	public static final String GVPNBYONSFDCPRODUCTNAME="GVPNBYONSFDCProductName";
	public static final String IPTRANSITSFDCProductId="IPTransitSFDCProductId";
	public static final String IPTRANSITSFDCProductName="IPTransitSFDCProductName";
	public static final String IWANSFDCProductId="IWANSFDCProductId";
	public static final String IWANSFDCProductName="IWANSFDCProductName";
	public static final String IWANSFDCNewProductId="IWANSFDCNewProductId";
	public static final String IWANSFDCNewProductName="IWANSFDCNewProductName";
	public static final String VutmSFDCProductId="VutmSFDCProductId";
	public static final String VutmSFDCProductName="VproxySFDCProductName";
	public static final String IP_TRANSIT_PRODUCT="IP Transit";
	public static final String SFDC_GVPN_BYON="GVPN BYON";
	public static final String SFDC_IAS_BYON="IAS BYON";
	
	public static final String IASSFDCNewProductId="IASSFDCNewProductId";
	public static final String IASSFDCNewProductName="IASSFDCNewProductName";
	public static final String IPTRANSITSFDCNewProductId="IPTRANSITSFDCNewProductId";
	public static final String IPTRANSITSFDCNewProductName="IPTRANSITSFDCNewProductName";
	public static final String GVPNSFDCNewProductId="GVPNSFDCNewProductId";
	public static final String GVPNSFDCNewProductName="GVPNSFDCNewProductName";
	

    public static final String BYON="BYON";
    public static final String IAS="Internet Access Service";
    public static final String MSS="Managed Security Services";
    public static final String GVPN="Global VPN";
    public static final String SFDC="SFDC";
    
    public static final String ISSDWANPRICINGSUCCESS="ISSDWANPRICINGSUCCESS";
    public static final String SELECT_SERVICES = "Select Service";
    
    

    public static final String FIXED_PORT_MRC="Fixed Port(MRC)";
    public static final String FIXED_PORT="Fixed Port";
    public static final String PORT_NRC="Port NRC";
	public static final String CPE_HW_RENTAL="CPE Hardware - Rental";
	public static final String CPE_HW_OUTRIGHT="CPE Hardware - Outright";
	public static final String CPE_INSTALL="CPE Installation";
	public static final String CPE_CUSTOMER_TAX="CPE Custom Tax";
	public static final String CPE_DELIVERY="CPE Delivery";
	public static final String CPE_LOCAL_TAX="CPE Local Tax";
	public static final String CPE_SUPPORT="CPE Support";
	public static final String LM_MAN_BW="LM MAN BW";
	public static final String LM_MAN_MUX="LM MAN MUX";
	public static final String LM_MAN_INB="LM MAN inbuilding";
	public static final String MAN_OCP="MAN OCP";
	public static final String MAN_RENTALS="MAN Rentals";
	public static final String MAN_OTC="MAN OTC";
	public static final String PROW_OTC="PROW Value (OTC)";
	public static final String PROW_ARC="PROW Value (ARC)";
	public static final String ARC="ARC";
	public static final String NRC="NRC";
	public static final String PROVIDER_CHARGE="Provider Charge";
	public static final String PROVIDER_CHARGE_OTC="Provider Charge OTC";
	public static final String MAST_CHARGER_OFFNET="Mast Charge offnet";
	public static final String RADWIN="Radwin";
	public static final String OTC_NRC_INSTALL="OTC/NRC - Installation";
	public static final String MAST_CHARGE_ONNET="Mast Charge onnet";
	public static final String ARC_CONVERTER_CHARGES="ARC Converter Charges";
	public static final String ARC_COLOCATION="ARC-Colocation";
	public static final String ARC_BW="ARC - BW";
	public static final String LM_MRC="LM MRC";
	public static final String LM_NRC="LM NRC";
	public static final String XCONN_MRC="XConnect MRC";
	public static final String XCONN_NRC="XConnect NRC";
	public static final String ARC_MODEM_CHARGED="ARC Modem charges";
	public static final String NRC_MODEM_CHARGED="NRC Modem charges";
	public static final String NRC_INSTALL="NRC Installation";
	public static final String OFFNET_PROVIDER_ARC="OffnetProvider ARC";

	public static final String OLD_PORT_BANDWIDTH="OLD_PORT_BANDWIDTH";
	public static final String OLD_ARC="OLD_ARC";
	public static final String OLD_NRC="OLD_NRC";
	public static final String COF_SLT_VARIANT = "SLT Variant";

	public static final String VUTM = "vUTM";
	public static final String REMOVE = "Remove";
	public static final String TOTAL = "Total";

	public static final String BYONI="BYON Internet";

	public static final String IZOSDWAN_NAME = "IZO SDWAN";

	public static final String PROACTIVE_MANAGED = "Proactive Managed";
	public static final String EXISTING_BANDWIDTH = "Existing Bandwidth";
	public static final String LAST_MILE = "Last mile";
	public static final String INTERNET_PORT = "Internet Port";
	public static final String VPN_PORT = "VPN Port";
	public static final String IAS_START = "IP_START";

	public static final String IAS_END = "IAS_END";

	public static final String GVPN_START = "GVPN_START";

	public static final String GVPN_END = "GVPN_END";
	
	public static final String SECURE_SELECT_PREMIUM = "Select Secure Premium";
	
	
	public static final String IZOSDWAN_VPROXY = "IZOSDWAN_VPROXY";
	public static final String VPROXY_SPA = "Secure Private Access";
	public static final String VPROXY_SWG ="Secure Web Gateway";
	//vproxy addons
	public static final String SSL_INTERCEPTION = "SSL Interception";
	public static final String NSS_LOG_STREAMING_SERVICE = "NSS Log Streaming Service";
	public static final String ADVANCED_CLOUD_FIREWALL = "Advanced Cloud Firewall";
	public static final String BANDWIDTH_CONTROL = "Bandwidth Control";
	public static final String ADVANCED_CLOUD_SANDBOX = "Advanced Cloud Sandbox";
	public static final String ADVANCED_THREAT_PROTECTION = "Advanced Threat Protection";
	public static final String CLOUD_APPS_CONTROL = "Cloud Apps Control";
	public static final String WEB_ACCESS_CONTROL = "Web Access Control";
	public static final String DATA_LOSS_PREVENTION = "Data Loss Prevention";
	public static final String EXACT_DATA_MATCH = "Exact Data Match";
	public static final String SERVER_AND_IOT_PROTECTION = "Server and IOT Protection";
	public static final String SWG_INLINE_GUEST_WIFI = "SWG Inline Guest WiFi";
	public static final String DEPLOYMENT_ADVISORY_SERVICES_FROM_OEM = "Deployment Advisory services from OEM";
	public static final String OEM_PREMIUM_SUPPORT = "OEM Premium Support";
	public static final String LOG_STREAMING_SERVICE = "Log Streaming Service";
	public static final String DOUBLE_ENCRYPTION_WITH_CUSTOMER_PROVIDED_PKI = "Double Encryption with customer provided PKI";
	public static final String SPA_APPLICATION_ACCESS_CONNECTION = "SPA Application Access Connection";
	public static final String NSS_LOG_RECOVERY_WEB_MANAGEMENT_FEE = "NSS Log Recovery Web Management Fee";
	public static final String NSS_LOG_RECOVERY_FIREWALL_MANAGEMENT_FEE = "NSS Log Recovery Firewall Management Fee";
	public static final String ICAP_FOR_DLP = "ICAP For DLP";
	public static final String DEDICATED_PROXY_PORT = "Dedicated Proxy Port";
	public static final String SSL_INTERCEPTION_PRIVATE_CERTIFICATION = "SSL Interception Private Certification";
	public static final String PRIORITY_CATEGORIZATION_SERVICE = "Priority Categorization Service";

	public static final String LICENCE_ATTRIBUTE = "IZO SDWAN service charges";

	public static final String TOTAL_NO_OF_USERS="Total no of users";
	public static final String TOTAL_NO_OF_MIDDLE_EAST="No.of Users in Middle East";
	public static final String VOLUME_REQUIRED="Volume required";
	public static final String VOLUME="GB Per Month";
	public static final String MILLION_CELLS="million cells";
	public static final String SELECT_TYPE="Select Type";
	public static final String CONNECTORS = "Connectors";
	public static final String QUANTITY = "Quantity";
	
	public static final String PRODUCT_NAME = "ProductName";
	public static final String PRODUCT_ID = "ProductId";
	
	public static final String MANAGED_SERVICES = "Managed Services";
	public static final String VPROXY_SFDC ="Vproxy";
	
	public static final String STRUCK = "STRUCK";
	
	public static final String IAS_CODE="IAS";
    public static final String GVPN_CODE="GVPN";
    
    public static final String VPROXY_COMMON = "VPROXYCOMMON";
    
    public static final String OTI_COMPONENT = "Tata Communications one time implementation";
    public static final String TATA_SUPPORT = "Tata Communications support";
    public static final String SUPPORT_CHARGES = "Support";
    
	public static final String IZOSDWAN_VUTM = "IZOSDWAN_VUTM";
	
    public static final String VUTM_COMMON = "VUTMCOMMON";

    public static final String BREAK_OUT_ATTRIBUTE_NAME_CATALOG = "Breakout Location";
	public static final String OLD_PORT_BANDWIDTH_UNIT="OLD_PORT_BANDWIDTH_UNIT";
	public static final String OLD_LOCAL_LOOP_BANDWIDTH="OLD_LOCAL_LOOP_BANDWIDTH";

	public static final String OLD_LOCAL_LOOP_BANDWIDTH_UNIT="OLD_LOCAL_LOOP_BANDWIDTH_UNIT";
	
	public static final String IS_SEA = "isSEA";
	public static final String IS_SRA = "isSRA";
	
	public static final String SRA_VPN_USERS="SRA_VPN_USERS";
	public static final String SRA_BW_SITE = "SRA_BW_SITE";
	public static final String SRA_AVG_BW = "SRA_AVG_BW";
	public static final String SRA_CON_FACTOR = "SRA_CON_FACTOR";
	public static final String TOTAL_NO_OF_USERS_IN_COUNTRIES = "Total no. of users in  Australia, New Zealand, Central & Latin America, Africa, Korea and mainland China";
	
	public static final String PRIMARY ="Primary";
	public static final String SECONDARY ="Secondary";
	
	public static final String OVERLAY = "OVERLAY";
	public static final String UNDERLAY = "UNDERLAY";
	public static final String COMMON = "COMMON";
	
	public static final String LM_TYPE = "lmType";
	public static final String SERVICE_AVAILABILITY= "Service Availability %";

	public static final String MACD_QUOTE_TYPE="MACD";
	public static final String HOT_UPGRADE="Hot Upgrade";
	
	public static final String CHANGE_BANDWIDTH = "Change Bandwidth";
	public static final String LICENSE_COMP = "IZO SDWAN service charges";
	public static final String CHANGE_BANDWIDTH_SERVICE="CHANGE_BANDWIDTH";
	public static final String CPE_MANGEMENT_TYPE = "cpeManagementType";
	public static final String L3_PORTS = "L3_PORTS";
	public static final String L2_PORTS = "L2_PORTS";
	public static final String LICENSE_CONTRACT_TYPE="License Contract Type";
	public static final String WHERE_CLAUSE = "Opportunity_ID__c in";
	public static final String OBJECT_NAME = "Opportunity";
	public static final String QUERY_FIELD = "Opportunity_Owner_s_Region__c";
	public static final String OWNER_REGION="Owner Region";
	
	public static final String CPE_MAX_BW = "CPE_MAX_BW";
	public static final String CPE_MODEL_END_OF_SALE = "CPE_MODEL_END_OF_SALE";
	public static final String CPE_MODEL_END_OF_LIFE = "CPE_MODEL_END_OF_LIFE";
	public static final String ORDER_CATEGORY_CB = "CHANGE_BANDWIDTH";
	public static final String ORDER_CATEGORY_CO = "CHANGE_ORDER";
	
	public static final String CGW="IZO SDWAN - CLOUD GATEWAY";
	public static final String GVPN_SITE_TYPE="Site Type";
	
	public static final String CUSTOMER_TRIGRAM = "CUSTOMER_TRIGRAM";
	public static final String SERVICE_SCHEDULE = "SS";
	public static final String SERVICE_SCHEDULE_IZOSDWAN = "IZOSDWAN_SS";
	public static final String SERVICE_SCHEDULE_IZOSDWANIAS = "IZOSDWAN_IAS_SS";
	public static final String SERVICE_SCHEDULE_IZOSDWAN_OTHERS = "IZOSDWAN_OTHERS_SS";
	public static final String PRIMARY_GST = "primaryGstNo";
	public static final String SECONDARY_GST = "secondaryGstNo";
	public static final String PRIMARY_CGW = "PRIMARY CGW";
	public static final String SECONDARY_CGW = "SECONDARY CGW";
	public static final String HYBRID_OPPORTUNITY = "Hybrid Opportunity";
	public static final String PRIMARY_LOCATION = "Mumbai (LVSB)";
	public static final String SECONDRY_LOCATION = "Chennai (VSB)";
	
	public static final String POP_SITE_ADDRESS="POP_SITE_ADDRESS";
	public static final String POP_SITE_CODE="POP_SITE_CODE";
	public static final String POP_COUNTRY="POP_COUNTRY";
	public static final String POP_CITY="POP_CITY";
	public static final String POP_STATE="POP_STATE";
	public static final List<String> SERVICE_VARIENT=Arrays.asList("Standard","Enhanced",
			"Premium","Compressed");
	
	public static final String SUPPLIER_ADDRESS_ID_CUSTOMER="Supplier Contracting Entity";

	public static final String SUPPLIER_ADDRESS_ID_OMS="supplierContractingAddressId";

	public static final String SUPPLIER_ADDRESS="supplierContractingAddress";

	public static final String SUPPLIER_COUNTRY="spLeCountry";
	
	public static final String IS_BILLING_INTL = "isBillingInternational";

	public static final String NS_QUOTE_ATTRIBUTE = "nsQuote";
	
	public static final String CPE_INSTALLATION ="CPE Installation";

	public static final String CUSTOM ="Custom";

	public static final String SDWAN_CPE_ID ="SDWAN CPE Id";

	public static final List<String> PHYSICAL_RESOURCE=Arrays.asList("physical resource code for cpe",
	"physical resource code for nmc",
	"physical resource code for rackmount",
	"physical resource code for sfp",
	"physical resource code for sfp_plus",
	"powercord",
	"cpe description",
	"rackmount description",
	"nmc description",
	"sfp description",
	"sfp_plus description",
	"powercord description",
	"nmc cost",
	"rackmount cost",
	"sfp cost",
	"sfp+ cost",
	"powercord Cost",
	"router cost");
	public static final List<String> PHYSICAL_RESOURCE_INTERFACE=Arrays.asList(
			"physical resource code for sfp",
			"physical resource code for sfp_plus",
			"sfp description",
			"sfp_plus description",
			"sfp cost",
			"sfp+ cost"
			);
	
	public static final String SHARED_SERVICE_IDS = "Shared CPE Service Id";
	
	public static final Map<String, String> OFFERING_MAP = new HashMap<String, String>() {{
	    put("single unmanaged gvpn", "Single Managed GVPN");
	    put("dual unmanaged gvpn", "Dual Managed GVPN");
	}};
	public static final List<String> OFFERING_IAS=Arrays.asList("internet access","internet access with backup","internet access with back-up");
	public static final String EMPTY_STRING = "";
}


