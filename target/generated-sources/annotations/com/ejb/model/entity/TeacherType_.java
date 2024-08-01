package com.ejb.model.entity;

import com.ejb.model.entity.GradeClassStreamManager;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-01T15:32:19")
@StaticMetamodel(TeacherType.class)
public class TeacherType_ { 

    public static volatile SingularAttribute<TeacherType, Integer> id;
    public static volatile SingularAttribute<TeacherType, String> type;
    public static volatile CollectionAttribute<TeacherType, GradeClassStreamManager> gradeClassStreamManagerCollection;

}