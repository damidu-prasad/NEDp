/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Thilini Madagama
 */
@Entity
@Table(name = "general_user_profile")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GeneralUserProfile.findAll", query = "SELECT g FROM GeneralUserProfile g")
    , @NamedQuery(name = "GeneralUserProfile.findById", query = "SELECT g FROM GeneralUserProfile g WHERE g.id = :id")
    , @NamedQuery(name = "GeneralUserProfile.findByNic", query = "SELECT g FROM GeneralUserProfile g WHERE g.nic = :nic")
    , @NamedQuery(name = "GeneralUserProfile.findByChildNo", query = "SELECT g FROM GeneralUserProfile g WHERE g.childNo = :childNo")
    , @NamedQuery(name = "GeneralUserProfile.findByTitle", query = "SELECT g FROM GeneralUserProfile g WHERE g.title = :title")
    , @NamedQuery(name = "GeneralUserProfile.findByMidName", query = "SELECT g FROM GeneralUserProfile g WHERE g.midName = :midName")
    , @NamedQuery(name = "GeneralUserProfile.findByAddress1", query = "SELECT g FROM GeneralUserProfile g WHERE g.address1 = :address1")
    , @NamedQuery(name = "GeneralUserProfile.findByAddress2", query = "SELECT g FROM GeneralUserProfile g WHERE g.address2 = :address2")
    , @NamedQuery(name = "GeneralUserProfile.findByAddress3", query = "SELECT g FROM GeneralUserProfile g WHERE g.address3 = :address3")
    , @NamedQuery(name = "GeneralUserProfile.findByCompany", query = "SELECT g FROM GeneralUserProfile g WHERE g.company = :company")
    , @NamedQuery(name = "GeneralUserProfile.findByDob", query = "SELECT g FROM GeneralUserProfile g WHERE g.dob = :dob")
    , @NamedQuery(name = "GeneralUserProfile.findByOfficePhone", query = "SELECT g FROM GeneralUserProfile g WHERE g.officePhone = :officePhone")
    , @NamedQuery(name = "GeneralUserProfile.findByHomePhone", query = "SELECT g FROM GeneralUserProfile g WHERE g.homePhone = :homePhone")
    , @NamedQuery(name = "GeneralUserProfile.findByMobilePhone", query = "SELECT g FROM GeneralUserProfile g WHERE g.mobilePhone = :mobilePhone")
    , @NamedQuery(name = "GeneralUserProfile.findByIp", query = "SELECT g FROM GeneralUserProfile g WHERE g.ip = :ip")
    , @NamedQuery(name = "GeneralUserProfile.findByImg", query = "SELECT g FROM GeneralUserProfile g WHERE g.img = :img")
    , @NamedQuery(name = "GeneralUserProfile.findByProfileCreatedDate", query = "SELECT g FROM GeneralUserProfile g WHERE g.profileCreatedDate = :profileCreatedDate")
    , @NamedQuery(name = "GeneralUserProfile.findByIsActive", query = "SELECT g FROM GeneralUserProfile g WHERE g.isActive = :isActive")
    , @NamedQuery(name = "GeneralUserProfile.findByFirstName", query = "SELECT g FROM GeneralUserProfile g WHERE g.firstName = :firstName")
    , @NamedQuery(name = "GeneralUserProfile.findByLastName", query = "SELECT g FROM GeneralUserProfile g WHERE g.lastName = :lastName")
    , @NamedQuery(name = "GeneralUserProfile.findByEmail", query = "SELECT g FROM GeneralUserProfile g WHERE g.email = :email")
    , @NamedQuery(name = "GeneralUserProfile.findByNameWithIn", query = "SELECT g FROM GeneralUserProfile g WHERE g.nameWithIn = :nameWithIn")
    , @NamedQuery(name = "GeneralUserProfile.findByLicenseNo", query = "SELECT g FROM GeneralUserProfile g WHERE g.licenseNo = :licenseNo")
    , @NamedQuery(name = "GeneralUserProfile.findBySkype", query = "SELECT g FROM GeneralUserProfile g WHERE g.skype = :skype")
    , @NamedQuery(name = "GeneralUserProfile.findBySignature", query = "SELECT g FROM GeneralUserProfile g WHERE g.signature = :signature")
    , @NamedQuery(name = "GeneralUserProfile.findByIsMailSubscribed", query = "SELECT g FROM GeneralUserProfile g WHERE g.isMailSubscribed = :isMailSubscribed")
    , @NamedQuery(name = "GeneralUserProfile.findByIsSmsSubscribed", query = "SELECT g FROM GeneralUserProfile g WHERE g.isSmsSubscribed = :isSmsSubscribed")
    , @NamedQuery(name = "GeneralUserProfile.findByOccupationIdO", query = "SELECT g FROM GeneralUserProfile g WHERE g.occupationIdO = :occupationIdO")})
public class GeneralUserProfile implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generalUserProfileId")
    private Collection<Employee> employeeCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "enteredBy")
    private Collection<StudentMarks> studentMarksCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generalUserProfileId")
    private Collection<Students> studentsCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "nic")
    private String nic;
    @Column(name = "child_no")
    private Integer childNo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title")
    private String title;
    @Size(max = 255)
    @Column(name = "mid_name")
    private String midName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "address1")
    private String address1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "address2")
    private String address2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "address3")
    private String address3;
    @Size(max = 255)
    @Column(name = "company")
    private String company;
    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;
    @Size(max = 255)
    @Column(name = "office_phone")
    private String officePhone;
    @Size(max = 255)
    @Column(name = "home_phone")
    private String homePhone;
    @Size(max = 255)
    @Column(name = "mobile_phone")
    private String mobilePhone;
    @Size(max = 255)
    @Column(name = "ip")
    private String ip;
    @Size(max = 255)
    @Column(name = "img")
    private String img;
    @Lob
    @Size(max = 65535)
    @Column(name = "about_me")
    private String aboutMe;
    @Column(name = "profile_created_date")
    @Temporal(TemporalType.DATE)
    private Date profileCreatedDate;
    @Column(name = "is_active")
    private Integer isActive;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "first_name")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "last_name")
    private String lastName;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 450)
    @Column(name = "name_with_in")
    private String nameWithIn;
    @Lob
    @Size(max = 65535)
    @Column(name = "family_background")
    private String familyBackground;
    @Size(max = 45)
    @Column(name = "license_no")
    private String licenseNo;
    @Size(max = 45)
    @Column(name = "skype")
    private String skype;
    @Size(max = 145)
    @Column(name = "signature")
    private String signature;
    @Column(name = "is_mail_subscribed")
    private Integer isMailSubscribed;
    @Column(name = "is_sms_subscribed")
    private Integer isSmsSubscribed;
    @Column(name = "occupation_id_o")
    private Integer occupationIdO;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generalUserProfileId")
    private Collection<Teacher> teacherCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generalUserProfileId")
    private Collection<UserLogin> userLoginCollection;
    @JoinColumn(name = "civil_status_id", referencedColumnName = "id")
    @ManyToOne
    private CivilStatus civilStatusId;
    @JoinColumn(name = "country_id_c", referencedColumnName = "id")
    @ManyToOne
    private Country countryIdC;
    @JoinColumn(name = "education_level_id", referencedColumnName = "id")
    @ManyToOne
    private EducationLevel educationLevelId;
    @JoinColumn(name = "employeement_status_id", referencedColumnName = "id")
    @ManyToOne
    private EmployeementStatus employeementStatusId;
    @JoinColumn(name = "gender_id", referencedColumnName = "id")
    @ManyToOne
    private Gender genderId;
    @JoinColumn(name = "general_organization_profile_id_gop", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GeneralOrganizationProfile generalOrganizationProfileIdGop;
    @JoinColumn(name = "profession_id", referencedColumnName = "id")
    @ManyToOne
    private Profession professionId;
    @JoinColumn(name = "religion_id", referencedColumnName = "id")
    @ManyToOne
    private Religion religionId;
    @OneToMany(mappedBy = "generalUserProfileGupId")
    private Collection<GeneralUserProfile> generalUserProfileCollection;
    @JoinColumn(name = "general_user_profile_gup_id", referencedColumnName = "id")
    @ManyToOne
    private GeneralUserProfile generalUserProfileGupId;
    @JoinColumn(name = "industry_id", referencedColumnName = "id")
    @ManyToOne
    private Industry industryId;
    @JoinColumn(name = "languages_id", referencedColumnName = "id")
    @ManyToOne
    private Languages languagesId;
    @JoinColumn(name = "nationality_id", referencedColumnName = "id")
    @ManyToOne
    private Nationality nationalityId;
    @JoinColumn(name = "political_views_id", referencedColumnName = "id")
    @ManyToOne
    private PoliticalViews politicalViewsId;

    public GeneralUserProfile() {
    }

    public GeneralUserProfile(Integer id) {
        this.id = id;
    }

    public GeneralUserProfile(Integer id, String title, String address1, String address2, String address3, String firstName, String lastName) {
        this.id = id;
        this.title = title;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public Integer getChildNo() {
        return childNo;
    }

    public void setChildNo(Integer childNo) {
        this.childNo = childNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMidName() {
        return midName;
    }

    public void setMidName(String midName) {
        this.midName = midName;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public Date getProfileCreatedDate() {
        return profileCreatedDate;
    }

    public void setProfileCreatedDate(Date profileCreatedDate) {
        this.profileCreatedDate = profileCreatedDate;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNameWithIn() {
        return nameWithIn;
    }

    public void setNameWithIn(String nameWithIn) {
        this.nameWithIn = nameWithIn;
    }

    public String getFamilyBackground() {
        return familyBackground;
    }

    public void setFamilyBackground(String familyBackground) {
        this.familyBackground = familyBackground;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getIsMailSubscribed() {
        return isMailSubscribed;
    }

    public void setIsMailSubscribed(Integer isMailSubscribed) {
        this.isMailSubscribed = isMailSubscribed;
    }

    public Integer getIsSmsSubscribed() {
        return isSmsSubscribed;
    }

    public void setIsSmsSubscribed(Integer isSmsSubscribed) {
        this.isSmsSubscribed = isSmsSubscribed;
    }

    public Integer getOccupationIdO() {
        return occupationIdO;
    }

    public void setOccupationIdO(Integer occupationIdO) {
        this.occupationIdO = occupationIdO;
    }

    @XmlTransient
    public Collection<Teacher> getTeacherCollection() {
        return teacherCollection;
    }

    public void setTeacherCollection(Collection<Teacher> teacherCollection) {
        this.teacherCollection = teacherCollection;
    }

    @XmlTransient
    public Collection<UserLogin> getUserLoginCollection() {
        return userLoginCollection;
    }

    public void setUserLoginCollection(Collection<UserLogin> userLoginCollection) {
        this.userLoginCollection = userLoginCollection;
    }

    public CivilStatus getCivilStatusId() {
        return civilStatusId;
    }

    public void setCivilStatusId(CivilStatus civilStatusId) {
        this.civilStatusId = civilStatusId;
    }

    public Country getCountryIdC() {
        return countryIdC;
    }

    public void setCountryIdC(Country countryIdC) {
        this.countryIdC = countryIdC;
    }

    public EducationLevel getEducationLevelId() {
        return educationLevelId;
    }

    public void setEducationLevelId(EducationLevel educationLevelId) {
        this.educationLevelId = educationLevelId;
    }

    public EmployeementStatus getEmployeementStatusId() {
        return employeementStatusId;
    }

    public void setEmployeementStatusId(EmployeementStatus employeementStatusId) {
        this.employeementStatusId = employeementStatusId;
    }

    public Gender getGenderId() {
        return genderId;
    }

    public void setGenderId(Gender genderId) {
        this.genderId = genderId;
    }

    public GeneralOrganizationProfile getGeneralOrganizationProfileIdGop() {
        return generalOrganizationProfileIdGop;
    }

    public void setGeneralOrganizationProfileIdGop(GeneralOrganizationProfile generalOrganizationProfileIdGop) {
        this.generalOrganizationProfileIdGop = generalOrganizationProfileIdGop;
    }

    public Profession getProfessionId() {
        return professionId;
    }

    public void setProfessionId(Profession professionId) {
        this.professionId = professionId;
    }

    public Religion getReligionId() {
        return religionId;
    }

    public void setReligionId(Religion religionId) {
        this.religionId = religionId;
    }

    @XmlTransient
    public Collection<GeneralUserProfile> getGeneralUserProfileCollection() {
        return generalUserProfileCollection;
    }

    public void setGeneralUserProfileCollection(Collection<GeneralUserProfile> generalUserProfileCollection) {
        this.generalUserProfileCollection = generalUserProfileCollection;
    }

    public GeneralUserProfile getGeneralUserProfileGupId() {
        return generalUserProfileGupId;
    }

    public void setGeneralUserProfileGupId(GeneralUserProfile generalUserProfileGupId) {
        this.generalUserProfileGupId = generalUserProfileGupId;
    }

    public Industry getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Industry industryId) {
        this.industryId = industryId;
    }

    public Languages getLanguagesId() {
        return languagesId;
    }

    public void setLanguagesId(Languages languagesId) {
        this.languagesId = languagesId;
    }

    public Nationality getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(Nationality nationalityId) {
        this.nationalityId = nationalityId;
    }

    public PoliticalViews getPoliticalViewsId() {
        return politicalViewsId;
    }

    public void setPoliticalViewsId(PoliticalViews politicalViewsId) {
        this.politicalViewsId = politicalViewsId;
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
        if (!(object instanceof GeneralUserProfile)) {
            return false;
        }
        GeneralUserProfile other = (GeneralUserProfile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.GeneralUserProfile[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<Students> getStudentsCollection() {
        return studentsCollection;
    }

    public void setStudentsCollection(Collection<Students> studentsCollection) {
        this.studentsCollection = studentsCollection;
    }

    @XmlTransient
    public Collection<StudentMarks> getStudentMarksCollection() {
        return studentMarksCollection;
    }

    public void setStudentMarksCollection(Collection<StudentMarks> studentMarksCollection) {
        this.studentMarksCollection = studentMarksCollection;
    }

    @XmlTransient
    public Collection<Employee> getEmployeeCollection() {
        return employeeCollection;
    }

    public void setEmployeeCollection(Collection<Employee> employeeCollection) {
        this.employeeCollection = employeeCollection;
    }
    
}
