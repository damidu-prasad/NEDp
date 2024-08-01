/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ejb.model.entity;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "teacher_monthly_scores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TeacherMonthlyScores.findAll", query = "SELECT t FROM TeacherMonthlyScores t")
    , @NamedQuery(name = "TeacherMonthlyScores.findById", query = "SELECT t FROM TeacherMonthlyScores t WHERE t.id = :id")
    , @NamedQuery(name = "TeacherMonthlyScores.findByDate", query = "SELECT t FROM TeacherMonthlyScores t WHERE t.date = :date")
    , @NamedQuery(name = "TeacherMonthlyScores.findByMonthlyAttendanceScore", query = "SELECT t FROM TeacherMonthlyScores t WHERE t.monthlyAttendanceScore = :monthlyAttendanceScore")
    , @NamedQuery(name = "TeacherMonthlyScores.findByMonthlyDedicationScore", query = "SELECT t FROM TeacherMonthlyScores t WHERE t.monthlyDedicationScore = :monthlyDedicationScore")})
public class TeacherMonthlyScores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monthly_attendance_score")
    private Double monthlyAttendanceScore;
    @Column(name = "monthly_dedication_score")
    private Double monthlyDedicationScore;
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Teacher teacherId;

    public TeacherMonthlyScores() {
    }

    public TeacherMonthlyScores(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getMonthlyAttendanceScore() {
        return monthlyAttendanceScore;
    }

    public void setMonthlyAttendanceScore(Double monthlyAttendanceScore) {
        this.monthlyAttendanceScore = monthlyAttendanceScore;
    }

    public Double getMonthlyDedicationScore() {
        return monthlyDedicationScore;
    }

    public void setMonthlyDedicationScore(Double monthlyDedicationScore) {
        this.monthlyDedicationScore = monthlyDedicationScore;
    }

    public Teacher getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Teacher teacherId) {
        this.teacherId = teacherId;
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
        if (!(object instanceof TeacherMonthlyScores)) {
            return false;
        }
        TeacherMonthlyScores other = (TeacherMonthlyScores) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.TeacherMonthlyScores[ id=" + id + " ]";
    }
    
}
