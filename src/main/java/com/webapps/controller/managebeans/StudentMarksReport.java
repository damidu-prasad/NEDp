/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.managebeans;

import com.ejb.model.common.ComLib;
import com.ejb.model.common.ComPath;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.EducationDivision;
import com.ejb.model.entity.EducationZone;
import com.ejb.model.entity.GradeClassHasSubjects;
import com.ejb.model.entity.GradeClassStream;
import com.ejb.model.entity.GradeClassStreamManager;
import com.ejb.model.entity.GradeClassStudents;
import com.ejb.model.entity.GradeClassStudentsHasSubjects;
import com.ejb.model.entity.GradeClassSubjectTeacher;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.Province;
import com.ejb.model.entity.School;
import com.ejb.model.entity.Streams;
import com.ejb.model.entity.StudentMarks;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.Terms;
import com.ejb.model.entity.Year;
import com.webapps.controller.utilities.SortArraysStudentMarksReport;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIData;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.component.api.UIColumn;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Thilini Madagama
 */
@ManagedBean(name = "studentMarksReport")
@ViewScoped

public class StudentMarksReport implements Serializable {

    private String provinceName = "0";
    private List<SelectItem> provinceNameList = new ArrayList<SelectItem>();

    private String zoneName = "0";
    private List<SelectItem> zoneNameList = new ArrayList<SelectItem>();

    private String divisionName = "0";
    private List<SelectItem> divisionNameList = new ArrayList<SelectItem>();

    private String schoolName = "0";
    private List<SelectItem> schoolNameList = new ArrayList<SelectItem>();

    private String year = "0";
    private List<SelectItem> YearList = new ArrayList<SelectItem>();

    private String className = "0";
    private List<SelectItem> classNameList = new ArrayList<SelectItem>();

    private String termName = "0";
    private List<SelectItem> termNameList = new ArrayList<SelectItem>();

    private boolean disabledFiledProvince = false;
    private boolean disabledFiledZone = false;
    private boolean disabledFiledDivision = false;
    private boolean disabledFiledSchool = false;

    private StreamedContent downloadExcel;

    private List<StudentList> studentList = new ArrayList<StudentList>();

    private int def_province = 0;
    private int def_zone = 0;
    private int def_division = 0;
    private int def_school = 0;

    HttpServletResponse response;
    HttpServletRequest request;

    private ComPath comPath;

    @EJB
    private UniDBLocal uni;
    LoginSession ls;

    private ComLib comlib;

    @PostConstruct
    public void init() {

        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        ls = (LoginSession) uni.find(Integer.parseInt(request.getSession().getAttribute("LS").toString()), LoginSession.class);

        loadSchoolDetails(Integer.parseInt(request.getSession().getAttribute("LS").toString()));
    }

    public void loadSchoolDetails(int ls_id) {

        LoginSession lss = (LoginSession) uni.find(ls_id, LoginSession.class);
        if (lss != null) {

            if (lss.getUserLoginGroupId().getGeneralOrganizationProfileId().getOrganizationTypeId().getId() == 4) { //Province

                List<Province> p = uni.searchByQuerySingle("SELECT im FROM Province im WHERE im.generalOrganizationProfileId.id='" + lss.getUserLoginGroupId().getGeneralOrganizationProfileId().getId() + "'  ");

                setDef_province(p.iterator().next().getId());

                disabledFiledProvince = true;

            } else if (lss.getUserLoginGroupId().getGeneralOrganizationProfileId().getOrganizationTypeId().getId() == 5) { //Zone

                List<EducationZone> p = uni.searchByQuerySingle("SELECT im FROM EducationZone im WHERE im.generalOrganizationProfileId.id='" + lss.getUserLoginGroupId().getGeneralOrganizationProfileId().getId() + "'  ");

                setDef_zone(p.iterator().next().getId());
                setDef_province(p.iterator().next().getProvinceId().getId());

                disabledFiledProvince = true;
                disabledFiledZone = true;

            } else if (lss.getUserLoginGroupId().getGeneralOrganizationProfileId().getOrganizationTypeId().getId() == 6) { //Division

                List<EducationDivision> p = uni.searchByQuerySingle("SELECT im FROM EducationDivision im WHERE im.generalOrganizationProfileId.id='" + lss.getUserLoginGroupId().getGeneralOrganizationProfileId().getId() + "'  ");

                setDef_division(p.iterator().next().getId());
                setDef_zone(p.iterator().next().getEducationZoneId().getId());
                setDef_province(p.iterator().next().getEducationZoneId().getProvinceId().getId());

                disabledFiledProvince = true;
                disabledFiledZone = true;
                disabledFiledDivision = true;

            } else if (lss.getUserLoginGroupId().getGeneralOrganizationProfileId().getOrganizationTypeId().getId() == 2) { //School

                List<School> p = uni.searchByQuerySingle("SELECT im FROM School im WHERE im.generalOrganizationProfileId.id='" + lss.getUserLoginGroupId().getGeneralOrganizationProfileId().getId() + "'  ");

                setDef_division(p.iterator().next().getEducationDivisionId().getId());
                setDef_zone(p.iterator().next().getEducationDivisionId().getEducationZoneId().getId());
                setDef_province(p.iterator().next().getEducationDivisionId().getEducationZoneId().getProvinceId().getId());
                setDef_school(p.iterator().next().getId());

                disabledFiledProvince = true;
                disabledFiledZone = true;
                disabledFiledDivision = true;
                disabledFiledSchool = true;

            }

        }
        loadTypes();
    }

    public void loadTypes() {

        // Get Province
        getProvinceNameList().add(new SelectItem("0", "Select"));

        String query = "SELECT g FROM Province g order by g.name ASC";
        List<Province> listAS = uni.searchByQuery(query);
        for (Province cc : listAS) {

            getProvinceNameList().add(new SelectItem(cc.getId(), cc.getName()));
        }
        setProvinceName(def_province + "");
        getEducationZoneList();

        String cur_year = comlib.GetCurrentYear() + "";
        // Get Year
        String queryy = "SELECT g FROM Year g  order by g.id ASC";
        List<Year> list_year = uni.searchByQuery(queryy);
        for (Year cc : list_year) {

            if (cc.getName().equals(cur_year)) {
                this.year = cc.getId() + "";
            }

            getYearList().add(new SelectItem(cc.getId(), cc.getName()));
        }

        // Get Term
        String queryt = "SELECT g FROM Terms g  order by g.id ASC";
        List<Terms> list_term = uni.searchByQuery(queryt);
        for (Terms cc : list_term) {

            getTermNameList().add(new SelectItem(cc.getId(), cc.getName()));
        }

    }

    public String getEducationZoneList() {

        getZoneNameList().clear();

        // Get Province
        getZoneNameList().add(new SelectItem("0", "Select"));

        String query = "SELECT g FROM EducationZone g where g.provinceId.id='" + Integer.parseInt(getProvinceName()) + "' order by g.name ASC";
        List<EducationZone> listAS = uni.searchByQuery(query);
        for (EducationZone cc : listAS) {

            getZoneNameList().add(new SelectItem(cc.getId(), cc.getName()));
        }
        setZoneName(def_zone + "");

        getEducationDivisionList();

        return null;

    }

    public String getEducationDivisionList() {

        getDivisionNameList().clear();

        // Get Province
        getDivisionNameList().add(new SelectItem("0", "Select"));

        String query = "SELECT g FROM EducationDivision g where g.educationZoneId.id='" + Integer.parseInt(getZoneName()) + "' order by g.name ASC";
        List<EducationDivision> listAS = uni.searchByQuery(query);
        for (EducationDivision cc : listAS) {

            getDivisionNameList().add(new SelectItem(cc.getId(), cc.getName()));
        }
        System.out.println("w1");

        System.out.println("w2");
        setDivisionName(def_division + "");
        getSchoolList();

        return null;
    }

    public String getSchoolList() {

        if (!divisionName.equals("0")) {

            getSchoolNameList().clear();

            // Get Province
            getSchoolNameList().add(new SelectItem("0", "Select"));

            String query = "SELECT g FROM School g where g.educationDivisionId.id='" + Integer.parseInt(getDivisionName()) + "' order by g.generalOrganizationProfileId.name ASC";
            List<School> listAS = uni.searchByQuery(query);
            for (School cc : listAS) {

                getSchoolNameList().add(new SelectItem(cc.getId(), cc.getGeneralOrganizationProfileId().getName()));
            }
        }
        setSchoolName(def_school + "");

        getClasses();
        return null;
    }

    public String getClasses() {

        getClassNameList().clear();
        if (!schoolName.equals("")) {
            String query_al = "SELECT g FROM GradeClassStream g where g.schoolId.id='" + Integer.parseInt(schoolName) + "' and g.isActive='1' order by g.gradeId.id,g.classesId.name ASC ";
            List<GradeClassStream> listAS_al = uni.searchByQuery(query_al);
            for (GradeClassStream cc : listAS_al) {

                String name = cc.getGradeId().getName() + "-" + cc.getClassesId().getName();
                if (cc.getGradeId().getId() == 12 || cc.getGradeId().getId() == 13) {

                    name = cc.getGradeId().getName() + "-" + cc.getClassesId().getName() + " " + cc.getStreamsId().getName();
                }

                getClassNameList().add(new SelectItem(cc.getId(), name));

            }
        }

        return null;
    }
//     public UIData getDataTable() {
//        UIColumn col;
//        UIOutput out = null;
//        Application app = app = FacesContext.getCurrentInstance().getApplication();
//
//        int colCount = 3;
//
//        for (int j = 0; j < colCount; ++j) {
//            out = new UIOutput();
//            col = new UIColumn();
//            ValueBinding vb = app.createValueBinding("#{" + j + "}"); //just a simple expression with nothing difficult in it.
//            out.setValueBinding("value", vb);
//            out.setRendererType("Text");
//            col.getChildren().add(out);
//            dataTable.getChildren().add(col);
//        }
//
//        return dataTable;
//
//    }

    public void loadStudentMarksReport() {
        FacesMessage msg = null;

        if (schoolName.equals("0") || schoolName.equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select School !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else if (className.equals("0") || className.equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select Class !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else {

            studentList.clear();

            List<MarksList> header = new ArrayList();

            String queryt = "SELECT g FROM GradeClassStreamManager g where g.gradeClassStreamId.id='" + className + "' and g.yearId.id='" + year + "'";
            List<GradeClassStreamManager> listASt = uni.searchByQuerySingle(queryt);
            if (listASt.size() > 0) {

                int grade = listASt.iterator().next().getGradeClassStreamId().getGradeId().getId();

                header.add(new MarksList("#", "#ffad33", "center", "bold", ""));
                header.add(new MarksList("Index No", "#ffad33", "center", "bold", ""));
                header.add(new MarksList("Name", "#ffad33", "left", "bold", ""));

                String querysub = "SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.gradeClassStreamId.id='" + listASt.iterator().next().getGradeClassStreamId().getId() + "' and g.gradeClassStreamManagerId.yearId.id='" + year + "' and g.isActive='1' and  g.gradeClassHasSubjectsId.subjectsId.isActive='1' ";
                List<GradeClassSubjectTeacher> sublist = uni.searchByQuery(querysub);
                if (sublist.size() > 0) {
                    for (GradeClassSubjectTeacher gchs : sublist) {
                        header.add(new MarksList(gchs.getGradeClassHasSubjectsId().getSubjectsId().getName(), "#ffad33", "center", "bold", "rotated"));
                    }
                }
                if (!(grade == 1 || grade == 2)) {
                    header.add(new MarksList("Total Marks", "#ffad33", "center", "bold", ""));
                    header.add(new MarksList("AVG", "#ffad33", "center", "bold", ""));
                    header.add(new MarksList("Position", "#ffad33", "center", "bold", ""));
                }
                studentList.add(new StudentList(header));

                if (sublist.size() > 0) {

                    // order start
                    List<StudentOrderList> sol = new ArrayList();

                    String queryc = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.id='" + listASt.iterator().next().getId() + "' and g.isRemoved='0' order by g.studentsId.generalUserProfileId.nameWithIn ASC";
                    List<GradeClassStudents> listc = uni.searchByQuery(queryc);
                    if (listc.size() > 0) {

                        for (GradeClassStudents gcs : listc) {

                            double tot_marks = 0;
                            double avg = 0;

                            String queryf = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.gradeClassHasSubjectsId.subjectsId.isActive='1' ";
                            List<GradeClassStudentsHasSubjects> listf = uni.searchByQuery(queryf);

                            for (GradeClassSubjectTeacher gchs : sublist) {
                                String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.id='" + gchs.getGradeClassHasSubjectsId().getId() + "' and g.isRemoved='0' ";
                                List<StudentMarks> listm = uni.searchByQuerySingle(querym);
                                if (listm.size() > 0) {
                                    StudentMarks sm = listm.iterator().next();
                                    tot_marks += sm.getMarks();
                                }
                            }
                            if (listf.size() > 0) {

                                avg = tot_marks / listf.size();
                            }
                            sol.add(new StudentOrderList(gcs, tot_marks, avg, 1));
                        }

                    }
                    StudentOrderList[] addressArray = new StudentOrderList[sol.size()];
                    for (int i = 0; i < sol.size(); i++) {

                        addressArray[i] = sol.get(i);
                    }

                    StudentOrderList[] s = SortArraysStudentMarksReport.GetArray(addressArray);

                    List<StudentOrderList> sol_ordered_list = new ArrayList();

                    int order = 0;
                    double lasttotal = 0;
                    int stuc = 1;
                    for (int i = (s.length - 1); i >= 0; i--) {

                        if (s[i].getTotal() != lasttotal) {

                            order = stuc;
                            lasttotal = s[i].getTotal();
                        }
                        System.out.println("order "+order);
                        sol_ordered_list.add(new StudentOrderList(s[i].getGcs(), s[i].getTotal(), s[i].getAvg(), order));
                        stuc++;
                    }

                    // order over
                    int i = 1;

                    for (StudentOrderList so : sol_ordered_list) {

                        List<MarksList> ar = new ArrayList();
                        ar.add(new MarksList(i + "", "white", "center", "normal", ""));
                        ar.add(new MarksList(so.getGcs().getStudentsId().getStudentId(), "white", "center", "normal", ""));
                        ar.add(new MarksList(so.getGcs().getStudentsId().getGeneralUserProfileId().getNameWithIn(), "white", "left", "normal", ""));

                        double tot_marks = 0;
                        double avg = 0;

                        String queryf = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassStudentsId.id='" + so.getGcs().getId() + "' and g.gradeClassHasSubjectsId.subjectsId.isActive='1' ";
                        List<GradeClassStudentsHasSubjects> listf = uni.searchByQuery(queryf);

                        for (GradeClassSubjectTeacher gchs : sublist) {
                            String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + so.getGcs().getId() + "' and g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.id='" + gchs.getGradeClassHasSubjectsId().getId() + "' and g.isRemoved='0' ";
                            List<StudentMarks> listm = uni.searchByQuerySingle(querym);
                            if (listm.size() > 0) {
                                StudentMarks sm = listm.iterator().next();

                                String marks = "";
                                String color = "white";
                                if (sm.getIsPresent() == false) {

                                    marks = "ab";
                                    color = "#FFAB91";
                                    tot_marks += 0;
                                } else if (sm.getIsMandatory() == false) {
                                    marks = "";
                                    color = "#cacaca";
                                } else {
                                    tot_marks += sm.getMarks();

                                    if (grade == 1 || grade == 2) {

                                        marks = comlib.GetValueByMark(sm.getMarks());
                                    } else {
                                        marks = sm.getMarks().intValue() + "";
                                    }

                                }

                                ar.add(new MarksList(marks, color, "center", "normal", ""));

                            } else {

                                ar.add(new MarksList("", "white", "center", "normal", ""));

                            }
                        }
                        if (!(grade == 1 || grade == 2)) {
                            ar.add(new MarksList(comlib.getDouble(tot_marks) + "", "white", "center", "bold", ""));

                            if (listf.size() > 0) {

                                avg = tot_marks / listf.size();
                            }
                            ar.add(new MarksList(comlib.getDouble(avg) + "", "white", "center", "bold", ""));
                            ar.add(new MarksList(so.getOrder() + "", "white", "center", "bold", ""));
                        }
                        studentList.add(new StudentList(ar));

                        i++;

                    }

                }
            }
        }
    }

    public void lOBExport2Excel() {

        FacesMessage msg = null;

        if (schoolName.equals("0") || schoolName.equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select School !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else if (className.equals("0") || className.equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select Class !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else {

            try {
                String schoolname = "";
                String divisionname = "";
                String gradename = "";
                String yearname = "";
                String termname = "";

                School sc = (School) uni.find(Integer.parseInt(schoolName), School.class);
                schoolname = sc.getGeneralOrganizationProfileId().getName();
                divisionname = sc.getEducationDivisionId().getName();

                GradeClassStream gsc = (GradeClassStream) uni.find(Integer.parseInt(className), GradeClassStream.class);
                gradename = gsc.getGradeId().getName() + "-" + gsc.getClassesId().getName();

                Year y = (Year) uni.find(Integer.parseInt(year), Year.class);
                yearname = y.getName();

                Terms term = (Terms) uni.find(Integer.parseInt(termName), Terms.class);
                termname = term.getName();

                String excel_name = "";
                excel_name = "Student Marks of " + yearname + " " + termname + " (" + gradename + ") Class .xlsx";

                XSSFWorkbook workbook = new XSSFWorkbook();

                XSSFSheet sheet = workbook.createSheet();

                XSSFFont font_bold = workbook.createFont();
                font_bold.setBold(true);
                CellStyle style_bold = sheet.getWorkbook().createCellStyle();
                style_bold.setFont(font_bold);

                Row row1 = sheet.createRow(0);

                Cell cell0 = row1.createCell(1);
                cell0.setCellStyle(style_bold);
                cell0.setCellValue("Student Exam Marks");

                Row row2 = sheet.createRow(1);

                Cell cell2 = row2.createCell(1);
                cell2.setCellStyle(style_bold);
                cell2.setCellValue("Year : " + yearname + " " + termname);

                Row row3 = sheet.createRow(2);

                Cell cell3 = row3.createCell(1);
                cell3.setCellStyle(style_bold);
                cell3.setCellValue("School : " + schoolname);

                Row row4 = sheet.createRow(3);

                Cell cell4 = row4.createCell(1);
                cell4.setCellStyle(style_bold);
                cell4.setCellValue("Division : " + divisionname);

                Row row5 = sheet.createRow(4);

                Cell cell5 = row5.createCell(1);
                cell5.setCellStyle(style_bold);
                cell5.setCellValue("Grade : " + gradename);

                String queryt = "SELECT g FROM GradeClassStreamManager g where g.gradeClassStreamId.id='" + className + "' and g.yearId.id='" + year + "'";
                List<GradeClassStreamManager> listASt = uni.searchByQuerySingle(queryt);
                if (listASt.size() > 0) {

                    Row row6 = sheet.createRow(5);

                    Cell cell6_0 = row6.createCell(0);
                    cell6_0.setCellStyle(style_bold);
                    cell6_0.setCellValue("No");

                    Cell cell6_1 = row6.createCell(1);
                    cell6_1.setCellStyle(style_bold);
                    cell6_1.setCellValue("Index No");

                    Cell cell6_2 = row6.createCell(2);
                    cell6_2.setCellStyle(style_bold);
                    cell6_2.setCellValue("Name of the Students");

//
                    String querysub = "SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.gradeClassStreamId.id='" + listASt.iterator().next().getGradeClassStreamId().getId() + "' and g.gradeClassStreamManagerId.yearId.id='" + year + "' and g.isActive='1' and  g.gradeClassHasSubjectsId.subjectsId.isActive='1' ";
                    List<GradeClassSubjectTeacher> sublist = uni.searchByQuery(querysub);
                    if (sublist.size() > 0) {
                        int k = 3;
                        for (GradeClassSubjectTeacher gchs : sublist) {
//                        header.add(new MarksList(gchs.getGradeClassHasSubjectsId().getSubjectsId().getName(), "#ffad33", "center", "bold"));

                            Cell cellk = row6.createCell(k);
                            cellk.setCellStyle(style_bold);
                            cellk.setCellValue(gchs.getGradeClassHasSubjectsId().getSubjectsId().getName());
                            k++;

                        }
                    }

                    if (sublist.size() > 0) {
                        int i = 1;
                        String query = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.id='" + listASt.iterator().next().getId() + "' and g.isRemoved='0' order by g.studentsId.generalUserProfileId.nameWithIn ASC";
                        List<GradeClassStudents> list = uni.searchByQuery(query);
                        if (list.size() > 0) {

                            int st_row = 6;

                            for (GradeClassStudents gcs : list) {
//                            List<MarksList> ar = new ArrayList();
//                            ar.add(new MarksList(i + "", "white", "center", "normal"));
//                            ar.add(new MarksList(gcs.getStudentsId().getStudentId(), "white", "center", "normal"));
//                            ar.add(new MarksList(gcs.getStudentsId().getGeneralUserProfileId().getNameWithIn(), "white", "left", "normal"));
                                Row row7 = sheet.createRow(st_row);

                                Cell cellk1 = row7.createCell(0);
                                cellk1.setCellValue(i);

                                Cell cellk2 = row7.createCell(1);
                                cellk2.setCellType(CellType.NUMERIC);
                                cellk2.setCellValue(gcs.getStudentsId().getStudentId());

                                Cell cellk3 = row7.createCell(2);
                                cellk3.setCellValue(gcs.getStudentsId().getGeneralUserProfileId().getNameWithIn());

                                int st_colom = 3;

                                for (GradeClassSubjectTeacher gchs : sublist) {
                                    String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.id='" + gchs.getGradeClassHasSubjectsId().getId() + "' and g.isRemoved='0' ";
                                    List<StudentMarks> listm = uni.searchByQuerySingle(querym);
                                    String marks = "";
                                    Cell cellkk = row7.createCell(st_colom);
                                    cellkk.setCellType(CellType.NUMERIC);

                                    if (listm.size() > 0) {
                                        StudentMarks sm = listm.iterator().next();

                                        if (sm.getIsPresent() == false) {

                                            marks = "ab";
                                            cellkk.setCellValue("ab");

                                        } else if (sm.getIsMandatory() == false) {
                                            marks = "";
                                            cellkk.setCellValue("");

                                        } else {

                                            if (gsc.getGradeId().getId() == 1 || gsc.getGradeId().getId() == 2) {

                                                marks = comlib.GetValueByMark(sm.getMarks());
                                            } else {
                                                marks = sm.getMarks().intValue() + "";
                                            }

                                            cellkk.setCellValue(sm.getMarks().intValue());
                                        }

                                    } else {

                                        marks = "";
                                        cellkk.setCellValue("");
                                    }

                                    st_colom++;

                                }

                                i++;
                                st_row++;
                            }
                        }
                    }
                }

                FileOutputStream fos = new FileOutputStream(excel_name);
                workbook.write(fos);
                fos.flush();
                fos.close();

                InputStream stream = new BufferedInputStream(new FileInputStream(excel_name));
                downloadExcel = new DefaultStreamedContent(stream, "application/xls", excel_name);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public class StudentOrderList {

        private GradeClassStudents gcs;
        private double total;
        private double avg;
        private int order;

        public StudentOrderList(GradeClassStudents gcs, double total, double avg, int order) {
            this.gcs = gcs;
            this.total = total;
            this.avg = avg;
            this.order = order;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public double getAvg() {
            return avg;
        }

        public void setAvg(double avg) {
            this.avg = avg;
        }

        public GradeClassStudents getGcs() {
            return gcs;
        }

        public void setGcs(GradeClassStudents gcs) {
            this.gcs = gcs;
        }
    }

    public class StudentList {

        private List<MarksList> name;

        public StudentList(List<MarksList> name) {
            this.name = name;
        }

        public List<MarksList> getName() {
            return name;
        }

        public void setName(List<MarksList> name) {
            this.name = name;
        }

    }

    public class MarksList {

        private String marks;
        private String color;
        private String align;
        private String weight;
        private String horalignment;

        public MarksList(String marks, String color, String align, String weight, String horalignment) {
            this.marks = marks;
            this.color = color;
            this.align = align;
            this.weight = weight;
            this.horalignment = horalignment;
        }

        public String getMarks() {
            return marks;
        }

        public void setMarks(String marks) {
            this.marks = marks;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getAlign() {
            return align;
        }

        public void setAlign(String align) {
            this.align = align;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getHoralignment() {
            return horalignment;
        }

        public void setHoralignment(String horalignment) {
            this.horalignment = horalignment;
        }

    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public List<SelectItem> getProvinceNameList() {
        return provinceNameList;
    }

    public void setProvinceNameList(List<SelectItem> provinceNameList) {
        this.provinceNameList = provinceNameList;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public List<SelectItem> getZoneNameList() {
        return zoneNameList;
    }

    public void setZoneNameList(List<SelectItem> zoneNameList) {
        this.zoneNameList = zoneNameList;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public List<SelectItem> getDivisionNameList() {
        return divisionNameList;
    }

    public void setDivisionNameList(List<SelectItem> divisionNameList) {
        this.divisionNameList = divisionNameList;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public List<SelectItem> getSchoolNameList() {
        return schoolNameList;
    }

    public void setSchoolNameList(List<SelectItem> schoolNameList) {
        this.schoolNameList = schoolNameList;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<SelectItem> getYearList() {
        return YearList;
    }

    public void setYearList(List<SelectItem> YearList) {
        this.YearList = YearList;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public List<SelectItem> getTermNameList() {
        return termNameList;
    }

    public void setTermNameList(List<SelectItem> termNameList) {
        this.termNameList = termNameList;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<SelectItem> getClassNameList() {
        return classNameList;
    }

    public void setClassNameList(List<SelectItem> classNameList) {
        this.classNameList = classNameList;
    }

    public List<StudentList> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<StudentList> studentList) {
        this.studentList = studentList;
    }

    public int getDef_province() {
        return def_province;
    }

    public void setDef_province(int def_province) {
        this.def_province = def_province;
    }

    public int getDef_zone() {
        return def_zone;
    }

    public void setDef_zone(int def_zone) {
        this.def_zone = def_zone;
    }

    public int getDef_division() {
        return def_division;
    }

    public void setDef_division(int def_division) {
        this.def_division = def_division;
    }

    public int getDef_school() {
        return def_school;
    }

    public void setDef_school(int def_school) {
        this.def_school = def_school;
    }

    public StreamedContent getDownloadExcel() {
        return downloadExcel;
    }

    public void setDownloadExcel(StreamedContent downloadExcel) {
        this.downloadExcel = downloadExcel;
    }

    public boolean isDisabledFiledProvince() {
        return disabledFiledProvince;
    }

    public void setDisabledFiledProvince(boolean disabledFiledProvince) {
        this.disabledFiledProvince = disabledFiledProvince;
    }

    public boolean isDisabledFiledZone() {
        return disabledFiledZone;
    }

    public void setDisabledFiledZone(boolean disabledFiledZone) {
        this.disabledFiledZone = disabledFiledZone;
    }

    public boolean isDisabledFiledDivision() {
        return disabledFiledDivision;
    }

    public void setDisabledFiledDivision(boolean disabledFiledDivision) {
        this.disabledFiledDivision = disabledFiledDivision;
    }

    public boolean isDisabledFiledSchool() {
        return disabledFiledSchool;
    }

    public void setDisabledFiledSchool(boolean disabledFiledSchool) {
        this.disabledFiledSchool = disabledFiledSchool;
    }

}
