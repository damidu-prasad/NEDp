package com.ejb.model.entity;

import com.ejb.model.entity.Designation;
import com.ejb.model.entity.DesignationGrade;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-01T15:32:18")
@StaticMetamodel(DesignationLevel.class)
public class DesignationLevel_ { 

    public static volatile CollectionAttribute<DesignationLevel, Designation> designationCollection;
    public static volatile SingularAttribute<DesignationLevel, String> name;
    public static volatile SingularAttribute<DesignationLevel, Integer> id;
    public static volatile CollectionAttribute<DesignationLevel, DesignationGrade> designationGradeCollection;

}