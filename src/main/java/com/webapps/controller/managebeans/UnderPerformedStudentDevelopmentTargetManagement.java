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
import com.ejb.model.entity.DevTarget;
import com.ejb.model.entity.EducationDivision;
import com.ejb.model.entity.EducationZone;
import com.ejb.model.entity.Grade;
import com.ejb.model.entity.GradeClassStream;
import com.ejb.model.entity.GradeClassStudents;
import com.ejb.model.entity.GradeClassStudentsHasSubjects;
import com.ejb.model.entity.GradeClassSubjectTeacher;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.Province;
import com.ejb.model.entity.School;
import com.ejb.model.entity.StudentMarks;
import com.ejb.model.entity.Students;
import com.ejb.model.entity.Subjects;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.Terms;
import com.ejb.model.entity.Year;
import com.webapps.controller.utilities.SortArraysTeacherPerformanceAnalyzer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
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
 * @author Thilini Madagama
 */
@ManagedBean(name = "underPerformedStudents")
@ViewScoped
public class UnderPerformedStudentDevelopmentTargetManagement {

    private String provinceName = "0";
    private List<SelectItem> provinceNameList = new ArrayList<SelectItem>();

    private String zoneName = "0";
    private List<SelectItem> zoneNameList = new ArrayList<SelectItem>();

    private String divisionName = "0";
    private List<SelectItem> divisionNameList = new ArrayList<SelectItem>();

//    String[] selectedCities2
    private String[] schoolNameAr;
    private List<SelectItem> schoolNameList = new ArrayList<SelectItem>();

    private String teacherName = "0";
    private List<SelectItem> teacherNameList = new ArrayList<SelectItem>();

    private boolean disabledFiledProvince = false;
    private boolean disabledFiledZone = false;
    private boolean disabledFiledDivision = false;
    private boolean disabledFiledSchool = false;

    private String year = "0";
    private List<SelectItem> YearList = new ArrayList<SelectItem>();

    private String subjectName = "0";
    private List<SelectItem> subjectNameList = new ArrayList<SelectItem>();

    private String termName = "0";
    private List<SelectItem> termNameList = new ArrayList<SelectItem>();

    private String marksUnder = "40";
    private String targetForSelectedTerm = "";

    private List<SchoolRecordList> schoolRecordList = new ArrayList();
    private List<MarksNotEntered> marksNotEnteredList = new ArrayList();
    private String selectedSchoolName;

    private String lastTermTarget = "";
    private String thisTermTarget = "";

    private String lastTermName;
    private String thisTermName;

    private int def_province = 0;
    private int def_zone = 0;
    private int def_division = 0;
    private int def_school = 0;

    int lastYear;
    int lastTerm;

    private boolean disableTarget;

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

        // Get Subject
        getSubjectNameList().add(new SelectItem("0", "All"));
        String querys = "SELECT g FROM Subjects g where g.isActive='1' order by g.name ASC";
        List<Subjects> list_sub = uni.searchByQuery(querys);
        for (Subjects cc : list_sub) {

            getSubjectNameList().add(new SelectItem(cc.getId(), cc.getName()));
        }

        this.termName = "1";
        setTargetDisabled();
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

        if (!divisionName.equals("0")) {

            // Get Province
            String query = "SELECT g FROM School g where g.educationDivisionId.id='" + Integer.parseInt(getDivisionName()) + "' order by g.generalOrganizationProfileId.name ASC";
            List<School> listAS = uni.searchByQuery(query);
            schoolNameAr = new String[listAS.size()];
            int i = 0;
            for (School cc : listAS) {
                schoolNameAr[i] = cc.getId() + "";
                getSchoolNameList().add(new SelectItem(cc.getId(), cc.getGeneralOrganizationProfileId().getName()));
                i++;
            }
        }
//        schoolNameAr[0]=getDef_school() + "";
//        setSchoolName(getDef_school() + "");
        getTeachersList();
        return null;
    }

    public String getTeachersList() {

        getTeacherNameList().clear();
        getTeacherNameList().add(new SelectItem("0", "All"));
        System.out.println("ar " + schoolNameAr);
        if (schoolNameAr != null) {
            if (schoolNameAr.length > 0) {

                String sch = "";
                int k = 0;
                for (String p : schoolNameAr) {

                    if (k == 0) {
                        sch = p;
                        k++;
                    } else {
                        sch += "," + p;
                    }
                }

                // Get Province
                String query = "SELECT g FROM Teacher g where g.schoolId.id IN (" + sch + ") and g.isActive='1' order by g.generalUserProfileId.nameWithIn ASC";
                List<Teacher> listAS = uni.searchByQuery(query);
                for (Teacher cc : listAS) {

                    getTeacherNameList().add(new SelectItem(cc.getId(), cc.getGeneralUserProfileId().getNameWithIn()));
                }
            }
        }

        return null;
    }

    public synchronized String saveTargetforSelectedTerm() {
        FacesMessage msg = null;
        if (targetForSelectedTerm.equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Enter Target !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else {
            boolean result = comDiv.saveTargetForSelectedTerm(Double.parseDouble(targetForSelectedTerm), Integer.parseInt(year), Integer.parseInt(termName));
            if (result == true) {

                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Saved !", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);

            } else {
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error !", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);

            }

        }
        return null;
    }

    public String setTargetDisabled() {

        disableTarget = false;

        double this_target = 0;
        List<DevTarget> gop_list1 = uni.searchByQuerySingle("SELECT im FROM DevTarget im WHERE im.devTargetTypeId.id='1' and im.termsId.id='" + termName + "' and im.yearId.id='" + year + "'");
        if (gop_list1.size() > 0) {
            this_target = gop_list1.iterator().next().getTarget();
            disableTarget = true;
        }
        targetForSelectedTerm = this_target + "";

        return null;

    }

    public synchronized void loadReport() {

        schoolRecordList.clear();

        List<School> sc_list = new ArrayList();

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
        } else if (schoolNameAr.length == 0) {
            String query = "SELECT g FROM School g where g.educationDivisionId.id='" + Integer.parseInt(divisionName) + "' order by g.generalOrganizationProfileId.name ASC";

            sc_list = uni.searchByQuery(query);
            System.out.println("awap4");
        } else if (schoolNameAr.length > 0) {

            String sch = "";
            int k = 0;
            for (String p : schoolNameAr) {

                if (k == 0) {
                    sch = p;
                    k++;
                } else {
                    sch += "," + p;
                }
            }

            String query = "SELECT g FROM School g where g.id IN (" + sch + ")";

            sc_list = uni.searchByQuery(query);
            System.out.println("awap5");
        }

        int lastYear = 0;
        int lastTerm = 0;

        if (termName.equals("1")) {

            lastTerm = 3;
            lastYear = Integer.parseInt(year) - 1;

        } else {

            lastTerm = Integer.parseInt(termName) - 1;
            lastYear = Integer.parseInt(year);
        }

        this.lastTerm = lastTerm;
        this.lastYear = lastYear;

        double marks_under = 40;
        if (!marksUnder.equals("")) {
            marks_under = Double.parseDouble(marksUnder);
        }
        double last_target = 0;
        List<DevTarget> gop_list = uni.searchByQuerySingle("SELECT im FROM DevTarget im WHERE im.devTargetTypeId.id='1' and im.termsId.id='" + lastTerm + "' and im.yearId.id='" + lastYear + "'");
        if (gop_list.size() > 0) {
            last_target = gop_list.iterator().next().getTarget();
        }
        double this_target = 0;
        List<DevTarget> gop_list1 = uni.searchByQuerySingle("SELECT im FROM DevTarget im WHERE im.devTargetTypeId.id='1' and im.termsId.id='" + termName + "' and im.yearId.id='" + year + "'");
        if (gop_list1.size() > 0) {
            this_target = gop_list1.iterator().next().getTarget();
        }
        int i = 1;
        for (School sc : sc_list) {

            int last_term_fail_student_count = 0;
            int this_term_fail_student_count = 0;
            if (teacherName.equals("0") && subjectName.equals("0")) {

                List<Students> stList = new ArrayList();

                String query11 = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.yearId.id='" + lastYear + "' and g.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id=" + sc.getId() + "  and g.isRemoved='0'";
                List<GradeClassStudents> listAS11 = uni.searchByQuery(query11);
                for (GradeClassStudents gcs : listAS11) {

                    if (!stList.contains(gcs.getStudentsId())) {
                        stList.add(gcs.getStudentsId());
                    }
                    double tot_val = 0;
                    double avg = 0;

                    String query = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.gradeClassHasSubjectsId.subjectsId.isActive='1' ";
                    List<GradeClassStudentsHasSubjects> list = uni.searchByQuery(query);
                    if (list.size() > 0) {

                        for (GradeClassStudentsHasSubjects gcshs : list) {

                            String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.termsId.id='" + lastTerm + "' and g.gradeClassHasSubjectsId.id='" + gcshs.getGradeClassHasSubjectsId().getId() + "' and g.isRemoved='0' ";
                            List<StudentMarks> listm = uni.searchByQuerySingle(querym);
                            if (listm.size() > 0) {
                                StudentMarks sm = listm.iterator().next();
                                tot_val += sm.getMarks();
                            }

                        }
                        avg = tot_val / list.size();

                    }

                    if (avg < marks_under) {

                        last_term_fail_student_count++;

                    }

                }
                System.out.println("st1 " + stList.size());

                // this
                String query12 = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.yearId.id='" + year + "' and g.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id=" + sc.getId() + "  and g.isRemoved='0'";
                List<GradeClassStudents> listAS12 = uni.searchByQuery(query12);
                for (GradeClassStudents gcs : listAS12) {

                    if (stList.contains(gcs.getStudentsId())) {

                        stList.remove(gcs.getStudentsId());
                    }

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

                    if (avg < marks_under) {

                        this_term_fail_student_count++;

                    }

                }
                System.out.println("st2 " + stList.size());
                this_term_fail_student_count += stList.size();

            } else if (!(teacherName.equals("0")) && subjectName.equals("0")) {

                List<Students> stList = new ArrayList();

                List<GradeClassStudents> lastStudent_list = new ArrayList();

                List<GradeClassSubjectTeacher> list_gcst_subjects = uni.searchByQuery("SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.yearId.id='" + lastYear + "'  and g.teacherId.id='" + teacherName + "' and g.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + sc.getId() + "'");
                for (GradeClassSubjectTeacher gcst_sub : list_gcst_subjects) {

                    String query11 = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.yearId.id='" + lastYear + "' and g.gradeClassStreamManagerId.id='" + gcst_sub.getGradeClassStreamManagerId().getId() + "'  and g.isRemoved='0'";
                    List<GradeClassStudents> listAS11 = uni.searchByQuery(query11);
                    for (GradeClassStudents gcs : listAS11) {
                        if (!lastStudent_list.contains(gcs)) {
                            lastStudent_list.add(gcs);

                            if (!stList.contains(gcs.getStudentsId())) {
                                stList.add(gcs.getStudentsId());
                            }

                        }
                    }
                }
                for (GradeClassStudents gcs : lastStudent_list) {

                    double tot_val = 0;
                    double avg = 0;
                    List<GradeClassSubjectTeacher> list_gcst_subjects1 = uni.searchByQuery("SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.yearId.id='" + lastYear + "'  and g.teacherId.id='" + teacherName + "' and g.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + sc.getId() + "'");
                    for (GradeClassSubjectTeacher gcst_sub : list_gcst_subjects1) {
                        String query = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.gradeClassHasSubjectsId.id='" + gcst_sub.getGradeClassHasSubjectsId().getId() + "' and g.gradeClassHasSubjectsId.subjectsId.isActive='1' ";
                        List<GradeClassStudentsHasSubjects> list = uni.searchByQuery(query);
                        if (list.size() > 0) {

                            for (GradeClassStudentsHasSubjects gcshs : list) {

                                String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.termsId.id='" + lastTerm + "' and g.gradeClassHasSubjectsId.id='" + gcshs.getGradeClassHasSubjectsId().getId() + "' and g.isRemoved='0' ";
                                List<StudentMarks> listm = uni.searchByQuerySingle(querym);
                                if (listm.size() > 0) {
                                    StudentMarks sm = listm.iterator().next();
                                    tot_val += sm.getMarks();
                                }

                            }
                            avg = tot_val / list.size();
                        }
                    }

                    if (avg < marks_under) {

                        last_term_fail_student_count++;

                    }

                }

                // this
                List<GradeClassStudents> thisStudent_list = new ArrayList();

                List<GradeClassSubjectTeacher> list_gcst_subjects2 = uni.searchByQuery("SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.yearId.id='" + year + "'  and g.teacherId.id='" + teacherName + "' and g.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + sc.getId() + "'");
                for (GradeClassSubjectTeacher gcst_sub : list_gcst_subjects2) {

                    String query11 = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.yearId.id='" + year + "' and g.gradeClassStreamManagerId.id='" + gcst_sub.getGradeClassStreamManagerId().getId() + "'  and g.isRemoved='0'";
                    List<GradeClassStudents> listAS11 = uni.searchByQuery(query11);
                    for (GradeClassStudents gcs : listAS11) {
                        if (!thisStudent_list.contains(gcs)) {
                            thisStudent_list.add(gcs);
                        }
                    }
                }
                for (GradeClassStudents gcs : thisStudent_list) {

                    if (stList.contains(gcs.getStudentsId())) {

                        stList.remove(gcs.getStudentsId());
                    }

                    double tot_val = 0;
                    double avg = 0;
                    List<GradeClassSubjectTeacher> list_gcst_subjects1 = uni.searchByQuery("SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.yearId.id='" + year + "'  and g.teacherId.id='" + teacherName + "' and g.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + sc.getId() + "'");
                    for (GradeClassSubjectTeacher gcst_sub : list_gcst_subjects1) {
                        String query = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.gradeClassHasSubjectsId.id='" + gcst_sub.getGradeClassHasSubjectsId().getId() + "' and g.gradeClassHasSubjectsId.subjectsId.isActive='1' ";
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
                    }

                    if (avg < marks_under) {

                        this_term_fail_student_count++;

                    }

                }
                this_term_fail_student_count += stList.size();

            } else if (teacherName.equals("0") && (!subjectName.equals("0"))) {
                List<Students> stList = new ArrayList();
                String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.id='" + lastYear + "' and  g.gradeClassHasSubjectsId.subjectsId.id='" + subjectName + "' and g.termsId.id='" + lastTerm + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + sc.getId() + "' and g.isRemoved='0' and g.isMandatory=true ";
                List<StudentMarks> listm = uni.searchByQuery(querym);
                for (StudentMarks sm : listm) {

                    if (!stList.contains(sm.getGradeClassStudentsId().getStudentsId())) {
                        stList.add(sm.getGradeClassStudentsId().getStudentsId());
                    }

                    if (sm.getMarks() < marks_under) {
                        last_term_fail_student_count++;
                    }
                }

                String querym1 = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.id='" + year + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + subjectName + "' and  g.termsId.id='" + termName + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + sc.getId() + "' and g.isRemoved='0' and g.isMandatory=true ";
                List<StudentMarks> listm1 = uni.searchByQuery(querym1);
                for (StudentMarks sm : listm1) {

                    if (stList.contains(sm.getGradeClassStudentsId().getStudentsId())) {

                        stList.remove(sm.getGradeClassStudentsId().getStudentsId());
                    }

                    if (sm.getMarks() < marks_under) {
                        this_term_fail_student_count++;
                    }
                }
                this_term_fail_student_count += stList.size();

            } else if (!(teacherName.equals("0")) && (!subjectName.equals("0"))) {
                List<Students> stList = new ArrayList();
                List<GradeClassSubjectTeacher> list_gcst_subjects = uni.searchByQuery("SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.yearId.id='" + lastYear + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + subjectName + "'  and g.teacherId.id='" + teacherName + "' and g.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + sc.getId() + "'");
                for (GradeClassSubjectTeacher gcst_sub : list_gcst_subjects) {

                    String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.id='" + gcst_sub.getGradeClassStreamManagerId().getId() + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + subjectName + "' and g.gradeClassHasSubjectsId.id='" + gcst_sub.getGradeClassHasSubjectsId().getId() + "' and  g.termsId.id='" + lastTerm + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + sc.getId() + "' and g.isRemoved='0' and g.isMandatory=true ";
                    List<StudentMarks> listm = uni.searchByQuery(querym);
                    for (StudentMarks sm : listm) {

                        if (!stList.contains(sm.getGradeClassStudentsId().getStudentsId())) {
                            stList.add(sm.getGradeClassStudentsId().getStudentsId());
                        }

                        if (sm.getMarks() < marks_under) {
                            last_term_fail_student_count++;
                        }
                    }

                }

                List<GradeClassSubjectTeacher> list_gcst_subjects1 = uni.searchByQuery("SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.yearId.id='" + year + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + subjectName + "'  and g.teacherId.id='" + teacherName + "' and g.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + sc.getId() + "'");
                for (GradeClassSubjectTeacher gcst_sub : list_gcst_subjects1) {

                    String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.id='" + gcst_sub.getGradeClassStreamManagerId().getId() + "' and g.gradeClassHasSubjectsId.subjectsId.id='" + subjectName + "' and g.gradeClassHasSubjectsId.id='" + gcst_sub.getGradeClassHasSubjectsId().getId() + "' and  g.termsId.id='" + termName + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + sc.getId() + "' and g.isRemoved='0' and g.isMandatory=true";
                    List<StudentMarks> listm = uni.searchByQuery(querym);
                    for (StudentMarks sm : listm) {

                        if (stList.contains(sm.getGradeClassStudentsId().getStudentsId())) {

                            stList.remove(sm.getGradeClassStudentsId().getStudentsId());
                        }

                        if (sm.getMarks() < marks_under) {
                            this_term_fail_student_count++;
                        }
                    }

                }
                this_term_fail_student_count += stList.size();
            }

            double y = (last_term_fail_student_count * last_target) / 100;
            double under_marks_acheivement = 0;

            if (last_target
                    != 0.0) {

                if (y != 0.0) {
                    under_marks_acheivement = ((last_term_fail_student_count - this_term_fail_student_count) / y) * 100;
                }
            }

            double this_term_expected_developped_count = this_term_fail_student_count * this_target / 100;

            double this_term_expected_under_marks_count = this_term_fail_student_count - this_term_expected_developped_count;

            String color = "";

            if (under_marks_acheivement
                    >= 80) {

                color = "#83da77";
            } else if (under_marks_acheivement >= 60 && under_marks_acheivement
                    <= 79) {

                color = "#f8eb7f";
            } else if (under_marks_acheivement >= 40 && under_marks_acheivement
                    <= 59) {

                color = "#f9a152";
            } else if (under_marks_acheivement
                    <= 39) {

                color = "#fe080e";
            }

            lastTermTarget = comlib.getDouble(last_target);
            thisTermTarget = comlib.getDouble(this_target);

            schoolRecordList.add(
                    new SchoolRecordList(i, sc.getId(), sc.getGeneralOrganizationProfileId().getName(), last_term_fail_student_count, comlib.getDouble(y), comlib.getDouble(under_marks_acheivement), this_term_fail_student_count, comlib.getDouble(this_term_expected_developped_count), comlib.getDouble(this_term_expected_developped_count), comlib.getDouble(this_term_expected_under_marks_count), color));
            i++;
        }

    }

    public synchronized void loadDataEnteredClasses(int schoolId) {
        marksNotEnteredList.clear();

        Year ylast = (Year) uni.find(lastYear, Year.class);
        Year ythis = (Year) uni.find(Integer.parseInt(year), Year.class);

        Terms tlast = (Terms) uni.find(lastTerm, Terms.class);
        Terms tthis = (Terms) uni.find(Integer.parseInt(termName), Terms.class);

        lastTermName = ylast.getName() + " - " + tlast.getName();
        thisTermName = ythis.getName() + " - " + tthis.getName();

        School sc = (School) uni.find(schoolId, School.class);

        selectedSchoolName = sc.getGeneralOrganizationProfileId().getName();

        String querym = "SELECT g FROM GradeClassStream g where g.schoolId.id='" + schoolId + "' and g.isActive='1' order by g.gradeId.id,g.classesId.name ";
        List<GradeClassStream> listm = uni.searchByQuery(querym);
        for (GradeClassStream gcs : listm) {

            String lastColor = "";
            String thisColor = "";
            String lastName = "";
            String thisName = "";

            String querym1 = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.id='" + gcs.getId() + "' and g.termsId.id='" + lastTerm + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.id='" + lastYear + "' and g.isRemoved='0' and g.isMandatory=true";
            List<StudentMarks> listm1 = uni.searchByQuery(querym1);
            if (listm1.size() > 1) {

                lastColor = "#3ca650";
                lastName = "Entered";

            } else {

                lastColor = "#ef212b";
                lastName = "Not Entered";
            }

            String querym2 = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.id='" + gcs.getId() + "' and g.termsId.id='" + termName + "' and g.gradeClassStudentsId.gradeClassStreamManagerId.yearId.id='" + year + "' and g.isRemoved='0' and g.isMandatory=true";
            List<StudentMarks> listm2 = uni.searchByQuery(querym2);
            if (listm2.size() > 1) {

                thisColor = "#3ca650";
                thisName = "Entered";

            } else {

                thisColor = "#ef212b";
                thisName = "Not Entered";
            }
            marksNotEnteredList.add(new MarksNotEntered(gcs.getGradeId().getName() + "-" + gcs.getClassesId().getName(), lastColor, thisColor, lastName, thisName));
        }

    }

    public class MarksNotEntered implements Serializable {

        private String className;
        private String lastColor;
        private String thisColor;
        private String lastName;
        private String thisName;

        public MarksNotEntered(String className, String lastColor, String thisColor, String lastName, String thisName) {
            this.className = className;
            this.lastColor = lastColor;
            this.thisColor = thisColor;
            this.lastName = lastName;
            this.thisName = thisName;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getLastColor() {
            return lastColor;
        }

        public void setLastColor(String lastColor) {
            this.lastColor = lastColor;
        }

        public String getThisColor() {
            return thisColor;
        }

        public void setThisColor(String thisColor) {
            this.thisColor = thisColor;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getThisName() {
            return thisName;
        }

        public void setThisName(String thisName) {
            this.thisName = thisName;
        }
    }

    public class SchoolRecordList implements Serializable {

        private int no;
        private int schoolID;
        private String schoolName;
        private int lastTermFailStudentCount;
        private String lastTarget;
        private String lastAchievement;
        private int thisTermFailStudentCount;
        private String thisTarget;
        private String thisTermExpectedDeveloppedCount;
        private String thisTermExpectedUnderMarksCount;
        private String color;

        public SchoolRecordList(int no, int schoolID, String schoolName, int lastTermFailStudentCount, String lastTarget, String lastAchievement, int thisTermFailStudentCount, String thisTarget, String thisTermExpectedDeveloppedCount, String thisTermExpectedUnderMarksCount, String color) {
            this.no = no;
            this.schoolID = schoolID;
            this.schoolName = schoolName;
            this.lastTermFailStudentCount = lastTermFailStudentCount;
            this.lastTarget = lastTarget;
            this.lastAchievement = lastAchievement;
            this.thisTermFailStudentCount = thisTermFailStudentCount;
            this.thisTarget = thisTarget;
            this.thisTermExpectedDeveloppedCount = thisTermExpectedDeveloppedCount;
            this.thisTermExpectedUnderMarksCount = thisTermExpectedUnderMarksCount;
            this.color = color;
        }

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }

        public int getSchoolID() {
            return schoolID;
        }

        public void setSchoolID(int schoolID) {
            this.schoolID = schoolID;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public int getLastTermFailStudentCount() {
            return lastTermFailStudentCount;
        }

        public void setLastTermFailStudentCount(int lastTermFailStudentCount) {
            this.lastTermFailStudentCount = lastTermFailStudentCount;
        }

        public String getLastTarget() {
            return lastTarget;
        }

        public void setLastTarget(String lastTarget) {
            this.lastTarget = lastTarget;
        }

        public String getLastAchievement() {
            return lastAchievement;
        }

        public void setLastAchievement(String lastAchievement) {
            this.lastAchievement = lastAchievement;
        }

        public int getThisTermFailStudentCount() {
            return thisTermFailStudentCount;
        }

        public void setThisTermFailStudentCount(int thisTermFailStudentCount) {
            this.thisTermFailStudentCount = thisTermFailStudentCount;
        }

        public String getThisTarget() {
            return thisTarget;
        }

        public void setThisTarget(String thisTarget) {
            this.thisTarget = thisTarget;
        }

        public String getThisTermExpectedDeveloppedCount() {
            return thisTermExpectedDeveloppedCount;
        }

        public void setThisTermExpectedDeveloppedCount(String thisTermExpectedDeveloppedCount) {
            this.thisTermExpectedDeveloppedCount = thisTermExpectedDeveloppedCount;
        }

        public String getThisTermExpectedUnderMarksCount() {
            return thisTermExpectedUnderMarksCount;
        }

        public void setThisTermExpectedUnderMarksCount(String thisTermExpectedUnderMarksCount) {
            this.thisTermExpectedUnderMarksCount = thisTermExpectedUnderMarksCount;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
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

    public String getMarksUnder() {
        return marksUnder;
    }

    public void setMarksUnder(String marksUnder) {
        this.marksUnder = marksUnder;
    }

    public String getTargetForSelectedTerm() {
        return targetForSelectedTerm;
    }

    public void setTargetForSelectedTerm(String targetForSelectedTerm) {
        this.targetForSelectedTerm = targetForSelectedTerm;
    }

    public List<SchoolRecordList> getSchoolRecordList() {
        return schoolRecordList;
    }

    public void setSchoolRecordList(List<SchoolRecordList> schoolRecordList) {
        this.schoolRecordList = schoolRecordList;
    }

    public String getLastTermTarget() {
        return lastTermTarget;
    }

    public void setLastTermTarget(String lastTermTarget) {
        this.lastTermTarget = lastTermTarget;
    }

    public String getThisTermTarget() {
        return thisTermTarget;
    }

    public void setThisTermTarget(String thisTermTarget) {
        this.thisTermTarget = thisTermTarget;
    }

    public boolean isDisableTarget() {
        return disableTarget;
    }

    public void setDisableTarget(boolean disableTarget) {
        this.disableTarget = disableTarget;
    }

    public List<MarksNotEntered> getMarksNotEnteredList() {
        return marksNotEnteredList;
    }

    public void setMarksNotEnteredList(List<MarksNotEntered> marksNotEnteredList) {
        this.marksNotEnteredList = marksNotEnteredList;
    }

    public String getLastTermName() {
        return lastTermName;
    }

    public void setLastTermName(String lastTermName) {
        this.lastTermName = lastTermName;
    }

    public String getThisTermName() {
        return thisTermName;
    }

    public void setThisTermName(String thisTermName) {
        this.thisTermName = thisTermName;
    }

    public String getSelectedSchoolName() {
        return selectedSchoolName;
    }

    public void setSelectedSchoolName(String selectedSchoolName) {
        this.selectedSchoolName = selectedSchoolName;
    }

    public String[] getSchoolNameAr() {
        return schoolNameAr;
    }

    public void setSchoolNameAr(String[] schoolNameAr) {
        this.schoolNameAr = schoolNameAr;
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
