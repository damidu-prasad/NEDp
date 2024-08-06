package com.ejb.model.entity;

import com.ejb.model.entity.GeneralUserProfile;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-05T16:19:25")
@StaticMetamodel(Nationality.class)
public class Nationality_ { 

    public static volatile CollectionAttribute<Nationality, GeneralUserProfile> generalUserProfileCollection;
    public static volatile SingularAttribute<Nationality, String> name;
    public static volatile SingularAttribute<Nationality, Integer> id;

}