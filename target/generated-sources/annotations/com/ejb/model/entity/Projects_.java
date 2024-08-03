package com.ejb.model.entity;

import com.ejb.model.entity.OrgSystemInterfaceController;
import com.ejb.model.entity.UserLoginGroup;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-03T19:27:51")
@StaticMetamodel(Projects.class)
public class Projects_ { 

    public static volatile CollectionAttribute<Projects, OrgSystemInterfaceController> orgSystemInterfaceControllerCollection;
    public static volatile CollectionAttribute<Projects, UserLoginGroup> userLoginGroupCollection;
    public static volatile SingularAttribute<Projects, String> name;
    public static volatile SingularAttribute<Projects, Integer> id;

}