/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.printbeans;

import com.ejb.model.common.ComLib;
import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.GradeClassHasSubjects;
import com.ejb.model.entity.GradeClassStudents;
import com.ejb.model.entity.GradeClassStudentsHasSubjects;
import com.ejb.model.entity.StudentMarks;
import com.ejb.model.entity.Students;
import com.ejb.model.entity.Year;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Thilini Madagama
 */
@ManagedBean
@ViewScoped
public class PrintReportCard implements Serializable {

    HttpServletResponse response;
    HttpServletRequest request;

    private int school;
    private int grade;
    private int year;
    private int term;
    private String YearName;
    private List<StudentList> stList = new ArrayList();
    private String gradeAvg1;
    private String gradeAvg2;
    private String gradeAvg3;
    private boolean is_upper_class = true;

    @EJB
    UniDBLocal uni;
    private ComLib comlib;

    @PostConstruct
    public void init() {

        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        if (request.getParameter("school") != null) {

            setSchool(Integer.parseInt(request.getParameter("school")));
            setGrade(Integer.parseInt(request.getParameter("grade")));
            setYear(Integer.parseInt(request.getParameter("year")));
            setTerm(Integer.parseInt(request.getParameter("term")));

            if (grade == 1 || grade == 2) {

                is_upper_class = false;
            }

            Year y = (Year) uni.find(year, Year.class);
            YearName = y.getName();
            try {

                String jsn = request.getParameter("json_array").trim();
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(jsn);
                JSONObject job = (JSONObject) obj;
                JSONArray jsnarr = (JSONArray) job.get("jsn");

                int old_class = 0;

                double classAvg1 = 0;
                double classAvg2 = 0;
                double classAvg3 = 0;

                double classTot1 = 0;
                double classTot2 = 0;
                double classTot3 = 0;

                int subSize = 0;

                for (Iterator it = jsnarr.iterator(); it.hasNext();) {

                    JSONObject json_object = (JSONObject) it.next();

                    int sid = Integer.valueOf(json_object.get("sid").toString().trim());
                    GradeClassStudents gcs = (GradeClassStudents) uni.find(sid, GradeClassStudents.class);

                    double tot_val1 = 0;
                    double tot_val2 = 0;
                    double tot_val3 = 0;

                    double avg1 = 0;
                    double avg2 = 0;
                    double avg3 = 0;

                    List<SubjectList> subList = new ArrayList();

                    String query = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassStudentsId.id='" + gcs.getId() + "'  and g.gradeClassStudentsId.isRemoved='0' and g.gradeClassHasSubjectsId.subjectsId.isActive='1'";
                    List<GradeClassStudentsHasSubjects> list = uni.searchByQuery(query);

                    if (list.size() > subSize) {
                        subSize = list.size();
                    }

                    if (list.size() > 0) {
                        int i = 1;
                        for (GradeClassStudentsHasSubjects gcshs : list) {
                            String term1_marks = "0";
                            String term2_marks = "0";
                            String term3_marks = "0";

                            String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.termsId.id='1' and g.gradeClassHasSubjectsId.id='" + gcshs.getGradeClassHasSubjectsId().getId() + "'  and g.isRemoved='0' and g.isMandatory='1' ";
                            List<StudentMarks> listm = uni.searchByQuerySingle(querym);
                            if (listm.size() > 0) {
                                StudentMarks sm = listm.iterator().next();

                                if (grade == 1 || grade == 2) {

                                    term1_marks = comlib.GetValueByMark(sm.getMarks());
                                } else {

                                    term1_marks = comlib.getInt(sm.getMarks());
                                }
                                tot_val1 += sm.getMarks();
                            }
                            String querym2 = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.termsId.id='2' and g.gradeClassHasSubjectsId.id='" + gcshs.getGradeClassHasSubjectsId().getId() + "'  and g.isRemoved='0' and g.isMandatory='1' ";
                            List<StudentMarks> listm2 = uni.searchByQuerySingle(querym2);
                            if (listm2.size() > 0) {
                                StudentMarks sm = listm2.iterator().next();
                                if (grade == 1 || grade == 2) {
                                    term2_marks = comlib.GetValueByMark(sm.getMarks());
                                } else {
                                    term2_marks = comlib.getInt(sm.getMarks());
                                }
                                tot_val2 += sm.getMarks();
                            }

                            String querym3 = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcs.getId() + "' and g.termsId.id='3' and g.gradeClassHasSubjectsId.id='" + gcshs.getGradeClassHasSubjectsId().getId() + "'  and g.isRemoved='0' and g.isMandatory='1'  ";
                            List<StudentMarks> listm3 = uni.searchByQuerySingle(querym3);
                            if (listm3.size() > 0) {
                                System.out.println("awawawa");
                                StudentMarks sm = listm3.iterator().next();
                                if (grade == 1 || grade == 2) {
                                    term3_marks = comlib.GetValueByMark(sm.getMarks());
                                } else {
                                    term3_marks = comlib.getInt(sm.getMarks());
                                }
                                tot_val3 += sm.getMarks();
                            }

                            subList.add(new SubjectList(i, gcshs.getGradeClassHasSubjectsId().getSubjectsId().getName(), term1_marks, term2_marks, term3_marks));
                            i++;
                        }
                        System.out.println("list " + list.size());
                        avg1 = tot_val1 / list.size();
                        avg2 = tot_val2 / list.size();
                        avg3 = tot_val3 / list.size();
                    }

                    if (old_class != gcs.getGradeClassStreamManagerId().getGradeClassStreamId().getClassesId().getId()) {

                        String queryt = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.gradeClassStreamId.id='" + gcs.getGradeClassStreamManagerId().getGradeClassStreamId().getId() + "' and g.gradeClassStreamManagerId.yearId.id='" + year + "'  and g.isRemoved='0'";
                        List<GradeClassStudents> stu_list = uni.searchByQuery(queryt);
                        System.out.println("stu_list " + stu_list.size());
                        for (GradeClassStudents gcsl : stu_list) {
                            String query1 = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassStudentsId.id='" + gcsl.getId() + "'  and g.gradeClassStudentsId.isRemoved='0' and g.gradeClassHasSubjectsId.subjectsId.isActive='1'";
                            List<GradeClassStudentsHasSubjects> list1 = uni.searchByQuery(query1);
                            for (GradeClassStudentsHasSubjects gcshs1 : list1) {

                                String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcsl.getId() + "' and g.termsId.id='1'  and g.isRemoved='0' and g.isMandatory='1' and g.gradeClassHasSubjectsId.id='" + gcshs1.getGradeClassHasSubjectsId().getId() + "'";
                                List<StudentMarks> listm = uni.searchByQuery(querym);
                                if (listm.size() > 0) {
                                    for (StudentMarks sm : listm) {
                                        classTot1 += sm.getMarks();
                                    }
                                }
                                String querym2 = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcsl.getId() + "' and g.termsId.id='2'  and g.isRemoved='0' and g.isMandatory='1' and g.gradeClassHasSubjectsId.id='" + gcshs1.getGradeClassHasSubjectsId().getId() + "'";
                                List<StudentMarks> listm2 = uni.searchByQuery(querym2);
                                if (listm2.size() > 0) {
                                    for (StudentMarks sm : listm2) {
                                        classTot2 += sm.getMarks();
                                    }
                                }
                                String querym3 = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcsl.getId() + "' and g.termsId.id='3'  and g.isRemoved='0' and g.isMandatory='1' and g.gradeClassHasSubjectsId.id='" + gcshs1.getGradeClassHasSubjectsId().getId() + "'";
                                List<StudentMarks> listm3 = uni.searchByQuery(querym3);
                                if (listm3.size() > 0) {
                                    for (StudentMarks sm : listm3) {
                                        classTot3 += sm.getMarks();
                                    }
                                }
                            }
                        }

                        System.out.println("tot " + classTot1);
                        if (stu_list.size() > 0) {
                            classAvg1 = classTot1 / (stu_list.size() * list.size());
                            classAvg2 = classTot2 / (stu_list.size() * list.size());
                            classAvg3 = classTot3 / (stu_list.size() * list.size());
                        }
                    }
                    old_class = gcs.getGradeClassStreamManagerId().getGradeClassStreamId().getClassesId().getId();

                    String order1 = "(" + json_object.get("order1").toString().trim() + ")";
                    String order2 = "(" + json_object.get("order2").toString().trim() + ")";
                    String order3 = "(" + json_object.get("order3").toString().trim() + ")";
                    if (tot_val1 == 0) {

                        order1 = "";
                    }
                    if (tot_val2 == 0) {

                        order2 = "";
                    }
                    if (tot_val3 == 0) {

                        order3 = "";
                    }
                    stList.add(new StudentList(sid, gcs.getStudentsId().getGeneralUserProfileId().getNameWithIn(), gcs.getStudentsId().getStudentId(), order1, order2, order3, subList, comlib.getDouble(tot_val1), comlib.getDouble(tot_val2), comlib.getDouble(tot_val3), gcs.getGradeClassStreamManagerId().getGradeClassStreamId().getGradeId().getName() + "-" + gcs.getGradeClassStreamManagerId().getGradeClassStreamId().getClassesId().getName(), comlib.getDouble(avg1), comlib.getDouble(avg2), comlib.getDouble(avg3), comlib.getDouble(classAvg1), comlib.getDouble(classAvg2), comlib.getDouble(classAvg3)));
                }

                double gradeAvg1 = 0;
                double gradeAvg2 = 0;
                double gradeAvg3 = 0;

                double gradeTot1 = 0;
                double gradeTot2 = 0;
                double gradeTot3 = 0;
                String queryt1 = "SELECT g FROM GradeClassStudents g where g.gradeClassStreamManagerId.gradeClassStreamId.gradeId.id='" + grade + "' and g.gradeClassStreamManagerId.yearId.id='" + year + "' and g.gradeClassStreamManagerId.gradeClassStreamId.schoolId.id='" + school + "'  and g.isRemoved='0' ";
                List<GradeClassStudents> stu_list1 = uni.searchByQuery(queryt1);

                for (GradeClassStudents gcsl : stu_list1) {

                    String query1 = "SELECT g FROM GradeClassStudentsHasSubjects g where g.gradeClassStudentsId.id='" + gcsl.getId() + "'  and g.gradeClassStudentsId.isRemoved='0' and g.gradeClassHasSubjectsId.subjectsId.isActive='1'";
                    List<GradeClassStudentsHasSubjects> list1 = uni.searchByQuery(query1);
                    for (GradeClassStudentsHasSubjects gcshs1 : list1) {

                        String querym = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcsl.getId() + "' and g.termsId.id='1'  and g.isRemoved='0' and g.isMandatory='1' and g.gradeClassHasSubjectsId.id='" + gcshs1.getGradeClassHasSubjectsId().getId() + "'";
                        List<StudentMarks> listm = uni.searchByQuery(querym);
                        if (listm.size() > 0) {
                            for (StudentMarks sm : listm) {
                                gradeTot1 += sm.getMarks();
                            }
                        }
                        String querym2 = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcsl.getId() + "' and g.termsId.id='2'  and g.isRemoved='0' and g.isMandatory='1' and g.gradeClassHasSubjectsId.id='" + gcshs1.getGradeClassHasSubjectsId().getId() + "'";
                        List<StudentMarks> listm2 = uni.searchByQuery(querym2);
                        if (listm2.size() > 0) {
                            for (StudentMarks sm : listm2) {
                                gradeTot2 += sm.getMarks();
                            }
                        }
                        String querym3 = "SELECT g FROM StudentMarks g where g.gradeClassStudentsId.id='" + gcsl.getId() + "' and g.termsId.id='3'  and g.isRemoved='0' and g.isMandatory='1' and g.gradeClassHasSubjectsId.id='" + gcshs1.getGradeClassHasSubjectsId().getId() + "'";
                        List<StudentMarks> listm3 = uni.searchByQuery(querym3);
                        if (listm3.size() > 0) {
                            for (StudentMarks sm : listm3) {
                                gradeTot3 += sm.getMarks();
                            }
                        }
                    }

                }

                System.out.println("size " + stu_list1.size());
                System.out.println("subSize " + subSize);
                if (stu_list1.size() > 0 && subSize > 0) {
                    System.out.println(gradeTot3 + "|" + stu_list1.size() + "|" + subSize);
                    gradeAvg1 = gradeTot1 / (stu_list1.size() * subSize);
                    gradeAvg2 = gradeTot2 / (stu_list1.size() * subSize);
                    gradeAvg3 = gradeTot3 / (stu_list1.size() * subSize);
                }
                this.gradeAvg1 = comlib.getDouble(gradeAvg1);
                this.gradeAvg2 = comlib.getDouble(gradeAvg2);
                this.gradeAvg3 = comlib.getDouble(gradeAvg3);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public int getSchool() {
        return school;
    }

    public void setSchool(int school) {
        this.school = school;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public List<StudentList> getStList() {
        return stList;
    }

    public void setStList(List<StudentList> stList) {
        this.stList = stList;
    }

    public String getYearName() {
        return YearName;
    }

    public void setYearName(String YearName) {
        this.YearName = YearName;
    }

    public String getGradeAvg1() {
        return gradeAvg1;
    }

    public void setGradeAvg1(String gradeAvg1) {
        this.gradeAvg1 = gradeAvg1;
    }

    public String getGradeAvg2() {
        return gradeAvg2;
    }

    public void setGradeAvg2(String gradeAvg2) {
        this.gradeAvg2 = gradeAvg2;
    }

    public String getGradeAvg3() {
        return gradeAvg3;
    }

    public void setGradeAvg3(String gradeAvg3) {
        this.gradeAvg3 = gradeAvg3;

    }

    public boolean isIs_upper_class() {
        return is_upper_class;
    }

    public void setIs_upper_class(boolean is_upper_class) {
        this.is_upper_class = is_upper_class;
    }

    public class StudentList {

        public String getTot1() {
            return tot1;
        }

        public void setTot1(String tot1) {
            this.tot1 = tot1;
        }

        public String getTot2() {
            return tot2;
        }

        public void setTot2(String tot2) {
            this.tot2 = tot2;
        }

        public String getTot3() {
            return tot3;
        }

        public void setTot3(String tot3) {
            this.tot3 = tot3;
        }

        public String getClassAvg1() {
            return classAvg1;
        }

        public void setClassAvg1(String classAvg1) {
            this.classAvg1 = classAvg1;
        }

        public String getClassAvg2() {
            return classAvg2;
        }

        public void setClassAvg2(String classAvg2) {
            this.classAvg2 = classAvg2;
        }

        public String getClassAvg3() {
            return classAvg3;
        }

        public void setClassAvg3(String classAvg3) {
            this.classAvg3 = classAvg3;
        }

        private int sid;
        private String name;
        private String order1;
        private String order2;
        private String order3;
        private String avg1;
        private String avg2;
        private String avg3;
        private String tot1;
        private String tot2;
        private String tot3;
        private String classAvg1;
        private String classAvg2;
        private String classAvg3;
        private String indexNo;
        private List<SubjectList> subList;
        private String className;

        public StudentList(int sid, String name, String indexNo, String order1, String order2, String order3, List<SubjectList> subList, String tot1, String tot2, String tot3, String className, String avg1, String avg2, String avg3, String classAvg1, String classAvg2, String classAvg3) {
            this.sid = sid;
            this.name = name;
            this.indexNo = indexNo;
            this.order1 = order1;
            this.order2 = order2;
            this.order3 = order3;
            this.subList = subList;
            this.avg1 = avg1;
            this.avg2 = avg2;
            this.avg3 = avg3;
            this.className = className;
            this.tot1 = tot1;
            this.tot2 = tot2;
            this.tot3 = tot3;
            this.classAvg1 = classAvg1;
            this.classAvg2 = classAvg2;
            this.classAvg3 = classAvg3;

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

        public String getOrder1() {
            return order1;
        }

        public void setOrder1(String order1) {
            this.order1 = order1;
        }

        public String getOrder2() {
            return order2;
        }

        public void setOrder2(String order2) {
            this.order2 = order2;
        }

        public String getOrder3() {
            return order3;
        }

        public void setOrder3(String order3) {
            this.order3 = order3;
        }

        public String getIndexNo() {
            return indexNo;
        }

        public void setIndexNo(String indexNo) {
            this.indexNo = indexNo;
        }

        public List<SubjectList> getSubList() {
            return subList;
        }

        public void setSubList(List<SubjectList> subList) {
            this.subList = subList;
        }

        public String getAvg1() {
            return avg1;
        }

        public void setAvg1(String avg1) {
            this.avg1 = avg1;
        }

        public String getAvg2() {
            return avg2;
        }

        public void setAvg2(String avg2) {
            this.avg2 = avg2;
        }

        public String getAvg3() {
            return avg3;
        }

        public void setAvg3(String avg3) {
            this.avg3 = avg3;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }

    public class SubjectList {

        private int sibId;
        private String subjectName;
        private String term1_marks;
        private String term2_marks;
        private String term3_marks;

        public SubjectList(int sibId, String subjectName, String term1_marks, String term2_marks, String term3_marks) {

            this.sibId = sibId;
            this.subjectName = subjectName;
            this.term1_marks = term1_marks;
            this.term2_marks = term2_marks;
            this.term3_marks = term3_marks;

        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public int getSibId() {
            return sibId;
        }

        public void setSibId(int sibId) {
            this.sibId = sibId;
        }

        public String getTerm1_marks() {
            return term1_marks;
        }

        public void setTerm1_marks(String term1_marks) {
            this.term1_marks = term1_marks;
        }

        public String getTerm2_marks() {
            return term2_marks;
        }

        public void setTerm2_marks(String term2_marks) {
            this.term2_marks = term2_marks;
        }

        public String getTerm3_marks() {
            return term3_marks;
        }

        public void setTerm3_marks(String term3_marks) {
            this.term3_marks = term3_marks;
        }
    }

}
