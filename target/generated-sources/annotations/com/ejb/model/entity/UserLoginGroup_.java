package com.ejb.model.entity;

import com.ejb.model.entity.GeneralOrganizationProfile;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.Projects;
import com.ejb.model.entity.UserLogin;
import com.ejb.model.entity.UserRole;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-03T19:27:51")
@StaticMetamodel(UserLoginGroup.class)
public class UserLoginGroup_ { 

    public static volatile SingularAttribute<UserLoginGroup, UserLogin> userLoginId;
    public static volatile SingularAttribute<UserLoginGroup, Projects> projectsId;
    public static volatile SingularAttribute<UserLoginGroup, GeneralOrganizationProfile> generalOrganizationProfileId;
    public static volatile CollectionAttribute<UserLoginGroup, LoginSession> loginSessionCollection;
    public static volatile SingularAttribute<UserLoginGroup, Integer> maxLoginAttempt;
    public static volatile SingularAttribute<UserLoginGroup, Integer> isFirstTime;
    public static volatile SingularAttribute<UserLoginGroup, Integer> countAttempt;
    public static volatile SingularAttribute<UserLoginGroup, Integer> id;
    public static volatile SingularAttribute<UserLoginGroup, UserRole> userRoleId;
    public static volatile SingularAttribute<UserLoginGroup, Integer> isActive;

}