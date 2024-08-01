package com.ejb.model.entity;

import com.ejb.model.entity.DevTarget;
import com.ejb.model.entity.EducationalAreaMean;
import com.ejb.model.entity.StudentMarks;
import com.ejb.model.entity.StudentMean;
import com.ejb.model.entity.TeacherClassSubjectMean;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-01T15:32:18")
@StaticMetamodel(Terms.class)
public class Terms_ { 

    public static volatile CollectionAttribute<Terms, TeacherClassSubjectMean> teacherClassSubjectMeanCollection;
    public static volatile CollectionAttribute<Terms, DevTarget> devTargetCollection;
    public static volatile SingularAttribute<Terms, String> name;
    public static volatile CollectionAttribute<Terms, StudentMean> studentMeanCollection;
    public static volatile CollectionAttribute<Terms, StudentMarks> studentMarksCollection;
    public static volatile CollectionAttribute<Terms, EducationalAreaMean> educationalAreaMeanCollection;
    public static volatile SingularAttribute<Terms, Integer> id;

}