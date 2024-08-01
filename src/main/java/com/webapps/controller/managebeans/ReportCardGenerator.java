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
import com.ejb.model.entity.GradeClassStream;
import com.ejb.model.entity.GradeClassStreamManager;
import com.ejb.model.entity.GradeClassStudents;
import com.ejb.model.entity.GradeClassStudentsHasSubjects;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.Province;
import com.ejb.model.entity.School;
import com.ejb.model.entity.StudentMarks;
import com.ejb.model.entity.Students;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.Terms;
import com.ejb.model.entity.Year;
import com.webapps.controller.utilities.SortArraysReportCardGenerator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
@ManagedBean(name = "reportCardGenerator")
@ViewScoped

public class ReportCardGenerator implements Serializable {

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

    private String gradeName = "0";
    private List<SelectItem> gradeNameList = new ArrayList<SelectItem>();

    private String className = "0";
    private List<SelectItem> classNameList = new ArrayList<SelectItem>();

    private String termName = "0";
    private List<SelectItem> termNameList = new ArrayList<SelectItem>();

    private boolean disabledFiledProvince = false;
    private boolean disabledFiledZone = false;
    private boolean disabledFiledDivision = false;
    private boolean disabledFiledSchool = false;

    private int def_province = 0;
    private int def_zone = 0;
    private int def_division = 0;
    private int def_school = 0;

    private boolean selectAllBox;

    private List<StudentList> student_order_list = new ArrayList();

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

                setDisabledFiledProvince(true);

            } else if (lss.getUserLoginGroupId().getGeneralOrganizationProfileId().getOrganizationTypeId().getId() == 5) { //Zone

                List<EducationZone> p = uni.searchByQuerySingle("SELECT im FROM EducationZone im WHERE im.generalOrganizationProfileId.id='" + lss.getUserLoginGroupId().getGeneralOrganizationProfileId().getId() + "'  ");

                setDef_zone(p.iterator().next().getId());
                setDef_province(p.iterator().next().getProvinceId().getId());

                setDisabledFiledProvince(true);
                setDisabledFiledZone(true);

            } else if (lss.getUserLoginGroupId().getGeneralOrganizationProfileId().getOrganizationTypeId().getId() == 6) { //Division

                List<EducationDivision> p = uni.searchByQuerySingle("SELECT im FROM EducationDivision im WHERE im.generalOrganizationProfileId.id='" + lss.getUserLoginGroupId().getGeneralOrganizationProfileId().getId() + "'  ");

                setDef_division(p.iterator().next().getId());
                setDef_zone(p.iterator().next().getEducationZoneId().getId());
                setDef_province(p.iterator().next().getEducationZoneId().getProvinceId().getId());

                setDisabledFiledProvince(true);
                setDisabledFiledZone(true);
                setDisabledFiledDivision(true);

            } else if (lss.getUserLoginGroupId().getGeneralOrganizationProfileId().getOrganizationTypeId().getId() == 2) { //School

                List<School> p = uni.searchByQuerySingle("SELECT im FROM School im WHERE im.generalOrganizationProfileId.id='" + lss.getUserLoginGroupId().getGeneralOrganizationProfileId().getId() + "'  ");

                setDef_division(p.iterator().next().getEducationDivisionId().getId());
                setDef_zone(p.iterator().next().getEducationDivisionId().getEducationZoneId().getId());
                setDef_province(p.iterator().next().getEducationDivisionId().getEducationZoneId().getProvinceId().getId());
                setDef_school(p.iterator().next().getId());

                setDisabledFiledProvince(true);
                setDisabledFiledZone(true);
                setDisabledFiledDivision(true);
                setDisabledFiledSchool(true);

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
        setProvinceName(getDef_province() + "");
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

        getGrades();
        return null;
    }

    public String getGrades() {

        getGradeNameList().clear();
        getGradeNameList().add(new SelectItem("0", "Select"));
        if (!schoolName.equals("")) {

            String query_al = "SELECT g FROM GradeClassStream g where g.schoolId.id='" + Integer.parseInt(getSchoolName()) + "' and g.isActive='1' group by g.gradeId  order by g.gradeId.id ASC ";
            List<GradeClassStream> listAS_al = uni.searchByQuery(query_al);
            for (GradeClassStream cc : listAS_al) {

                String name = cc.getGradeId().getName();

                getGradeNameList().add(new SelectItem(cc.getGradeId().getId(), name));

            }
        }
        getClasses();
        return null;
    }

    public String getClasses() {

        getClassNameList().clear();
        getClassNameList().add(new SelectItem("0", "All"));
        if (!gradeName.equals("")) {
            System.out.println("gn " + gradeName);
            String query_al = "SELECT g FROM GradeClassStream g where g.schoolId.id='" + Integer.parseInt(getSchoolName()) + "' and g.gradeId.id='" + Integer.parseInt(gradeName) + "' and g.isActive='1' order by g.gradeId.id ASC ";
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

    public void orderStudents() {

        selectAllBox = false;

        student_order_list.clear();

        FacesMessage msg = null;
        if (schoolName.equals("0") || schoolName.equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select School !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else if (className.equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select Class !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else {

            List<GradeClassStudents> stu_list = new ArrayList();

            if (!className.equals("0")) {

                String queryt = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.gradeClassStreamId.id='" + className + "' and g.gradeClassStreamManagerId.yearId.id='" + year + "' and g.isRemoved='0'";
                stu_list = uni.searchByQuery(queryt);

            } else {
                String queryt = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + gradeName + "' and g.gradeClassStreamManagerId.yearId.id='" + year + "' and g.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + schoolName + "' and g.isRemoved='0' ";
                stu_list = uni.searchByQuery(queryt);
            }

            List<StudentList> student_list = new ArrayList();
            for (GradeClassStudents gcs : stu_list) {

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
                student_list.add(new StudentList(gcs.getId(), gcs.getStudentsId().getGeneralUserProfileId().getNameWithIn(), 1, 1, 1, 1, avg, comlib.getDouble(avg), false));
            }

            HashMap<Integer, Integer> hmap1 = new HashMap<Integer, Integer>();
            HashMap<Integer, Integer> hmap2 = new HashMap<Integer, Integer>();

            if (termName.equals("2")) {

                // term 1
                List<StudentList> st_1 = new ArrayList();

                for (GradeClassStudents gcs : stu_list) {

                    double tot_val = 0;
                    double avg = 0;

                    String query = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.gradeClassHasSubjectsId.subjectsId.isActive='1'";
                    List<GradeClassStudentsHasSubjects> list = uni.searchByQuery(query);
                    if (list.size() > 0) {

                        for (GradeClassStudentsHasSubjects gcshs : list) {

                            String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.termsId.id='1' and g.gradeClassHasSubjectsId.id='" + gcshs.getGradeClassHasSubjectsId().getId() + "' and g.isRemoved='0' ";
                            List<StudentMarks> listm = uni.searchByQuerySingle(querym);
                            if (listm.size() > 0) {
                                StudentMarks sm = listm.iterator().next();
                                tot_val += sm.getMarks();
                            }

                        }
                        avg = tot_val / list.size();

                    }

                    st_1.add(new StudentList(gcs.getId(), gcs.getStudentsId().getGeneralUserProfileId().getNameWithIn(), 1, 1, 1, 1, avg, comlib.getDouble(avg), false));

                }

                StudentList[] addressArray1 = new StudentList[st_1.size()];
                for (int i = 0; i < st_1.size(); i++) {

                    addressArray1[i] = st_1.get(i);
                }

                StudentList[] s1 = SortArraysReportCardGenerator.GetArray(addressArray1);

                int order1 = 0;
                String last_avg1 = "";
                int stuc = 1;
                for (int i = (s1.length - 1); i >= 0; i--) {

                    if (!s1[i].getAvgVal().equals(last_avg1)) {
                        order1 = stuc;
                        last_avg1 = s1[i].getAvgVal();
                        System.out.println("awapc");
                    }

                    hmap1.put(s1[i].getSid(), order1);

                    stuc++;
                }

            }
            if (termName.equals("3")) {

                // term 1
                List<StudentList> st_1 = new ArrayList();

                for (GradeClassStudents gcs : stu_list) {

                    double tot_val = 0;
                    double avg = 0;

                    String query = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.gradeClassHasSubjectsId.subjectsId.isActive='1' ";
                    List<GradeClassStudentsHasSubjects> list = uni.searchByQuery(query);
                    if (list.size() > 0) {

                        for (GradeClassStudentsHasSubjects gcshs : list) {

                            String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.termsId.id='1' and g.gradeClassHasSubjectsId.id='" + gcshs.getGradeClassHasSubjectsId().getId() + "' and g.isRemoved='0' ";
                            List<StudentMarks> listm = uni.searchByQuerySingle(querym);
                            if (listm.size() > 0) {
                                StudentMarks sm = listm.iterator().next();
                                tot_val += sm.getMarks();
                            }

                        }
                        avg = tot_val / list.size();

                    }

                    st_1.add(new StudentList(gcs.getId(), gcs.getStudentsId().getGeneralUserProfileId().getNameWithIn(), 1, 1, 1, 1, avg, comlib.getDouble(avg), false));

                }

                StudentList[] addressArray1 = new StudentList[st_1.size()];
                for (int i = 0; i < st_1.size(); i++) {

                    addressArray1[i] = st_1.get(i);
                }

                StudentList[] s1 = SortArraysReportCardGenerator.GetArray(addressArray1);

                int order1 = 0;
                String last_avg1 = "";
                int stuc = 1;
                for (int i = (s1.length - 1); i >= 0; i--) {

                    if (!s1[i].getAvgVal().equals(last_avg1)) {
                        order1 = stuc;
                        last_avg1 = s1[i].getAvgVal();

                    }
                    hmap1.put(s1[i].getSid(), order1);
                    stuc++;
                }

                // term 2
                List<StudentList> st_2 = new ArrayList();

                for (GradeClassStudents gcs : stu_list) {

                    double tot_val = 0;
                    double avg = 0;

                    String query = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.gradeClassHasSubjectsId.subjectsId.isActive='1' ";
                    List<GradeClassStudentsHasSubjects> list = uni.searchByQuery(query);
                    if (list.size() > 0) {

                        for (GradeClassStudentsHasSubjects gcshs : list) {

                            String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.termsId.id='2' and g.gradeClassHasSubjectsId.id='" + gcshs.getGradeClassHasSubjectsId().getId() + "' and g.isRemoved='0' ";
                            List<StudentMarks> listm = uni.searchByQuerySingle(querym);
                            if (listm.size() > 0) {
                                StudentMarks sm = listm.iterator().next();
                                tot_val += sm.getMarks();
                            }

                        }
                        avg = tot_val / list.size();

                    }

                    st_2.add(new StudentList(gcs.getId(), gcs.getStudentsId().getGeneralUserProfileId().getNameWithIn(), 1, 1, 1, 1, avg, comlib.getDouble(avg), false));

                }

                StudentList[] addressArray2 = new StudentList[st_2.size()];
                for (int i = 0; i < st_2.size(); i++) {

                    addressArray2[i] = st_2.get(i);
                }

                StudentList[] s2 = SortArraysReportCardGenerator.GetArray(addressArray2);

                int order2 = 0;
                String last_avg2 = "";
                int stuc2 = 1;
                for (int i = (s2.length - 1); i >= 0; i--) {
                    if (!s2[i].getAvgVal().equals(last_avg2)) {
                        order2 = stuc2;
                        last_avg2 = s2[i].getAvgVal();

                    }

                    hmap2.put(s2[i].getSid(), order2);
                    stuc2++;

                }

            }

            StudentList[] addressArray = new StudentList[student_list.size()];
            for (int i = 0; i < student_list.size(); i++) {

                addressArray[i] = student_list.get(i);
            }

            StudentList[] s = SortArraysReportCardGenerator.GetArray(addressArray);

            int order = 0;
            String last_avg = "";
            int stuc = 1;
            for (int i = (s.length - 1); i >= 0; i--) {

                if (!s[i].getAvgVal().equals(last_avg)) {

                    order = stuc;
                    last_avg = s[i].getAvgVal();
                }

                int order_1 = 0;
                int order_2 = 0;
                int order_3 = 0;

                if (termName.equals("1")) {

                    order_1 = order;
                } else if (termName.equals("2")) {

                    order_1 = hmap1.get(s[i].getSid());
                    order_2 = order;
                } else if (termName.equals("3")) {

                    order_1 = hmap1.get(s[i].getSid());
                    order_2 = hmap2.get(s[i].getSid());
                    order_3 = order;
                }

                student_order_list.add(new StudentList(s[i].getSid(), s[i].getName(), order, order_1, order_2, order_3, s[i].getAvg(), s[i].getAvgVal(), false));
                stuc++;
            }

        }
    }

    public String setSelectedCheckBoxes() {

        if (this.selectAllBox == true) {

            for (int i = 0; i < this.student_order_list.size(); i++) {

                this.student_order_list.get(i).setSelected(true);

            }
        } else {
            for (int i = 0; i < this.student_order_list.size(); i++) {

                this.student_order_list.get(i).setSelected(false);
            }
        }

        return null;
    }

    public class StudentList {

        private int sid;
        private String name;
        private int order;
        private int order1;
        private int order2;
        private int order3;
        private double avg;
        private String avgVal;
        private boolean selected;

        public StudentList(int sid, String name, int order, int order1, int order2, int order3, double avg, String avgVal, boolean selected) {
            this.sid = sid;
            this.name = name;
            this.order = order;
            this.order1 = order1;
            this.order2 = order2;
            this.order3 = order3;
            this.avg = avg;
            this.avgVal = avgVal;
            this.selected = selected;
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

        public double getAvg() {
            return avg;
        }

        public void setAvg(double avg) {
            this.avg = avg;
        }

        public String getAvgVal() {
            return avgVal;
        }

        public void setAvgVal(String avgVal) {
            this.avgVal = avgVal;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public int getOrder1() {
            return order1;
        }

        public void setOrder1(int order1) {
            this.order1 = order1;
        }

        public int getOrder2() {
            return order2;
        }

        public void setOrder2(int order2) {
            this.order2 = order2;
        }

        public int getOrder3() {
            return order3;
        }

        public void setOrder3(int order3) {
            this.order3 = order3;
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

    public List<StudentList> getStudent_order_list() {
        return student_order_list;
    }

    public void setStudent_order_list(List<StudentList> student_order_list) {
        this.student_order_list = student_order_list;
    }

    public boolean isSelectAllBox() {
        return selectAllBox;
    }

    public void setSelectAllBox(boolean selectAllBox) {
        this.selectAllBox = selectAllBox;
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
