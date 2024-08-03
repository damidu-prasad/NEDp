package com.ejb.model.entity;

import com.ejb.model.entity.TableManager;
import com.ejb.model.entity.UserLogin;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-03T19:27:51")
@StaticMetamodel(DataChangedLogManager.class)
public class DataChangedLogManager_ { 

    public static volatile SingularAttribute<DataChangedLogManager, Date> date;
    public static volatile SingularAttribute<DataChangedLogManager, Integer> reference;
    public static volatile SingularAttribute<DataChangedLogManager, UserLogin> userLoginId;
    public static volatile SingularAttribute<DataChangedLogManager, String> oldData;
    public static volatile SingularAttribute<DataChangedLogManager, String> attributeName;
    public static volatile SingularAttribute<DataChangedLogManager, String> comment;
    public static volatile SingularAttribute<DataChangedLogManager, Integer> id;
    public static volatile SingularAttribute<DataChangedLogManager, TableManager> tableManagerId;
    public static volatile SingularAttribute<DataChangedLogManager, String> newData;

}