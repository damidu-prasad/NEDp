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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Thilini Madagama
 */
@Entity
@Table(name = "system_interface")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SystemInterface.findAll", query = "SELECT s FROM SystemInterface s")
    , @NamedQuery(name = "SystemInterface.findById", query = "SELECT s FROM SystemInterface s WHERE s.id = :id")
    , @NamedQuery(name = "SystemInterface.findByInterfaceName", query = "SELECT s FROM SystemInterface s WHERE s.interfaceName = :interfaceName")
    , @NamedQuery(name = "SystemInterface.findByDisplayName", query = "SELECT s FROM SystemInterface s WHERE s.displayName = :displayName")
    , @NamedQuery(name = "SystemInterface.findByUrl", query = "SELECT s FROM SystemInterface s WHERE s.url = :url")
    , @NamedQuery(name = "SystemInterface.findByStatus", query = "SELECT s FROM SystemInterface s WHERE s.status = :status")
    , @NamedQuery(name = "SystemInterface.findByIcon", query = "SELECT s FROM SystemInterface s WHERE s.icon = :icon")})
public class SystemInterface implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "systemInterfaceId")
    private Collection<OrgSystemInterfaceController> orgSystemInterfaceControllerCollection;

    @Lob
    @Size(max = 65535)
    @Column(name = "description")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "systemInterfaceId")
    private Collection<UserRoleHasSystemInterface> userRoleHasSystemInterfaceCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "interface_name")
    private String interfaceName;
    @Size(max = 255)
    @Column(name = "display_name")
    private String displayName;
    @Size(max = 255)
    @Column(name = "url")
    private String url;
    @Column(name = "status")
    private Integer status;
    @Size(max = 45)
    @Column(name = "icon")
    private String icon;
    @JoinColumn(name = "interface_menu_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InterfaceMenu interfaceMenuId;
    @JoinColumn(name = "interface_sub_menu_id", referencedColumnName = "id")
    @ManyToOne
    private InterfaceSubMenu interfaceSubMenuId;
    @OneToMany(mappedBy = "systemInterfaceId")
    private Collection<SystemInterface> systemInterfaceCollection;
    @JoinColumn(name = "system_interface_id", referencedColumnName = "id")
    @ManyToOne
    private SystemInterface systemInterfaceId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "systemInterfaceId")
    private Collection<UserLogin> userLoginCollection;

    public SystemInterface() {
    }

    public SystemInterface(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public InterfaceMenu getInterfaceMenuId() {
        return interfaceMenuId;
    }

    public void setInterfaceMenuId(InterfaceMenu interfaceMenuId) {
        this.interfaceMenuId = interfaceMenuId;
    }

    public InterfaceSubMenu getInterfaceSubMenuId() {
        return interfaceSubMenuId;
    }

    public void setInterfaceSubMenuId(InterfaceSubMenu interfaceSubMenuId) {
        this.interfaceSubMenuId = interfaceSubMenuId;
    }

    @XmlTransient
    public Collection<SystemInterface> getSystemInterfaceCollection() {
        return systemInterfaceCollection;
    }

    public void setSystemInterfaceCollection(Collection<SystemInterface> systemInterfaceCollection) {
        this.systemInterfaceCollection = systemInterfaceCollection;
    }

    public SystemInterface getSystemInterfaceId() {
        return systemInterfaceId;
    }

    public void setSystemInterfaceId(SystemInterface systemInterfaceId) {
        this.systemInterfaceId = systemInterfaceId;
    }

    @XmlTransient
    public Collection<UserLogin> getUserLoginCollection() {
        return userLoginCollection;
    }

    public void setUserLoginCollection(Collection<UserLogin> userLoginCollection) {
        this.userLoginCollection = userLoginCollection;
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
        if (!(object instanceof SystemInterface)) {
            return false;
        }
        SystemInterface other = (SystemInterface) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.SystemInterface[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<UserRoleHasSystemInterface> getUserRoleHasSystemInterfaceCollection() {
        return userRoleHasSystemInterfaceCollection;
    }

    public void setUserRoleHasSystemInterfaceCollection(Collection<UserRoleHasSystemInterface> userRoleHasSystemInterfaceCollection) {
        this.userRoleHasSystemInterfaceCollection = userRoleHasSystemInterfaceCollection;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Collection<OrgSystemInterfaceController> getOrgSystemInterfaceControllerCollection() {
        return orgSystemInterfaceControllerCollection;
    }

    public void setOrgSystemInterfaceControllerCollection(Collection<OrgSystemInterfaceController> orgSystemInterfaceControllerCollection) {
        this.orgSystemInterfaceControllerCollection = orgSystemInterfaceControllerCollection;
    }
    
}
