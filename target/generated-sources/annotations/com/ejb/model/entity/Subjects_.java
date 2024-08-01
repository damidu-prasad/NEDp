package com.ejb.model.entity;

import com.ejb.model.entity.EducationLevel;
import com.ejb.model.entity.GradeClassHasSubjects;
import com.ejb.model.entity.TeacherClassSubjectMean;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-01T15:32:18")
@StaticMetamodel(Subjects.class)
public class Subjects_ { 

    public static volatile CollectionAttribute<Subjects, TeacherClassSubjectMean> teacherClassSubjectMeanCollection;
    public static volatile SingularAttribute<Subjects, String> code;
    public static volatile SingularAttribute<Subjects, String> name;
    public static volatile CollectionAttribute<Subjects, GradeClassHasSubjects> gradeClassHasSubjectsCollection;
    public static volatile SingularAttribute<Subjects, Integer> id;
    public static volatile SingularAttribute<Subjects, Integer> isActive;
    public static volatile SingularAttribute<Subjects, EducationLevel> educationLevelId;

}