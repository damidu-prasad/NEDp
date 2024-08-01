/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.managebeans;

import com.ejb.model.common.ComDev;
import com.ejb.model.common.ComLib;
import com.ejb.model.common.ComPath;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.Days;
import com.ejb.model.entity.DaysPeriod;
import com.ejb.model.entity.FingerPrintRegionUser;
import com.ejb.model.entity.GeneralUserProfile;
import com.ejb.model.entity.Grade;
import com.ejb.model.entity.GradeClassHasSubjects;
import com.ejb.model.entity.GradeClassStream;
import com.ejb.model.entity.GradeClassStreamManager;
import com.ejb.model.entity.GradeClassSubjectTeacher;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.Periods;
import com.ejb.model.entity.School;
import com.ejb.model.entity.Subjects;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.TimeTable;
import com.ejb.model.entity.Year;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.persistence.descriptors.SelectedFieldsLockingPolicy;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author USER
 */
@ManagedBean(name = "teacherAttendanceList")
@ViewScoped
public class TeacherAttendanceList {

    private String searchByName = "";

    private List<TeacherAttendance> attendanceList = new ArrayList();

    private String attendaceType = "1";

    private String attendanceTypeId;

    private Date currentDate;

    private Date currentDateTeacherAttendance;

    private String yearName = "0";
    private List<SelectItem> yearList = new ArrayList<>();
    private String yearValue = "";

    private String selectedGrade = "0";
    private List<SelectItem> gradeList = new ArrayList<>();

    private String selectedClass = "";

    private List<SelectItem> classNameList = new ArrayList<SelectItem>();

    private String classId = "";

    private String gradeSubject = "0";
    private List<SelectItem> gradeSubjectNameList = new ArrayList<>();
    private String gradeSubjectId = "";

    private String subjectTeacher = "0";
    private List<SelectItem> subjectTeacherNameList = new ArrayList<>();
    private String subjectTeacherId = "";

    private List<TimeSlot> timeTable = new ArrayList<>();

    private List<TimeTableSetUp> timeTableSetupList = new ArrayList<>();

    private List<PeriodData> selectedDataCellIds = new ArrayList<>();
    private List<PeriodData> selectedDataCellsToUpdate = new ArrayList<>();
    private String gcmId;

    private List<TimeTableHeader> timeTableHeaders = new ArrayList<>();

    private String loadViewsNo = "1";

    private int loginUserSchoolId;

    HttpServletResponse response;
    HttpServletRequest request;
    private ComLib comlib;
    private ComPath comPath;

    private BarChartModel barModel;

    @EJB
    private UniDBLocal uni;
    LoginSession ls;

    @EJB
    private ComDev comDiv;

    @Resource(lookup = "java:app/ds_education_db")
    private javax.sql.DataSource dataSource;

    @PostConstruct
    public void init() {

        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        ls = (LoginSession) uni.find(Integer.parseInt(request.getSession().getAttribute("LS").toString()), LoginSession.class);

        Date date = new Date();

        currentDate = date;

        currentDateTeacherAttendance = date;

        initializeData();

    }

    public void initializeData() {
        int ls_id = Integer.parseInt(request.getSession().getAttribute("LS").toString());
        LoginSession lss = (LoginSession) uni.find(ls_id, LoginSession.class);
        int gopi = lss.getUserLoginGroupId().getGeneralOrganizationProfileId().getId();
        String query = "SELECT g from School g where g.generalOrganizationProfileId.id = '" + gopi + "'";
        List<School> schoolList = uni.searchByQuery(query);
        for (School school : schoolList) {
            setLoginUserSchoolId(school.getId());
        }

        loadYearList();
        loadTImeTableDates();
        attendanceList.clear();
        createBarModel();
    }

    public void loadTImeTableDates() {

        String query_timeTableDates = "SELECT g FROM Days g";
        List<Days> days_list = uni.searchByQuery(query_timeTableDates);

        Days monday = null;
        Days tuesday = null;
        Days wednesday = null;
        Days thursday = null;
        Days friday = null;

        if (days_list.size() > 0) {
            for (Days days : days_list) {
                switch (days.getId()) {
                    case 1:
                        monday = days;
                        break;
                    case 2:
                        tuesday = days;
                        break;
                    case 3:
                        wednesday = days;
                        break;
                    case 4:
                        thursday = days;
                    case 5:
                        friday = days;
                        break;
                    default:
                        break;
                }
            }
            timeTableHeaders.add(new TimeTableHeader(monday, tuesday, wednesday, thursday, friday));
        }

    }

    public void searchTeachersByName() {

        System.out.println("searchByName " + searchByName);

        attendanceList.clear();

        System.out.println("A");

        String query_teachers = "SELECT gup FROM GeneralUserProfile gup INNER JOIN Teacher t ON gup.id = t.generalUserProfileId.id WHERE gup.nameWithIn LIKE '%" + searchByName + "%' AND t.schoolId.id = '" + loginUserSchoolId + "' AND t.isActive='1'";

        System.out.println("B");

        List<GeneralUserProfile> gup_list = uni.searchByQuery(query_teachers);

        System.out.println("gup list " + gup_list.size());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String smpDate = dateFormat.format(currentDate);

        System.out.println("String smpDate" + smpDate);

        String query_getTeacherAttendance = "SELECT g FROM TeacherAttendance g WHERE g.date=' " + smpDate + " '";

        List<com.ejb.model.entity.TeacherAttendance> teacherAttendanceList = uni.searchByQuery(query_getTeacherAttendance);

        System.out.println("teacherAttendanceList " + teacherAttendanceList.size());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm aa");

        int i = 0;

        if (gup_list != null) {

            System.out.println("gup_list != null");

            for (GeneralUserProfile gup : gup_list) {

                for (com.ejb.model.entity.TeacherAttendance teacherAttendance : teacherAttendanceList) {

                    if (gup.getId() == teacherAttendance.getTeacherId().getGeneralUserProfileId().getId()) {

                        System.out.println(gup.getNameWithIn());

                        attendanceList.add(new TeacherAttendance(String.valueOf(i), teacherAttendance.getTeacherId().getGeneralUserProfileId().getNic(), teacherAttendance.getTeacherId().getGeneralUserProfileId().getNameWithIn(), timeFormat.format(teacherAttendance.getIn_time()), timeFormat.format(teacherAttendance.getOut_time()), teacherAttendance.getIsAttended(), "N/A"));

                        break;

                    }

                }

            }

        } else {
            System.out.println("General User Profile is empty");
        }

    }

    public void loadTeacherAttendance() {

        setAttendanceTypeId("0");

        attendanceList.clear();

        setAttendanceTypeId(attendaceType);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String smpDate = dateFormat.format(currentDate);

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm aa");

        SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy");
        String smpDate3 = dateFormat3.format(currentDate);

        String query_year = "SELECT g FROM Year g WHERE g.name='" + smpDate3 + "' ";

        List<Year> year = uni.searchByQuery(query_year);

        if (year.size() > 0) {

            String query_getFingerPrintRegionUsers = "SELECT g FROM FingerPrintRegionUser g WHERE g.isActive='1'";

            List<FingerPrintRegionUser> fingerPrintRegionUserList = uni.searchByQuery(query_getFingerPrintRegionUsers);

            if (fingerPrintRegionUserList.size() > 0) {

                switch (attendaceType) {
                    case "1": {
                        // All

                        String query_getTeacherAttendance = "SELECT g FROM TeacherAttendance g WHERE g.date=' " + smpDate + " '";
                        List<com.ejb.model.entity.TeacherAttendance> teacherAttendanceList = uni.searchByQuery(query_getTeacherAttendance);
                        int i = 0;
                        for (FingerPrintRegionUser fingerPrintRegionUser : fingerPrintRegionUserList) {

                            System.out.println("fingerPrintRegionUser" + fingerPrintRegionUser.getGeneralUserProfileGupId().getId());

                            String query_teacherList = "SELECT g FROM Teacher g WHERE g.generalUserProfileId.id= " + fingerPrintRegionUser.getGeneralUserProfileGupId().getId();

                            List<Teacher> teacherList = uni.searchByQuery(query_teacherList);

                            List<Grade> gradeList = new ArrayList<>();

                            for (Teacher teacher : teacherList) {

                                String query_gradeClassStreamManagerList = "SELECT g FROM GradeClassStreamManager g WHERE g.teacherId.id='" + teacher.getId() + "' AND g.yearId.id='" + year.get(0).getId() + "' ";

                                List<GradeClassStreamManager> gradeClassStreamManagerList = uni.searchByQuery(query_gradeClassStreamManagerList);

                                if (gradeClassStreamManagerList != null) {

                                    for (GradeClassStreamManager manager : gradeClassStreamManagerList) {

                                        if (!gradeList.contains(manager.getGradeClassStreamId().getGradeId())) {

                                            gradeList.add(manager.getGradeClassStreamId().getGradeId());

                                        }

                                    }

                                }

                                break;
                            }

                            String gradeString = "N/A";

                            if (!gradeList.isEmpty()) {

                                gradeString = "";

                                for (Grade grade : gradeList) {

                                    gradeString = gradeString + grade.getName() + " , ";

                                }

                            }

                            System.out.println("gradeString " + gradeString);

                            i++;

                            TeacherAttendance dailyTeacherAttendanceObj = new TeacherAttendance(String.valueOf(i), "", "N/A", "N/A", "N/A", false, "N/A");

                            dailyTeacherAttendanceObj.setTeacher_id(fingerPrintRegionUser.getGeneralUserProfileGupId().getNic());

                            dailyTeacherAttendanceObj.setTeacher_name(fingerPrintRegionUser.getGeneralUserProfileGupId().getNameWithIn());

                            if (!gradeString.equalsIgnoreCase("N/A")) {
                                dailyTeacherAttendanceObj.setGrades(gradeString);
                            }

                            for (com.ejb.model.entity.TeacherAttendance teacherAttendance : teacherAttendanceList) {

                                if (fingerPrintRegionUser.getGeneralUserProfileGupId().equals(teacherAttendance.getTeacherId().getGeneralUserProfileId())) {

                                    System.out.println("fingerPrintRegionUser.getGeneralUserProfileGupId() == teacherAttendance.getTeacherId().getGeneralUserProfileId()");

                                    System.out.println("teacherAttendance.getIn_time() " + teacherAttendance.getIn_time());

                                    String intime = dateFormat2.format(teacherAttendance.getIn_time());

                                    String outTime = null;

                                    if (teacherAttendance.getOut_time() != null) {

                                        outTime = dateFormat2.format(teacherAttendance.getOut_time());

                                    }

                                    dailyTeacherAttendanceObj.setIn_time(intime);

                                    if (outTime != null) {

                                        dailyTeacherAttendanceObj.setOut_time(outTime);

                                    }

                                    dailyTeacherAttendanceObj.setAttendance(true);

                                    break;

                                }

                            }

                            attendanceList.add(dailyTeacherAttendanceObj);

                        }
                        System.out.println("AttendanceList size" + attendanceList.size());
                        break;
                    }
                    case "2": {
                        // Attend

                        String query_getTeacherAttendance = "SELECT g FROM TeacherAttendance g WHERE g.date=' " + smpDate + " '";
                        List<com.ejb.model.entity.TeacherAttendance> teacherAttendanceList = uni.searchByQuery(query_getTeacherAttendance);
                        int i = 0;
                        for (FingerPrintRegionUser fingerPrintRegionUser : fingerPrintRegionUserList) {

                            TeacherAttendance dailyTeacherAttendanceObj = null;

                            for (com.ejb.model.entity.TeacherAttendance teacherAttendance : teacherAttendanceList) {

                                if (fingerPrintRegionUser.getGeneralUserProfileGupId().equals(teacherAttendance.getTeacherId().getGeneralUserProfileId()) && teacherAttendance.getIn_time() != null) {

                                    System.out.println("fingerPrintRegionUser" + fingerPrintRegionUser.getGeneralUserProfileGupId().getId());

                                    String query_teacherList = "SELECT g FROM Teacher g WHERE g.generalUserProfileId.id= " + fingerPrintRegionUser.getGeneralUserProfileGupId().getId();

                                    List<Teacher> teacherList = uni.searchByQuery(query_teacherList);

                                    List<Grade> gradeList = new ArrayList<>();

                                    for (Teacher teacher : teacherList) {

                                        String query_gradeClassStreamManagerList = "SELECT g FROM GradeClassStreamManager g WHERE g.teacherId.id='" + teacher.getId() + "' AND g.yearId.id='" + year.get(0).getId() + "' ";

                                        List<GradeClassStreamManager> gradeClassStreamManagerList = uni.searchByQuery(query_gradeClassStreamManagerList);

                                        if (gradeClassStreamManagerList != null) {

                                            for (GradeClassStreamManager manager : gradeClassStreamManagerList) {

                                                if (!gradeList.contains(manager.getGradeClassStreamId().getGradeId())) {

                                                    gradeList.add(manager.getGradeClassStreamId().getGradeId());

                                                }

                                            }

                                        }

                                        break;
                                    }

                                    String gradeString = "N/A";

                                    if (!gradeList.isEmpty()) {

                                        gradeString = "";

                                        for (Grade grade : gradeList) {

                                            gradeString = gradeString + grade.getName() + " , ";

                                        }

                                    }

                                    System.out.println("gradeString " + gradeString);

                                    i++;

                                    dailyTeacherAttendanceObj = new TeacherAttendance(String.valueOf(i), "", "N/A", "N/A", "N/A", false, "N/A");

                                    dailyTeacherAttendanceObj.setTeacher_id(fingerPrintRegionUser.getGeneralUserProfileGupId().getNic());

                                    dailyTeacherAttendanceObj.setTeacher_name(fingerPrintRegionUser.getGeneralUserProfileGupId().getNameWithIn());

                                    if (!gradeString.equalsIgnoreCase("N/A")) {
                                        dailyTeacherAttendanceObj.setGrades(gradeString);
                                    }

                                    String intime = dateFormat2.format(teacherAttendance.getIn_time());

                                    String outTime = null;

                                    if (teacherAttendance.getOut_time() != null) {

                                        outTime = dateFormat2.format(teacherAttendance.getOut_time());

                                    }

                                    dailyTeacherAttendanceObj.setIn_time(intime);

                                    if (outTime != null) {

                                        dailyTeacherAttendanceObj.setOut_time(outTime);

                                    }

                                    dailyTeacherAttendanceObj.setAttendance(true);

                                    break;

                                }

                            }

                            if (dailyTeacherAttendanceObj != null) {

                                attendanceList.add(dailyTeacherAttendanceObj);

                            }

                        }
                        break;
                    }
                    case "3": {
                        // Absent

                        String query_getTeacherAttendance = "SELECT g FROM TeacherAttendance g WHERE g.date=' " + smpDate + " '";
                        List<com.ejb.model.entity.TeacherAttendance> teacherAttendanceList = uni.searchByQuery(query_getTeacherAttendance);

                        int i = 0;

                        List<GeneralUserProfile> generalUserProfilesList = new ArrayList<>();

                        for (com.ejb.model.entity.TeacherAttendance obj : teacherAttendanceList) {

                            generalUserProfilesList.add(obj.getTeacherId().getGeneralUserProfileId());

                        }

                        for (FingerPrintRegionUser fingerPrintRegionUser : fingerPrintRegionUserList) {

                            if (!generalUserProfilesList.contains(fingerPrintRegionUser.getGeneralUserProfileGupId())) {

                                System.out.println("fingerPrintRegionUser" + fingerPrintRegionUser.getGeneralUserProfileGupId().getId());

                                String query_teacherList = "SELECT g FROM Teacher g WHERE g.generalUserProfileId.id= " + fingerPrintRegionUser.getGeneralUserProfileGupId().getId();

                                List<Teacher> teacherList = uni.searchByQuery(query_teacherList);

                                List<Grade> gradeList = new ArrayList<>();

                                for (Teacher teacher : teacherList) {

                                    String query_gradeClassStreamManagerList = "SELECT g FROM GradeClassStreamManager g WHERE g.teacherId.id='" + teacher.getId() + "' AND g.yearId.id='" + year.get(0).getId() + "' ";

                                    List<GradeClassStreamManager> gradeClassStreamManagerList = uni.searchByQuery(query_gradeClassStreamManagerList);

                                    if (gradeClassStreamManagerList != null) {

                                        for (GradeClassStreamManager manager : gradeClassStreamManagerList) {

                                            if (!gradeList.contains(manager.getGradeClassStreamId().getGradeId())) {

                                                gradeList.add(manager.getGradeClassStreamId().getGradeId());

                                            }

                                        }

                                    }

                                    break;
                                }

                                String gradeString = "N/A";

                                if (!gradeList.isEmpty()) {

                                    gradeString = "";

                                    for (Grade grade : gradeList) {

                                        gradeString = gradeString + grade.getName() + " , ";

                                    }

                                }

                                System.out.println("gradeString " + gradeString);

                                i++;

                                TeacherAttendance dailyTeacherAttendanceObj = new TeacherAttendance(String.valueOf(i), "", "N/A", "N/A", "N/A", false, "N/A");

                                dailyTeacherAttendanceObj.setTeacher_id(fingerPrintRegionUser.getGeneralUserProfileGupId().getNic());

                                dailyTeacherAttendanceObj.setTeacher_name(fingerPrintRegionUser.getGeneralUserProfileGupId().getNameWithIn());

                                if (!gradeString.equalsIgnoreCase("N/A")) {

                                    dailyTeacherAttendanceObj.setGrades(gradeString);

                                }
                                attendanceList.add(dailyTeacherAttendanceObj);
                            }
                        }
                        break;
                    }
                    default:
                        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Please Select a type", "");
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                        break;
                }

            } else {

                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "No users registered with fingerprint machines", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);

            }

        } else {

            System.out.println("Year is empty");

        }
    }

    public void loadYearList() {
        yearList.clear();
        yearList.add(new SelectItem("0", "Select"));

        String cur_year = comlib.GetCurrentYear() + "";
        String queryy = "SELECT g FROM Year g  order by g.id ASC";
        List<Year> list_year = uni.searchByQuery(queryy);
        for (Year cc : list_year) {

            if (cc.getName().equals(cur_year)) {
                this.yearName = cc.getId() + "";
                setYearValue(cc.getName());
            }

            yearList.add(new SelectItem(cc.getId(), cc.getName()));

        }
    }

    public void loadGradeList() {

        gradeList.clear();
        gradeList.add(new SelectItem("0", "Select"));
        String query_al = "SELECT g FROM GradeClassStream g where g.schoolId.id='" + loginUserSchoolId + "' and g.isActive='1' group by g.gradeId  order by g.gradeId.id ASC ";
        List<GradeClassStream> listAS_al = uni.searchByQuery(query_al);
        for (GradeClassStream cc : listAS_al) {

            String name = cc.getGradeId().getName();

            gradeList.add(new SelectItem(cc.getGradeId().getId(), name));

        }
       
    }

    public void loadClassList() {
        
        System.out.println("loadClassList");
        
        classNameList.clear();
        classNameList.add(new SelectItem("0", "Select"));

        if (!selectedGrade.equals("")) {

            String query_al = "SELECT g FROM GradeClassStream g where g.schoolId.id='" + loginUserSchoolId + "' and g.gradeId.id='" + Integer.parseInt(selectedGrade) + "' and g.isActive='1' order by g.gradeId.id ASC ";
            List<GradeClassStream> listAS_al = uni.searchByQuery(query_al);
            for (GradeClassStream cc : listAS_al) {

                String name = cc.getGradeId().getName() + "-" + cc.getClassesId().getName();
                if (cc.getGradeId().getId() == 12 || cc.getGradeId().getId() == 13) {

                    name = cc.getGradeId().getName() + "-" + cc.getClassesId().getName() + " " + cc.getStreamsId().getName();
                }

                classNameList.add(new SelectItem(cc.getId(), name));

            }
        }
    }

    public void loadGradeSubjects(String para) {

        System.out.println("String value" + para);

        System.out.println("loadGradeSubjects");

        System.out.println("yearName :" + yearName);

        System.out.println("class Name :" + this.selectedClass);

        System.out.println("gradeName :" + selectedGrade);

        String query_al = "SELECT g FROM GradeClassStream g where g.id='" + selectedClass + "'";
//        String query_al = "SELECT g FROM GradeClassSubjectTeacher g WHERE g.gradeClassStreamManagerId.id IN (SELECT s.id FROM GradeClassStreamManager s WHERE s.yearId.id ='" + yearName + "' AND s.gradeClassStreamId.id IN (SELECT l.id FROM GradeClassStream l WHERE l.id ='"+selectedClass+"' AND l.schoolId.id='" + loginUserSchoolId + "'))";
        List<GradeClassStream> listAS_al = uni.searchByQuery(query_al);
        for (GradeClassStream cc : listAS_al) {

            String name = cc.getGradeId().getName() + "-" + cc.getClassesId().getName();
            if (cc.getGradeId().getId() == 12 || cc.getGradeId().getId() == 13) {

                name = cc.getGradeId().getName() + "-" + cc.getClassesId().getName() + " " + cc.getStreamsId().getName();
            }
            System.out.println("setClassId(name); " + name);
            setClassId(name);

        }

        String queryy = "SELECT g FROM Year g where g.id='" + yearName + "' ";
        List<Year> list_year = uni.searchByQuery(queryy);
        for (Year cc : list_year) {
            System.out.println("setYearValue(cc.getName()); " + cc.getName());
            setYearValue(cc.getName());
        }

        gradeSubjectNameList.clear();

        gradeSubjectNameList.add(new SelectItem("0", "Select"));
        String query_gcst = "SELECT g FROM GradeClassSubjectTeacher g WHERE g.gradeClassStreamManagerId.id IN (SELECT s.id FROM GradeClassStreamManager s WHERE s.yearId.id ='" + yearName + "' AND s.gradeClassStreamId.id IN (SELECT l.id FROM GradeClassStream l WHERE l.id ='" + selectedClass + "' AND l.schoolId.id='" + loginUserSchoolId + "'))";
        List<GradeClassSubjectTeacher> listAS_al2 = uni.searchByQuery(query_gcst);
        System.out.println("size" + listAS_al2.size());
        for (GradeClassSubjectTeacher cc : listAS_al2) {

            gradeSubjectNameList.add(new SelectItem(cc.getGradeClassHasSubjectsId().getSubjectsId().getId(), cc.getGradeClassHasSubjectsId().getSubjectsId().getName()));

        }

        selectedDataCellIds.clear();

        for (int i = 0; i <= timeTable.size() - 1; i++) {

            TimeSlot slot = timeTable.get(i);

            if (slot.period1.isSelected) {

                selectedDataCellIds.add(timeTable.get(i).period1);

            }
            if (slot.period2.isSelected) {

                selectedDataCellIds.add(timeTable.get(i).period2);

            }
            if (slot.period3.isSelected) {

                selectedDataCellIds.add(timeTable.get(i).period3);

            }
            if (slot.period4.isSelected) {

                selectedDataCellIds.add(timeTable.get(i).period4);

            }
            if (slot.period5.isSelected) {

                selectedDataCellIds.add(timeTable.get(i).period5);

            }
        }

        loadSubjectTeacherNames();

    }

    public void loadSubjectTeacherNames() {

        subjectTeacherNameList.clear();
        subjectTeacherNameList.add(new SelectItem(0, "Select"));

        if (!gradeSubject.equals("0")) {
            String query = "SELECT g FROM GradeClassStreamManager g WHERE g.yearId.id = '" + yearName + "' AND g.gradeClassStreamId.id IN (SELECT gs.gradeClassStreamId.id FROM GradeClassHasSubjects gs WHERE gs.subjectsId.id IN(SELECT l.id FROM Subjects l WHERE l.id = '" + gradeSubject + "')) ";
            List<GradeClassStreamManager> gcstr = uni.searchByQuery(query);

            if (gcstr.size() > 1) {
                for (GradeClassStreamManager g : gcstr) {
                    System.out.println("g :" + g.getId());
                    if (g.getTeacherId() != null) {
                        subjectTeacherNameList.add(new SelectItem(g.getTeacherId().getId(), g.getTeacherId().getGeneralUserProfileId().getNameWithIn()));
                    }
                }
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "You Does not have assign a relevant teachers to the selected subject for the selected year. Please add teachers from Grade Class Manager", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }

    }

    public void loadTimeTable() {

        timeTable.clear();

        if (!selectedGrade.equals("0") && !selectedClass.equals("0") && !yearName.equals("0")) {

            String query_gradeClassManagerId = "SELECT g FROM GradeClassStreamManager g WHERE g.gradeClassStreamId.id='" + selectedClass + "' AND g.yearId.id = '" + yearName + "'";

            List<GradeClassStreamManager> gradeClassStreamManager = uni.searchByQuery(query_gradeClassManagerId);

            if (!gradeClassStreamManager.isEmpty()) {

                setGcmId(String.valueOf(gradeClassStreamManager.iterator().next().getId()));

                String query_ltt = "SELECT g FROM TimeTable g WHERE g.gradeClassStreamManagerId.id='" + gcmId + "' ";

                List<TimeTable> timetableList = uni.searchByQuery(query_ltt);

                if (!timetableList.isEmpty()) {

                    String query_periods = "SELECT g FROM Periods g where g.schoolId.id = '" + loginUserSchoolId + "'";

                    List<Periods> periods_list = uni.searchByQuery(query_periods);

                    Set<String> daysPeriodIds = new HashSet<>();

                    for (Periods periods : periods_list) {

                        String query_days_periods = "SELECT g FROM DaysPeriod g where g.periodsId.id = '" + periods.getId() + "' ";

                        List<DaysPeriod> daysPeriodsList = uni.searchByQuery(query_days_periods);

                        PeriodData[] periodDataArray = new PeriodData[5];

                        for (int i = 0; i < periodDataArray.length; i++) {

                            System.out.println("Days Period id" + daysPeriodsList.get(i).getId().toString());

                            periodDataArray[i] = new PeriodData(null, null, daysPeriodsList.get(i).getDaysId().getName(), daysPeriodsList.get(i).getPeriodsId().getPeriodNo(), daysPeriodsList.isEmpty() ? "N/A" : daysPeriodsList.get(i).getId().toString(), "0", "N/A", "0", "N/A", false);

                        }

                        for (DaysPeriod daysPeriod : daysPeriodsList) {

                            for (TimeTable timeTable : timetableList) {

                                if (timeTable.getDaysPeriodId().equals(daysPeriod)) {

                                    int periodIndex = timeTable.getId() - 1;

                                    if (periodIndex >= 0 && periodIndex < periodDataArray.length) {

                                        periodDataArray[periodIndex] = new PeriodData(timeTable, timeTable.getDaysPeriodId(), timeTable.getDaysPeriodId().getDaysId().getName(), timeTable.getDaysPeriodId().getPeriodsId().getPeriodNo(), timeTable.getDaysPeriodId().getId().toString(), timeTable.getGradeClassSubjectTeacherId().getGradeClassHasSubjectsId().getSubjectsId().getCode(), timeTable.getGradeClassSubjectTeacherId().getGradeClassHasSubjectsId().getSubjectsId().getName(), timeTable.getGradeClassSubjectTeacherId().getTeacherId().getTeacherId(), timeTable.getGradeClassSubjectTeacherId().getTeacherId().getGeneralUserProfileId().getNameWithIn(), false);

                                    }
                                }

                            }
                        }

                        timeTable.add(new TimeSlot(periods.getPeriodNo(), periodDataArray[0], periodDataArray[1], periodDataArray[2], periodDataArray[3], periodDataArray[4]));

                        System.out.println("first row ends");
                    }

//                    for (Periods periods : periods_list) {
//
//                        PeriodData periodData1 = new PeriodData(null, null, periods.getId().toString(), "0", "N/A", "0", "N/A", false);
//                        PeriodData periodData2 = new PeriodData(null, null, periods.getId().toString(), "0", "N/A", "0", "N/A", false);
//                        PeriodData periodData3 = new PeriodData(null, null, periods.getId().toString(), "0", "N/A", "0", "N/A", false);
//                        PeriodData periodData4 = new PeriodData(null, null, periods.getId().toString(), "0", "N/A", "0", "N/A", false);
//                        PeriodData periodData5 = new PeriodData(null, null, periods.getId().toString(), "0", "N/A", "0", "N/A", false);
//
//                        for (TimeTable timeTable1 : timetableList) {
//                            if (timeTable1.getDaysPeriodId().getPeriodsId().getId().equals(periods.getId())) {
//                                if (timeTable1.getDaysPeriodId().getDaysId().getId() == 1) {
//                                    periodData1 = new PeriodData(timeTable1, timeTable1.getDaysPeriodId(), timeTable1.getDaysPeriodId().getId().toString(), timeTable1.getGradeClassSubjectTeacherId().getGradeClassHasSubjectsId().getSubjectsId().getCode(), timeTable1.getGradeClassSubjectTeacherId().getGradeClassHasSubjectsId().getSubjectsId().getName(), timeTable1.getGradeClassSubjectTeacherId().getTeacherId().getTeacherId(), timeTable1.getGradeClassSubjectTeacherId().getTeacherId().getGeneralUserProfileId().getNameWithIn(), false);
//
//                                }
//
//                                if (timeTable1.getDaysPeriodId().getDaysId().getId() == 2) {
//                                    periodData2 = new PeriodData(timeTable1, timeTable1.getDaysPeriodId(), timeTable1.getDaysPeriodId().getId().toString(), timeTable1.getGradeClassSubjectTeacherId().getGradeClassHasSubjectsId().getSubjectsId().getCode(), timeTable1.getGradeClassSubjectTeacherId().getGradeClassHasSubjectsId().getSubjectsId().getName(), timeTable1.getGradeClassSubjectTeacherId().getTeacherId().getTeacherId(), timeTable1.getGradeClassSubjectTeacherId().getTeacherId().getGeneralUserProfileId().getNameWithIn(), false);
//
//                                }
//                                if (timeTable1.getDaysPeriodId().getDaysId().getId() == 3) {
//                                    periodData3 = new PeriodData(timeTable1, timeTable1.getDaysPeriodId(), timeTable1.getDaysPeriodId().getId().toString(), timeTable1.getGradeClassSubjectTeacherId().getGradeClassHasSubjectsId().getSubjectsId().getCode(), timeTable1.getGradeClassSubjectTeacherId().getGradeClassHasSubjectsId().getSubjectsId().getName(), timeTable1.getGradeClassSubjectTeacherId().getTeacherId().getTeacherId(), timeTable1.getGradeClassSubjectTeacherId().getTeacherId().getGeneralUserProfileId().getNameWithIn(), false);
//
//                                }
//                                if (timeTable1.getDaysPeriodId().getDaysId().getId() == 4) {
//                                    periodData2 = new PeriodData(timeTable1, timeTable1.getDaysPeriodId(), timeTable1.getDaysPeriodId().getId().toString(), timeTable1.getGradeClassSubjectTeacherId().getGradeClassHasSubjectsId().getSubjectsId().getCode(), timeTable1.getGradeClassSubjectTeacherId().getGradeClassHasSubjectsId().getSubjectsId().getName(), timeTable1.getGradeClassSubjectTeacherId().getTeacherId().getTeacherId(), timeTable1.getGradeClassSubjectTeacherId().getTeacherId().getGeneralUserProfileId().getNameWithIn(), false);
//
//                                }
//
//                                if (timeTable1.getDaysPeriodId().getDaysId().getId() == 5) {
//                                    periodData2 = new PeriodData(timeTable1, timeTable1.getDaysPeriodId(), timeTable1.getDaysPeriodId().getId().toString(), timeTable1.getGradeClassSubjectTeacherId().getGradeClassHasSubjectsId().getSubjectsId().getCode(), timeTable1.getGradeClassSubjectTeacherId().getGradeClassHasSubjectsId().getSubjectsId().getName(), timeTable1.getGradeClassSubjectTeacherId().getTeacherId().getTeacherId(), timeTable1.getGradeClassSubjectTeacherId().getTeacherId().getGeneralUserProfileId().getNameWithIn(), false);
//
//                                }
//                            }
//                        }
////
//                        timeTable.add(new TimeSlot(periods.getPeriodNo(), periodData1, periodData2, periodData3, periodData4, periodData5));
//                    }
                } else {

                    String query_periods = "SELECT g FROM Periods g where g.schoolId.id = '" + loginUserSchoolId + "'";

                    List<Periods> periods_list = uni.searchByQuery(query_periods);

                    for (Periods periods : periods_list) {

                        String query_days_periods = "SELECT g FROM DaysPeriod g where g.periodsId.id = '" + periods.getId() + "' ";

                        List<DaysPeriod> daysPeriodsList = uni.searchByQuery(query_days_periods);

                        PeriodData[] periodDataArray = new PeriodData[5];

                        for (int i = 0; i < periodDataArray.length; i++) {

                            periodDataArray[i] = new PeriodData(null, null, daysPeriodsList.get(0).getDaysId().getName(), daysPeriodsList.get(0).getPeriodsId().getPeriodNo(), daysPeriodsList.isEmpty() ? "N/A" : daysPeriodsList.get(0).getId().toString(), "0", "N/A", "0", "N/A", false);

                        }

                        for (DaysPeriod daysPeriod : daysPeriodsList) {

                            for (TimeTable timeTable : timetableList) {

                                if (timeTable.getDaysPeriodId().equals(daysPeriod)) {

                                    int periodIndex = timeTable.getId() - 1;

                                    if (periodIndex >= 0 && periodIndex < periodDataArray.length) {

                                        periodDataArray[periodIndex] = new PeriodData(timeTable, timeTable.getDaysPeriodId(), timeTable.getDaysPeriodId().getDaysId().getName(), timeTable.getDaysPeriodId().getPeriodsId().getPeriodNo(), timeTable.getDaysPeriodId().getId().toString(), timeTable.getGradeClassSubjectTeacherId().getGradeClassHasSubjectsId().getSubjectsId().getCode(), timeTable.getGradeClassSubjectTeacherId().getGradeClassHasSubjectsId().getSubjectsId().getName(), timeTable.getGradeClassSubjectTeacherId().getTeacherId().getTeacherId(), timeTable.getGradeClassSubjectTeacherId().getTeacherId().getGeneralUserProfileId().getNameWithIn(), false);

                                    }
                                }

                            }
                        }

                        timeTable.add(new TimeSlot(periods.getPeriodNo(), periodDataArray[0], periodDataArray[1], periodDataArray[2], periodDataArray[3], periodDataArray[4]));

                    }

                }
            } else {

                System.out.println("grade class stream manager is empty");
                timeTable.clear();

                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "To Proceed Please assign a class teacher from Grade Class Manager", "");

                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
    }

    public void addSubjectAndTeacherToTimeTableSetup() {

        System.out.println("into addSubjectAndTeacherToTimeTableSetup");

        setSubjectTeacherId(subjectTeacher);

        setGradeSubjectId(gradeSubject);

        if (!selectedDataCellsToUpdate.isEmpty()) {
            boolean b = false;
            for (PeriodData data : selectedDataCellIds) {
                for (PeriodData data2 : selectedDataCellsToUpdate) {
                    if (data2.equals(data) && data2.getCellSubjcetId() != null && data.getCellSubjectTeacherId() != null) {
                        break;
                    }
                    b = true;
                }
            }

            if (b) {
                for (PeriodData data : selectedDataCellIds) {

                    if (data.isSelected == true) {

                        System.out.println(" data.getDaysPeriodId() " + data.getDaysPeriodId());

                        for (SelectItem item : subjectTeacherNameList) {

                            System.out.println(subjectTeacherId + " = " + item.getValue());

                            if (subjectTeacherId.equals(item.getValue().toString())) {

                                System.out.println(subjectTeacherId + " = " + item.getValue());

                                data.setCellSubjectTeacherId(subjectTeacher);

                                data.setCellSubjectTeacher(item.getLabel());

                                break;

                            }

                        }

                        for (SelectItem item : gradeSubjectNameList) {

                            System.out.println(gradeSubjectId + " = " + item.getValue());

                            if (gradeSubjectId.equals(item.getValue().toString())) {

                                System.out.println(gradeSubjectId + " = " + item.getValue());

                                data.setCellSubjcetId(gradeSubject);

                                data.setCellSubjectName(item.getLabel());

                                break;

                            }

                        }

                        selectedDataCellsToUpdate.add(data);

                    }

                }

            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "You are trying to add a subject to a already filled timeslot.Please check and try again", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }

        } else {
            for (PeriodData data : selectedDataCellIds) {

                if (data.isSelected == true) {

                    System.out.println(" data.getDaysPeriodId() " + data.getDaysPeriodId());

                    for (SelectItem item : subjectTeacherNameList) {

                        System.out.println(subjectTeacherId + " = " + item.getValue());

                        if (subjectTeacherId.equals(item.getValue().toString())) {

                            System.out.println(subjectTeacherId + " = " + item.getValue());

                            data.setCellSubjectTeacherId(subjectTeacher);

                            data.setCellSubjectTeacher(item.getLabel());

                            break;

                        }

                    }

                    for (SelectItem item : gradeSubjectNameList) {

                        System.out.println(gradeSubjectId + " = " + item.getValue());

                        if (gradeSubjectId.equals(item.getValue().toString())) {

                            System.out.println(gradeSubjectId + " = " + item.getValue());

                            data.setCellSubjcetId(gradeSubject);

                            data.setCellSubjectName(item.getLabel());

                            break;

                        }

                    }

                    selectedDataCellsToUpdate.add(data);

                }

            }
        }

        System.out.println("selectedDataCellsToUpdate.size() :" + selectedDataCellsToUpdate.size());
        loadGradeSubjects("para");

    }

    public void clearSubjectAndTeacherFromTimeTableSetup() {
        selectedDataCellsToUpdate.clear();
    }

//    public void saveSelectedTeacherToTimeTable() {
//        System.out.println("saveSelectedTeacherToTimeTable");
//        System.out.println(loginUserSchoolId);
//        System.out.println(gcmId);
//        boolean result = comDiv.saveSelectedTeacherToTimeTable(selectedDataCellsToUpdate, gcmId, loginUserSchoolId);
//        FacesMessage msg = null;
//        if (result) {
//            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "New Subject added successfully", "");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//        } else {
//            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Adding new Subject to the time table failed", " To add a subject teacher to the class. You have setup Grade Class Manager with relevant class teacher with relevant Subject Teachers");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//        }
//    }
    public void loadAbsentTeachersAndClassesToSetup() {

        System.out.println("LoadTimeTableSetupTable()");
        System.out.println(loadViewsNo);

        if (true) {

            SimpleDateFormat smp = new SimpleDateFormat("YYYY");

            String date = smp.format(currentDateTeacherAttendance);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String selectedDateFormat = dateFormat.format(currentDateTeacherAttendance);

            Calendar calendar = Calendar.getInstance();

            calendar.setTime(currentDateTeacherAttendance);

            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            String day_id;

            switch (dayOfWeek) {

                case Calendar.MONDAY:

                    day_id = "1";

                    break;

                case Calendar.TUESDAY:

                    day_id = "2";

                    break;

                case Calendar.WEDNESDAY:

                    day_id = "3";

                    break;

                case Calendar.THURSDAY:

                    day_id = "4";

                    break;

                case Calendar.FRIDAY:

                    day_id = "5";

                    break;

                default:

                    day_id = "0";

                    break;

            }

            String query_daysPeriod = "SELECT g FROM DaysPeriod g WHERE g.daysId.id='" + day_id + "'";

            List<DaysPeriod> daysPeriodsList = uni.searchByQuery(query_daysPeriod);

            String query_getClassList = "SELECT g FROM GradeClassStream g WHERE g.schoolId.id='" + loginUserSchoolId + "' AND g.isActive='1' ORDER BY g.gradeId.id";

            List<GradeClassStream> classList = uni.searchByQuery(query_getClassList);

            String year_query = "SELECT g FROM Year g WHERE g.name='" + date + "'";

            List<Year> year = uni.searchByQuerySingle(year_query);

            for (GradeClassStream classObj : classList) {

                String query_gradeClassStreamManager = "SELECT g FROM GradeClassStreamManager g WHERE g.gradeClassStreamId.id='" + classObj.getId() + "' AND g.yearId.id='" + year.get(0).getId() + "'";

                List<GradeClassStreamManager> gradeClassStreamManagerList = uni.searchByQuery(query_gradeClassStreamManager);

                for (GradeClassStreamManager gradeClassStreamManagerObj : gradeClassStreamManagerList) {

                    for (DaysPeriod daysPeriod : daysPeriodsList) {

                        String query_timeTable = "SELECT g FROM TimeTable g WHERE g.gradeClassStreamManagerId.id='" + gradeClassStreamManagerObj.getId() + "' AND g.daysPeriodId.id = '" + daysPeriod.getId() + "' ";

                        List<TimeTable> timeTableList = uni.searchByQuery(query_timeTable);

                        if (timeTableList.size() > 0) {

                            String query_teacherAttendance = "SELECT g FROM TeacherAttendance g WHERE g.teacherId.id='" + timeTableList.get(0).getGradeClassSubjectTeacherId().getTeacherId().getId() + "' AND g.date='" + selectedDateFormat + "'";

                            List<com.ejb.model.entity.TeacherAttendance> teacherAttendanceList = uni.searchByQuery(query_teacherAttendance);

                            if (teacherAttendanceList.size() > 0) {
                                System.out.println("Teacher is present");
                            } else {
                                System.out.println("Teacher is Absenet");
                            }

                        }

                    }
                }

            }

        } else {

        }
    }

    public void createBarModel() {

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

            model.setTitle("Daily Attendance Report - " + calendar.get(Calendar.YEAR) + " " + new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)]);
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

    public void testing() {
        System.out.println("testing method" + selectedClass);
    }

    class AbsentTeachers implements Serializable {

    }

    public String getSearchByName() {
        return searchByName;
    }

    public void setSearchByName(String searchByName) {
        this.searchByName = searchByName;
    }

    public List<TimeTableHeader> getTimeTableHeaders() {
        return timeTableHeaders;
    }

    public void setTimeTableHeaders(List<TimeTableHeader> timeTableHeaders) {
        this.timeTableHeaders = timeTableHeaders;
    }

    public class TimeTableHeader implements Serializable {

        private Days monday;
        private Days tuesday;
        private Days wednesday;
        private Days thursday;
        private Days friDay;

        public TimeTableHeader(Days monday, Days tuesday, Days wednesday, Days thursday, Days friDay) {
            this.monday = monday;
            this.tuesday = tuesday;
            this.wednesday = wednesday;
            this.thursday = thursday;
            this.friDay = friDay;
        }

        public Days getMonday() {
            return monday;
        }

        public void setMonday(Days monday) {
            this.monday = monday;
        }

        public Days getTuesday() {
            return tuesday;
        }

        public void setTuesday(Days tuesday) {
            this.tuesday = tuesday;
        }

        public Days getWednesday() {
            return wednesday;
        }

        public void setWednesday(Days wednesday) {
            this.wednesday = wednesday;
        }

        public Days getThursday() {
            return thursday;
        }

        public void setThursday(Days thursday) {
            this.thursday = thursday;
        }

        public Days getFriDay() {
            return friDay;
        }

        public void setFriDay(Days friDays) {
            this.friDay = friDays;
        }

    }

    public List<PeriodData> getSelectedDataCellsToUpdate() {
        return selectedDataCellsToUpdate;
    }

    public void setSelectedDataCellsToUpdate(List<PeriodData> selectedDataCellsToUpdate) {
        this.selectedDataCellsToUpdate = selectedDataCellsToUpdate;
    }

    public String getGradeSubjectId() {
        return gradeSubjectId;
    }

    public void setGradeSubjectId(String gradeSubjectId) {
        this.gradeSubjectId = gradeSubjectId;
    }

    public String getSubjectTeacherId() {
        return subjectTeacherId;
    }

    public void setSubjectTeacherId(String subjectTeacherId) {
        this.subjectTeacherId = subjectTeacherId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public void LoadTimeTableSetupTable() {
    }

    public int getLoginUserSchoolId() {
        return loginUserSchoolId;
    }

    public void setLoginUserSchoolId(int LoginUserSchoolId) {
        this.loginUserSchoolId = LoginUserSchoolId;
    }

    public String getYearValue() {
        return yearValue;
    }

    public void setYearValue(String yearValue) {
        this.yearValue = yearValue;
    }

    public List<PeriodData> getSelectedDataCellIds() {
        return selectedDataCellIds;
    }

    public void setSelectedDataCellIds(List<PeriodData> selectedDataCellIds) {
        this.selectedDataCellIds = selectedDataCellIds;
    }

    public List<TimeTableSetUp> getTimeTableSetupList() {
        return timeTableSetupList;
    }

    public void setTimeTableSetupList(List<TimeTableSetUp> timeTableSetupList) {
        this.timeTableSetupList = timeTableSetupList;
    }

    public List<TimeSlot> getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(List<TimeSlot> timeTable) {
        this.timeTable = timeTable;
    }

    public String getSubjectTeacher() {
        return subjectTeacher;
    }

    public void setSubjectTeacher(String subjectTeacher) {
        this.subjectTeacher = subjectTeacher;
    }

    public List<SelectItem> getSubjectTeacherNameList() {
        return subjectTeacherNameList;
    }

    public void setSubjectTeacherNameList(List<SelectItem> subjectTeacherNameList) {
        this.subjectTeacherNameList = subjectTeacherNameList;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

    public String getYearName() {
        return yearName;
    }

    public void setYearName(String yearName) {
        this.yearName = yearName;
    }

    public List<SelectItem> getGradeSubjectNameList() {
        return gradeSubjectNameList;
    }

    public void setGradeSubject(String gradeSubject) {
        this.gradeSubject = gradeSubject;
    }

    public String getGradeSubject() {
        return gradeSubject;
    }

    public String getSelectedGrade() {
        return selectedGrade;
    }

    public void setSelectedGrade(String selectedGrade) {
        this.selectedGrade = selectedGrade;
    }

    public String getSelectedClass() {
        return selectedClass;
    }

    public void setSelectedClass(String selectedClass) {
        this.selectedClass = selectedClass;
    }

    public List<SelectItem> getYearList() {
        return yearList;
    }

    public List<SelectItem> getGradeList() {
        return gradeList;
    }

    public List<SelectItem> getClassNameList() {
        return classNameList;
    }

    public String getAttendanceTypeId() {
        return attendanceTypeId;
    }

    public void setAttendanceTypeId(String attendanceTypeId) {
        this.attendanceTypeId = attendanceTypeId;
    }

    public void setYearList(List<SelectItem> yearList) {
        this.yearList = yearList;
    }

    public void setGradeList(List<SelectItem> gradeList) {
        this.gradeList = gradeList;
    }

    public void setClassNameList(List<SelectItem> classNameList) {
        this.classNameList = classNameList;
    }

    public void setGradeSubjectNameList(List<SelectItem> gradeSubjectNameList) {
        this.gradeSubjectNameList = gradeSubjectNameList;
    }

    public List<TeacherAttendance> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<TeacherAttendance> attendanceList) {
        this.attendanceList = attendanceList;
    }

    public String getAttendaceType() {
        return attendaceType;
    }

    public void setAttendaceType(String attendaceType) {
        this.attendaceType = attendaceType;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public class TimeTableSetUp implements Serializable {

        private String timeSlotId;
        private String subjectId;
        private String subjctName;
        private String teacherId;
        private String teacherName;

        public TimeTableSetUp(String timeSlotId, String subjectId, String subjctName, String teacherId, String teacherName) {
            this.timeSlotId = timeSlotId;
            this.subjectId = subjectId;
            this.subjctName = subjctName;
            this.teacherId = teacherId;
            this.teacherName = teacherName;
        }

        public String getTimeSlotId() {
            return timeSlotId;
        }

        public void setTimeSlotId(String timeSlotId) {
            this.timeSlotId = timeSlotId;
        }

        public String getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(String subjectId) {
            this.subjectId = subjectId;
        }

        public String getSubjctName() {
            return subjctName;
        }

        public void setSubjctName(String subjctName) {
            this.subjctName = subjctName;
        }

        public String getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(String teacherId) {
            this.teacherId = teacherId;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

    }

    public class PeriodData implements Serializable {

        private TimeTable timeTableObj;
        private DaysPeriod daysPeriodObj;
        private String date;
        private String timeSlot;
        private String daysPeriodId;
        private String cellSubjcetId;
        private String cellSubjectName;
        private String cellSubjectTeacherId;
        private String cellSubjectTeacher;
        private boolean isSelected;

        public PeriodData(TimeTable timeTableObj, DaysPeriod daysPeriodObj, String date, String timeSlot, String daysPeriodId, String cellSubjcetId, String cellSubjectName, String cellSubjectTeacherId, String cellSubjectTeacher, boolean isSelected) {
            this.timeTableObj = timeTableObj;
            this.daysPeriodObj = daysPeriodObj;
            this.date = date;
            this.timeSlot = timeSlot;
            this.daysPeriodId = daysPeriodId;
            this.cellSubjcetId = cellSubjcetId;
            this.cellSubjectName = cellSubjectName;
            this.cellSubjectTeacherId = cellSubjectTeacherId;
            this.cellSubjectTeacher = cellSubjectTeacher;
            this.isSelected = isSelected;
        }

        public TimeTable getTimeTableObj() {
            return timeTableObj;
        }

        public void setTimeTableObj(TimeTable timeTableObj) {
            this.timeTableObj = timeTableObj;
        }

        public DaysPeriod getDaysPeriodObj() {
            return daysPeriodObj;
        }

        public void setDaysPeriodObj(DaysPeriod daysPeriodObj) {
            this.daysPeriodObj = daysPeriodObj;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTimeSlot() {
            return timeSlot;
        }

        public void setTimeSlot(String timeSlot) {
            this.timeSlot = timeSlot;
        }

        public String getDaysPeriodId() {
            return daysPeriodId;
        }

        public void setDaysPeriodId(String daysPeriodId) {
            this.daysPeriodId = daysPeriodId;
        }

        public String getCellSubjcetId() {
            return cellSubjcetId;
        }

        public void setCellSubjcetId(String cellSubjcetId) {
            this.cellSubjcetId = cellSubjcetId;
        }

        public String getCellSubjectName() {
            return cellSubjectName;
        }

        public void setCellSubjectName(String cellSubjectName) {
            this.cellSubjectName = cellSubjectName;
        }

        public String getCellSubjectTeacherId() {
            return cellSubjectTeacherId;
        }

        public void setCellSubjectTeacherId(String cellSubjectTeacherId) {
            this.cellSubjectTeacherId = cellSubjectTeacherId;
        }

        public String getCellSubjectTeacher() {
            return cellSubjectTeacher;
        }

        public void setCellSubjectTeacher(String cellSubjectTeacher) {
            this.cellSubjectTeacher = cellSubjectTeacher;
        }

        public boolean isIsSelected() {
            return isSelected;
        }

        public void setIsSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

    }

    public class TimeSlot implements Serializable {

        private String time_slot;
        private PeriodData period1;
        private PeriodData period2;
        private PeriodData period3;
        private PeriodData period4;
        private PeriodData period5;

        public TimeSlot(String time_slot, PeriodData period1, PeriodData period2, PeriodData period3, PeriodData period4, PeriodData period5) {
            this.time_slot = time_slot;
            this.period1 = period1;
            this.period2 = period2;
            this.period3 = period3;
            this.period4 = period4;
            this.period5 = period5;
        }

        public String getTime_slot() {
            return time_slot;
        }

        public void setTime_slot(String time_slot) {
            this.time_slot = time_slot;
        }

        public PeriodData getPeriod1() {
            return period1;
        }

        public void setPeriod1(PeriodData period1) {
            this.period1 = period1;
        }

        public PeriodData getPeriod2() {
            return period2;
        }

        public void setPeriod2(PeriodData period2) {
            this.period2 = period2;
        }

        public PeriodData getPeriod3() {
            return period3;
        }

        public void setPeriod3(PeriodData period3) {
            this.period3 = period3;
        }

        public PeriodData getPeriod4() {
            return period4;
        }

        public void setPeriod4(PeriodData period4) {
            this.period4 = period4;
        }

        public PeriodData getPeriod5() {
            return period5;
        }

        public void setPeriod5(PeriodData period5) {
            this.period5 = period5;
        }

    }

    public class TeacherAttendance implements Serializable {

        private String no;
        private String teacher_id;
        private String teacher_name;
        private String in_time;
        private String out_time;
        private boolean attendance;
        private String grades;

        public TeacherAttendance(String no, String teacher_id, String teacher_name, String in_time, String out_time, boolean attendance, String grades) {
            this.no = no;
            this.teacher_id = teacher_id;
            this.teacher_name = teacher_name;
            this.in_time = in_time;
            this.out_time = out_time;
            this.attendance = attendance;
            this.grades = grades;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getTeacher_id() {
            return teacher_id;
        }

        public void setTeacher_id(String teacher_id) {
            this.teacher_id = teacher_id;
        }

        public String getTeacher_name() {
            return teacher_name;
        }

        public void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
        }

        public String getIn_time() {
            return in_time;
        }

        public void setIn_time(String in_time) {
            this.in_time = in_time;
        }

        public String getOut_time() {
            return out_time;
        }

        public void setOut_time(String out_time) {
            this.out_time = out_time;
        }

        public boolean isAttendance() {
            return attendance;
        }

        public void setAttendance(boolean attendance) {
            this.attendance = attendance;
        }

        public String getGrades() {
            return grades;
        }

        public void setGrades(String grades) {
            this.grades = grades;
        }

    }

    public void loadClasses() {

//        attendanceList.add(e)
    }

    public class ClassList implements Serializable {

        private String class_id;

        public ClassList(String class_id) {
            this.class_id = class_id;
        }

        public String getClass_id() {
            return class_id;
        }

        public void setClass_id(String class_id) {
            this.class_id = class_id;
        }

    }

    public class YearList implements Serializable {

        private String year;
        private int value;

        public YearList(int value, String year) {
            this.year = year;
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

    }

    public String getLoadViewsNo() {
        return loadViewsNo;
    }

    public void setLoadViewsNo(String loadViewsNo) {
        this.loadViewsNo = loadViewsNo;
    }

    public Date getCurrentDateTeacherAttendance() {
        return currentDateTeacherAttendance;
    }

    public void setCurrentDateTeacherAttendance(Date currentDateTeacherAttendance) {
        this.currentDateTeacherAttendance = currentDateTeacherAttendance;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

}
