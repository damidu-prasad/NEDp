package com.ejb.model.entity;

import com.ejb.model.entity.GeneralUserProfile;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-03T19:27:51")
@StaticMetamodel(CivilStatus.class)
public class CivilStatus_ { 

    public static volatile CollectionAttribute<CivilStatus, GeneralUserProfile> generalUserProfileCollection;
    public static volatile SingularAttribute<CivilStatus, String> name;
    public static volatile SingularAttribute<CivilStatus, Integer> id;

}