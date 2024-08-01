/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.model.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author Thilini Madagama
 */
@Stateless
public class StoredProcedures {

    @Resource(lookup = "java:app/ds_education_db")
    private javax.sql.DataSource dataSource;

    @PersistenceContext(unitName = "com.mycompany_contex_war_1.0-SNAPSHOTPU")
    EntityManager em;

    public double get_all_province_mean(int yearval, int termval) {
        Double mean = 0.0;
        try {

            StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("get_all_province_mean");
// set parameters
            storedProcedure.registerStoredProcedureParameter("termval", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("yearval", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("mean", Double.class, ParameterMode.OUT);
            storedProcedure.setParameter("termval", termval);
            storedProcedure.setParameter("yearval", yearval);
// execute SP
            storedProcedure.execute();
// get result
            mean = (Double) storedProcedure.getOutputParameterValue("mean");

        } catch (Exception e) {
            System.out.println("e is>>>>>>>>>>>>>>>>>>>>>>>>>" + e);
        }
        return mean;
    }

    public double get_selected_province_mean(int yearval, int termval, int province) {
        Double mean = 0.0;
        try {

            StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("get_selected_province_mean");
// set parameters
            storedProcedure.registerStoredProcedureParameter("termval", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("yearval", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("province", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("mean", Double.class, ParameterMode.OUT);
            storedProcedure.setParameter("termval", termval);
            storedProcedure.setParameter("yearval", yearval);
            storedProcedure.setParameter("province", province);
// execute SP
            storedProcedure.execute();
// get result
            mean = (Double) storedProcedure.getOutputParameterValue("mean");

        } catch (Exception e) {
            System.out.println("e is>>>>>>>>>>>>>>>>>>>>>>>>>" + e);
        }
        return mean;
    }

    public double get_selected_zone_mean(int yearval, int termval, int zone) {
        Double mean = 0.0;
        try {

            StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("get_selected_zone_mean");
// set parameters
            storedProcedure.registerStoredProcedureParameter("termval", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("yearval", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("zone", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("mean", Double.class, ParameterMode.OUT);
            storedProcedure.setParameter("termval", termval);
            storedProcedure.setParameter("yearval", yearval);
            storedProcedure.setParameter("zone", zone);
// execute SP
            storedProcedure.execute();
// get result
            mean = (Double) storedProcedure.getOutputParameterValue("mean");

        } catch (Exception e) {
            System.out.println("e is>>>>>>>>>>>>>>>>>>>>>>>>>" + e);
        }
        return mean;
    }

    public double get_selected_division_mean(int yearval, int termval, int division) {
        Double mean = 0.0;
        try {

            StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("get_selected_division_mean");
// set parameters
            storedProcedure.registerStoredProcedureParameter("termval", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("yearval", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("division", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("mean", Double.class, ParameterMode.OUT);
            storedProcedure.setParameter("termval", termval);
            storedProcedure.setParameter("yearval", yearval);
            storedProcedure.setParameter("division", division);
// execute SP
            storedProcedure.execute();
// get result
            mean = (Double) storedProcedure.getOutputParameterValue("mean");

        } catch (Exception e) {
            System.out.println("e is>>>>>>>>>>>>>>>>>>>>>>>>>" + e);
        }
        return mean;
    }

    public double get_selected_school_mean(int yearval, int termval, int school) {
        Double mean = 0.0;
        try {

            StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("get_selected_school_mean");
// set parameters
            storedProcedure.registerStoredProcedureParameter("termval", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("yearval", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("school", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("mean", Double.class, ParameterMode.OUT);
            storedProcedure.setParameter("termval", termval);
            storedProcedure.setParameter("yearval", yearval);
            storedProcedure.setParameter("school", school);
// execute SP
            storedProcedure.execute();
// get result
            mean = (Double) storedProcedure.getOutputParameterValue("mean");

        } catch (Exception e) {
            System.out.println("e is>>>>>>>>>>>>>>>>>>>>>>>>>" + e);
        }
        return mean;
    }

    public int get_data_entered_tot_count(String yearval, int termval, int school) {
        int count = 0;
        try {
            System.out.println(yearval + "|" + termval + "|" + school);
            StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("get_data_entered_student_count");
// set parameters
            storedProcedure.registerStoredProcedureParameter("termval", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("yearval", String.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("school", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("stuCount", Integer.class, ParameterMode.OUT);
            storedProcedure.setParameter("termval", termval);
            storedProcedure.setParameter("yearval", yearval);
            storedProcedure.setParameter("school", school);
// execute SP
            storedProcedure.execute();
// get result
            count = (Integer) storedProcedure.getOutputParameterValue("stuCount");

        } catch (Exception e) {
            System.out.println("e is>>>>>>>>>>>>>>>>>>>>>>>>>" + e);
        }
        return count;
    }

    public double get_selected_teacher_mean(int yearval, int termval, int school, int teacher, int grade, int subjectval) {
        Double mean = 0.0;
        try {

            StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("get_selected_teacher_mean");
// set parameters
            storedProcedure.registerStoredProcedureParameter("termval", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("yearval", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("school", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("teacher", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("grade", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("subjectval", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("mean", Double.class, ParameterMode.OUT);
            storedProcedure.setParameter("termval", termval);
            storedProcedure.setParameter("yearval", yearval);
            storedProcedure.setParameter("school", school);
            storedProcedure.setParameter("teacher", teacher);
            storedProcedure.setParameter("grade", grade);
            storedProcedure.setParameter("subjectval", subjectval);
// execute SP
            storedProcedure.execute();
// get result
            mean = (Double) storedProcedure.getOutputParameterValue("mean");

        } catch (Exception e) {
            System.out.println("e is>>>>>>>>>>>>>>>>>>>>>>>>>" + e);
        }
        return mean;
    }

    public double get_selected_class_subject_mean(int yearval, int termval, int gcsm, int subject) {
        Double mean = 0.0;
        try {

            StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("get_selected_class_subject_mean");
// set parameters
            storedProcedure.registerStoredProcedureParameter("termval", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("yearval", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("gcsmval", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("subjectval", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("mean", Double.class, ParameterMode.OUT);
            storedProcedure.setParameter("termval", termval);
            storedProcedure.setParameter("yearval", yearval);
            storedProcedure.setParameter("gcsmval", gcsm);
            storedProcedure.setParameter("subjectval", subject);
// execute SP
            storedProcedure.execute();
// get result
            mean = (Double) storedProcedure.getOutputParameterValue("mean");

        } catch (Exception e) {
            System.out.println("e is>>>>>>>>>>>>>>>>>>>>>>>>>" + e);
        }
        return mean;
    }

    public List<Object> get_teacher_attendance(String date, String teacherType, String attendanceType) {
        StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("getDailyTeacherAttendanceReport");

        storedProcedure.registerStoredProcedureParameter("date", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("teacherType", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("attendanceType", String.class, ParameterMode.IN);

        storedProcedure.setParameter("date", date);
        storedProcedure.setParameter("teacherType", teacherType);
        storedProcedure.setParameter("attendanceType", attendanceType);

        storedProcedure.execute();

        List<Object> rs = storedProcedure.getResultList();

        if (rs.size() > 0) {
            return rs;
        } else {
            return null;
        }
    }

    public List<String[]> get_All_teachers_Monthly_Attendance(Date date) {
        StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("getAllTeachersMonthlyAttendanceOverview");

        storedProcedure.registerStoredProcedureParameter("selectedDate", java.sql.Date.class, ParameterMode.IN);

        storedProcedure.setParameter("selectedDate", new java.sql.Date(date.getTime()));

        storedProcedure.execute();

        List<Object[]> rs = storedProcedure.getResultList();
        List<String[]> stringArrayList = new ArrayList<String[]>();

        for (Object[] row : rs) {
            String[] stringRow = new String[row.length];
            for (int i = 0; i < row.length; i++) {
                stringRow[i] = row[i] != null ? row[i].toString() : null;
            }
            stringArrayList.add(stringRow);
        }

        if (stringArrayList.size() > 0) {
            return stringArrayList;
        } else {
            return null;
        }
    }

}
