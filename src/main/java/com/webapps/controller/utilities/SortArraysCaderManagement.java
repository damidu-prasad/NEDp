/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.utilities;

import com.webapps.controller.managebeans.CaderManagement;
import com.webapps.controller.managebeans.DataInputScore;
import com.webapps.controller.managebeans.ReportCardGenerator;
import com.webapps.controller.managebeans.TeacherPerformanceAnalyzer;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Thilini Madagama
 */
public class SortArraysCaderManagement {

    static final Comparator<CaderManagement.TeacherList> ORDER_BY_MEAN = new Comparator<CaderManagement.TeacherList>() {

        @Override
        public int compare(CaderManagement.TeacherList o1, CaderManagement.TeacherList o2) {
            return Double.compare(o1.getNotAllocatedTeachers(), o2.getNotAllocatedTeachers());
        }
    };

    static final Comparator<CaderManagement.TeacherList> ORDER_BY_RULES = new Comparator<CaderManagement.TeacherList>() {

        @Override
        public int compare(CaderManagement.TeacherList o1, CaderManagement.TeacherList o2) {
            int i = ORDER_BY_MEAN.compare(o1, o2);
            return i;
        }
    };

    public static CaderManagement.TeacherList[] GetArray(CaderManagement.TeacherList[] addressArray) {

        Arrays.sort(addressArray, ORDER_BY_RULES);

        return addressArray;
    }
}
