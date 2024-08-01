package com.ejb.model.entity;

import com.ejb.model.entity.GeneralOrganizationProfile;
import com.ejb.model.entity.OrganizationTypeManager;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-01T15:32:19")
@StaticMetamodel(OrganizationType.class)
public class OrganizationType_ { 

    public static volatile CollectionAttribute<OrganizationType, OrganizationTypeManager> organizationTypeManagerCollection;
    public static volatile SingularAttribute<OrganizationType, String> name;
    public static volatile SingularAttribute<OrganizationType, Integer> id;
    public static volatile CollectionAttribute<OrganizationType, GeneralOrganizationProfile> generalOrganizationProfileCollection;

}