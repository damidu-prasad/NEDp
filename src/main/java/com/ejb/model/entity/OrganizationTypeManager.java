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
@Table(name = "organization_type_manager")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrganizationTypeManager.findAll", query = "SELECT o FROM OrganizationTypeManager o")
    , @NamedQuery(name = "OrganizationTypeManager.findById", query = "SELECT o FROM OrganizationTypeManager o WHERE o.id = :id")})
public class OrganizationTypeManager implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "general_organization_profile_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GeneralOrganizationProfile generalOrganizationProfileId;
    @JoinColumn(name = "general_organization_profile_id1", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GeneralOrganizationProfile generalOrganizationProfileId1;
    @JoinColumn(name = "organization_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private OrganizationType organizationTypeId;

    public OrganizationTypeManager() {
    }

    public OrganizationTypeManager(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GeneralOrganizationProfile getGeneralOrganizationProfileId() {
        return generalOrganizationProfileId;
    }

    public void setGeneralOrganizationProfileId(GeneralOrganizationProfile generalOrganizationProfileId) {
        this.generalOrganizationProfileId = generalOrganizationProfileId;
    }

    public GeneralOrganizationProfile getGeneralOrganizationProfileId1() {
        return generalOrganizationProfileId1;
    }

    public void setGeneralOrganizationProfileId1(GeneralOrganizationProfile generalOrganizationProfileId1) {
        this.generalOrganizationProfileId1 = generalOrganizationProfileId1;
    }

    public OrganizationType getOrganizationTypeId() {
        return organizationTypeId;
    }

    public void setOrganizationTypeId(OrganizationType organizationTypeId) {
        this.organizationTypeId = organizationTypeId;
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
        if (!(object instanceof OrganizationTypeManager)) {
            return false;
        }
        OrganizationTypeManager other = (OrganizationTypeManager) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.OrganizationTypeManager[ id=" + id + " ]";
    }
    
}
