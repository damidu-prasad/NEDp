/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.managebeans;

import com.ejb.model.common.ComDev;
import com.ejb.model.common.ComLib;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.Classes;
import com.ejb.model.entity.EducationDivision;
import com.ejb.model.entity.EducationLevel;
import com.ejb.model.entity.EducationZone;
import com.ejb.model.entity.Grade;
import com.ejb.model.entity.GradeClassHasSubjects;
import com.ejb.model.entity.GradeClassStream;
import com.ejb.model.entity.GradeClassStreamManager;
import com.ejb.model.entity.GradeClassStudents;
import com.ejb.model.entity.GradeClassSubjectTeacher;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.Province;
import com.ejb.model.entity.School;
import com.ejb.model.entity.Streams;
import com.ejb.model.entity.StudentMarks;
import com.ejb.model.entity.Subjects;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.Terms;
import com.ejb.model.entity.Year;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Thilini Madagama
 */
@ManagedBean
@ViewScoped
public class UploadMarks implements Serializable {

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

    private String excelyear = "0";
    private List<SelectItem> excelyearList = new ArrayList<SelectItem>();

    private String excelClass = "0";
    private List<SelectItem> excelClassList = new ArrayList<SelectItem>();

    private String termName = "0";
    private List<SelectItem> termNameList = new ArrayList<SelectItem>();

    private String streamName = "1";
    private List<SelectItem> streamNameList = new ArrayList<SelectItem>();

    private String gradeName = "1";
    private List<SelectItem> gradeNameList = new ArrayList<SelectItem>();

    private String classTeacherName = "1";
    private List<SelectItem> classTeacherNameList = new ArrayList<SelectItem>();

    private List<GradeClassSubjectTeacherList> gradeclassSubjectTeacherList = new ArrayList();

    private boolean disabledFiledProvince = false;
    private boolean disabledFiledZone = false;
    private boolean disabledFiledDivision = false;
    private boolean disabledFiledSchool = false;
    private String subjectNameTopic;

    private String sheetNo = "1";

    private String className = "";
    private String fileUploadedStatus = "";

    private List<UploadedFile> uploadedExcelList = new ArrayList<UploadedFile>();

    private List<LastStudentList> studentList = new ArrayList<LastStudentList>();

    private String lastYear;
    private String lastTerm;
    private String lastClass;

    private String subjectYear;
    private String subjectClass;

    private GradeClassStream gcs;
    private GradeClassStreamManager gcsm;
    private List<GradeClassHasSubjects> subjects = new ArrayList();

    private List<String> subList = new ArrayList();

    private Terms terms;

    private List<StudentList> stList = new ArrayList();

    private StreamedContent downloadExcel;

    private int def_province = 0;
    private int def_zone = 0;
    private int def_division = 0;
    private int def_school = 0;

    String colNames[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC"};

    HttpServletResponse response;
    HttpServletRequest request;

    @EJB
    private UniDBLocal uni;
    LoginSession ls;

    private ComLib comlib;

    @EJB
    private ComDev comDiv;

    @PostConstruct
    public void init() {

        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        ls = (LoginSession) uni.find(Integer.parseInt(request.getSession().getAttribute("LS").toString()), LoginSession.class);

//        InputStream stream_file_1_5_sinhala = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/demo/download/1-5_Sinhala_Medium.xlsx");
//        file_1_5_sinhala = new DefaultStreamedContent(stream_file_1_5_sinhala, "application/vnd.ms-excel", "1-5_Sinhala_Medium.xlsx");
//
//        InputStream stream_file_1_5_english = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/demo/download/1-5_English_Medium.xlsx");
//        file_1_5_english = new DefaultStreamedContent(stream_file_1_5_english, "application/vnd.ms-excel", "1-5_English_Medium.xlsx");
//
//        InputStream stream_file_1_5_tamil = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/demo/download/1-5_Tamil_Medium.xlsx");
//        file_1_5_tamil = new DefaultStreamedContent(stream_file_1_5_tamil, "application/vnd.ms-excel", "1-5_Tamil_Medium.xlsx");
//
//        InputStream stream_file_6_11_sinhala = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/demo/download/6-11_Sinhala_Medium.xlsx");
//        file_6_11_sinhala = new DefaultStreamedContent(stream_file_6_11_sinhala, "application/vnd.ms-excel", "6-11_Sinhala_Medium.xlsx");
//
//        InputStream stream_file_6_11_english = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/demo/download/6-11_English_Medium.xlsx");
//        file_6_11_english = new DefaultStreamedContent(stream_file_6_11_english, "application/vnd.ms-excel", "6-11_English_Medium.xlsx");
//
//        InputStream stream_file_6_11_tamil = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/demo/download/6-11_Tamil_Medium.xlsx");
//        file_6_11_tamil = new DefaultStreamedContent(stream_file_6_11_tamil, "application/vnd.ms-excel", "6-11_Tamil_Medium.xlsx");
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
        getClasses();
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

        // Get Year
        String cur_year = comlib.GetCurrentYear() + "";
        String queryy = "SELECT g FROM Year g  order by g.id ASC";
        List<Year> list_year = uni.searchByQuery(queryy);
        for (Year cc : list_year) {
            if (cc.getName().equals(cur_year)) {
                this.year = cc.getId() + "";
            }
            getYearList().add(new SelectItem(cc.getId(), cc.getName()));
        }

        // Get Excel Year
        getExcelyearList().add(new SelectItem(0, "Select"));

        String queryc = "SELECT g FROM Year g  order by g.id ASC";
        List<Year> list_yearc = uni.searchByQuery(queryc);
        for (Year cc : list_yearc) {
            if (cc.getName().equals(cur_year)) {
                this.excelyear = cc.getId() + "";
            }
            getExcelyearList().add(new SelectItem(cc.getId(), cc.getName()));
        }

        // Get Term
        String queryt = "SELECT g FROM Terms g  order by g.id ASC";
        List<Terms> list_term = uni.searchByQuery(queryt);
        for (Terms cc : list_term) {

            getTermNameList().add(new SelectItem(cc.getId(), cc.getName()));
        }

        // Get Stream
        String query_stream = "SELECT g FROM Streams g  order by g.id ASC";
        List<Streams> list_stream = uni.searchByQuery(query_stream);
        for (Streams cc : list_stream) {

            getStreamNameList().add(new SelectItem(cc.getId(), cc.getName()));
        }
        getGrades();
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

        return null;
    }

    public String getClasses() {

        getExcelClassList().clear();

        getExcelClassList().add(new SelectItem(0, "Select"));

        if (!schoolName.equals("")) {
            String query_al = "SELECT g FROM GradeClassStream g where g.schoolId.id='" + Integer.parseInt(schoolName) + "' and g.isActive='1' order by g.gradeId.id,g.classesId.name ASC ";
            List<GradeClassStream> listAS_al = uni.searchByQuery(query_al);
            for (GradeClassStream cc : listAS_al) {

                String name = cc.getGradeId().getName() + "-" + cc.getClassesId().getName();
                if (cc.getGradeId().getId() == 12 || cc.getGradeId().getId() == 13) {

                    name = cc.getGradeId().getName() + "-" + cc.getClassesId().getName() + " " + cc.getStreamsId().getName();
                }

                getExcelClassList().add(new SelectItem(cc.getId(), name));

            }
        }

        return null;
    }

    public String getGrades() {

        getGradeNameList().clear();
        if (streamName.equals("1")) {
            getGradeNameList().add(new SelectItem(1, "1"));
            getGradeNameList().add(new SelectItem(2, "2"));
            getGradeNameList().add(new SelectItem(3, "3"));
            getGradeNameList().add(new SelectItem(4, "4"));
            getGradeNameList().add(new SelectItem(5, "5"));
            getGradeNameList().add(new SelectItem(6, "6"));
            getGradeNameList().add(new SelectItem(7, "7"));
            getGradeNameList().add(new SelectItem(8, "8"));
            getGradeNameList().add(new SelectItem(9, "9"));
            getGradeNameList().add(new SelectItem(10, "10"));
            getGradeNameList().add(new SelectItem(11, "11"));
        } else {
            getGradeNameList().add(new SelectItem(12, "12"));
            getGradeNameList().add(new SelectItem(13, "13"));

        }
        getSubjectsFromGrade();

        return null;
    }

    public String getSubjectsFromGrade() {

//        Grade g = (Grade) uni.find(Integer.parseInt(gradeName), Grade.class);
//
//        subjectNameTopic = "Grade " + g.getName() + " Subjects";
        subList.clear();
//        if (!gradeName.equals("0")) {
//
//            String query = "SELECT g FROM GradeClassHasSubjects g where g.gradeClassStreamId.gradeId.id='" + gradeName + "' and g.subjectsId.isActive='1' group by g.subjectsId order by g.subjectsId.name ASC";
//            List<GradeClassHasSubjects> listAS = uni.searchByQuery(query);
//            for (GradeClassHasSubjects cc : listAS) {
//
//                subList.add(cc.getSubjectsId().getName());
//            }
//        }

        String query = "SELECT g FROM Subjects g where g.isActive='1' order by g.name ASC";
        List<Subjects> listAS = uni.searchByQuery(query);
        for (Subjects cc : listAS) {

            subList.add(cc.getName());
        }

        return null;
    }

    public List<String> LoadClassNameAutoComplete(String query) {

        List<String> results = new ArrayList<String>();
        System.out.println("sc " + schoolName);
        if (!schoolName.equals("0")) {
            System.out.println("as");
            List<GradeClassStream> emp_list = uni.searchByQuery("SELECT e from GradeClassStream e where  e.classesId.name like '%" + query + "%' and e.schoolId.id='" + schoolName + "' group by e.classesId.name order by e.classesId.name ASC");
            for (GradeClassStream e : emp_list) {

                results.add(e.getClassesId().getName());
            }
        }

        return results;
    }

    public void handleFileUpload(FileUploadEvent event) {
        getUploadedExcelList().clear();
        this.getUploadedExcelList().add(event.getFile());
        fileUploadedStatus = "File Uploaded ! Click Save Button.";
    }

    public synchronized void saveUploadMarks() {

        System.out.println("saveUploadMarks");

        RequestContext requestContext = RequestContext.getCurrentInstance();
        if (schoolName == null || schoolName.equals("") || schoolName.equals("0")) {

            requestContext.execute("$('#loginModalMsg').html('Select School !')");
            requestContext.execute("PF('login_error').show()");
        } else if (sheetNo == null || sheetNo.equals("")) {

            requestContext.execute("$('#loginModalMsg').html('Enter Excel Sheet No !')");
            requestContext.execute("PF('login_error').show()");

        } else if (className == null || className.equals("")) {

            requestContext.execute("$('#loginModalMsg').html('Enter Class Name !')");
            requestContext.execute("PF('login_error').show()");

        } else if (getUploadedExcelList().isEmpty()) {

            requestContext.execute("$('#loginModalMsg').html('Select Excel File !')");
            requestContext.execute("PF('login_error').show()");

        } else {
            try {

                System.out.println("in try loop");

                className = className.trim();
                boolean check_class_format = true;

                String first = Character.toString(className.charAt(0));

                if (className.matches("[0-9]+")) {

                    check_class_format = true;
                } else if (first.matches("[0-9]+")) {
                    check_class_format = false;

                }
                if (check_class_format == false) {
                    requestContext.execute("$('#loginModalMsg').html('First Letter of the Class Name cannot be a Number !')");
                    requestContext.execute("PF('login_error').show()");
                } else {
                    System.out.println("A");
                    stList.clear();
                    subjects.clear();
                    InputStream stream = new ByteArrayInputStream(getUploadedExcelList().iterator().next().getContents());

                    //Create Workbook instance holding reference to .xlsx file
                    XSSFWorkbook workbook = new XSSFWorkbook(stream);

                    //Get first/desired sheet from the workbook
                    XSSFSheet sheet = workbook.getSheetAt(Integer.parseInt(sheetNo) - 1);

                    //Iterate through each rows one by one
                    Iterator<Row> rowIterator1 = sheet.iterator();

                    boolean check_no = false;
                    boolean check_index_no = false;
                    boolean check_name = false;

                    while (rowIterator1.hasNext()) {
                        Row row = rowIterator1.next();
                        //For each row, iterate through all the columns
                        //  System.out.println(row.getRowNum());

                        if (row.getRowNum() > 4) {
                            Iterator<Cell> cellIterator = row.cellIterator();
                            while (cellIterator.hasNext()) {
                                Cell cell = cellIterator.next();
                                cell.getColumnIndex();
                                if (row.getRowNum() == 5) {

                                    if (cell.getColumnIndex() == 0) {
                                        if (cell.getCellTypeEnum() == CellType.STRING) {
                                            if (cell.getStringCellValue().trim().equals("No")) {
                                                check_no = true;
                                                System.out.println("No=" + cell.getStringCellValue());
                                            }

                                        }

                                    }
                                    if (cell.getColumnIndex() == 1) {
                                        if (cell.getCellTypeEnum() == CellType.STRING) {
                                            if (cell.getStringCellValue().trim().equals("Index No")) {
                                                check_index_no = true;
                                                System.out.println("Index No=" + cell.getStringCellValue());
                                            }

                                        }

                                    }
                                    if (cell.getColumnIndex() == 2) {
                                        if (cell.getCellTypeEnum() == CellType.STRING) {
                                            System.out.println("v " + cell.getStringCellValue());
                                            if (cell.getStringCellValue().trim().equals("Name of the Students")) {
                                                check_name = true;
                                                System.out.println("Name of the Students= " + cell.getStringCellValue());
                                            }

                                        }

                                    }

                                }

                            }

                            // code
                        }

                    }

                    boolean is_available_sub = true;

                    String subnamelist = "";

                    boolean is_slash = false;
                    String is_slash_subject = "";
                    int i = 0;
                    // check subject available
                    Iterator<Row> rowIteratorsub = sheet.iterator();
                    while (rowIteratorsub.hasNext()) {
                        System.out.println("while loop" + i);
                        Row row3 = rowIteratorsub.next();
                        //For each row, iterate through all the columns
                        //  System.out.println(row.getRowNum());

                        if (row3.getRowNum() > 4) {
                            Iterator<Cell> cellIterator = row3.cellIterator();
                            while (cellIterator.hasNext()) {
                                Cell cell = cellIterator.next();
                                cell.getColumnIndex();
                                if (row3.getRowNum() == 5) {
                                    if (cell.getColumnIndex() > 2) {
                                        if (!cell.getStringCellValue().trim().equals("")) {
                                            System.out.println("sub|" + cell.getStringCellValue() + "|");

                                            if (cell.getStringCellValue().trim().contains("/")) {
                                                is_slash = true;
                                                is_slash_subject = cell.getStringCellValue().trim();
                                            }

                                            List<Subjects> cl_list = uni.searchByQuerySingle("SELECT im FROM Subjects im WHERE im.name='" + cell.getStringCellValue().trim() + "' and im.isActive='1'");
                                            if (cl_list.size() == 0) {
                                                is_available_sub = false;
                                                if (i == 0) {
                                                    subnamelist = "<li>" + cell.getStringCellValue().trim() + "</li>";
                                                    i++;
                                                } else {

                                                    subnamelist += "<li>" + cell.getStringCellValue().trim() + "</li>";
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    boolean is_available_text = false;
                    String line = "";

                    // check subject available
                    Iterator<Row> rowIteratorText = sheet.iterator();
                    while (rowIteratorText.hasNext()) {
                        Row row3 = rowIteratorText.next();
                        //For each row, iterate through all the columns
                        //  System.out.println(row.getRowNum());

                        if (row3.getRowNum() > 4) {
                            Iterator<Cell> cellIterator = row3.cellIterator();
                            while (cellIterator.hasNext()) {
                                Cell cell = cellIterator.next();
                                cell.getColumnIndex();
                                if (row3.getRowNum() >= 6) {
                                    if (cell.getColumnIndex() > 2) {
                                        if (cell.getCellTypeEnum() == CellType.STRING) {

                                            if (gradeName.equals("1") || gradeName.equals("2")) {

                                                if (!(cell.getStringCellValue().trim().equalsIgnoreCase("ab") || cell.getStringCellValue().trim().equals("") || cell.getStringCellValue().trim().equalsIgnoreCase("A") || cell.getStringCellValue().trim().equalsIgnoreCase("B") || cell.getStringCellValue().trim().equalsIgnoreCase("C") || cell.getStringCellValue().trim().equalsIgnoreCase("S") || cell.getStringCellValue().trim().equalsIgnoreCase("W"))) {
                                                    is_available_text = true;
                                                    line = "(" + (row3.getRowNum() + 1) + ", " + (colNames[cell.getColumnIndex()]) + ")";
                                                }

                                            } else {

                                                if (!(cell.getStringCellValue().trim().equalsIgnoreCase("ab") || cell.getStringCellValue().trim().equals(""))) {
                                                    is_available_text = true;
                                                    line = "(" + (row3.getRowNum() + 1) + ", " + (colNames[cell.getColumnIndex()]) + ")";
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (check_no == false) {

                        requestContext.execute("$('#loginModalMsg').html('(6,A) cell should be \"No\"')");
                        requestContext.execute("PF('login_error').show()");

                    } else if (check_index_no == false) {

                        requestContext.execute("$('#loginModalMsg').html('(6,B) cell should be \"Index No\"')");
                        requestContext.execute("PF('login_error').show()");

                    } else if (check_name == false) {

                        requestContext.execute("$('#loginModalMsg').html('(6,C) cell should be \"Name of the Students\"')");
                        requestContext.execute("PF('login_error').show()");

                    } else if (is_slash == true) {

                        requestContext.execute("$('#subjectModalMsg2').html('You are trying to include results of two subjects in a single cell (" + is_slash_subject + "). It is not allowed. Take those two subjects into two cells.')");
                        requestContext.execute("$('#subjectModalMsg3').html('විෂයන් දෙකක ලකුණු කොලම් එකකකට ඇතුලත් කිරීමට ඔබ උත්සාහා කර තිබේ (" + is_slash_subject + "). මීට මෘදුකාංග පද්ධතිය සහයෝගය නොදක්වයි.  මෙම විෂයන් දෙකහි ලකුණු කොලම් දෙකකට ගන්න.')");
                        requestContext.execute("PF('subject_error2').show()");

                    } else if (is_available_sub == false) {

                        requestContext.execute("$('#subjectModalMsg').html('" + subnamelist + "')");
                        requestContext.execute("$('#subjectModalMsg0').html('" + subnamelist + "')");
                        requestContext.execute("PF('subject_error').show()");

                    } else if (is_available_text == true) {

                        requestContext.execute("$('#loginModalMsg').html('Marks must be Numbers ! Check " + line + " Cell.')");
                        requestContext.execute("PF('login_error').show()");

                    } else {
                        saveGradeClassManager();
                        Iterator<Row> rowIterator2 = sheet.iterator();
                        while (rowIterator2.hasNext()) {
                            Row row = rowIterator2.next();
                            //For each row, iterate through all the columns
                            //  System.out.println(row.getRowNum());

                            if (row.getRowNum() > 4) {
                                Iterator<Cell> cellIterator = row.cellIterator();
                                while (cellIterator.hasNext()) {
                                    Cell cell = cellIterator.next();
                                    cell.getColumnIndex();
                                    if (row.getRowNum() == 5) {
                                        if (cell.getColumnIndex() > 2) {
                                            if (!cell.getStringCellValue().trim().equals("")) {
                                                Subjects sub;
                                                List<Subjects> cl_list = uni.searchByQuerySingle("SELECT im FROM Subjects im WHERE im.name='" + cell.getStringCellValue().trim() + "' and im.isActive='1'");
                                                if (cl_list.size() > 0) {
                                                    sub = cl_list.iterator().next();
                                                } else {

                                                    int education_level = 2;
                                                    if (gradeName.equals("12") || gradeName.equals("13")) {
                                                        education_level = 3;

                                                    }

                                                    sub = new Subjects();
                                                    sub.setName(cell.getStringCellValue().trim());
                                                    sub.setCode("");
                                                    sub.setEducationLevelId((EducationLevel) uni.find(education_level, EducationLevel.class));
                                                    sub.setIsActive(1);

                                                    uni.create(sub);

                                                    System.out.println("saveUploadMarks");
                                                }
                                                GradeClassHasSubjects gchs = null;
                                                List<GradeClassHasSubjects> sub_list = uni.searchByQuery("SELECT im FROM GradeClassHasSubjects im WHERE im.gradeClassStreamId.id='" + getGcs().getId() + "' and im.subjectsId.id='" + sub.getId() + "' ");
                                                if (sub_list.size() > 0) {

                                                    gchs = sub_list.iterator().next();
                                                    gchs.setIsActive(1);
                                                    uni.update(gchs);

                                                } else {

                                                    gchs = new GradeClassHasSubjects();
                                                    gchs.setGradeClassStreamId(getGcs());
                                                    gchs.setIsActive(1);
                                                    gchs.setSubjectsId(sub);
                                                    uni.create(gchs);

                                                }
                                                System.out.println("gg " + cell.getStringCellValue().trim());
                                                getSubjects().add(gchs);
                                            }
                                        }
                                    }

                                }
                            }
                        }

                        boolean check_index_no_empty = false;
                        boolean check_subject_marks_entered = true;
                        String not_entered_student_name = "";

                        int subject_count = 0;
                        if (gradeName.equals("6") || gradeName.equals("7") || gradeName.equals("8") || gradeName.equals("9")) {
                            subject_count = 10;
                        } else if (gradeName.equals("10") || gradeName.equals("11")) {
                            subject_count = 9;
                        }

                        // students save
                        getStList().clear();
                        Iterator<Row> rowIterator3 = sheet.iterator();
                        while (rowIterator3.hasNext()) {

                            Row row = rowIterator3.next();
                            //For each row, iterate through all the columns
                            //  System.out.println(row.getRowNum());

                            if (row.getRowNum() > 5) {
                                Iterator<Cell> cellIterator = row.cellIterator();

                                String stu_name = "";
                                String index_no = "";
                                List<String> val = new ArrayList();
//                                int colom = 0;
                                for (int colom = 0; colom < subjects.size() + 3; colom++) {
//                                while (cellIterator.hasNext()) {
                                    System.out.println("asa" + colom);
                                    if (colom < subjects.size() + 3) {
                                        Cell cell = row.getCell(colom);

                                        System.out.println("awaawaw" + subjects.size() + "|" + colom);
                                        System.out.println("index " + row.getRowNum());

                                        if (colom == 1) {
                                            if (cell != null) {

                                                if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                                    index_no = ((int) cell.getNumericCellValue()) + "";
                                                } else if (cell.getCellTypeEnum() == CellType.STRING) {
                                                    index_no = cell.getStringCellValue().trim();
                                                }
                                            }

                                        } else if (colom == 2) {
                                            if (cell != null) {

                                                stu_name = cell.getStringCellValue().trim();
                                                System.out.println("stu " + stu_name);
                                            }
                                        } else if (colom > 2) {
                                            if (cell == null) {

                                                val.add("");

                                            } else {
//                                               System.out.println("null  "+cell.getNumericCellValue());
//                                            System.out.println("celltype "+cell.getCellTypeEnum().name());
                                                if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
                                                    val.add(cell.getNumericCellValue() + "");
                                                    System.out.println("A" + cell.getNumericCellValue());
                                                } else {
                                                    System.out.println("else");
                                                    val.add(cell.getStringCellValue().trim());
                                                    System.out.println("C" + cell.getStringCellValue().trim());
                                                }
                                            }
                                        }
//                                         colom++;
                                    }

                                }

                                if (!stu_name.equals("")) {
                                    if (index_no.trim().equals("")) {
                                        check_index_no_empty = true;
                                    }
                                    boolean check_sub = true;
                                    int subc = 0;
                                    System.out.println("subject_count----- " + subject_count);
                                    if (subject_count != 0) {
                                        for (String v : val) {
                                            System.out.println("v is " + v);
                                            if (!(v.equals("") || v.equals("-"))) {

                                                subc++;
                                            }
                                        }
                                        System.out.println("subc " + subc + " | " + subject_count + " ");
                                        if (subc < subject_count) {

                                            check_sub = false;
                                        }
                                    }
                                    if (check_sub == false) {

                                        check_subject_marks_entered = false;
                                        not_entered_student_name = stu_name;
                                        break;
                                    }

                                    System.out.println("BB " + val.size());
                                    stList.add(new StudentList(index_no, stu_name, val));

                                }
                                // code
                            }

                        }
                        if (check_index_no_empty == true) {

                            requestContext.execute("$('#loginModalMsg').html('Index No Can not be Empty !')");
                            requestContext.execute("PF('login_error').show()");
                        } else if (check_subject_marks_entered == false) {

                            requestContext.execute("$('#subjectModalMsg2').html(' The Student of " + not_entered_student_name + "  Marks Entered Subject Count Should be " + subject_count + "')");
                            requestContext.execute("$('#subjectModalMsg3').html('" + not_entered_student_name + " යන ශිෂ්‍යාගේ ලකුණු ඇතුලත් කර ඇති විෂයන් ගණන " + subject_count + "ක් විය යුතුයි. ')");
                            requestContext.execute("PF('subject_error2').show()");

//                            requestContext.execute("$('#loginModalMsg').html('" + not_entered_student_name + " \'s Marks Entered Subject Count Should be " + subject_count + "')");
//                            requestContext.execute("PF('login_error').show()");
                        } else {

                            if (stList.size() > 0) {

                                gradeclassSubjectTeacherList.clear();

                                List<SelectItem> teacherNameList1 = new ArrayList<SelectItem>();
                                // Get Teachers
                                teacherNameList1.add(new SelectItem("0", "Subject Teacher Not Assigned"));
                                System.out.println("D");
                                String query1 = "SELECT g FROM Teacher g where g.schoolId.id='" + Integer.parseInt(schoolName) + "' and g.isActive='1' order by g.generalUserProfileId.nameWithIn ASC";
                                List<Teacher> listAS1 = uni.searchByQuery(query1);
                                System.out.println("E");
                                for (Teacher cc : listAS1) {

                                    teacherNameList1.add(new SelectItem(cc.getId(), cc.getGeneralUserProfileId().getNameWithIn()));
                                }

                                for (GradeClassHasSubjects sub : subjects) {
                                    System.out.println("F");
                                    List<GradeClassSubjectTeacher> sub_list = uni.searchByQuery("SELECT im FROM GradeClassSubjectTeacher im WHERE im.gradeClassHasSubjectsId.id='" + sub.getId() + "' and im.gradeClassStreamManagerId.yearId.id='" + year + "' and im.isActive='1' ");
                                    if (sub_list.isEmpty()) {
                                        System.out.println("G");
                                        gradeclassSubjectTeacherList.add(new GradeClassSubjectTeacherList(sub.getId(), sub.getSubjectsId().getName(), "0", teacherNameList1));

                                    }

                                }
                                if (gradeclassSubjectTeacherList.size() > 0) {
                                    System.out.println("H");
                                    Year y = (Year) uni.find(Integer.parseInt(year), Year.class);
                                    subjectYear = y.getName();
                                    if (gcs.getGradeId().getId() == 12 || gcs.getGradeId().getId() == 13) {
                                        subjectClass = gcs.getGradeId().getName() + "-" + gcs.getClassesId().getName() + " " + gcs.getStreamsId().getName();
                                    } else {

                                        subjectClass = gcs.getGradeId().getName() + "-" + gcs.getClassesId().getName();
                                    }

                                    classTeacherNameList.clear();
                                    // Get Teachers
                                    classTeacherNameList.add(new SelectItem("0", "Class Teacher Not Assigned"));

                                    String query = "SELECT g FROM Teacher g where g.schoolId.id='" + Integer.parseInt(schoolName) + "' and g.isActive='1' order by g.generalUserProfileId.nameWithIn ASC";
                                    List<Teacher> listAS = uni.searchByQuery(query);
                                    for (Teacher cc : listAS) {

                                        classTeacherNameList.add(new SelectItem(cc.getId(), cc.getGeneralUserProfileId().getNameWithIn()));
                                    }
                                    classTeacherName = "0";
                                    String queryt = "SELECT g FROM GradeClassStreamManager g where g.id='" + gcsm.getId() + "'";
                                    List<GradeClassStreamManager> listASt = uni.searchByQuerySingle(queryt);
                                    if (listASt.size() > 0) {
                                        if (listASt.iterator().next().getTeacherId() != null) {
                                            classTeacherName = listASt.iterator().next().getTeacherId().getId() + "";
                                        }
                                    }
                                    System.out.println("subjectYear " + subjectYear);
                                    System.out.println("subjectClass " + subjectClass);
                                    System.out.println("gg " + gradeclassSubjectTeacherList.size());
                                    requestContext.execute("PF('enterSubjectTeachersDLG').show()");
                                } else {

                                    subjectYear = "";
                                    subjectClass = "";
                                    gradeclassSubjectTeacherList.clear();
                                    finallySaveMarks();
                                }
                            } else {

                                requestContext.execute("$('#loginModalMsg').html('Student Marks Table is Empty !')");
                                requestContext.execute("PF('login_error').show()");

                            }
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void finallySaveMarks() {
        System.out.println("finallySaveMarks");
        try {
            for (StudentList st : stList) {
                System.out.println("cc " + st.getValue().size());
                comDiv.saveMarks(st.getIndexNo(), st.getName(), st.getValue(), gcs.getSchoolId(), gcs, gcsm, subjects, terms, ls);
            }
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("$('#successModalMsg').html('Successfully Saved !')");
            requestContext.execute("PF('login_success').show()");

            gcs = null;
            gcsm = null;
            className = "";
            uploadedExcelList.clear();
            fileUploadedStatus = "";
            subjects.clear();
            subjectYear = "";
            subjectClass = "";
            gradeclassSubjectTeacherList.clear();

            loadLastUploadedMarks();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public synchronized void saveGradeClassManager() {

        Year y = (Year) uni.find(Integer.parseInt(this.year), Year.class);

        Classes cl = null;
        List<Classes> cl_list = uni.searchByQuerySingle("SELECT im FROM Classes im WHERE im.name='" + className + "'");
        if (cl_list.size() > 0) {
            cl = cl_list.iterator().next();

        } else {

            cl = new Classes();
            cl.setName(className);
            uni.create(cl);
        }

        List<GradeClassStream> gop_list1 = uni.searchByQuerySingle("SELECT im FROM GradeClassStream im WHERE im.gradeId.id='" + gradeName + "' and im.schoolId.id='" + schoolName + "' and im.classesId.id='" + cl.getId() + "' and im.streamsId.id='" + streamName + "' ");
        if (gop_list1.size() > 0) {
            setGcs(gop_list1.iterator().next());

            GradeClassStream g = gop_list1.iterator().next();
            g.setIsActive(1);
            uni.update(g);

        } else {
            setGcs(new GradeClassStream());
            getGcs().setClassesId(cl);
            getGcs().setGradeId((Grade) uni.find(Integer.parseInt(gradeName), Grade.class));
            getGcs().setSchoolId((School) uni.find(Integer.parseInt(schoolName), School.class));
            getGcs().setStreamsId((Streams) uni.find(Integer.parseInt(streamName), Streams.class));
            getGcs().setIsActive(1);
            uni.create(getGcs());
        }

        List<GradeClassStreamManager> gop_list = uni.searchByQuerySingle("SELECT im FROM GradeClassStreamManager im WHERE im.yearId.id='" + year + "' and im.gradeClassStreamId.id='" + getGcs().getId() + "'  ");
        if (gop_list.size() > 0) {
            setGcsm(gop_list.iterator().next());

            GradeClassStreamManager g = gop_list.iterator().next();
            g.setIsActive(1);
            uni.update(g);
        } else {
            setGcsm(new GradeClassStreamManager());
            getGcsm().setGradeClassStreamId(getGcs());
            getGcsm().setYearId(y);
            getGcsm().setIsActive(1);
            uni.create(getGcsm());
        }
        terms = (Terms) uni.find(Integer.parseInt(termName), Terms.class);

    }

    public void loadLastUploadedMarks() {
        System.out.println("awas");

        studentList.clear();

        if (!schoolName.equals("0")) {
            List<MarksList> header = new ArrayList();

            int gcsID = 0;
            int yearID = 0;
            int termID = 0;
            int grade = 0;

            String querym0 = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.studentsId.schoolId.id='" + schoolName + "' and g.enteredBy.id='" + ls.getUserLoginId().getGeneralUserProfileId().getId() + "' and g.isRemoved='0' order by g.id DESC";
            List<StudentMarks> listm0 = uni.searchByQuerySingle(querym0);
            if (listm0.size() > 0) {
                System.out.println("awad");
                gcsID = listm0.iterator().next().getGradeClassStudentsId().getGradeClassStreamManagerId().getGradeClassStreamId().getId();
                yearID = listm0.iterator().next().getGradeClassStudentsId().getGradeClassStreamManagerId().getYearId().getId();
                termID = listm0.iterator().next().getTermsId().getId();

                lastClass = listm0.iterator().next().getGradeClassStudentsId().getGradeClassStreamManagerId().getGradeClassStreamId().getGradeId().getName() + " - " + listm0.iterator().next().getGradeClassStudentsId().getGradeClassStreamManagerId().getGradeClassStreamId().getClassesId().getName();
                lastYear = listm0.iterator().next().getGradeClassStudentsId().getGradeClassStreamManagerId().getYearId().getName();
                lastTerm = listm0.iterator().next().getTermsId().getName();

                grade = listm0.iterator().next().getGradeClassStudentsId().getGradeClassStreamManagerId().getGradeClassStreamId().getGradeId().getId();
            }
            if (gcsID != 0) {

                String queryt = "SELECT g FROM GradeClassStreamManager g where g.gradeClassStreamId.id='" + gcsID + "' and g.yearId.id='" + yearID + "'";
                List<GradeClassStreamManager> listASt = uni.searchByQuerySingle(queryt);
                if (listASt.size() > 0) {

                    header.add(new MarksList("#", "#ffad33", "center", ""));
                    header.add(new MarksList("Index No", "#ffad33", "center", ""));
                    header.add(new MarksList("Name", "#ffad33", "left", ""));

//                    String querysub = "SELECT g FROM GradeClassHasSubjects g where g.gradeClassStreamId.id='" + listASt.iterator().next().getGradeClassStreamId().getId() + "'  and g.isActive='1' and g.gradeClassStreamManagerId.yearId.name='" + lastYear + "' ";
//                    List<GradeClassHasSubjects> sublist = uni.searchByQuery(querysub);
//                    if (sublist.size() > 0) {
//                        for (GradeClassHasSubjects gchs : sublist) {
//                            header.add(new MarksList(gchs.getSubjectsId().getName(), "#ffad33", "center"));
//                        }
//                    }
                    String querysub = "SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.gradeClassStreamId.id='" + listASt.iterator().next().getGradeClassStreamId().getId() + "' and g.gradeClassStreamManagerId.yearId.name='" + lastYear + "' and g.isActive='1' and  g.gradeClassHasSubjectsId.subjectsId.isActive='1' ";
                    List<GradeClassSubjectTeacher> sublist = uni.searchByQuery(querysub);
                    if (sublist.size() > 0) {
                        for (GradeClassSubjectTeacher gchs : sublist) {
                            header.add(new MarksList(gchs.getGradeClassHasSubjectsId().getSubjectsId().getName(), "#ffad33", "center", "rotated"));
                        }
                    }
                    studentList.add(new LastStudentList(header));

                    if (sublist.size() > 0) {
                        int i = 1;
                        String query = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.id='" + listASt.iterator().next().getId() + "' and g.isRemoved='0'";
                        List<GradeClassStudents> list = uni.searchByQuery(query);
                        if (list.size() > 0) {

                            for (GradeClassStudents gcs : list) {
                                List<MarksList> ar = new ArrayList();
                                ar.add(new MarksList(i + "", "white", "center", ""));
                                ar.add(new MarksList(gcs.getStudentsId().getStudentId(), "white", "left", ""));
                                ar.add(new MarksList(gcs.getStudentsId().getGeneralUserProfileId().getNameWithIn(), "white", "left", ""));
                                System.out.println("pp " + sublist.size());
                                for (GradeClassSubjectTeacher gchs : sublist) {
                                    String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.termsId.id='" + termID + "' and g.gradeClassHasSubjectsId.id='" + gchs.getGradeClassHasSubjectsId().getId() + "' and g.isRemoved='0' ";
                                    List<StudentMarks> listm = uni.searchByQuerySingle(querym);
                                    if (listm.size() > 0) {
                                        StudentMarks sm = listm.iterator().next();

                                        String marks = "";
                                        String color = "white";
                                        if (sm.getIsPresent() == false) {

                                            marks = "ab";
                                            color = "#FFAB91";
                                        } else if (sm.getIsMandatory() == false) {
                                            marks = "";
                                            color = "#cacaca";
                                        } else {
                                            if (grade == 1 || grade == 2) {

                                                marks = comlib.GetValueByMark(sm.getMarks());
                                            } else {
                                                marks = sm.getMarks().intValue() + "";
                                            }
                                        }

                                        ar.add(new MarksList(marks, color, "center", ""));

                                    } else {

                                        ar.add(new MarksList("", "white", "center", ""));

                                    }
                                }

                                studentList.add(new LastStudentList(ar));

                                i++;

                            }
                        }
                    }
                }
            }
        }
    }

    public void saveSubjectTeachers() {

        try {

            int classT = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("classT").toString());

            gcsm.setTeacherId((Teacher) uni.find(classT, Teacher.class));
            uni.update(gcsm);

            String json_array = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("json_array").toString();
            System.out.println(json_array);
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(json_array);
            JSONObject job = (JSONObject) obj;
            JSONArray jsnarr = (JSONArray) job.get("jsn");

            for (Iterator it = jsnarr.iterator(); it.hasNext();) {

                JSONObject json_object = (JSONObject) it.next();

                int subID = Integer.valueOf(json_object.get("subID").toString().trim());
                int subjectTeacherID = Integer.valueOf(json_object.get("subjectTeacherID").toString().trim());

                GradeClassHasSubjects gchs = (GradeClassHasSubjects) uni.find(subID, GradeClassHasSubjects.class);

                System.out.println(gchs.getGradeClassStreamId().getGradeId().getName() + "-" + gchs.getGradeClassStreamId().getClassesId().getName());

                List<GradeClassSubjectTeacher> sub_list = uni.searchByQuery("SELECT im FROM GradeClassSubjectTeacher im WHERE im.gradeClassHasSubjectsId.id='" + gchs.getId() + "' and im.gradeClassStreamManagerId.yearId.id='" + year + "' ");
                if (sub_list.isEmpty()) {

                    GradeClassSubjectTeacher gcst = new GradeClassSubjectTeacher();
                    gcst.setGradeClassHasSubjectsId(gchs);
                    gcst.setGradeClassStreamManagerId(gcsm);
                    gcst.setTeacherId((Teacher) uni.find(subjectTeacherID, Teacher.class));
                    gcst.setIsActive(1);
                    uni.create(gcst);

                } else {

                    GradeClassSubjectTeacher gcst = sub_list.iterator().next();
                    gcst.setIsActive(1);
                    uni.update(gcst);

                }
            }

            finallySaveMarks();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void lOBExport2Excel(String medium) {

        try {
            String schoolname = "";
            String divisionname = "";
            String gradename = "";
            if (!schoolName.equals("0")) {

                School sc = (School) uni.find(Integer.parseInt(schoolName), School.class);
                schoolname = sc.getGeneralOrganizationProfileId().getName();
                divisionname = sc.getEducationDivisionId().getName();
            }
            if (!excelClass.equals("0")) {
                GradeClassStream sc = (GradeClassStream) uni.find(Integer.parseInt(excelClass), GradeClassStream.class);
                gradename = sc.getGradeId().getName() + "-" + sc.getClassesId().getName();
            }

            String excel_name = "";
            InputStream ins = null;
            if (medium.equals("file_1_2_sinhala")) {
                ins = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/demo/download/1-2_Sinhala_Medium.xlsx");
                excel_name = "1-2_Sinhala_Medium.xlsx";
            } else if (medium.equals("file_1_2_english")) {
                ins = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/demo/download/1-2_English_Medium.xlsx");
                excel_name = "1-2_English_Medium.xlsx";
            } else if (medium.equals("file_1_2_tamil")) {
                ins = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/demo/download/1-2_Tamil_Medium.xlsx");
                excel_name = "1-2_Tamil_Medium.xlsx";
            } else if (medium.equals("file_3_5_sinhala")) {
                ins = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/demo/download/3-5_Sinhala_Medium.xlsx");
                excel_name = "3-5_Sinhala_Medium.xlsx";
            } else if (medium.equals("file_3_5_english")) {
                ins = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/demo/download/3-5_English_Medium.xlsx");
                excel_name = "3-5_English_Medium.xlsx";
            } else if (medium.equals("file_3_5_tamil")) {
                ins = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/demo/download/3-5_Tamil_Medium.xlsx");
                excel_name = "3-5_Tamil_Medium.xlsx";
            } else if (medium.equals("file_6_11_sinhala")) {
                ins = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/demo/download/6-11_Sinhala_Medium.xlsx");
                excel_name = "6-11_Sinhala_Medium.xlsx";
            } else if (medium.equals("file_6_11_english")) {
                ins = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/demo/download/6-11_English_Medium.xlsx");
                excel_name = "6-11_English_Medium.xlsx";
            } else if (medium.equals("file_6_11_tamil")) {
                ins = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/demo/download/6-11_Tamil_Medium.xlsx");
                excel_name = "6-11_Tamil_Medium.xlsx";
            }

            XSSFWorkbook workbook = new XSSFWorkbook(ins);

            XSSFSheet sheet = workbook.getSheetAt(0);

            Row row1 = sheet.getRow(0);

            Cell cell0 = row1.getCell(1);
            cell0.setCellValue("Student Exam Marks");

            Row row2 = sheet.getRow(1);
            Cell cell1 = row2.getCell(1);
            cell1.setCellValue("");

            Row row3 = sheet.getRow(2);
            Cell cell3 = row3.getCell(1);
            cell3.setCellValue("School : " + schoolname);

            Row row4 = sheet.getRow(3);
            Cell cell4 = row4.getCell(1);
            cell4.setCellValue("Division : " + divisionname);

            Row row5 = sheet.getRow(4);
            Cell cell5 = row5.getCell(1);
            cell5.setCellValue("Grade : " + gradename);
            int rw = 6;
            if (!schoolName.equals("0") && !excelClass.equals("0") && !excelyear.equals("0")) {

                int i = 1;
                String query = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.gradeClassStreamId.id='" + excelClass + "' and g.gradeClassStreamManagerId.yearId.id='" + excelyear + "'  and g.isRemoved='0' order by g.studentsId.generalUserProfileId.nameWithIn ASC";
                List<GradeClassStudents> listAS1 = uni.searchByQuery(query);
                for (GradeClassStudents cc : listAS1) {
                    System.out.println("rw " + rw);
                    Row rowa = sheet.createRow(rw);

                    Cell cell_no = rowa.createCell(0);
                    cell_no.setCellValue(i);

                    Cell cell_index = rowa.createCell(1);
                    cell_index.setCellValue(cc.getStudentsId().getStudentId());

                    Cell cell_name = rowa.createCell(2);
                    cell_name.setCellValue(cc.getStudentsId().getGeneralUserProfileId().getNameWithIn());

                    i++;
                    rw++;
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

    public class LastStudentList {

        private List<MarksList> name;

        public LastStudentList(List<MarksList> name) {
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
        private String horalignment;

        public MarksList(String marks, String color, String align, String horalignment) {
            this.marks = marks;
            this.color = color;
            this.align = align;
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

        public String getHoralignment() {
            return horalignment;
        }

        public void setHoralignment(String horalignment) {
            this.horalignment = horalignment;
        }

    }

    public class StudentList {

        private String name;
        private String indexNo;
        private List<String> value;

        public StudentList(String indexNo, String name, List<String> value) {
            this.indexNo = indexNo;
            this.name = name;
            this.value = value;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIndexNo() {
            return indexNo;
        }

        public void setIndexNo(String indexNo) {
            this.indexNo = indexNo;
        }

    }

    public class GradeClassSubjectTeacherList implements Serializable {

        private String subjectName;
        private int subjectId;
        private String teacherName;
        private List<SelectItem> teacherNameList = new ArrayList<SelectItem>();

        public GradeClassSubjectTeacherList(int subjectId, String subjectName, String teacherName, List<SelectItem> teacherNameList) {
            this.subjectName = subjectName;
            this.subjectId = subjectId;
            this.teacherName = teacherName;
            this.teacherNameList = teacherNameList;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public int getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(int subjectId) {
            this.subjectId = subjectId;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public List<SelectItem> getTeacherNameList() {
            return teacherNameList;
        }

        public void setTeacherNameList(List<SelectItem> teacherNameList) {
            this.teacherNameList = teacherNameList;
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

    public String getSheetNo() {
        return sheetNo;
    }

    public void setSheetNo(String sheetNo) {
        this.sheetNo = sheetNo;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public List<SelectItem> getStreamNameList() {
        return streamNameList;
    }

    public void setStreamNameList(List<SelectItem> streamNameList) {
        this.streamNameList = streamNameList;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public List<SelectItem> getGradeNameList() {
        return gradeNameList;
    }

    public void setGradeNameList(List<SelectItem> gradeNameList) {
        this.gradeNameList = gradeNameList;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<UploadedFile> getUploadedExcelList() {
        return uploadedExcelList;
    }

    public void setUploadedExcelList(List<UploadedFile> uploadedExcelList) {
        this.uploadedExcelList = uploadedExcelList;
    }

    public String getFileUploadedStatus() {
        return fileUploadedStatus;
    }

    public void setFileUploadedStatus(String fileUploadedStatus) {
        this.fileUploadedStatus = fileUploadedStatus;
    }

    public GradeClassStream getGcs() {
        return gcs;
    }

    public void setGcs(GradeClassStream gcs) {
        this.gcs = gcs;
    }

    public GradeClassStreamManager getGcsm() {
        return gcsm;
    }

    public void setGcsm(GradeClassStreamManager gcsm) {
        this.gcsm = gcsm;
    }

    public List<GradeClassHasSubjects> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<GradeClassHasSubjects> subjects) {
        this.subjects = subjects;
    }

    public List<StudentList> getStList() {
        return stList;
    }

    public void setStList(List<StudentList> stList) {
        this.stList = stList;
    }

    public Terms getTerms() {
        return terms;
    }

    public void setTerms(Terms terms) {
        this.terms = terms;
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

    public List<String> getSubList() {
        return subList;
    }

    public void setSubList(List<String> subList) {
        this.subList = subList;
    }

    public String getSubjectNameTopic() {
        return subjectNameTopic;
    }

    public void setSubjectNameTopic(String subjectNameTopic) {
        this.subjectNameTopic = subjectNameTopic;
    }

    public List<LastStudentList> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<LastStudentList> studentList) {
        this.studentList = studentList;
    }

    public String getLastYear() {
        return lastYear;
    }

    public void setLastYear(String lastYear) {
        this.lastYear = lastYear;
    }

    public String getLastTerm() {
        return lastTerm;
    }

    public void setLastTerm(String lastTerm) {
        this.lastTerm = lastTerm;
    }

    public String getLastClass() {
        return lastClass;
    }

    public void setLastClass(String lastClass) {
        this.lastClass = lastClass;
    }

    public String getSubjectYear() {
        return subjectYear;
    }

    public void setSubjectYear(String subjectYear) {
        this.subjectYear = subjectYear;
    }

    public String getSubjectClass() {
        return subjectClass;
    }

    public void setSubjectClass(String subjectClass) {
        this.subjectClass = subjectClass;
    }

    public List<GradeClassSubjectTeacherList> getGradeclassSubjectTeacherList() {
        return gradeclassSubjectTeacherList;
    }

    public void setGradeclassSubjectTeacherList(List<GradeClassSubjectTeacherList> gradeclassSubjectTeacherList) {
        this.gradeclassSubjectTeacherList = gradeclassSubjectTeacherList;
    }

    public String getClassTeacherName() {
        return classTeacherName;
    }

    public void setClassTeacherName(String classTeacherName) {
        this.classTeacherName = classTeacherName;
    }

    public List<SelectItem> getClassTeacherNameList() {
        return classTeacherNameList;
    }

    public void setClassTeacherNameList(List<SelectItem> classTeacherNameList) {
        this.classTeacherNameList = classTeacherNameList;
    }

    public String getExcelyear() {
        return excelyear;
    }

    public void setExcelyear(String excelyear) {
        this.excelyear = excelyear;
    }

    public List<SelectItem> getExcelyearList() {
        return excelyearList;
    }

    public void setExcelyearList(List<SelectItem> excelyearList) {
        this.excelyearList = excelyearList;
    }

    public String getExcelClass() {
        return excelClass;
    }

    public void setExcelClass(String excelClass) {
        this.excelClass = excelClass;
    }

    public List<SelectItem> getExcelClassList() {
        return excelClassList;
    }

    public void setExcelClassList(List<SelectItem> excelClassList) {
        this.excelClassList = excelClassList;
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
