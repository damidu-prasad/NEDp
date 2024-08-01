package com.ejb.model.entity;

import com.ejb.model.entity.Classes;
import com.ejb.model.entity.Grade;
import com.ejb.model.entity.GradeClassHasSubjects;
import com.ejb.model.entity.GradeClassStreamManager;
import com.ejb.model.entity.School;
import com.ejb.model.entity.Streams;
import com.ejb.model.entity.TeacherClassSubjectMean;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-01T15:32:18")
@StaticMetamodel(GradeClassStream.class)
public class GradeClassStream_ { 

    public static volatile SingularAttribute<GradeClassStream, Grade> gradeId;
    public static volatile SingularAttribute<GradeClassStream, Classes> classesId;
    public static volatile CollectionAttribute<GradeClassStream, TeacherClassSubjectMean> teacherClassSubjectMeanCollection;
    public static volatile SingularAttribute<GradeClassStream, School> schoolId;
    public static volatile CollectionAttribute<GradeClassStream, GradeClassHasSubjects> gradeClassHasSubjectsCollection;
    public static volatile SingularAttribute<GradeClassStream, Integer> id;
    public static volatile SingularAttribute<GradeClassStream, Integer> isActive;
    public static volatile CollectionAttribute<GradeClassStream, GradeClassStreamManager> gradeClassStreamManagerCollection;
    public static volatile SingularAttribute<GradeClassStream, Streams> streamsId;

}