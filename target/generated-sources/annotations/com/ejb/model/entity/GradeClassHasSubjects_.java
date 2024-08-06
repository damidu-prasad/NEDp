package com.ejb.model.entity;

import com.ejb.model.entity.GradeClassStream;
import com.ejb.model.entity.GradeClassStudentsHasSubjects;
import com.ejb.model.entity.GradeClassSubjectTeacher;
import com.ejb.model.entity.StudentMarks;
import com.ejb.model.entity.Subjects;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-05T16:19:25")
@StaticMetamodel(GradeClassHasSubjects.class)
public class GradeClassHasSubjects_ { 

    public static volatile SingularAttribute<GradeClassHasSubjects, GradeClassStream> gradeClassStreamId;
    public static volatile CollectionAttribute<GradeClassHasSubjects, GradeClassStudentsHasSubjects> gradeClassStudentsHasSubjectsCollection;
    public static volatile CollectionAttribute<GradeClassHasSubjects, StudentMarks> studentMarksCollection;
    public static volatile CollectionAttribute<GradeClassHasSubjects, GradeClassSubjectTeacher> gradeClassSubjectTeacherCollection;
    public static volatile SingularAttribute<GradeClassHasSubjects, Integer> id;
    public static volatile SingularAttribute<GradeClassHasSubjects, Integer> isActive;
    public static volatile SingularAttribute<GradeClassHasSubjects, Subjects> subjectsId;

}