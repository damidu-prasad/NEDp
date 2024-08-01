package com.webapps.controller.managebeans;

import com.ejb.model.common.ComDev;
import com.ejb.model.common.ComLib;
import com.ejb.model.common.ComPath;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.FingerPrintRegionUser;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.School;
import com.ejb.model.entity.TeacherMonthlyScores;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author USER
 */
@ManagedBean(name = "publicTeacherAttendanceReport")
@ViewScoped
public class publicTeacherAttendanceReport {

    private Date currentDate;
    private String currentDateTeacherAttendance;
    private String loginUserSchool;

    private int totalRegisteredTeachers;
    private int presentTeachersCount;
    private int absentTeachersCount;
    private int attendPercentageCount;

    private int attendBeforeSeventTwenty;
    private int attendBeforePercentage;

    List<TeacherMonthlyScores> teachersTopFiveAttendanceMonthlyScores;
    List<TeacherMonthlyScores> teachersTopFiveDedicationMonthlyScores;

    private double totalRegisteredTeachersPercentage;
    private double presenteachersCountPercentage;
    private double absentTeachersCountPercentage;

    private BarChartModel barModel;

    HttpServletResponse response;
    HttpServletRequest request;
    private ComLib comlib;
    private ComPath comPath;
    private boolean loggedIn = false;

    @EJB
    private UniDBLocal uni;
    
    LoginSession ls;

    @EJB
    private ComDev comDiv;

    @Resource(lookup = "java:app/ds_education_db")
    private javax.sql.DataSource dataSource;

    @PostConstruct
    public void init() {
//
//        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//
//        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//
//        ls = (LoginSession) uni.find(Integer.parseInt(request.getSession().getAttribute("LS").toString()), LoginSession.class);

        Date date = new Date();

        currentDate = date;

        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        currentDateTeacherAttendance = formattedDate;

        initializeData();

        teacherAttendance();

        getTeacherScores();

    }

    public void initializeData() {
//        int ls_id = Integer.parseInt(request.getSession().getAttribute("LS").toString());
//        LoginSession lss = (LoginSession) uni.find(ls_id, LoginSession.class);
//        int gopi = lss.getUserLoginGroupId().getGeneralOrganizationProfileId().getId();
//        String query = "SELECT g from School g where g.generalOrganizationProfileId.id = '" + gopi + "'";
//        List<School> schoolList = uni.searchByQuery(query);
//        for (School school : schoolList) {
//            setLoginUserSchoolId(school.getGeneralOrganizationProfileId().getName());
//        }

        createBarModel();
    }

    private void teacherAttendance() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String smpDate = dateFormat.format(currentDate);

        String query_getFingerPrintRegionUsers = "SELECT g FROM FingerPrintRegionUser g WHERE g.isActive='1'";

        List<FingerPrintRegionUser> fingerPrintRegionUserList = uni.searchByQuery(query_getFingerPrintRegionUsers);

        if (fingerPrintRegionUserList.size() > 0) {

            setTotalRegisteredTeachers(fingerPrintRegionUserList.size());
            
            String query_getTeacherAttendance = "SELECT g FROM TeacherAttendance g WHERE g.date=' " + smpDate + " '";
            List<com.ejb.model.entity.TeacherAttendance> teacherAttendanceList = uni.searchByQuery(query_getTeacherAttendance);

            setPresentTeachersCount(teacherAttendanceList.size());

            setAbsentTeachersCount(fingerPrintRegionUserList.size() - teacherAttendanceList.size());

            setAttendPercentageCount((teacherAttendanceList.size() / fingerPrintRegionUserList.size()) * 100);

            String query_getTeacherAttendanceBeforeSevenTwenty = "SELECT g FROM TeacherAttendance g WHERE g.date=' " + smpDate + " ' AND  g.in_time < '07:20:00'";
            List<com.ejb.model.entity.TeacherAttendance> teacherAttendanceListBeforeSevenTwenty = uni.searchByQuery(query_getTeacherAttendanceBeforeSevenTwenty);

            if (teacherAttendanceListBeforeSevenTwenty.size() > 0) {

                setAttendBeforeSeventTwenty(teacherAttendanceListBeforeSevenTwenty.size());

                setAttendBeforePercentage((teacherAttendanceListBeforeSevenTwenty.size() / teacherAttendanceList.size()) * 100);

            } else {

                setAttendBeforeSeventTwenty(0);

            }

        } else {

            setTotalRegisteredTeachers(0);
            setTotalRegisteredTeachersPercentage((fingerPrintRegionUserList.size() / 353) * 100);
        }
    }

    private void getTeacherScores() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String smpDate = dateFormat.format(currentDate);

        int year = Integer.parseInt(smpDate.substring(0, 4));

        int month = Integer.parseInt(smpDate.substring(5, 7));

        String query_getTopFiveMonthlyAttendance = "SELECT g FROM TeacherMonthlyScores g WHERE EXTRACT(YEAR FROM g.date)='" + year + "' AND  EXTRACT(MONTH FROM g.date)='" + month + "' ORDER BY g.monthlyAttendanceScore DESC";
        List<TeacherMonthlyScores> topFiveMonthlyAttendance = uni.searchByQuery(query_getTopFiveMonthlyAttendance);

        if (topFiveMonthlyAttendance.size() > 0) {

            setTeachersTopFiveAttendanceMonthlyScores(topFiveMonthlyAttendance);

        } else {

            setTeachersTopFiveAttendanceMonthlyScores(null);

        }

        String query_getTopFiveMonthlyDedication = "SELECT g FROM TeacherMonthlyScores g WHERE EXTRACT(YEAR FROM g.date)='" + year + "' AND  EXTRACT(MONTH FROM g.date)='" + month + "' ORDER BY g.monthlyDedicationScore DESC";
        List<TeacherMonthlyScores> topFiveMonthlyDedication = uni.searchByQuery(query_getTopFiveMonthlyDedication);

        if (topFiveMonthlyDedication.size() > 0) {

            setTeachersTopFiveDedicationMonthlyScores(topFiveMonthlyDedication);

        } else {

            setTeachersTopFiveAttendanceMonthlyScores(null);

        }

    }

    private void createBarModel() {

        try {

            System.out.println("currentDate " + currentDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);

            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date firstDateOfMonth = calendar.getTime();

            BarChartModel model;
            ChartSeries attend;
            ChartSeries absent;

            try (Connection connection = dataSource.getConnection()) {

                CallableStatement cs = connection.prepareCall("{call CalculateAttendancePercentage(?, ?)}");
                cs.setDate(1, new java.sql.Date(firstDateOfMonth.getTime()));
                cs.setDate(2, new java.sql.Date(currentDate.getTime()));

                cs.execute();

                ResultSet rs = cs.getResultSet();

                model = new BarChartModel();

                attend = new ChartSeries();
                attend.setLabel("Attendance");

                absent = new ChartSeries();
                absent.setLabel("Abesnt");

                while (rs.next()) {

                    Date rsCurrentDate = rs.getDate("currentDate");

                    calendar.setTime(rsCurrentDate);

                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    double attendancePercentage = rs.getDouble("attendance_percentage");
                    double absentPercentage = rs.getDouble("absent_percentage");

                    attend.set(day, attendancePercentage);
                    absent.set(day, absentPercentage);
                   
                    System.out.println("Date: " + day + ", Attendance Percentage: " + attendancePercentage + ", Absent Percentage: " + absentPercentage);
                }

                rs.close();
                cs.close();
            }

            model.addSeries(attend);
            model.addSeries(absent);

            model.setTitle("Attendance Report of " + calendar.get(Calendar.YEAR) + " " + new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)]);
            model.setLegendPosition("ne");

            Axis xAxis = model.getAxis(AxisType.X);
            xAxis.setLabel("Date");

            Axis yAxis = model.getAxis(AxisType.Y);
            yAxis.setLabel("Attendance Percentage");
            yAxis.setMin(0);
            yAxis.setMax(100);
            yAxis.setTickInterval("10");

            setBarModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public double getTotalRegisteredTeachersPercentage() {
        return totalRegisteredTeachersPercentage;
    }

    public void setTotalRegisteredTeachersPercentage(double totalRegisteredTeachersPercentage) {
        this.totalRegisteredTeachersPercentage = totalRegisteredTeachersPercentage;
    }

    public double getPresenteachersCountPercentage() {
        return presenteachersCountPercentage;
    }

    public void setPresenteachersCountPercentage(double presenteachersCountPercentage) {
        this.presenteachersCountPercentage = presenteachersCountPercentage;
    }

    public double getAbsentTeachersCountPercentage() {
        return absentTeachersCountPercentage;
    }

    public void setAbsentTeachersCountPercentage(double absentTeachersCountPercentage) {
        this.absentTeachersCountPercentage = absentTeachersCountPercentage;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentDateTeacherAttendance() {
        return currentDateTeacherAttendance;
    }

    public void setCurrentDateTeacherAttendance(String currentDateTeacherAttendance) {
        this.currentDateTeacherAttendance = currentDateTeacherAttendance;
    }

    public String getLoginUserSchoolId() {
        return loginUserSchool;
    }

    public void setLoginUserSchoolId(String loginUserSchool) {
        this.loginUserSchool = loginUserSchool;
    }

    public int getTotalRegisteredTeachers() {
        return totalRegisteredTeachers;
    }

    public void setTotalRegisteredTeachers(int totalRegisteredTeachers) {
        this.totalRegisteredTeachers = totalRegisteredTeachers;
    }

    public int getPresentTeachersCount() {
        return presentTeachersCount;
    }

    public void setPresentTeachersCount(int presentTeachersCount) {
        this.presentTeachersCount = presentTeachersCount;
    }

    public int getAbsentTeachersCount() {
        return absentTeachersCount;
    }

    public void setAbsentTeachersCount(int absentTeachersCount) {
        this.absentTeachersCount = absentTeachersCount;
    }

    public int getAttendPercentageCount() {
        return attendPercentageCount;
    }

    public void setAttendPercentageCount(int attendPercentageCount) {
        this.attendPercentageCount = attendPercentageCount;
    }

    public int getAttendBeforeSeventTwenty() {
        return attendBeforeSeventTwenty;
    }

    public void setAttendBeforeSeventTwenty(int attendBeforeSeventTwenty) {
        this.attendBeforeSeventTwenty = attendBeforeSeventTwenty;
    }

    public int getAttendBeforePercentage() {
        return attendBeforePercentage;
    }

    public void setAttendBeforePercentage(int attendBeforePercentage) {
        this.attendBeforePercentage = attendBeforePercentage;
    }

    public List<TeacherMonthlyScores> getTeachersTopFiveAttendanceMonthlyScores() {
        return teachersTopFiveAttendanceMonthlyScores;
    }

    public void setTeachersTopFiveAttendanceMonthlyScores(List<TeacherMonthlyScores> teachersTopFiveAttendanceMonthlyScores) {
        this.teachersTopFiveAttendanceMonthlyScores = teachersTopFiveAttendanceMonthlyScores;
    }

    public List<TeacherMonthlyScores> getTeachersTopFiveDedicationMonthlyScores() {
        return teachersTopFiveDedicationMonthlyScores;
    }

    public void setTeachersTopFiveDedicationMonthlyScores(List<TeacherMonthlyScores> teachersTopFiveDedicationMonthlyScores) {
        this.teachersTopFiveDedicationMonthlyScores = teachersTopFiveDedicationMonthlyScores;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

}
