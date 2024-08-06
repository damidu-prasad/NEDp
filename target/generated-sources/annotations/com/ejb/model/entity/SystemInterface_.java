package com.ejb.model.entity;

import com.ejb.model.entity.InterfaceMenu;
import com.ejb.model.entity.InterfaceSubMenu;
import com.ejb.model.entity.OrgSystemInterfaceController;
import com.ejb.model.entity.SystemInterface;
import com.ejb.model.entity.UserLogin;
import com.ejb.model.entity.UserRoleHasSystemInterface;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-05T16:19:25")
@StaticMetamodel(SystemInterface.class)
public class SystemInterface_ { 

    public static volatile CollectionAttribute<SystemInterface, SystemInterface> systemInterfaceCollection;
    public static volatile CollectionAttribute<SystemInterface, OrgSystemInterfaceController> orgSystemInterfaceControllerCollection;
    public static volatile SingularAttribute<SystemInterface, String> displayName;
    public static volatile SingularAttribute<SystemInterface, String> icon;
    public static volatile SingularAttribute<SystemInterface, InterfaceMenu> interfaceMenuId;
    public static volatile SingularAttribute<SystemInterface, String> description;
    public static volatile SingularAttribute<SystemInterface, String> url;
    public static volatile CollectionAttribute<SystemInterface, UserRoleHasSystemInterface> userRoleHasSystemInterfaceCollection;
    public static volatile SingularAttribute<SystemInterface, InterfaceSubMenu> interfaceSubMenuId;
    public static volatile CollectionAttribute<SystemInterface, UserLogin> userLoginCollection;
    public static volatile SingularAttribute<SystemInterface, Integer> id;
    public static volatile SingularAttribute<SystemInterface, String> interfaceName;
    public static volatile SingularAttribute<SystemInterface, SystemInterface> systemInterfaceId;
    public static volatile SingularAttribute<SystemInterface, Integer> status;

}