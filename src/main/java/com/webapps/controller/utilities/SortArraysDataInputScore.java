/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.utilities;

import com.webapps.controller.managebeans.DataInputScore;
import com.webapps.controller.managebeans.ReportCardGenerator;
import com.webapps.controller.managebeans.TeacherPerformanceAnalyzer;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Thilini Madagama
 */
public class SortArraysDataInputScore {

    static final Comparator<DataInputScore.SchoolNameList> ORDER_BY_MEAN = new Comparator<DataInputScore.SchoolNameList>() {

        @Override
        public int compare(DataInputScore.SchoolNameList o1, DataInputScore.SchoolNameList o2) {
            return Double.compare(o1.getMarksEnter(), o2.getMarksEnter());
        }
    };

    static final Comparator<DataInputScore.SchoolNameList> ORDER_BY_RULES = new Comparator<DataInputScore.SchoolNameList>() {

        @Override
        public int compare(DataInputScore.SchoolNameList o1, DataInputScore.SchoolNameList o2) {
            int i = ORDER_BY_MEAN.compare(o1, o2);
            return i;
        }
    };

    public static DataInputScore.SchoolNameList[] GetArray(DataInputScore.SchoolNameList[] addressArray) {

        Arrays.sort(addressArray, ORDER_BY_RULES);

        return addressArray;
    }
}
