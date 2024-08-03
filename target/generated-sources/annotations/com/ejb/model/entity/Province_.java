package com.ejb.model.entity;

import com.ejb.model.entity.Country;
import com.ejb.model.entity.EducationZone;
import com.ejb.model.entity.GeneralOrganizationProfile;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-03T19:27:51")
@StaticMetamodel(Province.class)
public class Province_ { 

    public static volatile SingularAttribute<Province, GeneralOrganizationProfile> generalOrganizationProfileId;
    public static volatile SingularAttribute<Province, String> name;
    public static volatile CollectionAttribute<Province, EducationZone> educationZoneCollection;
    public static volatile SingularAttribute<Province, Integer> id;
    public static volatile SingularAttribute<Province, String> provinceId;
    public static volatile SingularAttribute<Province, Country> countryId;

}