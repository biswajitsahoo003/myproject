package com.tcl.dias.customer.service.v1;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.customer.bean.CallidusPartnerCommisions;
import com.tcl.dias.customer.bean.CommissionReportBean;
import com.tcl.dias.customer.bean.CommissionReportResponseBean;
import com.tcl.dias.customer.bean.PartnerCommissionReportBean;
import com.tcl.dias.customer.bean.YearlyCommissionReportBean;
import com.tcl.dias.customer.constants.PDFConstants;
import com.tcl.dias.customer.constants.PartnerCustomerConstants;

@Service
public class PartnerDashboardPdfReportService {

    @Autowired
    PartnerCustomerService partnerCustomerService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SpringTemplateEngine templateEngine;

    private static final Logger LOGGER = LoggerFactory.getLogger(PartnerDashboardPdfReportService.class);

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-yy");

    private static List<String> months= Arrays.asList("APRIL","MAY","JUNE","JULY","AUGUST","SEPTEMBER",
        "OCTOBER","NOVEMBER","DECEMBER","JANUARY","FEBRUARY","MARCH");


    /**
     * Get download Partner Compensation Details Report from Callidus
     *
     * @param partnerId
     * @param response
     * @return {@link String}
     */
    public String processCompensationDetailReport(Integer partnerId, String reportName, HttpServletResponse response) {
        String html = null;
        try {
            Objects.requireNonNull(partnerId, "PartnerID cannot be null");
            partnerCustomerService.validatePartnerId(partnerId);
            List<String> partnerCUIDs = partnerCustomerService.getPartnerCUIDs(partnerId);
            CommissionReportResponseBean commissionReportResponseBean = null;
            String templateName = null;
            String fileName = null;
            switch (reportName) {
                /** for Compensation Summary report download*/
                case "compensation_detailed":
                    commissionReportResponseBean = downloadCompensationDetailedReport(partnerCUIDs);
                    templateName = "compensation_template";
                    fileName = "Compensation-Detailed-Statement.pdf";
                    break;
                /**for Compensation Detailed report download*/
                case "compensation_summary":
                    commissionReportResponseBean = downloadCompensationSummaryReport(partnerCUIDs);
                    templateName = "compensation_summery_template";
                    fileName = "Compensation-Statement-Summary.pdf";
                    break;
                /**for Yearly summery report download*/
                default:
                    commissionReportResponseBean = downloadYearlySummaryReport(partnerCUIDs);
                    templateName = "yearly_summary_template";
                    fileName = "Yearly-Summary-Report.pdf";
            }

            Map<String, Object> variable = objectMapper.convertValue(commissionReportResponseBean, Map.class);

            Context context = new Context();
            context.setVariables(variable);

            html = templateEngine.process(templateName, context);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PDFGenerator.createPdf(html, bos);
            byte[] outArray = bos.toByteArray();
            response.reset();
            response.setContentType(MediaType.APPLICATION_PDF_VALUE);
            response.setContentLength(outArray.length);
            response.setHeader(PDFConstants.EXPIRES + CommonConstants.COLON, "0");
            response.setHeader(PDFConstants.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + fileName + "\"");
            FileCopyUtils.copy(outArray, response.getOutputStream());
            bos.flush();
            bos.close();
        } catch (Exception e) {
            LOGGER.error("Error while downloding CompensationDetailReport for Partner portal" + e.getMessage());
        }
        return html;
    }

    private CommissionReportResponseBean downloadCompensationDetailedReport(List<String> partnerCUIDs) {
        CommissionReportResponseBean finalCommissionReportBean = new CommissionReportResponseBean();
        finalCommissionReportBean.setReportDownloadDate(LocalDate.now().toString());
        String monthStartDate=LocalDate.now().minusMonths(1).withDayOfMonth(1).format(formatter);
        String monthEndDate=LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth()).format(formatter);
        List<CallidusPartnerCommisions> callidusPartnerCommisions = partnerCustomerService.getPartnerCommisionsBetweenDateRange(partnerCUIDs,monthStartDate,monthEndDate);
        Map<String, List<CallidusPartnerCommisions>> callidusData = callidusPartnerCommisions.stream()
                .collect(Collectors.groupingBy(CallidusPartnerCommisions::getPartnerId));
        List<PartnerCommissionReportBean> partnerCommissionReportBeans = new ArrayList<>();

        callidusData.forEach((key, callidusPartnerCommisionsByPartnerCuid) -> {
            PartnerCommissionReportBean partnerCommissionReportBean = new PartnerCommissionReportBean();
            CommissionReportBean commissionReportBean = new CommissionReportBean();
            List<CommissionReportBean> commissionReportBeans = new ArrayList<>();
            partnerCommissionReportBean.setBaseCurrency(callidusPartnerCommisionsByPartnerCuid.stream().findFirst().get().getPartnerBaseCurrency());
            partnerCommissionReportBean.setPartnerName(callidusPartnerCommisionsByPartnerCuid.stream().findFirst().get().getPartnerName());
            String previousMonth = LocalDate.now().minusMonths(1).getMonth().name();
            partnerCommissionReportBean.setReportPeriod(previousMonth);


            Map<String, List<CallidusPartnerCommisions>> sellWithMonth = callidusPartnerCommisionsByPartnerCuid.stream().
                    filter(partnerCommission -> partnerCommission.getCommissionType().contains(PartnerCustomerConstants.CALLIDUS_SELL_WITH_COMISSION)).collect(Collectors.groupingBy(this::getMonthNameForGroupBy, Collectors.toList()));
            Map<String, List<CallidusPartnerCommisions>> sellThruMonth = callidusPartnerCommisionsByPartnerCuid.stream().
                    filter(partnerCommission -> partnerCommission.getCommissionType().contains(PartnerCustomerConstants.CALLIDUS_SELL_THROUGH_COMISSION)).collect(Collectors.groupingBy(this::getMonthNameForGroupBy, Collectors.toList()));
            Map<String, List<CallidusPartnerCommisions>> leadReferalMonth = callidusPartnerCommisionsByPartnerCuid.stream().
                    filter(partnerCommission -> partnerCommission.getCommissionType().contains(PartnerCustomerConstants.CALLIDUS_LEAD_REFERRAL_COMISSION)).collect(Collectors.groupingBy(this::getMonthNameForGroupBy, Collectors.toList()));


            previousMonth = previousMonth.toUpperCase();
            if (!sellWithMonth.isEmpty() && sellWithMonth.containsKey(previousMonth)) {
                commissionReportBean.setTotalBilledValueSellWith(BigDecimal.valueOf(calculateTotalBilledValue(sellWithMonth.get(previousMonth))).toString());
                commissionReportBean.setTotalCommissionSellWith(calculateTotalCommissionPersentage(sellWithMonth.get(previousMonth)) + "%");
                commissionReportBean.setTotalCommissionAmmountSellWith(BigDecimal.valueOf(calculateTotalIncentiveAmount(sellWithMonth.get(previousMonth))).toString());
                commissionReportBean.setCommisionsSellWith(sellWithMonth.get(previousMonth));
            }
            if (!sellThruMonth.isEmpty() && sellThruMonth.containsKey(previousMonth)) {
                commissionReportBean.setTotalBilledValueSellThru(BigDecimal.valueOf(calculateTotalBilledValue(sellThruMonth.get(previousMonth))).toString());
                commissionReportBean.setTotalCommissionSellThru(calculateTotalCommissionPersentage(sellThruMonth.get(previousMonth)) + "%");
                commissionReportBean.setTotalCommissionAmmountSellThru(BigDecimal.valueOf(calculateTotalIncentiveAmount(sellThruMonth.get(previousMonth))).toString());
                commissionReportBean.setCommisionsSellThru(sellThruMonth.get(previousMonth));
            }
            if (!leadReferalMonth.isEmpty() && leadReferalMonth.containsKey(previousMonth)) {
                commissionReportBean.setTotalBilledValueLeadReferral(BigDecimal.valueOf(calculateTotalBilledValue(leadReferalMonth.get(previousMonth))).toString());
                commissionReportBean.setTotalCommissionLeadReferral(calculateTotalCommissionPersentage(leadReferalMonth.get(previousMonth)) + "%");
                commissionReportBean.setTotalCommissionAmmountLeadReferral(BigDecimal.valueOf(calculateTotalIncentiveAmount(leadReferalMonth.get(previousMonth))).toString());
                commissionReportBean.setLeadReferral(leadReferalMonth.get(previousMonth));
            }
            commissionReportBeans.add(commissionReportBean);
            partnerCommissionReportBean.setCommissionReportBean(commissionReportBeans);
            partnerCommissionReportBeans.add(partnerCommissionReportBean);

        });
        finalCommissionReportBean.setPartnerCommissionReportBeans(partnerCommissionReportBeans);

        return finalCommissionReportBean;

    }

    private CommissionReportResponseBean downloadCompensationSummaryReport(List<String> partnerCUIDs) {
        CommissionReportResponseBean partnerCommissionReportBean = downloadCompensationDetailedReport(partnerCUIDs);
        partnerCommissionReportBean.getPartnerCommissionReportBeans().forEach(partnerCommissionReport -> {
            partnerCommissionReport.getCommissionReportBean().forEach(commissionReportBean -> {
                Double totalEarnings = 0.0D;
                Double overAchievementValue = 0.0D;
                if (Objects.nonNull(commissionReportBean.getCommisionsSellWith()) && !commissionReportBean.getCommisionsSellWith().isEmpty()) {
                    //overAchievementValue = commissionReportBean.getTotalBilledValueSellWith() +
                            //commissionReportBean.getTotalCommissionAmmountSellWith();
                    //commissionReportBean.setOverachievementValueSellWith(doRoundForTwoDecimal(overAchievementValue));
                    totalEarnings = Double.valueOf(commissionReportBean.getTotalCommissionAmmountSellWith());
                }
                if (Objects.nonNull(commissionReportBean.getCommisionsSellThru()) && !commissionReportBean.getCommisionsSellThru().isEmpty()) {
                   //overAchievementValue = commissionReportBean.getTotalBilledValueSellThru() +
                           // commissionReportBean.getTotalCommissionAmmountSellThru();
                   // commissionReportBean.setOverachievementValuetSellThru(doRoundForTwoDecimal(overAchievementValue));
                    totalEarnings = totalEarnings + Double.valueOf(commissionReportBean.getTotalCommissionAmmountSellThru());
                }
                if (Objects.nonNull(commissionReportBean.getLeadReferral()) && !commissionReportBean.getLeadReferral().isEmpty()) {
                    //overAchievementValue = commissionReportBean.getTotalBilledValueLeadReferral() +
                            //commissionReportBean.getTotalCommissionAmmountLeadReferral();
                   // commissionReportBean.setOverachievementValueLeadReferral(doRoundForTwoDecimal(overAchievementValue));
                    totalEarnings = totalEarnings +  Double.valueOf(commissionReportBean.getTotalCommissionAmmountLeadReferral());
                }
                commissionReportBean.setTotalEarnings(BigDecimal.valueOf(totalEarnings).toString());
            });
        });

        return partnerCommissionReportBean;
    }

    private CommissionReportResponseBean downloadYearlySummaryReport(List<String> partnerCUIDs) {
        CommissionReportResponseBean finalCommissionReportBean = new CommissionReportResponseBean();
        finalCommissionReportBean.setReportDownloadDate(LocalDate.now().toString());

        try {
            Map<String, String> dates = getStartAndEndDateOfFinancialYear();
            List<CallidusPartnerCommisions> callidusPartnerCommisions = partnerCustomerService.getPartnerCommisionsBetweenDateRange(partnerCUIDs,dates.get("startDate"),dates.get("endDate"));

            Map<String, List<CallidusPartnerCommisions>> callidusData = callidusPartnerCommisions.stream()
                    .collect(Collectors.groupingBy(CallidusPartnerCommisions::getPartnerId));
            List<PartnerCommissionReportBean> partnerCommissionReportBeans = new ArrayList<>();
            callidusData.forEach((key, callidusPartnerCommisionsByPartnerCuid) -> {
                PartnerCommissionReportBean partnerCommissionReportBean = new PartnerCommissionReportBean();
                CommissionReportBean commissionReportBean = new CommissionReportBean();
                List<CommissionReportBean> commissionReportBeans = new ArrayList<>();

                partnerCommissionReportBean.setBaseCurrency(callidusPartnerCommisionsByPartnerCuid.stream().findFirst().get().getPartnerBaseCurrency());
                partnerCommissionReportBean.setPartnerName(callidusPartnerCommisionsByPartnerCuid.stream().findFirst().get().getPartnerName());
                partnerCommissionReportBean.setReportPeriod(dates.get("startDate") + "-" + dates.get("endDate"));



                Map<String, List<CallidusPartnerCommisions>> sellWithMonth = callidusPartnerCommisionsByPartnerCuid.stream().
                        filter(partnerCommission -> partnerCommission.getCommissionType().contains(PartnerCustomerConstants.CALLIDUS_SELL_WITH_COMISSION)).
                        collect(Collectors.groupingBy(this::getMonthNameForGroupBy, Collectors.toList()));
                commissionReportBean.setYearlyCommissionSellWith(doSortByMonth(sellWithMonth));
                Map<String, List<CallidusPartnerCommisions>> sellThruMonth = callidusPartnerCommisionsByPartnerCuid.stream().
                        filter(partnerCommission -> partnerCommission.getCommissionType().contains(PartnerCustomerConstants.CALLIDUS_SELL_THROUGH_COMISSION)).
                        collect(Collectors.groupingBy(this::getMonthNameForGroupBy, Collectors.toList()));
                commissionReportBean.setYearlyCommissionSellThru(doSortByMonth(sellThruMonth));
                Map<String, List<CallidusPartnerCommisions>> leadReferalMonth = callidusPartnerCommisionsByPartnerCuid.stream().
                        filter(partnerCommission -> partnerCommission.getCommissionType().contains(PartnerCustomerConstants.CALLIDUS_LEAD_REFERRAL_COMISSION)).
                        collect(Collectors.groupingBy(this::getMonthNameForGroupBy, Collectors.toList()));
                commissionReportBean.setYearlyCommissionLeadReferral(doSortByMonth(leadReferalMonth));
                commissionReportBean.setBusinessTotal("0.0");
                commissionReportBean.setCommissionsTotal("0.0");
                commissionReportBean.setIncentivesPaid("0.0");
                getAllTotalValueForYearlySummery(commissionReportBean, commissionReportBean.getYearlyCommissionSellWith().
                        get(commissionReportBean.getYearlyCommissionSellWith().size() - 1));
                getAllTotalValueForYearlySummery(commissionReportBean, commissionReportBean.getYearlyCommissionSellThru().
                        get(commissionReportBean.getYearlyCommissionSellThru().size() - 1));
                getAllTotalValueForYearlySummery(commissionReportBean, commissionReportBean.getYearlyCommissionLeadReferral().
                        get(commissionReportBean.getYearlyCommissionLeadReferral().size() - 1));
                commissionReportBeans.add(commissionReportBean);
                partnerCommissionReportBean.setCommissionReportBean(commissionReportBeans);
                partnerCommissionReportBeans.add(partnerCommissionReportBean);
            });
            finalCommissionReportBean.setPartnerCommissionReportBeans(partnerCommissionReportBeans);
        } catch (Exception ex) {
            LOGGER.error("Error while downloding downloadYearlySummaryReport() for Partner portal" + ex.getMessage());
        }

        return finalCommissionReportBean;
    }

    private static Map<String, String> getStartAndEndDateOfFinancialYear() {
        Map<String, String> dates = new HashMap();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int currentYear = month < 3 ? year - 1 : year;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-yyyy");
        dates.put("startDate", "01-Apr-" + String.valueOf(currentYear - 1));
        dates.put("endDate","31-Mar-" + String.valueOf(currentYear));
        /*dates.put("startDate", LocalDate.parse("01-Apr-" + String.valueOf(y), formatter));
        dates.put("endDate", LocalDate.parse("31-Mar-" + String.valueOf(y + 1), formatter));*/
        return dates;
    }

    private static void getAllTotalValueForYearlySummery(CommissionReportBean commissionReportBean, YearlyCommissionReportBean yearlyCommissionReportBean) {
        Double bussinessTotal = 0.0D;
        Double commissionTotal = 0.0D;
        Double incentivesPaid = 0.0D;

        if (!yearlyCommissionReportBean.getBusiness().equalsIgnoreCase("-")) {
            bussinessTotal = Double.parseDouble(yearlyCommissionReportBean.getBusiness());
            bussinessTotal = Double.parseDouble(commissionReportBean.getBusinessTotal()) + bussinessTotal;
            commissionReportBean.setBusinessTotal(BigDecimal.valueOf(bussinessTotal).toString());
        }
        if (!yearlyCommissionReportBean.getEligibleIncentives().equalsIgnoreCase("-")) {
            commissionTotal = Double.parseDouble(yearlyCommissionReportBean.getEligibleIncentives());
            commissionTotal = Double.parseDouble(commissionReportBean.getCommissionsTotal()) + commissionTotal;
            commissionReportBean.setCommissionsTotal(BigDecimal.valueOf(commissionTotal).toString());
        }
        if (!yearlyCommissionReportBean.getIncentivesProcesse().equalsIgnoreCase("-")) {
            incentivesPaid = Double.parseDouble(yearlyCommissionReportBean.getIncentivesProcesse());
            incentivesPaid = Double.parseDouble(commissionReportBean.getIncentivesPaid()) + incentivesPaid;
            commissionReportBean.setIncentivesPaid(BigDecimal.valueOf(incentivesPaid).toString());
        }
    }


    private Double calculateTotalBilledValue(List<CallidusPartnerCommisions> callidusPartnerCommisions) {
        Double billedValue = callidusPartnerCommisions.stream()
                .mapToDouble(currencyVal -> currencyVal.getBilledValueInBaseCurrency())
                .sum();
        return Utils.doRoundForTwoDecimal(billedValue);
    }

    private Integer calculateTotalCommissionPersentage(List<CallidusPartnerCommisions> callidusPartnerCommisions) {
        return callidusPartnerCommisions.stream().
                mapToInt(commissionPersentage -> Integer.parseInt(commissionPersentage.getCommissionedPercentage().
                        replace("%", ""))).sum();
    }

    private Double calculateTotalIncentiveAmount(List<CallidusPartnerCommisions> callidusPartnerCommisions) {
        Double incentiveAmount = callidusPartnerCommisions.stream()
                .mapToDouble(insentiveAmt -> insentiveAmt.getIncentiveValueInBaseCurrency())
                .sum();
        return Utils.doRoundForTwoDecimal(incentiveAmount);
    }

    private String getMonthNameForGroupBy(CallidusPartnerCommisions callidusPartnerCommisions) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-yy");
        LocalDate currentDate = LocalDate.parse(callidusPartnerCommisions.getCompDate().trim(), formatter);
        return currentDate.getMonth().name();

    }
    private List<YearlyCommissionReportBean> doSortByMonth(Map<String, List<CallidusPartnerCommisions>> commissionData) {

        List<YearlyCommissionReportBean> callidusPartnerCommisionsData = new ArrayList<>();
        Map<String, Double> ytdTotal = new HashMap<>();
        ytdTotal.put("ytdBusiness", 0.0D);
        ytdTotal.put("ytdEligibleIncentives", 0.0D);
        ytdTotal.put("ytdIncentivesProcesse", 0.0D);
        List<Double> incentivePross = new ArrayList<>();

        months.stream().forEach(month -> {
            YearlyCommissionReportBean yearlyCommissionReportBean = new YearlyCommissionReportBean();
            yearlyCommissionReportBean.setBusiness("-");
            yearlyCommissionReportBean.setEligibleIncentives("-");
            yearlyCommissionReportBean.setIncentivesProcesse("-");
            if (commissionData.containsKey(month)) {
                Double totalBilledValue = calculateTotalBilledValue(commissionData.get(month));
                Double eligibleIncentives = calculateTotalIncentiveAmount(commissionData.get(month));
                String commissionType = commissionData.get(month).stream().findFirst().get().getCommissionType();
                if (Objects.nonNull(totalBilledValue)) {
                    yearlyCommissionReportBean.setBusiness(BigDecimal.valueOf(totalBilledValue).toString());
                    ytdTotal.put("ytdBusiness", ytdTotal.get("ytdBusiness") + totalBilledValue);
                }
                if (Objects.nonNull(eligibleIncentives)) {
                    yearlyCommissionReportBean.setEligibleIncentives(String.valueOf(eligibleIncentives));
                    ytdTotal.put("ytdEligibleIncentives", ytdTotal.get("ytdEligibleIncentives") + eligibleIncentives);
                    incentivePross.add(eligibleIncentives);
                }
                /*TODO :set the value after quaterly and monthly commission paid rule*/
                if (commissionType.contains(PartnerCustomerConstants.CALLIDUS_LEAD_REFERRAL_COMISSION)) {
                    String incentiveMonthName = getIncentiveProcessMonthForLeadReffral(month);

                    if (incentiveMonthName.equalsIgnoreCase(month) && !incentivePross.isEmpty()) {
                        yearlyCommissionReportBean.setIncentivesProcesse(String.valueOf(Utils.doRoundForTwoDecimal(incentivePross.stream()
                                .mapToDouble(Double::doubleValue)
                                .sum())));
                        ytdTotal.put("ytdIncentivesProcesse", ytdTotal.get("ytdIncentivesProcesse") + eligibleIncentives);
                        incentivePross.clear();
                    } else {
                        yearlyCommissionReportBean.setIncentivesProcesse("-");
                    }
                }
                callidusPartnerCommisionsData.add(yearlyCommissionReportBean);
            } else {
                callidusPartnerCommisionsData.add(yearlyCommissionReportBean);
            }
        });
        callidusPartnerCommisionsData.add(getYTDTotalValues(ytdTotal));
        return callidusPartnerCommisionsData;
    }

    private static String getIncentiveProcessMonthForLeadReffral(String month) {
        String monthName = null;
        if (month.equalsIgnoreCase("APRIL") || month.equalsIgnoreCase("MAY") || month.equalsIgnoreCase("JUNE")) {
            monthName = "JUNE";
        } else if (month.equalsIgnoreCase("JULY") || month.equalsIgnoreCase("AUGUST") || month.equalsIgnoreCase("SEPTEMBER")) {
            monthName = "SEPTEMBER";
        } else if (month.equalsIgnoreCase("OCTOBER") || month.equalsIgnoreCase("NOVEMBER") || month.equalsIgnoreCase("DECEMBER")) {
            monthName = "DECEMBER";
        } else {
            monthName = "MARCH";
        }
        return monthName;
    }


    private static YearlyCommissionReportBean getYTDTotalValues(Map<String, Double> ytdTotal) {
        YearlyCommissionReportBean ytdCommissionReportBean = new YearlyCommissionReportBean();
        if (ytdTotal.get("ytdBusiness") == 0.0D) {
            ytdCommissionReportBean.setBusiness("-");
        } else {
            ytdCommissionReportBean.setBusiness(BigDecimal.valueOf(ytdTotal.get("ytdBusiness")).toString());
        }
        if (ytdTotal.get("ytdEligibleIncentives") == 0.0D) {
            ytdCommissionReportBean.setEligibleIncentives("-");
        } else {
            ytdCommissionReportBean.setEligibleIncentives(BigDecimal.valueOf(ytdTotal.get("ytdEligibleIncentives")).toString());
        }
        if (ytdTotal.get("ytdIncentivesProcesse") == 0.0D) {
            ytdCommissionReportBean.setIncentivesProcesse("-");
        } else {
            ytdCommissionReportBean.setIncentivesProcesse(BigDecimal.valueOf(ytdTotal.get("ytdIncentivesProcesse")).toString());
        }
        return ytdCommissionReportBean;
    }

    private static boolean checkBetween(LocalDate dateToCheck, LocalDate startDate, LocalDate endDate) {
        return dateToCheck.compareTo(startDate) >= 0 && dateToCheck.compareTo(endDate) <= 0;
    }

    /*private static Double doRoundForTwoDecimal(Double value) {
        if (Objects.nonNull(value)) {
            value = Double.valueOf(df.format(value));
        }
        return value;
    }*/
}
