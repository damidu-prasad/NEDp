package com.ejb.model.entity;

import com.ejb.model.entity.SystemInterface;
import com.ejb.model.entity.UserRole;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-01T15:32:18")
@StaticMetamodel(UserRoleHasSystemInterface.class)
public class UserRoleHasSystemInterface_ { 

    public static volatile SingularAttribute<UserRoleHasSystemInterface, Integer> id;
    public static volatile SingularAttribute<UserRoleHasSystemInterface, SystemInterface> systemInterfaceId;
    public static volatile SingularAttribute<UserRoleHasSystemInterface, UserRole> userRoleId;

}