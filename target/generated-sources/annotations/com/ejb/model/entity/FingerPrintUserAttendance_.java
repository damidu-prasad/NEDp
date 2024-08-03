package com.ejb.model.entity;

import com.ejb.model.entity.FingerPrintRegionUser;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-03T19:27:51")
@StaticMetamodel(FingerPrintUserAttendance.class)
public class FingerPrintUserAttendance_ { 

    public static volatile SingularAttribute<FingerPrintUserAttendance, Date> actionTime;
    public static volatile SingularAttribute<FingerPrintUserAttendance, FingerPrintRegionUser> fingerPrintRegionUserId;
    public static volatile SingularAttribute<FingerPrintUserAttendance, Integer> id;

}