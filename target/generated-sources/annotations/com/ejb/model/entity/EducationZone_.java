package com.ejb.model.entity;

import com.ejb.model.entity.EducationDivision;
import com.ejb.model.entity.GeneralOrganizationProfile;
import com.ejb.model.entity.Province;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-03T19:27:51")
@StaticMetamodel(EducationZone.class)
public class EducationZone_ { 

    public static volatile SingularAttribute<EducationZone, GeneralOrganizationProfile> generalOrganizationProfileId;
    public static volatile SingularAttribute<EducationZone, String> code;
    public static volatile SingularAttribute<EducationZone, String> name;
    public static volatile CollectionAttribute<EducationZone, EducationDivision> educationDivisionCollection;
    public static volatile SingularAttribute<EducationZone, Integer> id;
    public static volatile SingularAttribute<EducationZone, Province> provinceId;

}