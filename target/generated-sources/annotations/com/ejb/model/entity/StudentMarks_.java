package com.ejb.model.entity;

import com.ejb.model.entity.GeneralUserProfile;
import com.ejb.model.entity.GradeClassHasSubjects;
import com.ejb.model.entity.GradeClassStudents;
import com.ejb.model.entity.Terms;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-05T16:19:25")
@StaticMetamodel(StudentMarks.class)
public class StudentMarks_ { 

    public static volatile SingularAttribute<StudentMarks, Integer> isRemoved;
    public static volatile SingularAttribute<StudentMarks, GradeClassStudents> gradeClassStudentsId;
    public static volatile SingularAttribute<StudentMarks, Terms> termsId;
    public static volatile SingularAttribute<StudentMarks, GradeClassHasSubjects> gradeClassHasSubjectsId;
    public static volatile SingularAttribute<StudentMarks, Boolean> isPresent;
    public static volatile SingularAttribute<StudentMarks, Integer> id;
    public static volatile SingularAttribute<StudentMarks, Double> marks;
    public static volatile SingularAttribute<StudentMarks, GeneralUserProfile> enteredBy;
    public static volatile SingularAttribute<StudentMarks, Boolean> isMandatory;

}