package com.ejb.model.entity;

import com.ejb.model.entity.GeneralUserProfile;
import com.ejb.model.entity.GradeClassStreamManager;
import com.ejb.model.entity.GradeClassSubjectTeacher;
import com.ejb.model.entity.School;
import com.ejb.model.entity.TeacherClassSubjectMean;
import com.ejb.model.entity.TeacherMonthlyScores;
import com.ejb.model.entity.TeacherType;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-05T16:19:25")
@StaticMetamodel(Teacher.class)
public class Teacher_ { 

    public static volatile CollectionAttribute<Teacher, TeacherClassSubjectMean> teacherClassSubjectMeanCollection;
    public static volatile SingularAttribute<Teacher, String> teacherId;
    public static volatile CollectionAttribute<Teacher, TeacherMonthlyScores> teacherMonthlyScoresCollection;
    public static volatile SingularAttribute<Teacher, Boolean> isVerified;
    public static volatile SingularAttribute<Teacher, GeneralUserProfile> generalUserProfileId;
    public static volatile SingularAttribute<Teacher, TeacherType> teacherTypeId;
    public static volatile SingularAttribute<Teacher, School> schoolId;
    public static volatile CollectionAttribute<Teacher, GradeClassSubjectTeacher> gradeClassSubjectTeacherCollection;
    public static volatile SingularAttribute<Teacher, Integer> id;
    public static volatile SingularAttribute<Teacher, Integer> isActive;
    public static volatile CollectionAttribute<Teacher, GradeClassStreamManager> gradeClassStreamManagerCollection;

}