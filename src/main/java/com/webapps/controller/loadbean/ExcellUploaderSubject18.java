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
public class ExcellUploaderSubject18 {

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
        school = (School) uni.find(37, School.class);
        year = (Year) uni.find(1, Year.class);
        term = (Terms) uni.find(3, Terms.class);
        grade = (Grade) uni.find(11, Grade.class);  // 3
        classes = (Classes) uni.find(1, Classes.class);  // A
        stream = (Streams) uni.find(1, Streams.class); // OL
        fpath = "E:\\Colombo-Office\\NEDP_Files\\st_joseph_boys\\original\\11AB-2017-3_Term.xlsx";

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
            XSSFSheet sheet = workbook.getSheetAt(0);

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
                            if (cell.getColumnIndex() > 1 && cell.getColumnIndex() <= 19) {

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
            XSSFSheet sheet = workbook.getSheetAt(0);

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
                    String sub13 = "";
                    String sub14 = "";
                    String sub15 = "";
                    String sub16 = "";
                    String sub17 = "";

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        cell.getColumnIndex();

                        if (cell.getColumnIndex() == 1) {
                            stu_name = cell.getStringCellValue();

                        }
                        if (cell.getColumnIndex() == 2) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub0 = cell.getNumericCellValue() + "";
                            }
                        }
                        if (cell.getColumnIndex() == 3) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub1 = cell.getNumericCellValue() + "";
                            }
                        }
                        if (cell.getColumnIndex() == 4) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub2 = cell.getNumericCellValue() + "";
                            }
                        }
                        if (cell.getColumnIndex() == 5) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub3 = cell.getNumericCellValue() + "";
                            }
                        }
                        if (cell.getColumnIndex() == 6) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub4 = cell.getNumericCellValue() + "";
                            }
                        }
                        if (cell.getColumnIndex() == 7) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub5 = cell.getNumericCellValue() + "";
                            }
                        }
                        if (cell.getColumnIndex() == 8) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub6 = cell.getNumericCellValue() + "";
                            }
                        }
                        if (cell.getColumnIndex() == 9) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub7 = cell.getNumericCellValue() + "";
                            }
                        }
                        if (cell.getColumnIndex() == 10) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub8 = cell.getNumericCellValue() + "";
                            }
                        }
                        if (cell.getColumnIndex() == 11) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub9 = cell.getNumericCellValue() + "";
                            }
                        }
                        if (cell.getColumnIndex() == 12) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub10 = cell.getNumericCellValue() + "";
                            }
                        }
                        if (cell.getColumnIndex() == 13) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub11 = cell.getNumericCellValue() + "";
                            }
                        }
                        if (cell.getColumnIndex() == 14) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub12 = cell.getNumericCellValue() + "";
                            }
                        }
                        if (cell.getColumnIndex() == 15) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub13 = cell.getNumericCellValue() + "";
                            }
                        }
                        if (cell.getColumnIndex() == 16) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub14 = cell.getNumericCellValue() + "";
                            }
                        }
                        if (cell.getColumnIndex() == 17) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub15 = cell.getNumericCellValue() + "";
                            }
                        }
                        if (cell.getColumnIndex() == 18) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub16 = cell.getNumericCellValue() + "";
                            }
                        }
                        if (cell.getColumnIndex() == 19) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                sub17 = cell.getNumericCellValue() + "";
                            }
                        }

                    }
                    if (!stu_name.equals("")) {
                        System.out.println(stu_name + " | " + sub0);
                        saveMarks(stu_name, sub0, sub1, sub2, sub3, sub4, sub5, sub6, sub7, sub8, sub9, sub10, sub11, sub12, sub13, sub14, sub15, sub16, sub17);
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

    public synchronized void saveMarks(String name, String sub0, String sub1, String sub2, String sub3, String sub4, String sub5, String sub6, String sub7, String sub8, String sub9, String sub10, String sub11, String sub12, String sub13, String sub14, String sub15, String sub16, String sub17) {

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
        if (sub0.equals("")) {
            marks0 = 0.0;
            is_mandatory0 = false;
        } else {
            is_mandatory0 = true;
            marks0 = Double.parseDouble(sub0);
        }

        StudentMarks sm0 = null;
        List<StudentMarks> sm_list0 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(0).getId() + "' ");
        if (sm_list0.size() > 0) {
            sm0 = sm_list0.iterator().next();
            sm0.setIsMandatory(is_mandatory0);
            sm0.setMarks(marks0);
            uni.update(sm0);
        } else {
            sm0 = new StudentMarks();
            sm0.setGradeClassHasSubjectsId(subjects.get(0));
            sm0.setGradeClassStudentsId(gcst);
            sm0.setIsMandatory(is_mandatory0);
            sm0.setMarks(marks0);
            sm0.setTermsId(term);
            uni.create(sm0);
        }

        // sub 1
        double marks1 = 0.0;
        boolean is_mandatory1 = true;
        if (sub1.equals("")) {
            marks1 = 0.0;
            is_mandatory1 = false;
        } else {
            is_mandatory1 = true;
            marks1 = Double.parseDouble(sub1);
        }

        StudentMarks sm1 = null;
        List<StudentMarks> sm_list1 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(1).getId() + "' ");
        if (sm_list1.size() > 0) {
            sm1 = sm_list1.iterator().next();
            sm1.setIsMandatory(is_mandatory1);
            sm1.setMarks(marks1);
            uni.update(sm1);
        } else {
            sm1 = new StudentMarks();
            sm1.setGradeClassHasSubjectsId(subjects.get(1));
            sm1.setGradeClassStudentsId(gcst);
            sm1.setIsMandatory(is_mandatory1);
            sm1.setMarks(marks1);
            sm1.setTermsId(term);
            uni.create(sm1);
        }

        // sub 2
        double marks2 = 0.0;
        boolean is_mandatory2 = true;
        if (sub2.equals("")) {
            marks2 = 0.0;
            is_mandatory2 = false;
        } else {
            is_mandatory2 = true;
            marks2 = Double.parseDouble(sub2);
        }

        StudentMarks sm2 = null;
        List<StudentMarks> sm_list2 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(2).getId() + "' ");
        if (sm_list2.size() > 0) {
            sm2 = sm_list2.iterator().next();
            sm2.setIsMandatory(is_mandatory2);
            sm2.setMarks(marks2);
            uni.update(sm2);
        } else {
            sm2 = new StudentMarks();
            sm2.setGradeClassHasSubjectsId(subjects.get(2));
            sm2.setGradeClassStudentsId(gcst);
            sm2.setIsMandatory(is_mandatory2);
            sm2.setMarks(marks2);
            sm2.setTermsId(term);
            uni.create(sm2);
        }

        // sub 3
        double marks3 = 0.0;
        boolean is_mandatory3 = true;
        if (sub3.equals("")) {
            marks3 = 0.0;
            is_mandatory3 = false;
        } else {
            is_mandatory3 = true;
            marks3 = Double.parseDouble(sub3);
        }

        StudentMarks sm3 = null;
        List<StudentMarks> sm_list3 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(3).getId() + "' ");
        if (sm_list3.size() > 0) {
            sm3 = sm_list3.iterator().next();
            sm3.setIsMandatory(is_mandatory3);
            sm3.setMarks(marks3);
            uni.update(sm3);
        } else {
            sm3 = new StudentMarks();
            sm3.setGradeClassHasSubjectsId(subjects.get(3));
            sm3.setGradeClassStudentsId(gcst);
            sm3.setIsMandatory(is_mandatory3);
            sm3.setMarks(marks3);
            sm3.setTermsId(term);
            uni.create(sm3);
        }

        // sub 4
        double marks4 = 0.0;
        boolean is_mandatory4 = true;
        if (sub4.equals("")) {
            marks4 = 0.0;
            is_mandatory4 = false;
        } else {
            is_mandatory4 = true;
            marks4 = Double.parseDouble(sub4);
        }

        StudentMarks sm4 = null;
        List<StudentMarks> sm_list4 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(4).getId() + "' ");
        if (sm_list4.size() > 0) {
            sm4 = sm_list4.iterator().next();
            sm4.setIsMandatory(is_mandatory4);
            sm4.setMarks(marks4);
            uni.update(sm4);
        } else {
            sm4 = new StudentMarks();
            sm4.setGradeClassHasSubjectsId(subjects.get(4));
            sm4.setGradeClassStudentsId(gcst);
            sm4.setIsMandatory(is_mandatory4);
            sm4.setMarks(marks4);
            sm4.setTermsId(term);
            uni.create(sm4);
        }

        // sub 5
        double marks5 = 0.0;
        boolean is_mandatory5 = true;
        if (sub5.equals("")) {
            marks5 = 0.0;
            is_mandatory5 = false;
        } else {
            is_mandatory5 = true;
            marks5 = Double.parseDouble(sub5);
        }

        StudentMarks sm5 = null;
        List<StudentMarks> sm_list5 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(5).getId() + "' ");
        if (sm_list5.size() > 0) {
            sm5 = sm_list5.iterator().next();
            sm5.setIsMandatory(is_mandatory5);
            sm5.setMarks(marks5);
            uni.update(sm5);
        } else {
            sm5 = new StudentMarks();
            sm5.setGradeClassHasSubjectsId(subjects.get(5));
            sm5.setGradeClassStudentsId(gcst);
            sm5.setIsMandatory(is_mandatory5);
            sm5.setMarks(marks5);
            sm5.setTermsId(term);
            uni.create(sm5);
        }

        // sub 6
        double marks6 = 0.0;
        boolean is_mandatory6 = true;
        if (sub6.equals("")) {
            marks6 = 0.0;
            is_mandatory6 = false;
        } else {
            is_mandatory6 = true;
            marks6 = Double.parseDouble(sub6);
        }

        StudentMarks sm6 = null;
        List<StudentMarks> sm_list6 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(6).getId() + "' ");
        if (sm_list6.size() > 0) {
            sm6 = sm_list6.iterator().next();
            sm6.setIsMandatory(is_mandatory6);
            sm6.setMarks(marks6);
            uni.update(sm6);
        } else {
            sm6 = new StudentMarks();
            sm6.setGradeClassHasSubjectsId(subjects.get(6));
            sm6.setGradeClassStudentsId(gcst);
            sm6.setIsMandatory(is_mandatory6);
            sm6.setMarks(marks6);
            sm6.setTermsId(term);
            uni.create(sm6);
        }

        // sub 7
        double marks7 = 0.0;
        boolean is_mandatory7 = true;
        if (sub7.equals("")) {
            marks7 = 0.0;
            is_mandatory7 = false;
        } else {
            is_mandatory7 = true;
            marks7 = Double.parseDouble(sub7);
        }

        StudentMarks sm7 = null;
        List<StudentMarks> sm_list7 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(7).getId() + "' ");
        if (sm_list7.size() > 0) {
            sm7 = sm_list7.iterator().next();
            sm7.setIsMandatory(is_mandatory7);
            sm7.setMarks(marks7);
            uni.update(sm7);
        } else {
            sm7 = new StudentMarks();
            sm7.setGradeClassHasSubjectsId(subjects.get(7));
            sm7.setGradeClassStudentsId(gcst);
            sm7.setIsMandatory(is_mandatory7);
            sm7.setMarks(marks7);
            sm7.setTermsId(term);
            uni.create(sm7);
        }

        // sub 8
        double marks8 = 0.0;
        boolean is_mandatory8 = true;
        if (sub8.equals("")) {
            marks8 = 0.0;
            is_mandatory8 = false;
        } else {
            is_mandatory8 = true;
            marks8 = Double.parseDouble(sub8);
        }

        StudentMarks sm8 = null;
        List<StudentMarks> sm_list8 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(8).getId() + "' ");
        if (sm_list8.size() > 0) {
            sm8 = sm_list8.iterator().next();
            sm8.setIsMandatory(is_mandatory8);
            sm8.setMarks(marks8);
            uni.update(sm8);
        } else {
            sm8 = new StudentMarks();
            sm8.setGradeClassHasSubjectsId(subjects.get(8));
            sm8.setGradeClassStudentsId(gcst);
            sm8.setIsMandatory(is_mandatory8);
            sm8.setMarks(marks8);
            sm8.setTermsId(term);
            uni.create(sm8);
        }

        // sub 9
        double marks9 = 0.0;
        boolean is_mandatory9 = true;
        if (sub9.equals("")) {
            marks9 = 0.0;
            is_mandatory9 = false;
        } else {
            is_mandatory9 = true;
            marks9 = Double.parseDouble(sub9);
        }

        StudentMarks sm9 = null;
        List<StudentMarks> sm_list9 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(9).getId() + "' ");
        if (sm_list9.size() > 0) {
            sm9 = sm_list9.iterator().next();
            sm9.setIsMandatory(is_mandatory9);
            sm9.setMarks(marks9);
            uni.update(sm9);
        } else {
            sm9 = new StudentMarks();
            sm9.setGradeClassHasSubjectsId(subjects.get(9));
            sm9.setGradeClassStudentsId(gcst);
            sm9.setIsMandatory(is_mandatory9);
            sm9.setMarks(marks9);
            sm9.setTermsId(term);
            uni.create(sm9);
        }

        // sub 10
        double marks10 = 0.0;
        boolean is_mandatory10 = true;
        if (sub10.equals("")) {
            marks10 = 0.0;
            is_mandatory10 = false;
        } else {
            is_mandatory10 = true;
            marks10 = Double.parseDouble(sub10);
        }

        StudentMarks sm10 = null;
        List<StudentMarks> sm_list10 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(10).getId() + "' ");
        if (sm_list10.size() > 0) {
            sm10 = sm_list10.iterator().next();
            sm10.setIsMandatory(is_mandatory10);
            sm10.setMarks(marks10);
            uni.update(sm10);
        } else {
            sm10 = new StudentMarks();
            sm10.setGradeClassHasSubjectsId(subjects.get(10));
            sm10.setGradeClassStudentsId(gcst);
            sm10.setIsMandatory(is_mandatory10);
            sm10.setMarks(marks10);
            sm10.setTermsId(term);
            uni.create(sm10);
        }

        // sub 11
        double marks11 = 0.0;
        boolean is_mandatory11 = true;
        if (sub11.equals("")) {
            marks11 = 0.0;
            is_mandatory11 = false;
        } else {
            is_mandatory11 = true;
            marks11 = Double.parseDouble(sub11);
        }

        StudentMarks sm11 = null;
        List<StudentMarks> sm_list11 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(11).getId() + "' ");
        if (sm_list11.size() > 0) {
            sm11 = sm_list11.iterator().next();
            sm11.setIsMandatory(is_mandatory11);
            sm11.setMarks(marks11);
            uni.update(sm11);
        } else {
            sm11 = new StudentMarks();
            sm11.setGradeClassHasSubjectsId(subjects.get(11));
            sm11.setGradeClassStudentsId(gcst);
            sm11.setIsMandatory(is_mandatory11);
            sm11.setMarks(marks11);
            sm11.setTermsId(term);
            uni.create(sm11);
        }

        // sub 12
        double marks12 = 0.0;
        boolean is_mandatory12 = true;
        if (sub12.equals("")) {
            marks12 = 0.0;
            is_mandatory12 = false;
        } else {
            is_mandatory12 = true;
            marks12 = Double.parseDouble(sub12);
        }

        StudentMarks sm12 = null;
        List<StudentMarks> sm_list12 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(12).getId() + "' ");
        if (sm_list12.size() > 0) {
            sm12 = sm_list12.iterator().next();
            sm12.setIsMandatory(is_mandatory12);
            sm12.setMarks(marks12);
            uni.update(sm12);
        } else {
            sm12 = new StudentMarks();
            sm12.setGradeClassHasSubjectsId(subjects.get(12));
            sm12.setGradeClassStudentsId(gcst);
            sm12.setIsMandatory(is_mandatory12);
            sm12.setMarks(marks12);
            sm12.setTermsId(term);
            uni.create(sm12);
        }
        // sub 13
        double marks13 = 0.0;
        boolean is_mandatory13 = true;
        if (sub13.equals("")) {
            marks13 = 0.0;
            is_mandatory13 = false;
        } else {
            is_mandatory13 = true;
            marks13 = Double.parseDouble(sub13);
        }

        StudentMarks sm13 = null;
        List<StudentMarks> sm_list13 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(13).getId() + "' ");
        if (sm_list13.size() > 0) {
            sm13 = sm_list13.iterator().next();
            sm13.setIsMandatory(is_mandatory13);
            sm13.setMarks(marks13);
            uni.update(sm13);
        } else {
            sm13 = new StudentMarks();
            sm13.setGradeClassHasSubjectsId(subjects.get(13));
            sm13.setGradeClassStudentsId(gcst);
            sm13.setIsMandatory(is_mandatory13);
            sm13.setMarks(marks13);
            sm13.setTermsId(term);
            uni.create(sm13);
        }

        // sub 14
        double marks14 = 0.0;
        boolean is_mandatory14 = true;
        if (sub14.equals("")) {
            marks14 = 0.0;
            is_mandatory14 = false;
        } else {
            is_mandatory14 = true;
            marks14 = Double.parseDouble(sub14);
        }

        StudentMarks sm14 = null;
        List<StudentMarks> sm_list14 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(14).getId() + "' ");
        if (sm_list14.size() > 0) {
            sm14 = sm_list14.iterator().next();
            sm14.setIsMandatory(is_mandatory14);
            sm14.setMarks(marks14);
            uni.update(sm14);
        } else {
            sm14 = new StudentMarks();
            sm14.setGradeClassHasSubjectsId(subjects.get(14));
            sm14.setGradeClassStudentsId(gcst);
            sm14.setIsMandatory(is_mandatory14);
            sm14.setMarks(marks14);
            sm14.setTermsId(term);
            uni.create(sm14);
        }
        // sub 15
        double marks15 = 0.0;
        boolean is_mandatory15 = true;
        if (sub15.equals("")) {
            marks15 = 0.0;
            is_mandatory15 = false;
        } else {
            is_mandatory15 = true;
            marks15 = Double.parseDouble(sub15);
        }

        StudentMarks sm15 = null;
        List<StudentMarks> sm_list15 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(15).getId() + "' ");
        if (sm_list15.size() > 0) {
            sm15 = sm_list15.iterator().next();
            sm15.setIsMandatory(is_mandatory15);
            sm15.setMarks(marks15);
            uni.update(sm15);
        } else {
            sm15 = new StudentMarks();
            sm15.setGradeClassHasSubjectsId(subjects.get(15));
            sm15.setGradeClassStudentsId(gcst);
            sm15.setIsMandatory(is_mandatory15);
            sm15.setMarks(marks15);
            sm15.setTermsId(term);
            uni.create(sm15);
        }
        // sub 16
        double marks16 = 0.0;
        boolean is_mandatory16 = true;
        if (sub16.equals("")) {
            marks16 = 0.0;
            is_mandatory16 = false;
        } else {
            is_mandatory16 = true;
            marks16 = Double.parseDouble(sub16);
        }

        StudentMarks sm16 = null;
        List<StudentMarks> sm_list16 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(16).getId() + "' ");
        if (sm_list16.size() > 0) {
            sm16 = sm_list16.iterator().next();
            sm16.setIsMandatory(is_mandatory16);
            sm16.setMarks(marks16);
            uni.update(sm16);
        } else {
            sm16 = new StudentMarks();
            sm16.setGradeClassHasSubjectsId(subjects.get(16));
            sm16.setGradeClassStudentsId(gcst);
            sm16.setIsMandatory(is_mandatory16);
            sm16.setMarks(marks16);
            sm16.setTermsId(term);
            uni.create(sm16);
        }
        // sub 17
        double marks17 = 0.0;
        boolean is_mandatory17 = true;
        if (sub17.equals("")) {
            marks17 = 0.0;
            is_mandatory17 = false;
        } else {
            is_mandatory17 = true;
            marks17 = Double.parseDouble(sub17);
        }

        StudentMarks sm17 = null;
        List<StudentMarks> sm_list17 = uni.searchByQuerySingle("SELECT im FROM StudentMarks im WHERE im.gradeClassStudentsId.id='" + gcst.getId() + "' and im.termsId.id='" + term.getId() + "' and im.gradeClassHasSubjectsId.id='" + subjects.get(17).getId() + "' ");
        if (sm_list17.size() > 0) {
            sm17 = sm_list17.iterator().next();
            sm17.setIsMandatory(is_mandatory17);
            sm17.setMarks(marks17);
            uni.update(sm17);
        } else {
            sm17 = new StudentMarks();
            sm17.setGradeClassHasSubjectsId(subjects.get(17));
            sm17.setGradeClassStudentsId(gcst);
            sm17.setIsMandatory(is_mandatory17);
            sm17.setMarks(marks17);
            sm17.setTermsId(term);
            uni.create(sm17);
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
