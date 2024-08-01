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
@Table(name = "education_zone")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EducationZone.findAll", query = "SELECT e FROM EducationZone e")
    , @NamedQuery(name = "EducationZone.findById", query = "SELECT e FROM EducationZone e WHERE e.id = :id")
    , @NamedQuery(name = "EducationZone.findByCode", query = "SELECT e FROM EducationZone e WHERE e.code = :code")
    , @NamedQuery(name = "EducationZone.findByName", query = "SELECT e FROM EducationZone e WHERE e.name = :name")})
public class EducationZone implements Serializable {



    @JoinColumn(name = "general_organization_profile_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GeneralOrganizationProfile generalOrganizationProfileId;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "code")
    private String code;
    @Size(max = 100)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "educationZoneId")
    private Collection<EducationDivision> educationDivisionCollection;
    @JoinColumn(name = "province_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Province provinceId;

    public EducationZone() {
    }

    public EducationZone(Integer id) {
        this.id = id;
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

    @XmlTransient
    public Collection<EducationDivision> getEducationDivisionCollection() {
        return educationDivisionCollection;
    }

    public void setEducationDivisionCollection(Collection<EducationDivision> educationDivisionCollection) {
        this.educationDivisionCollection = educationDivisionCollection;
    }

    public Province getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Province provinceId) {
        this.provinceId = provinceId;
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
        if (!(object instanceof EducationZone)) {
            return false;
        }
        EducationZone other = (EducationZone) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.EducationZone[ id=" + id + " ]";
    }

    public GeneralOrganizationProfile getGeneralOrganizationProfileId() {
        return generalOrganizationProfileId;
    }

    public void setGeneralOrganizationProfileId(GeneralOrganizationProfile generalOrganizationProfileId) {
        this.generalOrganizationProfileId = generalOrganizationProfileId;
    }

  
}
