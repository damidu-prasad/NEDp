package com.ejb.model.entity;

import com.ejb.model.entity.Designation;
import com.ejb.model.entity.DesignationLevel;
import com.ejb.model.entity.Employee;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-08T18:03:02")
@StaticMetamodel(DesignationGrade.class)
public class DesignationGrade_ { 

    public static volatile SingularAttribute<DesignationGrade, DesignationLevel> designationLevelId;
    public static volatile CollectionAttribute<DesignationGrade, Designation> designationCollection;
    public static volatile SingularAttribute<DesignationGrade, String> name;
    public static volatile CollectionAttribute<DesignationGrade, Employee> employeeCollection;
    public static volatile SingularAttribute<DesignationGrade, Integer> id;

}