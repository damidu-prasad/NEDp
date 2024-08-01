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
import javax.persistence.Lob;
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
@Table(name = "user_login")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserLogin.findAll", query = "SELECT u FROM UserLogin u")
    , @NamedQuery(name = "UserLogin.findById", query = "SELECT u FROM UserLogin u WHERE u.id = :id")
    , @NamedQuery(name = "UserLogin.findByUsername", query = "SELECT u FROM UserLogin u WHERE u.username = :username")
    , @NamedQuery(name = "UserLogin.findByPassword", query = "SELECT u FROM UserLogin u WHERE u.password = :password")
    , @NamedQuery(name = "UserLogin.findByIsActive", query = "SELECT u FROM UserLogin u WHERE u.isActive = :isActive")
    , @NamedQuery(name = "UserLogin.findByIsMultipleLogin", query = "SELECT u FROM UserLogin u WHERE u.isMultipleLogin = :isMultipleLogin")
    , @NamedQuery(name = "UserLogin.findByMaxLoginAttempt", query = "SELECT u FROM UserLogin u WHERE u.maxLoginAttempt = :maxLoginAttempt")
    , @NamedQuery(name = "UserLogin.findByGeneratedPassword", query = "SELECT u FROM UserLogin u WHERE u.generatedPassword = :generatedPassword")
    , @NamedQuery(name = "UserLogin.findByIsFirstTime", query = "SELECT u FROM UserLogin u WHERE u.isFirstTime = :isFirstTime")
    , @NamedQuery(name = "UserLogin.findByCountAttempt", query = "SELECT u FROM UserLogin u WHERE u.countAttempt = :countAttempt")})
public class UserLogin implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userLoginId")
    private Collection<DataChangedLogManager> dataChangedLogManagerCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userLoginId")
    private Collection<UserLoginGroup> userLoginGroupCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_active")
    private int isActive;
    @Column(name = "is_multiple_login")
    private Integer isMultipleLogin;
    @Column(name = "max_login_attempt")
    private Integer maxLoginAttempt;
    @Size(max = 255)
    @Column(name = "generated_password")
    private String generatedPassword;
    @Column(name = "is_first_time")
    private Integer isFirstTime;
    @Column(name = "count_attempt")
    private Integer countAttempt;
    @Lob
    @Size(max = 65535)
    @Column(name = "answer")
    private String answer;
    @JoinColumn(name = "general_organization_profile_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GeneralOrganizationProfile generalOrganizationProfileId;
    @JoinColumn(name = "general_user_profile_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GeneralUserProfile generalUserProfileId;
    @JoinColumn(name = "security_question_id", referencedColumnName = "id")
    @ManyToOne
    private SecurityQuestion securityQuestionId;
    @JoinColumn(name = "system_interface_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SystemInterface systemInterfaceId;
    @JoinColumn(name = "user_role_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserRole userRoleId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userLoginId")
    private Collection<LoginSession> loginSessionCollection;

    public UserLogin() {
    }

    public UserLogin(Integer id) {
        this.id = id;
    }

    public UserLogin(Integer id, String username, String password, int isActive) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public Integer getIsMultipleLogin() {
        return isMultipleLogin;
    }

    public void setIsMultipleLogin(Integer isMultipleLogin) {
        this.isMultipleLogin = isMultipleLogin;
    }

    public Integer getMaxLoginAttempt() {
        return maxLoginAttempt;
    }

    public void setMaxLoginAttempt(Integer maxLoginAttempt) {
        this.maxLoginAttempt = maxLoginAttempt;
    }

    public String getGeneratedPassword() {
        return generatedPassword;
    }

    public void setGeneratedPassword(String generatedPassword) {
        this.generatedPassword = generatedPassword;
    }

    public Integer getIsFirstTime() {
        return isFirstTime;
    }

    public void setIsFirstTime(Integer isFirstTime) {
        this.isFirstTime = isFirstTime;
    }

    public Integer getCountAttempt() {
        return countAttempt;
    }

    public void setCountAttempt(Integer countAttempt) {
        this.countAttempt = countAttempt;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public GeneralOrganizationProfile getGeneralOrganizationProfileId() {
        return generalOrganizationProfileId;
    }

    public void setGeneralOrganizationProfileId(GeneralOrganizationProfile generalOrganizationProfileId) {
        this.generalOrganizationProfileId = generalOrganizationProfileId;
    }

    public GeneralUserProfile getGeneralUserProfileId() {
        return generalUserProfileId;
    }

    public void setGeneralUserProfileId(GeneralUserProfile generalUserProfileId) {
        this.generalUserProfileId = generalUserProfileId;
    }

    public SecurityQuestion getSecurityQuestionId() {
        return securityQuestionId;
    }

    public void setSecurityQuestionId(SecurityQuestion securityQuestionId) {
        this.securityQuestionId = securityQuestionId;
    }

    public SystemInterface getSystemInterfaceId() {
        return systemInterfaceId;
    }

    public void setSystemInterfaceId(SystemInterface systemInterfaceId) {
        this.systemInterfaceId = systemInterfaceId;
    }

    public UserRole getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(UserRole userRoleId) {
        this.userRoleId = userRoleId;
    }

    @XmlTransient
    public Collection<LoginSession> getLoginSessionCollection() {
        return loginSessionCollection;
    }

    public void setLoginSessionCollection(Collection<LoginSession> loginSessionCollection) {
        this.loginSessionCollection = loginSessionCollection;
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
        if (!(object instanceof UserLogin)) {
            return false;
        }
        UserLogin other = (UserLogin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.UserLogin[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<UserLoginGroup> getUserLoginGroupCollection() {
        return userLoginGroupCollection;
    }

    public void setUserLoginGroupCollection(Collection<UserLoginGroup> userLoginGroupCollection) {
        this.userLoginGroupCollection = userLoginGroupCollection;
    }

    @XmlTransient
    public Collection<DataChangedLogManager> getDataChangedLogManagerCollection() {
        return dataChangedLogManagerCollection;
    }

    public void setDataChangedLogManagerCollection(Collection<DataChangedLogManager> dataChangedLogManagerCollection) {
        this.dataChangedLogManagerCollection = dataChangedLogManagerCollection;
    }
    
}
