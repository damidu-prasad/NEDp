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
@Table(name = "grade_class_stream_manager")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GradeClassStreamManager.findAll", query = "SELECT g FROM GradeClassStreamManager g")
    , @NamedQuery(name = "GradeClassStreamManager.findById", query = "SELECT g FROM GradeClassStreamManager g WHERE g.id = :id")})
public class GradeClassStreamManager implements Serializable {

    @Column(name = "is_active")
    private Integer isActive;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gradeClassStreamManagerId")
    private Collection<GradeClassSubjectTeacher> gradeClassSubjectTeacherCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gradeClassStreamManagerId")
    private Collection<TimeTable> timeTableCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gradeClassStreamManagerId")
    private Collection<GradeClassStudents> gradeClassStudentsCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "grade_class_stream_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GradeClassStream gradeClassStreamId;
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    @ManyToOne
    private Teacher teacherId;
    @JoinColumn(name = "year_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Year yearId;
    @ManyToOne(optional = false)
    @JoinColumn(name = "teacher_type_id", referencedColumnName = "id")
    private TeacherType teacherType;

    public GradeClassStreamManager() {
    }

    public GradeClassStreamManager(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GradeClassStream getGradeClassStreamId() {
        return gradeClassStreamId;
    }

    public void setGradeClassStreamId(GradeClassStream gradeClassStreamId) {
        this.gradeClassStreamId = gradeClassStreamId;
    }

    public Teacher getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Teacher teacherId) {
        this.teacherId = teacherId;
    }

    public Year getYearId() {
        return yearId;
    }

    public void setYearId(Year yearId) {
        this.yearId = yearId;
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
        if (!(object instanceof GradeClassStreamManager)) {
            return false;
        }
        GradeClassStreamManager other = (GradeClassStreamManager) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public TeacherType getTeacherType() {
        return teacherType;
    }

    public void setTeacherType(TeacherType teacherType) {
        this.teacherType = teacherType;
    }

    @Override
    public String toString() {
        return "com.ejb.model.entity.GradeClassStreamManager[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<GradeClassStudents> getGradeClassStudentsCollection() {
        return gradeClassStudentsCollection;
    }

    public void setGradeClassStudentsCollection(Collection<GradeClassStudents> gradeClassStudentsCollection) {
        this.gradeClassStudentsCollection = gradeClassStudentsCollection;
    }

    @XmlTransient
    public Collection<GradeClassSubjectTeacher> getGradeClassSubjectTeacherCollection() {
        return gradeClassSubjectTeacherCollection;
    }

    public void setGradeClassSubjectTeacherCollection(Collection<GradeClassSubjectTeacher> gradeClassSubjectTeacherCollection) {
        this.gradeClassSubjectTeacherCollection = gradeClassSubjectTeacherCollection;
    }

    @XmlTransient
    public Collection<TimeTable> getTimeTableCollection() {
        return timeTableCollection;
    }

    public void setTimeTableCollection(Collection<TimeTable> timeTableCollection) {
        this.timeTableCollection = timeTableCollection;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

}
