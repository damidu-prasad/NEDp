package com.ejb.model.entity;

import com.ejb.model.entity.CivilStatus;
import com.ejb.model.entity.Country;
import com.ejb.model.entity.EducationLevel;
import com.ejb.model.entity.Employee;
import com.ejb.model.entity.EmployeementStatus;
import com.ejb.model.entity.Gender;
import com.ejb.model.entity.GeneralOrganizationProfile;
import com.ejb.model.entity.GeneralUserProfile;
import com.ejb.model.entity.Industry;
import com.ejb.model.entity.Languages;
import com.ejb.model.entity.Nationality;
import com.ejb.model.entity.PoliticalViews;
import com.ejb.model.entity.Profession;
import com.ejb.model.entity.Religion;
import com.ejb.model.entity.StudentMarks;
import com.ejb.model.entity.Students;
import com.ejb.model.entity.Teacher;
import com.ejb.model.entity.UserLogin;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-03T19:27:51")
@StaticMetamodel(GeneralUserProfile.class)
public class GeneralUserProfile_ { 

    public static volatile SingularAttribute<GeneralUserProfile, String> lastName;
    public static volatile SingularAttribute<GeneralUserProfile, String> licenseNo;
    public static volatile CollectionAttribute<GeneralUserProfile, GeneralUserProfile> generalUserProfileCollection;
    public static volatile SingularAttribute<GeneralUserProfile, String> img;
    public static volatile SingularAttribute<GeneralUserProfile, PoliticalViews> politicalViewsId;
    public static volatile SingularAttribute<GeneralUserProfile, String> signature;
    public static volatile SingularAttribute<GeneralUserProfile, Integer> childNo;
    public static volatile SingularAttribute<GeneralUserProfile, GeneralOrganizationProfile> generalOrganizationProfileIdGop;
    public static volatile SingularAttribute<GeneralUserProfile, Country> countryIdC;
    public static volatile SingularAttribute<GeneralUserProfile, String> nic;
    public static volatile SingularAttribute<GeneralUserProfile, Integer> isActive;
    public static volatile SingularAttribute<GeneralUserProfile, String> title;
    public static volatile SingularAttribute<GeneralUserProfile, Religion> religionId;
    public static volatile SingularAttribute<GeneralUserProfile, Integer> isMailSubscribed;
    public static volatile SingularAttribute<GeneralUserProfile, String> aboutMe;
    public static volatile SingularAttribute<GeneralUserProfile, String> familyBackground;
    public static volatile SingularAttribute<GeneralUserProfile, Integer> isSmsSubscribed;
    public static volatile SingularAttribute<GeneralUserProfile, String> skype;
    public static volatile SingularAttribute<GeneralUserProfile, Industry> industryId;
    public static volatile CollectionAttribute<GeneralUserProfile, UserLogin> userLoginCollection;
    public static volatile SingularAttribute<GeneralUserProfile, String> nameWithIn;
    public static volatile SingularAttribute<GeneralUserProfile, String> officePhone;
    public static volatile SingularAttribute<GeneralUserProfile, GeneralUserProfile> generalUserProfileGupId;
    public static volatile CollectionAttribute<GeneralUserProfile, Employee> employeeCollection;
    public static volatile SingularAttribute<GeneralUserProfile, String> company;
    public static volatile SingularAttribute<GeneralUserProfile, Integer> id;
    public static volatile SingularAttribute<GeneralUserProfile, String> email;
    public static volatile SingularAttribute<GeneralUserProfile, EducationLevel> educationLevelId;
    public static volatile SingularAttribute<GeneralUserProfile, Profession> professionId;
    public static volatile SingularAttribute<GeneralUserProfile, Integer> occupationIdO;
    public static volatile SingularAttribute<GeneralUserProfile, CivilStatus> civilStatusId;
    public static volatile SingularAttribute<GeneralUserProfile, String> address3;
    public static volatile SingularAttribute<GeneralUserProfile, String> address2;
    public static volatile SingularAttribute<GeneralUserProfile, String> address1;
    public static volatile CollectionAttribute<GeneralUserProfile, Students> studentsCollection;
    public static volatile SingularAttribute<GeneralUserProfile, String> homePhone;
    public static volatile SingularAttribute<GeneralUserProfile, String> ip;
    public static volatile SingularAttribute<GeneralUserProfile, Nationality> nationalityId;
    public static volatile SingularAttribute<GeneralUserProfile, Gender> genderId;
    public static volatile SingularAttribute<GeneralUserProfile, String> midName;
    public static volatile SingularAttribute<GeneralUserProfile, Date> profileCreatedDate;
    public static volatile SingularAttribute<GeneralUserProfile, String> firstName;
    public static volatile SingularAttribute<GeneralUserProfile, EmployeementStatus> employeementStatusId;
    public static volatile SingularAttribute<GeneralUserProfile, String> mobilePhone;
    public static volatile SingularAttribute<GeneralUserProfile, Date> dob;
    public static volatile CollectionAttribute<GeneralUserProfile, StudentMarks> studentMarksCollection;
    public static volatile SingularAttribute<GeneralUserProfile, Languages> languagesId;
    public static volatile CollectionAttribute<GeneralUserProfile, Teacher> teacherCollection;

}