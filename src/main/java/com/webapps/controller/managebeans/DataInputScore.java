/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.managebeans;

import com.ejb.model.businesslogic.GoogleMail;
import com.ejb.model.common.ComDev;
import com.ejb.model.common.ComLib;
import com.ejb.model.common.ComPath;
import com.ejb.model.common.StoredProcedures;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.EducationDivision;
import com.ejb.model.entity.EducationZone;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.Province;
import com.ejb.model.entity.School;
import com.ejb.model.entity.StudentMarks;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.Year;
import com.webapps.controller.utilities.SortArraysDataInputScore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Thilini Madagama
 */
@ManagedBean(name = "dataInputScore")
@ViewScoped
public class DataInputScore {

    public String provinceName = "0";
    private List<SelectItem> provinceNameList = new ArrayList<SelectItem>();

    public String zoneName = "0";
    private List<SelectItem> zoneNameList = new ArrayList<SelectItem>();

    public String divisionName = "0";
    private List<SelectItem> divisionNameList = new ArrayList<SelectItem>();

    public String schoolName = "0";
    private List<SelectItem> schoolNameList = new ArrayList<SelectItem>();

//    public List<SchoolList> scoreSchoolList = new ArrayList();
    private List<SchoolNameList> scoreSchoolList = new ArrayList();
    public List<SearchingCriteria> searchingCriteriaList = new ArrayList();

    private boolean disabledFiledProvince = false;
    private boolean disabledFiledZone = false;
    private boolean disabledFiledDivision = false;
    private boolean disabledFiledSchool = false;
    private String selectedSchoolName;

    private int emailCount;
    private boolean selectAllBox;
    private String subject = "";
    private String emailContent = "";

    private List<TeacherDetails> alloList = new ArrayList();
    private List<TeacherDetails> notAlloList = new ArrayList();

    private int def_province = 0;
    private int def_zone = 0;
    private int def_division = 0;
    private int def_school = 0;

    private List<YearList> yearList = new ArrayList();

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
        loadSearchingCriteria();
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

        if (!divisionName.equals("0")) {

            getSchoolNameList().clear();

            // Get Province
            getSchoolNameList().add(new SelectItem("0", "All"));

            String query = "SELECT g FROM School g where g.educationDivisionId.id='" + Integer.parseInt(getDivisionName()) + "' order by g.generalOrganizationProfileId.name ASC";
            List<School> listAS = uni.searchByQuery(query);
            for (School cc : listAS) {

                getSchoolNameList().add(new SelectItem(cc.getId(), cc.getGeneralOrganizationProfileId().getName()));
            }
        }
        setSchoolName(getDef_school() + "");

        return null;
    }

    public void loadSearchingCriteria() {

        int curYear = comlib.GetCurrentYear();
        int k = 0;
        for (int i = curYear - 2; i <= curYear; i++) {

            searchingCriteriaList.add(new SearchingCriteria(k, i + "", false, false, false));
            k++;
        }
    }

    public String loadListOfSchools1() {
        final List<String> ar = new ArrayList();
        TestTask task = new TestTask(50);
        ExecutorService executor = Executors.newFixedThreadPool(50);
        List<Callable<String>> callables = new ArrayList<>(50);
        for (int i = 1; i < 10; i++) {

            callables.add(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    try {
                        ar.add("ss");
                        return "task  finished @ " + new Date();

                    } catch (Exception e) {
                        return "Interrupted";
                    }
                }
            });
        }
        try {
            List<Future<String>> results = executor.invokeAll(callables);
            for (Future<String> result : results) {
                System.out.println(result.get());
            }
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        executor.shutdown();
        for (String s : ar) {

            System.out.println("t " + s);
        }

        task.setState(" processed... ");
        return null;
    }

    public String loadListOfSchools() {

        getScoreSchoolList().clear();

        List<School> school_list = new ArrayList();

        final List<SchoolNameList> sc_list = new ArrayList();

        boolean check_term = false;

        for (SearchingCriteria sc : searchingCriteriaList) {

            if (sc.isTerm1Selected() || sc.isTerm2Selected() || sc.isTerm3Selected()) {

                check_term = true;
            }

        }

        FacesMessage msg = null;

        if (check_term == false) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select at least one Term", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {

            try {

                if (provinceName.equals("0")) {

                    String query = "SELECT g FROM School g  order by g.id ASC";

                    school_list = uni.searchByQuery(query);

                } else if (zoneName.equals("0")) {
                    String query = "SELECT g FROM School g where g.educationDivisionId.educationZoneId.provinceId.id='" + Integer.parseInt(provinceName) + "' order by g.id ASC";

                    school_list = uni.searchByQuery(query);

                } else if (divisionName.equals("0")) {
                    String query = "SELECT g FROM School g where g.educationDivisionId.educationZoneId.id='" + Integer.parseInt(zoneName) + "' order by g.id ASC";

                    school_list = uni.searchByQuery(query);

                } else if (schoolName.equals("0")) {
                    String query = "SELECT g FROM School g where g.educationDivisionId.id='" + Integer.parseInt(divisionName) + "' order by g.id ASC";

                    school_list = uni.searchByQuery(query);

                } else if (!schoolName.equals("0")) {
                    String query = "SELECT g FROM School g where g.id='" + Integer.parseInt(schoolName) + "'";

                    school_list = uni.searchByQuery(query);

                }

//                int k = 1;
//                TestTask task = new TestTask(school_list.size());
                ExecutorService executor = Executors.newFixedThreadPool(school_list.size());
                List<Callable<String>> callables = new ArrayList<>(school_list.size());

                for (final School v : school_list) {
                    System.out.println("awap" + v.getId());
                    callables.add(new Callable<String>() {
                        @Override
                        public String call() throws Exception {
                            try {
                                System.out.println("awa>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + v.getId());
                                double val = 0;

                                int selectedTerms = 0;
                                int enteredCount = 0;
//                    List<YearList> yearList = new ArrayList();

                                for (SearchingCriteria sc : searchingCriteriaList) {
//                        List<TermList> termList = new ArrayList();
                                    if (sc.term1Selected) {
                                        System.out.println("awa1");
                                        selectedTerms++;
                                        enteredCount += stored.get_data_entered_tot_count(sc.year_name, 1, v.getId());

//                                        List<StudentMarks> sm_list0 = uni.searchByQuery("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + v.getId() + "' and im.termsId.id='1' and im.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + sc.year_name + "'  and im.isRemoved='0' group by im.gradeClassStudentsId.studentsId.id");
//                                        enteredCount += sm_list0.size();
                                    }
                                    if (sc.term2Selected) {
                                        System.out.println("awa2");
                                        selectedTerms++;

                                        enteredCount += stored.get_data_entered_tot_count(sc.year_name, 2, v.getId());

//                                        List<StudentMarks> sm_list0 = uni.searchByQuery("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + v.getId() + "' and im.termsId.id='2' and im.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + sc.year_name + "'  and im.isRemoved='0' group by im.gradeClassStudentsId.studentsId.id");
//                                        enteredCount += sm_list0.size();
                                    }
                                    if (sc.term3Selected) {
                                        System.out.println("awa3");
                                        selectedTerms++;

                                        enteredCount += stored.get_data_entered_tot_count(sc.year_name, 3, v.getId());

//                                        List<StudentMarks> sm_list0 = uni.searchByQuery("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + v.getId() + "' and im.termsId.id='3' and im.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + sc.year_name + "'  and im.isRemoved='0' group by im.gradeClassStudentsId.studentsId.id");
//                                        enteredCount += sm_list0.size();
                                    }
//                        
                                }

                                if (v.getStudentsCount() != 0) {
                                    System.out.println(v.getStudentsCount());
                                    val = (enteredCount * 100) / (v.getStudentsCount() * selectedTerms);

                                }
                                int teacher_count = 0;
                                // teacher count
//                                String query = "SELECT g FROM Teacher g where g.schoolId.id='" + v.getId() + "' and g.isActive='1' order by g.generalUserProfileId.nameWithIn ASC";
//                                List<Teacher> listAS = uni.searchByQuery(query);
                                int loggedCount = 0;
//                                for (Teacher t : listAS) {
//                                    List<LoginSession> listAS1 = uni.searchByQuery("SELECT g FROM LoginSession g where g.userLoginId.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "' and g.userLoginGroupId.projectsId.id='1'");
//                                    if (listAS1.size() > 0) {
//                                        loggedCount++;
//
//                                    }
//
//                                }
//teacher_count=listAS.size();

                                double logg_perc = 0;
//                                if (listAS.size() > 0) {
//
//                                    logg_perc = ((loggedCount * 1.0) / listAS.size()) * 100;
//
//                                }

                                sc_list.add(new SchoolNameList(0, v.getId(), v.getGeneralOrganizationProfileId().getName(), comlib.getDouble(val), val, teacher_count + "", loggedCount + "", comlib.getDouble(logg_perc)));
//                    k++;
                                return "task  finished @ " + new Date();
                            } catch (Exception e) {
                                return "Interrupted";
                            }
                        }
                    });
                }
                try {
                    System.out.println("awa final");
                    List<Future<String>> results = executor.invokeAll(callables);
                    for (Future<String> result : results) {
                        System.out.println(result.get());
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
                executor.shutdown();

                SchoolNameList[] addressArray2 = new SchoolNameList[sc_list.size()];
                for (int i = 0; i < sc_list.size(); i++) {

                    addressArray2[i] = sc_list.get(i);
                }

                SchoolNameList[] s2 = SortArraysDataInputScore.GetArray(addressArray2);

                int order2 = 1;
                for (int i = (s2.length - 1); i >= 0; i--) {
                    scoreSchoolList.add(new SchoolNameList(order2, s2[i].sid, s2[i].sname, s2[i].marks_enter_perc, s2[i].marksEnter, s2[i].no_of_teachers, s2[i].no_of_active, s2[i].active_perc));
                    order2++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public String loadDataEnteredClasses(int sid) {

        yearList.clear();

        School s = (School) uni.find(sid, School.class);
        selectedSchoolName = s.getGeneralOrganizationProfileId().getName();

        for (SearchingCriteria sc : searchingCriteriaList) {
            List<TermList> termList = new ArrayList();
            if (sc.term1Selected) {

                List<String> clList = new ArrayList();

                List<StudentMarks> cl_list0 = uni.searchByQuery("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + sid + "' and im.termsId.id='1' and im.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + sc.year_name + "'  and im.isRemoved='0' group by im.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.id order by im.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.name,im.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.classesId.name ASC ");
                for (StudentMarks sm : cl_list0) {

                    clList.add(sm.getGradeClassStudentsId().getGradeClassStreamManagerId().getGradeClassStreamId().getGradeId().getName() + "-" + sm.getGradeClassStudentsId().getGradeClassStreamManagerId().getGradeClassStreamId().getClassesId().getName());
                }
                if (clList.size() > 0) {
                    termList.add(new TermList("Term 1", clList));
                }
            }
            if (sc.term2Selected) {

                List<String> clList = new ArrayList();

                List<StudentMarks> cl_list0 = uni.searchByQuery("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + sid + "' and im.termsId.id='2' and im.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + sc.year_name + "'  and im.isRemoved='0' group by im.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.id order by im.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.name,im.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.classesId.name ASC");
                for (StudentMarks sm : cl_list0) {

                    clList.add(sm.getGradeClassStudentsId().getGradeClassStreamManagerId().getGradeClassStreamId().getGradeId().getName() + "-" + sm.getGradeClassStudentsId().getGradeClassStreamManagerId().getGradeClassStreamId().getClassesId().getName());
                }
                if (clList.size() > 0) {
                    termList.add(new TermList("Term 2", clList));
                }
            }
            if (sc.term3Selected) {

                List<String> clList = new ArrayList();

                List<StudentMarks> cl_list0 = uni.searchByQuery("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + sid + "' and im.termsId.id='3' and im.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name='" + sc.year_name + "'  and im.isRemoved='0' group by im.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.id order by im.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.gradeId.name,im.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.classesId.name ASC");
                for (StudentMarks sm : cl_list0) {

                    clList.add(sm.getGradeClassStudentsId().getGradeClassStreamManagerId().getGradeClassStreamId().getGradeId().getName() + "-" + sm.getGradeClassStudentsId().getGradeClassStreamManagerId().getGradeClassStreamId().getClassesId().getName());
                }
                if (clList.size() > 0) {
                    termList.add(new TermList("Term 3", clList));
                }

            }
            if (termList.size() > 0) {
                getYearList().add(new YearList(sc.getYear_name(), termList));
            }
        }

        return null;
    }

    public String loadTeacherList(int sid) {

        alloList.clear();
        notAlloList.clear();

        School s = (School) uni.find(sid, School.class);
        selectedSchoolName = s.getGeneralOrganizationProfileId().getName();

        String query = "SELECT g FROM Teacher g where g.schoolId.id='" + sid + "' and g.isActive='1' order by g.generalUserProfileId.nameWithIn ASC";
        List<Teacher> listAS = uni.searchByQuery(query);

        int al = 1;
        int nal = 1;

        for (Teacher t : listAS) {
            List<LoginSession> listAS1 = uni.searchByQuery("SELECT g FROM LoginSession g where g.userLoginId.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "' and g.userLoginGroupId.projectsId.id='1'");
            if (listAS1.size() > 0) {
                alloList.add(new TeacherDetails(al, t.getGeneralUserProfileId().getNic(), t.getGeneralUserProfileId().getNameWithIn()));
                al++;
            } else {

                notAlloList.add(new TeacherDetails(nal, t.getGeneralUserProfileId().getNic(), t.getGeneralUserProfileId().getNameWithIn()));
                nal++;
            }

        }

        return null;
    }

    public class TeacherDetails {

        private int no;
        private String nic;
        private String name;

        public TeacherDetails(int no, String nic, String name) {
            this.no = no;
            this.nic = nic;
            this.name = name;

        }

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }

        public String getNic() {
            return nic;
        }

        public void setNic(String nic) {
            this.nic = nic;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class SchoolNameList implements Serializable {

        private int order;
        private int sid;
        private String sname;
        private String marks_enter_perc;
        private double marksEnter;
        private String no_of_teachers;
        private String no_of_active;
        private String active_perc;

        public SchoolNameList() {
        }

        public SchoolNameList(int order, int sid, String sname, String marks_enter_perc, double marksEnter, String no_of_teachers, String no_of_active, String active_perc) {
            this.order = order;
            this.sid = sid;
            this.sname = sname;
            this.marks_enter_perc = marks_enter_perc;
            this.marksEnter = marksEnter;
            this.no_of_teachers = no_of_teachers;
            this.no_of_active = no_of_active;
            this.active_perc = active_perc;

        }

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public String getMarks_enter_perc() {
            return marks_enter_perc;
        }

        public void setMarks_enter_perc(String marks_enter_perc) {
            this.marks_enter_perc = marks_enter_perc;
        }

        public String getNo_of_teachers() {
            return no_of_teachers;
        }

        public void setNo_of_teachers(String no_of_teachers) {
            this.no_of_teachers = no_of_teachers;
        }

        public String getNo_of_active() {
            return no_of_active;
        }

        public void setNo_of_active(String no_of_active) {
            this.no_of_active = no_of_active;
        }

        public String getActive_perc() {
            return active_perc;
        }

        public void setActive_perc(String active_perc) {
            this.active_perc = active_perc;
        }

        public double getMarksEnter() {
            return marksEnter;
        }

        public void setMarksEnter(double marksEnter) {
            this.marksEnter = marksEnter;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
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

    public int getEmailCount() {
        return emailCount;
    }

    public void setEmailCount(int emailCount) {
        this.emailCount = emailCount;
    }

    public boolean isSelectAllBox() {
        return selectAllBox;
    }

    public void setSelectAllBox(boolean selectAllBox) {
        this.selectAllBox = selectAllBox;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
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

    public List<SearchingCriteria> getSearchingCriteriaList() {
        return searchingCriteriaList;
    }

    public void setSearchingCriteriaList(List<SearchingCriteria> searchingCriteriaList) {
        this.searchingCriteriaList = searchingCriteriaList;
    }

    public List<SchoolNameList> getScoreSchoolList() {
        return scoreSchoolList;
    }

    public void setScoreSchoolList(List<SchoolNameList> scoreSchoolList) {
        this.scoreSchoolList = scoreSchoolList;
    }

    public List<YearList> getYearList() {
        return yearList;
    }

    public void setYearList(List<YearList> yearList) {
        this.yearList = yearList;
    }

    public String getSelectedSchoolName() {
        return selectedSchoolName;
    }

    public void setSelectedSchoolName(String selectedSchoolName) {
        this.selectedSchoolName = selectedSchoolName;
    }

    public List<TeacherDetails> getAlloList() {
        return alloList;
    }

    public void setAlloList(List<TeacherDetails> alloList) {
        this.alloList = alloList;
    }

    public List<TeacherDetails> getNotAlloList() {
        return notAlloList;
    }

    public void setNotAlloList(List<TeacherDetails> notAlloList) {
        this.notAlloList = notAlloList;
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

    public class SearchingCriteria implements Serializable {

        private int no;
        private String year_name;
        private boolean term1Selected;
        private boolean term2Selected;
        private boolean term3Selected;

        public SearchingCriteria() {
        }

        public SearchingCriteria(int no, String year_name, boolean term1Selected, boolean term2Selected, boolean term3Selected) {
            this.no = no;
            this.year_name = year_name;
            this.term1Selected = term1Selected;
            this.term2Selected = term2Selected;
            this.term3Selected = term3Selected;

        }

        public String getYear_name() {
            return year_name;
        }

        public void setYear_name(String year_name) {
            this.year_name = year_name;
        }

        public boolean isTerm1Selected() {
            return term1Selected;
        }

        public void setTerm1Selected(boolean term1Selected) {
            this.term1Selected = term1Selected;
        }

        public boolean isTerm2Selected() {
            return term2Selected;
        }

        public void setTerm2Selected(boolean term2Selected) {
            this.term2Selected = term2Selected;
        }

        public boolean isTerm3Selected() {
            return term3Selected;
        }

        public void setTerm3Selected(boolean term3Selected) {
            this.term3Selected = term3Selected;
        }

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }

    }

    public static class SchoolList implements Serializable {

        private int no;
        private String school_name;
        private double val;
        private String value;
        private List<YearList> yearList;

        public SchoolList() {
        }

        public SchoolList(int no, String school_name, double val, String value, List<YearList> yearList) {
            this.no = no;
            this.school_name = school_name;
            this.val = val;
            this.value = value;
            this.yearList = yearList;

        }

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }

        public String getSchool_name() {
            return school_name;
        }

        public void setSchool_name(String school_name) {
            this.school_name = school_name;
        }

        public double getVal() {
            return val;
        }

        public void setVal(double val) {
            this.val = val;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public List<YearList> getYearList() {
            return yearList;
        }

        public void setYearList(List<YearList> yearList) {
            this.yearList = yearList;
        }

    }

    public static class YearList implements Serializable {

        private String yearName;
        private List<TermList> termList;

        public YearList() {
        }

        public YearList(String yearName, List<TermList> termList) {
            this.yearName = yearName;
            this.termList = termList;

        }

        public String getYearName() {
            return yearName;
        }

        public void setYearName(String yearName) {
            this.yearName = yearName;
        }

        public List<TermList> getTermList() {
            return termList;
        }

        public void setTermList(List<TermList> termList) {
            this.termList = termList;
        }
    }

    public static class TermList implements Serializable {

        private String classNames;
        private List<String> classList;

        public TermList() {
        }

        public TermList(String classNames, List<String> classList) {
            this.classNames = classNames;
            this.classList = classList;

        }

        public String getClassNames() {
            return classNames;
        }

        public void setClassNames(String classNames) {
            this.classNames = classNames;
        }

        public List<String> getClassList() {
            return classList;
        }

        public void setClassList(List<String> classList) {
            this.classList = classList;
        }
    }

    public static class TestTask {

        private final int index;
        private String state;

        public TestTask(int index) {
            this.index = index;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getIndex() {
            return index;
        }
    }
}
