/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.managebeans;

import com.ejb.model.businesslogic.GoogleMail;
import com.ejb.model.businesslogic.NewMailSender;
import com.ejb.model.businesslogic.mailsend;
import com.ejb.model.common.ComDev;
import com.ejb.model.common.StoredProcedures;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.FingerPrintRegionUser;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.TeacherAttendance;
import com.ejb.model.entity.TeacherType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import static jdk.nashorn.internal.objects.NativeError.printStackTrace;
import static org.apache.poi.hssf.usermodel.HeaderFooter.font;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author USER
 */
@ManagedBean(name = "TeachersMonthlyAttendanceOverviewReport")
@RequestScoped
public class TeachersMonthlyAttendanceOverviewReport {

    private Date selectedMonth;
    private String previousMonthFormatted;
    private String teacherType;
    private String teacherId;
    private String email;
    private byte[] excelData;
    private List<SelectItem> teacherTypeList = new ArrayList<SelectItem>();
    private List<String> monthDates = new ArrayList<String>();
    private List<TeacherMonthlyAttendance> teacherAttendancesList = new ArrayList<TeacherMonthlyAttendance>();
    private Map<Integer, String> dayStyles = new HashMap<>();
    private StreamedContent file;
    private static List<TeacherMonthlyAttendance> loadedAttendances;
    @EJB
    private UniDBLocal uni;

    LoginSession ls;

    @EJB
    private ComDev comDiv;

    @EJB
    private StoredProcedures sp;

//    @Resource(lookup = "java:app/ds_education_db")
//    private javax.sql.DataSource dataSource;
    @PostConstruct
    public void init() {
        initializeData();

    }

    public void initializeData() {

        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        calendar.add(Calendar.MONTH, -1);

        setSelectedMonth(calendar.getTime());

        selectCalendarDate();

        generateMonthDates(selectedMonth);

        loadTeacherTypes();
        initializeDayStyles(selectedMonth);
    }

     public void generateMonthlyAttendanceData() {

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Teachers Data is Loading", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        selectCalendarDate();
        initializeDayStyles(selectedMonth);
        System.out.println("teacherId " + teacherId);
        if (teacherId == null || teacherId.equals("")) {
            String get_finger_print_users = "SELECT g FROM FingerPrintRegionUser g WHERE g.isActive='1' ";
            List<FingerPrintRegionUser> fpru_list = uni.searchByQuery(get_finger_print_users);

            teacherAttendancesList.clear();

            if (fpru_list != null) {
                for (FingerPrintRegionUser fingerPrintRegionUser : fpru_list) {

                    TeacherMonthlyAttendance t = new TeacherMonthlyAttendance();

                    String get_teacher = "SELECT g FROM Teacher g WHERE g.generalUserProfileId.id='" + fingerPrintRegionUser.getGeneralUserProfileGupId().getId() + "' AND g.schoolId.id='100' AND g.isActive='1'";
                    List<Teacher> teacher = uni.searchByQuery(get_teacher);

                    String teacherName = teacher.get(0).getGeneralUserProfileId().getNameWithIn();
                    t.setTeacherName(teacherName);
                    t.setTeacherSignatureId(teacher.get(0).getTeacherId());

                    SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
                    SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    String year = sdfYear.format(selectedMonth);
                    String month = sdfMonth.format(selectedMonth);

                    for (String dateValue : monthDates) {
                        String get_teacher_attendance = "SELECT g FROM TeacherAttendance g WHERE g.date= '" + dateValue + "' AND g.teacherId.id = '" + teacher.get(0).getId() + "' ";
                        List<TeacherAttendance> teacherAttendanceLists = uni.searchByQuery(get_teacher_attendance);

                        String attendanceValue = "-";
                        if (teacherAttendanceLists.size() > 0) {
                            TeacherAttendance obj = teacherAttendanceLists.get(0);
                            String inTime = obj.getIn_time() != null ? timeFormat.format(obj.getIn_time()) : "N/A";
                            String outTime = obj.getOut_time() != null ? timeFormat.format(obj.getOut_time()) : "N/A";
                            attendanceValue = inTime + " - " + outTime;
                        }

                        switch (Integer.parseInt(dateValue.substring(8, 10))) {
                            case 1:
                                t.setDay01(attendanceValue);
                                break;
                            case 2:
                                t.setDay02(attendanceValue);
                                break;
                            case 3:
                                t.setDay03(attendanceValue);
                                break;
                            case 4:
                                t.setDay04(attendanceValue);
                                break;
                            case 5:
                                t.setDay05(attendanceValue);
                                break;
                            case 6:
                                t.setDay06(attendanceValue);
                                break;
                            case 7:
                                t.setDay07(attendanceValue);
                                break;
                            case 8:
                                t.setDay08(attendanceValue);
                                break;
                            case 9:
                                t.setDay09(attendanceValue);
                                break;
                            case 10:
                                t.setDay10(attendanceValue);
                                break;
                            case 11:
                                t.setDay11(attendanceValue);
                                break;
                            case 12:
                                t.setDay12(attendanceValue);
                                break;
                            case 13:
                                t.setDay13(attendanceValue);
                                break;
                            case 14:
                                t.setDay14(attendanceValue);
                                break;
                            case 15:
                                t.setDay15(attendanceValue);
                                break;
                            case 16:
                                t.setDay16(attendanceValue);
                                break;
                            case 17:
                                t.setDay17(attendanceValue);
                                break;
                            case 18:
                                t.setDay18(attendanceValue);
                                break;
                            case 19:
                                t.setDay19(attendanceValue);
                                break;
                            case 20:
                                t.setDay20(attendanceValue);
                                break;
                            case 21:
                                t.setDay21(attendanceValue);
                                break;
                            case 22:
                                t.setDay22(attendanceValue);
                                break;
                            case 23:
                                t.setDay23(attendanceValue);
                                break;
                            case 24:
                                t.setDay24(attendanceValue);
                                break;
                            case 25:
                                t.setDay25(attendanceValue);
                                break;
                            case 26:
                                t.setDay26(attendanceValue);
                                break;
                            case 27:
                                t.setDay27(attendanceValue);
                                break;
                            case 28:
                                t.setDay28(attendanceValue);
                                break;
                            case 29:
                                t.setDay29(attendanceValue);
                                break;
                            case 30:
                                t.setDay30(attendanceValue);
                                break;
                            case 31:
                                t.setDay31(attendanceValue);
                                break;
                        }
                    }
                    teacherAttendancesList.add(t);
                }
            } else {
                System.out.println("fpru list empty");
            }
        } else {
            System.out.println("in else");
            TeacherMonthlyAttendance t = new TeacherMonthlyAttendance();

            teacherAttendancesList.clear();

            String get_teacher = "SELECT g FROM Teacher g WHERE g.teacherId='" + teacherId + "' AND g.schoolId.id='100' AND g.isActive='1'";
            List<Teacher> teacher = uni.searchByQuery(get_teacher);

            if (teacher.size() > 0) {
                String teacherName = teacher.get(0).getGeneralUserProfileId().getNameWithIn();
                t.setTeacherName(teacherName);
                t.setTeacherSignatureId(teacher.get(0).getTeacherId());

                SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
                SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                String year = sdfYear.format(selectedMonth);
                String month = sdfMonth.format(selectedMonth);

                for (String dateValue : monthDates) {
                    System.out.println("dateValue :" + dateValue);
                    String get_teacher_attendance = "SELECT g FROM TeacherAttendance g WHERE g.date= '" + dateValue + "' AND g.teacherId.id = '" + teacher.get(0).getId() + "' ";
                    List<TeacherAttendance> teacherAttendanceLists = uni.searchByQuery(get_teacher_attendance);

                    String attendanceValue = " - ";
                    System.out.println("teacherAttendanceLists :" + teacherAttendanceLists.size());
                    if (teacherAttendanceLists.size() > 0) {
                        TeacherAttendance obj = teacherAttendanceLists.get(0);
                        String inTime = obj.getIn_time() != null ? timeFormat.format(obj.getIn_time()) : "N/A";
                        String outTime = obj.getOut_time() != null ? timeFormat.format(obj.getOut_time()) : "N/A";
                        attendanceValue = inTime + " - " + outTime;
                    }

                    switch (Integer.parseInt(dateValue.substring(8, 10))) {
                        case 1:
                            t.setDay01(attendanceValue);
                            break;
                        case 2:
                            t.setDay02(attendanceValue);
                            break;
                        case 3:
                            t.setDay03(attendanceValue);
                            break;
                        case 4:
                            t.setDay04(attendanceValue);
                            break;
                        case 5:
                            t.setDay05(attendanceValue);
                            break;
                        case 6:
                            t.setDay06(attendanceValue);
                            break;
                        case 7:
                            t.setDay07(attendanceValue);
                            break;
                        case 8:
                            t.setDay08(attendanceValue);
                            break;
                        case 9:
                            t.setDay09(attendanceValue);
                            break;
                        case 10:
                            t.setDay10(attendanceValue);
                            break;
                        case 11:
                            t.setDay11(attendanceValue);
                            break;
                        case 12:
                            t.setDay12(attendanceValue);
                            break;
                        case 13:
                            t.setDay13(attendanceValue);
                            break;
                        case 14:
                            t.setDay14(attendanceValue);
                            break;
                        case 15:
                            t.setDay15(attendanceValue);
                            break;
                        case 16:
                            t.setDay16(attendanceValue);
                            break;
                        case 17:
                            t.setDay17(attendanceValue);
                            break;
                        case 18:
                            t.setDay18(attendanceValue);
                            break;
                        case 19:
                            t.setDay19(attendanceValue);
                            break;
                        case 20:
                            t.setDay20(attendanceValue);
                            break;
                        case 21:
                            t.setDay21(attendanceValue);
                            break;
                        case 22:
                            t.setDay22(attendanceValue);
                            break;
                        case 23:
                            t.setDay23(attendanceValue);
                            break;
                        case 24:
                            t.setDay24(attendanceValue);
                            break;
                        case 25:
                            t.setDay25(attendanceValue);
                            break;
                        case 26:
                            t.setDay26(attendanceValue);
                            break;
                        case 27:
                            t.setDay27(attendanceValue);
                            break;
                        case 28:
                            t.setDay28(attendanceValue);
                            break;
                        case 29:
                            t.setDay29(attendanceValue);
                            break;
                        case 30:
                            t.setDay30(attendanceValue);
                            break;
                        case 31:
                            t.setDay31(attendanceValue);
                            break;
                    }
                }
                teacherAttendancesList.add(t);
                setTeacherId("");
                loadTeacherTypes();
            } else {
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selected Teacher is not found", "");
                FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            }
        }

    }

    public List<TeacherMonthlyAttendance> selectTeacherType() throws IOException {

        teacherAttendancesList.clear();

        selectCalendarDate();
        initializeDayStyles(selectedMonth);

        System.out.println("teacher type " + teacherType);
        String get_teacher = "SELECT g FROM Teacher g WHERE g.teacherTypeId.id='" + teacherType + "' AND g.schoolId.id='100' AND g.isActive='1'";
        List<Teacher> teachers = uni.searchByQuery(get_teacher);

        if (teachers.size() > 0) {
            System.out.println("teacher size " + teachers.size());

            String get_finger_print_users = "SELECT g FROM FingerPrintRegionUser g WHERE g.isActive='1'";
            List<FingerPrintRegionUser> fpruList = uni.searchByQuery(get_finger_print_users);

            for (Teacher teacher : teachers) {
                for (FingerPrintRegionUser fingerPrintRegionUser : fpruList) {
                    System.out.println("in teacher loop");
                    System.out.println("fpru" + fingerPrintRegionUser.getGeneralUserProfileGupId().getId());
                    System.out.println("teacherobj " + teacher.getGeneralUserProfileId().getId());
                    if (teacher.getGeneralUserProfileId().getId().equals(fingerPrintRegionUser.getGeneralUserProfileGupId().getId())) {
                        TeacherMonthlyAttendance t = new TeacherMonthlyAttendance();

                        String teacherName = teacher.getGeneralUserProfileId().getNameWithIn();
                        t.setTeacherName(teacherName);
                        t.setTeacherSignatureId(teacher.getTeacherId());

                        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
                        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                        String year = sdfYear.format(selectedMonth);
                        String month = sdfMonth.format(selectedMonth);

                        for (String dateValue : monthDates) {
                            String get_teacher_attendance = "SELECT g FROM TeacherAttendance g WHERE g.date= '" + dateValue + "' AND g.teacherId.id = '" + teacher.getId() + "' ";
                            List<TeacherAttendance> teacherAttendanceLists = uni.searchByQuery(get_teacher_attendance);

                            String attendanceValue = "-";
                            if (teacherAttendanceLists.size() > 0) {
                                TeacherAttendance obj = teacherAttendanceLists.get(0);
                                String inTime = obj.getIn_time() != null ? timeFormat.format(obj.getIn_time()) : "N/A";
                                String outTime = obj.getOut_time() != null ? timeFormat.format(obj.getOut_time()) : "N/A";
                                attendanceValue = inTime + " - " + outTime;
                            }

                            switch (Integer.parseInt(dateValue.substring(8, 10))) {
                                case 1:
                                    t.setDay01(attendanceValue);
                                    break;
                                case 2:
                                    t.setDay02(attendanceValue);
                                    break;
                                case 3:
                                    t.setDay03(attendanceValue);
                                    break;
                                case 4:
                                    t.setDay04(attendanceValue);
                                    break;
                                case 5:
                                    t.setDay05(attendanceValue);
                                    break;
                                case 6:
                                    t.setDay06(attendanceValue);
                                    break;
                                case 7:
                                    t.setDay07(attendanceValue);
                                    break;
                                case 8:
                                    t.setDay08(attendanceValue);
                                    break;
                                case 9:
                                    t.setDay09(attendanceValue);
                                    break;
                                case 10:
                                    t.setDay10(attendanceValue);
                                    break;
                                case 11:
                                    t.setDay11(attendanceValue);
                                    break;
                                case 12:
                                    t.setDay12(attendanceValue);
                                    break;
                                case 13:
                                    t.setDay13(attendanceValue);
                                    break;
                                case 14:
                                    t.setDay14(attendanceValue);
                                    break;
                                case 15:
                                    t.setDay15(attendanceValue);
                                    break;
                                case 16:
                                    t.setDay16(attendanceValue);
                                    break;
                                case 17:
                                    t.setDay17(attendanceValue);
                                    break;
                                case 18:
                                    t.setDay18(attendanceValue);
                                    break;
                                case 19:
                                    t.setDay19(attendanceValue);
                                    break;
                                case 20:
                                    t.setDay20(attendanceValue);
                                    break;
                                case 21:
                                    t.setDay21(attendanceValue);
                                    break;
                                case 22:
                                    t.setDay22(attendanceValue);
                                    break;
                                case 23:
                                    t.setDay23(attendanceValue);
                                    break;
                                case 24:
                                    t.setDay24(attendanceValue);
                                    break;
                                case 25:
                                    t.setDay25(attendanceValue);
                                    break;
                                case 26:
                                    t.setDay26(attendanceValue);
                                    break;
                                case 27:
                                    t.setDay27(attendanceValue);
                                    break;
                                case 28:
                                    t.setDay28(attendanceValue);
                                    break;
                                case 29:
                                    t.setDay29(attendanceValue);
                                    break;
                                case 30:
                                    t.setDay30(attendanceValue);
                                    break;
                                case 31:
                                    t.setDay31(attendanceValue);
                                    break;
                            }
                        }
                        teacherAttendancesList.add(t);
                    }
                }
            }

        } else {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selected Teacher Type does not have teachers to view", "");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
        loadedAttendances = teacherAttendancesList;
        setTeacherId("");
        loadTeacherTypes();
//        System.out.println(teacherAttendancesList);
        return teacherAttendancesList;
    }

    public void initializeDayStyles(Date selectedMonth) {
        dayStyles.clear();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 1; i <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i);
            Date date = calendar.getTime();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            String styleClass = "";
            if (dayOfWeek == Calendar.SATURDAY) {
                styleClass = "saturday";
            } else if (dayOfWeek == Calendar.SUNDAY) {
                styleClass = "sunday";
            }
            dayStyles.put(i, styleClass);
        }
    }

    public String getDayStyleClass(int day) {
        return dayStyles.getOrDefault(day, "");
    }

    public void generateMonthDates(Date date) {

        getMonthDates().clear();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Move to the start of the month
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        // Formatter to convert date to String
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");

        // Loop through the month
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int day = 1; day <= maxDay; day++) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
            getMonthDates().add(sdf.format(calendar.getTime()));
        }
    }

    public void selectCalendarDate() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(selectedMonth);
        setPreviousMonthFormatted(formattedDate);
        generateMonthDates(selectedMonth);
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

    private byte[] generateExcelFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public class TeacherMonthlyAttendance {

        private String teacherSignatureId;
        private String teacherName;
        private String day01;
        private String day02;
        private String day03;
        private String day04;
        private String day05;
        private String day06;
        private String day07;
        private String day08;
        private String day09;
        private String day10;
        private String day11;
        private String day12;
        private String day13;
        private String day14;
        private String day15;
        private String day16;
        private String day17;
        private String day18;
        private String day19;
        private String day20;
        private String day21;
        private String day22;
        private String day23;
        private String day24;
        private String day25;
        private String day26;
        private String day27;
        private String day28;
        private String day29;
        private String day30;
        private String day31;

        // Getter and setter methods for all properties
        public String getTeacherSignatureId() {
            return teacherSignatureId;
        }

        public void setTeacherSignatureId(String teacherSignatureId) {
            this.teacherSignatureId = teacherSignatureId;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public String getDay01() {
            return day01;
        }

        public void setDay01(String day01) {
            this.day01 = day01;
        }

        public String getDay02() {
            return day02;
        }

        public void setDay02(String day02) {
            this.day02 = day02;
        }

        public String getDay03() {
            return day03;
        }

        public void setDay03(String day03) {
            this.day03 = day03;
        }

        public String getDay04() {
            return day04;
        }

        public void setDay04(String day04) {
            this.day04 = day04;
        }

        public String getDay05() {
            return day05;
        }

        public void setDay05(String day05) {
            this.day05 = day05;
        }

        public String getDay06() {
            return day06;
        }

        public void setDay06(String day06) {
            this.day06 = day06;
        }

        public String getDay07() {
            return day07;
        }

        public void setDay07(String day07) {
            this.day07 = day07;
        }

        public String getDay08() {
            return day08;
        }

        public void setDay08(String day08) {
            this.day08 = day08;
        }

        public String getDay09() {
            return day09;
        }

        public void setDay09(String day09) {
            this.day09 = day09;
        }

        public String getDay10() {
            return day10;
        }

        public void setDay10(String day10) {
            this.day10 = day10;
        }

        public String getDay11() {
            return day11;
        }

        public void setDay11(String day11) {
            this.day11 = day11;
        }

        public String getDay12() {
            return day12;
        }

        public void setDay12(String day12) {
            this.day12 = day12;
        }

        public String getDay13() {
            return day13;
        }

        public void setDay13(String day13) {
            this.day13 = day13;
        }

        public String getDay14() {
            return day14;
        }

        public void setDay14(String day14) {
            this.day14 = day14;
        }

        public String getDay15() {
            return day15;
        }

        public void setDay15(String day15) {
            this.day15 = day15;
        }

        public String getDay16() {
            return day16;
        }

        public void setDay16(String day16) {
            this.day16 = day16;
        }

        public String getDay17() {
            return day17;
        }

        public void setDay17(String day17) {
            this.day17 = day17;
        }

        public String getDay18() {
            return day18;
        }

        public void setDay18(String day18) {
            this.day18 = day18;
        }

        public String getDay19() {
            return day19;
        }

        public void setDay19(String day19) {
            this.day19 = day19;
        }

        public String getDay20() {
            return day20;
        }

        public void setDay20(String day20) {
            this.day20 = day20;
        }

        public String getDay21() {
            return day21;
        }

        public void setDay21(String day21) {
            this.day21 = day21;
        }

        public String getDay22() {
            return day22;
        }

        public void setDay22(String day22) {
            this.day22 = day22;
        }

        public String getDay23() {
            return day23;
        }

        public void setDay23(String day23) {
            this.day23 = day23;
        }

        public String getDay24() {
            return day24;
        }

        public void setDay24(String day24) {
            this.day24 = day24;
        }

        public String getDay25() {
            return day25;
        }

        public void setDay25(String day25) {
            this.day25 = day25;
        }

        public String getDay26() {
            return day26;
        }

        public void setDay26(String day26) {
            this.day26 = day26;
        }

        public String getDay27() {
            return day27;
        }

        public void setDay27(String day27) {
            this.day27 = day27;
        }

        public String getDay28() {
            return day28;
        }

        public void setDay28(String day28) {
            this.day28 = day28;
        }

        public String getDay29() {
            return day29;
        }

        public void setDay29(String day29) {
            this.day29 = day29;
        }

        public String getDay30() {
            return day30;
        }

        public void setDay30(String day30) {
            this.day30 = day30;
        }

        public String getDay31() {
            return day31;
        }

        public void setDay31(String day31) {
            this.day31 = day31;
        }
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public Map<Integer, String> getDayStyles() {
        return dayStyles;
    }

    public void setDayStyles(Map<Integer, String> dayStyles) {
        this.dayStyles = dayStyles;
    }

    public List<TeacherMonthlyAttendance> getTeacherAttendancesList() {
        return teacherAttendancesList;
    }

    public void setTeacherAttendancesList(List<TeacherMonthlyAttendance> teacherAttendancesList) {
        this.teacherAttendancesList = teacherAttendancesList;
    }

    public UniDBLocal getUni() {
        return uni;
    }

    public void setUni(UniDBLocal uni) {
        this.uni = uni;
    }

    public LoginSession getLs() {
        return ls;
    }

    public void setLs(LoginSession ls) {
        this.ls = ls;
    }

    public ComDev getComDiv() {
        return comDiv;
    }

    public void setComDiv(ComDev comDiv) {
        this.comDiv = comDiv;
    }

    public StoredProcedures getSp() {
        return sp;
    }

    public void setSp(StoredProcedures sp) {
        this.sp = sp;
    }

    public List<String> getMonthDates() {
        return monthDates;
    }

    public void setMonthDates(List<String> monthDates) {
        this.monthDates = monthDates;
    }

    public String getTeacherType() {
        return teacherType;
    }

    public void setTeacherType(String teacherType) {
        this.teacherType = teacherType;
    }

    public List<SelectItem> getTeacherTypeList() {
        return teacherTypeList;
    }

    public void setTeacherTypeList(List<SelectItem> teacherTypeList) {
        this.teacherTypeList = teacherTypeList;
    }

    public String getPreviousMonthFormatted() {
        return previousMonthFormatted;
    }

    public void setPreviousMonthFormatted(String previousMonthFormatted) {
        this.previousMonthFormatted = previousMonthFormatted;
    }

    public Date getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(Date selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getExcelData() {
        return excelData;
    }

    public StreamedContent getFile() {
        return file;
    }

    public static List<TeacherMonthlyAttendance> getLoadedAttendances() {
        return loadedAttendances;
    }

    public static void setLoadedAttendances(List<TeacherMonthlyAttendance> loadedAttendances) {
        TeachersMonthlyAttendanceOverviewReport.loadedAttendances = loadedAttendances;
    }

    public File genarateExel(List<TeacherMonthlyAttendance> attendanceList) throws IOException {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Attendance");
            sheet.autoSizeColumn(100);
            Row headerRow = sheet.createRow(0);
            headerRow.setHeight((short) 500);
            Cell headerCell = headerRow.createCell(0);
            headerCell.setCellValue("TEACHERS MONTHLY ATTENDANCE OVERVIEW REPORT OF ANANDA COLLEGE");

           
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
//            headerCellStyle.setFont(font);
//            headerCellStyle.getFillBackgroundColorColor();
            headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font headerFont = workbook.createFont();
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerFont.setBold(true);
            headerCellStyle.setFont(headerFont);
            headerCell.setCellStyle(headerCellStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 33));
            Row row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue("#");
            row1.createCell(1).setCellValue("TID");
            row1.createCell(2).setCellValue("Name");
            sheet.setColumnWidth(2, 256 * 25);

            for (int i = 1; i <= 31; i++) {

                row1.createCell(i + 2).setCellValue(String.format("%02d", i));
                sheet.setColumnWidth(i + 2, 256 * 15);

            }

            List<TeacherMonthlyAttendance> attendance = getLoadedAttendances();
            int RowNum = 2;

            for (TeacherMonthlyAttendance atendance : attendance) {
                RowNum = RowNum++;

                Row row = sheet.createRow(RowNum++);

//                for (int j = 0; j < 10; j++) {
//                    row.createCell(0).setCellValue(j);
//                }
                row.createCell(0).setCellValue(RowNum - 2);

                row.createCell(1).setCellValue(atendance.teacherSignatureId);
                row.createCell(2).setCellValue(atendance.teacherName);
                row.createCell(3).setCellValue(atendance.day01);
                row.createCell(4).setCellValue(atendance.day02);
                row.createCell(5).setCellValue(atendance.day03);
                row.createCell(6).setCellValue(atendance.day04);
                row.createCell(7).setCellValue(atendance.day05);
                row.createCell(8).setCellValue(atendance.day06);
                row.createCell(9).setCellValue(atendance.day07);
                row.createCell(10).setCellValue(atendance.day08);
                row.createCell(11).setCellValue(atendance.day09);
                row.createCell(12).setCellValue(atendance.day10);
                row.createCell(13).setCellValue(atendance.day11);
                row.createCell(14).setCellValue(atendance.day12);
                row.createCell(15).setCellValue(atendance.day13);
                row.createCell(16).setCellValue(atendance.day14);
                row.createCell(17).setCellValue(atendance.day15);
                row.createCell(18).setCellValue(atendance.day16);
                row.createCell(19).setCellValue(atendance.day17);
                row.createCell(20).setCellValue(atendance.day18);
                row.createCell(21).setCellValue(atendance.day19);
                row.createCell(22).setCellValue(atendance.day20);
                row.createCell(23).setCellValue(atendance.day21);
                row.createCell(24).setCellValue(atendance.day22);
                row.createCell(25).setCellValue(atendance.day23);
                row.createCell(26).setCellValue(atendance.day24);
                row.createCell(27).setCellValue(atendance.day25);
                row.createCell(28).setCellValue(atendance.day26);
                row.createCell(29).setCellValue(atendance.day27);
                row.createCell(30).setCellValue(atendance.day28);
                row.createCell(31).setCellValue(atendance.day29);
                row.createCell(32).setCellValue(atendance.day30);
                row.createCell(33).setCellValue(atendance.day31);
            }
            File tempFile = File.createTempFile("attendance_report", ".xlsx");
            tempFile.deleteOnExit();
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                workbook.write(fos);
            }
            workbook.close();
            System.out.println("Excel file created successfully");
            System.out.println(tempFile);
            return tempFile;
        } catch (Exception e) {
            printStackTrace(e);
        }

        return null;
    }

    public String sendEmailWithAtt() {
        System.out.println("aa");
        FacesMessage msg = null;

        try {
            String to = email;
            List<TeacherMonthlyAttendance> attendanceList = getTeacherAttendancesList();
            File excelFile = genarateExel(attendanceList);

            byte[] excelFileBytes = Files.readAllBytes(excelFile.toPath());
            String attachmentFileName = "attendance_report.xlsx";

            mailsend.Send("noreply@srilankasoftwarevalley.lk", "SLsvnorep@jiat2022", to, "damiduprasadjayarathna@gmail.com", "teachers attendance list", "", excelFileBytes, attachmentFileName);

            System.out.println("Email sent successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }

        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Email sent Succesfully", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        return null;
    }

}
