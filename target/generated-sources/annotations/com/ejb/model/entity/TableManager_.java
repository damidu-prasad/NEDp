package com.ejb.model.entity;

import com.ejb.model.entity.DataChangedLogManager;
import com.ejb.model.entity.PackageManager;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-08T18:03:02")
@StaticMetamodel(TableManager.class)
public class TableManager_ { 

    public static volatile SingularAttribute<TableManager, PackageManager> packageManagerId;
    public static volatile SingularAttribute<TableManager, String> name;
    public static volatile SingularAttribute<TableManager, Integer> id;
    public static volatile CollectionAttribute<TableManager, DataChangedLogManager> dataChangedLogManagerCollection;

}