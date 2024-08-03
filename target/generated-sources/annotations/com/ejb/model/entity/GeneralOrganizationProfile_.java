package com.ejb.model.entity;

import com.ejb.model.entity.Country;
import com.ejb.model.entity.EducationDivision;
import com.ejb.model.entity.EducationZone;
import com.ejb.model.entity.Employee;
import com.ejb.model.entity.GeneralUserProfile;
import com.ejb.model.entity.LoginSession;
import com.ejb.model.entity.OrgSystemInterfaceController;
import com.ejb.model.entity.OrganizationType;
import com.ejb.model.entity.OrganizationTypeManager;
import com.ejb.model.entity.Province;
import com.ejb.model.entity.RegistrationType;
import com.ejb.model.entity.School;
import com.ejb.model.entity.UserLogin;
import com.ejb.model.entity.UserLoginGroup;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-08-03T19:27:51")
@StaticMetamodel(GeneralOrganizationProfile.class)
public class GeneralOrganizationProfile_ { 

    public static volatile CollectionAttribute<GeneralOrganizationProfile, OrgSystemInterfaceController> orgSystemInterfaceControllerCollection;
    public static volatile CollectionAttribute<GeneralOrganizationProfile, GeneralUserProfile> generalUserProfileCollection;
    public static volatile SingularAttribute<GeneralOrganizationProfile, String> code;
    public static volatile SingularAttribute<GeneralOrganizationProfile, RegistrationType> registrationTypeId;
    public static volatile SingularAttribute<GeneralOrganizationProfile, String> postalCode;
    public static volatile SingularAttribute<GeneralOrganizationProfile, Country> countryIdC;
    public static volatile CollectionAttribute<GeneralOrganizationProfile, UserLogin> userLoginCollection;
    public static volatile CollectionAttribute<GeneralOrganizationProfile, OrganizationTypeManager> organizationTypeManagerCollection;
    public static volatile CollectionAttribute<GeneralOrganizationProfile, Employee> employeeCollection;
    public static volatile SingularAttribute<GeneralOrganizationProfile, String> logo;
    public static volatile CollectionAttribute<GeneralOrganizationProfile, EducationDivision> educationDivisionCollection;
    public static volatile SingularAttribute<GeneralOrganizationProfile, Integer> id;
    public static volatile SingularAttribute<GeneralOrganizationProfile, String> fax;
    public static volatile SingularAttribute<GeneralOrganizationProfile, String> email;
    public static volatile SingularAttribute<GeneralOrganizationProfile, String> website;
    public static volatile SingularAttribute<GeneralOrganizationProfile, String> address3;
    public static volatile SingularAttribute<GeneralOrganizationProfile, String> address2;
    public static volatile SingularAttribute<GeneralOrganizationProfile, String> phoneOther;
    public static volatile CollectionAttribute<GeneralOrganizationProfile, LoginSession> loginSessionCollection;
    public static volatile SingularAttribute<GeneralOrganizationProfile, String> address1;
    public static volatile CollectionAttribute<GeneralOrganizationProfile, School> schoolCollection;
    public static volatile SingularAttribute<GeneralOrganizationProfile, String> mision;
    public static volatile SingularAttribute<GeneralOrganizationProfile, String> moto;
    public static volatile CollectionAttribute<GeneralOrganizationProfile, OrganizationTypeManager> organizationTypeManagerCollection1;
    public static volatile SingularAttribute<GeneralOrganizationProfile, String> vision;
    public static volatile SingularAttribute<GeneralOrganizationProfile, String> phone;
    public static volatile CollectionAttribute<GeneralOrganizationProfile, UserLoginGroup> userLoginGroupCollection;
    public static volatile SingularAttribute<GeneralOrganizationProfile, String> registrationNo;
    public static volatile SingularAttribute<GeneralOrganizationProfile, String> name;
    public static volatile SingularAttribute<GeneralOrganizationProfile, String> header;
    public static volatile SingularAttribute<GeneralOrganizationProfile, OrganizationType> organizationTypeId;
    public static volatile CollectionAttribute<GeneralOrganizationProfile, EducationZone> educationZoneCollection;
    public static volatile SingularAttribute<GeneralOrganizationProfile, String> objectives;
    public static volatile CollectionAttribute<GeneralOrganizationProfile, Province> provinceCollection;

}