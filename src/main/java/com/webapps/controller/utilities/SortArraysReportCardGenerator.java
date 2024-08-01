/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.utilities;

import com.webapps.controller.managebeans.ReportCardGenerator;
import com.webapps.controller.managebeans.TeacherPerformanceAnalyzer;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Thilini Madagama
 */
public class SortArraysReportCardGenerator {

    static final Comparator<ReportCardGenerator.StudentList> ORDER_BY_MEAN = new Comparator<ReportCardGenerator.StudentList>() {

        @Override
        public int compare(ReportCardGenerator.StudentList o1, ReportCardGenerator.StudentList o2) {
            return Double.compare(o1.getAvg(), o2.getAvg());
        }
    };

    static final Comparator<ReportCardGenerator.StudentList> ORDER_BY_RULES = new Comparator<ReportCardGenerator.StudentList>() {

        @Override
        public int compare(ReportCardGenerator.StudentList o1, ReportCardGenerator.StudentList o2) {
            int i = ORDER_BY_MEAN.compare(o1, o2);
            return i;
        }
    };

    public static ReportCardGenerator.StudentList[] GetArray(ReportCardGenerator.StudentList[] addressArray) {

        Arrays.sort(addressArray, ORDER_BY_RULES);

        return addressArray;
    }
}
