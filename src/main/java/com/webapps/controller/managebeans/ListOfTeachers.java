/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.managebeans;

import com.ejb.model.businesslogic.GoogleMail;
import com.ejb.model.businesslogic.NewMailSender;
import com.ejb.model.common.ComDev;
import com.ejb.model.common.ComLib;
import com.ejb.model.common.ComPath;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.EducationDivision;
import com.ejb.model.entity.EducationZone;
import com.ejb.model.entity.Employee;
import com.ejb.model.entity.EmployeeCategory;
import com.ejb.model.entity.Grade;
import com.ejb.model.entity.GradeClassSubjectTeacher;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.Province;
import com.ejb.model.entity.School;
import com.ejb.model.entity.Subjects;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.TeacherType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Thilini Madagama
 */
@ManagedBean(name = "listOfTeachers")
@ViewScoped
public class ListOfTeachers {

    private String provinceName = "0";
    private List<SelectItem> provinceNameList = new ArrayList<SelectItem>();

    private String zoneName = "0";
    private List<SelectItem> zoneNameList = new ArrayList<SelectItem>();

    private String divisionName = "0";
    private List<SelectItem> divisionNameList = new ArrayList<SelectItem>();

    private String schoolName = "0";
    private List<SelectItem> schoolNameList = new ArrayList<SelectItem>();

    private String gradeName = "0";
    private List<SelectItem> gradeNameList = new ArrayList<SelectItem>();

    private String subjectName = "0";
    private List<SelectItem> subjectNameList = new ArrayList<SelectItem>();

    private String regTypeName = "0";
    private List<SelectItem> regTypeNameList = new ArrayList<SelectItem>();

    private List<TeacherList> teacherList = new ArrayList();

    private TeacherList selectedTeacher;

    private String searchTeacher = "";

    private boolean disabledFiledProvince = false;
    private boolean disabledFiledZone = false;
    private boolean disabledFiledDivision = false;
    private boolean disabledFiledSchool = false;

    private int emailCount;
    private boolean selectAllBox;
    private String subject = "";
    private String emailContent = "";

    private int def_province = 0;
    private int def_zone = 0;
    private int def_division = 0;
    private int def_school = 0;

    private String firstName = " ";
    private String lastName = " ";
    private String nameWithIn = " ";
    private String mobile = " ";
    private String address1 = " ";
    private String address2 = " ";
    private String address3 = " ";
    private String email = "";
    private String nic = " ";
    private Date dob = new Date();
    private String gender = "1";

    private List<SelectItem> teacherTypesList = new ArrayList<>();
    private String teacherType;
    private String teacherId;

    HttpServletResponse response;
    HttpServletRequest request;
    private ComLib comlib;
    private ComPath comPath;

    @EJB
    private UniDBLocal uni;
    LoginSession ls;

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
        teacherTypes();
    }

    public void teacherTypes() {
        String get_gib = "SELECT g FROM TeacherType g";
        List<TeacherType> teacherTypes = uni.searchByQuery(get_gib);
        getTeacherTypesList().clear();
        getTeacherTypesList().add(new SelectItem("", "Select"));
        for (TeacherType employementSectorObj : teacherTypes) {
            getTeacherTypesList().add(new SelectItem(employementSectorObj.getId().toString(), employementSectorObj.getType()));
        }
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

        String query1 = "SELECT g FROM EmployeeCategory g order by g.name ASC";
        List<EmployeeCategory> listAS1 = uni.searchByQuery(query1);
        for (EmployeeCategory cc : listAS1) {

            getRegTypeNameList().add(new SelectItem(cc.getId(), cc.getName()));
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

        return null;
    }

    public String loadListOfTeachers() {

        try {
            if (regTypeName.equals("1")) {
                List<Teacher> teacher_list = new ArrayList();
                if (provinceName.equals("0")) {

                    String query = "SELECT g FROM Teacher g where g.isActive='1' order by g.schoolId.id ASC";

                    teacher_list = uni.searchByQuery(query);

                } else if (zoneName.equals("0")) {
                    String query = "SELECT g FROM Teacher g where g.schoolId.educationDivisionId.educationZoneId.provinceId.id='" + Integer.parseInt(provinceName) + "' and g.isActive='1' order by g.schoolId.id ASC";

                    teacher_list = uni.searchByQuery(query);

                } else if (divisionName.equals("0")) {
                    String query = "SELECT g FROM Teacher g where g.schoolId.educationDivisionId.educationZoneId.id='" + Integer.parseInt(zoneName) + "' and g.isActive='1' order by g.schoolId.id ASC";

                    teacher_list = uni.searchByQuery(query);

                } else if (schoolName.equals("0")) {
                    String query = "SELECT g FROM Teacher g where g.schoolId.educationDivisionId.id='" + Integer.parseInt(divisionName) + "' and g.isActive='1' order by g.schoolId.id ASC";

                    teacher_list = uni.searchByQuery(query);

                } else if (!schoolName.equals("0")) {
                    String query = "SELECT g FROM Teacher g where g.schoolId.id='" + Integer.parseInt(schoolName) + "' and g.isActive='1' order by g.generalUserProfileId.nameWithIn ASC";

                    teacher_list = uni.searchByQuery(query);

                }
                if (!gradeName.equals("0") || !subjectName.equals("0")) {
                    if (teacher_list.size() > 0) {

                        String gradeQuery = "";
                        String subjectQuery = "";

                        if (!gradeName.equals("0")) {
                            gradeQuery = " and g.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + Integer.parseInt(gradeName) + "' ";

                        }
                        if (!subjectName.equals("0")) {
                            subjectQuery = " and g.gradeClassHasSubjectsId.subjectsId.id='" + Integer.parseInt(subjectName) + "' ";

                        }
                        String ids = "";
                        int o = 0;
                        for (Teacher v : teacher_list) {
                            if (o == 0) {
                                ids = v.getId() + "";
                                o++;
                            } else {

                                ids += "," + v.getId() + "";
                            }
                        }

                        teacher_list.clear();

                        List<GradeClassSubjectTeacher> list_gcst_individualy = uni.searchByQuery("SELECT g FROM GradeClassSubjectTeacher g where g.gradeClassStreamManagerId.yearId.name='" + comlib.GetCurrentYear() + "' and g.teacherId.id in (" + ids + ")  " + gradeQuery + subjectQuery + " group by g.teacherId.id");
                        for (GradeClassSubjectTeacher gcst_individualy : list_gcst_individualy) { // Retrive group of subject Teachers
                            teacher_list.add(gcst_individualy.getTeacherId());
                        }
                    }
                }

                teacherList = new ArrayList();

                int i = 1;

                emailCount = 0;
                for (Teacher v : teacher_list) {

                    boolean dis = true;

                    String email = "";
                    if (v.getGeneralUserProfileId().getEmail() != null) {
                        email = v.getGeneralUserProfileId().getEmail();

                        if (!email.equals("")) {
                            emailCount++;
                            dis = false;
                        }

                    }
                    String subs = "";
                    int o = 0;
                    String querys_selected1 = "SELECT g FROM GradeClassSubjectTeacher g where g.teacherId.id='" + v.getId() + "' and g.gradeClassStreamManagerId.yearId.name='" + comlib.GetCurrentYear() + "' ";
                    List<GradeClassSubjectTeacher> list_selected1 = uni.searchByQuery(querys_selected1);
                    for (GradeClassSubjectTeacher gss : list_selected1) {

                        if (o == 0) {
                            subs = "(" + gss.getGradeClassStreamManagerId().getGradeClassStreamId().getGradeId().getName() + gss.getGradeClassStreamManagerId().getGradeClassStreamId().getClassesId().getName() + ") - " + gss.getGradeClassHasSubjectsId().getSubjectsId().getName();
                            o++;
                        } else {

                            subs += ", (" + gss.getGradeClassStreamManagerId().getGradeClassStreamId().getGradeId().getName() + gss.getGradeClassStreamManagerId().getGradeClassStreamId().getClassesId().getName() + ") - " + gss.getGradeClassHasSubjectsId().getSubjectsId().getName();
                        }

                    }

                    List<Employee> t_list = uni.searchByQuerySingle("SELECT e from Employee e where   e.generalUserProfileId.id='" + v.getGeneralUserProfileId().getId() + "' and e.generalOrganizationProfileId.id='" + v.getSchoolId().getGeneralOrganizationProfileId().getId() + "' and  e.isActive='1'");
                    if (t_list.size() > 0) {
                        Employee t = t_list.iterator().next();

                        teacherList.add(new TeacherList(i, v.getSchoolId().getSchoolId(), v.getSchoolId().getGeneralOrganizationProfileId().getName(), v.getGeneralUserProfileId().getNic(), v.getGeneralUserProfileId().getNameWithIn(), email, false, dis, t.getId(), subs));

                        i++;
                    }
                }
            } else {

                List<Employee> employee_list = new ArrayList();

                if (provinceName.equals("0")) {

                    String query = "SELECT g FROM Employee g where g.isActive='1' and g.employeeCategoryId.id='2'";

                    employee_list = uni.searchByQuery(query);

                } else if (zoneName.equals("0")) {

                    Province p = (Province) uni.find(Integer.parseInt(provinceName), Province.class);

                    String query = "SELECT g FROM Employee g where g.isActive='1' and g.employeeCategoryId.id='2' and g.generalOrganizationProfileId.id='" + p.getGeneralOrganizationProfileId().getId() + "'  ";
                    List<Employee> emp_list = uni.searchByQuery(query);

                    for (Employee e : emp_list) {
                        employee_list.add(e);

                    }

                    String queryz = "SELECT g FROM EducationZone g where g.provinceId.id='" + p.getId() + "' order by g.name ASC";
                    List<EducationZone> listASz = uni.searchByQuery(queryz);
                    for (EducationZone ez : listASz) {

                        String queryze = "SELECT g FROM Employee g where g.isActive='1' and g.employeeCategoryId.id='2' and g.generalOrganizationProfileId.id='" + ez.getGeneralOrganizationProfileId().getId() + "'  ";
                        List<Employee> emp_listze = uni.searchByQuery(queryze);

                        for (Employee e : emp_listze) {
                            employee_list.add(e);

                        }

                        String queryed = "SELECT g FROM EducationDivision g where g.educationZoneId.id='" + ez.getId() + "' order by g.name ASC";
                        List<EducationDivision> listASed = uni.searchByQuery(queryed);
                        for (EducationDivision ed : listASed) {

                            String queryde = "SELECT g FROM Employee g where g.isActive='1' and g.employeeCategoryId.id='2' and g.generalOrganizationProfileId.id='" + ed.getGeneralOrganizationProfileId().getId() + "'  ";
                            List<Employee> emp_listde = uni.searchByQuery(queryde);

                            for (Employee e : emp_listde) {
                                employee_list.add(e);

                            }

                            String querys = "SELECT g FROM School g where g.educationDivisionId.id='" + ed.getId() + "' order by g.generalOrganizationProfileId.name ASC";
                            List<School> listASs = uni.searchByQuery(querys);
                            for (School s : listASs) {

                                String queryse = "SELECT g FROM Employee g where g.isActive='1' and g.employeeCategoryId.id='2' and g.generalOrganizationProfileId.id='" + s.getGeneralOrganizationProfileId().getId() + "'  ";
                                List<Employee> emp_listse = uni.searchByQuery(queryse);

                                for (Employee e : emp_listse) {
                                    employee_list.add(e);

                                }

                            }

                        }

                    }

                } else if (divisionName.equals("0")) {

                    EducationZone ez = (EducationZone) uni.find(Integer.parseInt(zoneName), EducationZone.class);

                    String query = "SELECT g FROM Employee g where g.isActive='1' and g.employeeCategoryId.id='2' and g.generalOrganizationProfileId.id='" + ez.getGeneralOrganizationProfileId().getId() + "'  ";
                    List<Employee> emp_list = uni.searchByQuery(query);

                    for (Employee e : emp_list) {
                        employee_list.add(e);

                    }

                    String queryed = "SELECT g FROM EducationDivision g where g.educationZoneId.id='" + ez.getId() + "' order by g.name ASC";
                    List<EducationDivision> listASed = uni.searchByQuery(queryed);
                    for (EducationDivision ed : listASed) {

                        String queryde = "SELECT g FROM Employee g where g.isActive='1' and g.employeeCategoryId.id='2' and g.generalOrganizationProfileId.id='" + ed.getGeneralOrganizationProfileId().getId() + "'  ";
                        List<Employee> emp_listde = uni.searchByQuery(queryde);

                        for (Employee e : emp_listde) {
                            employee_list.add(e);

                        }

                        String querys = "SELECT g FROM School g where g.educationDivisionId.id='" + ed.getId() + "' order by g.generalOrganizationProfileId.name ASC";
                        List<School> listASs = uni.searchByQuery(querys);
                        for (School s : listASs) {

                            String queryse = "SELECT g FROM Employee g where g.isActive='1' and g.employeeCategoryId.id='2' and g.generalOrganizationProfileId.id='" + s.getGeneralOrganizationProfileId().getId() + "'  ";
                            List<Employee> emp_listse = uni.searchByQuery(queryse);

                            for (Employee e : emp_listse) {
                                employee_list.add(e);

                            }

                        }

                    }

                } else if (schoolName.equals("0")) {
                    EducationDivision ed = (EducationDivision) uni.find(Integer.parseInt(divisionName), EducationDivision.class);

                    String query = "SELECT g FROM Employee g where g.isActive='1' and g.employeeCategoryId.id='2' and g.generalOrganizationProfileId.id='" + ed.getGeneralOrganizationProfileId().getId() + "'  ";
                    List<Employee> emp_list = uni.searchByQuery(query);

                    for (Employee e : emp_list) {
                        employee_list.add(e);

                    }
                    String querys = "SELECT g FROM School g where g.educationDivisionId.id='" + ed.getId() + "' order by g.generalOrganizationProfileId.name ASC";
                    List<School> listASs = uni.searchByQuery(querys);
                    for (School s : listASs) {

                        String queryse = "SELECT g FROM Employee g where g.isActive='1' and g.employeeCategoryId.id='2' and g.generalOrganizationProfileId.id='" + s.getGeneralOrganizationProfileId().getId() + "'  ";
                        List<Employee> emp_listse = uni.searchByQuery(queryse);

                        for (Employee e : emp_listse) {
                            employee_list.add(e);

                        }

                    }

                } else if (!schoolName.equals("0")) {
                    School s = (School) uni.find(Integer.parseInt(schoolName), School.class);

                    String queryse = "SELECT g FROM Employee g where g.isActive='1' and g.employeeCategoryId.id='2' and g.generalOrganizationProfileId.id='" + s.getGeneralOrganizationProfileId().getId() + "'  ";
                    List<Employee> emp_listse = uni.searchByQuery(queryse);

                    for (Employee e : emp_listse) {
                        employee_list.add(e);

                    }

                }
                teacherList = new ArrayList();
                int i = 1;
                for (Employee e : employee_list) {

                    boolean dis = true;

                    String email = "";
                    if (e.getGeneralUserProfileId().getEmail() != null) {
                        email = e.getGeneralUserProfileId().getEmail();

                        if (!email.equals("")) {
                            emailCount++;
                            dis = false;
                        }

                    }

                    teacherList.add(new TeacherList(i, "", e.getGeneralOrganizationProfileId().getName(), e.getGeneralUserProfileId().getNic(), e.getGeneralUserProfileId().getNameWithIn(), email, false, dis, e.getId(), ""));
                    i++;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String setSelectedCheckBoxes() {

        if (this.selectAllBox == true) {

            for (int i = 0; i < this.teacherList.size(); i++) {

                if (this.teacherList.get(i).disabledCheck == false) {

                    this.teacherList.get(i).setCheckMail(true);

                }
            }
        } else {
            for (int i = 0; i < this.teacherList.size(); i++) {
                if (this.teacherList.get(i).disabledCheck == false) {

                    this.teacherList.get(i).setCheckMail(false);
                }
            }
        }

        return null;
    }

    public String checkforSendMail() {

        boolean check = false;

        for (int i = 0; i < this.teacherList.size(); i++) {
            if (this.teacherList.get(i).checkMail == true) {
                check = true;
                break;
            }
        }

        if (check == false) {

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select Teachers for Send Emails !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else {
            RequestContext requestContext = RequestContext.getCurrentInstance();

            requestContext.execute("PF('carDialog').show()");

        }
        return null;
    }

    public String sendEmailToTeachers() {
        System.out.println("aa");
        FacesMessage msg = null;

        System.out.println("sub" + this.subject);

        if (this.subject.equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Enter Email Subject !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (this.emailContent.equals("")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Enter Email Content !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            try {

                for (int i = 0; i < this.teacherList.size(); i++) {
                    if (this.teacherList.get(i).checkMail == true) {
                        System.out.println("awa " + this.teacherList.get(i).getEmail());
                        String cont = "<div>Dear " + this.teacherList.get(i).name + ",</div><br/>";

                        cont += "<span>" + comlib.txtToHtml(emailContent) + "</span>";

                        cont += "<br/><br/><span>Thank You,</span><br/>";
                        cont += "<span>Provincial Education Ministry</span><br/>";

                        NewMailSender nms = new NewMailSender();
                        nms.sendM(this.teacherList.get(i).getEmail(), subject, cont);

//                        GoogleMail.Send("noreply.contex", "exoncontex1992", this.teacherList.get(i).getEmail(), subject, cont, null);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();;
            }
            this.subject = "";
            this.emailContent = "";

            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Email sent Succesfully", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        }
        return null;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherType() {
        return teacherType;
    }

    public void setTeacherType(String teacherType) {
        this.teacherType = teacherType;
    }

    public List<SelectItem> getTeacherTypesList() {
        return teacherTypesList;
    }

    public void setTeacherTypesList(List<SelectItem> teacherTypesList) {
        this.teacherTypesList = teacherTypesList;
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

    public List<TeacherList> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<TeacherList> teacherList) {
        this.teacherList = teacherList;
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

    public TeacherList getSelectedTeacher() {

        return selectedTeacher;

    }

    public void setSelectedTeacher(TeacherList selectedTeacher) {

        Employee t = (Employee) uni.find(selectedTeacher.tid, Employee.class);

        firstName = t.getGeneralUserProfileId().getFirstName();
        lastName = t.getGeneralUserProfileId().getLastName();
        nameWithIn = t.getGeneralUserProfileId().getNameWithIn();
        mobile = t.getGeneralUserProfileId().getMobilePhone();
        address1 = t.getGeneralUserProfileId().getAddress1();
        address2 = t.getGeneralUserProfileId().getAddress2();
        address3 = t.getGeneralUserProfileId().getAddress3();
        email = t.getGeneralUserProfileId().getEmail();
        nic = t.getGeneralUserProfileId().getNic();
        if (t.getGeneralUserProfileId().getDob() == null) {
            dob = new Date();
        } else {

            dob = t.getGeneralUserProfileId().getDob();
        }

        gender = t.getGeneralUserProfileId().getGenderId().getId() + "";

        this.selectedTeacher = selectedTeacher;

    }

    public String updateTeacherRegistration() {

        FacesMessage msg;

        System.out.println(teacherType);

        boolean result = comDiv.updateTeacherRegistration(selectedTeacher.tid, this.firstName, this.lastName, this.nameWithIn, this.nic, this.dob, this.gender, this.mobile, this.email, this.address1, this.address2, this.address3, this.teacherType,this.teacherId);

        if (result == true) {

            this.firstName = "";
            this.lastName = "";
            this.nameWithIn = "";
            this.nic = "";
            this.dob = null;
            this.gender = "";
            this.mobile = "";
            this.email = "";
            this.address1 = "";
            this.address2 = "";
            this.address3 = "";

            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succesfully Updated !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            loadListOfTeachers();

        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Something went wrong !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        }

        return null;
    }

    public List<String> LoadTeacherSearch(String query) {

        List<String> results = new ArrayList<String>();

        if (ls.getGeneralOrganizationProfileId().getOrganizationTypeId().getId() == 1) { // All

            List<Employee> emp_list = uni.searchByQuery("SELECT e from Employee e where ( e.generalUserProfileId.nic like '%" + query + "%' or e.generalUserProfileId.nameWithIn like '%" + query + "%') and  e.isActive='1'  order by e.generalUserProfileId.nameWithIn ASC");
            for (Employee e : emp_list) {

                results.add(e.getGeneralUserProfileId().getNic() + "-" + e.getGeneralUserProfileId().getNameWithIn() + " (" + e.getEmployeeCategoryId().getName() + " - " + e.getGeneralOrganizationProfileId().getName() + ")");
            }

        } else if (ls.getGeneralOrganizationProfileId().getOrganizationTypeId().getId() == 4) { // Province

            String queryp = "SELECT g FROM Province g where g.generalOrganizationProfileId.id='" + ls.getGeneralOrganizationProfileId().getId() + "'";
            List<Province> listASp = uni.searchByQuery(queryp);

            List<Employee> emp_list = uni.searchByQuery("SELECT e from Employee e where ( e.generalUserProfileId.nic like '%" + query + "%' or e.generalUserProfileId.nameWithIn like '%" + query + "%') and  e.isActive='1' and e.generalOrganizationProfileId.id='" + ls.getGeneralOrganizationProfileId().getId() + "'  order by e.generalUserProfileId.nameWithIn ASC");
            for (Employee e : emp_list) {

                results.add(e.getGeneralUserProfileId().getNic() + "-" + e.getGeneralUserProfileId().getNameWithIn() + " (" + e.getEmployeeCategoryId().getName() + " - " + e.getGeneralOrganizationProfileId().getName() + ")");
            }

            String queryz = "SELECT g FROM EducationZone g where g.provinceId.id='" + listASp.iterator().next().getId() + "' order by g.name ASC";
            List<EducationZone> listASz = uni.searchByQuery(queryz);
            for (EducationZone ez : listASz) {

                String queryze = "SELECT g FROM Employee g where g.isActive='1' and ( g.generalUserProfileId.nic like '%" + query + "%' or g.generalUserProfileId.nameWithIn like '%" + query + "%')  and g.generalOrganizationProfileId.id='" + ez.getGeneralOrganizationProfileId().getId() + "'  ";
                List<Employee> emp_listze = uni.searchByQuery(queryze);

                for (Employee e : emp_listze) {
                    results.add(e.getGeneralUserProfileId().getNic() + "-" + e.getGeneralUserProfileId().getNameWithIn() + " (" + e.getEmployeeCategoryId().getName() + " - " + e.getGeneralOrganizationProfileId().getName() + ")");

                }

                String queryed = "SELECT g FROM EducationDivision g where g.educationZoneId.id='" + ez.getId() + "' order by g.name ASC";
                List<EducationDivision> listASed = uni.searchByQuery(queryed);
                for (EducationDivision ed : listASed) {

                    String queryde = "SELECT g FROM Employee g where g.isActive='1' and (g.generalUserProfileId.nic like '%" + query + "%' or g.generalUserProfileId.nameWithIn like '%" + query + "%') and g.generalOrganizationProfileId.id='" + ed.getGeneralOrganizationProfileId().getId() + "'  ";
                    List<Employee> emp_listde = uni.searchByQuery(queryde);

                    for (Employee e : emp_listde) {
                        results.add(e.getGeneralUserProfileId().getNic() + "-" + e.getGeneralUserProfileId().getNameWithIn() + " (" + e.getEmployeeCategoryId().getName() + " - " + e.getGeneralOrganizationProfileId().getName() + ")");

                    }

                    String querys = "SELECT g FROM School g where g.educationDivisionId.id='" + ed.getId() + "' order by g.generalOrganizationProfileId.name ASC";
                    List<School> listASs = uni.searchByQuery(querys);
                    for (School s : listASs) {

                        String queryse = "SELECT g FROM Employee g where g.isActive='1' and ( g.generalUserProfileId.nic like '%" + query + "%' or g.generalUserProfileId.nameWithIn like '%" + query + "%')  and g.generalOrganizationProfileId.id='" + s.getGeneralOrganizationProfileId().getId() + "'  ";
                        List<Employee> emp_listse = uni.searchByQuery(queryse);

                        for (Employee e : emp_listse) {
                            results.add(e.getGeneralUserProfileId().getNic() + "-" + e.getGeneralUserProfileId().getNameWithIn() + " (" + e.getEmployeeCategoryId().getName() + " - " + e.getGeneralOrganizationProfileId().getName() + ")");

                        }

                    }

                }

            }

        } else if (ls.getGeneralOrganizationProfileId().getOrganizationTypeId().getId() == 5) { // Zone

            String queryp = "SELECT g FROM EducationZone g where g.generalOrganizationProfileId.id='" + ls.getGeneralOrganizationProfileId().getId() + "'";
            List<EducationZone> listASp = uni.searchByQuery(queryp);

            List<Employee> emp_list = uni.searchByQuery("SELECT e from Employee e where ( e.generalUserProfileId.nic like '%" + query + "%' or e.generalUserProfileId.nameWithIn like '%" + query + "%') and  e.isActive='1' and e.generalOrganizationProfileId.id='" + ls.getGeneralOrganizationProfileId().getId() + "'  order by e.generalUserProfileId.nameWithIn ASC");
            for (Employee e : emp_list) {

                results.add(e.getGeneralUserProfileId().getNic() + "-" + e.getGeneralUserProfileId().getNameWithIn() + " (" + e.getEmployeeCategoryId().getName() + " - " + e.getGeneralOrganizationProfileId().getName() + ")");
            }

            String queryed = "SELECT g FROM EducationDivision g where g.educationZoneId.id='" + listASp.iterator().next().getId() + "' order by g.name ASC";
            List<EducationDivision> listASed = uni.searchByQuery(queryed);
            for (EducationDivision ed : listASed) {

                String queryde = "SELECT g FROM Employee g where g.isActive='1' and ( g.generalUserProfileId.nic like '%" + query + "%' or g.generalUserProfileId.nameWithIn like '%" + query + "%') and g.generalOrganizationProfileId.id='" + ed.getGeneralOrganizationProfileId().getId() + "'  ";
                List<Employee> emp_listde = uni.searchByQuery(queryde);

                for (Employee e : emp_listde) {
                    results.add(e.getGeneralUserProfileId().getNic() + "-" + e.getGeneralUserProfileId().getNameWithIn() + " (" + e.getEmployeeCategoryId().getName() + " - " + e.getGeneralOrganizationProfileId().getName() + ")");

                }

                String querys = "SELECT g FROM School g where g.educationDivisionId.id='" + ed.getId() + "' order by g.generalOrganizationProfileId.name ASC";
                List<School> listASs = uni.searchByQuery(querys);
                for (School s : listASs) {

                    String queryse = "SELECT g FROM Employee g where g.isActive='1' and ( g.generalUserProfileId.nic like '%" + query + "%' or g.generalUserProfileId.nameWithIn like '%" + query + "%')  and g.generalOrganizationProfileId.id='" + s.getGeneralOrganizationProfileId().getId() + "'  ";
                    List<Employee> emp_listse = uni.searchByQuery(queryse);

                    for (Employee e : emp_listse) {
                        results.add(e.getGeneralUserProfileId().getNic() + "-" + e.getGeneralUserProfileId().getNameWithIn() + " (" + e.getEmployeeCategoryId().getName() + " - " + e.getGeneralOrganizationProfileId().getName() + ")");

                    }

                }

            }

        } else if (ls.getGeneralOrganizationProfileId().getOrganizationTypeId().getId() == 6) { // Division

            String queryp = "SELECT g FROM EducationDivision g where g.generalOrganizationProfileId.id='" + ls.getGeneralOrganizationProfileId().getId() + "'";
            List<EducationDivision> listASp = uni.searchByQuery(queryp);

            List<Employee> emp_list = uni.searchByQuery("SELECT e from Employee e where ( e.generalUserProfileId.nic like '%" + query + "%' or e.generalUserProfileId.nameWithIn like '%" + query + "%') and  e.isActive='1' and e.generalOrganizationProfileId.id='" + ls.getGeneralOrganizationProfileId().getId() + "'  order by e.generalUserProfileId.nameWithIn ASC");
            for (Employee e : emp_list) {

                results.add(e.getGeneralUserProfileId().getNic() + "-" + e.getGeneralUserProfileId().getNameWithIn() + " (" + e.getEmployeeCategoryId().getName() + " - " + e.getGeneralOrganizationProfileId().getName() + ")");
            }

            String querys = "SELECT g FROM School g where g.educationDivisionId.id='" + listASp.iterator().next().getId() + "' order by g.generalOrganizationProfileId.name ASC";
            List<School> listASs = uni.searchByQuery(querys);
            for (School s : listASs) {

                String queryse = "SELECT g FROM Employee g where g.isActive='1' and ( g.generalUserProfileId.nic like '%" + query + "%' or g.generalUserProfileId.nameWithIn like '%" + query + "%')  and g.generalOrganizationProfileId.id='" + s.getGeneralOrganizationProfileId().getId() + "'  ";
                List<Employee> emp_listse = uni.searchByQuery(queryse);

                for (Employee e : emp_listse) {
                    results.add(e.getGeneralUserProfileId().getNic() + "-" + e.getGeneralUserProfileId().getNameWithIn() + " (" + e.getEmployeeCategoryId().getName() + " - " + e.getGeneralOrganizationProfileId().getName() + ")");

                }

            }

        } else if (ls.getGeneralOrganizationProfileId().getOrganizationTypeId().getId() == 2) { // School

            String queryse = "SELECT g FROM Employee g where g.isActive='1' and ( g.generalUserProfileId.nic like '%" + query + "%' or g.generalUserProfileId.nameWithIn like '%" + query + "%')  and g.generalOrganizationProfileId.id='" + ls.getGeneralOrganizationProfileId().getId() + "'  ";
            List<Employee> emp_listse = uni.searchByQuery(queryse);

            for (Employee e : emp_listse) {
                results.add(e.getGeneralUserProfileId().getNic() + "-" + e.getGeneralUserProfileId().getNameWithIn() + " (" + e.getEmployeeCategoryId().getName() + " - " + e.getGeneralOrganizationProfileId().getName() + ")");

            }

        }

        return results;
    }

    public void setSearchStudentToTable(SelectEvent e) {
        int min = 0;
        int max = searchTeacher.indexOf("-");
        String nic = searchTeacher.substring(min, max);;

        if (!nic.equals("")) {

            teacherList.clear();
            List<Employee> emp_list = uni.searchByQuerySingle("SELECT e from Employee e where   e.generalUserProfileId.nic='" + nic + "' and  e.isActive='1'");
            if (emp_list.size() > 0) {

                Employee emp = emp_list.iterator().next();

                boolean dis = true;

                String email = "";
                if (emp.getGeneralUserProfileId().getEmail() != null) {
                    email = emp.getGeneralUserProfileId().getEmail();

                    if (!email.equals("")) {
                        emailCount++;
                        dis = false;
                    }

                }
                Teacher t = null;
                List<Teacher> t_list = uni.searchByQuerySingle("SELECT e from Teacher e where   e.generalUserProfileId.id='" + emp.getGeneralUserProfileId().getId() + "' and e.schoolId.generalOrganizationProfileId.id='" + emp.getGeneralOrganizationProfileId().getId() + "' and  e.isActive='1'");
                if (t_list.size() > 0) {
                    t = t_list.iterator().next();
                }

                String subs = "";
                if (t != null) {
                    int o = 0;
                    String querys_selected1 = "SELECT g FROM GradeClassSubjectTeacher g where g.teacherId.id='" + t.getId() + "' and g.gradeClassStreamManagerId.yearId.name='" + comlib.GetCurrentYear() + "' ";
                    List<GradeClassSubjectTeacher> list_selected1 = uni.searchByQuery(querys_selected1);
                    for (GradeClassSubjectTeacher gss : list_selected1) {

                        if (o == 0) {
                            subs = "(" + gss.getGradeClassStreamManagerId().getGradeClassStreamId().getGradeId().getName() + gss.getGradeClassStreamManagerId().getGradeClassStreamId().getClassesId().getName() + ") - " + gss.getGradeClassHasSubjectsId().getSubjectsId().getName();
                            o++;
                        } else {

                            subs += ", (" + gss.getGradeClassStreamManagerId().getGradeClassStreamId().getGradeId().getName() + gss.getGradeClassStreamManagerId().getGradeClassStreamId().getClassesId().getName() + ") - " + gss.getGradeClassHasSubjectsId().getSubjectsId().getName();
                        }

                    }
                }
                teacherList.add(new TeacherList(1, "", emp.getGeneralOrganizationProfileId().getName(), emp.getGeneralUserProfileId().getNic(), emp.getGeneralUserProfileId().getNameWithIn(), email, false, dis, emp.getId(), subs));

                searchTeacher = "";
            }

        }

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNameWithIn() {
        return nameWithIn;
    }

    public void setNameWithIn(String nameWithIn) {
        this.nameWithIn = nameWithIn;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getSearchTeacher() {
        return searchTeacher;
    }

    public void setSearchTeacher(String searchTeacher) {
        this.searchTeacher = searchTeacher;
    }

    public String getRegTypeName() {
        return regTypeName;
    }

    public void setRegTypeName(String regTypeName) {
        this.regTypeName = regTypeName;
    }

    public List<SelectItem> getRegTypeNameList() {
        return regTypeNameList;
    }

    public void setRegTypeNameList(List<SelectItem> regTypeNameList) {
        this.regTypeNameList = regTypeNameList;
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

    public class TeacherList implements Serializable {

        private int no;
        private String school_no;
        private String school_name;
        private String nic;
        private String name;
        private String email;
        private boolean checkMail;
        private boolean disabledCheck;
        private String subject;
        private int tid;

        public TeacherList() {
        }

        public TeacherList(int no, String school_no, String school_name, String nic, String name, String email, boolean checkMail, boolean disabledCheck, int tid, String subject) {
            this.no = no;
            this.school_no = school_no;
            this.school_name = school_name;
            this.nic = nic;
            this.name = name;
            this.email = email;
            this.checkMail = checkMail;
            this.disabledCheck = disabledCheck;
            this.tid = tid;
            this.subject = subject;

        }

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }

        public String getSchool_no() {
            return school_no;
        }

        public void setSchool_no(String school_no) {
            this.school_no = school_no;
        }

        public String getSchool_name() {
            return school_name;
        }

        public void setSchool_name(String school_name) {
            this.school_name = school_name;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public boolean isCheckMail() {
            return checkMail;
        }

        public void setCheckMail(boolean checkMail) {
            this.checkMail = checkMail;
        }

        public boolean isDisabledCheck() {
            return disabledCheck;
        }

        public void setDisabledCheck(boolean disabledCheck) {
            this.disabledCheck = disabledCheck;
        }

        public int getTid() {
            return tid;
        }

        public void setTid(int tid) {
            this.tid = tid;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

    }

}
