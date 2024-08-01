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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Thilini Madagama
 */
@Entity
@Table(name = "days_period")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DaysPeriod.findAll", query = "SELECT d FROM DaysPeriod d")
    , @NamedQuery(name = "DaysPeriod.findById", query = "SELECT d FROM DaysPeriod d WHERE d.id = :id")})
public class DaysPeriod implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "days_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Days daysId;
    @JoinColumn(name = "periods_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Periods periodsId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "daysPeriodId")
    private Collection<TimeTable> timeTableCollection;

    public DaysPeriod() {
    }

    public DaysPeriod(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Days getDaysId() {
        return daysId;
    }

    public void setDaysId(Days daysId) {
        this.daysId = daysId;
    }

    public Periods getPeriodsId() {
        return periodsId;
    }

    public void setPeriodsId(Periods periodsId) {
        this.periodsId = periodsId;
    }

    @XmlTransient
    public Collection<TimeTable> getTimeTableCollection() {
        return timeTableCollection;
    }

    public void setTimeTableCollection(Collection<TimeTable> timeTableCollection) {
        this.timeTableCollection = timeTableCollection;
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
        if (!(object instanceof DaysPeriod)) {
            return false;
        }
        DaysPeriod other = (DaysPeriod) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.DaysPeriod[ id=" + id + " ]";
    }
    
}
