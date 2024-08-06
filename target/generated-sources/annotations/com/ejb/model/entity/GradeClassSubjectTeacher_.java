package com.ejb.model.entity;

import com.ejb.model.entity.GradeClassHasSubjects;
import com.ejb.model.entity.GradeClassStreamManager;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.TimeTable;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-05T16:19:25")
@StaticMetamodel(GradeClassSubjectTeacher.class)
public class GradeClassSubjectTeacher_ { 

    public static volatile CollectionAttribute<GradeClassSubjectTeacher, TimeTable> timeTableCollection;
    public static volatile SingularAttribute<GradeClassSubjectTeacher, Teacher> teacherId;
    public static volatile SingularAttribute<GradeClassSubjectTeacher, GradeClassHasSubjects> gradeClassHasSubjectsId;
    public static volatile SingularAttribute<GradeClassSubjectTeacher, GradeClassStreamManager> gradeClassStreamManagerId;
    public static volatile SingularAttribute<GradeClassSubjectTeacher, Integer> id;
    public static volatile SingularAttribute<GradeClassSubjectTeacher, Integer> isActive;

}