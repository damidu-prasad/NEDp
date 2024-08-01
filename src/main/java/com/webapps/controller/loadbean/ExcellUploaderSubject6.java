/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.loadbean;

import com.ejb.model.common.UniDBLocal;
import com.ejb.model.entity.Classes;
import com.ejb.model.entity.EducationLevel;
import com.ejb.model.entity.GeneralUserProfile;
import com.ejb.model.entity.Grade;
import com.ejb.model.entity.GradeClassHasSubjects;
import com.ejb.model.entity.GradeClassStream;
import com.ejb.model.entity.GradeClassStreamManager;
import com.ejb.model.entity.GradeClassStudents;
import com.ejb.model.entity.School;
import com.ejb.model.entity.Streams;
import com.ejb.model.entity.StudentMarks;
import com.ejb.model.entity.Students;
import com.ejb.model.entity.Subjects;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.Terms;
import com.ejb.model.entity.Year;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Thilini Madagama
 */
@ManagedBean
@ViewScoped
public class ExcellUploaderSubject6 {

    private List<GradeClassHasSubjects> subjects = new ArrayList();

    private School school;
    private Year year;
    private Terms term;
    private Grade grade;
    private Classes classes;
    private Streams stream;
    private GradeClassStream gcs;
    private GradeClassStreamManager gcsm;
    private String fpath;

    @EJB
    UniDBLocal uni;

    public void initial() {
        school = (School) uni.find(75, School.class);
        year = (Year) uni.find(1, Year.class);
        term = (Terms) uni.find(3, Terms.class);
        grade = (Grade) uni.find(5, Grade.class);  // 3
        classes = (Classes) uni.find(1, Classes.class);  // A
        stream = (Streams) uni.find(1, Streams.class); // OL
        fpath = "E:\\Colombo-Office\\NEDP_Files\\SWRDBandaranayake_Vidyalaya\\Grade 3 4 5.xlsx";

        saveGradeClassManager();
    }

    public void saveGradeClassManager() {

        List<GradeClassStream> gop_list1 = uni.searchByQuerySingle("SELECT im FROM GradeClassStream im WHERE im.gradeId.id='" + grade.getId() + "' and im.schoolId.id='" + school.getId() + "' and im.classesId.id='" + classes.getId() + "' and im.streamsId.id='" + getStream().getId() + "' ");
        if (gop_list1.size() > 0) {
            gcs = gop_list1.iterator().next();

        } else {
            gcs = new GradeClassStream();
            gcs.setClassesId(classes);
            gcs.setGradeId(grade);
            gcs.setSchoolId(school);
            gcs.setStreamsId(getStream());
            uni.create(gcs);
        }

        List<GradeClassStreamManager> gop_list = uni.searchByQuerySingle("SELECT im FROM GradeClassStreamManager im WHERE im.yearId.id='" + year.getId() + "' and im.gradeClassStreamId.id='" + gcs.getId() + "'  ");
        if (gop_list.size() > 0) {
            gcsm = gop_list.iterator().next();

        } else {
            gcsm = new GradeClassStreamManager();
            gcsm.setGradeClassStreamId(gcs);
            gcsm.setYearId(year);
            uni.create(gcsm);
        }
        addSubjects();

    }

    public void addSubjects() {
        try {

            String path = String.valueOf(getFpath());
            FileInputStream file = new FileInputStream(new File(path));

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(2);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            int x = 0;

            while (rowIterator.hasNext()) {
                x++;
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                //  System.out.println(row.getRowNum());

                if (row.getRowNum() > 4) {
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        cell.getColumnIndex();
                        if (row.getRowNum() == 5) {
                            if (cell.getColumnIndex() > 1 && cell.getColumnIndex() <= 7) {

                                Subjects sub;
                                List<Subjects> cl_list = uni.searchByQuerySingle("SELECT im FROM Subjects im WHERE im.name='" + cell.getStringCellValue() + "'");
                                if (cl_list.size() > 0) {
                                    sub = cl_list.iterator().next();
                                } else {

                                    int education_level = 2;
                                    if (grade.getId() == 12 || grade.getId() == 13) {
                                        education_level = 3;

                                    }

                                    sub = new Subjects();
                                    sub.setName(cell.getStringCellValue());
                                    sub.setCode("");
                                    sub.setEducationLevelId((EducationLevel) uni.find(education_level, EducationLevel.class));
                                    sub.setIsActive(1);

                                    uni.create(sub);
                                }
                                GradeClassHasSubjects gchs = null;
                                List<GradeClassHasSubjects> sub_list = uni.searchByQuery("SELECT im FROM GradeClassHasSubjects im WHERE im.gradeClassStreamId.id='" + gcs.getId() + "' and im.subjectsId.id='" + sub.getId() + "' ");
                                if (sub_list.size() > 0) {

                                    gchs = sub_list.iterator().next();
                                    gchs.setIsActive(1);
                                    uni.update(gchs);

                                } else {

                                    gchs = new GradeClassHasSubjects();
                                    gchs.setGradeClassStreamId(gcs);
                                    gchs.setIsActive(1);
                                    gchs.setSubjectsId(sub);
                                    uni.create(gchs);

                                }
                                subjects.add(gchs);

                            }
                        }

                    }

                    // code
                }

            }
//            System.out.println(getSubjects().toString());
//           
//for(Integer in : subjects){
//
//    System.out.println("in "+in);
//}
            file.close();
            upload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void upload() {

        try {

            String path = String.valueOf(getFpath());
            FileInputStream file = new FileInputStream(new File(path));

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(2);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            int x = 0;

            while (rowIterator.hasNext()) {
                x++;
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                //  System.out.println(row.getRowNum());

                if (row.getRowNum() > 5) {
                    Iterator<Cell> cellIterator = row.cellIterator();

                    String stu_name = "";
                    String sub0 = "";
                    String sub1 = "";
                    String sub2 = "";
                    String sub3 = "";
                    String sub4 = "";
                    String sub5 = "";
                    String sub6 = "";
                    String sub7 = "";
                    String sub8 = "";
                    String sub9 = "";
                    String sub10 = "";
                    String sub11 = "";
                    String sub12 = "";

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        cell.getColumnIndex();

                        if (cell.getColumnIndex() == 1) {
                            stu_name = cell.getStringCellValue();

                        }
                        if (cell.getColumnIndex() == 2) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub0 = cell.getNumericCellValue() + "";
                            } else {

                                sub0 = cell.getStringCellValue();
                            }
                        }
                        if (cell.getColumnIndex() == 3) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub1 = cell.getNumericCellValue() + "";
                            } else {

                                sub1 = cell.getStringCellValue();
                            }
                        }
                        if (cell.getColumnIndex() == 4) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub2 = cell.getNumericCellValue() + "";
                            } else {

                                sub2 = cell.getStringCellValue();
                            }
                        }
                        if (cell.getColumnIndex() == 5) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub3 = cell.getNumericCellValue() + "";
                            } else {

                                sub3 = cell.getStringCellValue();
                            }
                        }
                        if (cell.getColumnIndex() == 6) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub4 = cell.getNumericCellValue() + "";
                            } else {

                                sub4 = cell.getStringCellValue();
                            }
                        }
                        if (cell.getColumnIndex() == 7) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub5 = cell.getNumericCellValue() + "";
                            } else {

                                sub5 = cell.getStringCellValue();
                            }
                        }
                        if (cell.getColumnIndex() == 8) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub6 = cell.getNumericCellValue() + "";
                            } else {

                                sub6 = cell.getStringCellValue();
                            }
                        }
                        if (cell.getColumnIndex() == 9) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub7 = cell.getNumericCellValue() + "";
                            } else {

                                sub7 = cell.getStringCellValue();
                            }
                        }
                        if (cell.getColumnIndex() == 10) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub8 = cell.getNumericCellValue() + "";
                            } else {

                                sub8 = cell.getStringCellValue();
                            }
                        }
                        if (cell.getColumnIndex() == 11) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub9 = cell.getNumericCellValue() + "";
                            } else {

                                sub9 = cell.getStringCellValue();
                            }
                        }
                        if (cell.getColumnIndex() == 12) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub10 = cell.getNumericCellValue() + "";
                            } else {

                                sub10 = cell.getStringCellValue();
                            }
                        }
                        if (cell.getColumnIndex() == 13) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub11 = cell.getNumericCellValue() + "";
                            } else {

                                sub11 = cell.getStringCellValue();
                            }
                        }
                        if (cell.getColumnIndex() == 14) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub12 = cell.getNumericCellValue() + "";
                            } else {

                                sub12 = cell.getStringCellValue();
                            }
                        }

                    }
                    if (!stu_name.equals("")) {
                        System.out.println(stu_name + " | " + sub0);
                        saveMarks(stu_name, sub0, sub1, sub2, sub3, sub4, sub5, sub6, sub7, sub8, sub9, sub10, sub11, sub12);
                    }
                    // code
                }

            }
//            System.out.println(getSubjects().toString());
//           
//for(Integer in : subjects){
//
//    System.out.println("in "+in);
//}
            file.close();

            // System.out.println(a.length);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public synchronized void saveMarks(String name, String sub0, String sub1, String sub2, String sub3, String sub4, String sub5, String sub6, String sub7, String sub8, String sub9, String sub10, String sub11, String sub12) {

        Students st = null;
        List<Students> cl_list = uni.searchByQuerySingle("SELECT im FROM Students im WHERE im.generalUserProfileId.nameWithIn='" + name + "' and im.schoolId.id='" + school.getId() + "' ");
        if (cl_list.size() > 0) {
            st = cl_list.iterator().next();
        } else {
            GeneralUserProfile gup = null;
            List<GeneralUserProfile> gup_list = uni.searchByQuerySingle("SELECT im FROM GeneralUserProfile im WHERE im.nameWithIn='" + name + "' and im.generalOrganizationProfileIdGop.id='" + school.getGeneralOrganizationProfileId().getId() + "' ");
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
                uni.create(gup);

            }

            st = new Students();
            st.setGeneralUserProfileId(gup);
            st.setSchoolId(school);
            st.setStudentId("");
            uni.create(st);

        }

        GradeClassStudents gcst = null;
        List<GradeClassStudents> gcst_list = uni.searchByQuerySingle("SELECT im FROM GradeClassStudents im WHERE im.gradeClassStreamManagerId.id='" + gcsm.getId() + "' and im.studentsId.id='" + st.getId() + "' ");
        if (gcst_list.size() > 0) {
            gcst = gcst_list.iterator().next();
        } else {

            gcst = new GradeClassStudents();
            gcst.setGradeClassStreamManagerId(gcsm);
            gcst.setStudentsId(st);
            uni.create(gcst);
        }

        // sub 0
        double marks0 = 0.0;
        boolean is_mandatory0 = true;
        boolean is_present0 = true;
        if (sub0.equals("") || sub0.equals("-")) {
            marks0 = 0.0;
            is_mandatory0 = false;
            is_present0 = true;
        } else if (sub0.equals("ab")) {
            marks0 = 0.0;
            is_mandatory0 = true;
            is_present0 = false;
        } else {
            is_mandatory0 = true;
            marks0 = Double.parseDouble(sub0);
            is_present0 = true;
        }

        StudentMarks sm0 = null;
        List<StudentMarks> sm_list0 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(0).getId() + "' ");
        if (sm_list0.size() > 0) {
            sm0 = sm_list0.iterator().next();
            sm0.setIsMandatory(is_mandatory0);
            sm0.setMarks(marks0);
            sm0.setIsPresent(is_present0);
            uni.update(sm0);
        } else {
            sm0 = new StudentMarks();
            sm0.setGradeClassHasSubjectsId(subjects.get(0));
            sm0.setGradeClassStudentsId(gcst);
            sm0.setIsMandatory(is_mandatory0);
            sm0.setMarks(marks0);
            sm0.setTermsId(term);
            sm0.setIsPresent(is_present0);
            uni.create(sm0);
        }

        // sub 1
        double marks1 = 0.0;
        boolean is_mandatory1 = true;
        boolean is_present1 = true;
        if (sub1.equals("") || sub1.equals("-")) {
            marks1 = 0.0;
            is_mandatory1 = false;
            is_present1 = true;
        } else if (sub1.equals("ab")) {
            marks1 = 0.0;
            is_mandatory1 = true;
            is_present1 = false;
        } else {
            is_mandatory1 = true;
            marks1 = Double.parseDouble(sub1);
            is_present1 = true;
        }

        StudentMarks sm1 = null;
        List<StudentMarks> sm_list1 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(1).getId() + "' ");
        if (sm_list1.size() > 0) {
            sm1 = sm_list1.iterator().next();
            sm1.setIsMandatory(is_mandatory1);
            sm1.setMarks(marks1);
            sm1.setIsPresent(is_present1);
            uni.update(sm1);
        } else {
            sm1 = new StudentMarks();
            sm1.setGradeClassHasSubjectsId(subjects.get(1));
            sm1.setGradeClassStudentsId(gcst);
            sm1.setIsMandatory(is_mandatory1);
            sm1.setMarks(marks1);
            sm1.setTermsId(term);
            sm1.setIsPresent(is_present1);
            uni.create(sm1);
        }

        // sub 2
        double marks2 = 0.0;
        boolean is_mandatory2 = true;
        boolean is_present2 = true;
        if (sub2.equals("") || sub2.equals("-")) {
            marks2 = 0.0;
            is_mandatory2 = false;
            is_present2 = true;
        } else if (sub2.equals("ab")) {
            marks2 = 0.0;
            is_mandatory2 = true;
            is_present2 = false;
        } else {
            is_mandatory2 = true;
            marks2 = Double.parseDouble(sub2);
            is_present2 = true;
        }

        StudentMarks sm2 = null;
        List<StudentMarks> sm_list2 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(2).getId() + "' ");
        if (sm_list2.size() > 0) {
            sm2 = sm_list2.iterator().next();
            sm2.setIsMandatory(is_mandatory2);
            sm2.setMarks(marks2);
            sm2.setIsPresent(is_present2);
            uni.update(sm2);
        } else {
            sm2 = new StudentMarks();
            sm2.setGradeClassHasSubjectsId(subjects.get(2));
            sm2.setGradeClassStudentsId(gcst);
            sm2.setIsMandatory(is_mandatory2);
            sm2.setMarks(marks2);
            sm2.setTermsId(term);
            sm2.setIsPresent(is_present2);
            uni.create(sm2);
        }

        // sub 3
        double marks3 = 0.0;
        boolean is_mandatory3 = true;
        boolean is_present3 = true;
        if (sub3.equals("") || sub3.equals("-")) {
            marks3 = 0.0;
            is_mandatory3 = false;
            is_present3 = true;
        } else if (sub3.equals("ab")) {
            marks3 = 0.0;
            is_mandatory3 = true;
            is_present3 = false;
        } else {
            is_mandatory3 = true;
            marks3 = Double.parseDouble(sub3);
            is_present3 = true;
        }

        StudentMarks sm3 = null;
        List<StudentMarks> sm_list3 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(3).getId() + "' ");
        if (sm_list3.size() > 0) {
            sm3 = sm_list3.iterator().next();
            sm3.setIsMandatory(is_mandatory3);
            sm3.setMarks(marks3);
            sm3.setIsPresent(is_present3);
            uni.update(sm3);
        } else {
            sm3 = new StudentMarks();
            sm3.setGradeClassHasSubjectsId(subjects.get(3));
            sm3.setGradeClassStudentsId(gcst);
            sm3.setIsMandatory(is_mandatory3);
            sm3.setMarks(marks3);
            sm3.setTermsId(term);
            sm3.setIsPresent(is_present3);
            uni.create(sm3);
        }

        // sub 4
        double marks4 = 0.0;
        boolean is_mandatory4 = true;
        boolean is_present4 = true;
        if (sub4.equals("") || sub4.equals("-")) {
            marks4 = 0.0;
            is_mandatory4 = false;
            is_present4 = true;
        } else if (sub4.equals("ab")) {
            marks4 = 0.0;
            is_mandatory4 = true;
            is_present4 = false;
        } else {
            is_mandatory4 = true;
            marks4 = Double.parseDouble(sub4);
            is_present4 = true;
        }

        StudentMarks sm4 = null;
        List<StudentMarks> sm_list4 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(4).getId() + "' ");
        if (sm_list4.size() > 0) {
            sm4 = sm_list4.iterator().next();
            sm4.setIsMandatory(is_mandatory4);
            sm4.setMarks(marks4);
            sm4.setIsPresent(is_present4);
            uni.update(sm4);
        } else {
            sm4 = new StudentMarks();
            sm4.setGradeClassHasSubjectsId(subjects.get(4));
            sm4.setGradeClassStudentsId(gcst);
            sm4.setIsMandatory(is_mandatory4);
            sm4.setMarks(marks4);
            sm4.setTermsId(term);
            sm4.setIsPresent(is_present4);
            uni.create(sm4);
        }

        // sub 5
        double marks5 = 0.0;
        boolean is_mandatory5 = true;
        boolean is_present5 = true;
        if (sub5.equals("") || sub5.equals("-")) {
            marks5 = 0.0;
            is_mandatory5 = false;
            is_present5 = true;
        } else if (sub5.equals("ab")) {
            marks5 = 0.0;
            is_mandatory5 = true;
            is_present5 = false;
        } else {
            is_mandatory5 = true;
            marks5 = Double.parseDouble(sub5);
            is_present5 = true;
        }

        StudentMarks sm5 = null;
        List<StudentMarks> sm_list5 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(5).getId() + "' ");
        if (sm_list5.size() > 0) {
            sm5 = sm_list5.iterator().next();
            sm5.setIsMandatory(is_mandatory5);
            sm5.setMarks(marks5);
            sm5.setIsPresent(is_present5);
            uni.update(sm5);
        } else {
            sm5 = new StudentMarks();
            sm5.setGradeClassHasSubjectsId(subjects.get(5));
            sm5.setGradeClassStudentsId(gcst);
            sm5.setIsMandatory(is_mandatory5);
            sm5.setMarks(marks5);
            sm5.setTermsId(term);
            sm5.setIsPresent(is_present5);
            uni.create(sm5);
        }

        System.out.println("ok");

    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public Terms getTerm() {
        return term;
    }

    public void setTerm(Terms term) {
        this.term = term;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Classes getClasses() {
        return classes;
    }

    public void setClasses(Classes classes) {
        this.classes = classes;
    }

    public GradeClassStreamManager getGcsm() {
        return gcsm;
    }

    public void setGcsm(GradeClassStreamManager gcsm) {
        this.gcsm = gcsm;
    }

    public String getFpath() {
        return fpath;
    }

    public void setFpath(String fpath) {
        this.fpath = fpath;
    }

    public Streams getStream() {
        return stream;
    }

    public void setStream(Streams stream) {
        this.stream = stream;
    }

    public GradeClassStream getGcs() {
        return gcs;
    }

    public void setGcs(GradeClassStream gcs) {
        this.gcs = gcs;
    }

    public List<GradeClassHasSubjects> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<GradeClassHasSubjects> subjects) {
        this.subjects = subjects;
    }

}
