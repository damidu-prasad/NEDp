/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.utilities;

import com.webapps.controller.managebeans.TeacherPerformanceAnalyzer;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Thilini Madagama
 */
public class SortArraysTeacherPerformanceAnalyzer {

    static final Comparator<TeacherPerformanceAnalyzer.MeanTeacherList> ORDER_BY_MEAN = new Comparator<TeacherPerformanceAnalyzer.MeanTeacherList>() {

        @Override
        public int compare(TeacherPerformanceAnalyzer.MeanTeacherList o1, TeacherPerformanceAnalyzer.MeanTeacherList o2) {
            return Double.compare(o1.getTeachermean(), o2.getTeachermean());
        }
    };

    static final Comparator<TeacherPerformanceAnalyzer.MeanTeacherList> ORDER_BY_RULES = new Comparator<TeacherPerformanceAnalyzer.MeanTeacherList>() {

        @Override
        public int compare(TeacherPerformanceAnalyzer.MeanTeacherList o1, TeacherPerformanceAnalyzer.MeanTeacherList o2) {
            int i = ORDER_BY_MEAN.compare(o1, o2);
            return i;
        }
    };

    public static TeacherPerformanceAnalyzer.MeanTeacherList[] GetArray(TeacherPerformanceAnalyzer.MeanTeacherList[] addressArray) {

        Arrays.sort(addressArray, ORDER_BY_RULES);

        return addressArray;
    }

    public static void main(String args[]) {
        //Array with address to sort
        StudentList[] addressArray = new StudentList[]{
            new StudentList(121, 5, 25),
            new StudentList(122, 9, 23),
            new StudentList(123, 5, 29),
            new StudentList(124, 4, 23),
            new StudentList(125, 5, 21),
            new StudentList(126, 5, 10)};
//        Arrays.sort(addressArray, ORDER_BY_RULES);
        //Print the sorted array
        for (int i = 0; i < addressArray.length; i++) {
            System.out.println(addressArray[i].toString());
        }
        System.out.println();
    }

    public static class StudentList {

        public int studentId;
        public int exam_count;
        public int assignment_count;

        @Override
        public String toString() {
            return "StudentList [" + studentId + " | " + exam_count + " | "
                    + assignment_count + "]";
        }

        public StudentList() {
        }

        public StudentList(int studentId, int exam_count,
                int assignment_count) {
            super();
            this.studentId = studentId;
            this.exam_count = exam_count;
            this.assignment_count = assignment_count;

        }
    }
}
