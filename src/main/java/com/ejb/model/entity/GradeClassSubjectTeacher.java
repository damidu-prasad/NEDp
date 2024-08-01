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
@Table(name = "grade_class_subject_teacher")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GradeClassSubjectTeacher.findAll", query = "SELECT g FROM GradeClassSubjectTeacher g")
    , @NamedQuery(name = "GradeClassSubjectTeacher.findById", query = "SELECT g FROM GradeClassSubjectTeacher g WHERE g.id = :id")})
public class GradeClassSubjectTeacher implements Serializable {

    @Column(name = "is_active")
    private Integer isActive;

    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Teacher teacherId;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "grade_class_has_subjects_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GradeClassHasSubjects gradeClassHasSubjectsId;
    @JoinColumn(name = "grade_class_stream_manager_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GradeClassStreamManager gradeClassStreamManagerId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gradeClassSubjectTeacherId")
    private Collection<TimeTable> timeTableCollection;

    public GradeClassSubjectTeacher() {
    }

    public GradeClassSubjectTeacher(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GradeClassHasSubjects getGradeClassHasSubjectsId() {
        return gradeClassHasSubjectsId;
    }

    public void setGradeClassHasSubjectsId(GradeClassHasSubjects gradeClassHasSubjectsId) {
        this.gradeClassHasSubjectsId = gradeClassHasSubjectsId;
    }

    public GradeClassStreamManager getGradeClassStreamManagerId() {
        return gradeClassStreamManagerId;
    }

    public void setGradeClassStreamManagerId(GradeClassStreamManager gradeClassStreamManagerId) {
        this.gradeClassStreamManagerId = gradeClassStreamManagerId;
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
        if (!(object instanceof GradeClassSubjectTeacher)) {
            return false;
        }
        GradeClassSubjectTeacher other = (GradeClassSubjectTeacher) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.GradeClassSubjectTeacher[ id=" + id + " ]";
    }

    public Teacher getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Teacher teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
    
}
