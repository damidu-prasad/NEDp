/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.utilities;

import com.webapps.controller.managebeans.ReportCardGenerator;
import com.webapps.controller.managebeans.StudentMarksReport;
import com.webapps.controller.managebeans.TeacherPerformanceAnalyzer;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Thilini Madagama
 */
public class SortArraysStudentMarksReport {

    static final Comparator<StudentMarksReport.StudentOrderList> ORDER_BY_MEAN = new Comparator<StudentMarksReport.StudentOrderList>() {

        @Override
        public int compare(StudentMarksReport.StudentOrderList o1, StudentMarksReport.StudentOrderList o2) {
            return Double.compare(o1.getTotal(), o2.getTotal());
        }
    };

    static final Comparator<StudentMarksReport.StudentOrderList> ORDER_BY_RULES = new Comparator<StudentMarksReport.StudentOrderList>() {

        @Override
        public int compare(StudentMarksReport.StudentOrderList o1, StudentMarksReport.StudentOrderList o2) {
            int i = ORDER_BY_MEAN.compare(o1, o2);
            return i;
        }
    };

    public static StudentMarksReport.StudentOrderList[] GetArray(StudentMarksReport.StudentOrderList[] addressArray) {

        Arrays.sort(addressArray, ORDER_BY_RULES);

        return addressArray;
    }
}
