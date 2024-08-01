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
import com.ejb.model.entity.GradeClassStudentsHasSubjects;
import com.ejb.model.entity.GradeClassSubjectTeacher;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.Province;
import com.ejb.model.entity.School;
import com.ejb.model.entity.Streams;
import com.ejb.model.entity.StudentMarks;
import com.ejb.model.entity.Students;
import com.ejb.model.entity.Subjects;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.Terms;
import com.ejb.model.entity.Year;
import com.webapps.controller.utilities.SortArraysCaderManagement;
import com.webapps.controller.utilities.SortArraysStudentLongitudinalDevelopment;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author Thilini Madagama
 */
@ManagedBean
@ViewScoped
public class ClassAchiementsLevels implements Serializable {

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

    private String termName = "0";
    private List<SelectItem> termNameList = new ArrayList<SelectItem>();

    private String gradeName = "0";
    private List<SelectItem> gradeNameList = new ArrayList<SelectItem>();

    private BarChartModel barModel = new BarChartModel();

    private boolean disabledFiledProvince = false;
    private boolean disabledFiledZone = false;
    private boolean disabledFiledDivision = false;
    private boolean disabledFiledSchool = false;

    private String studentName;
    private String indexNo;

    private int def_province = 0;
    private int def_zone = 0;
    private int def_division = 0;
    private int def_school = 0;

    private List<StudentList> recordList = new ArrayList();

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

        // Get Year
        String cur_year = comlib.GetCurrentYear() + "";
        String queryy = "SELECT g FROM Year g  order by g.id ASC";
        List<Year> list_year = uni.searchByQuery(queryy);
        for (Year cc : list_year) {
            if (cc.getName().equals(cur_year)) {

                this.year = cc.getName() + "";
            }
            getYearList().add(new SelectItem(cc.getName(), cc.getName()));
        }

        // Get Term
        String queryt = "SELECT g FROM Terms g  order by g.id ASC";
        List<Terms> list_term = uni.searchByQuery(queryt);
        for (Terms cc : list_term) {

            getTermNameList().add(new SelectItem(cc.getId(), cc.getName()));
        }

        // Get Grade
        String queryc = "SELECT g FROM Grade g  order by g.id ASC";
        List<Grade> list_class = uni.searchByQuery(queryc);
        for (Grade cc : list_class) {

            getGradeNameList().add(new SelectItem(cc.getId(), cc.getName()));
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

        return null;
    }

    public void loadClassAchievementsLevels() {
        recordList.clear();
        FacesMessage msg = null;
        if (provinceName.equals("0")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select Searching Criterias !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {

            if (!schoolName.equals("0")) {

                School sch = (School) uni.find(Integer.parseInt(schoolName), School.class);

                String topic = "Enhancement of Educational Achievements - " + sch.getGeneralOrganizationProfileId().getName();

                String querytc = "SELECT g FROM GradeClassStream g where g.gradeId.id='" + gradeName + "' and g.schoolId.id='" + schoolName + "' order by g.classesId.name ASC";
                List<GradeClassStream> listAStc = uni.searchByQuery(querytc);

                List<MarksList> top_header = new ArrayList();
                top_header.add(new MarksList(topic, "#ffad33", "center", "bold", (listAStc.size() + 2) + ""));
                recordList.add(new StudentList(top_header));

                List<MarksList> header = new ArrayList();

                List<MarksList> headerPass = new ArrayList();
                List<MarksList> headerFail = new ArrayList();
                List<MarksList> headerTot = new ArrayList();
                List<MarksList> header1 = new ArrayList();

                Terms tr = (Terms) uni.find(Integer.parseInt(termName), Terms.class);

                header1.add(new MarksList("Grade " + gradeName + " - " + year + " " + tr.getName(), "#ffad33", "center", "bold", (listAStc.size() + 2) + ""));
                recordList.add(new StudentList(header1));
                header.add(new MarksList("Class -->", "#ffad33", "center", "bold", "1"));

                String query1 = "SELECT g FROM GradeClassStream g where g.gradeId.id='" + gradeName + "' and g.schoolId.id='" + schoolName + "' order by g.classesId.name ASC";
                List<GradeClassStream> listAS1 = uni.searchByQuery(query1);
                for (GradeClassStream gcs : listAS1) {

                    header.add(new MarksList(gcs.getClassesId().getName(), "#ffad33", "center", "bold", "1"));
                }

                header.add(new MarksList("Total", "#ffad33", "center", "bold", "1"));
                recordList.add(new StudentList(header));

                headerPass.add(new MarksList("Pass", "#9af893", "center", "bold", "1"));
                headerFail.add(new MarksList("Fail", "#f8939f", "center", "bold", "1"));
                headerTot.add(new MarksList("Total", "#9fbcb0", "center", "bold", "1"));

                int totPass = 0;
                int totFail = 0;

                for (GradeClassStream gcsm : listAStc) {

                    int pass = 0;
                    int fail = 0;

                    String query11 = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "' and g.gradeClassStreamManagerId.yearId.name='" + year + "' and g.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + schoolName + "' and g.gradeClassStreamManagerId.gradeClassStreamId.classesId.id='" + gcsm.getClassesId().getId() + "' and g.isRemoved='0'";
                    List<GradeClassStudents> listAS11 = uni.searchByQuery(query11);
                    for (GradeClassStudents gcs : listAS11) {

                        double tot_val = 0;
                        double avg = 0;

                        String query = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.gradeClassHasSubjectsId.subjectsId.isActive='1' ";
                        List<GradeClassStudentsHasSubjects> list = uni.searchByQuery(query);
                        if (list.size() > 0) {

                            for (GradeClassStudentsHasSubjects gcshs : list) {

                                String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.id='" + gcshs.getGradeClassHasSubjectsId().getId() + "' and g.isRemoved='0' ";
                                List<StudentMarks> listm = uni.searchByQuerySingle(querym);
                                if (listm.size() > 0) {
                                    StudentMarks sm = listm.iterator().next();
                                    tot_val += sm.getMarks();
                                }

                            }
                            avg = tot_val / list.size();

                        }

                        if (avg >= 40) {

                            pass++;
                            totPass++;

                        } else {
                            fail++;
                            totFail++;
                        }

                    }
                    headerPass.add(new MarksList(pass + "", "#9af893", "center", "bold", "1"));
                    headerFail.add(new MarksList(fail + "", "#f8939f", "center", "bold", "1"));
                    headerTot.add(new MarksList((pass + fail) + "", "#9fbcb0", "center", "bold", "1"));
                }
                headerPass.add(new MarksList(totPass + "", "#9af893", "center", "bold", "1"));
                headerFail.add(new MarksList(totFail + "", "#f8939f", "center", "bold", "1"));
                headerTot.add(new MarksList((totPass + totFail) + "", "#9fbcb0", "center", "bold", "1"));

                recordList.add(new StudentList(headerPass));
                recordList.add(new StudentList(headerFail));
                recordList.add(new StudentList(headerTot));

                String query = "SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassHasSubjectsId.gradeClassStreamId.gradeId.id='" + gradeName + "' and g.gradeClassStreamManagerId.yearId.name='" + year + "' and g.gradeClassHasSubjectsId.subjectsId.isActive='1' and g.gradeClassHasSubjectsId.gradeClassStreamId.schoolId.id='" + schoolName + "' group by g.gradeClassHasSubjectsId.subjectsId order by g.gradeClassHasSubjectsId.subjectsId.name ASC";
                List<GradeClassSubjectTeacher> listAS = uni.searchByQuery(query);
                for (GradeClassSubjectTeacher cc : listAS) {

                    List<MarksList> subheader = new ArrayList();

                    String queryt = "SELECT g FROM GradeClassStream g where g.gradeId.id='" + gradeName + "' and g.schoolId.id='" + schoolName + "' order by g.classesId.name ASC";
                    List<GradeClassStream> listASt = uni.searchByQuery(queryt);

                    subheader.add(new MarksList(cc.getGradeClassHasSubjectsId().getSubjectsId().getName(), "#9bf8be", "left", "bold", (listASt.size() + 2) + ""));

                    recordList.add(new StudentList(subheader));

                    List<MarksList> bodyA = new ArrayList();
                    bodyA.add(new MarksList("A", "#d4e3ec", "left", "bold", "1"));

                    List<MarksList> bodyB = new ArrayList();
                    bodyB.add(new MarksList("B", "#d4e3ec", "left", "bold", "1"));

                    List<MarksList> bodyC = new ArrayList();
                    bodyC.add(new MarksList("C", "#d4e3ec", "left", "bold", "1"));

                    List<MarksList> bodyS = new ArrayList();
                    bodyS.add(new MarksList("S", "#d4e3ec", "left", "bold", "1"));

                    List<MarksList> bodyW = new ArrayList();
                    bodyW.add(new MarksList("W", "#d4e3ec", "left", "bold", "1"));

                    List<MarksList> body31 = new ArrayList();
                    body31.add(new MarksList("31 - 40", "#88cdf7", "right", "bold", "1"));

                    List<MarksList> body21 = new ArrayList();
                    body21.add(new MarksList("21 - 30", "#88cdf7", "right", "bold", "1"));

                    List<MarksList> body11 = new ArrayList();
                    body11.add(new MarksList("11 - 20", "#88cdf7", "right", "bold", "1"));

                    List<MarksList> body0 = new ArrayList();
                    body0.add(new MarksList("0 - 10", "#88cdf7", "right", "bold", "1"));

                    int totA = 0;
                    int totB = 0;
                    int totC = 0;
                    int totS = 0;
                    int totW = 0;

                    int tot31 = 0;
                    int tot21 = 0;
                    int tot11 = 0;
                    int tot0 = 0;

                    for (GradeClassStream gcs : listASt) {

                        String querymA = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + year + "' and  g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + cc.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.classesId.id='" + gcs.getClassesId().getId() + "'  and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + schoolName + "' and g.isRemoved='0' and g.isMandatory=true and g.marks>=75";
                        List<StudentMarks> listmA = uni.searchByQuery(querymA);
                        bodyA.add(new MarksList(listmA.size() + "", "#ffffff", "center", "", "1"));
                        totA += listmA.size();

                        String querymB = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + year + "' and  g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + cc.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.classesId.id='" + gcs.getClassesId().getId() + "'  and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + schoolName + "' and g.isRemoved='0' and g.isMandatory=true and g.marks>=65 and g.marks<75";
                        List<StudentMarks> listmB = uni.searchByQuery(querymB);
                        bodyB.add(new MarksList(listmB.size() + "", "#ffffff", "center", "", "1"));
                        totB += listmB.size();

                        String querymC = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + year + "' and  g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + cc.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.classesId.id='" + gcs.getClassesId().getId() + "'  and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + schoolName + "' and g.isRemoved='0' and g.isMandatory=true and g.marks>=55 and g.marks<65";
                        List<StudentMarks> listmC = uni.searchByQuery(querymC);
                        bodyC.add(new MarksList(listmC.size() + "", "#ffffff", "center", "", "1"));
                        totC += listmC.size();

                        String querymS = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + year + "' and  g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + cc.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.classesId.id='" + gcs.getClassesId().getId() + "'  and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + schoolName + "' and g.isRemoved='0' and g.isMandatory=true and g.marks>=40 and g.marks<55";
                        List<StudentMarks> listmS = uni.searchByQuery(querymS);
                        bodyS.add(new MarksList(listmS.size() + "", "#ffffff", "center", "", "1"));
                        totS += listmS.size();

                        String querymW = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + year + "' and  g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + cc.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.classesId.id='" + gcs.getClassesId().getId() + "'  and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + schoolName + "' and g.isRemoved='0' and g.isMandatory=true and g.marks<40";
                        List<StudentMarks> listmW = uni.searchByQuery(querymW);
                        bodyW.add(new MarksList(listmW.size() + "", "#ffffff", "center", "", "1"));
                        totW += listmW.size();

                        String querym31 = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + year + "' and  g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + cc.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.classesId.id='" + gcs.getClassesId().getId() + "'  and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + schoolName + "' and g.isRemoved='0' and g.isMandatory=true and g.marks>=31 and g.marks<=40";
                        List<StudentMarks> listm31 = uni.searchByQuery(querym31);
                        body31.add(new MarksList(listm31.size() + "", "#dcdfe0", "center", "", "1"));
                        tot31 += listm31.size();

                        String querym21 = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + year + "' and  g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + cc.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.classesId.id='" + gcs.getClassesId().getId() + "'  and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + schoolName + "' and g.isRemoved='0' and g.isMandatory=true and g.marks>=21 and g.marks<=30";
                        List<StudentMarks> listm21 = uni.searchByQuery(querym21);
                        body21.add(new MarksList(listm21.size() + "", "#dcdfe0", "center", "", "1"));
                        tot21 += listm21.size();

                        String querym11 = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + year + "' and  g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + cc.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.classesId.id='" + gcs.getClassesId().getId() + "'  and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + schoolName + "' and g.isRemoved='0' and g.isMandatory=true and g.marks>=11 and g.marks<=20";
                        List<StudentMarks> listm11 = uni.searchByQuery(querym11);
                        body11.add(new MarksList(listm11.size() + "", "#dcdfe0", "center", "", "1"));
                        tot11 += listm11.size();

                        String querym0 = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + year + "' and  g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + cc.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.classesId.id='" + gcs.getClassesId().getId() + "'  and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + schoolName + "' and g.isRemoved='0' and g.isMandatory=true and g.marks>=0 and g.marks<=10";
                        List<StudentMarks> listm0 = uni.searchByQuery(querym0);
                        body0.add(new MarksList(listm0.size() + "", "#dcdfe0", "center", "", "1"));
                        tot0 += listm0.size();

                    }

                    bodyA.add(new MarksList(totA + "", "#ffffff", "center", "bold", "1"));
                    bodyB.add(new MarksList(totB + "", "#ffffff", "center", "bold", "1"));
                    bodyC.add(new MarksList(totC + "", "#ffffff", "center", "bold", "1"));
                    bodyS.add(new MarksList(totS + "", "#ffffff", "center", "bold", "1"));
                    bodyW.add(new MarksList(totW + "", "#ffffff", "center", "bold", "1"));

                    body31.add(new MarksList(tot31 + "", "#dcdfe0", "center", "bold", "1"));
                    body21.add(new MarksList(tot21 + "", "#dcdfe0", "center", "bold", "1"));
                    body11.add(new MarksList(tot11 + "", "#dcdfe0", "center", "bold", "1"));
                    body0.add(new MarksList(tot0 + "", "#dcdfe0", "center", "bold", "1"));

                    recordList.add(new StudentList(bodyA));
                    recordList.add(new StudentList(bodyB));
                    recordList.add(new StudentList(bodyC));
                    recordList.add(new StudentList(bodyS));
                    recordList.add(new StudentList(bodyW));

                    recordList.add(new StudentList(body31));
                    recordList.add(new StudentList(body21));
                    recordList.add(new StudentList(body11));
                    recordList.add(new StudentList(body0));

                }
            } else {

                String topic = "";

                String query_school = "";
                if (zoneName.equals("0")) {
                    query_school = ".educationDivisionId.educationZoneId.provinceId.id='" + Integer.parseInt(provinceName) + "'";

                    Province p = (Province) uni.find(Integer.parseInt(provinceName), Province.class);
                    topic = "Enhancement of Educational Achievements - " + p.getName() + " Province";

                } else if (divisionName.equals("0")) {
                    query_school = ".educationDivisionId.educationZoneId.id='" + Integer.parseInt(zoneName) + "'";

                    EducationZone p = (EducationZone) uni.find(Integer.parseInt(zoneName), EducationZone.class);
                    topic = "Enhancement of Educational Achievements - " + p.getName();

                } else if (schoolName.equals("0")) {
                    query_school = ".educationDivisionId.id='" + Integer.parseInt(divisionName) + "'";

                    EducationDivision p = (EducationDivision) uni.find(Integer.parseInt(divisionName), EducationDivision.class);
                    topic = "Enhancement of Educational Achievements - " + p.getName() + " Division";
                }

                List<MarksList> top_header = new ArrayList();
                top_header.add(new MarksList(topic, "#ffad33", "center", "bold", "2"));
                recordList.add(new StudentList(top_header));

                List<MarksList> header = new ArrayList();

                List<MarksList> headerPass = new ArrayList();
                List<MarksList> headerFail = new ArrayList();
                List<MarksList> headerTot = new ArrayList();

                Terms tr = (Terms) uni.find(Integer.parseInt(termName), Terms.class);

                header.add(new MarksList("Grade " + gradeName + " - " + year + " " + tr.getName(), "#ffad33", "center", "bold", "2"));

                recordList.add(new StudentList(header));

                headerPass.add(new MarksList("Pass", "#9af893", "center", "bold", "1"));
                headerFail.add(new MarksList("Fail", "#f8939f", "center", "bold", "1"));
                headerTot.add(new MarksList("Total", "#9fbcb0", "center", "bold", "1"));

                int pass = 0;
                int fail = 0;

                String query11 = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "' and g.gradeClassStreamManagerId.yearId.name='" + year + "' and g.gradeClassStreamManagerId.gradeClassStreamId.schoolId" + query_school + "  and g.isRemoved='0'";
                List<GradeClassStudents> listAS11 = uni.searchByQuery(query11);
                for (GradeClassStudents gcs : listAS11) {

                    double tot_val = 0;
                    double avg = 0;

                    String query = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.gradeClassHasSubjectsId.subjectsId.isActive='1' ";
                    List<GradeClassStudentsHasSubjects> list = uni.searchByQuery(query);
                    if (list.size() > 0) {

                        for (GradeClassStudentsHasSubjects gcshs : list) {

                            String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.id='" + gcshs.getGradeClassHasSubjectsId().getId() + "' and g.isRemoved='0' ";
                            List<StudentMarks> listm = uni.searchByQuerySingle(querym);
                            if (listm.size() > 0) {
                                StudentMarks sm = listm.iterator().next();
                                tot_val += sm.getMarks();
                            }

                        }
                        avg = tot_val / list.size();

                    }

                    if (avg >= 40) {

                        pass++;

                    } else {
                        fail++;
                    }

                }
                headerPass.add(new MarksList(pass + "", "#9af893", "center", "bold", "1"));
                headerFail.add(new MarksList(fail + "", "#f8939f", "center", "bold", "1"));
                headerTot.add(new MarksList((pass + fail) + "", "#9fbcb0", "center", "bold", "1"));

                recordList.add(new StudentList(headerPass));
                recordList.add(new StudentList(headerFail));
                recordList.add(new StudentList(headerTot));

                String query = "SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassHasSubjectsId.gradeClassStreamId.gradeId.id='" + gradeName + "' and g.gradeClassStreamManagerId.yearId.name='" + year + "' and g.gradeClassHasSubjectsId.subjectsId.isActive='1' and g.gradeClassHasSubjectsId.gradeClassStreamId.schoolId" + query_school + " group by g.gradeClassHasSubjectsId.subjectsId order by g.gradeClassHasSubjectsId.subjectsId.name ASC";
                List<GradeClassSubjectTeacher> listAS = uni.searchByQuery(query);
                for (GradeClassSubjectTeacher cc : listAS) {

                    List<MarksList> subheader = new ArrayList();

                    subheader.add(new MarksList(cc.getGradeClassHasSubjectsId().getSubjectsId().getName(), "#9bf8be", "left", "bold", "2"));

                    recordList.add(new StudentList(subheader));

                    List<MarksList> bodyA = new ArrayList();
                    bodyA.add(new MarksList("A", "#d4e3ec", "left", "bold", "1"));

                    List<MarksList> bodyB = new ArrayList();
                    bodyB.add(new MarksList("B", "#d4e3ec", "left", "bold", "1"));

                    List<MarksList> bodyC = new ArrayList();
                    bodyC.add(new MarksList("C", "#d4e3ec", "left", "bold", "1"));

                    List<MarksList> bodyS = new ArrayList();
                    bodyS.add(new MarksList("S", "#d4e3ec", "left", "bold", "1"));

                    List<MarksList> bodyW = new ArrayList();
                    bodyW.add(new MarksList("W", "#d4e3ec", "left", "bold", "1"));

                    List<MarksList> body31 = new ArrayList();
                    body31.add(new MarksList("31 - 40", "#88cdf7", "right", "bold", "1"));

                    List<MarksList> body21 = new ArrayList();
                    body21.add(new MarksList("21 - 30", "#88cdf7", "right", "bold", "1"));

                    List<MarksList> body11 = new ArrayList();
                    body11.add(new MarksList("11 - 20", "#88cdf7", "right", "bold", "1"));

                    List<MarksList> body0 = new ArrayList();
                    body0.add(new MarksList("0 - 10", "#88cdf7", "right", "bold", "1"));

                    String querymA = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + year + "' and  g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + cc.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "'   and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId" + query_school + " and g.isRemoved='0' and g.isMandatory=true and g.marks>=75";
                    List<StudentMarks> listmA = uni.searchByQuery(querymA);
                    bodyA.add(new MarksList(listmA.size() + "", "#ffffff", "center", "", "1"));

                    String querymB = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + year + "' and  g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + cc.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "'   and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId" + query_school + " and g.isRemoved='0' and g.isMandatory=true and g.marks>=65 and g.marks<75";
                    List<StudentMarks> listmB = uni.searchByQuery(querymB);
                    bodyB.add(new MarksList(listmB.size() + "", "#ffffff", "center", "", "1"));

                    String querymC = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + year + "' and  g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + cc.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "'   and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId" + query_school + " and g.isRemoved='0' and g.isMandatory=true and g.marks>=55 and g.marks<65";
                    List<StudentMarks> listmC = uni.searchByQuery(querymC);
                    bodyC.add(new MarksList(listmC.size() + "", "#ffffff", "center", "", "1"));

                    String querymS = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + year + "' and  g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + cc.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "'   and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId" + query_school + " and g.isRemoved='0' and g.isMandatory=true and g.marks>=40 and g.marks<55";
                    List<StudentMarks> listmS = uni.searchByQuery(querymS);
                    bodyS.add(new MarksList(listmS.size() + "", "#ffffff", "center", "", "1"));

                    String querymW = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + year + "' and  g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + cc.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "'   and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId" + query_school + " and g.isRemoved='0' and g.isMandatory=true and g.marks<40";
                    List<StudentMarks> listmW = uni.searchByQuery(querymW);
                    bodyW.add(new MarksList(listmW.size() + "", "#ffffff", "center", "", "1"));

                    String querym31 = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + year + "' and  g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + cc.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "'   and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId" + query_school + " and g.isRemoved='0' and g.isMandatory=true and g.marks>=31 and g.marks<=40";
                    List<StudentMarks> listm31 = uni.searchByQuery(querym31);
                    body31.add(new MarksList(listm31.size() + "", "#dcdfe0", "center", "", "1"));

                    String querym21 = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + year + "' and  g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + cc.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "'   and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId" + query_school + " and g.isRemoved='0' and g.isMandatory=true and g.marks>=21 and g.marks<=30";
                    List<StudentMarks> listm21 = uni.searchByQuery(querym21);
                    body21.add(new MarksList(listm21.size() + "", "#dcdfe0", "center", "", "1"));

                    String querym11 = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + year + "' and  g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + cc.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "'   and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId" + query_school + " and g.isRemoved='0' and g.isMandatory=true and g.marks>=11 and g.marks<=20";
                    List<StudentMarks> listm11 = uni.searchByQuery(querym11);
                    body11.add(new MarksList(listm11.size() + "", "#dcdfe0", "center", "", "1"));

                    String querym0 = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + year + "' and  g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + cc.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "'  and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId" + query_school + " and g.isRemoved='0' and g.isMandatory=true and g.marks>=0 and g.marks<=10";
                    List<StudentMarks> listm0 = uni.searchByQuery(querym0);
                    body0.add(new MarksList(listm0.size() + "", "#dcdfe0", "center", "", "1"));

                    recordList.add(new StudentList(bodyA));
                    recordList.add(new StudentList(bodyB));
                    recordList.add(new StudentList(bodyC));
                    recordList.add(new StudentList(bodyS));
                    recordList.add(new StudentList(bodyW));

                    recordList.add(new StudentList(body31));
                    recordList.add(new StudentList(body21));
                    recordList.add(new StudentList(body11));
                    recordList.add(new StudentList(body0));

                }

            }
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
        private String colspan;

        public MarksList(String marks, String color, String align, String weight, String colspan) {
            this.marks = marks;
            this.color = color;
            this.align = align;
            this.weight = weight;
            this.colspan = colspan;
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

        public String getColspan() {
            return colspan;
        }

        public void setColspan(String colspan) {
            this.colspan = colspan;
        }

    }
//    public class ValueList {
//
//        private String columnName;
//        private String value;
//
//        public ValueList(String columnName, String value) {
//            this.columnName = columnName;
//            this.value = value;
//
//        }
//
//        public String getColumnName() {
//            return columnName;
//        }
//
//        public void setColumnName(String columnName) {
//            this.columnName = columnName;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        public void setValue(String value) {
//            this.value = value;
//        }
//    }
//
//    public class PassFailList {
//
//        private String title;
//        private List<ValueList> valueList;
//
//        public PassFailList(String title, List<ValueList> valueList) {
//            this.title = title;
//            this.valueList = valueList;
//
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public List<ValueList> getValueList() {
//            return valueList;
//        }
//
//        public void setValueList(List<ValueList> valueList) {
//            this.valueList = valueList;
//        }
//
//    }
//
//    public class DataList {
//
//        private String subName;
//        private List<PassFailList> passFailList;
//
//        public DataList(String subName, List<PassFailList> passFailList) {
//
//            this.subName = subName;
//            this.passFailList = passFailList;
//
//        }
//
//        public String getSubName() {
//            return subName;
//        }
//
//        public void setSubName(String subName) {
//            this.subName = subName;
//        }
//
//        public List<PassFailList> getPassFailList() {
//            return passFailList;
//        }
//
//        public void setPassFailList(List<PassFailList> passFailList) {
//            this.passFailList = passFailList;
//        }
//
//    }

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

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(String indexNo) {
        this.indexNo = indexNo;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
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

    public List<StudentList> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<StudentList> recordList) {
        this.recordList = recordList;
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
