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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Thilini Madagama
 */
@Entity
@Table(name = "periods")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Periods.findAll", query = "SELECT p FROM Periods p")
    , @NamedQuery(name = "Periods.findById", query = "SELECT p FROM Periods p WHERE p.id = :id")
    , @NamedQuery(name = "Periods.findByPeriodNo", query = "SELECT p FROM Periods p WHERE p.periodNo = :periodNo")
    , @NamedQuery(name = "Periods.findByStartTime", query = "SELECT p FROM Periods p WHERE p.startTime = :startTime")
    , @NamedQuery(name = "Periods.findByEndTime", query = "SELECT p FROM Periods p WHERE p.endTime = :endTime")})
public class Periods implements Serializable {

    @JoinColumn(name = "school_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private School schoolId;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "period_no")
    private String periodNo;
    @Column(name = "start_time")
    @Temporal(TemporalType.TIME)
    private Date startTime;
    @Column(name = "end_time")
    @Temporal(TemporalType.TIME)
    private Date endTime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodsId")
    private Collection<DaysPeriod> daysPeriodCollection;
    

    public Periods() {
    }

    public Periods(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPeriodNo() {
        return periodNo;
    }

    public void setPeriodNo(String periodNo) {
        this.periodNo = periodNo;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @XmlTransient
    public Collection<DaysPeriod> getDaysPeriodCollection() {
        return daysPeriodCollection;
    }

    public void setDaysPeriodCollection(Collection<DaysPeriod> daysPeriodCollection) {
        this.daysPeriodCollection = daysPeriodCollection;
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
        if (!(object instanceof Periods)) {
            return false;
        }
        Periods other = (Periods) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.Periods[ id=" + id + " ]";
    }

    public School getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(School schoolId) {
        this.schoolId = schoolId;
    }
    
}
