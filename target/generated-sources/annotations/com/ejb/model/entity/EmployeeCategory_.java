package com.ejb.model.entity;

import com.ejb.model.entity.Employee;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-01T15:32:18")
@StaticMetamodel(EmployeeCategory.class)
public class EmployeeCategory_ { 

    public static volatile SingularAttribute<EmployeeCategory, String> name;
    public static volatile CollectionAttribute<EmployeeCategory, Employee> employeeCollection;
    public static volatile SingularAttribute<EmployeeCategory, Integer> id;

}