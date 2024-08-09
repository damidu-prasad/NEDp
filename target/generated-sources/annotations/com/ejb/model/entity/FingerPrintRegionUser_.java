package com.ejb.model.entity;

import com.ejb.model.entity.FingerPrintRegion;
import com.ejb.model.entity.FingerPrintUserAttendance;
import com.ejb.model.entity.GeneralUserProfile;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-08T18:03:02")
@StaticMetamodel(FingerPrintRegionUser.class)
public class FingerPrintRegionUser_ { 

    public static volatile SingularAttribute<FingerPrintRegionUser, Date> addedDate;
    public static volatile SingularAttribute<FingerPrintRegionUser, Integer> enrollementId;
    public static volatile CollectionAttribute<FingerPrintRegionUser, FingerPrintUserAttendance> fingerPrintUserAttendanceCollection;
    public static volatile SingularAttribute<FingerPrintRegionUser, FingerPrintRegion> fingerPrintRegionId;
    public static volatile SingularAttribute<FingerPrintRegionUser, GeneralUserProfile> generalUserProfileGupId;
    public static volatile SingularAttribute<FingerPrintRegionUser, Integer> id;
    public static volatile SingularAttribute<FingerPrintRegionUser, Boolean> isActive;

}