package com.ejb.model.entity;

import com.ejb.model.entity.UserLogin;
import com.ejb.model.entity.UserLoginGroup;
import com.ejb.model.entity.UserRoleHasSystemInterface;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-08T18:03:02")
@StaticMetamodel(UserRole.class)
public class UserRole_ { 

    public static volatile CollectionAttribute<UserRole, UserRoleHasSystemInterface> userRoleHasSystemInterfaceCollection;
    public static volatile SingularAttribute<UserRole, Double> discountRate;
    public static volatile CollectionAttribute<UserRole, UserLogin> userLoginCollection;
    public static volatile CollectionAttribute<UserRole, UserLoginGroup> userLoginGroupCollection;
    public static volatile SingularAttribute<UserRole, String> roleName;
    public static volatile SingularAttribute<UserRole, Integer> id;
    public static volatile SingularAttribute<UserRole, Integer> roleOrder;

}