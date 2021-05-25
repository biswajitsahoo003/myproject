package com.tcl.dias.oms.renewals.serviceV1;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.tcl.dias.oms.renewals.bean.RenewalsConstant;

@Service
public class RenewalsExcelPriceMappingV1 {
	String pattern = "dd-MMM-yyyy";
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	public Map<String, Map<String, Object>> constructPrice(XSSFWorkbook workbook) {
		XSSFSheet worksheet = workbook.getSheetAt(0);
		Map<String, Map<String, Object>> serviceIdPriceMapping = new HashMap<String, Map<String, Object>>();
		DataFormatter formatter = new DataFormatter();	

		for (int i = 3; i <worksheet.getPhysicalNumberOfRows(); i++) {
			XSSFRow row = worksheet.getRow(i);
			String val = formatter.formatCellValue(row.getCell(0)).toString().trim();
			if(val!=null & val!="") {
			Map<String, Object> mappingexcel = new HashMap<String, Object>();

			
			if (row.getCell(3) != null && row.getCell(3).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.INTERNET_PORT+ RenewalsConstant.PRIMARY_COMP_MRC_SPACE,
						row.getCell(3) != null ? row.getCell(3).getNumericCellValue() : null);
			}
			if (row.getCell(4) != null && row.getCell(4).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.INTERNET_PORT+ RenewalsConstant.PRIMARY_COMP_NRC_SPACE,
						row.getCell(4) != null ? row.getCell(4).getNumericCellValue() : null);
			}
			if (row.getCell(5) != null && row.getCell(5).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.INTERNET_PORT+ RenewalsConstant.PRIMARY_COMP_ARC_SPACE,
						row.getCell(5) != null ? row.getCell(5).getNumericCellValue() : null);
			}

			if (row.getCell(6) != null && row.getCell(6).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.PRIMARY_COMP_MRC_SPACE, row.getCell(6) != null ? row.getCell(6).getNumericCellValue() : null);
			}
			if (row.getCell(7) != null && row.getCell(7).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.PRIMARY_COMP_NRC_SPACE, row.getCell(7) != null ? row.getCell(7).getNumericCellValue() : null);
			}
			if (row.getCell(8) != null && row.getCell(8).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.PRIMARY_COMP_ARC_SPACE, row.getCell(8) != null ? row.getCell(8).getNumericCellValue() : null);
			}

			// 5
			if (row.getCell(9) != null && row.getCell(9).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE +RenewalsConstant.PRIMARY_COMP_MRC_SPACE,
						row.getCell(9) != null ? row.getCell(9).getNumericCellValue() : null);
			}
			if (row.getCell(10) != null && row.getCell(10).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE+ RenewalsConstant.PRIMARY_COMP_NRC_SPACE,
						row.getCell(10) != null ? row.getCell(10).getNumericCellValue() : null);
			}
			if (row.getCell(11) != null && row.getCell(11).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE+ RenewalsConstant.PRIMARY_COMP_ARC_SPACE,
						row.getCell(11) != null ? row.getCell(11).getNumericCellValue() : null);
			}

			// 6
			if (row.getCell(12) != null && row.getCell(12).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.ADDON + RenewalsConstant.PRIMARY_COMP_MRC_SPACE,
						row.getCell(12) != null ? row.getCell(12).getNumericCellValue() : null);
			}

			if (row.getCell(13) != null && row.getCell(13).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.ADDON+RenewalsConstant.PRIMARY_COMP_ARC_SPACE,
						row.getCell(13) != null ? row.getCell(13).getNumericCellValue() : null);
			}

			if (row.getCell(14) != null && row.getCell(14).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.ADDITIONAL_IPS+ RenewalsConstant.PRIMARY_COMP_MRC_SPACE,
						row.getCell(14) != null ? row.getCell(14).getNumericCellValue() : null);
			}

			if (row.getCell(15) != null && row.getCell(15).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.ADDITIONAL_IPS+ RenewalsConstant.PRIMARY_COMP_ARC_SPACE,
						row.getCell(15) != null ? row.getCell(15).getNumericCellValue() : null);
			}

			if (row.getCell(16) != null && row.getCell(16).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.MAST_COST+ RenewalsConstant.PRIMARY_ATTR_EUC_SPACE,
						row.getCell(16) != null ? row.getCell(16).getNumericCellValue() : null);
			}

			if (row.getCell(17) != null && row.getCell(17).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.BURSTABLE_BANDWIDTH+ RenewalsConstant.PRIMARY_ATTR_EUC_SPACE,
						row.getCell(17) != null ? row.getCell(17).getNumericCellValue() : null);
			}

			if (row.getCell(18) != null && row.getCell(18).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.INTERNET_PORT+ RenewalsConstant.SECONDARY_COMP_MRC_SPACE,
						row.getCell(18) != null ? row.getCell(18).getNumericCellValue() : null);
			}
			if (row.getCell(19) != null && row.getCell(19).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.INTERNET_PORT+ RenewalsConstant.SECONDARY_COMP_NRC_SPACE,
						row.getCell(19) != null ? row.getCell(19).getNumericCellValue() : null);
			}
			if (row.getCell(20) != null && row.getCell(20).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.INTERNET_PORT+ RenewalsConstant.SECONDARY_COMP_ARC_SPACE,
						row.getCell(20) != null ? row.getCell(20).getNumericCellValue() : null);
			}

			if (row.getCell(21) != null && row.getCell(21).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.SECONDARY_COMP_MRC_SPACE, row.getCell(21) != null ? row.getCell(21).getNumericCellValue() : null);
			}
			if (row.getCell(22) != null && row.getCell(22).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.SECONDARY_COMP_NRC_SPACE, row.getCell(22) != null ? row.getCell(22).getNumericCellValue() : null);
			}
			if (row.getCell(23) != null && row.getCell(23).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.SECONDARY_COMP_ARC_SPACE, row.getCell(23) != null ? row.getCell(23).getNumericCellValue() : null);
			}

			// 5
			if (row.getCell(24) != null && row.getCell(24).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE+ RenewalsConstant.SECONDARY_COMP_MRC_SPACE,
						row.getCell(24) != null ? row.getCell(24).getNumericCellValue() : null);
			}
			if (row.getCell(25) != null && row.getCell(25).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE+ RenewalsConstant.SECONDARY_COMP_NRC_SPACE,
						row.getCell(25) != null ? row.getCell(25).getNumericCellValue() : null);
			}
			if (row.getCell(26) != null && row.getCell(26).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE +RenewalsConstant.SECONDARY_COMP_ARC_SPACE,
						row.getCell(26) != null ? row.getCell(26).getNumericCellValue() : null);
			}

			// 6
			if (row.getCell(27) != null && row.getCell(27).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.ADDON+ RenewalsConstant.SECONDARY_COMP_MRC_SPACE,
						row.getCell(27) != null ? row.getCell(27).getNumericCellValue() : null);
			}
				
			if (row.getCell(28) != null && row.getCell(28).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.ADDON +RenewalsConstant.SECONDARY_COMP_ARC_SPACE,
						row.getCell(28) != null ? row.getCell(28).getNumericCellValue() : null);
			}

			if (row.getCell(29) != null && row.getCell(29).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.ADDITIONAL_IPS+ RenewalsConstant.SECONDARY_COMP_MRC_SPACE,
						row.getCell(29) != null ? row.getCell(29).getNumericCellValue() : null);
			}

			if (row.getCell(30) != null && row.getCell(30).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.ADDITIONAL_IPS+ RenewalsConstant.SECONDARY_COMP_ARC_SPACE,
						row.getCell(30) != null ? row.getCell(30).getNumericCellValue() : null);
			}

			if (row.getCell(31) != null && row.getCell(31).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.MAST_COST+ RenewalsConstant.SECONDARY_ATTR_EUC_SPACE,
						row.getCell(31) != null ? row.getCell(31).getNumericCellValue() : null);
			}

			if (row.getCell(32) != null && row.getCell(32).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.BURSTABLE_BANDWIDTH+ RenewalsConstant.SECONDARY_ATTR_EUC_SPACE,
						row.getCell(32) != null ? row.getCell(32).getNumericCellValue() : null);
			}
			if (row.getCell(33) != null && row.getCell(33).getStringCellValue() !=null) {
			mappingexcel.put(RenewalsConstant.GST_NUMBER, row.getCell(33).getStringCellValue());
			}
			if(row.getCell(1)!=null) {
			String poNumber = formatter.formatCellValue(row.getCell(1));
			if (poNumber != null && poNumber != "") {
				mappingexcel.put(RenewalsConstant.PO_NUMBER, poNumber);
			}
			}
			if(row.getCell(2)!=null) {
			Date poDate =row.getCell(2).getDateCellValue();
			if (poDate != null) {
				mappingexcel.put(RenewalsConstant.PO_DATE,simpleDateFormat.format(poDate));
			}
			}
			
			
			serviceIdPriceMapping.put(val.trim(), mappingexcel);
			}
		}
		
		return serviceIdPriceMapping;
	}

	public Map<String, Map<String, Object>> constructPriceGvpn(XSSFWorkbook workbook) {
		XSSFSheet worksheet = workbook.getSheetAt(0);
		Map<String, Map<String, Object>> serviceIdPriceMapping = new HashMap<String, Map<String, Object>>();
		DataFormatter formatter = new DataFormatter();	
		for (int i = 3; i < worksheet.getPhysicalNumberOfRows(); i++) {
			XSSFRow row = worksheet.getRow(i);
			String val = formatter.formatCellValue(row.getCell(0));
			if(val!=null & val!="") {
			Map<String, Object> mappingexcel = new HashMap<String, Object>();
			if(row.getCell(1)!=null) {
			String poNumber = formatter.formatCellValue(row.getCell(1));
			if (poNumber != null && poNumber != "") {
				mappingexcel.put(RenewalsConstant.PO_NUMBER, poNumber);
			}
			}
			if(row.getCell(2)!=null) {
			Date poDate =row.getCell(2).getDateCellValue();
			if (poDate != null) {
				mappingexcel.put(RenewalsConstant.PO_DATE,simpleDateFormat.format(poDate));
			}
			}
			// 3
			if (row.getCell(3) != null && row.getCell(3).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.VPN_PORT + RenewalsConstant.PRIMARY_COMP_MRC_SPACE, row.getCell(3).getNumericCellValue());
			}
			if (row.getCell(4) != null && row.getCell(4).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.VPN_PORT + RenewalsConstant.PRIMARY_COMP_NRC_SPACE, row.getCell(4).getNumericCellValue());
			}
			if (row.getCell(5) != null && row.getCell(5).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.VPN_PORT + RenewalsConstant.PRIMARY_COMP_ARC_SPACE, row.getCell(5).getNumericCellValue());
			}

			// 4
			if (row.getCell(6) != null && row.getCell(6).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.PRIMARY_COMP_MRC_SPACE, row.getCell(6).getNumericCellValue());
			}
			if (row.getCell(7) != null && row.getCell(7).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.PRIMARY_COMP_NRC_SPACE, row.getCell(7).getNumericCellValue());
			}
			if (row.getCell(8) != null && row.getCell(8).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.PRIMARY_COMP_ARC_SPACE, row.getCell(8).getNumericCellValue());
			}

			// 5
			if (row.getCell(9) != null && row.getCell(9).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE+ RenewalsConstant.PRIMARY_COMP_MRC_SPACE, row.getCell(9).getNumericCellValue());
			}
			if (row.getCell(10) != null && row.getCell(10).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE+ RenewalsConstant.PRIMARY_COMP_NRC_SPACE, row.getCell(10).getNumericCellValue());
			}
			if (row.getCell(11) != null && row.getCell(11).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE+ RenewalsConstant.PRIMARY_COMP_ARC_SPACE, row.getCell(11).getNumericCellValue());
			}

			// 6
				/*
				 * if (row.getCell(12) != null && row.getCell(12).getNumericCellValue() != 0) {
				 * mappingexcel.put("RenewalsConstant.ADDON_RenewalsConstant.PRIMARY_COMP_MRC_SPACE, row.getCell(12).getNumericCellValue()); }
				 * 
				 * if (row.getCell(13) != null && row.getCell(13).getNumericCellValue() != 0) {
				 * mappingexcel.put("RenewalsConstant.ADDON_RenewalsConstant.PRIMARY_COMP_NRC_SPACE, row.getCell(13).getNumericCellValue()); }
				 * 
				 * if (row.getCell(14) != null && row.getCell(14).getNumericCellValue() != 0) {
				 * mappingexcel.put("RenewalsConstant.ADDON_RenewalsConstant.PRIMARY_COMP_ARC_SPACE, row.getCell(14).getNumericCellValue()); }
				 */

			// 7
			/*
			 * if (row.getCell(15) != null && row.getCell(15).getNumericCellValue() != 0) {
			 * mappingexcel.put("Shifting Charges RenewalsConstant.PRIMARY_COMP_MRC_SPACE,
			 * row.getCell(15).getNumericCellValue()); } if (row.getCell(16) != null &&
			 * row.getCell(16).getNumericCellValue() != 0) {
			 * mappingexcel.put("Shifting Charges RenewalsConstant.PRIMARY_COMP_NRC_SPACE,
			 * row.getCell(16).getNumericCellValue()); } if (row.getCell(17) != null &&
			 * row.getCell(17).getNumericCellValue() != 0) {
			 * mappingexcel.put("Shifting Charges RenewalsConstant.PRIMARY_COMP_ARC_SPACE,
			 * row.getCell(17).getNumericCellValue()); }
			 */

			// 8
			if (row.getCell(12) != null && row.getCell(12).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE_RECOVERY_CHARGES + RenewalsConstant.PRIMARY_COMP_MRC_SPACE, row.getCell(12).getNumericCellValue());
			}
			if (row.getCell(13) != null && row.getCell(13).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE_RECOVERY_CHARGES + RenewalsConstant.PRIMARY_COMP_NRC_SPACE, row.getCell(13).getNumericCellValue());
			}
			if (row.getCell(14) != null && row.getCell(14).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE_RECOVERY_CHARGES + RenewalsConstant.PRIMARY_COMP_ARC_SPACE, row.getCell(14).getNumericCellValue());
			}

				/*
				 * if (row.getCell(15) != null && row.getCell(15).getNumericCellValue() != 0) {
				 * mappingexcel.put(RenewalsConstant.MAST_COST p mrc", row.getCell(15).getNumericCellValue()); }
				 * if (row.getCell(16) != null && row.getCell(16).getNumericCellValue() != 0) {
				 * mappingexcel.put(RenewalsConstant.MAST_COST p arc", row.getCell(16).getNumericCellValue()); }
				 */
			if (row.getCell(15) != null && row.getCell(15).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.MAST_COST+ RenewalsConstant.PRIMARY_ATTR_EUC_SPACE, row.getCell(15).getNumericCellValue());
			}
			if (row.getCell(16) != null && row.getCell(16).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.BURSTABLE_BANDWIDTH+ RenewalsConstant.PRIMARY_ATTR_EUC_SPACE, row.getCell(16).getNumericCellValue());
			}

			if (row.getCell(17) != null && row.getCell(17).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.VPN_PORT + RenewalsConstant.SECONDARY_COMP_MRC_SPACE, row.getCell(17).getNumericCellValue());
			}
			if (row.getCell(18) != null && row.getCell(18).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.VPN_PORT + RenewalsConstant.SECONDARY_COMP_NRC_SPACE, row.getCell(18).getNumericCellValue());
			}
			if (row.getCell(19) != null && row.getCell(19).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.VPN_PORT + RenewalsConstant.SECONDARY_COMP_ARC_SPACE, row.getCell(19).getNumericCellValue());
			}

			// 4
			if (row.getCell(20) != null && row.getCell(20).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.SECONDARY_COMP_MRC_SPACE, row.getCell(20).getNumericCellValue());
			}
			if (row.getCell(21) != null && row.getCell(21).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.SECONDARY_COMP_NRC_SPACE, row.getCell(21).getNumericCellValue());
			}
			if (row.getCell(22) != null && row.getCell(22).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.SECONDARY_COMP_ARC_SPACE, row.getCell(22).getNumericCellValue());
			}

			// 5
			if (row.getCell(23) != null && row.getCell(23).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE +RenewalsConstant.SECONDARY_COMP_MRC_SPACE, row.getCell(23).getNumericCellValue());
			}
			if (row.getCell(24) != null && row.getCell(24).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE+ RenewalsConstant.SECONDARY_COMP_NRC_SPACE, row.getCell(24).getNumericCellValue());
			}
			if (row.getCell(25) != null && row.getCell(25).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE+ RenewalsConstant.SECONDARY_COMP_ARC_SPACE, row.getCell(25).getNumericCellValue());
			}
			// 6
				/*
				 * if (row.getCell(31) != null && row.getCell(31).getNumericCellValue() != 0) {
				 * mappingexcel.put(RenewalsConstant.ADDON RenewalsConstant.SECONDARY_COMP_MRC_SPACE, row.getCell(31).getNumericCellValue()); }
				 * 
				 * if (row.getCell(32) != null && row.getCell(32).getNumericCellValue() != 0) {
				 * mappingexcel.put(RenewalsConstant.ADDON RenewalsConstant.SECONDARY_COMP_NRC_SPACE, row.getCell(32).getNumericCellValue()); }
				 * 
				 * if (row.getCell(31) != null && row.getCell(31).getNumericCellValue() != 0) {
				 * mappingexcel.put(RenewalsConstant.ADDON RenewalsConstant.SECONDARY_COMP_ARC_SPACE, row.getCell(31).getNumericCellValue()); }
				 */

			// 7
			/*
			 * if (row.getCell(35) != null && row.getCell(35).getNumericCellValue() != 0) {
			 * mappingexcel.put("Shifting Charges RenewalsConstant.SECONDARY_COMP_MRC_SPACE,
			 * row.getCell(35).getNumericCellValue()); } if (row.getCell(36) != null &&
			 * row.getCell(36).getNumericCellValue() != 0) {
			 * mappingexcel.put("Shifting Charges RenewalsConstant.SECONDARY_COMP_NRC_SPACE,
			 * row.getCell(36).getNumericCellValue()); } if (row.getCell(37) != null &&
			 * row.getCell(37).getNumericCellValue() != 0) {
			 * mappingexcel.put("Shifting Charges RenewalsConstant.SECONDARY_COMP_ARC_SPACE,
			 * row.getCell(37).getNumericCellValue()); }
			 */

			// 8
			if (row.getCell(26) != null && row.getCell(26).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE_RECOVERY_CHARGES + RenewalsConstant.SECONDARY_COMP_MRC_SPACE, row.getCell(26).getNumericCellValue());
			}
			if (row.getCell(27) != null && row.getCell(27).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE_RECOVERY_CHARGES + RenewalsConstant.SECONDARY_COMP_NRC_SPACE, row.getCell(27).getNumericCellValue());
			}
			if (row.getCell(28) != null && row.getCell(28).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE_RECOVERY_CHARGES + RenewalsConstant.SECONDARY_COMP_ARC_SPACE, row.getCell(28).getNumericCellValue());
			}

				/*
				 * if (row.getCell(31) != null && row.getCell(31).getNumericCellValue() != 0) {
				 * mappingexcel.put(RenewalsConstant.MAST_COST s mrc", row.getCell(31).getNumericCellValue()); }
				 * if (row.getCell(32) != null && row.getCell(32).getNumericCellValue() != 0) {
				 * mappingexcel.put(RenewalsConstant.MAST_COST s arc", row.getCell(32).getNumericCellValue()); }
				 */
			if (row.getCell(29) != null && row.getCell(29).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.MAST_COST+ RenewalsConstant.SECONDARY_ATTR_EUC_SPACE, row.getCell(29).getNumericCellValue());
			}
			if (row.getCell(30) != null && row.getCell(30).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.BURSTABLE_BANDWIDTH+ RenewalsConstant.SECONDARY_ATTR_EUC_SPACE, row.getCell(30).getNumericCellValue());
			}
			if (row.getCell(31) != null && row.getCell(31).getStringCellValue() !=null) {
				mappingexcel.put(RenewalsConstant.GST_NUMBER, row.getCell(31).getStringCellValue());
				}
			serviceIdPriceMapping.put(val, mappingexcel);
			}
		}
		return serviceIdPriceMapping;
	}

	public Map<String, Map<String, Object>> constructPriceNpl(XSSFWorkbook workbook) {
		XSSFSheet worksheet = workbook.getSheetAt(0);
		Map<String, Map<String, Object>> serviceIdPriceMapping = new HashMap<String, Map<String, Object>>();
		DataFormatter formatter = new DataFormatter();	
		for (int i = 3; i < worksheet.getPhysicalNumberOfRows(); i++) {
			XSSFRow row = worksheet.getRow(i);
			String val = formatter.formatCellValue(row.getCell(0));
			if(val!=null & val!="") {
			Map<String, Object> mappingexcel = new HashMap<String, Object>();
			

			if (row.getCell(3) != null && row.getCell(3).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.NATIONAL_CONNECTIVITY + RenewalsConstant.PRIMARY_COMP_MRC_SPACE, row.getCell(3).getNumericCellValue());
				}
	    		if (row.getCell(4) != null && row.getCell(4).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.NATIONAL_CONNECTIVITY + RenewalsConstant.PRIMARY_COMP_NRC_SPACE, row.getCell(4).getNumericCellValue());
				}
				if (row.getCell(5) != null && row.getCell(5).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.NATIONAL_CONNECTIVITY + RenewalsConstant.PRIMARY_COMP_ARC_SPACE, row.getCell(5).getNumericCellValue());
				}
				
				if (row.getCell(6) != null && row.getCell(6).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LINK_MANAGEMENT_CHARGES + RenewalsConstant.PRIMARY_COMP_MRC_SPACE, row.getCell(6).getNumericCellValue());
				}
				if (row.getCell(7) != null && row.getCell(7).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LINK_MANAGEMENT_CHARGES + RenewalsConstant.PRIMARY_COMP_NRC_SPACE, row.getCell(7).getNumericCellValue());
				}
				if (row.getCell(8) != null && row.getCell(8).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LINK_MANAGEMENT_CHARGES + RenewalsConstant.PRIMARY_COMP_ARC_SPACE, row.getCell(8).getNumericCellValue());
				}
				if(row.getCell(1)!=null) {
				String poNumber = formatter.formatCellValue(row.getCell(1));
				if (poNumber != null && poNumber != "") {
					mappingexcel.put(RenewalsConstant.PO_NUMBER, poNumber);
				}
				}
				if(row.getCell(2)!=null) {
				Date poDate =row.getCell(2).getDateCellValue();
				if (poDate != null) {
					mappingexcel.put(RenewalsConstant.PO_DATE,simpleDateFormat.format(poDate));
				}
				}
				if (row.getCell(9) != null && row.getCell(9).getStringCellValue() !=null) {
					mappingexcel.put(RenewalsConstant.GST_NUMBER+"A", row.getCell(9).getStringCellValue());
					}
				if (row.getCell(10) != null && row.getCell(10).getStringCellValue() !=null) {
					mappingexcel.put(RenewalsConstant.GST_NUMBER+"B", row.getCell(10).getStringCellValue());
					}
			serviceIdPriceMapping.put(val, mappingexcel);
			}
		}
		return serviceIdPriceMapping;
	}


}
