package com.tcl.dias.oms.renewals.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.tcl.dias.oms.renewals.bean.RenewalsConstant;
import com.tcl.dias.oms.renewals.bean.RenewalsExcelObject;

@Service
public class RenewalsExcelPriceMapping {

	public Map<String, Map<String, Double>> constructPrice(XSSFWorkbook workbook) {
		XSSFSheet worksheet = workbook.getSheetAt(0);
		Map<String, Map<String, Double>> serviceIdPriceMapping = new HashMap<String, Map<String, Double>>();
		DataFormatter formatter = new DataFormatter();	
		
		for (int i = 3; i <worksheet.getPhysicalNumberOfRows(); i++) {
			XSSFRow row = worksheet.getRow(i);
			String val = formatter.formatCellValue(row.getCell(0)).toString().trim();
			if(val!=null & val!="") {
			Map<String, Double> mappingexcel = new HashMap<String, Double>();
		
			if (row.getCell(1) != null && row.getCell(1).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.INTERNET_PORT+ RenewalsConstant.PRIMARY_COMP_MRC_SPACE,
						row.getCell(1) != null ? row.getCell(1).getNumericCellValue() : null);
			}
			if (row.getCell(2) != null && row.getCell(2).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.INTERNET_PORT+ RenewalsConstant.PRIMARY_COMP_NRC_SPACE,
						row.getCell(2) != null ? row.getCell(2).getNumericCellValue() : null);
			}
			if (row.getCell(3) != null && row.getCell(3).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.INTERNET_PORT+ RenewalsConstant.PRIMARY_COMP_ARC_SPACE,
						row.getCell(3) != null ? row.getCell(3).getNumericCellValue() : null);
			}

			if (row.getCell(4) != null && row.getCell(4).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.PRIMARY_COMP_MRC_SPACE, row.getCell(4) != null ? row.getCell(4).getNumericCellValue() : null);
			}
			if (row.getCell(5) != null && row.getCell(5).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.PRIMARY_COMP_NRC_SPACE, row.getCell(5) != null ? row.getCell(5).getNumericCellValue() : null);
			}
			if (row.getCell(6) != null && row.getCell(6).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.PRIMARY_COMP_ARC_SPACE, row.getCell(6) != null ? row.getCell(6).getNumericCellValue() : null);
			}

			// 3
			if (row.getCell(7) != null && row.getCell(7).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE +RenewalsConstant.PRIMARY_COMP_MRC_SPACE,
						row.getCell(7) != null ? row.getCell(7).getNumericCellValue() : null);
			}
			if (row.getCell(8) != null && row.getCell(8).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE+ RenewalsConstant.PRIMARY_COMP_NRC_SPACE,
						row.getCell(8) != null ? row.getCell(8).getNumericCellValue() : null);
			}
			if (row.getCell(9) != null && row.getCell(9).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE+ RenewalsConstant.PRIMARY_COMP_ARC_SPACE,
						row.getCell(9) != null ? row.getCell(9).getNumericCellValue() : null);
			}

			// 4
			if (row.getCell(10) != null && row.getCell(10).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.ADDON + RenewalsConstant.PRIMARY_COMP_MRC_SPACE,
						row.getCell(10) != null ? row.getCell(10).getNumericCellValue() : null);
			}
				/*
				 * if (row.getCell(11) != null && row.getCell(11).getNumericCellValue() != 0) {
				 * mappingexcel.put("RenewalsConstant.ADDON_RenewalsConstant.PRIMARY_COMP_NRC_SPACE, row.getCell(11) != null ?
				 * row.getCell(11).getNumericCellValue() : null); }
				 */
			if (row.getCell(11) != null && row.getCell(11).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.ADDON+RenewalsConstant.PRIMARY_COMP_ARC_SPACE,
						row.getCell(11) != null ? row.getCell(11).getNumericCellValue() : null);
			}

			if (row.getCell(12) != null && row.getCell(12).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.ADDITIONAL_IPS+ RenewalsConstant.PRIMARY_COMP_MRC_SPACE,
						row.getCell(12) != null ? row.getCell(12).getNumericCellValue() : null);
			}
			/*
			 * if (row.getCell(14) != null && row.getCell(14).getNumericCellValue() != 0) {
			 * mappingexcel.put(RenewalsConstant.ADDITIONAL_IPS RenewalsConstant.PRIMARY_COMP_NRC_SPACE, row.getCell(14) != null ?
			 * row.getCell(14).getNumericCellValue() : null); }
			 */
			if (row.getCell(13) != null && row.getCell(13).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.ADDITIONAL_IPS+ RenewalsConstant.PRIMARY_COMP_ARC_SPACE,
						row.getCell(13) != null ? row.getCell(13).getNumericCellValue() : null);
			}

			/*
			 * if (row.getCell(16) != null && row.getCell(16).getNumericCellValue() != 0) {
			 * mappingexcel.put("Shifting Charges RenewalsConstant.PRIMARY_COMP_MRC_SPACE, row.getCell(16) != null ?
			 * row.getCell(16).getNumericCellValue() : null); } if (row.getCell(17) != null
			 * && row.getCell(17).getNumericCellValue() != 0) {
			 * mappingexcel.put("Shifting Charges RenewalsConstant.PRIMARY_COMP_NRC_SPACE, row.getCell(17) != null ?
			 * row.getCell(17).getNumericCellValue() : null); } if (row.getCell(18) != null
			 * && row.getCell(18).getNumericCellValue() != 0) {
			 * mappingexcel.put("Shifting Charges RenewalsConstant.PRIMARY_COMP_ARC_SPACE, row.getCell(18) != null ?
			 * row.getCell(18).getNumericCellValue() : null); }
			 */

			// attributes
				/*
				 * if (row.getCell(15) != null && row.getCell(15).getNumericCellValue() != 0) {
				 * mappingexcel.put(RenewalsConstant.MAST_COST p mrc", row.getCell(15) != null ?
				 * row.getCell(15).getNumericCellValue() : null); }
				 */
				/*
				 * if (row.getCell(16) != null && row.getCell(16).getNumericCellValue() != 0) {
				 * mappingexcel.put(RenewalsConstant.MAST_COST p arc", row.getCell(16) != null ?
				 * row.getCell(16).getNumericCellValue() : null); }
				 */
			if (row.getCell(14) != null && row.getCell(14).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.MAST_COST+ RenewalsConstant.PRIMARY_ATTR_EUC_SPACE,
						row.getCell(14) != null ? row.getCell(14).getNumericCellValue() : null);
			}

			if (row.getCell(15) != null && row.getCell(15).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.BURSTABLE_BANDWIDTH+ RenewalsConstant.PRIMARY_ATTR_EUC_SPACE,
						row.getCell(15) != null ? row.getCell(15).getNumericCellValue() : null);
			}

			if (row.getCell(16) != null && row.getCell(16).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.INTERNET_PORT+ RenewalsConstant.SECONDARY_COMP_MRC_SPACE,
						row.getCell(16) != null ? row.getCell(16).getNumericCellValue() : null);
			}
			if (row.getCell(17) != null && row.getCell(17).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.INTERNET_PORT+ RenewalsConstant.SECONDARY_COMP_NRC_SPACE,
						row.getCell(17) != null ? row.getCell(17).getNumericCellValue() : null);
			}
			if (row.getCell(18) != null && row.getCell(18).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.INTERNET_PORT+ RenewalsConstant.SECONDARY_COMP_ARC_SPACE,
						row.getCell(18) != null ? row.getCell(18).getNumericCellValue() : null);
			}

			if (row.getCell(19) != null && row.getCell(19).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.SECONDARY_COMP_MRC_SPACE, row.getCell(19) != null ? row.getCell(19).getNumericCellValue() : null);
			}
			if (row.getCell(20) != null && row.getCell(20).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.SECONDARY_COMP_NRC_SPACE, row.getCell(20) != null ? row.getCell(20).getNumericCellValue() : null);
			}
			if (row.getCell(21) != null && row.getCell(21).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.SECONDARY_COMP_ARC_SPACE, row.getCell(21) != null ? row.getCell(21).getNumericCellValue() : null);
			}

			// 3
			if (row.getCell(22) != null && row.getCell(22).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE+ RenewalsConstant.SECONDARY_COMP_MRC_SPACE,
						row.getCell(22) != null ? row.getCell(22).getNumericCellValue() : null);
			}
			if (row.getCell(23) != null && row.getCell(23).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE+ RenewalsConstant.SECONDARY_COMP_NRC_SPACE,
						row.getCell(23) != null ? row.getCell(23).getNumericCellValue() : null);
			}
			if (row.getCell(24) != null && row.getCell(24).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE +RenewalsConstant.SECONDARY_COMP_ARC_SPACE,
						row.getCell(24) != null ? row.getCell(24).getNumericCellValue() : null);
			}

			// 4
			if (row.getCell(25) != null && row.getCell(25).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.ADDON+ RenewalsConstant.SECONDARY_COMP_MRC_SPACE,
						row.getCell(25) != null ? row.getCell(25).getNumericCellValue() : null);
			}
				/*
				 * if (row.getCell(29) != null && row.getCell(29).getNumericCellValue() != 0) {
				 * mappingexcel.put(RenewalsConstant.ADDON RenewalsConstant.SECONDARY_COMP_NRC_SPACE, row.getCell(29) != null ?
				 * row.getCell(29).getNumericCellValue() : null); }
				 */
			if (row.getCell(26) != null && row.getCell(26).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.ADDON +RenewalsConstant.SECONDARY_COMP_ARC_SPACE,
						row.getCell(26) != null ? row.getCell(26).getNumericCellValue() : null);
			}

			if (row.getCell(27) != null && row.getCell(27).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.ADDITIONAL_IPS+ RenewalsConstant.SECONDARY_COMP_MRC_SPACE,
						row.getCell(27) != null ? row.getCell(27).getNumericCellValue() : null);
			}
			/*
			 * if (row.getCell(36) != null && row.getCell(36).getNumericCellValue() != 0) {
			 * mappingexcel.put(RenewalsConstant.ADDITIONAL_IPS RenewalsConstant.SECONDARY_COMP_NRC_SPACE, row.getCell(36) != null ?
			 * row.getCell(36).getNumericCellValue() : null); }
			 */
			if (row.getCell(28) != null && row.getCell(28).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.ADDITIONAL_IPS+ RenewalsConstant.SECONDARY_COMP_ARC_SPACE,
						row.getCell(28) != null ? row.getCell(28).getNumericCellValue() : null);
			}

			/*
			 * if (row.getCell(38) != null && row.getCell(38).getNumericCellValue() != 0) {
			 * mappingexcel.put("Shifting Charges RenewalsConstant.SECONDARY_COMP_MRC_SPACE, row.getCell(38) != null ?
			 * row.getCell(38).getNumericCellValue() : null); } if (row.getCell(39) != null
			 * && row.getCell(39).getNumericCellValue() != 0) {
			 * mappingexcel.put("Shifting Charges RenewalsConstant.SECONDARY_COMP_NRC_SPACE, row.getCell(39) != null ?
			 * row.getCell(39).getNumericCellValue() : null); } if (row.getCell(40) != null
			 * && row.getCell(40).getNumericCellValue() != 0) {
			 * mappingexcel.put("Shifting Charges RenewalsConstant.SECONDARY_COMP_ARC_SPACE, row.getCell(40) != null ?
			 * row.getCell(40).getNumericCellValue() : null); }
			 */

			// attributes
				/*
				 * if (row.getCell(33) != null && row.getCell(33).getNumericCellValue() != 0) {
				 * mappingexcel.put(RenewalsConstant.MAST_COST s mrc", row.getCell(33) != null ?
				 * row.getCell(33).getNumericCellValue() : null); }
				 * 
				 * if (row.getCell(34) != null && row.getCell(34).getNumericCellValue() != 0) {
				 * mappingexcel.put(RenewalsConstant.MAST_COST s arc", row.getCell(34) != null ?
				 * row.getCell(34).getNumericCellValue() : null); }
				 */
			if (row.getCell(29) != null && row.getCell(29).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.MAST_COST+ RenewalsConstant.SECONDARY_ATTR_EUC_SPACE,
						row.getCell(29) != null ? row.getCell(29).getNumericCellValue() : null);
			}

			if (row.getCell(30) != null && row.getCell(30).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.BURSTABLE_BANDWIDTH+ RenewalsConstant.SECONDARY_ATTR_EUC_SPACE,
						row.getCell(30) != null ? row.getCell(30).getNumericCellValue() : null);
			}
			serviceIdPriceMapping.put(val.trim(), mappingexcel);
			}
		}
		
		return serviceIdPriceMapping;
	}

	public Map<String, Map<String, Double>> constructPriceGvpn(XSSFWorkbook workbook) {
		XSSFSheet worksheet = workbook.getSheetAt(0);
		Map<String, Map<String, Double>> serviceIdPriceMapping = new HashMap<String, Map<String, Double>>();
		DataFormatter formatter = new DataFormatter();	
		for (int i = 3; i < worksheet.getPhysicalNumberOfRows(); i++) {
			XSSFRow row = worksheet.getRow(i);
			String val = formatter.formatCellValue(row.getCell(0));
			if(val!=null & val!="") {
			Map<String, Double> mappingexcel = new HashMap<String, Double>();
			
			// 1
			if (row.getCell(1) != null && row.getCell(1).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.VPN_PORT + RenewalsConstant.PRIMARY_COMP_MRC_SPACE, row.getCell(1).getNumericCellValue());
			}
			if (row.getCell(2) != null && row.getCell(2).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.VPN_PORT + RenewalsConstant.PRIMARY_COMP_NRC_SPACE, row.getCell(2).getNumericCellValue());
			}
			if (row.getCell(3) != null && row.getCell(3).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.VPN_PORT + RenewalsConstant.PRIMARY_COMP_ARC_SPACE, row.getCell(3).getNumericCellValue());
			}

			// 2
			if (row.getCell(4) != null && row.getCell(4).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.PRIMARY_COMP_MRC_SPACE, row.getCell(4).getNumericCellValue());
			}
			if (row.getCell(5) != null && row.getCell(5).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.PRIMARY_COMP_NRC_SPACE, row.getCell(5).getNumericCellValue());
			}
			if (row.getCell(6) != null && row.getCell(6).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.PRIMARY_COMP_ARC_SPACE, row.getCell(6).getNumericCellValue());
			}

			// 3
			if (row.getCell(7) != null && row.getCell(7).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE+ RenewalsConstant.PRIMARY_COMP_MRC_SPACE, row.getCell(7).getNumericCellValue());
			}
			if (row.getCell(8) != null && row.getCell(8).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE+ RenewalsConstant.PRIMARY_COMP_NRC_SPACE, row.getCell(8).getNumericCellValue());
			}
			if (row.getCell(9) != null && row.getCell(9).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE+ RenewalsConstant.PRIMARY_COMP_ARC_SPACE, row.getCell(9).getNumericCellValue());
			}

			// 4
				/*
				 * if (row.getCell(10) != null && row.getCell(10).getNumericCellValue() != 0) {
				 * mappingexcel.put("RenewalsConstant.ADDON_RenewalsConstant.PRIMARY_COMP_MRC_SPACE, row.getCell(10).getNumericCellValue()); }
				 * 
				 * if (row.getCell(11) != null && row.getCell(11).getNumericCellValue() != 0) {
				 * mappingexcel.put("RenewalsConstant.ADDON_RenewalsConstant.PRIMARY_COMP_NRC_SPACE, row.getCell(11).getNumericCellValue()); }
				 * 
				 * if (row.getCell(12) != null && row.getCell(12).getNumericCellValue() != 0) {
				 * mappingexcel.put("RenewalsConstant.ADDON_RenewalsConstant.PRIMARY_COMP_ARC_SPACE, row.getCell(12).getNumericCellValue()); }
				 */

			// 5
			/*
			 * if (row.getCell(13) != null && row.getCell(13).getNumericCellValue() != 0) {
			 * mappingexcel.put("Shifting Charges RenewalsConstant.PRIMARY_COMP_MRC_SPACE,
			 * row.getCell(13).getNumericCellValue()); } if (row.getCell(14) != null &&
			 * row.getCell(14).getNumericCellValue() != 0) {
			 * mappingexcel.put("Shifting Charges RenewalsConstant.PRIMARY_COMP_NRC_SPACE,
			 * row.getCell(14).getNumericCellValue()); } if (row.getCell(15) != null &&
			 * row.getCell(15).getNumericCellValue() != 0) {
			 * mappingexcel.put("Shifting Charges RenewalsConstant.PRIMARY_COMP_ARC_SPACE,
			 * row.getCell(15).getNumericCellValue()); }
			 */

			// 6
			if (row.getCell(10) != null && row.getCell(10).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE_RECOVERY_CHARGES + RenewalsConstant.PRIMARY_COMP_MRC_SPACE, row.getCell(10).getNumericCellValue());
			}
			if (row.getCell(11) != null && row.getCell(11).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE_RECOVERY_CHARGES + RenewalsConstant.PRIMARY_COMP_NRC_SPACE, row.getCell(11).getNumericCellValue());
			}
			if (row.getCell(12) != null && row.getCell(12).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE_RECOVERY_CHARGES + RenewalsConstant.PRIMARY_COMP_ARC_SPACE, row.getCell(12).getNumericCellValue());
			}

				/*
				 * if (row.getCell(13) != null && row.getCell(13).getNumericCellValue() != 0) {
				 * mappingexcel.put(RenewalsConstant.MAST_COST p mrc", row.getCell(13).getNumericCellValue()); }
				 * if (row.getCell(14) != null && row.getCell(14).getNumericCellValue() != 0) {
				 * mappingexcel.put(RenewalsConstant.MAST_COST p arc", row.getCell(14).getNumericCellValue()); }
				 */
			if (row.getCell(13) != null && row.getCell(13).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.MAST_COST+ RenewalsConstant.PRIMARY_ATTR_EUC_SPACE, row.getCell(13).getNumericCellValue());
			}
			if (row.getCell(14) != null && row.getCell(14).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.BURSTABLE_BANDWIDTH+ RenewalsConstant.PRIMARY_ATTR_EUC_SPACE, row.getCell(14).getNumericCellValue());
			}

			if (row.getCell(15) != null && row.getCell(15).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.VPN_PORT + RenewalsConstant.SECONDARY_COMP_MRC_SPACE, row.getCell(15).getNumericCellValue());
			}
			if (row.getCell(16) != null && row.getCell(16).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.VPN_PORT + RenewalsConstant.SECONDARY_COMP_NRC_SPACE, row.getCell(16).getNumericCellValue());
			}
			if (row.getCell(17) != null && row.getCell(17).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.VPN_PORT + RenewalsConstant.SECONDARY_COMP_ARC_SPACE, row.getCell(17).getNumericCellValue());
			}

			// 2
			if (row.getCell(18) != null && row.getCell(18).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.SECONDARY_COMP_MRC_SPACE, row.getCell(18).getNumericCellValue());
			}
			if (row.getCell(19) != null && row.getCell(19).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.SECONDARY_COMP_NRC_SPACE, row.getCell(19).getNumericCellValue());
			}
			if (row.getCell(20) != null && row.getCell(20).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE + RenewalsConstant.SECONDARY_COMP_ARC_SPACE, row.getCell(20).getNumericCellValue());
			}

			// 3
			if (row.getCell(21) != null && row.getCell(21).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE +RenewalsConstant.SECONDARY_COMP_MRC_SPACE, row.getCell(21).getNumericCellValue());
			}
			if (row.getCell(22) != null && row.getCell(22).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE+ RenewalsConstant.SECONDARY_COMP_NRC_SPACE, row.getCell(22).getNumericCellValue());
			}
			if (row.getCell(23) != null && row.getCell(23).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LAST_MILE+ RenewalsConstant.SECONDARY_COMP_ARC_SPACE, row.getCell(23).getNumericCellValue());
			}
			// 4
				/*
				 * if (row.getCell(29) != null && row.getCell(29).getNumericCellValue() != 0) {
				 * mappingexcel.put(RenewalsConstant.ADDON RenewalsConstant.SECONDARY_COMP_MRC_SPACE, row.getCell(29).getNumericCellValue()); }
				 * 
				 * if (row.getCell(30) != null && row.getCell(30).getNumericCellValue() != 0) {
				 * mappingexcel.put(RenewalsConstant.ADDON RenewalsConstant.SECONDARY_COMP_NRC_SPACE, row.getCell(30).getNumericCellValue()); }
				 * 
				 * if (row.getCell(31) != null && row.getCell(31).getNumericCellValue() != 0) {
				 * mappingexcel.put(RenewalsConstant.ADDON RenewalsConstant.SECONDARY_COMP_ARC_SPACE, row.getCell(31).getNumericCellValue()); }
				 */

			// 5
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

			// 6
			if (row.getCell(24) != null && row.getCell(24).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE_RECOVERY_CHARGES + RenewalsConstant.SECONDARY_COMP_MRC_SPACE, row.getCell(24).getNumericCellValue());
			}
			if (row.getCell(25) != null && row.getCell(25).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE_RECOVERY_CHARGES + RenewalsConstant.SECONDARY_COMP_NRC_SPACE, row.getCell(25).getNumericCellValue());
			}
			if (row.getCell(26) != null && row.getCell(26).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.CPE_RECOVERY_CHARGES + RenewalsConstant.SECONDARY_COMP_ARC_SPACE, row.getCell(26).getNumericCellValue());
			}

				/*
				 * if (row.getCell(29) != null && row.getCell(29).getNumericCellValue() != 0) {
				 * mappingexcel.put(RenewalsConstant.MAST_COST s mrc", row.getCell(29).getNumericCellValue()); }
				 * if (row.getCell(30) != null && row.getCell(30).getNumericCellValue() != 0) {
				 * mappingexcel.put(RenewalsConstant.MAST_COST s arc", row.getCell(30).getNumericCellValue()); }
				 */
			if (row.getCell(27) != null && row.getCell(27).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.MAST_COST+ RenewalsConstant.SECONDARY_ATTR_EUC_SPACE, row.getCell(27).getNumericCellValue());
			}
			if (row.getCell(28) != null && row.getCell(28).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.BURSTABLE_BANDWIDTH+ RenewalsConstant.SECONDARY_ATTR_EUC_SPACE, row.getCell(28).getNumericCellValue());
			}

			serviceIdPriceMapping.put(val, mappingexcel);
			}
		}
		return serviceIdPriceMapping;
	}

	public Map<String, Map<String, Double>> constructPriceNpl(XSSFWorkbook workbook) {
		XSSFSheet worksheet = workbook.getSheetAt(0);
		Map<String, Map<String, Double>> serviceIdPriceMapping = new HashMap<String, Map<String, Double>>();
		DataFormatter formatter = new DataFormatter();	
		for (int i = 3; i < worksheet.getPhysicalNumberOfRows(); i++) {
			XSSFRow row = worksheet.getRow(i);
			String val = formatter.formatCellValue(row.getCell(0));
			if(val!=null & val!="") {
			Map<String, Double> mappingexcel = new HashMap<String, Double>();
				
			if (row.getCell(1) != null && row.getCell(1).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.NATIONAL_CONNECTIVITY + RenewalsConstant.PRIMARY_COMP_MRC_SPACE, row.getCell(1).getNumericCellValue());
				}
	    		if (row.getCell(2) != null && row.getCell(2).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.NATIONAL_CONNECTIVITY + RenewalsConstant.PRIMARY_COMP_NRC_SPACE, row.getCell(2).getNumericCellValue());
				}
				if (row.getCell(3) != null && row.getCell(3).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.NATIONAL_CONNECTIVITY + RenewalsConstant.PRIMARY_COMP_ARC_SPACE, row.getCell(3).getNumericCellValue());
				}
				
				if (row.getCell(4) != null && row.getCell(4).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LINK_MANAGEMENT_CHARGES + RenewalsConstant.PRIMARY_COMP_MRC_SPACE, row.getCell(4).getNumericCellValue());
				}
				if (row.getCell(5) != null && row.getCell(5).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LINK_MANAGEMENT_CHARGES + RenewalsConstant.PRIMARY_COMP_NRC_SPACE, row.getCell(5).getNumericCellValue());
				}
				if (row.getCell(6) != null && row.getCell(6).getNumericCellValue() != 0) {
				mappingexcel.put(RenewalsConstant.LINK_MANAGEMENT_CHARGES + RenewalsConstant.PRIMARY_COMP_ARC_SPACE, row.getCell(6).getNumericCellValue());
				}
			/*
			 * if (row.getCell(6) != null && row.getCell(6).getNumericCellValue() != 0) {
			 * mappingexcel.put("Shifting Charges RenewalsConstant.PRIMARY_COMP_MRC_SPACE,
			 * row.getCell(6).getNumericCellValue()); } if (row.getCell(7) != null &&
			 * row.getCell(7).getNumericCellValue() != 0) {
			 * mappingexcel.put("Shifting Charges RenewalsConstant.PRIMARY_COMP_NRC_SPACE,
			 * row.getCell(7).getNumericCellValue()); } if (row.getCell(8) != null &&
			 * row.getCell(8).getNumericCellValue() != 0) {
			 * mappingexcel.put("Shifting Charges RenewalsConstant.PRIMARY_COMP_ARC_SPACE,
			 * row.getCell(8).getNumericCellValue()); }
			 */
			// 1
			serviceIdPriceMapping.put(val, mappingexcel);
			}
		}
		return serviceIdPriceMapping;
	}

	public Map<String, RenewalsExcelObject> constructIllPrice(XSSFWorkbook workbook) {
		XSSFSheet worksheet = workbook.getSheetAt(0);
		Map<String, RenewalsExcelObject> serviceIdPriceMapping = new HashMap<String, RenewalsExcelObject>();
		DataFormatter formatter = new DataFormatter();
		// for (int rowCount = 0; rowCount < worksheet.getPhysicalNumberOfRows();
		// rowCount++) {
		Row row = worksheet.getRow(0);
		for (int colCount = 1; colCount < row.getPhysicalNumberOfCells(); colCount = colCount + 4) {
			Cell cell = row.getCell(colCount);
			if (cell != null) {
				String val = formatter.formatCellValue(row.getCell(colCount));
				if (val != null && val != "") {
					RenewalsExcelObject renewalsExcelObject = mapPriceIll(worksheet, colCount, colCount + 4);
					serviceIdPriceMapping.put(cell.getStringCellValue(), renewalsExcelObject);
				} else {
					break;
				}
			} else {
				break;
			}
		}
		// }
		return serviceIdPriceMapping;
	}

	public RenewalsExcelObject mapPriceIll(XSSFSheet worksheet, int startColumn, int endColumn) {
		Map<String, Double> mappingexcel = new HashMap<String, Double>();
		RenewalsExcelObject renewalsExcelObject = new RenewalsExcelObject();
		Map<String, Map<String, Double>> componentAttributes = new HashMap<String, Map<String, Double>>();
		// List<Map<String, Map<String, Double>>> listOfComAttri = new
		// ArrayList<Map<String, Map<String, Double>>>();
		String componentKey = null;
		for (int rowCount = 3; rowCount <= worksheet.getPhysicalNumberOfRows(); rowCount++) {
			Row row = worksheet.getRow(rowCount);
			int i = 0;
			if (rowCount == worksheet.getPhysicalNumberOfRows()) {
				if (componentKey != null && !mappingexcel.isEmpty()) {
					componentAttributes.put(componentKey, mappingexcel);
					// listOfComAttri.add(componentAttributes);
				}
				break;
			}
			if (row.getCell(0).getStringCellValue().contains("component")) {
				if (componentKey != null && !mappingexcel.isEmpty()) {
					componentAttributes.put(componentKey, mappingexcel);
					// listOfComAttri.add(componentAttributes);
				}
				mappingexcel = new HashMap<String, Double>();

			}
			for (int colCount = startColumn; colCount <= endColumn; colCount++) {

				i++;

				Cell cell = row.getCell(colCount);
				if (cell != null && i == 1 && cell.getNumericCellValue() != 0) {
					mappingexcel.put(row.getCell(0).getStringCellValue() + " p " + "mrc",
							row.getCell(colCount).getNumericCellValue());
				}
				if (cell != null && i == 2 && cell.getNumericCellValue() != 0) {
					mappingexcel.put(row.getCell(0).getStringCellValue() + " p " + "nrc",
							row.getCell(colCount).getNumericCellValue());
				}
				if (cell != null && i == 3 && cell.getNumericCellValue() != 0) {
					mappingexcel.put(row.getCell(0).getStringCellValue() + " p " + "arc",
							row.getCell(colCount).getNumericCellValue());
				}
				if (cell != null && i == 4 && cell.getNumericCellValue() != 0) {
					mappingexcel.put(row.getCell(0).getStringCellValue() + " p " + "euc",
							row.getCell(colCount).getNumericCellValue());
				}

				/*
				 * if (cell != null && i == 5 && cell.getNumericCellValue()!=0) {
				 * mappingexcel.put(row.getCell(0).getStringCellValue() + " s " + "mrc",
				 * row.getCell(colCount).getNumericCellValue());
				 * renewalsExcelObject.setDual(true); } if (cell != null && i == 6 &&
				 * cell.getNumericCellValue()!=0) {
				 * mappingexcel.put(row.getCell(0).getStringCellValue() + " s " + "nrc",
				 * row.getCell(colCount).getNumericCellValue());
				 * renewalsExcelObject.setDual(true); } if (cell != null && i == 7 &&
				 * cell.getNumericCellValue()!=0) {
				 * mappingexcel.put(row.getCell(0).getStringCellValue() + " s " + "arc",
				 * row.getCell(colCount).getNumericCellValue());
				 * renewalsExcelObject.setDual(true); } if (cell != null && i == 8 &&
				 * cell.getNumericCellValue()!=0) {
				 * mappingexcel.put(row.getCell(0).getStringCellValue() + " s " + "euc",
				 * row.getCell(colCount).getNumericCellValue());
				 * renewalsExcelObject.setDual(true); }
				 */

			}
			if (row.getCell(0).getStringCellValue().contains("component")) {
				componentKey = row.getCell(0).getStringCellValue();
			}
		}
		renewalsExcelObject.setRenewalsComponentDetailList(componentAttributes);
		return renewalsExcelObject;
		// return componentAttributes;
	}
}
