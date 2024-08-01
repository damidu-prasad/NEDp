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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "finger_print_region")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FingerPrintRegion.findAll", query = "SELECT f FROM FingerPrintRegion f")
    , @NamedQuery(name = "FingerPrintRegion.findById", query = "SELECT f FROM FingerPrintRegion f WHERE f.id = :id")
    , @NamedQuery(name = "FingerPrintRegion.findByName", query = "SELECT f FROM FingerPrintRegion f WHERE f.name = :name")})
public class FingerPrintRegion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fingerPrintRegionId")
    private Collection<FingerPrintRegionUser> fingerPrintRegionUserCollection;

    public FingerPrintRegion() {
    }

    public FingerPrintRegion(Integer id) {
        this.id = id;
    }

    public FingerPrintRegion(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<FingerPrintRegionUser> getFingerPrintRegionUserCollection() {
        return fingerPrintRegionUserCollection;
    }

    public void setFingerPrintRegionUserCollection(Collection<FingerPrintRegionUser> fingerPrintRegionUserCollection) {
        this.fingerPrintRegionUserCollection = fingerPrintRegionUserCollection;
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
        if (!(object instanceof FingerPrintRegion)) {
            return false;
        }
        FingerPrintRegion other = (FingerPrintRegion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.FingerPrintRegion[ id=" + id + " ]";
    }
    
}
