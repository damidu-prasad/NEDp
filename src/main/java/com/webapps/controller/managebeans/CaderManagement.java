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
public class CaderManagement implements Serializable {

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

    private TeacherList selectedSchool;

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

    private List<TeacherList> teacherList = new ArrayList();

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

    public void loadCaderList() {

        getTeacherList().clear();
        List<TeacherList> teaList = new ArrayList();

        List<School> school_list = new ArrayList();

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

        int k = 1;

        for (School v : school_list) {

            List<Teacher> tea = uni.searchByQuery("SELECT g FROM Teacher g where g.schoolId.id='" + v.getId() + "' and g.isActive='1' ");

            int total_count = tea.size();

            List<TeacherDetails> alloList = new ArrayList();
            List<TeacherDetails> notAlloList = new ArrayList();
            int o = 1;
            int p = 1;
            for (Teacher t : tea) {

                List<GradeClassSubjectTeacher> gcst_list = uni.searchByQuery("SELECT g FROM GradeClassSubjectTeacher g where g.teacherId.id='" + t.getId() + "' and g.teacherId.isActive='1' and g.isActive='1' and g.gradeClassStreamManagerId.yearId.name='" + year + "' group by g.teacherId ");
                if (gcst_list.size() > 0) {
                    alloList.add(new TeacherDetails(o, t.getGeneralUserProfileId().getNic(), t.getGeneralUserProfileId().getNameWithIn()));
                    o++;
                } else {

                    notAlloList.add(new TeacherDetails(p, t.getGeneralUserProfileId().getNic(), t.getGeneralUserProfileId().getNameWithIn()));
                    p++;
                }

            }

            int allo = alloList.size();

            teaList.add(new TeacherList(k, v.getGeneralOrganizationProfileId().getName(), total_count, allo, (total_count - allo), alloList, notAlloList));

            k++;
        }

        TeacherList[] addressArray2 = new TeacherList[teaList.size()];
        for (int i = 0; i < teaList.size(); i++) {

            addressArray2[i] = teaList.get(i);
        }

        TeacherList[] s2 = SortArraysCaderManagement.GetArray(addressArray2);

        int order2 = 1;
        for (int i = (s2.length - 1); i >= 0; i--) {
            teacherList.add(new TeacherList(order2, s2[i].schoolName, s2[i].totalTeachers, s2[i].allocatedTeachers, s2[i].notAllocatedTeachers, s2[i].alloList, s2[i].notAlloList));
            order2++;
        }

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

    public class TeacherList {

        private int no;
        private String schoolName;
        private int totalTeachers;
        private int allocatedTeachers;
        private int notAllocatedTeachers;
        private List<TeacherDetails> alloList;
        private List<TeacherDetails> notAlloList;

        public TeacherList(int no, String schoolName, int totalTeachers, int allocatedTeachers, int notAllocatedTeachers, List<TeacherDetails> alloList, List<TeacherDetails> notAlloList) {
            this.no = no;
            this.schoolName = schoolName;
            this.totalTeachers = totalTeachers;
            this.allocatedTeachers = allocatedTeachers;
            this.notAllocatedTeachers = notAllocatedTeachers;
            this.alloList = alloList;
            this.notAlloList = notAlloList;

        }

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public int getTotalTeachers() {
            return totalTeachers;
        }

        public void setTotalTeachers(int totalTeachers) {
            this.totalTeachers = totalTeachers;
        }

        public int getAllocatedTeachers() {
            return allocatedTeachers;
        }

        public void setAllocatedTeachers(int allocatedTeachers) {
            this.allocatedTeachers = allocatedTeachers;
        }

        public int getNotAllocatedTeachers() {
            return notAllocatedTeachers;
        }

        public void setNotAllocatedTeachers(int notAllocatedTeachers) {
            this.notAllocatedTeachers = notAllocatedTeachers;
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

    public List<TeacherList> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<TeacherList> teacherList) {
        this.teacherList = teacherList;
    }

    public TeacherList getSelectedSchool() {
        return selectedSchool;
    }

    public void setSelectedSchool(TeacherList selectedSchool) {
        this.selectedSchool = selectedSchool;
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
