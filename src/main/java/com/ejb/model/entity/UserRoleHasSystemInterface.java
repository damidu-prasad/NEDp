/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Thilini Madagama
 */
@Entity
@Table(name = "user_role_has_system_interface")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserRoleHasSystemInterface.findAll", query = "SELECT u FROM UserRoleHasSystemInterface u")
    , @NamedQuery(name = "UserRoleHasSystemInterface.findById", query = "SELECT u FROM UserRoleHasSystemInterface u WHERE u.id = :id")})
public class UserRoleHasSystemInterface implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "system_interface_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SystemInterface systemInterfaceId;
    @JoinColumn(name = "user_role_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserRole userRoleId;

    public UserRoleHasSystemInterface() {
    }

    public UserRoleHasSystemInterface(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserRoleHasSystemInterface)) {
            return false;
        }
        UserRoleHasSystemInterface other = (UserRoleHasSystemInterface) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.UserRoleHasSystemInterface[ id=" + id + " ]";
    }
    
}
