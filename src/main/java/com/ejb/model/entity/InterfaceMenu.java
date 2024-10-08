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
@Table(name = "interface_menu")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InterfaceMenu.findAll", query = "SELECT i FROM InterfaceMenu i")
    , @NamedQuery(name = "InterfaceMenu.findById", query = "SELECT i FROM InterfaceMenu i WHERE i.id = :id")
    , @NamedQuery(name = "InterfaceMenu.findByMenuName", query = "SELECT i FROM InterfaceMenu i WHERE i.menuName = :menuName")
    , @NamedQuery(name = "InterfaceMenu.findByIcon", query = "SELECT i FROM InterfaceMenu i WHERE i.icon = :icon")})
public class InterfaceMenu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "menu_name")
    private String menuName;
    @Size(max = 45)
    @Column(name = "icon")
    private String icon;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "interfaceMenuId")
    private Collection<SystemInterface> systemInterfaceCollection;

    public InterfaceMenu() {
    }

    public InterfaceMenu(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @XmlTransient
    public Collection<SystemInterface> getSystemInterfaceCollection() {
        return systemInterfaceCollection;
    }

    public void setSystemInterfaceCollection(Collection<SystemInterface> systemInterfaceCollection) {
        this.systemInterfaceCollection = systemInterfaceCollection;
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
        if (!(object instanceof InterfaceMenu)) {
            return false;
        }
        InterfaceMenu other = (InterfaceMenu) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.InterfaceMenu[ id=" + id + " ]";
    }
    
}
