package com.ejb.model.entity;

import com.ejb.model.entity.DesignationGrade;
import com.ejb.model.entity.DesignationLevel;
import com.ejb.model.entity.Employee;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-01T15:32:18")
@StaticMetamodel(Designation.class)
public class Designation_ { 

    public static volatile SingularAttribute<Designation, String> name;
    public static volatile CollectionAttribute<Designation, Employee> employeeCollection;
    public static volatile SingularAttribute<Designation, Integer> id;
    public static volatile SingularAttribute<Designation, DesignationGrade> designationGradeId;
    public static volatile SingularAttribute<Designation, DesignationLevel> designationLevelIdDl;

}