package com.ejb.model.entity;

import com.ejb.model.entity.GeneralUserProfile;
import com.ejb.model.entity.Subjects;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-08T18:03:02")
@StaticMetamodel(EducationLevel.class)
public class EducationLevel_ { 

    public static volatile CollectionAttribute<EducationLevel, GeneralUserProfile> generalUserProfileCollection;
    public static volatile SingularAttribute<EducationLevel, String> name;
    public static volatile SingularAttribute<EducationLevel, Integer> id;
    public static volatile CollectionAttribute<EducationLevel, Subjects> subjectsCollection;

}