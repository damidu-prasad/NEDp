/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.model.systemautomated;

import com.ejb.model.common.UniDB;
import com.ejb.model.entity.Teacher;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.ejb.Timer;

/**
 *
 * @author USER
 */
@Singleton
public class Trigger {

    @EJB
    UniDB uniDB;

    @Schedule(dayOfWeek = "Sat", hour = "18", minute = "49", second = "0", persistent = false)
    public void sendWeeklyEmail(Timer timer) {
        System.out.println("Timer Called");
        String query_get_all_teachers = "SELECT g FROM Teacher g WHERE g.schoolId.id='100' AND g.isActive='1' ";
        List<Teacher> teachersList = uniDB.searchByQuerySingle(query_get_all_teachers);

        if (teachersList.size() > 0) {
            System.out.println("in if");
        } else {
            System.out.println("in else");
        }

    }

}
