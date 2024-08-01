/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.model.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Thilini Madagama
 */
@Entity
@Table(name = "general_organization_profile")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GeneralOrganizationProfile.findAll", query = "SELECT g FROM GeneralOrganizationProfile g")
    , @NamedQuery(name = "GeneralOrganizationProfile.findById", query = "SELECT g FROM GeneralOrganizationProfile g WHERE g.id = :id")
    , @NamedQuery(name = "GeneralOrganizationProfile.findByCode", query = "SELECT g FROM GeneralOrganizationProfile g WHERE g.code = :code")
    , @NamedQuery(name = "GeneralOrganizationProfile.findByName", query = "SELECT g FROM GeneralOrganizationProfile g WHERE g.name = :name")
    , @NamedQuery(name = "GeneralOrganizationProfile.findByAddress1", query = "SELECT g FROM GeneralOrganizationProfile g WHERE g.address1 = :address1")
    , @NamedQuery(name = "GeneralOrganizationProfile.findByAddress2", query = "SELECT g FROM GeneralOrganizationProfile g WHERE g.address2 = :address2")
    , @NamedQuery(name = "GeneralOrganizationProfile.findByAddress3", query = "SELECT g FROM GeneralOrganizationProfile g WHERE g.address3 = :address3")
    , @NamedQuery(name = "GeneralOrganizationProfile.findByEmail", query = "SELECT g FROM GeneralOrganizationProfile g WHERE g.email = :email")
    , @NamedQuery(name = "GeneralOrganizationProfile.findByPhone", query = "SELECT g FROM GeneralOrganizationProfile g WHERE g.phone = :phone")
    , @NamedQuery(name = "GeneralOrganizationProfile.findByPhoneOther", query = "SELECT g FROM GeneralOrganizationProfile g WHERE g.phoneOther = :phoneOther")
    , @NamedQuery(name = "GeneralOrganizationProfile.findByVision", query = "SELECT g FROM GeneralOrganizationProfile g WHERE g.vision = :vision")
    , @NamedQuery(name = "GeneralOrganizationProfile.findByMision", query = "SELECT g FROM GeneralOrganizationProfile g WHERE g.mision = :mision")
    , @NamedQuery(name = "GeneralOrganizationProfile.findByRegistrationNo", query = "SELECT g FROM GeneralOrganizationProfile g WHERE g.registrationNo = :registrationNo")
    , @NamedQuery(name = "GeneralOrganizationProfile.findByWebsite", query = "SELECT g FROM GeneralOrganizationProfile g WHERE g.website = :website")
    , @NamedQuery(name = "GeneralOrganizationProfile.findByFax", query = "SELECT g FROM GeneralOrganizationProfile g WHERE g.fax = :fax")
    , @NamedQuery(name = "GeneralOrganizationProfile.findByObjectives", query = "SELECT g FROM GeneralOrganizationProfile g WHERE g.objectives = :objectives")
    , @NamedQuery(name = "GeneralOrganizationProfile.findByMoto", query = "SELECT g FROM GeneralOrganizationProfile g WHERE g.moto = :moto")
    , @NamedQuery(name = "GeneralOrganizationProfile.findByLogo", query = "SELECT g FROM GeneralOrganizationProfile g WHERE g.logo = :logo")
    , @NamedQuery(name = "GeneralOrganizationProfile.findByHeader", query = "SELECT g FROM GeneralOrganizationProfile g WHERE g.header = :header")
    , @NamedQuery(name = "GeneralOrganizationProfile.findByPostalCode", query = "SELECT g FROM GeneralOrganizationProfile g WHERE g.postalCode = :postalCode")})
public class GeneralOrganizationProfile implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generalOrganizationProfileId")
    private Collection<Employee> employeeCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generalOrganizationProfileId")
    private Collection<Province> provinceCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generalOrganizationProfileId")
    private Collection<EducationDivision> educationDivisionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generalOrganizationProfileId")
    private Collection<EducationZone> educationZoneCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generalOrganizationProfileId")
    private Collection<OrgSystemInterfaceController> orgSystemInterfaceControllerCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generalOrganizationProfileId")
    private Collection<UserLoginGroup> userLoginGroupCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generalOrganizationProfileId")
    private Collection<OrganizationTypeManager> organizationTypeManagerCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generalOrganizationProfileId1")
    private Collection<OrganizationTypeManager> organizationTypeManagerCollection1;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 10)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 450)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 450)
    @Column(name = "address1")
    private String address1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 450)
    @Column(name = "address2")
    private String address2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 450)
    @Column(name = "address3")
    private String address3;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 45)
    @Column(name = "email")
    private String email;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "phone")
    private String phone;
    @Size(max = 45)
    @Column(name = "phone_other")
    private String phoneOther;
    @Size(max = 45)
    @Column(name = "vision")
    private String vision;
    @Size(max = 45)
    @Column(name = "mision")
    private String mision;
    @Size(max = 45)
    @Column(name = "registration_no")
    private String registrationNo;
    @Size(max = 45)
    @Column(name = "website")
    private String website;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 45)
    @Column(name = "fax")
    private String fax;
    @Size(max = 45)
    @Column(name = "objectives")
    private String objectives;
    @Size(max = 45)
    @Column(name = "moto")
    private String moto;
    @Size(max = 45)
    @Column(name = "logo")
    private String logo;
    @Size(max = 45)
    @Column(name = "header")
    private String header;
    @Size(max = 45)
    @Column(name = "postal_code")
    private String postalCode;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generalOrganizationProfileId")
    private Collection<School> schoolCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generalOrganizationProfileId")
    private Collection<UserLogin> userLoginCollection;
    @JoinColumn(name = "country_id_c", referencedColumnName = "id")
    @ManyToOne
    private Country countryIdC;
    @JoinColumn(name = "organization_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private OrganizationType organizationTypeId;
    @JoinColumn(name = "registration_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RegistrationType registrationTypeId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generalOrganizationProfileId")
    private Collection<LoginSession> loginSessionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generalOrganizationProfileIdGop")
    private Collection<GeneralUserProfile> generalUserProfileCollection;

    public GeneralOrganizationProfile() {
    }

    public GeneralOrganizationProfile(Integer id) {
        this.id = id;
    }

    public GeneralOrganizationProfile(Integer id, String name, String address1, String address2, String address3, String phone) {
        this.id = id;
        this.name = name;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneOther() {
        return phoneOther;
    }

    public void setPhoneOther(String phoneOther) {
        this.phoneOther = phoneOther;
    }

    public String getVision() {
        return vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public String getMision() {
        return mision;
    }

    public void setMision(String mision) {
        this.mision = mision;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getMoto() {
        return moto;
    }

    public void setMoto(String moto) {
        this.moto = moto;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @XmlTransient
    public Collection<School> getSchoolCollection() {
        return schoolCollection;
    }

    public void setSchoolCollection(Collection<School> schoolCollection) {
        this.schoolCollection = schoolCollection;
    }

    @XmlTransient
    public Collection<UserLogin> getUserLoginCollection() {
        return userLoginCollection;
    }

    public void setUserLoginCollection(Collection<UserLogin> userLoginCollection) {
        this.userLoginCollection = userLoginCollection;
    }

    public Country getCountryIdC() {
        return countryIdC;
    }

    public void setCountryIdC(Country countryIdC) {
        this.countryIdC = countryIdC;
    }

    public OrganizationType getOrganizationTypeId() {
        return organizationTypeId;
    }

    public void setOrganizationTypeId(OrganizationType organizationTypeId) {
        this.organizationTypeId = organizationTypeId;
    }

    public RegistrationType getRegistrationTypeId() {
        return registrationTypeId;
    }

    public void setRegistrationTypeId(RegistrationType registrationTypeId) {
        this.registrationTypeId = registrationTypeId;
    }

    @XmlTransient
    public Collection<LoginSession> getLoginSessionCollection() {
        return loginSessionCollection;
    }

    public void setLoginSessionCollection(Collection<LoginSession> loginSessionCollection) {
        this.loginSessionCollection = loginSessionCollection;
    }

    @XmlTransient
    public Collection<GeneralUserProfile> getGeneralUserProfileCollection() {
        return generalUserProfileCollection;
    }

    public void setGeneralUserProfileCollection(Collection<GeneralUserProfile> generalUserProfileCollection) {
        this.generalUserProfileCollection = generalUserProfileCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GeneralOrganizationProfile)) {
            return false;
        }
        GeneralOrganizationProfile other = (GeneralOrganizationProfile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.GeneralOrganizationProfile[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<OrganizationTypeManager> getOrganizationTypeManagerCollection() {
        return organizationTypeManagerCollection;
    }

    public void setOrganizationTypeManagerCollection(Collection<OrganizationTypeManager> organizationTypeManagerCollection) {
        this.organizationTypeManagerCollection = organizationTypeManagerCollection;
    }

    @XmlTransient
    public Collection<OrganizationTypeManager> getOrganizationTypeManagerCollection1() {
        return organizationTypeManagerCollection1;
    }

    public void setOrganizationTypeManagerCollection1(Collection<OrganizationTypeManager> organizationTypeManagerCollection1) {
        this.organizationTypeManagerCollection1 = organizationTypeManagerCollection1;
    }

    @XmlTransient
    public Collection<UserLoginGroup> getUserLoginGroupCollection() {
        return userLoginGroupCollection;
    }

    public void setUserLoginGroupCollection(Collection<UserLoginGroup> userLoginGroupCollection) {
        this.userLoginGroupCollection = userLoginGroupCollection;
    }

    @XmlTransient
    public Collection<OrgSystemInterfaceController> getOrgSystemInterfaceControllerCollection() {
        return orgSystemInterfaceControllerCollection;
    }

    public void setOrgSystemInterfaceControllerCollection(Collection<OrgSystemInterfaceController> orgSystemInterfaceControllerCollection) {
        this.orgSystemInterfaceControllerCollection = orgSystemInterfaceControllerCollection;
    }

    @XmlTransient
    public Collection<Province> getProvinceCollection() {
        return provinceCollection;
    }

    public void setProvinceCollection(Collection<Province> provinceCollection) {
        this.provinceCollection = provinceCollection;
    }

    @XmlTransient
    public Collection<EducationDivision> getEducationDivisionCollection() {
        return educationDivisionCollection;
    }

    public void setEducationDivisionCollection(Collection<EducationDivision> educationDivisionCollection) {
        this.educationDivisionCollection = educationDivisionCollection;
    }

    @XmlTransient
    public Collection<EducationZone> getEducationZoneCollection() {
        return educationZoneCollection;
    }

    public void setEducationZoneCollection(Collection<EducationZone> educationZoneCollection) {
        this.educationZoneCollection = educationZoneCollection;
    }

    @XmlTransient
    public Collection<Employee> getEmployeeCollection() {
        return employeeCollection;
    }

    public void setEmployeeCollection(Collection<Employee> employeeCollection) {
        this.employeeCollection = employeeCollection;
    }
    
}
