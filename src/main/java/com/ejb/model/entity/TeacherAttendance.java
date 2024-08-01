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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Nilupul Nethmina
 */
@Entity
@Table(name = "teacher_attendance")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TeacherAttendance.findAll", query = "SELECT t FROM TeacherAttendance t")
    , @NamedQuery(name = "TeacherAttendance.findById", query = "SELECT t FROM TeacherAttendance t WHERE t.id = :id")
    , @NamedQuery(name = "TeacherAttendance.findByIsAttended", query = "SELECT t FROM TeacherAttendance t WHERE t.isAttended = :isAttended")})
public class TeacherAttendance implements Serializable {



    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "is_attended")
    private Boolean isAttended;
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Teacher teacherId;
    @Column(name = "date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;
    @Column(name = "in_time")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date in_time;
    @Column(name = "out_time")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date out_time;

    public TeacherAttendance() {
    }

    public TeacherAttendance(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsAttended() {
        return isAttended;
    }

    public void setIsAttended(Boolean isAttended) {
        this.isAttended = isAttended;
    }

    public Teacher getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Teacher teacherId) {
        this.teacherId = teacherId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getIn_time() {
        return in_time;
    }

    public void setIn_time(Date in_time) {
        this.in_time = in_time;
    }

    public Date getOut_time() {
        return out_time;
    }

    public void setOut_time(Date out_time) {
        this.out_time = out_time;
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
        if (!(object instanceof TeacherAttendance)) {
            return false;
        }
        TeacherAttendance other = (TeacherAttendance) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.TeacherAttendance[ id=" + id + " ]";
    }

   
   

}
