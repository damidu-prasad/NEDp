package com.ejb.model.entity;

import com.ejb.model.entity.GradeClassStreamManager;
import com.ejb.model.entity.GradeClassStudentsHasSubjects;
import com.ejb.model.entity.StudentMarks;
import com.ejb.model.entity.StudentMean;
import com.ejb.model.entity.Students;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-05T16:19:25")
@StaticMetamodel(GradeClassStudents.class)
public class GradeClassStudents_ { 

    public static volatile SingularAttribute<GradeClassStudents, Integer> isRemoved;
    public static volatile SingularAttribute<GradeClassStudents, Students> studentsId;
    public static volatile CollectionAttribute<GradeClassStudents, GradeClassStudentsHasSubjects> gradeClassStudentsHasSubjectsCollection;
    public static volatile CollectionAttribute<GradeClassStudents, StudentMean> studentMeanCollection;
    public static volatile SingularAttribute<GradeClassStudents, GradeClassStreamManager> gradeClassStreamManagerId;
    public static volatile CollectionAttribute<GradeClassStudents, StudentMarks> studentMarksCollection;
    public static volatile SingularAttribute<GradeClassStudents, Integer> id;

}