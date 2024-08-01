/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.utilities;

import com.webapps.controller.managebeans.StudentLongitudinalDevelopment;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Thilini Madagama
 */
public class SortArraysStudentLongitudinalDevelopment {

    static final Comparator<StudentLongitudinalDevelopment.StudentsList> ORDER_BY_MEAN = new Comparator<StudentLongitudinalDevelopment.StudentsList>() {

        @Override
        public int compare(StudentLongitudinalDevelopment.StudentsList o1, StudentLongitudinalDevelopment.StudentsList o2) {
            return Double.compare(o1.getSelectedYearAVG(), o2.getSelectedYearAVG());
        }
    };

    static final Comparator<StudentLongitudinalDevelopment.StudentsList> ORDER_BY_RULES = new Comparator<StudentLongitudinalDevelopment.StudentsList>() {

        @Override
        public int compare(StudentLongitudinalDevelopment.StudentsList o1, StudentLongitudinalDevelopment.StudentsList o2) {
            int i = ORDER_BY_MEAN.compare(o1, o2);
            return i;
        }
    };

    public static StudentLongitudinalDevelopment.StudentsList[] GetArray(StudentLongitudinalDevelopment.StudentsList[] addressArray) {

        Arrays.sort(addressArray, ORDER_BY_RULES);

        return addressArray;
    }
}
