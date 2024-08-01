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
import com.ejb.model.entity.EducationDivision;
import com.ejb.model.entity.EducationZone;
import com.ejb.model.entity.EducationalAreaMean;
import com.ejb.model.entity.Grade;
import com.ejb.model.entity.GradeClassStream;
import com.ejb.model.entity.GradeClassStudents;
import com.ejb.model.entity.GradeClassStudentsHasSubjects;
import com.ejb.model.entity.GradeClassSubjectTeacher;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.Province;
import com.ejb.model.entity.School;
import com.ejb.model.entity.StudentMarks;
import com.ejb.model.entity.Subjects;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.TeacherClassSubjectMean;
import com.ejb.model.entity.Terms;
import com.ejb.model.entity.Year;
import com.webapps.controller.utilities.SortArraysTeacherPerformanceAnalyzer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Thilini Madagama
 */
@ManagedBean(name = "teacherPerformanceAnalyzer")
@ViewScoped
public class TeacherPerformanceAnalyzer {

    private String provinceName = "0";
    private List<SelectItem> provinceNameList = new ArrayList<SelectItem>();

    private String zoneName = "0";
    private List<SelectItem> zoneNameList = new ArrayList<SelectItem>();

    private String divisionName = "0";
    private List<SelectItem> divisionNameList = new ArrayList<SelectItem>();

    private String schoolName = "0";
    private List<SelectItem> schoolNameList = new ArrayList<SelectItem>();

    private String teacherName = "0";
    private List<SelectItem> teacherNameList = new ArrayList<SelectItem>();

    private int sliderMin = 0;
    private int slidrMax = 100;

    private boolean disabledFiledProvince = false;
    private boolean disabledFiledZone = false;
    private boolean disabledFiledDivision = false;
    private boolean disabledFiledSchool = false;

    private List<MeanSchoolList> meanSchoolList = new ArrayList();
    private List<MeanSchoolList> meanOldSchoolList = new ArrayList();
    private List<AnalysisSummary> analysisSummary = new ArrayList();

    private String year = "0";
    private List<SelectItem> YearList = new ArrayList<SelectItem>();

    private String gradeName = "0";
    private List<SelectItem> gradeNameList = new ArrayList<SelectItem>();

    private String subjectName = "0";
    private List<SelectItem> subjectNameList = new ArrayList<SelectItem>();

    private String termName = "0";
    private List<SelectItem> termNameList = new ArrayList<SelectItem>();

    private int studentCount = 0;
    private int old_studentCount = 0;

    private boolean is_slider;
    private boolean is_studentCount;
    private int def_province = 0;
    private int def_zone = 0;
    private int def_division = 0;
    private int def_school = 0;

    private List<GradeClassStudents> gcs_list_filter = new ArrayList();

    private List<StudentCountSchoolList> scSchoolList = new ArrayList();

    HttpServletResponse response;
    HttpServletRequest request;
    private ComLib comlib;
    private ComPath comPath;

    @EJB
    private UniDBLocal uni;
    LoginSession ls;

    @EJB
    private ComDev comDiv;

    @EJB
    StoredProcedures stored;

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
        getProvinceNameList().add(new SelectItem("0", "All"));

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

        // Get Term
        String queryt = "SELECT g FROM Terms g  order by g.id ASC";
        List<Terms> list_term = uni.searchByQuery(queryt);
        for (Terms cc : list_term) {

            getTermNameList().add(new SelectItem(cc.getId(), cc.getName()));
        }
        // Get Grade

        getGradeNameList().add(new SelectItem("0", "All"));
        String queryc = "SELECT g FROM Grade g  order by g.id ASC";
        List<Grade> list_class = uni.searchByQuery(queryc);
        for (Grade cc : list_class) {

            getGradeNameList().add(new SelectItem(cc.getId(), cc.getName()));
        }

        // Get Subject
        getSubjectNameList().add(new SelectItem("0", "All"));
        String querys = "SELECT g FROM Subjects g where g.isActive='1' order by g.name ASC";
        List<Subjects> list_sub = uni.searchByQuery(querys);
        for (Subjects cc : list_sub) {

            getSubjectNameList().add(new SelectItem(cc.getId(), cc.getName()));
        }
    }

    public String getEducationZoneList() {

        getZoneNameList().clear();

        // Get Province
        getZoneNameList().add(new SelectItem("0", "All"));

        String query = "SELECT g FROM EducationZone g where g.provinceId.id='" + Integer.parseInt(getProvinceName()) + "' order by g.name ASC";
        List<EducationZone> listAS = uni.searchByQuery(query);
        for (EducationZone cc : listAS) {

            getZoneNameList().add(new SelectItem(cc.getId(), cc.getName()));
        }
        setZoneName(getDef_zone() + "");

        getEducationDivisionList();

        return null;

    }

    public String getEducationDivisionList() {

        getDivisionNameList().clear();

        // Get Province
        getDivisionNameList().add(new SelectItem("0", "All"));

        String query = "SELECT g FROM EducationDivision g where g.educationZoneId.id='" + Integer.parseInt(getZoneName()) + "' order by g.name ASC";
        List<EducationDivision> listAS = uni.searchByQuery(query);
        for (EducationDivision cc : listAS) {

            getDivisionNameList().add(new SelectItem(cc.getId(), cc.getName()));
        }
        System.out.println("w1");

        System.out.println("w2");
        setDivisionName(getDef_division() + "");
        getSchoolList();

        return null;
    }

    public String getSchoolList() {

        getSchoolNameList().clear();
        getSchoolNameList().add(new SelectItem("0", "All"));
        if (!divisionName.equals("0")) {

            // Get Province
            String query = "SELECT g FROM School g where g.educationDivisionId.id='" + Integer.parseInt(getDivisionName()) + "' order by g.generalOrganizationProfileId.name ASC";
            List<School> listAS = uni.searchByQuery(query);
            for (School cc : listAS) {

                getSchoolNameList().add(new SelectItem(cc.getId(), cc.getGeneralOrganizationProfileId().getName()));
            }
        }
        setSchoolName(getDef_school() + "");
        getTeachersList();
        return null;
    }

    public String getTeachersList() {

        getTeacherNameList().clear();
        getTeacherNameList().add(new SelectItem("0", "All"));
        if (!schoolName.equals("0")) {

            // Get Province
            String query = "SELECT g FROM Teacher g where g.schoolId.id='" + Integer.parseInt(getSchoolName()) + "' and g.isActive='1' order by g.generalUserProfileId.nameWithIn ASC";
            List<Teacher> listAS = uni.searchByQuery(query);
            for (Teacher cc : listAS) {

                getTeacherNameList().add(new SelectItem(cc.getId(), cc.getGeneralUserProfileId().getNameWithIn()));
            }
        }

        return null;
    }

    public void loadSummary() {

        analysisSummary.clear();

        if (provinceName.equals("0") && zoneName.equals("0") && divisionName.equals("0") && schoolName.equals("0")) {

            double mean = 0.0;
            List<EducationalAreaMean> list_area = uni.searchByQuerySingle("SELECT g FROM EducationalAreaMean g where g.termsId.id='" + termName + "' and g.yearId.id='" + year + "' and g.educationalAreaMeanTypeId.id='1' and g.reference='0' ");
            if (list_area.size() > 0) {
                mean = list_area.iterator().next().getMeanValue();

            }

            List<AnalysisSummaryRecord> asr_list = new ArrayList();
            asr_list.add(new AnalysisSummaryRecord("Mean of the All Province", comlib.getDouble(mean)));
            analysisSummary.add(new AnalysisSummary("Province", asr_list));

            System.out.println("awa1");
        } else if ((!provinceName.equals("0")) && zoneName.equals("0") && divisionName.equals("0") && schoolName.equals("0")) {
            System.out.println("awa2");

            double mean_all_province = 0.0;
            List<EducationalAreaMean> list_area = uni.searchByQuerySingle("SELECT g FROM EducationalAreaMean g where g.termsId.id='" + termName + "' and g.yearId.id='" + year + "' and g.educationalAreaMeanTypeId.id='1' and g.reference='0' ");
            if (list_area.size() > 0) {
                mean_all_province = list_area.iterator().next().getMeanValue();

            }

            double mean_selected_province = 0.0;
            List<EducationalAreaMean> list_selected_province = uni.searchByQuerySingle("SELECT g FROM EducationalAreaMean g where g.termsId.id='" + termName + "' and g.yearId.id='" + year + "' and g.educationalAreaMeanTypeId.id='1' and g.reference='" + provinceName + "' ");
            if (list_selected_province.size() > 0) {
                mean_selected_province = list_selected_province.iterator().next().getMeanValue();

            }

            Province p = (Province) uni.find(Integer.parseInt(provinceName), Province.class);

            List<AnalysisSummaryRecord> asr_list_province = new ArrayList();
            asr_list_province.add(new AnalysisSummaryRecord("Mean of the All Province", comlib.getDouble(mean_all_province)));
            asr_list_province.add(new AnalysisSummaryRecord("Mean of the " + p.getName() + " Province", comlib.getDouble(mean_selected_province)));
            analysisSummary.add(new AnalysisSummary("Province", asr_list_province));

        } else if ((!provinceName.equals("0")) && (!zoneName.equals("0")) && divisionName.equals("0") && schoolName.equals("0")) {
            // province
            double mean_all_province = 0.0;
            List<EducationalAreaMean> list_area = uni.searchByQuerySingle("SELECT g FROM EducationalAreaMean g where g.termsId.id='" + termName + "' and g.yearId.id='" + year + "' and g.educationalAreaMeanTypeId.id='1' and g.reference='0' ");
            if (list_area.size() > 0) {
                mean_all_province = list_area.iterator().next().getMeanValue();

            }

            double mean_selected_province = 0.0;
            List<EducationalAreaMean> list_selected_province = uni.searchByQuerySingle("SELECT g FROM EducationalAreaMean g where g.termsId.id='" + termName + "' and g.yearId.id='" + year + "' and g.educationalAreaMeanTypeId.id='1' and g.reference='" + provinceName + "' ");
            if (list_selected_province.size() > 0) {
                mean_selected_province = list_selected_province.iterator().next().getMeanValue();

            }

            Province p = (Province) uni.find(Integer.parseInt(provinceName), Province.class);

            List<AnalysisSummaryRecord> asr_list_province = new ArrayList();
            asr_list_province.add(new AnalysisSummaryRecord("Mean of the All Province", comlib.getDouble(mean_all_province)));
            asr_list_province.add(new AnalysisSummaryRecord("Mean of the " + p.getName() + " Province", comlib.getDouble(mean_selected_province)));
            analysisSummary.add(new AnalysisSummary("Province", asr_list_province));
            // zone

            double mean_selected_zone = 0.0;
            List<EducationalAreaMean> list_selected_zone = uni.searchByQuerySingle("SELECT g FROM EducationalAreaMean g where g.termsId.id='" + termName + "' and g.yearId.id='" + year + "' and g.educationalAreaMeanTypeId.id='2' and g.reference='" + zoneName + "' ");
            if (list_selected_zone.size() > 0) {
                mean_selected_zone = list_selected_zone.iterator().next().getMeanValue();

            }

            EducationZone z = (EducationZone) uni.find(Integer.parseInt(zoneName), EducationZone.class);

            List<AnalysisSummaryRecord> asr_list_zone = new ArrayList();
            asr_list_zone.add(new AnalysisSummaryRecord("Mean of the All Zones of " + p.getName() + " Province", comlib.getDouble(mean_selected_province)));
            asr_list_zone.add(new AnalysisSummaryRecord("Mean of the " + z.getName() + "", comlib.getDouble(mean_selected_zone)));
            analysisSummary.add(new AnalysisSummary("Zone", asr_list_zone));

        } else if ((!provinceName.equals("0")) && (!zoneName.equals("0")) && (!divisionName.equals("0")) && schoolName.equals("0")) {
            // province
            double mean_all_province = 0.0;
            List<EducationalAreaMean> list_area = uni.searchByQuerySingle("SELECT g FROM EducationalAreaMean g where g.termsId.id='" + termName + "' and g.yearId.id='" + year + "' and g.educationalAreaMeanTypeId.id='1' and g.reference='0' ");
            if (list_area.size() > 0) {
                mean_all_province = list_area.iterator().next().getMeanValue();

            }
            double mean_selected_province = 0.0;
            List<EducationalAreaMean> list_selected_province = uni.searchByQuerySingle("SELECT g FROM EducationalAreaMean g where g.termsId.id='" + termName + "' and g.yearId.id='" + year + "' and g.educationalAreaMeanTypeId.id='1' and g.reference='" + provinceName + "' ");
            if (list_selected_province.size() > 0) {
                mean_selected_province = list_selected_province.iterator().next().getMeanValue();

            }

            Province p = (Province) uni.find(Integer.parseInt(provinceName), Province.class);

            List<AnalysisSummaryRecord> asr_list_province = new ArrayList();
            asr_list_province.add(new AnalysisSummaryRecord("Mean of the All Province", comlib.getDouble(mean_all_province)));
            asr_list_province.add(new AnalysisSummaryRecord("Mean of the " + p.getName() + " Province", comlib.getDouble(mean_selected_province)));
            analysisSummary.add(new AnalysisSummary("Province", asr_list_province));
            // zone

            double mean_selected_zone = 0.0;
            List<EducationalAreaMean> list_selected_zone = uni.searchByQuerySingle("SELECT g FROM EducationalAreaMean g where g.termsId.id='" + termName + "' and g.yearId.id='" + year + "' and g.educationalAreaMeanTypeId.id='2' and g.reference='" + zoneName + "' ");
            if (list_selected_zone.size() > 0) {
                mean_selected_zone = list_selected_zone.iterator().next().getMeanValue();

            }

            EducationZone z = (EducationZone) uni.find(Integer.parseInt(zoneName), EducationZone.class);

            List<AnalysisSummaryRecord> asr_list_zone = new ArrayList();
            asr_list_zone.add(new AnalysisSummaryRecord("Mean of the All Zones of " + p.getName() + " Province", comlib.getDouble(mean_selected_province)));
            asr_list_zone.add(new AnalysisSummaryRecord("Mean of the " + z.getName() + "", comlib.getDouble(mean_selected_zone)));
            analysisSummary.add(new AnalysisSummary("Zone", asr_list_zone));

            // division
            double mean_selected_division = 0.0;
            List<EducationalAreaMean> list_selected_division = uni.searchByQuerySingle("SELECT g FROM EducationalAreaMean g where g.termsId.id='" + termName + "' and g.yearId.id='" + year + "' and g.educationalAreaMeanTypeId.id='3' and g.reference='" + divisionName + "' ");
            if (list_selected_division.size() > 0) {
                mean_selected_division = list_selected_division.iterator().next().getMeanValue();

            }

            EducationDivision d = (EducationDivision) uni.find(Integer.parseInt(divisionName), EducationDivision.class);

            List<AnalysisSummaryRecord> asr_list_division = new ArrayList();
            asr_list_division.add(new AnalysisSummaryRecord("Mean of the All Divisions of " + z.getName() + "", comlib.getDouble(mean_selected_zone)));
            asr_list_division.add(new AnalysisSummaryRecord("Mean of the " + d.getName() + " Division", comlib.getDouble(mean_selected_division)));
            analysisSummary.add(new AnalysisSummary("Division", asr_list_division));

        } else if ((!provinceName.equals("0")) && (!zoneName.equals("0")) && (!divisionName.equals("0")) && (!schoolName.equals("0"))) {
            // province
            double mean_all_province = 0.0;
            List<EducationalAreaMean> list_area = uni.searchByQuerySingle("SELECT g FROM EducationalAreaMean g where g.termsId.id='" + termName + "' and g.yearId.id='" + year + "' and g.educationalAreaMeanTypeId.id='1' and g.reference='0' ");
            if (list_area.size() > 0) {
                mean_all_province = list_area.iterator().next().getMeanValue();

            }

            double mean_selected_province = 0.0;
            List<EducationalAreaMean> list_selected_province = uni.searchByQuerySingle("SELECT g FROM EducationalAreaMean g where g.termsId.id='" + termName + "' and g.yearId.id='" + year + "' and g.educationalAreaMeanTypeId.id='1' and g.reference='" + provinceName + "' ");
            if (list_selected_province.size() > 0) {
                mean_selected_province = list_selected_province.iterator().next().getMeanValue();

            }
            Province p = (Province) uni.find(Integer.parseInt(provinceName), Province.class);

            List<AnalysisSummaryRecord> asr_list_province = new ArrayList();
            asr_list_province.add(new AnalysisSummaryRecord("Mean of the All Province", comlib.getDouble(mean_all_province)));
            asr_list_province.add(new AnalysisSummaryRecord("Mean of the " + p.getName() + " Province", comlib.getDouble(mean_selected_province)));
            analysisSummary.add(new AnalysisSummary("Province", asr_list_province));
            // zone

            double mean_selected_zone = 0.0;
            List<EducationalAreaMean> list_selected_zone = uni.searchByQuerySingle("SELECT g FROM EducationalAreaMean g where g.termsId.id='" + termName + "' and g.yearId.id='" + year + "' and g.educationalAreaMeanTypeId.id='2' and g.reference='" + zoneName + "' ");
            if (list_selected_zone.size() > 0) {
                mean_selected_zone = list_selected_zone.iterator().next().getMeanValue();

            }
            EducationZone z = (EducationZone) uni.find(Integer.parseInt(zoneName), EducationZone.class);

            List<AnalysisSummaryRecord> asr_list_zone = new ArrayList();
            asr_list_zone.add(new AnalysisSummaryRecord("Mean of the All Zones of " + p.getName() + " Province", comlib.getDouble(mean_selected_province)));
            asr_list_zone.add(new AnalysisSummaryRecord("Mean of the " + z.getName(), comlib.getDouble(mean_selected_zone)));
            analysisSummary.add(new AnalysisSummary("Zone", asr_list_zone));

            // division
            double mean_selected_division = 0.0;
            List<EducationalAreaMean> list_selected_division = uni.searchByQuerySingle("SELECT g FROM EducationalAreaMean g where g.termsId.id='" + termName + "' and g.yearId.id='" + year + "' and g.educationalAreaMeanTypeId.id='3' and g.reference='" + divisionName + "' ");
            if (list_selected_division.size() > 0) {
                mean_selected_division = list_selected_division.iterator().next().getMeanValue();

            }
            EducationDivision d = (EducationDivision) uni.find(Integer.parseInt(divisionName), EducationDivision.class);

            List<AnalysisSummaryRecord> asr_list_division = new ArrayList();
            asr_list_division.add(new AnalysisSummaryRecord("Mean of the All Divisions of " + z.getName() + "", comlib.getDouble(mean_selected_zone)));
            asr_list_division.add(new AnalysisSummaryRecord("Mean of the " + d.getName() + " Division", comlib.getDouble(mean_selected_division)));
            analysisSummary.add(new AnalysisSummary("Division", asr_list_division));

            // school
            double mean_selected_school = 0.0;
            List<EducationalAreaMean> list_selected_school = uni.searchByQuerySingle("SELECT g FROM EducationalAreaMean g where g.termsId.id='" + termName + "' and g.yearId.id='" + year + "' and g.educationalAreaMeanTypeId.id='4' and g.reference='" + schoolName + "' ");
            if (list_selected_school.size() > 0) {
                mean_selected_school = list_selected_school.iterator().next().getMeanValue();

            }

            School s = (School) uni.find(Integer.parseInt(schoolName), School.class);

            List<AnalysisSummaryRecord> asr_list_school = new ArrayList();
            asr_list_school.add(new AnalysisSummaryRecord("Mean of the All Schools of " + d.getName() + " Division", comlib.getDouble(mean_selected_division)));
            asr_list_school.add(new AnalysisSummaryRecord("Mean of the " + s.getGeneralOrganizationProfileId().getName(), comlib.getDouble(mean_selected_school)));
            analysisSummary.add(new AnalysisSummary("School", asr_list_school));

        }

        loadTeacherPerformanceAnalyzerReport();
    }

//    public List<School> getProvinceSchoolList(int pid) {
//
//        String query = "";
//        if (pid != 0) {
//
//            query = " where g.educationDivisionId.educationZoneId.provinceId.id='" + pid + "'";
//        }
//
//        String query_all = "SELECT g FROM School g " + query + " order by g.generalOrganizationProfileId.name ASC";
//        List<School> sc_list_all = uni.searchByQuery(query_all);
//
//        return sc_list_all;
//    }
//    public List<School> getZoneSchoolList(int zid) {
//
//        String query = "SELECT g FROM School g where g.educationDivisionId.educationZoneId.id='" + zid + "' order by g.generalOrganizationProfileId.name ASC";
//
//        List<School> sc_list_all = uni.searchByQuery(query);
//
//        return sc_list_all;
//    }
//
//    public List<School> getDivisionSchoolList(int did) {
//
//        String query = "SELECT g FROM School g where g.educationDivisionId.id='" + did + "' order by g.generalOrganizationProfileId.name ASC";
//
//        List<School> sc_list_all = uni.searchByQuery(query);
//
//        return sc_list_all;
//    }
//
//    public List<School> getSchoolList(int sid) {
//
//        String query = "SELECT g FROM School g where g.id='" + sid + "' order by g.generalOrganizationProfileId.name ASC";
//
//        List<School> sc_list_all = uni.searchByQuery(query);
//
//        return sc_list_all;
//    }
//
//    public double getMean(List<School> sc_list) {
//
//        double mean = 0.0;
//        double totMarks = 0.0;
//        int totStudents = 0;
//        String scids = "";
//        int k = 0;
////        for (School school : sc_list) {
////            if (k == 0) {
////                scids = school.getId() + "";
////                k++;
////            } else {
////
////                scids += "," + school.getId();
////            }
////        }
//        for (School school : sc_list) {
//
////            List<GradeClassSubjectTeacher> list_gcst_individualy = uni.searchByQuery("SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.yearId.id='" + Integer.parseInt(year) + "' and g.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + school.getId() + "'  group by g.teacherId.id");
////            for (GradeClassSubjectTeacher gcst_individualy : list_gcst_individualy) { // Retrive group of subject Teachers
////                double teacherTotMarks = 0.0;
////                int teacherTotStudents = 0;
////                List<GradeClassSubjectTeacher> list_gcst = uni.searchByQuery("SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.yearId.id='" + Integer.parseInt(year) + "' and g.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + school.getId() + "' and g.teacherId.id='" + gcst_individualy.getTeacherId().getId() + "'  ");
////                for (GradeClassSubjectTeacher gcst : list_gcst) { // me teacher me awurudde me iskole ugannapu panthi tika Ex : 3A,4A,4B
//            List<GradeClassStudentsHasSubjects> list_gcshs = uni.searchByQuery("SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + school.getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.id='" + Integer.parseInt(year) + "' and g.gradeClassStudentsId.isRemoved='0'");
////                    teacherTotStudents += list_gcshs.size();
//            totStudents += list_gcshs.size();
//            for (GradeClassStudentsHasSubjects gcshs : list_gcshs) {
//                System.out.println("awap");
//                String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcshs.getGradeClassStudentsId().getId() + "' and g.termsId.id='" + Integer.parseInt(termName) + "' and g.gradeClassHasSubjectsId.id='" + gcshs.getGradeClassHasSubjectsId().getId() + "' and g.isRemoved='0' ";
//                List<StudentMarks> listm = uni.searchByQuerySingle(querym);
//                if (listm.size() > 0) {
////                            teacherTotMarks += listm.iterator().next().getMarks();
//                    totMarks += listm.iterator().next().getMarks();
//                } else {
//
////                            teacherTotMarks += 0.0;
//                    totMarks += 0.0;
//                }
//            }
//
//        }
////            }
////        }
//        if (totStudents != 0) {
//
//            mean = totMarks / totStudents;
//        }
//        return mean;
//    }
    public synchronized void loadTeacherPerformanceAnalyzerReport() {

        meanSchoolList.clear();
        meanOldSchoolList.clear();
        scSchoolList.clear();
        List<School> sc_list = new ArrayList();

        is_slider = true;
        is_studentCount = true;

        sliderMin = 0;
        slidrMax = 100;

        List<MeanSchoolList> unorder_meanSchoolArray = new ArrayList();
        List<MeanTeacherList> allTeacherArray = new ArrayList();

        if (provinceName.equals("0")) {
            String query = "SELECT g FROM School g  order by g.generalOrganizationProfileId.name ASC";
            sc_list = uni.searchByQuery(query);
            System.out.println("awap1");
        } else if (zoneName.equals("0")) {
            String query = "SELECT g FROM School g where g.educationDivisionId.educationZoneId.provinceId.id='" + Integer.parseInt(provinceName) + "' order by g.generalOrganizationProfileId.name ASC";

            sc_list = uni.searchByQuery(query);
            System.out.println("awap2");
        } else if (divisionName.equals("0")) {
            String query = "SELECT g FROM School g where g.educationDivisionId.educationZoneId.id='" + Integer.parseInt(zoneName) + "' order by g.generalOrganizationProfileId.name ASC";

            sc_list = uni.searchByQuery(query);
            System.out.println("awap3");
        } else if (schoolName.equals("0")) {
            String query = "SELECT g FROM School g where g.educationDivisionId.id='" + Integer.parseInt(divisionName) + "' order by g.generalOrganizationProfileId.name ASC";

            sc_list = uni.searchByQuery(query);
            System.out.println("awap4");
        } else if (!schoolName.equals("0")) {
            String query = "SELECT g FROM School g where g.id='" + Integer.parseInt(schoolName) + "'";

            sc_list = uni.searchByQuery(query);
            System.out.println("awap5");
        }

        String gradeQuery = "";
        String teacherQuery = "";
        String subjectQuery = "";

        if (!gradeName.equals("0")) {
            gradeQuery = " and g.gradeClassStreamId.gradeId.id='" + Integer.parseInt(gradeName) + "' ";

        }
        if (!teacherName.equals("0")) {
            teacherQuery = " and g.teacherId.id='" + Integer.parseInt(teacherName) + "' ";

        }
        if (!subjectName.equals("0")) {
            subjectQuery = " and g.subjectsId.id='" + Integer.parseInt(subjectName) + "' ";

        }
        System.out.println("six " + sc_list.size());
        for (School school : sc_list) {
            System.out.println("awas" + school.getId());
            List<MeanTeacherList> meanTeacherList = new ArrayList();

            // code start
            List<TeacherClassSubjectMean> list_tcsm_individualy = uni.searchByQuery("SELECT g FROM TeacherClassSubjectMean g where g.yearId.id='" + Integer.parseInt(year) + "'  and g.teacherId.schoolId.id='" + school.getId() + "' and g.teacherId.isActive='1'  " + gradeQuery + teacherQuery + subjectQuery + " group by g.teacherId.id");
            for (TeacherClassSubjectMean gcst_individualy : list_tcsm_individualy) { // Retrive group of subject Teachers

                List<MeanSubjectList> meanSubjectList = new ArrayList();
                double tot_subject = 0.0;
                List<TeacherClassSubjectMean> list_tcsm_subjects = uni.searchByQuery("SELECT g FROM TeacherClassSubjectMean g where g.yearId.id='" + Integer.parseInt(year) + "'  and g.teacherId.id='" + gcst_individualy.getTeacherId().getId() + "'  and g.termsId.id='" + termName + "' " + gradeQuery + teacherQuery + subjectQuery + " group by g.subjectsId");
                for (TeacherClassSubjectMean gcst_sub : list_tcsm_subjects) { // Retrive Selected Teacher's Subjects

                    List<MeanGradeList> meanGradeList = new ArrayList();

                    double tot_class = 0.0;

                    List<TeacherClassSubjectMean> list_tcsm_class = uni.searchByQuery("SELECT g FROM TeacherClassSubjectMean g where g.yearId.id='" + Integer.parseInt(year) + "'  and g.teacherId.id='" + gcst_individualy.getTeacherId().getId() + "'  and g.termsId.id='" + termName + "' and g.subjectsId.id='" + gcst_sub.getSubjectsId().getId() + "' " + gradeQuery + teacherQuery + subjectQuery + "  group by g.gradeClassStreamId ");
                    for (TeacherClassSubjectMean gcst_class : list_tcsm_class) { // Retrive Selected Teacher's Subjects classes

                        meanGradeList.add(new MeanGradeList(gcst_class.getGradeClassStreamId().getGradeId().getName() + gcst_class.getGradeClassStreamId().getClassesId().getName(), gcst_class.getMeanValue(), comlib.getDouble(gcst_class.getMeanValue())));
                        tot_class += gcst_class.getMeanValue();
                    }

                    double mean_subject = 0.0;
                    if (list_tcsm_class.size() > 0) {
                        mean_subject = tot_class / list_tcsm_class.size();

                    }
                    tot_subject += mean_subject;
                    meanSubjectList.add(new MeanSubjectList(gcst_sub.getSubjectsId().getName(), mean_subject, comlib.getDouble(mean_subject), meanGradeList));

                }

                double mean_teacher = 0.0;
                if (list_tcsm_subjects.size() > 0) {

                    mean_teacher = tot_subject / list_tcsm_subjects.size();
                }

                MeanTeacherList mtl = new MeanTeacherList(gcst_individualy.getTeacherId().getSchoolId().getId(), gcst_individualy.getTeacherId().getSchoolId().getGeneralOrganizationProfileId().getName(), gcst_individualy.getTeacherId().getGeneralUserProfileId().getNameWithIn(), mean_teacher, comlib.getDouble(mean_teacher), meanSubjectList);
                meanTeacherList.add(mtl);
                allTeacherArray.add(mtl);

            }

            // code over
//            List<GradeClassSubjectTeacher> list_gcst_individualy = uni.searchByQuery("SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.yearId.id='" + Integer.parseInt(year) + "' and g.teacherId!=null and g.teacherId.isActive='1' and g.teacherId.schoolId.id='" + school.getId() + "' and g.teacherId.isActive='1' and g.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + school.getId() + "' " + gradeQuery + teacherQuery + subjectQuery + " group by g.teacherId.id");
//            for (GradeClassSubjectTeacher gcst_individualy : list_gcst_individualy) { // Retrive group of subject Teachers
//
//                System.out.println("gcst_individualy " + gcst_individualy.getTeacherId().getId());
//                double meanTeacher = 0.0;
//
//                meanTeacher = stored.get_selected_teacher_mean(Integer.parseInt(year), Integer.parseInt(termName), school.getId(), gcst_individualy.getTeacherId().getId(), Integer.parseInt(gradeName), Integer.parseInt(subjectName));
//                System.out.println("meanTeacher " + meanTeacher);
//                List<MeanSubjectList> meanSubjectList = new ArrayList();
//                List<GradeClassSubjectTeacher> list_gcst_subjects = uni.searchByQuery("SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.yearId.id='" + Integer.parseInt(year) + "'  and g.teacherId.id='" + gcst_individualy.getTeacherId().getId() + "' " + gradeQuery + subjectQuery + " group by g.gradeClassHasSubjectsId.subjectsId.id");
//                for (GradeClassSubjectTeacher gcst_sub : list_gcst_subjects) { // Retrive Selected Teacher's Subjects
//
//                    double meanSubject = 0.0;
//                    double SubjectTotMarks = 0.0;
//                    int SubjectTotStudents = 0;
//                    List<GradeClassSubjectTeacher> list_gcst_class = uni.searchByQuery("SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.yearId.id='" + Integer.parseInt(year) + "' and g.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + school.getId() + "' and g.teacherId.id='" + gcst_individualy.getTeacherId().getId() + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + gcst_sub.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' " + gradeQuery + " ");
//                    for (GradeClassSubjectTeacher gcst : list_gcst_class) { // me teacher me awurudde me iskole me subject eka ugannapu panthi tika Ex : 3A,4A,4B
//
//                        List<GradeClassStudentsHasSubjects> list_gcshs_sub = uni.searchByQuery("SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassHasSubjectsId.subjectsId.id='" + gcst_sub.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.id='" + gcst.getGradeClassStreamManagerId().getId() + "'  and g.gradeClassStudentsId.isRemoved='0'");
//                        SubjectTotStudents += list_gcshs_sub.size();
//                        for (GradeClassStudentsHasSubjects gcshs : list_gcshs_sub) {
//
//                            String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcshs.getGradeClassStudentsId().getId() + "' and g.termsId.id='" + Integer.parseInt(termName) + "' and g.gradeClassHasSubjectsId.id='" + gcshs.getGradeClassHasSubjectsId().getId() + "'  and g.isRemoved='0'";
//                            List<StudentMarks> listm = uni.searchByQuerySingle(querym);
//                            if (listm.size() > 0) {
//                                SubjectTotMarks += listm.iterator().next().getMarks();
//                            } else {
//
//                                SubjectTotMarks += 0.0;
//                            }
//                        }
//                    }
//
//                    if (SubjectTotStudents != 0) {
//
//                        meanSubject = SubjectTotMarks / SubjectTotStudents;
//                    }
//                    List<MeanGradeList> meanGradeList = new ArrayList();
//                    List<GradeClassSubjectTeacher> list_gcst_grades = uni.searchByQuery("SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.yearId.id='" + Integer.parseInt(year) + "'  and g.teacherId.id='" + gcst_individualy.getTeacherId().getId() + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + gcst_sub.getGradeClassHasSubjectsId().getSubjectsId().getId() + "' " + gradeQuery + " group by g.gradeClassStreamManagerId.id");
//                    for (GradeClassSubjectTeacher gcst_grade : list_gcst_grades) { // Retrive Selected Subjects's Classes
//
//                        double meanGrade = 0.0;
//                        double GradeTotMarks = 0.0;
//                        int GradeTotStudents = 0;
//
//                        List<GradeClassStudentsHasSubjects> list_gcshs_grade = uni.searchByQuery("SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassHasSubjectsId.id='" + gcst_sub.getGradeClassHasSubjectsId().getId() + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.id='" + gcst_grade.getGradeClassStreamManagerId().getId() + "'  and g.gradeClassStudentsId.isRemoved='0'");
//                        GradeTotStudents += list_gcshs_grade.size();
//                        for (GradeClassStudentsHasSubjects gcshs : list_gcshs_grade) {
////                            System.out.println("name " + gcshs.getGradeClassStudentsId().getStudentsId().getGeneralUserProfileId().getNameWithIn());
//                            String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcshs.getGradeClassStudentsId().getId() + "' and g.termsId.id='" + Integer.parseInt(termName) + "' and g.gradeClassHasSubjectsId.id='" + gcshs.getGradeClassHasSubjectsId().getId() + "'  and g.isRemoved='0' ";
//                            List<StudentMarks> listm = uni.searchByQuerySingle(querym);
//                            if (listm.size() > 0) {
////                                System.out.println("mark " + listm.iterator().next().getMarks());
//                                GradeTotMarks += listm.iterator().next().getMarks();
//                            } else {
//
//                                GradeTotMarks += 0.0;
//                            }
//                        }
////                        System.out.println("GradeTotMarks " + GradeTotMarks);
////                        System.out.println("GradeTotStudents " + GradeTotStudents);
//                        if (GradeTotStudents != 0) {
//
//                            meanGrade = GradeTotMarks / GradeTotStudents;
//                        }
//
//                        meanGradeList.add(new MeanGradeList(gcst_grade.getGradeClassStreamManagerId().getGradeClassStreamId().getGradeId().getName() + gcst_grade.getGradeClassStreamManagerId().getGradeClassStreamId().getClassesId().getName(), meanGrade, comlib.getDouble(meanGrade)));
//
//                    }
//                    meanSubjectList.add(new MeanSubjectList(gcst_sub.getGradeClassHasSubjectsId().getSubjectsId().getName(), meanSubject, comlib.getDouble(meanSubject), meanGradeList));
//                }
//                System.out.println("g " + gcst_individualy.getTeacherId().getGeneralUserProfileId().getNameWithIn());
//                MeanTeacherList mtl = new MeanTeacherList(gcst_individualy.getTeacherId().getSchoolId().getId(), gcst_individualy.getTeacherId().getSchoolId().getGeneralOrganizationProfileId().getName(), gcst_individualy.getTeacherId().getGeneralUserProfileId().getNameWithIn(), meanTeacher, comlib.getDouble(meanTeacher), meanSubjectList);
//                meanTeacherList.add(mtl);
//                allTeacherArray.add(mtl);
//            }
            unorder_meanSchoolArray.add(new MeanSchoolList(school.getId(), school.getGeneralOrganizationProfileId().getName(), meanTeacherList, ""));

        }
////        if (allTeacherArray.size() > 0) {
//            is_slider = true;
//        }
        MeanTeacherList[] addressArray = new MeanTeacherList[allTeacherArray.size()];
        for (int i = 0; i < allTeacherArray.size(); i++) {

            addressArray[i] = allTeacherArray.get(i);
        }

        MeanTeacherList[] s = SortArraysTeacherPerformanceAnalyzer.GetArray(addressArray);

        for (int i = (s.length - 1); i >= 0; i--) {

            boolean check_available = false;

            int k = 0;
            for (MeanSchoolList mscl : meanSchoolList) {

                if (mscl.getSid() == s[i].getSid()) {
                    meanSchoolList.get(k).getMeanTeacherList().add(s[i]);

                    check_available = true;
                    break;
                }
                k++;

            }
            if (check_available == false) {
                List<MeanTeacherList> mtl_default = new ArrayList();
                mtl_default.add(s[i]);
                meanSchoolList.add(new MeanSchoolList(s[i].sid, s[i].schoolname, mtl_default, ""));

            }

        }
        // teachers not assign schools
        for (MeanSchoolList msl_empty : unorder_meanSchoolArray) {
            boolean check_available = false;

            for (MeanSchoolList mscl : meanSchoolList) {

                if (mscl.getSid() == msl_empty.getSid()) {
                    check_available = true;
                    break;
                }

            }
            if (check_available == false) {
                List<MeanTeacherList> mtl_default = new ArrayList();
                meanSchoolList.add(new MeanSchoolList(msl_empty.sid, msl_empty.name, mtl_default, "defaultTab"));

            }
        }

        List<MeanSchoolList> msl_list = new ArrayList();
        for (MeanSchoolList mtll : meanSchoolList) {
            msl_list.add(mtll);
        }

        for (MeanSchoolList msl : msl_list) {

            List<MeanTeacherList> mtl = new ArrayList();

            for (MeanTeacherList mtll : msl.getMeanTeacherList()) {

                mtl.add(new MeanTeacherList(mtll.getSid(), mtll.getSchoolname(), mtll.getTeachername(), mtll.getTeachermean(), mtll.getTeachermeanVal(), mtll.getMeanSubjectList()));
            }
            System.out.println("sc " + msl.getName());
            meanOldSchoolList.add(new MeanSchoolList(msl.getSid(), msl.getName(), mtl, msl.getStatus()));
        }
        loadStudentCount();
    }

    public void loadStudentCount() {

        List<GradeClassStudents> gcs_list = new ArrayList();

        gcs_list_filter.clear();

        if (provinceName.equals("0")) {
            String query = "SELECT g FROM GradeClassStudents g  where g.gradeClassStreamManagerId.yearId.id='" + year + "'  and g.isRemoved='0'";
            gcs_list = uni.searchByQuery(query);

        } else if (zoneName.equals("0")) {
            String query = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.gradeClassStreamId.schoolId.educationDivisionId.educationZoneId.provinceId.id='" + Integer.parseInt(provinceName) + "' and g.gradeClassStreamManagerId.yearId.id='" + year + "'  and g.isRemoved='0'";

            gcs_list = uni.searchByQuery(query);

        } else if (divisionName.equals("0")) {
            String query = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.gradeClassStreamId.schoolId.educationDivisionId.educationZoneId.id='" + Integer.parseInt(zoneName) + "' and g.gradeClassStreamManagerId.yearId.id='" + year + "'  and g.isRemoved='0' ";

            gcs_list = uni.searchByQuery(query);

        } else if (schoolName.equals("0")) {
            String query = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.gradeClassStreamId.schoolId.educationDivisionId.id='" + Integer.parseInt(divisionName) + "' and g.gradeClassStreamManagerId.yearId.id='" + year + "'  and g.isRemoved='0'";

            gcs_list = uni.searchByQuery(query);

        } else if (!schoolName.equals("0")) {
            String query = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + Integer.parseInt(schoolName) + "' and g.gradeClassStreamManagerId.yearId.id='" + year + "'  and g.isRemoved='0'";

            gcs_list = uni.searchByQuery(query);

        } else if (!teacherName.equals("0")) {
            String query1 = "SELECT g FROM GradeClassSubjectTeacher g where g.teacherId.id='" + Integer.parseInt(teacherName) + "' and g.gradeClassStreamManagerId.yearId.id='" + year + "'";

            List<GradeClassSubjectTeacher> gst_list = uni.searchByQuery(query1);
            for (GradeClassSubjectTeacher g : gst_list) {
                String querys = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassHasSubjectsId.id='" + g.getGradeClassHasSubjectsId().getId() + "'  and g.gradeClassStudentsId.isRemoved='0' ";

                List<GradeClassStudentsHasSubjects> gcshs_list = uni.searchByQuery(querys);
                for (GradeClassStudentsHasSubjects gcshs : gcshs_list) {
                    gcs_list.add(gcshs.getGradeClassStudentsId());
                }

            }

        }
        if (gradeName.equals("0") && subjectName.equals("0")) {
            for (GradeClassStudents gcs : gcs_list) {
                getGcs_list_filter().add(gcs);
            }

        } else if (!subjectName.equals("0") && !gradeName.equals("0")) {

            for (GradeClassStudents gcs : gcs_list) {
                String querys = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassHasSubjectsId.subjectsId.id='" + Integer.parseInt(subjectName) + "' and g.gradeClassStudentsId.id='" + gcs.getId() + "' and  g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "'  and g.gradeClassStudentsId.isRemoved='0'";

                List<GradeClassStudentsHasSubjects> gcshs_list = uni.searchByQuerySingle(querys);
                if (gcshs_list.size() > 0) {

                    getGcs_list_filter().add(gcs);
                }
            }
        } else if (!gradeName.equals("0")) {
            for (GradeClassStudents gcs : gcs_list) {
                if (gcs.getGradeClassStreamManagerId().getGradeClassStreamId().getGradeId().getId() == Integer.parseInt(gradeName)) {

                    getGcs_list_filter().add(gcs);

                }

            }
        } else if (!subjectName.equals("0")) {

            for (GradeClassStudents gcs : gcs_list) {
                String querys = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassHasSubjectsId.subjectsId.id='" + Integer.parseInt(subjectName) + "' and g.gradeClassStudentsId.id='" + gcs.getId() + "'  and g.gradeClassStudentsId.isRemoved='0' ";

                List<GradeClassStudentsHasSubjects> gcshs_list = uni.searchByQuerySingle(querys);
                if (gcshs_list.size() > 0) {

                    getGcs_list_filter().add(gcs);
                }
            }
        }

        this.studentCount = getGcs_list_filter().size();
        old_studentCount = getGcs_list_filter().size();
    }

    public void loadAjustMean() {

        System.out.println("siz " + getMeanOldSchoolList().size());

        List<MeanTeacherList> remove_mtl = new ArrayList();
        List<MeanSchoolList> remove_msl = new ArrayList();

        List<MeanSchoolList> msl_list = new ArrayList();
        for (MeanSchoolList mtll : meanSchoolList) {
            msl_list.add(mtll);
        }
        System.out.println("siz1 " + getMeanOldSchoolList().size());
        for (MeanSchoolList msl : msl_list) {
            if (msl.getMeanTeacherList().size() > 0) {

                for (MeanTeacherList mtl : msl.getMeanTeacherList()) {
                    if (!(mtl.teachermean >= sliderMin && mtl.teachermean <= slidrMax)) {
                        remove_mtl.add(mtl);
                    }

                }
            } else {

                remove_msl.add(msl);
            }
        }
        System.out.println("siz2 " + getMeanOldSchoolList().size());
        for (MeanSchoolList msl_remove : remove_msl) {
            msl_list.remove(msl_remove);
        }
        System.out.println("siz3 " + getMeanOldSchoolList().size());
        for (MeanTeacherList mtl_remove : remove_mtl) {
            for (MeanSchoolList msl : msl_list) {

                msl.getMeanTeacherList().remove(mtl_remove);

            }
        }
        System.out.println("siz4 " + getMeanOldSchoolList().size());
        meanSchoolList = msl_list;
        System.out.println("siz5 " + getMeanOldSchoolList().size());

        loadFilteredStudentCount();
    }

    public void loadFilteredStudentCount() {
        scSchoolList.clear();
        List<GradeClassStudents> gcs_list_old = new ArrayList();

        List<School> school_array = new ArrayList();

        for (GradeClassStudents gcs : gcs_list_filter) {

            gcs_list_old.add(gcs);

        }

        List<GradeClassStudents> nameList = new ArrayList();
        int count = 0;

        if (subjectName.equals("0")) {  // get mean of each student

            for (GradeClassStudents gcs : gcs_list_old) {

                double mark = 0;

                String query = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassStudentsId.id='" + gcs.getId() + "'  and g.gradeClassStudentsId.isRemoved='0' ";
                List<GradeClassStudentsHasSubjects> list = uni.searchByQuery(query);

                if (list.size() > 0) {

                    for (GradeClassStudentsHasSubjects gcshs : list) {
                        System.out.println("awasuha");
                        String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.id='" + gcshs.getGradeClassHasSubjectsId().getId() + "'  and g.isRemoved='0'";
                        List<StudentMarks> listm = uni.searchByQuerySingle(querym);
                        if (listm.size() > 0) {
                            StudentMarks sm = listm.iterator().next();
                            mark += sm.getMarks();
                        }
                    }

                    double avg = mark / list.size();

                    if ((avg >= sliderMin && avg <= slidrMax)) {
                        count++;

                        if (!nameList.contains(gcs)) {

                            nameList.add(gcs);
                        }

                    }
                }

            }

        } else {

            for (GradeClassStudents gcs : gcs_list_old) {
                String query = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + Integer.parseInt(subjectName) + "'  and g.gradeClassStudentsId.isRemoved='0' ";
                List<GradeClassStudentsHasSubjects> list = uni.searchByQuerySingle(query);

                if (list.size() > 0) {

                    double mark = 0;

                    String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.termsId.id='" + termName + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + Integer.parseInt(subjectName) + "'  and g.isRemoved='0' ";
                    List<StudentMarks> listm = uni.searchByQuerySingle(querym);
                    if (listm.size() > 0) {
                        StudentMarks sm = listm.iterator().next();
                        mark = sm.getMarks();
                    }
                    System.out.println("mark is " + mark);
                    if ((mark >= sliderMin && mark <= slidrMax)) {
                        count++;

                        if (!nameList.contains(gcs)) {

                            nameList.add(gcs);
                        }

                    }

                }
            }

        }

        for (GradeClassStudents gcs : nameList) {

            if (!school_array.contains(gcs.getGradeClassStreamManagerId().getGradeClassStreamId().getSchoolId())) {

                school_array.add(gcs.getGradeClassStreamManagerId().getGradeClassStreamId().getSchoolId());

            }

        }

        for (School s : school_array) {
            System.out.println("dddd");
            List<GradeClassStream> gcs_array = new ArrayList();

            for (GradeClassStudents gcs : nameList) {
                if (gcs.getGradeClassStreamManagerId().getGradeClassStreamId().getSchoolId().getId() == s.getId()) {
                    if (!gcs_array.contains(gcs.getGradeClassStreamManagerId().getGradeClassStreamId())) {

                        gcs_array.add(gcs.getGradeClassStreamManagerId().getGradeClassStreamId());

                    }
                }
            }
            List<StudentCountClassList> stclass = new ArrayList();

            for (GradeClassStream gcstr : gcs_array) {

                List<StudentCountStudentList> ststu = new ArrayList();
                int k = 1;
                for (GradeClassStudents gcs : nameList) {
                    if (gcs.getGradeClassStreamManagerId().getGradeClassStreamId().getId() == gcstr.getId()) {

                        ststu.add(new StudentCountStudentList(k, gcs.getStudentsId().getStudentId(), gcs.getStudentsId().getGeneralUserProfileId().getNameWithIn()));
                        k++;
                    }

                }

                stclass.add(new StudentCountClassList(gcstr.getGradeId().getName() + "-" + gcstr.getClassesId().getName(), ststu));

            }
            getScSchoolList().add(new StudentCountSchoolList(s.getGeneralOrganizationProfileId().getName(), stclass));
        }

        this.studentCount = count;

    }

    public void resetAjustMean() {
        System.out.println("get " + getMeanOldSchoolList().size());
        sliderMin = 0;
        slidrMax = 100;

        meanSchoolList.clear();

        List<MeanSchoolList> msl_list = new ArrayList();
        for (MeanSchoolList mtll : meanOldSchoolList) {
            msl_list.add(mtll);
        }

        for (MeanSchoolList msl : msl_list) {

            List<MeanTeacherList> mtl = new ArrayList();

            for (MeanTeacherList mtll : msl.getMeanTeacherList()) {

                mtl.add(new MeanTeacherList(mtll.getSid(), mtll.getSchoolname(), mtll.getTeachername(), mtll.getTeachermean(), mtll.getTeachermeanVal(), mtll.getMeanSubjectList()));
            }
            meanSchoolList.add(new MeanSchoolList(msl.getSid(), msl.getName(), mtl, msl.getStatus()));
        }
        resetAjustStudentCount();
    }

    public void resetAjustStudentCount() {

        this.studentCount = old_studentCount;

    }

    public static class StudentCountSchoolList {

        private String name;
        private List<StudentCountClassList> studentCountClassList;

        public StudentCountSchoolList(String name, List<StudentCountClassList> studentCountClassList) {
            this.name = name;
            this.studentCountClassList = studentCountClassList;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<StudentCountClassList> getStudentCountClassList() {
            return studentCountClassList;
        }

        public void setStudentCountClassList(List<StudentCountClassList> studentCountClassList) {
            this.studentCountClassList = studentCountClassList;
        }
    }

    public static class StudentCountClassList {

        private String name;
        private List<StudentCountStudentList> studentCountStudentList;

        public StudentCountClassList(String name, List<StudentCountStudentList> studentCountStudentList) {
            this.name = name;
            this.studentCountStudentList = studentCountStudentList;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<StudentCountStudentList> getStudentCountStudentList() {
            return studentCountStudentList;
        }

        public void setStudentCountStudentList(List<StudentCountStudentList> studentCountStudentList) {
            this.studentCountStudentList = studentCountStudentList;
        }
    }

    public static class StudentCountStudentList {

        private int no;
        private String index_no;
        private String name;

        public StudentCountStudentList(int no, String index_no, String name) {
            this.no = no;
            this.index_no = index_no;
            this.name = name;

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

        public String getIndex_no() {
            return index_no;
        }

        public void setIndex_no(String index_no) {
            this.index_no = index_no;
        }
    }

    public static class MeanSchoolList {

        private int sid;
        private String name;
        private List<MeanTeacherList> meanTeacherList;
        private String status;

        public MeanSchoolList(int sid, String name, List<MeanTeacherList> meanTeacherList, String status) {
            this.sid = sid;
            this.name = name;
            this.meanTeacherList = meanTeacherList;
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<MeanTeacherList> getMeanTeacherList() {
            return meanTeacherList;
        }

        public void setMeanTeacherList(List<MeanTeacherList> meanTeacherList) {
            this.meanTeacherList = meanTeacherList;
        }

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

    public class MeanTeacherList {

        private int sid;
        private String schoolname;
        private String teachername;
        private double teachermean;
        private String teachermeanVal;
        private List<MeanSubjectList> meanSubjectList;

        public MeanTeacherList(int sid, String schoolname, String teachername, double teachermean, String teachermeanVal, List<MeanSubjectList> meanSubjectList) {
            this.sid = sid;
            this.schoolname = schoolname;
            this.teachername = teachername;
            this.teachermean = teachermean;
            this.meanSubjectList = meanSubjectList;
            this.teachermeanVal = teachermeanVal;
        }

        public String getTeachername() {
            return teachername;
        }

        public void setTeachername(String teachername) {
            this.teachername = teachername;
        }

        public double getTeachermean() {
            return teachermean;
        }

        public void setTeachermean(double teachermean) {
            this.teachermean = teachermean;
        }

        public List<MeanSubjectList> getMeanSubjectList() {
            return meanSubjectList;
        }

        public void setMeanSubjectList(List<MeanSubjectList> meanSubjectList) {
            this.meanSubjectList = meanSubjectList;
        }

        public String getTeachermeanVal() {
            return teachermeanVal;
        }

        public void setTeachermeanVal(String teachermeanVal) {
            this.teachermeanVal = teachermeanVal;
        }

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }

        public String getSchoolname() {
            return schoolname;
        }

        public void setSchoolname(String schoolname) {
            this.schoolname = schoolname;
        }

    }

    public class MeanSubjectList {

        private String subjectname;
        private double subjectmean;
        private String subjectmeanVal;
        private List<MeanGradeList> meanGradeList;

        public MeanSubjectList(String subjectname, double subjectmean, String subjectmeanVal, List<MeanGradeList> meanGradeList) {
            this.subjectname = subjectname;
            this.subjectmean = subjectmean;
            this.meanGradeList = meanGradeList;
            this.subjectmeanVal = subjectmeanVal;
        }

        public String getSubjectname() {
            return subjectname;
        }

        public void setSubjectname(String subjectname) {
            this.subjectname = subjectname;
        }

        public double getSubjectmean() {
            return subjectmean;
        }

        public void setSubjectmean(double subjectmean) {
            this.subjectmean = subjectmean;
        }

        public List<MeanGradeList> getMeanGradeList() {
            return meanGradeList;
        }

        public void setMeanGradeList(List<MeanGradeList> meanGradeList) {
            this.meanGradeList = meanGradeList;
        }

        public String getSubjectmeanVal() {
            return subjectmeanVal;
        }

        public void setSubjectmeanVal(String subjectmeanVal) {
            this.subjectmeanVal = subjectmeanVal;
        }

    }

    public class MeanGradeList {

        private String gradename;
        private double grademean;
        private String grademeanVal;

        public MeanGradeList(String gradename, double grademean, String grademeanVal) {
            this.gradename = gradename;
            this.grademean = grademean;
            this.grademeanVal = grademeanVal;
        }

        public String getGradename() {
            return gradename;
        }

        public void setGradename(String gradename) {
            this.gradename = gradename;
        }

        public double getGrademean() {
            return grademean;
        }

        public void setGrademean(double grademean) {
            this.grademean = grademean;
        }

        public String getGrademeanVal() {
            return grademeanVal;
        }

        public void setGrademeanVal(String grademeanVal) {
            this.grademeanVal = grademeanVal;
        }

    }

    public class AnalysisSummary {

        private String area;
        private List<AnalysisSummaryRecord> records;

        public AnalysisSummary(String area, List<AnalysisSummaryRecord> records) {
            this.area = area;
            this.records = records;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public List<AnalysisSummaryRecord> getRecords() {
            return records;
        }

        public void setRecords(List<AnalysisSummaryRecord> records) {
            this.records = records;
        }

    }

    public class AnalysisSummaryRecord {

        private String name;
        private String mean;

        public AnalysisSummaryRecord(String name, String mean) {
            this.name = name;
            this.mean = mean;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMean() {
            return mean;
        }

        public void setMean(String mean) {
            this.mean = mean;
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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public List<SelectItem> getSubjectNameList() {
        return subjectNameList;
    }

    public void setSubjectNameList(List<SelectItem> subjectNameList) {
        this.subjectNameList = subjectNameList;
    }

    public List<MeanSchoolList> getMeanSchoolList() {
        return meanSchoolList;
    }

    public void setMeanSchoolList(List<MeanSchoolList> meanSchoolList) {
        this.meanSchoolList = meanSchoolList;
    }

    public List<AnalysisSummary> getAnalysisSummary() {
        return analysisSummary;
    }

    public void setAnalysisSummary(List<AnalysisSummary> analysisSummary) {
        this.analysisSummary = analysisSummary;
    }

    public boolean isIs_slider() {
        return is_slider;
    }

    public void setIs_slider(boolean is_slider) {
        this.is_slider = is_slider;
    }

    public int getSliderMin() {
        return sliderMin;
    }

    public void setSliderMin(int sliderMin) {
        this.sliderMin = sliderMin;
    }

    public int getSlidrMax() {
        return slidrMax;
    }

    public void setSlidrMax(int slidrMax) {
        this.slidrMax = slidrMax;
    }

    public List<MeanSchoolList> getMeanOldSchoolList() {
        return meanOldSchoolList;
    }

    public void setMeanOldSchoolList(List<MeanSchoolList> meanOldSchoolList) {
        this.meanOldSchoolList = meanOldSchoolList;
    }

    public boolean isIs_studentCount() {
        return is_studentCount;
    }

    public void setIs_studentCount(boolean is_studentCount) {
        this.is_studentCount = is_studentCount;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public List<GradeClassStudents> getGcs_list_filter() {
        return gcs_list_filter;
    }

    public void setGcs_list_filter(List<GradeClassStudents> gcs_list_filter) {
        this.gcs_list_filter = gcs_list_filter;
    }

    public int getOld_studentCount() {
        return old_studentCount;
    }

    public void setOld_studentCount(int old_studentCount) {
        this.old_studentCount = old_studentCount;
    }

    public List<StudentCountSchoolList> getScSchoolList() {
        return scSchoolList;
    }

    public void setScSchoolList(List<StudentCountSchoolList> scSchoolList) {
        this.scSchoolList = scSchoolList;
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
