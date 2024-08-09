package com.ejb.model.entity;

import com.ejb.model.entity.GeneralOrganizationProfile;
import com.ejb.model.entity.UserLogin;
import com.ejb.model.entity.UserLoginGroup;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-08T18:03:02")
@StaticMetamodel(LoginSession.class)
public class LoginSession_ { 

    public static volatile SingularAttribute<LoginSession, UserLogin> userLoginId;
    public static volatile SingularAttribute<LoginSession, GeneralOrganizationProfile> generalOrganizationProfileId;
    public static volatile SingularAttribute<LoginSession, String> ip;
    public static volatile SingularAttribute<LoginSession, Date> startTime;
    public static volatile SingularAttribute<LoginSession, Integer> id;
    public static volatile SingularAttribute<LoginSession, Date> endTime;
    public static volatile SingularAttribute<LoginSession, UserLoginGroup> userLoginGroupId;

}