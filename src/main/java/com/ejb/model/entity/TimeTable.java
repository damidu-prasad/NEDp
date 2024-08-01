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
@Table(name = "time_table")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TimeTable.findAll", query = "SELECT t FROM TimeTable t")
    , @NamedQuery(name = "TimeTable.findById", query = "SELECT t FROM TimeTable t WHERE t.id = :id")})
public class TimeTable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @JoinColumn(name = "days_period_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DaysPeriod daysPeriodId;
    @JoinColumn(name = "grade_class_stream_manager_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GradeClassStreamManager gradeClassStreamManagerId;
    @JoinColumn(name = "grade_class_subject_teacher_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GradeClassSubjectTeacher gradeClassSubjectTeacherId;

    public TimeTable() {
    }

    public TimeTable(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DaysPeriod getDaysPeriodId() {
        return daysPeriodId;
    }

    public void setDaysPeriodId(DaysPeriod daysPeriodId) {
        this.daysPeriodId = daysPeriodId;
    }

    public GradeClassStreamManager getGradeClassStreamManagerId() {
        return gradeClassStreamManagerId;
    }

    public void setGradeClassStreamManagerId(GradeClassStreamManager gradeClassStreamManagerId) {
        this.gradeClassStreamManagerId = gradeClassStreamManagerId;
    }

    public GradeClassSubjectTeacher getGradeClassSubjectTeacherId() {
        return gradeClassSubjectTeacherId;
    }

    public void setGradeClassSubjectTeacherId(GradeClassSubjectTeacher gradeClassSubjectTeacherId) {
        this.gradeClassSubjectTeacherId = gradeClassSubjectTeacherId;
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
        if (!(object instanceof TimeTable)) {
            return false;
        }
        TimeTable other = (TimeTable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.TimeTable[ id=" + id + " ]";
    }
    
}
