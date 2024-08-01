/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.model.common;

import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.SystemInterface;
import com.ejb.model.entity.UserRoleHasSystemInterface;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sameera
 */
@Stateless
@LocalBean
public class ComLib {

    public static String SCHOOL_USER_ROLE_LIST = "3,4,8";

    public static Date getDateObject(String date) {
        Date FormattedDate = null;
        try {

            SimpleDateFormat parser = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
            FormattedDate = parser.parse(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormattedDate;
    }

    public static String getDate(Date date) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        return formatter.format(date);

    }

    public static String getDouble(double value) {

        DecimalFormat myFormatter = new DecimalFormat("###,###,###.##");
        String output = myFormatter.format(value);
        return output;

    }

    public static String getInt(double value) {

        DecimalFormat myFormatter = new DecimalFormat("###,###,###");
        String output = myFormatter.format(value);
        return output;

    }

    public static int GetCurrentYear() {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(new Date());

        return Integer.valueOf(cal1.getTime().toString().split(" ")[5]);
    }

    public static int GetCurrentFinanceYear() {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(new Date());

        int curYear = Integer.valueOf(cal1.getTime().toString().split(" ")[5]);

        int curmonth = GetCurrentMonth();

        if (curmonth >= 1 && curmonth < 4) {
            curYear = curYear - 1;
        }

        return curYear;
    }

    public static int GetCurrentMonth() {
        SimpleDateFormat mm = new SimpleDateFormat("MM");
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(new Date());

        return Integer.valueOf(mm.format(cal1.getTime()));
    }

    public static String getFullDate(Date time) {

        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.US);

        return formatter.format(time);
    }

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH)
                || (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    public static long getDayCount(String start, String end) {
        long diff = -1;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateStart = simpleDateFormat.parse(start);
            Date dateEnd = simpleDateFormat.parse(end);

            //time is always 00:00:00 so rounding should help to ignore the missing hour when going from winter to summer time as well as the extra hour in the other direction
            diff = Math.round((dateEnd.getTime() - dateStart.getTime()) / (double) 86400000);
        } catch (Exception e) {
            //handle the exception according to your own situation
        }
        return diff;
    }

    public static double getRounded(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

//    public static String VIDGenerator(UniDBLocal uniDB, int gop_id, int vt_id) {
//        System.out.println("gg " + gop_id + " " + vt_id);
//
//        VoucherType vt = (VoucherType) uniDB.find(vt_id, VoucherType.class);
//        GeneralOrganizationProfile gop = (GeneralOrganizationProfile) uniDB.find(gop_id, GeneralOrganizationProfile.class);
//
//        int id = 0;
//        String abrivation = "";
//        String generated_id = "";
//
//        abrivation = vt.getIdAbbreviation();
//
//        String voucher_id = "";
//        String v = null;
//        System.out.println("awa1");
//        try {
//            List<Voucher> VoucherList = uniDB.searchByQuerySingle("SELECT v FROM Voucher v WHERE v.voucherTypeId.id='" + vt.getId() + "' AND v.generalOrganizationProfileId.id='" + gop.getId() + "' ORDER BY v.voucherId DESC");
//            if (VoucherList.size() > 0) {
//                v = VoucherList.iterator().next().getVoucherId();
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("awa2");
//        if (v == null) {
//            id = 0;
//        } else {
//            String vid = v;
//
//            String number = "";
//
//            for (int x = 0; x < vid.length(); x++) {
//                if (Character.isDigit(vid.charAt(x))) {
//                    number += vid.charAt(x);
//                }
//            }
//
//            id = Integer.parseInt(number);
//        }
//
//        id++;
//
//        String idlen = "" + id;
//
//        for (int x = idlen.length(); x < 6; x++) {
//            generated_id += "0";
//        }
//
//        generated_id += "" + id;
//
//        voucher_id = gop.getCode() + abrivation + generated_id;
//        System.out.println("voucher id " + voucher_id);
//        return voucher_id;
//    }
    public static void setHeaders(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache"); //Forces caches to obtain a new copy of the page from the origin server
        response.setHeader("Cache-Control", "no-store"); //Directs caches not to store the page under any circumstance
        response.setDateHeader("Expires", 0); //Causes the proxy cache to see the page as "stale"
        response.setHeader("Pragma", "no-cache"); //HTTP 1.0 backward compatibility
    }

    public static Date getLastDaYOfMonthFromMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);

        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE)); // changed calendar to cal

        Date lastDateOfMonth = cal.getTime();

        return lastDateOfMonth;
    }

    public static Date getFirstDaYOfMonthFromMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);

        Date lastDateOfMonth = cal.getTime();

        return lastDateOfMonth;
    }

    public static Date getFirstDaYOfMonthFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, 1);
        Date firstDateOfMonth = cal.getTime();
        return firstDateOfMonth;
    }

    public static Date getYesterdayFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        //  System.out.println("Yesterday's date = " + cal.getTime());
        return cal.getTime();
    }

    public static Date getLastDaYOfMonthFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, 1);

        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE)); // changed calendar to cal

        Date lastDateOfMonth = cal.getTime();

        return lastDateOfMonth;
    }

    public static boolean CheckUserPrivilage(UniDBLocal uniDB, int userRole, String systemInterfaceName) {
        boolean check = false;

        List<SystemInterface> si = uniDB.searchByQuerySingle("SELECT im FROM SystemInterface im WHERE im.interfaceName='" + systemInterfaceName + "' ");
        if (si.size() > 0) {

            List<UserRoleHasSystemInterface> gop_list = uniDB.searchByQuerySingle("SELECT im FROM UserRoleHasSystemInterface im WHERE im.systemInterfaceId.id='" + si.iterator().next().getId() + "' and im.userRoleId.id='" + userRole + "' ");
            if (gop_list.size() > 0) {
                check = true;
            }
        }
        return check;

    }

    public static String txtToHtml(String s) {
        StringBuilder builder = new StringBuilder();
        boolean previousWasASpace = false;
        for (char c : s.toCharArray()) {
            if (c == ' ') {
                if (previousWasASpace) {
                    builder.append("&nbsp;");
                    previousWasASpace = false;
                    continue;
                }
                previousWasASpace = true;
            } else {
                previousWasASpace = false;
            }
            switch (c) {
//                case '<':
//                    builder.append("&lt;");
//                    break;
//                case '>':
//                    builder.append("&gt;");
//                    break;
//                case '&':
//                    builder.append("&amp;");
//                    break;
//                case '"':
//                    builder.append("&quot;");
//                    break;
                case '\n':
                    builder.append("<br/>");
                    break;
                // We need Tab support here, because we print StackTraces as HTML

                default:
                    builder.append(c);

            }
        }

        return builder.toString();

    }

    public static boolean checkSchoolDisable(LoginSession lss) {

        
        
        boolean disables = false;
        if (lss.getUserLoginGroupId().getUserRoleId().getId() == 1 || lss.getUserLoginGroupId().getUserRoleId().getId() == 2 || lss.getUserLoginGroupId().getUserRoleId().getId() == 7) {

            disables = false;

        } else if (lss.getUserLoginGroupId().getUserRoleId().getId() == 3 || lss.getUserLoginGroupId().getUserRoleId().getId() == 4 || lss.getUserLoginGroupId().getUserRoleId().getId() == 8) {
            disables = true;
        }
        return disables;
    }

    public static double GetMarkByValue(String value) {
        double mark = 0;
        if (value.equalsIgnoreCase("A")) {
            mark = 87;

        } else if (value.equalsIgnoreCase("B")) {
            mark = 70;

        } else if (value.equalsIgnoreCase("C")) {
            mark = 60;

        } else if (value.equalsIgnoreCase("S")) {
            mark = 47;

        } else if (value.equalsIgnoreCase("W")) {
            mark = 20;

        }
        return mark;
    }

    public static String GetValueByMark(double mark) {
        String value = "";

        if (mark >= 75) {
            value = "A";

        } else if (mark >= 65 && mark < 75) {
            value = "B";

        } else if (mark >= 55 && mark < 65) {
            value = "C";

        } else if (mark >= 40 && mark < 55) {
            value = "S";

        } else if (mark < 40) {
            value = "W";

        }

        return value;

    }

}
