package com.ejb.model.entity;

import com.ejb.model.entity.Days;
import com.ejb.model.entity.Periods;
import com.ejb.model.entity.TimeTable;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-03T19:27:51")
@StaticMetamodel(DaysPeriod.class)
public class DaysPeriod_ { 

    public static volatile CollectionAttribute<DaysPeriod, TimeTable> timeTableCollection;
    public static volatile SingularAttribute<DaysPeriod, Days> daysId;
    public static volatile SingularAttribute<DaysPeriod, Periods> periodsId;
    public static volatile SingularAttribute<DaysPeriod, Integer> id;

}