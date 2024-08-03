package com.ejb.model.entity;

import com.ejb.model.entity.School;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-03T19:27:51")
@StaticMetamodel(SchoolCategory.class)
public class SchoolCategory_ { 

    public static volatile CollectionAttribute<SchoolCategory, School> schoolCollection;
    public static volatile SingularAttribute<SchoolCategory, String> name;
    public static volatile SingularAttribute<SchoolCategory, Integer> id;

}