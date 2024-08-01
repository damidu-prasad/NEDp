package com.ejb.model.entity;

import com.ejb.model.entity.Designation;
import com.ejb.model.entity.DesignationGrade;
import com.ejb.model.entity.EmployeeCategory;
import com.ejb.model.entity.EmployeePermanentStstus;
import com.ejb.model.entity.EmployeeType;
import com.ejb.model.entity.GeneralOrganizationProfile;
import com.ejb.model.entity.GeneralUserProfile;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-01T15:32:19")
@StaticMetamodel(Employee.class)
public class Employee_ { 

    public static volatile SingularAttribute<Employee, String> dayOff;
    public static volatile SingularAttribute<Employee, EmployeeCategory> employeeCategoryId;
    public static volatile SingularAttribute<Employee, Double> pettyCashLevel;
    public static volatile SingularAttribute<Employee, GeneralOrganizationProfile> generalOrganizationProfileId;
    public static volatile SingularAttribute<Employee, Date> resignDate;
    public static volatile SingularAttribute<Employee, Date> regDate;
    public static volatile SingularAttribute<Employee, String> description;
    public static volatile SingularAttribute<Employee, Integer> isActive;
    public static volatile SingularAttribute<Employee, EmployeeType> employeeTypeId;
    public static volatile SingularAttribute<Employee, Integer> isValidLoan;
    public static volatile SingularAttribute<Employee, String> number;
    public static volatile SingularAttribute<Employee, Double> ignoreStartTimePeriod;
    public static volatile SingularAttribute<Employee, Integer> annualLeaves;
    public static volatile SingularAttribute<Employee, String> oldRegNo;
    public static volatile SingularAttribute<Employee, Date> verifiedDate;
    public static volatile SingularAttribute<Employee, String> insuranceId;
    public static volatile SingularAttribute<Employee, Date> startTime;
    public static volatile SingularAttribute<Employee, Integer> id;
    public static volatile SingularAttribute<Employee, GeneralUserProfile> generalUserProfileId;
    public static volatile SingularAttribute<Employee, Designation> designationId;
    public static volatile SingularAttribute<Employee, DesignationGrade> designationGradeId;
    public static volatile SingularAttribute<Employee, Integer> fpId;
    public static volatile SingularAttribute<Employee, Double> basicSalary;
    public static volatile SingularAttribute<Employee, Date> endTime;
    public static volatile SingularAttribute<Employee, String> epfNo;
    public static volatile SingularAttribute<Employee, EmployeePermanentStstus> employeePermanentStstusId;

}