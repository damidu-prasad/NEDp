/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.managebeans;

import com.ejb.model.businesslogic.GoogleMail;
import com.ejb.model.common.ComLib;
import com.ejb.model.common.ComPath;
import com.ejb.model.common.Security;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.DataChangedLogManager;
import com.ejb.model.entity.EducationDivision;
import com.ejb.model.entity.EducationZone;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.Projects;
import com.ejb.model.entity.Province;
import com.ejb.model.entity.School;
import com.ejb.model.entity.SystemInterface;
import com.ejb.model.entity.TableManager;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.UserLogin;
import com.ejb.model.entity.UserLoginGroup;
import com.ejb.model.entity.UserRole;
import com.webapps.controller.utilities.PasswordGenerator;
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

/**
 *
 * @author Thilini Madagama
 */
@ManagedBean(name = "createUserAccounts")
@ViewScoped
public class CreateUserAccounts {

    private String provinceName = "0";
    private List<SelectItem> provinceNameList = new ArrayList<SelectItem>();

    private String zoneName = "0";
    private List<SelectItem> zoneNameList = new ArrayList<SelectItem>();

    private String divisionName = "0";
    private List<SelectItem> divisionNameList = new ArrayList<SelectItem>();

    private String schoolName = "0";
    private List<SelectItem> schoolNameList = new ArrayList<SelectItem>();

    private List<TeacherList> teacherList = new ArrayList();

    private String userRoleName;
    private List<SelectItem> userRoleNameList = new ArrayList<SelectItem>();

    private boolean disabledFiledProvince = false;
    private boolean disabledFiledZone = false;
    private boolean disabledFiledDivision = false;
    private boolean disabledFiledSchool = false;

    private int emailCount;
    private boolean selectAllBox;
    private boolean selectAllNotExistBox;
    private String subject = "User Account Details of NEDp ";
    private String emailContent = "";

    private int def_province = 0;
    private int def_zone = 0;
    private int def_division = 0;
    private int def_school = 0;

    HttpServletResponse response;
    HttpServletRequest request;
    private ComLib comlib;
    private ComPath comPath;

    @EJB
    private UniDBLocal uni;
    LoginSession ls;

    PasswordGenerator usernameGenerator;
    PasswordGenerator passwordGenerator;

    @PostConstruct
    public void init() {

        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        ls = (LoginSession) uni.find(Integer.parseInt(request.getSession().getAttribute("LS").toString()), LoginSession.class);

        usernameGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(false)
                .useLower(false)
                .useUpper(true)
                .build();

        passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .build();
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

        String queryp = "SELECT g FROM Province g order by g.name ASC";
        List<Province> listASp = uni.searchByQuery(queryp);
        for (Province cc : listASp) {

            getProvinceNameList().add(new SelectItem(cc.getId(), cc.getName()));
        }
        setProvinceName(def_province + "");
        getEducationZoneList();

        getUserRoleNameList().add(new SelectItem("0", "Select"));

        String query = "SELECT g FROM UserRole g where g.id in (" + comlib.SCHOOL_USER_ROLE_LIST + ") order by g.roleName ASC";
        List<UserRole> listAS = uni.searchByQuery(query);
        for (UserRole cc : listAS) {

            getUserRoleNameList().add(new SelectItem(cc.getId(), cc.getRoleName()));
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

        List<Teacher> teacher_list = new ArrayList();

        try {

            if (!schoolName.equals("0")) {
                String query = "SELECT g FROM Teacher g where g.schoolId.id='" + Integer.parseInt(schoolName) + "' order by g.generalUserProfileId.nameWithIn ASC";

                teacher_list = uni.searchByQuery(query);

            }

            teacherList = new ArrayList();

            int i = 1;

            emailCount = 0;
            for (Teacher v : teacher_list) {

                String email = "";
                if (v.getGeneralUserProfileId().getEmail() != null) {
                    email = v.getGeneralUserProfileId().getEmail();

                }
                int account_status = 2;
                String account_name = "Not Exists";
                String accountStatusName = "";

                List<UserLoginGroup> gop_list = uni.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.generalUserProfileId.id='" + v.getId() + "' and im.generalOrganizationProfileId.id='" + v.getSchoolId().getGeneralOrganizationProfileId().getId() + "' and im.projectsId.id='1'");
                if (gop_list.size() > 0) {
                    account_name = gop_list.iterator().next().getUserRoleId().getRoleName();

                    if (gop_list.iterator().next().getIsActive() == 0) {

                        account_status = 0;
                        accountStatusName = " : Inactive";
                    } else {
                        account_status = 1;
                        accountStatusName = " : Active";
                    }
                }

                teacherList.add(new TeacherList(i, v.getSchoolId().getSchoolId(), v.getSchoolId().getGeneralOrganizationProfileId().getName(), v.getGeneralUserProfileId().getNic(), v.getGeneralUserProfileId().getNameWithIn(), email, false, account_status, account_name, v.getId(), accountStatusName));

                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String setSelectedCheckBoxes() {

        if (this.selectAllBox == true) {

            for (int i = 0; i < this.teacherList.size(); i++) {

                this.teacherList.get(i).setCheckMail(true);

            }
        } else {
            for (int i = 0; i < this.teacherList.size(); i++) {

                this.teacherList.get(i).setCheckMail(false);
            }
        }

        return null;
    }

    public String setSelectedNotExistsCheckBoxes() {

        if (this.selectAllNotExistBox == true) {

            for (int i = 0; i < this.teacherList.size(); i++) {
                if (this.teacherList.get(i).getAccountStatus() == 2) {

                    this.teacherList.get(i).setCheckMail(true);
                }
            }
        } else {
            for (int i = 0; i < this.teacherList.size(); i++) {
                if (this.teacherList.get(i).getAccountStatus() == 2) {
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

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select Teachers for Create Accounts !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else {
            sendEmailToTeachers();

        }
        return null;
    }

    public String sendEmailToTeachers() {

        FacesMessage msg = null;
        System.out.println("userRoleName" + userRoleName);
        if (userRoleName.equals("0")) {

            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Select User Role !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            try {

                for (int i = 0; i < this.teacherList.size(); i++) {
                    if (this.teacherList.get(i).checkMail == true) {
                        System.out.println("awa " + this.teacherList.get(i).getEmail());

                        Teacher t = (Teacher) uni.find(this.teacherList.get(i).getTid(), Teacher.class);
                        String username = "";
                        String password = "";
                        List<UserLoginGroup> gop_list = uni.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "' and im.generalOrganizationProfileId.id='" + t.getSchoolId().getGeneralOrganizationProfileId().getId() + "' and im.projectsId.id='1'");
                        if (gop_list.size() > 0) {
                            UserLoginGroup ulg = gop_list.iterator().next();
                            ulg.setIsActive(1);
                            ulg.setUserRoleId((UserRole) uni.find(Integer.parseInt(userRoleName), UserRole.class));
                            ulg.setGeneralOrganizationProfileId(t.getSchoolId().getGeneralOrganizationProfileId());
                            uni.update(ulg);
                            username = ulg.getUserLoginId().getUsername();
                            password = Security.decrypt(ulg.getUserLoginId().getPassword());
                        } else {

                            UserLogin ul = null;

                            List<UserLogin> gop1_list = uni.searchByQuerySingle("SELECT im FROM UserLogin im WHERE im.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "' ");
                            if (gop1_list.size() > 0) {
                                ul = gop1_list.iterator().next();
                                ul.setIsActive(1);
                                ul.setUserRoleId((UserRole) uni.find(Integer.parseInt(userRoleName), UserRole.class));
                                ul.setGeneralOrganizationProfileId(t.getSchoolId().getGeneralOrganizationProfileId());
                                uni.update(ul);

                                username = ul.getUsername();
                                password = Security.decrypt(ul.getPassword());

                            } else {

                                if (!t.getGeneralUserProfileId().getNic().equals("")) {

                                    username = t.getGeneralUserProfileId().getNic();

                                    int min = t.getGeneralUserProfileId().getNic().length() - 4;

                                    password = t.getGeneralUserProfileId().getNic().substring(min, t.getGeneralUserProfileId().getNic().length());
                                    System.out.println("awawa");
                                    ul = new UserLogin();
                                    ul.setGeneralOrganizationProfileId(t.getSchoolId().getGeneralOrganizationProfileId());
                                    ul.setGeneralUserProfileId(t.getGeneralUserProfileId());
                                    ul.setGeneratedPassword(Security.encrypt(password));
                                    ul.setIsActive(1);
                                    ul.setPassword(Security.encrypt(password));
                                    ul.setSystemInterfaceId((SystemInterface) uni.find(1, SystemInterface.class));
                                    ul.setUserRoleId((UserRole) uni.find(Integer.parseInt(userRoleName), UserRole.class));
                                    ul.setUsername(username);
                                    uni.create(ul);

                                    List<UserLoginGroup> ulg_list = uni.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "' and im.generalOrganizationProfileId.id='" + t.getSchoolId().getGeneralOrganizationProfileId().getId() + "' and im.projectsId.id='1'");
                                    if (ulg_list.size() == 0) {
                                        UserLoginGroup ulg = new UserLoginGroup();
                                        ulg.setCountAttempt(0);
                                        ulg.setGeneralOrganizationProfileId(t.getSchoolId().getGeneralOrganizationProfileId());
                                        ulg.setIsActive(1);
                                        ulg.setIsFirstTime(1);
                                        ulg.setMaxLoginAttempt(3);
                                        ulg.setProjectsId((Projects) uni.find(1, Projects.class));
                                        ulg.setUserLoginId(ul);
                                        ulg.setUserRoleId((UserRole) uni.find(Integer.parseInt(userRoleName), UserRole.class));
                                        uni.create(ulg);
                                    }
                                }
                            }

                        }
                        System.out.println("username " + username);
                        System.out.println("password " + password);
                        if (t.getGeneralUserProfileId().getEmail() != null) {
                            if (!t.getGeneralUserProfileId().getEmail().equals("")) {

                                String cont = "<div>Dear " + t.getGeneralUserProfileId().getNameWithIn() + ",</div><br/>";

                                cont += "<span>Your Account Details to Access the National Education Development Portal (NEDp) as follows;</span><br/>";
                                cont += "<span>Username : " + username + "</span><br/>";
                                cont += "<span>Password : " + password + "</span><br/>";

                                cont += "<br/><br/><span>Thank You,</span><br/>";
                                cont += "<span>Provincial Education Ministry</span><br/>";
                                try {

                                    GoogleMail.Send("noreply.contex", "exoncontex1992", t.getGeneralUserProfileId().getEmail(), subject, cont, null,null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();;
            }

            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Accounts Created Successfully", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            loadListOfTeachers();
        }
        return null;
    }

    public synchronized void activeOrInactive(int id, int tid) {

        String status = "Activated !";
        if (id == 0) {
            status = "Suspended !";
        }

        System.out.println("awa " + id + " " + tid);
        Teacher t = (Teacher) uni.find(tid, Teacher.class);

        List<UserLoginGroup> gop_list = uni.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "' and im.generalOrganizationProfileId.id='" + t.getSchoolId().getGeneralOrganizationProfileId().getId() + "' and im.projectsId.id='1'");

        if (gop_list.size() > 0) {
            UserLoginGroup ul = gop_list.iterator().next();

            DataChangedLogManager gtlm2 = new DataChangedLogManager();
            gtlm2.setAttributeName("is_active");
            gtlm2.setComment("");
            gtlm2.setDate(new Date());
            gtlm2.setNewData(id + "");
            gtlm2.setOldData(ul.getIsActive() + "");
            gtlm2.setReference(ul.getId());
            gtlm2.setTableManagerId((TableManager) uni.find(5, TableManager.class));
            gtlm2.setUserLoginId(ls.getUserLoginId());
            uni.create(gtlm2);

            ul.setIsActive(id);
            uni.update(ul);

        }
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully " + status, "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        loadListOfTeachers();
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

    public List<SelectItem> getUserRoleNameList() {
        return userRoleNameList;
    }

    public void setUserRoleNameList(List<SelectItem> userRoleNameList) {
        this.userRoleNameList = userRoleNameList;
    }

    public String getUserRoleName() {
        return userRoleName;
    }

    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }

    public boolean isSelectAllNotExistBox() {
        return selectAllNotExistBox;
    }

    public void setSelectAllNotExistBox(boolean selectAllNotExistBox) {
        this.selectAllNotExistBox = selectAllNotExistBox;
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
        private int accountStatus;
        private String accountName;
        private String accountStatusName;
        private int tid;

        public TeacherList() {
        }

        public TeacherList(int no, String school_no, String school_name, String nic, String name, String email, boolean checkMail, int accountStatus, String accountName, int tid, String accountStatusName) {
            this.no = no;
            this.school_no = school_no;
            this.school_name = school_name;
            this.nic = nic;
            this.name = name;
            this.email = email;
            this.checkMail = checkMail;
            this.accountStatus = accountStatus;
            this.accountName = accountName;
            this.tid = tid;
            this.accountStatusName = accountStatusName;

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

        public int getAccountStatus() {
            return accountStatus;
        }

        public void setAccountStatus(int accountStatus) {
            this.accountStatus = accountStatus;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public int getTid() {
            return tid;
        }

        public void setTid(int tid) {
            this.tid = tid;
        }

        public String getAccountStatusName() {
            return accountStatusName;
        }

        public void setAccountStatusName(String accountStatusName) {
            this.accountStatusName = accountStatusName;
        }

    }

}
