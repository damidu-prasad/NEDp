package com.ejb.model.entity;

import com.ejb.model.entity.GeneralUserProfile;
import com.ejb.model.entity.GradeClassStudents;
import com.ejb.model.entity.School;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-03T19:27:51")
@StaticMetamodel(Students.class)
public class Students_ { 

    public static volatile SingularAttribute<Students, String> studentId;
    public static volatile CollectionAttribute<Students, GradeClassStudents> gradeClassStudentsCollection;
    public static volatile SingularAttribute<Students, GeneralUserProfile> generalUserProfileId;
    public static volatile SingularAttribute<Students, School> schoolId;
    public static volatile SingularAttribute<Students, Integer> id;

}