/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.model.common;

/**
 *
 * @author Thilini Madagama
 */
import com.ejb.model.entity.Students;
import javax.ejb.EJB;
import javax.ejb.Schedule;

import javax.ejb.Singleton;

@Singleton

public class DeclarativeScheduler {

    @EJB
    private UniDBLocal uni;
    
//    @Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
    
     

    public void atSchedule() throws InterruptedException {

        Students s = (Students) uni.find(100, Students.class);
        
        System.out.println("DeclarativeScheduler:: In atSchedule()"+s.getGeneralUserProfileId().getNameWithIn());

    }

}
