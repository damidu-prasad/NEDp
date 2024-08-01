package com.ejb.model.entity;

import com.ejb.model.entity.GradeClassStream;
import com.ejb.model.entity.GradeClassStudents;
import com.ejb.model.entity.GradeClassSubjectTeacher;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.TeacherType;
import com.ejb.model.entity.TimeTable;
import com.ejb.model.entity.Year;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-01T15:32:18")
@StaticMetamodel(GradeClassStreamManager.class)
public class GradeClassStreamManager_ { 

    public static volatile CollectionAttribute<GradeClassStreamManager, TimeTable> timeTableCollection;
    public static volatile SingularAttribute<GradeClassStreamManager, TeacherType> teacherType;
    public static volatile CollectionAttribute<GradeClassStreamManager, GradeClassStudents> gradeClassStudentsCollection;
    public static volatile SingularAttribute<GradeClassStreamManager, Teacher> teacherId;
    public static volatile SingularAttribute<GradeClassStreamManager, GradeClassStream> gradeClassStreamId;
    public static volatile CollectionAttribute<GradeClassStreamManager, GradeClassSubjectTeacher> gradeClassSubjectTeacherCollection;
    public static volatile SingularAttribute<GradeClassStreamManager, Integer> id;
    public static volatile SingularAttribute<GradeClassStreamManager, Integer> isActive;
    public static volatile SingularAttribute<GradeClassStreamManager, Year> yearId;

}