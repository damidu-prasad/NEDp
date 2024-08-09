package com.ejb.model.entity;

import com.ejb.model.entity.EducationZone;
import com.ejb.model.entity.GeneralOrganizationProfile;
import com.ejb.model.entity.School;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-08T18:03:02")
@StaticMetamodel(EducationDivision.class)
public class EducationDivision_ { 

    public static volatile SingularAttribute<EducationDivision, GeneralOrganizationProfile> generalOrganizationProfileId;
    public static volatile SingularAttribute<EducationDivision, String> code;
    public static volatile SingularAttribute<EducationDivision, EducationZone> educationZoneId;
    public static volatile CollectionAttribute<EducationDivision, School> schoolCollection;
    public static volatile SingularAttribute<EducationDivision, String> name;
    public static volatile SingularAttribute<EducationDivision, Integer> id;

}