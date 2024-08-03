package com.ejb.model.entity;

import com.ejb.model.entity.School;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-03T19:27:51")
@StaticMetamodel(GradeDescription.class)
public class GradeDescription_ { 

    public static volatile CollectionAttribute<GradeDescription, School> schoolCollection;
    public static volatile SingularAttribute<GradeDescription, String> name;
    public static volatile SingularAttribute<GradeDescription, Integer> id;

}