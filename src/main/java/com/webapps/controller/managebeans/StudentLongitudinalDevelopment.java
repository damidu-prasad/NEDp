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
public class StudentLongitudinalDevelopment implements Serializable {

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

    private String classTeacherName = "1";
    private List<SelectItem> classTeacherNameList = new ArrayList<SelectItem>();

    private BarChartModel barModel = new BarChartModel();

    private boolean disabledFiledProvince = false;
    private boolean disabledFiledZone = false;
    private boolean disabledFiledDivision = false;
    private boolean disabledFiledSchool = false;

    private String selectedYear = "";
    private String lastYear = "";

    private String studentName;
    private String indexNo;

    private String className = "0";
    private List<SelectItem> classNameList = new ArrayList<SelectItem>();

    private int def_province = 0;
    private int def_zone = 0;
    private int def_division = 0;
    private int def_school = 0;

    private List<StudentsList> studentList = new ArrayList();

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
        selectedYear = year;
        lastYear = (Integer.parseInt(year) - 1) + "";
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

    public void loadClassStudents() {
        FacesMessage msg = null;
        studentList.clear();

        selectedYear = year;
        lastYear = (Integer.parseInt(year) - 1) + "";

        List<StudentsList> stuList = new ArrayList();

        if (schoolName.equals("0") || schoolName.equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select School !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else if (className.equals("0") || className.equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select Class !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else {
            int k = 1;
            String query = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.gradeClassStreamId.id='" + this.className + "' and g.gradeClassStreamManagerId.yearId.name='" + year + "' and g.isRemoved='0'";
            List<GradeClassStudents> listAS1 = uni.searchByQuery(query);
            for (GradeClassStudents gcs : listAS1) {

                double allAVG = 0.0;
                double allCoeffVariance = 0.0;
                double lastYearAVG = 0.0;
                double lastYearCoeffVariance = 0.0;
                double selectedYearAVG = 0.0;
                double selectedYearCoeffVariance = 0.0;

                // all
                String querys = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassStudentsId.studentsId.id='" + gcs.getStudentsId().getId() + "'  and g.gradeClassStudentsId.isRemoved='0' and g.gradeClassHasSubjectsId.subjectsId.isActive='1'";
                List<GradeClassStudentsHasSubjects> list = uni.searchByQuery(querys);

                double allMarks = 0.0;

                List<Double> all_marks_list = new ArrayList();

                String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.studentsId.id='" + gcs.getStudentsId().getId() + "' and g.isMandatory='1' and g.isRemoved='0' and g.gradeClassHasSubjectsId.subjectsId.isActive='1'";
                List<StudentMarks> listm = uni.searchByQuery(querym);
                if (listm.size() > 0) {
                    for (StudentMarks sm : listm) {
                        allMarks += sm.getMarks();

                        all_marks_list.add(sm.getMarks());
                    }
                }
                if (list.size() > 0) {
                    allAVG = allMarks / (list.size()*3);
                }

                if (all_marks_list.size() > 0) {
                    double allvariance = 0;
                    for (int i = 0; i < all_marks_list.size(); i++) {
                        allvariance += (all_marks_list.get(i) - allAVG) * (all_marks_list.get(i) - allAVG);

                    }

                    if (allAVG > 0) {
                        allvariance /= all_marks_list.size();
                        allCoeffVariance = Math.sqrt(allvariance) / allAVG;
                    }

                }

                // selected year
                String querys_last = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassStudentsId.studentsId.id='" + gcs.getStudentsId().getId() + "' and  g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + lastYear + "' and g.gradeClassStudentsId.isRemoved='0' and g.gradeClassHasSubjectsId.subjectsId.isActive='1'";
                List<GradeClassStudentsHasSubjects> list_last = uni.searchByQuery(querys_last);

                double lsatMarks = 0.0;

                List<Double> last_marks_list = new ArrayList();

                String queryml = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.studentsId.id='" + gcs.getStudentsId().getId() + "' and  g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + lastYear + "' and g.isMandatory='1' and g.isRemoved='0' and g.gradeClassHasSubjectsId.subjectsId.isActive='1' ";
                List<StudentMarks> listml = uni.searchByQuery(queryml);
                if (listml.size() > 0) {
                    for (StudentMarks sm : listml) {
                        lsatMarks += sm.getMarks();

                        last_marks_list.add(sm.getMarks());
                    }
                }
                if (list_last.size() > 0) {
                    lastYearAVG = lsatMarks / (list_last.size()*3);
                }

                if (last_marks_list.size() > 0) {
                    double lastvariance = 0;
                    for (int i = 0; i < last_marks_list.size(); i++) {
                        lastvariance += (last_marks_list.get(i) - lastYearAVG) * (last_marks_list.get(i) - lastYearAVG);

                    }

                    if (lastYearAVG > 0) {
                        lastvariance /= last_marks_list.size();
                        lastYearCoeffVariance = Math.sqrt(lastvariance) / lastYearAVG;
                    }

                }
                // selected year
                String querys_selected = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassStudentsId.studentsId.id='" + gcs.getStudentsId().getId() + "' and  g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + selectedYear + "' and g.gradeClassStudentsId.isRemoved='0' and g.gradeClassHasSubjectsId.subjectsId.isActive='1'";
                List<GradeClassStudentsHasSubjects> list_selected = uni.searchByQuery(querys_selected);

                double selectedMarks = 0.0;

                List<Double> selected_marks_list = new ArrayList();

                String queryms = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.studentsId.id='" + gcs.getStudentsId().getId() + "' and  g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + selectedYear + "' and g.isMandatory='1' and g.isRemoved='0' and g.gradeClassHasSubjectsId.subjectsId.isActive='1' ";
                List<StudentMarks> listms = uni.searchByQuery(queryms);
                if (listms.size() > 0) {
                    for (StudentMarks sm : listms) {


                            selectedMarks += sm.getMarks();
                            if (sm.getGradeClassStudentsId().getStudentsId().getGeneralUserProfileId().getNameWithIn().equals("H.P.V.Sandun Kumara")) {

                                System.out.println(sm.getGradeClassStudentsId().getGradeClassStreamManagerId().getYearId().getName() + "|" + sm.getTermsId().getName() + "|" + sm.getMarks() + "|" + sm.getGradeClassHasSubjectsId().getSubjectsId().getName());
                            }

                            selected_marks_list.add(sm.getMarks());

                    }
                }
                if (list_selected.size() > 0) {
                    if (gcs.getStudentsId().getGeneralUserProfileId().getNameWithIn().equals("H.P.V.Sandun Kumara")) {
                        System.out.println(selectedMarks+" | "+list_selected.size());
                    }

                    
                    selectedYearAVG = selectedMarks / (list_selected.size()*3);
                }

                if (selected_marks_list.size() > 0) {
                    double selectedvariance = 0;
                    for (int i = 0; i < selected_marks_list.size(); i++) {
                        selectedvariance += (selected_marks_list.get(i) - selectedYearAVG) * (selected_marks_list.get(i) - selectedYearAVG);

                    }

                    if (selectedYearAVG > 0) {
                        selectedvariance /= selected_marks_list.size();
                        selectedYearCoeffVariance = Math.sqrt(selectedvariance) / selectedYearAVG;
                    }

                }

                stuList.add(new StudentsList(0, gcs.getId(), gcs.getStudentsId().getGeneralUserProfileId().getNameWithIn(), allAVG, allCoeffVariance, lastYearAVG, lastYearCoeffVariance, selectedYearAVG, selectedYearCoeffVariance, comlib.getRounded(allAVG, 2) + "", comlib.getRounded(allCoeffVariance, 4) + "", comlib.getRounded(lastYearAVG, 2) + "", comlib.getRounded(lastYearCoeffVariance, 4) + "", comlib.getRounded(selectedYearAVG, 2) + "", comlib.getRounded(selectedYearCoeffVariance, 4) + ""));

            }
            StudentsList[] addressArray = new StudentsList[stuList.size()];
            for (int i = 0; i < stuList.size(); i++) {

                addressArray[i] = stuList.get(i);
            }

            StudentsList[] s = SortArraysStudentLongitudinalDevelopment.GetArray(addressArray);

            int order = 1;
            for (int i = (s.length - 1); i >= 0; i--) {

                studentList.add(new StudentsList(order, s[i].getSid(), s[i].getName(), s[i].getAllAVG(), s[i].getAllVariance(), s[i].getLastYearAVG(), s[i].getLastYearVariance(), s[i].getSelectedYearAVG(), s[i].getSelectedYearVariance(), comlib.getRounded(s[i].getAllAVG(), 4) + "", comlib.getRounded(s[i].getAllVariance(), 4) + "", comlib.getRounded(s[i].getLastYearAVG(), 4) + "", comlib.getRounded(s[i].getLastYearVariance(), 4) + "", comlib.getRounded(s[i].getSelectedYearAVG(), 4) + "", comlib.getRounded(s[i].getSelectedYearVariance(), 4) + ""));
                order++;
            }
        }
    }

    public void loadStudentDetails(int sid) {

        GradeClassStudents s = (GradeClassStudents) uni.find(sid, GradeClassStudents.class);

        studentName = s.getStudentsId().getGeneralUserProfileId().getNameWithIn();
        indexNo = s.getStudentsId().getStudentId();

        createBarModel(s);
    }

    private void createBarModel(GradeClassStudents s) {

        BarChartModel model = new BarChartModel();

        String querys_selected1 = "SELECT g FROM GradeClassStudents g where g.studentsId.id='" + s.getStudentsId().getId() + "' group by g.gradeClassStreamManagerId.yearId ";
        List<GradeClassStudents> list_selected1 = uni.searchByQuery(querys_selected1);

        ChartSeries term1 = new ChartSeries();
        term1.setLabel("Term 1");

        ChartSeries term2 = new ChartSeries();
        term2.setLabel("Term 2");

        ChartSeries term3 = new ChartSeries();
        term3.setLabel("Term 3");

        for (GradeClassStudents gcs : list_selected1) {  // yesr wise
            String querys_selected = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassStudentsId.studentsId.id='" + gcs.getStudentsId().getId() + "' and  g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + gcs.getGradeClassStreamManagerId().getYearId().getName() + "' ";
            List<GradeClassStudentsHasSubjects> list_selected = uni.searchByQuery(querys_selected);

            double selectedMarks1 = 0.0;
            double selectedMarks2 = 0.0;
            double selectedMarks3 = 0.0;

            double term1Avg = 0.0;
            double term2Avg = 0.0;
            double term3Avg = 0.0;

            String queryms1 = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.studentsId.id='" + gcs.getStudentsId().getId() + "' and g.termsId.id='1' and g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + gcs.getGradeClassStreamManagerId().getYearId().getName() + "' and g.isMandatory='1' and g.isRemoved='0' ";
            List<StudentMarks> listms1 = uni.searchByQuery(queryms1);
            if (listms1.size() > 0) {
                for (StudentMarks sm : listms1) {
                    selectedMarks1 += sm.getMarks();
                }
            }
            if (list_selected.size() > 0) {
                term1Avg = selectedMarks1 / list_selected.size();
            }

            String queryms2 = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.studentsId.id='" + gcs.getStudentsId().getId() + "' and g.termsId.id='2' and g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + gcs.getGradeClassStreamManagerId().getYearId().getName() + "' and g.isMandatory='1' and g.isRemoved='0' ";
            List<StudentMarks> listms2 = uni.searchByQuery(queryms2);
            if (listms2.size() > 0) {
                for (StudentMarks sm : listms2) {
                    selectedMarks2 += sm.getMarks();
                }
            }
            if (list_selected.size() > 0) {
                term2Avg = selectedMarks2 / list_selected.size();
            }

            String queryms3 = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.studentsId.id='" + gcs.getStudentsId().getId() + "' and g.termsId.id='3' and g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + gcs.getGradeClassStreamManagerId().getYearId().getName() + "' and g.isMandatory='1' and g.isRemoved='0' ";
            List<StudentMarks> listms3 = uni.searchByQuery(queryms3);
            if (listms3.size() > 0) {
                for (StudentMarks sm : listms3) {
                    selectedMarks3 += sm.getMarks();
                }
            }
            if (list_selected.size() > 0) {
                term3Avg = selectedMarks3 / list_selected.size();
            }

            term1.set(gcs.getGradeClassStreamManagerId().getYearId().getName(), term1Avg);
            term2.set(gcs.getGradeClassStreamManagerId().getYearId().getName(), term2Avg);
            term3.set(gcs.getGradeClassStreamManagerId().getYearId().getName(), term3Avg);
        }
//        ChartSeries term1 = new ChartSeries();
//        term1.setLabel("Term 1");
//        term1.set("2004", 120);
//        term1.set("2005", 100);
//        term1.set("2006", 44);
//        term1.set("2007", 150);
//        term1.set("2008", 25);
//
//        ChartSeries term2 = new ChartSeries();
//        term2.setLabel("Term 2");
//        term2.set("2004", 52);
//        term2.set("2005", 60);
//        term2.set("2006", 110);
//        term2.set("2007", 135);
//        term2.set("2008", 120);
//
//        ChartSeries term3 = new ChartSeries();
//        term3.setLabel("Term 3");
//        term3.set("2004", 52);
//        term3.set("2005", 60);
//        term3.set("2006", 110);
//        term3.set("2007", 135);
//        term3.set("2008", 120);

        model.addSeries(term1);
        model.addSeries(term2);
        model.addSeries(term3);

        barModel = model;

        barModel.setTitle("Student Academic Performance - Overall");
        barModel.setLegendPosition("ne");

        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Year");

        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Average");
        yAxis.setMin(0);
        yAxis.setMax(100);
    }

    public class StudentsList {

        private int no;
        private int sid;
        private String name;
        private double allAVG;
        private double allVariance;
        private double lastYearAVG;
        private double lastYearVariance;
        private double selectedYearAVG;
        private double selectedYearVariance;
        private String allAVGVal;
        private String allVarianceVal;
        private String lastYearAVGVal;
        private String lastYearVarianceVal;
        private String selectedYearAVGVal;
        private String selectedYearVarianceVal;

        public StudentsList(int no, int sid, String name, double allAVG, double allVariance, double lastYearAVG, double lastYearVariance, double selectedYearAVG, double selectedYearVariance, String allAVGVal, String allVarianceVal, String lastYearAVGVal, String lastYearVarianceVal, String selectedYearAVGVal, String selectedYearVarianceVal) {
            this.no = no;
            this.sid = sid;
            this.name = name;
            this.allAVG = allAVG;
            this.allVariance = allVariance;
            this.lastYearAVG = lastYearAVG;
            this.lastYearVariance = lastYearVariance;
            this.selectedYearAVG = selectedYearAVG;
            this.selectedYearVariance = selectedYearVariance;
            this.allAVGVal = allAVGVal;
            this.allVarianceVal = allVarianceVal;
            this.lastYearAVGVal = lastYearAVGVal;
            this.lastYearVarianceVal = lastYearVarianceVal;
            this.selectedYearAVGVal = selectedYearAVGVal;
            this.selectedYearVarianceVal = selectedYearVarianceVal;
        }

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }

        public double getAllAVG() {
            return allAVG;
        }

        public void setAllAVG(double allAVG) {
            this.allAVG = allAVG;
        }

        public double getAllVariance() {
            return allVariance;
        }

        public void setAllVariance(double allVariance) {
            this.allVariance = allVariance;
        }

        public double getLastYearAVG() {
            return lastYearAVG;
        }

        public void setLastYearAVG(double lastYearAVG) {
            this.lastYearAVG = lastYearAVG;
        }

        public double getLastYearVariance() {
            return lastYearVariance;
        }

        public void setLastYearVariance(double lastYearVariance) {
            this.lastYearVariance = lastYearVariance;
        }

        public double getSelectedYearAVG() {
            return selectedYearAVG;
        }

        public void setSelectedYearAVG(double selectedYearAVG) {
            this.selectedYearAVG = selectedYearAVG;
        }

        public double getSelectedYearVariance() {
            return selectedYearVariance;
        }

        public void setSelectedYearVariance(double selectedYearVariance) {
            this.selectedYearVariance = selectedYearVariance;
        }

        public String getAllAVGVal() {
            return allAVGVal;
        }

        public void setAllAVGVal(String allAVGVal) {
            this.allAVGVal = allAVGVal;
        }

        public String getAllVarianceVal() {
            return allVarianceVal;
        }

        public void setAllVarianceVal(String allVarianceVal) {
            this.allVarianceVal = allVarianceVal;
        }

        public String getLastYearAVGVal() {
            return lastYearAVGVal;
        }

        public void setLastYearAVGVal(String lastYearAVGVal) {
            this.lastYearAVGVal = lastYearAVGVal;
        }

        public String getLastYearVarianceVal() {
            return lastYearVarianceVal;
        }

        public void setLastYearVarianceVal(String lastYearVarianceVal) {
            this.lastYearVarianceVal = lastYearVarianceVal;
        }

        public String getSelectedYearAVGVal() {
            return selectedYearAVGVal;
        }

        public void setSelectedYearAVGVal(String selectedYearAVGVal) {
            this.selectedYearAVGVal = selectedYearAVGVal;
        }

        public String getSelectedYearVarianceVal() {
            return selectedYearVarianceVal;
        }

        public void setSelectedYearVarianceVal(String selectedYearVarianceVal) {
            this.selectedYearVarianceVal = selectedYearVarianceVal;
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

    public List<StudentsList> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<StudentsList> studentList) {
        this.studentList = studentList;
    }

    public String getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(String selectedYear) {
        this.selectedYear = selectedYear;
    }

    public String getLastYear() {
        return lastYear;
    }

    public void setLastYear(String lastYear) {
        this.lastYear = lastYear;
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
