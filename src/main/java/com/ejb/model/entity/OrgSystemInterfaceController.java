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
@Table(name = "org_system_interface_controller")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrgSystemInterfaceController.findAll", query = "SELECT o FROM OrgSystemInterfaceController o")
    , @NamedQuery(name = "OrgSystemInterfaceController.findById", query = "SELECT o FROM OrgSystemInterfaceController o WHERE o.id = :id")
    , @NamedQuery(name = "OrgSystemInterfaceController.findByIsActive", query = "SELECT o FROM OrgSystemInterfaceController o WHERE o.isActive = :isActive")})
public class OrgSystemInterfaceController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "is_active")
    private Integer isActive;
    @JoinColumn(name = "general_organization_profile_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GeneralOrganizationProfile generalOrganizationProfileId;
    @JoinColumn(name = "projects_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Projects projectsId;
    @JoinColumn(name = "system_interface_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SystemInterface systemInterfaceId;

    public OrgSystemInterfaceController() {
    }

    public OrgSystemInterfaceController(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public GeneralOrganizationProfile getGeneralOrganizationProfileId() {
        return generalOrganizationProfileId;
    }

    public void setGeneralOrganizationProfileId(GeneralOrganizationProfile generalOrganizationProfileId) {
        this.generalOrganizationProfileId = generalOrganizationProfileId;
    }

    public Projects getProjectsId() {
        return projectsId;
    }

    public void setProjectsId(Projects projectsId) {
        this.projectsId = projectsId;
    }

    public SystemInterface getSystemInterfaceId() {
        return systemInterfaceId;
    }

    public void setSystemInterfaceId(SystemInterface systemInterfaceId) {
        this.systemInterfaceId = systemInterfaceId;
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
        if (!(object instanceof OrgSystemInterfaceController)) {
            return false;
        }
        OrgSystemInterfaceController other = (OrgSystemInterfaceController) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.OrgSystemInterfaceController[ id=" + id + " ]";
    }
    
}
