package com.ejb.model.entity;

import com.ejb.model.entity.GradeClassStudents;
import com.ejb.model.entity.Terms;
import com.ejb.model.entity.Year;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-08T18:03:02")
@StaticMetamodel(StudentMean.class)
public class StudentMean_ { 

    public static volatile SingularAttribute<StudentMean, GradeClassStudents> gradeClassStudentsId;
    public static volatile SingularAttribute<StudentMean, Terms> termsId;
    public static volatile SingularAttribute<StudentMean, Double> meanValue;
    public static volatile SingularAttribute<StudentMean, Integer> id;
    public static volatile SingularAttribute<StudentMean, Year> yearId;

}