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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "finger_print_region_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FingerPrintRegionUser.findAll", query = "SELECT f FROM FingerPrintRegionUser f")
    , @NamedQuery(name = "FingerPrintRegionUser.findById", query = "SELECT f FROM FingerPrintRegionUser f WHERE f.id = :id")
    , @NamedQuery(name = "FingerPrintRegionUser.findByAddedDate", query = "SELECT f FROM FingerPrintRegionUser f WHERE f.addedDate = :addedDate")
    , @NamedQuery(name = "FingerPrintRegionUser.findByIsActive", query = "SELECT f FROM FingerPrintRegionUser f WHERE f.isActive = :isActive")
    , @NamedQuery(name = "FingerPrintRegionUser.findByEnrollementId", query = "SELECT f FROM FingerPrintRegionUser f WHERE f.enrollementId = :enrollementId")})
public class FingerPrintRegionUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "added_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addedDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_active")
    private boolean isActive;
    @Basic(optional = false)
    @NotNull
    @Column(name = "enrollement_id")
    private int enrollementId;
    @JoinColumn(name = "finger_print_region_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FingerPrintRegion fingerPrintRegionId;
    @JoinColumn(name = "general_user_profile_gup_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GeneralUserProfile generalUserProfileGupId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fingerPrintRegionUserId")
    private Collection<FingerPrintUserAttendance> fingerPrintUserAttendanceCollection;

    public FingerPrintRegionUser() {
    }

    public FingerPrintRegionUser(Integer id) {
        this.id = id;
    }

    public FingerPrintRegionUser(Integer id, Date addedDate, boolean isActive, int enrollementId) {
        this.id = id;
        this.addedDate = addedDate;
        this.isActive = isActive;
        this.enrollementId = enrollementId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getEnrollementId() {
        return enrollementId;
    }

    public void setEnrollementId(int enrollementId) {
        this.enrollementId = enrollementId;
    }

    public FingerPrintRegion getFingerPrintRegionId() {
        return fingerPrintRegionId;
    }

    public void setFingerPrintRegionId(FingerPrintRegion fingerPrintRegionId) {
        this.fingerPrintRegionId = fingerPrintRegionId;
    }

    public GeneralUserProfile getGeneralUserProfileGupId() {
        return generalUserProfileGupId;
    }

    public void setGeneralUserProfileGupId(GeneralUserProfile generalUserProfileGupId) {
        this.generalUserProfileGupId = generalUserProfileGupId;
    }

    @XmlTransient
    public Collection<FingerPrintUserAttendance> getFingerPrintUserAttendanceCollection() {
        return fingerPrintUserAttendanceCollection;
    }

    public void setFingerPrintUserAttendanceCollection(Collection<FingerPrintUserAttendance> fingerPrintUserAttendanceCollection) {
        this.fingerPrintUserAttendanceCollection = fingerPrintUserAttendanceCollection;
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
        if (!(object instanceof FingerPrintRegionUser)) {
            return false;
        }
        FingerPrintRegionUser other = (FingerPrintRegionUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.FingerPrintRegionUser[ id=" + id + " ]";
    }
    
}
