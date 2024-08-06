package com.ejb.model.entity;

import com.ejb.model.entity.DevTarget;
import com.ejb.model.entity.EducationalAreaMean;
import com.ejb.model.entity.GradeClassStreamManager;
import com.ejb.model.entity.StudentMean;
import com.ejb.model.entity.TeacherClassSubjectMean;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-05T16:19:25")
@StaticMetamodel(Year.class)
public class Year_ { 

    public static volatile CollectionAttribute<Year, TeacherClassSubjectMean> teacherClassSubjectMeanCollection;
    public static volatile CollectionAttribute<Year, DevTarget> devTargetCollection;
    public static volatile SingularAttribute<Year, String> name;
    public static volatile CollectionAttribute<Year, StudentMean> studentMeanCollection;
    public static volatile CollectionAttribute<Year, EducationalAreaMean> educationalAreaMeanCollection;
    public static volatile SingularAttribute<Year, Integer> id;
    public static volatile CollectionAttribute<Year, GradeClassStreamManager> gradeClassStreamManagerCollection;

}