package com.ejb.model.entity;

import com.ejb.model.entity.Teacher;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-05T16:19:25")
@StaticMetamodel(TeacherMonthlyScores.class)
public class TeacherMonthlyScores_ { 

    public static volatile SingularAttribute<TeacherMonthlyScores, Date> date;
    public static volatile SingularAttribute<TeacherMonthlyScores, Teacher> teacherId;
    public static volatile SingularAttribute<TeacherMonthlyScores, Integer> id;
    public static volatile SingularAttribute<TeacherMonthlyScores, Double> monthlyDedicationScore;
    public static volatile SingularAttribute<TeacherMonthlyScores, Double> monthlyAttendanceScore;

}