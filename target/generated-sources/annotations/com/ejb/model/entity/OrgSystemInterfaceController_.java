package com.ejb.model.entity;

import com.ejb.model.entity.GeneralOrganizationProfile;
import com.ejb.model.entity.Projects;
import com.ejb.model.entity.SystemInterface;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-08T18:03:02")
@StaticMetamodel(OrgSystemInterfaceController.class)
public class OrgSystemInterfaceController_ { 

    public static volatile SingularAttribute<OrgSystemInterfaceController, Projects> projectsId;
    public static volatile SingularAttribute<OrgSystemInterfaceController, GeneralOrganizationProfile> generalOrganizationProfileId;
    public static volatile SingularAttribute<OrgSystemInterfaceController, Integer> id;
    public static volatile SingularAttribute<OrgSystemInterfaceController, SystemInterface> systemInterfaceId;
    public static volatile SingularAttribute<OrgSystemInterfaceController, Integer> isActive;

}