/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps.controller.utilities;

import com.webapps.controller.loadbean.LoadBanners;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Thilini Madagama
 */
public class BannerValus {

    public static Date getDate() {
        return date;
    }

    public static void setDate(Date aDate) {
        date = aDate;
    }

    public static List<LoadBanners.SchoolNameList> getScoreSchoolList() {
        return scoreSchoolList;
    }

    public static void setScoreSchoolList(List<LoadBanners.SchoolNameList> aScoreSchoolList) {
        scoreSchoolList = aScoreSchoolList;
    }

    public static List<LoadBanners.TeacherList> getScoreTeacherList() {
        return scoreTeacherList;
    }

    public static void setScoreTeacherList(List<LoadBanners.TeacherList> aScoreTeacherList) {
        scoreTeacherList = aScoreTeacherList;
    }

    public static boolean isIsSent() {
        return isSent;
    }

    public static void setIsSent(boolean aisSent) {
        isSent = aisSent;
    }

    private static Date date;
    private static List<LoadBanners.SchoolNameList> scoreSchoolList = new ArrayList();
    private static List<LoadBanners.TeacherList> scoreTeacherList = new ArrayList();
    private static boolean isSent;

}
