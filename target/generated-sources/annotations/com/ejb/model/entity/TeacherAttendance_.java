package com.ejb.model.entity;

import com.ejb.model.entity.Teacher;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-01T15:32:19")
@StaticMetamodel(TeacherAttendance.class)
public class TeacherAttendance_ { 

    public static volatile SingularAttribute<TeacherAttendance, Date> date;
    public static volatile SingularAttribute<TeacherAttendance, Date> out_time;
    public static volatile SingularAttribute<TeacherAttendance, Boolean> isAttended;
    public static volatile SingularAttribute<TeacherAttendance, Teacher> teacherId;
    public static volatile SingularAttribute<TeacherAttendance, Date> in_time;
    public static volatile SingularAttribute<TeacherAttendance, Integer> id;

}