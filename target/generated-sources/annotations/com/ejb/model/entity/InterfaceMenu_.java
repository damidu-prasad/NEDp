package com.ejb.model.entity;

import com.ejb.model.entity.SystemInterface;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-03T19:27:51")
@StaticMetamodel(InterfaceMenu.class)
public class InterfaceMenu_ { 

    public static volatile CollectionAttribute<InterfaceMenu, SystemInterface> systemInterfaceCollection;
    public static volatile SingularAttribute<InterfaceMenu, String> icon;
    public static volatile SingularAttribute<InterfaceMenu, String> menuName;
    public static volatile SingularAttribute<InterfaceMenu, Integer> id;

}