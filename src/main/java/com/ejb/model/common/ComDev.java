/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.model.common;

import com.ejb.model.businesslogic.NewMailSender;
import com.ejb.model.entity.Classes;
import com.ejb.model.entity.DataChangedLogManager;
import com.ejb.model.entity.DaysPeriod;
import com.ejb.model.entity.DevTarget;
import com.ejb.model.entity.DevTargetType;
import com.ejb.model.entity.EducationDivision;
import com.ejb.model.entity.EducationLevel;
import com.ejb.model.entity.EducationZone;
import com.ejb.model.entity.Employee;
import com.ejb.model.entity.EmployeeCategory;
import com.ejb.model.entity.EmployeePermanentStstus;
import com.ejb.model.entity.EmployeeType;
import com.ejb.model.entity.FingerPrintRegion;
import com.ejb.model.entity.FingerPrintRegionUser;
import com.ejb.model.entity.Gender;
import com.ejb.model.entity.GeneralOrganizationProfile;
import com.ejb.model.entity.GeneralUserProfile;
import com.ejb.model.entity.Grade;
import com.ejb.model.entity.GradeClassHasSubjects;
import com.ejb.model.entity.GradeClassStream;
import com.ejb.model.entity.GradeClassStreamManager;
import com.ejb.model.entity.GradeClassStudents;
import com.ejb.model.entity.GradeClassStudentsHasSubjects;
import com.ejb.model.entity.GradeClassSubjectTeacher;
import com.ejb.model.entity.InterfaceMenu;
import com.ejb.model.entity.InterfaceSubMenu;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.OrgSystemInterfaceController;
import com.ejb.model.entity.Periods;
import com.ejb.model.entity.Projects;
import com.ejb.model.entity.Province;
import com.ejb.model.entity.School;
import com.ejb.model.entity.Streams;
import com.ejb.model.entity.StudentMarks;
import com.ejb.model.entity.Students;
import com.ejb.model.entity.Subjects;
import com.ejb.model.entity.SystemInterface;
import com.ejb.model.entity.TableManager;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.TeacherClassSubjectMean;
import com.ejb.model.entity.TeacherType;
import com.ejb.model.entity.Terms;
import com.ejb.model.entity.TimeTable;
import com.ejb.model.entity.UserLogin;
import com.ejb.model.entity.UserLoginGroup;
import com.ejb.model.entity.UserRole;
import com.ejb.model.entity.UserRoleHasSystemInterface;
import com.ejb.model.entity.Year;
import com.webapps.controller.managebeans.Run;
import com.webapps.controller.managebeans.TeacherAttendanceList;
import com.webapps.controller.managebeans.TimeTableManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Sameera
 */
@Stateless
@LocalBean
public class ComDev {

    @EJB
    private UniDBLocal uniDB;

    public synchronized boolean saveRegisterSystemInterface(String interfaceMenu, String interfaceSubMenu, String interfaceName, String displayName, String url, String displayIcon, String description) {

        boolean status = true;
        try {

            SystemInterface si = null;
            List<SystemInterface> gop_list = uniDB.searchByQuerySingle("SELECT im FROM SystemInterface im WHERE im.interfaceMenuId.id='" + interfaceMenu + "' and im.interfaceSubMenuId.id='" + interfaceSubMenu + "' and im.interfaceName='" + interfaceName + "' and im.displayName='" + displayName + "' and im.url='" + url + "' ");
            if (gop_list.size() > 0) {
                status = false;

            } else {
                si = new SystemInterface();
                si.setDisplayName(displayName);
                si.setInterfaceMenuId((InterfaceMenu) uniDB.find(Integer.parseInt(interfaceMenu), InterfaceMenu.class));
                si.setInterfaceName(interfaceName);
                si.setInterfaceSubMenuId((InterfaceSubMenu) uniDB.find(Integer.parseInt(interfaceSubMenu), InterfaceSubMenu.class));
                si.setStatus(1);
                si.setUrl(url);
                si.setIcon(displayIcon);
                si.setDescription(description);
                uniDB.create(si);

                OrgSystemInterfaceController osic = new OrgSystemInterfaceController();
                osic.setGeneralOrganizationProfileId((GeneralOrganizationProfile) uniDB.find(1, GeneralOrganizationProfile.class));
                osic.setIsActive(1);
                osic.setProjectsId((Projects) uniDB.find(1, Projects.class));
                osic.setSystemInterfaceId(si);
                uniDB.create(osic);

            }
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    public synchronized boolean saveAssignSystemInterface(int systemInterface, int userRole) {

        boolean status = true;
        try {

            UserRoleHasSystemInterface si = null;
            List<UserRoleHasSystemInterface> gop_list = uniDB.searchByQuerySingle("SELECT im FROM UserRoleHasSystemInterface im WHERE im.systemInterfaceId.id='" + systemInterface + "' and im.userRoleId.id='" + userRole + "'  ");
            if (gop_list.size() > 0) {
                status = false;

            } else {
                si = new UserRoleHasSystemInterface();
                si.setSystemInterfaceId((SystemInterface) uniDB.find(systemInterface, SystemInterface.class));
                si.setUserRoleId((UserRole) uniDB.find(userRole, UserRole.class));

                uniDB.create(si);
            }
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    public synchronized boolean saveTeacherRegistration(int province, int zone, int division, int school, String firstName, String lastName, String nameWithIn, String nic, Date dob, String gender, String mobile, String email, String address1, String address2, String address3, LoginSession ls, int regType) {

        boolean status = true;
        try {
            GeneralOrganizationProfile gop = null;
            int userRole = 4;
            if (school != 0) {
                School s = (School) uniDB.find(school, School.class);
                gop = s.getGeneralOrganizationProfileId();
                userRole = 4;
            } else if (division != 0) {

                EducationDivision s = (EducationDivision) uniDB.find(division, EducationDivision.class);
                gop = s.getGeneralOrganizationProfileId();
                userRole = 1;
            } else if (zone != 0) {

                EducationZone s = (EducationZone) uniDB.find(zone, EducationZone.class);
                gop = s.getGeneralOrganizationProfileId();
                userRole = 1;
            } else if (province != 0) {

                Province s = (Province) uniDB.find(province, Province.class);
                gop = s.getGeneralOrganizationProfileId();
                userRole = 1;
            }
            System.out.println(gop.getId() + "|" + userRole);
            GeneralUserProfile si = null;
            List<GeneralUserProfile> gop_list = uniDB.searchByQuerySingle("SELECT im FROM GeneralUserProfile im WHERE im.nic='" + nic + "' ");
            if (gop_list.size() > 0) {
                si = gop_list.iterator().next();

            } else {

                String title = "Mr";
                if (gender.equals("2")) {
                    title = "Mrs";
                }

                si = new GeneralUserProfile();
                si.setAddress1(address1);
                si.setAddress2(address2);
                si.setAddress3(address3);
                si.setDob(dob);
                si.setEmail(email);
                si.setFirstName(firstName);
                si.setGenderId((Gender) uniDB.find(Integer.parseInt(gender), Gender.class));
                si.setGeneralOrganizationProfileIdGop(gop);
                si.setIsActive(1);
                si.setLastName(lastName);
                si.setMobilePhone(mobile);
                si.setNameWithIn(nameWithIn);
                si.setNic(nic);
                si.setProfileCreatedDate(new Date());
                si.setTitle(title);
                uniDB.create(si);

            }
            if (school != 0) {
                School s = (School) uniDB.find(school, School.class);

                Teacher t = null;
                List<Teacher> tea_list = uniDB.searchByQuerySingle("SELECT im FROM Teacher im WHERE im.generalUserProfileId.id='" + si.getId() + "' and im.isActive='1' ");
                if (tea_list.size() > 0) {

                    if (regType == 1) {

                        Teacher ex = tea_list.iterator().next();
                        if (ex.getSchoolId().getId() == s.getId()) {
                            status = false;
                            t = ex;
                        } else {

                            ex.setIsActive(0);
                            uniDB.update(ex);

                            DataChangedLogManager gtlm2 = new DataChangedLogManager();
                            gtlm2.setAttributeName("is_active");
                            gtlm2.setComment("");
                            gtlm2.setDate(new Date());
                            gtlm2.setNewData("0");
                            gtlm2.setOldData("1");
                            gtlm2.setReference(ex.getId());
                            gtlm2.setTableManagerId((TableManager) uniDB.find(6, TableManager.class));
                            gtlm2.setUserLoginId(ls.getUserLoginId());
                            uniDB.create(gtlm2);

                            t = new Teacher();
                            t.setGeneralUserProfileId(si);
                            t.setIsActive(1);
                            t.setSchoolId(s);
                            t.setTeacherId("");
                            t.setIsVerified(true);
                            uniDB.create(t);

                            List<UserLoginGroup> ulg_list = uniDB.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.generalUserProfileId.id='" + t.getGeneralUserProfileId().getId() + "'  and im.projectsId.id='1'");
                            if (ulg_list.size() > 0) {

                                UserLoginGroup ulg = ulg_list.iterator().next();
                                ulg.setGeneralOrganizationProfileId(s.getGeneralOrganizationProfileId());
                                uniDB.update(ulg);

                            }

                            status = true;
                        }
                    } else {

                        Teacher tm = tea_list.iterator().next();
                        tm.setIsActive(0);
                        uniDB.update(tm);

                    }

                } else {
                    if (regType == 1) {
                        t = new Teacher();
                        t.setGeneralUserProfileId(si);
                        t.setIsActive(1);
                        t.setSchoolId(s);
                        t.setTeacherId("");
                        t.setIsVerified(true);
                        uniDB.create(t);
                    }
                }
            }

            Employee e = null;
            List<Employee> em_list = uniDB.searchByQuerySingle("SELECT im FROM Employee im WHERE im.generalUserProfileId.id='" + si.getId() + "' and im.isActive='1' and im.generalOrganizationProfileId.id='" + gop.getId() + "' ");
            if (em_list.size() > 0) {

                e = em_list.iterator().next();

                e.setEmployeeCategoryId((EmployeeCategory) uniDB.find(regType, EmployeeCategory.class));
                uniDB.update(e);

            } else {

                e = new Employee();
                e.setRegDate(new Date());
                e.setGeneralUserProfileId(si);
                e.setEmployeeTypeId((EmployeeType) uniDB.find(1, EmployeeType.class));
                e.setGeneralOrganizationProfileId(gop);
                e.setEmployeeCategoryId((EmployeeCategory) uniDB.find(regType, EmployeeCategory.class));
                e.setEmployeePermanentStstusId((EmployeePermanentStstus) uniDB.find(1, EmployeePermanentStstus.class));
                e.setIsActive(1);
                uniDB.create(e);

            }
            if (si != null) {
                String username = "";
                String password = "";
                List<UserLoginGroup> gop_list1 = uniDB.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.generalUserProfileId.id='" + si.getId() + "'  and im.projectsId.id='1'");
                if (gop_list1.size() > 0) {
                    UserLoginGroup ulg = gop_list1.iterator().next();
                    ulg.setIsActive(1);
                    ulg.setUserRoleId((UserRole) uniDB.find(userRole, UserRole.class));
                    ulg.setGeneralOrganizationProfileId(gop);
                    uniDB.update(ulg);
                    username = ulg.getUserLoginId().getUsername();
                    password = Security.decrypt(ulg.getUserLoginId().getPassword());
                } else {

                    UserLogin ul = null;

                    List<UserLogin> gop1_list = uniDB.searchByQuerySingle("SELECT im FROM UserLogin im WHERE im.generalUserProfileId.id='" + si.getId() + "' ");
                    if (gop1_list.size() > 0) {
                        ul = gop1_list.iterator().next();
                        ul.setIsActive(1);
                        ul.setUserRoleId((UserRole) uniDB.find(userRole, UserRole.class));
                        ul.setGeneralOrganizationProfileId(gop);
                        uniDB.update(ul);

                        username = ul.getUsername();
                        password = Security.decrypt(ul.getPassword());

                    } else {

                        if (!si.getNic().equals("")) {

                            username = si.getNic();

                            int min = si.getNic().length() - 4;

                            password = si.getNic().substring(min, si.getNic().length());
                            System.out.println("awawa");
                            ul = new UserLogin();
                            ul.setGeneralOrganizationProfileId(gop);
                            ul.setGeneralUserProfileId(si);
                            ul.setGeneratedPassword(Security.encrypt(password));
                            ul.setIsActive(1);
                            ul.setPassword(Security.encrypt(password));
                            ul.setSystemInterfaceId((SystemInterface) uniDB.find(1, SystemInterface.class));
                            ul.setUserRoleId((UserRole) uniDB.find(userRole, UserRole.class));
                            ul.setUsername(username);
                            uniDB.create(ul);

                            List<UserLoginGroup> ulg_list = uniDB.searchByQuerySingle("SELECT im FROM UserLoginGroup im WHERE im.userLoginId.generalUserProfileId.id='" + si.getId() + "' and im.generalOrganizationProfileId.id='" + gop.getId() + "' and im.projectsId.id='1'");
                            if (ulg_list.size() == 0) {
                                UserLoginGroup ulg = new UserLoginGroup();
                                ulg.setCountAttempt(0);
                                ulg.setGeneralOrganizationProfileId(gop);
                                ulg.setIsActive(1);
                                ulg.setIsFirstTime(1);
                                ulg.setMaxLoginAttempt(3);
                                ulg.setProjectsId((Projects) uniDB.find(1, Projects.class));
                                ulg.setUserLoginId(ul);
                                ulg.setUserRoleId((UserRole) uniDB.find(userRole, UserRole.class));
                                uniDB.create(ulg);
                            }
                        }
                    }

                }
            }

        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    public synchronized boolean updateTeacherRegistration(int tid, String firstName, String lastName, String nameWithIn, String nic, Date dob, String gender, String mobile, String email, String address1, String address2, String address3, String teacherTypeId, String teacherId) {
        System.out.println("updateTeacherRegistration");
        boolean status = true;
        try {

            Employee t = (Employee) uniDB.find(tid, Employee.class);
            System.out.println("Employee");
            GeneralUserProfile si = t.getGeneralUserProfileId();
            si.setAddress1(address1);
            si.setAddress2(address2);
            si.setAddress3(address3);
            si.setDob(dob);
            si.setEmail(email);
            si.setFirstName(firstName);
            si.setGenderId((Gender) uniDB.find(Integer.parseInt(gender), Gender.class));
            System.out.println("Gender");
            si.setIsActive(1);
            si.setLastName(lastName);
            si.setMobilePhone(mobile);
            si.setNameWithIn(nameWithIn);
            System.out.println("Gender2");

            if (!teacherTypeId.equals("") || !teacherId.equals("")) {
                String sql = "SELECT g FROM Teacher g WHERE g.generalUserProfileId.id ='" + tid + "' ";
                List<Teacher> teacher = uniDB.searchByQuery(sql);
                for (Teacher teacher1 : teacher) {
                    System.out.println("teacher uniDB " + teacher1.getId());
                    if (!teacherTypeId.equals("")) {
                        teacher1.setTeacherTypeId((TeacherType) uniDB.find(Integer.parseInt(teacherTypeId), TeacherType.class));
                    }
                    if (!teacherId.equals("")) {
                        teacher1.setTeacherTypeId((TeacherType) uniDB.find(Integer.parseInt(teacherTypeId), TeacherType.class));
                        teacher1.setTeacherId(teacherId);
                    }

                    uniDB.update(teacher1);
                }
            }
            uniDB.update(si);

        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    public synchronized boolean saveCreateNewClass(int school, int grade, String className, int stream) {

        boolean status = true;
        try {

            Classes cl = null;
            List<Classes> cl_list = uniDB.searchByQuerySingle("SELECT im FROM Classes im WHERE im.name='" + className + "'");
            if (cl_list.size() > 0) {
                cl = cl_list.iterator().next();

            } else {

                cl = new Classes();
                cl.setName(className);
                uniDB.create(cl);
            }

            GradeClassStream gcs = null;
            List<GradeClassStream> gop_list = uniDB.searchByQuerySingle("SELECT im FROM GradeClassStream im WHERE im.gradeId.id='" + grade + "' and im.schoolId.id='" + school + "' and im.classesId.id='" + cl.getId() + "' and im.streamsId.id='" + stream + "' ");
            if (gop_list.size() > 0) {
                status = false;
                gcs = gop_list.iterator().next();
                gcs.setIsActive(1);
                uniDB.update(gcs);

            } else {
                gcs = new GradeClassStream();
                gcs.setClassesId(cl);
                gcs.setGradeId((Grade) uniDB.find(grade, Grade.class));
                gcs.setSchoolId((School) uniDB.find(school, School.class));
                gcs.setStreamsId((Streams) uniDB.find(stream, Streams.class));
                gcs.setIsActive(1);
                uniDB.create(gcs);

                List<GradeClassHasSubjects> sub_list = uniDB.searchByQuery("SELECT im FROM GradeClassHasSubjects im WHERE im.gradeClassStreamId.gradeId.id='" + gcs.getGradeId().getId() + "' and im.gradeClassStreamId.streamsId.id='" + gcs.getStreamsId().getId() + "' and im.gradeClassStreamId.schoolId.id='" + school + "' group by im.subjectsId ");
                for (GradeClassHasSubjects sub : sub_list) {

                    GradeClassHasSubjects g = new GradeClassHasSubjects();
                    g.setGradeClassStreamId(gcs);
                    g.setIsActive(1);
                    g.setSubjectsId(sub.getSubjectsId());
                    uniDB.create(g);

                }

            }

        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    public synchronized boolean saveCreateNewSubject(int school, int grade, String subjectName, int stream, LoginSession ls) {

        boolean status = true;
        boolean isavai = false;
        try {

            Subjects sub = null;
            List<Subjects> cl_list = uniDB.searchByQuerySingle("SELECT im FROM Subjects im WHERE im.name='" + subjectName + "'");
            if (cl_list.size() > 0) {
                sub = cl_list.iterator().next();
                isavai = true;
            } else {

                int education_level = 2;
                if (grade == 12 || grade == 13) {
                    education_level = 3;

                }

                sub = new Subjects();
                sub.setName(subjectName);
                sub.setCode("");
                sub.setEducationLevelId((EducationLevel) uniDB.find(education_level, EducationLevel.class));
                sub.setIsActive(0);

                uniDB.create(sub);
            }
            System.out.println("sub " + sub.getId());
            List<GradeClassStream> gop_list = uniDB.searchByQuery("SELECT im FROM GradeClassStream im WHERE im.gradeId.id='" + grade + "' and im.schoolId.id='" + school + "'  and im.streamsId.id='" + stream + "' ");

            for (GradeClassStream gcs : gop_list) {

                List<GradeClassHasSubjects> sub_list = uniDB.searchByQuery("SELECT im FROM GradeClassHasSubjects im WHERE im.gradeClassStreamId.id='" + gcs.getId() + "' and im.subjectsId.id='" + sub.getId() + "' ");
                if (sub_list.size() > 0) {

                    GradeClassHasSubjects g = sub_list.iterator().next();
                    if (isavai == true) {
                        g.setIsActive(1);
                        uniDB.update(g);
                    }

                } else {

                    GradeClassHasSubjects g = new GradeClassHasSubjects();
                    g.setGradeClassStreamId(gcs);
                    if (isavai == true) {
                        g.setIsActive(1);
                    } else {
                        g.setIsActive(0);

                    }
                    g.setSubjectsId(sub);
                    uniDB.create(g);

                }

            }
            if (isavai == false) {
                Grade g = (Grade) uniDB.find(grade, Grade.class);
                School s = (School) uniDB.find(school, School.class);
                String content = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                        + "<head>\n"
                        + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n"
                        + "<title>Subject Authorizaion</title>\n"
                        + "</head>\n"
                        + "\n"
                        + "<body style=\" font-family:Arial, Helvetica, sans-serif;\">\n"
                        + "<div style=\"width:800px;margin:20px;padding:50px;background-color:#E8E8E8\">\n"
                        + "<div style=\"width:800px;background-color:#F2F2F2;border: 3px solid #D7D7D7\">\n"
                        + "  <div style=\"width:100%;height:50px;background-color:#68b5c5;text-align:center;padding-top:15px;color:#FFF\"><strong>NATIONAL EDUCATION DEVELOPMENT PORTAL</strong></div>\n"
                        + "  <div style=\"width:100%;height:6px;background-color:#3e9fb0\">\n"
                        + "  \n"
                        + "    \n"
                        + "  </div>\n"
                        + "  \n"
                        + "  \n"
                        + " <div>\n"
                        + " <table width=\"800\" border=\"0\" cellspacing=\"0\" cellpadding=\"5\" >\n"
                        + "  <tr>\n"
                        + "    <td width=\"35\"><div style=\"width:15px;height:15px;background-color:#A2A2A2;margin:10px;\"> </div></td>\n"
                        + "    <td width=\"745\" style=\"color:#555555\"><strong>Subject Authorization</strong></td>\n"
                        + "  </tr>\n"
                        + "  \n"
                        + "</table>\n"
                        + "<table width=\"800\" border=\"0\" cellspacing=\"0\" cellpadding=\"5\" style=\"font-size:13px;font-weight:bold;\" >\n"
                        + "  <tr>\n"
                        + "    <td width=\"195\" style=\"padding-left:20px;\">Requested Subject Name</td>\n"
                        + "    <td width=\"6\" >-</td>\n"
                        + "    <td width=\"569\" style=\"color:#555555\">" + subjectName + "</td>\n"
                        + "  </tr>\n"
                        + "  <tr>\n"
                        + "    <td width=\"195\" style=\"padding-left:20px;\">Subject for</td>\n"
                        + "    <td width=\"6\" >-</td>\n"
                        + "    <td width=\"569\" style=\"color:#555555\">Grade " + g.getName() + "</td>\n"
                        + "  </tr>\n"
                        + "  <tr>\n"
                        + "    <td width=\"195\" style=\"padding-left:20px;\">Requested By</td>\n"
                        + "    <td width=\"6\" >-</td>\n"
                        + "    <td width=\"569\" style=\"color:#555555\">" + ls.getUserLoginId().getGeneralUserProfileId().getTitle() + " " + ls.getUserLoginId().getGeneralUserProfileId().getNameWithIn() + " (" + ls.getUserLoginId().getGeneralUserProfileId().getNic() + ")" + "<br/>" + s.getGeneralOrganizationProfileId().getName() + "</td>\n"
                        + "  </tr>\n"
                        + "  <tr>\n"
                        + "    <td width=\"195\" style=\"padding-left:20px;\">Contact Details</td>\n"
                        + "    <td width=\"6\" >-</td>\n"
                        + "    <td width=\"569\" style=\"color:#555555\">" + ls.getUserLoginId().getGeneralUserProfileId().getMobilePhone() + " | " + ls.getUserLoginId().getGeneralUserProfileId().getEmail() + "</td>\n"
                        + "  </tr>\n"
                        + "  \n"
                        + "</table>\n"
                        + " </div>\n"
                        + " \n"
                        + "</div>\n"
                        + "<div style=\"padding-top:20px;width:100%;text-align:center;font-size:12px;color:#666\">Powered by Web Technology Research Center of Java Institute for Advanced Technology</div>\n"
                        + "</div>\n"
                        + "</body>\n"
                        + "</html>";
                NewMailSender nms = new NewMailSender();
                nms.sendM("thilinimadagama23@gmail.com ", "NEDp Subject Authorization", content);
                nms.sendM("ishantha@javainstitute.edu.lk ", "NEDp Subject Authorization", content);

            }
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return isavai;
    }

    public synchronized boolean saveAssignTeacher(int teacher, int gcmID, String year) {

        boolean status = true;
        try {
            System.out.println("sa 1" + teacher + "|" + gcmID + "|" + year);
            if (teacher != 0) {

                Year cl = null;
                List<Year> cl_list = uniDB.searchByQuerySingle("SELECT im FROM Year im WHERE im.name='" + year + "'");
                if (cl_list.size() > 0) {
                    cl = cl_list.iterator().next();

                } else {
                    cl = new Year();
                    cl.setName(year);
                    uniDB.create(cl);
                }

                GradeClassStreamManager gcs = null;
                List<GradeClassStreamManager> gop_list = uniDB.searchByQuerySingle("SELECT im FROM GradeClassStreamManager im WHERE im.yearId.name='" + year + "' and im.gradeClassStreamId.id='" + gcmID + "'  ");
                if (gop_list.size() > 0) {
                    GradeClassStreamManager d = gop_list.iterator().next();
                    d.setTeacherId((Teacher) uniDB.find(teacher, Teacher.class));
                    uniDB.update(d);

                } else {
                    gcs = new GradeClassStreamManager();
                    gcs.setGradeClassStreamId((GradeClassStream) uniDB.find(gcmID, GradeClassStream.class));
                    gcs.setTeacherId((Teacher) uniDB.find(teacher, Teacher.class));
                    gcs.setYearId(cl);
                    uniDB.create(gcs);
                }

            }
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    public synchronized boolean saveSubjectTeacher(int teacher, int subID, String year, int gcsID) {

        boolean status = true;
        try {
            System.out.println("year " + year);
            if (teacher != 0) {

                Year cl = null;
                List<Year> cl_list = uniDB.searchByQuerySingle("SELECT im FROM Year im WHERE im.name='" + year + "'");
                if (cl_list.size() > 0) {
                    cl = cl_list.iterator().next();

                } else {
                    cl = new Year();
                    cl.setName(year);
                    uniDB.create(cl);
                }

                GradeClassHasSubjects gcs = null;
                List<GradeClassHasSubjects> gop_list = uniDB.searchByQuerySingle("SELECT im FROM GradeClassHasSubjects im WHERE im.gradeClassStreamId.id='" + gcsID + "' and im.subjectsId.id='" + subID + "' ");
                if (gop_list.size() > 0) {
                    gcs = gop_list.iterator().next();
                } else {

                    gcs = new GradeClassHasSubjects();
                    gcs.setGradeClassStreamId((GradeClassStream) uniDB.find(gcsID, GradeClassStream.class));
                    gcs.setIsActive(1);
                    gcs.setSubjectsId((Subjects) uniDB.find(subID, Subjects.class));
                    uniDB.create(gcs);
                }
                System.out.println("bb " + year + " " + gcsID);
                GradeClassStreamManager gcsm = null;
                List<GradeClassStreamManager> gcsm_list = uniDB.searchByQuerySingle("SELECT im FROM GradeClassStreamManager im WHERE im.yearId.name='" + year + "' and im.gradeClassStreamId.id='" + gcsID + "'");
                if (gcsm_list.size() > 0) {
                    gcsm = gcsm_list.iterator().next();
                } else {
                    gcsm = new GradeClassStreamManager();
                    gcsm.setGradeClassStreamId((GradeClassStream) uniDB.find(gcsID, GradeClassStream.class));
                    gcsm.setYearId(cl);
                    uniDB.create(gcsm);
                }

                GradeClassSubjectTeacher gcst = null;
                System.out.println(gcs.getId() + "sub");
                List<GradeClassSubjectTeacher> gcst_list = uniDB.searchByQuerySingle("SELECT im FROM GradeClassSubjectTeacher im WHERE im.gradeClassHasSubjectsId.id='" + gcs.getId() + "' and im.gradeClassStreamManagerId.id='" + gcsm.getId() + "'  ");
                if (gcst_list.size() > 0) {
                    gcst = gcst_list.iterator().next();
                    gcst.setTeacherId((Teacher) uniDB.find(teacher, Teacher.class));
                    gcst.setIsActive(1);
                    uniDB.update(gcst);
                    System.out.println("awap1" + gcst.getId());
                } else {
                    gcst = new GradeClassSubjectTeacher();
                    gcst.setGradeClassHasSubjectsId(gcs);
                    gcst.setTeacherId((Teacher) uniDB.find(teacher, Teacher.class));
                    gcst.setGradeClassStreamManagerId(gcsm);
                    gcst.setIsActive(1);
                    uniDB.create(gcst);
                    System.out.println("awap2");
                }

            }
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    public synchronized void saveMarks(String indexNo, String name, List<String> sub, School school, GradeClassStream gcs, GradeClassStreamManager gcsm, List<GradeClassHasSubjects> subjects, Terms term, LoginSession ls) {

        String search_name = name.replace("'", "''");

        Students st = null;
        List<Students> cl_list = uniDB.searchByQuerySingle("SELECT im FROM Students im WHERE im.generalUserProfileId.nameWithIn='" + search_name + "' and im.schoolId.id='" + school.getId() + "' ");
        if (cl_list.size() > 0) {
            st = cl_list.iterator().next();
            if (!indexNo.equals("")) {
                st.setStudentId(indexNo);
                uniDB.update(st);
            }
        } else {
            GeneralUserProfile gup = null;

            List<GeneralUserProfile> gup_list = uniDB.searchByQuerySingle("SELECT im FROM GeneralUserProfile im WHERE im.nameWithIn='" + search_name + "' and im.generalOrganizationProfileIdGop.id='" + school.getGeneralOrganizationProfileId().getId() + "' ");
            if (gup_list.size() > 0) {
                gup = gup_list.iterator().next();
            } else {
                gup = new GeneralUserProfile();
                gup.setAddress1(" ");
                gup.setAddress2(" ");
                gup.setAddress3(" ");
                gup.setTitle("Mr");
                gup.setNic("");
                gup.setFirstName(" ");
                gup.setLastName(" ");
                gup.setNameWithIn(name);
                gup.setGeneralOrganizationProfileIdGop(school.getGeneralOrganizationProfileId());
                uniDB.create(gup);

            }

            st = new Students();
            st.setGeneralUserProfileId(gup);
            st.setSchoolId(school);
            st.setStudentId(indexNo);
            uniDB.create(st);

        }

        GradeClassStudents gcst = null;
        List<GradeClassStudents> gcst_list = uniDB.searchByQuerySingle("SELECT im FROM GradeClassStudents im WHERE im.gradeClassStreamManagerId.id='" + gcsm.getId() + "' and im.studentsId.id='" + st.getId() + "'  and im.isRemoved='0' ");
        if (gcst_list.size() > 0) {
            gcst = gcst_list.iterator().next();
        } else {

            gcst = new GradeClassStudents();
            gcst.setGradeClassStreamManagerId(gcsm);
            gcst.setStudentsId(st);
            gcst.setIsRemoved(0);
            uniDB.create(gcst);
        }
        System.out.println("subb " + sub.size());
        for (int i = 0; i < sub.size(); i++) {
            System.out.println("i " + i);
            String sub0 = sub.get(i);

            double marks0 = 0.0;
            boolean is_mandatory0 = true;
            boolean is_present0 = true;
            if (sub0.equals("") || sub0.equals("-")) {
                marks0 = 0.0;
                is_mandatory0 = false;
                is_present0 = true;
            } else if (sub0.equalsIgnoreCase("ab")) {
                marks0 = 0.0;
                is_mandatory0 = true;
                is_present0 = false;
            } else {
                is_mandatory0 = true;
                is_present0 = true;
                if (sub0.equalsIgnoreCase("A") || sub0.equalsIgnoreCase("B") || sub0.equalsIgnoreCase("C") || sub0.equalsIgnoreCase("S") || sub0.equalsIgnoreCase("W")) {
                    marks0 = ComLib.GetMarkByValue(sub0);
                } else {
                    marks0 = Double.parseDouble(sub0);
                }
            }
            if (is_mandatory0 == true) {

                List<GradeClassStudentsHasSubjects> gcshs_list0 = uniDB.searchByQuerySingle("SELECT im FROM GradeClassStudentsHasSubjects im WHERE im.gradeClassHasSubjectsId.id='" + subjects.get(i).getId() + "' and im.gradeClassStudentsId.id='" + gcst.getId() + "'  ");
                if (gcshs_list0.size() == 0) {
                    GradeClassStudentsHasSubjects gcshs = new GradeClassStudentsHasSubjects();
                    gcshs.setGradeClassHasSubjectsId(subjects.get(i));
                    gcshs.setGradeClassStudentsId(gcst);
                    uniDB.create(gcshs);
                }
            } else {

                List<GradeClassStudentsHasSubjects> gcshs_list0 = uniDB.searchByQuerySingle("SELECT im FROM GradeClassStudentsHasSubjects im WHERE im.gradeClassHasSubjectsId.id='" + subjects.get(i).getId() + "' and im.gradeClassStudentsId.id='" + gcst.getId() + "'  ");
                if (gcshs_list0.size() > 0) {
                    uniDB.remove(gcshs_list0.iterator().next().getId(), GradeClassStudentsHasSubjects.class);
                }
            }

            StudentMarks sm0 = null;
            List<StudentMarks> sm_list0 = uniDB.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(i).getId() + "'  and im.isRemoved='0' ");
            if (sm_list0.size() > 0) {
                sm0 = sm_list0.iterator().next();
                sm0.setIsMandatory(is_mandatory0);
                sm0.setMarks(marks0);
                sm0.setIsPresent(is_present0);
                sm0.setEnteredBy(ls.getUserLoginId().getGeneralUserProfileId());
                uniDB.update(sm0);
            } else {
                sm0 = new StudentMarks();
                sm0.setGradeClassHasSubjectsId(subjects.get(i));
                sm0.setGradeClassStudentsId(gcst);
                sm0.setIsMandatory(is_mandatory0);
                sm0.setMarks(marks0);
                sm0.setTermsId(term);
                sm0.setIsPresent(is_present0);
                sm0.setEnteredBy(ls.getUserLoginId().getGeneralUserProfileId());
                sm0.setIsRemoved(0);
                uniDB.create(sm0);
            }

        }

    }

    public synchronized boolean saveStudentRegistration(int school, int classId, int year, String index, String nameWithIn, String gender, String address1, String address2, String address3) {

        boolean status = true;

        try {

            School s = (School) uniDB.find(school, School.class
            );

            String title = "Mr";
            if (gender.equals("2")) {
                title = "Mrs";
            }

            GeneralUserProfile si = new GeneralUserProfile();
            si.setAddress1(address1);
            si.setAddress2(address2);
            si.setAddress3(address3);
            si.setDob(null);
            si.setEmail("");
            si.setFirstName(" ");
            si.setGenderId((Gender) uniDB.find(Integer.parseInt(gender), Gender.class));
            si.setGeneralOrganizationProfileIdGop(s.getGeneralOrganizationProfileId());
            si.setIsActive(1);
            si.setLastName(" ");
            si.setMobilePhone(" ");
            si.setNameWithIn(nameWithIn);
            si.setNic("");
            si.setProfileCreatedDate(new Date());
            si.setTitle(title);
            uniDB.create(si);

            List<Students> tea_list = uniDB.searchByQuerySingle("SELECT im FROM Students im WHERE im.studentId='" + index + "' and im.schoolId.id='" + s.getId() + "' ");
            if (tea_list.size() > 0) {
                status = false;

            } else {
                Students st = new Students();
                st.setGeneralUserProfileId(si);
                st.setSchoolId(s);
                st.setStudentId(index);
                uniDB.create(st);

                GradeClassStreamManager gcsm = null;
                List<GradeClassStreamManager> gop_list = uniDB.searchByQuerySingle("SELECT im FROM GradeClassStreamManager im WHERE im.yearId.id='" + year + "' and im.gradeClassStreamId.id='" + classId + "'  ");
                if (gop_list.size() > 0) {
                    gcsm = gop_list.iterator().next();

                } else {
                    gcsm = new GradeClassStreamManager();
                    gcsm.setGradeClassStreamId((GradeClassStream) uniDB.find(classId, GradeClassStream.class));
                    gcsm.setTeacherId(null);
                    gcsm.setYearId((Year) uniDB.find(year, Year.class));
                    uniDB.create(gcsm);
                }
                GradeClassStudents gcst = null;
                List<GradeClassStudents> gcst_list = uniDB.searchByQuerySingle("SELECT im FROM GradeClassStudents im WHERE im.gradeClassStreamManagerId.id='" + gcsm.getId() + "' and im.studentsId.id='" + st.getId() + "' and im.isRemoved='0'");
                if (gcst_list.size() > 0) {
                    gcst = gcst_list.iterator().next();
                } else {

                    gcst = new GradeClassStudents();
                    gcst.setGradeClassStreamManagerId(gcsm);
                    gcst.setStudentsId(st);
                    gcst.setIsRemoved(0);
                    uniDB.create(gcst);
                }
            }
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    public synchronized boolean SaveUpdateContactDetails(LoginSession ls, String nameWithIn, String mobile, String email, String address1, String address2, String address3) {

        boolean status = true;

        try {
            if (address1.equals("")) {
                address1 = " ";
            }
            ls.getUserLoginId().getGeneralUserProfileId().setNameWithIn(nameWithIn);
            ls.getUserLoginId().getGeneralUserProfileId().setMobilePhone(mobile);
            ls.getUserLoginId().getGeneralUserProfileId().setEmail(email);
            ls.getUserLoginId().getGeneralUserProfileId().setAddress1(address1);
            ls.getUserLoginId().getGeneralUserProfileId().setAddress2(address2);
            ls.getUserLoginId().getGeneralUserProfileId().setAddress3(address3);
            uniDB.update(ls.getUserLoginId().getGeneralUserProfileId());

        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    public synchronized boolean saveTargetForSelectedTerm(double target, int year, int term) {

        boolean status = true;
        try {

            DevTarget si = null;
            List<DevTarget> gop_list = uniDB.searchByQuerySingle("SELECT im FROM DevTarget im WHERE im.devTargetTypeId.id='1' and im.termsId.id='" + term + "' and im.yearId.id='" + year + "'");
            if (gop_list.size() > 0) {
                si = gop_list.iterator().next();

                si.setTarget(target);
                uniDB.update(si);

            } else {
                si = new DevTarget();
                si.setDevTargetTypeId((DevTargetType) uniDB.find(1, DevTargetType.class));
                si.setTarget(target);
                si.setTermsId((Terms) uniDB.find(term, Terms.class));
                si.setYearId((Year) uniDB.find(year, Year.class));
                uniDB.create(si);

            }
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    public synchronized boolean saveTeacherMean(List<Run.TeacherMeanValues> teacherMeanValues, int year, int term) {

        boolean status = true;
        try {

            if (teacherMeanValues.size() > 0) {

                List<TeacherClassSubjectMean> gop_list = uniDB.searchByQuery("SELECT im FROM TeacherClassSubjectMean im WHERE im.termsId.id='" + term + "' and im.yearId.id='" + year + "' ");
                if (gop_list.size() > 0) {
                    for (TeacherClassSubjectMean e : gop_list) {

                        uniDB.remove(e.getId(), TeacherClassSubjectMean.class);
                    }

                }

                for (Run.TeacherMeanValues f : teacherMeanValues) {

                    TeacherClassSubjectMean ea = new TeacherClassSubjectMean();
                    ea.setGradeClassStreamId(f.getClassID());
                    ea.setSubjectsId(f.getSubject());
                    ea.setTeacherId(f.getTeacher());
                    ea.setMeanValue(f.getMean());
                    ea.setTermsId((Terms) uniDB.find(f.getTerm(), Terms.class));
                    ea.setYearId((Year) uniDB.find(f.getYear(), Year.class));
                    uniDB.create(ea);

                }

            }
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    public synchronized boolean saveSelectedTeacherToTimeTable(List<TimeTableManager.PeriodData> selectedDataCell, String gcmId, int loginUserSchoolId) {
        boolean status = false;
        try {

            for (TimeTableManager.PeriodData data : selectedDataCell) {

                System.out.println("llllll" + data.getCellSubjectTeacherId());

                if (data.getTimeTableObj() == null) {

                    System.out.println("Periods Id " + data.getDaysPeriodId());

                    System.out.println("GradeClassStreamManager " + gcmId);

                    System.out.println("GradeClassSubjectTeacher " + data.getCellSubjectTeacherId());

                    System.out.println("gradeClassHasSubjectsId " + data.getCellSubjcetId());

                    if (!data.getCellSubjectTeacherId().equals("0")) {

                        String query_searchTimeTable = "SELECT g FROM TimeTable g WHERE g.daysPeriodId.id = '" + data.getDaysPeriodId() + "' AND g.gradeClassStreamManagerId.id='" + gcmId + "'";

                        List<TimeTable> timeTableList = uniDB.searchByQuery(query_searchTimeTable);

                        if (timeTableList.isEmpty()) {

                            String query_days_periods = "SELECT g FROM DaysPeriod g where g.id = '" + data.getDaysPeriodId() + "' ";

                            List<DaysPeriod> daysPeriodsList = uniDB.searchByQuerySingle(query_days_periods);

                            DaysPeriod periodsOb = daysPeriodsList.get(0);

                            String query_gradeClassManagerId = "SELECT g FROM GradeClassStreamManager g WHERE g.id='" + gcmId + "'";

                            List<GradeClassStreamManager> gradeClassStreamManagerList = uniDB.searchByQuerySingle(query_gradeClassManagerId);

                            GradeClassStreamManager gcmOb = gradeClassStreamManagerList.get(0);

//                        String query_gradeClassHasSubject = "SELECT g FROM GradeClassSubjectTeacher g WHERE g.teacherId.id='" + data.getCellSubjectTeacherId() + "'AND g.gradeClassStreamManagerId.id='" + gcmId + "' ";
                            String query_gradeClassHasSubject = "SELECT g FROM GradeClassSubjectTeacher g WHERE g.teacherId.id='" + data.getCellSubjectTeacherId() + "' ";

                            List<GradeClassSubjectTeacher> gradeClassSubjectTeacherList = uniDB.searchByQuerySingle(query_gradeClassHasSubject);

                            GradeClassSubjectTeacher gcstOb = null;

                            if (gradeClassSubjectTeacherList.size() > 0) {

                                System.out.println("gradeClassSubjectTeacherList size : " + gradeClassSubjectTeacherList.size());

                                gcstOb = gradeClassSubjectTeacherList.get(0);
                            } else {
//                            this teacher does not have assign to selected grade
                                return false;
                            }

                            TimeTable timeTable = new TimeTable();

                            timeTable.setDaysPeriodId(periodsOb);

                            timeTable.setGradeClassStreamManagerId(gcmOb);

                            timeTable.setGradeClassSubjectTeacherId(gcstOb);

                            uniDB.create(timeTable);

                            status = true;
                        }
                    } else {

                    }
                } else {

                    System.out.println("kkkkkkk");

                    String quey_search = "SELECT g FROM TimeTable g WHERE g.id='" + data.getTimeTableObj().getId() + "'";

                    List<TimeTable> timeTablesList = uniDB.searchByQuery(quey_search);

                    DaysPeriod periodsOb = null;

                    GradeClassStreamManager gcmOb = null;

                    GradeClassSubjectTeacher gcstOb = null;

                    String query_days_periods = "SELECT g FROM DaysPeriod g where g.id = '" + data.getDaysPeriodId() + "' ";

                    List<DaysPeriod> daysPeriodsList = uniDB.searchByQuery(query_days_periods);

                    if (daysPeriodsList.size() > 0) {

                        for (DaysPeriod p : daysPeriodsList) {

                            periodsOb = p;

                            break;
                        }

                    } else {

                        return false;

                    }

                    String query_gradeClassManagerId = "SELECT g FROM GradeClassStreamManager g WHERE g.id='" + gcmId + "'";

                    List<GradeClassStreamManager> gradeClassStreamManagerList = uniDB.searchByQuery(query_gradeClassManagerId);

                    if (gradeClassStreamManagerList.size() > 0) {
                        System.out.println("gradeClassStreamManagerList " + gradeClassStreamManagerList.size());
                        for (GradeClassStreamManager gradeClassStreamManager : gradeClassStreamManagerList) {

                            gcmOb = gradeClassStreamManager;

                            break;
                        }

                    } else {

                        return false;

                    }

                    String query_gradeClassHasSubject = "SELECT g FROM GradeClassSubjectTeacher g WHERE g.teacherId.id='" + data.getCellSubjectTeacherId() + "'AND g.gradeClassStreamManagerId.id='" + gcmId + "' ";

                    List<GradeClassSubjectTeacher> gradeClassSubjectTeacherList = uniDB.searchByQuery(query_gradeClassHasSubject);

                    if (gradeClassSubjectTeacherList.size() > 0) {
                        System.out.println("gradeClassStreamManagerList " + gradeClassSubjectTeacherList.size());
                        for (GradeClassSubjectTeacher gradeClassSubjectTeacher : gradeClassSubjectTeacherList) {

                            gcstOb = gradeClassSubjectTeacher;

                        }

                    } else {

                        return false;

                    }

                    if (timeTablesList.size() > 0) {

                        TimeTable obj = timeTablesList.iterator().next();

                        obj.setDaysPeriodId(periodsOb);

                        obj.setGradeClassStreamManagerId(gcmOb);

                        obj.setGradeClassSubjectTeacherId(gcstOb);

                        uniDB.update(obj);

                        status = true;

                    } else {

                        status = false;

                    }
                }
            }
        } catch (Exception e) {

            e.printStackTrace();

            status = false;
        }

        return status;
    }

    public boolean saveFingerPrintUser(GeneralUserProfile gup, int enrollementNo) {

        try {
            List<FingerPrintRegion> region = uniDB.searchByQuery("SELECT g FROM FingerPrintRegion g WHERE g.id='1'");

            if (region != null) {
                FingerPrintRegionUser gpru = new FingerPrintRegionUser();

                gpru.setFingerPrintRegionId(region.iterator().next());
                gpru.setGeneralUserProfileGupId(gup);
                gpru.setAddedDate(new Date());
                gpru.setIsActive(true);
                gpru.setEnrollementId(enrollementNo);
                uniDB.create(gpru);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }

    }

}
