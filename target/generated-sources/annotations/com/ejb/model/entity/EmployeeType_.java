package com.ejb.model.entity;

import com.ejb.model.entity.Employee;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-03T19:27:51")
@StaticMetamodel(EmployeeType.class)
public class EmployeeType_ { 

    public static volatile SingularAttribute<EmployeeType, String> name;
    public static volatile CollectionAttribute<EmployeeType, Employee> employeeCollection;
    public static volatile SingularAttribute<EmployeeType, Integer> id;

}