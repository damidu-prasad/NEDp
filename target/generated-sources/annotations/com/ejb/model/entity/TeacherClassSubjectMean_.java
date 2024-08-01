package com.ejb.model.entity;

import com.ejb.model.entity.GradeClassStream;
import com.ejb.model.entity.Subjects;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.Terms;
import com.ejb.model.entity.Year;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-01T15:32:18")
@StaticMetamodel(TeacherClassSubjectMean.class)
public class TeacherClassSubjectMean_ { 

    public static volatile SingularAttribute<TeacherClassSubjectMean, Teacher> teacherId;
    public static volatile SingularAttribute<TeacherClassSubjectMean, Terms> termsId;
    public static volatile SingularAttribute<TeacherClassSubjectMean, GradeClassStream> gradeClassStreamId;
    public static volatile SingularAttribute<TeacherClassSubjectMean, Double> meanValue;
    public static volatile SingularAttribute<TeacherClassSubjectMean, Integer> id;
    public static volatile SingularAttribute<TeacherClassSubjectMean, Subjects> subjectsId;
    public static volatile SingularAttribute<TeacherClassSubjectMean, Year> yearId;

}