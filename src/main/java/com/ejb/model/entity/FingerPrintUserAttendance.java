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
@Table(name = "finger_print_user_attendance")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FingerPrintUserAttendance.findAll", query = "SELECT f FROM FingerPrintUserAttendance f")
    , @NamedQuery(name = "FingerPrintUserAttendance.findById", query = "SELECT f FROM FingerPrintUserAttendance f WHERE f.id = :id")
    , @NamedQuery(name = "FingerPrintUserAttendance.findByActionTime", query = "SELECT f FROM FingerPrintUserAttendance f WHERE f.actionTime = :actionTime")})
public class FingerPrintUserAttendance implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "action_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionTime;
    @JoinColumn(name = "finger_print_region_user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FingerPrintRegionUser fingerPrintRegionUserId;

    public FingerPrintUserAttendance() {
    }

    public FingerPrintUserAttendance(Integer id) {
        this.id = id;
    }

    public FingerPrintUserAttendance(Integer id, Date actionTime) {
        this.id = id;
        this.actionTime = actionTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

    public FingerPrintRegionUser getFingerPrintRegionUserId() {
        return fingerPrintRegionUserId;
    }

    public void setFingerPrintRegionUserId(FingerPrintRegionUser fingerPrintRegionUserId) {
        this.fingerPrintRegionUserId = fingerPrintRegionUserId;
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
        if (!(object instanceof FingerPrintUserAttendance)) {
            return false;
        }
        FingerPrintUserAttendance other = (FingerPrintUserAttendance) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.FingerPrintUserAttendance[ id=" + id + " ]";
    }
    
}
