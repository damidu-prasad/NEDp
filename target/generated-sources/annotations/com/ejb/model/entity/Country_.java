package com.ejb.model.entity;

import com.ejb.model.entity.GeneralOrganizationProfile;
import com.ejb.model.entity.GeneralUserProfile;
import com.ejb.model.entity.Province;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-05T16:19:25")
@StaticMetamodel(Country.class)
public class Country_ { 

    public static volatile CollectionAttribute<Country, GeneralUserProfile> generalUserProfileCollection;
    public static volatile SingularAttribute<Country, String> name;
    public static volatile SingularAttribute<Country, Integer> id;
    public static volatile CollectionAttribute<Country, Province> provinceCollection;
    public static volatile CollectionAttribute<Country, GeneralOrganizationProfile> generalOrganizationProfileCollection;

}