package com.ejb.model.entity;

import com.ejb.model.entity.GeneralOrganizationProfile;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-05T16:19:25")
@StaticMetamodel(RegistrationType.class)
public class RegistrationType_ { 

    public static volatile SingularAttribute<RegistrationType, String> name;
    public static volatile SingularAttribute<RegistrationType, Integer> id;
    public static volatile CollectionAttribute<RegistrationType, GeneralOrganizationProfile> generalOrganizationProfileCollection;

}