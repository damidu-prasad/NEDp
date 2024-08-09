package com.ejb.model.entity;

import com.ejb.model.entity.DataChangedLogManager;
import com.ejb.model.entity.GeneralOrganizationProfile;
import com.ejb.model.entity.GeneralUserProfile;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.SecurityQuestion;
import com.ejb.model.entity.SystemInterface;
import com.ejb.model.entity.UserLoginGroup;
import com.ejb.model.entity.UserRole;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-08T18:03:02")
@StaticMetamodel(UserLogin.class)
public class UserLogin_ { 

    public static volatile SingularAttribute<UserLogin, Integer> isMultipleLogin;
    public static volatile SingularAttribute<UserLogin, GeneralOrganizationProfile> generalOrganizationProfileId;
    public static volatile SingularAttribute<UserLogin, String> generatedPassword;
    public static volatile SingularAttribute<UserLogin, GeneralUserProfile> generalUserProfileId;
    public static volatile CollectionAttribute<UserLogin, LoginSession> loginSessionCollection;
    public static volatile SingularAttribute<UserLogin, Integer> maxLoginAttempt;
    public static volatile SingularAttribute<UserLogin, Integer> countAttempt;
    public static volatile SingularAttribute<UserLogin, SecurityQuestion> securityQuestionId;
    public static volatile SingularAttribute<UserLogin, Integer> isActive;
    public static volatile SingularAttribute<UserLogin, String> password;
    public static volatile SingularAttribute<UserLogin, String> answer;
    public static volatile CollectionAttribute<UserLogin, UserLoginGroup> userLoginGroupCollection;
    public static volatile SingularAttribute<UserLogin, Integer> isFirstTime;
    public static volatile SingularAttribute<UserLogin, Integer> id;
    public static volatile SingularAttribute<UserLogin, SystemInterface> systemInterfaceId;
    public static volatile SingularAttribute<UserLogin, UserRole> userRoleId;
    public static volatile CollectionAttribute<UserLogin, DataChangedLogManager> dataChangedLogManagerCollection;
    public static volatile SingularAttribute<UserLogin, String> username;

}