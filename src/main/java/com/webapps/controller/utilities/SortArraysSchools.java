/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.utilities;

import com.webapps.controller.loadbean.LoadBanners;
import com.webapps.controller.managebeans.DataInputScore;
import com.webapps.controller.managebeans.ReportCardGenerator;
import com.webapps.controller.managebeans.TeacherPerformanceAnalyzer;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Thilini Madagama
 */
public class SortArraysSchools {

    static final Comparator<LoadBanners.SchoolNameList> ORDER_BY_MEAN = new Comparator<LoadBanners.SchoolNameList>() {

        @Override
        public int compare(LoadBanners.SchoolNameList o1, LoadBanners.SchoolNameList o2) {
            return Double.compare(o1.getMarksEnter(), o2.getMarksEnter());
        }
    };

    static final Comparator<LoadBanners.SchoolNameList> ORDER_BY_RULES = new Comparator<LoadBanners.SchoolNameList>() {

        @Override
        public int compare(LoadBanners.SchoolNameList o1, LoadBanners.SchoolNameList o2) {
            int i = ORDER_BY_MEAN.compare(o1, o2);
            return i;
        }
    };

    public static LoadBanners.SchoolNameList[] GetArray(LoadBanners.SchoolNameList[] addressArray) {

        Arrays.sort(addressArray, ORDER_BY_RULES);

        return addressArray;
    }
}
