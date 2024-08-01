/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.managebeans;

import com.ejb.model.common.ComDev;
import com.ejb.model.common.ComLib;
import com.ejb.model.common.ComPath;
import com.ejb.model.common.StoredProcedures;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.FingerPrintRegionUser;
import com.ejb.model.entity.GeneralUserProfile;
import com.ejb.model.entity.Grade;
import com.ejb.model.entity.GradeClassStream;
import com.ejb.model.entity.GradeClassStreamManager;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.TeacherType;
import com.ejb.model.entity.Year;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author USER
 */
@ManagedBean(name = "DailyAbsentReport")
@ViewScoped
public class DailyAbsentReport {

    private Date currentDate;
    private String currentDateTeacherAttendance;
    private String loginUserSchool;

    private int totalRegisteredTeachers;
    private int presentTeachersCount;
    private int absentTeachersCount;

    private double attendPercentageCount;
    private int attendBeforeSeventTwenty;
    private double attendBeforePercentage;

    private double totalRegisteredTeachersPercentage;
    private double presenteachersCountPercentage;
    private double absentTeachersCountPercentage;
    private double absentTeachersPercentage;
    private int totalTotalTeachers;

    private int secondaryTeacherCount;
    private int alTeacherCount;
    private int TrainingTeacherCount;
    private int administrationTeacherCount;
    private int sdsTeacherCount;
    private int primary;

    private int secondaryTeacherCountAttend;
    private int alTeacherCountAttend;
    private int TrainingTeacherCountAttend;
    private int administrationTeacherCountAttend;
    private int sdsTeacherCountAttend;
    private int primaryAttend;

    private String selectedTeacher;

    private List<SelectItem> gradeList = new ArrayList<SelectItem>();
    private List<SelectItem> teacherTypeList = new ArrayList<SelectItem>();
    private List<DailyAbsentReport.TeacherAttendance> attendanceList = new ArrayList();

    private String attendaceType = "3";
    private String attendanceTypeId = "0";
    private String teacherType;
    HttpServletResponse response;
    HttpServletRequest request;
    private ComLib comlib;
    private ComPath comPath;
    private final boolean loggedIn = false;

    @EJB
    private StoredProcedures st;

    @EJB
    private UniDBLocal uni;
    LoginSession ls;

    @EJB
    private ComDev comDiv;

    @Resource(lookup = "java:app/ds_education_db")
    private javax.sql.DataSource dataSource;

    @PostConstruct
    public void init() {
        Date date = new Date();

        currentDate = date;

        String formattedDate = new SimpleDateFormat("yyyy-MM").format(date);

        currentDateTeacherAttendance = formattedDate;

        initializeData();
    }

    public void initializeData() {
        loadTeacherTypes();
        teacherAttendance();
        loadGrades();
        loadTeacherAttendance();
    }

    public void setSelectedTeacherToTable() {

        attendanceList.clear();

        System.out.println("selected Teacher " + selectedTeacher);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String smpDate = dateFormat.format(currentDate);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm aa");

        int min = 0;
        int max = selectedTeacher.indexOf("-");
        String tid = selectedTeacher.substring(min, max);

        if (!tid.equals("")) {

            List<Teacher> getTeacher = uni.searchByQuery("SELECT g FROM Teacher g WHERE g.teacherId = '" + tid + "' AND g.schoolId.id='100' and g.isActive='1'");

            Teacher teahcer = getTeacher.get(0);

            for (Teacher teacher : getTeacher) {
                List<FingerPrintRegionUser> fingerPrintRegionUsersList = uni.searchByQuery("SELECT g FROM FingerPrintRegionUser g WHERE g.generalUserProfileGupId.id='" + teacher.getGeneralUserProfileId().getId() + "' AND g.isActive = '1'");

                if ((fingerPrintRegionUsersList.size() > 0) && (fingerPrintRegionUsersList.size() < 2)) {

                    List<com.ejb.model.entity.TeacherAttendance> teacherAttendances = uni.searchByQuery("SELECT g FROM TeacherAttendance g WHERE g.date='" + smpDate + "' AND g.teacherId.id='" + teacher.getId() + "' ");

                    String inTime = "N/A";
                    String outTime = "N/A";

                    if (teacherAttendances.size() > 0) {

                        com.ejb.model.entity.TeacherAttendance teacherobj = teacherAttendances.get(0);

                        if (teacherobj.getIn_time() != null) {
                            inTime = dateFormat2.format(teacherobj.getIn_time());
                        }

                        if (teacherobj.getOut_time() != null) {
                            outTime = dateFormat2.format(teacherobj.getOut_time());
                        }

                        String ttype = "N/A";

                        if (teacher.getTeacherTypeId() != null) {
                            ttype = teacher.getTeacherTypeId().getType();
                        }

                        getAttendanceList().add(new TeacherAttendance("", teacherobj.getTeacherId().getTeacherId(), teacherobj.getTeacherId().getGeneralUserProfileId().getNameWithIn(), inTime, outTime, true, ttype));
                    } else {

                        String ttype = "N/A";

                        if (teacher.getTeacherTypeId() != null) {
                            ttype = teacher.getTeacherTypeId().getType();
                        }

                        getAttendanceList().add(new TeacherAttendance("", teacher.getTeacherId(), teacher.getGeneralUserProfileId().getNameWithIn(), inTime, outTime, true, ttype));
                    }

                }
            }

        }

    }

    public List<String> loadSearchTeacher(String query) {
        System.out.println("awa " + query);
        List<String> searchTeacher = new ArrayList<>();
        if (query != null) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String smpDate = dateFormat.format(currentDate);

            List<Teacher> getTeacherList = uni.searchByQuery("SELECT g FROM Teacher g WHERE g.teacherId LIKE '%" + query + "%' AND g.schoolId.id='100' and g.isActive='1'");

            for (Teacher teacher : getTeacherList) {
                System.out.println("awa2");
                List<FingerPrintRegionUser> fingerPrintRegionUsersList = uni.searchByQuery("SELECT g FROM FingerPrintRegionUser g WHERE g.generalUserProfileGupId.id='" + teacher.getGeneralUserProfileId().getId() + "' AND g.isActive = '1'");

                if ((fingerPrintRegionUsersList.size() > 0)) {

                    System.out.println("aw3");
                    searchTeacher.add(teacher.getTeacherId() + "-" + teacher.getGeneralUserProfileId().getNameWithIn());

                }
            }

        }
        return searchTeacher;
    }

    private void teacherAttendance() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String smpDate = dateFormat.format(currentDate);

        String query_getFingerPrintRegionUsers = "SELECT g FROM FingerPrintRegionUser g WHERE g.isActive='1' AND g.fingerPrintRegionId.id='1'";

        List<FingerPrintRegionUser> fingerPrintRegionUserList = uni.searchByQuery(query_getFingerPrintRegionUsers);
        System.out.println("fingerPrintRegionUserList " + fingerPrintRegionUserList.size());
        setTotalRegisteredTeachers(fingerPrintRegionUserList.size());

        List<Teacher> secondaryTeacherList = uni.searchByQuery("SELECT g FROM Teacher g WHERE g.schoolId.id='100' and g.isActive='1' and g.teacherTypeId.id='1' ");
        System.out.println("teacherList.size()" + secondaryTeacherList.size());
        setSecondaryTeacherCount(secondaryTeacherList.size());

        List<Teacher> alTeacherList = uni.searchByQuery("SELECT g FROM Teacher g WHERE g.schoolId.id='100' and g.isActive='1' and g.teacherTypeId.id='2' ");
        System.out.println("teacherList.size()" + alTeacherList.size());
        setAlTeacherCount(alTeacherList.size());

        List<Teacher> trainingTeacherList = uni.searchByQuery("SELECT g FROM Teacher g WHERE g.schoolId.id='100' and g.isActive='1' and g.teacherTypeId.id='3' ");
        System.out.println("teacherList.size()" + trainingTeacherList.size());
        setTrainingTeacherCount(trainingTeacherList.size());

        List<Teacher> admin = uni.searchByQuery("SELECT g FROM Teacher g WHERE g.schoolId.id='100' and g.isActive='1' and g.teacherTypeId.id='4' ");
        System.out.println("teacherList.size()" + admin.size());
        setAdministrationTeacherCount(admin.size());

        List<Teacher> sds = uni.searchByQuery("SELECT g FROM Teacher g WHERE g.schoolId.id='100' and g.isActive='1' and g.teacherTypeId.id='5' ");
        System.out.println("teacherList.size()" + sds.size());
        setSdsTeacherCount(sds.size());

        List<Teacher> primary = uni.searchByQuery("SELECT g FROM Teacher g WHERE g.schoolId.id='100' and g.isActive='1' and g.teacherTypeId.id='6' ");
        System.out.println("teacherList.size()" + primary.size());
        setPrimary(primary.size());

        for (SelectItem selectItem : teacherTypeList) {
            System.out.println("teacher type " + selectItem.getValue());
            List<Teacher> teacherListAccordingToType = uni.searchByQuery("SELECT g FROM Teacher g WHERE g.schoolId.id='100' AND g.isActive='1' AND g.teacherTypeId.id='" + selectItem.getValue() + "'");
            System.out.println(selectItem.getValue() + ":" + teacherListAccordingToType.size());
            if (selectItem.getValue().toString().equals("1")) {
                System.out.println("into 1 " + selectItem.getValue());

                int i = 0;

                for (Teacher teacher : teacherListAccordingToType) {
                    List<FingerPrintRegionUser> fingerPrintRegionUsersList = uni.searchByQuery("SELECT g FROM FingerPrintRegionUser g WHERE g.generalUserProfileGupId.id='" + teacher.getGeneralUserProfileId().getId() + "' AND g.isActive = '1'");

                    if ((fingerPrintRegionUsersList.size() > 0)) {
                        System.out.println("into 2 ");
                        List<com.ejb.model.entity.TeacherAttendance> teacherAttendances = uni.searchByQuery("SELECT g FROM TeacherAttendance g WHERE g.date='" + smpDate + "' AND g.teacherId.id='" + teacher.getId() + "' ");

                        if (teacherAttendances.size() > 0) {
                            System.out.println("in the loop 1");
                            i++;
                        }

                    }
                }
                System.out.println("setSecondaryTeacherCount " + i);
                setSecondaryTeacherCountAttend(i);

            } else if (selectItem.getValue().toString().equals("2")) {

                int i = 0;

                for (Teacher teacher : teacherListAccordingToType) {
                    List<FingerPrintRegionUser> fingerPrintRegionUsersList = uni.searchByQuery("SELECT g FROM FingerPrintRegionUser g WHERE g.generalUserProfileGupId.id='" + teacher.getGeneralUserProfileId().getId() + "' AND g.isActive = '1'");

                    if ((fingerPrintRegionUsersList.size() > 0)) {

                        List<com.ejb.model.entity.TeacherAttendance> teacherAttendances = uni.searchByQuery("SELECT g FROM TeacherAttendance g WHERE g.date='" + smpDate + "' AND g.teacherId.id='" + teacher.getId() + "' ");

                        if (teacherAttendances.size() > 0) {
                            System.out.println("in the loop 2");
                            i++;
                        }

                    }
                }
                System.out.println("setAlTeacherCountAttend " + i);
                setAlTeacherCountAttend(i);

            } else if (selectItem.getValue().toString().equals("3")) {

                int i = 0;

                for (Teacher teacher : teacherListAccordingToType) {
                    List<FingerPrintRegionUser> fingerPrintRegionUsersList = uni.searchByQuery("SELECT g FROM FingerPrintRegionUser g WHERE g.generalUserProfileGupId.id='" + teacher.getGeneralUserProfileId().getId() + "' AND g.isActive = '1'");

                    if ((fingerPrintRegionUsersList.size() > 0)) {

                        List<com.ejb.model.entity.TeacherAttendance> teacherAttendances = uni.searchByQuery("SELECT g FROM TeacherAttendance g WHERE g.date='" + smpDate + "' AND g.teacherId.id='" + teacher.getId() + "' ");

                        if (teacherAttendances.size() > 0) {
                            System.out.println("in the loop 3");
                            i++;
                        }

                    }
                }
                System.out.println("setTrainingTeacherCountAttend " + i);
                setTrainingTeacherCountAttend(i);

            } else if (selectItem.getValue().toString().equals("4")) {
                int i = 0;

                for (Teacher teacher : teacherListAccordingToType) {
                    List<FingerPrintRegionUser> fingerPrintRegionUsersList = uni.searchByQuery("SELECT g FROM FingerPrintRegionUser g WHERE g.generalUserProfileGupId.id='" + teacher.getGeneralUserProfileId().getId() + "' AND g.isActive = '1'");

                    if ((fingerPrintRegionUsersList.size() > 0)) {
                        System.out.println("into 2 ");
                        List<com.ejb.model.entity.TeacherAttendance> teacherAttendances = uni.searchByQuery("SELECT g FROM TeacherAttendance g WHERE g.date='" + smpDate + "' AND g.teacherId.id='" + teacher.getId() + "' ");

                        if (teacherAttendances.size() > 0) {
                            System.out.println("in the loop 4");
                            i++;
                        }

                    }
                }
                System.out.println("setSdsTeacherCountAttend " + i);
                setAdministrationTeacherCountAttend(i);
            } else if (selectItem.getValue().toString().equals("5")) {
                int i = 0;

                for (Teacher teacher : teacherListAccordingToType) {
                    List<FingerPrintRegionUser> fingerPrintRegionUsersList = uni.searchByQuery("SELECT g FROM FingerPrintRegionUser g WHERE g.generalUserProfileGupId.id='" + teacher.getGeneralUserProfileId().getId() + "' AND g.isActive = '1'");

                    if ((fingerPrintRegionUsersList.size() > 0)) {
                        System.out.println("into 2 ");
                        List<com.ejb.model.entity.TeacherAttendance> teacherAttendances = uni.searchByQuery("SELECT g FROM TeacherAttendance g WHERE g.date='" + smpDate + "' AND g.teacherId.id='" + teacher.getId() + "' ");

                        if (teacherAttendances.size() > 0) {
                            System.out.println("in the loop 5");

                            i++;
                        }

                    }
                }
                System.out.println("setSdsTeacherCountAttend " + i);
                setSdsTeacherCountAttend(i);
            } else if (selectItem.getValue().toString().equals("6")) {
                int i = 0;

                for (Teacher teacher : teacherListAccordingToType) {
                    List<FingerPrintRegionUser> fingerPrintRegionUsersList = uni.searchByQuery("SELECT g FROM FingerPrintRegionUser g WHERE g.generalUserProfileGupId.id='" + teacher.getGeneralUserProfileId().getId() + "' AND g.isActive = '1'");

                    if ((fingerPrintRegionUsersList.size() > 0) && (fingerPrintRegionUsersList.size() < 2)) {
                        System.out.println("into 2 ");
                        List<com.ejb.model.entity.TeacherAttendance> teacherAttendances = uni.searchByQuery("SELECT g FROM TeacherAttendance g WHERE g.date='" + smpDate + "' AND g.teacherId.id='" + teacher.getId() + "' ");

                        if (teacherAttendances.size() > 0) {
                            System.out.println("in the loop 6");
                            i++;
                        }

                    }
                }
                System.out.println("primary " + i);
                setPrimaryAttend(i);
            }
        }

        if (fingerPrintRegionUserList.size() > 0) {

            String query_getTeacherAttendance = "SELECT g FROM TeacherAttendance g WHERE g.date=' " + smpDate + " '";
            List<com.ejb.model.entity.TeacherAttendance> teacherAttendanceList = uni.searchByQuery(query_getTeacherAttendance);

            setPresentTeachersCount(teacherAttendanceList.size());

            setAbsentTeachersCount(fingerPrintRegionUserList.size() - teacherAttendanceList.size());

            setAttendPercentageCount((teacherAttendanceList.size() / (double) fingerPrintRegionUserList.size()) * 100);

            String query_getTeacherAttendanceBeforeSevenTwenty = "SELECT g FROM TeacherAttendance g WHERE g.date=' " + smpDate + " ' AND  g.in_time < '07:20:00'";
            List<com.ejb.model.entity.TeacherAttendance> teacherAttendanceListBeforeSevenTwenty = uni.searchByQuery(query_getTeacherAttendanceBeforeSevenTwenty);

            setAbsentTeachersPercentage(((fingerPrintRegionUserList.size() - teacherAttendanceList.size()) / (double) fingerPrintRegionUserList.size()) * 100);

            if (teacherAttendanceListBeforeSevenTwenty.size() > 0) {

                setAttendBeforeSeventTwenty(teacherAttendanceListBeforeSevenTwenty.size());

                setAttendBeforePercentage((teacherAttendanceListBeforeSevenTwenty.size() / (double) fingerPrintRegionUserList.size()) * 100);

            } else {

                setAttendBeforeSeventTwenty(0);

            }

        } else {

            setTotalRegisteredTeachers(0);
            setTotalRegisteredTeachersPercentage((fingerPrintRegionUserList.size() / 353) * 100);
        }
    }

    public void loadGrades() {
        gradeList.clear();
        String query_gcs = "SELECT g FROM GradeClassStream g where g.schoolId.id='101' and g.isActive='1' group by g.gradeId.id order by g.gradeId.id ASC ";
        List<GradeClassStream> listAS_gcs = uni.searchByQuery(query_gcs);
        gradeList.add(new SelectItem("", "Select"));
        for (GradeClassStream cc : listAS_gcs) {
            getGradeList().add(new SelectItem(cc.getGradeId().getId(), cc.getGradeId().getName()));
        }
    }

    public void loadTeacherTypes() {
        getTeacherTypeList().clear();
        String query_gcs = "SELECT g FROM TeacherType g";
        List<TeacherType> listAS_gcs = uni.searchByQuery(query_gcs);
        getTeacherTypeList().add(new SelectItem("0", "All"));
        for (TeacherType cc : listAS_gcs) {
            getTeacherTypeList().add(new SelectItem(cc.getId(), cc.getType()));
        }
    }

    public void loadTeacherAttendance() {

        setAttendanceTypeId("0");

        attendanceList.clear();

        System.out.println("attendaceType " + attendaceType);

        setAttendanceTypeId(attendaceType);
        System.out.println("teacher type " + teacherType);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String smpDate = dateFormat.format(currentDate);

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm aa");

        SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy");
        String smpDate3 = dateFormat3.format(currentDate);

        String query_year = "SELECT g FROM Year g WHERE g.name='" + smpDate3 + "' ";

        List<Year> year = uni.searchByQuery(query_year);

        getAttendanceList().clear();

        if (year.size() > 0) {

            SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd");
            String date = smp.format(currentDate);
            System.out.println("date " + date);
            if (attendanceTypeId.equals("3") && teacherType == null) {
                System.out.println("attendanceTypeId.equals(\"3\") && teacherType == null 1");
                List<Teacher> teacherList = uni.searchByQuery("SELECT g FROM Teacher g WHERE g.schoolId.id='100' and g.isActive='1' ");
                if (teacherList.size() > 0) {
                    for (Teacher teacher : teacherList) {
                        List<FingerPrintRegionUser> fingerPrintRegionUsersList = uni.searchByQuery("SELECT g FROM FingerPrintRegionUser g WHERE g.generalUserProfileGupId.id='" + teacher.getGeneralUserProfileId().getId() + "' AND g.isActive = '1'");

                        if ((fingerPrintRegionUsersList.size() > 0) && (fingerPrintRegionUsersList.size() < 2)) {

                            List<com.ejb.model.entity.TeacherAttendance> teacherAttendances = uni.searchByQuery("SELECT g FROM TeacherAttendance g WHERE g.date='" + date + "' AND g.teacherId.id='" + teacher.getId() + "' ");

                            String inTime = "N/A";
                            String outTime = "N/A";

                            if (teacherAttendances.size() == 0) {

                                String ttype = "N/A";

                                if (teacher.getTeacherTypeId() != null) {
                                    System.out.println("teacher.getTeacherTypeId().getType()" + teacher.getTeacherTypeId().getType());
                                    ttype = teacher.getTeacherTypeId().getType();
                                }

                                getAttendanceList().add(new TeacherAttendance("", teacher.getTeacherId(), teacher.getGeneralUserProfileId().getNameWithIn(), inTime, outTime, true, ttype));
                            }

                        }

                    }
                } else {
                    System.out.println("In it Teacher List is Empty");
                }

            } else if ((attendanceTypeId.equals("1") && teacherType.equals("0")) || (attendanceTypeId.equals("2") && teacherType.equals("0") || attendanceTypeId.equals("2") && teacherType.equals("0"))) {
                System.out.println(" teacherType values na ");
                if (attendanceTypeId.equals("1")) {
                    List<Teacher> teacherList = uni.searchByQuery("SELECT g FROM Teacher g WHERE g.schoolId.id='100' and g.isActive='1' ");
                    if (teacherList.size() > 0) {
                        for (Teacher teacher : teacherList) {
                            List<FingerPrintRegionUser> fingerPrintRegionUsersList = uni.searchByQuery("SELECT g FROM FingerPrintRegionUser g WHERE g.generalUserProfileGupId.id='" + teacher.getGeneralUserProfileId().getId() + "' AND g.isActive = '1'");

                            if ((fingerPrintRegionUsersList.size() > 0) && (fingerPrintRegionUsersList.size() < 2)) {

                                List<com.ejb.model.entity.TeacherAttendance> teacherAttendances = uni.searchByQuery("SELECT g FROM TeacherAttendance g WHERE g.date='" + date + "' AND g.teacherId.id='" + teacher.getId() + "' ");

                                String inTime = "N/A";
                                String outTime = "N/A";

                                if (teacherAttendances.size() > 0) {

                                    com.ejb.model.entity.TeacherAttendance teacherobj = teacherAttendances.get(0);

                                    if (teacherobj.getIn_time() != null) {
                                        inTime = dateFormat2.format(teacherobj.getIn_time());
                                    }

                                    if (teacherobj.getOut_time() != null) {
                                        outTime = dateFormat2.format(teacherobj.getOut_time());
                                    }

                                    String ttype = "N/A";

                                    if (teacher.getTeacherTypeId() != null) {
                                        System.out.println("teacher.getTeacherTypeId().getType()" + teacher.getTeacherTypeId().getType());
                                        ttype = teacher.getTeacherTypeId().getType();
                                    }

                                    getAttendanceList().add(new TeacherAttendance("", teacherobj.getTeacherId().getTeacherId(), teacherobj.getTeacherId().getGeneralUserProfileId().getNameWithIn(), inTime, outTime, true, ttype));
                                } else {

                                    String ttype = "N/A";

                                    if (teacher.getTeacherTypeId() != null) {
                                        ttype = teacher.getTeacherTypeId().getType();
                                    }

                                    getAttendanceList().add(new TeacherAttendance("", teacher.getTeacherId(), teacher.getGeneralUserProfileId().getNameWithIn(), inTime, outTime, true, ttype));
                                }

                            }

                        }
                    } else {
                        System.out.println("In it Teacher List is Empty");
                    }
                } else if (attendanceTypeId.equals("2")) {
                    List<Teacher> teacherList = uni.searchByQuery("SELECT g FROM Teacher g WHERE g.schoolId.id='100' and g.isActive='1' ");
                    if (teacherList.size() > 0) {
                        for (Teacher teacher : teacherList) {
                            List<FingerPrintRegionUser> fingerPrintRegionUsersList = uni.searchByQuery("SELECT g FROM FingerPrintRegionUser g WHERE g.generalUserProfileGupId.id='" + teacher.getGeneralUserProfileId().getId() + "' AND g.isActive = '1'");

                            if ((fingerPrintRegionUsersList.size() > 0) && (fingerPrintRegionUsersList.size() < 2)) {

                                List<com.ejb.model.entity.TeacherAttendance> teacherAttendances = uni.searchByQuery("SELECT g FROM TeacherAttendance g WHERE g.date='" + date + "' AND g.teacherId.id='" + teacher.getId() + "' ");

                                String inTime = "N/A";
                                String outTime = "N/A";

                                if (teacherAttendances.size() > 0) {

                                    com.ejb.model.entity.TeacherAttendance teacherobj = teacherAttendances.get(0);

                                    if (teacherobj.getIn_time() != null) {
                                        inTime = dateFormat2.format(teacherobj.getIn_time());
                                    }

                                    if (teacherobj.getOut_time() != null) {
                                        outTime = dateFormat2.format(teacherobj.getOut_time());
                                    }

                                    String ttype = "N/A";

                                    if (teacher.getTeacherTypeId() != null) {
                                        System.out.println("teacher.getTeacherTypeId().getType()" + teacher.getTeacherTypeId().getType());
                                        ttype = teacher.getTeacherTypeId().getType();
                                    }

                                    getAttendanceList().add(new TeacherAttendance("", teacherobj.getTeacherId().getTeacherId(), teacherobj.getTeacherId().getGeneralUserProfileId().getNameWithIn(), inTime, outTime, true, ttype));
                                }

                            }

                        }
                    } else {
                        System.out.println("In it Teacher List is Empty");
                    }
                } else if (attendanceTypeId.equals("2")) {
                    List<Teacher> teacherList = uni.searchByQuery("SELECT g FROM Teacher g WHERE g.schoolId.id='100' and g.isActive='1' ");
                    if (teacherList.size() > 0) {
                        for (Teacher teacher : teacherList) {
                            List<FingerPrintRegionUser> fingerPrintRegionUsersList = uni.searchByQuery("SELECT g FROM FingerPrintRegionUser g WHERE g.generalUserProfileGupId.id='" + teacher.getGeneralUserProfileId().getId() + "' AND g.isActive = '1'");

                            if ((fingerPrintRegionUsersList.size() > 0) && (fingerPrintRegionUsersList.size() < 2)) {

                                List<com.ejb.model.entity.TeacherAttendance> teacherAttendances = uni.searchByQuery("SELECT g FROM TeacherAttendance g WHERE g.date='" + date + "' AND g.teacherId.id='" + teacher.getId() + "' ");

                                String inTime = "N/A";
                                String outTime = "N/A";

                                if (teacherAttendances.size() > 0) {

                                    com.ejb.model.entity.TeacherAttendance teacherobj = teacherAttendances.get(0);

                                    if (teacherobj.getIn_time() != null) {
                                        inTime = dateFormat2.format(teacherobj.getIn_time());
                                    }

                                    if (teacherobj.getOut_time() != null) {
                                        outTime = dateFormat2.format(teacherobj.getOut_time());
                                    }

                                    String ttype = "N/A";

                                    if (teacher.getTeacherTypeId() != null) {
                                        System.out.println("teacher.getTeacherTypeId().getType()" + teacher.getTeacherTypeId().getType());
                                        ttype = teacher.getTeacherTypeId().getType();
                                    }

                                    getAttendanceList().add(new TeacherAttendance("", teacherobj.getTeacherId().getTeacherId(), teacherobj.getTeacherId().getGeneralUserProfileId().getNameWithIn(), inTime, outTime, true, ttype));
                                }

                            }

                        }
                    } else {
                        System.out.println("In it Teacher List is Empty");
                    }
                }
            } else {
                System.out.println("teacherType values tiyenawa");
                if (attendanceTypeId.equals("3")) {
                    if (teacherType.equals("0")) {
                        List<Teacher> teacherList = uni.searchByQuery("SELECT g FROM Teacher g WHERE g.schoolId.id='100' and g.isActive='1' ");
                        if (teacherList.size() > 0) {
                            for (Teacher teacher : teacherList) {
                                List<FingerPrintRegionUser> fingerPrintRegionUsersList = uni.searchByQuery("SELECT g FROM FingerPrintRegionUser g WHERE g.generalUserProfileGupId.id='" + teacher.getGeneralUserProfileId().getId() + "' AND g.isActive = '1'");

                                if ((fingerPrintRegionUsersList.size() > 0) && (fingerPrintRegionUsersList.size() < 2)) {

                                    List<com.ejb.model.entity.TeacherAttendance> teacherAttendances = uni.searchByQuery("SELECT g FROM TeacherAttendance g WHERE g.date='" + date + "' AND g.teacherId.id='" + teacher.getId() + "' ");

                                    String inTime = "N/A";
                                    String outTime = "N/A";

                                    if (teacherAttendances.size() == 0) {

                                        String ttype = "N/A";

                                        if (teacher.getTeacherTypeId() != null) {
                                            System.out.println("teacher.getTeacherTypeId().getType()" + teacher.getTeacherTypeId().getType());
                                            ttype = teacher.getTeacherTypeId().getType();
                                        }

                                        getAttendanceList().add(new TeacherAttendance("", teacher.getTeacherId(), teacher.getGeneralUserProfileId().getNameWithIn(), inTime, outTime, true, ttype));
                                    }

                                }

                            }
                        } else {
                            System.out.println("In it Teacher List is Empty");
                        }
                    } else {
                        List<Teacher> teacherList = uni.searchByQuery("SELECT g FROM Teacher g WHERE g.schoolId.id='100' and g.isActive='1' AND g.teacherTypeId.id='" + teacherType + "' ");
                        if (teacherList.size() > 0) {
                            for (Teacher teacher : teacherList) {
                                List<FingerPrintRegionUser> fingerPrintRegionUsersList = uni.searchByQuery("SELECT g FROM FingerPrintRegionUser g WHERE g.generalUserProfileGupId.id='" + teacher.getGeneralUserProfileId().getId() + "' AND g.isActive = '1'");

                                if ((fingerPrintRegionUsersList.size() > 0) && (fingerPrintRegionUsersList.size() < 2)) {

                                    List<com.ejb.model.entity.TeacherAttendance> teacherAttendances = uni.searchByQuery("SELECT g FROM TeacherAttendance g WHERE g.date='" + date + "' AND g.teacherId.id='" + teacher.getId() + "' ");

                                    String inTime = "N/A";
                                    String outTime = "N/A";

                                    if (teacherAttendances.size() == 0) {

                                        String ttype = "N/A";

                                        if (teacher.getTeacherTypeId() != null) {
                                            System.out.println("teacher.getTeacherTypeId().getType()" + teacher.getTeacherTypeId().getType());
                                            ttype = teacher.getTeacherTypeId().getType();
                                        }

                                        getAttendanceList().add(new TeacherAttendance("", teacher.getTeacherId(), teacher.getGeneralUserProfileId().getNameWithIn(), inTime, outTime, true, ttype));
                                    }

                                }

                            }
                        } else {
                            System.out.println("In it Teacher List is Empty");
                        }
                    }
                } else if (attendanceTypeId.equals("2")) {
                    List<Teacher> teacherList = uni.searchByQuery("SELECT g FROM Teacher g WHERE g.schoolId.id='100' and g.isActive='1' AND g.teacherTypeId.id='" + teacherType + "' ");
                    if (teacherList.size() > 0) {
                        for (Teacher teacher : teacherList) {
                            List<FingerPrintRegionUser> fingerPrintRegionUsersList = uni.searchByQuery("SELECT g FROM FingerPrintRegionUser g WHERE g.generalUserProfileGupId.id='" + teacher.getGeneralUserProfileId().getId() + "' AND g.isActive = '1'");

                            if ((fingerPrintRegionUsersList.size() > 0) && (fingerPrintRegionUsersList.size() < 2)) {

                                List<com.ejb.model.entity.TeacherAttendance> teacherAttendances = uni.searchByQuery("SELECT g FROM TeacherAttendance g WHERE g.date='" + date + "' AND g.teacherId.id='" + teacher.getId() + "' ");

                                String inTime = "N/A";
                                String outTime = "N/A";

                                if (teacherAttendances.size() > 0) {

                                    com.ejb.model.entity.TeacherAttendance teacherobj = teacherAttendances.get(0);

                                    if (teacherobj.getIn_time() != null) {
                                        inTime = dateFormat2.format(teacherobj.getIn_time());
                                    }

                                    if (teacherobj.getOut_time() != null) {
                                        outTime = dateFormat2.format(teacherobj.getOut_time());
                                    }

                                    String ttype = "N/A";

                                    if (teacher.getTeacherTypeId() != null) {
                                        System.out.println("teacher.getTeacherTypeId().getType()" + teacher.getTeacherTypeId().getType());
                                        ttype = teacher.getTeacherTypeId().getType();
                                    }

                                    getAttendanceList().add(new TeacherAttendance("", teacherobj.getTeacherId().getTeacherId(), teacherobj.getTeacherId().getGeneralUserProfileId().getNameWithIn(), inTime, outTime, true, ttype));
                                }

                            }

                        }
                    } else {
                        System.out.println("In it Teacher List is Empty");
                    }
                } else if (attendanceTypeId.equals("3")) {
                    List<Teacher> teacherList = uni.searchByQuery("SELECT g FROM Teacher g WHERE g.schoolId.id='100' and g.isActive='1' AND g.teacherTypeId.id='" + teacherType + "' ");
                    if (teacherList.size() > 0) {
                        for (Teacher teacher : teacherList) {
                            List<FingerPrintRegionUser> fingerPrintRegionUsersList = uni.searchByQuery("SELECT g FROM FingerPrintRegionUser g WHERE g.generalUserProfileGupId.id='" + teacher.getGeneralUserProfileId().getId() + "' AND g.isActive = '1'");

                            if ((fingerPrintRegionUsersList.size() > 0) && (fingerPrintRegionUsersList.size() < 2)) {

                                List<com.ejb.model.entity.TeacherAttendance> teacherAttendances = uni.searchByQuery("SELECT g FROM TeacherAttendance g WHERE g.date='" + date + "' AND g.teacherId.id='" + teacher.getId() + "' ");

                                String inTime = "N/A";
                                String outTime = "N/A";

                                if (teacherAttendances.size() == 0) {

                                    String ttype = "N/A";

                                    if (teacher.getTeacherTypeId() != null) {
                                        System.out.println("teacher.getTeacherTypeId().getType()" + teacher.getTeacherTypeId().getType());
                                        ttype = teacher.getTeacherTypeId().getType();
                                    }

                                    getAttendanceList().add(new TeacherAttendance("", teacher.getTeacherId(), teacher.getGeneralUserProfileId().getNameWithIn(), inTime, outTime, true, ttype));

                                }

                            }

                        }
                    } else {
                        System.out.println("In it Teacher List is Empty");
                    }
                } else if (attendanceTypeId.equals("1")) {
                    List<Teacher> teacherList = uni.searchByQuery("SELECT g FROM Teacher g WHERE g.schoolId.id='100' and g.isActive='1' AND g.teacherTypeId.id='" + teacherType + "' ");
                    if (teacherList.size() > 0) {
                        for (Teacher teacher : teacherList) {
                            List<FingerPrintRegionUser> fingerPrintRegionUsersList = uni.searchByQuery("SELECT g FROM FingerPrintRegionUser g WHERE g.generalUserProfileGupId.id='" + teacher.getGeneralUserProfileId().getId() + "' AND g.isActive = '1'");

                            if ((fingerPrintRegionUsersList.size() > 0) && (fingerPrintRegionUsersList.size() < 2)) {

                                List<com.ejb.model.entity.TeacherAttendance> teacherAttendances = uni.searchByQuery("SELECT g FROM TeacherAttendance g WHERE g.date='" + date + "' AND g.teacherId.id='" + teacher.getId() + "' ");

                                String inTime = "N/A";
                                String outTime = "N/A";

                                if (teacherAttendances.size() > 0) {

                                    com.ejb.model.entity.TeacherAttendance teacherobj = teacherAttendances.get(0);

                                    if (teacherobj.getIn_time() != null) {
                                        inTime = dateFormat2.format(teacherobj.getIn_time());
                                    }

                                    if (teacherobj.getOut_time() != null) {
                                        outTime = dateFormat2.format(teacherobj.getOut_time());
                                    }

                                    String ttype = "N/A";

                                    if (teacher.getTeacherTypeId() != null) {
                                        System.out.println("teacher.getTeacherTypeId().getType()" + teacher.getTeacherTypeId().getType());
                                        ttype = teacher.getTeacherTypeId().getType();
                                    }

                                    getAttendanceList().add(new TeacherAttendance("", teacherobj.getTeacherId().getTeacherId(), teacherobj.getTeacherId().getGeneralUserProfileId().getNameWithIn(), inTime, outTime, true, ttype));
                                } else {

                                    String ttype = "N/A";

                                    if (teacher.getTeacherTypeId() != null) {
                                        System.out.println("teacher.getTeacherTypeId().getType()" + teacher.getTeacherTypeId().getType());
                                        ttype = teacher.getTeacherTypeId().getType();
                                    }

                                    getAttendanceList().add(new TeacherAttendance("", teacher.getTeacherId(), teacher.getGeneralUserProfileId().getNameWithIn(), inTime, outTime, true, ttype));
                                }

                            }

                        }
                    } else {
                        System.out.println("In it Teacher List is Empty");
                    }
                }
            }
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

    public String getSelectedTeacher() {
        return selectedTeacher;
    }

    public void setSelectedTeacher(String selectedTeacher) {
        this.selectedTeacher = selectedTeacher;
    }

    public int getSecondaryTeacherCountAttend() {
        return secondaryTeacherCountAttend;
    }

    public void setSecondaryTeacherCountAttend(int secondaryTeacherCountAttend) {
        this.secondaryTeacherCountAttend = secondaryTeacherCountAttend;
    }

    public int getAlTeacherCountAttend() {
        return alTeacherCountAttend;
    }

    public void setAlTeacherCountAttend(int alTeacherCountAttend) {
        this.alTeacherCountAttend = alTeacherCountAttend;
    }

    public int getTrainingTeacherCountAttend() {
        return TrainingTeacherCountAttend;
    }

    public void setTrainingTeacherCountAttend(int TrainingTeacherCountAttend) {
        this.TrainingTeacherCountAttend = TrainingTeacherCountAttend;
    }

    public int getAdministrationTeacherCountAttend() {
        return administrationTeacherCountAttend;
    }

    public void setAdministrationTeacherCountAttend(int administrationTeacherCountAttend) {
        this.administrationTeacherCountAttend = administrationTeacherCountAttend;
    }

    public int getSdsTeacherCountAttend() {
        return sdsTeacherCountAttend;
    }

    public void setSdsTeacherCountAttend(int sdsTeacherCountAttend) {
        this.sdsTeacherCountAttend = sdsTeacherCountAttend;
    }

    public int getPrimaryAttend() {
        return primaryAttend;
    }

    public void setPrimaryAttend(int primaryAttend) {
        this.primaryAttend = primaryAttend;
    }

    public int getPrimary() {
        return primary;
    }

    public void setPrimary(int primary) {
        this.primary = primary;
    }

    public int getSecondaryTeacherCount() {
        return secondaryTeacherCount;
    }

    public void setSecondaryTeacherCount(int secondaryTeacherCount) {
        this.secondaryTeacherCount = secondaryTeacherCount;
    }

    public int getAlTeacherCount() {
        return alTeacherCount;
    }

    public void setAlTeacherCount(int alTeacherCount) {
        this.alTeacherCount = alTeacherCount;
    }

    public int getTrainingTeacherCount() {
        return TrainingTeacherCount;
    }

    public void setTrainingTeacherCount(int TrainingTeacherCount) {
        this.TrainingTeacherCount = TrainingTeacherCount;
    }

    public int getAdministrationTeacherCount() {
        return administrationTeacherCount;
    }

    public void setAdministrationTeacherCount(int administrationTeacherCount) {
        this.administrationTeacherCount = administrationTeacherCount;
    }

    public int getSdsTeacherCount() {
        return sdsTeacherCount;
    }

    public void setSdsTeacherCount(int sdsTeacherCount) {
        this.sdsTeacherCount = sdsTeacherCount;
    }

    public int getTotalTotalTeachers() {
        return totalTotalTeachers;
    }

    public void setTotalTotalTeachers(int totalTotalTeachers) {
        this.totalTotalTeachers = totalTotalTeachers;
    }

    public String getTeacherType() {
        return teacherType;
    }

    public void setTeacherType(String teacherType) {
        this.teacherType = teacherType;
    }

    public String getAttendanceTypeId() {
        return attendanceTypeId;
    }

    public void setAttendanceTypeId(String attendanceTypeId) {
        this.attendanceTypeId = attendanceTypeId;
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

    public List<SelectItem> getTeacherTypeList() {
        return teacherTypeList;
    }

    public void setTeacherTypeList(List<SelectItem> teacherTypeList) {
        this.teacherTypeList = teacherTypeList;
    }

    public List<SelectItem> getGradeList() {
        return gradeList;
    }

    public void setGradeList(List<SelectItem> gradeList) {
        this.gradeList = gradeList;
    }

    public double getAbsentTeachersPercentage() {
        return absentTeachersPercentage;
    }

    public void setAbsentTeachersPercentage(double absentTeachersPercentage) {
        this.absentTeachersPercentage = absentTeachersPercentage;
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

    public double getAttendPercentageCount() {
        return attendPercentageCount;
    }

    public void setAttendPercentageCount(double attendPercentageCount) {
        this.attendPercentageCount = attendPercentageCount;
    }

    public int getAttendBeforeSeventTwenty() {
        return attendBeforeSeventTwenty;
    }

    public void setAttendBeforeSeventTwenty(int attendBeforeSeventTwenty) {
        this.attendBeforeSeventTwenty = attendBeforeSeventTwenty;
    }

    public double getAttendBeforePercentage() {
        return attendBeforePercentage;
    }

    public void setAttendBeforePercentage(double attendBeforePercentage) {
        this.attendBeforePercentage = attendBeforePercentage;
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

    public String getLoginUserSchool() {
        return loginUserSchool;
    }

    public void setLoginUserSchool(String loginUserSchool) {
        this.loginUserSchool = loginUserSchool;
    }

}
