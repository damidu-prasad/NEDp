package com.ejb.model.entity;

import com.ejb.model.entity.DaysPeriod;
import com.ejb.model.entity.School;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-01T15:32:18")
@StaticMetamodel(Periods.class)
public class Periods_ { 

    public static volatile CollectionAttribute<Periods, DaysPeriod> daysPeriodCollection;
    public static volatile SingularAttribute<Periods, School> schoolId;
    public static volatile SingularAttribute<Periods, Date> startTime;
    public static volatile SingularAttribute<Periods, Integer> id;
    public static volatile SingularAttribute<Periods, Date> endTime;
    public static volatile SingularAttribute<Periods, String> periodNo;

}