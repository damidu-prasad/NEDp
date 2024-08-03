package com.ejb.model.entity;

import com.ejb.model.entity.DevTargetType;
import com.ejb.model.entity.Terms;
import com.ejb.model.entity.Year;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-03T19:27:51")
@StaticMetamodel(DevTarget.class)
public class DevTarget_ { 

    public static volatile SingularAttribute<DevTarget, DevTargetType> devTargetTypeId;
    public static volatile SingularAttribute<DevTarget, Terms> termsId;
    public static volatile SingularAttribute<DevTarget, Integer> id;
    public static volatile SingularAttribute<DevTarget, Double> target;
    public static volatile SingularAttribute<DevTarget, Year> yearId;

}