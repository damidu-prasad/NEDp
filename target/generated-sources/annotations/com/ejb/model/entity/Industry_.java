package com.ejb.model.entity;

import com.ejb.model.entity.GeneralUserProfile;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-01T15:32:19")
@StaticMetamodel(Industry.class)
public class Industry_ { 

    public static volatile CollectionAttribute<Industry, GeneralUserProfile> generalUserProfileCollection;
    public static volatile SingularAttribute<Industry, String> name;
    public static volatile SingularAttribute<Industry, Integer> id;

}