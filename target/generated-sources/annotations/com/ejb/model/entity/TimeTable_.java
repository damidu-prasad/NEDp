package com.ejb.model.entity;

import com.ejb.model.entity.DaysPeriod;
import com.ejb.model.entity.GradeClassStreamManager;
import com.ejb.model.entity.GradeClassSubjectTeacher;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-05T16:19:25")
@StaticMetamodel(TimeTable.class)
public class TimeTable_ { 

    public static volatile SingularAttribute<TimeTable, GradeClassSubjectTeacher> gradeClassSubjectTeacherId;
    public static volatile SingularAttribute<TimeTable, GradeClassStreamManager> gradeClassStreamManagerId;
    public static volatile SingularAttribute<TimeTable, Integer> id;
    public static volatile SingularAttribute<TimeTable, DaysPeriod> daysPeriodId;

}