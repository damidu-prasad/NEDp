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
import com.ejb.model.entity.DataChangedLogManager;
import com.ejb.model.entity.EducationDivision;
import com.ejb.model.entity.EducationZone;
import com.ejb.model.entity.GeneralOrganizationProfile;
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
import com.ejb.model.entity.TableManager;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.Year;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Thilini Madagama
 */
@ManagedBean
@ViewScoped

public class GradeClassManager implements Serializable {

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

    private String streamName = "";
    private List<SelectItem> streamNameList = new ArrayList<SelectItem>();
    private String gradeName = "";
    private List<SelectItem> gradeNameList = new ArrayList<SelectItem>();
    private String teacherName = "";
    private List<SelectItem> teacherNameList = new ArrayList<SelectItem>();

    private String assignSubjectName = "0";
    private List<SelectItem> assignSubjectNameList = new ArrayList<SelectItem>();

    private String visibleStream = "none";
    private String visibleStreamSubject = "none";
    private String gradeClassName = "";

    private List<GradeClass> gc_list_ol = new ArrayList();
    private List<GradeClass> gc_list_al = new ArrayList();
    private List<GradeArray> gradeArrayOL = new ArrayList();
    private List<GradeArray> gradeArrayAL = new ArrayList();

    private String gradeSubjectName = "";
    private List<SelectItem> gradeSubjectNameList = new ArrayList<SelectItem>();
    private String subjectName = "";

    private String streamSubjectName = "";
    private List<SelectItem> streamSubjectNameList = new ArrayList<SelectItem>();

    private int removeClass = 0;
    private int removeSubject = 0;

    private boolean disabledFiledProvince = false;
    private boolean disabledFiledZone = false;
    private boolean disabledFiledDivision = false;
    private boolean disabledFiledSchool = false;

    private int selectedGCSID = 0;

    private String assignSubjectModelClassName;

    private int selectedGCSIDforStudents = 0;

    private String classStudentsClassName;

    private List<GradeClassSubjectTeacherList> gradeclassSubjectTeacherList = new ArrayList();
    private List<CurrentStudentsList> currentStudentsList = new ArrayList();

    private int selectedstudentforRemove;
    private String selectedStudentsforRemoveName = "";

    private String reasonName = "Mistakenly Entered";
    private List<SelectItem> reasonNameList = new ArrayList<SelectItem>();

    private String studentClasses = "0";
    private List<SelectItem> studentClassesList = new ArrayList<SelectItem>();
    private String studentYear = "0";
    private List<SelectItem> studentYearList = new ArrayList<SelectItem>();

    private List<SourceStudentsList> sourceStudentsList = new ArrayList();
    private boolean selectAllBox;
    private boolean selectAllBoxCur;

    HttpServletResponse response;
    HttpServletRequest request;

    private int def_province = 0;
    private int def_zone = 0;
    private int def_division = 0;
    private int def_school = 0;

    private ComLib comlib;
    private ComPath comPath;

    private LoginSession ls;

    @EJB
    private ComDev comDiv;

    @EJB
    UniDBLocal uni;

    @PostConstruct
    public void init() {

        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        initializeData();

        loadSchoolDetails(Integer.parseInt(request.getSession().getAttribute("LS").toString()));
        loadTeachers();
        loadSubjects();
    }

    public void loadSchoolDetails(int ls_id) {

        LoginSession lss = (LoginSession) uni.find(ls_id, LoginSession.class);
        if (lss != null) {
            ls = lss;

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

    public void initializeData() {

        // Get Menu
        getYearList().add(new SelectItem("0", "Select"));

        String query = "SELECT g FROM Year g order by g.name  ASC";
        List<Year> listAS = uni.searchByQuery(query);
        for (Year cc : listAS) {

            getYearList().add(new SelectItem(cc.getName(), cc.getName()));
        }
        this.year = comlib.GetCurrentYear() + "";

        getReasonNameList().clear();

        getReasonNameList().add(new SelectItem("Mistakenly Entered", "Mistakenly Entered"));
        getReasonNameList().add(new SelectItem("Leaving School", "Leaving School"));

    }

    public void loadSubjects() {
        assignSubjectNameList.clear();
        assignSubjectNameList.add(new SelectItem("0", "Select"));

        String query = "SELECT g FROM Subjects g where g.isActive='1' order by g.name  ASC";
        List<Subjects> listAS = uni.searchByQuery(query);
        for (Subjects cc : listAS) {

            assignSubjectNameList.add(new SelectItem(cc.getId(), cc.getName()));
        }
    }

    public void loadTeachers() {

        // Get Teachers
        getTeacherNameList().clear();

        getTeacherNameList().add(new SelectItem("", "Select"));

        if (!(schoolName.equals("0") || schoolName == null)) {

            String query = "SELECT g FROM Teacher g where g.schoolId.id='" + Integer.parseInt(schoolName) + "' and g.isActive='1' order by g.generalUserProfileId.nameWithIn  ASC";
            List<Teacher> listAS = uni.searchByQuery(query);
            for (Teacher cc : listAS) {

                getTeacherNameList().add(new SelectItem(cc.getId(), cc.getGeneralUserProfileId().getNameWithIn()));
            }
        }

    }

    public void loadTypes() {

        // Get Province
        getProvinceNameList().add(new SelectItem("", "Select"));

        String query = "SELECT g FROM Province g order by g.name ASC";
        List<Province> listAS = uni.searchByQuery(query);
        for (Province cc : listAS) {

            getProvinceNameList().add(new SelectItem(cc.getId(), cc.getName()));
        }
        setProvinceName(def_province + "");
        getEducationZoneList();

        // Get Grade
        getGradeNameList().add(new SelectItem("", "Select"));

        String querysg = "SELECT g FROM Grade g ";
        List<Grade> list_grade = uni.searchByQuery(querysg);
        for (Grade cc : list_grade) {

            getGradeNameList().add(new SelectItem(cc.getId(), cc.getName()));
        }

        // Get Stream
        getStreamNameList().add(new SelectItem("", "Select"));

        String querys = "SELECT g FROM Streams g where g.id!='1' order by g.name ASC";
        List<Streams> list_stream = uni.searchByQuery(querys);
        for (Streams cc : list_stream) {

            getStreamNameList().add(new SelectItem(cc.getId(), cc.getName()));
        }

    }

    public String getStreams() {

        if (gradeName.equals("12") || gradeName.equals("13")) {

            visibleStream = "block";
        } else {
            visibleStream = "none";

        }

        return null;
    }

    public String getStreamsSubject() {

        if (gradeSubjectName.equals("12") || gradeSubjectName.equals("13")) {

            visibleStreamSubject = "block";
        } else {
            visibleStreamSubject = "none";

        }

        return null;
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
        setZoneName(getDef_zone() + "");

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
        setDivisionName(getDef_division() + "");
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
        setSchoolName(getDef_school() + "");

        loadGradeClasses();

        return null;
    }

    public List<String> LoadSubjectsAutoComplete(String query) {

        List<String> results = new ArrayList<String>();

        List<Subjects> emp_list = uni.searchByQuery("SELECT e from Subjects e where  e.name like '%" + query + "%' and e.isActive='1'  order by e.name ASC");
        for (Subjects e : emp_list) {

            results.add(e.getName());
        }

        return results;
    }

    public String SaveCreateNewClass() {

        FacesMessage msg = null;

        boolean checkStream = true;

        if (gradeName.equals("12") || gradeName.equals("13")) {
            if (streamName.equals("")) {
                checkStream = false;
            }

        }

        if (checkStream == false) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select Stream !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (this.schoolName.equals("0")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select School !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else {

            gradeClassName = gradeClassName.trim();
            boolean check_class_format = true;

            String first = Character.toString(gradeClassName.charAt(0));

            if (gradeClassName.matches("[0-9]+")) {

                check_class_format = true;
            } else if (first.matches("[0-9]+")) {
                check_class_format = false;

            }
            if (check_class_format == false) {
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "First Letter of the Class Name cannot be a Number !", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);

            } else {

                String stream = "1";
                if (gradeName.equals("12") || gradeName.equals("13")) {
                    stream = streamName;
                }

                boolean result = comDiv.saveCreateNewClass(Integer.parseInt(schoolName), Integer.parseInt(gradeName), gradeClassName, Integer.parseInt(stream));

                if (result == true) {

                    this.gradeClassName = "";
                    loadGradeSubjects();

                    msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succesfully Saved !", "");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                } else {
                    msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Duplicate Record Found !", "");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                }
            }

        }

        return null;
    }

    public String SaveSubject() {

        FacesMessage msg = null;

        this.subjectName = subjectName.trim();

        RequestContext requestContext = RequestContext.getCurrentInstance();

        boolean checkStream = true;

        if (gradeSubjectName.equals("12") || gradeSubjectName.equals("13")) {
            if (streamSubjectName.equals("")) {
                checkStream = false;
            }

        }

        if (checkStream == false) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select Stream !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else if (schoolName.equals("0")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select School !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else if (this.subjectName.equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Enter Subject !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else {

            String stream = "1";
            if (gradeSubjectName.equals("12") || gradeSubjectName.equals("13")) {
                stream = streamSubjectName;
            }

            boolean result = comDiv.saveCreateNewSubject(Integer.parseInt(schoolName), Integer.parseInt(gradeSubjectName), this.subjectName, Integer.parseInt(stream), ls);

            if (result != false) {

                this.subjectName = "";

                requestContext.execute("$('#subjectModalMsg2').html('You have introduced a new subject name to our database. The name should be verified by the authorities. We will contact you within 48 hours.')");
                requestContext.execute("$('#subjectModalMsg3').html('ඔබ විසින් අලුතෙන් විෂය නාමයක් අප දත්ත පදතිය වෙත හඳුන්වා දී තිබේ. මෙම නාමය බලයලත් නිලධාරීන් විසින් තහවුරු කල යුතුයි. පැය 48ක් යාමට මත්තෙන් මේ පිළිබඳව ඔබව දැනුවත් කිරීමට බලාපොරොත්තු වෙමු.')");
                requestContext.execute("PF('subject_error2').show()");

//                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succesfully Saved !", "");
//                FacesContext.getCurrentInstance().addMessage(null, msg);
                loadGradeSubjects();

            } else {
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Duplicate Record Found !", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);

            }

        }

        return null;
    }

    public void loadAssignSubjectTeacher() {
        int gcs_id = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("gcs_id"));

        this.selectedGCSID = gcs_id;

        GradeClassStream gcs = (GradeClassStream) uni.find(gcs_id, GradeClassStream.class);

        this.assignSubjectModelClassName = gcs.getGradeId().getName() + "-" + gcs.getClassesId().getName();

        loadAssignSubjectTeacherContent();

        RequestContext requestContext = RequestContext.getCurrentInstance();

        requestContext.execute("PF('AssignSubjectTearcherDLG').show()");

    }

    public void loadAssignSubjectTeacherContent() {

        gradeclassSubjectTeacherList.clear();

        List<SelectItem> teacherNameList1 = new ArrayList<SelectItem>();
        // Get Teachers
        teacherNameList1.add(new SelectItem("0", "Subject Teacher Not Assigned"));

        String query1 = "SELECT g FROM Teacher g where g.schoolId.id='" + Integer.parseInt(schoolName) + "' and g.isActive='1' order by g.generalUserProfileId.nameWithIn ASC";
        List<Teacher> listAS1 = uni.searchByQuery(query1);
        for (Teacher cc : listAS1) {

            teacherNameList1.add(new SelectItem(cc.getId(), cc.getGeneralUserProfileId().getNameWithIn()));
        }
        System.out.println("selectedGCSID " + selectedGCSID);
//        GradeClassStreamManager gcsm = null;
//        String query_gcsm = "SELECT g FROM GradeClassStreamManager g where g.gradeClassStreamId.id='" + selectedGCSID + "'  and g.yearId.name='" + year + "'  and g.isActive='1'  ";
//        List<GradeClassStreamManager> list_gcsm = uni.searchByQuerySingle(query_gcsm);
//        if (list_gcsm.size() > 0) {
//            gcsm = list_gcsm.iterator().next();
//            System.out.println("gcsm " + gcsm.getId());

        String query_ols1 = "SELECT g FROM GradeClassHasSubjects g where g.gradeClassStreamId.id='" + selectedGCSID + "' and g.subjectsId.isActive='1'   and g.isActive='1' order by g.subjectsId.name ASC ";
        List<GradeClassHasSubjects> listAS_ols1 = uni.searchByQuery(query_ols1);
        for (GradeClassHasSubjects cc_ols : listAS_ols1) {
            String teacherN = "0";
            String query_ols = "SELECT g FROM GradeClassSubjectTeacher g where g.isActive='1' and g.gradeClassHasSubjectsId.id='" + cc_ols.getId() + "' and g.gradeClassStreamManagerId.yearId.name='" + year + "' ";
            List<GradeClassSubjectTeacher> listAS_ols = uni.searchByQuery(query_ols);
            for (GradeClassSubjectTeacher cc_olsub : listAS_ols) {
                teacherN = cc_olsub.getTeacherId().getId() + "";
               }

            gradeclassSubjectTeacherList.add(new GradeClassSubjectTeacherList(cc_ols.getSubjectsId().getId(), cc_ols.getSubjectsId().getName(), teacherN, teacherNameList1));

        }

//            String query_ols = "SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.id='" + gcsm.getId() + "' and g.gradeClassHasSubjectsId.subjectsId.isActive='1'  and g.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + Integer.parseInt(schoolName) + "'  and g.isActive='1' order by g.gradeClassHasSubjectsId.subjectsId.name ASC ";
//            List<GradeClassSubjectTeacher> listAS_ols = uni.searchByQuery(query_ols);
//            for (GradeClassSubjectTeacher cc_ols : listAS_ols) {
//
//                String teacherN = "0";
//                if (cc_ols.getTeacherId() != null) {
//                    teacherN = cc_ols.getTeacherId().getId() + "";
//
//                }
//
//                gradeclassSubjectTeacherList.add(new GradeClassSubjectTeacherList(cc_ols.getGradeClassHasSubjectsId().getSubjectsId().getId(), cc_ols.getGradeClassHasSubjectsId().getSubjectsId().getName(), teacherN, teacherNameList1));
//
//            }
        System.out.println("danaaaaa!");
//        } else {
//            int yearold = Integer.parseInt(year) - 1;
//
//            String query_gcsmold = "SELECT g FROM GradeClassStreamManager g where g.gradeClassStreamId.id='" + selectedGCSID + "'  and g.yearId.name='" + yearold + "'  and g.isActive='1'  ";
//            List<GradeClassStreamManager> list_gcsm_old = uni.searchByQuerySingle(query_gcsmold);
//            if (list_gcsm_old.size() > 0) {
//
//                for (GradeClassStreamManager gcsm_old : list_gcsm_old) {
//
//                    GradeClassStreamManager gcsm_new = new GradeClassStreamManager();
//                    gcsm_new.setGradeClassStreamId(gcsm_old.getGradeClassStreamId());
//                    gcsm_new.setIsActive(1);
//                    gcsm_new.setTeacherId(null);
//                    gcsm_new.setYearId((Year) uni.find(Integer.parseInt(year), Year.class));
//                    uni.create(gcsm_new);
//                    
//                    
//                    sd
//                    
//                }
//
//            }

//        }
    }

    public void loadCurrentStudents() {
        int gcs_id = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("gcs_id"));

        this.selectedGCSIDforStudents = gcs_id;

        GradeClassStream gcs = (GradeClassStream) uni.find(gcs_id, GradeClassStream.class);

        this.classStudentsClassName = year + " (" + gcs.getGradeId().getName() + "-" + gcs.getClassesId().getName() + ")";

        loadCurrentStudentsContent();

        getStudentYearList().clear();
        getStudentYearList().add(new SelectItem("0", "Select"));
        String query = "SELECT g FROM Year g order by g.name  ASC";
        List<Year> listAS = uni.searchByQuery(query);
        for (Year cc : listAS) {

            getStudentYearList().add(new SelectItem(cc.getName(), cc.getName()));
        }
        this.studentYear = comlib.GetCurrentYear() + "";

        getStudentClassesList().clear();
        getStudentClassesList().add(new SelectItem("0", "Select"));

        String query_ol = "SELECT g FROM GradeClassStream g where g.schoolId.id='" + Integer.parseInt(schoolName) + "' and g.gradeId.id!='12' and g.gradeId.id!='13'  order by g.gradeId.id ASC ";
        List<GradeClassStream> listAS_ol = uni.searchByQuery(query_ol);
        for (GradeClassStream cc_ol : listAS_ol) {

            getStudentClassesList().add(new SelectItem(cc_ol.getId(), cc_ol.getGradeId().getName() + " - " + cc_ol.getClassesId().getName()));
        }
        String query_al = "SELECT g FROM GradeClassStream g where g.schoolId.id='" + Integer.parseInt(schoolName) + "' and (g.gradeId.id='12' or g.gradeId.id='13')  order by g.gradeId.id ASC ";
        List<GradeClassStream> listAS_al = uni.searchByQuery(query_al);
        for (GradeClassStream cc_al : listAS_al) {

            getStudentClassesList().add(new SelectItem(cc_al.getId(), cc_al.getGradeId().getName() + " - " + cc_al.getClassesId().getName() + " " + cc_al.getStreamsId().getName()));
        }

        RequestContext requestContext = RequestContext.getCurrentInstance();

        requestContext.execute("PF('ClassStudentsDLG').show()");

    }

    public void loadCurrentStudentsContent() {

        currentStudentsList.clear();
        sourceStudentsList.clear();
        studentClasses = "0";
        studentYear = "0";
        selectAllBox = false;
        int i = 1;
        String query = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.gradeClassStreamId.id='" + this.selectedGCSIDforStudents + "' and g.gradeClassStreamManagerId.yearId.name='" + year + "' and g.isRemoved='0'";
        List<GradeClassStudents> listAS1 = uni.searchByQuery(query);
        for (GradeClassStudents cc : listAS1) {

            currentStudentsList.add(new CurrentStudentsList(i, cc.getId(), cc.getStudentsId().getStudentId(), cc.getStudentsId().getGeneralUserProfileId().getNameWithIn(), false));

            i++;
        }

    }

    public void removeStudentsLoadReason() {

        int gcs_id = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("gcs_id"));
        String name = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("name");
        selectedstudentforRemove = gcs_id;
        setSelectedStudentsforRemoveName(name);
        RequestContext requestContext = RequestContext.getCurrentInstance();

        requestContext.execute("PF('reasonDLG').show()");

    }

    public void removeStudents() {

        if (ls != null) {

            String data = "1";
            if (reasonName.equals("Leaving School")) {
                data = "2";
            }

            String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + selectedstudentforRemove + "' and g.isRemoved='0' ";
            List<StudentMarks> listm = uni.searchByQuery(querym);
            if (listm.size() > 0) {
                for (StudentMarks sm : listm) {

                    DataChangedLogManager gtlm = new DataChangedLogManager();
                    gtlm.setAttributeName("is_removed");
                    gtlm.setComment(reasonName);
                    gtlm.setDate(new Date());
                    gtlm.setNewData(data);
                    gtlm.setOldData(sm.getIsRemoved() + "");
                    gtlm.setReference(sm.getId());
                    gtlm.setTableManagerId((TableManager) uni.find(2, TableManager.class));
                    gtlm.setUserLoginId(ls.getUserLoginId());
                    uni.create(gtlm);

                    sm.setIsRemoved(1);
                    uni.update(sm);
                }
            }

            GradeClassStudents gcs = (GradeClassStudents) uni.find(selectedstudentforRemove, GradeClassStudents.class);

            DataChangedLogManager gtlm = new DataChangedLogManager();
            gtlm.setAttributeName("is_removed");
            gtlm.setComment(reasonName);
            gtlm.setDate(new Date());
            gtlm.setNewData(data);
            gtlm.setOldData(gcs.getIsRemoved() + "");
            gtlm.setReference(gcs.getId());
            gtlm.setTableManagerId((TableManager) uni.find(1, TableManager.class));
            gtlm.setUserLoginId(ls.getUserLoginId());
            uni.create(gtlm);

            gcs.setIsRemoved(1);
            uni.update(gcs);

            loadCurrentStudentsContent();
            RequestContext requestContext = RequestContext.getCurrentInstance();

            requestContext.execute("PF('reasonDLG').hide()");

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Removed !", "");

        }
    }

    public void removeClassConfirmation() {
        int gcs_id = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("gcs_id"));

        removeClass = gcs_id;

        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("PF('del_class_error').show()");
    }

    public void removeClass() {

        System.out.println("remo " + removeClass);

        GradeClassStream gcs = (GradeClassStream) uni.find(removeClass, GradeClassStream.class);
        gcs.setIsActive(0);
        uni.update(gcs);

        DataChangedLogManager gtlm = new DataChangedLogManager();
        gtlm.setAttributeName("is_active");
        gtlm.setComment("Class Removed");
        gtlm.setDate(new Date());
        gtlm.setNewData("0");
        gtlm.setOldData("1");
        gtlm.setReference(gcs.getId());
        gtlm.setTableManagerId((TableManager) uni.find(7, TableManager.class));
        gtlm.setUserLoginId(ls.getUserLoginId());
        uni.create(gtlm);

        String queryc = "SELECT g FROM GradeClassStreamManager g where g.gradeClassStreamId.id='" + gcs.getId() + "'";
        List<GradeClassStreamManager> listmc = uni.searchByQuery(queryc);
        for (GradeClassStreamManager gcsm : listmc) {

            gcsm.setIsActive(0);
            uni.update(gcsm);

            DataChangedLogManager gtlm1 = new DataChangedLogManager();
            gtlm1.setAttributeName("is_active");
            gtlm1.setComment("Class Removed");
            gtlm1.setDate(new Date());
            gtlm1.setNewData("0");
            gtlm1.setOldData("1");
            gtlm1.setReference(gcsm.getId());
            gtlm1.setTableManagerId((TableManager) uni.find(8, TableManager.class));
            gtlm1.setUserLoginId(ls.getUserLoginId());
            uni.create(gtlm1);

            String querys = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.id='" + gcsm.getId() + "' and g.isRemoved='0' ";
            List<GradeClassStudents> lists = uni.searchByQuery(querys);
            if (lists.size() > 0) {
                for (GradeClassStudents gcst : lists) {
                    gcst.setIsRemoved(1);
                    uni.update(gcst);

                    DataChangedLogManager gtlm2 = new DataChangedLogManager();
                    gtlm2.setAttributeName("is_removed");
                    gtlm2.setComment("Class Removed");
                    gtlm2.setDate(new Date());
                    gtlm2.setNewData("1");
                    gtlm2.setOldData("0");
                    gtlm2.setReference(gcst.getId());
                    gtlm2.setTableManagerId((TableManager) uni.find(1, TableManager.class));
                    gtlm2.setUserLoginId(ls.getUserLoginId());
                    uni.create(gtlm2);

                }

            }

            String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.gradeClassStreamManagerId.id='" + gcsm.getId() + "' and g.isRemoved='0' ";
            List<StudentMarks> listm = uni.searchByQuery(querym);
            if (listm.size() > 0) {
                for (StudentMarks sm : listm) {

                    sm.setIsRemoved(1);
                    uni.update(sm);

                    DataChangedLogManager gtlm2 = new DataChangedLogManager();
                    gtlm2.setAttributeName("is_removed");
                    gtlm2.setComment("Class Removed");
                    gtlm2.setDate(new Date());
                    gtlm2.setNewData("1");
                    gtlm2.setOldData("0");
                    gtlm2.setReference(sm.getId());
                    gtlm2.setTableManagerId((TableManager) uni.find(2, TableManager.class));
                    gtlm2.setUserLoginId(ls.getUserLoginId());
                    uni.create(gtlm2);
                }
            }

        }
        loadGradeClasses();
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("PF('del_class_error').hide()");
        FacesMessage msg = null;
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Removed !", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public void removeSubjectConfirmation() {
        int sub_id = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("sub_id"));

        removeSubject = sub_id;

        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("PF('del_subject_error').show()");

    }
    public void assignSubjectToClass() {
        int sub_id = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("sub_id"));

        String query_ols1 = "SELECT g FROM GradeClassHasSubjects g where g.gradeClassStreamId.id='" + selectedGCSID + "' and g.subjectsId.isActive='1'   and g.isActive='1' order by g.subjectsId.name ASC ";
        List<GradeClassHasSubjects> listAS_ols1 = uni.searchByQuery(query_ols1);
        for (GradeClassHasSubjects cc_ols : listAS_ols1) {
            
        }
        
        

    }

    public void removeSubject() {

        System.out.println("awawawawgg" + removeSubject);
        System.out.println("awawawawgg" + year);
        System.out.println("awawawawgg" + selectedGCSID);

        GradeClassSubjectTeacher gcste = null;
        List<GradeClassSubjectTeacher> gcst_list = uni.searchByQuerySingle("SELECT im FROM GradeClassSubjectTeacher im WHERE im.gradeClassHasSubjectsId.subjectsId.id='" + removeSubject + "' and im.gradeClassStreamManagerId.yearId.name='" + year + "' and im.gradeClassStreamManagerId.gradeClassStreamId.id='" + selectedGCSID + "'  ");
        if (gcst_list.size() > 0) {
            System.out.println("awawawawgg");

            gcste = gcst_list.iterator().next();

            gcste.setIsActive(0);
            uni.update(gcste);

            DataChangedLogManager gtlm2 = new DataChangedLogManager();
            gtlm2.setAttributeName("is_active");
            gtlm2.setComment("Subject Removed");
            gtlm2.setDate(new Date());
            gtlm2.setNewData("0");
            gtlm2.setOldData("1");
            gtlm2.setReference(gcste.getId());
            gtlm2.setTableManagerId((TableManager) uni.find(9, TableManager.class));
            gtlm2.setUserLoginId(ls.getUserLoginId());
            uni.create(gtlm2);

            String queryc = "SELECT g FROM GradeClassStreamManager g where g.gradeClassStreamId.id='" + selectedGCSID + "' and g.yearId.name='" + year + "'";
            List<GradeClassStreamManager> listmc = uni.searchByQuery(queryc);
            for (GradeClassStreamManager gcsm : listmc) {
                String querys = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.id='" + gcsm.getId() + "' and g.isRemoved='0' ";
                List<GradeClassStudents> lists = uni.searchByQuery(querys);
                if (lists.size() > 0) {
                    for (GradeClassStudents gcst : lists) {

                        String queryst = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassStudentsId.gradeClassStreamManagerId.id='" + gcsm.getId() + "' and g.gradeClassStudentsId.id='" + gcst.getId() + "' and g.gradeClassHasSubjectsId.id='" + gcste.getGradeClassHasSubjectsId().getId() + "' ";
                        List<GradeClassStudentsHasSubjects> listst = uni.searchByQuery(queryst);
                        if (listst.size() > 0) {
                            for (GradeClassStudentsHasSubjects gcstt : listst) {
                                uni.remove(gcstt.getId(), GradeClassStudentsHasSubjects.class);

                            }

                        }

                        String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcst.getId() + "' and g.gradeClassHasSubjectsId.id='" + gcste.getGradeClassHasSubjectsId().getId() + "' and g.isRemoved='0' ";
                        List<StudentMarks> listm = uni.searchByQuery(querym);
                        if (listm.size() > 0) {
                            for (StudentMarks sm : listm) {

                                sm.setIsRemoved(1);
                                uni.update(sm);

                                DataChangedLogManager gtlm3 = new DataChangedLogManager();
                                gtlm3.setAttributeName("is_removed");
                                gtlm3.setComment("Subject Removed");
                                gtlm3.setDate(new Date());
                                gtlm3.setNewData("1");
                                gtlm3.setOldData("0");
                                gtlm3.setReference(sm.getId());
                                gtlm3.setTableManagerId((TableManager) uni.find(2, TableManager.class));
                                gtlm3.setUserLoginId(ls.getUserLoginId());
                                uni.create(gtlm3);
                            }
                        }
                    }

                }

            }
        }

        loadAssignSubjectTeacherContent();
        loadGradeSubjects();
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("PF('del_subject_error').hide()");
        FacesMessage msg = null;
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Removed !", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public synchronized void addStudentsToClass() {
        boolean isSelected = false;
        for (SourceStudentsList so : sourceStudentsList) {
            if (so.isSelected()) {
                isSelected = true;
                break;
            }
        }
        FacesMessage msg = null;
        if (isSelected == false) {

            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select atleast one Student !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else {

            System.out.println("selectedGCSIDforStudents " + selectedGCSIDforStudents);
            System.out.println("year " + year);

            GradeClassStreamManager gcsm = null;
            List<GradeClassStreamManager> gcsm_list = uni.searchByQuerySingle("SELECT im FROM GradeClassStreamManager im WHERE im.yearId.name='" + year + "' and im.gradeClassStreamId.id='" + selectedGCSIDforStudents + "'");
            if (gcsm_list.size() > 0) {
                gcsm = gcsm_list.iterator().next();
            } else {

                Year cl = null;
                List<Year> cl_list = uni.searchByQuerySingle("SELECT im FROM Year im WHERE im.name='" + year + "'");
                if (cl_list.size() > 0) {
                    cl = cl_list.iterator().next();

                } else {
                    cl = new Year();
                    cl.setName(year);
                    uni.create(cl);
                }

                gcsm = new GradeClassStreamManager();
                gcsm.setGradeClassStreamId((GradeClassStream) uni.find(selectedGCSIDforStudents, GradeClassStream.class));
                gcsm.setYearId(cl);
                uni.create(gcsm);
            }

            for (SourceStudentsList so : sourceStudentsList) {
                if (so.isSelected()) {

                    List<GradeClassStudents> gcst_list = uni.searchByQuerySingle("SELECT im FROM GradeClassStudents im WHERE im.gradeClassStreamManagerId.id='" + gcsm.getId() + "' and im.studentsId.id='" + so.getSid() + "'  and im.isRemoved='0' ");
                    if (gcst_list.isEmpty()) {

                        GradeClassStudents gcst = new GradeClassStudents();
                        gcst.setGradeClassStreamManagerId(gcsm);
                        gcst.setStudentsId((Students) uni.find(so.getSid(), Students.class));
                        gcst.setIsRemoved(0);
                        uni.create(gcst);
                    }

                }
            }
            loadCurrentStudentsContent();
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Added !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    public synchronized void RemoveStudents() {
        boolean isSelected = false;
        for (CurrentStudentsList so : currentStudentsList) {
            if (so.isSelected()) {
                isSelected = true;
                break;
            }
        }
        FacesMessage msg = null;
        if (isSelected == false) {

            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select atleast one Student !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else {
            for (CurrentStudentsList so : currentStudentsList) {
                if (so.isSelected()) {

                    String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + so.getGcsId() + "' and g.isRemoved='0' ";
                    List<StudentMarks> listm = uni.searchByQuery(querym);
                    if (listm.size() > 0) {
                        for (StudentMarks sm : listm) {

                            DataChangedLogManager gtlm = new DataChangedLogManager();
                            gtlm.setAttributeName("is_removed");
                            gtlm.setComment(reasonName);
                            gtlm.setDate(new Date());
                            gtlm.setNewData("1");
                            gtlm.setOldData("0");
                            gtlm.setReference(sm.getId());
                            gtlm.setTableManagerId((TableManager) uni.find(2, TableManager.class));
                            gtlm.setUserLoginId(ls.getUserLoginId());
                            uni.create(gtlm);

                            sm.setIsRemoved(1);
                            uni.update(sm);
                        }
                    }

                    GradeClassStudents gcs = (GradeClassStudents) uni.find(so.getGcsId(), GradeClassStudents.class);

                    DataChangedLogManager gtlm = new DataChangedLogManager();
                    gtlm.setAttributeName("is_removed");
                    gtlm.setComment(reasonName);
                    gtlm.setDate(new Date());
                    gtlm.setNewData("1");
                    gtlm.setOldData("0");
                    gtlm.setReference(gcs.getId());
                    gtlm.setTableManagerId((TableManager) uni.find(1, TableManager.class));
                    gtlm.setUserLoginId(ls.getUserLoginId());
                    uni.create(gtlm);

                    gcs.setIsRemoved(1);
                    uni.update(gcs);

                }
            }
            loadCurrentStudentsContent();
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Removed !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void SaveAssignTeacher() {
        int teacherID = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("teacherID"));
        int gcmID = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("gcmID"));

        FacesMessage msg = null;

        if (!year.equals("0")) {

            boolean result = comDiv.saveAssignTeacher(teacherID, gcmID, year);

            if (result == true) {

                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succesfully Assigned !", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);

            }

        }

    }

    public void SaveSubjectTeacher() {
        int teacherID = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("teacherID"));
        int subID = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("subID"));

        FacesMessage msg = null;

        if (!(year.equals("0") && subID == 0)) {

            boolean result = comDiv.saveSubjectTeacher(teacherID, subID, year, selectedGCSID);

            if (result == true) {

                loadAssignSubjectTeacherContent();

                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succesfully Assigned !", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);

            }

        }

    }

    public void loadGradeSubjects() {

        getGradeArrayOL().clear();
        getGradeArrayAL().clear();

        if (!this.schoolName.equals("") && !this.schoolName.equals("0")) {

            String query_ol = "SELECT g FROM GradeClassStream g where g.schoolId.id='" + Integer.parseInt(schoolName) + "' and g.gradeId.id!='12' and g.gradeId.id!='13' and g.isActive='1' group by g.gradeId.id order by g.gradeId.id ASC ";
            List<GradeClassStream> listAS_ol = uni.searchByQuery(query_ol);
            for (GradeClassStream cc_ol : listAS_ol) {

                List<GradeSubjectArray> gsa_list = new ArrayList();
                String query_ols = "SELECT g FROM GradeClassHasSubjects g where g.gradeClassStreamId.gradeId.id='" + cc_ol.getGradeId().getId() + "' and g.gradeClassStreamId.schoolId.id='" + Integer.parseInt(schoolName) + "' and g.subjectsId.isActive='1' group by g.subjectsId.id order by g.subjectsId.name ASC ";
                List<GradeClassHasSubjects> listAS_ols = uni.searchByQuery(query_ols);
                for (GradeClassHasSubjects cc_ols : listAS_ols) {
                    gsa_list.add(new GradeSubjectArray(cc_ols.getSubjectsId().getId(), cc_ols.getSubjectsId().getName()));

                }

                if (gsa_list.isEmpty()) {
                    gsa_list.add(new GradeSubjectArray(0, "Subjects not Assigned"));

                }

                getGradeArrayOL().add(new GradeArray("Grade " + cc_ol.getGradeId().getName(), gsa_list));

            }

            String query_al = "SELECT g FROM GradeClassStream g where g.schoolId.id='" + Integer.parseInt(schoolName) + "' and (g.gradeId.id='12' or g.gradeId.id='13') and g.isActive='1' group by g.gradeId.id order by g.gradeId.id ASC ";
            List<GradeClassStream> listAS_al = uni.searchByQuery(query_al);
            for (GradeClassStream cc_al : listAS_al) {

                String query = "SELECT g FROM GradeClassStream g where g.schoolId.id='" + Integer.parseInt(schoolName) + "' and g.gradeId.id='" + cc_al.getGradeId().getId() + "' and g.isActive='1' group by g.streamsId order by g.streamsId.name,g.classesId.name ASC";
                List<GradeClassStream> listAS = uni.searchByQuery(query);
                for (GradeClassStream cc : listAS) {

                    List<GradeSubjectArray> gsa_list = new ArrayList();
                    String query_ols = "SELECT g FROM GradeClassHasSubjects g where g.gradeClassStreamId.gradeId.id='" + cc.getGradeId().getId() + "' and g.gradeClassStreamId.streamsId.id='" + cc.getStreamsId().getId() + "' and g.gradeClassStreamId.schoolId.id='" + Integer.parseInt(schoolName) + "' and g.subjectsId.isActive='1' group by g.subjectsId.id order by g.subjectsId.name ASC ";
                    List<GradeClassHasSubjects> listAS_ols = uni.searchByQuery(query_ols);
                    for (GradeClassHasSubjects cc_ols : listAS_ols) {
                        gsa_list.add(new GradeSubjectArray(cc_ols.getSubjectsId().getId(), cc_ols.getSubjectsId().getName()));

                    }

                    if (gsa_list.isEmpty()) {
                        gsa_list.add(new GradeSubjectArray(0, "Subjects not Assigned"));

                    }

                    getGradeArrayAL().add(new GradeArray("Grade " + cc_al.getGradeId().getName() + " - " + cc.getStreamsId().getName(), gsa_list));
                }
            }

        }

    }

    public void loadGradeClasses() {

        loadGradeSubjects();

        getGc_list_al().clear();
        getGc_list_ol().clear();

        if (!this.schoolName.equals("") && !this.schoolName.equals("0")) {

            getGradeSubjectNameList().clear();

            // Get Grade Subject
            getGradeSubjectNameList().add(new SelectItem("", "Select"));
            String query_gcs = "SELECT g FROM GradeClassStream g where g.schoolId.id='" + Integer.parseInt(schoolName) + "' and g.isActive='1' group by g.gradeId.id order by g.gradeId.id ASC ";
            List<GradeClassStream> listAS_gcs = uni.searchByQuery(query_gcs);
            for (GradeClassStream cc : listAS_gcs) {
                getGradeSubjectNameList().add(new SelectItem(cc.getGradeId().getId(), cc.getGradeId().getName()));
            }

            getStreamSubjectNameList().clear();

            // Get Stream Subject
            getStreamSubjectNameList().add(new SelectItem("", "Select"));

            String queryss = "SELECT g FROM GradeClassStream g where g.schoolId.id='" + Integer.parseInt(schoolName) + "' and (g.gradeId.id='12'  or g.gradeId.id='13') and g.isActive='1' group by g.streamsId  order by g.streamsId.name,g.classesId.name ASC";
            List<GradeClassStream> list_streams = uni.searchByQuery(queryss);
            for (GradeClassStream cc : list_streams) {
                getStreamSubjectNameList().add(new SelectItem(cc.getStreamsId().getId(), cc.getStreamsId().getName()));
            }

            List<SelectItem> teacherNameList1 = new ArrayList<SelectItem>();

            // Get Teachers
            teacherNameList1.add(new SelectItem("0", "Class Teacher Not Assigned"));

            String query1 = "SELECT g FROM Teacher g where g.schoolId.id='" + Integer.parseInt(schoolName) + "' and g.isActive='1' order by g.generalUserProfileId.nameWithIn ASC";
            List<Teacher> listAS1 = uni.searchByQuery(query1);
            for (Teacher cc : listAS1) {

                teacherNameList1.add(new SelectItem(cc.getId(), cc.getGeneralUserProfileId().getNameWithIn()));
            }

            String query_ol = "SELECT g FROM GradeClassStream g where g.schoolId.id='" + Integer.parseInt(schoolName) + "' and g.gradeId.id!='12' and g.gradeId.id!='13' and g.isActive='1' group by g.gradeId.id order by g.gradeId.id ASC ";
            List<GradeClassStream> listAS_ol = uni.searchByQuery(query_ol);
            for (GradeClassStream cc_ol : listAS_ol) {

                List<GradeClassArray> gca = new ArrayList();

                String query = "SELECT g FROM GradeClassStream g where g.schoolId.id='" + Integer.parseInt(schoolName) + "' and g.gradeId.id='" + cc_ol.getGradeId().getId() + "' and g.isActive='1'  order by g.streamsId.name,g.classesId.name ASC";
                List<GradeClassStream> listAS = uni.searchByQuery(query);
                for (GradeClassStream cc : listAS) {

                    boolean as_assigned = true;
                    String teacherN = "0";
                    String btn_color = "green-btn";
                    boolean is_sub_assgn = true;
                    if (!year.equals("0")) {

                        String queryt = "SELECT g FROM GradeClassStreamManager g where g.gradeClassStreamId.id='" + cc.getId() + "' and g.yearId.name='" + year + "'";
                        List<GradeClassStreamManager> listASt = uni.searchByQuerySingle(queryt);
                        if (listASt.size() > 0) {

                            if (listASt.iterator().next().getTeacherId() != null) {
                                teacherN = listASt.iterator().next().getTeacherId().getId() + "";
                                as_assigned = false;
                            }
                            String query_ols = "SELECT g FROM GradeClassHasSubjects g where g.gradeClassStreamId.id='" + cc.getId() + "' and g.gradeClassStreamId.schoolId.id='" + Integer.parseInt(schoolName) + "' and g.isActive='1' order by g.subjectsId.name ASC ";
                            List<GradeClassHasSubjects> listAS_ols = uni.searchByQuery(query_ols);
                            if (listAS_ols.size() > 0) {
                                for (GradeClassHasSubjects cc_ols : listAS_ols) {

                                    String querysub = "SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.gradeClassStreamId.id='" + cc.getId() + "'  and g.gradeClassStreamManagerId.yearId.name='" + year + "' and g.gradeClassHasSubjectsId.id='" + cc_ols.getId() + "' and g.isActive='1'";
                                    List<GradeClassSubjectTeacher> sub_listASt = uni.searchByQuerySingle(querysub);

                                    if (sub_listASt.size() > 0) {

                                        if (sub_listASt.iterator().next().getTeacherId() == null) {

                                            is_sub_assgn = false;
                                        } else {

                                        }
                                    } else {

                                        is_sub_assgn = false;
                                    }

                                }
                            } else {

                                is_sub_assgn = false;
                            }

                            if (is_sub_assgn == false) {

                                btn_color = "red-btn";
                            }

                        } else {
                            btn_color = "red-btn";
                        }
                    } else {
                        btn_color = "red-btn";

                    }

                    gca.add(new GradeClassArray(cc.getId(), cc.getGradeId().getName() + " - " + cc.getClassesId().getName(), teacherN, as_assigned, teacherNameList1, btn_color));
                }
                gc_list_ol.add(new GradeClass("Grade " + cc_ol.getGradeId().getName(), gca));

            }

            // al list
            String query_al = "SELECT g FROM GradeClassStream g where g.schoolId.id='" + Integer.parseInt(schoolName) + "' and (g.gradeId.id='12' or g.gradeId.id='13') and g.isActive='1' group by g.gradeId.id order by g.gradeId.id ASC ";
            List<GradeClassStream> listAS_al = uni.searchByQuery(query_al);
            for (GradeClassStream cc_al : listAS_al) {

                List<GradeClassArray> gca2 = new ArrayList();

                String query2 = "SELECT g FROM GradeClassStream g where g.schoolId.id='" + Integer.parseInt(schoolName) + "' and g.gradeId.id='" + cc_al.getGradeId().getId() + "' and g.isActive='1'  order by g.streamsId.name,g.classesId.name ASC";
                List<GradeClassStream> listAS2 = uni.searchByQuery(query2);
                for (GradeClassStream cc : listAS2) {

                    boolean as_assigned = true;
                    String teacherN = "0";
                    String btn_color = "green-btn";
                    boolean is_sub_assgn = true;

                    if (!year.equals("0")) {

                        String queryt = "SELECT g FROM GradeClassStreamManager g where g.gradeClassStreamId.id='" + cc.getId() + "' and g.yearId.name='" + year + "'";
                        List<GradeClassStreamManager> listASt = uni.searchByQuery(queryt);
                        if (listASt.size() > 0) {
                            if (listASt.iterator().next().getTeacherId() != null) {
                                teacherN = listASt.iterator().next().getTeacherId().getId() + "";
                                as_assigned = false;
                            }
                            String query_ols = "SELECT g FROM GradeClassHasSubjects g where g.gradeClassStreamId.id='" + cc.getId() + "' and g.gradeClassStreamId.schoolId.id='" + Integer.parseInt(schoolName) + "' and g.isActive='1' order by g.subjectsId.name ASC ";
                            List<GradeClassHasSubjects> listAS_ols = uni.searchByQuery(query_ols);
                            if (listAS_ols.size() > 0) {
                                for (GradeClassHasSubjects cc_ols : listAS_ols) {

                                    String querysub = "SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.gradeClassStreamId.id='" + cc.getId() + "' and g.gradeClassStreamManagerId.yearId.name='" + year + "' and g.gradeClassHasSubjectsId.id='" + cc_ols.getId() + "'";
                                    List<GradeClassSubjectTeacher> sub_listASt = uni.searchByQuerySingle(querysub);
                                    if (sub_listASt.isEmpty()) {
                                        is_sub_assgn = false;
                                        break;
                                    }

                                }
                            } else {

                                is_sub_assgn = false;
                            }

                            if (is_sub_assgn == false) {

                                btn_color = "red-btn";
                            }
                        } else {
                            btn_color = "red-btn";
                        }

                    } else {
                        btn_color = "red-btn";

                    }

                    gca2.add(new GradeClassArray(cc.getId(), cc.getGradeId().getName() + " - " + cc.getClassesId().getName() + " | " + cc.getStreamsId().getName(), teacherN, as_assigned, teacherNameList1, btn_color));
                }
                gc_list_al.add(new GradeClass("Grade " + cc_al.getGradeId().getName(), gca2));

            }
        }

    }

    public String setSelectedCheckBoxes() {

        if (this.selectAllBox == true) {

            for (int i = 0; i < this.sourceStudentsList.size(); i++) {

                this.sourceStudentsList.get(i).setSelected(true);

            }
        } else {
            for (int i = 0; i < this.sourceStudentsList.size(); i++) {

                this.sourceStudentsList.get(i).setSelected(false);
            }
        }

        return null;
    }

    public String setSelectedCheckBoxesCur() {

        if (this.selectAllBoxCur == true) {

            for (int i = 0; i < this.currentStudentsList.size(); i++) {

                this.currentStudentsList.get(i).setSelected(true);

            }
        } else {
            for (int i = 0; i < this.currentStudentsList.size(); i++) {

                this.currentStudentsList.get(i).setSelected(false);
            }
        }

        return null;
    }

    public String searchSourceStudents() {

        sourceStudentsList.clear();

        FacesMessage msg = null;
        if (studentYear.equals("0")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select Year !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else if (this.studentClasses.equals("0")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select Class !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else {
            int i = 1;
            List<GradeClassStudents> gcs_list = uni.searchByQuery("SELECT u FROM GradeClassStudents u where u.gradeClassStreamManagerId.yearId.name='" + studentYear + "' and u.gradeClassStreamManagerId.gradeClassStreamId.id='" + studentClasses + "' and u.isRemoved='0'");
            for (GradeClassStudents y : gcs_list) {

                sourceStudentsList.add(new SourceStudentsList(i, y.getStudentsId().getId(), y.getStudentsId().getStudentId(), y.getStudentsId().getGeneralUserProfileId().getNameWithIn(), false));
                i++;
            }

        }
        return null;
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

    public class GradeClass implements Serializable {

        String gradeName;
        List<GradeClassArray> gca;

        public GradeClass(String gradeName, List<GradeClassArray> gca) {
            this.gradeName = gradeName;
            this.gca = gca;
        }

        public List<GradeClassArray> getGca() {
            return gca;
        }

        public void setGca(List<GradeClassArray> gca) {
            this.gca = gca;
        }

        public String getGradeName() {
            return gradeName;
        }

        public void setGradeName(String gradeName) {
            this.gradeName = gradeName;
        }

    }

    public class GradeClassArray implements Serializable {

        private String classNames;
        private String teacherName;
        public boolean isAssigned;
        private int gcmID;
        private List<SelectItem> teacherNameList = new ArrayList<SelectItem>();
        private String btnColor;

        public GradeClassArray(int gcmID, String classNames, String teacherName, boolean isAssigned, List<SelectItem> teacherNameList, String btnColor) {
            this.classNames = classNames;
            this.teacherName = teacherName;
            this.isAssigned = isAssigned;
            this.gcmID = gcmID;
            this.teacherNameList = teacherNameList;
            this.btnColor = btnColor;
        }

        public String getClassNames() {
            return classNames;
        }

        public void setClassNames(String classNames) {
            this.classNames = classNames;
        }

        public int getGcmID() {
            return gcmID;
        }

        public void setGcmID(int gcmID) {
            this.gcmID = gcmID;
        }

        public boolean getIsAssigned() {
            return isAssigned;
        }

        public void setIsAssigned(boolean isAssigned) {
            this.isAssigned = isAssigned;
        }

        public List<SelectItem> getTeacherNameList() {
            return teacherNameList;
        }

        public void setTeacherNameList(List<SelectItem> teacherNameList) {
            this.teacherNameList = teacherNameList;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public String getBtnColor() {
            return btnColor;
        }

        public void setBtnColor(String btnColor) {
            this.btnColor = btnColor;
        }

    }

    public class GradeArray implements Serializable {

        private String gradeName;
        private List<GradeSubjectArray> gsa;

        public GradeArray(String gradeName, List<GradeSubjectArray> gsa) {
            this.gradeName = gradeName;
            this.gsa = gsa;
        }

        public String getGradeName() {
            return gradeName;
        }

        public void setGradeName(String gradeName) {
            this.gradeName = gradeName;
        }

        public List<GradeSubjectArray> getGsa() {
            return gsa;
        }

        public void setGsa(List<GradeSubjectArray> gsa) {
            this.gsa = gsa;
        }

    }

    public class GradeSubjectArray implements Serializable {

        private int subjectId;
        private String subjectName;

        public GradeSubjectArray(int subjectId, String subjectName) {
            this.subjectId = subjectId;
            this.subjectName = subjectName;
        }

        public int getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(int subjectId) {
            this.subjectId = subjectId;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

    }

    public class CurrentStudentsList implements Serializable {

        private int no;
        private int gcsId;
        private String indexNo;
        private String Name;
        private boolean selected;

        public CurrentStudentsList(int no, int gcsId, String indexNo, String Name, boolean selected) {
            this.no = no;
            this.gcsId = gcsId;
            this.indexNo = indexNo;
            this.Name = Name;
            this.selected = selected;
        }

        public int getGcsId() {
            return gcsId;
        }

        public void setGcsId(int gcsId) {
            this.gcsId = gcsId;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getIndexNo() {
            return indexNo;
        }

        public void setIndexNo(String indexNo) {
            this.indexNo = indexNo;
        }

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    public class SourceStudentsList implements Serializable {

        private int no;
        private int sid;
        private String indexNo;
        private String Name;
        private boolean selected;

        public SourceStudentsList(int no, int sid, String indexNo, String Name, boolean selected) {
            this.no = no;
            this.sid = sid;
            this.indexNo = indexNo;
            this.Name = Name;
            this.selected = selected;
        }

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }

        public String getIndexNo() {
            return indexNo;
        }

        public void setIndexNo(String indexNo) {
            this.indexNo = indexNo;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

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
        System.out.println("abc");
        return divisionName;

    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
        System.out.println("def");
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

    public String getVisibleStream() {
        return visibleStream;
    }

    public void setVisibleStream(String visibleStream) {
        this.visibleStream = visibleStream;
    }

    public String getGradeClassName() {
        return gradeClassName;
    }

    public void setGradeClassName(String gradeClassName) {
        this.gradeClassName = gradeClassName;
    }

    public List<GradeClass> getGc_list_ol() {
        return gc_list_ol;
    }

    public void setGc_list_ol(List<GradeClass> gc_list_ol) {
        this.gc_list_ol = gc_list_ol;
    }

    public List<GradeClass> getGc_list_al() {
        return gc_list_al;
    }

    public void setGc_list_al(List<GradeClass> gc_list_al) {
        this.gc_list_al = gc_list_al;
    }

    public String getGradeSubjectName() {
        return gradeSubjectName;
    }

    public void setGradeSubjectName(String gradeSubjectName) {
        this.gradeSubjectName = gradeSubjectName;
    }

    public List<SelectItem> getGradeSubjectNameList() {
        return gradeSubjectNameList;
    }

    public void setGradeSubjectNameList(List<SelectItem> gradeSubjectNameList) {
        this.gradeSubjectNameList = gradeSubjectNameList;
    }

    public String getVisibleStreamSubject() {
        return visibleStreamSubject;
    }

    public void setVisibleStreamSubject(String visibleStreamSubject) {
        this.visibleStreamSubject = visibleStreamSubject;
    }

    public String getStreamSubjectName() {
        return streamSubjectName;
    }

    public void setStreamSubjectName(String streamSubjectName) {
        this.streamSubjectName = streamSubjectName;
    }

    public List<SelectItem> getStreamSubjectNameList() {
        return streamSubjectNameList;
    }

    public void setStreamSubjectNameList(List<SelectItem> streamSubjectNameList) {
        this.streamSubjectNameList = streamSubjectNameList;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public List<GradeArray> getGradeArrayOL() {
        return gradeArrayOL;
    }

    public void setGradeArrayOL(List<GradeArray> gradeArrayOL) {
        this.gradeArrayOL = gradeArrayOL;
    }

    public List<GradeArray> getGradeArrayAL() {
        return gradeArrayAL;
    }

    public void setGradeArrayAL(List<GradeArray> gradeArrayAL) {
        this.gradeArrayAL = gradeArrayAL;
    }

    public int getSelectedGCSID() {
        return selectedGCSID;
    }

    public void setSelectedGCSID(int selectedGCSID) {
        this.selectedGCSID = selectedGCSID;
    }

    public String getAssignSubjectModelClassName() {
        return assignSubjectModelClassName;
    }

    public void setAssignSubjectModelClassName(String assignSubjectModelClassName) {
        this.assignSubjectModelClassName = assignSubjectModelClassName;
    }

    public List<GradeClassSubjectTeacherList> getGradeclassSubjectTeacherList() {
        return gradeclassSubjectTeacherList;
    }

    public void setGradeclassSubjectTeacherList(List<GradeClassSubjectTeacherList> gradeclassSubjectTeacherList) {
        this.gradeclassSubjectTeacherList = gradeclassSubjectTeacherList;
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

    public int getSelectedGCSIDforStudents() {
        return selectedGCSIDforStudents;
    }

    public void setSelectedGCSIDforStudents(int selectedGCSIDforStudents) {
        this.selectedGCSIDforStudents = selectedGCSIDforStudents;
    }

    public String getClassStudentsClassName() {
        return classStudentsClassName;
    }

    public void setClassStudentsClassName(String classStudentsClassName) {
        this.classStudentsClassName = classStudentsClassName;
    }

    public List<CurrentStudentsList> getCurrentStudentsList() {
        return currentStudentsList;
    }

    public void setCurrentStudentsList(List<CurrentStudentsList> currentStudentsList) {
        this.currentStudentsList = currentStudentsList;
    }

    public LoginSession getLs() {
        return ls;
    }

    public void setLs(LoginSession ls) {
        this.ls = ls;
    }

    public int getSelectedstudentforRemove() {
        return selectedstudentforRemove;
    }

    public void setSelectedstudentforRemove(int selectedstudentforRemove) {
        this.selectedstudentforRemove = selectedstudentforRemove;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public List<SelectItem> getReasonNameList() {
        return reasonNameList;
    }

    public void setReasonNameList(List<SelectItem> reasonNameList) {
        this.reasonNameList = reasonNameList;
    }

    public String getSelectedStudentsforRemoveName() {
        return selectedStudentsforRemoveName;
    }

    public void setSelectedStudentsforRemoveName(String selectedStudentsforRemoveName) {
        this.selectedStudentsforRemoveName = selectedStudentsforRemoveName;
    }

    public String getStudentClasses() {
        return studentClasses;
    }

    public void setStudentClasses(String studentClasses) {
        this.studentClasses = studentClasses;
    }

    public List<SelectItem> getStudentClassesList() {
        return studentClassesList;
    }

    public void setStudentClassesList(List<SelectItem> studentClassesList) {
        this.studentClassesList = studentClassesList;
    }

    public String getStudentYear() {
        return studentYear;
    }

    public void setStudentYear(String studentYear) {
        this.studentYear = studentYear;
    }

    public List<SelectItem> getStudentYearList() {
        return studentYearList;
    }

    public void setStudentYearList(List<SelectItem> studentYearList) {
        this.studentYearList = studentYearList;
    }

    public List<SourceStudentsList> getSourceStudentsList() {
        return sourceStudentsList;
    }

    public void setSourceStudentsList(List<SourceStudentsList> sourceStudentsList) {
        this.sourceStudentsList = sourceStudentsList;
    }

    public boolean isSelectAllBox() {
        return selectAllBox;
    }

    public void setSelectAllBox(boolean selectAllBox) {
        this.selectAllBox = selectAllBox;
    }

    public int getRemoveClass() {
        return removeClass;
    }

    public void setRemoveClass(int removeClass) {
        this.removeClass = removeClass;
    }

    public int getRemoveSubject() {
        return removeSubject;
    }

    public void setRemoveSubject(int removeSubject) {
        this.removeSubject = removeSubject;
    }

    public boolean isSelectAllBoxCur() {
        return selectAllBoxCur;
    }

    public void setSelectAllBoxCur(boolean selectAllBoxCur) {
        this.selectAllBoxCur = selectAllBoxCur;
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

    public String getAssignSubjectName() {
        return assignSubjectName;
    }

    public void setAssignSubjectName(String assignSubjectName) {
        this.assignSubjectName = assignSubjectName;
    }

    public List<SelectItem> getAssignSubjectNameList() {
        return assignSubjectNameList;
    }

    public void setAssignSubjectNameList(List<SelectItem> assignSubjectNameList) {
        this.assignSubjectNameList = assignSubjectNameList;
    }

}
