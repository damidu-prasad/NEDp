/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.loadbean;

import com.ejb.model.common.ComDev;
import com.ejb.model.common.ComLib;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.OrganizationType;
import com.ejb.model.entity.OrganizationTypeManager;
import com.ejb.model.entity.School;
import com.ejb.model.entity.StudentMarks;
import com.ejb.model.entity.SystemInterface;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.UserRoleHasSystemInterface;
import com.webapps.controller.utilities.BannerValus;
import com.webapps.controller.utilities.SortArraysSchools;
import com.webapps.controller.utilities.SortArraysTeachers;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Thilini Madagama
 */
@ManagedBean
@ViewScoped

public class LoadBanners implements Serializable {

    private List<SchoolNameList> scoreSchoolList = new ArrayList();
    private List<TeacherList> scoreTeacherList = new ArrayList();

    @EJB
    private UniDBLocal uniDB;

    Runnable r0 = new Runnable() {
        public void run() {
            loadTopFiveSchools();
//            loadTopFiveTeachers();
        }
    };
    Runnable r1 = new Runnable() {
        public void run() {
            loadTopFiveTeachers();
        }
    };

    Runnable r2 = new Runnable() {
        public void run() {
            if (BannerValus.getDate() != null) {
                System.out.println("");
                if (ComLib.getDate(BannerValus.getDate()).equals(ComLib.getDate(new Date()))) {
                    
                    scoreSchoolList = BannerValus.getScoreSchoolList();
                    scoreTeacherList = BannerValus.getScoreTeacherList();
                } else {

                    if (BannerValus.isIsSent() == false) {
                        BannerValus.setIsSent(true);
                     
                        new Thread(r0).start();
                        new Thread(r1).start();
                        BannerValus.setDate(new Date());

                    }

                }
            } else {
                System.out.println("awa3");
//                loadTopFiveSchools();
//                loadTopFiveTeachers();
                if (BannerValus.isIsSent() == false) {
                    BannerValus.setIsSent(true);
                    new Thread(r0).start();
                    new Thread(r1).start();

                }

                BannerValus.setDate(new Date());
            }
        }
    };

    @PostConstruct
    public void init() {
        try {

            InetAddress ip = InetAddress.getLocalHost();
            if (!ip.getHostName().equals("Thilini")) {
            new Thread(r2).start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadTopFiveSchools() {

        String query = "SELECT g FROM School g  order by g.id ASC";
        List<School> school_list = uniDB.searchByQuery(query);

        List<SchoolNameList> sc_list = new ArrayList();

        int last = ComLib.GetCurrentYear();
        int last1 = last - 1;
        int last2 = last1 - 1;

        int k = 1;
        for (School v : school_list) {
            double val = 0;
            List<StudentMarks> sm_list0 = uniDB.searchByQuery("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + v.getId() + "'  and im.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name in ('" + last + "','" + last1 + "','" + last2 + "')  and im.isRemoved='0' group by im.gradeClassStudentsId.studentsId.id,im.gradeClassStudentsId.gradeClassStreamManagerId.yearId.id,im.termsId.id");
            if (v.getStudentsCount() != 0) {
                System.out.println("awa1");
                val = (sm_list0.size() * 100) / (v.getStudentsCount() * 9);

            }
            sc_list.add(new SchoolNameList(0, v.getId(), v.getGeneralOrganizationProfileId().getName(), ComLib.getDouble(val), val, "", "", ""));
            k++;

        }
        SchoolNameList[] addressArray2 = new SchoolNameList[sc_list.size()];
        for (int i = 0; i < sc_list.size(); i++) {

            addressArray2[i] = sc_list.get(i);
        }

        SchoolNameList[] s2 = SortArraysSchools.GetArray(addressArray2);

        int order2 = 1;
        for (int i = (s2.length - 1); i >= s2.length - 5; i--) {
            System.out.println("ppp");
            getScoreSchoolList().add(new SchoolNameList(order2, s2[i].getSid(), s2[i].getSname(), s2[i].getMarks_enter_perc(), s2[i].getMarksEnter(), s2[i].getNo_of_teachers(), s2[i].getNo_of_active(), s2[i].getActive_perc()));
            order2++;
        }
        BannerValus.setScoreSchoolList(getScoreSchoolList());
        System.out.println("pp1 "+BannerValus.getScoreSchoolList().size());
    }

    public void loadTopFiveTeachers() {
        int last = ComLib.GetCurrentYear();
        int last1 = last - 1;
        int last2 = last1 - 1;

        List<TeacherList> tea_list = new ArrayList();
        int k = 1;
        List<StudentMarks> sm_tea = uniDB.searchByQuery("SELECT im FROM StudentMarks im WHERE  im.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name in ('" + last + "','" + last1 + "','" + last2 + "')  and im.isRemoved='0' group by im.enteredBy.id");
        for (StudentMarks smt : sm_tea) {
            List<Teacher> tea = uniDB.searchByQuerySingle("SELECT im FROM Teacher im WHERE  im.generalUserProfileId.id='" + smt.getEnteredBy().getId() + "' and im.schoolId.id='" + smt.getGradeClassStudentsId().getStudentsId().getSchoolId().getId() + "'");
            if (tea.size() > 0) {
                System.out.println("awa2");
                double val = 0;
                List<StudentMarks> sm_list0 = uniDB.searchByQuery("SELECT im FROM StudentMarks im WHERE im.enteredBy.id='" + smt.getEnteredBy().getId() + "'  and im.gradeClassStudentsId.gradeClassStreamManagerId.yearId.name in ('" + last + "','" + last1 + "','" + last2 + "')  and im.isRemoved='0' group by im.gradeClassStudentsId.studentsId.id,im.gradeClassStudentsId.gradeClassStreamManagerId.yearId.id,im.termsId.id");
                if (smt.getGradeClassStudentsId().getStudentsId().getSchoolId().getStudentsCount() != 0) {

                    val = (sm_list0.size() * 100) / (smt.getGradeClassStudentsId().getStudentsId().getSchoolId().getStudentsCount() * 9);

                }
                tea_list.add(new TeacherList(k, smt.getGradeClassStudentsId().getStudentsId().getSchoolId().getGeneralOrganizationProfileId().getName(), smt.getEnteredBy().getNameWithIn(), val, ComLib.getDouble(val)));
                k++;
            }
        }
        TeacherList[] addressArray2 = new TeacherList[tea_list.size()];
        for (int i = 0; i < tea_list.size(); i++) {

            addressArray2[i] = tea_list.get(i);
        }

        TeacherList[] s2 = SortArraysTeachers.GetArray(addressArray2);

        int order2 = 1;
        for (int i = (s2.length - 1); i >= s2.length - 5; i--) {

            getScoreTeacherList().add(new TeacherList(order2, s2[i].getSchool_name(), s2[i].getTeacher_name(), s2[i].getVal(), s2[i].getValue()));
            order2++;
        }
        BannerValus.setScoreTeacherList(getScoreTeacherList());
 System.out.println("pp2 "+BannerValus.getScoreSchoolList().size());
    }

    public static class TeacherList implements Serializable {

        private int no;
        private String school_name;
        private String teacher_name;
        private double val;
        private String value;

        public TeacherList() {
        }

        public TeacherList(int no, String school_name, String teacher_name, double val, String value) {
            this.no = no;
            this.school_name = school_name;
            this.teacher_name = teacher_name;
            this.val = val;
            this.value = value;

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

        public String getTeacher_name() {
            return teacher_name;
        }

        public void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
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

    public List<SchoolNameList> getScoreSchoolList() {
        return scoreSchoolList;
    }

    public void setScoreSchoolList(List<SchoolNameList> scoreSchoolList) {
        this.scoreSchoolList = scoreSchoolList;
    }

    public List<TeacherList> getScoreTeacherList() {
        return scoreTeacherList;
    }

    public void setScoreTeacherList(List<TeacherList> scoreTeacherList) {
        this.scoreTeacherList = scoreTeacherList;
    }

}
